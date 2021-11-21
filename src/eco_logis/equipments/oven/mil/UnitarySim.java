package eco_logis.equipments.oven.mil;

import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
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
 * This class is used to run a unary simulation on the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class UnitarySim {
    public static void main(String[] args) {
        try {

            // The map of the atomic model description
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the oven electricity model
            atomicModelDescriptors.put(
                    OvenElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            OvenElectricityModel.class,
                            OvenElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the oven temperature model
            atomicModelDescriptors.put(
                    OvenTemperatureModel.URI,
                    AtomicHIOA_Descriptor.create(
                            OvenTemperatureModel.class,
                            OvenTemperatureModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the oven user model
            atomicModelDescriptors.put(
                    OvenUserModel.URI,
                    AtomicModelDescriptor.create(
                            OvenUserModel.class,
                            OvenUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled oven model
            Set<String> subModel = new HashSet<>();
            subModel.add(OvenElectricityModel.URI);
            subModel.add(OvenTemperatureModel.URI);
            subModel.add(OvenUserModel.URI);

            // The oven submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the switch on event connection
            connections.put(
                    new EventSource(OvenUserModel.URI, SwitchOnOven.class),
                    new EventSink[] {
                            new EventSink(OvenElectricityModel.URI, SwitchOnOven.class),
                            new EventSink(OvenTemperatureModel.URI, SwitchOnOven.class)
                    }
            );

            // Add the switch off event connection
            connections.put(
                    new EventSource(OvenUserModel.URI, SwitchOffOven.class),
                    new EventSink[] {
                            new EventSink(OvenElectricityModel.URI, SwitchOffOven.class),
                            new EventSink(OvenTemperatureModel.URI, SwitchOffOven.class)
                    }
            );

            // Add the oven coupled model
            coupledModelDescriptors.put(
                    OvenCoupledModel.URI,
                    new CoupledModelDescriptor(
                            OvenCoupledModel.class,
                            OvenCoupledModel.URI,
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
                    OvenCoupledModel.URI,
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