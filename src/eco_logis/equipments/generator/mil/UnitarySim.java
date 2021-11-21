package eco_logis.equipments.generator.mil;

import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
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

public class UnitarySim {
    public static void main(String[] args) {
        try {

            // The map of the atomic model description
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the generator electricity model
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

            // Add the generator fuel model
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

            // Add the generator user model
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


            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled generator model
            Set<String> subModel = new HashSet<>();
            subModel.add(GeneratorElectricityModel.URI);
            subModel.add(GeneratorFuelModel.URI);
            subModel.add(GeneratorUserModel.URI);

            // The generator submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the switch on event connection
            connections.put(
                    new EventSource(GeneratorUserModel.URI, SwitchOnGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricityModel.URI, SwitchOnGenerator.class),
                            new EventSink(GeneratorFuelModel.URI, SwitchOnGenerator.class)
                    }
            );

            // Add the switch off event connection
            connections.put(
                    new EventSource(GeneratorUserModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricityModel.URI, SwitchOffGenerator.class),
                            new EventSink(GeneratorFuelModel.URI, SwitchOffGenerator.class),
                    }
            );

            // Add the generator coupled model
            coupledModelDescriptors.put(
                    GeneratorCoupledModel.URI,
                    new CoupledModelDescriptor(
                            GeneratorCoupledModel.class,
                            GeneratorCoupledModel.URI,
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
                    GeneratorCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 100.0);
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
