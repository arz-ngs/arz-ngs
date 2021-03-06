package at.arz.ngs.resources;

import java.net.URI;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.job.JobScheduler;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverviewList;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;
import at.arz.ngs.serviceinstance.commands.update.UpdateStatus;

@Dependent
@Path("/instances")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ServiceInstanceResource {

	@Inject
	private ServiceInstanceAdmin instanceAdmin;

	@Inject
	private JobScheduler jobScheduler;

	@Context
	private HttpServletRequest request;

	/**
	 * Need Administrator rights.
	 * 
	 * @param command
	 * @return
	 */
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response createNewServiceInstance(CreateNewServiceInstance command) {
		instanceAdmin.createNewServiceInstance(command);

		String path = command.getServiceName() + "/" + command.getEnvironmentName() + "/" + command.getHostName() + "/"
				+ command.getInstanceName();

		URI location = URI.create("/instances/" + path);
		return Response.ok().status(Status.CREATED).location(location).build();
	}

	/**
	 * Must be exactly specified with the parameters of the old data. The new
	 * data relies in the JSON/XML object.
	 * 
	 * Need Administrator rights.
	 * 
	 * @param serviceName
	 *            The old Param.
	 * @param environmentName
	 *            The old Param.
	 * @param hostName
	 *            The old Param.
	 * @param instanceName
	 *            The old Param.
	 * @param command
	 *            Object with the new data.
	 * @return Returns a HTTP status if the operation was successul or not.
	 */
	@POST
	@Path("{service}/{environment}/{host}/{name}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateServiceInstance(@PathParam("service") String serviceName,
			@PathParam("environment") String environmentName, @PathParam("host") String hostName,
			@PathParam("name") String instanceName, UpdateServiceInstance command) {
		instanceAdmin.updateServiceInstance(command, serviceName, environmentName, hostName, instanceName);

		String path = command.getServiceName() + "/" + command.getEnvironmentName() + "/" + command.getHostName() + "/"
				+ command.getInstanceName();

		URI location = URI.create("/instances/" + path);
		return Response.ok().status(Status.OK).location(location).build();
	}

	/**
	 * The params must be set for exactly specifying which instance should be
	 * deleted.
	 * 
	 * Need Administrator rights.
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @param command
	 *            Additional information for deletion. Must be set, especially
	 *            the version tag.
	 */
	@DELETE
	@Path("{service}/{environment}/{host}/{name}")
	public void deleteServiceInstance(@PathParam("service") String serviceName,
			@PathParam("environment") String environmentName, @PathParam("host") String hostName,
			@PathParam("name") String instanceName) {

		instanceAdmin.removeServiceInstance(serviceName, environmentName, hostName, instanceName);
	}

	/**
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @return Exact one ServiceInstance which matches all params.
	 */
	@GET
	@Path("{service}/{environment}/{host}/{name}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ServiceInstanceResponse getServiceInstance(@PathParam("service") String serviceName,
			@PathParam("environment") String environmentName, @PathParam("host") String hostName,
			@PathParam("name") String instanceName) {
		ServiceInstanceResponse serviceInstance = instanceAdmin.getServiceInstance(serviceName, environmentName,
				hostName, instanceName);
		return serviceInstance;
	}

	/**
	 * The query-params are SQL-regex conform entries.
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @return All ServiceInstances which match the query-param-regex.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ServiceInstanceOverviewList getServiceInstances(@QueryParam("serviceName") String serviceName,
			@QueryParam("environmentName") String environmentName, @QueryParam("hostName") String hostName,
			@QueryParam("instanceName") String instanceName) {

		if (serviceName == null || serviceName.equals("")) {
			serviceName = "*";
		}
		if (environmentName == null || environmentName.equals("")) {
			environmentName = "*";
		}
		if (hostName == null || hostName.equals("")) {
			hostName = "*";
		}
		if (instanceName == null || instanceName.equals("")) {
			instanceName = "*";
		}
		return instanceAdmin.getServiceInstances(serviceName, environmentName, hostName, instanceName);
	}

	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })

	/**
	 * Unique specifying the targeted serviceInstance. Then perform the chosen
	 * action on it.
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @param performAction
	 *            Action to be performed. Available actions: start, stop,
	 *            restart
	 * @return
	 */
	@POST
	@Path("{service}/{environment}/{host}/{name}/action")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void performAction(@PathParam("service") String serviceName,
			@PathParam("environment") String environmentName, @PathParam("host") String hostName,
			@PathParam("name") String instanceName, PerformAction performAction) {
		Action action = Action.valueOf(performAction.getPerformAction());

		JobId jobId = jobScheduler.scheduleAction(action, new ServiceName(serviceName),
				new EnvironmentName(environmentName),
				new ServiceInstanceLocation(new HostName(hostName), new ServiceInstanceName(instanceName)));

		jobScheduler.startJob(jobId);
	}

	/**
	 * Unique specifying the targeted serviceInstance. Then update the status of
	 * the selected instance.
	 * 
	 * Need Administrator rights.
	 * 
	 * @param serviceName
	 * @param environmentName
	 * @param hostName
	 * @param instanceName
	 * @param command
	 *            {@link UpdateStatus}
	 */
	@POST
	@Path("{service}/{environment}/{host}/{name}/status")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void updateStatus(@PathParam("service") String serviceName, @PathParam("environment") String environmentName,
			@PathParam("host") String hostName, @PathParam("name") String instanceName, UpdateStatus command) {

		instanceAdmin.updateServiceInstanceStatus(serviceName, environmentName, hostName, instanceName, command);
	}

}
