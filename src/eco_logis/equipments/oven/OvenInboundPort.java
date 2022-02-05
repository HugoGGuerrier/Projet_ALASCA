package eco_logis.equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.heater.HeaterImplementationI;
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


    /** @see OvenCI#isOn() */
    @Override
    public boolean isOn() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((Oven) o).isOn()
        );
    }

    /** @see OvenCI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((Oven) o).powerOn();
                return null;
            }
        );
    }

    /** @see OvenCI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((Oven) o).powerOff();
                return null;
            }
        );
    }

    /** @see OvenCI#getCurrentTemperature() */
    @Override
    public double getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequest(
                o -> ((Oven) o).getCurrentTemperature()
        );
    }

    /** @see OvenCI#getTargetTemperature() */
    @Override
    public double getTargetTemperature() throws Exception {
        return this.getOwner().handleRequest(
                o -> ((Oven) o).getTargetTemperature()
        );
    }

    /** @see OvenCI#setTargetTemperature(double) */
    @Override
    public void setTargetTemperature(double targetTemp) throws Exception {
        this.getOwner().handleRequest(
                o -> {	((HeaterImplementationI)o).setTargetTemperature(targetTemp);
                    return null;
                }
        );
    }

}
