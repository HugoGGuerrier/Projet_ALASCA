package eco_logis.equipments.generator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class GeneratorOutboundPort
    extends AbstractOutboundPort
    implements GeneratorCI
{

    // ========== Constructors ==========

    /**
     * Create a new generator outbound port with the owner component
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public GeneratorOutboundPort(ComponentI owner) throws Exception {
        super(GeneratorCI.class, owner);
    }

    /**
     * Create a new generator outbound port with the uri and the owner component
     *
     * @see AbstractOutboundPort#AbstractOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port uri
     * @param owner The port owner
     * @throws Exception TODO
     */
    public GeneratorOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, GeneratorCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see GeneratorCI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        return ((GeneratorCI) getConnector()).isRunning();
    }

    /** @see GeneratorCI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        ((GeneratorCI) getConnector()).startGenerator();
    }

    /** @see GeneratorCI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        ((GeneratorCI) getConnector()).stopGenerator();
    }

    /** @see GeneratorCI#getEnergyProduction() */
    @Override
    public double getEnergyProduction() throws Exception {
        return ((GeneratorCI) getConnector()).getEnergyProduction();
    }

    /** @see GeneratorCI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        return ((GeneratorCI) getConnector()).getFuelLevel();
    }

    /** @see GeneratorCI#refill() */
    @Override
    public void refill() throws Exception {
        ((GeneratorCI) getConnector()).refill();
    }

}
