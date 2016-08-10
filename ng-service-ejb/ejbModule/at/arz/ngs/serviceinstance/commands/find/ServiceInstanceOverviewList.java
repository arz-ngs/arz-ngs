package at.arz.ngs.serviceinstance.commands.find;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ServiceInstacne-overview")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInstanceOverviewList {
	
	@XmlElementWrapper(name = "service-instances")
	@XmlElement(name = "service-instance")
	private List<ServiceInstanceOverview> serviceInstances;

	public List<ServiceInstanceOverview> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(List<ServiceInstanceOverview> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}
}
