package eco_logis.equipments.power_bank.mil;

import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.power_bank.mil.events.AbstractPowerBankEvent;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
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
 * This class represents the electricity model for the power bank with production and consumption
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        ChargePowerBank.class,
        DischargePowerBank.class,
        StandbyPowerBank.class
})
public class PowerBankElectricityModel
    extends AtomicHIOA
{


    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = PowerBankElectricityModel.class.getSimpleName();

    /** The production of the power bank */
    private static final double PRODUCTION = 1300.0;

    /** The consumption of the power bank */
    private static final double CONSUMPTION = 650.0;


    // ========== Attributes ==========

    /** The current power bank state */
    private PowerBank.State currentState;

    /** If the state has changed and you have to perform an internal transition */
    private boolean hasChanged;

    /** The current charge level between 0 and 1 */
    @ImportedVariable(type = Double.class)
    protected Value<Double> currentChargeLevel;

    /** The current power bank production */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

    /** The current power bank consumption */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========


    /**
     * Create a new power bank electricity model with the wanter parameters
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The model URI
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The engine
     * @throws Exception TODO
     */
    public PowerBankElectricityModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    /**
     * Get the current power bank state
     *
     * @return The current power bank state
     */
    public PowerBank.State getCurrentState() {
        return currentState;
    }


    // ========== Setters ==========


    /**
     * Set the power bank current state
     *
     * @param currentState The new state of the power bank
     */
    public void setCurrentState(PowerBank.State currentState) {
        this.currentState = currentState;
    }

    /**
     * Set if the model has changed
     *
     * @param hasChanged If the model has changed
     */
    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        currentState = PowerBank.State.STANDBY;

        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        currentConsumption.v = 0.0;
        currentProduction.v = 0.0;
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        // This model doesn't export events
        return null;
    }

    /** @see AtomicHIOA#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if(hasChanged) {
            hasChanged = false;
            return new Duration(0.0, getSimulatedTimeUnit());
        }
        return Duration.INFINITY;
    }

    /** @see AtomicHIOA#userDefinedInternalTransition(Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Set the current production and consumption
        switch (currentState) {
            case CHARGING:
                currentProduction.v = 0.0;
                currentConsumption.v = CONSUMPTION;
                break;

            case DISCHARGING:
                currentProduction.v = PRODUCTION;
                currentConsumption.v = 0.0;
                break;

            case STANDBY:
                currentProduction.v = 0.0;
                currentConsumption.v = 0.0;
                break;
        }

        // Set the value time
        currentProduction.time = getCurrentStateTime();
        currentConsumption.time = getCurrentStateTime();

        // Tracing
        logMessage("Current production " + currentProduction.v + " current consumption" + currentConsumption.v
                + " at " + currentProduction.time
                + " | Charge level " + currentChargeLevel.v * 100 + "%" + "\n");
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {

        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1;
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
