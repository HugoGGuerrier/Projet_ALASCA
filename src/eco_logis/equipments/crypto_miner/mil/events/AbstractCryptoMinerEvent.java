package eco_logis.equipments.crypto_miner.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class is an abstract an uninstantiable event for the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public abstract class AbstractCryptoMinerEvent
    extends ES_Event
{

    // ========== Constructors ==========


    /** @see ES_Event#ES_Event(Time, EventInformationI) */
    public AbstractCryptoMinerEvent(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }

}
