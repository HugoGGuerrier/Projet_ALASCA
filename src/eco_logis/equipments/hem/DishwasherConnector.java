package equipments.hem;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.PlanningEquipmentCI;

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

    @Override
    public int maxMode() throws Exception {
        return 0;
    }

    @Override
    public boolean upMode() throws Exception {
        return false;
    }

    @Override
    public boolean downMode() throws Exception {
        return false;
    }

    @Override
    public boolean setMode(int mode) throws Exception {
        return false;
    }

    @Override
    public int currentMode() throws Exception {
        return 0;
    }

}
