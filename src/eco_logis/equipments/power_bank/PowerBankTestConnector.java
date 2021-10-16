package equipments.power_bank;

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

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        ((PowerBankCI) offering).startCharging();
    }

    /** @see PowerBankImplementationI#stopCharging() */
    @Override
    public void stopCharging() throws Exception {
        ((PowerBankCI) offering).stopCharging();
    }

    /** @see PowerBankImplementationI#isCharging() */
    @Override
    public boolean isCharging() throws Exception {
        return ((PowerBankCI) offering).isCharging();
    }

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        ((PowerBankCI) offering).startDischarging();
    }

    /** @see PowerBankImplementationI#stopDischarging() */
    @Override
    public void stopDischarging() throws Exception {
        ((PowerBankCI) offering).stopDischarging();
    }

    /** @see PowerBankImplementationI#isDischarging() */
    @Override
    public boolean isDischarging() throws Exception {
        return ((PowerBankCI) offering).isDischarging();
    }

    /** @see PowerBankImplementationI#getBatteryLevel() () */
    @Override
    public double getBatteryLevel() throws Exception {
        return ((PowerBankCI) offering).getBatteryLevel();
    }
}
