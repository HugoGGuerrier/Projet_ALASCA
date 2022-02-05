package eco_logis.equipments.oven;

import eco_logis.equipments.oven.mil.OvenCoupledModel;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import eco_logis.equipments.oven.sil.OvenUserSILModel;
import eco_logis.equipments.oven.sil.OvenElectricitySILModel;
import eco_logis.equipments.oven.sil.OvenStateModel;
import eco_logis.equipments.oven.sil.OvenTemperatureSILModel;
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
 * The class <code>OvenRTAtomicSimulatorPlugin</code> defines the plug-in
 * that manages the SIL simulation inside the oven component.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;

    /** Simulation architectures can have URI to name them; this is the URI used in this example for unit tests. */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestOven";

    /** Name used to pass the owner component reference as simulation parameter. */
    public static final String OWNER_REFERENCE_NAME = "OCRN";

    /** Name for room temperature */
    public static final String ROOM_TEMPERATURE = "room-temp";


    // ========== Class methods ==========


    /**
     * Create and set the simulation architecture internal to this component.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param simArchURI	URI of the simulation architecture to be created.
     * @param accFactor				acceleration factor used in the real time simulation.
     * @throws Exception			<i>to do</i>.
     */
    public void initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {
        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

        Set<String> submodels = new HashSet<String>();
        submodels.add(OvenUserSILModel.URI);
        submodels.add(OvenStateModel.URI);
        submodels.add(OvenTemperatureSILModel.URI);

        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = new HashMap<EventSource, EventSink[]>();

        atomicModelDescriptors.put(
                OvenUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        OvenUserSILModel.class,
                        OvenUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));

        atomicModelDescriptors.put(
                OvenStateModel.URI,
                RTAtomicModelDescriptor.create( // RTAtomicHIOA_Descriptor??
                        OvenStateModel.class,
                        OvenStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));

        atomicModelDescriptors.put(
                OvenTemperatureSILModel.URI,
                RTAtomicModelDescriptor.create( // RTAtomicHIOA_Descriptor??
                        OvenTemperatureSILModel.class,
                        OvenTemperatureSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));

        if (simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {
            /* When executed as a unit test, the simulation architecture
            includes the oven electricity model and events exported
            by the state model are directed to the electricity model */
            submodels.add(OvenElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    OvenElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            OvenElectricitySILModel.class,
                            OvenElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor));

            connections.put(
                    new EventSource(OvenStateModel.URI, SwitchOnOven.class),
                    new EventSink[] {
                            new EventSink(OvenElectricitySILModel.URI, SwitchOnOven.class)
                    });
            connections.put(
                    new EventSource(OvenStateModel.URI, SwitchOffOven.class),
                    new EventSink[] {
                            new EventSink(OvenElectricitySILModel.URI, SwitchOffOven.class)
                    });

        } else {
            /* When *not* executed as a unit test, the simulation architecture
            does not include the oven electricity model and events
            exported by the state model are reexported by the coupled model */

            reexported = new HashMap<Class<? extends EventI>,ReexportedEvent>();

            reexported.put(
                    SwitchOnOven.class,
                    new ReexportedEvent(OvenStateModel.URI, SwitchOnOven.class));

            reexported.put(
                    SwitchOffOven.class,
                    new ReexportedEvent(OvenStateModel.URI, SwitchOffOven.class));
        }

        coupledModelDescriptors.put(
                OvenCoupledModel.URI,
                new RTCoupledModelDescriptor(
                        OvenCoupledModel.class,
                        OvenCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        accFactor));

        // This sets the architecture in the plug-in for further reference and use
        this.setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        OvenCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor));
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        /* Initialise the simulation parameter giving the reference to the
        owner component before passing the parameters to the simulation models */
        simParams.put(OWNER_REFERENCE_NAME, this.getOwner());

        /* This will pass the parameters to the simulation models that will
        then be able to get their own parameters. */
        super.setSimulationRunParameters(simParams);

        // Remove the value so that the reference may not exit the context of the component
        simParams.remove(OWNER_REFERENCE_NAME);
    }

}
