package eco_logis.equipments.power_bank;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the power bank
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PowerBankTestConnector
    extends AbstractConnector
    implements PowerBankCI {

    /** @see PowerBankCI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        ((PowerBankCI) offering).startCharging();
    }

    /** @see PowerBankCI#stopCharging() */
    @Override
    public void stopCharging() throws Exception {
        ((PowerBankCI) offering).stopCharging();
    }

    /** @see PowerBankCI#isCharging() */
    @Override
    public boolean isCharging() throws Exception {
        return ((PowerBankCI) offering).isCharging();
    }

    /** @see PowerBankCI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        ((PowerBankCI) offering).startDischarging();
    }

    /** @see PowerBankCI#stopDischarging() */
    @Override
    public void stopDischarging() throws Exception {
        ((PowerBankCI) offering).stopDischarging();
    }

    /** @see PowerBankCI#isDischarging() */
    @Override
    public boolean isDischarging() throws Exception {
        return ((PowerBankCI) offering).isDischarging();
    }

    /** @see PowerBankCI#getBatteryLevel() () */
    @Override
    public double getBatteryLevel() throws Exception {
        return ((PowerBankCI) offering).getBatteryLevel();
    }

    /** @see PowerBankCI#setBatteryLevel(double) */
    @Override
    public void setBatteryLevel(double level) throws Exception {
        ((PowerBankCI) offering).setBatteryLevel(level);
    }
}
