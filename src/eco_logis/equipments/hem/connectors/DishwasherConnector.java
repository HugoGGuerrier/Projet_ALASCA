package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.hem.PlanningEquipmentOutboundPort;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.PlanningEquipmentControlCI;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This class is an integration test connector for the dishwasher component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DishwasherConnector
    extends AbstractConnector
    implements PlanningEquipmentControlCI
{

    // ========== Override methods ==========


    /** @see PlanningEquipmentOutboundPort#hasPlan() */
    @Override
    public boolean hasPlan() throws Exception {
        return false;
    }

    /** @see PlanningEquipmentOutboundPort#startTime() */
    @Override
    public LocalTime startTime() throws Exception {
        return null;
    }

    /** @see PlanningEquipmentOutboundPort#duration() */
    @Override
    public Duration duration() throws Exception {
        return null;
    }

    /** @see PlanningEquipmentOutboundPort#deadline() */
    @Override
    public LocalTime deadline() throws Exception {
        return null;
    }

    /** @see PlanningEquipmentOutboundPort#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        return false;
    }

    /** @see PlanningEquipmentOutboundPort#cancel() */
    @Override
    public boolean cancel() throws Exception {
        return false;
    }

    /** @see PlanningEquipmentOutboundPort#on() */
    @Override
    public boolean on() throws Exception {
        return false;
    }

    /** @see PlanningEquipmentOutboundPort#switchOn()  */
    @Override
    public boolean switchOn() throws Exception {
        return false;
    }

    /** @see PlanningEquipmentOutboundPort#switchOff()  */
    @Override
    public boolean switchOff() throws Exception {
        return false;
    }

}
