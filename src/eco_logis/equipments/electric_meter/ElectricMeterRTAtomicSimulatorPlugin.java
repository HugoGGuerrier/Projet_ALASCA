package eco_logis.equipments.electric_meter;

import eco_logis.equipments.crypto_miner.CryptoMinerRTAtomicSimulatorPlugin;
import eco_logis.equipments.crypto_miner.mil.events.MineOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.MineOnCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOffCryptoMiner;
import eco_logis.equipments.crypto_miner.mil.events.SwitchOnCryptoMiner;
import eco_logis.equipments.crypto_miner.sil.CryptoMinerElectricitySILModel;
import eco_logis.equipments.dishwasher.DishwasherRTAtomicSimulatorPlugin;
import eco_logis.equipments.dishwasher.mil.events.*;
import eco_logis.equipments.dishwasher.sil.DishwasherElectricitySILModel;
import eco_logis.equipments.electric_meter.mil.ElectricMeterElectricityModel;
import eco_logis.equipments.electric_meter.sil.ElectricMeterCoupledModel;
import eco_logis.equipments.electric_meter.sil.ElectricMeterElectricitySILModel;
import eco_logis.equipments.generator.GeneratorRTAtomicSimulatorPlugin;
import eco_logis.equipments.generator.mil.events.SwitchOffGenerator;
import eco_logis.equipments.generator.mil.events.SwitchOnGenerator;
import eco_logis.equipments.generator.sil.GeneratorElectricitySILModel;
import eco_logis.equipments.generator.sil.GeneratorFuelSILModel;
import eco_logis.equipments.oven.OvenRTAtomicSimulatorPlugin;
import eco_logis.equipments.oven.mil.events.SwitchOffOven;
import eco_logis.equipments.oven.mil.events.SwitchOnOven;
import eco_logis.equipments.oven.sil.OvenElectricitySILModel;
import eco_logis.equipments.power_bank.PowerBankRTAtomicSimulatorPlugin;
import eco_logis.equipments.power_bank.mil.events.ChargePowerBank;
import eco_logis.equipments.power_bank.mil.events.DischargePowerBank;
import eco_logis.equipments.power_bank.mil.events.StandbyPowerBank;
import eco_logis.equipments.power_bank.sil.PowerBankChargeSILModel;
import eco_logis.equipments.power_bank.sil.PowerBankElectricitySILModel;
import eco_logis.equipments.wind_turbine.WindTurbineRTAtomicSimulatorPlugin;
import eco_logis.equipments.wind_turbine.sil.WindTurbineElectricitySILModel;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>ElectricMeterRTAtomicSimulatorPlugin</code> implements
 * the simulation plug-in for the <code>ElectricMeter</code> component.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ElectricMeterRTAtomicSimulatorPlugin
    extends RTAtomicSimulatorPlugin
{

    // ========== Macros ==========


    /** URI used for unit tests */
    public static final String UNIT_TEST_SIM_ARCHITECTURE_URI = "UnitTestElectricMeter";

    /** Name used to pass the owner component reference as simulation parameter */
    public static final String METER_REFERENCE_NAME = "EMCRN";

    /** URI of the crypto miner electricity model */
    protected static final String CRYPTO_MINER_ELECTRICITY_MODEL_URI = CryptoMinerElectricitySILModel.URI;

    /** Class implementing the crypto miner electricity model */
    protected static final Class<CryptoMinerElectricitySILModel> CRYPTO_MINER_ELECTRICITY_SIL_MODEL_CLASS = CryptoMinerElectricitySILModel.class;

    /** URI of the dishwasher electricity model */
    protected static final String DISHWASHER_ELECTRICITY_MODEL_URI = DishwasherElectricitySILModel.URI;

    /** Class implementing the dishwasher electricity model */
    protected static final Class<DishwasherElectricitySILModel> DISHWASHER_ELECTRICITY_MODEL_CLASS = DishwasherElectricitySILModel.class;

    /** URI of the generator electricity model */
    protected static final String GENERATOR_ELECTRICITY_MODEL_URI = GeneratorElectricitySILModel.URI;

    /** URI of the generator fuel model */
    protected static final String GENERATOR_FUEL_MODEL_URI = GeneratorFuelSILModel.URI;

    /** Class implementing the generator electricity model */
    protected static final Class<GeneratorElectricitySILModel> GENERATOR_ELECTRICITY_MODEL_CLASS = GeneratorElectricitySILModel.class;

    /** Class implementing the generator fuel model */
    protected static final Class<GeneratorFuelSILModel> GENERATOR_FUEL_SIL_MODEL_CLASS = GeneratorFuelSILModel.class;

    /** URI of the oven electricity model */
    protected static final String OVEN_ELECTRICITY_MODEL_URI = OvenElectricitySILModel.URI;

    /** Class implementing the oven electricity model */
    protected static final Class<OvenElectricitySILModel> OVEN_ELECTRICITY_MODEL_CLASS = OvenElectricitySILModel.class;

    /** URI of the power bank electricity model */
    protected static final String POWER_BANK_ELECTRICITY_MODEL_URI = PowerBankElectricitySILModel.URI;

    /** URI of the power bank charge model */
    protected static final String POWER_BANK_CHARGE_MODEL_URI = PowerBankChargeSILModel.URI;

    /** Class implementing the power bank electricity model */
    protected static final Class<PowerBankElectricitySILModel> POWER_BANK_ELECTRICITY_SIL_MODEL_CLASS = PowerBankElectricitySILModel.class;

    /** Class implementing the power bank charge model */
    protected static final Class<PowerBankChargeSILModel> POWER_BANK_CHARGE_SIL_MODEL_CLASS = PowerBankChargeSILModel.class;

    /** URI of the wind turbine electricity model */
    protected static final String WIND_TURBINE_ELECTRICITY_MODEL_URI = WindTurbineElectricitySILModel.URI;

    /** Class implementing the wind turbine electricity model */
    protected static final Class<WindTurbineElectricitySILModel> WIND_TURBINE_ELECTRICITY_MODEL_CLASS = WindTurbineElectricitySILModel.class;


    public static final String CONS = "cons";
    public static final String PROD = "prod";

    // ========== Class methods ==========


    /**
     * Create and set the simulation architecture internal to this component
     *
     * <p><strong>Contract</strong></p>
     * <pre>
     * pre	{@code simArchURI != null && !simArchURIisEmpty()}
     * pre	{@code accFactor > 0.0}
     * post	true		// no postcondition.
     * </pre>
     *
     * @param simArchURI	URI of the simulation architecture to be created.
     * @param accFactor		acceleration factor used in the real time simulation.
     * @throws Exception	<i>to do</i>.
     */
    public void initialiseSimulationArchitecture(String simArchURI, double accFactor) throws Exception {
        Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

        Set<String> submodels = new HashSet<String>();
        submodels.add(ElectricMeterElectricitySILModel.URI);
        submodels.add(CRYPTO_MINER_ELECTRICITY_MODEL_URI);
        submodels.add(GENERATOR_ELECTRICITY_MODEL_URI);
        submodels.add(GENERATOR_FUEL_MODEL_URI);
        submodels.add(POWER_BANK_ELECTRICITY_MODEL_URI);
        submodels.add(POWER_BANK_CHARGE_MODEL_URI);
        submodels.add(DISHWASHER_ELECTRICITY_MODEL_URI);
        /* TODO
        submodels.add(OVEN_ELECTRICITY_MODEL_URI);
        submodels.add(WIND_TURBINE_ELECTRICITY_MODEL_URI);
         */

        atomicModelDescriptors.put(
                ElectricMeterElectricitySILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        ElectricMeterElectricitySILModel.class,
                        ElectricMeterElectricitySILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                CRYPTO_MINER_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        CRYPTO_MINER_ELECTRICITY_SIL_MODEL_CLASS,
                        CRYPTO_MINER_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor)
        );

        atomicModelDescriptors.put(
                GENERATOR_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        GENERATOR_ELECTRICITY_MODEL_CLASS,
                        GENERATOR_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                GENERATOR_FUEL_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        GENERATOR_FUEL_SIL_MODEL_CLASS,
                        GENERATOR_FUEL_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                POWER_BANK_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        POWER_BANK_ELECTRICITY_SIL_MODEL_CLASS,
                        POWER_BANK_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                POWER_BANK_CHARGE_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        POWER_BANK_CHARGE_SIL_MODEL_CLASS,
                        POWER_BANK_CHARGE_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        atomicModelDescriptors.put(
                DISHWASHER_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        DISHWASHER_ELECTRICITY_MODEL_CLASS,
                        DISHWASHER_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor
                )
        );

        /* TODO


        atomicModelDescriptors.put(
                OVEN_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        OVEN_ELECTRICITY_MODEL_CLASS,
                        OVEN_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));

        atomicModelDescriptors.put(
                WIND_TURBINE_ELECTRICITY_MODEL_URI,
                RTAtomicHIOA_Descriptor.create(
                        WIND_TURBINE_ELECTRICITY_MODEL_CLASS,
                        WIND_TURBINE_ELECTRICITY_MODEL_URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        accFactor));
        */


        Map<Class<? extends EventI>, EventSink[]> imported = new HashMap<>();

        if (!simArchURI.equals(UNIT_TEST_SIM_ARCHITECTURE_URI)) {

            /* TODO
            imported.put(
                    SwitchOnOven.class,
                    new EventSink[] {
                            new EventSink(OVEN_ELECTRICITY_MODEL_URI, SwitchOnOven.class)
                    });
            imported.put(
                    SwitchOffOven.class,
                    new EventSink[] {
                            new EventSink(OVEN_ELECTRICITY_MODEL_URI, SwitchOffOven.class)
                    });
             */

            // --- Crypto miner events

            imported.put(
                    SwitchOnCryptoMiner.class,
                    new EventSink[] {
                            new EventSink(CRYPTO_MINER_ELECTRICITY_MODEL_URI, SwitchOnCryptoMiner.class)
                    });
            imported.put(
                    SwitchOffCryptoMiner.class,
                    new EventSink[] {
                            new EventSink(CRYPTO_MINER_ELECTRICITY_MODEL_URI, SwitchOffCryptoMiner.class)
                    });
            imported.put(
                    MineOnCryptoMiner.class,
                    new EventSink[] {
                            new EventSink(CRYPTO_MINER_ELECTRICITY_MODEL_URI, MineOnCryptoMiner.class)
                    });
            imported.put(
                    MineOffCryptoMiner.class,
                    new EventSink[] {
                            new EventSink(CRYPTO_MINER_ELECTRICITY_MODEL_URI, MineOffCryptoMiner.class)
                    });

            // --- Generator events

            imported.put(
                    SwitchOnGenerator.class,
                    new EventSink[] {
                            new EventSink(GENERATOR_ELECTRICITY_MODEL_URI, SwitchOnGenerator.class),
                            new EventSink(GENERATOR_FUEL_MODEL_URI, SwitchOnGenerator.class)
                    }
            );

            imported.put(
                    SwitchOffGenerator.class,
                    new EventSink[] {
                            new EventSink(GENERATOR_ELECTRICITY_MODEL_URI, SwitchOffGenerator.class),
                            new EventSink(GENERATOR_FUEL_MODEL_URI, SwitchOffGenerator.class)
                    }
            );

            // -- Power bank events

            imported.put(
                    StandbyPowerBank.class,
                    new EventSink[] {
                            new EventSink(POWER_BANK_ELECTRICITY_MODEL_URI, StandbyPowerBank.class),
                            new EventSink(POWER_BANK_CHARGE_MODEL_URI, StandbyPowerBank.class)
                    }
            );

            imported.put(
                    ChargePowerBank.class,
                    new EventSink[] {
                            new EventSink(POWER_BANK_ELECTRICITY_MODEL_URI, ChargePowerBank.class),
                            new EventSink(POWER_BANK_CHARGE_MODEL_URI, ChargePowerBank.class)
                    }
            );

            imported.put(
                    DischargePowerBank.class,
                    new EventSink[] {
                            new EventSink(POWER_BANK_ELECTRICITY_MODEL_URI, DischargePowerBank.class),
                            new EventSink(POWER_BANK_CHARGE_MODEL_URI, DischargePowerBank.class)
                    }
            );

            // --- Dishwasher events

            imported.put(
                    SetEcoProgram.class,
                    new EventSink[] {
                            new EventSink(DISHWASHER_ELECTRICITY_MODEL_URI, SetEcoProgram.class)
                    }
            );

            imported.put(
                    SetFastProgram.class,
                    new EventSink[] {
                            new EventSink(DISHWASHER_ELECTRICITY_MODEL_URI, SetFastProgram.class)
                    }
            );

            imported.put(
                    SetFullProgram.class,
                    new EventSink[] {
                            new EventSink(DISHWASHER_ELECTRICITY_MODEL_URI, SetFullProgram.class)
                    }
            );

            imported.put(
                    SetRinseProgram.class,
                    new EventSink[] {
                            new EventSink(DISHWASHER_ELECTRICITY_MODEL_URI, SetRinseProgram.class)
                    }
            );

            imported.put(
                    SwitchOffDishwasher.class,
                    new EventSink[] {
                            new EventSink(DISHWASHER_ELECTRICITY_MODEL_URI, SwitchOffDishwasher.class)
                    }
            );
        }

        // Variable bindings between exporting and importing models
        Map<VariableSource, VariableSink[]> bindings = new HashMap<>();

        // --- Bindings for the crypto miner

        bindings.put(
                new VariableSource("currentConsumption",
                        Double.class,
                        CRYPTO_MINER_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentCryptoConsumption",
                                Double.class,
                                ElectricMeterElectricitySILModel.URI)
                }
        );

        // --- Bindings for the generator

        bindings.put(
                new VariableSource("currentProduction",
                        Double.class,
                        GENERATOR_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentGeneratorProduction",
                                Double.class,
                                ElectricMeterElectricitySILModel.URI)
                }
        );

        bindings.put(
                new VariableSource("currentFuelLevel",
                        Double.class,
                        GENERATOR_FUEL_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentFuelLevel",
                                Double.class,
                                GENERATOR_ELECTRICITY_MODEL_URI)
                }
        );

        // -- Bindings for the power bank

        bindings.put(
                new VariableSource("currentConsumption",
                        Double.class,
                        POWER_BANK_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentPowerBankConsumption",
                                Double.class,
                                ElectricMeterElectricitySILModel.URI)
                }
        );

        bindings.put(
                new VariableSource("currentProduction",
                        Double.class,
                        POWER_BANK_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentPowerBankProduction",
                                Double.class,
                                ElectricMeterElectricitySILModel.URI)
                }
        );

        bindings.put(
                new VariableSource("currentChargeLevel",
                        Double.class,
                        POWER_BANK_CHARGE_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentChargeLevel",
                                Double.class,
                                POWER_BANK_ELECTRICITY_MODEL_URI)
                }
        );

        // --- Bindings for the dishwasher

        bindings.put(
                new VariableSource("currentConsumption",
                        Double.class,
                        DISHWASHER_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentDishwasherConsumption",
                                Double.class,
                                ElectricMeterElectricitySILModel.URI)
                }
        );

        /* TODO
        bindings.put(
                new VariableSource("currentConsumption",
                        Double.class,
                        OVEN_ELECTRICITY_MODEL_URI),
                new VariableSink[] {
                        new VariableSink("currentOvenConsumption",
                                Double.class,
                                ElectricMeterElectricityModel.URI)
                });
        */

        // Coupled model descriptor: an HIOA requires a RTCoupledHIOA_Descriptor
        coupledModelDescriptors.put(
                ElectricMeterCoupledModel.URI,
                new RTCoupledHIOA_Descriptor(
                        ElectricMeterCoupledModel.class,
                        ElectricMeterCoupledModel.URI,
                        submodels,
                        imported,
                        null,
                        null,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        null,
                        null,
                        bindings,
                        accFactor)
        );

        // This sets the architecture in the plug-in for further reference and use
        this.setSimulationArchitecture(
                new RTArchitecture(
                        simArchURI,
                        ElectricMeterCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        accFactor)
        );
    }


    // ========== Override methods ==========

    @Override
    public Object getModelStateValue(String uri, String name) throws Exception {
        assert uri.equals(ElectricMeterElectricitySILModel.URI);
        assert name != null;

        if(name.equals(PROD)) {
            return ((ElectricMeterElectricitySILModel) getDescendentModel(uri)).getCurrentProduction();
        } else if (name.equals(CONS)) {
            return ((ElectricMeterElectricitySILModel) getDescendentModel(uri)).getCurrentConsumption();
        }

        return null;
    }


    /** @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map) */
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
        simParams.put(METER_REFERENCE_NAME, this.getOwner());
        simParams.put(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        simParams.put(GeneratorRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        simParams.put(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        simParams.put(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        /*

        simParams.put(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
        simParams.put(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME, this.getOwner());
         */

        super.setSimulationRunParameters(simParams);

        simParams.remove(METER_REFERENCE_NAME);
        simParams.remove(CryptoMinerRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        simParams.remove(GeneratorRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        simParams.remove(PowerBankRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        simParams.remove(DishwasherRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        /*

        simParams.remove(OvenRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        simParams.remove(WindTurbineRTAtomicSimulatorPlugin.OWNER_REFERENCE_NAME);
        */
    }

}
