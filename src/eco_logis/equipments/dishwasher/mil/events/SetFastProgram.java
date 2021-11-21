package eco_logis.equipments.dishwasher.mil.events;

import eco_logis.equipments.crypto_miner.mil.events.AbstractCryptoMinerEvent;
import eco_logis.equipments.dishwasher.mil.DishwasherElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the transition between off and fast program
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SetFastProgram
    extends AbstractDishwasherEvent
{

    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public SetFastProgram(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return !(e instanceof SwitchOffDishwasher) && !(e instanceof SetEcoProgram) && !(e instanceof SetFullProgram);
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof DishwasherElectricityModel;
        DishwasherElectricityModel m = (DishwasherElectricityModel) model;
        if(m.getCurrentState() == DishwasherElectricityModel.State.OFF) {
            m.setCurrentState(DishwasherElectricityModel.State.FAST);
            m.setHasChanged(true);
        }
    }

}
