package eco_logis.equipments.oven;

import eco_logis.CVM_SIL;
import eco_logis.equipments.oven.mil.OvenCoupledModel;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import eco_logis.equipments.oven.sil.OvenStateModel;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;

/**
 * The class <code>Oven</code> implements the oven component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The oven is an uncontrollable appliance, hence it does not connect
 * with the household energy manager (we don't want any control over the oven).
 * However, it will connect later to the electric panel to take its (simulated)
 * electricity consumption into account.
 * </p>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */

@OfferedInterfaces(offered = {OvenCI.class})
public class Oven
    extends AbstractCyPhyComponent
    implements OvenImplementationI 
{

    // ========== Macros ==========


    /** URI of the oven reflection inbound port used */
    public static final String REFLECTION_INBOUND_PORT_URI = "OVEN-rip";

    /** URI of the oven inbound port used in tests */
    public static final String INBOUND_PORT_URI = "OVEN-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;

    /** Initial state of the oven (false -> OFF) */
    public static final boolean INITIAL_STATE = false;

    /** acceleration factor used when executing as a unit test */
    protected static final double ACC_FACTOR = 1.0;

    /** URI of the executor service used to execute the real time simulation */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";


    // ========== Attributes ==========


    /** State of the oven : baking (on) or not (off) */
    private boolean isBaking;

    /** Goal temperature of the oven, Celsius degrees (°C) */
    private double goalTemperature;

    /** Temperature of the oven, Celsius degrees (°C) */
    private double temperature;

    /** Inbound port offering the <code>OvenCI</code> interface */
    private OvenInboundPort oip;

    // --- For the SIL simulation

    /** URI of the simulation architecture to be created or the empty string
     *  if the component does not execute as a SIL simulation */
    protected String simArchitectureURI;

    /** Simulator plug-in that holds the SIL simulator for this component */
    protected OvenRTAtomicSimulatorPlugin simulatorPlugin;

    /** True if the component executes as a SIL simulation, false otherwise */
    protected boolean isSILsimulated;

    /** True if the component executes as a unit test, false otherwise */
    protected boolean executesAsUnitTest;


    // ========== Constructors ==========


    /**
     * Create an oven component including a SIL simulation.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post {@code !isBaking}
     * post {@code temperature == 20.0}
     * </pre>
     *
     * @param simArchitectureURI	URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest	true if the component executes as a unit test, false otherwise.
     * @throws Exception			<i>to do</i>.
     */
    protected Oven(
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        this.initialise(INBOUND_PORT_URI, simArchitectureURI, executesAsUnitTest);
    }


    /**
     * Create an oven component.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post {@code !isBaking}
     * post {@code temperature == 20.0}
     * </pre>
     *
     * @param ovenInboundPortURI	    URI of the oven inbound port.
     * @param simArchitectureURI		URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest		true if the component executes as a unit test, false otherwise.
     * @throws Exception				<i>to do</i>.
     */
    protected Oven(
            String ovenInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        this.initialise(ovenInboundPortURI, simArchitectureURI, executesAsUnitTest);
    }


    /**
     * Create an oven component with the given reflection inbound port
     * URI.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post {@code !isBaking}
     * post {@code temperature == 20.0}
     * </pre>
     *
     * @param reflectionInboundPortURI	URI of the reflection inbound port of the component.
     * @param ovenInboundPortURI    	URI of the oven inbound port.
     * @param simArchitectureURI		URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest		true if the component executes as a unit test, false otherwise.
     * @throws Exception				<i>to do</i>.
     */
    protected Oven(
            String reflectionInboundPortURI,
            String ovenInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        this.initialise(ovenInboundPortURI, simArchitectureURI, executesAsUnitTest);
    }


    // ========== Class methods ==========


    /**
     * Initialise the newly created oven
     *
     * <p><strong>Contract</strong></p>
     * 
     * <pre>
     * pre	{@code ovenInboundPortURI != null}
     * pre	{@code !ovenInboundPortURI.isEmpty()}
     * pre	{@code simArchitectureURI != null}
     * pre	{@code !simArchitectureURI.isEmpty() || !executesAsUnitTest}
     * post {@code !isBaking}
     * post {@code temperature == 20.0}
     * post {@code goalTemperature == 100.0}
     * post	{@code oip.isPublished()}
     * </pre>
     *
     * @param ovenInboundPortURI    The oven inbound port URI
     * @param simArchitectureURI    URI of the simulation architecture to be created or the empty string  if the component does not execute as a SIL simulation.
     * @param executesAsUnitTest    true if the component executes as a unit test, false otherwise.
     *
     * @throws Exception TODO
     */
    protected void initialise(
            String ovenInboundPortURI,
            String simArchitectureURI,
            boolean executesAsUnitTest
    ) throws Exception {
        // Assert the URI consistence
        assert ovenInboundPortURI != null : new PreconditionException("ovenInboundPortURI != null");
        assert !ovenInboundPortURI.isEmpty() : new PreconditionException("!ovenInboundPortURI.isEmpty()");
        assert simArchitectureURI != null;
        assert !simArchitectureURI.isEmpty() || !executesAsUnitTest;

        // Initialise the component
        this.temperature = 20.0;
        this.goalTemperature = 100.0;
        this.isBaking = INITIAL_STATE;

        // Initialise the SIL simulation
        this.simArchitectureURI = simArchitectureURI;
        this.isSILsimulated = !simArchitectureURI.isEmpty();
        this.executesAsUnitTest = executesAsUnitTest;

        // Create the inbound port
        this.oip = new OvenInboundPort(ovenInboundPortURI, this);
        this.oip.publishPort();

        // Create the trace
        if (Oven.VERBOSE) {
            this.tracer.get().setTitle("Oven component");
            this.tracer.get().setRelativePosition(3, 0);
            this.toggleTracing();
        }
    }


    // ========== Override methods ==========



    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        this.traceMessage("Oven starts.\n");

        if (this.isSILsimulated) {
            this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            this.simulatorPlugin = new OvenRTAtomicSimulatorPlugin();
            this.simulatorPlugin.setPluginURI(OvenCoupledModel.URI);
            this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
            try {
                this.simulatorPlugin.initialiseSimulationArchitecture(
                        this.simArchitectureURI,
                        this.executesAsUnitTest ?
                                ACC_FACTOR
                                :	CVM_SIL.ACC_FACTOR
                );
                this.installPlugin(this.simulatorPlugin);
            } catch (Exception e) {
                throw new ComponentStartException(e) ;
            }
        }
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        if (this.executesAsUnitTest) {
            this.simulatorPlugin.setSimulationRunParameters(new HashMap<String, Object>());
            long simStart = System.currentTimeMillis() + 1000L;
            double endTime = 10.0/ACC_FACTOR;
            this.simulatorPlugin.startRTSimulation(simStart, 0.0, endTime);
            this.traceMessage("real time of start = " + simStart + "\n");
        }
    }


    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        this.traceMessage("Oven stops.\n");

        try {
            this.oip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e) ;
        }
        super.shutdown();
    }


    /** @see OvenImplementationI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        if(Oven.VERBOSE) {
            logMessage("Oven is baking : " + isBaking);
        }
        return isBaking;
    }

    /** @see OvenImplementationI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        assert !isBaking : new PreconditionException("isBaking == false");

        if(Oven.VERBOSE) {
            logMessage("Oven is turned on, starts baking");
        }

        assert !isBaking : new PreconditionException("startBaking() -> !isBaking()");

        isBaking = true;

        if (this.isSILsimulated) {
            this.simulatorPlugin.triggerExternalEvent(
                    OvenStateModel.URI,
                    t -> new SwitchOnOven(t, goalTemperature));
        }
    }

    /** @see OvenImplementationI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        assert isBaking : new PreconditionException("isBaking() == true");

        if(Oven.VERBOSE) {
            logMessage("Oven is turned off, stops baking. The cake is ready !");
        }

        assert isBaking : new PreconditionException("stopBaking() -> isBaking()");

        isBaking = false;

        if (this.isSILsimulated) {
            this.simulatorPlugin.triggerExternalEvent(
                    OvenStateModel.URI,
                    t -> new SwitchOffOven(t));
        }
    }

    /** @see OvenImplementationI#getTemperature() */
    @Override
    public double getTemperature() {
        if(Oven.VERBOSE) {
            logMessage("Oven temperature is : " + temperature + "°C");
        }
        assert isBaking;
        return temperature;
    }

    /** @see OvenImplementationI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) {
        if(Oven.VERBOSE) {
            logMessage("Oven temperature is set to " + temp + "°C");
        }
        assert isBaking;
        this.temperature = temp;
    }

}
