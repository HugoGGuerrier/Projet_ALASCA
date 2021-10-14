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

    @Override
    public void startBaking() throws Exception {
        ((OvenCI) offering).startBaking();
    }

    @Override
    public void stopBaking() throws Exception {
        ((OvenCI) offering).stopBaking();
    }

    @Override
    public double getTemperature() throws Exception {
        return ((OvenCI) offering).getTemperature();
    }

    @Override
    public void setTemperature(double temp) throws Exception {
        ((OvenCI) offering).setTemperature(temp);
    }
}
