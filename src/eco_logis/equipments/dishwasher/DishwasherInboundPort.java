package equipments.dishwasher;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for the dishwasher services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DishwasherInboundPort
    extends AbstractInboundPort
    implements DishwasherCI
{

    // ========== Constructors ==========


    /**
     * Create a new dishwasher inbound port with the wanted owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherInboundPort(ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }

    /**
     * Create a new dishwasher inbound port with the wanted URI and owner component
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, DishwasherCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).isWashing()
        );
    }

    /** @see DishwasherCI#startWasherFull() */
    @Override
    public void startWasherFull() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Dishwasher) o).startWasherFull();
                    return null;
                }
        );
    }

    /** @see DishwasherCI#startWasherEco() */
    @Override
    public void startWasherEco() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Dishwasher) o).startWasherEco();
                    return null;
                }
        );
    }

    /** @see DishwasherCI#startWasherFast() */
    @Override
    public void startWasherFast() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Dishwasher) o).startWasherFast();
                    return null;
                }
        );
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Dishwasher) o).stopWashing();
                    return null;
                }
        );
    }

    /** @see DishwasherCI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).getProgram()
        );
    }

}
