package com.rakesh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to read resources of a class.
 *
 * @author rakesh
 */
public class ClassResources {

	private final Properties p;

	public ClassResources(final Class<?> cl){
		try {
			p = loadProperties(cl);
		} catch (IOException e) {
			throw new RuntimeIOException(cl.getSimpleName() + ".properties file not found!", e);
		}
	}

	/**
	 * returns value for the given key which is read from the resource file
	 */
	public String getValue(final String key) {
		return p.getProperty(key);
	}

	/**
	 * private method to load properties from the [class name passed].properties
	 * file.
	 *
	 * @throws IOException
	 */
	private static final Properties loadProperties(final Class<?> cl) throws IOException {
		final String resourceFile = cl.getSimpleName() + ".properties";
		InputStream in = cl.getResourceAsStream(resourceFile);
		if (in == null)
			in = cl.getClassLoader().getResourceAsStream(resourceFile);
		if (in == null) {
			throw new IOException("Resource file '" + resourceFile + "' not found!");
		}
		// load properties by reading from the resource file
		Properties p = new Properties();
		p.load(in);
		return p;
	}
}
