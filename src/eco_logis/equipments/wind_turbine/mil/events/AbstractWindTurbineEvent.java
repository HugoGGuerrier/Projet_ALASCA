package eco_logis.equipments.wind_turbine.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public abstract class AbstractWindTurbineEvent
    extends ES_Event
{

    // ========== Constructors ==========


    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractWindTurbineEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
