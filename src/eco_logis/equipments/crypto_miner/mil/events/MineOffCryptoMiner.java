package eco_logis.equipments.crypto_miner.mil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represent the stopping of the mining process on the crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class MineOffCryptoMiner
    extends AbstractCryptoMinerEvent
{

    // ========== Constructors ==========


    /** @see AbstractCryptoMinerEvent#AbstractCryptoMinerEvent(Time, EventInformationI) */
    public MineOffCryptoMiner(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }


    // ========== Override methods ==========


    /** @see AbstractCryptoMinerEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Only more prior switch of
        return !(e instanceof MineOnCryptoMiner || e instanceof SwitchOnCryptoMiner);
    }

    /** @see AbstractCryptoMinerEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        // TODO
        System.out.println("MineOffCryptoMiner#executeOn TODO");
    }

}
