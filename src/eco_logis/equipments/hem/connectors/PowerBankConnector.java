package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.hem.StorageEquipmentOutboundPort;
import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.power_bank.PowerBankCI;
import eco_logis.interfaces.StorageEquipmentControlCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is an integration test connector for the power bank component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PowerBankConnector
    extends AbstractConnector
    implements StorageEquipmentControlCI
{

    // ========== Override methods ==========


    /** @see StorageEquipmentOutboundPort#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((PowerBankCI) offering).getCurrentState() == PowerBank.State.DISCHARGING;
    }

    /** @see StorageEquipmentOutboundPort#startProducing() */
    @Override
    public boolean startProducing() throws Exception {
        ((PowerBankCI) offering).startDischarging();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#stopProducing() */
    @Override
    public boolean stopProducing() throws Exception {
        ((PowerBankCI) offering).standBy();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#isConsuming() */
    @Override
    public boolean isConsuming() throws Exception {
        return ((PowerBankCI) offering).getCurrentState() == PowerBank.State.CHARGING;
    }

    /** @see StorageEquipmentOutboundPort#startConsuming() */
    @Override
    public boolean startConsuming() throws Exception {
        ((PowerBankCI) offering).startCharging();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#stopConsuming() */
    @Override
    public boolean stopConsuming() throws Exception {
        ((PowerBankCI) offering).standBy();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#getPowerLevel() */
    @Override
    public double getPowerLevel() throws Exception {
        return ((PowerBankCI) offering).getBatteryLevel();
    }
}
