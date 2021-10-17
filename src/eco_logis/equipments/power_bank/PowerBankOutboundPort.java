package equipments.power_bank;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * This class represent an outbound port for the power bank component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PowerBankOutboundPort
    extends AbstractOutboundPort
    implements PowerBankCI {

    // ========== Constructors ==========

    /**
     * Create a new power bank outbound port with its owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception
     */
    public PowerBankOutboundPort(ComponentI owner) throws Exception {
        super(PowerBankCI.class, owner);
    }

    /**
     * Create a new power bank outbound port with its uri and owner
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port uri
     * @param owner The port owner
     * @throws Exception
     */
    public PowerBankOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PowerBankCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see PowerBankImplementationI#isCharging() */
    @Override
    public boolean isCharging() throws Exception {
        return ((PowerBankCI) getConnector()).isCharging();
    }

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        ((PowerBankCI) getConnector()).startCharging();
    }

    /** @see PowerBankImplementationI#stopCharging() */
    @Override
    public void stopCharging() throws Exception {
        ((PowerBankCI) getConnector()).stopCharging();
    }

    /** @see PowerBankImplementationI#isDischarging() */
    @Override
    public boolean isDischarging() throws Exception {
        return ((PowerBankCI) getConnector()).isDischarging();
    }

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        ((PowerBankCI) getConnector()).startDischarging();
    }

    /** @see PowerBankImplementationI#stopDischarging() */
    @Override
    public void stopDischarging() throws Exception {
        ((PowerBankCI) getConnector()).stopDischarging();
    }

    /** @see PowerBankImplementationI#getBatteryLevel() */
    @Override
    public double getBatteryLevel() throws Exception {
        return ((PowerBankCI) getConnector()).getBatteryLevel();
    }

}
