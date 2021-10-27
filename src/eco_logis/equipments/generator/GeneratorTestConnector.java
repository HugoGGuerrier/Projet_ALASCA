package eco_logis.equipments.generator;

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

    /** @see GeneratorCI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        return ((GeneratorCI) offering).isRunning();
    }

    /** @see GeneratorCI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        ((GeneratorCI) offering).startGenerator();
    }

    /** @see GeneratorCI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        ((GeneratorCI) offering).stopGenerator();
    }

    /** @see GeneratorCI#getEnergyProduction() */
    @Override
    public double getEnergyProduction() throws Exception {
        return ((GeneratorCI) offering).getEnergyProduction();
    }

    /** @see GeneratorCI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        return ((GeneratorCI) offering).getFuelLevel();
    }

    /** @see GeneratorCI#refill() */
    @Override
    public void refill() throws Exception {
        ((GeneratorCI) offering).refill();
    }

}
