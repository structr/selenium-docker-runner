/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;
import org.structr.selenium.dsl.selector.AbstractSelector;

/**
 */
public class PrintCommand extends AbstractAction {

	private AbstractSelector selector = null;

	public PrintCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		selector = args.abstractSelector(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {

		final Object value = selector.get();

		if (value instanceof Map || value instanceof Iterable) {

			final Gson gson   = new GsonBuilder().setPrettyPrinting().create();
			final String json = gson.toJson(value);

			out.println(json);

		} else {

			out.println(value);
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "print <selector> - prints the result of the given selector.";
	}
}
