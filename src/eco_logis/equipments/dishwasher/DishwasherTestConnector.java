package equipments.dishwasher;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class DishwasherTestConnector
    extends AbstractConnector
    implements DishwasherCI
{

    /** @see DishwasherImplementationI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) offering).isWashing();
    }

    /** @see DishwasherImplementationI#startWasherFull() */
    @Override
    public void startWasherFull() throws Exception {
        ((DishwasherCI) offering).startWasherFull();
    }

    /** @see DishwasherImplementationI#startWasherEco() */
    @Override
    public void startWasherEco() throws Exception {
        ((DishwasherCI) offering).startWasherEco();
    }

    /** @see DishwasherImplementationI#startWasherFast() */
    @Override
    public void startWasherFast() throws Exception {
        ((DishwasherCI) offering).startWasherFast();
    }

    /** @see DishwasherImplementationI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
        ((DishwasherCI) offering).stopWashing();
    }

    /** @see DishwasherImplementationI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return ((DishwasherCI) offering).getProgram();
    }

}
