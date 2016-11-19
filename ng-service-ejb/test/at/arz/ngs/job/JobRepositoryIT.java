package at.arz.ngs.job;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.Job;
import at.arz.ngs.JobRepository;
import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.UserName;
import at.arz.ngs.environment.jpa.JPAEnvironmentRepository;
import at.arz.ngs.host.jpa.JPAHostRepository;
import at.arz.ngs.job.jpa.JPAJobRepository;
import at.arz.ngs.script.jpa.JPAScriptRepository;
import at.arz.ngs.security.RoleRepository;
import at.arz.ngs.security.User;
import at.arz.ngs.security.UserRepository;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;
import at.arz.ngs.security.user.jpa.JPAUserRepository;
import at.arz.ngs.security.userrole.jpa.JPAUser_RoleRepository;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;

public class JobRepositoryIT extends AbstractJpaIT{

	private JobRepository repository;
	private UserRepository userRepository;
	private ServiceInstanceRepository instanceRepository;
	private ServiceRepository serviceRepository;
	private HostRepository hostRepository;
	private EnvironmentRepository environmentRepository;
	private ScriptRepository scriptRepository;
	
	@Before
	public void setUpBefore() {
		repository = new JPAJobRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		
		instanceRepository = new JPAServiceInstanceRepository(getEntityManager());
		serviceRepository = new JPAServiceRepository(getEntityManager());
		hostRepository = new JPAHostRepository(getEntityManager());
		environmentRepository = new JPAEnvironmentRepository(getEntityManager());
		scriptRepository = new JPAScriptRepository(getEntityManager());
	}
	
	@Test
	public void addJob() {
		userRepository.addUser(new UserName("testUser"), new FirstName("d"), new LastName("t"), new Email("test@email.at"));
		User user = userRepository.getUser(new UserName("testUser"));
		
		hostRepository.addHost(new HostName("hostName"));
		serviceRepository.addService(new ServiceName("serviceName"));
		environmentRepository.addEnvironment(new EnvironmentName("environmentName"));
		environmentRepository.addEnvironment(new EnvironmentName("environmentName2"));
		scriptRepository.addScript(new ScriptName("script"), new PathStart("start"), new PathStop("stop"), new PathRestart("restart"), new PathStatus("status"));
		Host host = hostRepository.getHost(new HostName("hostName")); 
		Service service = serviceRepository.getService(new ServiceName("serviceName"));
		Environment environment = environmentRepository.getEnvironment(new EnvironmentName("environmentName"));
		Environment environment2 = environmentRepository.getEnvironment(new EnvironmentName("environmentName2"));
		Script script = scriptRepository.getScript(new ScriptName("script"));
		
		instanceRepository.addServiceInstance(host, service, environment, script, new ServiceInstanceName("serviceName"), Status.not_active, "Test Information 1");
		instanceRepository.addServiceInstance(host, service, environment2, script, new ServiceInstanceName("serviceName"), Status.active, "Test Information 2");
		
		List<ServiceInstance> instances = new LinkedList<ServiceInstance>();
		ServiceInstance instance1 = instanceRepository.getServiceInstance(new ServiceInstanceName("serviceName"), service, host, environment);
		ServiceInstance instance2 = instanceRepository.getServiceInstance(new ServiceInstanceName("serviceName"), service, host, environment2);
		instances.add(instance1);
		instances.add(instance2);
		
		assertEquals(0, repository.getAllJobs().size());
		repository.addJob(new Job(new JobId("job1"), user, Action.start, instances));
		List<Job> jobs = repository.getAllJobs();
		assertEquals(1, jobs.size());
		Job job = repository.getJob(new JobId("job1"));
		assertEquals(2, job.getInstances().size());
		assertEquals("testUser", job.getCreator().getUserName().getName());
		assertEquals(2, instanceRepository.getAllInstances().size());
//		repository.addJob(new Job(new JobId("job2"), user, Action.stop, instances));
//		assertEquals(2, repository.getAllJobs().size());
//		repository.removeJob(job);
//		assertEquals(2, instanceRepository.getAllInstances().size());
//		Job job2 = repository.getJob(new JobId("job2"));
//		assertEquals("job2", job2.getJobId().getValue());
//		repository.removeJob(job2);
//		assertEquals(0, repository.getAllJobs().size());
	}
	
	@Test
	public void removeJob(){
		userRepository.addUser(new UserName("testUser"), new FirstName("d"), new LastName("t"), new Email("test@email.at"));
		User user = userRepository.getUser(new UserName("testUser"));
		
		hostRepository.addHost(new HostName("hostName"));
		serviceRepository.addService(new ServiceName("serviceName"));
		environmentRepository.addEnvironment(new EnvironmentName("environmentName"));
		environmentRepository.addEnvironment(new EnvironmentName("environmentName2"));
		scriptRepository.addScript(new ScriptName("script"), new PathStart("start"), new PathStop("stop"), new PathRestart("restart"), new PathStatus("status"));
		Host host = hostRepository.getHost(new HostName("hostName")); 
		Service service = serviceRepository.getService(new ServiceName("serviceName"));
		Environment environment = environmentRepository.getEnvironment(new EnvironmentName("environmentName"));
		Environment environment2 = environmentRepository.getEnvironment(new EnvironmentName("environmentName2"));
		Script script = scriptRepository.getScript(new ScriptName("script"));
		
		instanceRepository.addServiceInstance(host, service, environment, script, new ServiceInstanceName("serviceName"), Status.not_active, "Test Information 1");
		instanceRepository.addServiceInstance(host, service, environment2, script, new ServiceInstanceName("serviceName"), Status.active, "Test Information 2");
		
		List<ServiceInstance> instances = new LinkedList<ServiceInstance>();
		ServiceInstance instance1 = instanceRepository.getServiceInstance(new ServiceInstanceName("serviceName"), service, host, environment);
		ServiceInstance instance2 = instanceRepository.getServiceInstance(new ServiceInstanceName("serviceName"), service, host, environment2);
		instances.add(instance1);
		instances.add(instance2);
		
		
		repository.addJob(new Job(new JobId("job1"), user, Action.start, instances));
		repository.addJob(new Job(new JobId("job2"), user, Action.stop, instances));
		assertEquals(2, repository.getAllJobs().size());
		Job job = repository.getJob(new JobId("job1"));
		repository.removeJob(job);
		assertEquals(1, repository.getAllJobs().size());
		Job job2 = repository.getJob(new JobId("job2"));
		repository.removeJob(job2);
		assertEquals(0, repository.getAllJobs().size());
	}
	
//	@After
//	public void cleanup() {
//		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
//		d1.executeUpdate();
//		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
//		d2.executeUpdate();
//		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
//		d3.executeUpdate();
//		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
//		d4.executeUpdate();
//		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
//		d5.executeUpdate();
//		Query d7 = super.getEntityManager().createNativeQuery("DROP TABLE USER_ROLE");
//		d7.executeUpdate();
//		Query d8 = super.getEntityManager().createNativeQuery("DROP TABLE USER_");
//		d8.executeUpdate();
//		Query d10 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
//		d10.executeUpdate();
//		Query d9 = super.getEntityManager().createNativeQuery("DROP TABLE ROLE");
//		d9.executeUpdate();
//		Query d6 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION");
//		d6.executeUpdate();
//		Query d11 = super.getEntityManager().createNativeQuery("DROP TABLE JOURNALENTRY");
//		d11.executeUpdate();
//
//	}

}
