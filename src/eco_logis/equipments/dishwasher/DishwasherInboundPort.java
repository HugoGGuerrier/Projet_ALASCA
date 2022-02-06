package eco_logis.equipments.dishwasher;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This class represent an inbound port for the dishwasher services
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DishwasherInboundPort
    extends AbstractInboundPort
    implements DishwasherCI
{

    // ========== Constructors ==========


    /**
     * Create a new dishwasher inbound port with the wanted owner
     *
     * @see AbstractInboundPort#AbstractInboundPort(Class, ComponentI)
     *
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherInboundPort(ComponentI owner) throws Exception {
        super(DishwasherCI.class, owner);
    }

    /**
     * Create a new dishwasher inbound port with the wanted URI and owner component
     *
     * @see AbstractInboundPort#AbstractInboundPort(String, Class, ComponentI)
     *
     * @param uri The port URI
     * @param owner The port owner
     * @throws Exception TODO
     */
    public DishwasherInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, DishwasherCI.class, owner);
    }


    // ========== Override methods ==========


    /** @see DishwasherCI#getProgram() */
    @Override
    public DishwasherProgram getProgram() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).getProgram()
        );
    }

    /** @see DishwasherCI#getProgramDuration() */
    @Override
    public Duration getProgramDuration() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).getProgramDuration()
        );
    }

    /** @see DishwasherCI#getDeadline() */
    @Override
    public LocalTime getDeadline() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).getDeadline()
        );
    }

    /** @see DishwasherCI#getStartTime() */
    @Override
    public LocalTime getStartTime() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).getStartTime()
        );
    }

    /** @see DishwasherCI#isPlanned() */
    @Override
    public boolean isPlanned() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).isPlanned()
        );
    }

    /** @see DishwasherCI#plan(LocalTime) */
    @Override
    public boolean plan(LocalTime deadline) throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).plan(deadline)
        );
    }

    /** @see DishwasherCI#plan(LocalTime, DishwasherProgram) */
    @Override
    public boolean plan(LocalTime deadline, DishwasherProgram program) throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).plan(deadline, program)
        );
    }

    /** @see DishwasherCI#cancel() */
    @Override
    public boolean cancel() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).cancel()
        );
    }

    /** @see DishwasherCI#postPone(Duration) */
    @Override
    public boolean postPone(Duration duration) throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).postPone(duration)
        );
    }

    /** @see DishwasherCI#isWashing() */
    @Override
    public boolean isWashing() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).isWashing()
        );
    }

    /** @see DishwasherCI#startWashing() () */
    @Override
    public boolean startWashing() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).startWashing()
        );
    }

    /** @see DishwasherCI#stopWashing() */
    @Override
    public boolean stopWashing() throws Exception {
        return getOwner().handleRequest(
                o -> ((Dishwasher) o).stopWashing()
        );
    }

}
