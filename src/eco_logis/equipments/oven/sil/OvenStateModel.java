package eco_logis.equipments.oven.sil;

import eco_logis.equipments.oven.Oven;
import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.OvenElectricityModel;
import eco_logis.equipments.oven.mil.events.AbstractOvenEvent;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>OvenStateModel</code> defines a simulation model
 * tracking the state changes on an oven.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * The model receives event from the oven component (corresponding to
 * calls to operations on the oven in this component), keeps track of
 * the current state of the oven in the simulation and then emits the
 * received events again towards another model simulating the electricity
 * consumption of the oven given its current operating state (switched
 * on/off).
 * </p>
 * <p>
 * This model becomes necessary in a SIL simulation of the household energy
 * management system because the electricity model must be put in the electric
 * meter component to share variables with other electricity models so this
 * state model will serve as a bridge between the models put in the oven
 * component and its electricity model put in the electric meter component.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(
        imported = {
                SwitchOnOven.class,
                SwitchOffOven.class
        },
        exported = {
                SwitchOnOven.class,
                SwitchOffOven.class
        }
)
public class OvenStateModel
    extends AtomicModel
{

    // ========== Macros ==========


    private static final long serialVersionUID = 1L;

    /** URI for an instance model; works as long as only one instance is created. */
    public static final String URI = OvenStateModel.class.getSimpleName();


    // ========== Attributes ==========


    /** If the oven is currently on (baking) */
    protected OvenElectricityModel.State isBaking;

    /** Last received event or null if none */
    protected AbstractOvenEvent lastReceived;

    /** Owner component */
    protected Oven owner;


    // ========== Constructors ==========


    /**
     * Create an oven state model instance.
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simulatedTimeUnit != null}
     * pre	{@code simulationEngine != null implies simulationEngine instanceof AtomicEngine}
     * post	{@code getURI() != null}
     * post	{@code uri != null implies getURI().equals(uri)}
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
    public OvenStateModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========


    /** @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        super.setSimulationRunParameters(simParams);
        // Retrieve the reference to the owner component that must be passed as a simulation run parameter
        assert simParams.containsKey(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.owner = (Oven) simParams.get(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        this.setLogger(new StandardComponentLogger(this.owner));
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        this.lastReceived = null;
        this.isBaking = OvenElectricityModel.State.OFF;

        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    @Override
    public ArrayList<EventI> output() {
        assert	this.lastReceived != null;
        ArrayList<EventI> ret = new ArrayList<EventI>();
        ret.add(this.lastReceived);
        this.lastReceived = null;
        return ret;
    }

    @Override
    public Duration timeAdvance() {
        if (this.lastReceived != null) {
            // Trigger an immediate internal transition
            return Duration.zero(this.getSimulatedTimeUnit());
        } else {
            // Wait until the next external event that will trigger an internal transition
            return Duration.INFINITY;
        }
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        super.userDefinedExternalTransition(elapsedTime);

        // Get the vector of current external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1;

        this.lastReceived = (AbstractOvenEvent) currentEvents.get(0);

        StringBuffer message = new StringBuffer(this.uri);
        message.append(" executes the external event ");
        message.append(this.lastReceived.getClass().getSimpleName());
        message.append("(");
        message.append(this.lastReceived.getTimeOfOccurrence().getSimulatedTime());
        message.append(")\n");
        this.logMessage(message.toString());
    }

    /** @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
