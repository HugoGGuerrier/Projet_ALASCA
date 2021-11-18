package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.time.Duration;
import java.time.LocalTime;
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
    private Map<DishwasherProgram, Duration> programDurationMap;

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


    // ========== Override methods ==========


    /** @see DishwasherImplementationI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher get program : " + program);
        }

        assert isPlanned || isWashing;
        assert program != null;

        return program;
    }

    /** @see DishwasherImplementationI#isPlanned() */
    @Override
    public boolean isPlanned() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher is planned : " + isWashing);
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

        assert !isPlanned && !isWashing;
        assert program == null;

        isPlanned = true;
        program = DishwasherProgram.FULL;

        return true;
    }

    /** @see DishwasherImplementationI#plan(LocalTime, DishwasherProgram) */
    @Override
    public boolean plan(LocalTime deadline, DishwasherProgram prog) throws Exception {
        assert deadline != null;
        assert prog != null;

        if(VERBOSE) {
            logMessage("Dishwasher planned (defined prog), must be ready for " + deadline);
        }

        assert !isPlanned && !isWashing;
        assert program == null;

        isPlanned = true;
        program = prog;

        return true;
    }

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher is washing : " + isWashing);
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
            logMessage("Dishwasher stop washing");
        }

        assert isWashing && isPlanned;
        assert program != null;

        isPlanned = false;
        isWashing = false;
        program = null;
    }

}
