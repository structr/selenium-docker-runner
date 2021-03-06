/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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

		if (part != null) {

			final List<Completion> results = new LinkedList<>();
			final boolean isAbsolute       = part.startsWith("/");
			final Path baseDir             = getWorkDir();
			Path startDir                  = baseDir.resolve(Paths.get(part)).toAbsolutePath();

			if (!Files.isDirectory(startDir) && startDir.getParent() != null) {
				startDir = startDir.getParent();
			}

			try {

				final List<Completion> list = Files.find(startDir, 1, (t, u) -> {

					if (isAbsolute) {

						return t.toString().startsWith(part);

					} else {

						return baseDir.relativize(t).toString().startsWith(part);
					}

				}).map((t) -> {

					if (t != null) {

						final Path fileName = t.getFileName();
						if (fileName != null) {

							if (isAbsolute) {

								final String displayValue = fileName.toString();
								final String value        = t.toString();

								if (Files.isDirectory(t)) {
									return new Completion(displayValue, value + "/", false);
								}

								return new Completion(displayValue, value, false);

							} else {

								final String value = baseDir.relativize(t).toString();

								if (Files.isDirectory(t)) {
									return new Completion(value, value + "/", false);
								}

								return new Completion(value, value, false);

							}
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

	// ----- protected methods -----
	protected Path getWorkDir() {

		final File workDir = context.getWorkDirectory();
		if (workDir != null) {

			return workDir.toPath().toAbsolutePath();
		}

		return Paths.get("").toAbsolutePath();
	}
}
