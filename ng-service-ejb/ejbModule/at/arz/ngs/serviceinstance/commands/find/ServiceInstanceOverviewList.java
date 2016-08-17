package at.arz.ngs.serviceinstance.commands.find;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceInstance-overview")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInstanceOverviewList {
	
	@XmlElementWrapper(name = "service-instances")
	@XmlElement(name = "service-instances")
	private List<ServiceInstanceOverview> serviceInstances;

	/**
	 * Contains the number of elements which are available in the current regex selection.
	 * Differs to the size of the {@link ServiceInstanceOverviewList.serviceInstances} only if pagination is enabled.
	 * If pagination is enabled the value is NOT the count of the current selection of the page, but the size of the
	 * regex selection, thus the size of the regex selection is greater.
	 */
	private int numElementsFound;

	public List<ServiceInstanceOverview> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(List<ServiceInstanceOverview> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}

	public int getNumElementsFound() {
		return numElementsFound;
	}

	public void setNumElementsFound(int numElementsFound) {
		this.numElementsFound = numElementsFound;
	}
}
