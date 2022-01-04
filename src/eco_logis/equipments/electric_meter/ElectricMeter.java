package eco_logis.equipments.electric_meter;

import eco_logis.CVM_SIL;
import eco_logis.equipments.electric_meter.sil.ElectricMeterCoupledModel;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// -----------------------------------------------------------------------------
/**
 * The class <code>ElectricMeter</code> implements a simplified electric meter
 * component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@OfferedInterfaces(offered={ElectricMeterCI.class})
public class ElectricMeter
    extends AbstractCyPhyComponent
    implements ElectricMeterImplementationI
{

    // ========== Macros ==========


    /** URI of the electric meter reflection inbound port */
    public static final String	REFLECTION_INBOUND_PORT_URI = "ELECTRIC-METER-rip";

    /** URI of the electric meter inbound port used in tests */
    public static final String	ELECTRIC_METER_INBOUND_PORT_URI = "ELECTRIC-METER";

    /** when true, methods trace their actions */
    public static final boolean VERBOSE = true;

    /** Acceleration factor used when executing as a unit test */
    protected static final double ACC_FACTOR = 1.0;

    /** URI of the executor service used to execute the real time simulation */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";


    // ========== Attributes ==========


    /** inbound port offering the <code>ElectricMeterCI</code> interface */
    protected ElectricMeterInboundPort emip;

    /** Simulator plug-in that holds the SIL simulator for this component */
    protected ElectricMeterRTAtomicSimulatorPlugin simulatorPlugin;

    /** True if the component executes as a SIL simulation, false otherwise */
    protected boolean isSILsimulated;

    /** True if the component executes as a unit test, false otherwise */
    protected boolean executesAsUnitTest;

    /** URI of the simulation architecture to be created or the empty string
     *  if the component does not execute as a SIL simulation */
    protected String simArchitectureURI;


    // ========== Constructors ==========


    /**
     * Create an electric meter component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ELECTRIC_METER_INBOUND_PORT_URI != null}
     * pre	{@code !ELECTRIC_METER_INBOUND_PORT_URI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param simArchitectureURI	URI of the simulation architecture to be created or the empty string if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest	true if the component executes as a unit test, false otherwise.
     * @throws Exception			<i>to do</i>.
     */
    protected ElectricMeter(
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        this(ELECTRIC_METER_INBOUND_PORT_URI, simArchitectureURI,
                executesAsUnitTest);
    }

    /**
     * Create an electric meter component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code electricMeterInboundPortURI != null}
     * pre	{@code !electricMeterInboundPortURI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param electricMeterInboundPortURI	URI of the electric meter inbound port.
     * @param simArchitectureURI			URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest			true if the component executes as a unit test, false otherwise.
     * @throws Exception					<i>to do</i>.
     */
    protected ElectricMeter(
            String electricMeterInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        this(electricMeterInboundPortURI, simArchitectureURI,
                executesAsUnitTest, 1, 1);
    }

    /**
     * Create an electric meter component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code electricMeterInboundPortURI != null}
     * pre	{@code !electricMeterInboundPortURI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param electricMeterInboundPortURI	URI of the electric meter inbound port.
     * @param simArchitectureURI			URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest			true if the component executes as a unit test, false otherwise.
     * @param nbThreads						number of standard threads.
     * @param nbSchedulableThreads			number of schedulable threads.
     * @throws Exception					<i>to do</i>.
     */
    protected ElectricMeter(
            String electricMeterInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest,
            int nbThreads,
            int nbSchedulableThreads
    ) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, nbThreads, nbSchedulableThreads);
        this.initialise(electricMeterInboundPortURI, simArchitectureURI,
                executesAsUnitTest);
    }

    /**
     * Create an electric meter component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * pre	{@code electricMeterInboundPortURI != null}
     * pre	{@code !electricMeterInboundPortURI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * pre	{@code nbThreads >= 0 && nbSchedulableThreads >= 0}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param reflectionInboundPortURI		URI of the reflection innbound port of the component.
     * @param electricMeterInboundPortURI	URI of the electric meter inbound port.
     * @param simArchitectureURI			URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest			true if the component executes as a unit test, false otherwise.
     * @param nbThreads						number of standard threads.
     * @param nbSchedulableThreads			number of schedulable threads.
     * @throws Exception					<i>to do</i>.
     */
    protected ElectricMeter(
            String reflectionInboundPortURI,
            String electricMeterInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest,
            int nbThreads,
            int nbSchedulableThreads
    ) throws Exception {
        super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);
        this.initialise(electricMeterInboundPortURI, simArchitectureURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise an electric meter component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code electricMeterInboundPortURI != null}
     * pre	{@code !electricMeterInboundPortURI.isEmpty()}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param electricMeterInboundPortURI	URI of the electric meter inbound port.
     * @throws Exception					<i>to do</i>.
     */
    protected void initialise(
            String electricMeterInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        // Assert the URI consistence
        assert electricMeterInboundPortURI != null : new PreconditionException(
                "electricMeterInboundPortURI != null");
        assert !electricMeterInboundPortURI.isEmpty(): new PreconditionException(
                "!electricMeterInboundPortURI.isEmpty()");

        // Initialise the component
        this.simArchitectureURI = simArchitectureURI;
        this.executesAsUnitTest = executesAsUnitTest;
        this.isSILsimulated = !simArchitectureURI.isEmpty();
        this.emip = new ElectricMeterInboundPort(electricMeterInboundPortURI, this);
        this.emip.publishPort();

        // Create the trace
        if (VERBOSE) {
            this.tracer.get().setTitle("Electric meter component");
            this.tracer.get().setRelativePosition(2, 1);
            this.toggleTracing();
        }
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        this.traceMessage("Electric meter starts.\n");

        if (this.isSILsimulated) {
            // create the simulator plug-in instance, attaching to it a
            // scheduled executor service allowing it to perform the
            // simulation steps in real time
            this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            this.simulatorPlugin = new ElectricMeterRTAtomicSimulatorPlugin();
            this.simulatorPlugin.setPluginURI(ElectricMeterCoupledModel.URI);
            this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
            try {
                // the plug-in is programmed to be able to create the
                // simulation architecture and initialise itself to be able
                // to perform the simulations
                this.simulatorPlugin.initialiseSimulationArchitecture(
                        this.simArchitectureURI,
                        this.executesAsUnitTest ?
                                ACC_FACTOR
                                :	CVM_SIL.ACC_FACTOR);
                // lastly, install the plug-in on the component
                this.installPlugin(this.simulatorPlugin);
            } catch (Exception e) {
                throw new ComponentStartException(e) ;
            }
        }
    }


    /** @see fr.sorbonne_u.components.AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        if (this.executesAsUnitTest) {
            this.simulatorPlugin.setSimulationRunParameters(new HashMap<String, Object>());
            long simStart = System.currentTimeMillis() + 1000L;
            this.simulatorPlugin.startRTSimulation(simStart, 0.0, 10.0);
            this.traceMessage("real time if start = " + simStart + "\n");

            /* Test scenario: code executions are scheduled to happen during
            the simulation; SIL simulations execute in real time
            (possibly accelerated) so that, when correctly scheduled, code
            execution can occur on the same time reference in order to get
            coherent exchanges between the two */
            final ElectricMeterRTAtomicSimulatorPlugin sp = this.simulatorPlugin;

            this.scheduleTask(
                    AbstractComponent.STANDARD_SCHEDULABLE_HANDLER_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                // Trigger the SwitchOnOven event
                                sp.triggerExternalEvent(
                                        ElectricMeterRTAtomicSimulatorPlugin.
                                                OVEN_ELECTRICITY_MODEL_URI,
                                        t -> new SwitchOnOven(t, 200));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    (long)(2.0/ACC_FACTOR),
                    TimeUnit.SECONDS);

            this.scheduleTask(
                    AbstractComponent.STANDARD_SCHEDULABLE_HANDLER_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                // Trigger the SwitchOffOven event
                                sp.triggerExternalEvent(
                                        ElectricMeterRTAtomicSimulatorPlugin.
                                                OVEN_ELECTRICITY_MODEL_URI,
                                        t -> new SwitchOffOven(t));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    (long)(12.0/ACC_FACTOR),
                    TimeUnit.SECONDS);
        }
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.emip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e) ;
        }
        super.shutdown();
    }

    /** @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterImplementationI#getCurrentConsumption() */
    @Override
    public double getCurrentConsumption() throws Exception {
        if (VERBOSE) {
            this.traceMessage("Electric meter returns is current consumption.\n");
        }
        // TODO will need a computation.
        return 0;
    }

    /** @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterImplementationI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        if (VERBOSE) {
            this.traceMessage("Electric meter returns is current production.\n");
        }
        // TODO will need a computation.
        return 0;
    }
}
// -----------------------------------------------------------------------------
