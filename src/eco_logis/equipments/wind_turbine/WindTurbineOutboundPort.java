package equipments.wind_turbine;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * This class represent an outbound port for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class WindTurbineOutboundPort
    extends AbstractOutboundPort
    implements WindTurbineCI {

    // ========== Constructors ==========

    /**
     * Create a new wind turbine outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception
     */
    public WindTurbineOutboundPort(ComponentI owner) throws Exception {
        super(WindTurbineCI.class, owner);
    }

    /**
     * Create a new wind turbine outbound port with its uri and owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port uri
     * @param owner The port owner
     * @throws Exception
     */
    public WindTurbineOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, WindTurbineCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see WindTurbineImplementationI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        return ((WindTurbineCI) getConnector()).isTurning();
    }

    /** @see WindTurbineImplementationI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        return ((WindTurbineCI) getConnector()).isBlocked();
    }

    /** @see WindTurbineImplementationI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        return ((WindTurbineCI) getConnector()).getCurrentProduction();
    }

    /** @see WindTurbineImplementationI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        ((WindTurbineCI) getConnector()).blockTurning();
    }

    /** @see WindTurbineImplementationI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        ((WindTurbineCI) getConnector()).unblockTurning();
    }

}
