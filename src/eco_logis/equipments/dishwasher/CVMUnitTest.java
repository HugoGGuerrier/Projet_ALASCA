package eco_logis.equipments.dishwasher;

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
        AbstractComponent.createComponent(Dishwasher.class.getCanonicalName(), new Object[]{});
        AbstractComponent.createComponent(DishwasherUnitTester.class.getCanonicalName(), new Object[]{});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(1000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
