package eco_logis.equipments.crypto_miner;

import eco_logis.equipments.crypto_miner.mil.CryptoMinerCoupledModel;
import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.crypto_miner.sil.CryptoMinerElectricitySILModel;
import eco_logis.equipments.crypto_miner.sil.CryptoMinerStateModel;
import eco_logis.equipments.crypto_miner.sil.CryptoMinerUserSILModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the plugin for the SIL simulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestCryptoMiner";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "CMCRN";


    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the Crypto Miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true
     * </pre>
     *
     * @param simArchURI URI of the simulation architecture to be created.
     * @param accFactor	Acceleration factor used in the real time simulation.
     * @throws Exception TODO
     */
    public void	initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {
        // Create the model descriptors
        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

        // Create and set the sub-models set
        Set<String> submodels = new HashSet<>();
        submodels.add(CryptoMinerUserSILModel.URI);
        submodels.add(CryptoMinerStateModel.URI);

        // Declare the connections and reexported maps for the events
        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;

        // Put the model in the descriptors
        atomicModelDescriptors.put(
                CryptoMinerUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        CryptoMinerUserSILModel.class,
                        CryptoMinerUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                CryptoMinerStateModel.URI,
                RTAtomicModelDescriptor.create(
                        CryptoMinerStateModel.class,
                        CryptoMinerStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        if(simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            // Add the electricity model to the arch
            submodels.add(CryptoMinerElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    CryptoMinerElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            CryptoMinerElectricitySILModel.class,
                            CryptoMinerElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            // Create the event connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(CryptoMinerStateModel.URI, SwitchOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricitySILModel.URI, SwitchOnCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerStateModel.URI, SwitchOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricitySILModel.URI, SwitchOffCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerStateModel.URI, MineOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricitySILModel.URI, MineOnCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerStateModel.URI, MineOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricitySILModel.URI, MineOffCryptoMiner.class)
                    }
            );

        } else {

            // When not in unit test mode just create reexported events
            reexported = new HashMap<>();

            reexported.put(
                    SwitchOnCryptoMiner.class,
                    new ReexportedEvent(CryptoMinerStateModel.URI, SwitchOnCryptoMiner.class)
            );

            reexported.put(
                    SwitchOffCryptoMiner.class,
                    new ReexportedEvent(CryptoMinerStateModel.URI, SwitchOffCryptoMiner.class)
            );

            reexported.put(
                    MineOnCryptoMiner.class,
                    new ReexportedEvent(CryptoMinerStateModel.URI, MineOnCryptoMiner.class)
            );

            reexported.put(
                    MineOffCryptoMiner.class,
                    new ReexportedEvent(CryptoMinerStateModel.URI, MineOffCryptoMiner.class)
            );

        }

        // Create the coupled model descriptor
        coupledModelDescriptors.put(
                CryptoMinerCoupledModel.URI,
                new RTCoupledModelDescriptor(
                        CryptoMinerCoupledModel.class,
                        CryptoMinerCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        accFactor
                )
        );

        // Set the plugin arch
        setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        CryptoMinerCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor
                )
        );
    }


    // ========== Override methods ==========


    /** @see RTAtomicSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Put the owner component in the params
        simParams.put(OWNER_REFERENCE_NAME, getOwner());

        // Call super
        super.setSimulationRunParameters(simParams);

        // Remove the owner from the params
        simParams.remove(OWNER_REFERENCE_NAME);
    }

}
