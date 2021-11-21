package eco_logis.equipments.oven.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents an abstract event for the oven equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class AbstractOvenEvent
    extends ES_Event
{

    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractOvenEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
