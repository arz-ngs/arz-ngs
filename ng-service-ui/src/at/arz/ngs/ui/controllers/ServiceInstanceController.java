package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.search.OrderCondition;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverviewList;
import at.arz.ngs.ui.data_collections.ConfirmStopAllCollection;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;
import at.arz.ngs.ui.data_collections.OrderImgCollection;
import at.arz.ngs.ui.data_collections.OverviewCollection;
import at.arz.ngs.ui.data_collections.PaginationCollection;

@SessionScoped
@Named("serviceinstance")
public class ServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin admin;

	private List<OverviewCollection> instancesCollection;

	private PaginationCondition pagination;
	private PaginationCollection paginationCollection;
	private int numElementsFound;

	private String serviceRegex;
	private String envIdRegex;
	private String hostRegex;
	private String instanceRegex;

	private OrderCondition order;
	private OrderImgCollection orderCollection;

	private ErrorCollection errorCollection;

	private ConfirmStopAllCollection confirmCollection;

	@PostConstruct
	public void init() {
		pagination = new PaginationCondition(50, 1); // default is first page with 50 elements
		order = new OrderCondition(OrderCondition.ORDERBY_SERVICEINSTANCE, OrderCondition.ASCENDING);

		errorCollection = new ErrorCollection();
		try {
			mapToOverviewCollection(admin.getServiceInstances("*", "*", "*", "*", order, pagination));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}

		paginationCollection = new PaginationCollection();
		orderCollection = new OrderImgCollection();

		doPaginationValidation();
		doSortValidation("instance");

		confirmCollection = new ConfirmStopAllCollection();
		confirmCollection.setShowPopup(false);
	}

	private void mapToOverviewCollection(ServiceInstanceOverviewList list) {
		numElementsFound = list.getNumElementsFound();

		instancesCollection = new ArrayList<>();
		for (ServiceInstanceOverview o : list.getServiceInstances()) {
			instancesCollection.add(new OverviewCollection(o));
		}
	}

	public String goToOverview() {
		formSubmit();
		return "overview";
	}

	/**
	 * This method should be invoked when someone presses Enter on the overview
	 * (sets the current page to the first one)
	 */
	public void enterFormSubmit() {
		pagination.setCurrentPage(1);
		formSubmit();
	}

	public void formSubmit() {
		errorCollection = new ErrorCollection();
		try {
			mapToOverviewCollection(admin.getServiceInstances(cumputeServiceRegex(), cumputeEnvRegex(),
					cumputeHostRegex(), cumputeInstanceRegex(), order, pagination));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}

		doPaginationValidation();
	}

	public void sortAction(String sortBy) {
		if (sortBy.equals(orderCollection.getLastSortedBy())) { // turn arrow
			if (orderCollection.isLastSortedASC()) { // if asc last, now sort desc
				order.setOrder(OrderCondition.DESCENDING);
			}
			else {
				order.setOrder(OrderCondition.ASCENDING);
			}
		}
		else {
			order.setOrder(OrderCondition.ASCENDING);
		}

		if (sortBy.equals("service")) {
			order.setOrderByField(OrderCondition.ORDERBY_SERVICE);
		}
		else if (sortBy.equals("envId")) {
			order.setOrderByField(OrderCondition.ORDERBY_ENVIRONMENT);
		}
		else if (sortBy.equals("host")) {
			order.setOrderByField(OrderCondition.ORDERBY_HOST);
		}
		else if (sortBy.equals("instance")) {
			order.setOrderByField(OrderCondition.ORDERBY_SERVICEINSTANCE);
		}
		else if (sortBy.equals("status")) {
			order.setOrderByField(OrderCondition.ORDERBY_STATUS);
		}

		errorCollection = new ErrorCollection();
		try {
			mapToOverviewCollection(admin.getServiceInstances(cumputeServiceRegex(), cumputeEnvRegex(),
					cumputeHostRegex(), cumputeInstanceRegex(), order, pagination));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}

		doSortValidation(sortBy);
	}

	private void doSortValidation(String lastSortedBy) {
		orderCollection.setLastSortedASC(order.getOrder().equals(OrderCondition.ASCENDING));
		orderCollection.setLastSortedBy(lastSortedBy);

		orderCollection.setServiceOrderSRC(resolveOrderImg("service"));
		orderCollection.setEnvOrderSRC(resolveOrderImg("envId"));
		orderCollection.setHostOrderSRC(resolveOrderImg("host"));
		orderCollection.setInstanceOrderSRC(resolveOrderImg("instance"));
		orderCollection.setStatusOrderSRC(resolveOrderImg("status"));
	}

	private String resolveOrderImg(String field) {
		if (orderCollection.getLastSortedBy().equals(field)) {
			if (orderCollection.isLastSortedASC()) {
				return OrderImgCollection.ASC_enabled;
			}
			else {
				return OrderImgCollection.DESC_enabled;
			}
		}
		else {
			return OrderImgCollection.ASC_disabled;
		}
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

		errorCollection = new ErrorCollection();
		try {
			mapToOverviewCollection(admin.getServiceInstances(cumputeServiceRegex(), cumputeEnvRegex(),
					cumputeHostRegex(), cumputeInstanceRegex(), order, pagination));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}

		doPaginationValidation();
	}

	/**
	 * Computes the fields in the pagination (ui)
	 */
	private void doPaginationValidation() {
		int overallElementCount = numElementsFound;

		int currentPage = pagination.getCurrentPage();
		int elemPerPage = pagination.getElementsPerPage();
		int lastPage = 0;
		if (overallElementCount % elemPerPage == 0) {
			lastPage = overallElementCount / elemPerPage;
		}
		else {
			lastPage = (overallElementCount / elemPerPage) + 1;
		}

		paginationCollection.setLeftCaretClass(currentPage == 1 ? PaginationCollection.DISABLED : null);
		paginationCollection.setLeftCaretDisabled(currentPage == 1);
		paginationCollection.setRightCaretClass(currentPage == lastPage ? PaginationCollection.DISABLED : null);
		paginationCollection.setRightCaretDisabled(currentPage == lastPage);

		int numPages = lastPage;

		if (numPages == 1 || numPages == 0) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(false);

			paginationCollection.setSecondElement("-1"); // not shown here
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("-1");
			paginationCollection.setFithElement("-1");
		}
		else if (numPages == 2) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("-1");
		}
		else if (numPages == 3) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("2");
		}
		else if (numPages == 4) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("2");
			paginationCollection.setFourthElement("3");
		}
		else if (numPages >= 5) {
			paginationCollection.setShowSecondElem(true);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);
			// don't hide anything

			if (currentPage == 1 || currentPage == 2) {
				paginationCollection.setSecondElement("2");
				paginationCollection.setThirdElement("3");
				paginationCollection.setFourthElement("4");
			}
			else if (currentPage == lastPage || currentPage == lastPage - 1) {
				paginationCollection.setFourthElement((lastPage - 1) + "");
				paginationCollection.setThirdElement((lastPage - 2) + "");
				paginationCollection.setSecondElement((lastPage - 3) + "");
			}
			else { // if the current page is somewhere in the middle
				paginationCollection.setSecondElement((currentPage - 1) + "");
				paginationCollection.setThirdElement(currentPage + "");
				paginationCollection.setFourthElement((currentPage + 1) + "");
			}
		}

		paginationCollection.setFithElement(lastPage + "");

		// now highlight current page to be active

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

	public String cancelPendingJob() {
		confirmCollection.dispose();

		formSubmit();
		return "overview";
	}

	public void cancelPendingSingleJob() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("detailview.xhtml?instance=" + confirmCollection.getInstance() + "&service="
							+ confirmCollection.getService() + "&env=" + confirmCollection.getEnvironment() + "&host="
							+ confirmCollection.getHost());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		confirmCollection.dispose();
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
		return admin;
	}

	public void setService(ServiceInstanceAdmin service) {
		this.admin = service;
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

	public void setPagination(PaginationCondition pagination) {
		this.pagination = pagination;
	}

	public void setPaginationCollection(PaginationCollection paginationCollection) {
		this.paginationCollection = paginationCollection;
	}

	public OrderImgCollection getOrderCollection() {
		return orderCollection;
	}

	public void setOrderCollection(OrderImgCollection orderCollection) {
		this.orderCollection = orderCollection;
	}

	public List<OverviewCollection> getInstancesCollection() {
		return instancesCollection;
	}

	public void setInstancesCollection(List<OverviewCollection> instancesCollection) {
		this.instancesCollection = instancesCollection;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

	public ConfirmStopAllCollection getConfirmCollection() {
		return confirmCollection;
	}

	public void setConfirmCollection(ConfirmStopAllCollection confirmCollection) {
		this.confirmCollection = confirmCollection;
	}
}
