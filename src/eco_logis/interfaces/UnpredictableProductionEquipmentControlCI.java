package eco_logis.interfaces;

public interface UnpredictableProductionEquipmentControlCI
    extends ProductionEquipmentControlCI
{


    /**
     *  Return if the energy production is forbidden
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return true the equipment production is forbidden, false otherwise
     * @throws Exception TODO
     */
    boolean isForbidden() throws Exception;


    /**
     *  Forbid the energy production of the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code !isForbidden()}
     * post	{@code isForbidden()}
     * </pre>
     *
     * @return If the equipment production has been forbidden
     * @throws Exception TODO
     */
    boolean forbidProduction() throws Exception;

    /**
     *  Allow the energy production of the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code isForbidden()}
     * post	{@code !isForbidden()}
     * </pre>
     *
     * @return If the equipment production has been allowed
     * @throws Exception TODO
     */
    boolean allowProduction() throws Exception;

}
