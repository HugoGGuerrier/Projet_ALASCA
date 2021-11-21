package eco_logis.equipments.generator.mil.events;

import eco_logis.equipments.generator.mil.GeneratorElectricityModel;
import eco_logis.equipments.generator.mil.GeneratorFuelModel;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * This class represents the event to switch off the generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SwitchOffGenerator
    extends AbstractGeneratorEvent
{

    // ========== Constructors ==========


    /** @see AbstractGeneratorEvent#AbstractGeneratorEvent(Time, EventInformationI) */
    public SwitchOffGenerator(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }


    // ========== Override methods ==========


    /** @see AbstractGeneratorEvent#hasPriorityOver(EventI) */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return false;
    }

    /** @see AbstractGeneratorEvent#executeOn(AtomicModel) */
    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof GeneratorElectricityModel || model instanceof GeneratorFuelModel;
        if(model instanceof GeneratorElectricityModel) {
            GeneratorElectricityModel m = (GeneratorElectricityModel) model;
            if(m.isRunning()) {
                m.setRunning(false);
                m.setHasChanged(true);
            }
        }
        else {
            GeneratorFuelModel m = (GeneratorFuelModel) model;
            if(m.isRunning()) {
                m.setRunning(false);
            }
        }
    }

}
