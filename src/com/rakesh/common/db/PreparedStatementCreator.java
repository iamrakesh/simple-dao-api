package com.rakesh.common.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import com.rakesh.common.util.ClassResources;

/**
 * PreparedStatement Creator classes, which reads SQL query from resources file,
 * creates PreparedStatement instance and sets parameters.
 * 
 * @author rakesh
 */
public class PreparedStatementCreator {

	private final String sql;
	private Object[] params;

	PreparedStatementCreator(ClassResources resources, final String sqlKey,
			Object... params) {
		this.sql = resources.getValue(sqlKey);
		this.params = params;
	}

	/**
	 * method to create PreparedStatement instance and sets all the parameters
	 * and returns it.
	 * 
	 * @throws SQLException
	 */
	PreparedStatement createPreparedStatement(Connection c) throws SQLException {
		// create prepared statement instance
		PreparedStatement pst = c.prepareStatement(sql);
		// set parameters
		if (params != null)
			setValues(pst, params);
		return pst;
	}

	/**
	 * method to create callable statement instance and sets all the parameters
	 * and returns it.
	 * 
	 * @throws SQLException
	 */
	CallableStatement createPreparedCall(Connection c) throws SQLException {
		CallableStatement prepareCall = c.prepareCall(sql);
		// set parameters
		if (params != null)
			setValues(prepareCall, params);
		return prepareCall;
	}

	/**
	 * loops through all parameters and checks each parameter type and sets them
	 * by calling appropriate setXXX() method on PreparedStatement.
	 * 
	 * @throws SQLException
	 */
	private static void setValues(PreparedStatement pst, Object... params)
			throws SQLException {
		for (int i = 0; i < params.length; i++) {
			Object arg = params[i];
			if (arg == null)
				pst.setObject(i + 1, null);
			else if (arg instanceof String)
				pst.setString(i + 1, (String) arg);
			else if (arg instanceof Integer)
				pst.setInt(i + 1, (Integer) arg);
			else if (arg instanceof Float)
				pst.setFloat(i + 1, (Float) arg);
			else if (arg instanceof Long)
				pst.setLong(i + 1, (Long) arg);
			else if (arg instanceof Double)
				pst.setDouble(i + 1, (Double) arg);
			else if (arg instanceof Boolean)
				pst.setBoolean(i + 1, (Boolean) arg);
			else if (arg instanceof Date)
				pst.setDate(i + 1, (Date) arg);
			else if (arg instanceof Time)
				pst.setTime(i + 1, (Time) arg);
			else if (arg instanceof Timestamp)
				pst.setTimestamp(i + 1, (Timestamp) arg);
			else if (arg instanceof Object[])
				pst.setObject(i + 1, arg);
			else
				throw new RuntimeException(
						"Unsupported parameter type passed as parameter for Prepared statement => '"
								+ arg.getClass().getSimpleName() + "'");
		}
	}
}
