package equipments.crypto_miner;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * This class represent a crypto-currency miner which can be powered on and off.
 * This is a suspensable component.
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@OfferedInterfaces(offered = {CryptoMinerCI.class})
public class CryptoMiner
    extends AbstractComponent
    implements CryptoMinerImplementationI
{

    // ========== Macros ==========

    public static final String INBOUND_PORT_URI = "CRYPTO-INBOUND-PORT-URI";
    public static final boolean VERBOSE = true;

    // ========== Attributes ==========

    private boolean isMining;
    private CryptoMinerInboundPort cmip;

    // ========== Constructors ==========

    /**
     * Create a new crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post	true
     * </pre>
     *
     * @throws Exception TODO
     */
    protected CryptoMiner() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String cryptoMinerInboundPortURI) throws Exception {
        super(1, 0);
        initialise(cryptoMinerInboundPortURI);
    }

    /**
     * Create a new crypto miner with the wanted inbound port URI and the reflection inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param reflectionInboundPortURI  The reflection inbound port URI
     * @param cryptoMinerInboundPortURI The inbound port URI
     * @throws Exception TODO
     */
    protected CryptoMiner(String reflectionInboundPortURI, String cryptoMinerInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 1, 0);
        initialise(cryptoMinerInboundPortURI);
    }

    // ========== Class methods ==========

    /**
     * Initialise the newly created crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code cryptoMinerInboundPortURI != null}
     * pre	{@code !cryptoMinerInboundPortURI.isEmpty()}
     * post {@code !isMining}
     * post	{@code cmip.isPublished()}
     * </pre>
     *
     * @param cryptoMinerInboundPortURI The crypto miner inbound port URI
     * @throws Exception TODO
     */
    protected void initialise(String cryptoMinerInboundPortURI) throws Exception {
        // Assert the URI consistence
        assert cryptoMinerInboundPortURI != null : new PreconditionException(
                "cryptoMinerInboundPortURI != null");
        assert !cryptoMinerInboundPortURI.isEmpty() : new PreconditionException(
                "!cryptoMinerInboundPortURI.isEmpty()");

        // Initialise the component
        isMining = false;

        // Create the inbound port
        cmip = new CryptoMinerInboundPort(cryptoMinerInboundPortURI, this);
        cmip.publishPort();

        // Create the trace
        if(CryptoMiner.VERBOSE) {
            tracer.get().setTitle("Crypto miner component");
            tracer.get().setRelativePosition(0, 0);
            toggleTracing();
        }
    }

    // ========== Override methods ==========

    /** @see CryptoMinerImplementationI#isMining() */
    @Override
    public boolean isMining() throws Exception {
        if(CryptoMiner.VERBOSE) {
            traceMessage("Crypto miner get mining : " + isMining);
        }

        return isMining;
    }

    /** @see CryptoMinerImplementationI#startMiner() */
    @Override
    public void startMiner() throws Exception {
        if(CryptoMiner.VERBOSE) {
            traceMessage("Start the crypto mining");
        }
        assert !isMining;
        isMining = true;
    }

    /** @see CryptoMinerImplementationI#stopMiner() */
    @Override
    public void stopMiner() throws Exception {
        if(CryptoMiner.VERBOSE) {
            traceMessage("Stop the crypto mining");
        }
        assert isMining;
        isMining = false;
    }

}
