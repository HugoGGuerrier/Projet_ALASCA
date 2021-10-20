package equipments.generator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * This class represent an inbound port for the generator component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorInboundPort
    extends AbstractInboundPort
    implements GeneratorCI
{

    // ========== Constructors ==========


    /**
     * Create a new generator inbound port with the owner component
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public GeneratorInboundPort(ComponentI owner) throws Exception {
        super(GeneratorCI.class, owner);
    }

    /**
     * Create a new generator inbound port with the URI and the owner component
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public GeneratorInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, GeneratorCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see GeneratorCI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        return getOwner().handleRequest(
                o -> ((Generator)  o).isRunning()
        );
    }

    /** @see GeneratorCI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Generator) o).startGenerator();
                    return null;
                }
        );
    }

    /** @see GeneratorCI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Generator) o).stopGenerator();
                    return null;
                }
        );
    }

    /** @see GeneratorCI#getEnergyProduction() */
    @Override
    public double getEnergyProduction() throws Exception {
        return getOwner().handleRequest(
                o -> ((Generator) o).getEnergyProduction()
        );
    }

    /** @see GeneratorCI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        return getOwner().handleRequest(
                o -> ((Generator) o).getFuelLevel()
        );
    }

    /** @see GeneratorCI#refill() */
    @Override
    public void refill() throws Exception {
        getOwner().handleRequest(
                o -> {
                    ((Generator) o).refill();
                    return null;
                }
        );
    }

}
