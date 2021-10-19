package equipments.generator;

import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * This class is a test connector for the generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorTestConnector
    extends AbstractConnector
    implements GeneratorCI
{

    // ========== Override methods ==========

    /** @see GeneratorImplementationI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        return ((GeneratorCI) offering).isRunning();
    }

    /** @see GeneratorImplementationI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        ((GeneratorCI) offering).startGenerator();
    }

    /** @see GeneratorImplementationI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        ((GeneratorCI) offering).stopGenerator();
    }

    /** @see GeneratorImplementationI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        return ((GeneratorCI) offering).getFuelLevel();
    }

    /** @see GeneratorImplementationI#refill() */
    @Override
    public void refill() throws Exception {
        ((GeneratorCI) offering).refill();
    }

}
