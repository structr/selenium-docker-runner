/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class PutCommand extends AbstractAction {

	private String url  = null;
	private String data = null;

	public PutCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {

		url  = args.string(context, true);
		data = args.string(context, false);
	}

	@Override
	public boolean execute(final Terminal out) {

		configureRest();

		final Response response = RestAssured
			.given()
				.accept("application/json")
				.header("X-User",     username)
				.header("X-Password", password)
				.body(data)
			.then()
				.put(url)

			.andReturn();

 		if (out.isInteractive()) {
			out.println(response.getStatusLine());
		}

		return response.getStatusCode() == 200;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String usage() {
		return "put <url> <JSON data> - sends an HTTP PUT request with the given data to the given URL.";
	}
}
