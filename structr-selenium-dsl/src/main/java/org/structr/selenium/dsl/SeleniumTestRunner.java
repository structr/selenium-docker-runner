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
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.structr.selenium.dsl.common.FailsafeReportGenerator;
import org.structr.selenium.dsl.runner.script.ScriptFile;
import org.structr.selenium.dsl.runner.side.SideFileRunner;

/**
 */
public class SeleniumTestRunner {

	private boolean interactive = false;
	private boolean headless    = true;
	private WebDriver driver    = null;
	private Context context     = null;
	private String summaryPath  = null;
	private File suiteDir       = null;
	private int width           = 140;
	private int tests           = 0;
	private int passed          = 0;
	private int failed          = 0;
	private int errors          = 0;

	public SeleniumTestRunner(final Queue<String> args) throws IOException {
		init(args);

		Logger.getLogger("org.openqa.selenium.interactions.Actions").setLevel(Level.WARNING);
	}

	public static void main(final String[] args) {

		if (args.length < 1) {

			System.out.println("usage: SeleniumTestRunner [-e <browser-engine>] [-i] [-s <path>] [-u <baseUrl>] [-w <width>] <testsuite>");
			System.out.println("");
			System.out.println("parameters:");
			System.out.println("    <testsuite> - directory with test files");
			System.out.println("");
			System.out.println("options:");
			System.out.println("    -e  - browser engine [chrome|firefox]");
			System.out.println("    -i  - interactive mode");
			System.out.println("    -s  - write summary file to given path");
			System.out.println("    -u  - base URL for test browser to connect to");
			System.out.println("    -v  - visible mode (as opposed to headless): show test browser window");
			System.out.println("    -w  - display width of output (default: 140)");
			System.out.println("");

		} else {

			try {

				final SeleniumTestRunner runner = new SeleniumTestRunner(new LinkedList<>(Arrays.asList(args)));

				// register shutdown hook
				Runtime.getRuntime().addShutdownHook(new ShutdownHook(runner));

				runner.run();

			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Error: " + t.getMessage());
			}


		}
	}

	public void run() throws InterruptedException {

		if (interactive) {

			if (suiteDir != null && suiteDir.exists() && suiteDir.isFile()) {

				// try to load the given test file into the context
				context.setCurrentScript(new ScriptFile(suiteDir.getAbsolutePath()));
			}

			new InteractiveTestRunner(context).run();

		} else {

			runFromFile();
		}

		// write failsafe summary report
		if (summaryPath != null) {

			try {

				final FailsafeReportGenerator reportGenerator = new FailsafeReportGenerator(passed, errors, failed, 0);
				reportGenerator.writeReport(summaryPath);

			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void runFromFile() {

		try {

			final SideFileRunner sideRunner     = new SideFileRunner(context);
			final ScriptFileRunner scriptRunner = new ScriptFileRunner(context);
			final Path root                     = suiteDir.toPath();
			final Map<String, Path> sorted      = new TreeMap<>();

			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

					sorted.put(file.toString(), file);

					return FileVisitResult.CONTINUE;
				}
			});

			for (final Path path : sorted.values()) {

				final String fileName = path.getFileName().toString();

				if (fileName.endsWith(".side")) {

					sideRunner.runSideFile(root, path);

				} else if (fileName.endsWith(".test")) {

					scriptRunner.runScriptFile(root, path);
				}
			}

			tests  = context.getTests();
			passed = context.getPassed();
			failed = context.getFailed();
			errors = context.getErrors();

			System.out.println("Summary: " + tests + " test" + (tests == 1 ? "" : "s") + " executed, " + passed + "/" + (passed + failed) + " commands succeeded, " + errors + " errors.");

		} catch (IOException ioex) {

			System.out.println("Error: " + ioex.getMessage());
		}
	}

	public void done() {
		driver.quit();
	}

	// ----- private methods -----
	private void init(final Queue<String> args) throws IOException {

		String baseUrl = "http://localhost:11223";
		String which   = null;
		String dir     = null;

		while (!args.isEmpty()) {

			final String current = args.poll();
			if (current != null) {

				switch (current) {

					case "-e":
						which = getOrThrow(args.poll(), "Missing parameter for browser engine (-e).");
						break;

					case "-i":
						interactive = true;
						break;

					case "-s":
						summaryPath = getOrThrow(args.poll(), "Missing parameter for summary file (-s).");
						break;

					case "-u":
						baseUrl = getOrThrow(args.poll(), "Missing parameter for base URL (-u).");
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

		getOrThrow(which, "Browser engine not specified, please use \"-e chrome\" or \"-e firefox\".");

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
		this.context.setWorkDirectory(suiteDir);
		this.context.define("baseUrl", baseUrl);
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
