package equipments.crypto_miner;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * This class represent an outbound port for the crypto miner component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerOutboundPort
    extends AbstractOutboundPort
    implements CryptoMinerCI
{

    // ========== Constructors ==========

    /**
     * Create a new crypto miner outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public CryptoMinerOutboundPort(ComponentI owner) throws Exception {
        super(CryptoMinerCI.class, owner);
    }

    /**
     * Create a new crypto miner outbound port with its uri and owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port uri
     * @param owner The port owner
     * @throws Exception TODO
     */
    public CryptoMinerOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, CryptoMinerCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        return ((CryptoMinerCI) getConnector()).isMining();
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        ((CryptoMinerCI) getConnector()).startMiner();
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        ((CryptoMinerCI) getConnector()).stopMiner();
    }

}
