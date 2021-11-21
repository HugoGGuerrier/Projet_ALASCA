package eco_logis.equipments.crypto_miner.mil;


import eco_logis.equipments.crypto_miner.mil.events.*;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the electricity model of a crypto miner
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        SwitchOnCryptoMiner.class,
        SwitchOffCryptoMiner.class,
        MineOnCryptoMiner.class,
        MineOffCryptoMiner.class
})
public class CryptoMinerElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = CryptoMinerElectricityModel.class.getSimpleName();

    /** The consumption of the crypto miner when it's currently mining */
    private static final double MINING_CONSUMPTION = 1200.0; // Watts

    /** The consumption of the crypto miner when it's in standby mode */
    private static final double STANDBY_CONSUMPTION = 50.0; // Watts


    // ========== Attributes ==========


    /** If the miner is currently on */
    private boolean isOn;

    /** If the miner is currently mining */
    private boolean isMining;

    /** If the internal state has changed */
    private boolean hasChanged;

    /** The current consumption of the miner in a shared var */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentConsumption = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========

    /**
     * Create a new crypto miner electricity model
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The electricity model URI
     * @param timeUnit The simulation time unit
     * @param engine The simulation engine
     *
     * @throws Exception TODO
     */
    public CryptoMinerElectricityModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
        setLogger(new StandardLogger());
    }


    // ========== Getters methods ==========


    /**
     * Get if the miner is currently on
     *
     * @return True if the miner is on, false else
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * Get if the miner is currently mining
     *
     * @return True if the miner is mining, false else
     */
    public boolean isMining() {
        return isMining;
    }

    /**
     * Get if the model has to do an internal transition
     *
     * @return True if there is an internal transition, false else
     */
    public boolean hasChanged() {
        return hasChanged;
    }


    // ========== Setters methods ==========


    /**
     * Set if the crypto miner is on
     *
     * @param on True if the crypto miner is on, false else
     */
    public void setOn(boolean on) {
        isOn = on;
    }

    /**
     * Set if the crypto miner is mining
     *
     * @param mining True if the crypto miner is mining, false else
     */
    public void setMining(boolean mining) {
        isMining = mining;
    }

    /**
     * Set if the model has changed
     *
     * @param hasChanged If the model has changed
     */
    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }


    // ========== Override methods ==========


    /** @see AtomicHIOA#initialiseState(Time) */
    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        
        isOn = false;
        isMining = false;
        hasChanged = false;

        toggleDebugMode();
        logMessage("CryptoMiner simulation starts...\n");
    }

    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);
        
        currentConsumption.v = 0.0d;
    }

    /** @see AtomicHIOA#output() */
    @Override
    public ArrayList<EventI> output() {
        // The model does not export events
        return null;
    }

    /** @see AtomicHIOA#timeAdvance() */
    @Override
    public Duration timeAdvance() {
        if(this.hasChanged) {
            this.hasChanged = false;
            return new Duration(0.0, this.getSimulatedTimeUnit());
        }
        return Duration.INFINITY;
    }

    /** @see AtomicHIOA#userDefinedInternalTransition(Duration) */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        // Set the current consumption
        if(isOn) {
            if(isMining) {
                currentConsumption.v = MINING_CONSUMPTION;
            } else {
                currentConsumption.v = STANDBY_CONSUMPTION;
            }
        } else {
            currentConsumption.v = 0.0d;
        }

        // Set the value time
        currentConsumption.time = getCurrentStateTime();

        // Tracing
        StringBuilder builder = new StringBuilder("Execute internal transition | ");
        builder.append("Current consumption ").append(currentConsumption.v).append(" at ").append(currentConsumption.time);
        builder.append('\n');
        logMessage(builder.toString());
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert	currentEvents != null && currentEvents.size() == 1;
        Event currentEvent = (Event) currentEvents.get(0);

        // Execute the event on the model
        assert currentEvent instanceof AbstractCryptoMinerEvent;
        currentEvent.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see AtomicHIOA#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        logMessage("CryptoMiner simulations ends!\n");
        super.endSimulation(endTime);
    }
    
}
