package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.DetailDom;

import org.eclipse.swt.widgets.TableItem;
import java.io.FileWriter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

/**
 * DetailsListener.java
 * 
 * @author mo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class DetailsListener {
	
	private String        jobChainname      = null;
	
	private String        state             = null;
	
	private Element       noteEN            = null;
	
	private Element       noteDE            = null;
	
	private List          params            = null;
	
	private Element       application       = null;
	
	private Document      doc               = null;
	
	private String        xmlFilename       = null; 
	
	private String        orderId           = null;
	
	
	/** Wer hat ihn aufgerufen? */
	private int           type              = -1;
	
	
	/** Falls Konfigurationsdatei neu generiert wird */
	private String        encoding          = "ISO-8859-1";
	
	/** Falls Konfigurationsdatei neu generiert wird */
	//private String        stylesheet        = "../scheduler_configuration_documentation.xsl";
	
	private DetailDom     dom               = null;
	
	private boolean       hasError          = false;
	
	public DetailsListener(String jobChainname_) {
		jobChainname = jobChainname_;
		init();
		
	}
	
	public DetailsListener(String jobChainname_, String state_) {
		jobChainname = jobChainname_;
		state = state_;
		init();
		
	}
	
	public DetailsListener(String jobChainname_, String state_, String orderId_, int type_, DetailDom  dom_) {
		dom = dom_;
		if(dom != null)
			doc = dom.getDoc();
		jobChainname = jobChainname_;
		state = state_;
		orderId = orderId_;
		type = type_;
		init();
		
	}
	
	private void init() {
		noteEN = null;
		noteDE = null;
		params = null;
		
		parseDocuments();
		
	}
	
	public void parseDocuments() {
		
		
		String xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHome() ;
		xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? "config/" : xmlPaths.concat("/config/"));
		String _currOrderId = orderId != null && orderId.length()>0? "_" + orderId : "";
		
		xmlFilename = xmlPaths + "scheduler_" +jobChainname+ _currOrderId + ".config.xml";
		
		Element root        = null;			
		
		Element order       = null;  
		Element params_      = null;
		
		try {
			SAXBuilder builder = new SAXBuilder();
						
			if(doc == null) {
				File f = new File(xmlFilename);
				if(!f.exists()) {					
					String xml = createConfigurationFile();
					
					doc = builder.build(new StringReader(xml));
					if(type == Editor.DETAILS) {
						f.deleteOnExit();					
						dom.setDoc(doc);
					}
				} else {				
					doc = builder.build( new File( xmlFilename ) );
					if(type == Editor.DETAILS) {
						dom.setDoc(doc);
					}
				}
				
			}
			
			root = doc.getRootElement();
			
			application = root.getChild("job_chain");
			
			if (application == null) {
				application = root.getChild("application");
			}
			
			if (application == null) {
				MainWindow.message(new org.eclipse.swt.widgets.Shell(SWT.NONE), sos.scheduler.editor.app.Messages.getString("details.listener.missing_job_chain_node"), SWT.OK );
				System.out.println("error: " + sos.scheduler.editor.app.Messages.getString("details.listener.missing_job_chain_node"));
				hasError = true;
				return;
			}
			//globale Detail note			
			if(state==null || state.length() == 0) {
				List note = application.getChildren("note");
				setGlobaleNote(note);				
			}
			
			if(application != null)
				order =   application.getChild("order");
			
			
			if(order != null) {
				if(state!=null && state.length() > 0) {
//					Parameter der Job mit der state.. bestimmen
					params_ = getStateParams(order);			       				
				} else {
					//globale parameter
					params_ = order.getChild("params");		
				}
			}
			
			
			params = params_.getChildren();  
			
		} catch(Exception e) {
			System.err.println("..error im DetailsListener.parseDocuments(): " + e.getMessage());
		}
		
	}
	
	public String getNote(String language) {
		if(language == null)
			return getNoteText(noteEN);
		if(language.equalsIgnoreCase("de"))
			return getNoteText(noteDE);
		else 
			return getNoteText(noteEN);
		
	}
	
	
	public void setNote(String noteText, String language) {
		if(language.equalsIgnoreCase("de")) {
			if(noteDE == null) {
				noteDE=createNote(language);
			}
			setNoteText(noteDE, noteText);
			
		} else {
			if(noteEN == null) {
				noteEN=createNote(language);
			}
			setNoteText(noteEN, noteText);
			
		}
		
	}
	
	private Element createNote(String language) {
		Element n = new Element("note");
		Utils.setAttribute("language", language, n);
		application.addContent(n);
		return n;
	}
	
	private void setNoteText(Element note, String text) {
		Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
		Element p = null;
		if(div != null) {
			p = div.getChild("p", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
			if(p==null)
				p = new Element("p", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));		
		} else {
			div = new Element("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
			note.addContent(div);
			p = new Element("p", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
			div.addContent(p);
		}
		if(p != null)
			p.setText(text);
	}
	
	private String getNoteText(Element note) {
		String noteText = "";
		Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
		Element p = null;
		if(div != null)
			p = div.getChild("p", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
		if(p != null)
			noteText = p.getTextNormalize();
		return noteText;
	}
	
	public void fillParams(Table tableParams) {
		
		String name = "";
		String value = "";
		
		for( int i=0; i<params.size(); i++ ){					
			Element param  = (Element)(params.get( i ));
			if(param.getName().equalsIgnoreCase("param")) {
				TableItem item = new TableItem(tableParams, SWT.NONE);
				name =  (param.getAttributeValue("name") != null ? param.getAttributeValue("name") : "");
				value = param.getAttributeValue("value")!= null?param.getAttributeValue("value"): "";															
				item.setText(0, name);
				item.setText(1, value);					
				item.setData(param);//lösch mich test				
				
			}	
		}
		
	}
	
	public String save() {		
		File tmp = new File(xmlFilename);
		try {
			JDOMSource in = new JDOMSource(doc);
			Format format = Format.getPrettyFormat();
			format.setEncoding(encoding);
			XMLOutputter outp = new XMLOutputter(format);					
			outp.output(in.getDocument(), new FileWriter(tmp));
		} catch (Exception e) {
			System.out.println("..error in DetailsListener.save. Could not save file " + e.getMessage());
		}
		return tmp.getAbsolutePath();
	}
	
	/**
	 * liefert den Parametername
	 * @param paramname_language
	 */
	public String getParamNote(String name, String language) {
		for(int i=0; i < params.size(); i++) {
			Element param = (Element)params.get(i);
			if(param.getName().equals("param") && Utils.getAttributeValue("name", param).equalsIgnoreCase(name)) {
				for (int j = 1; j < 3; j++) {
					//nur zweimal durchlaufen, weil die nächsten beiden Elemente note Knoten sein können 
					Element note = (Element)params.get(i+j);
					if(note.getName().equals("param")) {
						break;//die nächsten beiden Knoten der param Elemente sind nicht die note Elemente
					}
					if(note.getName().equals("note") && Utils.getAttributeValue("language", note).equals(language)) {
						return getNoteText(note);
					}
				}
			}
		}
		return "";
		
	}
	
	public void setParam( String name, String value, String note, String language){
		for(int i =0; i < params.size(); i++) {
			Element param = (Element)params.get(i);
			String pName = Utils.getAttributeValue("name", param);
			if(name.equalsIgnoreCase(pName)){
				Utils.setAttribute("value", value, param);
				for(int j = 1; j < 3; j++ ){
					Element elNote = (Element)params.get(i+j);
					if(elNote.getName().equals("param")) {
						break;//die nächsten beiden Knoten der param Elemente sind nicht die note Elemente
					}
					if(elNote.getName().equalsIgnoreCase("note") && Utils.getAttributeValue("language", elNote).equalsIgnoreCase(language)) {
						setNoteText(elNote, note);
					}
				}
				return;
			}
		}
		//neues Element
		Element param = new Element("param");
		Utils.setAttribute("name", name, param);
		Utils.setAttribute("value", value, param);		
		Element newNoteDE = new Element("note");
		
		Utils.setAttribute("language", "de", newNoteDE);
		Element newNoteEN = new Element("note");
		Utils.setAttribute("language", "en", newNoteEN);
		//Reihenfolge ist wichtig
		params.add(param);
		params.add(newNoteDE);
		params.add(newNoteEN);
		if(language.equals("de"))
			setNoteText(newNoteDE, note);
		else
			setNoteText(newNoteEN, note);
		
	}
	
	public void deleteParameter(Table table, int index) {
		
		String name = table.getItem(index).getText(0);
		for(int i = 0; i < params.size(); i++) {
			Element p = (Element)params.get(i);
			if(Utils.getAttributeValue("name", p).equalsIgnoreCase(name)) {
				params.remove(i);
				
				Element pnde = (Element)params.get(i);
				if(pnde.getName().equals("note")) {
					params.remove(i);//note de
				} else {
					break;//das nächste Element ist param, daher abbrechen-> dh. es ex. kein engl. Note
				}
				
				Element pnen = (Element)params.get(i);
				if(pnen.getName().equals("note")) {
					params.remove(i);//note en
				}
				
			}
		}		
		
		table.remove(index);
		
	}	
	
	private void setGlobaleNote(List note) {
		for(int i=0; i < note.size(); i++) {
			Element n = (Element)note.get(i); 
			if(n.getAttributeValue("language").equals("de"))					
				noteDE = n;
			else if(n.getAttributeValue("language").equals("en"))					
				noteEN = n;
		}
	}
	
	private String  createConfigurationFile() {
		String xml = "<?xml version=\"1.0\" encoding=\""+ encoding + "\"?> ";
		
		try {
			if(Options.getDetailXSLT() != null && Options.getDetailXSLT().length() > 0) {
				xml = xml + "<?xml-stylesheet type=\"text/xsl\" href=\""+ Options.getDetailXSLT() + "\"?> ";
			}
			xml = xml + "<settings>" + 		  
			"  <job_chain name=\""+jobChainname+"\"> " + 
			"    <note language=\"de\"/> " + 
			"    <note language=\"en\"/> " + 
			"    <order> " +
			"      <params/> " +                       
			"    </order> " +
			"  </job_chain> " +
			"</settings> ";
			
			
		} catch (Exception e) {
			System.out.println("..error in DetailsListener.createConfigurationFile(). Could not create a new configuration file: " + e.getMessage());
		}
		return xml;
	}
	
	private Element getStateParams(Element order) {
		Element params_ = null;
		
		//Parameter der Job mit der state.. bestimmen
		List processList = order.getChildren("process");		
		for(int i = 0; i < processList.size(); i++) {						
			Element process = (Element)processList.get(i);
			if(Utils.getAttributeValue("state",process).equalsIgnoreCase(state)) {
				params_=process.getChild("params");
				List note = process.getChildren("note");
				setGlobaleNote(note);
			}			
		}
		
		if(params_ == null) {
			//configurationsdatei hat keinen process element mit dieser Zustand 
			Element process = new Element("process");
			Utils.setAttribute("state", state, process);
			
			Element notede = new Element("note");
			Utils.setAttribute("language", "de", notede);		   
			process.addContent(notede);
			
			Element noteen = new Element("note");
			Utils.setAttribute("language", "en", noteen);		   		   
			process.addContent(noteen);
			
			List note = process.getChildren("note");
			setGlobaleNote(note);
			
			params_ = new Element("params");
			process.addContent(params_);
			order.addContent(process);
			
			
		}
		return params_;
	}
	
	public String getConfigurationFilename() {
		return xmlFilename;
	}
	
	public void setJobChainname(String jobChainname) {
		this.jobChainname = jobChainname;
		if(application != null)
			Utils.setAttribute("name", jobChainname, application);
	}
	
	public Document getDoc() {
		return doc;
	}
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}	
	
	public void setType(int type_) {
		type = type_;
	}
	
	public void updateState(String oldState, String newState){
		Element order = null;
		
		this.state = state;
		if(application != null) {
			order =   application.getChild("order");			
		}
		if(order != null) {
			List pList = order.getChildren("process");
			for(int i = 0; i < pList.size(); i++) {
				Element process = (Element)pList.get(i);
				if(Utils.getAttributeValue("state", process).equalsIgnoreCase(oldState)) {
					Utils.setAttribute("state", newState, process);		
					state = newState;
				}
				
			}
		} 				
	}
	
	public void deleteState(String state){
		Element order = null;		
		
		if(application != null) {
			order =   application.getChild("order");			
		}
		if(order != null) {
			List pList = order.getChildren("process");
			for(int i = 0; i < pList.size(); i++) {
				Element process = (Element)pList.get(i);
				if(Utils.getAttributeValue("state", process).equalsIgnoreCase(state)) {		
					pList.remove(i);					
				}
				
			}
		} 				
	}
	
	public boolean isValidState(String state) {
		Element order = null;		
		
		if(application != null) {
			order =   application.getChild("order");			
		}
		if(order != null) {
			List pList = order.getChildren("process");
			for(int i = 0; i < pList.size(); i++) {
				Element process = (Element)pList.get(i);
				if(Utils.getAttributeValue("state", process).equalsIgnoreCase(state)) {		
					return false;
				}
				
			}
		} 		
		return true;
	}

	public boolean hasError() {
		return hasError;
	}
	
}
