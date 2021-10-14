package equipments.dishwasher;

/**
 * This interface represents all methods of the dishwasher component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface DishwasherImplementationI {

    /**
     * This enum represents all possible program of the dishwasher
     */
    enum DishwasherProgram {
        /** A full program, this is the standard */
        FULL,

        /** Ecological and economical program */
        ECO,

        /** GOTTA GO FAST!!! */
        FAST
    }

    /**
     * Get if the washer is currently washing some dishes
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre true
     * post true
     * </pre>
     *
     * @return True if the dishwasher is currently washing
     * @throws Exception TODO
     */
    boolean isWashing() throws Exception;

    /**
     * Start the dishwasher in the full program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isWashing()}
     * pre {@code getProgram() == null}
     * post {@code isWashing()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startWasherFull() throws Exception;

    /**
     * Start the dishwasher in the eco program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isWashing()}
     * pre {@code getProgram() == null}
     * post {@code isWashing()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startWasherEco() throws Exception;

    /**
     * Start the dishwasher fast program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isWashing()}
     * pre {@code getProgram() == null}
     * post {@code isWashing()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startWasherFast() throws Exception;

    /**
     * Stop the dishwasher now
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code isWashing()}
     * post {@code !isWashing()}
     * </pre>
     *
     * @throws Exception TODO
     */
    void stopWashing() throws Exception;

    /**
     * Get the current washing program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code isWashing()}
     * post true
     * </pre>
     *
     * @return The current washing program
     * @throws Exception TODO
     */
    DishwasherProgram getProgram() throws Exception;

}
