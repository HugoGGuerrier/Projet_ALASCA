package eco_logis.equipments.wind_turbine.sil;

import eco_logis.equipments.wind_turbine.WindTurbine;
import eco_logis.equipments.wind_turbine.WindTurbineRTAtomicSimulatorPlugin;
import eco_logis.equipments.wind_turbine.mil.ExternalWindModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the external wind model of the wind turbine.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ExternalWindSILModel
    extends ExternalWindModel
{

    // ========== Attributes ===========


    /** Owner component */
    protected WindTurbine owner;


    // ========== Constructors ===========


    /**
     * Create an external wind model instance.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simulatedTimeUnit != null}
     * pre	{@code simulationEngine == null || simulationEngine instanceof HIOA_AtomicEngine}
     * post	{@code getURI() != null}
     * post	{@code uri != null implies this.getURI().equals(uri)}
     * post	{@code getSimulatedTimeUnit().equals(simulatedTimeUnit)}
     * post	{@code simulationEngine != null implies getSimulationEngine().equals(simulationEngine)}
     * post	{@code !isDebugModeOn()}
     * </pre>
     *
     * @param uri				URI of the model.
     * @param simulatedTimeUnit	time unit used for the simulation time.
     * @param simulationEngine	simulation engine to which the model is attached.
     * @throws Exception		<i>to do</i>.
     */
    public ExternalWindSILModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========



    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);

        assert simParams.containsKey(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.owner = (WindTurbine) simParams.get(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        this.setLogger(new StandardComponentLogger(this.owner));
    }

}
