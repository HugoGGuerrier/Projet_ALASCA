package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.crypto_miner.CryptoMinerCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.SuspensionEquipmentControlCI;

/**
 * This class is an integration test connector for the crypto miner component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerConnector
    extends AbstractConnector
    implements SuspensionEquipmentControlCI
{

    // ========== Override methods ==========


    /** @see SuspensionEquipmentControlCI#on() */
    @Override
    public boolean on() throws Exception {
        return ((CryptoMinerCI) offering).isOn();
    }

    /** @see SuspensionEquipmentControlCI#switchOn() */
    @Override
    public boolean switchOn() throws Exception {
        ((CryptoMinerCI) offering).powerOn();
        return true;
    }

    /** @see SuspensionEquipmentControlCI#switchOff() */
    @Override
    public boolean switchOff() throws Exception {
        ((CryptoMinerCI) offering).powerOff();
        return true;
    }

    /** @see SuspensionEquipmentControlCI#suspended() */
    @Override
    public boolean suspended() throws Exception {
        // If the miner is mining it is not suspended
        return !((CryptoMinerCI) offering).isMining();
    }

    /** @see SuspensionEquipmentControlCI#suspend() */
    @Override
    public boolean suspend() throws Exception {
        ((CryptoMinerCI) offering).stopMiner();
        return true;
    }

    /** @see SuspensionEquipmentControlCI#resume() */
    @Override
    public boolean resume() throws Exception {
        ((CryptoMinerCI) offering).startMiner();
        return true;
    }

    /** @see SuspensionEquipmentControlCI#emergency() */
    @Override
    public double emergency() throws Exception {
        // Mining crypto-currency is never a priority
        return 0d;
    }

}
