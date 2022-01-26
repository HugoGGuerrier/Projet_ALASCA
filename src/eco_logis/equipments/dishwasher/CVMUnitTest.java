package eco_logis.equipments.dishwasher;

import eco_logis.equipments.generator.Generator;
import eco_logis.equipments.generator.GeneratorRTAtomicSimulatorPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * This class starts the unit tests on the dishwasher component
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
        // Create the components to test the dishwasher
        AbstractComponent.createComponent(
                Dishwasher.class.getCanonicalName(),
                new Object[]{DishwasherRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI, true}
        );

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
