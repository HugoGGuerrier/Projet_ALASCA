package equipments.hem.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ProductionEquipmentCI;

public class GeneratorConnector
    extends AbstractConnector
    implements ProductionEquipmentCI
{

    // ========== Override methods ==========


    @Override
    public boolean isProducing() throws Exception {
        return false;
    }

    @Override
    public boolean startProducing() throws Exception {
        return false;
    }

    @Override
    public boolean stopProducing() throws Exception {
        return false;
    }

    @Override
    public double getProduction() throws Exception {
        return 0;
    }

}
