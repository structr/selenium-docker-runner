/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Completion;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class ClassCommand extends MultiElementSelector {

	private String css = null;
	private int wait  = 0;

	public ClassCommand() {
		super();
	}

	public ClassCommand(final String css, final int waitTimeInSeconds) {

		super();

		this.wait = waitTimeInSeconds;
		this.css  = css;
	}

	@Override
	public void init(final TokenQueue args) {
		css = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "element with class \"" + css + "\"";
	}

	@Override
	public List<WebElement> get() {
		return className(css);
	}

	@Override
	public String usage() {
		return "class <class> - returns the element with the given class(es).";
	}

	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (StringUtils.isNotBlank(part)) {

			final WebDriver driver          = context.getWebDriver();
			final List<WebElement> elements = driver.findElements(By.xpath("//*[starts-with(@class,'" + part + "')]"));
			final List<Completion> results  = new LinkedList<>();

			if (elements != null) {

				for (final WebElement elem : elements) {

					final String tagName = elem.getTagName();
					final String css     = elem.getAttribute("class");

					results.add(new Completion(css + " (" + tagName + ")", css, false));
				}
			}

			return results;
		}

		return null;
	}
}
