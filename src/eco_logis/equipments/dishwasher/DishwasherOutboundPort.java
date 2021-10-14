package equipments.dishwasher;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class DishwasherOutboundPort
    extends AbstractOutboundPort
    implements DishwasherCI
{

    // ========== Constructors ==========

    /**
     * Create a new dishwasher outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherOutboundPort(ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }

    /**
     * Create a new dishwasher outbound port with its URI and its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherOutboundPort(String uri, ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) getConnector()).isWashing();
    }

    /** @see DishwasherImplementationI#startWasherFull() */
    @Override
    public void startWasherFull() throws Exception {

    }

    /** @see DishwasherImplementationI#startWasherEco() */
    @Override
    public void startWasherEco() throws Exception {

    }

    /** @see DishwasherImplementationI#startWasherFast() */
    @Override
    public void startWasherFast() throws Exception {

    }

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {

    }

    /** @see DishwasherImplementationI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return null;
    }

}
