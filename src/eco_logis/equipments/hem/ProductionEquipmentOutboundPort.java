package equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.ProductionEquipmentCI;
import interfaces.StandardEquipmentCI;

/**
 * This class represents an outbound port for a production equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ProductionEquipmentOutboundPort
    extends AbstractOutboundPort
    implements ProductionEquipmentCI
{

    // ========== Constructors ==========


    /**
     * Create a new production equipment outbound port with the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public ProductionEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(ProductionEquipmentCI.class, owner);
    }

    /**
     * Create a new production equipment outbound port with the URI and the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public ProductionEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ProductionEquipmentCI.class, owner);
    }

    /**
     * Create a new production equipment outbound port with the implemented interface and the owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param implInterface The implemented interface
     * @param owner The port owner
     * @throws Exception TODO
     */
    protected ProductionEquipmentOutboundPort(Class<? extends RequiredCI> implInterface, ComponentI owner) throws Exception {
        super(implInterface, owner);
    }

    /**
     * Create a new production equipment outbound port with the URI, the implemented interface and the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param implInterface The implemented interface
     * @param owner The port owner
     * @throws Exception TODO
     */
    protected ProductionEquipmentOutboundPort(String uri, Class<? extends RequiredCI> implInterface, ComponentI owner) throws Exception {
        super(uri, implInterface, owner);
    }


    // ========== Override methods ==========


    /** @see ProductionEquipmentCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((ProductionEquipmentCI) getConnector()).isProducing();
    }

    /** @see ProductionEquipmentCI#getProduction() */
    @Override
    public double getProduction() throws Exception {
        assert isProducing() : new PreconditionException("getProduction() -> isProducing()");
        return ((ProductionEquipmentCI) getConnector()).getProduction();
    }

}
