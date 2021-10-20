package equipments.hem;

import equipments.crypto_miner.CryptoMiner;
import equipments.oven.Oven;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMIntegrationTest
    extends AbstractCVM
{

    // ========== Constructors ==========


    public CVMIntegrationTest() throws Exception {}


    // ========== Lifecycle ==========


    /** @see AbstractCVM#deploy() */
    @Override
    public void deploy() throws Exception {
        // Create the crypto miner
        AbstractComponent.createComponent(
                CryptoMiner.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the oven
        AbstractComponent.createComponent(
                Oven.class.getCanonicalName(),
                new Object[]{}
        );

        // Create the HEM
        AbstractComponent.createComponent(
                HEM.class.getCanonicalName(),
                new Object[]{}
        );

        // Super deploy
        super.deploy();
    }


    // ========== Class methods ==========


    public static void main(String[] args) {
        try {
            CVMIntegrationTest cvm = new CVMIntegrationTest();
            cvm.startStandardLifeCycle(1000L);
            Thread.sleep(100000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
