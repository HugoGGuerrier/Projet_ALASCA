package equipments.dishwasher;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This interface represents all dishwasher services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface DishwasherCI
        extends DishwasherImplementationI, RequiredCI, OfferedCI
{

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    boolean isWashing() throws Exception;

    /** @see DishwasherImplementationI#startWasherFull() */
    @Override
    void startWasherFull() throws Exception;

    /** @see DishwasherImplementationI#startWasherEco() */
    @Override
    void startWasherEco() throws Exception;

    /** @see DishwasherImplementationI#startWasherFast() */
    @Override
    void startWasherFast() throws Exception;

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    void stopWashing() throws Exception;

    /** @see DishwasherImplementationI#getProgram() */
    @Override
    DishwasherProgram getProgram() throws Exception;

}
