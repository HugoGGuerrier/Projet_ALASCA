package eco_logis.equipments.oven.mil.events;

import eco_logis.equipments.oven.mil.OvenElectricityModel;
import eco_logis.equipments.oven.mil.OvenTemperatureModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represent a switching off event for the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOffOven
    extends AbstractOvenEvent
{

    // ========== Constructors ==========


    /** @see AbstractOvenEvent#AbstractOvenEvent(Time, EventInformationI) */
    public SwitchOffOven(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractOvenEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching off has the lowest priority
        return false;
    }

    /** @see AbstractOvenEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        if(model instanceof  OvenElectricityModel) {
            OvenElectricityModel m = (OvenElectricityModel) model;
            if (m.getState() == OvenElectricityModel.State.ON) {
                m.setState(OvenElectricityModel.State.OFF);
                m.toggleConsumptionHasChanged();
            }
        } else if (model instanceof OvenTemperatureModel) {
            OvenTemperatureModel m = (OvenTemperatureModel) model;
            if (m.getState() == OvenTemperatureModel.State.ON) {
                m.setState(OvenTemperatureModel.State.OFF);
                m.setGoalTemperature(OvenTemperatureModel.ROOM_TEMPERATURE);
            }
        } else assert false;
    }

}
