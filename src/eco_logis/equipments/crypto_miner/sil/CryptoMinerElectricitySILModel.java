package eco_logis.equipments.crypto_miner.sil;

import eco_logis.equipments.crypto_miner.CryptoMinerRTAtomicSimulatorPlugin;
import eco_logis.equipments.crypto_miner.mil.CryptoMinerElectricityModel;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the SIL model for the electricity model in the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerElectricitySILModel
    extends CryptoMinerElectricityModel
{

    // ========== Attributes ==========


    /** This is the SIL model owner */
    protected AbstractComponent owner;


    // ========== Constructors ==========


    /** @see CryptoMinerElectricityModel#CryptoMinerElectricityModel(String, TimeUnit, SimulatorI) */
    public CryptoMinerElectricitySILModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
    }


    // ========== Override methods ==========


    /** @see CryptoMinerElectricityModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super method
        super.setSimulationRunParameters(simParams);

        // Get the owner component in the simulation params
        assert simParams.containsKey(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (AbstractComponent) simParams.get(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger to the component logger
        setLogger(new StandardComponentLogger(owner));
    }

}
