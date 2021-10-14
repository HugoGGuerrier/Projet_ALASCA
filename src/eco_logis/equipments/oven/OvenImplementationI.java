package equipments.oven;


/**
 * The interface <code>OvenImplementationI</code> defines the signatures
 * of services implemented by the oven component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface OvenImplementationI {

    /**
     * Turn on the oven, start baking
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code !isBaking()}
     * post	{@code isBaking()}
     * </pre>
     *
     * @throws Exception
     */
    void startBaking() throws Exception;

    /**
     * Turn off the oven, stop baking
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true // no precondition.
     * post	{@code !isBaking()}
     * </pre>
     *
     * @throws Exception
     */
    void stopBaking() throws Exception;

    /**
     * Get the current state of the oven (baking or not)
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return true if the oven is on/baking, false otherwise
     * @throws Exception
     */
    boolean isBaking() throws Exception;

    /**
     * Get the oven temperature (Celsius degrees °C)
     * @return the temperature (°C)
     * @throws Exception
     */
    double getTemperature() throws Exception;

    /**
     * Set the oven temperature to the given temperature (Celsius degrees °C)
     * @param temp the temperature (°C)
     * @throws Exception
     */
    void setTemperature(double temp) throws Exception;
}
