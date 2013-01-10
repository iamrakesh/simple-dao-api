package com.rakesh.common.util;

/**
 * Exception class to convert an checked IOException to an unchecked
 * RuntimeExeption.
 * 
 * @author rakesh
 */
public class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = -773704577999567524L;

	public RuntimeIOException(final String msg, Exception e) {
		super(msg, e);
	}
}