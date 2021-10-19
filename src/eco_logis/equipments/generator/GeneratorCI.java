package equipments.generator;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * Interface that represents services for the generator component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public interface GeneratorCI
    extends GeneratorImplementationI, RequiredCI, OfferedCI
{

    /** @see GeneratorImplementationI#isRunning() */
    @Override
    boolean isRunning() throws Exception;

    /** @see GeneratorImplementationI#startGenerator() */
    @Override
    void startGenerator() throws Exception;

    /** @see GeneratorImplementationI#stopGenerator() */
    @Override
    void stopGenerator() throws Exception;

    /** @see GeneratorImplementationI#getFuelLevel() */
    @Override
    float getFuelLevel() throws Exception;

    /** @see GeneratorImplementationI#refill() */
    @Override
    void refill() throws Exception;

}
