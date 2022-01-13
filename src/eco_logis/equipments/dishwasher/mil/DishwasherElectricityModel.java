package eco_logis.equipments.dishwasher.mil;

import eco_logis.equipments.crypto_miner.mil.events.*;
import eco_logis.equipments.dishwasher.mil.events.*;
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
 * The electricity model for the dishwasher
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SetEcoProgram.class,
        SetFastProgram.class,
        SetFullProgram.class,
        SetRinseProgram.class,
        SwitchOffDishwasher.class
})
public class DishwasherElectricityModel
    extends AtomicHIOA
{

    // ========== Inner class and types ==========


    public enum State {
        /** The dishwasher is off */
        OFF,

        /** The dishwasher is currently running the full program */
        FULL,

        /** The dishwasher is currently running the eco program */
        ECO,

        /** The dishwasher is currently running the fast program */
        FAST,

        /** The dishwasher is currently running the rinse program */
        RINSE
    }


    // ========== Macros ==========


    /** The URI of the dishwasher electricity model */
    public static final String URI = DishwasherElectricityModel.class.getSimpleName();

    /** Consumption of the full program */
    private final static double FULL_PROGRAM_CONSUMPTION = 1000.0;

    /** Consumption of the eco program */
    private final static double ECO_PROGRAM_CONSUMPTION = 718.0;

    /** Consumption of the fast program */
    private final static double FAST_PROGRAM_CONSUMPTION = 1720.0;

    /** Consumption of the rinse program */
    private final static double RINSE_PROGRAM_CONSUMPTION = 652.0;


    // ========== Attributes ==========


    /** Current state of the dishwasher */
    private State currentState;

    /** If the state has changed and you have to perform an internal transition */
    private boolean hasChanged;

    /** The current consumption of the dishwasher in a shared var */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0);


    // ========== Attributes ==========


    /**
     * Create a new dishwasher electricity model
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The electricity model URI
     * @param timeUnit The simulation time unit
     * @param engine The simulation engine
     *
     * @throws Exception TODO
     */
    public DishwasherElectricityModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    public State getCurrentState() {
        return currentState;
    }

    public boolean hasChanged() {
        return hasChanged;
    }


    // ========== Setters ==========


    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        currentState = State.OFF;
        hasChanged = false;

        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        currentConsumption.v = 0.0d;
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        // The model does not export events
        return null;
    }

    /** @see AtomicHIOA#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if(this.hasChanged) {
            this.hasChanged = false;
            return new Duration(0.0, this.getSimulatedTimeUnit());
        }
        return Duration.INFINITY;
    }

    /** @see AtomicHIOA#userDefinedInternalTransition(Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Set the current consumption
        switch (currentState) {
            case OFF:
                currentConsumption.v = 0.0;
                break;

            case FULL:
                currentConsumption.v = FULL_PROGRAM_CONSUMPTION;
                break;

            case ECO:
                currentConsumption.v = ECO_PROGRAM_CONSUMPTION;
                break;

            case FAST:
                currentConsumption.v = FAST_PROGRAM_CONSUMPTION;
                break;

            case RINSE:
                currentConsumption.v = RINSE_PROGRAM_CONSUMPTION;
                break;
        }

        // Set the value time
        currentConsumption.time = getCurrentStateTime();

        // Tracing
        StringBuilder builder = new StringBuilder("Execute internal transition | ");
        builder.append("Current consumption ").append(currentConsumption.v).append(" at ").append(currentConsumption.time);
        builder.append('\n');
        logMessage(builder.toString());
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert	currentEvents != null && currentEvents.size() == 1;
        Event currentEvent = (Event) currentEvents.get(0);

        // Execute the event on the model
        assert currentEvent instanceof AbstractDishwasherEvent;
        currentEvent.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see AtomicHIOA#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        logMessage("Simulations ends!\n");
        super.endSimulation(endTime);
    }

}
