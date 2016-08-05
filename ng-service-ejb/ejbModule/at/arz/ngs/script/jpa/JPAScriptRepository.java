package at.arz.ngs.script.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;

public class JPAScriptRepository
		implements ScriptRepository {

	private EntityManager entityManager;

	public JPAScriptRepository(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public Script getScript(ScriptName scriptName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Script> getAllScripts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addScript(	ScriptName newScriptName,
							PathStart newPathStart,
							PathStop newPathStop,
							PathRestart newPathRestart,
							PathStatus newPathStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeScript(Script script) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateScript(	Script oldScript,
								ScriptName newScriptName,
								PathStart newPathStart,
								PathStop newPathStop,
								PathRestart newPathRestart,
								PathStatus newPathStatus) {
		// TODO Auto-generated method stub

	}
}
