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
public class NumberToken extends Token<Integer> {

	private Integer value = null;

	public NumberToken(final Integer value) {
		this.value = value;
	}

	@Override
	public Integer resolve(final Context contexti, final TokenQueue args) {
		return value;
	}

	@Override
	public String name() {
		return value.toString();
	}

	@Override
	public String type() {
		return "number";
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
		return this;
	}
}
