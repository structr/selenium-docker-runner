/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.structr.selenium.dsl.runner.interactive.Completion;

/**
 */
public abstract class AbstractScriptAction extends AbstractAction {

	public AbstractScriptAction() {
		super();
	}

	// ----- private methods -----
	@Override
	public List<Completion> getAutocompleteResults(final String part) {

		if (StringUtils.isNotBlank(part)) {

			final List<Completion> results = new LinkedList<>();
			Path startDir                  = Paths.get(part);

			if (!Files.isDirectory(startDir)) {
				startDir = startDir.getParent();
			}

			try {

				final List<Completion> list = Files.find(startDir, 1, (t, u) -> {

					return t.toString().startsWith(part);

				}).map((t) -> {

					if (t != null) {

						final Path fileName = t.getFileName();
						if (fileName != null) {

							final String displayValue = fileName.toString();
							final String value        = t.toString();

							if (Files.isDirectory(t)) {
								return new Completion(displayValue, value + "/");
							}

							return new Completion(displayValue, value);
						}
					}

					return null;

				}).filter(e -> { return e != null; }).collect(Collectors.toList());

				// collect results
				results.addAll(list);

			} catch (IOException ioex) {

				ioex.printStackTrace();

			}

			return results;
		}

		return null;
	}
}
