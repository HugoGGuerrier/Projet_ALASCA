package eco_logis.interfaces;

/**
 * This interface defines all services for a storage equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface StorageEquipmentCI
    extends ProductionEquipmentCI
{

    /**
     * Get if the equipment is currently consuming power
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return If the equipment is charging
     * @throws Exception TODO
     */
    boolean isConsuming() throws Exception;

    /**
     * Start charging the storage device and return if the consuming state changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code !isConsuming()}
     * post	{@code isConsuming()}
     * </pre>
     *
     * @return If the charging hsa started
     * @throws Exception TODO
     */
    boolean startConsuming() throws Exception;

    /**
     * Stop charging the device and return if it worked
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code isConsuming()}
     * post	{@code !isConsuming()}
     * </pre>
     *
     * @return If the operation has succeeded
     * @throws Exception TODO
     */
    boolean stopConsuming() throws Exception;

    /**
     * Get the equipment consumption on the moment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code isConsuming()}
     * post	true
     * </pre>
     *
     * @return The consumption in Watt
     * @throws Exception TODO
     */
    double getConsumption() throws Exception;

    /**
     * Get the equipment energy level
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return The energy level between 0 and 1
     * @throws Exception TODO
     */
    float getPowerLevel() throws Exception;

}
