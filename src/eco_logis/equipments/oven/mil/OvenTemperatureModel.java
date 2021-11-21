package eco_logis.equipments.oven.mil;

import eco_logis.equipments.oven.mil.events.AbstractOvenEvent;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SwitchOnOven.class,
        SwitchOffOven.class
})
public class OvenTemperatureModel
    extends AtomicHIOAwithDE
{

    // ========== Macros ===========


    /** State of the oven */
    public static enum State {
        /** Oven is on and heating to its goal temperature */
        ON,
        /** Oven is off and getting to room temperature */
        OFF
    }

    private static final long serialVersionUID = 1L;

    /** URI for a model; works when only one instance is created */
    public static final String URI = OvenTemperatureModel.class.getSimpleName();

    /** Integration step for the differential equation(assumed in seconds).	*/
    protected static final double STEP = 0.1;

    /** Room temperature in Celsius (°C) */
    public static final double roomTemperature = 20.0;


    // ========== Attributes ===========


    /** Integration step as a duration, including the time unit	*/
    protected final Duration integrationStep;

    /** Current temperature in the oven (°C) */
    protected double currentTemperature;

    /** Current goal temperature of the oven (°C) */
    protected double goalTemperature;

    /** Current state of the oven */
    protected OvenTemperatureModel.State currentState = OvenTemperatureModel.State.OFF;

    /** Time that has passed since the first temperature */
    protected Time temperatureTime;


    // ========== Constructors ==========

    /**
     * Create a <code>OvenTemperatureModel</code> instance
     *
     * @param uri				URI of the model.
     * @param simulatedTimeUnit	time unit used for the simulation time.
     * @param simulationEngine	simulation engine to which the model is attached.
     * @throws Exception		<i>to do</i>.
     */
    public OvenTemperatureModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception
    {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.integrationStep = new Duration(STEP, simulatedTimeUnit);
        this.setLogger(new StandardLogger());
    }


    // ========== Class methods ===========


    /**
     * Set the goal temperature of the oven
     *
     * @param goalTemperature the goal temperature
     */
    public void setGoalTemperature(double goalTemperature) {
        this.goalTemperature = goalTemperature;
    }


    /**
     * Set the state of the model
     *
     * @param s	the new state
     */
    public void setState(OvenTemperatureModel.State s) {
        this.currentState = s;
    }

    /**
     * Get the current state of the model
     *
     * @return the current state
     */
    public OvenTemperatureModel.State getState() {
        return this.currentState;
    }


    // ========== DEVS simulation protocol ==========


    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        this.currentState = OvenTemperatureModel.State.OFF;

//        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        this.temperatureTime = startTime;
        this.currentTemperature = roomTemperature;
        this.goalTemperature = roomTemperature;
    }


    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseDerivatives() */
    @Override
    protected void initialiseDerivatives()
    {
        this.computeDerivatives();
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
        return this.integrationStep;
    }

    @Override
    protected void computeDerivatives() {
        // TODO
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        this.temperatureTime = this.temperatureTime.add(elapsedTime);

        // Adjust temperature (trivial method)
        if(this.currentTemperature < this.goalTemperature)
            this.currentTemperature *= 1.2;
        else if (this.currentTemperature > this.goalTemperature)
            this.currentTemperature *= 0.70;

        // Tracing
        String mark = this.currentState == OvenTemperatureModel.State.ON ? " (h)" : " (-)";
        StringBuffer message = new StringBuffer();
        message.append(this.temperatureTime);
        message.append(mark);
        message.append(" : ");
        message.append(this.currentTemperature);
        message.append('\n');
        this.logMessage(message.toString());

        super.userDefinedInternalTransition(elapsedTime);
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the vector of current external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();

        /* When this method is called, there is at least one external event,
           and for the oven model, there will be exactly one by construction. */
        assert currentEvents != null && currentEvents.size() == 1; // TODO

        Event ce = (Event) currentEvents.get(0);
        assert ce instanceof AbstractOvenEvent;

        StringBuffer sb = new StringBuffer("executing the external event: ");
        sb.append(ce.eventAsString());
        sb.append(".\n");
        this.logMessage(sb.toString());

        /* The next call will update the current state of the heater and if
           this state has changed, it will toggle the boolean
           consumptionHasChanged, which in turn will trigger an immediate
           internal transition to update the current intensity of the
           heater electricity consumption. */
        ce.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
