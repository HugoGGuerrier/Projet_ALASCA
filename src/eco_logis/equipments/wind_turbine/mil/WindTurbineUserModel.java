package eco_logis.equipments.wind_turbine.mil;

import eco_logis.equipments.wind_turbine.mil.events.SwitchOffWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.SwitchOnWindTurbine;
import fr.sorbonne_u.devs_simulation.es.events.ES_EventI;
import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the user model for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(exported = {
        SwitchOnWindTurbine.class,
        SwitchOffWindTurbine.class})
public class WindTurbineUserModel
    extends AtomicES_Model
{

    // ========== Macros ==========


    /** Unique URI of the wind turbine user model */
    public static final String URI = WindTurbineUserModel.class.getSimpleName();

    /** Mean time between two outputs */
    protected static double STEP_MEAN_DURATION = 1.0;

    /** The random data generator from the common math lib */
    protected RandomDataGenerator generator;


    // ========== Constructors ==========


    /**
     * Create a new user model for the wind turbine
     *
     * @see AtomicES_Model#AtomicES_Model(String, TimeUnit, SimulatorI)
     *
     * @param uri The identifier of the model
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The simulation engine bound to the model
     * @throws Exception TODO
     */
    public WindTurbineUserModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        setLogger(new StandardLogger());
    }



    // ========== Class methods ==========


    /**
     * Generate the next event of the user model
     */
    protected void generateNextEvent() {
        // Get the current event and the time of the next
        EventI current = eventList.peek();
        assert current != null;
        Time nextTime = computeTimeOfNextEvent(current.getTimeOfOccurrence());

        // Create the next event
        ES_EventI next = null;
        if(current instanceof SwitchOnWindTurbine) {
            next = new SwitchOffWindTurbine(nextTime);
        }
        else if(current instanceof SwitchOffWindTurbine) {
            next = new SwitchOnWindTurbine(nextTime);
        }
        scheduleEvent(next);
    }

    /**
     * Compute the time of the next event by a random gaussian generation
     *
     * @param from The time of the previous event
     * @return The time of the next event
     */
    protected Time computeTimeOfNextEvent(Time from) {
        double delay = Math.max(generator.nextGaussian(STEP_MEAN_DURATION, STEP_MEAN_DURATION/2.0), 0.1);
        return from.add(new Duration(delay, this.getSimulatedTimeUnit()));
    }


    // ========== Override methods ==========


    /** @see AtomicES_Model#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        generator.reSeedSecure();

        // Create the first event
        Time nextTime = computeTimeOfNextEvent(getCurrentStateTime());
        scheduleEvent(new SwitchOnWindTurbine(nextTime));

        // Reinitialize the time
        nextTimeAdvance = timeAdvance();
        timeOfNextEvent = getCurrentStateTime().add(getNextTimeAdvance());

        // Debug
        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicES_Model#output() */
    @Override
    public ArrayList<EventI> output() {
        if(eventList.peek() != null) generateNextEvent();

        return super.output();
    }

    /** @see AtomicES_Model#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        logMessage("Simulation ends!\n");
        super.endSimulation(endTime);
    }


}
