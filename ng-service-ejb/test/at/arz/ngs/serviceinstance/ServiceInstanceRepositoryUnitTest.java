package at.arz.ngs.serviceinstance;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import at.arz.ngs.Environment;
import at.arz.ngs.Host;
import at.arz.ngs.Script;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;


public class ServiceInstanceRepositoryUnitTest {

	@Test
	public void add() {
		repository.addServiceInstance(host1, service1, environment1, script1, serviceInstanceName1, status1);
		repository.addServiceInstance(host2, service2, environment2, script2, serviceInstanceName2, status2);
	}

	@Test
	public void remove() {
		repository.removeServiceInstance(serviceInstance1);
	}

	@Test
	public void find() {
		repository.getServiceInstance(serviceInstanceName2, service2, host2, environment2);
	}

	private ServiceInstanceRepository repository;
	private EntityManager em;

	private ServiceInstanceName serviceInstanceName1;

	private ServiceName serviceName1;
	private Service service1;

	private HostName hostName1;
	private Host host1;

	private EnvironmentName environmentName1;
	private Environment environment1;

	private ScriptName scriptName1;
	private PathStart pathStart1;
	private PathStop pathStop1;
	private PathRestart pathRestart1;
	private PathStatus pathStatus1;
	private Script script1;

	private Status status1;

	private ServiceInstance serviceInstance1;

	private ServiceInstanceName serviceInstanceName2;

	private ServiceName serviceName2;
	private Service service2;

	private HostName hostName2;
	private Host host2;

	private EnvironmentName environmentName2;
	private Environment environment2;

	private ScriptName scriptName2;
	private PathStart pathStart2;
	private PathStop pathStop2;
	private PathRestart pathRestart2;
	private PathStatus pathStatus2;
	private Script script2;

	private Status status2;

	private ServiceInstance serviceInstance2;

	private ServiceInstanceName serviceInstanceName3;

	private ServiceName serviceName3;
	private Service service3;

	private HostName hostName3;
	private Host host3;

	private EnvironmentName environmentName3;
	private Environment environment3;

	private ScriptName scriptName3;
	private PathStart pathStart3;
	private PathStop pathStop3;
	private PathRestart pathRestart3;
	private PathStatus pathStatus3;
	private Script script3;

	private Status status3;

	private ServiceInstance serviceInstance3;

	@BeforeClass
	public void setUpBeforeClass() {
		repository = new JPAServiceInstanceRepository(em);

		serviceInstanceName1 = new ServiceInstanceName("serviceInstanceName1");

		serviceName1 = new ServiceName("serviceName1");
		service1 = new Service(serviceName1);

		hostName1 = new HostName("hostName1");
		host1 = new Host(hostName1);

		environmentName1 = new EnvironmentName("environmentName1");
		environment1 = new Environment(environmentName1);

		scriptName1 = new ScriptName("sricptName1");
		pathStart1 = new PathStart("pathStart1");
		pathStop1 = new PathStop("pathStop1");
		pathRestart1 = new PathRestart("pathRestart1");
		pathStatus1 = new PathStatus("pathStatus1");
		script1 = new Script(scriptName1, pathStart1, pathStop1, pathRestart1, pathStatus1);

		status1 = Status.active;

		serviceInstance1 = new ServiceInstance(serviceInstanceName1, service1, host1, environment1, script1, status1);



		serviceInstanceName2 = new ServiceInstanceName("serviceInstanceName2");

		serviceName2 = new ServiceName("serviceName2");
		service2 = new Service(serviceName2);

		hostName2 = new HostName("hostName2");
		host2 = new Host(hostName2);

		environmentName2 = new EnvironmentName("environmentName2");
		environment2 = new Environment(environmentName2);

		scriptName2 = new ScriptName("sricptName2");
		pathStart2 = new PathStart("pathStart2");
		pathStop2 = new PathStop("pathStop2");
		pathRestart2 = new PathRestart("pathRestart2");
		pathStatus2 = new PathStatus("pathStatus2");
		script2 = new Script(scriptName2, pathStart2, pathStop2, pathRestart2, pathStatus2);

		status2 = Status.not_active;

		serviceInstance2 = new ServiceInstance(serviceInstanceName2, service2, host2, environment2, script2, status2);


		serviceInstanceName3 = new ServiceInstanceName("serviceInstanceName2");

		serviceName3 = new ServiceName("serviceName2");
		service3 = new Service(serviceName2);

		hostName3 = new HostName("hostName2");
		host3 = new Host(hostName2);

		environmentName3 = new EnvironmentName("environmentName2");
		environment3 = new Environment(environmentName2);

		scriptName3 = new ScriptName("sricptName2");
		pathStart3 = new PathStart("pathStart2");
		pathStop3 = new PathStop("pathStop2");
		pathRestart3 = new PathRestart("pathRestart2");
		pathStatus3 = new PathStatus("pathStatus2");
		script3 = new Script(scriptName2, pathStart2, pathStop2, pathRestart2, pathStatus2);

		status3 = Status.active;

		serviceInstance3 = new ServiceInstance(serviceInstanceName3, service3, host3, environment3, script3, status3);

	}

}
