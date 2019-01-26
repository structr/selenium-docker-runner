/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.token;

import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.command.Command;
import org.structr.selenium.dsl.selector.AbstractSelector;

/**
 *
 */
public class KeywordToken extends Token<Object> {

	private String name = null;

	public KeywordToken(final String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Object resolve(final Context context, final TokenQueue args) {

		if (context.isDefined(name)) {

			return context.getDefined(name);
		}

		final CommandToken commandToken = resolveCommandToken(context, args);
		final Command command           = commandToken.resolve(context, args);

		if (command != null && command instanceof AbstractSelector) {

			return ((AbstractSelector)command).get();
		}

		throw referenceError(name);
	}

	@Override
	public String type() {
		return "keyword";
	}

	@Override
	public CommandToken resolveCommandToken(final Context context, final TokenQueue args) {
		return new CommandToken(name);
	}

	@Override
	public KeywordToken resolveKeywordToken(final Context context, final TokenQueue args) {
		return this;
	}

	@Override
	public StringToken resolveStringToken(final Context context, final TokenQueue args) {

		final Object value = resolve(context, args);
		if (value instanceof String) {

			return new StringToken((String)value);
		}

		throw typeError("string", value.getClass().getSimpleName());
	}

	@Override
	public NumberToken resolveNumberToken(final Context context, final TokenQueue args) {

		final Object value = resolve(context, args);
		if (value instanceof Integer) {

			return new NumberToken((Integer)value);
		}

		throw typeError("number", value.getClass().getSimpleName());
	}
}
