package equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredInterfaces(required = {OvenCI.class})
public class OvenUnitTester
    extends AbstractComponent {

    // ========== Attributes ==========

    private String ovenInboundPortUri;
    private OvenOutboundPort ovenOutboundPort;

    // ========== Constructors ==========

    protected OvenUnitTester() throws Exception {
        this(Oven.INBOUND_PORT_URI);
    }

    protected OvenUnitTester(String ovenInboundPortUri) throws Exception {
        super(1, 0);
        initialise(ovenInboundPortUri);
    }

    protected OvenUnitTester(String reflectionInboundPortURI, String ovenInboundPortUri) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(ovenInboundPortUri);
    }

    // ========== Class methods ==========

    protected void initialise(String ovenInboundPortUri) throws Exception {
        // Set the inbound port uri
        this.ovenInboundPortUri = ovenInboundPortUri;

        // Create the outbound port
        ovenOutboundPort = new OvenOutboundPort(this);
        ovenOutboundPort.publishPort();

        // Trace the execution
        tracer.get().setTitle("Oven tester component");
        tracer.get().setRelativePosition(0, 1);
        toggleTracing();
    }

    // ========== Test methods ==========

    protected void testIsBaking() {
        logMessage("Test isBaking()...");
        try {
            assertFalse(ovenOutboundPort.isBaking());
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testStartStopBaking() {
        logMessage("Test startBaking() and stopBaking()...");
        try {
            assertFalse(ovenOutboundPort.isBaking());
            ovenOutboundPort.startBaking();
            assertTrue(ovenOutboundPort.isBaking());
            ovenOutboundPort.stopBaking();
            assertFalse(ovenOutboundPort.isBaking());
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("...Done!");
    }

    protected void runAllTests() {
        logMessage("Starting test suite...");
        testIsBaking();
        testStartStopBaking();
        logMessage("All tests passed !");
    }

    // ========== Lifecycle methods ==========

    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        // Connect the outbound port to the inbound port
        try {
            doPortConnection(
                    ovenOutboundPort.getPortURI(),
                    ovenInboundPortUri,
                    OvenTestConnector.class.getCanonicalName()
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
        doPortDisconnection(ovenOutboundPort.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            ovenOutboundPort.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }
}
