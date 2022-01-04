package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.UnpredictableProductionEquipmentControlCI;

/**
 * This class represents an outbound port for an unpredictable production equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class UnpredictableProductionEquipmentOutboundPort
        extends ProductionEquipmentOutboundPort
        implements UnpredictableProductionEquipmentControlCI
{

    /**
     * Create a new unpredictable production equipment outbound port with the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public UnpredictableProductionEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(UnpredictableProductionEquipmentControlCI.class, owner);
    }

    /**
     * Create a new unpredictable production equipment outbound port with the URI and the port owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public UnpredictableProductionEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, UnpredictableProductionEquipmentControlCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see UnpredictableProductionEquipmentControlCI#isForbidden() */
    @Override
    public boolean isForbidden() throws Exception {
        return ((UnpredictableProductionEquipmentControlCI) getConnector()).isForbidden();
    }

    /** @see UnpredictableProductionEquipmentControlCI#forbidProduction() */
    @Override
    public boolean forbidProduction() throws Exception {
        assert !isForbidden() : new PreconditionException("forbidProduction() -> !isForbidden()");
        return ((UnpredictableProductionEquipmentControlCI) getConnector()).forbidProduction();
    }

    /** @see UnpredictableProductionEquipmentControlCI#allowProduction() */
    @Override
    public boolean allowProduction() throws Exception {
        assert isForbidden() : new PreconditionException("allowProduction() -> isForbidden()");
        return ((UnpredictableProductionEquipmentControlCI) getConnector()).allowProduction();
    }
}
