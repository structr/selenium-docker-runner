package org.structr.selenium.dsl;

import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.runner.interactive.InteractiveTestRunner;
import org.structr.selenium.dsl.runner.script.ScriptFileRunner;
import org.structr.selenium.dsl.command.CommandFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.runner.side.SideFileRunner;

/**
 */
public class SeleniumTestRunner {

	private boolean interactive = false;
	private boolean headless    = true;
	private WebDriver driver    = null;
	private Context context     = null;
	private File suiteDir       = null;
	private int width           = 140;

	public SeleniumTestRunner(final Queue<String> args) throws IOException {
		init(args);

		Logger.getLogger("org.openqa.selenium.interactions.Actions").setLevel(Level.WARNING);
	}

	public static void main(final String[] args) {

		if (args.length < 1) {

			System.out.println("usage: SeleniumTestRunner [-b <browser-engine>] [-i] [-w <width>] <testsuite>");
			System.out.println("");
			System.out.println("parameters:");
			System.out.println("    <testsuite> - directory with test files");
			System.out.println("");
			System.out.println("options:");
			System.out.println("    -b  - browser engine [chrome|firefox]");
			System.out.println("    -i  - interactive mode");
			System.out.println("    -v  - visible mode (as opposed to headless): show test browser window");
			System.out.println("    -w  - display width of output (default: 140)");
			System.out.println("");

		} else {

			int exitCode = 0;

			try {

				final SeleniumTestRunner runner = new SeleniumTestRunner(new LinkedList<>(Arrays.asList(args)));

				// register shutdown hook
				Runtime.getRuntime().addShutdownHook(new ShutdownHook(runner));

				exitCode = runner.run();


			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Error: " + t.getMessage());
			}

			System.exit(exitCode);
		}
	}

	public int run() throws InterruptedException {

		if (interactive) {

			if (suiteDir != null && suiteDir.exists() && suiteDir.isFile()) {

				// try to load the given test file into the context
				context.setCurrentScript(new ScriptFile(suiteDir.getAbsolutePath()));
			}

			return new InteractiveTestRunner(context).run();

		} else {

			return runFromFile();
		}
	}

	public int runFromFile() {

		try {

			final SideFileRunner sideRunner     = new SideFileRunner(context);
			final ScriptFileRunner scriptRunner = new ScriptFileRunner(context);
			final Path root                     = suiteDir.toPath();

			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

					if (file.getFileName().toString().toLowerCase().endsWith(".side")) {

						sideRunner.runSideFile(root, file);

					} else {

						scriptRunner.runScriptFile(root, file);
					}

					return FileVisitResult.CONTINUE;
				}
			});

			final int tests  = context.getTests();
			final int passed = context.getPassed();
			final int failed = context.getFailed();
			final int errors = context.getErrors();

			System.out.println("Summary: " + tests + " test" + (tests == 1 ? "" : "s") + " executed, " + passed + "/" + (passed + failed) + " assertions passed, " + errors + " errors.");

			return failed + errors;

		} catch (IOException ioex) {

			System.out.println("Error: " + ioex.getMessage());
		}

		return 1;
	}

	public void done() {
		driver.quit();
	}

	// ----- private methods -----
	private void init(final Queue<String> args) throws IOException {

		String which = null;
		String dir   = null;

		while (!args.isEmpty()) {

			final String current = args.poll();
			if (current != null) {

				switch (current) {

					case "-b":
						which = getOrThrow(args.poll(), "Missing parameter for browser engine (-b).");
						break;

					case "-i":
						interactive = true;
						break;

					case "-v":
						headless = false;
						break;

					case "-w":
						width = Integer.valueOf(getOrThrow(args.poll(), "Missing parameter for width (-w)."));
						break;

					default:
						dir = current;
						break;
				}
			}
		}

		getOrThrow(which, "Browser engine not specified, please use \"-b chrome\" or \"-b firefox\".");

		if (!interactive) {

			// validate parameters
			getOrThrow(dir, "Missing test suite directory parameter.");
		}

		if (dir != null) {

			suiteDir = new File(dir);
			if (!suiteDir.exists()) {

				throw new IllegalArgumentException("Test suite " + dir + " does not exist.");
			}
		}

		switch (which.toLowerCase()) {

			case "firefox":
				initFirefox();
				break;

			case "chrome":
				initChrome();
				break;

			default:
				throw new IllegalArgumentException("Unsupported browser engine " + which + ".");
		}

		this.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		this.context = new Context(new CommandFactory(), driver, new Actions(driver), width);
	}

	private void initFirefox() {

		final FirefoxOptions firefoxOptions = new FirefoxOptions();

		firefoxOptions.setHeadless(headless);
		firefoxOptions.addPreference("webdriver.log.driver", "OFF");

		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");

		driver = new FirefoxDriver(firefoxOptions);

	}

	private void initChrome() {

		final ChromeOptions options = new ChromeOptions();
		options.setHeadless(headless);

		driver = new ChromeDriver(options);
	}

	private String getOrThrow(final String value, final String message) {

		if (value != null) {
			return value;
		}

		throw new IllegalArgumentException(message);
	}

	// ----- nested classes -----
	private static class ShutdownHook extends Thread {

		private SeleniumTestRunner runner = null;

		public ShutdownHook(final SeleniumTestRunner runner) {
			this.runner = runner;
		}

		@Override
		public void run() {
			runner.done();
		}
	}
}
