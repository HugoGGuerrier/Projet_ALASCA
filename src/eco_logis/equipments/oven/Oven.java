package equipments.oven;

import equipments.crypto_miner.CryptoMinerImplementationI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * The class <code>Oven</code> implements the oven component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * The oven is an uncontrollable appliance, hence it does not connect
 * with the household energy manager (we don't want any control over the oven).
 * However, it will connect later to the electric panel to take its (simulated)
 * electricity consumption into account.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */

//@OfferedInterfaces(offered = {OvenCI.class})
public class Oven extends AbstractComponent implements OvenImplementationI {

    // ========== Macros ==========

    /** URI of the oven inbound port used in tests */
    public static final String INBOUND_PORT_URI = "OVEN-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    /** Initial state of the oven (false -> OFF) */
    public static final boolean INITIAL_STATE = false;

    // ========== Attributes ==========

    /** Temperature of the oven, celsius degrees (°C) */
    private double temperature;

    /** Inbound port offering the <code>OvenCI</code> interface */
    private OvenInboundPort ovenInboundPort;

    /** State of the oven : ON or OFF */
    private boolean isBaking;

    // ========== Constructors ==========

    /**
    * Create an oven component
     *
    * <p><strong>Contract</strong></p>
    *
    * <pre>
    * pre	{@code INBOUND_PORT_URI != null}
    * pre	{@code !INBOUND_PORT_URI.isEmpty()}
    * post	{@code getState() == OvenState.OFF}
    * post	{@code getTemperature() == 0.0}
    * </pre>
    *
    * @throws Exception TODO
    */
    protected Oven() throws Exception {
        super(1, 0);
        this.initialise(INBOUND_PORT_URI);
    }

    /**
     * Create an oven component.
     *
     * @param ovenInboundPortURI URI of the hair dryer inbound port.
     * @throws Exception <i>to do</i>.
     */
    protected Oven(String ovenInboundPortURI) throws Exception {
        super(1, 0);
        this.initialise(ovenInboundPortURI);
    }

    // ========== Class methods ==========


    protected void initialise(String ovenInboundPortURI) throws Exception {
        assert ovenInboundPortURI != null : new PreconditionException("hairDryerInboundPortURI != null");
        assert !ovenInboundPortURI.isEmpty() : new PreconditionException("!hairDryerInboundPortURI.isEmpty()");

        this.temperature = 0.0;
        this.isBaking = INITIAL_STATE;
        // TODO
        //this.ovenInboundPort = new OvenInboundPort(ovenInboundPortURI, this);
        //this.ovenInboundPort.publishPort();

        if (Oven.VERBOSE) {
            this.tracer.get().setTitle("Oven component");
            this.tracer.get().setRelativePosition(1, 0);
            this.toggleTracing();
        }
    }

    // ========== Override methods ==========

    /** @see OvenImplementationI#isBaking() */
    @Override
    public boolean isBaking() {
        if(Oven.VERBOSE) {
            traceMessage("Oven returns if it is baking : " + this.isBaking + ".\n");
        }
        return isBaking;
    }

    /** @see OvenImplementationI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        if(Oven.VERBOSE) {
            traceMessage("");
        }
        isBaking = true;
    }

    /** @see OvenImplementationI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        if(Oven.VERBOSE) {
            traceMessage("");
        }
        isBaking = false;
    }

    /** @see OvenImplementationI#getTemperature() */
    @Override
    public double getTemperature() {
        if(Oven.VERBOSE) {
            traceMessage("");
        }
        return temperature;
    }

    /** @see OvenImplementationI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) {
        if(Oven.VERBOSE) {
            traceMessage("");
        }
        this.temperature = temp;
    }
}
