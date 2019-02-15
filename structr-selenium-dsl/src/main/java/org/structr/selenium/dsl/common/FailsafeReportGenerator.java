/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.structr.selenium.dsl.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 */
public class FailsafeReportGenerator {

	private int completed = 0;
	private int errors    = 0;
	private int failures  = 0;
	private int skipped   = 0;

	public FailsafeReportGenerator(final int completed, final int errors, final int failures, final int skipped) {

		this.completed = completed;
		this.errors    = errors;
		this.failures  = failures;
		this.skipped   = skipped;
	}

	public void writeReport(final String path) throws ParserConfigurationException {

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder        = factory.newDocumentBuilder();
		final Document document              = builder.newDocument();
		final Element rootElement            = document.createElement("failsafe-summary");

		rootElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:noNamespaceSchemaLocation", "https://maven.apache.org/surefire/maven-surefire-plugin/xsd/failsafe-summary.xsd");
		rootElement.setAttribute("timeout", "false"); // no timeout by definition
		rootElement.setAttribute("result",  Integer.toString(getResult()));

		document.setXmlStandalone(true);
		document.appendChild(rootElement);

		addElement(document, rootElement, "completed",      Integer.toString(completed));
		addElement(document, rootElement, "errors",         Integer.toString(errors));
		addElement(document, rootElement, "failures",       Integer.toString(failures));
		addElement(document, rootElement, "skipped",        Integer.toString(skipped));

		final Element failureMessage = document.createElement("failureMessage");
		failureMessage.setAttribute("xsi:nil", "true");

		final File file = new File(path);

		// create parent directories
		try { file.getParentFile().mkdirs(); } catch (Throwable t) {}

		// write output file
		try (final FileWriter writer = new FileWriter(file)) {

			// write file to stdout
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer               = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "8");

			final DOMSource source          = new DOMSource(document);
			final StreamResult streamResult = new StreamResult(writer);

			transformer.transform(source, streamResult);

			writer.flush();
			writer.close();

		} catch (IOException|TransformerException ioex) {
			ioex.printStackTrace();
		}
	}

	// ----- private methods -----
	private int getResult() {
		return 254;
	}

	private void addElement(final Document document, final Element parent, final String name, final String value) {

		final Element element = document.createElement(name);
		final Node content    = document.createTextNode(value);

		element.appendChild(content);
		parent.appendChild(element);
	}
}
