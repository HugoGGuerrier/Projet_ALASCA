package eco_logis.equipments.oven.sil;

import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the user action for the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenUserSILModel
    extends AtomicModel
{

    // ========== Macros ==========


    /** URI for an instance model; works as long as only one instance is created */
    public static final String URI = OvenUserSILModel.class.getSimpleName();

    /** Run parameter name for {@code STEP_MEAN_DURATION} */
    public static final String STEP_MEAN_DURATION_RUNPNAME = URI + ":STEP_MEAN_DURATION";

    /** Time interval between event outputs */
    protected static double STEP_MEAN_DURATION = 3.0;

    /** Last step in the test scenario */
    protected static final int LAST_STEP = 2;


    // ========== Attributes ==========


    /**	The random number generator from common math library */
    protected final RandomDataGenerator rg ;

    /** Current step in the test scenario */
    protected int currentStep;

    /** Delay before performing the next step in the test scenario */
    protected Duration time2nextStep;

    /** Owner component */
    protected Oven owner;


    // ========== Constructors ==========


    /** @see AtomicModel#AtomicModel(String, TimeUnit, SimulatorI) */
    public OvenUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.rg = new RandomDataGenerator();
    }


    // ========== Class methods ==========


    /** Generate the duration to the next event */
    protected void generateNextEvent() {
        if (this.currentStep <= LAST_STEP) {
            double delay = Math.max(
                    this.rg.nextGaussian(STEP_MEAN_DURATION,STEP_MEAN_DURATION/2.0),
                    0.1);
            this.time2nextStep = new Duration(delay, this.getSimulatedTimeUnit());
        } else {
            this.time2nextStep = Duration.INFINITY;
        }
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);

        if (simParams.containsKey(STEP_MEAN_DURATION_RUNPNAME)) {
            STEP_MEAN_DURATION = (double) simParams.get(STEP_MEAN_DURATION_RUNPNAME);
        }

        // Retrieve the reference to the owner component that must be passed as a simulation run parameter
        assert simParams.containsKey(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.owner = (Oven) simParams.get(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.setLogger(new StandardComponentLogger(this.owner));
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time initialTime) {
        this.rg.reSeedSecure();

        // Compute the delay to the first event
        this.generateNextEvent();
        this.currentStep = 1;
        // Re-initialisation of the time of occurrence of the next event
        super.initialiseState(initialTime);

        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output() */
    @Override
    public ArrayList<EventI> output()
    {
        return null;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance() */
    @Override
    public Duration timeAdvance()
    {
        return this.time2nextStep;
    }


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        StringBuffer message = new StringBuffer(this.uri);

        // Switch the case of the transition
        switch (this.currentStep) {

            case 1:
                this.owner.runTask(o -> {
                    try {
                        ((Oven) o).powerOn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                message.append(" executes the operation powerOn\n");
                break;

            case 2:
                this.owner.runTask(o -> {
                    try {
                        ((Oven) o).powerOff();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                message.append(" executes the operation powerOff\n");
                break;

            default:
                message.append(" ended.\n");
        }

        this.currentStep++;
        this.generateNextEvent();
        this.logMessage(message.toString());
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
