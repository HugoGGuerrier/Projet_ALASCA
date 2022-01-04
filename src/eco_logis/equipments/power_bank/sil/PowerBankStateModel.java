package eco_logis.equipments.power_bank.sil;

import eco_logis.equipments.power_bank.PowerBank;
import eco_logis.equipments.power_bank.PowerBankRTAtomicSimulatorPlugin;
import eco_logis.equipments.power_bank.mil.events.AbstractPowerBankEvent;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
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
 * This class represent the event broadcasting for the power bank
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(
        imported = {
                ChargePowerBank.class,
                DischargePowerBank.class,
                StandbyPowerBank.class
        },
        exported = {
                ChargePowerBank.class,
                DischargePowerBank.class,
                StandbyPowerBank.class
        }
)
public class PowerBankStateModel
    extends AtomicModel
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = PowerBankStateModel.class.getSimpleName();


    // ========== Attributes ==========


    /** The current power bank state */
    protected PowerBank.State currentState;

    /** The last received event */
    protected AbstractPowerBankEvent lastEvent;

    /** The model owner */
    protected PowerBank owner;


    // ========== Constructors ==========


    public PowerBankStateModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }


    // ========== Override methods ==========


    /** @see AtomicModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super
        super.setSimulationRunParameters(simParams);

        // Get the model owner in the params
        assert simParams.containsKey(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (PowerBank) simParams.get(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

        // Set the logger in the component logger
        setLogger(new StandardComponentLogger(owner));
    }

    /** @see AtomicModel#initialiseState(Time) */
    @Override
    public void	initialiseState(Time initialTime) {
        // Call the super
        super.initialiseState(initialTime);

        // Set the initial miner state
        lastEvent = null;
        currentState = PowerBank.State.STANDBY;

        // Tracing
        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see AtomicModel#output() */
    @Override
    public ArrayList<EventI> output() {
        assert lastEvent != null;
        ArrayList<EventI> res = new ArrayList<>();
        res.add(lastEvent);
        lastEvent = null;
        return res;
    }

    /** @see AtomicModel#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if(lastEvent != null) return Duration.zero(getSimulatedTimeUnit());
        return Duration.INFINITY;
    }

    /** @see AtomicModel#userDefinedExternalTransition(Duration) */
    @Override
    public void	userDefinedExternalTransition(Duration elapsedTime) {
        // Call the super
        super.userDefinedExternalTransition(elapsedTime);

        // Get the current events to perform transitions
        ArrayList<EventI> events = getStoredEventAndReset();
        assert events != null && events.size() == 1;

        // Get the last received event
        lastEvent = (AbstractPowerBankEvent) events.get(0);

        // Trace
        logMessage(URI + " executes the external event " +
                lastEvent.getClass().getSimpleName() + " at " +
                lastEvent.getTimeOfOccurrence().getSimulatedTime() + "\n");
    }

    /** @see AtomicModel#endSimulation(Time) */
    @Override
    public void	endSimulation(Time endTime) throws Exception {
        logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
