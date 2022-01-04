package eco_logis.equipments.oven;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * This class starts the unit tests on the oven component
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class CVMUnitTest
    extends AbstractCVM
{

    // ========== Constructors ==========


    public CVMUnitTest() throws Exception {}


    // ========== Class methods ==========


    @Override
    public void deploy() throws Exception {
        // Create the components to test the oven

        //AbstractComponent.createComponent(Oven.class.getCanonicalName(), new Object[]{});
        //AbstractComponent.createComponent(OvenUnitTester.class.getCanonicalName(), new Object[]{});

        AbstractComponent.createComponent(
                Oven.class.getCanonicalName(),
                new Object[]{OvenRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI,
                    true});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(15000L);
            Thread.sleep(5000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
