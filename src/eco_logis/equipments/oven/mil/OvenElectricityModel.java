package eco_logis.equipments.oven.mil;

import eco_logis.equipments.oven.mil.events.*;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
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
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@ModelExternalEvents(imported = {
        SwitchOnOven.class,
        SwitchOffOven.class,
        HeatOven.class,
        DoNotHeatOven.class
})
public class OvenElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** State of the oven */
    public enum State {
        /** Oven is on but not heating */
        ON,
        /** Oven is on and heating */
        HEATING,
        /** Oven is off */
        OFF
    }

    /** URI for an instance model; works as long as only one instance is created. */
    public static final String URI = OvenElectricityModel.class.getSimpleName();

    /** Energy consumption (in Watts) of the heating oven */
    public static double HEATING_CONSUMPTION = 2000.0;

    /** Energy consumption (in Watts) of the not heating oven */
    public static double NOT_HEATING_CONSUMPTION = 50.0;


    // ========== Attributes ==========


    /** Current intensity in amperes; intensity is power/tension */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0);

    /** Current state (OFF, ON, HEATING) of the oven */
    protected OvenElectricityModel.State currentState = OvenElectricityModel.State.OFF;

    /** True when the electricity consumption of the oven has changed
     *  after executing an external event; the external event changes the
     *  value of <code>currentState</code> and then an internal transition
     *  will be triggered by putting through in this variable which will
     *  update the variable <code>currentIntensity</code>. */
    protected boolean consumptionHasChanged = false;


    // ========== Constructors ==========


    /**
     * Create an oven MIL model instance
     *
     * @param uri				URI of the model.
     * @param simulatedTimeUnit	time unit used for the simulation time.
     * @param simulationEngine	simulation engine to which the model is attached.
     * @throws Exception		<i>to do</i>.
     */
    public OvenElectricityModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine)
    throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.setLogger(new StandardLogger());
    }


    // ========== Getters & setters ==========


    /**
     * Set the state of the oven
     *
     * @param s the new state
     */
    public void	setState(OvenElectricityModel.State s) {
        if(this.currentState == s) this.consumptionHasChanged = true;
        this.currentState = s;
    }

    /**
     * Return the state of the oven
     *
     * @return the state of the oven
     */
    public OvenElectricityModel.State getState() {
        return this.currentState;
    }


    // ========== Class methods ==========


    /**
     * Toggle the value of the state of the model telling whether the
     * electricity consumption level has just changed or not; when it changes
     * after receiving an external event, an immediate internal transition
     * is triggered to update the level of electricity consumption.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     */
    public void toggleConsumptionHasChanged() {
        this.consumptionHasChanged = !this.consumptionHasChanged;
    }


    // ========== DEVS simulation protocol ==========


    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);
        // Initially, the oven is off, so its consumption is zero.
        this.currentConsumption.v = 0.0;
    }

    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time startTime) {
        super.initialiseState(startTime);

        // Initially the oven is off and its electricity consumption is not about to change.
        this.currentState = OvenElectricityModel.State.OFF;
        this.consumptionHasChanged = false;

        this.toggleDebugMode();
        this.logMessage("Simulation starts...\n");
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output() */
    @Override
    public ArrayList<EventI> output() {
        // The model does not export events.
        return null;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if (this.consumptionHasChanged) {
            this.toggleConsumptionHasChanged();
            return Duration.zero(this.getSimulatedTimeUnit());
        } else {
            return Duration.INFINITY;
        }
    }


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Set the current electricity consumption from the current state
        switch (this.currentState) {
            case ON :
                this.currentConsumption.v = NOT_HEATING_CONSUMPTION;
                break;
            case HEATING :
                this.currentConsumption.v = HEATING_CONSUMPTION;
                break;
            case OFF :
                this.currentConsumption.v = 0.0;
                break;
        }
        this.currentConsumption.time = this.getCurrentStateTime();

        // Tracing
        logMessage("Current consumption " + currentConsumption.v + " at " + currentConsumption.time + "\n");
    }


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the vector of currently received external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1;

        Event ce = (Event) currentEvents.get(0);

        // Tracing
        StringBuffer message = new StringBuffer("executes an external transition ");
        message.append(ce.getClass().getSimpleName());
        message.append("(");
        message.append(ce.getTimeOfOccurrence().getSimulatedTime());
        message.append(")\n");
        this.logMessage(message.toString());

        assert ce instanceof AbstractOvenEvent;
        // Events have a method execute on to perform their effect on this model
        ce.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("Simulation ends!\n");
        super.endSimulation(endTime);
    }

}
