package eco_logis.equipments.oven.sil;

import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.OvenElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the SIL model for the electricity model in the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenElectricitySILModel
    extends OvenElectricityModel
{

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
