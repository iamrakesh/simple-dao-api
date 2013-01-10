package com.rakesh.common.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DataSource wrapper for logging
 *
 * @author Elli Albek
 */
public class DataSourceLog implements DataSource {

	private static final Log logger = LogFactory.getLog(DataSourceLog.class);

	private static final boolean isDebugEnabled = logger.isDebugEnabled();

	private final BasicDataSource source;

	public DataSourceLog(final BasicDataSource delegate) {
		source = delegate;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (isDebugEnabled)
			logger.debug("GET CONNECTION");
		long t1 = System.currentTimeMillis();
		Connection c = source.getConnection();
		if (isDebugEnabled)
			logger.debug("ACTIVE CONNECTIONS:\t" + source.getNumActive() + " (get connection took "
					+ (System.currentTimeMillis() - t1) + "ms)");
		return c;
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		if (isDebugEnabled)
			logger.debug("GET CONNECTION");
		Connection c = source.getConnection(username, password);
		if (isDebugEnabled)
			logger.debug("ACTIVE CONNECTIONS:\t" + source.getNumActive());
		return c;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return source.getLoginTimeout();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return source.getLogWriter();
	}

	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
		source.setLoginTimeout(seconds);
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		source.setLogWriter(out);
	}

	public void close() throws SQLException {
		source.close();
	}

    /* JDBC_4_ANT_KEY_BEGIN */
    @Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }

    @Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLException("BasicDataSource is not a wrapper.");
    }
    /* JDBC_4_ANT_KEY_END */

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Datasource uses Log4J not Java Logging...");
	}

}
