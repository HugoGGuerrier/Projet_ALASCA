package eco_logis.equipments.generator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredInterfaces(required = {GeneratorCI.class})
public class GeneratorUnitTester
        extends AbstractComponent
{

    // ========== Attributes ==========

    private String generatorInboundPortUri;
    private GeneratorOutboundPort gop;

    // ========== Constructors ==========

    protected GeneratorUnitTester() throws Exception {
        this(Generator.INBOUND_PORT_URI);
    }

    protected GeneratorUnitTester(String generatorInboundPortUri) throws Exception {
        super(1, 0);
        initialise(generatorInboundPortUri);
    }

    protected GeneratorUnitTester(String reflectionInboundPortURI, String generatorInboundPortUri) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(generatorInboundPortUri);
    }

    // ========== Class methods ==========

    protected void initialise(String generatorInboundPortUri) throws Exception {
        // Set the inbound port uri
        this.generatorInboundPortUri = generatorInboundPortUri;

        // Create the outbound port
        gop = new GeneratorOutboundPort(this);
        gop.publishPort();

        // Trace the execution
        tracer.get().setTitle("Generator tester component");
        tracer.get().setRelativePosition(0, 2);
        toggleTracing();
    }

    // ========== Test methods ==========

    protected void testIsRunning() {
        logMessage("Test isRunning()...");
        try {
            assertFalse(gop.isRunning());
        } catch (Exception e) {
            e.printStackTrace();
            logMessage("... FAILED !");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testStartStopGenerator() {
        logMessage("Test startGenerator() and stopGenerator()...");
        try {
            assertFalse(gop.isRunning());
            gop.startGenerator();
            assertTrue(gop.isRunning());
            gop.stopGenerator();
            assertFalse(gop.isRunning());
        } catch (Exception e) {
            logMessage("... FAILED !");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testGetFuelLevel() {
        logMessage("Test getFuelLevel()...");
        try {
            assertEquals(0.0f, gop.getFuelLevel());
        } catch (Exception e) {
            logMessage("... FAILED !");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testRefill() {
        logMessage("Test refill()...");
        try {
            assertEquals(0.0f, gop.getFuelLevel());
            gop.refill();
            assertEquals(1.0f, gop.getFuelLevel());
        } catch (Exception e) {
            logMessage("... FAILED !");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void runAllTests() {
        logMessage("Starting test suite...");
        testIsRunning();
        testStartStopGenerator();
        testGetFuelLevel();
        testRefill();
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
                    gop.getPortURI(),
                    generatorInboundPortUri,
                    GeneratorTestConnector.class.getCanonicalName()
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
        doPortDisconnection(gop.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            gop.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }

}
