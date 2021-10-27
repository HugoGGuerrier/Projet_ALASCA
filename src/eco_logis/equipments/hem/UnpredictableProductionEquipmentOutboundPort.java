package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.UnpredictableProductionEquipmentCI;

/**
 * This class represents an outbound port for an unpredictable production equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class UnpredictableProductionEquipmentOutboundPort
        extends ProductionEquipmentOutboundPort
        implements UnpredictableProductionEquipmentCI
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
        super(UnpredictableProductionEquipmentCI.class, owner);
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
        super(uri, UnpredictableProductionEquipmentCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see UnpredictableProductionEquipmentCI#isForbidden() */
    @Override
    public boolean isForbidden() throws Exception {
        return ((UnpredictableProductionEquipmentCI) getConnector()).isForbidden();
    }

    /** @see UnpredictableProductionEquipmentCI#forbidProduction() */
    @Override
    public boolean forbidProduction() throws Exception {
        assert !isForbidden() : new PreconditionException("forbidProduction() -> !isForbidden()");
        return ((UnpredictableProductionEquipmentCI) getConnector()).forbidProduction();
    }

    /** @see UnpredictableProductionEquipmentCI#allowProduction() */
    @Override
    public boolean allowProduction() throws Exception {
        assert isForbidden() : new PreconditionException("allowProduction() -> isForbidden()");
        return ((UnpredictableProductionEquipmentCI) getConnector()).allowProduction();
    }
}
