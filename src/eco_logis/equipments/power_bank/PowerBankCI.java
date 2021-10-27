package eco_logis.equipments.power_bank;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * The component interface <code>PowerBankCI</code> defines the services a
 * power bank component offers and that can be required from it.
 *
 * <p><strong>Description</strong></p>
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant		true
 * </pre>
 * <p>Created on : 2021-10-16</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface PowerBankCI
    extends OfferedCI, RequiredCI, PowerBankImplementationI
{

    /** @see PowerBankImplementationI#isCharging() */
    @Override
    boolean isCharging() throws Exception;

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    void startCharging() throws Exception;

    /** @see PowerBankImplementationI#stopCharging() */
    @Override
    void stopCharging() throws Exception;

    /** @see PowerBankImplementationI#isDischarging() */
    @Override
    boolean isDischarging() throws Exception;

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    void startDischarging() throws Exception;

    /** @see PowerBankImplementationI#stopDischarging() */
    @Override
    void stopDischarging() throws Exception;

    /** @see PowerBankImplementationI#getBatteryLevel()  */
    @Override
    double getBatteryLevel() throws Exception;

    /** @see PowerBankImplementationI#setBatteryLevel(double)  */
    @Override
    void setBatteryLevel(double level) throws Exception;

}
