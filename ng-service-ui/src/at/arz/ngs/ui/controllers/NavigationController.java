package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("navigator")
public class NavigationController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ACTIVE_CSS_CLASS = "active";

	@Inject
	private ServiceInstanceController serviceInstanceController;

	private String si_overviewCSS;
	private String timelineCSS;
	private String permission_dropdownCSS;
	private String userOverviewCSS;
	private String roleOverviewCSS;

	@PostConstruct
	public void init() {
		setAllNavPointsNotActive();

		si_overviewCSS = ACTIVE_CSS_CLASS;
	}

	public String navigateToSIOverview() {
		highlightSIOverview();

		return serviceInstanceController.goToOverview();
	}

	public void highlightSIOverview() {
		setAllNavPointsNotActive();

		si_overviewCSS = ACTIVE_CSS_CLASS;
	}

	public String navigateToJournal() {
		highlightJournal();

		return "journal";
	}

	public void highlightJournal() {
		setAllNavPointsNotActive();

		timelineCSS = ACTIVE_CSS_CLASS;
	}

	public String navigateToUserOverview() {
		highlightUserOverview();

		return "useroverview";
	}

	public void highlightUserOverview() {
		setAllNavPointsNotActive();

		permission_dropdownCSS = ACTIVE_CSS_CLASS;
		userOverviewCSS = ACTIVE_CSS_CLASS;
	}

	public String navigateToRoleOverview() {
		highlightRoleOverview();

		return "roleoverview";
	}

	public void highlightRoleOverview() {
		setAllNavPointsNotActive();

		permission_dropdownCSS = ACTIVE_CSS_CLASS;
		roleOverviewCSS = ACTIVE_CSS_CLASS;
	}

	private void setAllNavPointsNotActive() {
		si_overviewCSS = "";
		timelineCSS = "";
		permission_dropdownCSS = "";
		userOverviewCSS = "";
		roleOverviewCSS = "";
	}

	public String getSi_overviewCSS() {
		return si_overviewCSS;
	}

	public void setSi_overviewCSS(String si_overviewCSS) {
		this.si_overviewCSS = si_overviewCSS;
	}

	public String getTimelineCSS() {
		return timelineCSS;
	}

	public void setTimelineCSS(String timelineCSS) {
		this.timelineCSS = timelineCSS;
	}

	public String getPermission_dropdownCSS() {
		return permission_dropdownCSS;
	}

	public void setPermission_dropdownCSS(String permission_dropdownCSS) {
		this.permission_dropdownCSS = permission_dropdownCSS;
	}

	public String getUserOverviewCSS() {
		return userOverviewCSS;
	}

	public void setUserOverviewCSS(String userOverviewCSS) {
		this.userOverviewCSS = userOverviewCSS;
	}

	public String getRoleOverviewCSS() {
		return roleOverviewCSS;
	}

	public void setRoleOverviewCSS(String roleOverviewCSS) {
		this.roleOverviewCSS = roleOverviewCSS;
	}
}
