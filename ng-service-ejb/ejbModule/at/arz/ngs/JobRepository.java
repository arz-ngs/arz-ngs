package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.JobId;

public interface JobRepository {
	void addJob(Job job);

	Job getJob(JobId jobId);

	void removeJob(Job job);

	List<Job> getAllJobs();
}
