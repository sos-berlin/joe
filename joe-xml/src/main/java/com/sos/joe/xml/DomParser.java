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
import com.sos.resources.SOSResourceFactory;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en") public abstract class DomParser extends I18NBase {
	public static final String				conSchema_SCHEDULER_EDITOR_SCHEMA		= "scheduler_editor_schema";
	public static final String				conMessage_MAIN_LISTENER_OUTPUT_INVALID	= "MainListener.outputInvalid";
	private final static String				conSVNVersion							= "$Id: DomParser.java 17406 2012-06-21 15:32:56Z ur $";
	protected static final String			DEFAULT_ENCODING						= "ISO-8859-1";
	private Document						_doc;
	private boolean							_changed								= false;
	private boolean							_init									= false;
	private IDataChanged					_changedListener;
	private final HashMap<String, String[]>	_orders									= new HashMap<String, String[]>();
	// private String[] _schemaTmpFile;
	// private String[] _schemaResource;
	private String							_xslt;
	private String							_filename								= null;
	/** wann wurde die Konfigurationsdatei zuletzt geändert. Dieser parameter soll dazu dienen, mitzubekommen, ob die 
	 * Konfigurationsdatei von einem anderen Process verändert wurde*/
	private long							_lastModifiedFile						= 0;
	class ClasspathResourceURIResolver implements URIResolver {
		private String	strBasePath	= "";

		public ClasspathResourceURIResolver(String strPath) {
			super();
			strBasePath = strPath;
		}

		@Override public Source resolve(String href, String base) throws TransformerException {
			String strH = href;
			StreamSource objSS = null;
			if (strH.equalsIgnoreCase("jobdoc.languages.xml")) {
				objSS = new StreamSource(this.getClass().getResourceAsStream("/com/sos/resources/xsl/" + href));
			}
			else {
				if (strH.startsWith("../")) {
					strH = strH.substring(3);
				}
				else {
					if (strH.startsWith("./")) {
						strH = strH.substring(2);
						if (strH.startsWith("param_")) {
							strH = "params/" + strH;
						}
					}
				}
				JSFile objF = new JSFile (strBasePath, strH);
				if (objF.exists()) {
					try {
						objSS = new StreamSource(new FileInputStream(objF));
					}
					catch (FileNotFoundException e) {
					}
				}
				else {
					JSFolder objFolder = new JSFolder(strBasePath);
					// hier jetzt die Datei im FolderBaum suchen und finden
				}
			}
			return objSS;
		}
	}

	public DomParser(String[] schemaTmp, String[] schemaResource, String xslt) {
		super("JOEMessages"); // , Options.getLanguage());
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
		// readFileMD5encrypt();
	}

	public String getFilename() {
		return _filename;
	}

	/**
	 * Liest den letzten Änderungszeitpunkt (in long) der Konfigurationsdatei.
	 * Wurde ausserhalb vom Editor etwas verändert?
	 * 
	 */
	public void readFileLastModified() {
		if (_filename == null)
			_lastModifiedFile = 0;
		File f = new File(_filename);
		if (f.exists())
			_lastModifiedFile = f.lastModified();
		else
			_lastModifiedFile = 0;
		// System.out.println("domparser= " + _lastModifiedFile);
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
			if (this instanceof ActionsDom)
				s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.EVENT_SERVICE_XSD.getFullName()).toString();
			else
				if (this instanceof DocumentationDom) {
					s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.JOB_DOC_XSD.getFullName()).toString();
				}
				else {
					if (this instanceof SchedulerDom) {
					    s[0] = this.getClass().getClassLoader().getResource(SOSProductionResource.SCHEDULER_XSD.getFullName()).toString();
					}else {
						s[0] = "";
					}
				}
			return s;
		}
		catch (Exception e) {
			String strM = this.getMsg("JOE-E-1100: Schema file '%1$s' not found, but required. check editor.properties.\n Execption is %2$s", strT,
					e.toString());
			try {
				e.printStackTrace(System.err);
				new ErrorLog(strM, e);
			}
			catch (Exception ee) {
			}
			throw new IOException("error in writeSchemaFile(). could not get schema " + strT + "\n" + e.toString());
		}
	}

	/* protected String[] writeSchemaFile_old() throws IOException {
	     ArrayList urls = new ArrayList();

	     for (int i = 0; i < _schemaTmpFile.length; i++) {
	         if (_schemaTmpFile[i] != null && !_schemaTmpFile[i].equals("") && _schemaResource[i] != null
	                 && !_schemaResource[i].equals("")) {

	             File tmp = File.createTempFile(_schemaTmpFile[i], ".xsd");
	             tmp.deleteOnExit();

	             InputStream in = getClass().getResourceAsStream(_schemaResource[i]);
	             FileOutputStream out = new FileOutputStream(tmp, true);

	             int c;
	             while ((c = in.read()) != -1)
	                 out.write(c);

	             in.close();
	             out.close();

	             urls.add(tmp.getAbsolutePath());
	         }
	     }

	     return (String[]) urls.toArray(new String[urls.size()]);
	 }

	*/
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
		// escape element Attributes
		escape(element);
		String strT = "huhu";
		// check if an order list exists for this element
		if (getDomOrders().containsKey(element.getName())) {
			// get children names in right order of this element
			String[] order = getDomOrders().get(element.getName());
			// iterate children names
			for (int i = 0; i < order.length; i++) {
				// get _new_ list of the children
				List list = new ArrayList(element.getChildren(order[i], ns));
				if (list.size() > 0) {
					// remove them all
					element.removeChildren(order[i], ns);
					// iterate children list
					for (Iterator it2 = list.iterator(); it2.hasNext();) {
						Element children = (Element) it2.next();
						// readd it at the end
						element.addContent(children);
						// recursion
						reorderDOM(children, ns);
					}
				}
			}
		}
		else {
			// reorder the children
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
		// escape element Attributes
		deEscape(element);
		// check if an order list exists for this element
		if (getDomOrders().containsKey(element.getName())) {
			// get children names in right order of this element
			String[] order = getDomOrders().get(element.getName());
			// iterate children names
			for (int i = 0; i < order.length; i++) {
				// get _new_ list of the children
				List list = new ArrayList(element.getChildren(order[i], ns));
				if (list.size() > 0) {
					// remove them all
					element.removeChildren(order[i], ns);
					// iterate children list
					for (Iterator it2 = list.iterator(); it2.hasNext();) {
						Element children = (Element) it2.next();
						// readd it at the end
						element.addContent(children);
						// recursion
						deorderDOM(children, ns);
					}
				}
			}
		}
		else {
			// reorder the children
			List children = element.getChildren();
			for (Iterator it = children.iterator(); it.hasNext();) {
				deorderDOM((Element) it.next(), ns);
			}
		}
	}

	public String transform(Element element, final String pstrFileName) {
		String strUserDir = System.getProperty("user.dir");
		// just to make it possible to access the xincluded files
		// TODO Option BaseDir (für alle inludes), damit auch Files aus anderen Verzeichnissen verarbeitet werden können
		String strPath = new File(pstrFileName).getParent();
		String strR = "";
		if (strPath != null) {
			System.setProperty("user.dir", strPath);  // hat keinen Effekt auf die Transformation ?
		}
		try {
			objUriResolver = new ClasspathResourceURIResolver(strPath);
			strR = transform(element);
		}
		catch (TransformerFactoryConfigurationError | TransformerException | IOException e) {
		}
		finally {
			System.setProperty("user.dir", strUserDir);
		}
		return strR;
	}
	private ClasspathResourceURIResolver	objUriResolver	= null;

	public String transform(Element element) throws TransformerFactoryConfigurationError, TransformerException, IOException {
		File tmp = null;
		try {
			Document doc = new Document((Element) element.clone());
			TransformErrorListener objEL = new TransformErrorListener();
			String strJobDocXslt = SOSProductionResource.JOB_DOC_XSLT.getFullName();
			//			String strJobDocXslt = "/com/sos/resources/xsl/scheduler_job_documentation_v1.1.xsl"; // Options.getXSLT();
			//			StreamSource objSS = new StreamSource(ResourceManager.getInputStream4Resource("/" + strJobDocXslt));
			StreamSource objSS = SOSProductionResource.JOB_DOC_XSLT.getAsStreamSource();
			Transformer transformer = TransformerFactory.newInstance().newTransformer(objSS);
			transformer.setErrorListener(objEL);
			if (objUriResolver != null) {
//				transformer.setURIResolver(objUriResolver);
			}
			JDOMSource in = new JDOMSource(doc);
			JDOMResult out = new JDOMResult();
			// see: http://docs.oracle.com/javase/7/docs/api/javax/xml/transform/OutputKeys.html
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
			if (objEL.isBufferNotEmpty() == true) {
//				new ErrorLog("Transformer reported some warnings/errors:\n\n" + objEL.getBuffer());
			}
			return tmp.getAbsolutePath();
		}
		catch (Exception e) {
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
			if (_changedListener != null)
				_changedListener.dataChanged();
		}
	}

	public void setInit(boolean init) {
		_init = init;
		_changed = false;
	}

	private void escape(Element e) {
		List listOfAtrributes = e.getAttributes();
		for (int i = 0; i < listOfAtrributes.size(); i++) {
			// System.out.println(listOfAtrributes.get(i));
			Attribute attr = (Attribute) listOfAtrributes.get(i);
			// System.out.println("name  : " + attr.getName());
			// System.out.println("value : " + attr.getValue());
			// Utils.setAttribute(attr.getName(), Utils.escape(attr.getValue()), e);
			// ok e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
			Attribute a = new Attribute(attr.getName(), Utils.escape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
			e.setAttribute(a);
			// e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
			// System.out.println("neue value  : " + e.getAttributeValue(attr.getName()));
			// System.out.println("*************************************");
		}
	}

	private void deEscape(Element e) {
		List listOfAtrributes = e.getAttributes();
		for (int i = 0; i < listOfAtrributes.size(); i++) {
			// System.out.println(listOfAtrributes.get(i));
			Attribute attr = (Attribute) listOfAtrributes.get(i);
			// System.out.println("name  : " + attr.getName());
			// System.out.println("value : " + attr.getValue());
			/*String newValue = attr.getValue();
			newValue = newValue.replaceAll("&quot;", "\"");
			newValue = newValue.replaceAll("&lt;", "<");
			newValue = newValue.replaceAll("&gt;", ">");
			newValue = newValue.replaceAll("&amp;", "&");
			*/
			// Utils.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()), e);
			// ok e.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()));
			Attribute a = new Attribute(attr.getName(), Utils.deEscape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
			e.setAttribute(a);
			// System.out.println("neue value  : " + newValue);
			// System.out.println("*************************************");
		}
	}

	/**
	 * @return the _init
	 */
	public boolean isInit() {
		return _init;
	}

	/**
	 * @return the _lastModifiedFile
	 */
	public long getLastModifiedFile() {
		return _lastModifiedFile;
	}

	/**
	 * @return the _lastModifiedFile
	 */
	public void setLastModifiedFile(long lastModifiedFile) {
		_lastModifiedFile = lastModifiedFile;
	}
}
