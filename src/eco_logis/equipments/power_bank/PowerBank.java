package eco_logis.equipments.power_bank;

import eco_logis.CVM_SIL;
import eco_logis.equipments.power_bank.mil.PowerBankCoupledModel;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import eco_logis.equipments.power_bank.sil.PowerBankStateModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;

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
    extends AbstractCyPhyComponent
    implements PowerBankImplementationI
{

    // ========== Macros ==========


    /** The enum that represent the current power bank state */
    public enum State {
        CHARGING,
        DISCHARGING,
        STANDBY
    }

    /** URI of the power bank inbound port used in tests */
    public static final String INBOUND_PORT_URI = "POWERBANK-INBOUND-PORT-URI";

    /** The URI of the reflection inbound port */
    public static final String REFLECTION_INBOUND_PORT_URI = "POWER_BANK_rip";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** The percentage of the power bank capacity that is filled with energy (0=empty, 100=full) */
    private double batteryLevel;

    /** The current state of the power bank */
    private State currentState;

    /** Inbound port offering the <code>PowerBankCI</code> interface */
    private PowerBankInboundPort pbip;

    // --- For the SIL simulation

    /** The URI of the created simulation architecture */
    protected String simArchURI;

    /** The URI of the executor */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

    /** The plugin that contains the SIL simulation */
    protected PowerBankRTAtomicSimulatorPlugin simulatorPlugin;

    /** If the component is in a SIL simulation */
    protected boolean isSILSimulated;

    /** If the component executes a unit test */
    protected boolean executesAsUnitTest;

    /** Acceleration factor for the simulation */
    protected static final double ACC_FACT = 1.0;


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
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @throws Exception TODO
     */
    protected PowerBank(String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(INBOUND_PORT_URI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a power bank component
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param powerBankInboundPortURI URI of the power bank inbound port.
     * @throws Exception <i>to do</i>.
     */
    protected PowerBank(String powerBankInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(REFLECTION_INBOUND_PORT_URI, powerBankInboundPortURI, simArchURI, executesAsUnitTest);
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
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param powerBankInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected PowerBank(String reflectionInboundPortURI, String powerBankInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(powerBankInboundPortURI, simArchURI, executesAsUnitTest);
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
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param powerBankInboundPortURI The power bank inbound port URI
     * @throws Exception TODO
     */
    protected void initialise(String powerBankInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        // Assert the URI consistence
        assert powerBankInboundPortURI != null : new PreconditionException(
                "powerBankInboundPortURI != null");
        assert !powerBankInboundPortURI.isEmpty() : new PreconditionException(
                "!powerBankInboundPortURI.isEmpty()");

        assert simArchURI != null;
        assert !simArchURI.isEmpty() || !executesAsUnitTest;

        // Initialise the component
        currentState = State.STANDBY;
        batteryLevel = 0.0;

        // Initialise the SIL simulation
        this.simArchURI = simArchURI;
        this.isSILSimulated = !simArchURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Create the inbound port
        pbip = new PowerBankInboundPort(powerBankInboundPortURI, this);
        pbip.publishPort();

        // Create the trace
        if(PowerBank.VERBOSE) {
            tracer.get().setTitle("Power bank component");
            tracer.get().setRelativePosition(0, 1);
            toggleTracing();
        }
    }


    // ========== Component lifecycle ==========


    /** @see AbstractCyPhyComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        // Call super
        super.start();

        // Trace
        this.traceMessage("Power bank starts.\n");

        if(isSILSimulated) {
            createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            simulatorPlugin = new PowerBankRTAtomicSimulatorPlugin();
            simulatorPlugin.setPluginURI(PowerBankCoupledModel.URI);
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
        this.traceMessage("Power bank stops.\n");

        try {
            pbip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }


    // ========== Override methods ==========


    /** @see PowerBankImplementationI#getCurrentState() */
    public State getCurrentState() {
        return currentState;
    }

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Start charging the power bank (current charge = " + batteryLevel + ")");
        }
        assert currentState == State.STANDBY;

        currentState = State.CHARGING;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(PowerBankStateModel.URI, ChargePowerBank::new);
    }

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Start discharging the power bank (current charge = " + batteryLevel + ")");
        }
        assert currentState == State.STANDBY;

        currentState = State.DISCHARGING;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(PowerBankStateModel.URI, DischargePowerBank::new);
    }

    /** @see PowerBankImplementationI#standBy() */
    @Override
    public void standBy() throws Exception {
        if(PowerBank.VERBOSE) {
            logMessage("Set the power bank to standby (current charge = " + batteryLevel + ")");
        }
        assert currentState == State.CHARGING || currentState == State.DISCHARGING;

        currentState = State.STANDBY;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(PowerBankStateModel.URI, StandbyPowerBank::new);
    }

    /** @see PowerBankImplementationI#getBatteryLevel() */
    @Override
    public double getBatteryLevel() throws Exception {
        return batteryLevel;
    }

}
