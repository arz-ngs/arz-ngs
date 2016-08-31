package at.arz.ngs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import at.arz.ngs.resources.ServiceInstanceResource;
import at.arz.ngs.resources.exceptionMapper.AlreadyModifiedMapper;
import at.arz.ngs.resources.exceptionMapper.ActionInProgressMapper;
import at.arz.ngs.resources.exceptionMapper.EmptyFieldMapper;
import at.arz.ngs.resources.exceptionMapper.EnvironmentNotFoundMapper;
import at.arz.ngs.resources.exceptionMapper.ExecuteActionMapper;
import at.arz.ngs.resources.exceptionMapper.HostNotFoundMapper;
import at.arz.ngs.resources.exceptionMapper.JPAExceptionMapper;
import at.arz.ngs.resources.exceptionMapper.NoPermissionMapper;
import at.arz.ngs.resources.exceptionMapper.RuntimeMapper;
import at.arz.ngs.resources.exceptionMapper.ServiceInstanceAlreadyExistMapper;
import at.arz.ngs.resources.exceptionMapper.ServiceInstanceNotFoundMapper;
import at.arz.ngs.resources.exceptionMapper.UnknownActionMapper;
import at.arz.ngs.resources.exceptionMapper.UnknownMapper;
import at.arz.ngs.resources.exceptionMapper.WrongParamMapper;

@ApplicationPath("/v1")
public class NgsRestAPIV1
		extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		// Resources
		classes.add(ServiceInstanceResource.class);

		// Exception Mapper
		classes.add(AlreadyModifiedMapper.class);
		classes.add(ActionInProgressMapper.class);
		classes.add(EmptyFieldMapper.class);
		classes.add(EnvironmentNotFoundMapper.class);
		classes.add(HostNotFoundMapper.class);
		classes.add(JPAExceptionMapper.class);
		classes.add(NoPermissionMapper.class);
		classes.add(RuntimeMapper.class);
		classes.add(ServiceInstanceAlreadyExistMapper.class);
		classes.add(ServiceInstanceNotFoundMapper.class);
		classes.add(WrongParamMapper.class);
		classes.add(UnknownActionMapper.class);
		classes.add(UnknownMapper.class);
		classes.add(ExecuteActionMapper.class);
		return classes;
	}
}
