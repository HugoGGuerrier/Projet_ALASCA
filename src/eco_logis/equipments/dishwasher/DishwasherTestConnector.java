package equipments.dishwasher;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class DishwasherTestConnector
    extends AbstractConnector
    implements DishwasherCI
{

    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return ((DishwasherCI) offering).isWashing();
    }

    /** @see DishwasherCI#startWasherFull() */
    @Override
    public void startWasherFull() throws Exception {
        ((DishwasherCI) offering).startWasherFull();
    }

    /** @see DishwasherCI#startWasherEco() */
    @Override
    public void startWasherEco() throws Exception {
        ((DishwasherCI) offering).startWasherEco();
    }

    /** @see DishwasherCI#startWasherFast() */
    @Override
    public void startWasherFast() throws Exception {
        ((DishwasherCI) offering).startWasherFast();
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public void stopWashing() throws Exception {
        ((DishwasherCI) offering).stopWashing();
    }

    /** @see DishwasherCI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return ((DishwasherCI) offering).getProgram();
    }

}
