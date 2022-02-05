package eco_logis.equipments.oven;


/**
 * The interface <code>OvenImplementationI</code> defines the signatures
 * of services implemented by the oven component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface OvenImplementationI
{

    /**
     * Get if the oven is on
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return If the oven is on
     * @throws Exception TODO
     */
    boolean isOn() throws Exception;

    /**
     * Power on the oven
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
     * Power off the oven
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
     * Get the oven temperature (Celsius degrees °C)
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return the temperature of the oven (°C)
     * @throws Exception TODO
     */
    double getCurrentTemperature() throws Exception;

    /**
     * Get the oven target temperature (Celsius degrees °C)
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return the target temperature of the oven (°C)
     * @throws Exception TODO
     */
    double getTargetTemperature() throws Exception;

    /**
     * Set the oven temperature to the given temperature (Celsius degrees °C)
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code isBaking()}
     * post	true
     * </pre>
     *
     * @param targetTemp the target temperature of the oven (°C)
     * @throws Exception TODO
     */
    void setTargetTemperature(double targetTemp) throws Exception;

}
