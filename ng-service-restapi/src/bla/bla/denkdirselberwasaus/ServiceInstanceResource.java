package bla.bla.denkdirselberwasaus;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.serviceinstance.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;

@Path("/instances")
public class ServiceInstanceResource {

	@Inject
	private ServiceInstanceAdmin instanceAdmin;

	@PUT
	public Response execute(CreateNewServiceInstance command) {
		// ok, aber muss dann als ergebnis die location liefern
		instanceAdmin.execute(command);
		
		URI location = URI.create("/instances/..."); // der komplette pfad (relativ
		return Response.ok().status(Status.CREATED).location(location).build();
	}

	@PUT
	@Path("{service}/{environment}/{host}/{name}")
	public void setServiceScript(	@PathParam("service") ServiceName serviceName,
	                          @PathParam("environment") EnvironmentName environmentName,
	                          @PathParam("host") HostName hostName,
								@PathParam("name") ServiceInstanceName instanceName,
									SetServiceScript command
	                          ) {
		
		execute(command);
	}
	
}
