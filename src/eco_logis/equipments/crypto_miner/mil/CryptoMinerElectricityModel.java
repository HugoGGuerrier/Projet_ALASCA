package eco_logis.equipments.crypto_miner.mil;


import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the electricity model of a crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SwitchOnCryptoMiner.class,
        SwitchOffCryptoMiner.class,
        MineOnCryptoMiner.class,
        MineOffCryptoMiner.class
})
public class CryptoMinerElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = CryptoMinerElectricityModel.class.getSimpleName();

    /** The consumption of the crypto miner when it's currently mining */
    private static final double MINING_CONSUMPTION = 1200.0; // Watts

    /** The consumption of the crypto miner when it's in standby mode */
    private static final double STANDBY_CONSUMPTION = 50.0; // Watts


    // ========== Attributes ==========


    /** If the miner is currently on */
    private boolean isOn;

    /** If the miner is currently mining */
    private boolean isMining;

    /** The current consumption of the miner in a shared var */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========

    /**
     * Create a new crypto miner electricity model
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The electricity model URI
     * @param timeUnit The simulation time unit
     * @param engine The simulation engine
     *
     * @throws Exception TODO
     */
    public CryptoMinerElectricityModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
    }


    // ========== Override methods ==========


    @Override
    public ArrayList<EventI> output() {
        // The model does not export events
        return null;
    }

    @Override
    public Duration timeAdvance() {
        return null;
    }
}
