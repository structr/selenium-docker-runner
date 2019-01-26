/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.runner.script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Christian Morgner
 */
public class ScriptFile {

	private final List<String> lines = new LinkedList<>();
	private boolean modified         = false;
	private String path              = null;

	public ScriptFile() {
	}

	public ScriptFile(final String path) {

		this.path = path;

		load();
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public int size() {
		return lines.size();
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public void addCommand(final String line) {

		lines.add(line);
		modified = true;
	}

	public void removeCommand(final int index) {
		lines.remove(index);
		modified = true;
	}

	public boolean hasChanges() {
		return modified;
	}

	public void load() {

		try (final BufferedReader reader = new BufferedReader(new FileReader(path))) {

			reader.lines().forEach(lines::add);

		} catch (IOException ioex) {

			throw new IllegalArgumentException(ioex.getMessage());
		}
	}

	public void save() {

		if (path == null) {
			throw new IllegalArgumentException("Current script cannot be saved, no path set. Please use `save` to save the script, or discard " + size() + " commands.");
		}

		try (final PrintWriter writer = new PrintWriter(new FileWriter(path))) {

			lines.stream().forEach(writer::println);

			writer.flush();
			writer.close();

			modified = false;

		} catch (IOException ioex) {

			throw new IllegalArgumentException(ioex.getMessage());
		}
	}
}
