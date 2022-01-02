package eco_logis.equipments.crypto_miner.sil;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.crypto_miner.CryptoMinerRTAtomicSimulatorPlugin;
import eco_logis.equipments.crypto_miner.mil.events.*;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the state model of the crypto miner. It broadcast event from the component methods to the
 * models.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(
        imported = {
                MineOnCryptoMiner.class,
                MineOffCryptoMiner.class,
                SwitchOnCryptoMiner.class,
                SwitchOffCryptoMiner.class
        },
        exported = {
                MineOnCryptoMiner.class,
                MineOffCryptoMiner.class,
                SwitchOnCryptoMiner.class,
                SwitchOffCryptoMiner.class
        }
)
public class CryptoMinerStateModel
    extends AtomicModel
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = CryptoMinerStateModel.class.getSimpleName();


    // ========== Attributes ==========


    /** If the miner is currently on */
    protected boolean isOn;

    /** If the miner is currently mining currency */
    protected boolean isMining;

    /** The last received event */
    protected AbstractCryptoMinerEvent lastEvent;

    /** The model owner */
    protected CryptoMiner owner;


    // ========== Constructors ==========


    /** @see AtomicModel#AtomicModel(String, TimeUnit, SimulatorI) */
    public CryptoMinerStateModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========


    /** @see AtomicModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super
        super.setSimulationRunParameters(simParams);

        // Get the model owner in the params
        assert simParams.containsKey(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (CryptoMiner) simParams.get(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger in the component logger
        setLogger(new StandardComponentLogger(owner));
    }

    /** @see AtomicModel#initialiseState(Time) */
    @Override
    public void	initialiseState(Time initialTime) {
        // Call the super
        super.initialiseState(initialTime);

        // Set the initial miner state
        lastEvent = null;
        isOn = false;
        isMining = false;

        // Tracing
        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see AtomicModel#output() */
    @Override
    public ArrayList<EventI> output() {
        assert lastEvent != null;
        ArrayList<EventI> res = new ArrayList<>();
        res.add(lastEvent);
        lastEvent = null;
        return res;
    }

    /** @see AtomicModel#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if(lastEvent != null) return Duration.zero(getSimulatedTimeUnit());
        return Duration.INFINITY;
    }

    /** @see AtomicModel#userDefinedExternalTransition(Duration) */
    @Override
    public void	userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current events to perform transitions
        ArrayList<EventI> events = getStoredEventAndReset();
        assert events != null && events.size() == 1;

        // Get the last received event
        lastEvent = (AbstractCryptoMinerEvent) events.get(0);

        // Trace
        logMessage(URI + " executes the external event " +
                lastEvent.getClass().getSimpleName() + " at " +
                lastEvent.getTimeOfOccurrence().getSimulatedTime() + "\n");
    }

    /** @see AtomicModel#endSimulation(Time) */
    @Override
    public void	endSimulation(Time endTime) throws Exception {
        logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
