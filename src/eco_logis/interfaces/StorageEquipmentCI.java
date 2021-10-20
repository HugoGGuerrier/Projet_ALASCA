package interfaces;

public interface StorageEquipmentCI
    extends ProductionEquipmentCI
{

    /**
     * Start the discharging phase, enable the energy production and return the success
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code !isProducing()}
     * post	{@code isProducing()}
     * </pre>
     *
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean startDischarging() throws Exception;

    /**
     * Stop the discharging phase, disable the energy production and return success
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code isProducing()}
     * post	{@code !isProducing()}
     * </pre>
     *
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean stopDischarging() throws Exception;

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
    boolean startCharging() throws Exception;

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
    boolean stopCharging() throws Exception;

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
