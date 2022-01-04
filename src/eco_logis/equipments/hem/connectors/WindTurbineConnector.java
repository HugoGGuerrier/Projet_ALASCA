package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.wind_turbine.WindTurbineCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.ProductionEquipmentControlCI;
import eco_logis.interfaces.UnpredictableProductionEquipmentControlCI;

/**
 * This class is an integration test connector for the wind turbine component (unpredictable producer)
 */
public class WindTurbineConnector
    extends AbstractConnector
    implements UnpredictableProductionEquipmentControlCI
{

    // ========== Override methods ==========


    /** @see UnpredictableProductionEquipmentControlCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((WindTurbineCI) offering).isTurning();
    }

    /** @see ProductionEquipmentControlCI#startProducing() */
    @Override
    public boolean startProducing() throws Exception {
        // Cannot happen ; unpredictable producer
        return false;
    }

    /** @see ProductionEquipmentControlCI#stopProducing() */
    @Override
    public boolean stopProducing() throws Exception {
        // Cannot happen ; unpredictable producer
        return false;
    }

    /** @see UnpredictableProductionEquipmentControlCI#isForbidden() */
    @Override
    public boolean isForbidden() throws Exception {
        return ((WindTurbineCI) offering).isBlocked();
    }

    /** @see UnpredictableProductionEquipmentControlCI#forbidProduction() */
    @Override
    public boolean forbidProduction() throws Exception {
        ((WindTurbineCI) offering).blockTurning();
        return true;
    }

    /** @see UnpredictableProductionEquipmentControlCI#allowProduction() */
    @Override
    public boolean allowProduction() throws Exception {
        ((WindTurbineCI) offering).unblockTurning();
        return true;
    }
}
