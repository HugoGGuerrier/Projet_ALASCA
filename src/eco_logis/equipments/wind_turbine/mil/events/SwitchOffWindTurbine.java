package eco_logis.equipments.wind_turbine.mil.events;

import eco_logis.equipments.wind_turbine.mil.WindTurbineElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represent a switching off event for the wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOffWindTurbine
    extends AbstractWindTurbineEvent
{

    // ========== Constructors ==========


    /** @see AbstractWindTurbineEvent#AbstractWindTurbineEvent(Time, EventInformationI) */
    public SwitchOffWindTurbine(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractWindTurbineEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching off has the lowest priority
        return false;
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
