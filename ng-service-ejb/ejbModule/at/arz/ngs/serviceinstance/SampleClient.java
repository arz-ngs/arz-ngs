package at.arz.ngs.serviceinstance;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.arz.ngs.api.EnvironmentName;

public class SampleClient
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin instanceAdmin;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String environmentString = req.getParameter("environment");
		final EnvironmentName environmentName = new EnvironmentName(environmentString);

		instanceAdmin.execute(new CreateNewServiceInstance(environmentName, null, null, null));
	}

}
