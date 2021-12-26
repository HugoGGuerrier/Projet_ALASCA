package eco_logis.equipments.power_bank.mil.events;

import eco_logis.equipments.generator.mil.events.AbstractGeneratorEvent;
import eco_logis.equipments.power_bank.mil.PowerBankChargeModel;
import eco_logis.equipments.power_bank.mil.PowerBankElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the event when the power bank charge is switch on
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ChargePowerBank
    extends AbstractPowerBankEvent
{

    // ========== Constructors ==========


    /** @see AbstractPowerBankEvent#AbstractPowerBankEvent(Time, EventInformationI) */
    public ChargePowerBank(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractGeneratorEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return !(e instanceof StandbyPowerBank);
    }

    /** @see AbstractGeneratorEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof PowerBankElectricityModel || model instanceof PowerBankChargeModel;
        if(model instanceof PowerBankElectricityModel) {
            PowerBankElectricityModel m = (PowerBankElectricityModel) model;
            if(m.getCurrentState() != PowerBankElectricityModel.State.CHARGE) {
                m.setCurrentState(PowerBankElectricityModel.State.CHARGE);
                m.setHasChanged(true);
            }
        }
        else {
            PowerBankChargeModel m = (PowerBankChargeModel) model;
            if(m.getCurrentState() != PowerBankElectricityModel.State.CHARGE) {
                m.setCurrentState(PowerBankElectricityModel.State.CHARGE);
            }
        }
    }

}
