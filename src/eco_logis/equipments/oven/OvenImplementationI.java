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
 * @author Emilie Siau
 * @author Hugo Guerrier
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
     * @throws Exception TODO
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
     * @throws Exception TODO
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
     * @throws Exception TODO
     * @return true if the oven is on/baking, false otherwise
     */
    boolean isBaking() throws Exception;

    /**
     * Get the oven temperature
     * @return the temperature (Celsius °C)
     * @throws Exception
     */
    double getTemperature() throws Exception;

    /**
     * Set the oven temperature to the given temperature
     * @param temp the temperature (Celsius °C)
     * @throws Exception
     */
    void setTemperature(double temp) throws Exception;
}
