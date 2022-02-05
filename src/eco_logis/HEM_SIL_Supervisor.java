package eco_logis;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.crypto_miner.mil.CryptoMinerCoupledModel;
import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.electric_meter.ElectricMeter;
import eco_logis.equipments.electric_meter.sil.ElectricMeterCoupledModel;
import eco_logis.equipments.hem.mil.HEMCoupledModel;
import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.oven.mil.OvenCoupledModel;
import eco_logis.equipments.oven.mil.events.DoNotHeatOven;
import eco_logis.equipments.oven.mil.events.HeatOven;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTCoordinatorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitecture;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentAtomicModelDescriptor;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentCoupledModelDescriptor;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentModelArchitecture;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>HEM_SIL_Supervisor</code> implements the supervisor component
 * to perform software-in-the-loop simulations of the household energy manager
 * project.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * In SIL simulations for testing or other purposes, supervisor components
 * have the responsibility:
 * </p>
 * <ul>
 * <li>to create the overall simulation architecture over components from the
 *   local simulation architectures created in each participant component,</li>
 * <li>to perform the simulation campaign by successively starting simulation
 *   runs, often with different simulation run parameters, and</li>
 * <li>to process and analyse the results of the simulation campaign from the
 *   reports returned after each run.</li>
 * </ul>
 * <p>
 * To do so, the component uses the supervisor plug-in provided by
 * BCM4Java-CyPhy to which it passes the global simulation architecture and
 * then uses its methods to create the global simulator, run simulations and
 * get back the simulation reports.
 * </p>
 * <p>
 * In SIL simulations, the simulation architecture (composed of DEVS atomic
 * and coupled models) has a specific form. Each component defines its
 * local architecture that may be a composition of atomic and coupled models
 * with a local root coupled model. Each local architecture is then seen as an
 * atomic model, thanks to the DEVS closed over composition property (the
 * composition of models under a coupled model also defines an atomic model
 * hiding its internal structure). Hence, the global architecture defined by
 * the supervisor sees all of these local architectures to be composed under
 * a (hierarchy of) coupled models.
 * </p>
 * <p>
 * Coupled models appearing in the global simulation architecture must be run
 * by components. At this time, the simulation library and BCM4Java-CyPhy
 * impose that each of these coupled models are run by distinct components
 * that are seen as simulation coordinator components. When creating the
 * simulation architecture, BCM4Java-CyPhy interconnects the involved
 * components through component interfaces, ports and connectors (all of
 * them predefined by BCM4Java-CyPhy) that will allow for managing and running
 * the simulations.
 * </p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class HEM_SIL_Supervisor
    extends AbstractCyPhyComponent
{

    // ========== Macros ==========


    /** Simulation architectures can have URI to name them; this is the URI used in this example */
    public static final String SIM_ARCHITECTURE_URI = "global";

    /** URI of the supervisor plug-in */
    protected static final String SUPERVISOR_PLUGIN_URI = "supervisor-uri";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** The supervisor plug-in attached to this component */
    protected SupervisorPlugin sp;


    // ========== Constructors ==========


    /**
     * Create the supervisor component.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	{@code isInstalled(SUPERVISOR_PLUGIN_URI)}
     * </pre>
     */
    protected HEM_SIL_Supervisor() {
        super(1, 0);
        this.initialise();
    }

    /**
     * Create the supervisor component.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code reflectionInboundPortURI != null && !reflectionInboundPortURI.isEmpty()}
     * post	{@code isInstalled(SUPERVISOR_PLUGIN_URI)}
     * </pre>
     *
     * @param reflectionInboundPortURI	URI of the reflection inbound port
     */
    protected HEM_SIL_Supervisor(String reflectionInboundPortURI) {
        super(reflectionInboundPortURI, 1, 0);
        this.initialise();
    }


    // ========== Class methods ==========


    /**
     * Initialise the supervisor plug-in of this component.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	{@code isInstalled(SUPERVISOR_PLUGIN_URI)}
     * </pre>
     */
    protected void initialise() {
        try {
            this.sp = new SupervisorPlugin(this.createArchitecture());
            this.sp.setPluginURI(SUPERVISOR_PLUGIN_URI);
            this.installPlugin(this.sp);
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }

        if (VERBOSE) {
            this.tracer.get().setTitle("Supervisor component");
            this.tracer.get().setRelativePosition(0, 0);
            this.toggleTracing();
        }
    }


    /**
     * Create the global simulation architecture.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true			// no precondition.
     * post	true			// no postcondition.
     * </pre>
     *
     * @return				the global simulation architecture.
     * @throws Exception	<i>to do</i>.
     */
    protected ComponentModelArchitecture createArchitecture() throws Exception {
        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

        /* Descriptors for atomic models included in components must define
        simulation models specific information plus the reflection inbound
        port URI of the component holding them in order to connect this
        component inside the component-based simulation architecture. */

        // The crypto miner simulation model held by the CryptoMiner component
        atomicModelDescriptors.put(
                CryptoMinerCoupledModel.URI,
                RTComponentAtomicModelDescriptor.create(
                        CryptoMinerCoupledModel.URI,
                        new Class[]{},
                        new Class[]{
                                SwitchOnCryptoMiner.class,
                                SwitchOffCryptoMiner.class,
                                MineOnCryptoMiner.class,
                                MineOffCryptoMiner.class
                        },
                        TimeUnit.SECONDS,
                        CryptoMiner.REFLECTION_INBOUND_PORT_URI));
/*
        // The oven simulation model held by the Oven component
        atomicModelDescriptors.put(
                OvenCoupledModel.URI,
                RTComponentAtomicModelDescriptor.create(
                        OvenCoupledModel.URI,
                        new Class[]{},
                        new Class[]{
                                SwitchOnOven.class,
                                SwitchOffOven.class,
                                HeatOven.class,
                                DoNotHeatOven.class},
                        TimeUnit.SECONDS,
                        Oven.REFLECTION_INBOUND_PORT_URI));
*/

        // The electric meter simulation model held by the ElectricMeter component
        atomicModelDescriptors.put(
                ElectricMeterCoupledModel.URI,
                RTComponentAtomicModelDescriptor.create(
                        ElectricMeterCoupledModel.URI,
                        new Class[]{
                                SwitchOnCryptoMiner.class,
                                SwitchOffCryptoMiner.class,
                                MineOnCryptoMiner.class,
                                MineOffCryptoMiner.class/*,

                                SwitchOnOven.class,
                                SwitchOffOven.class,
                                HeatOven.class,
                                DoNotHeatOven.class*/
                        },
                        new Class[]{},
                        TimeUnit.SECONDS,
                        ElectricMeter.REFLECTION_INBOUND_PORT_URI));

        Set<String> submodels = new HashSet<String>();
        submodels.add(CryptoMinerCoupledModel.URI);
        //submodels.add(OvenCoupledModel.URI);
        submodels.add(ElectricMeterCoupledModel.URI);

        Map<EventSource, EventSink[]> connections = new HashMap<EventSource,EventSink[]>();

        /* In the component-based simulation architecture, electric consumption
        simulation models of appliances must be co-located with the electric
        meter simulation model to share continuous variables. Hence, events
        coming from the appliances towards their electric consumption models
        must pass from their components to the ElectricMeter component as
        shown in the next connections. */
        connections.put(
                new EventSource(CryptoMinerCoupledModel.URI, SwitchOnCryptoMiner.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, SwitchOnCryptoMiner.class)
                });
        connections.put(
                new EventSource(CryptoMinerCoupledModel.URI, SwitchOffCryptoMiner.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, SwitchOffCryptoMiner.class)
                });
        connections.put(
                new EventSource(CryptoMinerCoupledModel.URI, MineOnCryptoMiner.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, MineOnCryptoMiner.class)
                });
        connections.put(
                new EventSource(CryptoMinerCoupledModel.URI, MineOffCryptoMiner.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, MineOffCryptoMiner.class)
                });
/*
        connections.put(
                new EventSource(OvenCoupledModel.URI, SwitchOnOven.class),
                new EventSink[] {
                         new EventSink(ElectricMeterCoupledModel.URI, SwitchOnOven.class)
                });
        connections.put(
                new EventSource(OvenCoupledModel.URI, SwitchOffOven.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, SwitchOffOven.class)
                });
        connections.put(
                new EventSource(OvenCoupledModel.URI, HeatOven.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, HeatOven.class)
                });
        connections.put(
                new EventSource(OvenCoupledModel.URI, DoNotHeatOven.class),
                new EventSink[] {
                        new EventSink(ElectricMeterCoupledModel.URI, DoNotHeatOven.class)
                });

*/
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();
        coupledModelDescriptors.put(
                HEMCoupledModel.URI,
                RTComponentCoupledModelDescriptor.create(
                        HEMCoupledModel.class,
                        HEMCoupledModel.URI,
                        submodels,
                        null,
                        null,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        SIL_Coordinator.REFLECTION_INBOUND_PORT_URI,
                        RTCoordinatorPlugin.class,
                        null,
                        CVM_SIL.ACC_FACTOR));

        /* SIL simulations must be executed under the real time modality to
        be able to intertwine simulation events processing and code
        execution under the same timeline. */
        RTComponentModelArchitecture arch =			// RT = RealTime
                new RTComponentModelArchitecture(
                        SIM_ARCHITECTURE_URI,
                        HEMCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS);

        return arch;
    }


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        this.traceMessage("Supervisor starts.\n");
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        this.sp.createSimulator();
        this.sp.setSimulationRunParameters(new HashMap<String, Object>());
        long realTimeOfStart =
                System.currentTimeMillis() + fr.sorbonne_u.components.cyphy.hem2021e3.CVM_SIL.DELAY_TO_START_SIMULATION;
        this.sp.startRTSimulation(realTimeOfStart, 0.0,
                fr.sorbonne_u.components.cyphy.hem2021e3.CVM_SIL.SIMULATION_DURATION);
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        this.traceMessage("Supervisor stops.\n");
        super.shutdown();
    }

}
