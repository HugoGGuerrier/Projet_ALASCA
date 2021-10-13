package equipments.crypto_miner;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for a CryptoMinerCI component interface
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerInboundPort
    extends AbstractInboundPort
    implements CryptoMinerCI
{

    // ===== Constructors =====

    /**
     * Create a new crypto miner inbound port with its owner
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public CryptoMinerInboundPort(ComponentI owner) throws Exception {
        super(CryptoMinerCI.class, owner);
    }

    /**
     * Create a new crypto miner inbound port with its uri and owner
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public CryptoMinerInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, CryptoMinerCI.class, owner);
    }

    // ===== Override methods =====

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        return this.getOwner().handleRequest(
                o -> ((CryptoMiner) o).isMining()
        );
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        this.getOwner().handleRequest(
                o -> {
                    ((CryptoMiner) o).startMiner();
                    return null;
                }
        );
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        this.getOwner().handleRequest(
                o -> {
                    ((CryptoMiner) o).stopMiner();
                    return null;
                }
        );
    }

}
