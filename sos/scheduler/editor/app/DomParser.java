package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.swt.SWT;
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

public abstract class DomParser {
	
    protected static final String DEFAULT_ENCODING = "ISO-8859-1";

    private Document              _doc;

    private boolean               _changed         = false;

    private boolean               _init            = false;

    private IDataChanged          _changedListener;

    private HashMap               _orders          = new HashMap();

    private String[]              _schemaTmpFile;

    private String[]              _schemaResource;

    private String                _xslt;

    private String                _filename        = null;

 
    public DomParser(String[] schemaTmp, String[] schemaResource, String xslt) {
        _schemaTmpFile = schemaTmp;
        _schemaResource = schemaResource;
        _xslt = xslt;
    }


    protected void putDomOrder(String parentElement, String[] orderedElements) {
        _orders.put(parentElement, orderedElements);
    }


    protected HashMap getDomOrders() {
        return _orders;
    }


    public void setFilename(String filename) {
        _filename = filename;
    }


    public String getFilename() {
        return _filename;
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
		try {
			String[] s = new String[1];			
			s[0] = getClass().getResource(Options.getSchema()).toString();
			return s;
		} catch (Exception e){
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could get schema ", e);
			} catch(Exception ee) {
				//tu nichts
			}
			throw new IOException ("error in writeSchemaFile(). could get schema " + e.toString());
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
        	
            builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
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
    	//escape element Attributes
    	escape(element);
    	
    	// check if an order list exists for this element
    	if (getDomOrders().containsKey(element.getName())) {
    		// get children names in right order of this element
    		String[] order = (String[]) getDomOrders().get(element.getName());
    		
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
    	} else {
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
    	//escape element Attributes
    	deEscape(element);
    	
    	// check if an order list exists for this element
    	if (getDomOrders().containsKey(element.getName())) {
    		// get children names in right order of this element
    		String[] order = (String[]) getDomOrders().get(element.getName());
    		
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
    	} else {
    		// reorder the children
    		List children = element.getChildren();
    		for (Iterator it = children.iterator(); it.hasNext();) {
    			deorderDOM((Element) it.next(), ns);
    		}
    	}    	
    }
    public String transform(Element element) throws TransformerFactoryConfigurationError, TransformerException,
            IOException {
    	
    	File tmp = null;
    	try {
        Document doc = new Document((Element) element.clone());

      Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(Options.getXSLT()));
        //Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(getXSLT()));
        JDOMSource in = new JDOMSource(doc);
        JDOMResult out = new JDOMResult();
        transformer.transform(in, out);

        List result = out.getResult();

        tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());
        
        tmp.deleteOnExit();

        XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
        outp.output(result, new FileWriter(tmp));

        return tmp.getAbsolutePath();
    	} catch (Exception e) {
    		//System.err.println("error in DomParser.transform: " + (tmp != null ? tmp.getCanonicalPath() : "")+ e.getMessage());
    		try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
    		MainWindow.message(MainWindow.getSShell(), 
    				"error in DomParser.transform: " + (tmp != null ? tmp.getCanonicalPath() : "")+ e.getMessage(), 
    				SWT.ICON_WARNING | SWT.OK );
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
    }
    
    private void escape(Element e) {
    	List listOfAtrributes = e.getAttributes();
    	for(int i = 0; i< listOfAtrributes.size(); i++) {
    		//System.out.println(listOfAtrributes.get(i));
    		Attribute attr = (Attribute)listOfAtrributes.get(i);
    		//System.out.println("name  : " + attr.getName());
    		//System.out.println("value : " + attr.getValue());    		
    		//Utils.setAttribute(attr.getName(), Utils.escape(attr.getValue()), e);
    		
    		//ok e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
    		Attribute a = new Attribute(attr.getName(), Utils.escape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
    		e.setAttribute(a);
    		//e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
    		//System.out.println("neue value  : " + e.getAttributeValue(attr.getName()));
    		
    		//System.out.println("*************************************");
    		
    	}		
    }
    
    private void deEscape(Element e) {
    	List listOfAtrributes = e.getAttributes();
    	for(int i = 0; i< listOfAtrributes.size(); i++) {
    		//System.out.println(listOfAtrributes.get(i));
    		Attribute attr = (Attribute)listOfAtrributes.get(i);
    		//System.out.println("name  : " + attr.getName());
    		//System.out.println("value : " + attr.getValue());
    		/*String newValue = attr.getValue();
    		newValue = newValue.replaceAll("&quot;", "\"");
    		newValue = newValue.replaceAll("&lt;", "<");
    		newValue = newValue.replaceAll("&gt;", ">");
    		newValue = newValue.replaceAll("&amp;", "&");
    		*/
    		//Utils.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()), e);
    		
    		//ok e.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()));
    		
    		Attribute a = new Attribute(attr.getName(), Utils.deEscape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
    		e.setAttribute(a);
    		
    		//System.out.println("neue value  : " + newValue);
    		
    		//System.out.println("*************************************");
    		
    	}		
    }


	/**
	 * @return the _init
	 */
	public boolean isInit() {
		return _init;
	}
    
}
