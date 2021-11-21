package eco_logis.equipments.wind_turbine.mil.events;

import eco_logis.equipments.wind_turbine.mil.WindTurbineElectricityModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class SwitchOnWindTurbine
    extends AbstractWindTurbineEvent
{

    // ========== Constructors ==========


    /** @see AbstractWindTurbineEvent#AbstractWindTurbineEvent(Time, EventInformationI) */
    public SwitchOnWindTurbine(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractWindTurbineEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        // Switching on has priority over all others events
        return true;
    }

    /** @see AbstractWindTurbineEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof WindTurbineElectricityModel;
        WindTurbineElectricityModel m = (WindTurbineElectricityModel) model;
        if(!m.isOn()) {
            m.setOn(true);
            m.setHasChanged(true);
        }
    }

}
