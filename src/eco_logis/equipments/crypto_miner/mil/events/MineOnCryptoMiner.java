package eco_logis.equipments.crypto_miner.mil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents an event to turn on the mining process on the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class MineOnCryptoMiner 
    extends AbstractCryptoMinerEvent
{
    
    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public MineOnCryptoMiner(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI)  */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // The only event more prior is the switch on
        return !(e instanceof SwitchOnCryptoMiner);
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        // TODO
        System.out.println("MineOnCryptoMiner#executeOn TODO");
    }

}
