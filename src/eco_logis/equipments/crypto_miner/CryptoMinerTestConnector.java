package eco_logis.equipments.crypto_miner;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerTestConnector
    extends AbstractConnector
    implements CryptoMinerCI
{

    // ========== Override methods ==========


    /** @see CryptoMinerCI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        return ((CryptoMinerCI) offering).isOn();
    }

    /** @see CryptoMinerCI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        ((CryptoMinerCI) offering).powerOn();
    }

    /** @see CryptoMinerCI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        ((CryptoMinerCI) offering).powerOff();
    }

    /** @see CryptoMinerCI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        return ((CryptoMinerCI) offering).isMining();
    }

    /** @see CryptoMinerCI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        ((CryptoMinerCI) offering).startMiner();
    }

    /** @see CryptoMinerCI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        ((CryptoMinerCI) offering).stopMiner();
    }

}
