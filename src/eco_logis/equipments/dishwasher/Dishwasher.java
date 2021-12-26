package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

public class Dishwasher
    extends AbstractComponent
    implements DishwasherImplementationI
{

    // ========== Macros ==========


    /** The string representing the default dishwasher inbound port URI */
    public static final String INBOUND_PORT_URI = "DISHWASHER-INBOUND-PORT-URI";

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
     * @throws Exception TODO
     */
    protected Dishwasher() throws Exception {
        this(INBOUND_PORT_URI);
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
     * @param dishwasherInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected Dishwasher(String dishwasherInboundPortURI) throws Exception {
        super(1, 0);
        initialise(dishwasherInboundPortURI);
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
     * @param reflectionInboundPortURI The reflection inbound port URI
     * @param dishwasherInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected Dishwasher(String reflectionInboundPortURI, String dishwasherInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(dishwasherInboundPortURI);
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
     * @throws Exception TODO
     */
    protected void initialise(String dishwasherInboundPortURI) throws Exception {
        assert dishwasherInboundPortURI != null : new PreconditionException(
                "dishwasherInboundPortURI != null");
        assert !dishwasherInboundPortURI.isEmpty() : new PreconditionException(
                "!dishwasherInboundPortURI.isEmpty()");

        // Initialise the component
        isPlanned = false;
        isWashing = false;
        program = null;
        if(dishwasherProgramDuration.isEmpty()) initProgamDurations();

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

    private static void initProgamDurations() {
        dishwasherProgramDuration.put(DishwasherProgram.FULL, Duration.ofMinutes(160));
        dishwasherProgramDuration.put(DishwasherProgram.ECO, Duration.ofMinutes(180));
        dishwasherProgramDuration.put(DishwasherProgram.FAST, Duration.ofMinutes(100));
        dishwasherProgramDuration.put(DishwasherProgram.RINSE, Duration.ofMinutes(15));
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
        assert program != null;

        if(VERBOSE) {
            logMessage("Dishwasher get program duration: " + dishwasherProgramDuration.get(program));
        }

        assert isPlanned;

        return dishwasherProgramDuration.get(program);
    }

    /** @see DishwasherImplementationI#getDeadline() */
    @Override
    public LocalTime getDeadline() throws Exception {
        assert deadline != null;

        if(VERBOSE) {
            logMessage("Dishwasher get deadline: " + deadline);
        }

        assert isPlanned;

        return deadline;
    }

    /** @see DishwasherImplementationI#getStartTime() */
    @Override
    public LocalTime getStartTime() throws Exception {
        assert startingTime != null;

        if(VERBOSE) {
            logMessage("Dishwasher get starting time: " + startingTime);
        }

        assert isPlanned;

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

        assert !this.isPlanned;
        assert this.program == null;

        this.isPlanned = true;
        this.deadline = deadline;
        this.program = DishwasherProgram.FULL;

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

        assert !this.isPlanned;
        assert this.program == null;

        this.isPlanned = true;
        this.deadline = deadline;
        this.program = program;

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
            assert isPlanned;
            assert program != null;
            assert deadline != null;

            isPlanned = false;
            program = null;
            deadline = null;
        }

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
    public void startWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher starts washing dishes");
        }

        assert isPlanned && !isWashing;
        assert program != null;

        isWashing = true;
    }

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
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
    }

}
