package interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

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
