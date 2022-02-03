package eco_logis.equipments.electric_meter;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

// -----------------------------------------------------------------------------
/**
 * This class performs the test on the Electric meter class
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CVMUnitTest
    extends	AbstractCVM
{

    // ========== Constructors ==========


    public CVMUnitTest() throws Exception { }


    // ========== Class methods ==========

    /** @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy() */
    @Override
    public void	deploy() throws Exception {
        // Create the components to test the electric meter
        AbstractComponent.createComponent(
                ElectricMeter.class.getCanonicalName(),
                new Object[]{ElectricMeterRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI, true}
        );

        super.deploy();
    }

    public static void	main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(30000L);
            Thread.sleep(5000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}