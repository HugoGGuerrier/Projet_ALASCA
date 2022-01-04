package eco_logis.equipments.power_bank;

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
    implements PowerBankCI
{

    // ========== Constructors ==========


    /**
     * Create a new power bank inbound port with its owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
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
     * @throws Exception TODO
     */
    public PowerBankInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PowerBankCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see PowerBankCI#getCurrentState() */
    @Override
    public PowerBank.State getCurrentState() throws Exception {
        return getOwner().handleRequest(
                o -> ((PowerBank) o).getCurrentState()
        );
    }

    /** @see PowerBankCI#startCharging() */
    @Override
    public void startCharging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).startCharging();
                return null;
            }
        );
    }

    /** @see PowerBankCI#startDischarging() */
    @Override
    public void startDischarging() throws Exception {
        getOwner().handleRequest(
            o -> {
                ((PowerBank) o).startDischarging();
                return null;
            }
        );
    }

    /** @see PowerBankCI#standBy() */
    @Override
    public void standBy() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((PowerBank) o).standBy();
                    return null;
                }
        );
    }

    /** @see PowerBankCI#getBatteryLevel() */
    @Override
    public double getBatteryLevel() throws Exception {
        return getOwner().handleRequest(
            o -> ((PowerBank) o).getBatteryLevel()
        );
    }

}
