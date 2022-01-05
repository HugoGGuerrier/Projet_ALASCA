package eco_logis.equipments.dishwasher;

import eco_logis.equipments.dishwasher.mil.DishwasherCoupledModel;
import eco_logis.equipments.dishwasher.mil.events.*;
import eco_logis.equipments.dishwasher.sil.DishwasherElectricitySILModel;
import eco_logis.equipments.dishwasher.sil.DishwasherStateModel;
import eco_logis.equipments.dishwasher.sil.DishwasherUserSILModel;
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
public class DishwasherRTAtomicSimulatorPlugin
        extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestDishwasher";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "DWCRN";



    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the Dishwasher
     *
     * <p><strong>Contract</strong></p>
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
        submodels.add(DishwasherUserSILModel.URI);
        submodels.add(DishwasherStateModel.URI);

        // Declare the connections and reexported maps for the events
        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;

        // Put the model in the descriptors
        atomicModelDescriptors.put(
                DishwasherUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        DishwasherUserSILModel.class,
                        DishwasherUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                DishwasherStateModel.URI,
                RTAtomicModelDescriptor.create(
                        DishwasherStateModel.class,
                        DishwasherStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        if(simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            // Add the electricity model to the arch
            submodels.add(DishwasherElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    DishwasherElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            DishwasherElectricitySILModel.class,
                            DishwasherElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            // Create the event connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(DishwasherStateModel.URI, SetEcoProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricitySILModel.URI, SetEcoProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherStateModel.URI, SetFastProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricitySILModel.URI, SetFastProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherStateModel.URI, SetFullProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricitySILModel.URI, SetFullProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherStateModel.URI, SetRinseProgram.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricitySILModel.URI, SetRinseProgram.class)
                    }
            );

            connections.put(
                    new EventSource(DishwasherStateModel.URI, SwitchOffDishwasher.class),
                    new EventSink[] {
                            new EventSink(DishwasherElectricitySILModel.URI, SwitchOffDishwasher.class)
                    }
            );

        } else {

            // When not in unit test mode just create reexported events
            reexported = new HashMap<>();

            reexported.put(
                    SetEcoProgram.class,
                    new ReexportedEvent(DishwasherStateModel.URI, SetEcoProgram.class)
            );

            reexported.put(
                    SetFastProgram.class,
                    new ReexportedEvent(DishwasherStateModel.URI, SetFastProgram.class)
            );

            reexported.put(
                    SetFullProgram.class,
                    new ReexportedEvent(DishwasherStateModel.URI, SetFullProgram.class)
            );

            reexported.put(
                    SetRinseProgram.class,
                    new ReexportedEvent(DishwasherStateModel.URI, SetRinseProgram.class)
            );

            reexported.put(
                    SwitchOffDishwasher.class,
                    new ReexportedEvent(DishwasherStateModel.URI, SwitchOffDishwasher.class)
            );

        }

        // Create the coupled model descriptor
        coupledModelDescriptors.put(
                DishwasherCoupledModel.URI,
                new RTCoupledModelDescriptor(
                        DishwasherCoupledModel.class,
                        DishwasherCoupledModel.URI,
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
                        DishwasherCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor
                )
        );
    }



}
