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


    /** @see OvenCI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        return ((OvenCI) getConnector()).isOn();
    }

    /** @see OvenCI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        ((OvenCI) getConnector()).powerOn();
    }

    /** @see OvenCI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        ((OvenCI) getConnector()).powerOff();
    }

    /** @see OvenCI#getCurrentTemperature() */
    @Override
    public double getCurrentTemperature() throws Exception {
        return ((OvenCI) getConnector()).getCurrentTemperature();
    }

    /** @see OvenCI#getTargetTemperature() */
    @Override
    public double getTargetTemperature() throws Exception {
        return ((OvenCI) getConnector()).getTargetTemperature();
    }

    /** @see OvenCI#setTargetTemperature(double) */
    @Override
    public void setTargetTemperature(double targetTemp) throws Exception {
        ((OvenCI) getConnector()).setTargetTemperature(targetTemp);
    }

}
