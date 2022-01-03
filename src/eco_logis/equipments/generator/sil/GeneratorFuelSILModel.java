package eco_logis.equipments.generator.sil;

import eco_logis.equipments.generator.GeneratorRTAtomicSimulatorPlugin;
import eco_logis.equipments.generator.mil.GeneratorFuelModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the Generator fuel model for the SIL simulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorFuelSILModel
    extends GeneratorFuelModel
{

    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see GeneratorFuelModel#GeneratorFuelModel(String, TimeUnit, SimulatorI) */
    public GeneratorFuelSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========


    /** @see GeneratorFuelModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super method
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(GeneratorRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (AbstractComponent) simParams.get(GeneratorRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        setLogger(new StandardComponentLogger(owner));
    }

}
