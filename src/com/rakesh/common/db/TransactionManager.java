package com.rakesh.common.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple implementation of Transaction management. Use this class when you want
 * to implement transactions to execute multiple queries. USAGE:
 * 
 * <pre>
 * 	<strong>TransactionManager trans = TransactionManager.start();</strong>
 *  	try {
 *  		// execute all queries
 *  		queryForInt(...);
 *  		query(...);
 *  		update(...);
 *  		<strong>trans.commit();</strong>
 *  	} catch (Exception e) {
 *  		<strong>trans.rollback();</strong>
 *  		// do rest of the work
 *  	} finally {
 *  		<strong>trans.finished();</strong>
 *  	}
 * </pre>
 * 
 * NOTE: Current implementation doesn't allow to have nested transaction
 * managers.
 * 
 * @author rakesh
 */
public class TransactionManager {

	private static final ThreadLocal<TransactionManager> threadData = new ThreadLocal<TransactionManager>();

	private BaseDAO dao;
	private Connection c;

	private TransactionManager(BaseDAO dao) {
		this.dao = dao;
		threadData.set(this);
	}

	/**
	 * method returns connection object being used in this transaction.
	 * 
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (c == null) {
			this.c = dao.doGetConnection();
			// set auto commit to false if its in a read/write transaction
			if (!dao.isReadOnly())
				this.c.setAutoCommit(false);
		}
		return c;
	}

	/**
	 * creates a transaction manager, denotes start of transaction.
	 */
	public static TransactionManager start(BaseDAO dao) {
		// check for transaction
		assertNotInTransaction("A Transaction is running!, Current API doesn't suport nested transactions!");
		return new TransactionManager(dao);
	}

	/**
	 * commits all changes till this point in a transaction
	 */
	public void commit() throws SQLException {
		if (this.c != null)
			this.c.commit();
	}

	/**
	 * roll backs all the changes till this point in a transaction
	 */
	public void rollback() throws SQLException {
		if (this.c != null)
			this.c.rollback();
	}

	/**
	 * finishes a transaction, removes all transaction related data and closes
	 * connection used in this transaction.
	 */
	public void finish() throws SQLException {
		// check for transaction
		assertInTransaction("No Active Transaction exists!");
		// remove thread local info.
		threadData.remove();
		// remove local reference
		final Connection c = this.c;
		this.c = null;
		// close connection properly
		dao.closeConnection(c);
	}

	/**
	 * return current transaction reference, if not in a transaction returns
	 * NULL.
	 */
	public static TransactionManager getCurrentTransaction() {
		return threadData.get();
	}

	/**
	 * asserts not in transaction. If fails then uses the passed string as error
	 * message.
	 */
	private static void assertNotInTransaction(final String msg) {
		assert (getCurrentTransaction() == null) : msg;
	}

	/**
	 * asserts in transaction. If fails then uses the passed string as error
	 * message.
	 */
	private static void assertInTransaction(final String msg) {
		assert (getCurrentTransaction() != null) : msg;
	}
}
