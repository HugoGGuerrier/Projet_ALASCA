package equipments.generator;

/**
 * Interface of all methods that a generator should implement
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface GeneratorImplementationI {

    /**
     * Get if the generator is currently generating power
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre true
     * post true
     * </pre>
     *
     * @return True if the generator is currently running
     * @throws Exception TODO
     */
    boolean isRunning() throws Exception;

    /**
     * Start the generator
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isRunning()}
     * post {@code isRunning()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startGenerator() throws Exception;

    /**
     * Stop the generator
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code isRunning()}
     * post {@code !isRunning()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void stopGenerator() throws Exception;

    /**
     * Get the generator fuel level
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre true
     * post true
     * </pre>
     *
     * @throws Exception TODO
     */
    float getFuelLevel() throws Exception;

    /**
     * Refill the generator fuel tank
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isRunning()}
     * post {@code getFuelLevel() > 0.0}
     * </pre>
     *
     * @throws Exception TODO
     */
    void refill() throws Exception;

}
