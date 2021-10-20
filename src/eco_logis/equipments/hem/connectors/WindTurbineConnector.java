package equipments.hem.connectors;

import equipments.wind_turbine.WindTurbineCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ProductionEquipmentCI;

/**
 * This class is an integration test connector for the wind turbine component
 */
public class WindTurbineConnector
    extends AbstractConnector
    implements ProductionEquipmentCI
{


    // ========== Override methods ==========


    /** @see ProductionEquipmentCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((WindTurbineCI) offering).isTurning();
    }

    /** @see ProductionEquipmentCI#getProduction() */
    @Override
    public double getProduction() throws Exception {
        return ((WindTurbineCI) offering).getCurrentProduction();
    }

}
