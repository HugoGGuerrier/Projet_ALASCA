package eco_logis.equipments.power_bank.mil;

import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

import java.util.concurrent.TimeUnit;

/**
 * This class represents the charge model for the power bank
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
@ModelExternalEvents(imported = {
        ChargePowerBank.class,
        DischargePowerBank.class,
        StandbyPowerBank.class
},
exported = {
        StandbyPowerBank.class
})
public class PowerBankChargeModel
    extends AtomicHIOA
{

    // ========== Macros ==========


    /** The model unique URI */
    public static final String URI = PowerBankChargeModel.class.getSimpleName();

    /** The equation step */
    protected static final double STEP = 0.1;

    /** The power discharging speed (in prop/h) */
    protected static final double DISCHARGE_SPEED = 0.254;

    /** The power charging speed (in prop/h) */
    protected static final double CHARGE_SPEED = 0.203;


    // ========== Attributes ==========


    /** The evaluation step in a duration */
    protected final Duration evaluationStep;

    /** The current state of the power bank */
    private PowerBankElectricityModel.State currentState;

    /** the current charge level */
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentChargeLevel = new Value<>(this, 0.0, 0);


    // ========== Constructors ==========


    /**
     * Create a new power bank charge model
     *
     * @param uri
     * @param simulatedTimeUnit
     * @param simulationEngine
     * @throws Exception
     */
    public PowerBankChargeModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        evaluationStep = new Duration(STEP, getSimulatedTimeUnit());
        setLogger(new StandardLogger());
    }


}
