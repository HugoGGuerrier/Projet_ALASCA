package eco_logis.equipments.hem.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.PlanningEquipmentCI;

import java.time.Duration;
import java.time.LocalTime;

public class DishwasherConnector
    extends AbstractConnector
    implements PlanningEquipmentCI
{

    // ========== Override methods ==========


    @Override
    public boolean hasPlan() throws Exception {
        return false;
    }

    @Override
    public LocalTime startTime() throws Exception {
        return null;
    }

    @Override
    public Duration duration() throws Exception {
        return null;
    }

    @Override
    public LocalTime deadline() throws Exception {
        return null;
    }

    @Override
    public boolean postPone(Duration duration) throws Exception {
        return false;
    }

    @Override
    public boolean cancel() throws Exception {
        return false;
    }

    @Override
    public boolean on() throws Exception {
        return false;
    }

    @Override
    public boolean switchOn() throws Exception {
        return false;
    }

    @Override
    public boolean switchOff() throws Exception {
        return false;
    }

}
