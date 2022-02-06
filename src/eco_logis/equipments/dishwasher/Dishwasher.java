package eco_logis.equipments.dishwasher;

import eco_logis.CVM_SIL;
import eco_logis.equipments.crypto_miner.CryptoMinerCI;
import eco_logis.equipments.dishwasher.mil.DishwasherCoupledModel;
import eco_logis.equipments.dishwasher.mil.events.*;
import eco_logis.equipments.dishwasher.sil.DishwasherStateModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represent a dishwasher which can be planned with different programs and be canceled
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@OfferedInterfaces(offered = {DishwasherCI.class})
public class Dishwasher
    extends AbstractCyPhyComponent
    implements DishwasherImplementationI
{

    // ========== Macros ==========


    /** The string representing the default dishwasher inbound port URI */
    public static final String INBOUND_PORT_URI = "DISHWASHER-INBOUND-PORT-URI";

    /** The URI of the reflection inbound port */
    public static final String REFLECTION_INBOUND_PORT_URI = "DISHWASHER_rip";

    /** Trace all actions on the component if true */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** If the dishwasher is currently planned */
    private boolean isPlanned;

    /** If the dishwasher is currently washing something */
    private boolean isWashing;

    /** The current dishwasher program */
    private DishwasherProgram program;

    /** Each program is associated with a duration */
    public final static Map<DishwasherProgram, Duration> dishwasherProgramDuration = new EnumMap<>(DishwasherProgram.class);

    /** The time the dishwasher should start */
    private LocalTime startingTime;

    /** The washing deadline */
    private LocalTime deadline;

    /** The dishwasher inbound port */
    private DishwasherInboundPort dwip;

    // --- For the SIL simulation

    /** The URI of the created simulation architecture */
    protected String simArchURI;

    /** The URI of the executor */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

    /** The plugin that contains the SIL simulation */
    protected DishwasherRTAtomicSimulatorPlugin simulatorPlugin;

    /** If the component is in a SIL simulation */
    protected boolean isSILSimulated;

    /** If the component executes a unit test */
    protected boolean executesAsUnitTest;

    /** Acceleration factor for the simulation */
    protected static final double ACC_FACT = 1.0;


    // ========== Constructors ==========


    /**
     * Create a new dishwasher
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @throws Exception TODO
     */
    protected Dishwasher(String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(INBOUND_PORT_URI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new dishwasher with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code dishwasherInboundPortURI != null}
     * pre	{@code !dishwasherInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(int, int)
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param dishwasherInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected Dishwasher(String dishwasherInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        initialise(dishwasherInboundPortURI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new dishwasher with the wanted inbound port URI and reflection port uri
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code dishwasherInboundPortURI != null}
     * pre	{@code !dishwasherInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(String, int, int)
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param dishwasherInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected Dishwasher(String reflectionInboundPortURI, String dishwasherInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(dishwasherInboundPortURI, simArchURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise the newly created dishwasher
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code dishwasherInboundPortURI != null}
     * pre	{@code !dishwasherInboundPortURI.isEmpty()}
     * post {@code !isWashing}
     * post	{@code dwip.isPublished()}
     * </pre>
     *
     * @param dishwasherInboundPortURI The dishwasher inbound port URI
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @throws Exception TODO
     */
    protected void initialise(
            String dishwasherInboundPortURI,
            String simArchURI,
            boolean executesAsUnitTest
    ) throws Exception {
        assert dishwasherInboundPortURI != null : new PreconditionException(
                "dishwasherInboundPortURI != null");
        assert !dishwasherInboundPortURI.isEmpty() : new PreconditionException(
                "!dishwasherInboundPortURI.isEmpty()");

        // Initialise the component
        isPlanned = false;
        isWashing = false;
        program = null;
        if(dishwasherProgramDuration.isEmpty()) initProgramDurations();

        // Initialise the SIL simulation
        this.simArchURI = simArchURI;
        this.isSILSimulated = !simArchURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Create the inbound port
        dwip = new DishwasherInboundPort(dishwasherInboundPortURI, this);
        dwip.publishPort();

        // Create the trace
        if(VERBOSE) {
            tracer.get().setTitle("Dishwasher component");
            tracer.get().setRelativePosition(1, 0);
            toggleTracing();
        }
    }

    private static void initProgramDurations() {
        dishwasherProgramDuration.put(DishwasherProgram.FULL, Duration.ofMinutes(160));
        dishwasherProgramDuration.put(DishwasherProgram.ECO, Duration.ofMinutes(180));
        dishwasherProgramDuration.put(DishwasherProgram.FAST, Duration.ofMinutes(100));
        dishwasherProgramDuration.put(DishwasherProgram.RINSE, Duration.ofMinutes(15));
    }


    // ========== Component lifecycle ==========


    /** @see AbstractCyPhyComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        // Call super
        super.start();

        // Trace
        this.traceMessage("Dishwasher starts.\n");

        if(isSILSimulated) {
            createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            simulatorPlugin = new DishwasherRTAtomicSimulatorPlugin();
            simulatorPlugin.setPluginURI(DishwasherCoupledModel.URI);
            simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
            try {

                simulatorPlugin.initialiseSimulationArchitecture(simArchURI, executesAsUnitTest ? ACC_FACT : CVM_SIL.ACC_FACTOR);
                installPlugin(simulatorPlugin);

            } catch (Exception e) {
                throw new ComponentStartException(e) ;
            }
        }
    }

    /** @see AbstractCyPhyComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        if(executesAsUnitTest) {
            simulatorPlugin.setSimulationRunParameters(new HashMap<>());
            long startTime = System.currentTimeMillis() + 1000L;
            double endTime = 10.0 / ACC_FACT;
            simulatorPlugin.startRTSimulation(startTime, 0.0, endTime);
            this.traceMessage("real time of start = " + startTime + "\n");
        }
    }

    /** @see AbstractCyPhyComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        this.traceMessage("Dishwasher stops.\n");

        try {
            dwip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }


    // ========== Override methods ==========


    /** @see DishwasherImplementationI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        assert program != null;

        if(VERBOSE) {
            logMessage("Dishwasher get program: " + program);
        }

        assert isPlanned;

        return program;
    }


    /** @see DishwasherImplementationI#getProgramDuration() */
    @Override
    public Duration getProgramDuration() throws Exception {
        assert isPlanned;
        assert program != null;

        if(VERBOSE) {
            logMessage("Dishwasher get program duration: " + dishwasherProgramDuration.get(program));
        }

        return dishwasherProgramDuration.get(program);
    }

    /** @see DishwasherImplementationI#getDeadline() */
    @Override
    public LocalTime getDeadline() throws Exception {
        assert deadline != null;
        assert isPlanned;

        if(VERBOSE) {
            logMessage("Dishwasher get deadline: " + deadline);
        }

        return deadline;
    }

    /** @see DishwasherImplementationI#getStartTime() */
    @Override
    public LocalTime getStartTime() throws Exception {
        assert startingTime != null;
        assert isPlanned;

        if(VERBOSE) {
            logMessage("Dishwasher get starting time: " + startingTime);
        }

        return startingTime;
    }

    /** @see DishwasherImplementationI#isPlanned() */
    @Override
    public boolean isPlanned() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher is planned: " + isPlanned);
        }
        return isPlanned;
    }

    /** @see DishwasherImplementationI#plan(LocalTime) */
    @Override
    public boolean plan(LocalTime deadline) throws Exception {
        assert deadline != null;

        if(VERBOSE) {
            logMessage("Dishwasher planned (default prog), must be ready for " + deadline);
        }

        assert !this.isPlanned: new PreconditionException("plan(...) -> !isPlanned()");
        assert this.program == null;

        this.isPlanned = true;
        this.deadline = deadline;
        this.program = DishwasherProgram.FULL;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SetFullProgram::new);

        return true;
    }

    /** @see DishwasherImplementationI#plan(LocalTime, DishwasherProgram) */
    @Override
    public boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception {
        assert deadline != null;
        assert program != null;

        if(VERBOSE) {
            logMessage("Dishwasher planned (defined prog), must be ready for " + deadline);
        }

        assert !this.isPlanned: new PreconditionException("plan(...) -> !isPlanned()");
        assert this.program == null;

        this.isPlanned = true;
        this.deadline = deadline;
        this.program = program;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) {
            switch (program) {
                case ECO:
                    simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SetEcoProgram::new);
                    break;
                case FAST:
                    simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SetFastProgram::new);
                    break;
                case FULL:
                    simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SetFullProgram::new);
                    break;
                case RINSE:
                    simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SetRinseProgram::new);
                    break;
                default:
            }
        }

        return true;
    }


    /** @see DishwasherImplementationI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher cancels its program (stops washing if it was washing)");
        }

        if(isWashing) {
            this.stopWashing();

        } else {
            assert isPlanned : new PreconditionException("cancel() -> isPlanned()");
            assert program != null;
            assert deadline != null;

            isPlanned = false;
            program = null;
            deadline = null;
        }

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(DishwasherStateModel.URI, SwitchOffDishwasher::new);

        return true;
    }

    /** @see DishwasherImplementationI#postPone(Duration) () */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        assert duration != null;
        assert startingTime != null;

        LocalTime newStartingTime = startingTime.plusSeconds(duration.getSeconds());

        if(VERBOSE) {
            logMessage("Dishwasher postpones its starting time of: " + duration + ", will start at: " + newStartingTime + "(old was: " + startingTime + ")");
        }

        // Verify that the new starting time will not make the washing exceed the deadline
        assert deadline.isAfter(newStartingTime.plusMinutes(dishwasherProgramDuration.get(program).toMinutes()));

        startingTime = newStartingTime;

        return true;
    }

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher is washing: " + isWashing);
        }

        return isWashing;
    }


    /** @see DishwasherImplementationI#startWashing() */
    @Override
    public boolean startWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher starts washing dishes");
        }

        assert isPlanned && !isWashing;
        assert program != null;

        isWashing = true;

        return true;
    }

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    public boolean stopWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher stops washing");
        }

        assert isWashing && isPlanned;
        assert program != null;
        assert deadline != null;

        isPlanned = false;
        isWashing = false;
        program = null;
        deadline = null;

        return true;
    }

}
