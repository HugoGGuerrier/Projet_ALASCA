package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.connectors.AbstractConnector;

import java.time.Duration;
import java.time.LocalTime;

public class DishwasherTestConnector
    extends AbstractConnector
    implements DishwasherCI
{

    // ========== Override methods ==========


    /** @see DishwasherCI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return ((DishwasherCI) offering).getProgram();
    }

    /** @see DishwasherCI#getProgramDuration() */
    @Override
    public Duration getProgramDuration() throws Exception {
        return ((DishwasherCI) offering).getProgramDuration();
    }

    /** @see DishwasherCI#getDeadline() */
    @Override
    public LocalTime getDeadline() throws Exception {
        return ((DishwasherCI) offering).getDeadline();
    }

    /** @see DishwasherCI#getStartTime() */
    @Override
    public LocalTime getStartTime() throws Exception {
        return ((DishwasherCI) offering).getStartTime();
    }

    /** @see DishwasherCI#isPlanned() */
    @Override
    public boolean isPlanned() throws Exception {
        return ((DishwasherCI) offering).isPlanned();
    }

    /** @see DishwasherCI#plan(LocalTime) */
    @Override
    public boolean plan(LocalTime deadline) throws Exception {
        return ((DishwasherCI) offering).plan(deadline);
    }

    /** @see DishwasherCI#plan(LocalTime, DishwasherProgram) */
    @Override
    public boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception {
        return ((DishwasherCI) offering).plan(deadline, program);
    }

    /** @see DishwasherCI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        return ((DishwasherCI) offering).cancel();
    }

    /** @see DishwasherCI#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        return ((DishwasherCI) offering).postPone(duration);
    }

    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) offering).isWashing();
    }

    /** @see DishwasherCI#startWashing() */
    @Override
    public boolean startWashing() throws Exception {
        return ((DishwasherCI) offering).startWashing();
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public boolean stopWashing() throws Exception {
        return ((DishwasherCI) offering).stopWashing();
    }
}
