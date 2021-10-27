package eco_logis.interfaces;

/**
 * This interface defines all services for a suspension equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface SuspensionEquipmentCI
    extends StandardEquipmentCI
{

    /**
     * Get if the equipment is suspended
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * post	true
     * </pre>
     *
     * @return If the equipment is suspended
     * @throws Exception TODO
     */
    boolean suspended() throws Exception;

    /**
     * Suspend the equipment and return if the suspension state changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * pre  {@code !suspended()}
     * post	{@code suspended()}
     * </pre>
     *
     * @return If the suspension state changed
     * @throws Exception TODO
     */
    boolean suspend() throws Exception;

    /**
     * Resume a suspended equipment adn return if the suspension state changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * pre  {@code suspended()}
     * post	{@code !suspended()}
     * </pre>
     *
     * @return If the suspension state changed
     * @throws Exception TODO
     */
    boolean resume() throws Exception;

    /**
     * Get the emergency level for the resuming of the component
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * pre  {@code suspended()}
     * post	true
     * </pre>
     *
     * @return The emergency level represented by a double
     * @throws Exception TODO
     */
    double emergency() throws Exception;

}
