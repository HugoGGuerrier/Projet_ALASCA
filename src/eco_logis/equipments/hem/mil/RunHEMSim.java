package eco_logis.equipments.hem.mil;

import eco_logis.equipments.crypto_miner.mil.CryptoMinerElectricityModel;
import eco_logis.equipments.crypto_miner.mil.CryptoMinerUserModel;
import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.dishwasher.mil.DishwasherElectricityModel;
import eco_logis.equipments.dishwasher.mil.DishwasherUserModel;
import eco_logis.equipments.dishwasher.mil.events.*;
import eco_logis.equipments.electric_meter.mil.ElectricMeterElectricityModel;
import eco_logis.equipments.generator.mil.GeneratorElectricityModel;
import eco_logis.equipments.generator.mil.GeneratorFuelModel;
import eco_logis.equipments.generator.mil.GeneratorUserModel;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
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

public class RunHEMSim {
    public static void main(String[] args) {
        try {

            // --- The model descriptors map
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the crypto miner
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

            // Add the dishwasher
            atomicModelDescriptors.put(
                    DishwasherElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            DishwasherElectricityModel.class,
                            DishwasherElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            atomicModelDescriptors.put(
                    DishwasherUserModel.URI,
                    AtomicModelDescriptor.create(
                            DishwasherUserModel.class,
                            DishwasherUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the generator
            atomicModelDescriptors.put(
                    GeneratorElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            GeneratorElectricityModel.class,
                            GeneratorElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            atomicModelDescriptors.put(
                    GeneratorFuelModel.URI,
                    AtomicHIOA_Descriptor.create(
                            GeneratorFuelModel.class,
                            GeneratorFuelModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            atomicModelDescriptors.put(
                    GeneratorUserModel.URI,
                    AtomicModelDescriptor.create(
                            GeneratorUserModel.class,
                            GeneratorUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the electric meter
            atomicModelDescriptors.put(
                    ElectricMeterElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ElectricMeterElectricityModel.class,
                            ElectricMeterElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );


            // --- Create the submodel set
            Set<String> submodels = new HashSet<>();
            submodels.add(CryptoMinerElectricityModel.URI);
            submodels.add(CryptoMinerUserModel.URI);
            submodels.add(DishwasherElectricityModel.URI);
            submodels.add(DishwasherUserModel.URI);
            submodels.add(GeneratorElectricityModel.URI);
            submodels.add(GeneratorFuelModel.URI);
            submodels.add(GeneratorUserModel.URI);
            submodels.add(ElectricMeterElectricityModel.URI);


            // --- Create the event connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the crypto miner event
            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, SwitchOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, SwitchOnCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, SwitchOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, SwitchOffCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, MineOnCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, MineOnCryptoMiner.class)
                    }
            );

            connections.put(
                    new EventSource(CryptoMinerUserModel.URI, MineOffCryptoMiner.class),
                    new EventSink[] {
                            new EventSink(CryptoMinerElectricityModel.URI, MineOffCryptoMiner.class)
                    }
            );

            // Add the dishwasher events
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SwitchOffDishwasher.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SwitchOffDishwasher.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetEcoProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetEcoProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetFastProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetFastProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetFullProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetFullProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetRinseProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetRinseProgram.class)
                    }
            );

            // Add the generator events
            connections.put(
                    new EventSource(GeneratorUserModel.URI, SwitchOnGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricityModel.URI, SwitchOnGenerator.class),
                            new EventSink(GeneratorFuelModel.URI, SwitchOnGenerator.class)
                    }
            );

            connections.put(
                    new EventSource(GeneratorUserModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricityModel.URI, SwitchOffGenerator.class),
                            new EventSink(GeneratorFuelModel.URI, SwitchOffGenerator.class),
                    }
            );

            connections.put(
                    new EventSource(GeneratorFuelModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricityModel.URI, SwitchOffGenerator.class),
                    }
            );


            // --- Create the variable bindings
            Map<VariableSource, VariableSink[]> bindings = new HashMap<>();

            // Bind the fuel level from the fuel model to the electricity model
            bindings.put(
                    new VariableSource("currentFuelLevel", Double.class, GeneratorFuelModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentFuelLevel", Double.class, GeneratorElectricityModel.URI)
                    }
            );

            // Bind the crypto miner consumption to the electric meter
            bindings.put(
                    new VariableSource("currentConsumption", Double.class, CryptoMinerElectricityModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentCryptoConsumption", Double.class, ElectricMeterElectricityModel.URI)
                    }
            );

            // Bind the dishwasher consumption to the electric meter
            bindings.put(
                    new VariableSource("currentConsumption", Double.class, DishwasherElectricityModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentDishwasherConsumption", Double.class, ElectricMeterElectricityModel.URI)
                    }
            );

            // Bind the generator production to the electric meter
            bindings.put(
                    new VariableSource("currentProduction", Double.class, GeneratorElectricityModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentGeneratorProduction", Double.class, ElectricMeterElectricityModel.URI)
                    }
            );


            // --- Create the coupled model descriptor
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            coupledModelDescriptors.put(
                    HEMCoupledModel.URI,
                    new CoupledHIOA_Descriptor(
                            HEMCoupledModel.class,
                            HEMCoupledModel.URI,
                            submodels,
                            null,
                            null,
                            connections,
                            null,
                            SimulationEngineCreationMode.COORDINATION_ENGINE,
                            null,
                            null,
                            bindings
                    ));


            // Create the architecture
            ArchitectureI architecture = new Architecture(
                    HEMCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 100.0);

            // Exit the simulation
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
