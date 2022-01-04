package eco_logis.equipments.crypto_miner;

import eco_logis.CVM_SIL;
import eco_logis.equipments.crypto_miner.mil.CryptoMinerCoupledModel;
import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.crypto_miner.sil.CryptoMinerStateModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;

/**
 * This class represent a crypto-currency miner which can be powered on and off
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@OfferedInterfaces(offered = {CryptoMinerCI.class})
public class CryptoMiner
    extends AbstractCyPhyComponent
    implements CryptoMinerImplementationI
{

    // ========== Macros ==========


    /** URI of the crypto miner inbound port */
    public static final String INBOUND_PORT_URI = "CRYPTO-INBOUND-PORT-URI";

    /** The URI of the reflection inbound port */
    public static final String REFLECTION_INBOUND_PORT_URI = "CRYPTO_MINER_rip";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** If the crypto miner is currently on */
    private boolean isOn;

    /** If the miner is currently mining crypto-currency */
    private boolean isMining;

    /** The inbound port */
    private CryptoMinerInboundPort cmip;

    // --- For the SIL simulation

    /** The URI of the created simulation architecture */
    protected String simArchURI;

    /** The URI of the executor */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

    /** The prugin that contains the SIL simulation */
    protected CryptoMinerRTAtomicSimulatorPlugin simulatorPlugin;

    /** If the component is in a SIL simulation */
    protected boolean isSILSimulated;

    /** If the component executes a unit test */
    protected boolean executesAsUnitTest;

    /** Acceleration factor for the simulation */
    protected static final double ACC_FACT = 1.0;


    // ========== Constructors ==========


    /**
     * Create a new crypto miner
     *
     * <p><strong>Contract</strong></p>
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
    protected CryptoMiner(String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(INBOUND_PORT_URI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(int, int)
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String cryptoMinerInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        initialise(cryptoMinerInboundPortURI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
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
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String reflectionInboundPortURI, String cryptoMinerInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(cryptoMinerInboundPortURI, simArchURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise the newly created crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post {@code !isMining}
     * post	{@code cmip.isPublished()}
     * </pre>
     *
     * @param cryptoMinerInboundPortURI The crypto miner inbound port URI
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @throws Exception TODO
     */
    protected void initialise(String cryptoMinerInboundPortURI,
                              String simArchURI,
                              boolean executesAsUnitTest
    ) throws Exception {
        // Assert the URI consistence
        assert cryptoMinerInboundPortURI != null : new PreconditionException(
                "cryptoMinerInboundPortURI != null");
        assert !cryptoMinerInboundPortURI.isEmpty() : new PreconditionException(
                "!cryptoMinerInboundPortURI.isEmpty()");

        assert simArchURI != null;
        assert !simArchURI.isEmpty() || !executesAsUnitTest;

        // Initialise the component
        isOn = false;
        isMining = false;

        // Initialise the SIL simulation
        this.simArchURI = simArchURI;
        this.isSILSimulated = !simArchURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Create the inbound port
        cmip = new CryptoMinerInboundPort(cryptoMinerInboundPortURI, this);
        cmip.publishPort();

        // Create the trace
        if(CryptoMiner.VERBOSE) {
            tracer.get().setTitle("Crypto miner component");
            tracer.get().setRelativePosition(0, 0);
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
        this.traceMessage("Crypto miner starts.\n");

        if(isSILSimulated) {
            createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            simulatorPlugin = new CryptoMinerRTAtomicSimulatorPlugin();
            simulatorPlugin.setPluginURI(CryptoMinerCoupledModel.URI);
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
        this.traceMessage("Crypto miner stops.\n");

        try {
            cmip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }


    // ========== Override methods ==========


    /** @see CryptoMinerImplementationI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner get on : " + isOn);
        }

        return isOn;
    }

    /** @see CryptoMinerImplementationI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner power on");
        }

        assert !isOn : new PreconditionException("powerOn() -> !isOn()");

        isOn = true;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(CryptoMinerStateModel.URI, SwitchOnCryptoMiner::new);
    }

    /** @see CryptoMinerImplementationI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        if(VERBOSE) {
            logMessage("Crypto miner power off");
        }

        assert isOn : new PreconditionException("powerOff() -> isOn()");

        isOn = false;
        isMining = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(CryptoMinerStateModel.URI, SwitchOffCryptoMiner::new);
    }

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        if(CryptoMiner.VERBOSE) {
            logMessage("Crypto miner get mining : " + isMining);
        }

        return isMining;
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        if(CryptoMiner.VERBOSE) {
            logMessage("Start the crypto mining");
        }

        assert isOn : new PreconditionException("startMiner() -> isOn()");
        assert !isMining : new PreconditionException("startMiner() -> !isMining()");

        isMining = true;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(CryptoMinerStateModel.URI, MineOnCryptoMiner::new);
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        if(CryptoMiner.VERBOSE) {
            logMessage("Stop the crypto mining");
        }

        assert isOn : new PreconditionException("stopMiner() -> isOn()");
        assert isMining : new PreconditionException("stopMiner() -> isMining()");

        isMining = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(CryptoMinerStateModel.URI, MineOffCryptoMiner::new);
    }

}
