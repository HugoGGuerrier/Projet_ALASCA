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

    /** @see StandardEquipmentOutboundPort#maxMode() */
    @Override
    public int maxMode() throws Exception {
        // Oven has only one sole mode for now
        return 1;
    }

    /** @see StandardEquipmentOutboundPort#upMode() */
    @Override
    public boolean upMode() throws Exception {
        // No mode, no up
        return false;
    }

    /** @see StandardEquipmentOutboundPort#downMode() */
    @Override
    public boolean downMode() throws Exception {
        // No mode, no down
        return false;
    }

    /** @see StandardEquipmentOutboundPort#setMode(int) */
    @Override
    public boolean setMode(int mode) throws Exception {
        // No mode, no set
        return false;
    }

    /** @see StandardEquipmentOutboundPort#currentMode() */
    @Override
    public int currentMode() throws Exception {
        // Return the only mode index
        return 1;
    }

}
