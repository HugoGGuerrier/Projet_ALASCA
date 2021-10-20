package equipments.hem.connectors;

import equipments.wind_turbine.WindTurbineCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ProductionEquipmentCI;
import interfaces.UnpredictableProductionEquipmentCI;

/**
 * This class is an integration test connector for the wind turbine component (unpredictable producer)
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

    /** @see ProductionEquipmentCI#startProducing() */
    @Override
    public boolean startProducing() throws Exception {
        // Cannot happen ; unpredictable producer
        return false;
    }

    /** @see ProductionEquipmentCI#stopProducing() */
    @Override
    public boolean stopProducing() throws Exception {
        // Cannot happen ; unpredictable producer
        return false;
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
