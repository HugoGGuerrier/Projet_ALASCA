package equipments.hem.connectors;

import equipments.wind_turbine.WindTurbineCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.UnpredictableProductionEquipmentCI;

/**
 * This class is an integration test connector for the wind turbine component
 */
public class WindTurbineConnector
    extends AbstractConnector
    implements UnpredictableProductionEquipmentCI
{

    // ========== Override methods ==========


    /** @see UnpredictableProductionEquipmentCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((WindTurbineCI) offering).isTurning();
    }

    /** @see UnpredictableProductionEquipmentCI#getProduction() */
    @Override
    public double getProduction() throws Exception {
        return ((WindTurbineCI) offering).getCurrentProduction();
    }

    /** @see UnpredictableProductionEquipmentCI#isForbidden() */
    @Override
    public boolean isForbidden() throws Exception {
        return ((WindTurbineCI) offering).isBlocked();
    }

    /** @see UnpredictableProductionEquipmentCI#forbidProduction() */
    @Override
    public boolean forbidProduction() throws Exception {
        ((WindTurbineCI) offering).blockTurning();
        return true;
    }

    /** @see UnpredictableProductionEquipmentCI#allowProduction() */
    @Override
    public boolean allowProduction() throws Exception {
        ((WindTurbineCI) offering).unblockTurning();
        return true;
    }
}
