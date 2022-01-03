package eco_logis.equipments.generator;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;

import java.util.Map;

/**
 * This class represents the SIL simulation plugin for the Generator
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class GeneratorRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestGenerator";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "GCRN";


    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the Generator
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true
     * </pre>
     *
     * @param simArchURI URI of the simulation architecture to be created.
     * @param accFactor	Acceleration factor used in the real time simulation.
     * @throws Exception TODO
     */
    public void	initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {

    }


    // ========== Override methods ==========


    /** @see RTAtomicSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Put the owner component in the params
        simParams.put(OWNER_REFERENCE_NAME, getOwner());

        // Call super
        super.setSimulationRunParameters(simParams);

        // Remove the owner from the params
        simParams.remove(OWNER_REFERENCE_NAME);
    }

}
