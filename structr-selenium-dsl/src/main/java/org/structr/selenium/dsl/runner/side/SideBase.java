package org.structr.selenium.dsl.runner.side;

import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class SideBase {

	protected final String getString(final Map<String, Object> data, final String key, final boolean optional) {

		final Object value = data.get(key);
		if (value != null) {

			if (value instanceof String) {

				return (String)value;
			}

			if (!optional) {
				throw new IllegalArgumentException("Invalid data type for key " + key + ", expected string, got " + value.getClass().getSimpleName());
			}
		}

		if (!optional) {
			throw new IllegalArgumentException("Missing data entry for key " + key + " in " + data);
		}

		return null;
	}

	protected final List<Map<String, Object>> getObjectList(final Map<String, Object> data, final String key, final boolean optional) {

		final Object value = data.get(key);
		if (value != null) {

			if (value instanceof List) {

				return (List)value;
			}

			if (!optional) {
				throw new IllegalArgumentException("Invalid data type for key " + key + ", expected list of objects, got " + value.getClass().getSimpleName());
			}
		}

		if (!optional) {
			throw new IllegalArgumentException("Missing data entry for key " + key + " in " + data);
		}

		return null;
	}

	protected final List<String> getStringList(final Map<String, Object> data, final String key, final boolean optional) {

		final Object value = data.get(key);
		if (value != null) {

			if (value instanceof List) {

				return (List)value;
			}

			if (!optional) {
				throw new IllegalArgumentException("Invalid data type for key " + key + ", expected list of string, got " + value.getClass().getSimpleName());
			}
		}

		if (!optional) {
			throw new IllegalArgumentException("Missing data entry for key " + key + " in " + data);
		}

		return null;
	}
}