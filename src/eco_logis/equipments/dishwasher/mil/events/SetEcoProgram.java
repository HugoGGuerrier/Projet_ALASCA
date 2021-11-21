package eco_logis.equipments.dishwasher.mil.events;

import eco_logis.equipments.crypto_miner.mil.events.AbstractCryptoMinerEvent;
import eco_logis.equipments.dishwasher.mil.DishwasherElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the transition between off and eco program
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SetEcoProgram
    extends AbstractDishwasherEvent
{

    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public SetEcoProgram(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return !(e instanceof SwitchOffDishwasher);
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof DishwasherElectricityModel;
        DishwasherElectricityModel m = (DishwasherElectricityModel) model;
        if(m.getCurrentState() == DishwasherElectricityModel.State.OFF) {
            m.setCurrentState(DishwasherElectricityModel.State.ECO);
            m.setHasChanged(true);
        }
    }

}
