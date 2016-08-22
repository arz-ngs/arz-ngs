package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import at.arz.ngs.security.commands.Actor;

@SessionScoped
@Named("user")
public class UserController
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private Actor actor;

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
