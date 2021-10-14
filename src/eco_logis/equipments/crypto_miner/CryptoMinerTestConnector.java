package equipments.crypto_miner;

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

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        return ((CryptoMinerCI) offering).isMining();
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        ((CryptoMinerCI) offering).startMiner();
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        ((CryptoMinerCI) offering).stopMiner();
    }

}
