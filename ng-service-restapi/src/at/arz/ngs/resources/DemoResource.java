package at.arz.ngs.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

	@GET
	public String sayHallo(@QueryParam("service") String var, @QueryParam("env") String env) {
		if (var == null) {
			var = "*";
		}
		if (env == null) {
			env = "*";
		}
		return queryServiceInstances(var, env);
	}

	@GET
	@Path("{serviceName}/{environmentName}")
	public String sayX(@PathParam("serviceName") String var, @PathParam("environmentName") String env) {
		return queryServiceInstances(var, env);
	}

	private String queryServiceInstances(String var, String env) {
		return "alle instanzen für Service " + var + " und env " + env;
	}

}
