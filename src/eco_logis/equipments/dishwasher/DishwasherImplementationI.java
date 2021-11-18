package eco_logis.equipments.dishwasher;

import java.time.LocalTime;

/**
 * This interface represents all methods of the dishwasher component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface DishwasherImplementationI
{

    /** This enum represents all possible program/cycle of the dishwasher */
    enum DishwasherProgram {
        /** A full program, this is the standard */
        FULL,

        /** Ecological and economical program */
        ECO,

        /** GOTTA GO FAST!!! */
        FAST,

        /** Only rinse program */
        RINSE
    }

    /**
     * Get the current washing program
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned() || isWashing()}
     * post true
     * </pre>
     *
     * @return The current planned/washing program
     * @throws Exception TODO
     */
    DishwasherProgram getProgram() throws Exception;

    /**
     * Get if the washer is currently planned
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre true
     * post true
     * </pre>
     *
     * @return True if the dishwasher is currently planned
     * @throws Exception TODO
     */
    boolean isPlanned() throws Exception;

    /**
     * Plan the dishwasher with default program and deadline
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code !isWashing() && !isPlanned()}
     * pre {@code getProgram() == null}
     * post {@code getProgram() != null}
     * post {@code isPlanned()}
     * </pre>
     *
     * @param deadline The deadline to respect (when it has to be finished)
     * @return If the operation succeed
     * @throws Exception TODO
     */
    boolean plan(LocalTime deadline) throws Exception;

    /**
     * Plan the dishwasher with chosen program and deadline
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code !isWashing() && !isPlanned()}
     * pre {@code getProgram() == null}
     * post {@code getProgram() != null}
     * post {@code isPlanned()}
     * </pre>
     *
     * @param deadline The deadline to respect (when it has to be finished)
     * @param program The program/cycle of washing to launch
     * @return If the operation succeed
     * @throws Exception TODO
     */
    boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception;

    /**
     * Get if the washer is currently washing some dishes
     *
     * <p><strong>Contract</strong></p>
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
     * Start the dishwasher with default program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre {@code !isWashing() && isPlanned()}
     * post {@code isWashing() && !isPlanned}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startWashing() throws Exception;

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

}
