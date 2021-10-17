package equipments.power_bank;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * The class <code>PowerBank</code> implements the power bank component.
 *
 * <p><strong>Description</strong></p>
 * <p>
 * The power bank can stock energy.
 * </p>
 *
 * <p>Created on : 2021-10-16</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@OfferedInterfaces(offered = {PowerBankCI.class})
public class PowerBank
    extends AbstractComponent
    implements PowerBankImplementationI {

    // ========== Macros ==========

    /** URI of the power bank inbound port used in tests */
    public static final String INBOUND_PORT_URI = "POWERBANK-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    // ========== Attributes ==========

    /** The percentage of the power bank capacity that is filled with energy (0=empty, 100=full) */
    private double batteryLevel;

    /** Charging state of the battery : charging (true) or not (false) */
    private boolean isCharging;

    /** Discharging state of the battery : discharging (true) or not (false) */
    private boolean isDischarging;

    /** Inbound port offering the <code>PowerBankCI</code> interface */
    private PowerBankInboundPort powerBankInboundPort;

    // ========== Constructors ==========

    /**
     * Create a power bank
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
    protected PowerBank() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create a power bank component
     *
     * @param powerBankInboundPortURI URI of the power bank inbound port.
     * @throws Exception <i>to do</i>.
     */
    protected PowerBank(String powerBankInboundPortURI) throws Exception {
        super(1, 0);
        this.initialise(powerBankInboundPortURI);
    }

    /**
     * Create a new power bank with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code powerBankInboundPortURI != null}
     * pre	{@code !powerBankInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param powerBankInboundPortURI The inbound port URI
     * @throws Exception
     */
    protected PowerBank(String reflectionInboundPortURI, String powerBankInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(powerBankInboundPortURI);
    }

    // ========== Class methods ==========

    /**
     * Initialise the newly created power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code powerBankInboundPortURI != null}
     * pre	{@code !powerBankInboundPortURI.isEmpty()}
     * post {@code batteryLevel == 0.0}
     * post {@code !isCharging}
     * post {@code !isDischarging}
     * post	{@code powerBankInboundPort.isPublished()}
     * </pre>
     *
     * @param powerBankInboundPortURI The power bank inbound port URI
     * @throws Exception
     */
    protected void initialise(String powerBankInboundPortURI) throws Exception {
        // Assert the URI consistence
        assert powerBankInboundPortURI != null : new PreconditionException(
                "powerBankInboundPortURI != null");
        assert !powerBankInboundPortURI.isEmpty() : new PreconditionException(
                "!powerBankInboundPortURI.isEmpty()");

        // Initialise the component
        isCharging = false;
        isDischarging = false;
        batteryLevel = 0.0;

        // Create the inbound port
        powerBankInboundPort = new PowerBankInboundPort(powerBankInboundPortURI, this);
        powerBankInboundPort.publishPort();

        // Create the trace
        if(PowerBank.VERBOSE) {
            tracer.get().setTitle("Power bank component");
            tracer.get().setRelativePosition(0, 0);
            toggleTracing();
        }
    }

    // ========== Override methods ==========

    /** @see PowerBankImplementationI#isCharging() */
    @Override
    public boolean isCharging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Power bank is charging : " + isCharging);
        }
        return isCharging;
    }

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Start charging the power bank (current charge = " + batteryLevel + ")");
        }
        assert !isDischarging;
        assert !isCharging;
        isCharging = true;
    }

    /** @see PowerBankImplementationI#stopCharging() */
    @Override
    public void stopCharging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Stop charging the power bank (current charge = " + batteryLevel + ")");
        }
        assert isCharging;
        isCharging = false;
    }

    /** @see PowerBankImplementationI#isDischarging() */
    @Override
    public boolean isDischarging() throws Exception {
        return isDischarging;
    }

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Start discharging the power bank (current charge = " + batteryLevel + ")");
        }
        assert !isCharging;
        assert !isDischarging;
        isDischarging = true;
    }

    /** @see PowerBankImplementationI#stopDischarging() */
    @Override
    public void stopDischarging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Stop discharging the power bank (current charge = " + batteryLevel + ")");
        }
        assert isDischarging;
        isDischarging = false;
    }

    /** @see PowerBankImplementationI#getBatteryLevel() */
    @Override
    public double getBatteryLevel() throws Exception {
        return batteryLevel;
    }


}
