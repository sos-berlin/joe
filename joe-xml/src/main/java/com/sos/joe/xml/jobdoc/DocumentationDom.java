package com.sos.joe.xml.jobdoc;

import java.io.File;
import java.io.FileInputStream;
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

public class DocumentationDom extends DomParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationDom.class);
    private static final String[] DESCRIPTION_ORDER = { "job", "releases", "resources", "configuration", "documentation" };
    private static final String[] JOB_ORDER = { "script", "process", "monitor" };
    private static final String[] RELEASE_ORDER = { "title", "author", "note", "changes" };
    private static final String[] RESOURCES_ORDER = { "database", "memory", "space", "file" };
    private static final String[] CONFIGURATION_ORDER = { "note", "params", "payload", "settings" };
    private static final String[] PARAMS_ORDER = { "note", "param" };
    private static final String[] PAYLOAD_ORDER = { "note", "params", "document" };
    private static final String[] SETTINGS_ORDER = { "note", "profile", "connection" };
    private static final String[] PROFILE_ORDER = { "note", "section" };
    private static final String[] CONNECTION_ORDER = { "note", "application" };
    private static final String[] TMP_FILES = { "documentation_editor_schema", "xhtml_schema" };
    private static final String[] SCHEMAS = { Options.getDocSchema(), Options.getXHTMLSchema() };

    public DocumentationDom() {
        super(TMP_FILES, SCHEMAS, Options.getDocXSLT());
        putDomOrder("description", DESCRIPTION_ORDER);
        putDomOrder("release", RELEASE_ORDER);
        putDomOrder("job", JOB_ORDER);
        putDomOrder("resources", RESOURCES_ORDER);
        putDomOrder("configuration", CONFIGURATION_ORDER);
        putDomOrder("params", PARAMS_ORDER);
        putDomOrder("payload", PAYLOAD_ORDER);
        putDomOrder("settings", SETTINGS_ORDER);
        putDomOrder("profile", PROFILE_ORDER);
        putDomOrder("connection", CONNECTION_ORDER);
        initDocumentation();
    }

    public void initDocumentation() {
        try {
            Document doc = getBuilder(false).build(getClass().getResource("/sos/scheduler/editor/documentation-template.xml"));
            Element description = doc.getRootElement();
            if (description != null && description.getChild("releases", description.getNamespace()) != null) {
                Element release = description.getChild("releases", description.getNamespace()).getChild("release", description.getNamespace());
                if (release != null) {
                    release.setAttribute("created", sos.util.SOSDate.getCurrentDateAsString("yyyy-MM-dd"));
                    release.setAttribute("modified", sos.util.SOSDate.getCurrentDateAsString("yyyy-MM-dd"));
                }
            }
            setDoc(doc);
            setChanged(false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean read(String filename) throws JDOMException, IOException {
        return read(filename, Options.isDocValidate());
    }

    @Override
    public boolean read(String filename, boolean validate) throws JDOMException, IOException {
        FileInputStream objFIS = new FileInputStream(new File(filename));
        Document doc = getBuilder(validate).build(objFIS);
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
        setFilename(filename);
        return true;
    }

    @Override
    public boolean readString(String str, boolean validate) throws JDOMException, IOException {
        StringReader sr = new StringReader(str);
        Document doc = getBuilder(validate).build(sr);
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

    @Override
    public void write(String filename) throws IOException, JDOMException {
        writeWithDom(filename);
    }

    public void writeWithDom(String filename) throws IOException, JDOMException {
        writeFileWithDom(new File(filename));
        setFilename(filename);
        setChanged(false);
    }

    public void writeFileWithDom(File file) throws IOException, JDOMException {
        String encoding = JOEConstants.DOCUMENTATION_ENCODING;
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
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), encoding);
        writer.write(s);
        writer.close();
    }

    public void writeWithHandler(String filename) throws IOException, JDOMException {
        String encoding = JOEConstants.DOCUMENTATION_ENCODING;
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

    @Override
    public String getXML(Element element) throws JDOMException {
        String encoding = JOEConstants.DOCUMENTATION_ENCODING;
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
        try {
            xml = xml.replaceAll("<pre space=\"preserve\">", "<pre>");
            StringReader reader = new StringReader(xml);
            Document doc = getBuilder(false).build(reader);
            Element root = doc.getRootElement();
            doc.removeContent();
            doc.addContent(((Element) getRoot().clone()).addContent(root));
            Element div = doc.getRootElement().getChild("div", getNamespace("xhtml"));
            doc.getRootElement().removeContent();
            return div;
        } catch (Exception e) {
            new ErrorLog("noteAdDom", e);
        }
        return null;
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