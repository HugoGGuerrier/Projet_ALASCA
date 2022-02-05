package eco_logis.equipments.oven.mil.events;

import eco_logis.equipments.oven.mil.OvenElectricityModel;
import eco_logis.equipments.oven.mil.OvenTemperatureModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents a do not heat event (the stopping of the heating process) on the oven.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DoNotHeatOven
extends AbstractOvenEvent
{

    // ========== Constructors ==========


    /** @see AbstractOvenEvent#AbstractOvenEvent(Time, EventInformationI) */
    public DoNotHeatOven(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Priority to SwitchOn
        if (e instanceof SwitchOnOven) return false;
        else return true;
    }

    /** @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        if(model instanceof OvenElectricityModel) {
            OvenElectricityModel m = (OvenElectricityModel) model;
            if(m.getState() == OvenElectricityModel.State.HEATING) {
                m.setState(OvenElectricityModel.State.ON);
                m.toggleConsumptionHasChanged();
            }

        } else if(model instanceof OvenTemperatureModel) {
            OvenTemperatureModel m = (OvenTemperatureModel) model;
            if(m.getState() == OvenTemperatureModel.State.HEATING) {
                m.setState(OvenTemperatureModel.State.NOT_HEATING);
            }

        } else assert false;
    }
}
