package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;

public interface ScriptRepository {

	Script getScript(ScriptName scriptName);

	List<Script> getAllScripts();

	void addScript(	ScriptName newScriptName,
					PathStart newPathStart,
					PathStop newPathStop,
					PathRestart newPathRestart,
					PathStatus newPathStatus);

	void removeScript(Script script);

	void updateScript(	Script oldScript,
						ScriptName newScriptName,
						PathStart newPathStart,
						PathStop newPathStop,
						PathRestart newPathRestart,
						PathStatus newPathStatus);
}
