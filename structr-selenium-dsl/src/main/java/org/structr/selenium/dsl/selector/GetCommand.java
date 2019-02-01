/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.selector;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import java.util.Map;
import org.structr.selenium.dsl.token.TokenQueue;
import org.structr.selenium.dsl.runner.interactive.Terminal;

/**
 */
public class GetCommand extends AbstractSelector {

	private String url  = null;
	private String path = null;

	public GetCommand() {
		super();
	}

	@Override
	public void init(final TokenQueue args) {
		url  = args.string(context, false);
		path = args.string(context, true);
	}

	@Override
	public boolean execute(final Terminal out) {
		return true;
	}

	@Override
	public Object get() {

		configureRest();

		final Response response = RestAssured
			.given()
				.accept("application/json")
				.header("X-User",     username)
				.header("X-Password", password)
			.then()
			.get(url)
			.andReturn();

		if (response.getStatusCode() == 200) {

			if (path != null) {

				return response.jsonPath().get(path);

			} else {

				return response.as(Map.class);
			}
		}

		throw new IllegalArgumentException(response.getStatusLine());
	}

	@Override
	public String usage() {
		return "get <url> <jsonPath> - return the REST object from the given URL, transformed with an optional jsonPath selector.";
	}

	@Override
	public String getElementMessage() {
		return "";
	}
}