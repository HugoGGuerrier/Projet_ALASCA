package eco_logis.equipments.dishwasher.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents an abstract event for the dishwasher
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public abstract class AbstractDishwasherEvent
    extends ES_Event
{

    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractDishwasherEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
