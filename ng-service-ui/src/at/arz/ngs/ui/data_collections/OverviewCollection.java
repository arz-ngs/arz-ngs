package at.arz.ngs.ui.data_collections;

import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;

/**
 * Have to wrapped by a list to be displayed in UI. Contains mapping between chechboxes and ServiceInstance-values.
 * 
 * @author alex 
 *
 */
public class OverviewCollection {

	private boolean checked;

	private ServiceInstanceOverview serviceInstance;

	public OverviewCollection() {
	}

	public OverviewCollection(ServiceInstanceOverview serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public ServiceInstanceOverview getServiceInstance() {
		return serviceInstance;
	}

}
