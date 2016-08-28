package at.arz.ngs.infrastructure.executors;

import java.util.concurrent.Executor;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

@Stateless
public class TransactionalExecutor
		implements Executor {

	@Override
	@Asynchronous
	public void execute(Runnable command) {
		command.run();
	}
}
