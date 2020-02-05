package com.sos.joe.xml.Events;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.DomParser;

public class ActionsDom extends DomParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsDom.class);
    private static final String[] ACTION = { "events", "commands" };
    public static final String conTemplate_ACTIONS_TEMPLATE_XML = "/sos/scheduler/editor/actions-template.xml";

    public ActionsDom() {
        super(new String[] { conSchema_SCHEDULER_EDITOR_SCHEMA }, new String[] { Options.getActionSchema() }, "");
        putDomOrder("action", ACTION);
        initActions();
    }

    public void initActions() {
        try {
            Document doc = getBuilder(false).build(getClass().getResource(conTemplate_ACTIONS_TEMPLATE_XML));
            setDoc(doc);
            setChanged(false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public boolean read(String filename) throws JDOMException, IOException {
        return read(filename, Options.isDocValidate());
    }

    public boolean read(String filename, boolean validate) throws JDOMException, IOException {
        Document doc = getBuilder(true).build(filename);
        setDoc(doc);
        setChanged(false);
        setFilename(filename);
        return true;
    }

    public boolean readString(String str, boolean validate) throws JDOMException, IOException {
        StringReader sr = new StringReader(str);
        Document doc = getBuilder(true).build(sr);
        if (!validate && (!doc.hasRootElement() || !"description".equals(doc.getRootElement().getName()))) {
            return false;
        } else if (!validate) {
            Element description = doc.getRootElement();
            if (description.getChild("job", getNamespace()) == null) {
                description.addContent(new Element("job", getNamespace()));
            }
            if (description.getChild("releases", getNamespace()) == null) {
                description.addContent(new Element("releases", getNamespace()));
            }
            if (description.getChild("configuration", getNamespace()) == null) {
                description.addContent(new Element("configuration", getNamespace()));
            }
        }
        setDoc(doc);
        setChanged(false);
        return true;
    }

    public void write(String filename) throws IOException, JDOMException {
        writeWithDom(filename);
    }

    public void writeWithDom(String filename) throws IOException, JDOMException {
        String encoding = JOEConstants.SCHEDULER_ENCODING;
        if ("".equals(encoding)) {
            encoding = DEFAULT_ENCODING;
        }
        reorderDOM(getDoc().getRootElement(), getNamespace());
        StringWriter stream = new StringWriter();
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(getDoc(), stream);
        stream.close();
        String s = stream.toString();
        try {
            s = s.replaceAll("<pre space=\"preserve\">", "<pre>");
            getBuilder(true).build(new StringReader(s));
        } catch (JDOMException e) {
            int res = ErrorLog.message(Messages.getMsg(conMessage_MAIN_LISTENER_OUTPUT_INVALID, e.getMessage()), SWT.ICON_WARNING | SWT.YES | SWT.NO);
            if (res == SWT.NO) {
                return;
            }
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);
        writer.write(s);
        writer.close();
        setFilename(filename);
        setChanged(false);
    }

    public void writeWithHandler(String filename) throws IOException, JDOMException {
        String encoding = JOEConstants.SCHEDULER_ENCODING;
        if ("".equals(encoding)) {
            encoding = DEFAULT_ENCODING;
        }
        reorderDOM(getDoc().getRootElement(), getNamespace());
        FormatHandler handler = new FormatHandler(this);
        handler.setEnconding(encoding);
        SAXOutputter saxo = new SAXOutputter(handler);
        saxo.output(getDoc());
        try {
            getBuilder(true).build(new StringReader(handler.getXML()));
        } catch (JDOMException e) {
            int res = ErrorLog.message(Messages.getMsg(conMessage_MAIN_LISTENER_OUTPUT_INVALID, e.getMessage()), SWT.ICON_WARNING | SWT.YES | SWT.NO);
            if (res == SWT.NO) {
                return;
            }
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);
        writer.write(handler.getXML());
        writer.close();
        setFilename(filename);
        setChanged(false);
    }

    public String getXML(Element element) throws JDOMException {
        String encoding = JOEConstants.SCHEDULER_ENCODING;
        if ("".equals(encoding)) {
            encoding = DEFAULT_ENCODING;
        }
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
        Document doc = getBuilder(true).build(reader);
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
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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