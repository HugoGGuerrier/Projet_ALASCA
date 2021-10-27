package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.StorageEquipmentCI;

/**
 * This class represents an outbound port for a storage equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class StorageEquipmentOutboundPort
    extends ProductionEquipmentOutboundPort
    implements StorageEquipmentCI
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
        super(StorageEquipmentCI.class, owner);
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
        super(uri, StorageEquipmentCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see StorageEquipmentCI#isConsuming() */
    @Override
    public boolean isConsuming() throws Exception {
        return ((StorageEquipmentCI) getConnector()).isConsuming();
    }

    /** @see StorageEquipmentCI#startConsuming() */
    @Override
    public boolean startConsuming() throws Exception {
        assert !isConsuming() : new PreconditionException("startCharging() -> !isConsuming()");
        boolean res = ((StorageEquipmentCI) getConnector()).startConsuming();
        assert isConsuming() : new PostconditionException("startCharging() -> isConsuming()");
        return res;
    }

    /** @see StorageEquipmentCI#stopConsuming() */
    @Override
    public boolean stopConsuming() throws Exception {
        assert isConsuming() : new PreconditionException("stopCharging() -> isConsuming()");
        boolean res = ((StorageEquipmentCI) getConnector()).stopConsuming();
        assert !isConsuming() : new PostconditionException("stopCharging() -> !isConsuming()");
        return res;
    }

    /** @see StorageEquipmentCI#getConsumption() */
    @Override
    public double getConsumption() throws Exception {
        assert isConsuming() : new PreconditionException("getConsumption() -> isConsuming()");
        return ((StorageEquipmentCI) getConnector()).getConsumption();
    }

    /** @see StorageEquipmentOutboundPort#getPowerLevel() */
    @Override
    public float getPowerLevel() throws Exception {
        return ((StorageEquipmentCI) getConnector()).getPowerLevel();
    }

}
