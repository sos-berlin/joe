package com.sos.joe.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import com.sos.JSHelper.io.Files.JSFile;
import com.sos.JSHelper.io.Files.JSFolder;
import com.sos.i18n.I18NBase;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.interfaces.IDataChanged;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Events.ActionsDom;
import com.sos.joe.xml.jobdoc.DocumentationDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.resources.SOSProductionResource;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public abstract class DomParser extends I18NBase {

    protected static final String DEFAULT_ENCODING = "ISO-8859-1";
    public static final String conSchema_SCHEDULER_EDITOR_SCHEMA = "scheduler_editor_schema";
    public static final String conMessage_MAIN_LISTENER_OUTPUT_INVALID = "MainListener.outputInvalid";
    private Document _doc;
    private boolean _changed = false;
    private boolean _init = false;
    private IDataChanged _changedListener;
    private final HashMap<String, String[]> _orders = new HashMap<String, String[]>();
    private String _xslt;
    private String _filename = null;
    private long _lastModifiedFile = 0;
    private ClasspathResourceURIResolver objUriResolver = null;

    class ClasspathResourceURIResolver implements URIResolver {

        private String strBasePath = "";

        public ClasspathResourceURIResolver(String strPath) {
            super();
            strBasePath = strPath;
        }

        @Override
        public Source resolve(String href, String base) throws TransformerException {
            String strH = href;
            StreamSource objSS = null;
            if ("jobdoc.languages.xml".equalsIgnoreCase(strH)) {
                objSS = new StreamSource(this.getClass().getResourceAsStream("/com/sos/resources/xsl/" + href));
            } else {
                if (strH.startsWith("../")) {
                    strH = strH.substring(3);
                } else if (strH.startsWith("./")) {
                    strH = strH.substring(2);
                    if (strH.startsWith("param_")) {
                        strH = "params/" + strH;
                    }
                }
                JSFile objF = new JSFile(strBasePath, strH);
                if (objF.exists()) {
                    try {
                        objSS = new StreamSource(new FileInputStream(objF));
                    } catch (FileNotFoundException e) {
                    }
                } else {
                    JSFolder objFolder = new JSFolder(strBasePath);
                }
            }
            return objSS;
        }
    }

    public DomParser(String[] schemaTmp, String[] schemaResource, String xslt) {
        super("JOEMessages");
        _xslt = xslt;
    }

    protected void putDomOrder(String parentElement, String[] orderedElements) {
        _orders.put(parentElement, orderedElements);
    }

    public HashMap<String, String[]> getDomOrders() {
        return _orders;
    }

    public String getFileName() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
        readFileLastModified();
    }

    public String getFilename() {
        return _filename;
    }

    public void readFileLastModified() {
        if (_filename == null) {
            _lastModifiedFile = 0;
        } else {
            File f = new File(_filename);
            if (f.exists()) {
                _lastModifiedFile = f.lastModified();
            } else {
                _lastModifiedFile = 0;
            }
        }
    }

    public void setXSLT(String xslt) {
        _xslt = xslt;
    }

    public String getXSLT() {
        return _xslt;
    }

    public void setDataChangedListener(IDataChanged listener) {
        _changedListener = listener;
    }

    public IDataChanged getDataChangedListener() {
        return _changedListener;
    }

    public Element getRoot() {
        return _doc.getRootElement();
    }

    public Document getDoc() {
        return _doc;
    }

    public void setDoc(Document doc) {
        _doc = doc;
    }

    public Namespace getNamespace() {
        return getRoot().getNamespace();
    }

    public Namespace getNamespace(String name) {
        return getRoot().getNamespace(name);
    }

    public List getAdditinalNamespaces() {
        return getRoot().getAdditionalNamespaces();
    }

    protected String[] writeSchemaFile() throws IOException {
        String strT = "";
        try {
            String[] s = new String[1];
            s[0] = "";
            if (this instanceof ActionsDom) {
                s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.EVENT_SERVICE_XSD.getFullName()).toString();
            } else if (this instanceof DocumentationDom) {
                s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.JOB_DOC_XSD.getFullName()).toString();
            } else {
                if (this instanceof SchedulerDom) {
                    s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.SCHEDULER_XSD.getFullName()).toString();
                } else {
                    s[0] = "";
                }
            }
            return s;
        } catch (Exception e) {
            String strM =
                    this.getMsg("JOE-E-1100: Schema file '%1$s' not found, but required. check editor.properties.\n Execption is %2$s", strT,
                            e.toString());
            new ErrorLog(strM, e);
            throw new IOException("error in writeSchemaFile(). could not get schema " + strT + "\n" + e.toString());
        }
    }

    protected SAXBuilder getBuilder(boolean validate) throws IOException {
        SAXBuilder builder = new SAXBuilder(validate);
        if (validate) {
            builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", writeSchemaFile());
        }
        return builder;
    }

    public SAXBuilder getBuilder() throws IOException {
        return getBuilder(false);
    }

    public abstract boolean read(String filename) throws JDOMException, IOException;

    public abstract boolean read(String filename, boolean validate) throws JDOMException, IOException;

    public abstract boolean readString(String str, boolean validate) throws JDOMException, IOException;

    public abstract void write(String filename) throws IOException, JDOMException;

    public abstract String getXML(Element element) throws JDOMException;

    public void reorderDOM() {
        reorderDOM(getDoc().getRootElement());
    }

    protected void reorderDOM(Element element) {
        reorderDOM(element, null);
    }

    protected void reorderDOM(Element element, Namespace ns) {
        escape(element);
        String strT = "huhu";
        if (getDomOrders().containsKey(element.getName())) {
            String[] order = getDomOrders().get(element.getName());
            for (int i = 0; i < order.length; i++) {
                List list = new ArrayList(element.getChildren(order[i], ns));
                if (!list.isEmpty()) {
                    element.removeChildren(order[i], ns);
                    for (Iterator it2 = list.iterator(); it2.hasNext();) {
                        Element children = (Element) it2.next();
                        element.addContent(children);
                        reorderDOM(children, ns);
                    }
                }
            }
        } else {
            List children = element.getChildren();
            for (Iterator it = children.iterator(); it.hasNext();) {
                reorderDOM((Element) it.next(), ns);
            }
        }
    }

    public void deorderDOM() {
        deorderDOM(getDoc().getRootElement());
    }

    protected void deorderDOM(Element element) {
        deorderDOM(element, null);
    }

    protected void deorderDOM(Element element, Namespace ns) {
        deEscape(element);
        if (getDomOrders().containsKey(element.getName())) {
            String[] order = getDomOrders().get(element.getName());
            for (int i = 0; i < order.length; i++) {
                List list = new ArrayList(element.getChildren(order[i], ns));
                if (!list.isEmpty()) {
                    element.removeChildren(order[i], ns);
                    for (Iterator it2 = list.iterator(); it2.hasNext();) {
                        Element children = (Element) it2.next();
                        element.addContent(children);
                        deorderDOM(children, ns);
                    }
                }
            }
        } else {
            List children = element.getChildren();
            for (Iterator it = children.iterator(); it.hasNext();) {
                deorderDOM((Element) it.next(), ns);
            }
        }
    }

    public String transform(Element element, final String pstrFileName) {
        String strUserDir = System.getProperty("user.dir");
        String strPath = new File(pstrFileName).getParent();
        String strR = "";
        if (strPath != null) {
            System.setProperty("user.dir", strPath);
        }
        try {
            objUriResolver = new ClasspathResourceURIResolver(strPath);
            strR = transform(element);
        } catch (TransformerFactoryConfigurationError | TransformerException | IOException e) {
        } finally {
            System.setProperty("user.dir", strUserDir);
        }
        return strR;
    }

    public String transform(Element element) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        File tmp = null;
        try {
            Document doc = new Document((Element) element.clone());
            TransformErrorListener objEL = new TransformErrorListener();
            String strJobDocXslt = SOSProductionResource.JOB_DOC_XSLT.getFullName();
            StreamSource objSS = SOSProductionResource.JOB_DOC_XSLT.getAsStreamSource();
            Transformer transformer = TransformerFactory.newInstance().newTransformer(objSS);
            transformer.setErrorListener(objEL);
            JDOMSource in = new JDOMSource(doc);
            JDOMResult out = new JDOMResult();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(in, out);
            List result = out.getResult();
            tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());
            tmp.deleteOnExit();
            XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
            FileOutputStream objFOP = new FileOutputStream(tmp);
            OutputStreamWriter objOSW = new OutputStreamWriter(objFOP, Charset.forName("UTF-8"));
            outp.output(result, objOSW);
            return tmp.getAbsolutePath();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return "";
    }

    public boolean isChanged() {
        return _changed;
    }

    public void setChanged(boolean changed) {
        if (!_init) {
            _changed = changed;
            if (_changedListener != null) {
                _changedListener.dataChanged();
            }
        }
    }

    public void setInit(boolean init) {
        _init = init;
        _changed = false;
    }

    private void escape(Element e) {
        List listOfAtrributes = e.getAttributes();
        for (int i = 0; i < listOfAtrributes.size(); i++) {
            Attribute attr = (Attribute) listOfAtrributes.get(i);
            Attribute a = new Attribute(attr.getName(), Utils.escape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
            e.setAttribute(a);
        }
    }

    private void deEscape(Element e) {
        List listOfAtrributes = e.getAttributes();
        for (int i = 0; i < listOfAtrributes.size(); i++) {
            Attribute attr = (Attribute) listOfAtrributes.get(i);
            Attribute a = new Attribute(attr.getName(), Utils.deEscape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
            e.setAttribute(a);
        }
    }

    public boolean isInit() {
        return _init;
    }

    public long getLastModifiedFile() {
        return _lastModifiedFile;
    }

    public void setLastModifiedFile(long lastModifiedFile) {
        _lastModifiedFile = lastModifiedFile;
    }

}
