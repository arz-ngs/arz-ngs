package at.arz.ngs.infrastructure.executors;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class TransactionalExecutor
		implements NGSExecutor {

	@Override
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void execute(Runnable command) {
		command.run();
	}

	/**
	 * Added to support the execution of StartupService.getFetcher() because
	 * here is no Transaction needed. This method works as long as the server is
	 * alive, so every transaction has a timeout. If this method starts a
	 * Status-Fetch it invokes the execute method and so it has a new
	 * Transaction.
	 * 
	 * @param command
	 */
	@Override
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void executeWithoutTransaction(Runnable command) {
		command.run();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void executeNotAsynchronously(Runnable command) {
		command.run();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void executeNotAsynchronouslyWithoutTransaction(Runnable command) {
		command.run();
	}
}
