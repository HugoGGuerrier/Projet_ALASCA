package eco_logis.equipments.wind_turbine;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class WindTurbineTestConnector
    extends AbstractConnector
    implements WindTurbineCI
{

    // ========== Override methods ==========


    /** @see WindTurbineCI#isTurning() */
    @Override
    public boolean isTurning() throws Exception {
        return ((WindTurbineCI) offering).isTurning();
    }

    /** @see WindTurbineCI#isBlocked() */
    @Override
    public boolean isBlocked() throws Exception {
        return ((WindTurbineCI) offering).isBlocked();
    }

    /** @see WindTurbineCI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        return ((WindTurbineCI) offering).getCurrentProduction();
    }

    /** @see WindTurbineCI#blockTurning() */
    @Override
    public void blockTurning() throws Exception {
        ((WindTurbineCI) offering).blockTurning();
    }

    /** @see WindTurbineCI#unblockTurning() */
    @Override
    public void unblockTurning() throws Exception {
        ((WindTurbineCI) offering).unblockTurning();
    }

}
