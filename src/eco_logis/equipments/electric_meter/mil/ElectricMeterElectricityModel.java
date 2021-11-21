package eco_logis.equipments.electric_meter.mil;

import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the electricity model of the electric meter
 */
public class ElectricMeterElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The electric meter model URI */
    public static final String URI = ElectricMeterElectricityModel.class.getSimpleName();

    /** The step duration for equations (in seconds) */
    protected static final double STEP = 0.1;


    // ========== Attributes ==========


    /** The evaluation step in a duration */
    protected final Duration evaluationStep;

    /** The current consumption of the crypto miner */
    @ImportedVariable(type = Double.class)
    protected Value<Double> currentCryptoConsumption;

    /** The current consumption of the dishwasher */
    @ImportedVariable(type = Double.class)
    protected Value<Double> currentDishwasherConsumption;

    /** The current total consumption in the house */
    @InternalVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========


    /** @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)  */
    public ElectricMeterElectricityModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        evaluationStep = new Duration(STEP, getSimulatedTimeUnit());
        setLogger(new StandardLogger());
    }


    // ========== Class methods ==========


    /**
     * Update the current consumption of all the house
     * 
     * @param d The time since the last update
     */
    protected void updateConsumption(Duration d) {
        currentConsumption.v = currentCryptoConsumption.v + currentDishwasherConsumption.v;
        currentConsumption.time = currentConsumption.time.add(d);
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);

        currentConsumption.v = 0.0;

        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        // The electric meter don't export events
        return null;
    }

    /** @see AtomicHIOA#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        return evaluationStep;
    }

    /** @see AtomicHIOA#userDefinedInternalTransition(Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);
        updateConsumption(elapsedTime);

        logMessage("Current global consumption " + currentConsumption.v + " watts\n");
    }

    /** @see AtomicHIOA#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        this.updateConsumption(endTime.subtract(currentConsumption.time));
        
        logMessage("Simulation ends!\n");
        super.endSimulation(endTime);
    }
    
}
