/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.token;

import org.structr.selenium.dsl.common.Context;
import org.structr.selenium.dsl.command.Command;

/**
 *
 */
public class CommandToken extends Token<Command> {

	private String name = null;

	public CommandToken(final String name) {
		this.name = name;
	}

	@Override
	public Command resolve(final Context context, final TokenQueue args) {
		return context.getCommandFactory().forName(context, name, args);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String type() {
		return "command";
	}

	@Override
	public CommandToken resolveCommandToken(final Context context, final TokenQueue args) {
		return this;
	}

	@Override
	public KeywordToken resolveKeywordToken(final Context context, final TokenQueue args) {
		throw typeError("keyword");
	}

	@Override
	public StringToken resolveStringToken(final Context context, final TokenQueue args) {
		throw typeError("string");
	}

	@Override
	public NumberToken resolveNumberToken(final Context context, final TokenQueue args) {
		throw typeError("number");
	}
}
