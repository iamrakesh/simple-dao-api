package com.rakesh.common.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains generic ResultSet row mapping functions and mapper objects that can
 * be used by DAOs.
 * 
 * @author rakesh
 */
public class GenericMappers {

	/**
	 * Row mapper that returns a single Boolean field.
	 */
	public static final RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
		public Boolean mapRow(ResultSet rs) throws SQLException {
			return rs.getBoolean(1);
		}
	};

	/**
	 * Row mapper that returns a single Integer field.
	 */
	public static RowMapper<Integer> integerRowMapper = new RowMapper<Integer>() {
		public Integer mapRow(ResultSet rs) throws SQLException {
			return rs.getInt(1);
		}
	};

	/**
	 * Row mapper that returns a single String field.
	 */
	public static final RowMapper<String> stringRowMapper = new RowMapper<String>() {
		public String mapRow(ResultSet rs) throws SQLException {
			return rs.getString(1);
		}
	};

	/**
	 * Base Interface for all row mapper objects.
	 * 
	 * @author rakesh
	 */
	public interface RowMapper<T> {
		/**
		 * subclasses should implement this method and write code to map single
		 * row to an Object of type <T>
		 * 
		 * @throws SQLException
		 */
		public T mapRow(ResultSet rs) throws SQLException;
	}
}
