package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.connectors.AbstractConnector;

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

    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) offering).isWashing();
    }

    /** @see DishwasherCI#startWashing() */
    @Override
    public void startWashing() throws Exception {
        ((DishwasherCI) offering).startWashing();
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
        ((DishwasherCI) offering).stopWashing();
    }
}
