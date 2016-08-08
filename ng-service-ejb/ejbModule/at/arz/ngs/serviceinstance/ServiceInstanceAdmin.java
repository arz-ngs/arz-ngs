package at.arz.ngs.serviceinstance;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.HostRepository;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;

@Stateless
public class ServiceInstanceAdmin {

	@Inject
	private ServiceRepository services;

	@Inject
	private HostRepository hosts;

	@Inject
	private EnvironmentRepository environments;

	@Inject
	private ServiceInstanceRepository instances;

	public void execute(CreateNewServiceInstance command) {
		command.getEnvironmentName();
	}
	
}
