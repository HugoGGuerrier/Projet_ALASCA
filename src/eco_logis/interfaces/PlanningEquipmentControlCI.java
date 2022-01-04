package eco_logis.interfaces;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an
// example of a cyber-physical system.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability.
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or
// data to be ensured and,  more generally, to use and operate it in the
// same conditions as regards security.
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.time.Duration;
import java.time.LocalTime;

/**
 * The component interface <code>PlanningEquipmentControlCI</code> defines the
 * operations that a controller can perform on an equipment which can have
 * planned programs.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	{@code !hasPlan() || (deadline().compareTo(startTime().plus(duration())}
 * </pre>
 *
 * <p>Created on : 2021-09-09</p>
 *
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface PlanningEquipmentControlCI
    extends StandardEquipmentControlCI
{

    /**
     * Get if the equipment has a plan ; if the equipment currently has a planned program
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true
     * post	true
     * </pre>
     *
     * @return If the equipment has a plan
     * @throws Exception TODO
     */
    boolean hasPlan() throws Exception;

    /**
     * Get the planned start time of the equipment
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The start time
     * @throws Exception TODO
     */
    LocalTime startTime() throws Exception;

    /**
     * Get the program duration if the equipment has a plan
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The program duration
     * @throws Exception TODO
     */
    Duration duration() throws Exception;

    /**
     * Get the equipment deadline if it has a plan
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	true
     * </pre>
     *
     * @return The deadline
     * @throws Exception TODO
     */
    LocalTime deadline() throws Exception;

    /**
     * Postpone the planed program with the wanted duration and return if the plan has been changed
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * pre  {@code startTime() + duration() + duration < deadline()}
     * post	{@code startTime() = startTime() + duration}
     * </pre>
     *
     * @param duration The duration to postpone
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean postPone(Duration duration) throws Exception;

    /**
     * Cancel the planned program and return if the plan has been modified
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code hasPlan()}
     * post	{@code !hasPlan()}
     * </pre>
     *
     * @return If the operation succeeded
     * @throws Exception TODO
     */
    boolean cancel() throws Exception;

}
