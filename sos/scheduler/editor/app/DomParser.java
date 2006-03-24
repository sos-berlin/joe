package sos.scheduler.editor.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.swt.SWT;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;

import sos.scheduler.editor.listeners.MainListener;

public class DomParser {
	private static final String DEFAULT_ENCODING = "ISO-8859-1";

	private static final String[] CONFIG_ELEMENTS = { "base", "security",
			"process_classes", "script", "web_services", "holidays", "jobs",
			"job_chains" };

	private static final String[] JOB_ELEMENTS = { "description", "params",
			"script", "process", "monitor", "start_when_directory_changed",
			"delay_after_error", "delay_order_after_setback", "run_time" };

	private static final String[] RUNTIME_ELEMENTS = { "period", "date",
			"weekdays", "monthdays", "ultimos", "holidays" };

	private Document _doc;

	private boolean _changed = false;

	private boolean _init = false;

	private IUpdate _changedListener;

	private ArrayList _disabled = new ArrayList();

	private HashMap _orders;

	public DomParser() {
		_orders = new HashMap();
		_orders.put("config", CONFIG_ELEMENTS);
		_orders.put("job", JOB_ELEMENTS);
		_orders.put("run_time", RUNTIME_ELEMENTS);

		init();
	}

	public void init() {
		Element config = new Element("config");
		_doc = new Document(new Element("spooler").addContent(config));
		Element processClasses = new Element("process_classes");
		Element defaultClass = new Element("process_class");
		defaultClass.setAttribute("max_processes", "10");
		config.addContent(processClasses.addContent(defaultClass));
	}

	public void setDataChangedListener(IUpdate listener) {
		_changedListener = listener;
	}

	public Element getRoot() {
		return _doc.getRootElement();
	}

	private String writeSchemaFile() throws IOException {
		File tmp = File.createTempFile("scheduler_editor_schema", ".xsd");
		tmp.deleteOnExit();
		
		InputStream in = getClass().getResourceAsStream(Options.getSchema());
		FileWriter out = new FileWriter(tmp);
		
		int c;
		while((c = in.read()) != -1)
			out.write(c);
		
		in.close();
		out.close();

		return tmp.getAbsolutePath();
	}
	
	private SAXBuilder getBuilder(boolean validate) throws IOException {
		SAXBuilder builder = new SAXBuilder(validate);
		if (validate) {
			builder.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			builder.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					writeSchemaFile());
		}
		return builder;
	}

	// public void read(URL url) throws JDOMException, IOException {
	// SAXBuilder sb = new SAXBuilder();
	// _doc = sb.build(url);
	// }

	public boolean read(File file) throws JDOMException, IOException {
		return read(file, Options.isValidate());
	}

	public boolean read(File file, boolean validate) throws JDOMException,
			IOException {

		StringReader sr = new StringReader(readFile(file));
		Document doc = getBuilder(validate).build(sr);

		if (!validate
				&& (!doc.hasRootElement() || !doc.getRootElement().getName()
						.equals("spooler")))
			return false;

		_doc = doc;

		// set comments as attributes
		setComments(_doc.getContent());

		setChanged(false);
		return true;
	}

	private String readFile(File file) throws IOException {
		_disabled = new ArrayList();

		String encoding = DEFAULT_ENCODING;
		String line = null;
		StringBuffer sb = new StringBuffer();
		boolean disabled = false;

		Pattern p1 = Pattern.compile("<!--\\s*disabled\\s*=\\s*\"([^\"]+)\"");
		Pattern p2 = Pattern.compile("-->");
		Pattern p3 = Pattern.compile("<?xml.+encoding\\s*=\\s*\"([^\"]+)\"");
		Pattern p4 = Pattern.compile("<!--\\s*disabled");

		BufferedReader br = new BufferedReader(new FileReader(file));

		while ((line = br.readLine()) != null) {
			Matcher m3 = p3.matcher(line);
			Matcher m4 = p4.matcher(line);
			if (m3.find()) {
				encoding = m3.group(1);
			} else if (m4.find()) { // disable start
				Matcher m1 = p1.matcher(line);
				if (m1.find()) { // disabled job with name
					_disabled.add(m1.group(1));
					line = m1.replaceFirst("");
				} else { // disabled jobs tag
					line = m4.replaceFirst("");
				}
				disabled = true;
			} else if (disabled) { // disable end
				Matcher m2 = p2.matcher(line);
				m2 = p2.matcher(line);
				if (m2.find()) {
					line = m2.replaceFirst("");
					disabled = false;
				}
			}

			// System.out.println(line);

			sb.append(line);
		}

		String str = new String(sb.toString().getBytes(), encoding);

		return str;
	}

	public void write(String filename, MainListener listener)
			throws IOException, JDOMException {
		reorderDOM();

		FormatHandler handler = new FormatHandler(this);
		handler.setEnconding(DEFAULT_ENCODING);
		handler.setDisableJobs(isJobsDisabled());
		SAXOutputter saxo = new SAXOutputter(handler);
		saxo.output(_doc);

		try {
			getBuilder(true).build(new StringReader(handler.getXML()));
		} catch (JDOMException e) {
			int res = listener.message(Messages.getString(
					"MainListener.outputInvalid",
					new String[] { e.getMessage() }), SWT.ICON_WARNING
					| SWT.YES | SWT.NO);
			if (res == SWT.NO)
				return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(filename), DEFAULT_ENCODING);
		writer.write(handler.getXML());
		writer.close();

		// FileOutputStream stream = new FileOutputStream(new File(filename));
		// XMLOutputter out = new XMLOutputter(getFormat());
		// out.output(_doc, stream);
		// stream.close();

		setChanged(false);
	}

	public String getXML(Element element) throws JDOMException {
		reorderDOM(element);

		FormatHandler handler = new FormatHandler(this);
		handler.setEnconding(DEFAULT_ENCODING);
		handler.setDisableJobs(isJobsDisabled());
		SAXOutputter saxo = new SAXOutputter(handler);
		saxo.output(element);

		return handler.getXML();
	}

	public void reorderDOM() {
		reorderDOM(_doc.getRootElement());
	}

	private void reorderDOM(Element parent) {
		List list = parent.getChildren();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Element e = (Element) it.next();

			for (Iterator it2 = _orders.keySet().iterator(); it2.hasNext();) {
				String key = (String) it2.next();
				if (e.getName().equals(key)) {
					String[] order = (String[]) _orders.get(key);
					for (int i = 0; i < order.length; i++)
						appendElement(order[i], e);
				}
			}

			reorderDOM(e);
		}
	}

	private void appendElement(String element, Element parent) {
		List list = new ArrayList(parent.getChildren(element));
		if (list.size() > 0) {
			parent.removeChildren(element);
			for (Iterator it = list.iterator(); it.hasNext();)
				parent.addContent((Element) it.next());
		}
	}

	private void setComments(List content) {
		if (content != null) {
			String comment = null;
			for (Iterator it = content.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Comment) {
					comment = ((Comment) o).getText();
				} else if (o instanceof Element) {
					Element e = (Element) o;
					if (comment != null) { // set comment as value
						e.setAttribute("__comment__", comment.trim());
						comment = null;
					}

					setComments(e.getContent()); // recursion
				} else if (!(o instanceof Text)) {
					comment = null;
				}
			}
		}
	}

	public String transform(Element element)
			throws TransformerFactoryConfigurationError, TransformerException,
			IOException {
		Document doc = new Document((Element) element.clone());

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer(new StreamSource(Options.getXSLT()));
		JDOMSource in = new JDOMSource(doc);
		JDOMResult out = new JDOMResult();
		transformer.transform(in, out);

		List result = out.getResult();

		File tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options
				.getXSLTFileSuffix());
		tmp.deleteOnExit();

		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		outp.output(result, new FileWriter(tmp));

		return tmp.getAbsolutePath();
	}

	public boolean isJobDisabled(String name) {
		return _disabled.contains(name);
	}

	public boolean isJobsDisabled() {
		int disabledJobs = _disabled.size();
		Element jobs = getRoot().getChild("config").getChild("jobs");
		if (jobs == null)
			return false;
		int jobCnt = jobs.getChildren("job").size();
		return disabledJobs >= jobCnt;
	}

	public void setJobDisabled(String name, boolean disabled) {
		boolean contains = _disabled.contains(name);
		if (contains && !disabled) {
			_disabled.remove(name);
			setChanged(true);
		} else if (!contains && disabled) {
			_disabled.add(name);
			setChanged(true);
		}
	}

	public boolean isChanged() {
		return _changed;
	}

	public void setChanged(boolean changed) {
		if (!_init) {
			_changed = changed;
			_changedListener.dataChanged();
		}
	}

	public void setInit(boolean init) {
		_init = init;
	}
}
