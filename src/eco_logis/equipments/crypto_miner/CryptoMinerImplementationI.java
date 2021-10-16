package equipments.crypto_miner;

/**
 * This interface defines all services implemented by a crypto miner
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public interface CryptoMinerImplementationI {

    /**
     * Get if the miner is currently mining crypto-currency
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre true
     * post true
     * </pre>
     *
     * @return True if the miner is currently mining
     * @throws Exception
     */
    boolean isMining() throws Exception;

    /**
     * Start the miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isMining()}
     * post {@code isMining()}
     * </pre>
     *
     * @throws Exception
     */
    void startMiner() throws Exception;

    /**
     * Stop the miner from mining
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code isMining()}
     * post {@code !isMining()}
     * </pre>
     *
     * @throws Exception
     */
    void stopMiner() throws Exception;

}
