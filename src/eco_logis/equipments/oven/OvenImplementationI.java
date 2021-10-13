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
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public interface OvenImplementationI {

    /**
     * The enumeration <code>OvenState</code> describes the operation
     * states of the oven.
     *
     * <p><strong>Description</strong></p>
     *
     * <p>Created on : 2021-10-05</p>
     *
     * @author Emilie Siau
     * @author Hugo Guerrier
     */
    enum OvenState {
        /** Oven is on */
        ON,

        /** Oven is off */
        OFF
    }

    /**
     * Turn on the oven
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code getState() == OvenState.OFF}
     * post	{@code getState() == OvenState.ON}
     * </pre>
     *
     * @throws Exception TODO
     */
    void turnOn() throws Exception;

    /**
     * Turn off the oven
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	{@code getState() == OvenState.OFF}
     * </pre>
     *
     * @throws Exception TODO
     */
    void turnOff() throws Exception;

    /**
     * Get the current state of the oven
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @throws Exception TODO
     * @return the current state of the oven
     */
    OvenState getState() throws Exception;

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
