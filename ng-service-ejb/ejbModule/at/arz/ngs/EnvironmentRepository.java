package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.EnvironmentName;

public interface EnvironmentRepository {

	Environment getEnvironment(EnvironmentName environmentName);

	List<Environment> getAllEnvironments();

	void addEnvironment(EnvironmentName environmentName);

	void removeEnvironment(Environment environment);
}
