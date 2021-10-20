package equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.PlanningEquipmentCI;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This class represents an outbound port for a planning equipment
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class PlanningEquipmentOutboundPort
    extends StandardEquipmentOutboundPort
    implements PlanningEquipmentCI
{

    // ========== Constructors ==========

    /**
     * Create a new planning equipment outbound port with the wanted owner
     *
     * @see StandardEquipmentOutboundPort#StandardEquipmentOutboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public PlanningEquipmentOutboundPort(ComponentI owner) throws Exception {
        super(PlanningEquipmentCI.class, owner);
    }

    /**
     * Create a new planning equipment outbound port with the wanted URI and owner
     *
     * @see StandardEquipmentOutboundPort#StandardEquipmentOutboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public PlanningEquipmentOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PlanningEquipmentCI.class, owner);
    }

    // ========== Override methods ==========

    /** @see PlanningEquipmentCI#hasPlan() */
    @Override
    public boolean hasPlan() throws Exception {
        return ((PlanningEquipmentCI) getConnector()).hasPlan();
    }

    /** @see PlanningEquipmentCI#startTime() */
    @Override
    public LocalTime startTime() throws Exception {
        assert hasPlan() : new PreconditionException("startTime() -> hasPlan()");
        return ((PlanningEquipmentCI) getConnector()).startTime();
    }

    /** @see PlanningEquipmentCI#duration() */
    @Override
    public Duration duration() throws Exception {
        assert hasPlan() : new PreconditionException("duration() -> hasPlan()");
        return ((PlanningEquipmentCI) getConnector()).duration();
    }

    /** @see PlanningEquipmentCI#deadline() */
    @Override
    public LocalTime deadline() throws Exception {
        assert hasPlan() : new PreconditionException("deadline() -> hasPlan()");
        return ((PlanningEquipmentCI) getConnector()).deadline();
    }

    /** @see PlanningEquipmentCI#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        assert hasPlan() : new PreconditionException("postPone(duration) -> hasPlan()");
        LocalTime nextStartTime = startTime().plusSeconds(duration.getSeconds());
        assert deadline().isAfter(nextStartTime.plusSeconds(duration().getSeconds()))
                : new PreconditionException("postPone(duration) -> startTime() + duration + duration() < deadline()");
        boolean res = ((PlanningEquipmentCI) getConnector()).postPone(duration);
        assert startTime().equals(nextStartTime)
                : new PostconditionException("postPone(duration) -> startTime() = startTime() + duration");
        return res;
    }

    /** @see PlanningEquipmentCI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        assert hasPlan() : new PreconditionException("cancel() -> hasPlan()");
        boolean res = ((PlanningEquipmentCI) getConnector()).cancel();
        assert !hasPlan() : new PostconditionException("cancel() -> !hasPlan()");
        return res;
    }

}
