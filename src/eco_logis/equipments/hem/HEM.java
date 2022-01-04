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
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

import java.util.LinkedList;
import java.util.List;

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


    public static enum OPType {
        SUSPENSION,
        PLANNING,
        PRODUCTION,
        STANDARD,
        STORAGE,
        UNPREDICTABLE
    }


    // ========== Attributes ==========


    private static final List<SuspensionEquipmentOutboundPort> suspOPs = new LinkedList<>();
    private static final List<PlanningEquipmentOutboundPort> planOPs = new LinkedList<>();
    private static final List<ProductionEquipmentOutboundPort> prodOPs = new LinkedList<>();
    private static final List<StandardEquipmentOutboundPort> standOPs = new LinkedList<>();
    private static final List<StorageEquipmentOutboundPort> storOPs = new LinkedList<>();
    private static final List<UnpredictableProductionEquipmentOutboundPort> unpredOPs = new LinkedList<>();


    // ========== Constructors ==========


    /** Create a new HEM component */
    protected HEM() {
        super(1, 0);
        this.tracer.get().setTitle("Home Energy Manager component");
        this.tracer.get().setRelativePosition(2, 2);
        this.toggleTracing();
    }


    // ========== Class methods ==========


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


    // ========== Lifecycle methods ==========


    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();

        // TODO
    }

    @Override
    public synchronized void execute() throws Exception {
        // TODO
    }

    @Override
    public synchronized void finalise() throws Exception {
        for(SuspensionEquipmentOutboundPort s : suspOPs) doPortDisconnection(s.getPortURI());
        for(PlanningEquipmentOutboundPort p : planOPs) doPortDisconnection(p.getPortURI());
        for(ProductionEquipmentOutboundPort p : prodOPs) doPortDisconnection(p.getPortURI());
        for(StandardEquipmentOutboundPort s : standOPs) doPortDisconnection(s.getPortURI());
        for(StorageEquipmentOutboundPort s : storOPs) doPortDisconnection(s.getPortURI());
        for(UnpredictableProductionEquipmentOutboundPort u : unpredOPs) doPortDisconnection(u.getPortURI());

        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            for(SuspensionEquipmentOutboundPort s : suspOPs) s.unpublishPort();
            for(PlanningEquipmentOutboundPort p : planOPs) p.unpublishPort();
            for(ProductionEquipmentOutboundPort p : prodOPs) p.unpublishPort();
            for(StandardEquipmentOutboundPort s : standOPs) s.unpublishPort();
            for(StorageEquipmentOutboundPort s : storOPs) s.unpublishPort();
            for(UnpredictableProductionEquipmentOutboundPort u : unpredOPs) u.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

}
