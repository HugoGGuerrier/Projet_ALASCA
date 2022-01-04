package eco_logis.equipments.generator;

import eco_logis.equipments.generator.mil.GeneratorCoupledModel;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
import eco_logis.equipments.generator.sil.GeneratorElectricitySILModel;
import eco_logis.equipments.generator.sil.GeneratorFuelSILModel;
import eco_logis.equipments.generator.sil.GeneratorStateModel;
import eco_logis.equipments.generator.sil.GeneratorUserModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.*;
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
 * This class represents the SIL simulation plugin for the Generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestGenerator";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "GCRN";


    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the Generator
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
        submodels.add(GeneratorUserModel.URI);
        submodels.add(GeneratorStateModel.URI);
        submodels.add(GeneratorFuelSILModel.URI);

        // Declare the connections and reexported maps for the events
        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;
        Map<VariableSource, VariableSink[]> bindings = null;

        // Put the model in the descriptors
        atomicModelDescriptors.put(
                GeneratorUserModel.URI,
                RTAtomicModelDescriptor.create(
                        GeneratorUserModel.class,
                        GeneratorUserModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                GeneratorStateModel.URI,
                RTAtomicModelDescriptor.create(
                        GeneratorStateModel.class,
                        GeneratorStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                GeneratorFuelSILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        GeneratorFuelSILModel.class,
                        GeneratorFuelSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        if(simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            // Add the electricity model to the submodels and descriptors
            submodels.add(GeneratorElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    GeneratorElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            GeneratorElectricitySILModel.class,
                            GeneratorElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            // Create the event connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(GeneratorStateModel.URI, SwitchOnGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricitySILModel.URI, SwitchOnGenerator.class),
                            new EventSink(GeneratorFuelSILModel.URI, SwitchOnGenerator.class)
                    }
            );

            connections.put(
                    new EventSource(GeneratorStateModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricitySILModel.URI, SwitchOffGenerator.class),
                            new EventSink(GeneratorFuelSILModel.URI, SwitchOffGenerator.class)
                    }
            );

            connections.put(
                    new EventSource(GeneratorFuelSILModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorElectricitySILModel.URI, SwitchOffGenerator.class)
                    }
            );

            // Put the variable binding
            bindings = new HashMap<>();

            bindings.put(
                    new VariableSource("currentFuelLevel", Double.class, GeneratorFuelSILModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentFuelLevel", Double.class, GeneratorElectricitySILModel.URI)
                    }
            );

        } else {

            // Just do the basic connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(GeneratorStateModel.URI, SwitchOnGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorFuelSILModel.URI, SwitchOnGenerator.class)
                    }
            );

            connections.put(
                    new EventSource(GeneratorStateModel.URI, SwitchOffGenerator.class),
                    new EventSink[] {
                            new EventSink(GeneratorFuelSILModel.URI, SwitchOffGenerator.class)
                    }
            );

            // Reexport the events
            reexported = new HashMap<>();

            reexported.put(
                    SwitchOnGenerator.class,
                    new ReexportedEvent(GeneratorStateModel.URI, SwitchOnGenerator.class)
            );

            reexported.put(
                    SwitchOffGenerator.class,
                    new ReexportedEvent(GeneratorStateModel.URI, SwitchOffGenerator.class)
            );

        }

        // Create the coupled model descriptor
        coupledModelDescriptors.put(
                GeneratorCoupledModel.URI,
                new RTCoupledHIOA_Descriptor(
                        GeneratorCoupledModel.class,
                        GeneratorCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        null,
                        bindings,
                        accFactor
                )
        );

        // Set the plugin arch
        setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        GeneratorCoupledModel.URI,
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
