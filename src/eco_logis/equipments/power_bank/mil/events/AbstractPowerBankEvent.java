package eco_logis.equipments.power_bank.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the abstract event for the power bank component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public abstract class AbstractPowerBankEvent
    extends ES_Event
{

    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractPowerBankEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
