package eco_logis.equipments.wind_turbine.mil;

import eco_logis.equipments.wind_turbine.mil.events.BlockWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.UnblockWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.WindSpeedChange;
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

/**
 * This class is used to run a unitary simulation on the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class UnitarySim {
    public static void main(String[] args) {
        try {

            // The map of the atomic model description
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the wind turbine electricity model
            atomicModelDescriptors.put(
                    WindTurbineElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            WindTurbineElectricityModel.class,
                            WindTurbineElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the external wind model
            atomicModelDescriptors.put(
                    ExternalWindModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ExternalWindModel.class,
                            ExternalWindModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the wind turbine user model
            atomicModelDescriptors.put(
                    WindTurbineUserModel.URI,
                    AtomicModelDescriptor.create(
                            WindTurbineUserModel.class,
                            WindTurbineUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );


            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled wind turbine model
            Set<String> subModels = new HashSet<>();
            subModels.add(WindTurbineElectricityModel.URI);
            subModels.add(WindTurbineUserModel.URI);
            subModels.add(ExternalWindModel.URI);


            // The wind turbine submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the unblock event connection
            connections.put(
                    new EventSource(WindTurbineUserModel.URI, UnblockWindTurbine.class),
                    new EventSink[] {
                            new EventSink(WindTurbineElectricityModel.URI, UnblockWindTurbine.class)
                    }
            );

            // Add the block event connection
            connections.put(
                    new EventSource(WindTurbineUserModel.URI, BlockWindTurbine.class),
                    new EventSink[] {
                            new EventSink(WindTurbineElectricityModel.URI, BlockWindTurbine.class)
                    }
            );

            // Add the wind speed change internal event connection
            connections.put(
                    new EventSource(ExternalWindModel.URI, WindSpeedChange.class),
                    new EventSink[] {
                            new EventSink(WindTurbineElectricityModel.URI, WindSpeedChange.class)
                    }
            );

            // Variable bindings between exporting and importing models
            Map<VariableSource, VariableSink[]> bindings = new HashMap<>();

            bindings.put(
                    new VariableSource("externalWindSpeed",
                            Double.class,
                            ExternalWindModel.URI),
                    new VariableSink[] {
                            new VariableSink("externalWindSpeed",
                                    Double.class,
                                    WindTurbineElectricityModel.URI)
                    });

            // Coupled model descriptor
            coupledModelDescriptors.put(
                    WindTurbineCoupledModel.URI,
                    new CoupledHIOA_Descriptor(
                            WindTurbineCoupledModel.class,
                            WindTurbineCoupledModel.URI,
                            subModels,
                            null,
                            null,
                            connections,
                            null,
                            SimulationEngineCreationMode.COORDINATION_ENGINE,
                            null,
                            null,
                            bindings));

            // Create the simulation architecture
            ArchitectureI architecture = new Architecture(
                            WindTurbineCoupledModel.URI,
                            atomicModelDescriptors,
                            coupledModelDescriptors,
                            TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 5.0);
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
