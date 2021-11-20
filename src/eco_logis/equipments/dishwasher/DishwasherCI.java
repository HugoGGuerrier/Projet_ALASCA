package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This interface represents all dishwasher services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface DishwasherCI
        extends DishwasherImplementationI, RequiredCI, OfferedCI
{

    /** @see DishwasherImplementationI#getProgram() */
    @Override
    DishwasherProgram getProgram() throws Exception;

    /** @see DishwasherImplementationI#getProgramDuration() */
    @Override
    Duration getProgramDuration() throws Exception;

    /** @see DishwasherImplementationI#getDeadline() */
    @Override
    LocalTime getDeadline() throws Exception;

    /** @see DishwasherImplementationI#getStartTime() */
    @Override
    LocalTime getStartTime() throws Exception;

    /** @see DishwasherImplementationI#isPlanned() */
    @Override
    boolean isPlanned() throws Exception;

    /** @see DishwasherImplementationI#plan(LocalTime) */
    @Override
    boolean plan(LocalTime deadline) throws Exception;

    /** @see DishwasherImplementationI#plan(LocalTime, DishwasherProgram) */
    @Override
    boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception;

    /** @see DishwasherImplementationI#cancel() */
    @Override
    boolean cancel() throws Exception;

    /** @see DishwasherImplementationI#postPone(Duration) */
    @Override
    boolean postPone(Duration duration) throws Exception;

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    boolean isWashing() throws Exception;

    /** @see DishwasherImplementationI#startWashing() */
    @Override
    void startWashing() throws Exception;

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    void stopWashing() throws Exception;

}
