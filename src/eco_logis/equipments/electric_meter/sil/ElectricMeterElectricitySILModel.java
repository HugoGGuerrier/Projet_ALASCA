package eco_logis.equipments.electric_meter.sil;

import eco_logis.equipments.electric_meter.ElectricMeter;
import eco_logis.equipments.electric_meter.ElectricMeterRTAtomicSimulatorPlugin;
import eco_logis.equipments.electric_meter.mil.ElectricMeterElectricityModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>ElectricMeterElectricitySILModel</code> extends the base
 * {@code ElectricMeterElectricityModel} to cater for its execution inside
 * the {@code ElectricMeter} component.
 *
 * <p><strong>Description</strong></p>
 * <p>
 * When executing MIL simulations, simulation models trace their actions by
 * printing messages using their own tracing service. When executed inside
 * components, it is better that the messages are printed using the component
 * trace service. To enable that, we use the {@code setSimulationRunParameters}
 * method to retrieve the reference on the component that is holding the
 * simulation model. Then, this reference can be used by a specific logger for
 * simulation models, {@code StandardComponentLogger}, that use the component
 * trace service rather than the standard simulation models tracing service.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ElectricMeterElectricitySILModel
    extends ElectricMeterElectricityModel
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;


    // ========== Attributes ==========


    /** Owner component */
    protected ElectricMeter owner;


    // ========== Constructors ==========


    /**
     * Create a electric meter electricity SIL model instance.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simulatedTimeUnit != null}
     * pre	{@code simulationEngine == null || simulationEngine instanceof HIOA_AtomicEngine}
     * post	{@code getURI() != null}
     * post	{@code uri != null implies this.getURI().equals(uri)}
     * post	{@code getSimulatedTimeUnit().equals(simulatedTimeUnit)}
     * post	{@code simulationEngine != null implies getSimulationEngine().equals(simulationEngine)}
     * post	{@code !isDebugModeOn()}
     * </pre>
     *
     * @param uri				URI of the model.
     * @param simulatedTimeUnit	time unit used for the simulation time.
     * @param simulationEngine	simulation engine to which the model is attached.
     * @throws Exception		<i>to do</i>.
     */
    public ElectricMeterElectricitySILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Class methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);
        // Retrieve the reference to the owner component that must be passed as a simulation run parameter
        assert simParams.containsKey(ElectricMeterRTAtomicSimulatorPlugin.METER_REFERENCE_NAME);
        this.owner = (ElectricMeter) simParams.get(ElectricMeterRTAtomicSimulatorPlugin.METER_REFERENCE_NAME);
        // Replace the logger set in the superclass by the one directing logs to the owner component logger
        this.setLogger(new StandardComponentLogger(this.owner));
    }


}
