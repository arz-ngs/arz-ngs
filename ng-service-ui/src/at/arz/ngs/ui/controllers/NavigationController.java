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

	@PostConstruct
	public void init() {
		si_overviewCSS = "";
	}

	public String navigateToSIOverview() {
		setAllNavPointNotActive();

		si_overviewCSS = ACTIVE_CSS_CLASS;

		return serviceInstanceController.goToOverview();
	}

	private void setAllNavPointNotActive() {
		si_overviewCSS = "";
	}

	public String getSi_overviewCSS() {
		return si_overviewCSS;
	}
}
