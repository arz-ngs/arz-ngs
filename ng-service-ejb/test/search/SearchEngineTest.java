package search;

import static org.junit.Assert.assertEquals;

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
import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
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
import at.arz.ngs.environment.jpa.JPAEnvironmentRepository;
import at.arz.ngs.host.jpa.JPAHostRepository;
import at.arz.ngs.script.jpa.JPAScriptRepository;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;

public class SearchEngineTest
		extends AbstractJpaIT {

	@Test
	public void testFindServiceInstances() {
		assertEquals(2, searchEngine.findServiceInstances("*", "*", "*", "*").size());
	}

	@Test
	public void orderByNameTest() {
		List<ServiceInstance> inst1 = repository.getAllInstances();
		System.err.println(inst1.size());
		for (ServiceInstance i : inst1) {
			System.err.println(i.toString());
		}
		List<ServiceInstance> inst = searchEngine.orderByHostNameTest();
		System.err.println(inst.size());
		for (ServiceInstance i : inst) {
			System.err.println(i.toString());
		}
		assertEquals(2, searchEngine.orderByHostNameTest().size());
	}

	private ServiceInstanceRepository repository;
	private ServiceRepository serviceRepository;
	private HostRepository hostRepository;
	private EnvironmentRepository environmentRepository;
	private ScriptRepository scriptRepository;
	private SearchEngine searchEngine;

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

	@Before
	public void setUpBefore() {
		repository = new JPAServiceInstanceRepository(getEntityManager());
		serviceRepository = new JPAServiceRepository(getEntityManager());
		hostRepository = new JPAHostRepository(getEntityManager());
		environmentRepository = new JPAEnvironmentRepository(getEntityManager());
		scriptRepository = new JPAScriptRepository(getEntityManager());
		searchEngine = new SearchEngine(getEntityManager());

		serviceInstanceName1 = new ServiceInstanceName("serviceInstanceName1");

		serviceName1 = new ServiceName("serviceName1");

		hostName1 = new HostName("hostName1");

		environmentName1 = new EnvironmentName("environmentName1");
		environment1 = new Environment(environmentName1);

		scriptName1 = new ScriptName("sricptName1");
		pathStart1 = new PathStart("pathStart1");
		pathStop1 = new PathStop("pathStop1");
		pathRestart1 = new PathRestart("pathRestart1");
		pathStatus1 = new PathStatus("pathStatus1");

		status1 = Status.active;

		serviceInstanceName2 = new ServiceInstanceName("serviceInstanceName2");

		serviceName2 = new ServiceName("serviceName2");

		hostName2 = new HostName("hostName2");

		environmentName2 = new EnvironmentName("environmentName2");

		scriptName2 = new ScriptName("sricptName2");
		pathStart2 = new PathStart("pathStart2");
		pathStop2 = new PathStop("pathStop2");
		pathRestart2 = new PathRestart("pathRestart2");
		pathStatus2 = new PathStatus("pathStatus2");

		status2 = Status.not_active;

		// put in data
		hostRepository.addHost(hostName1);
		serviceRepository.addService(serviceName1);
		environmentRepository.addEnvironment(environmentName1);
		scriptRepository.addScript(scriptName1, pathStart1, pathStop1, pathRestart1, pathStatus1);

		host1 = hostRepository.getHost(hostName1);
		service1 = serviceRepository.getService(serviceName1);
		environment1 = environmentRepository.getEnvironment(environmentName1);
		script1 = scriptRepository.getScript(scriptName1);

		hostRepository.addHost(hostName2);
		serviceRepository.addService(serviceName2);
		environmentRepository.addEnvironment(environmentName2);
		scriptRepository.addScript(scriptName2, pathStart2, pathStop2, pathRestart2, pathStatus2);

		host2 = hostRepository.getHost(hostName2);
		service2 = serviceRepository.getService(serviceName2);
		environment2 = environmentRepository.getEnvironment(environmentName2);
		script2 = scriptRepository.getScript(scriptName2);

		repository.addServiceInstance(host1, service1, environment1, script1, serviceInstanceName1, status1);
		repository.addServiceInstance(host2, service2, environment2, script2, serviceInstanceName2, status2);
	}

	/**
	 * cleanup table entries
	 */
	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
	}
}
