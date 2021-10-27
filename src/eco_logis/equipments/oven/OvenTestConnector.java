package eco_logis.equipments.oven;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenTestConnector
        extends AbstractConnector
        implements OvenCI
{

    /** @see OvenCI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        return ((OvenCI) offering).isBaking();
    }

    /** @see OvenCI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        ((OvenCI) offering).startBaking();
    }

    /** @see OvenCI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        ((OvenCI) offering).stopBaking();
    }

    /** @see OvenCI#getTemperature() */
    @Override
    public double getTemperature() throws Exception {
        return ((OvenCI) offering).getTemperature();
    }

    /** @see OvenCI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) throws Exception {
        ((OvenCI) offering).setTemperature(temp);
    }

}
