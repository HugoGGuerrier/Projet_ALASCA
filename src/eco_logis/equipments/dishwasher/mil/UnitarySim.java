package eco_logis.equipments.dishwasher.mil;

import eco_logis.equipments.dishwasher.mil.events.*;
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

            // Add the dishwasher electricity model
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

            // Add the dishwasher user model
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


            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled dishwasher model
            Set<String> subModel = new HashSet<>();
            subModel.add(DishwasherElectricityModel.URI);
            subModel.add(DishwasherUserModel.URI);

            // The dishwasher submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the switch off event connection
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SwitchOffDishwasher.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SwitchOffDishwasher.class)
                    }
            );

            // Add the eco program event connection
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetEcoProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetEcoProgram.class)
                    }
            );

            // Add the fast program event connection
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetFastProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetFastProgram.class)
                    }
            );

            // Add the full program event connection
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetFullProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetFullProgram.class)
                    }
            );

            // Add the rinse program event connection
            connections.put(
                    new EventSource(DishwasherUserModel.URI, SetRinseProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricityModel.URI, SetRinseProgram.class)
                    }
            );

            // Add the dishwasher coupled model
            coupledModelDescriptors.put(
                    DishwasherCoupledModel.URI,
                    new CoupledModelDescriptor(
                            DishwasherCoupledModel.class,
                            DishwasherCoupledModel.URI,
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
                    DishwasherCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 50.0);
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
