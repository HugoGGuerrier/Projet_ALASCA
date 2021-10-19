package equipments.wind_turbine;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMUnitTest
    extends AbstractCVM {

    // ========== Constructors ==========

    public CVMUnitTest() throws Exception {}

    // ========== Class methods ==========

    @Override
    public void deploy() throws Exception {
        // Create the components to test the wind turbine
        AbstractComponent.createComponent(WindTurbine.class.getCanonicalName(), new Object[]{});
        AbstractComponent.createComponent(WindTurbineUnitTester.class.getCanonicalName(), new Object[]{});

        super.deploy();
    }

    public static void main(String[] args) {
        try {
            equipments.wind_turbine.CVMUnitTest cvm = new equipments.wind_turbine.CVMUnitTest();
            cvm.startStandardLifeCycle(1000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}