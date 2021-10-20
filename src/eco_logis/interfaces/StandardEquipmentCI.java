package interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This class represents the standard services offered by a standard equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface StandardEquipmentCI
    extends RequiredCI
{

    /**
     * Return if the equipment si currently power on
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return If the equipment is on
     * @throws Exception TODO
     */
    boolean on() throws Exception;

    /**
     * Switch on the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code !on()}
     * post	{@code on()}
     * </pre>
     *
     * @return If the equipment has been switched on
     * @throws Exception TODO
     */
    boolean switchOn() throws Exception;

    /**
     * Switch off the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * post	{@code !on()}
     * </pre>
     *
     * @return If the equipment has been switched off
     * @throws Exception TODO
     */
    boolean switchOff() throws Exception;

    /**
     * Get the max mode for the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return The max mode represented by an integer
     * @throws Exception TODO
     */
    int maxMode() throws Exception;

    /**
     * Set the upper mode and return if the mode changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * post true
     * </pre>
     *
     * @return If the current mode has been changed
     * @throws Exception TODO
     */
    boolean upMode() throws Exception;

    /**
     * Set the lower mode and return if the mode changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * post true
     * </pre>
     *
     * @return If the current mode has been changed
     * @throws Exception TODO
     */
    boolean downMode() throws Exception;

    /**
     * Force the equipment to turn into a given mode adn return if the mode has been changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * pre  {@code mode <= maxMode()}
     * pre  {@code mode > 0}
     * post	true
     * </pre>
     *
     * @param mode The wanted mode
     * @return If the mode has been changed
     * @throws Exception TODO
     */
    boolean setMode(int mode) throws Exception;

    /**
     * Get the current equipment mode
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code on()}
     * post	true
     * </pre>
     *
     * @return The current mode
     * @throws Exception TODO
     */
    int currentMode() throws Exception;

}
