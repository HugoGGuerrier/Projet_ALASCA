package eco_logis.equipments.oven.mil.events;

import eco_logis.equipments.oven.mil.OvenElectricityModel;
import eco_logis.equipments.oven.mil.OvenTemperatureModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents a switch on event on the oven. The oven will start heating
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOnOven
    extends AbstractOvenEvent
{

    // ========== Attributes ==========


    /** Current goal temperature of the oven (°C) */
    protected double goalTemperature;


    // ========== Constructors ==========


    /** @see AbstractOvenEvent#AbstractOvenEvent(Time, EventInformationI) */
    public SwitchOnOven(Time timeOfOccurrence, double goalTemperature) {
        super(timeOfOccurrence, null);
        this.goalTemperature = goalTemperature;
    }


    // ========== Override methods ==========


    /** @see AbstractOvenEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching on has priority over all others events
        return true;
    }

    /** @see AbstractOvenEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        if(model instanceof  OvenElectricityModel) {
            OvenElectricityModel m = (OvenElectricityModel) model;
            if (m.getState() == OvenElectricityModel.State.OFF) {
                m.setState(OvenElectricityModel.State.ON);
                m.toggleConsumptionHasChanged();
            }
        } else if (model instanceof OvenTemperatureModel) {
            OvenTemperatureModel m = (OvenTemperatureModel) model;
            if (m.getState() == OvenTemperatureModel.State.OFF) {
                m.setState(OvenTemperatureModel.State.ON);
                m.setGoalTemperature(goalTemperature);
            }
        } else assert false;
    }

}
