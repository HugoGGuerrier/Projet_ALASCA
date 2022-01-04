package eco_logis.equipments.power_bank.mil;

import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.power_bank.mil.events.AbstractPowerBankEvent;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
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
 * This class represents the charge model for the power bank
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        ChargePowerBank.class,
        DischargePowerBank.class,
        StandbyPowerBank.class
},
exported = {
        StandbyPowerBank.class
})
public class PowerBankChargeModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = PowerBankChargeModel.class.getSimpleName();

    /** The equation step */
    protected static final double STEP = 0.1;

    /** The power discharging speed (in prop/h) */
    protected static final double DISCHARGE_SPEED = 0.254;

    /** The power charging speed (in prop/h) */
    protected static final double CHARGE_SPEED = 0.203;


    // ========== Attributes ==========


    /** The evaluation step in a duration */
    protected final Duration evaluationStep;

    /** The current state of the power bank */
    private PowerBank.State currentState;

    /** the current charge level */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentChargeLevel = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========


    /**
     * Create a new power bank charge model
     *
     * @param uri The model unique URI
     * @param simulatedTimeUnit The simulated time unit
     * @param simulationEngine The simulation engine
     * @throws Exception TODO
     */
    public PowerBankChargeModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        evaluationStep = new Duration(STEP, getSimulatedTimeUnit());
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    /**
     * Get the power bank current state
     *
     * @return The current state
     */
    public PowerBank.State getCurrentState() {
        return currentState;
    }


    // ========== Setters ==========


    /**
     * Set the current state
     *
     * @param currentState The new current state of the power bank
     */
    public void setCurrentState(PowerBank.State currentState) {
        this.currentState = currentState;
    }


    // ========== Class methods ==========


    /**
     * Increase the charge level according to the duration
     *
     * @param d The duration of the charge
     */
    private void charge(Duration d) {
        double duration = d.getSimulatedDuration();
        currentChargeLevel.v += Math.min((duration / 3600) * CHARGE_SPEED, 1.0);
    }

    /**
     * Decrease the charge level according to the duration
     *
     * @param d The duration of the discharge
     */
    private void discharge(Duration d) {
        double duration = d.getSimulatedDuration();
        currentChargeLevel.v -= Math.max((duration / 3600) * DISCHARGE_SPEED, 0.0);
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        currentChargeLevel.v = 1.0;
    }

    /** @see AtomicHIOA#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        currentState = PowerBank.State.STANDBY;

        toggleDebugMode();
        logMessage("Simulations starts...\n");
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        if(currentChargeLevel.v <= 0.0 || currentChargeLevel.v >= 1.0) {
            ArrayList<EventI> res = new ArrayList<>();
            res.add(new StandbyPowerBank(currentChargeLevel.time));
            return res;
        }
        return null;
    }

    /** @see AtomicHIOA#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        return evaluationStep;
    }

    /** @see AtomicHIOA#userDefinedInternalTransition(Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Update the charge level
        if(currentChargeLevel.v > 0.0 && currentState == PowerBank.State.DISCHARGING) {
            discharge(elapsedTime);
        } else if (currentChargeLevel.v < 1.0 && currentState == PowerBank.State.CHARGING) {
            charge(elapsedTime);
        }
        currentChargeLevel.time = getCurrentStateTime();

        // Tracing
        String stateString = currentState == PowerBank.State.STANDBY ? "standby" :
                (currentState == PowerBank.State.CHARGING ? "charging" : "discharging");
        logMessage("Power bank is " + stateString + " | Charge level : " + currentChargeLevel.v + " at " + currentChargeLevel.time + "\n");
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert	currentEvents != null && currentEvents.size() == 1;
        Event currentEvent = (Event) currentEvents.get(0);

        // Execute the event on the model
        assert currentEvent instanceof AbstractPowerBankEvent;
        currentEvent.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see AtomicHIOA#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        logMessage("Simulation ends!\n");
        super.endSimulation(endTime);
    }

}
