package com.rakesh.common.db;

import static com.rakesh.common.db.util.GenericMappers.integerRowMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rakesh.common.db.util.GenericMappers.RowMapper;
import com.rakesh.common.util.ClassResources;

/**
 * Utility class, provides all DAO related operations like Statement creation,
 * query execution, ReslutSet processing, etc.
 * 
 * @author rakesh
 */
public class JdbcUtills {

	/**
	 * method executes query which expects one row as result and it should be an
	 * Integer.
	 * 
	 * @throws SQLException
	 */
	static Integer queryForInt(Connection c, ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		// create prepared statement
		PreparedStatement pst = getPreparedStatement(c, resources, sqlKey,
				params);
		try {
			// execute query
			ResultSet rs = pst.executeQuery();
			try {
				// map results
				return processOneRow(integerRowMapper, rs);
			} finally {
				rs.close();
			}
		} finally {
			pst.close();
		}
	}

	/**
	 * method executes a query which expects one row as result and that row is
	 * mapped to an Object using the RowMapper instance passed.
	 * 
	 * @throws SQLException
	 */
	static <T> T queryForObject(Connection c, RowMapper<T> rm,
			ClassResources resources, final String sqlKey, Object... params)
			throws SQLException {
		// create prepared statement
		PreparedStatement pst = getPreparedStatement(c, resources, sqlKey,
				params);
		try {
			// execute query
			ResultSet rs = pst.executeQuery();
			try {
				// map results
				return processOneRow(rm, rs);
			} finally {
				rs.close();
			}
		} finally {
			pst.close();
		}
	}

	/**
	 * method to execute a query which expects multiple rows. This method
	 * returns an instance of List, with mapped results.
	 * 
	 * @throws SQLException
	 */
	static <T> List<T> query(Connection c, RowMapper<T> rm,
			ClassResources resources, final String sqlKey, Object... params)
			throws SQLException {
		// create prepared statement
		PreparedStatement pst = getPreparedStatement(c, resources, sqlKey,
				params);
		try {
			// execute query
			ResultSet rs = pst.executeQuery();
			try {
				// map results
				return processMultipleRows(rm, rs);
			} finally {
				rs.close();
			}
		} finally {
			pst.close();
		}
	}

	/**
	 * method to execute queries like INSERT, UPDATE, DELETE queries. Returns
	 * number of rows updated by the query.
	 * 
	 * @throws SQLException
	 */
	static int update(Connection c, ClassResources resources,
			final String sqlKey, Object... params) throws SQLException {
		PreparedStatement pst = getPreparedStatement(c, resources, sqlKey,
				params);
		try {
			return pst.executeUpdate();
		} finally {
			pst.close();
		}
	}

	/**
	 * method to execute callable statements.
	 * 
	 * @throws SQLException
	 */
	static boolean executeCall(Connection c, ClassResources resources,
			String sqlKey, Object[] params) throws SQLException {
		CallableStatement cst = getCallableStatement(c, resources, sqlKey,
				params);
		try {
			return cst.execute();
		} finally {
			cst.close();
		}

	}

	/**
	 * maps one row of the ResultSet and returns it. Mapping is done using the
	 * RowMapper implementation passed as parameter. If the ResultSet doesn't
	 * have any rows returns null.
	 * 
	 * @throws SQLException
	 */
	static <T> T processOneRow(RowMapper<T> rm, ResultSet rs)
			throws SQLException {
		if (rs.next())
			return rm.mapRow(rs);
		return null;
	}

	/**
	 * maps all the rows from ResultSet and returns an instance of ArrayList
	 * containing objects of type T. Mapping is done using the RowMapper
	 * implementation passed as parameter. If the ResultSet doesn't have any
	 * rows returns empty ArrayList instance.
	 * 
	 * @throws SQLException
	 */
	static <T> List<T> processMultipleRows(RowMapper<T> rm, ResultSet rs)
			throws SQLException {
		ArrayList<T> results = new ArrayList<T>();
		while (rs.next()) {
			T record = rm.mapRow(rs);
			results.add(record);
		}
		return results;
	}

	/**
	 * creates PreparedStatement instance and sets all parameters.
	 * 
	 * @throws SQLException
	 */
	private static PreparedStatement getPreparedStatement(Connection c,
			ClassResources resources, final String sqlKey, Object... params)
			throws SQLException {
		return new PreparedStatementCreator(resources, sqlKey, params)
				.createPreparedStatement(c);
	}

	/**
	 * creates CallableStatement instance and sets all parameters.
	 * 
	 * @throws SQLException
	 */
	private static CallableStatement getCallableStatement(Connection c,
			ClassResources resources, final String sqlKey, Object... params)
			throws SQLException {
		return new PreparedStatementCreator(resources, sqlKey, params)
				.createPreparedCall(c);
	}
}
