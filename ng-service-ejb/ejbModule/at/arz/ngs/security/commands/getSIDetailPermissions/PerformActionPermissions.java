package at.arz.ngs.security.commands.getSIDetailPermissions;

public class PerformActionPermissions {
	private boolean ableToStart;
	private boolean ableToStop;
	private boolean ableToRestart;
	private boolean ableToStatus;

	public boolean isAbleToStart() {
		return ableToStart;
	}

	public void setAbleToStart(boolean ableToStart) {
		this.ableToStart = ableToStart;
	}

	public boolean isAbleToStop() {
		return ableToStop;
	}

	public void setAbleToStop(boolean ableToStop) {
		this.ableToStop = ableToStop;
	}

	public boolean isAbleToRestart() {
		return ableToRestart;
	}

	public void setAbleToRestart(boolean ableToRestart) {
		this.ableToRestart = ableToRestart;
	}

	public boolean isAbleToStatus() {
		return ableToStatus;
	}

	public void setAbleToStatus(boolean ableToStatus) {
		this.ableToStatus = ableToStatus;
	}
}
