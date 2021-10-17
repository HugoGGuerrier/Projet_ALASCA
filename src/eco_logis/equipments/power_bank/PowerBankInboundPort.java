package equipments.power_bank;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for a PowerBankCI component interface
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PowerBankInboundPort
    extends AbstractInboundPort
    implements PowerBankCI {

    // ========== Constructors ==========

    /**
     * Create a new power bank inbound port with its owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception
     */
    public PowerBankInboundPort(ComponentI owner) throws Exception {
        super(PowerBankCI.class, owner);
    }

    /**
     * Create a new power bank inbound port with its uri and owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception
     */
    public PowerBankInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PowerBankCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see PowerBankImplementationI#isCharging() */
    @Override
    public boolean isCharging() throws Exception {
        return getOwner().handleRequest(
            o -> ((PowerBank) o).isCharging()
        );
    }

    /** @see PowerBankImplementationI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).startCharging();
                return null;
            }
        );
    }

    /** @see PowerBankImplementationI#stopCharging() */
    @Override
    public void stopCharging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).stopCharging();
                return null;
            }
        );
    }

    /** @see PowerBankImplementationI#isDischarging() */
    @Override
    public boolean isDischarging() throws Exception {
        return getOwner().handleRequest(
            o -> ((PowerBank) o).isDischarging()
        );
    }

    /** @see PowerBankImplementationI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).startDischarging();
                return null;
            }
        );
    }

    /** @see PowerBankImplementationI#stopDischarging() */
    @Override
    public void stopDischarging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).stopDischarging();
                return null;
            }
        );
    }

    /** @see PowerBankImplementationI#getBatteryLevel() */
    @Override
    public double getBatteryLevel() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((PowerBank) o).getBatteryLevel()
        );
    }

    /** @see PowerBankImplementationI#setBatteryLevel(double) */
    @Override
    public void setBatteryLevel(double level) throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).setBatteryLevel(level);
                return null;
            }
        );
    }
}