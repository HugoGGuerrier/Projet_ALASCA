package equipments.dishwasher;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.exceptions.PreconditionException;

public class Dishwasher
    extends AbstractComponent
    implements DishwasherImplementationI
{

    // ========== Macros ==========

    public static final String INBOUND_PORT_URI = "DISHWASHER-INBOUND-PORT-URI";
    public static final boolean VERBOSE = true;

    // ========== Attributes ==========

    private DishwasherProgram program;
    private boolean isWashing;
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

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher get washing : " + isWashing);
        }

        return isWashing;
    }

    /** @see DishwasherImplementationI#startWasherFull() */
    @Override
    public void startWasherFull() throws Exception {
        if(VERBOSE) {
            logMessage("Start the washer in full program");
        }

        assert !isWashing;
        assert program == null;

        isWashing = true;
        program = DishwasherProgram.FULL;
    }

    /** @see DishwasherImplementationI#startWasherEco() */
    @Override
    public void startWasherEco() throws Exception {
        if(VERBOSE) {
            logMessage("Start the washer in eco program");
        }

        assert !isWashing;
        assert program == null;

        isWashing = true;
        program = DishwasherProgram.ECO;
    }

    /** @see DishwasherImplementationI#startWasherFast() */
    @Override
    public void startWasherFast() throws Exception {
        if(VERBOSE) {
            logMessage("Start the washer in fast program");
        }

        assert !isWashing;
        assert program == null;

        isWashing = true;
        program = DishwasherProgram.FAST;
    }

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher stop washing");
        }

        assert isWashing;

        isWashing = false;
        program = null;
    }

    /** @see DishwasherImplementationI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        if(VERBOSE) {
            logMessage("Dishwasher get program : " + program);
        }

        assert isWashing;
        assert program != null;

        return program;
    }

}
