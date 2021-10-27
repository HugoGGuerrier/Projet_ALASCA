package eco_logis.equipments.hem.connectors;

import eco_logis.equipments.generator.GeneratorCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import eco_logis.interfaces.ProductionEquipmentCI;

/**
 * This class is a test connector for the generator component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorConnector
    extends AbstractConnector
    implements ProductionEquipmentCI
{

    // ========== Override methods ==========


    /** @see ProductionEquipmentCI#isProducing() */
    @Override
    public boolean isProducing() throws Exception {
        return ((GeneratorCI) offering).isRunning();
    }

    /** @see ProductionEquipmentCI#startProducing() */
    @Override
    public boolean startProducing() throws Exception {
        ((GeneratorCI) offering).startGenerator();
        return true;
    }

    /** @see ProductionEquipmentCI#stopProducing() */
    @Override
    public boolean stopProducing() throws Exception {
        ((GeneratorCI) offering).stopGenerator();
        return true;
    }

    /** @see ProductionEquipmentCI#getProduction() */
    @Override
    public double getProduction() throws Exception {
        return ((GeneratorCI) offering).getEnergyProduction();
    }

}
