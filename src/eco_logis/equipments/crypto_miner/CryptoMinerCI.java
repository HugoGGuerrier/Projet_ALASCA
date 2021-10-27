package eco_logis.equipments.crypto_miner;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This interface represents the crypto miner services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface CryptoMinerCI
    extends CryptoMinerImplementationI, RequiredCI, OfferedCI
{

    /** @see CryptoMinerImplementationI#isOn() */
    @Override
    boolean isOn() throws Exception;

    /** @see CryptoMinerImplementationI#powerOn() */
    @Override
    void powerOn() throws Exception;

    /** @see CryptoMinerImplementationI#powerOff() */
    @Override
    void powerOff() throws Exception;

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    boolean isMining() throws Exception;

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    void startMiner() throws Exception;

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    void stopMiner() throws Exception;

}
