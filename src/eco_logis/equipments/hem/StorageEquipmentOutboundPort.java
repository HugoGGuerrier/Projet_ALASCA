package equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.StorageEquipmentCI;

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


    /** @see StorageEquipmentCI#startDischarging() */
    @Override
    public boolean startDischarging() throws Exception {
        assert !isProducing() : new PreconditionException("startDischarging() -> !isProducing()");
        boolean res = ((StorageEquipmentCI) getConnector()).startDischarging();
        assert isProducing() : new PostconditionException("startDischarging() -> isProducing()");
        return res;
    }

    /** @see StorageEquipmentCI#stopDischarging() */
    @Override
    public boolean stopDischarging() throws Exception {
        assert isProducing() : new PreconditionException("stopDischarging() -> isProducing()");
        boolean res = ((StorageEquipmentCI) getConnector()).stopDischarging();
        assert !isProducing() : new PostconditionException("stopDischarging() -> !isProducing()");
        return res;
    }

    /** @see StorageEquipmentCI#isConsuming() */
    @Override
    public boolean isConsuming() throws Exception {
        return ((StorageEquipmentCI) getConnector()).isConsuming();
    }

    /** @see StorageEquipmentCI#startCharging() */
    @Override
    public boolean startCharging() throws Exception {
        assert !isConsuming() : new PreconditionException("startCharging() -> !isConsuming()");
        boolean res = ((StorageEquipmentCI) getConnector()).startCharging();
        assert isConsuming() : new PostconditionException("startCharging() -> isConsuming()");
        return res;
    }

    /** @see StorageEquipmentCI#stopCharging() */
    @Override
    public boolean stopCharging() throws Exception {
        assert isConsuming() : new PreconditionException("stopCharging() -> isConsuming()");
        boolean res = ((StorageEquipmentCI) getConnector()).stopCharging();
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
