package eco_logis.equipments.electric_meter;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

// -----------------------------------------------------------------------------
/**
 * The class <code>CVMUnitTest</code> performs unit tests on the electric
 * meter component.
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
public class CVMUnitTest
    extends	AbstractCVM
{

    // ========== Constructors ==========


    public CVMUnitTest() throws Exception { }


    // ========== Class methods ==========

    /**
     * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
     */
    @Override
    public void	deploy() throws Exception {
        // Create the components to test the electric meter
        AbstractComponent.createComponent(ElectricMeter.class.getCanonicalName(), new Object[]{});
        AbstractComponent.createComponent(ElectricMeterUnitTester.class.getCanonicalName(), new Object[]{});

        super.deploy();
    }

    public static void	main(String[] args) {
        try {
            fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.CVMUnitTest cvm = new fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.CVMUnitTest();
            cvm.startStandardLifeCycle(15000L);
            Thread.sleep(15000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}