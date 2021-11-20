package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class DishwasherUnitTester
    extends AbstractComponent
{

    // ========== Attributes ==========


    private String dishwasherInboundPortURI;
    private DishwasherOutboundPort dwop;


    // ========== Constructors ==========


    protected DishwasherUnitTester() throws Exception {
        this(Dishwasher.INBOUND_PORT_URI);
    }

    protected DishwasherUnitTester(String dishwasherInboundPortURI) throws Exception {
        super(1, 0);
        initialise(dishwasherInboundPortURI);
    }

    protected DishwasherUnitTester(String reflectionInboundPortURI, String dishwasherInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(dishwasherInboundPortURI);
    }


    // ========== Class methods ==========


    protected void initialise(String dishwasherInboundPortURI) throws Exception {
        // Set the inbound port uri
        this.dishwasherInboundPortURI = dishwasherInboundPortURI;

        // Create the outbound port
        dwop = new DishwasherOutboundPort(this);
        dwop.publishPort();

        // Trace the execution
        tracer.get().setTitle("Dishwasher tester component");
        tracer.get().setRelativePosition(0, 2);
        toggleTracing();
    }


    // ========== Test methods ==========


    protected void testIsPlanned() {
        logMessage("Test isPlanned()...");
        try {
            assertFalse(dwop.isPlanned());
        } catch(Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testIsWashing() {
        logMessage("Test isWashing()...");
        try {
            assertFalse(dwop.isWashing());
        } catch(Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void testPlanDefault() {
        logMessage("Test plan(deadline)...");
        try {
            assertFalse(dwop.isWashing());
            assertFalse(dwop.isPlanned());
            dwop.plan(LocalTime.now().plusMinutes(100));
            assertTrue(dwop.isPlanned());
            assertFalse(dwop.isWashing());
            assertEquals(DishwasherImplementationI.DishwasherProgram.FULL, dwop.getProgram());
        } catch(Exception e) {
            logMessage("... FAILED!");
            fail(e);
        }
        logMessage("... Done!");
    }

    protected void runAllTests() {
        logMessage("Starting test suite...");
        testIsWashing();
        testPlanDefault();
        // TODO : test starting/stopping
        // TODO : test cancel
        // TODO : test getters
        logMessage("All tests passed!");
    }
    

    // ========== Lifecycle methods ==========


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        // Connect the outbound port to the inbound port
        try {
            doPortConnection(
                    dwop.getPortURI(),
                    dishwasherInboundPortURI,
                    DishwasherTestConnector.class.getCanonicalName()
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
        doPortDisconnection(dwop.getPortURI());

        super.finalise();
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            dwop.unpublishPort();
        } catch(Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }

}
