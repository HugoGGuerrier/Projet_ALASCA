package eco_logis.equipments.power_bank.mil;

import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UnitarySim {
    public static void main(String[] args) {
        try {

            // The map of the atomic model description
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // Add the power bank electricity model
            atomicModelDescriptors.put(
                    PowerBankElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            PowerBankElectricityModel.class,
                            PowerBankElectricityModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the power bank charge model
            atomicModelDescriptors.put(
                    PowerBankChargeModel.URI,
                    AtomicHIOA_Descriptor.create(
                            PowerBankChargeModel.class,
                            PowerBankChargeModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );

            // Add the power bank user model
            atomicModelDescriptors.put(
                    PowerBankUserModel.URI,
                    AtomicModelDescriptor.create(
                            PowerBankUserModel.class,
                            PowerBankUserModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_ENGINE
                    )
            );


            // Map that contains coupled model description
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            // Set of submodels of the coupled power bank model
            Set<String> subModel = new HashSet<>();
            subModel.add(PowerBankElectricityModel.URI);
            subModel.add(PowerBankChargeModel.URI);
            subModel.add(PowerBankUserModel.URI);

            // The power bank submodel connections
            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // Add the charge event connection
            connections.put(
                    new EventSource(PowerBankUserModel.URI, ChargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricityModel.URI, ChargePowerBank.class),
                            new EventSink(PowerBankChargeModel.URI, ChargePowerBank.class)
                    }
            );

            // Add the discharge event connection
            connections.put(
                    new EventSource(PowerBankUserModel.URI, DischargePowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricityModel.URI, DischargePowerBank.class),
                            new EventSink(PowerBankChargeModel.URI, DischargePowerBank.class)
                    }
            );

            // Add the stand by event connection
            connections.put(
                    new EventSource(PowerBankUserModel.URI, StandbyPowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricityModel.URI, StandbyPowerBank.class),
                            new EventSink(PowerBankChargeModel.URI, StandbyPowerBank.class)
                    }
            );

            // Add the standby event when no more battery and fully loaded
            connections.put(
                    new EventSource(PowerBankChargeModel.URI, StandbyPowerBank.class),
                    new EventSink[] {
                            new EventSink(PowerBankElectricityModel.URI, StandbyPowerBank.class),
                    }
            );

            // Variable bindings
            Map<VariableSource, VariableSink[]> bindings = new HashMap<>();

            bindings.put(
                    new VariableSource("currentChargeLevel", Double.class, PowerBankChargeModel.URI),
                    new VariableSink[] {
                            new VariableSink("currentChargeLevel", Double.class, PowerBankElectricityModel.URI)
                    }
            );

            // Add the power bank coupled model
            coupledModelDescriptors.put(
                    PowerBankCoupledModel.URI,
                    new CoupledHIOA_Descriptor(
                            PowerBankCoupledModel.class,
                            PowerBankCoupledModel.URI,
                            subModel,
                            null,
                            null,
                            connections,
                            null,
                            SimulationEngineCreationMode.COORDINATION_ENGINE,
                            null,
                            null,
                            bindings
                    )
            );

            // Create the simulation architecture
            ArchitectureI architecture = new Architecture(
                    PowerBankCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    TimeUnit.SECONDS
            );

            // Start the simulation
            SimulationEngine engine = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
            engine.doStandAloneSimulation(0.0, 100.0);
            System.exit(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
