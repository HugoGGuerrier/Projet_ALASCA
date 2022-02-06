package eco_logis.equipments.oven;

import eco_logis.CVM_SIL;
import eco_logis.equipments.oven.mil.OvenCoupledModel;
import eco_logis.equipments.oven.mil.events.DoNotHeatOven;
import eco_logis.equipments.oven.mil.events.HeatOven;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import eco_logis.equipments.oven.sil.OvenStateModel;
import eco_logis.equipments.oven.sil.OvenTemperatureSILModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.heater.ThermostatedHeater;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.heater.ThermostatedHeaterRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.hem2021e3.equipments.heater.sil.HeaterTemperatureSILModel;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    /** Acceleration factor used when executing as a unit test */
    protected static final double ACC_FACTOR = 1.0;

    /** URI of the executor service used to execute the real time simulation */
    protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";


    // ========== Attributes ==========


    /** If the oven is currently on */
    private boolean isOn;

    /** If the oven is currently heating */
    private boolean isHeating;

    /** Target temperature of the oven, Celsius degrees (°C) */
    private double targetTemperature;

    /** Inbound port offering the <code>OvenCI</code> interface */
    private OvenInboundPort oip;

    /** Actual acceleration factor */
    protected double accFactor;

    // --- For the SIL simulation

    /** URI of the simulation architecture to be created or the empty string
     *  if the component does not execute as a SIL simulation */
    protected String simArchitectureURI;

    /** Simulator plug-in that holds the SIL simulator for this component */
    protected OvenRTAtomicSimulatorPlugin simulatorPlugin;

    /** True if the component executes as a SIL simulation, false otherwise */
    protected boolean isSILSimulated;

    /** True if the component executes as a unit test, false otherwise */
    protected boolean executesAsUnitTest;

    /** True if the component executes as a unit test, false otherwise */
    protected boolean composesAsUnitTest;

    // --- For control

    protected static long PERIOD = 200;
    protected static TimeUnit CONTROL_TIME_UNIT = TimeUnit.MILLISECONDS;
    protected static double HYSTERESIS = 2.0;


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
        this(INBOUND_PORT_URI, simArchitectureURI, executesAsUnitTest);
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
        this.targetTemperature = 100.0;
        this.isOn = false;
        this.isHeating = false;


        // Initialise the SIL simulation
        this.simArchitectureURI = simArchitectureURI;
        this.isSILSimulated = !simArchitectureURI.isEmpty();
        this.composesAsUnitTest = simArchitectureURI.equals(OvenRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI);
        this.accFactor = this.composesAsUnitTest ? ACC_FACTOR : CVM_SIL.ACC_FACTOR;
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


    /**
     * Implement the controller task that will be executed to decide when to
     * start or stop heating.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @param period	period at which the control task is executed.
     * @param u			time unit allowing to interpret the value of {@code period}.
     */
    protected void internalController(long period, TimeUnit u) {
        // When the oven is on, perform the control, but if the oven is switched off, stop the controller
        if(this.isOn) {
            try {
                if(this.isHeating && this.getCurrentTemperature() > this.targetTemperature + HYSTERESIS) {
                    if(Oven.VERBOSE) this.traceMessage("Oven decides to not heat.\n");
                    this.doNotHeat();

                } else if (!this.isHeating && this.getCurrentTemperature() < this.targetTemperature - HYSTERESIS) {
                    if (Oven.VERBOSE) this.traceMessage("Oven decides to heat.\n");
                    this.heat();

                } else {
                    if (Oven.VERBOSE) this.traceMessage("Oven decides to do nothing.\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            this.scheduleTask(
                    o -> ((Oven) o).internalController(period, u),
                    period, u);
        }
    }


    /**
     * Make the oven start heating; this internal method is
     * meant to be executed by the oven when its current temperature
     * is below the target temperature.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code this.isOn}
     * post	true		// no postcondition.
     * </pre>
     *
     * @throws Exception TODO
     */
    public void heat() throws Exception {
        assert isOn : new PreconditionException("heat() -> isOn()");
        assert !isHeating : new PreconditionException("heat() -> !isHeating()");

        isHeating = true;

        if (this.isSILSimulated) {
            this.simulatorPlugin.triggerExternalEvent(OvenStateModel.URI, HeatOven::new);
        }
    }

    /**
     * Make the oven stop heating; this internal method is
     * meant to be executed by the oven when its temperature
     * comes over the target temperature after a period of heating.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code internalIsRunning()}
     * post	true		// no postcondition.
     * </pre>
     *
     * @throws Exception TODO
     */
    public void doNotHeat() throws Exception {
        assert isOn : new PreconditionException("doNotHeat() -> isOn()");
        assert isHeating : new PreconditionException("doNotHeat() -> isHeating()");

        isHeating = false;

        if (this.isSILSimulated) {
            this.simulatorPlugin.triggerExternalEvent(OvenStateModel.URI, DoNotHeatOven::new);
        }
    }

    // ========== Override methods ==========


    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        // Call super
        super.start();

        // Trace
        this.traceMessage("Oven starts.\n");

        if (this.isSILSimulated) {
            this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
            this.simulatorPlugin = new OvenRTAtomicSimulatorPlugin();
            this.simulatorPlugin.setPluginURI(OvenCoupledModel.URI);
            this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
            try {
                this.simulatorPlugin.initialiseSimulationArchitecture(
                        this.simArchitectureURI,
                        this.executesAsUnitTest ? ACC_FACTOR : CVM_SIL.ACC_FACTOR
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
        if (this.composesAsUnitTest &&  this.executesAsUnitTest) {
            this.simulatorPlugin.setSimulationRunParameters(new HashMap<>());
            long simStart = System.currentTimeMillis() + 1000L;
            double endTime = 10.0 / ACC_FACTOR;
            this.simulatorPlugin.startRTSimulation(simStart, 0.0, endTime);
            this.traceMessage("real time of start = " + simStart + "\n");
        }
        if (this.executesAsUnitTest) {
            this.scheduleTask(
                    AbstractComponent.STANDARD_SCHEDULABLE_HANDLER_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                ((Oven)this.getTaskOwner()).powerOn();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    (long)(2.0/this.accFactor),
                    TimeUnit.SECONDS);
            this.scheduleTask(
                    AbstractComponent.STANDARD_SCHEDULABLE_HANDLER_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                double t = ((Oven)
                                        this.getTaskOwner()).getCurrentTemperature();
                                this.getTaskOwner().traceMessage(
                                        "Current oven temperature: " +
                                                t + "\n");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    (long)(7.0/this.accFactor),
                    TimeUnit.SECONDS);
            this.scheduleTask(
                    AbstractComponent.STANDARD_SCHEDULABLE_HANDLER_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                ((Oven)this.getTaskOwner()).powerOff();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    (long)(8.0/this.accFactor),
                    TimeUnit.SECONDS);
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


    // ========== Override methods ==========


    /** @see OvenImplementationI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        if(VERBOSE) {
            logMessage("Oven gets on : " + isOn);
        }

        return isOn;
    }

    /** @see OvenImplementationI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        if(VERBOSE) {
            logMessage("Oven powers on.");
        }

        assert !isOn : new PreconditionException("powerOn() -> !isOn()");

        isOn = true;
        isHeating = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) {
            simulatorPlugin.triggerExternalEvent(OvenStateModel.URI, SwitchOnOven::new);
        }

        /* When starting the oven, its internal controller is also started to execute at the predefined
        period to check the current temperature and decide when to start or stop heating */
        long accPeriod = (long) (PERIOD/this.accFactor);
        this.scheduleTask(
                o -> ((Oven) o).internalController(accPeriod, CONTROL_TIME_UNIT),
                accPeriod, CONTROL_TIME_UNIT);
        this.internalController(accPeriod, CONTROL_TIME_UNIT);

    }

    /** @see OvenImplementationI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        if(VERBOSE) {
            logMessage("Oven powers off.");
        }

        assert isOn : new PreconditionException("powerOff() -> isOn()");

        isOn = false;
        isHeating = false;

        // Trigger the event for the SIL simulation
        if(isSILSimulated) {
            simulatorPlugin.triggerExternalEvent(OvenStateModel.URI, SwitchOffOven::new);
        }
    }

    /** @see OvenImplementationI#getCurrentTemperature() */
    @Override
    public double getCurrentTemperature() throws Exception {
        double currentTemperature =  0.0;

        if (this.isSILSimulated) {
            currentTemperature = (double) this.simulatorPlugin.getModelStateValue(
                            OvenTemperatureSILModel.URI,
                            OvenRTAtomicSimulatorPlugin.ROOM_TEMPERATURE);
        } else {
            // Temporary implementation; would need a temperature sensor.
        }

        if(Oven.VERBOSE) {
            logMessage("Oven current temperature is : " + currentTemperature + "°C");
        }

        return currentTemperature;
    }

    /** @see OvenImplementationI#getTargetTemperature() */
    @Override
    public double getTargetTemperature() {
        if(Oven.VERBOSE) {
            logMessage("Oven target temperature is : " + targetTemperature + "°C");
        }
        assert isOn;
        return targetTemperature;
    }

    /** @see OvenImplementationI#setTargetTemperature(double) */
    public void setTargetTemperature(double tempTarget) {
        if(Oven.VERBOSE) {
            logMessage("Oven target temperature is set to " + tempTarget + "°C");
        }

        assert isOn;
        assert tempTarget > 20.0 && tempTarget < 300.1;

        this.targetTemperature = tempTarget;
    }

}
