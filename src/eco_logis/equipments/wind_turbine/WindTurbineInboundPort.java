package eco_logis.equipments.wind_turbine;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for a wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class WindTurbineInboundPort
    extends AbstractInboundPort
    implements WindTurbineCI
{

    // ========== Constructors ==========


    /**
     * Create a new wind turbine inbound port with its owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public WindTurbineInboundPort(ComponentI owner) throws Exception {
        super(WindTurbineCI.class, owner);
    }

    /**
     * Create a new wind turbine inbound port with its uri and owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public WindTurbineInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, WindTurbineCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see WindTurbineCI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        return getOwner().handleRequest(
                o -> ((WindTurbine) o).isTurning()
        );
    }

    /** @see WindTurbineCI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        return getOwner().handleRequest(
                o -> ((WindTurbine) o).isBlocked()
        );
    }

    /** @see WindTurbineCI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        return getOwner().handleRequest(
                o -> ((WindTurbine) o).getCurrentProduction()
        );
    }

    /** @see WindTurbineCI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((WindTurbine) o).blockTurning();
                return null;
            }
        );
    }

    /** @see WindTurbineCI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((WindTurbine) o).unblockTurning();
                    return null;
                }
        );
    }

}
