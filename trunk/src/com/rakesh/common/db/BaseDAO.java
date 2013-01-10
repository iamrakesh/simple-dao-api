package com.rakesh.common.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.rakesh.common.db.util.GenericMappers.RowMapper;
import com.rakesh.common.util.ClassResources;

/**
 * Base class for all DAO classes, which will provide methods to execute
 * different types of queries.
 * 
 * @author rakesh
 */
public class BaseDAO {

	private boolean readOnly = true;
	private final DataSource ds;

	/**
	 * constructor to create read only dao.
	 */
	public BaseDAO(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * constructor allows you to specify read only attribute.
	 */
	public BaseDAO(DataSource ds, boolean readOnly) {
		if (!readOnly)
			this.readOnly = false;
		this.ds = ds;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * method available for subclasses to read an integer from database.
	 */
	protected Integer queryForInt(ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		Connection c = getConnection();
		try {
			return JdbcUtills.queryForInt(c, resources, sqlKey, params);
		} finally {
			closeConnection(c);
		}
	}

	/**
	 * method available for subclasses to read one Object from database. Object
	 * mapping is done using the RowMapper instance passed as parameter.
	 */
	protected <T> T queryForObject(RowMapper<T> rm, ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		Connection c = getConnection();
		try {
			return JdbcUtills.queryForObject(c, rm, resources, sqlKey, params);
		} finally {
			closeConnection(c);
		}
	}

	/**
	 * method available for subclasses to read list of objects [multiple rows]
	 * from database. Object mapping [row to object] is done using the RowMapper
	 * instance passed as parameter.
	 */
	protected <T> List<T> query(RowMapper<T> rm, ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		Connection c = getConnection();
		try {
			return JdbcUtills.query(c, rm, resources, sqlKey, params);
		} finally {
			closeConnection(c);
		}
	}

	/**
	 * method available for subclasses to execute queries like INSERT, UPDATE,
	 * and DELETE. This method return an integer, number of rows updated by the
	 * query.
	 */
	protected int update(ClassResources resources, final String sqlKey,
			Object... params) throws SQLException {
		Connection c = getConnection();
		try {
			return JdbcUtills.update(c, resources, sqlKey, params);
		} finally {
			closeConnection(c);
		}
	}

	/**
	 * method available for subclasses to execute callable statements.
	 */
	protected boolean executeCall(ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		Connection c = getConnection();
		try {
			return JdbcUtills.executeCall(c, resources, sqlKey, params);
		} finally {
			closeConnection(c);
		}
	}

	/**
	 * method returns a new connection from the pool or the one from existing
	 * transaction.
	 */
	Connection getConnection() throws SQLException {
		TransactionManager trans = TransactionManager.getCurrentTransaction();
		if (trans != null) {
			return trans.getConnection();
		}
		return  doGetConnection();
	}

	/**
	 * return a connection from the pool
	 */
	Connection doGetConnection() throws SQLException {
		Connection c = ds.getConnection();
		c.setReadOnly(this.readOnly);
		// System.out.println("ACTIVE CONNECTIONS => "
		// + ((BasicDataSource) ds).getNumActive());
		return c;
	}

	/**
	 * method returns a connection to pool if its not in a transaction.
	 */
	void closeConnection(Connection c) throws SQLException {
		TransactionManager trans = TransactionManager.getCurrentTransaction();
		if (trans == null)
			doCloseConnection(c);
	}

	/**
	 * method returns a connection to pool by calling close() method on it.
	 */
	void doCloseConnection(Connection c) throws SQLException {
		if (!c.isClosed())
			c.close();
		// System.out.println("ACTIVE CONNECTIONS => "
		// + ((BasicDataSource) ds).getNumActive());
	}
}
