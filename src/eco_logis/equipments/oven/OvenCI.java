package equipments.oven;

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

    /** @see OvenImplementationI#isBaking() */
    @Override
    boolean isBaking() throws Exception;

    /** @see OvenImplementationI#startBaking() */
    @Override
    void startBaking() throws Exception;

    /** @see OvenImplementationI#stopBaking() */
    @Override
    void stopBaking() throws Exception;

    /** @see OvenImplementationI#getTemperature() */
    @Override
    double getTemperature() throws Exception;

    /** @see OvenImplementationI#setTemperature(double) */
    @Override
    void setTemperature(double temp) throws Exception;

}
