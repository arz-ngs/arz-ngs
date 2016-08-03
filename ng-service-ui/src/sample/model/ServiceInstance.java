package sample.model;


/**
 * Konfiguration einer Service-Instanz auf einem bestimment Host und einem bestimmten Environment;
 * 
 * @author rpri333
 *
 */

public class ServiceInstance {

	// host, enviroment & name sind unique
	private Host host;
	private Environment environment;
	private String name;

	private String status;

	private String startScript;
	private String stopScript;
	private String restartScript;
	private String statusScript;
	private String adminRole;
	private String xxxRole;

}
