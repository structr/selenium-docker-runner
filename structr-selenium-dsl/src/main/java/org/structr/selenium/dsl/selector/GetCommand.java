/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class GetCommand extends AbstractSelector<Response> {

	private final StringBuilder url = new StringBuilder();

	public GetCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		while (!args.isEmpty()) {
			url.append(args.string(context, false));
		}
	}

	@Override
	public boolean execute(final Terminal out) {
		return true;
	}

	@Override
	public Response get() {
		return RestAssured.get(url.toString());
	}

	@Override
	public String getElementMessage() {
		return "REST GET " + url.toString();
	}

	@Override
	public String usage() {
		return "get <url> - return the REST object under the given url - not yet supported.";
	}
}