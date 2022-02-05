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

    /** @see OvenCI#isOn() () */
    @Override
    public boolean isOn() throws Exception {
        return ((OvenCI) offering).isOn();
    }

    /** @see OvenCI#powerOn() */
    @Override
    public void powerOn() throws Exception {
        ((OvenCI) offering).powerOn();
    }

    /** @see OvenCI#powerOff() */
    @Override
    public void powerOff() throws Exception {
        ((OvenCI) offering).powerOff();
    }

    /** @see OvenCI#getCurrentTemperature() */
    @Override
    public double getCurrentTemperature() throws Exception {
        return ((OvenCI) offering).getCurrentTemperature();
    }

    /** @see OvenCI#getTargetTemperature() */
    @Override
    public double getTargetTemperature() throws Exception {
        return ((OvenCI) offering).getTargetTemperature();
    }

    /** @see OvenCI#setTargetTemperature(double) */
    @Override
    public void setTargetTemperature(double targetTemp) throws Exception {
        ((OvenCI) offering).setTargetTemperature(targetTemp);
    }

}
