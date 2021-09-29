package fr.sorbonne_u.components.cyphy.hem2021e1.equipments.hem;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a basic
// household management systems as an example of a cyber-physical system.
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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.hem2021.interfaces.SuspensionEquipmentControlCI;
import fr.sorbonne_u.components.cyphy.hem2021.interfaces.StandardEquipmentControlCI;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.heater.ThermostatedHeater;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterConnector;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterOutboundPort;
import fr.sorbonne_u.components.cyphy.hem2021e1.equipments.meter.ElectricMeterCI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

// -----------------------------------------------------------------------------
/**
 * The class <code>HEM</code> implements the basis for a household energy
 * management component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * As is, this component is only a very limited starting point for the actual
 * component. The given code is there only to ease the understanding of the
 * objectives, but most of it must be replaced to get the correct code.
 * Especially, no registration of the components representing the appliances
 * is given.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2021-09-09</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@RequiredInterfaces(required =	{StandardEquipmentControlCI.class,
								 SuspensionEquipmentControlCI.class,
								 ElectricMeterCI.class})
public class			HEM
extends		AbstractComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	protected ElectricMeterOutboundPort					meterop;
	protected SuspensionEquipmentControlOutboundPort	heaterop;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected 			HEM()
	{
		super(1, 0);

		this.tracer.get().setTitle("Home Energy Manager component");
		this.tracer.get().setRelativePosition(0, 0);
		this.toggleTracing();		
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.meterop = new ElectricMeterOutboundPort(this);
			this.meterop.publishPort();
			this.doPortConnection(
					this.meterop.getPortURI(),
					ElectricMeter.ELECTRIC_METER_INBOUND_PORT_URI,
					ElectricMeterConnector.class.getCanonicalName());

			this.heaterop = new SuspensionEquipmentControlOutboundPort(this);
			this.heaterop.publishPort();
			this.doPortConnection(
					this.heaterop.getPortURI(),
					ThermostatedHeater.INBOUND_PORT_URI,
					HeaterConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		// simplified integration testing.
		this.traceMessage("Electric meter current consumption? " +
				this.meterop.getCurrentConsumption() + "\n");
		this.traceMessage("Electric meter current production? " +
				this.meterop.getCurrentProduction() + "\n");

		this.traceMessage("Heater is on? " + this.heaterop.on() + "\n");
		this.traceMessage("Heater max mode index is? " +
				this.heaterop.maxMode() + "\n");
		this.traceMessage("Heater is switched on? " +
				this.heaterop.switchOn() + "\n");
		this.traceMessage("Heater current mode is? " +
				this.heaterop.currentMode() + "\n");
		this.traceMessage("Heater is suspended? " +
				this.heaterop.suspended() + "\n");
		this.traceMessage("Heater suspends? " +
				this.heaterop.suspend() + "\n");
		this.traceMessage("Heater emergency? " +
				this.heaterop.emergency() + "\n");
		this.traceMessage("Heater resumes? " +
				this.heaterop.resume() + "\n");
		this.traceMessage("Heater is suspended? " +
				this.heaterop.suspended() + "\n");
		this.traceMessage("Heater is switched off? " +
				this.heaterop.switchOff() + "\n");
		this.traceMessage("Heater is on? " + this.heaterop.on() + "\n");
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.meterop.getPortURI());
		this.doPortDisconnection(this.heaterop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.meterop.unpublishPort();
			this.heaterop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
}
// -----------------------------------------------------------------------------
