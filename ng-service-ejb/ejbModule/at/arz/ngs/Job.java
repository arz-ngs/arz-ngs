package at.arz.ngs;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.converter.jpa.JobIdConverter;
import at.arz.ngs.security.User;

@Entity
@NamedQuery(name = "Job.findByJobID", query = "SELECT j FROM Job j WHERE j.jobId=:id")
public class Job {

	@Id
	@GeneratedValue(generator = "ngs.job", strategy = GenerationType.TABLE)
	private long oid;

	@Convert(converter = JobIdConverter.class)
	@Column(name = "JOBID", nullable = false)
	private JobId jobId;

	@Column(name = "ACTION")
	@Enumerated(EnumType.STRING)
	private Action action;

	@OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
	private List<ServiceInstance> instances;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATOR_OID")
	private User creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_LAST_MODIFIED")
	private Date tslastmodified;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_CREATED")
	private Date tscreated;

	protected Job() {
		//JPA
	}

	public Job(JobId jobId, Action action, List<ServiceInstance> instances) {
		this.jobId = jobId;
		this.action = action;
		for (ServiceInstance instance : instances) {
			instance.setJob(this);
		}
		this.instances = instances;
		this.tscreated = new Date();
	}

	public boolean isCompleted() {
		return instances.isEmpty();
	}

	public ServiceInstance nextInstance() {
		this.tslastmodified = new Date();
		return instances.get(0);
	}

	public void actionPerformed(ServiceInstanceLocation loc) {
		this.tslastmodified = new Date();
		ServiceInstance instance = findServiceInstance(loc);
		instances.remove(instance);
		instance.setJob(null);
	}

	private ServiceInstance findServiceInstance(ServiceInstanceLocation loc) {
		for (ServiceInstance instance : instances) {
			if (loc.equals(
					new ServiceInstanceLocation(instance.getHost().getHostName(), instance.getServiceInstanceName()))) {
				return instance;
			}
		}
		throw new IllegalStateException("No instance found!");
	}

	public Action getAction() {
		return action;
	}

	public Date getTscreated() {
		return new Date(tscreated.getTime());
	}

	public Date getTslastmodiefied() {
		return new Date(tslastmodified.getTime());
	}

	public JobId getJobId() {
		return jobId;
	}
}
