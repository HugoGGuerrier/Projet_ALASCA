package eco_logis.equipments.generator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * This class tests the generator unitary
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
        // Create the components to test the generator
        AbstractComponent.createComponent(
                Generator.class.getCanonicalName(),
                new Object[]{GeneratorRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI, true}
        );

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(15000L);
            Thread.sleep(1000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
