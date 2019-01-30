package org.structr.selenium.dsl.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.structr.selenium.dsl.action.AbstractAction;
import org.structr.selenium.dsl.action.IfCommand;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.command.CommandFactory;
import static org.structr.selenium.dsl.common.AbstractTestRunner.pad;
import org.structr.selenium.dsl.runner.interactive.Terminal;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.runner.side.SideTest;

/**
 */
public class Context {

	private final Map<String, Object> data = new LinkedHashMap<>();
	private boolean recordingEnabled       = false;
	private boolean executeNextLine        = true;
	private CommandFactory factory         = null;
	private WebDriver driver               = null;
	private Actions actions                = null;
	private File workDir                   = null;
	private int width                      = 0;
	private int tests                      = 0;
	private int errors                     = 0;
	private int passed                     = 0;
	private int failed                     = 0;


	public Context(final CommandFactory factory, final WebDriver driver, final Actions actions, final int width) throws IOException {

		this.factory = factory;
		this.driver  = driver;
		this.actions = actions;
		this.width   = width;
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

	public void setWorkDirectory(final File directory) {
		this.workDir = directory;
	}

	public File getWorkDirectory() {
		return workDir;
	}

	public Path getPathRelativeToWorkDir(final Path p) {

		if (workDir != null) {

			final Path work = workDir.toPath();

			return work.resolve(p);
		}

		return p;
	}

	public void runLine(final Terminal terminal, final String line, final int lineNumber, final int indent) {

		final String trimmed  = line.trim();
		final int left        = width - trimmed.length() - 7 - indent;

		if (trimmed.length() > 0 && !trimmed.startsWith("#")) {

			terminal.print(StringUtils.leftPad("", indent));
			
			if (lineNumber > 0) {
				terminal.print(pad(lineNumber, 3, " ") + ": " + trimmed);
			}

			try {

				final Command command = factory.fromLine(this, trimmed);
				if (command != null) {

					if (executeNextLine) {

						terminal.resetReceivedOutputFlag();

						if (command instanceof IfCommand) {

							executeNextLine = command.execute(terminal);

							if (!terminal.isInteractive()) {
								terminal.println(pad("OK", left, " "));
							}
							
						} else if (command instanceof AbstractAction) {

							final AbstractAction action = (AbstractAction)command;

							if (command.execute(terminal)) {

								passed++;

								if (!terminal.receivedOutput() && !terminal.isInteractive()) {
									
									terminal.println(pad("OK", left, " "));
								}

							} else {

								failed++;

								if (!terminal.receivedOutput() && !terminal.isInteractive()) {
									
									terminal.println(pad("FAILED", left, " "));
								}

								terminal.println(action.getErrorMessage());
							}

						} else {

							errors++;

							if (!terminal.isInteractive()) {

								terminal.println(pad("ERROR", left, " "));
							}

							terminal.println("Error: command must be either action or assertion.");
						}
						
					} else {

						if (!terminal.isInteractive()) {
							terminal.println(pad("SKIPPED", left, " "));
						}
						
						// reset if condition
						executeNextLine = true;
					}

				} else {

					if (!terminal.isInteractive()) {

						terminal.println(pad("ERROR", left, " "));
					}
					
					terminal.println("Error: unknown command \"" + trimmed + "\"");
				}

			} catch (Throwable t) {

				errors++;

				if (!terminal.isInteractive()) {

					terminal.println(pad("ERROR", left, " "));
					terminal.println(t.getMessage());
				}
			}
		}
	}

	public void runSide(final Terminal terminal, final SideTest test, final int lineNumber) {

		final String trimmed  = test.toString();
		final int left        = width - trimmed.length() - 7;

		terminal.print(pad(lineNumber, 3, " ") + ": " + trimmed);

		try {

			final Command command = factory.fromSide(this, lineNumber, test);
			if (command != null) {

				if (command instanceof AbstractAction) {

					final AbstractAction action = (AbstractAction)command;

					if (command.execute(null)) {

						passed++;

						terminal.println(pad("OK", left, " "));

					} else {

						failed++;

						terminal.println(pad("FAILED", left, " "));
						terminal.println(action.getErrorMessage());
					}

				} else {

					errors++;

					terminal.println(pad("ERROR", left, " "));
					terminal.println("Error: command must be either action or assertion.");
				}

			} else {

				terminal.println(pad("ERROR", left, " "));
				terminal.println("Error: unknown command \"" + trimmed + "\"");
			}

		} catch (NullPointerException npe) {

			errors++;

			terminal.println(pad("ERROR", left, " "));

			npe.printStackTrace();

		} catch (Throwable t) {

			errors++;

			terminal.println(pad("ERROR", left, " "));
			terminal.println(t.getMessage());
		}
	}

	public void countTest() {
		tests++;
	}

	public int getPassed() {
		return passed;
	}

	public int getErrors() {
		return errors;
	}

	public int getFailed() {
		return failed;
	}

	public int getTests() {
		return tests;
	}

	public int getWidth() {
		return width;
	}
}
