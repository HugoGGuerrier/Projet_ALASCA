package eco_logis.equipments.generator.mil;

import eco_logis.equipments.generator.mil.events.AbstractGeneratorEvent;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
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
 * This class represents the model for the generator fuel
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SwitchOnGenerator.class,
        SwitchOffGenerator.class
},
exported = {
        SwitchOffGenerator.class
})
public class GeneratorFuelModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The fuel model URI */
    public static final String URI = GeneratorFuelModel.class.getSimpleName();

    /** The equation step */
    protected static final double STEP = 0.1;

    /** The generator fuel consumption in l/h */
    protected static final double FUEL_CONSUMPTION = 2.18;

    /** The generator tank capacity in l */
    protected static final double TANK_CAPACITY = 4.5;


    // ========== Attributes ==========


    /** The evaluation step in a duration */
    protected final Duration evaluationStep;

    /** If the generator is currently running */
    private boolean isRunning;

    /** The variable representing the current fuel level of the generator */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentFuelLevel = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========


    /**
     * Create a new generator fuel model level
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The model URI
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The simulation engine
     * @throws Exception TODO
     */
    public GeneratorFuelModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        evaluationStep = new Duration(STEP, getSimulatedTimeUnit());
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    /**
     * Get if the generator is currently running
     *
     * @return True if the generator is currently running, false else
     */
    public boolean isRunning() {
        return isRunning;
    }


    // ========== Setters ==========


    /**
     * Set if the generator is currently running
     *
     * @param running If the generator is currently running
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }


    // ========== Class methods ==========


    /**
     * Consume the fuel during the given time
     *
     * @param d The given time
     */
    private void consume(Duration d) {
        double duration = d.getSimulatedDuration();
        currentFuelLevel.v -= Math.max((duration / 3600) * FUEL_CONSUMPTION, 0.0);
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);
        
        currentFuelLevel.v = TANK_CAPACITY;
    }

    /** @see AtomicHIOA#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        
        isRunning = false;
        
        toggleDebugMode();
        logMessage("Simulations starts...\n");
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        if(currentFuelLevel.v <= 0.0) {
            ArrayList<EventI> res = new ArrayList<>();
            res.add(new SwitchOffGenerator(currentFuelLevel.time));
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

        // Update the fuel level
        if(currentFuelLevel.v > 0.0 && isRunning) {
            consume(elapsedTime);
        }
        currentFuelLevel.time = getCurrentStateTime();

        // Tracing
        logMessage("Generator is " + (isRunning ? "on" : "off") + " | Fuel level : " + currentFuelLevel.v + " at " + currentFuelLevel.time + "\n");
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
