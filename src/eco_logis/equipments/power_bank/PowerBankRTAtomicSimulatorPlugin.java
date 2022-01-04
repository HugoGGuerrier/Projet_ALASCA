package eco_logis.equipments.power_bank;

import eco_logis.equipments.power_bank.mil.PowerBankCoupledModel;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import eco_logis.equipments.power_bank.sil.PowerBankChargeSILModel;
import eco_logis.equipments.power_bank.sil.PowerBankElectricitySILModel;
import eco_logis.equipments.power_bank.sil.PowerBankStateModel;
import eco_logis.equipments.power_bank.sil.PowerBankUserSILModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PowerBankRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for the unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestPowerBank";

    /** Owner name used to pass the param in the model params */
    public static final String OWNER_REFERENCE_NAME = "PBCRN";


    // ========== Class methods ==========


    /**
     * Create and initialise the simulation architecture for the power bank
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true
     * </pre>
     *
     * @param simArchURI URI of the simulation architecture to be created.
     * @param accFactor	Acceleration factor used in the real time simulation.
     * @throws Exception TODO
     */
    public void	initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {
        // Create the model descriptors
        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

        // Create and set the sub-models set
        Set<String> submodels = new HashSet<>();
        submodels.add(PowerBankStateModel.URI);
        submodels.add(PowerBankUserSILModel.URI);
        submodels.add(PowerBankChargeSILModel.URI);

        // Declare the connections and reexported maps for the events
        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;
        Map<VariableSource, VariableSink[]> bindings = null;

        // Add the atomic model
        atomicModelDescriptors.put(
                PowerBankStateModel.URI,
                RTAtomicModelDescriptor.create(
                        PowerBankStateModel.class,
                        PowerBankStateModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                PowerBankUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        PowerBankUserSILModel.class,
                        PowerBankUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                PowerBankChargeSILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        PowerBankChargeSILModel.class,
                        PowerBankChargeSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        if(simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            // Add the electricity model to the architecture
            submodels.add(PowerBankElectricitySILModel.URI);

            atomicModelDescriptors.put(
                    PowerBankElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            PowerBankElectricitySILModel.class,
                            PowerBankElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            accFactor
                    )
            );

            // Create the event connection
            connections = new HashMap<>();

            connections.put(
                    new EventSource(PowerBankStateModel.URI, ChargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricitySILModel.URI, ChargePowerBank.class),
                            new EventSink(PowerBankChargeSILModel.URI, ChargePowerBank.class)
                    }
            );

            connections.put(
                    new EventSource(PowerBankStateModel.URI, DischargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricitySILModel.URI, DischargePowerBank.class),
                            new EventSink(PowerBankChargeSILModel.URI, DischargePowerBank.class)
                    }
            );

            connections.put(
                    new EventSource(PowerBankStateModel.URI, StandbyPowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricitySILModel.URI, StandbyPowerBank.class),
                            new EventSink(PowerBankChargeSILModel.URI, StandbyPowerBank.class)
                    }
            );

            connections.put(
                    new EventSource(PowerBankChargeSILModel.URI, StandbyPowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricitySILModel.URI, StandbyPowerBank.class)
                    }
            );

            // Create the variable bindings
            bindings = new HashMap<>();

            bindings.put(
                    new VariableSource("currentChargeLevel", Double.class, PowerBankChargeSILModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentChargeLevel", Double.class, PowerBankElectricitySILModel.URI)
                    }
            );

        } else {

            // Just do the basic connections
            connections = new HashMap<>();

            connections.put(
                    new EventSource(PowerBankStateModel.URI, ChargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankChargeSILModel.URI, ChargePowerBank.class)
                    }
            );

            connections.put(
                    new EventSource(PowerBankStateModel.URI, DischargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankChargeSILModel.URI, DischargePowerBank.class)
                    }
            );

            connections.put(
                    new EventSource(PowerBankStateModel.URI, StandbyPowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankChargeSILModel.URI, StandbyPowerBank.class)
                    }
            );

            // Add the reexport
            reexported = new HashMap<>();

            reexported.put(
                    ChargePowerBank.class,
                    new ReexportedEvent(PowerBankStateModel.URI, ChargePowerBank.class)
            );

            reexported.put(
                    DischargePowerBank.class,
                    new ReexportedEvent(PowerBankStateModel.URI, DischargePowerBank.class)
            );

            reexported.put(
                    StandbyPowerBank.class,
                    new ReexportedEvent(PowerBankStateModel.URI, StandbyPowerBank.class)
            );

        }

        // Create the coupled model descriptor
        coupledModelDescriptors.put(
                PowerBankCoupledModel.URI,
                new RTCoupledHIOA_Descriptor(
                        PowerBankCoupledModel.class,
                        PowerBankCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        null,
                        bindings,
                        accFactor
                )
        );

        // Set the plugin arch
        setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        PowerBankCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor
                )
        );

    }


    // ========== Override methods ==========


    /** @see RTAtomicSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        // Put the owner component in the params
        simParams.put(OWNER_REFERENCE_NAME, getOwner());

        // Call super
        super.setSimulationRunParameters(simParams);

        // Remove the owner from the params
        simParams.remove(OWNER_REFERENCE_NAME);
    }

}
