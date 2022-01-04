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
    implements PowerBankCI
{

    /** @see PowerBankCI#getCurrentState() */
    @Override
    public PowerBank.State getCurrentState() throws Exception {
        return ((PowerBankCI) offering).getCurrentState();
    }

    /** @see PowerBankCI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        ((PowerBankCI) offering).startCharging();
    }

    /** @see PowerBankCI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        ((PowerBankCI) offering).startDischarging();
    }

    /** @see PowerBankCI#standBy() */
    @Override
    public void standBy() throws Exception {
        ((PowerBankCI) offering).standBy();
    }

    /** @see PowerBankCI#getBatteryLevel() () */
    @Override
    public double getBatteryLevel() throws Exception {
        return ((PowerBankCI) offering).getBatteryLevel();
    }

}
