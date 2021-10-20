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

}
