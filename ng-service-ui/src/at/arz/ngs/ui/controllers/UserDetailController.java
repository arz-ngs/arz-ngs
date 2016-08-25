package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.user.commands.UserData;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.commands.removeRole.RemoveRoleFromUser;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;
import at.arz.ngs.ui.data_collections.UserRoleCollection;

@ViewScoped
@Named("userDetail")
public class UserDetailController
		implements Serializable {

	public static final String PLEASE_CHOOSE = "Rolle auswählen...";

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	@Inject
	private UserController userController;

	private UserData currentUser;

	private boolean handover;

	private List<UserRoleCollection> currentUserRoles;

	private List<String> availableRoles;

	private String chosenElement;

	private ErrorCollection errorCollection;

	private boolean renderNewRemoveRoleElements;

	@PostConstruct
	public void init() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		errorCollection = new ErrorCollection();
		try {
			currentUser = admin.getUserDataFromUser(params.get("username"));
			if (!(currentUser.getFirst_name() == null || currentUser.getFirst_name().equals("")
					|| currentUser.getFirst_name().equals(" "))) {
				currentUser.setFirst_name(" - " + currentUser.getFirst_name());
			}
		}
		catch (RuntimeException e) {
			availableRoles = new LinkedList<>();
			chosenElement = PLEASE_CHOOSE;
			availableRoles.add(PLEASE_CHOOSE);
			currentUser = new UserData("", "", "", "");

			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return;
		}
		refresh();
	}

	public void goToRoleDetail(String role) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("roledetail.xhtml?role=" + role);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void refresh() {
		availableRoles = new LinkedList<>();
		availableRoles.add(PLEASE_CHOOSE);
		handover = false;
		chosenElement = PLEASE_CHOOSE;

		errorCollection = new ErrorCollection();
		try {
			List<String> rolesToHandOver = admin.getAllRoles().getRoles(); //TODO get only those roles which the user can handover
			if (rolesToHandOver == null || rolesToHandOver.size() == 0) {
				renderNewRemoveRoleElements = false; //can not handover some roles, so he also can't remove roles
			}
			else {
				renderNewRemoveRoleElements = true;
				availableRoles.addAll(rolesToHandOver);
			}
			currentUserRoles = convert(admin.getRolesForUser(currentUser.getUserName()).getUserRoles());
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
	}

	private List<UserRoleCollection> convert(List<UserRole> roles) {
		List<UserRoleCollection> res = new LinkedList<>();

		for (UserRole r : roles) {
			res.add(new UserRoleCollection(availableRoles.contains(r.getRoleName()), r)); //if a user can handover a role, he also can remove it from a user
		}

		return res;
	}

	public String removeRoleFromUser(String role) {
		RemoveRoleFromUser command = new RemoveRoleFromUser(role, currentUser.getUserName());

		errorCollection = new ErrorCollection();
		try {
			admin.removeRoleFromUser(userController.getCurrentActor(), command);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return "";
		}

		refresh();
		return "";
	}

	public String addRoleToUser() {
		if (chosenElement.equals(PLEASE_CHOOSE)) {
			errorCollection = new ErrorCollection();
			errorCollection.addError(new Error(new IllegalArgumentException("No role is selected! Please choose one")));
			errorCollection.setShowPopup(true);
			return "";
		}

		AddRoleToUser command = new AddRoleToUser(currentUser.getUserName(), chosenElement, handover);

		errorCollection = new ErrorCollection();
		try {
			admin.addRoleToUser(userController.getCurrentActor(), command);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return "";
		}

		refresh();
		return "";
	}

	public String getChosenElement() {
		return chosenElement;
	}

	public void setChosenElement(String chosenElement) {
		this.chosenElement = chosenElement;
	}

	public UserData getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserData currentUser) {
		this.currentUser = currentUser;
	}

	public List<UserRoleCollection> getCurrentUserRoles() {
		return currentUserRoles;
	}

	public void setCurrentUserRoles(List<UserRoleCollection> currentUserRoles) {
		this.currentUserRoles = currentUserRoles;
	}

	public boolean isRenderNewRemoveRoleElements() {
		return renderNewRemoveRoleElements;
	}

	public void setRenderNewRemoveRoleElements(boolean renderNewRemoveRoleElements) {
		this.renderNewRemoveRoleElements = renderNewRemoveRoleElements;
	}

	public boolean isHandover() {
		return handover;
	}

	public void setHandover(boolean handover) {
		this.handover = handover;
	}

	public List<String> getAvailableRoles() {
		return availableRoles;
	}

	public void setAvailableRoles(List<String> availableRoles) {
		this.availableRoles = availableRoles;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}
}
