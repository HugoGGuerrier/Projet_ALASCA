package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.ProductionEquipmentControlCI;

/**
 * This class represents an outbound port for a production equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ProductionEquipmentOutboundPort
    extends AbstractOutboundPort
    implements ProductionEquipmentControlCI
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
        super(ProductionEquipmentControlCI.class, owner);
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
        super(uri, ProductionEquipmentControlCI.class, owner);
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


    /** @see ProductionEquipmentControlCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((ProductionEquipmentControlCI) getConnector()).isProducing();
    }

    /** @see ProductionEquipmentControlCI#startProducing() */
    @Override
    public boolean startProducing() throws Exception {
        assert !isProducing() : new PreconditionException("startProducing() -> !isProducing()");
        boolean res = ((ProductionEquipmentControlCI) getConnector()).startProducing();
        assert isProducing() : new PostconditionException("startProducing() -> isProducing()");
        return res;
    }

    /** @see ProductionEquipmentControlCI#stopProducing() */
    @Override
    public boolean stopProducing() throws Exception {
        assert isProducing() : new PreconditionException("stopProducing() -> isProducing()");
        boolean res = ((ProductionEquipmentControlCI) getConnector()).stopProducing();
        assert !isProducing() : new PostconditionException("stopProducing() -> !isProducing()");
        return res;
    }


}
