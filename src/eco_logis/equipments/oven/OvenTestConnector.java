package equipments.oven;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the oven
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class OvenTestConnector
        extends AbstractConnector
        implements OvenCI {

    /** @see OvenImplementationI#isBaking() */
    @Override
    public boolean isBaking() throws Exception {
        return ((OvenCI) offering).isBaking();
    }

    /** @see OvenImplementationI#startBaking() */
    @Override
    public void startBaking() throws Exception {
        ((OvenCI) offering).startBaking();
    }

    /** @see OvenImplementationI#stopBaking() */
    @Override
    public void stopBaking() throws Exception {
        ((OvenCI) offering).stopBaking();
    }

    /** @see OvenImplementationI#getTemperature() */
    @Override
    public double getTemperature() throws Exception {
        return ((OvenCI) offering).getTemperature();
    }

    /** @see OvenImplementationI#setTemperature(double) */
    @Override
    public void setTemperature(double temp) throws Exception {
        ((OvenCI) offering).setTemperature(temp);
    }
}
