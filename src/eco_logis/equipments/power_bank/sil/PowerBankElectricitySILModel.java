package eco_logis.equipments.power_bank.sil;

import eco_logis.equipments.power_bank.PowerBankRTAtomicSimulatorPlugin;
import eco_logis.equipments.power_bank.mil.PowerBankElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PowerBankElectricitySILModel
    extends PowerBankElectricityModel
{

    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see PowerBankElectricityModel#PowerBankElectricityModel(String, TimeUnit, SimulatorI) */
    public PowerBankElectricitySILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========


    /** @see PowerBankElectricityModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super method
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (AbstractComponent) simParams.get(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        setLogger(new StandardComponentLogger(owner));
    }

}
