package equipments.hem;

import equipments.oven.OvenCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.StandardEquipmentCI;

public class OvenConnector
    extends AbstractConnector
    implements StandardEquipmentCI
{

    // ========== Override methods ==========


    /** @see StandardEquipmentOutboundPort#on() */
    @Override
    public boolean on() throws Exception {
        return ((OvenCI) offering).isBaking();
    }

    /** @see StandardEquipmentOutboundPort#switchOn() */
    @Override
    public boolean switchOn() throws Exception {
        ((OvenCI) offering).startBaking();
        return true;
    }

    /** @see StandardEquipmentOutboundPort#switchOff() */
    @Override
    public boolean switchOff() throws Exception {
        ((OvenCI) offering).stopBaking();
        return false;
    }

}
