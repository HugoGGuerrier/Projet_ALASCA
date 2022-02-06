package eco_logis.equipments.dishwasher;

import java.time.Duration;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

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
     * Get the current planned washing program
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned()}
     * post true
     * </pre>
     *
     * @return The current planned washing program
     * @throws Exception TODO
     */
    DishwasherProgram getProgram() throws Exception;

    /**
     * Get the current planned washing program duration
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned()}
     * post true
     * </pre>
     *
     * @return The current planned washing program duration
     * @throws Exception TODO
     */
    Duration getProgramDuration() throws Exception;

    /**
     * Get the current deadline
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned()}
     * post true
     * </pre>
     *
     * @return The current deadline
     * @throws Exception TODO
     */
    LocalTime getDeadline() throws Exception;

    /**
     * Get the current planned start time of the equipment
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned()}
     * post true
     * </pre>
     *
     * @return The current start time
     * @throws Exception TODO
     */
    LocalTime getStartTime() throws Exception;

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
     * pre {@code !isPlanned()}
     * pre {@code getProgram() == null}
     * post {@code getProgram() != null}
     * post {@code isPlanned()}
     * </pre>
     *
     * @param deadline The deadline to respect (when it has to be finished)
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean plan(LocalTime deadline) throws Exception;

    /**
     * Plan the dishwasher with chosen program and deadline
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code !isPlanned()}
     * pre {@code getProgram() == null}
     * post {@code getProgram() != null}
     * post {@code isPlanned()}
     * </pre>
     *
     * @param deadline The deadline to respect (when it has to be finished)
     * @param program The program/cycle of washing to launch
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception;

    /**
     * Cancel the plan of the dishwasher, stops washing if it was washing
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre {@code isPlanned()}
     * pre {@code getProgram() != null}
     * post {@code getProgram() == null}
     * post {@code !isPlanned() && !isWashing()}
     * </pre>
     *
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean cancel() throws Exception;

    /**
     * Postpone the planed program with the wanted duration and return if the plan has been changed
     * Precision up to seconds.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code isPlanned() && !isWashing()}
     * pre	{@code getStartTime() != null}
     * pre  {@code getStartTime() + getDuration() + duration < getDeadline()}
     * post	{@code startTime() = startTime() + duration}
     * </pre>
     *
     * @param duration != null, The duration to postpone (precision up to seconds)
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean postPone(Duration duration) throws Exception;

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
    boolean startWashing() throws Exception;

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
    boolean stopWashing() throws Exception;

}
