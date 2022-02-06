package eco_logis.equipments.hem;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.dishwasher.Dishwasher;
import eco_logis.equipments.electric_meter.ElectricMeter;
import eco_logis.equipments.electric_meter.ElectricMeterOutboundPort;
import eco_logis.equipments.generator.Generator;
import eco_logis.equipments.hem.connectors.*;
import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.wind_turbine.WindTurbine;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>HEM</code> implements the basis for a household energy
 * management component.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class HEM
    extends AbstractComponent
{

    // ========== Internal Enums and class ==========


    public enum OPType {
        SUSPENSION,
        PLANNING,
        PRODUCTION,
        STANDARD,
        STORAGE,
        UNPREDICTABLE
    }


    // ========== Attributes ==========


    /** Period at which the HEM looks at the current consumption and makes
     * energy management decisions */
    protected static final long MANAGEMENT_PERIOD = 1;

    /** Time unit to interpret {@code MANAGEMENT_PERIOD} */
    protected static final TimeUnit MANAGEMENT_PERIOD_TIME_UNIT = TimeUnit.SECONDS;

    /** true if the component executes in a unit test mode, false otherwise */
    protected boolean executesAsUnitTest;

    /** Future allowing to act upon the management task */
    protected Future<?> managementTaskFuture;

    private static final List<SuspensionEquipmentOutboundPort> suspOPs = new LinkedList<>();
    private static final List<PlanningEquipmentOutboundPort> planOPs = new LinkedList<>();
    private static final List<ProductionEquipmentOutboundPort> prodOPs = new LinkedList<>();
    private static final List<StandardEquipmentOutboundPort> standOPs = new LinkedList<>();
    private static final List<StorageEquipmentOutboundPort> storOPs = new LinkedList<>();
    private static final List<UnpredictableProductionEquipmentOutboundPort> unpredOPs = new LinkedList<>();

    /** Outbound port to call the electric meter */
    protected ElectricMeterOutboundPort elecMeterOP;

    /** Outbound port to call the crypto miner */
    protected SuspensionEquipmentOutboundPort cryptoOP;

    /** Outbound port to call the dishwasher */
    protected PlanningEquipmentOutboundPort dishwasherOP;

    /** Outbound port to call the generator */
    protected ProductionEquipmentOutboundPort generatorOP;

    /** Outbound port to call the oven */
    protected StandardEquipmentOutboundPort ovenOP;

    /** Outbound port to call the power bank */
    protected StorageEquipmentOutboundPort powerBankOP;

    /** Outbound port to call the wind turbine */
    protected UnpredictableProductionEquipmentOutboundPort windTurbineOP;


    // ========== Constructors ==========


    /**
     * Create a new HEM component
     */
    protected HEM(boolean executesAsUnitTest) {
        super(1, 0);
        this.executesAsUnitTest = executesAsUnitTest;
        this.tracer.get().setTitle("Home Energy Manager component");
        this.tracer.get().setRelativePosition(2, 2);
        this.toggleTracing();
    }


    // ========== Class methods ==========


    /**
     * Register a component from its outbound port
     *
     * @param type          the outbound port type
     * @param IP_URI        URI
     * @param connectorName the connector name
     * @throws Exception
     * @throws Exception TODO
     */
    public void register(OPType type, String IP_URI, String connectorName) throws Exception {

        switch (type) {
            case SUSPENSION:
                SuspensionEquipmentOutboundPort susp = new SuspensionEquipmentOutboundPort(this);
                suspOPs.add(susp);
                susp.publishPort();
                doPortConnection(
                        susp.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;

            case PLANNING:
                PlanningEquipmentOutboundPort plan = new PlanningEquipmentOutboundPort(this);
                planOPs.add(plan);
                plan.publishPort();
                doPortConnection(
                        plan.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;

            case PRODUCTION:
                ProductionEquipmentOutboundPort prod = new ProductionEquipmentOutboundPort(this);
                prodOPs.add(prod);
                prod.publishPort();
                doPortConnection(
                        prod.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;

            case STANDARD:
                StandardEquipmentOutboundPort stand = new StandardEquipmentOutboundPort(this);
                standOPs.add(stand);
                stand.publishPort();
                doPortConnection(
                        stand.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;

            case STORAGE:
                StorageEquipmentOutboundPort stor = new StorageEquipmentOutboundPort(this);
                storOPs.add(stor);
                stor.publishPort();
                doPortConnection(
                        stor.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;

            case UNPREDICTABLE:
                UnpredictableProductionEquipmentOutboundPort unpred = new UnpredictableProductionEquipmentOutboundPort(this);
                unpredOPs.add(unpred);
                unpred.publishPort();
                doPortConnection(
                        unpred.getPortURI(),
                        IP_URI,
                        connectorName
                );
                break;
        }
    }


    /**
     * First draft of the management task for the HEM.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @throws Exception <i>to do</i>.
     */
    protected void manage() throws Exception {
        this.traceMessage("Electric meter current consumption? " +
                this.elecMeterOP.getCurrentConsumption() + "\n");
        this.traceMessage("Electric meter current production? " +
                this.elecMeterOP.getCurrentProduction() + "\n");
    }

    // ========== Lifecycle methods ==========

    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        this.traceMessage("Home Energy Manager starts.\n");

        try {
            // Electric meter
            this.elecMeterOP = new ElectricMeterOutboundPort(this);
            this.elecMeterOP.publishPort();
            this.doPortConnection(
                    this.elecMeterOP.getPortURI(),
                    ElectricMeter.ELECTRIC_METER_INBOUND_PORT_URI,
                    ElectricMeterConnector.class.getCanonicalName());

            // Crypto miner
            this.cryptoOP = new SuspensionEquipmentOutboundPort(this);
            this.cryptoOP.publishPort();
            this.doPortConnection(
                    this.cryptoOP.getPortURI(),
                    CryptoMiner.INBOUND_PORT_URI,
                    CryptoMinerConnector.class.getCanonicalName());


            // Dishwasher
            this.dishwasherOP = new PlanningEquipmentOutboundPort(this);
            this.dishwasherOP.publishPort();
            this.doPortConnection(
                    this.dishwasherOP.getPortURI(),
                    Dishwasher.INBOUND_PORT_URI,
                    DishwasherConnector.class.getCanonicalName());

            // Generator
            this.generatorOP = new ProductionEquipmentOutboundPort(this);
            this.generatorOP.publishPort();
            this.doPortConnection(
                    this.generatorOP.getPortURI(),
                    Generator.INBOUND_PORT_URI,
                    GeneratorConnector.class.getCanonicalName());
/*
            // Oven
            this.ovenOP = new StandardEquipmentOutboundPort(this);
            this.ovenOP.publishPort();
            this.doPortConnection(
                    this.ovenOP.getPortURI(),
                    Oven.INBOUND_PORT_URI,
                    OvenConnector.class.getCanonicalName());
*/

            // Power bank
            this.powerBankOP = new StorageEquipmentOutboundPort(this);
            this.powerBankOP.publishPort();
            this.doPortConnection(
                    this.powerBankOP.getPortURI(),
                    PowerBank.INBOUND_PORT_URI,
                    PowerBankConnector.class.getCanonicalName());
/*
            // Wind turbine
            this.windTurbineOP = new UnpredictableProductionEquipmentOutboundPort(this);
            this.windTurbineOP.publishPort();
            this.doPortConnection(
                    this.windTurbineOP.getPortURI(),
                    WindTurbine.INBOUND_PORT_URI,
                    WindTurbineConnector.class.getCanonicalName());
*/
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    /** @see AbstractComponent#execute() */
    @Override
    public synchronized void execute() throws Exception {
        if (this.executesAsUnitTest) {  // Simplified integration testing
            // Electric meter
            this.traceMessage("Electric meter current consumption? " +
                    this.elecMeterOP.getCurrentConsumption() + "\n");
            this.traceMessage("Electric meter current production? " +
                    this.elecMeterOP.getCurrentProduction() + "\n");

            // Crypto miner
            this.traceMessage("Crypto miner is on? " +
                    this.cryptoOP.on() + "\n");
            this.traceMessage("Crypto miner is switched on? " +
                    this.cryptoOP.switchOn() + "\n");
            this.traceMessage("Crypto miner is suspended? " +
                    this.cryptoOP.suspended() + "\n");
            this.traceMessage("Crypto miner suspends? " +
                    this.cryptoOP.suspend() + "\n");
            this.traceMessage("Crypto miner emergency? " +
                    this.cryptoOP.emergency() + "\n");
            this.traceMessage("Crypto miner resumes? " +
                    this.cryptoOP.resume() + "\n");
            this.traceMessage("Crypto miner is suspended? " +
                    this.cryptoOP.suspended() + "\n");
            this.traceMessage("Crypto miner is switched off? " +
                    this.cryptoOP.switchOff() + "\n");
            this.traceMessage("Crypto miner is on? " +
                    this.cryptoOP.on() + "\n");

            // Dishwasher
            this.traceMessage("Dishwasher is on? " +
                    this.dishwasherOP.on() + "\n");
            // TODO : more and fix the switchOn method for dishwasher

            // Generator
            this.traceMessage("Generator is producing? " +
                    this.generatorOP.isProducing() + "\n");
            this.traceMessage("Generator starts producing? " +
                    this.generatorOP.startProducing() + "\n");
            this.traceMessage("Generator is producing? " +
                    this.generatorOP.isProducing() + "\n");
            this.traceMessage("Generator stops producing? " +
                    this.generatorOP.stopProducing() + "\n");
            this.traceMessage("Generator is producing? " +
                    this.generatorOP.isProducing() + "\n");

/*
            // Oven
            this.traceMessage("Oven is on? " +
                    this.ovenOP.on() + "\n");
            this.traceMessage("Oven is switched on? " +
                    this.ovenOP.switchOn() + "\n");
            this.traceMessage("Oven is switched off? " +
                    this.ovenOP.switchOff() + "\n");
            this.traceMessage("Oven is on? " +
                    this.ovenOP.on() + "\n");
 */

            // Power bank
            this.traceMessage("Power bank is producing? " +
                    this.powerBankOP.isProducing() + "\n");
            this.traceMessage("Power bank is consuming? " +
                    this.powerBankOP.isConsuming() + "\n");
            this.traceMessage("Power bank starts consuming? " +
                    this.powerBankOP.startConsuming() + "\n");
            this.traceMessage("Power bank is consuming? " +
                    this.powerBankOP.isConsuming() + "\n");
            this.traceMessage("Power bank stops consuming? " +
                    this.powerBankOP.stopConsuming() + "\n");
            this.traceMessage("Power bank starts producing? " +
                    this.powerBankOP.startProducing() + "\n");
            this.traceMessage("Power bank is producing? " +
                    this.powerBankOP.isProducing() + "\n");
            this.traceMessage("Power bank stops producing? " +
                    this.powerBankOP.stopProducing() + "\n");
/*
            // Wind turbine
            this.traceMessage("Wind turbine is producing? " +
                    this.windTurbineOP.isProducing() + "\n");
            this.traceMessage("Wind turbine is forbidden? " +
                    this.windTurbineOP.isForbidden() + "\n");
*/
        } else {
            final HEM hem = this;
            this.managementTaskFuture =
                    this.scheduleTaskAtFixedRateOnComponent(
                            new AbstractTask() {
                                @Override
                                public void run() {
                                    try {
                                        hem.manage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            MANAGEMENT_PERIOD,
                            MANAGEMENT_PERIOD,
                            MANAGEMENT_PERIOD_TIME_UNIT);
        }
    }

    @Override
    public synchronized void finalise() throws Exception {
        /* TODO
        for(SuspensionEquipmentOutboundPort s : suspOPs) doPortDisconnection(s.getPortURI());
        for(PlanningEquipmentOutboundPort p : planOPs) doPortDisconnection(p.getPortURI());
        for(ProductionEquipmentOutboundPort p : prodOPs) doPortDisconnection(p.getPortURI());
        for(StandardEquipmentOutboundPort s : standOPs) doPortDisconnection(s.getPortURI());
        for(StorageEquipmentOutboundPort s : storOPs) doPortDisconnection(s.getPortURI());
        for(UnpredictableProductionEquipmentOutboundPort u : unpredOPs) doPortDisconnection(u.getPortURI());
        */

        if (this.managementTaskFuture != null && !this.managementTaskFuture.isCancelled()) {
            this.managementTaskFuture.cancel(true);
        }

        this.doPortDisconnection(this.elecMeterOP.getPortURI());
        this.doPortDisconnection(this.cryptoOP.getPortURI());
        this.doPortDisconnection(this.dishwasherOP.getPortURI());
        this.doPortDisconnection(this.generatorOP.getPortURI());
        //this.doPortDisconnection(this.ovenOP.getPortURI());
        this.doPortDisconnection(this.powerBankOP.getPortURI());
        //this.doPortDisconnection(this.windTurbineOP.getPortURI());

        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        this.traceMessage("Home Energy Manager stops.\n");

        try {
            /* TODO
            for(SuspensionEquipmentOutboundPort s : suspOPs) s.unpublishPort();
            for(PlanningEquipmentOutboundPort p : planOPs) p.unpublishPort();
            for(ProductionEquipmentOutboundPort p : prodOPs) p.unpublishPort();
            for(StandardEquipmentOutboundPort s : standOPs) s.unpublishPort();
            for(StorageEquipmentOutboundPort s : storOPs) s.unpublishPort();
            for(UnpredictableProductionEquipmentOutboundPort u : unpredOPs) u.unpublishPort();
            */
            this.elecMeterOP.unpublishPort();
            this.cryptoOP.unpublishPort();
            this.dishwasherOP.unpublishPort();
            this.generatorOP.unpublishPort();
            //this.ovenOP.unpublishPort();
            this.powerBankOP.unpublishPort();
            //this.windTurbineOP.unpublishPort();

        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }

}
