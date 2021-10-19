package equipments.wind_turbine;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class WindTurbineTestConnector
    extends AbstractConnector
    implements WindTurbineCI {

    // ========== Override methods ==========

    /** @see WindTurbineImplementationI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        return ((WindTurbineCI) offering).isTurning();
    }

    /** @see WindTurbineImplementationI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        return ((WindTurbineCI) offering).isBlocked();
    }

    /** @see WindTurbineImplementationI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        return ((WindTurbineCI) offering).getCurrentProduction();
    }

    /** @see WindTurbineImplementationI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        ((WindTurbineCI) offering).blockTurning();
    }

    /** @see WindTurbineImplementationI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        ((WindTurbineCI) offering).unblockTurning();
    }

}
