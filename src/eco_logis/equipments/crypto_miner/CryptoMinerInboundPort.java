package eco_logis.equipments.crypto_miner;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for a crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerInboundPort
    extends AbstractInboundPort
    implements CryptoMinerCI
{

    // ========== Constructors ==========


    /**
     * Create a new crypto miner inbound port with its owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception
     */
    public CryptoMinerInboundPort(ComponentI owner) throws Exception {
        super(CryptoMinerCI.class, owner);
    }

    /**
     * Create a new crypto miner inbound port with its uri and owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception
     */
    public CryptoMinerInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, CryptoMinerCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see CryptoMinerCI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        return getOwner().handleRequest(
                o -> ((CryptoMiner) o).isOn()
        );
    }

    /** @see CryptoMinerCI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((CryptoMiner) o).powerOn();
                    return null;
                }
        );
    }

    /** @see CryptoMinerCI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((CryptoMiner) o).powerOff();
                    return null;
                }
        );
    }

    /** @see CryptoMinerCI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        return getOwner().handleRequest(
                o -> ((CryptoMiner) o).isMining()
        );
    }

    /** @see CryptoMinerCI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((CryptoMiner) o).startMiner();
                return null;
            }
        );
    }

    /** @see CryptoMinerCI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((CryptoMiner) o).stopMiner();
                return null;
            }
        );
    }

}
