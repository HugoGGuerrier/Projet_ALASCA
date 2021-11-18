package eco_logis.equipments.hem;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.dishwasher.Dishwasher;
import eco_logis.equipments.generator.Generator;
import eco_logis.equipments.hem.connectors.*;
import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.wind_turbine.WindTurbine;
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
    private PlanningEquipmentOutboundPort dishwasherOP;
    private ProductionEquipmentOutboundPort generatorOP;
    private StandardEquipmentOutboundPort ovenOP;
    private StorageEquipmentOutboundPort powerBankOP;
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

            // Create the dishwasher outbound port and connect it to the component
            dishwasherOP = new PlanningEquipmentOutboundPort(this);
            dishwasherOP.publishPort();
            doPortConnection(
                    dishwasherOP.getPortURI(),
                    Dishwasher.INBOUND_PORT_URI,
                    DishwasherConnector.class.getCanonicalName());

            // Create the generator outbound port and connect it to the component
            generatorOP = new ProductionEquipmentOutboundPort(this);
            generatorOP.publishPort();
            doPortConnection(
                    generatorOP.getPortURI(),
                    Generator.INBOUND_PORT_URI,
                    GeneratorConnector.class.getCanonicalName());

            // Create the oven outbound port and connect it to the component
            ovenOP = new StandardEquipmentOutboundPort(this);
            ovenOP.publishPort();
            doPortConnection(
                    ovenOP.getPortURI(),
                    Oven.INBOUND_PORT_URI,
                    OvenConnector.class.getCanonicalName());

            // Create the power bank outbound port and connect it to the component
            powerBankOP = new StorageEquipmentOutboundPort(this);
            powerBankOP.publishPort();
            doPortConnection(
                    powerBankOP.getPortURI(),
                    PowerBank.INBOUND_PORT_URI,
                    PowerBankConnector.class.getCanonicalName());

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
        logMessage("User start the generator : " + generatorOP.startProducing());
        logMessage("The wind turbine is producing : " + windTurbineOP.isProducing());
        logMessage("The power bank starts charging : " + powerBankOP.startConsuming());
        logMessage("The power bank is charging : " + powerBankOP.isConsuming());
        logMessage("User forbids wind turbine production : " + windTurbineOP.forbidProduction());
        logMessage("The power bank stops charging : " + powerBankOP.stopConsuming());
        logMessage("User powers on the crypto miner : " + cryptoMinerOP.switchOn());
        logMessage("User starts mining crypto-currency : " + cryptoMinerOP.resume());
        logMessage("User powers on its oven : " + ovenOP.switchOn());
        logMessage("The power bank starts discharging : " + powerBankOP.startProducing());
        logMessage("The power bank is discharging : " + powerBankOP.isProducing());
        logMessage("The crypto miner is on : " + cryptoMinerOP.on());
        logMessage("The generator is on : " + generatorOP.isProducing());
        logMessage("The crypto miner is suspended : " + cryptoMinerOP.suspended());
        logMessage("HEM suspends the crypto miner : " + cryptoMinerOP.suspend());
        logMessage("User power of the oven : " + ovenOP.switchOff());
        logMessage("The oven is on : " + ovenOP.on());
        logMessage("The power bank stops discharging : " + powerBankOP.stopProducing());
        logMessage("The power bank is charging : " + powerBankOP.isConsuming());
        logMessage("The power bank is discharging : " + powerBankOP.isProducing());
        logMessage("HEM resumes the crypto miner : " + cryptoMinerOP.resume());
        logMessage("User power off the crypto miner : " + cryptoMinerOP.switchOff());
        logMessage("User allows wind turbine production : " + windTurbineOP.allowProduction());
        logMessage("User stop the generator : " + generatorOP.stopProducing());
        // TODO dishwasher
        // TODO electric meter
    }

    @Override
    public synchronized void finalise() throws Exception {
        doPortDisconnection(cryptoMinerOP.getPortURI());
        doPortDisconnection(dishwasherOP.getPortURI());
        doPortDisconnection(generatorOP.getPortURI());
        doPortDisconnection(ovenOP.getPortURI());
        doPortDisconnection(powerBankOP.getPortURI());
        doPortDisconnection(windTurbineOP.getPortURI());
        // TODO electric meter
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            cryptoMinerOP.unpublishPort();
            dishwasherOP.unpublishPort();
            generatorOP.unpublishPort();
            ovenOP.unpublishPort();
            powerBankOP.unpublishPort();
            windTurbineOP.unpublishPort();
            // TODO electric meter
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

}
