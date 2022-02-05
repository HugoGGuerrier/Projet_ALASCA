package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.hem.StandardEquipmentOutboundPort;
import eco_logis.equipments.oven.OvenCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.StandardEquipmentControlCI;

/**
 * This class is an integration test connector for the oven component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenConnector
    extends AbstractConnector
    implements StandardEquipmentControlCI
{

    // ========== Override methods ==========


    /** @see StandardEquipmentOutboundPort#on() */
    @Override
    public boolean on() throws Exception {
        return ((OvenCI) offering).isOn();
    }

    /** @see StandardEquipmentOutboundPort#switchOn() */
    @Override
    public boolean switchOn() throws Exception {
        ((OvenCI) offering).powerOn();
        return true;
    }

    /** @see StandardEquipmentOutboundPort#switchOff() */
    @Override
    public boolean switchOff() throws Exception {
        ((OvenCI) offering).powerOff();
        return false;
    }

}
