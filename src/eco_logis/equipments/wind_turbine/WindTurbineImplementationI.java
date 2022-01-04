package eco_logis.equipments.wind_turbine;

/**
 * This interface defines the wind turbine implementation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface WindTurbineImplementationI
{

    /**
     * Get if the wind turbine is turning
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return true if the turbine is currently turning, false otherwise
     * @throws Exception TODO
     */
    boolean isTurning() throws Exception;

    /**
     * Get if the wind turbine is blocked
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return true if the turbine is currently blocked, false otherwise
     * @throws Exception TODO
     */
    boolean isBlocked() throws Exception;

    /**
     * Get the wind turbine current production
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  true
     * post true
     * </pre>
     *
     * @return the current production (0 if not turning)
     * @throws Exception TODO
     */
    double getCurrentProduction() throws Exception;

    /**
     * Block the wind turbine from producing electricity
     * Can be useful for too strong winds.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code !isBlocked}
     * post {@code isBlocked && !isTurning}
     * </pre>
     *
     * @throws Exception TODO
     */
    void blockTurning() throws Exception;

    /**
     * Unblock the wind turbine from producing electricity
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre  {@code isBlocked && !isTurning}
     * post {@code !isBlocked}
     * </pre>
     *
     * @throws Exception TODO
     */
    void unblockTurning() throws Exception;

}
