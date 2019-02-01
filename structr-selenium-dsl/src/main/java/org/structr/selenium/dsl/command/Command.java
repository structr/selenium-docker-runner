package org.structr.selenium.dsl.command;

import com.jayway.restassured.RestAssured;
import org.structr.selenium.dsl.token.TokenQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.runner.interactive.Completion;
import org.structr.selenium.dsl.selector.IdCommand;
import org.structr.selenium.dsl.selector.NameCommand;
import org.structr.selenium.dsl.selector.SelectorCommand;
import org.structr.selenium.dsl.selector.XPathCommand;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public abstract class Command {

	private static int sequence = 0;
	private final int waitTime  = 3;
	protected Context context   = null;
	protected String baseUrl    = null;
	protected String username   = null;
	protected String password   = null;

	public abstract boolean execute(final Terminal out);
	public abstract void init(final TokenQueue args);

	public abstract String usage();

	public Command() {
	}

	public void init(final SideTest test) {
		 throw new UnsupportedOperationException("Not a SIDE command.");
	}

	public void setContext(final Context context) {
		this.context = context;
	}

	public List<Completion> getAutocompleteResults(final String part) {
		return null;
	}

	// ----- protected methods -----
	protected WebElement id(final String id) {
		return id(id, waitTime);
	}

	protected WebElement id(final String id, final int waitTimeInSeconds) {

		final int actualTime   = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver = context.getWebDriver();

		try {

			return new WebDriverWait(driver, actualTime).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));

		} catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> xpath(final String path) {
		return xpath(path, waitTime);
	}

	protected List<WebElement> xpath(final String path, final int waitTimeInSeconds) {

		final int actualTime     = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver   = context.getWebDriver();
		final WebDriverWait wait = new WebDriverWait(driver, actualTime);
		final By byXpath         = By.xpath(path);

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byXpath)); } catch (TimeoutException e) {}
		try { return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byXpath, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> selector(final String selector) {
		return selector(selector, waitTime);
	}

	protected List<WebElement> selector(final String selector, final int waitTimeInSeconds) {

		final int actualTime     = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver   = context.getWebDriver();
		final WebDriverWait wait = new WebDriverWait(driver, actualTime);
		final By bySelector      = By.cssSelector(selector);

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(bySelector)); } catch (TimeoutException e) {}
		try { return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(bySelector, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> name(final String name) {
		return name(name, waitTime);
	}

	protected List<WebElement> name(final String name, final int waitTimeInSeconds) {

		final int actualTime     = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver   = context.getWebDriver();
		final WebDriverWait wait = new WebDriverWait(driver, actualTime);
		final By byName          = By.name(name);

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byName)); } catch (TimeoutException e) {}
		try { return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byName, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> className(final String name) {
		return className(name, waitTime);
	}

	protected List<WebElement> className(final String name, final int waitTimeInSeconds) {

		final int actualTime     = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver   = context.getWebDriver();
		final WebDriverWait wait = new WebDriverWait(driver, actualTime);
		final By byClassName     = By.className(name);

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byClassName)); } catch (TimeoutException e) {}
		try { return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byClassName, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> text(final String text) {
		return text(text, waitTime);
	}

	protected List<WebElement> text(final String text, final int waitTimeInSeconds) {

		final int actualTime   = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver = context.getWebDriver();
		final WebDriverWait wait    = new WebDriverWait(driver, actualTime);
		final By byXpath            = By.xpath("//*[text()='" + text + "']");

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byXpath)); } catch (TimeoutException e) {}
		try { return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byXpath, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected List<WebElement> tagName(final String name) {
		return tagName(name, waitTime);
	}

	protected List<WebElement> tagName(final String name, final int waitTimeInSeconds) {

		final int actualTime     = waitTimeInSeconds > 0 ? waitTimeInSeconds : waitTime;
		final WebDriver driver   = context.getWebDriver();
		final WebDriverWait wait = new WebDriverWait(driver, actualTime);
		final By byTagName       = By.tagName(name);

		try { wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byTagName)); } catch (TimeoutException e) {}
		try {return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byTagName, 0)); } catch (TimeoutException e) {}

		return null;
	}

	protected AbstractSelector elementSelector(final String target) {
		return elementSelector(target, 0);
	}

	protected AbstractSelector elementSelector(final String target, final int waitTimeInSeconds) {

		final String[] parts = target.split("[=]+", 2);
		if (parts.length == 2) {

			AbstractSelector command = null;
			final String type       = parts[0];
			final String path       = parts[1];

			switch (type) {

				case "css":
					command = new SelectorCommand(path, waitTimeInSeconds);
					break;

				case "id":
					command = new IdCommand(path, waitTimeInSeconds);
					break;

				case "name":
					command = new NameCommand(path);
					break;

				case "xpath":
					command = new XPathCommand(path, waitTimeInSeconds);
					break;

				default:
					throw new IllegalArgumentException("Unknown target selector " + type);
			}

			if (command != null) {

				command.setContext(context);

				return command;
			}
		}

		throw new IllegalArgumentException("Unable to parse target selector \"" + target + "\"");
	}

	protected void delay() {
		delay(100);
	}

	protected void delay(final long milliseconds) {
		try { Thread.sleep(milliseconds); } catch (InterruptedException iex) {}
	}

	// interaction
	protected void hover(final WebElement element) {

		final Actions actions = context.getActions();

		actions.moveToElement(element, 0, 0).build().perform();
		delay();
	}

	protected void dragAndDrop(final WebElement source, final int dx, final int dy) {

		final Actions actions = context.getActions();

		actions.moveToElement(source, 0, 0).build().perform();
		delay();

		actions.clickAndHold().build().perform();
		delay();

		actions.moveByOffset(dx, dy).build().perform();
		delay();

		actions.release().build().perform();
		delay();
	}

	protected void input(final WebElement element, final String text) {

		element.click();
		element.clear();;
		element.sendKeys(text);
	}

	protected String join(final Iterable<String> src) {
		return join(src, " ");
	}

	protected String join(final Iterable<String> src, final String separator) {

		final StringBuilder buf  = new StringBuilder();
		final Iterator<String> i = src.iterator();

		while (i.hasNext()) {

			buf.append(i.next());
			if (i.hasNext()) {

				buf.append(separator);
			}
		}

		return buf.toString();
	}

	protected List<WebElement> collect(final Object value) {

		final List<WebElement> list = new LinkedList<>();

		collect(list, value);

		return list;
	}

	protected void collect(final List<WebElement> list, final Object value) {

		if (value instanceof WebElement) {

			final WebElement element  = (WebElement)value;
			final String display      = element.getCssValue("display");

			// do checks in two separate steps to avoid the overhead of
			// getSize if the element is not visible

			if (!"none".equals(display)) {

				final Dimension size = element.getSize();

				if (size.width > 0 && size.height > 0) {

					list.add((WebElement)value);
				}
			}

		} else if (value instanceof List) {

			for (final Object o : (List)value) {

				collect(list, o);
			}
		}
	}

	protected void configureRest() {

		baseUrl  = getStringOrThrow("baseUrl",  "token 'baseUrl' is not defined.");
		username = getStringOrThrow("username", "token 'username' is not defined.");
		password = getStringOrThrow("password", "token 'password' is not defined.");

		// configure RestAssured
		RestAssured.basePath = "/structr/rest";
		RestAssured.baseURI  = baseUrl;
	}

	protected String getStringOrThrow(final String key, final String error) {

		final String value = (String)context.getDefined(key);
		if (value == null) {

			throw new IllegalArgumentException(error);
		}

		return value;
	}

	// ----- error messages -----
	protected String getNonAssertionError(final String which) {
		return "Error in " + which + " command is not an assertion, please use \"find " + which + " ...\" instead.";
	}

	protected String getElementSelectorError(final String which, final String selector) {
		return "Error in " + which + ": invalid element selector " + selector + ", must be one of [id, class, link, button, selector].";
	}

	protected String getStringSelectorError(final String which, final String selector) {
		return "Error in " + which + ": invalid string selector " + selector + ", must be one of [title].";
	}

	protected String getUnknownCommandError(final String which, final String command) {
		return "Error in " + which + ": unknown command " + command + ".";
	}
}
