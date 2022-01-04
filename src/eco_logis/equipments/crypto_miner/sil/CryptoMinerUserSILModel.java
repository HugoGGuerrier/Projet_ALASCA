package eco_logis.equipments.crypto_miner.sil;

import eco_logis.equipments.crypto_miner.CryptoMiner;
import eco_logis.equipments.crypto_miner.CryptoMinerRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the user action for the Crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CryptoMinerUserSILModel
    extends AtomicModel
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = CryptoMinerUserSILModel.class.getSimpleName();

    /** Run parameter name for the step mean duration */
    public static final String STEP_MEAN_DURATION_RUNPNAME = URI + ":STEP_MEAN_DURATION";

    /** The mean duration between two events in second */
    protected static double STEP_MEAN_DURATION = 2.0;


    // ========== Attributes ==========


    /** The random number generator */
    protected RandomDataGenerator rg;

    /** The current scenario step */
    protected int currentStep;

    /** Time to the next event */
    protected Duration toNext;

    /** The model owner */
    protected CryptoMiner owner;


    // ========== Constructors ==========


    /** @see AtomicModel#AtomicModel(String, TimeUnit, SimulatorI) */
    public CryptoMinerUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        rg = new RandomDataGenerator();
    }


    // ========== Class methods ==========


    /**
     * Generate the duration to the next event
     */
    private void generateNextEvent() {
        if(currentStep <= 4) {

            double delay = Math.max(this.rg.nextGaussian(STEP_MEAN_DURATION, STEP_MEAN_DURATION/2.0), 0.1);
            toNext = new Duration(delay, getSimulatedTimeUnit());

        } else {
            toNext = Duration.INFINITY;
        }
    }


    // ========== Override methods ==========


    /** @see AtomicModel#setSimulationRunParameters(Map) */
    @Override
    public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Call the super
        super.setSimulationRunParameters(simParams);

        // Get the owner
        assert simParams.containsKey(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        owner = (CryptoMiner) simParams.get(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        setLogger(new StandardComponentLogger(owner));

        // Get the step mean duration
        if(simParams.containsKey(STEP_MEAN_DURATION_RUNPNAME)) {
            STEP_MEAN_DURATION = (double) simParams.get(STEP_MEAN_DURATION_RUNPNAME);
        }
    }

    /** @see AtomicModel#initialiseState(Time) */
    @Override
    public void	initialiseState(Time initialTime) {
        // Initialise the state
        rg.reSeedSecure();
        generateNextEvent();
        currentStep = 1;

        // Call the super
        super.initialiseState(initialTime);

        // Tracing
        toggleDebugMode();
        this.logMessage("simulation begins.\n");
    }

    /** @see AtomicModel#output() */
    @Override
    public ArrayList<EventI> output() {
        return null;
    }

    /** @see AtomicModel#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        return toNext;
    }

    /** @see AtomicModel#userDefinedInternalTransition(Duration) */
    @Override
    public void	userDefinedInternalTransition(Duration elapsedTime) {
        // Call super
        super.userDefinedInternalTransition(elapsedTime);

        // Prepare the log message
        StringBuilder msg = new StringBuilder(URI);

        // Switch the case of the transition
        switch (currentStep) {

            case 1:
                owner.runTask(o -> {
                    try {
                        ((CryptoMiner)o).powerOn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                msg.append(" executes the operation powerOn");
                break;

            case 2:
                owner.runTask(o -> {
                    try {
                        ((CryptoMiner)o).startMiner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                msg.append(" executes the operation startMiner");
                break;

            case 3:
                owner.runTask(o -> {
                    try {
                        ((CryptoMiner)o).stopMiner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                msg.append(" executes the operation stopMiner");
                break;

            case 4:
                owner.runTask(o -> {
                    try {
                        ((CryptoMiner)o).powerOff();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                msg.append(" executes the operation powerOff");
                break;

            default:
                msg.append(" ended.\n");

        }

        // Get the next time of event
        currentStep++;
        generateNextEvent();

        // Log the message
        logMessage(msg.toString() + '\n');
    }

    /** @see AtomicModel#endSimulation(Time) */
    @Override
    public void	endSimulation(Time endTime) throws Exception {
        logMessage("simulation ends.\n");
        super.endSimulation(endTime);
    }

}
