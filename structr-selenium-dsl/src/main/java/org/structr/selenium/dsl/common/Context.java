package org.structr.selenium.dsl.common;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.structr.selenium.dsl.command.CommandFactory;
import org.structr.selenium.dsl.runner.script.ScriptFile;

/**
 */
public class Context {

	private final Map<String, Object> data = new LinkedHashMap<>();
	private boolean recordingEnabled       = false;
	private CommandFactory factory         = null;
	private WebDriver driver               = null;
	private Actions actions                = null;

	public Context(final CommandFactory factory, final WebDriver driver, final Actions actions) throws IOException {

		this.factory = factory;
		this.driver  = driver;
		this.actions = actions;
	}

	public CommandFactory getCommandFactory() {
		return factory;
	}

	public WebDriver getWebDriver() {
		return driver;
	}

	public Actions getActions() {
		return actions;
	}

	public void undefine(final String name) {

		data.remove(name);
	}

	public void define(final String name, final Object value) {

		if (data.containsKey(name)) {

			throw new IllegalArgumentException("Error: " + name + " already defined.");
		}

		data.put(name, value);
	}

	public boolean isDefined(final String name) {
		return data.containsKey(name);
	}

	public Object getDefined(final String name) {
		return data.get(name);
	}

	public void setCurrentScript(final ScriptFile script) {
		data.remove("script");
		data.put("script", script);
	}

	public ScriptFile getCurrentScript() {
		return (ScriptFile)data.get("script");
	}

	public void startRecording() {
		this.recordingEnabled = true;
	}

	public void stopRecording() {
		this.recordingEnabled = false;
	}

	public boolean isRecording() {
		return recordingEnabled;
	}
}
