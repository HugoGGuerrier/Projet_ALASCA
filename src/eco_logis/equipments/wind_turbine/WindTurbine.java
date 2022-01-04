package eco_logis.equipments.wind_turbine;

import eco_logis.CVM_SIL;
import eco_logis.equipments.wind_turbine.mil.WindTurbineCoupledModel;
import eco_logis.equipments.wind_turbine.mil.events.BlockWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.UnblockWindTurbine;
import eco_logis.equipments.wind_turbine.sil.WindTurbineStateModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;

/**
 * This class represent a wind turbine which can be blocked and unblocked
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@OfferedInterfaces(offered = {WindTurbineCI.class})
public class WindTurbine
    extends AbstractCyPhyComponent
    implements WindTurbineImplementationI
{

    // ========== Macros ==========


    /** URI of the wind turbine inbound port */
    public static final String INBOUND_PORT_URI = "WIND-TURBINE-INBOUND-PORT-URI";

    /** The URI of the reflection inbound port */
    public static final String REFLECTION_INBOUND_PORT_URI = "WIND_TURBINE_rip";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** If the turbine is currently turning (there is wind, it produces electricity) */
    private boolean isTurning;

    /** If the turbine is blocked (cases of strong winds) */
    private boolean isBlocked;

    /** The current electricity production from the wind turbine */
    private double currentProduction;

    /** The inbound port */
    private WindTurbineInboundPort wtip;

    // --- For the SIL simulation

    /** The URI of the created simulation architecture */
    protected String simArchURI;

    /** The URI of the executor */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

    /** The prugin that contains the SIL simulation */
    protected WindTurbineRTAtomicSimulatorPlugin simulatorPlugin;

    /** If the component is in a SIL simulation */
    protected boolean isSILSimulated;

    /** If the component executes a unit test */
    protected boolean executesAsUnitTest;

    /** Acceleration factor for the simulation */
    protected static final double ACC_FACT = 1.0;


    // ========== Constructors ==========


    /**
     * Create a new wind turbine
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
    protected WindTurbine(String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(INBOUND_PORT_URI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new wind turbine with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @see AbstractComponent#AbstractComponent(int, int)
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param windTurbineInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected WindTurbine(String windTurbineInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        initialise(windTurbineInboundPortURI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new wind turbine with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
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
     * @param windTurbineInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected WindTurbine(String reflectionInboundPortURI, String windTurbineInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(windTurbineInboundPortURI, simArchURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise the newly created wind turbine
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code windTurbineInboundPortURI != null}
     * pre	{@code !windTurbineInboundPortURI.isEmpty()}
     * post {@code !isMining}
     * post	{@code wtip.isPublished()}
     * </pre>
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param windTurbineInboundPortURI The wind turbine inbound port URI
     * @throws Exception TODO
     */
    protected void initialise(
            String windTurbineInboundPortURI,
            String simArchURI,
            boolean executesAsUnitTest
    ) throws Exception {
        // Assert the URI consistence
        assert windTurbineInboundPortURI != null : new PreconditionException(
                "windTurbineInboundPortURI != null");
        assert !windTurbineInboundPortURI.isEmpty() : new PreconditionException(
                "!windTurbineInboundPortURI.isEmpty()");

        // Initialise the component
        isTurning = false;
        isBlocked = false;
        currentProduction = 0.0;

        // Initialise the SIL simulation
        this.simArchURI = simArchURI;
        this.isSILSimulated = !simArchURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Create the inbound port
        wtip = new WindTurbineInboundPort(windTurbineInboundPortURI, this);
        wtip.publishPort();

        // Create the trace
        if(WindTurbine.VERBOSE) {
            tracer.get().setTitle("Wind turbine component");
            tracer.get().setRelativePosition(1, 1);
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
        this.traceMessage("Wind turbine starts.\n");

        if(isSILSimulated) {
            createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            simulatorPlugin = new WindTurbineRTAtomicSimulatorPlugin();
            simulatorPlugin.setPluginURI(WindTurbineCoupledModel.URI);
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
        this.traceMessage("Wind turbine stops.\n");

        try {
            wtip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }


    // ========== Override methods ==========


    /** @see WindTurbineImplementationI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine is turning : " + isTurning);
        }

        return isTurning;
    }

    /** @see WindTurbineImplementationI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine is blocked : " + isBlocked);
        }

        return isBlocked;
    }

    /** @see WindTurbineImplementationI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Wind turbine current electricity production = " + currentProduction);
        }

        return currentProduction;
    }

    /** @see WindTurbineImplementationI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Block the wind turbine");
        }

        assert !isBlocked : new PreconditionException("blockTurning() -> !isBlocked()");

        isBlocked = true;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(WindTurbineStateModel.URI, BlockWindTurbine::new);
    }

    /** @see WindTurbineImplementationI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        if(WindTurbine.VERBOSE) {
            logMessage("Unblock the wind turbine");
        }

        assert isBlocked : new PreconditionException("unblockTurning() -> isBlocked()");
        assert !isTurning : new PreconditionException("unblockTurning() -> !isTurning()");

        isBlocked = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(WindTurbineStateModel.URI, UnblockWindTurbine::new);
    }

}
