/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Completion;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class IdCommand extends ElementSelector {

	private String id = null;
	private int wait  = 0;

	public IdCommand() {
		super();
	}

	public IdCommand(final String id, final int waitTimeInSeconds) {

		super();

		this.wait = waitTimeInSeconds;
		this.id   = id;
	}

	@Override
	public void init(final TokenQueue args) {
		id = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "element with ID \"" + id + "\"";
	}

	@Override
	public WebElement get() {
		return id(id);
	}

	@Override
	public String usage() {
		return "id <id> - return the element with the given ID.";
	}

	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (part != null) {

			final WebDriver driver          = context.getWebDriver();
			final List<WebElement> elements = driver.findElements(By.xpath("//*[starts-with(@id,'" + part + "')]"));
			final List<Completion> results  = new LinkedList<>();

			if (elements != null) {

				for (final WebElement elem : elements) {

					final String tagName = elem.getTagName();
					final String id      = elem.getAttribute("id");

					results.add(new Completion(id + " (" + tagName + ")", id, false));
				}
			}

			return results;
		}

		return null;
	}
}
