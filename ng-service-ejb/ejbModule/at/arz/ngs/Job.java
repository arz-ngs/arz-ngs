package at.arz.ngs;

import java.util.Collections;
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
import javax.persistence.NamedQueries;
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
@NamedQueries({@NamedQuery(name = Job.QUERY_ALL, query = "SELECT j FROM Job j"),
		@NamedQuery(name = Job.QUERY_BY_JOBID, query = "SELECT j FROM Job j WHERE j.jobId=:id")})
public class Job {

	public static final String QUERY_ALL = "Job.getAll";
	public static final String QUERY_BY_JOBID = "Job.findByUniqueKey";

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

	public Job(JobId jobId, User creator, Action action, List<ServiceInstance> instances) {
		this.jobId = jobId;
		this.creator = creator;
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

	public boolean hasNextInstance() {
		return instances.size() > 0;
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

	public JobId getJobId() {
		return jobId;
	}

	public List<ServiceInstance> getInstances() {
		return Collections.unmodifiableList(instances);
	}

	public User getCreator() {
		return creator;
	}

	public Date getTslastmodified() {
		return new Date(tslastmodified.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Job other = (Job) obj;
		if (jobId == null) {
			if (other.jobId != null) {
				return false;
			}
		}
		else if (!jobId.equals(other.jobId)) {
			return false;
		}
		return true;
	}
}
