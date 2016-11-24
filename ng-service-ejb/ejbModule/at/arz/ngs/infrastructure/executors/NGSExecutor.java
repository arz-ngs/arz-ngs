package at.arz.ngs.infrastructure.executors;

import java.util.concurrent.Executor;

public interface NGSExecutor
		extends Executor {

	void executeWithoutTransaction(Runnable command);

	void executeNotAsynchronously(Runnable command);

	void executeNotAsynchronouslyWithoutTransaction(Runnable command);
}
