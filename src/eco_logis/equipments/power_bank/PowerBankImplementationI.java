package eco_logis.equipments.power_bank;

/**
 * The interface <code>PowerBankImplementationI</code> defines the signatures
 * of services implemented by the power bank component.
 *
 * <p><strong>Description</strong></p>
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-16</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface PowerBankImplementationI
{

    /**
     * Get the current state of the power bank
     *
     * @return The current state of the power bank
     * @throws Exception TODO
     */
    PowerBank.State getCurrentState() throws Exception;

    /**
     * Start charging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code currentState == STANDBY}
     * post	{@code currentState == CHARGING}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startCharging() throws Exception;

    /**
     * Start discharging the power bank
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code currentState == STANDBY}
     * post	{@code currentState == DISCHARGING}
     * </pre>
     *
     * @throws Exception TODO
     */
    void startDischarging() throws Exception;

    /**
     * Set the power bak to the standby mode
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code currentState == CHARGING || currentState == DISCHARGING}
     * post	{@code currentState == STANDBY}
     * </pre>
     *
     * @throws Exception TODO
     */
    void standBy() throws Exception;

    /**
     * Get the power bank percentage of energy stocked (battery level)
     *
     * <p><strong>Contract</strong></p>double
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return the battery level (percentage)
     * @throws Exception TODO
     */
    double getBatteryLevel() throws Exception;

}
