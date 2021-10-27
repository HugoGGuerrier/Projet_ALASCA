package eco_logis.equipments.oven;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * This class represent an outbound port for the oven component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenOutboundPort
    extends AbstractOutboundPort
    implements OvenCI
{

    // ========== Constructors ==========


    /**
     * Create a new oven outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public OvenOutboundPort(ComponentI owner) throws Exception {
        super(OvenCI.class, owner);
    }

    /**
     * Create a new oven outbound port with its uri and owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port uri
     * @param owner The port owner
     * @throws Exception TODO
     */
    public OvenOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, OvenCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see OvenCI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        return ((OvenCI) getConnector()).isBaking();
    }

    /** @see OvenCI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        ((OvenCI) getConnector()).startBaking();
    }

    /** @see OvenCI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        ((OvenCI) getConnector()).stopBaking();
    }

    /** @see OvenCI#getTemperature() */
    @Override
    public double getTemperature() throws Exception {
        return ((OvenCI) getConnector()).getTemperature();
    }

    /** @see OvenCI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) throws Exception {
        ((OvenCI) getConnector()).setTemperature(temp);
    }

}
