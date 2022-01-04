package eco_logis.equipments.wind_turbine.mil.events;

import eco_logis.equipments.wind_turbine.mil.WindTurbineElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represent a blocking event for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class BlockWindTurbine
    extends AbstractWindTurbineEvent
{

    // ========== Constructors ==========


    /** @see AbstractWindTurbineEvent#AbstractWindTurbineEvent(Time, EventInformationI) */
    public BlockWindTurbine(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractWindTurbineEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        if (WindSpeedChange.class.equals(e.getClass())) {
            // Has priority over the wind speed change
            return true;
        } else {
            // Blocking hasn't priority over any other event
            return false;
        }
    }

    /** @see AbstractWindTurbineEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof WindTurbineElectricityModel;
        WindTurbineElectricityModel m = (WindTurbineElectricityModel) model;
        if(m.isOn()) {
            m.setOn(false);
            m.setHasChanged(true);
        }
    }

}
