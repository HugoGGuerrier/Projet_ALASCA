package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.StorageEquipmentControlCI;

/**
 * This class represents an outbound port for a storage equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class StorageEquipmentOutboundPort
    extends ProductionEquipmentOutboundPort
    implements StorageEquipmentControlCI
{

    // ========== Constructors ==========


    /**
     * Create a new storage equipment outbound port with the wanted owner
     *
     * @see ProductionEquipmentOutboundPort#ProductionEquipmentOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public StorageEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(StorageEquipmentControlCI.class, owner);
    }

    /**
     * Create a new storage equipment outbound port with the URI and the owner
     *
     * @see ProductionEquipmentOutboundPort#ProductionEquipmentOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public StorageEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, StorageEquipmentControlCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see StorageEquipmentControlCI#isConsuming() */
    @Override
    public boolean isConsuming() throws Exception {
        return ((StorageEquipmentControlCI) getConnector()).isConsuming();
    }

    /** @see StorageEquipmentControlCI#startConsuming() */
    @Override
    public boolean startConsuming() throws Exception {
        assert !isConsuming() : new PreconditionException("startCharging() -> !isConsuming()");
        boolean res = ((StorageEquipmentControlCI) getConnector()).startConsuming();
        assert isConsuming() : new PostconditionException("startCharging() -> isConsuming()");
        return res;
    }

    /** @see StorageEquipmentControlCI#stopConsuming() */
    @Override
    public boolean stopConsuming() throws Exception {
        assert isConsuming() : new PreconditionException("stopCharging() -> isConsuming()");
        boolean res = ((StorageEquipmentControlCI) getConnector()).stopConsuming();
        assert !isConsuming() : new PostconditionException("stopCharging() -> !isConsuming()");
        return res;
    }

    /** @see StorageEquipmentOutboundPort#getPowerLevel() */
    @Override
    public double getPowerLevel() throws Exception {
        return ((StorageEquipmentControlCI) getConnector()).getPowerLevel();
    }

}
