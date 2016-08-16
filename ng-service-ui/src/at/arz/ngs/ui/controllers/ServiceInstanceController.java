package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.search.OrderCondition;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.ui.data_collections.PaginationCollection;

@ViewScoped
@Named("serviceinstance")
public class ServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin service;

	private List<ServiceInstanceOverview> instances;

	private PaginationCondition pagination;
	private OrderCondition order;
	private PaginationCollection paginationCollection;

	private String serviceRegex;
	private String envIdRegex;
	private String hostRegex;
	private String instanceRegex;

	@PostConstruct
	public void init() {
		pagination = new PaginationCondition(50, 1); // default is first page with 50 elements
		order = new OrderCondition(OrderCondition.ORDERBY_SERVICEINSTANCE, OrderCondition.ASCENDING);
		instances = service.getServiceInstances("*", "*", "*", "*", order, pagination).getServiceInstances();
		paginationCollection = new PaginationCollection();
		doPaginationValidation();
	}

	public List<ServiceInstanceOverview> getInstances() {
		return instances;
	}

	public void performPagination(String newPage) {
		try {
			int page = new Integer(newPage);
			pagination.setCurrentPage(page);
		}
		catch (Exception e) {
			if (newPage.equals("_lt")) {
				pagination.setCurrentPage(pagination.getCurrentPage() - 1);
			}
			else if (newPage.equals("_gt")) {
				pagination.setCurrentPage(pagination.getCurrentPage() + 1);
			}
			else {
				throw e;
			}
		}
		System.out.println("current page: " + pagination.getCurrentPage());
		instances = service.getServiceInstances(cumputeServiceRegex(), cumputeEnvRegex(), cumputeHostRegex(), cumputeInstanceRegex(), order, pagination).getServiceInstances();

		doPaginationValidation();
	}

	/**
	 * Computes the fields in the pagination (ui)
	 */
	private void doPaginationValidation() {
		int overallElementCount = 10; // get the real value from admin (with regex params, without pagination, without
										// order, only number of count)
		int currentPage = pagination.getCurrentPage();
		int elemPerPage = pagination.getElementsPerPage();
		int lastPage = 0;
		if (overallElementCount % elemPerPage == 0) {
			lastPage = overallElementCount / elemPerPage;
		}
		else {
			lastPage = (overallElementCount / elemPerPage) + 1;
		}
		System.out.println("last page: " + lastPage);

		paginationCollection.setLeftCaretClass(currentPage == 1 ? PaginationCollection.DISABLED : null);
		paginationCollection.setRightCaretClass(currentPage == lastPage ? PaginationCollection.DISABLED : null);

		int numPages = lastPage;

		if (numPages == 1) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(false);
		}
		else if (numPages == 2) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(true);
		}
		else if (numPages == 3) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);
		}
		else if (numPages == 4) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);
		}
		else if (numPages >= 5) {
			paginationCollection.setShowSecondElem(true);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);
			// don't hide anything

			if (currentPage == 2) {
				paginationCollection.setSecondElement("2");
				paginationCollection.setThirdElement("3");
				paginationCollection.setFourthElement("4");
			}
			else if (currentPage == lastPage - 1) {
				paginationCollection.setFourthElement((lastPage - 1) + "");
				paginationCollection.setThirdElement((lastPage - 2) + "");
				paginationCollection.setSecondElement((lastPage - 3) + "");
			}
			else { //if the current page is somewhere in the middle
				paginationCollection.setSecondElement((currentPage - 1) + "");
				paginationCollection.setThirdElement(currentPage + "");
				paginationCollection.setFourthElement((currentPage + 1) + "");
			}
		}

		paginationCollection.setFithElement(lastPage + "");

		//now highlight current page to be active

		if (paginationCollection.getFirstElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getFithElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(PaginationCollection.ACTIVE);
		}
		else if (paginationCollection.getSecondElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getThirdElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getFourthElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setFifthElementClass(null);
		}
	}

	private String cumputeServiceRegex() {
		if (serviceRegex == null) {
			return "*";
		}
		return serviceRegex + "*";
	}

	private String cumputeEnvRegex() {
		if (envIdRegex == null) {
			return "*";
		}
		return envIdRegex + "*";
	}

	private String cumputeHostRegex() {
		if (hostRegex == null) {
			return "*";
		}
		return hostRegex + "*";
	}

	private String cumputeInstanceRegex() {
		if (instanceRegex == null) {
			return "*";
		}
		return instanceRegex + "*";
	}

	public PaginationCollection getPaginationCollection() {
		return paginationCollection;
	}

	public PaginationCondition getPagination() {
		return pagination;
	}

	public ServiceInstanceAdmin getService() {
		return service;
	}

	public void setService(ServiceInstanceAdmin service) {
		this.service = service;
	}

	public OrderCondition getOrder() {
		return order;
	}

	public void setOrder(OrderCondition order) {
		this.order = order;
	}

	public String getServiceRegex() {
		return serviceRegex;
	}

	public void setServiceRegex(String serviceRegex) {
		this.serviceRegex = serviceRegex;
	}

	public String getEnvIdRegex() {
		return envIdRegex;
	}

	public void setEnvIdRegex(String envIdRegex) {
		this.envIdRegex = envIdRegex;
	}

	public String getHostRegex() {
		return hostRegex;
	}

	public void setHostRegex(String hostRegex) {
		this.hostRegex = hostRegex;
	}

	public String getInstanceRegex() {
		return instanceRegex;
	}

	public void setInstanceRegex(String instanceRegex) {
		this.instanceRegex = instanceRegex;
	}

	public void setInstances(List<ServiceInstanceOverview> instances) {
		this.instances = instances;
	}

	public void setPagination(PaginationCondition pagination) {
		this.pagination = pagination;
	}

	public void setPaginationCollection(PaginationCollection paginationCollection) {
		this.paginationCollection = paginationCollection;
	}

}
