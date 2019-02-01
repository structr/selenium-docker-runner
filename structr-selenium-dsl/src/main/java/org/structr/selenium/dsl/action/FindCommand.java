/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.util.List;
import java.util.stream.Collectors;
import org.structr.selenium.dsl.selector.AbstractSelector;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class FindCommand extends AbstractSelectorAction {

	private AbstractSelector selector = null;

	public FindCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.abstractSelector(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {
	
		if (selector != null) {

			final List<String> result = collect(selector.get())
				.stream()
				.map(AbstractAction::getDescription)
				.collect(Collectors.toList());

			if (result.isEmpty()) {

				out.println("No results");

			} else {

				final int count = result.size();
				out.println(count, " result", count == 1 ? ":" : "s:");
				printPadded(out, result);
			}

			return true;
		}

		return false;
	}

	@Override
	public String getErrorMessage() {
		return "Assertion failure: " + selector.getElementMessage() + " not found.";
	}

	@Override
	public String usage() {
		return "find <selector> - lists all elements that match the given selector.";
	}

	@Override
	public boolean isRecordable() {
		return false;
	}
}
