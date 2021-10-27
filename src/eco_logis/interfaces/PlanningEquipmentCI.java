package eco_logis.interfaces;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This interface defines all services offered by a planning equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface PlanningEquipmentCI
    extends StandardEquipmentCI
{

    /**
     * Get if the equipment has a plan
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return If the equipment has a plan
     * @throws Exception TODO
     */
    boolean hasPlan() throws Exception;

    /**
     * Get the planed start time of the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The start time
     * @throws Exception TODO
     */
    LocalTime startTime() throws Exception;

    /**
     * Get the program duration if the equipment has a plan
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The program duration
     * @throws Exception TODO
     */
    Duration duration() throws Exception;

    /**
     * Get the equipment deadline if it has a plan
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The deadline
     * @throws Exception TODO
     */
    LocalTime deadline() throws Exception;

    /**
     * Postpone the planed program with the wanted duration and return if the plan has been changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * pre  {@code startTime() + duration() + duration < deadline()}
     * post	{@code startTime() = startTime() + duration}
     * </pre>
     *
     * @param duration The duration to postpone
     * @return If the plan has been changed
     * @throws Exception TODO
     */
    boolean postPone(Duration duration) throws Exception;

    /**
     * Cancel the planned program and return if the plan has been modified
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	{@code !hasPlan()}
     * </pre>
     *
     * @return If the plan has been changed
     * @throws Exception TODO
     */
    boolean cancel() throws Exception;

}
