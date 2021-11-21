package eco_logis.equipments.generator.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class is an abstract event for the generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public abstract class AbstractGeneratorEvent
    extends ES_Event
{
    
    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractGeneratorEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
