package equipments.generator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * This class represent a fuel generator component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@OfferedInterfaces(offered = {GeneratorCI.class})
public class Generator
    extends AbstractComponent
    implements GeneratorImplementationI
{

    // ========== Macros ==========


    /** URI of the generator inbound port */
    public static final String INBOUND_PORT_URI = "GENERATOR-INBOUND-PORT-URI";

    /** When true, methods trace their actions */
    public static final boolean VERBOSE = true;


    // ========== Attributes ==========


    /** If the generator is currently running */
    private boolean isRunning;

    /** The generator tank fuel level between 0 and 1 */
    private float fuelLevel;

    /** The inbound port */
    private GeneratorInboundPort gip;


    // ========== Constructors ==========


    /**
     * Create a new generator
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code INBOUND_PORT_URI != null}
     * pre	{@code !INBOUND_PORT_URI.isEmpty()}
     * post	true
     * </pre>
     *
     * @throws Exception TODO
     */
    protected Generator() throws Exception {
        this(INBOUND_PORT_URI);
    }

    /**
     * Create a new generator with the wanted generator inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param generatorInboundPortURI The wanted inbound port URI
     * @throws Exception TODO
     */
    protected Generator(String generatorInboundPortURI) throws Exception {
        super(0, 1);
        initialise(generatorInboundPortURI);
    }

    /**
     * Create a new generator with the wanted reflection inbound port URI and generator inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * pre	{@code reflectionInboundPortURI != null}
     * pre	{@code !reflectionInboundPortURI.isEmpty()}
     * post	true
     * </pre>
     *
     * @param reflectionInboundPortURI The reflection inbound port URI
     * @param generatorInboundPortURI The generator inbound pour URI
     * @throws Exception TODO
     */
    protected Generator(String reflectionInboundPortURI, String generatorInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        initialise(generatorInboundPortURI);
    }


    // ========== Class methods ==========


    /**
     * Initialise the generator with the wanted inbound port URI
     *
     * <p><strong>Contract</strong></p>
     *
     * pre	{@code generatorInboundPortURI != null}
     * pre	{@code !generatorInboundPortURI.isEmpty()}
     * post {@code !isRunning}
     * post {@code fuelLevel == 0.0}
     * post	{@code gip.isPublished()}
     *
     * @param generatorInboundPortURI The generator inbound port URI
     * @throws Exception TODO
     */
    private void initialise(String generatorInboundPortURI) throws Exception {
        // Assert the URI
        assert generatorInboundPortURI != null : new PreconditionException("generatorInboundPortURI != null");
        assert !generatorInboundPortURI.isEmpty() : new PreconditionException("!generatorInboundPortURI.isEmpty()");

        // Initialise the component
        isRunning = false;
        fuelLevel = 0.0f;

        // Initialise the inbound port
        gip = new GeneratorInboundPort(generatorInboundPortURI, this);
        gip.publishPort();

        // Create the tracer
        if(VERBOSE) {
            tracer.get().setTitle("Generator component");
            tracer.get().setRelativePosition(2, 0);
            toggleTracing();
        }
    }


    // ========== Override methods ==========


    /** @see GeneratorImplementationI#isRunning() */
    @Override
    public boolean isRunning() throws Exception {
        if(VERBOSE) {
            logMessage("Generator get running : " + isRunning);
        }

        return isRunning;
    }

    /** @see GeneratorImplementationI#startGenerator() */
    @Override
    public void startGenerator() throws Exception {
        if(VERBOSE) {
            logMessage("Generator start running");
        }

        assert !isRunning;

        isRunning = true;
    }

    /** @see GeneratorImplementationI#stopGenerator() */
    @Override
    public void stopGenerator() throws Exception {
        if(VERBOSE) {
            logMessage("Generator stop running");
        }

        assert isRunning;

        isRunning = false;
    }

    /** @see GeneratorImplementationI#getFuelLevel() */
    @Override
    public float getFuelLevel() throws Exception {
        if(VERBOSE) {
            logMessage("Generator get fuel level : " + fuelLevel);
        }

        return fuelLevel;
    }

    /** @see GeneratorImplementationI#refill() */
    @Override
    public void refill() throws Exception {
        if(VERBOSE) {
            logMessage("Generator refill the tank");
        }

        assert !isRunning;

        fuelLevel = 1.0f;
    }

}
