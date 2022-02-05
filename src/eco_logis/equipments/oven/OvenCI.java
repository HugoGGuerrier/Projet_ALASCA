package eco_logis.equipments.oven;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * The component interface <code>OvenCI</code> defines the services an
 * oven component offers and that can be required from it.
 *
 * <p><strong>Description</strong></p>
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant		true
 * </pre>
 * <p>Created on : 2021-10-13</p>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface OvenCI
        extends OfferedCI, RequiredCI, OvenImplementationI
{

    /** @see OvenImplementationI#isOn() */
    @Override
    boolean isOn() throws Exception;

    /** @see OvenImplementationI#powerOn() */
    @Override
    void powerOn() throws Exception;

    /** @see OvenImplementationI#powerOff() */
    @Override
    void powerOff() throws Exception;

    /** @see OvenImplementationI#getCurrentTemperature() */
    @Override
    double getCurrentTemperature() throws Exception;

    /** @see OvenImplementationI#getTargetTemperature() */
    @Override
    double getTargetTemperature() throws Exception;

    /** @see OvenImplementationI#setTargetTemperature(double) */
    @Override
    void setTargetTemperature(double temp) throws Exception;

}
