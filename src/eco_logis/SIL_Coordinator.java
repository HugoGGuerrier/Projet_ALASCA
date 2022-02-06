package eco_logis;

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * The class <code>SIL_Coordinator</code> defines the component used in the HEM
 * example to execute the simulation coordinator.
 *
 * <p><strong>Description</strong></p>
 * <p>
 * In SIL simulations, global simulation architectures created across components
 * use coordinator components to hold and execute coupled models. Little needs
 * to be defined in coordinator components as their coupled model will be
 * created automatically by the supervisor component from the simulation
 * architecture and it will be used through the coordinator plug-in provided
 * by BCM4Java-CyPhy and also created and installed on the component
 * automatically.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 * <pre>
 * invariant	true
 * </pre>
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class SIL_Coordinator
    extends AbstractCyPhyComponent
{

    // ========== Macros ==========


    public static final String	REFLECTION_INBOUND_PORT_URI = "coordination-ribpuri";

    /** When true, methods trace their actions */
    public static final boolean	VERBOSE = true;


    // ========== Constructors ==========


    protected SIL_Coordinator() {
        super(REFLECTION_INBOUND_PORT_URI, 1, 0);
        if (VERBOSE) {
            this.tracer.get().setTitle("Coordinator component");
            this.tracer.get().setRelativePosition(1, 4);
            this.toggleTracing();
        }
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#start() */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        this.traceMessage("Coordinator starts.\n");
    }

    /** @see fr.sorbonne_u.components.AbstractComponent#shutdown() */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        this.traceMessage("Coordinator stops.\n");
        super.shutdown();
    }

}
