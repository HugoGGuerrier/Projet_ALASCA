package eco_logis.equipments.electric_meter.sil;

import fr.sorbonne_u.devs_simulation.hioa.models.vars.StaticVariableDescriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>ElectricMeterCoupledModel</code> is the coupled model used
 * for SIL simulations of the HEM project.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ElectricMeterCoupledModel
    extends CoupledModel
{

    // =========== Macros ==========


    private static final long serialVersionUID = 1L;

    /** URI for an instance model; works as long as only one instance is created */
    public static final String URI = ElectricMeterCoupledModel.class.getSimpleName();


    // =========== Constructors ==========


    /**
     * Creating the coupled model
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code uri != null}
     * pre	{@code simulatedTimeUnit != null}
     * pre	{@code submodels != null and submodels.length > 1}
     * pre	{@code forall i, submodels[i] != null}
     * post	{@code uri.equals(getURI())}
     * post	{@code getSimulatedTimeUnit().equals(TimeUnit.SECONDS)}
     * post	{@code !isDebugModeOn()}
     * </pre>
     *
     * @param uri				URI of the coupled model to be created.
     * @param simulatedTimeUnit	time unit used in the simulation by the model.
     * @param simulationEngine	simulation engine enacting the model.
     * @param submodels			array of submodels of the new coupled model.
     * @param imported			map from imported event types to submodels consuming them.
     * @param reexported		map from event types exported by submodels that are reexported by this coupled model.
     * @param connections		map connecting event sources to arrays of event sinks among submodels.
     * @param importedVars		variables imported by the coupled model that are consumed by submodels.
     * @param reexportedVars	variables exported by submodels that are reexported by the coupled model.
     * @param bindings			bindings between exported and imported variables among submodels.
     * @throws Exception		<i>to do</i>.
     */
    public ElectricMeterCoupledModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine,
            ModelDescriptionI[] submodels,
            Map<Class<? extends EventI>, EventSink[]> imported,
            Map<Class<? extends EventI>, ReexportedEvent> reexported,
            Map<EventSource, EventSink[]> connections,
            Map<StaticVariableDescriptor, VariableSink[]> importedVars,
            Map<VariableSource, StaticVariableDescriptor> reexportedVars,
            Map<VariableSource, VariableSink[]> bindings
    ) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine, submodels,
                imported, reexported, connections,
                importedVars, reexportedVars, bindings);
    }

}
