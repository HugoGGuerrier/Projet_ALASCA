package equipments.hem.connectors;

import equipments.crypto_miner.CryptoMinerCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.SuspensionEquipmentCI;

/**
 * This class is an integration test connector for the crypto miner component
 */
public class CryptoMinerConnector
    extends AbstractConnector
    implements SuspensionEquipmentCI
{

    // ========== Override methods ==========


    /** @see SuspensionEquipmentCI#on() */
    @Override
    public boolean on() throws Exception {
        return ((CryptoMinerCI) offering).isOn();
    }

    /** @see SuspensionEquipmentCI#switchOn() */
    @Override
    public boolean switchOn() throws Exception {
        ((CryptoMinerCI) offering).powerOn();
        return true;
    }

    /** @see SuspensionEquipmentCI#switchOff() */
    @Override
    public boolean switchOff() throws Exception {
        ((CryptoMinerCI) offering).powerOff();
        return true;
    }

    /** @see SuspensionEquipmentCI#suspended() */
    @Override
    public boolean suspended() throws Exception {
        // If the miner is mining it is not suspended
        return !((CryptoMinerCI) offering).isMining();
    }

    /** @see SuspensionEquipmentCI#suspend() */
    @Override
    public boolean suspend() throws Exception {
        ((CryptoMinerCI) offering).stopMiner();
        return true;
    }

    /** @see SuspensionEquipmentCI#resume() */
    @Override
    public boolean resume() throws Exception {
        ((CryptoMinerCI) offering).startMiner();
        return true;
    }

    /** @see SuspensionEquipmentCI#emergency() */
    @Override
    public double emergency() throws Exception {
        // Mining crypto-currency is never a priority
        return 0d;
    }

}
