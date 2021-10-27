package eco_logis.equipments.crypto_miner;

/**
 * This interface defines the crypto miner implementation
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public interface CryptoMinerImplementationI
{

    /**
     * Get if the crypto miner is on
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return If the crypto miner is on
     * @throws Exception TODO
     */
    boolean isOn() throws Exception;

    /**
     * Power on the crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code !isOn()}
     * post {@code isOn()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void powerOn() throws Exception;

    /**
     * Power off the crypto miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code isOn()}
     * post {@code !isOn()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void powerOff() throws Exception;

    /**
     * Get if the miner is currently mining crypto-currency
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return True if the miner is currently mining
     * @throws Exception TODO
     */
    boolean isMining() throws Exception;

    /**
     * Start the miner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code isOn()}
     * pre  {@code !isMining()}
     * post {@code isMining()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startMiner() throws Exception;

    /**
     * Stop the miner from mining
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code isOn()}
     * pre  {@code isMining()}
     * post {@code !isMining()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void stopMiner() throws Exception;

}
