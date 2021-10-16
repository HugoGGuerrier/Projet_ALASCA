package equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * The class <code>Oven</code> implements the oven component.
 *
 * <p><strong>Description</strong></p>
 * <p>
 * The oven is an uncontrollable appliance, hence it does not connect
 * with the household energy manager (we don't want any control over the oven).
 * However, it will connect later to the electric panel to take its (simulated)
 * electricity consumption into account.
 * </p>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */

@OfferedInterfaces(offered = {OvenCI.class})
public class Oven
    extends AbstractComponent
    implements OvenImplementationI {

    // ========== Macros ==========

    /** URI of the oven inbound port used in tests */
    public static final String INBOUND_PORT_URI = "OVEN-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    /** Initial state of the oven (false -> OFF) */
    public static final boolean INITIAL_STATE = false;

    // ========== Attributes ==========

    /** Temperature of the oven, Celsius degrees (°C) */
    private double temperature;

    /** Inbound port offering the <code>OvenCI</code> interface */
    private OvenInboundPort ovenInboundPort;

    /** State of the oven : baking (on) or not (off) */
    private boolean isBaking;

    // ========== Constructors ==========

    /**
     * Create an oven component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post true
     * </pre>
     *
     * @throws Exception
     */
    protected Oven() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create an oven component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param ovenInboundPortURI URI of the oven inbound port.
     * @throws Exception
     */
    protected Oven(String ovenInboundPortURI) throws Exception {
        super(1, 0);
        this.initialise(ovenInboundPortURI);
    }

    /**
     * Create a new oven with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param ovenInboundPortURI The inbound port URI
     * @throws Exception
     */
    protected Oven(String reflectionInboundPortURI, String ovenInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(ovenInboundPortURI);
    }

    // ========== Class methods ==========

    /**
     * Initialise the newly created oven
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * post {@code !isBaking}
     * post {@code temperature == 0.0}
     * post	{@code ovenInboundPort.isPublished()}
     * </pre>
     *
     * @param ovenInboundPortURI The oven inbound port URI
     * @throws Exception
     */
    protected void initialise(String ovenInboundPortURI) throws Exception {
        // Assert the URI consistence
        assert ovenInboundPortURI != null : new PreconditionException("ovenInboundPortURI != null");
        assert !ovenInboundPortURI.isEmpty() : new PreconditionException("!ovenInboundPortURI.isEmpty()");

        // Initialise the component
        this.temperature = 0.0;
        this.isBaking = INITIAL_STATE;

        // Create the inbound port
        this.ovenInboundPort = new OvenInboundPort(ovenInboundPortURI, this);
        this.ovenInboundPort.publishPort();

        // Create the trace
        if (Oven.VERBOSE) {
            this.tracer.get().setTitle("Oven component");
            this.tracer.get().setRelativePosition(1, 0);
            this.toggleTracing();
        }
    }

    // ========== Override methods ==========

    /** @see OvenImplementationI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        if(Oven.VERBOSE) {
            logMessage("Oven is baking : " + isBaking);
        }
        return isBaking;
    }

    /** @see OvenImplementationI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        if(Oven.VERBOSE) {
            logMessage("Oven is turned on, starts baking");
        }
        assert !isBaking;
        isBaking = true;
    }

    /** @see OvenImplementationI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        if(Oven.VERBOSE) {
            logMessage("Oven is turned off, stops baking. The cake is ready !");
        }
        assert isBaking;
        isBaking = false;
    }

    /** @see OvenImplementationI#getTemperature() */
    @Override
    public double getTemperature() {
        if(Oven.VERBOSE) {
            logMessage("Oven temperature is : " + temperature + "°C");
        }
        assert isBaking;
        return temperature;
    }

    /** @see OvenImplementationI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) {
        if(Oven.VERBOSE) {
            logMessage("Oven temperature is set to " + temp + "°C");
        }
        assert isBaking;
        this.temperature = temp;
    }
}
