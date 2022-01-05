package eco_logis.equipments.dishwasher.sil;

import eco_logis.equipments.dishwasher.Dishwasher;
import eco_logis.equipments.dishwasher.DishwasherRTAtomicSimulatorPlugin;
import eco_logis.equipments.dishwasher.mil.events.*;
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
 * This class represents the state model of the dishwasher.
 * It broadcast event from the component methods to the models.
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(
        imported = {
                SetEcoProgram.class,
                SetFastProgram.class,
                SetFullProgram.class,
                SetRinseProgram.class,
                SwitchOffDishwasher.class
        },
        exported = {
                SetEcoProgram.class,
                SetFastProgram.class,
                SetFullProgram.class,
                SetRinseProgram.class,
                SwitchOffDishwasher.class
        }
)
public class DishwasherStateModel
        extends AtomicModel
{

        // ========== Macros ==========


        /** The model unique URI */
        public static final String URI = DishwasherStateModel.class.getSimpleName();


        // ========== Attributes ==========


        /** If the dishwasher is currently planned */
        private boolean isPlanned;

        /** If the dishwasher is currently washing something */
        private boolean isWashing;

        /** The last received event */
        protected AbstractDishwasherEvent lastEvent;

        /** The model owner */
        protected Dishwasher owner;


        // ========== Constructors ==========


        /** @see AtomicModel#AtomicModel(String, TimeUnit, SimulatorI) */
        public DishwasherStateModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
                super(uri, simulatedTimeUnit, simulationEngine);
        }


        // ========== Override methods ==========


        /** @see AtomicModel#setSimulationRunParameters(Map) */
        @Override
        public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
                // Call the super
                super.setSimulationRunParameters(simParams);

                // Get the model owner in the params
                assert simParams.containsKey(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
                owner = (Dishwasher) simParams.get(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);

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
                isPlanned = false;
                isWashing = false;

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
                lastEvent = (AbstractDishwasherEvent) events.get(0);

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
