/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Completion;
import org.structr.selenium.dsl.runner.side.SideTest;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class OpenCommand extends AbstractAction {

	private static final List<String> autocompleteOptions = Arrays.asList("http://", "https://", "http://localhost");
	private final StringBuilder buf                       = new StringBuilder();

	public OpenCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		while (!args.isEmpty()) {

			buf.append(args.string(context, false));
		}
	}

	@Override
	public void init(final SideTest test) {

		final String url = test.getParent().getUrl();

		buf.append(url);
		buf.append(test.getTarget());
	}

	@Override
	public boolean execute(final Terminal out) {

		final WebDriver driver = context.getWebDriver();

		driver.get(buf.toString());

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "open <url> - opens the given url in the test browser.";
	}

	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (StringUtils.isNotBlank(part)) {

			final List<Completion> results = new LinkedList<>();

			for (final String option : autocompleteOptions) {

				if (option.startsWith(part)) {

					results.add(new Completion(option, false));
				}
			}

			return results;
		}

		return null;
	}
}
