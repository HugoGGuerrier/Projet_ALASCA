package equipments.wind_turbine;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * This class represent a wind turbine which can be blocked and unblocked
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@OfferedInterfaces(offered = {WindTurbineCI.class})
public class WindTurbine
    extends AbstractComponent
    implements WindTurbineImplementationI {

    // ========== Macros ==========

    /** URI of the wind turbine inbound port */
    public static final String INBOUND_PORT_URI = "WIND-TURBINE-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    // ========== Attributes ==========

    /** If the turbine is currently turning (there is wind, it produces electricity) */
    private boolean isTurning;

    /** If the turbine is blocked (cases of strong winds) */
    private boolean isBlocked;

    /** The current electricity production from the wind turbine */
    private double currentProduction;

    /** The inbound port */
    private WindTurbineInboundPort windTurbineInboundPort;

    // ========== Constructors ==========

    /**
     * Create a new wind turbine
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post	true
     * </pre>
     *
     *
     * @throws Exception
     */
    protected WindTurbine() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create a new wind turbine with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(int, int)
     *
     * @param windTurbineInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected WindTurbine(String windTurbineInboundPortURI) throws Exception {
        super(1, 0);
        initialise(windTurbineInboundPortURI);
    }

    /**
     * Create a new wind turbine with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(String, int, int)
     *
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param windTurbineInboundPortURI The wind turbine inbound port URI
     * @throws Exception
     */
    protected WindTurbine(String reflectionInboundPortURI, String windTurbineInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(windTurbineInboundPortURI);
    }

    // ========== Class methods ==========

    /**
     * Initialise the newly created wind turbine
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
     * post {@code !isMining}
     * post	{@code cmip.isPublished()}
     * </pre>
     *
     * @param windTurbineInboundPortURI The wind turbine inbound port URI
     * @throws Exception TODO
     */
    protected void initialise(String windTurbineInboundPortURI) throws Exception {
        // Assert the URI consistence
        assert windTurbineInboundPortURI != null : new PreconditionException(
                "windTurbineInboundPortURI != null");
        assert !windTurbineInboundPortURI.isEmpty() : new PreconditionException(
                "!windTurbineInboundPortURI.isEmpty()");

        // Initialise the component
        isTurning = false;
        isBlocked = false;
        currentProduction = 0.0;

        // Create the inbound port
        windTurbineInboundPort = new WindTurbineInboundPort(windTurbineInboundPortURI, this);
        windTurbineInboundPort.publishPort();

        // Create the trace
        if(WindTurbine.VERBOSE) {
            tracer.get().setTitle("Wind turbine component");
            tracer.get().setRelativePosition(0, 0);
            toggleTracing();
        }
    }

    // ========== Override methods ==========

    /** @see WindTurbineImplementationI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine is turning : " + isTurning);
        }

        return isTurning;
    }

    /** @see WindTurbineImplementationI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine is blocked : " + isBlocked);
        }

        return isBlocked;
    }

    /** @see WindTurbineImplementationI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine current electricity production = " + currentProduction);
        }

        return currentProduction;
    }

    /** @see WindTurbineImplementationI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Block the wind turbine");
        }

        assert !isBlocked;

        isBlocked = true;
    }

    /** @see WindTurbineImplementationI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Unblock the wind turbine");
        }

        assert isBlocked;
        assert !isTurning;

        isBlocked = false;
    }
}
