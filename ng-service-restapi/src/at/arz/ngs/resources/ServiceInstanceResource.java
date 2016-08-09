package at.arz.ngs.resources;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;

@Path("/instances")
public class ServiceInstanceResource {

	@Inject
	private ServiceInstanceAdmin instanceAdmin;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response execute(CreateNewServiceInstance command) {
		// ok, aber muss dann als ergebnis die location liefern
		try {
			// instanceAdmin.execute(command);

			String path = command.getServiceName()+ "/"
							+ command.getEnvironmentName()
							+ "/"
							+ command.getHostName()
							+ "/"
							+ command.getInstanceName();

			URI location = URI.create("/instances/" + path);
			return Response.ok().status(Status.CREATED).location(location).build();
		} catch (RuntimeException e) { // TODO later on if more than one exception can be thrown, more Status can be
										// sent
			URI location = URI.create("/instances/");
			return Response.notModified().status(Status.NO_CONTENT).location(location).build();
		}
		
	}

	// @PUT
	// @Path("{service}/{environment}/{host}/{name}")
	// public void setServiceScript( @PathParam("service") ServiceName serviceName,
	// @PathParam("environment") EnvironmentName environmentName,
	// @PathParam("host") HostName hostName,
	// @PathParam("name") ServiceInstanceName instanceName,
	// SetServiceScript command
	// ) {
	//
	// execute(command);
	// }

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public void getServiceInstance(	@PathParam("service") ServiceName serviceName,
									@PathParam("environment") EnvironmentName environmentName,
									@PathParam("host") HostName hostName,
									@PathParam("name") ServiceInstanceName instanceName) {
	}

}
