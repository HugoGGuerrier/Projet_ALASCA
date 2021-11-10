package eco_logis.equipments.crypto_miner.mil;

import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the coupled model for the crypto miner models
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerCoupledModel
    extends CoupledModel
{

    // ========== Macros ==========


    /** The URI of the crypto miner coupled model */
    public static final String URI = CryptoMinerCoupledModel.class.getSimpleName();


    // ========== Constructors ==========


    /** @see CoupledModel#CoupledModel(String, TimeUnit, SimulatorI, ModelDescriptionI[], Map, Map, Map) */
    public CryptoMinerCoupledModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine,
            ModelDescriptionI[] submodels,
            Map<Class<? extends EventI>,
            EventSink[]> imported,
            Map<Class<? extends EventI>,
            ReexportedEvent> reexported,
            Map<EventSource, EventSink[]> connections
        ) throws Exception
    {
        super(uri, simulatedTimeUnit, simulationEngine, submodels, imported, reexported, connections);
    }

}
