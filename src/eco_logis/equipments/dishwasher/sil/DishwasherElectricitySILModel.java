package eco_logis.equipments.dishwasher.sil;

import eco_logis.equipments.dishwasher.DishwasherRTAtomicSimulatorPlugin;
import eco_logis.equipments.dishwasher.mil.DishwasherElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the SIL model for the electricity model in the dishwasher
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DishwasherElectricitySILModel
    extends DishwasherElectricityModel {

    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see DishwasherElectricityModel#DishwasherElectricityModel(String, TimeUnit, SimulatorI) */
    public DishwasherElectricitySILModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
    }


    // ========== Override methods ==========


    /** @see DishwasherElectricityModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super method
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (AbstractComponent) simParams.get(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        setLogger(new StandardComponentLogger(owner));
    }


}
