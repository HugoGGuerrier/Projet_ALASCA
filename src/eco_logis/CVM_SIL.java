package eco_logis;

import eco_logis.equipments.electric_meter.ElectricMeter;
import eco_logis.equipments.hem.HEM;
import eco_logis.equipments.oven.Oven;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * The class <code>CVM_SIL</code> execute the HEM in a software-in-the-loop
 * simulation mode.
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
public class CVM_SIL
    extends AbstractCVM
{

    // ========== Macros ==========


    /** Acceleration factor for the real time execution; it controls how fast
     *  the simulation will run but keeping the same time structure as a real
     *  time simulation running exactly at the pace of physical time; currently,
     *  because of implementation constraints revolving around the precision
     *  of the Java thread scheduler, this factor must be chosen in such a way
     *  that intervals between simulation transitions or the execution of pieces
     *  of code do not fall under 10 milliseconds approximately.			*/
    public static final double ACC_FACTOR = 1.0;

    /** Delay to start the real time simulations on every model at the
     *  same moment (the order is delivered to the models during this
     *  delay; this delay must be ample enough to give the time to notify
     *  all models of their start time and to initialise them before starting,
     *  a value that depends upon the complexity of the simulation architecture
     *  to be traversed and the component deployment (deployments on several
     *  JVM and even more several computers require a larger delay.			*/
    public static final long DELAY_TO_START_SIMULATION = 1000L;

    /** Duration  of the simulation.										*/
    public static final double SIMULATION_DURATION = 10.0;


    // ========== Constructors ==========


    public CVM_SIL() throws Exception { }


    // ========== Class methods ==========


    /** @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy() */
    @Override
    public void deploy() throws Exception {
        AbstractComponent.createComponent(
                Oven.class.getCanonicalName(),
                // the first actual parameter tells the component to create
                // a SIL simulation architecture for integration test and the
                // second 'false' that it must *not* be executed as a unit test.
                new Object[]{HEM_SIL_Supervisor.SIM_ARCHITECTURE_URI, false});
        /*
        AbstractComponent.createComponent(
                ThermostatedHeater.class.getCanonicalName(),
                // the first actual parameter tells the component to create
                // a SIL simulation architecture for integration test and the
                // second 'true' that it must be executed as a unit test.
                new Object[]{HEM_SIL_Supervisor.SIM_ARCHITECTURE_URI, true});
        */
        AbstractComponent.createComponent(
                ElectricMeter.class.getCanonicalName(),
                // the first actual parameter tells the component to create
                // a SIL simulation architecture for integration test and the
                // second 'false' that it must *not* be executed as a unit test.
                new Object[]{HEM_SIL_Supervisor.SIM_ARCHITECTURE_URI, false});

        AbstractComponent.createComponent(
                HEM.class.getCanonicalName(),
                new Object[]{false});

        AbstractComponent.createComponent(
                SIL_Coordinator.class.getCanonicalName(),
                new Object[]{});

        AbstractComponent.createComponent(
                HEM_SIL_Supervisor.class.getCanonicalName(),
                new Object[]{});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVM_SIL cvm = new CVM_SIL();
            // Given some margin to the component application execution compared to the simulation duration
            long d = (long) (SIMULATION_DURATION*1000.0/ACC_FACTOR);
            cvm.startStandardLifeCycle(d + 5000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
