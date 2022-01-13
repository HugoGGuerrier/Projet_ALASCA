package eco_logis.equipments.wind_turbine.mil;

import eco_logis.equipments.wind_turbine.mil.events.WindSpeedChange;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Represents the outside wind blowing and making turn the wind turbine.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ExternalWindModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** URI for a model; works when only one instance is created */
    public static final String URI = ExternalWindModel.class.getSimpleName();

    /** Minimal external wind speed (km/h) */
    public static final double MIN_EXTERNAL_WIND_SPEED = 0.0;

    /** Maximal external wind speed (km/h) */
    public static final double MAX_EXTERNAL_WIND_SPEED = 150.0;

    /** Period of the wind variation cycle (day); the cycle begins
     *  at the minimal speed and ends at the same speed */
    public static final double PERIOD = 0.1;


    // ========== Attributes ===========


    /** Evaluation step for the equation (assumed in seconds) */
    protected static final double STEP = .01;

    /** Evaluation step as a duration, including the time unit */
    protected final Duration evaluationStep;

    /** Current external wind speed in km/h */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> externalWindSpeed = new Value<>(this, 0.0);

    protected double cycleTime;

    /** Util for triggering internal event of wind speed changing */
    private boolean windSpeedHasChanged = false;


    // ========== Constructors ==========

    /**
     * Create an external wind MIL model instance.
     *
     * @param uri				URI of the model.
     * @param simulatedTimeUnit	time unit used for the simulation time.
     * @param simulationEngine	simulation engine to which the model is attached.
     * @throws Exception		<i>to do</i>.
     */
    public ExternalWindModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.evaluationStep = new Duration(STEP, this.getSimulatedTimeUnit());
        this.setLogger(new StandardLogger());
    }


    // ========== DEVS simulation protocol methods ==========


    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        this.cycleTime = 0.0;
    }

    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        this.externalWindSpeed.v = MIN_EXTERNAL_WIND_SPEED;

        this.toggleDebugMode();
        this.logMessage("Simulation starts.\n");
        StringBuffer message = new StringBuffer("current external wind speed: ");
        message.append(this.externalWindSpeed.v);
        message.append(" at ");
        message.append(this.getCurrentStateTime());
        message.append("\n");
        this.logMessage(message.toString());
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output() */
    @Override
    public ArrayList<EventI> output() {
        ArrayList<EventI> events = null;
        if(windSpeedHasChanged) {
            events = new ArrayList<>();
            events.add(new WindSpeedChange(this.externalWindSpeed.time));
            windSpeedHasChanged = false;
        }
        return events;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        // The model makes an internal transition every evaluation step duration
        return this.evaluationStep;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Compute the current time in the cycle
        this.cycleTime += elapsedTime.getSimulatedDuration();
        if (this.cycleTime > PERIOD) {
            this.cycleTime -= PERIOD;
        }

        // Compute the new wind speed
        double c = Math.cos((1.0 + this.cycleTime/(PERIOD/2.0))*Math.PI);

        // TODO Adjust correctly
        // simplifier equation avec sinus
        // il faut avoir : le pas d'intégration (STEP) < PERIOD !!
        // Créer loi normale N[0 (moyenne),5 (écart)]
        // rg = random number generator
        // r = rg.nextGaussian(0.0, 5.0)
        // facteur d'amplitude*sin(period) + r

        this.externalWindSpeed.v =
                MIN_EXTERNAL_WIND_SPEED
                + (MAX_EXTERNAL_WIND_SPEED - MIN_EXTERNAL_WIND_SPEED)
                * ((1.0 + c) / 2.0);

        this.externalWindSpeed.time = this.getCurrentStateTime();
        this.windSpeedHasChanged = true;

        // Tracing
        StringBuffer message = new StringBuffer("current external wind speed: ");
        message.append(this.externalWindSpeed.v);
        message.append(" at ");
        message.append(this.getCurrentStateTime());
        message.append("\n");
        this.logMessage(message.toString());
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("Simulation ends!\n");
        super.endSimulation(endTime);
    }
}
