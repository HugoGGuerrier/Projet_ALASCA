package eco_logis.equipments.electric_meter;

import eco_logis.equipments.electric_meter.mil.ElectricMeterElectricityModel;
import eco_logis.equipments.electric_meter.sil.ElectricMeterCoupledModel;
import eco_logis.equipments.electric_meter.sil.ElectricMeterElectricitySILModel;
import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import eco_logis.equipments.oven.sil.OvenElectricitySILModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>ElectricMeterRTAtomicSimulatorPlugin</code> implements
 * the simulation plug-in for the <code>ElectricMeter</code> component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ElectricMeterRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;

    /** Simulation architectures can have URI to name them; this is the
     *  URI used in this example for unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestMeter";

    /** Name used to pass the owner component reference as simulation parameter */
    public static final String METER_REFERENCE_NAME = "MCRN";

    /** URI of the oven electricity model */
    protected static final String OVEN_ELECTRICITY_MODEL_URI = OvenElectricitySILModel.URI;

    /** Class implementing the oven electricity model */
    protected static final Class<OvenElectricitySILModel> OVEN_ELECTRICITY_MODEL_CLASS = OvenElectricitySILModel.class;

    ///** URI of the heater electricity model */
    //protected static final String	HEATER_ELECTRICITY_MODEL_URI = HeaterElectricitySILModel.URI;

    ///** Class implementing the heater electricity model */
    //protected static final Class<HeaterElectricitySILModel> HEATER_ELECTRICITY_MODEL_CLASS = HeaterElectricitySILModel.class;


    // ========== Class methods ==========


    /** @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        /* Initialise the simulation parameter giving the reference to the
        owner component before passing the parameters to the simulation
        models; because each model has been defined to retrieve the
        reference to its owner component using its own parameter name,
        we must pass the reference under each different name */
        simParams.put(METER_REFERENCE_NAME, this.getOwner());
        //simParams.put(ThermostatedHeaterRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        simParams.put(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());

        /* This will pass the parameters to the simulation models that will
        then be able to get their own parameters */
        super.setSimulationRunParameters(simParams);

        // Remove the value so that the reference may not exit the context of the component
        simParams.remove(METER_REFERENCE_NAME);
        //simParams.remove(ThermostatedHeaterRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        simParams.remove(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
    }



    /**
     * Create and set the simulation architecture internal to this component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param simArchURI	URI of the simulation architecture to be created.
     * @param accFactor		acceleration factor used in the real time simulation.
     * @throws Exception	<i>to do</i>.
     */
    public void initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {
        // For the project, the coupled model created for the electric meter
        // will include all of the models simulating the electricity consumption
        // and production for appliances and production units.

        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

        Set<String> submodels = new HashSet<String>();
        submodels.add(OVEN_ELECTRICITY_MODEL_URI);
        //submodels.add(HEATER_ELECTRICITY_MODEL_URI);
        submodels.add(ElectricMeterElectricitySILModel.URI);

        atomicModelDescriptors.put(
                OVEN_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        OVEN_ELECTRICITY_MODEL_CLASS,
                        OVEN_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));
        /*atomicModelDescriptors.put(
                HEATER_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        HEATER_ELECTRICITY_MODEL_CLASS,
                        HEATER_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));
         */
        atomicModelDescriptors.put(
                ElectricMeterElectricitySILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        ElectricMeterElectricitySILModel.class,
                        ElectricMeterElectricitySILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));

        Map<Class<? extends EventI>, EventSink[]> imported = null;

        if (!simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {
/*
            imported = new HashMap<>();
            imported.put(
                    SwitchOnHeater.class,
                    new EventSink[] {
                            new EventSink(HEATER_ELECTRICITY_MODEL_URI,
                                    SwitchOnHeater.class)
                    });
            imported.put(
                    SwitchOffHeater.class,
                    new EventSink[] {
                            new EventSink(HEATER_ELECTRICITY_MODEL_URI,
                                    SwitchOffHeater.class)
                    });
*/
            imported.put(
                    SwitchOnOven.class,
                    new EventSink[] {
                            new EventSink(OVEN_ELECTRICITY_MODEL_URI, SwitchOnOven.class)
                    });
            imported.put(
                    SwitchOffOven.class,
                    new EventSink[] {
                            new EventSink(OVEN_ELECTRICITY_MODEL_URI, SwitchOffOven.class)
                    });
        }

        // Variable bindings between exporting and importing models
        Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource,VariableSink[]>();

        // Bindings between components models to the electric meter model
        bindings.put(
                new VariableSource("currentConsumption",
                        Double.class,
                        OVEN_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentOvenConsumption",
                                Double.class,
                                ElectricMeterElectricityModel.URI)
                });
/*
        bindings.put(
                new VariableSource("currentIntensity",
                        Double.class,
                        HEATER_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentHeaterIntensity",
                                Double.class,
                                ElectricMeterElectricityModel.URI)
                });
*/

        // Coupled model descriptor: an HIOA requires a RTCoupledHIOA_Descriptor
        coupledModelDescriptors.put(
                ElectricMeterCoupledModel.URI,
                new RTCoupledHIOA_Descriptor(
                        ElectricMeterCoupledModel.class,
                        ElectricMeterCoupledModel.URI,
                        submodels,
                        imported,
                        null,
                        null,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        null,
                        bindings,
                        accFactor));

        // This sets the architecture in the plug-in for further reference and use
        this.setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        ElectricMeterCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor));
    }

}
