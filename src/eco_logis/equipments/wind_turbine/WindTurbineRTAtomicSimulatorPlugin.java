package eco_logis.equipments.wind_turbine;

import eco_logis.equipments.wind_turbine.mil.WindTurbineCoupledModel;
import eco_logis.equipments.wind_turbine.mil.events.BlockWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.UnblockWindTurbine;
import eco_logis.equipments.wind_turbine.sil.ExternalWindSILModel;
import eco_logis.equipments.wind_turbine.sil.WindTurbineElectricitySILModel;
import eco_logis.equipments.wind_turbine.sil.WindTurbineStateModel;
import eco_logis.equipments.wind_turbine.sil.WindTurbineUserSILModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WindTurbineRTAtomicSimulatorPlugin
        extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestWindTurbine";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "WTCRN";


    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the Wind Turbine
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
        submodels.add(WindTurbineUserSILModel.URI);
        submodels.add(WindTurbineStateModel.URI);
        submodels.add(ExternalWindSILModel.URI);

        // Declare the connections and reexported maps for the events
        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;

        // Put the model in the descriptors
        atomicModelDescriptors.put(
                WindTurbineUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        WindTurbineUserSILModel.class,
                        WindTurbineUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                WindTurbineStateModel.URI,
                RTAtomicModelDescriptor.create(
                        WindTurbineStateModel.class,
                        WindTurbineStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        if(simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            // Add the electricity model to the arch
            submodels.add(WindTurbineElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    WindTurbineElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            WindTurbineElectricitySILModel.class,
                            WindTurbineElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            atomicModelDescriptors.put(
                    ExternalWindSILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            ExternalWindSILModel.class,
                            ExternalWindSILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            // Create the event connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(WindTurbineStateModel.URI, BlockWindTurbine.class),
                    new EventSink[] {
                            new EventSink(WindTurbineElectricitySILModel.URI, BlockWindTurbine.class)
                    }
            );

            connections.put(
                    new EventSource(WindTurbineStateModel.URI, UnblockWindTurbine.class),
                    new EventSink[] {
                            new EventSink(WindTurbineElectricitySILModel.URI, UnblockWindTurbine.class)
                    }
            );

        } else {

            // When not in unit test mode just create reexported events
            reexported = new HashMap<>();

            reexported.put(
                    BlockWindTurbine.class,
                    new ReexportedEvent(WindTurbineStateModel.URI, BlockWindTurbine.class)
            );

            reexported.put(
                    UnblockWindTurbine.class,
                    new ReexportedEvent(WindTurbineStateModel.URI, UnblockWindTurbine.class)
            );

        }

        // Variable bindings between exporting and importing models
        Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource,VariableSink[]>();

        bindings.put(new VariableSource("externalWindSpeed",
                        Double.class,
                        ExternalWindSILModel.URI),
                new VariableSink[] {
                        new VariableSink("externalWindSpeed",
                                Double.class,
                                WindTurbineElectricitySILModel.URI)
                });

        // Create the coupled model descriptor
        coupledModelDescriptors.put(
                WindTurbineCoupledModel.URI,
                new RTCoupledHIOA_Descriptor(
                        WindTurbineCoupledModel.class,
                        WindTurbineCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        null,
                        bindings,
                        accFactor));

        // Set the plugin arch
        setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        WindTurbineCoupledModel.URI,
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
