package eco_logis.equipments.generator.mil;

import eco_logis.equipments.generator.mil.events.AbstractGeneratorEvent;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
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
 * This class represents an electric model for the generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SwitchOnGenerator.class,
        SwitchOffGenerator.class
})
public class GeneratorElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = GeneratorElectricityModel.class.getSimpleName();

    /** The production of the generator when it's running */
    private static final double PRODUCTION = 2000.0;


    // ========== Attributes ==========


    /** If the generator is currently on */
    private boolean isRunning;

    /** If the internal state has changed */
    private boolean hasChanged;

    /** The current fuel level */
    @ImportedVariable(type = Double.class)
    protected Value<Double> currentFuelLevel;

    /** The current production of the generator in a shared var */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentProduction = new Value<>(this, 0.0);


    // ========== Constructors ==========


    /**
     * Create a new generator electricity model
     * 
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI) 
     * 
     * @param uri The model URI
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The engine
     * @throws Exception TODO
     */
    public GeneratorElectricityModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    /**
     * Get if the generator is currently running
     * 
     * @return True if the generator is running, false else
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Get if the model has to do an internal transition
     *
     * @return True if there is an internal transition, false else
     */
    public boolean hasChanged() {
        return hasChanged;
    }


    // ========== Setters ==========


    /**
     * Set if the generator is running
     * 
     * @param running If the generator is running
     */
    public void setRunning(boolean running) {
        isRunning = running;
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

        isRunning = false;

        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        currentProduction.v = 0.0;
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        // This model does not export events
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

        // Set the current production
        if(isRunning) {
            if(currentFuelLevel.v > 0.0) currentProduction.v = PRODUCTION;
        } else {
            currentProduction.v = 0.0;
        }

        // Set the value time
        currentProduction.time = getCurrentStateTime();

        // Tracing
        logMessage("Current production " + currentProduction.v + " at " + currentProduction.time +
                " | Fuel level " + currentFuelLevel.v + " l" + "\n");
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert	currentEvents != null && currentEvents.size() == 1;
        Event currentEvent = (Event) currentEvents.get(0);

        // Execute the event on the model
        assert currentEvent instanceof AbstractGeneratorEvent;
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
