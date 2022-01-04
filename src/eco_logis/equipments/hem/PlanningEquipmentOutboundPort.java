package eco_logis.equipments.hem;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import eco_logis.interfaces.PlanningEquipmentControlCI;

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
    implements PlanningEquipmentControlCI
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
        super(PlanningEquipmentControlCI.class, owner);
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
        super(uri, PlanningEquipmentControlCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see PlanningEquipmentControlCI#hasPlan() */
    @Override
    public boolean hasPlan() throws Exception {
        return ((PlanningEquipmentControlCI) getConnector()).hasPlan();
    }

    /** @see PlanningEquipmentControlCI#startTime() */
    @Override
    public LocalTime startTime() throws Exception {
        assert hasPlan() : new PreconditionException("startTime() -> hasPlan()");
        return ((PlanningEquipmentControlCI) getConnector()).startTime();
    }

    /** @see PlanningEquipmentControlCI#duration() */
    @Override
    public Duration duration() throws Exception {
        assert hasPlan() : new PreconditionException("duration() -> hasPlan()");
        return ((PlanningEquipmentControlCI) getConnector()).duration();
    }

    /** @see PlanningEquipmentControlCI#deadline() */
    @Override
    public LocalTime deadline() throws Exception {
        assert hasPlan() : new PreconditionException("deadline() -> hasPlan()");
        return ((PlanningEquipmentControlCI) getConnector()).deadline();
    }

    /** @see PlanningEquipmentControlCI#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        assert hasPlan() : new PreconditionException("postPone(duration) -> hasPlan()");
        LocalTime nextStartTime = startTime().plusSeconds(duration.getSeconds());
        assert deadline().isAfter(nextStartTime.plusSeconds(duration().getSeconds()))
                : new PreconditionException("postPone(duration) -> startTime() + duration + duration() < deadline()");
        boolean res = ((PlanningEquipmentControlCI) getConnector()).postPone(duration);
        assert startTime().equals(nextStartTime)
                : new PostconditionException("postPone(duration) -> startTime() = startTime() + duration");
        return res;
    }

    /** @see PlanningEquipmentControlCI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        assert hasPlan() : new PreconditionException("cancel() -> hasPlan()");
        boolean res = ((PlanningEquipmentControlCI) getConnector()).cancel();
        assert !hasPlan() : new PostconditionException("cancel() -> !hasPlan()");
        return res;
    }

}
