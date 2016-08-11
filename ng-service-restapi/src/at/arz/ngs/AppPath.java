package at.arz.ngs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import at.arz.ngs.resources.ServiceInstanceResource;
import at.arz.ngs.resources.exceptionWrapper.AlreadyModifiedWrapper;
import at.arz.ngs.resources.exceptionWrapper.AlreadyPerformWrapper;
import at.arz.ngs.resources.exceptionWrapper.EmptyFieldWrapper;
import at.arz.ngs.resources.exceptionWrapper.EnvironmentNotFoundWrapper;
import at.arz.ngs.resources.exceptionWrapper.HostNotFoundWrapper;
import at.arz.ngs.resources.exceptionWrapper.JPAExceptionWrapper;
import at.arz.ngs.resources.exceptionWrapper.NoPermissionWrapper;
import at.arz.ngs.resources.exceptionWrapper.RuntimeWrapper;
import at.arz.ngs.resources.exceptionWrapper.ServiceInstanceAlreadyExistWrapper;
import at.arz.ngs.resources.exceptionWrapper.ServiceInstanceNotFoundWrapper;
import at.arz.ngs.resources.exceptionWrapper.WrongParamWrapper;

@ApplicationPath("/api")
public class AppPath
		extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		// Resources
		classes.add(ServiceInstanceResource.class);

		// Exception Mapper
		classes.add(AlreadyModifiedWrapper.class);
		classes.add(AlreadyPerformWrapper.class);
		classes.add(EmptyFieldWrapper.class);
		classes.add(EnvironmentNotFoundWrapper.class);
		classes.add(HostNotFoundWrapper.class);
		classes.add(JPAExceptionWrapper.class);
		classes.add(NoPermissionWrapper.class);
		classes.add(RuntimeWrapper.class);
		classes.add(ServiceInstanceAlreadyExistWrapper.class);
		classes.add(ServiceInstanceNotFoundWrapper.class);
		classes.add(WrongParamWrapper.class);
		return classes;
	}
}
