package equipments.hem;

import equipments.crypto_miner.CryptoMinerCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.cyphy.hem2021.interfaces.SuspensionEquipmentControlCI;

/**
 * This class is an integration test connector for the crypto miner component
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

    /** @see SuspensionEquipmentControlCI#maxMode() */
    @Override
    public int maxMode() throws Exception {
        // Crypto miner has no mode for now, so return the sole mode
        return 1;
    }

    /** @see SuspensionEquipmentControlCI#upMode() */
    @Override
    public boolean upMode() throws Exception {
        // No mode, so no up
        return false;
    }

    /** @see SuspensionEquipmentControlCI#downMode() */
    @Override
    public boolean downMode() throws Exception {
        // No mode, so no down
        return false;
    }

    /** @see SuspensionEquipmentControlCI#setMode(int) */
    @Override
    public boolean setMode(int modeIndex) throws Exception {
        // No mode, so no set
        return false;
    }

    /** @see SuspensionEquipmentControlCI#currentMode() */
    @Override
    public int currentMode() throws Exception {
        // Return the only mode index
        return 1;
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
