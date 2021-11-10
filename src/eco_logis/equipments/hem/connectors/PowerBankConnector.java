package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.hem.StorageEquipmentOutboundPort;
import eco_logis.equipments.power_bank.PowerBankCI;
import eco_logis.interfaces.StorageEquipmentCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is an integration test connector for the power bank component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PowerBankConnector
    extends AbstractConnector
    implements StorageEquipmentCI
{

    // ========== Override methods ==========


    /** @see StorageEquipmentOutboundPort#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((PowerBankCI) offering).isDischarging();
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
        ((PowerBankCI) offering).stopDischarging();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#isConsuming() */
    @Override
    public boolean isConsuming() throws Exception {
        return ((PowerBankCI) offering).isCharging();
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
        ((PowerBankCI) offering).stopCharging();
        return true;
    }

    /** @see StorageEquipmentOutboundPort#getPowerLevel() */
    @Override
    public double getPowerLevel() throws Exception {
        return ((PowerBankCI) offering).getBatteryLevel();
    }
}
