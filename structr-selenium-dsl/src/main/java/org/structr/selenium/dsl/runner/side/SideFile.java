package org.structr.selenium.dsl.runner.side;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SideFile extends SideBase {

	private List<Map<String, Object>> suites = null;
	private List<SideTest> tests             = null;
	private int currentPosition              = 0;
	private String id                        = null;
	private String version                   = null;
	private String name                      = null;
	private String url                       = null;

	public SideFile(final Path source) throws IOException {

		init(source);
	}

	public List<Map<String, Object>> getSuites() {
		return suites;
	}

	public List<SideTest> getTests() {
		return tests;
	}

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setCurrentPosition(final int pos) {
		this.currentPosition = pos;
	}

	public int getCurrentPosition() {
		return this.currentPosition;
	}

	// ----- private methods -----
	private void init(final Path source) throws IOException {

		final Gson gson = new GsonBuilder().create();

		try (final FileReader reader = new FileReader(source.toFile())) {

			final Map<String, Object> data = gson.fromJson(reader, Map.class);
			this.tests                     = new LinkedList<>();
			this.id                        = getString(data, "id", false);
			this.version                   = getString(data, "version", false);
			this.name                      = getString(data, "name", false);
			this.url                       = getString(data, "url", false);
			this.suites                    = getObjectList(data, "suites", false);

			for (final Map<String, Object> test : getObjectList(data, "tests", false)) {

				final List<Map<String, Object>> commands = getObjectList(test, "commands", false);

				for (final Map<String, Object> command : commands) {
					this.tests.add(new SideTest(this, command));
				}
			}
		}
	}
}