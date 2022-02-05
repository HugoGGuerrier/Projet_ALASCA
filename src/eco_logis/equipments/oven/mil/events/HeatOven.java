package eco_logis.equipments.oven.mil.events;

import eco_logis.equipments.oven.mil.OvenElectricityModel;
import eco_logis.equipments.oven.mil.OvenTemperatureModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents a heating event on the oven.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class HeatOven
extends AbstractOvenEvent
{

    // ========== Constructors ==========


    /** @see AbstractOvenEvent#AbstractOvenEvent(Time, EventInformationI) */
    public HeatOven(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.events.Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Priority to SwitchOn & DoNotHeat
        if (e instanceof SwitchOnOven || e instanceof DoNotHeatOven) {
            return false;
        } else return true;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        if(model instanceof OvenElectricityModel) {
            OvenElectricityModel m = (OvenElectricityModel) model;
            if(m.getState() == OvenElectricityModel.State.ON) {
                m.setState(OvenElectricityModel.State.HEATING);
                m.toggleConsumptionHasChanged();
            }

        } else if(model instanceof OvenTemperatureModel) {
            OvenTemperatureModel m = (OvenTemperatureModel) model;
            if(m.getState() == OvenTemperatureModel.State.NOT_HEATING) {
                m.setState(OvenTemperatureModel.State.HEATING);
            }

        } else assert false;
    }

}
