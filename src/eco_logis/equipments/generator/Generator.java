package eco_logis.equipments.generator;

import eco_logis.CVM_SIL;
import eco_logis.equipments.generator.mil.GeneratorCoupledModel;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
import eco_logis.equipments.generator.sil.GeneratorStateModel;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;

/**
 * This class represent a fuel generator component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@OfferedInterfaces(offered = {GeneratorCI.class})
public class Generator
    extends AbstractCyPhyComponent
    implements GeneratorImplementationI
{

    // ========== Macros ==========


    /** URI of the generator inbound port */
    public static final String INBOUND_PORT_URI = "GENERATOR-INBOUND-PORT-URI";

    /** The URI of the reflection inbound port */
    public static final String REFLECTION_INBOUND_PORT_URI = "GENERATOR_rip";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    /** The default generator production */
    private static final double DEFAULT_PRODUCTION = 200.0d;


    // ========== Attributes ==========


    /** If the generator is currently running */
    private boolean isRunning;

    /** The generator tank fuel level between 0 and 1 */
    private float fuelLevel;

    /** The inbound port */
    private GeneratorInboundPort gip;

    // --- For the SIL simulation

    /** The URI of the created simulation architecture */
    protected String simArchURI;

    /** The URI of the executor */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

    /** The prugin that contains the SIL simulation */
    protected GeneratorRTAtomicSimulatorPlugin simulatorPlugin;

    /** If the component is in a SIL simulation */
    protected boolean isSILSimulated;

    /** If the component executes a unit test */
    protected boolean executesAsUnitTest;

    /** Acceleration factor for the simulation */
    protected static final double ACC_FACT = 1.0;


    // ========== Constructors ==========


    /**
     * Create a new generator
     *
     * <p><strong>Contract</strong></p>
     *
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
    protected Generator(String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(INBOUND_PORT_URI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new generator with the wanted generator inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param generatorInboundPortURI The wanted inbound port URI
     * @throws Exception TODO
     */
    protected Generator(String generatorInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        this(REFLECTION_INBOUND_PORT_URI, generatorInboundPortURI, simArchURI, executesAsUnitTest);
    }

    /**
     * Create a new generator with the wanted reflection inbound port URI and generator inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param reflectionInboundPortURI The reflection inbound port URI
     * @param generatorInboundPortURI The generator inbound pour URI
     * @throws Exception TODO
     */
    protected Generator(String reflectionInboundPortURI, String generatorInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        initialise(generatorInboundPortURI, simArchURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise the generator with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * post {@code !isRunning}
     * post {@code fuelLevel == 0.0}
     * post	{@code gip.isPublished()}
     *
     * @param simArchURI The simulation architecture URI
     * @param executesAsUnitTest If the component has to be executed as a unit test
     * @param generatorInboundPortURI The generator inbound port URI
     * @throws Exception TODO
     */
    private void initialise(String generatorInboundPortURI, String simArchURI, boolean executesAsUnitTest) throws Exception {
        // Assert the URI
        assert generatorInboundPortURI != null : new PreconditionException("generatorInboundPortURI != null");
        assert !generatorInboundPortURI.isEmpty() : new PreconditionException("!generatorInboundPortURI.isEmpty()");

        assert simArchURI != null;
        assert !simArchURI.isEmpty() || !executesAsUnitTest;

        // Initialise the component
        isRunning = false;
        fuelLevel = 0.0f;

        // Initialise the SIL simulation
        this.simArchURI = simArchURI;
        this.isSILSimulated = !simArchURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Initialise the inbound port
        gip = new GeneratorInboundPort(generatorInboundPortURI, this);
        gip.publishPort();

        // Create the tracer
        if(VERBOSE) {
            tracer.get().setTitle("Generator component");
            tracer.get().setRelativePosition(2, 0);
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
        this.traceMessage("Generator starts.\n");

        if(isSILSimulated) {
            createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            simulatorPlugin = new GeneratorRTAtomicSimulatorPlugin();
            simulatorPlugin.setPluginURI(GeneratorCoupledModel.URI);
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
        this.traceMessage("Generator stops.\n");

        try {
            gip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }


    // ========== Override methods ==========


    /** @see GeneratorImplementationI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        if(VERBOSE) {
            logMessage("Generator get running : " + isRunning);
        }

        return isRunning;
    }

    /** @see GeneratorImplementationI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        if(VERBOSE) {
            logMessage("Generator start running");
        }

        assert !isRunning : new PreconditionException("startGenerator() -> !isRunning");

        isRunning = true;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(GeneratorStateModel.URI, SwitchOnGenerator::new);
    }

    /** @see GeneratorImplementationI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        if(VERBOSE) {
            logMessage("Generator stop running");
        }

        assert isRunning : new PreconditionException("stopGenerator() -> isRunning");

        isRunning = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) simulatorPlugin.triggerExternalEvent(GeneratorStateModel.URI, SwitchOffGenerator::new);
    }

    @Override
    public double getEnergyProduction() throws Exception {
        if(VERBOSE) {
            logMessage("Generator get production : " + DEFAULT_PRODUCTION);
        }

        assert isRunning : new PreconditionException("getEnergyProduction() -> isRunning");

        return DEFAULT_PRODUCTION;
    }

    /** @see GeneratorImplementationI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        if(VERBOSE) {
            logMessage("Generator get fuel level : " + fuelLevel);
        }

        return fuelLevel;
    }

    /** @see GeneratorImplementationI#refill() */
    @Override
    public void refill() throws Exception {
        if(VERBOSE) {
            logMessage("Generator refill the tank");
        }

        assert !isRunning : new PreconditionException("refill() -> !isRunning");

        fuelLevel = 1.0f;
    }

}
