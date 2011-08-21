package sos.scheduler.editor.doc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.swt.SWT;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;

public class DocumentationDom extends DomParser {
	private final static String	conSVNVersion			= "$Id$";

	private static final String[]	_descriptionOrder	= { "job", "releases", "resources", "configuration", "documentation" };
	private static final String[]	_jobOrder			= { "script", "process", "monitor" };
	private static final String[]	_releaseOrder		= { "title", "author", "note", "changes" };
	private static final String[]	_resourcesOrder		= { "database", "memory", "space", "file" };
	private static final String[]	_configurationOrder	= { "note", "params", "payload", "settings" };
	private static final String[]	_paramsOrder		= { "note", "param" };
	private static final String[]	_payloadOrder		= { "note", "params", "document" };
	private static final String[]	_settingsOrder		= { "note", "profile", "connection" };
	private static final String[]	_profileOrder		= { "note", "section" };
	private static final String[]	_connectionOrder	= { "note", "application" };
	private static final String[]	_tmpFiles			= { "documentation_editor_schema", "xhtml_schema" };
	private static final String[]	_schemas			= { Options.getDocSchema(), Options.getXHTMLSchema() };

	public DocumentationDom() {
		super(_tmpFiles, _schemas, Options.getDocXSLT());

		putDomOrder("description", _descriptionOrder);
		putDomOrder("release", _releaseOrder);
		putDomOrder("job", _jobOrder);
		putDomOrder("resources", _resourcesOrder);
		putDomOrder("configuration", _configurationOrder);
		putDomOrder("params", _paramsOrder);
		putDomOrder("payload", _payloadOrder);
		putDomOrder("settings", _settingsOrder);
		putDomOrder("profile", _profileOrder);
		putDomOrder("connection", _connectionOrder);

		initDocumentation();
	}

	public void initDocumentation() {
		// open a template
		try {
			Document doc = getBuilder(false).build(getClass().getResource("/sos/scheduler/editor/documentation-template.xml"));
			//
			Element description = doc.getRootElement();
			if (description != null && description.getChild("releases", description.getNamespace()) != null) {
				Element release = description.getChild("releases", description.getNamespace()).getChild("release", description.getNamespace());
				if (release != null) {
					release.setAttribute("created", sos.util.SOSDate.getCurrentDateAsString("yyyy-MM-dd"));
					release.setAttribute("modified", sos.util.SOSDate.getCurrentDateAsString("yyyy-MM-dd"));
				}
			}

			//
			setDoc(doc);
			setChanged(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean read(String filename) throws JDOMException, IOException {
		return read(filename, Options.isDocValidate());
	}

	public boolean read(String filename, boolean validate) throws JDOMException, IOException {

		Document doc = getBuilder(validate).build(filename);

		if (!validate && (!doc.hasRootElement() || !doc.getRootElement().getName().equals("description")))
			return false;
		else
			if (!validate) {
				// try to avoid the worst
				Element description = doc.getRootElement();
				if (description.getChild("job", getNamespace()) == null)
					description.addContent(new Element("job", getNamespace()));
				if (description.getChild("releases", getNamespace()) == null)
					description.addContent(new Element("releases", getNamespace()));
				if (description.getChild("configuration", getNamespace()) == null)
					description.addContent(new Element("configuration", getNamespace()));
			}
		setDoc(doc);

		setChanged(false);
		setFilename(filename);
		return true;
	}

	public boolean readString(String str, boolean validate) throws JDOMException, IOException {

		StringReader sr = new StringReader(str);
		Document doc = getBuilder(validate).build(sr);

		if (!validate && (!doc.hasRootElement() || !doc.getRootElement().getName().equals("description")))
			return false;
		else
			if (!validate) {
				// try to avoid the worst
				Element description = doc.getRootElement();
				if (description.getChild("job", getNamespace()) == null)
					description.addContent(new Element("job", getNamespace()));
				if (description.getChild("releases", getNamespace()) == null)
					description.addContent(new Element("releases", getNamespace()));
				if (description.getChild("configuration", getNamespace()) == null)
					description.addContent(new Element("configuration", getNamespace()));

			}

		setDoc(doc);

		setChanged(false);
		// setFilename(filename);
		return true;
	}

	public void write(String filename) throws IOException, JDOMException {
		writeWithDom(filename);
	}

	public void writeWithDom(String filename) throws IOException, JDOMException {
		String encoding = Editor.DOCUMENTATION_ENCODING;
		if (encoding.equals(""))
			encoding = DEFAULT_ENCODING;
		reorderDOM(getDoc().getRootElement(), getNamespace());

		StringWriter stream = new StringWriter();
		// FileOutputStream stream = new FileOutputStream(new File(filename));

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(getDoc(), stream);
		stream.close();
		String s = stream.toString();

		try {
			s = s.replaceAll("<pre space=\"preserve\">", "<pre>");
			getBuilder(true).build(new StringReader(s));
		}
		catch (JDOMException e) {
			int res = MainWindow.message(Messages.getMsg(conMessage_MAIN_LISTENER_OUTPUT_INVALID, e.getMessage()), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			if (res == SWT.NO)
				return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		writer.write(s);
		writer.close();

		setFilename(filename);
		setChanged(false);
	}

	public void writeWithHandler(String filename) throws IOException, JDOMException {

		String encoding = Editor.DOCUMENTATION_ENCODING;
		if (encoding.equals(""))
			encoding = DEFAULT_ENCODING;
		reorderDOM(getDoc().getRootElement(), getNamespace());

		FormatHandler handler = new FormatHandler(this);
		handler.setEnconding(encoding);
		SAXOutputter saxo = new SAXOutputter(handler);
		saxo.output(getDoc());

		try {
			getBuilder(true).build(new StringReader(handler.getXML()));
		}
		catch (JDOMException e) {
			int res = MainWindow.message(Messages.getMsg(conMessage_MAIN_LISTENER_OUTPUT_INVALID, e.getMessage()), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			if (res == SWT.NO)
				return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		writer.write(handler.getXML());
		writer.close();

		setFilename(filename);
		setChanged(false);
	}

	public String getXML(Element element) throws JDOMException {
		String encoding = Editor.DOCUMENTATION_ENCODING;
		if (encoding.equals(""))
			encoding = DEFAULT_ENCODING;

		reorderDOM(element, getNamespace());

		FormatHandler handler = new FormatHandler(this);
		handler.setEnconding(encoding);
		SAXOutputter saxo = new SAXOutputter(handler);
		saxo.output(element);

		return handler.getXML();
	}

	public Element noteAsDom(String xml) throws JDOMException, IOException {
		xml = xml.replaceAll("<pre space=\"preserve\">", "<pre>");

		StringReader reader = new StringReader(xml);
		Document doc = getBuilder(false).build(reader);

		Element root = doc.getRootElement();
		doc.removeContent();
		doc.addContent(((Element) getRoot().clone()).addContent(root));

		Element div = doc.getRootElement().getChild("div", getNamespace("xhtml"));
		doc.getRootElement().removeContent();
		return div;
	}

	public String noteAsStr(Element element) {
		StringWriter stream = new StringWriter();
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			out.output(element.getContent(), stream);
			stream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String str = stream.toString().trim();
		if (str.startsWith("<div")) {
			str = str.replaceFirst("\\A<\\s*div\\s*xmlns\\s*=\\s*\"[a-zA-Z0-9/:\\.]*\"\\s*>\\s*", "");
			str = str.replaceFirst("\\s*<\\s*/\\s*div\\s*>\\Z", "");
		}
		str = str.replaceAll("<pre space=\"preserve\">", "<pre>");
		return str;

	}

}
