package eco_logis.equipments.wind_turbine;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import static org.junit.jupiter.api.Assertions.*;

@RequiredInterfaces(required = {WindTurbineCI.class})
public class WindTurbineUnitTester
    extends AbstractComponent
{

    // ========== Attributes ==========



    private String windTurbineInboundPortUri;
    private WindTurbineOutboundPort windTurbineOutboundPort;


    // ========== Constructors ==========


    protected WindTurbineUnitTester() throws Exception {
        this(WindTurbine.INBOUND_PORT_URI);
    }

    protected WindTurbineUnitTester(String windTurbineInboundPortURI) throws Exception {
        super(1, 0);
        initialise(windTurbineInboundPortURI);
    }

    protected WindTurbineUnitTester(String reflectionInboundPortURI, String windTurbineInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(windTurbineInboundPortURI);
    }


    // ========== Class methods ==========


    protected void initialise(String windTurbineInboundPortURI) throws Exception {
        // Set the inbound port uri
        this.windTurbineInboundPortUri = windTurbineInboundPortURI;

        // Create the outbound port
        windTurbineOutboundPort = new WindTurbineOutboundPort(this);
        windTurbineOutboundPort.publishPort();

        // Trace the execution
        tracer.get().setTitle("Wind turbine tester component");
        tracer.get().setRelativePosition(0, 2);
        toggleTracing();
    }


    // ========== Test methods ==========


    protected void testIsTurning() {
        logMessage("Test isTurning()...");
        try {
            assertFalse(windTurbineOutboundPort.isTurning());
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testIsBlocked() {
        logMessage("Test isBlocked()...");
        try {
            assertFalse(windTurbineOutboundPort.isBlocked());
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testBlockUnblockTurning() {
        logMessage("Test blockTurning() and unblockTurning()...");
        try {
            assertFalse(windTurbineOutboundPort.isBlocked());
            windTurbineOutboundPort.blockTurning();
            assertTrue(windTurbineOutboundPort.isBlocked());
            assertFalse(windTurbineOutboundPort.isTurning());
            windTurbineOutboundPort.unblockTurning();
            assertFalse(windTurbineOutboundPort.isBlocked());
        } catch (Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("...Done!");
    }

    protected void runAllTests() {
        logMessage("Starting wind turbine test suite...");
        testIsTurning();
        testIsBlocked();
        testBlockUnblockTurning();
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
                    windTurbineOutboundPort.getPortURI(),
                    windTurbineInboundPortUri,
                    WindTurbineTestConnector.class.getCanonicalName()
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
        doPortDisconnection(windTurbineOutboundPort.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            windTurbineOutboundPort.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }

}
