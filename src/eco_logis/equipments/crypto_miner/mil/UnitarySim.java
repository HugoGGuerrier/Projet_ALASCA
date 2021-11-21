package eco_logis.equipments.crypto_miner.mil;

import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to run a unary simulation on the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class UnitarySim {
    public static void main(String[] args) {
        try {

            // The map of the atomic model description
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the crypto miner electricity model
            atomicModelDescriptors.put(
                    CryptoMinerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            CryptoMinerElectricityModel.class,
                            CryptoMinerElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the crypto miner user model
            atomicModelDescriptors.put(
                    CryptoMinerUserModel.URI,
                    AtomicModelDescriptor.create(
                            CryptoMinerUserModel.class,
                            CryptoMinerUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );


            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled crypto miner model
            Set<String> subModel = new HashSet<>();
            subModel.add(CryptoMinerElectricityModel.URI);
            subModel.add(CryptoMinerUserModel.URI);

            // The crypto mine submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the switch on event connection
            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, SwitchOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, SwitchOnCryptoMiner.class)
                    }
            );

            // Add the switch off event connection
            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, SwitchOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, SwitchOffCryptoMiner.class)
                    }
            );

            // Add the mine on event connection
            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, MineOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, MineOnCryptoMiner.class)
                    }
            );

            // Add the mine off event connection
            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, MineOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, MineOffCryptoMiner.class)
                    }
            );

            // Add the crypto miner coupled model
            coupledModelDescriptors.put(
                    CryptoMinerCoupledModel.URI,
                    new CoupledModelDescriptor(
                            CryptoMinerCoupledModel.class,
                            CryptoMinerCoupledModel.URI,
                            subModel,
                            null,
                            null,
                            connections,
                            null,
                            SimulationEngineCreationMode.COORDINATION_ENGINE
                    )
            );

            // Create the simulation architecture
            ArchitectureI architecture = new Architecture(
                    CryptoMinerCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 10.0);
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
