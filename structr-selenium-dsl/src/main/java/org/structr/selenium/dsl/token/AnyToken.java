/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.token;

import org.structr.selenium.dsl.common.Context;

/**
 *
 */
public class AnyToken extends Token {

	private Object value = null;

	public AnyToken(final Object value) {
		this.value = value;
	}

	@Override
	public Object resolve(final Context context, final TokenQueue args) {
		return value;
	}

	@Override
	public String name() {
		return value.toString();
	}

	@Override
	public String type() {
		return "any";
	}

	@Override
	public CommandToken resolveCommandToken(Context context, final TokenQueue args) {
		throw typeError("command");
	}

	@Override
	public KeywordToken resolveKeywordToken(Context context, final TokenQueue args) {
		throw typeError("keyword");
	}

	@Override
	public StringToken resolveStringToken(Context context, final TokenQueue args) {
		throw typeError("string");
	}

	@Override
	public NumberToken resolveNumberToken(Context context, final TokenQueue args) {
		throw typeError("number");
	}
}
