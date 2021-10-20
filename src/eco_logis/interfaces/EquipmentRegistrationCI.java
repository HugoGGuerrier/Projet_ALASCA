package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * This interface represents a service for the equipment registration
 *
 * @author Emile SIAU
 * @author Hugo GUERRIER
 */
public interface EquipmentRegistrationCI
    extends RequiredCI, OfferedCI
{

    /**
     * Register an equipment to be controlled by the Home Energy Manager
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code id != null && !id.isEmpty()}
     * pre	{@code inboundPortUri != null && !inboundPortUri.isEmpty()}
     * pre	{@code xmlControllerPath != null && !xmlControllerPath.isEmpty()}
     * post	true
     * </pre>
     *
     * @param id ID of the equipment
     * @param inboundPortUri URI of the control inbound port of the equipment
     * @param xmlControllerPath	Path to the XML controller
     * @return If the registration succeeded
     * @throws Exception TODO
     */
    boolean register(
            String id,
            String inboundPortUri,
            String xmlControllerPath
    ) throws Exception;

}
