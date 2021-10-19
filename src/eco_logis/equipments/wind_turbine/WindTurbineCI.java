package equipments.wind_turbine;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This interface represents the wind turbine services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface WindTurbineCI
    extends WindTurbineImplementationI, RequiredCI, OfferedCI {

    /** @see WindTurbineImplementationI#isTurning() */
    @Override
    boolean isTurning() throws Exception;

    /** @see WindTurbineImplementationI#isBlocked() */
    @Override
    boolean isBlocked() throws Exception;

    /** @see WindTurbineImplementationI#getCurrentProduction() */
    @Override
    double getCurrentProduction() throws Exception;

    /** @see WindTurbineImplementationI#blockTurning() */
    @Override
    void blockTurning() throws Exception;

    /** @see WindTurbineImplementationI#unblockTurning() */
    @Override
    void unblockTurning() throws Exception;
}
