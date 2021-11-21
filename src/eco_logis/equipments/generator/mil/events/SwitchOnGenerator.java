package eco_logis.equipments.generator.mil.events;

import eco_logis.equipments.generator.mil.GeneratorElectricityModel;
import eco_logis.equipments.generator.mil.GeneratorFuelModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class SwitchOnGenerator
    extends AbstractGeneratorEvent
{

    // ========== Constructors ==========


    /** @see AbstractGeneratorEvent#AbstractGeneratorEvent(Time, EventInformationI) */
    public SwitchOnGenerator(Time timeOfOccurrence) {
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
        assert model instanceof GeneratorElectricityModel || model instanceof GeneratorFuelModel;
        if(model instanceof GeneratorElectricityModel) {
            GeneratorElectricityModel m = (GeneratorElectricityModel) model;
            if(!m.isRunning()) {
                m.setRunning(true);
                m.setHasChanged(true);
            }
        }
        else {
            GeneratorFuelModel m = (GeneratorFuelModel) model;
            if(!m.isRunning()) {
                m.setRunning(true);
            }
        }
    }

}
