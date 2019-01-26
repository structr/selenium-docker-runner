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
public abstract class Token<T> {

	public abstract String name();
	public abstract String type();

	public abstract T resolve(final Context context, final TokenQueue args);

	public abstract CommandToken resolveCommandToken(final Context context, final TokenQueue args);
	public abstract KeywordToken resolveKeywordToken(final Context context, final TokenQueue args);
	public abstract StringToken resolveStringToken(final Context context, final TokenQueue args);
	public abstract NumberToken resolveNumberToken(final Context context, final TokenQueue args);

	public AnyToken resolveAnyToken(Context context, final TokenQueue args) {
		return new AnyToken(resolve(context, args));
	}

	// ----- protected methods -----
	protected IllegalArgumentException typeError(final String expected)  {
		return typeError(expected, type());
	}

	protected IllegalArgumentException typeError(final String expected, final String actual)  {
		return new IllegalArgumentException("Type error: expected " + expected + ", got " + actual + ".");
	}

	protected IllegalArgumentException referenceError(final String token)  {
		return new IllegalArgumentException("Reference error: token " + token + " is not defined.");
	}
}
