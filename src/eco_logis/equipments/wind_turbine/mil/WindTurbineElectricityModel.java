package eco_logis.equipments.wind_turbine.mil;

import eco_logis.equipments.wind_turbine.mil.events.AbstractWindTurbineEvent;
import eco_logis.equipments.wind_turbine.mil.events.BlockWindTurbine;
import eco_logis.equipments.wind_turbine.mil.events.UnblockWindTurbine;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
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
 * This class represents the electricity model of a wind turbine
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        UnblockWindTurbine.class,
        BlockWindTurbine.class})
public class WindTurbineElectricityModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String	URI = WindTurbineElectricityModel.class.getSimpleName();


    // ========== Attributes ==========


    /** If the wind turbine is currently on */
    private boolean isOn;

    /** If the internal state has changed */
    private boolean hasChanged;

    /** @see ExternalWindModel#externalWindSpeed */
    @ImportedVariable(type = Double.class)
    protected Value<Double> externalWindSpeed;

    /** The current production of the wind turbine in a shared var */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentProduction = new Value<>(this, 0.0);


    // ========== Constructors ==========


    /**
     * Create a new wind turbine electricity model
     *
     * @see AtomicHIOA#AtomicHIOA(String, TimeUnit, SimulatorI)
     *
     * @param uri The electricity model URI
     * @param timeUnit The simulation time unit
     * @param engine The simulation engine
     *
     * @throws Exception TODO
     */
    public WindTurbineElectricityModel(String uri, TimeUnit timeUnit, SimulatorI engine) throws Exception {
        super(uri, timeUnit, engine);
        setLogger(new StandardLogger());
    }


    // ========== Getters ==========


    /**
     * Get if the wind turbine is currently on
     *
     * @return True if the wind turbine is on, false else
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * Get if the model has to do an internal transition
     *
     * @return True if there is an internal transition, false else
     */
    public boolean hasChanged() {
        return hasChanged;
    }


    // ========== Setters ==========


    /**
     * Set the wind turbine on/off (unblocked/blocked)
     *
     * @param on True if the wind turbine is on/unblocked, false else
     */
    public void setOn(boolean on) {
        isOn = on;
    }

    /**
     * Set the model hasChanged var
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
        hasChanged = false;

        toggleDebugMode();
        logMessage("Simulation starts...\n");
    }

    /** @see AtomicHIOA#initialiseVariables(Time) */
    @Override
    protected void initialiseVariables(Time startTime) {
        super.initialiseVariables(startTime);
        currentProduction.v = 0.0d;
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

        // Set the current production
        if(isOn) {
            // At maximum speed (150km/h), the wind turbine produces 5kW
            currentProduction.v = externalWindSpeed.v * 5000 / 150.0;
        } else {
            currentProduction.v = 0.0d;
        }

        // Set the value time
        currentProduction.time = getCurrentStateTime();

        // Tracing
        logMessage("Current production " + currentProduction.v + " at " + currentProduction.time + "\n");
    }

    /** @see AtomicHIOA#userDefinedExternalTransition(Duration) */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        // Get the current event
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert	currentEvents != null && currentEvents.size() == 1;
        Event currentEvent = (Event) currentEvents.get(0);

        // Execute the event on the model
        assert currentEvent instanceof AbstractWindTurbineEvent;
        currentEvent.executeOn(this);

        super.userDefinedExternalTransition(elapsedTime);
    }

    /** @see AtomicHIOA#endSimulation(Time) */
    @Override
    public void endSimulation(Time endTime) throws Exception {
        logMessage("simulations ends!\n");
        super.endSimulation(endTime);
    }


}
