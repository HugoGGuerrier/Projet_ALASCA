package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.SuspensionEquipmentCI;

/**
 * This class represents an outbound port for a suspension equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SuspensionEquipmentOutboundPort
    extends StandardEquipmentOutboundPort
    implements SuspensionEquipmentCI
{

    // ========== Constructors ==========

    /**
     * Create a new suspension equipment outbound port with the wanted owner
     *
     * @see StandardEquipmentOutboundPort#StandardEquipmentOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public SuspensionEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(SuspensionEquipmentCI.class, owner);
    }

    /**
     * Create a new suspension equipment outbound port with the wanted URI and owner
     *
     * @see StandardEquipmentOutboundPort#StandardEquipmentOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public SuspensionEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, SuspensionEquipmentCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see SuspensionEquipmentCI#suspended() */
    @Override
    public boolean suspended() throws Exception {
        assert on() : new PreconditionException("suspended() -> on()");
        return ((SuspensionEquipmentCI) getConnector()).suspended();
    }

    /** @see SuspensionEquipmentCI#suspend() */
    @Override
    public boolean suspend() throws Exception {
        assert on() : new PreconditionException("suspend() -> on()");
        assert !suspended() : new PreconditionException("suspend() -> !suspended()");
        boolean res = ((SuspensionEquipmentCI) getConnector()).suspend();
        assert suspended() : new PostconditionException("suspend() -> suspended()");
        return res;
    }

    /** @see SuspensionEquipmentCI#resume() */
    @Override
    public boolean resume() throws Exception {
        assert on() : new PreconditionException("resume() -> on()");
        assert suspended() : new PreconditionException("resume() -> suspended()");
        boolean res = ((SuspensionEquipmentCI) getConnector()).resume();
        assert !suspended() : new PostconditionException("resume() -> !suspended()");
        return res;
    }

    /** @see SuspensionEquipmentCI#emergency() */
    @Override
    public double emergency() throws Exception {
        assert on() : new PreconditionException("emergency() -> on()");
        return ((SuspensionEquipmentCI) getConnector()).emergency();
    }

}
