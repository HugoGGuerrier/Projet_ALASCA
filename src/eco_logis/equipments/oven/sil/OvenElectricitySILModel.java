package eco_logis.equipments.oven.sil;

import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.OvenElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>OvenElectricitySILModel</code> extends the base
 * {@code OvenElectricityModel} to cater for its execution inside
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
public class OvenElectricitySILModel
    extends OvenElectricityModel
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;


    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see OvenElectricityModel#OvenElectricityModel(String, TimeUnit, SimulatorI) */
    public OvenElectricitySILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Class methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.owner = (AbstractComponent) simParams.get(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        this.setLogger(new StandardComponentLogger(this.owner));
    }

}
