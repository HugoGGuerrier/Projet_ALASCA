package equipments.hem;

import equipments.crypto_miner.CryptoMiner;
import equipments.hem.connectors.CryptoMinerConnector;
import equipments.hem.connectors.OvenConnector;
import equipments.hem.connectors.WindTurbineConnector;
import equipments.oven.Oven;
import equipments.wind_turbine.WindTurbine;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * The class <code>HEM</code> implements the basis for a household energy
 * management component.
 *
 * <p>Created on : 2021-10-17</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class HEM
    extends AbstractComponent
{

    // ========== Attributes ==========


    private SuspensionEquipmentOutboundPort cryptoMinerOP;
    private StandardEquipmentOutboundPort ovenOP;
    private UnpredictableProductionEquipmentOutboundPort windTurbineOP;


    // ========== Constructors ==========


    /**
     * Create a new HEM component
     */
    protected HEM() {
        super(1, 0);

        this.tracer.get().setTitle("Home Energy Manager component");
        this.tracer.get().setRelativePosition(2, 2);
        this.toggleTracing();
    }


    // ========== Lifecycle methods ==========


    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        try {
            // Create the crypto miner outbound port and connect it to the component
            cryptoMinerOP = new SuspensionEquipmentOutboundPort(this);
            cryptoMinerOP.publishPort();
            doPortConnection(
                    cryptoMinerOP.getPortURI(),
                    CryptoMiner.INBOUND_PORT_URI,
                    CryptoMinerConnector.class.getCanonicalName());

            // Create the oven outbound port and connect it to the component
            ovenOP = new StandardEquipmentOutboundPort(this);
            ovenOP.publishPort();
            doPortConnection(
                    ovenOP.getPortURI(),
                    Oven.INBOUND_PORT_URI,
                    OvenConnector.class.getCanonicalName());

            // Create the wind turbine outbound port and connect it to the component
            windTurbineOP = new UnpredictableProductionEquipmentOutboundPort(this);
            windTurbineOP.publishPort();
            doPortConnection(
                    windTurbineOP.getPortURI(),
                    WindTurbine.INBOUND_PORT_URI,
                    WindTurbineConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    @Override
    public synchronized void execute() throws Exception {
        // First scenario
        logMessage("The wind turbine is producing : " + windTurbineOP.isProducing());
        logMessage("User forbids wind turbine production : " + windTurbineOP.forbidProduction());
        logMessage("User powers on the crypto miner : " + cryptoMinerOP.switchOn());
        logMessage("User starts mining crypto-currency : " + cryptoMinerOP.resume());
        logMessage("User powers on its oven : " + ovenOP.switchOn());
        logMessage("The crypto miner is on : " + cryptoMinerOP.on());
        logMessage("The crypto miner is suspended : " + cryptoMinerOP.suspended());
        logMessage("HEM suspends the crypto miner : " + cryptoMinerOP.suspend());
        logMessage("User power of the oven : " + ovenOP.switchOff());
        logMessage("The oven is on : " + ovenOP.on());
        logMessage("HEM resumes the crypto miner : " + cryptoMinerOP.resume());
        logMessage("User power off the crypto miner : " + cryptoMinerOP.switchOff());
        logMessage("User allows wind turbine production : " + windTurbineOP.allowProduction());

    }

    @Override
    public synchronized void finalise() throws Exception {
        doPortDisconnection(cryptoMinerOP.getPortURI());
        doPortDisconnection(ovenOP.getPortURI());
        doPortDisconnection(windTurbineOP.getPortURI());
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            cryptoMinerOP.unpublishPort();
            ovenOP.unpublishPort();
            windTurbineOP.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

}
