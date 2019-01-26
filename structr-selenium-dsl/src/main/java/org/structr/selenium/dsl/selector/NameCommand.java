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
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class NameCommand extends MultiElementSelector {

	private String name = null;

	public NameCommand() {
		super();
	}

	public NameCommand(final String name) {

		super();

		this.name = name;
	}

	@Override
	public void init(final TokenQueue args) {
		name = args.string(context, false);
	}

	@Override
	public void init(final SideTest test) {
		name = test.getTarget();
	}

	@Override
	public boolean execute(final Terminal out) {
		return false;
	}

	@Override
	public String getElementMessage() {
		return "elements with name \"" + name + "\"";
	}

	@Override
	public List<WebElement> get() {
		return name(name);
	}

	@Override
	public String usage() {
		return "name <name> - returns the elements with the given name.";
	}

	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (StringUtils.isNotBlank(part)) {

			final WebDriver driver          = context.getWebDriver();
			final List<WebElement> elements = driver.findElements(By.xpath("//*[starts-with(@name,'" + part + "')]"));
			final List<Completion> results  = new LinkedList<>();

			if (elements != null) {

				for (final WebElement elem : elements) {

					final String tagName = elem.getTagName();
					final String id      = elem.getAttribute("id");

					results.add(new Completion(id + " (" + tagName + ")", id));
				}
			}

			return results;
		}

		return null;
	}
}
