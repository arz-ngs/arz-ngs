package at.arz.ngs.job.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Job;
import at.arz.ngs.JobRepository;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.exception.JobNotFound;

@Dependent
public class JPAJobRepository
implements JobRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAJobRepository() {
		//ejb constructor
	}

	public JPAJobRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void addJob(Job job) {
		entityManager.persist(job);
	}

	@Override
	public Job getJob(JobId jobId) {
		try {
			TypedQuery<Job> getJob = entityManager.createNamedQuery(Job.QUERY_BY_JOBID, Job.class);
			getJob.setParameter("id", jobId);
			return getJob.getSingleResult();
		} catch(NoResultException e){
			throw new JobNotFound(jobId);
		}
	}

	@Override
	public void removeJob(Job job) {
		List<ServiceInstance> instances = job.getInstances();
		for(ServiceInstance si : instances) {
			si.clearJob();
		}
		entityManager.remove(job);
	}

	@Override
	public List<Job> getAllJobs() {
		TypedQuery<Job> getJobs = entityManager.createNamedQuery(Job.QUERY_ALL, Job.class);
		return getJobs.getResultList();
	}

}
