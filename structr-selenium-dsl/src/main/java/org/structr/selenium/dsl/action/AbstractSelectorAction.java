/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.runner.interactive.Completion;
import org.structr.selenium.dsl.selector.AbstractSelector;

/**
 */
public abstract class AbstractSelectorAction extends AbstractAction {

	public AbstractSelectorAction() {
		super();
	}

	// ----- private methods -----
	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (part != null) {

			final Map<String, Class<? extends Command>> commandMap = context.getCommandFactory().getCommands();
			final List<Completion> results                         = new LinkedList<>();

			for (final Entry<String, Class<? extends Command>> entry : commandMap.entrySet()) {

				final Class<? extends Command> type = entry.getValue();
				final String key                    = entry.getKey();

				if (AbstractSelector.class.isAssignableFrom(type) && key.startsWith(part)) {

					results.add(new Completion(key, true));
				}
			}

			return results;
		}

		return null;
	}
}
