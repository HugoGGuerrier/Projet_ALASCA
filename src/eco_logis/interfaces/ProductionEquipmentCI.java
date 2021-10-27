package eco_logis.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This interface represents a base production equipment services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface ProductionEquipmentCI
    extends RequiredCI
{

    /**
     * Get if the equipment is currently producing energy
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return If the equipment is producing
     * @throws Exception TODO
     */
    boolean isProducing() throws Exception;

    /**
     * Start the energy production and return if the operation succeeded
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
    boolean startProducing() throws Exception;

    /**
     * Stop the energy production and return if the operation succeeded
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
    boolean stopProducing() throws Exception;

    /**
     * Get the production on the moment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code isProducing()}
     * post	true
     * </pre>
     *
     * @return The production in Watt
     * @throws Exception TODO
     */
    double getProduction() throws Exception;

}
