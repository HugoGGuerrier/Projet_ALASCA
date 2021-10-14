package equipments.crypto_miner;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredInterfaces(required = {CryptoMinerCI.class})
public class CryptoMinerUnitTester
    extends AbstractComponent
{

    // ========== Attributes ==========

    private String cryptoMinerInboundPortUri;
    private CryptoMinerOutboundPort cmop;

    // ========== Constructors ==========

    protected CryptoMinerUnitTester() throws Exception {
        this(CryptoMiner.INBOUND_PORT_URI);
    }

    protected CryptoMinerUnitTester(String cryptoMinerInboundPortURI) throws Exception {
        super(1, 0);
        initialise(cryptoMinerInboundPortURI);
    }

    protected CryptoMinerUnitTester(String reflectionInboundPortURI, String cryptoMinerInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(cryptoMinerInboundPortURI);
    }

    // ========== Class methods ==========

    protected void initialise(String cryptoMinerInboundPortUri) throws Exception {
        // Set the inbound port uri
        this.cryptoMinerInboundPortUri = cryptoMinerInboundPortUri;

        // Create the outbound port
        cmop = new CryptoMinerOutboundPort(this);
        cmop.publishPort();

        // Trace the execution
        tracer.get().setTitle("Crypto miner tester component");
        tracer.get().setRelativePosition(0, 1);
        toggleTracing();
    }

    // ========== Test methods ==========

    protected void testIsMining() {
        traceMessage("Test isMining()...");
        try {
            assertFalse(cmop.isMining());
        } catch (Exception e) {
            traceMessage("... FAILED!");
            fail(e);
        }
        traceMessage("... Done!");
    }

    protected void testStartStopMiner() {
        traceMessage("Test startMiner() and stopMiner()...");
        try {
            assertFalse(cmop.isMining());
            cmop.startMiner();
            assertTrue(cmop.isMining());
            cmop.stopMiner();
            assertFalse(cmop.isMining());
        } catch (Exception e) {
            traceMessage("... FAILED!");
            fail(e);
        }
        traceMessage("...Done!");
    }

    protected void runAllTests() {
        testIsMining();
        testStartStopMiner();
    }

    // ========== Lifecycle methods ==========


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        // Connect the outbound port to the inbound port
        try {
            doPortConnection(
                    cmop.getPortURI(),
                    cryptoMinerInboundPortUri,
                    CryptoMinerTestConnector.class.getCanonicalName()
            );
        } catch(Exception e) {
            throw new ComponentStartException(e);
        }
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        // Run all the tests
        runAllTests();

        super.execute();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#finalise() */
    @Override
    public synchronized void finalise() throws Exception {
        // Disconnect the ports
        doPortDisconnection(cmop.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            cmop.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }

}
