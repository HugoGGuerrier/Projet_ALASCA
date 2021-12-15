package eco_logis.equipments.power_bank.mil.events;

import eco_logis.equipments.generator.mil.events.AbstractGeneratorEvent;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the event when the power bank is discharging
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DischargePowerBank
    extends AbstractPowerBankEvent
{

    // ========== Constructors ==========


    /** @see AbstractPowerBankEvent#AbstractPowerBankEvent(Time, EventInformationI) */
    public DischargePowerBank(Time timeOfOccurrence, EventInformationI content) {
        super(timeOfOccurrence, content);
    }


    // ========== Override methods ==========


    /** @see AbstractGeneratorEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return false;
    }

    /** @see AbstractGeneratorEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        // TODO
    }

}
