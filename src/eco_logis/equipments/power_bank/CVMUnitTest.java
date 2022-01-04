package eco_logis.equipments.power_bank;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMUnitTest
    extends AbstractCVM {

    // ========== Constructors ==========

    public CVMUnitTest() throws Exception {}

    // ========== Class methods ==========

    @Override
    public void deploy() throws Exception {
        // Create the components to test the power bank
        AbstractComponent.createComponent(
                PowerBank.class.getCanonicalName(),
                new Object[]{PowerBankRTAtomicSimulatorPlugin.UNIT_TEST_SIM_ARCHITECTURE_URI, true}
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
