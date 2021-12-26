package eco_logis.equipments.power_bank.mil.events;

import eco_logis.equipments.generator.mil.events.AbstractGeneratorEvent;
import eco_logis.equipments.power_bank.mil.PowerBankChargeModel;
import eco_logis.equipments.power_bank.mil.PowerBankElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import org.apache.commons.math3.analysis.function.Power;

/**
 * This class represents the event when the power bank is put on stand by
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class StandbyPowerBank
    extends AbstractPowerBankEvent
{

    // ========== Constructors ==========


    /** @see AbstractPowerBankEvent#AbstractPowerBankEvent(Time, EventInformationI) */
    public StandbyPowerBank(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractGeneratorEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return true;
    }

    /** @see AbstractGeneratorEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof PowerBankElectricityModel || model instanceof PowerBankChargeModel;
        if(model instanceof PowerBankElectricityModel) {
            PowerBankElectricityModel m = (PowerBankElectricityModel) model;
            if(m.getCurrentState() != PowerBankElectricityModel.State.STANDBY) {
                m.setCurrentState(PowerBankElectricityModel.State.STANDBY);
                m.setHasChanged(true);
            }
        }
        else {
            PowerBankChargeModel m = (PowerBankChargeModel) model;
            if(m.getCurrentState() != PowerBankElectricityModel.State.STANDBY) {
                m.setCurrentState(PowerBankElectricityModel.State.STANDBY);
            }
        }
    }

}
