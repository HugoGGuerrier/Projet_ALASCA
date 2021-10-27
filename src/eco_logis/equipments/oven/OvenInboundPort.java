package eco_logis.equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for an OvenCI component interface
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenInboundPort
        extends AbstractInboundPort
        implements OvenCI
{

    // ========== Constructors ==========


    /**
     * Create a new oven inbound port with its owner
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public OvenInboundPort(ComponentI owner) throws Exception {
        super(OvenCI.class, owner);
    }

    /**
     * Create a new oven inbound port with its uri and owner
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public OvenInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, OvenCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see OvenCI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((Oven) o).isBaking()
        );
    }

    /** @see OvenCI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((Oven) o).startBaking();
                return null;
            }
        );
    }

    /** @see OvenCI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((Oven) o).stopBaking();
                return null;
            }
        );
    }

    /** @see OvenCI#getTemperature() */
    @Override
    public double getTemperature() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((Oven) o).getTemperature()
        );
    }

    /** @see OvenCI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) throws Exception {
        this.getOwner().handleRequest(
            new AbstractComponent.AbstractService<Void>() {
                @Override
                public Void call() throws Exception {
                ((OvenImplementationI)
                    this.getServiceOwner()).setTemperature(temp);
                return null;
                }
            }
        );
    }

}
