package eco_logis.equipments.hem.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterCI;

public class ElectricMeterConnector
        extends AbstractConnector
        implements ElectricMeterCI
{
    /** @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterCI#getCurrentConsumption() */
    @Override
    public double getCurrentConsumption() throws Exception {
        return ((ElectricMeterCI)this.offering).getCurrentConsumption();
    }

    /** @see fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterCI#getCurrentProduction() */
    @Override
    public double getCurrentProduction() throws Exception {
        return ((ElectricMeterCI)this.offering).getCurrentProduction();
    }

}
