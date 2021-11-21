package eco_logis.equipments.oven.mil;

import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
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
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(exported = {
        SwitchOnOven.class,
        SwitchOffOven.class
})
public class OvenUserModel
    extends AtomicES_Model
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;

    /** URI for an instance model; works as long as only one instance is created */
    public static final String URI = OvenUserModel.class.getSimpleName();

    /** Time interval between event outputs.								*/
    protected static double STEP_MEAN_DURATION = 1.0;


    // ========== Attributes ==========


    /**	The random number generator from common math library.				*/
    protected final RandomDataGenerator rg;


    // ========== Constructors ==========


    /**
     * Create a new user model for the oven
     *
     * @see AtomicES_Model#AtomicES_Model(String, TimeUnit, SimulatorI)
     *
     * @param uri The identifier of the model
     * @param simulatedTimeUnit The simulation time unit
     * @param simulationEngine The simulation engine bound to the model
     * @throws Exception TODO
     */
    public OvenUserModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.rg = new RandomDataGenerator() ;
        this.setLogger(new StandardLogger());
    }


    // ========== Class methods ===========


    /**
     * Generate the next event in the test scenario
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code eventList.peek() != null}
     * post	{@code eventList.peek() != null}
     * </pre>
     *
     */
    protected void generateNextEvent() {
        // Get the current event and the time of the next
        EventI current = this.eventList.peek();
        assert current != null;
        // Compute the time of occurrence for the next event
        Time nextTime = this.computeTimeOfNextEvent(current.getTimeOfOccurrence());

        // Compute the next event type given the current event
        ES_EventI nextEvent = null;
        if(current instanceof SwitchOnOven) {
            nextEvent = new SwitchOffOven(nextTime);
        }
        else if(current instanceof SwitchOffOven) {
            nextEvent = new SwitchOnOven(nextTime,150 + Math.random() * 100);
        }

        // Schedule the event to be executed by this model
        this.scheduleEvent(nextEvent);
    }

    protected Time computeTimeOfNextEvent(Time from) {
        // Generate randomly the next time interval but force it to be greater than 0 by returning at least 0.1
        double delay = Math.max(this.rg.nextGaussian(STEP_MEAN_DURATION, STEP_MEAN_DURATION/2.0), 0.1);

        // Compute the new time by adding the delay to from
        return from.add(new Duration(delay, this.getSimulatedTimeUnit()));
    }


    // ========== DEVS simulation protocol ==========


    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        this.rg.reSeedSecure();

        // Compute the time of occurrence for the first event
        Time t = this.computeTimeOfNextEvent(this.getCurrentStateTime());
        // Schedule the first event
        this.scheduleEvent(new SwitchOnOven(t, 180));
        // Re-initialisation of the time of occurrence of the next event required here after adding a new event in the schedule.
        this.nextTimeAdvance = this.timeAdvance();
        this.timeOfNextEvent =
                this.getCurrentStateTime().add(this.getNextTimeAdvance());

        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output() */
    @Override
    public ArrayList<EventI> output() {
        // Generate and schedule the next event
        if (this.eventList.peek() != null) {
            this.generateNextEvent();
        }
        // This will extract the next event from the event list and emit it
        return super.output();
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
