package equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerCI;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hairdryer.HairDryerImplementationI;

/**
 * The class <code>Oven</code> implements the oven component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * The oven is an uncontrollable appliance, hence it does not connect
 * with the household energy manager (we don't want any control over the oven).
 * However, it will connect later to the electric panel to take its (simulated)
 * electricity consumption into account.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	true
 * </pre>
 *
 * <p>Created on : 2021-10-05</p>
 *
 * @author Emilie Siau
 * @author Hugo Guerrier
 */

//@OfferedInterfaces(offered = {OvenCI.class})
public class Oven extends AbstractComponent implements OvenImplementationI {

    // ========== Macros ==========

    /**
     * URI of the oven inbound port used in tests
     */
    public static final String INBOUND_PORT_URI = "OVEN-INBOUND-PORT-URI";
    /**
     * When true, methods trace their actions
     */
    public static final boolean VERBOSE = true;
    public static final OvenState INITIAL_STATE = OvenState.OFF;

    // ========== Attributes ==========

    private double temperature;

    // ========== Constructors ==========

    /**
    * Create an oven component
     *
    * <p><strong>Contract</strong></p>
    *
    * <pre>
    * pre	{@code INBOUND_PORT_URI != null}
    * pre	{@code !INBOUND_PORT_URI.isEmpty()}
    * post	{@code getState() == OvenState.OFF}
    * post	{@code getTemperature() == 0.0}
    * </pre>
    *
    * @throws Exception TODO
    */
    protected Oven() throws Exception {
        super(1, 0);
        this.initialise(INBOUND_PORT_URI);
    }

    /**
     * Create an oven component.
     *
     * @param ovenInboundPortURI URI of the hair dryer inbound port.
     * @throws Exception <i>to do</i>.
     */
    protected Oven(String ovenInboundPortURI) throws Exception {
        super(1, 0);
        this.initialise(ovenInboundPortURI);
    }

    // ========== Class methods ==========

    @Override
    public void turnOn() throws Exception {

    }

    @Override
    public void turnOff() throws Exception {

    }

    // ========== Getters ==========

    @Override
    public OvenState getState() throws Exception {
        return null;
    }

    @Override
    public double getTemperature() throws Exception {
        return 0;
    }

    // ========== Setters ==========

    @Override
    public void setTemperature(double temp) throws Exception {

    }
}
