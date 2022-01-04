package eco_logis.equipments.oven.sil;

import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.OvenTemperatureModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>OvenTemperatureSILModel</code> extends the base
 * {@code OvenTemperatureModel} to cater for its execution inside
 * the {@code Oven} component.
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
public class OvenTemperatureSILModel
    extends OvenTemperatureModel
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;


    // ========== Attributes ==========


    /** Owner component */
    protected Oven owner;


    // ========== Constructors ==========


    /**
     * Create an oven temperature model instance.
     *
     * <p><strong>Contract</strong></p>
     *
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
    public OvenTemperatureSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Class methods ==========


    /**
     * Return the current oven temperature.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return the current oven temperature
     */
    public double getCurrentTemperature()
    {
        return this.currentTemperature;
    }


    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);

        // Retrieve the reference to the owner component that must be passed as a simulation run parameter
        assert simParams.containsKey(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.owner = (Oven) simParams.get(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        // Replace the logger set in the superclass by the one directing logs to the owner component logger
        this.setLogger(new StandardComponentLogger(this.owner));
    }

}
