package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.JobId;

@ApplicationException(rollback = true)
public class JobNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private JobId jobId;
	
	public JobNotFound(JobId jobId) {
		super(jobId.getValue());
		this.jobId = jobId;
	}
	
	public JobId getJobId() {
		return this.jobId;
	}
}
