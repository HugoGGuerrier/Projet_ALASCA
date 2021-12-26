package eco_logis.equipments.power_bank.mil;

import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
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
 * This class represents the user actions at the power bank simulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(exported ={
        ChargePowerBank.class,
        DischargePowerBank.class,
        StandbyPowerBank.class
})
public class PowerBankUserModel
    extends AtomicES_Model
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = PowerBankUserModel.class.getSimpleName();

    /** Mean time between two outputs */
    protected static double STEP_MEAN_DURATION = 20.0;

    /** The random data generator from the common math lib */
    protected RandomDataGenerator generator;


    // ========== Constructors ==========


    /**
     * Create a new power bank user model
     *
     * @param uri The identifier of the model
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The simulation engine bound to the model
     * @throws Exception TODO
     */
    public PowerBankUserModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        generator = new RandomDataGenerator();
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
        if(current instanceof ChargePowerBank) {
            next = new StandbyPowerBank(nextTime);
        }
        else if(current instanceof DischargePowerBank) {
            next = new ChargePowerBank(nextTime);
        }
        else if(current instanceof StandbyPowerBank) {
            next = new DischargePowerBank(nextTime);
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
        scheduleEvent(new DischargePowerBank(nextTime));

        // Re initialize the time
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
