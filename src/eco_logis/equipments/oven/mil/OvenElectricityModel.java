package eco_logis.equipments.oven.mil;

import eco_logis.equipments.oven.mil.events.AbstractOvenEvent;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import fr.sorbonne_u.components.cyphy.hem2021e2.utils.Electricity;
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
        SwitchOffOven.class})
public class OvenElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** State of the oven */
    public static enum State {
        /** Oven is on and heating to its goal temperature */
        ON,
        /** Oven is off and getting to room temperature (does not consume electricity) */
        OFF
    }

    /** URI for an instance model; works as long as only one instance is created. */
    public static final String URI = OvenElectricityModel.class.getSimpleName();

    /** Energy consumption (in Watts) of the oven */
    public static double AVERAGE_CONSUMPTION = 2000.0; // Watts

    /** Nominal tension (in Volts) of the oven */
    public static double TENSION = 220.0; // Volts

    private static final long serialVersionUID = 1L;


    // ========== Attributes ==========


    /** Current temperature of the oven */
    protected int currentTemperature;

    /** Goal temperature of the oven, when oven is ON State */
    protected int goalTemperature;

    /** Current intensity in amperes; intensity is power/tension */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

    /** Current state (OFF,ON) of the oven */
    protected OvenElectricityModel.State currentState = OvenElectricityModel.State.OFF;

    /** True when the electricity consumption of the oven has changed
     *  after executing an external event; the external event changes the
     *  value of <code>currentState</code> and then an internal transition
     *  will be triggered by putting through in this variable which will
     *  update the variable <code>currentIntensity</code>. */
    protected boolean consumptionHasChanged = false;

    /** Total consumption of the oven during the simulation in kWh	*/
    protected double totalConsumption;


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


    // ========== Class methods ==========


    /**
     * Set the state of the oven
     *
     * @param s the new state
     */
    public void	setState(OvenElectricityModel.State s) {
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
        if (this.consumptionHasChanged) {
            this.consumptionHasChanged = false;
        } else {
            this.consumptionHasChanged = true;
        }
    }


    // ========== DEVS simulation protocol ==========


    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        // Initially, the oven is off, so its consumption is zero.
        this.currentIntensity.v = 0.0;
    }

    /** @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time startTime) {
        super.initialiseState(startTime);

        // Initially the oven is off and its electricity consumption is not about to change.
        this.currentState = OvenElectricityModel.State.OFF;
        this.consumptionHasChanged = false;
        this.totalConsumption = 0.0;

        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output() */
    @Override
    public ArrayList<EventI> output() {
        // The model does not export events.
        return null;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance() */
    @Override
    public Duration timeAdvance()
    {
        /*  To trigger an internal transition after an external transition, the
            variable consumptionHasChanged is set to true, hence when it is true
            return a zero delay otherwise return an infinite delay (no internal
            transition expected) */
        if (this.consumptionHasChanged) {
            // After triggering the internal transition, toggle the boolean to prepare for the next internal transition.
            this.toggleConsumptionHasChanged();
            return new Duration(0.0, this.getSimulatedTimeUnit());
        } else {
            return Duration.INFINITY;
        }
    }


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime)
    {
        super.userDefinedInternalTransition(elapsedTime);

        // Set the current electricity consumption from the current state
        switch (this.currentState)
        {
            case OFF :
                this.currentIntensity.v = 0.0;
                break;
            case ON :
                this.currentIntensity.v = AVERAGE_CONSUMPTION/TENSION;
                // TODO : adjust to the goal temperature of the oven
        }
        this.currentIntensity.time = this.getCurrentStateTime();

        // Tracing
        StringBuffer message =
                new StringBuffer("executes an internal transition ");
        message.append("with current consumption ");
        message.append(this.currentIntensity.v);
        message.append(" at ");
        message.append(this.currentIntensity.time);
        message.append(".\n");
        this.logMessage(message.toString());
    }


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime)
    {
        // Get the vector of currently received external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        /* When this method is called, there is at least one external event,
           and for the current oven model, there must be exactly one by
           construction. */
        assert	currentEvents != null && currentEvents.size() == 1;

        Event ce = (Event) currentEvents.get(0);

        // Compute the total consumption (in kWh) for the simulation report.
        this.totalConsumption += Electricity.computeConsumption(elapsedTime, TENSION*this.currentIntensity.v);

        // Tracing
        StringBuffer message =
                new StringBuffer("executes an external transition ");
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
        Duration d = endTime.subtract(this.getCurrentStateTime());
        this.totalConsumption += Electricity.computeConsumption(d,TENSION*this.currentIntensity.v);
        this.logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
