package eco_logis.equipments.wind_turbine.sil;

import eco_logis.equipments.wind_turbine.WindTurbineRTAtomicSimulatorPlugin;
import eco_logis.equipments.wind_turbine.mil.WindTurbineElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the SIL model for the electricity model in the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class WindTurbineElectricitySILModel
    extends WindTurbineElectricityModel
{

    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see WindTurbineElectricityModel(String, TimeUnit, SimulatorI) */
    public WindTurbineElectricitySILModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
    }


    // ========== Override methods ==========


    /** @see WindTurbineElectricityModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super method
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (AbstractComponent) simParams.get(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        setLogger(new StandardComponentLogger(owner));
    }

}
