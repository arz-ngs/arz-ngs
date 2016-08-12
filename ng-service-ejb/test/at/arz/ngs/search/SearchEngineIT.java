package at.arz.ngs.search;

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
import at.arz.ngs.search.OrderCondition;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;

public class SearchEngineIT
		extends AbstractJpaIT {

	@Test
	public void testFindServiceInstances() {
		assertEquals(3, searchEngine.findServiceInstances("*", "*", "*", "*").size());
		assertEquals(0, searchEngine.findServiceInstances("", "", "*", "*").size());
		assertEquals(0, searchEngine.findServiceInstances("*", "", "", "").size());
		assertEquals(3, searchEngine.findServiceInstances("__serviceName%", "*", "*", "*").size());
		assertEquals(0, searchEngine.findServiceInstances("serviceName", "*", "*", "*").size());
		assertEquals(0, searchEngine.findServiceInstances("*", "*", "hostName*", "*").size());
		assertEquals(3, searchEngine.findServiceInstances("*", "*", "*hostName*", "*").size());
	}

	@Test
	public void testOrderByGeneral() {
		print("Host ASC", searchEngine.orderByHostNameTest());
		
	}

	/**
	 * Visual control in console in this testing stage.
	 */
	@Test
	public void orderTest() {
		List<ServiceInstance> inst1 = repository.getAllInstances();
		System.err.println(inst1.size());
		for (ServiceInstance i : inst1) {
			System.err.println(i.toString());
		}

		List<ServiceInstance> inst = searchEngine.findServiceInstances(	"*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING));

		print("Service DSC", inst);
		List<ServiceInstance> inst2 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_ENVIRONMENT,
																							OrderCondition.DESCENDING));
		print("Env DSC", inst2);
		List<ServiceInstance> inst3 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_HOST,
																							OrderCondition.DESCENDING));
		print("Host DSC", inst3);
		List<ServiceInstance> inst4 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICEINSTANCE,
																							OrderCondition.DESCENDING));

		print("Instance DSC", inst4);
		List<ServiceInstance> inst5 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.ASCENDING));
		print("Service ASC", inst5);
		List<ServiceInstance> inst6 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_ENVIRONMENT,
																							OrderCondition.ASCENDING));
		print("Env ASC", inst6);
		List<ServiceInstance> inst7 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_HOST,
																							OrderCondition.ASCENDING));
		print("Host ASC", inst7);
		List<ServiceInstance> inst8 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*3",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICEINSTANCE,
																							OrderCondition.ASCENDING));
		print("Instance ASC only 1", inst8);

		List<ServiceInstance> inst9 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(null, null));
		print("with null values -> Instance ASC", inst9);
	}

	/**
	 * Visual control in console.
	 */
	@Test
	public void testPagination() {
		List<ServiceInstance> all = searchEngine.findServiceInstances(	"*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING));

		print("ALL -- Service DSC", all);

		List<ServiceInstance> inst1 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING),
																		new PaginationCondition(1, 1));

		print("Service DSC", 1, 1, inst1);

		List<ServiceInstance> inst3 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING),
																		new PaginationCondition(1, 2));

		print("Service DSC", 1, 2, inst3);

		List<ServiceInstance> inst2 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING),
																		new PaginationCondition(2, 1));

		print("Service DSC", 2, 1, inst2);

		List<ServiceInstance> inst25 = searchEngine.findServiceInstances(	"*",
																			"*",
																			"*",
																			"*",
																			new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																								OrderCondition.DESCENDING),
																			new PaginationCondition(2, 2));

		print("Service DSC", 2, 2, inst25);

		List<ServiceInstance> inst4 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING),
																		new PaginationCondition(3, 1));

		print("Service DSC", 3, 1, inst4);

		List<ServiceInstance> inst5 = searchEngine.findServiceInstances("*",
																		"*",
																		"*",
																		"*",
																		new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																							OrderCondition.DESCENDING),
																		new PaginationCondition(-1, 0));

		print("Service DSC", 50, 1, inst5);

		List<ServiceInstance> inst55 = searchEngine.findServiceInstances(	"*",
																			"*",
																			"*",
																			"*",
																			new OrderCondition(	OrderCondition.ORDERBY_SERVICE,
																								OrderCondition.DESCENDING),
																			new PaginationCondition(7, 8));

		print("Service DSC", 7, 8, inst55);
	}

	private void print(String comm, List<ServiceInstance> inst) {
		System.err.println("\n\n" + comm + ": " + inst.size());
		for (ServiceInstance i : inst) {
			System.err.println(i.toString());
		}
	}

	private void print(String comm, int elementsPerPage, int currentPage, List<ServiceInstance> inst) {
		System.err.println("\n\n"+ comm
							+ "\nElements per Page: "
							+ elementsPerPage
							+ "\nCurrentPage: "
							+ currentPage
							+ "\nSize: "
							+ inst.size());
		for (ServiceInstance i : inst) {
			System.err.println(i.toString());
		}
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

	private Status status3;

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

	private Status status2;

	@Before
	public void setUpBefore() {
		repository = new JPAServiceInstanceRepository(getEntityManager());
		serviceRepository = new JPAServiceRepository(getEntityManager());
		hostRepository = new JPAHostRepository(getEntityManager());
		environmentRepository = new JPAEnvironmentRepository(getEntityManager());
		scriptRepository = new JPAScriptRepository(getEntityManager());
		searchEngine = new SearchEngine(getEntityManager());

		serviceInstanceName1 = new ServiceInstanceName("C serviceInstanceName1");

		serviceName1 = new ServiceName("A serviceName1");

		hostName1 = new HostName("Z hostName1");

		environmentName1 = new EnvironmentName("B environmentName1");
		environment1 = new Environment(environmentName1);

		scriptName1 = new ScriptName("sricptName1");
		pathStart1 = new PathStart("pathStart1");
		pathStop1 = new PathStop("pathStop1");
		pathRestart1 = new PathRestart("pathRestart1");
		pathStatus1 = new PathStatus("pathStatus1");

		status1 = Status.active;

		serviceInstanceName2 = new ServiceInstanceName("A serviceInstanceName2");

		serviceName2 = new ServiceName("B serviceName2");

		hostName2 = new HostName("Y hostName2");

		environmentName2 = new EnvironmentName("C environmentName2");

		scriptName2 = new ScriptName("sricptName2");
		pathStart2 = new PathStart("pathStart2");
		pathStop2 = new PathStop("pathStop2");
		pathRestart2 = new PathRestart("pathRestart2");
		pathStatus2 = new PathStatus("pathStatus2");

		status2 = Status.not_active;

		serviceInstanceName3 = new ServiceInstanceName("B serviceInstanceName3");

		serviceName3 = new ServiceName("C serviceName3");

		hostName3 = new HostName("X hostName3");

		environmentName3 = new EnvironmentName("A environmentName3");

		scriptName3 = new ScriptName("sricptName3");
		pathStart3 = new PathStart("pathStart3");
		pathStop3 = new PathStop("pathStop3");
		pathRestart3 = new PathRestart("pathRestart3");
		pathStatus3 = new PathStatus("pathStatus3");

		status3 = Status.is_starting;

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

		hostRepository.addHost(hostName3);
		serviceRepository.addService(serviceName3);
		environmentRepository.addEnvironment(environmentName3);
		scriptRepository.addScript(scriptName3, pathStart3, pathStop3, pathRestart3, pathStatus3);

		host3 = hostRepository.getHost(hostName3);
		service3 = serviceRepository.getService(serviceName3);
		environment3 = environmentRepository.getEnvironment(environmentName3);
		script3 = scriptRepository.getScript(scriptName3);

		repository.addServiceInstance(host1, service1, environment1, script1, serviceInstanceName1, status1);
		repository.addServiceInstance(host2, service2, environment2, script2, serviceInstanceName2, status2);
		repository.addServiceInstance(host3, service3, environment3, script3, serviceInstanceName3, status3);
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
