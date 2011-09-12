package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import sos.ftp.profiles.FTPProfile;
import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.DetailDom;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;

import org.eclipse.swt.widgets.TableItem;


/**
 * DetailsListener.java
 * 
 * @author mo
 *
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

	private DetailDom     dom               = null;

	private boolean       hasError          = false;

	private Element       params_           = null;

	private boolean       isLifeElement     = false;

	private String        path              = null;


	public DetailsListener(String jobChainname_, 
			String state_, 
			String orderId_, 
			int type_, 
			DetailDom  dom_, 
			boolean isLifeElement_,
			String path_) {

		dom = dom_;
		if(dom != null)
			doc = dom.getDoc();

		jobChainname = jobChainname_;
		state = state_;
		orderId = orderId_;
		type = type_;
		isLifeElement = isLifeElement_;
		path = path_;


		init();

	}


	private void init() {
		noteEN = null;
		noteDE = null;
		params = null;

		parseDocuments();

	}

	public static void openFilePerFTP(String xmlFilename) {
		String file = "";
		try {

			org.eclipse.swt.custom.CTabItem currentTab  = MainWindow.getContainer().getCurrentTab();
			if(currentTab != null && currentTab.getData("ftp_title") != null && 
					currentTab.getData("ftp_title").toString().length()>0) {

				String remoteDir = currentTab.getData("ftp_remote_directory").toString();
				DomParser currdom = MainWindow.getSpecifiedDom();
				if(currdom == null)
					return;

				if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isDirectory()) {				
					remoteDir = remoteDir + "/" + new File(xmlFilename).getName();
				} else { //if( currdom instanceof SchedulerDom && ((SchedulerDom)currdom).isLifeElement()) {
					String p = new File(remoteDir).getParent();
					p = p == null ? "" : p + "/";
					remoteDir =  p + new File(xmlFilename).getName();
					remoteDir = remoteDir.replaceAll("\\\\", "/");
				}


				FTPProfile profile = (sos.ftp.profiles.FTPProfile)currentTab.getData("ftp_profile");



				profile.setLogText(null);
				//String a = profile.openFile(remoteDir, xmlFilename);
				profile.connect();
				//String parent = new File(remoteDir).getParent() != null ? new File(remoteDir).getParent() : ".";
				//if(profile.getList(parent).contains(new File(remoteDir))) {

				profile.getFile(remoteDir, xmlFilename);
				//long l = profile.getFile(remoteDir, xmlFilename);
				//}

				profile.disconnect();


			}
		} catch (Exception r) {
			try {
				MainWindow.message("could not open File: " + file + ", cause: " + r.toString(), SWT.ICON_WARNING);
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
			} catch(Exception ee) {
				//tu nichts
			}
		}
	}


	public void parseDocuments() {		
		String xmlPaths = "";
		try {


			if(isLifeElement || (MainWindow.getContainer().getCurrentTab().getData("ftp_title") != null && 
					MainWindow.getContainer().getCurrentTab().getData("ftp_title").toString().length()>0)) {								

				if(path != null && path.length() > 0) {
					File f = new File(path);
					if(f.isFile())
						xmlPaths = f.getParent();
					else
						xmlPaths = path; 
				} else { 
					xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHotFolder() ;
				}

				xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\")) ? xmlPaths : xmlPaths + "/" ;

			} else {
				if(path != null && path.length() > 0) {
					File f = new File(path);
					if(f.isFile())
						xmlPaths = f.getParent();
					else
						xmlPaths = path; 
				} else { 
					xmlPaths = sos.scheduler.editor.app.Options.getSchedulerData() ;
					xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths+ "config/" : xmlPaths.concat("/config/"));
				}
			}

			String _currOrderId = orderId != null && orderId.length()>0? "," + orderId : "";
			xmlFilename = new File(xmlPaths, jobChainname+ _currOrderId + ".config.xml").getCanonicalPath();


			if(_currOrderId != null && _currOrderId.length() > 0 ) {
				File jobChainConfig = new File(xmlPaths + jobChainname+  ".config.xml");
				if(jobChainConfig.exists() && !new File(xmlFilename).exists()) {
					//int c = MainWindow.message("Es gibt bereits eine Konfiguration für die Jobkette. Soll diese für den Auftrag übernommen werden?", SWT.ICON_QUESTION | SWT.YES | SWT.NO );
					int c = MainWindow.message("A configuration already exists for this job chain. Should this configuration be used for the order?", SWT.ICON_QUESTION | SWT.YES | SWT.NO );
					if(c == SWT.YES) {
						if(!sos.util.SOSFile.copyFile(jobChainConfig.getAbsolutePath(), xmlFilename))
							MainWindow.message("Could not copy configuration File?", SWT.ICON_QUESTION | SWT.YES | SWT.NO );							
					}
				}
			}

			//hier
			//if(xmlFilename.endsWith(".config.xml"))
			//	openFilePerFTP(xmlFilename); 

		} catch(Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			
			System.err.println("..error im DetailsListener.parseDocuments(): " + e.getMessage());
		}

		Element root        = null;					
		Element order       = null;  

		try {

			SAXBuilder builder = new SAXBuilder();

			if(doc == null) {

				File f = null;
				if(xmlFilename != null)
					f = new File(xmlFilename);
				if(f==null || !f.exists()) {					
					String xml = createConfigurationFile();

					doc = builder.build(new StringReader(xml));
					if(type == Editor.DETAILS) {
						if(f != null)
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

			if(params_ != null)
				params = params_.getChildren();
			else 
				params = new java.util.ArrayList();

		} catch(Exception e) {
			hasError = true;
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
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

	//mo
	private Element createNote(Element elem, String language) {
		Element n = new Element("note");
		Utils.setAttribute("language", language, n);
		elem.addContent(n);
		return n;
	}

	private Element createNewNoteElement(String text) {
		Element newNote = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(text));
			newNote = doc.getRootElement();
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);

		}
		return newNote;
	}

	private void setNoteText(Element note, String text) {

		Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));

		if(div == null) {						
			div = new Element("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
			note.addContent(div);
		}

		if(text.indexOf("<") == -1) {			
			div.setText(text);
		} else {
			//x-element wird temporär gebildet.
			Element newNote = createNewNoteElement("<x>" + text + "</x>");
			if(newNote != null){
				div.removeContent();
				div.addContent((List)newNote.cloneContent());
			} 
		}				
	}

	private String getNoteText(Element note) {
		String noteText = "";
		if(note != null) {
			Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
			if(div != null) {			
				noteText = Utils.noteAsStr(div);
			}		
		}
		return noteText;
	}

	public void fillParams(Table tableParams) {

		String name = "";
		String value = "";
		String text = "";
		for( int i=0; i<params.size(); i++ ){					
			Element param  = (Element)(params.get( i ));
			if(param.getName().equalsIgnoreCase("param")) {
				TableItem item = new TableItem(tableParams, SWT.NONE);
				name =  (param.getAttributeValue("name") != null ? param.getAttributeValue("name") : "");
				value = param.getAttributeValue("value")!= null? param.getAttributeValue("value"): "";	
				text =  param.getTextTrim();
				item.setText(0, name);
				item.setText(1, value);	
				item.setText(2, text);
				item.setData(param);	

			}	
		}

	}

	public String save() {		
		File f = new File(xmlFilename);
		try { 

			if(dom == null) {
				dom = new DetailDom();				
			}

			dom.writeElement(xmlFilename, doc);

		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("Could not save file, cause: " + e.toString(), SWT.ICON_WARNING);
			System.out.println("..error in DetailsListener.save. Could not save file " + e.getMessage());
		}
		return f.getAbsolutePath();
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
					if(params.size() <= i+j)
						return "";
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

	public String getParamsFileName() {
		if(params_ != null)
			return Utils.getAttributeValue("file", params_);
		else
			return "";
	}

	public void setParamsFileName(String filename) {
		if(params_ != null)
			Utils.setAttribute("file", filename, params_);


	}

	public void setParam( String name, String value, String note, String noteText, String language){
		try {
			boolean noNote = false;
			for(int i =0; i < params.size(); i++) {
				Element param = (Element)params.get(i);
				String pName = Utils.getAttributeValue("name", param);
				if(name.equalsIgnoreCase(pName)){
					Utils.setAttribute("value", value, param);
					if(noteText != null && noteText.trim().length() > 0) {
						while(!param.getContent().isEmpty()) {
							if(param.getContent().get(0) instanceof org.jdom.Text)
								param.getContent().remove(0);
						}
						org.jdom.Text txt = new org.jdom.Text(noteText);
						//org.jdom.CDATA txt = new org.jdom.CDATA(noteText);
						param.addContent(txt);
					}
					//if(params.size() > 1 || params.size() > i+1) {
					if(params.size() > i+1) {
						for(int j = 1; j < 3; j++ ){
							Element elNote = (Element)params.get(i+j);

							if(elNote.getName().equals("param")) {
								noNote = true;
								break;//die nächsten beiden Knoten der param Elemente sind nicht die note Elemente
							}
							if(elNote.getName().equalsIgnoreCase("note") && Utils.getAttributeValue("language", elNote).equalsIgnoreCase(language)) {
								setNoteText(elNote, note);
							}
						}
					} else {
						noNote = true;
					}
					if(noNote) {
						Element newNoteDE = new Element("note");
						Utils.setAttribute("language", "de", newNoteDE);
						Element newNoteEN = new Element("note");
						Utils.setAttribute("language", "en", newNoteEN);
						//Reihenfolge ist wichtig					
						params.add(params.indexOf(param) + 1, newNoteDE);
						params.add(params.indexOf(param) + 2, newNoteEN);
						return;
					}
					return;
				}
			}
			//neues Element
			//setParam(name, value, note, noteText, language);
			Element param = new Element("param");
			Utils.setAttribute("name", name, param);
			Utils.setAttribute("value", value, param);	
			if(noteText != null && noteText.trim().length() > 0) {
				//org.jdom.CDATA txt = new org.jdom.CDATA(noteText); 
				org.jdom.Text txt = new org.jdom.Text(noteText);
				param.addContent(txt);
			}
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
		} catch (Exception e) {
			MainWindow.message("Could not add Params cause: " + e.toString(), SWT.ICON_WARNING);
		}
	}

	/*
	 * Vom Wizzard generierte Parameter
	 */	
	public void refreshParams(Table table) {
		//params.clear();
		try {
			java.util.ArrayList list = new java.util.ArrayList();
			for(int i = 0 ; i < table.getItemCount();i++) {

				TableItem item = table.getItem(i);
				Element param = (Element)item.getData();

				if(param == null) {
					param = new Element("param");
					Utils.setAttribute("name", item.getText(0), param);
					Utils.setAttribute("value", item.getText(1) != null ? item.getText(1) : "", param);
					list.add(param);
					Element notede = new Element("note");
					Utils.setAttribute("language", "de", notede);
					String paramNoteDE = item.getData("parameter_description_de") != null ? item.getData("parameter_description_de").toString(): "";
					setNoteText(notede, paramNoteDE);
					list.add(notede);

					Element noteen = new Element("note");
					Utils.setAttribute("language", "en", noteen);
					String paramNoteEN = item.getData("parameter_description_en") != null ? item.getData("parameter_description_en").toString(): "";
					setNoteText(noteen, paramNoteEN);
					list.add(noteen);

					//params.set(i, param);
					//params.set(i+1, notede);
					//params.set(i+2, noteen);

				} else {
					list.add(param);
					int index = params.indexOf(param);
					if(params.size() > index +1) {
						Element notede = (Element)params.get(index+1);
						if(notede.getName().equals("note") && Utils.getAttributeValue("language", notede).equals("de"))
							list.add(notede);
					}
					if(params.size() > index +1) {
						Element noteen = (Element)params.get(index+2);
						if(noteen.getName().equals("note") && Utils.getAttributeValue("language", noteen).equals("en"))
							list.add(noteen);
					}

				}
				//list.add(param);
				/*list.add(param);

			if(params.size() > (params.indexOf(param) + 1)) {
				String paramNoteDE = item.getData("parameter_description_de") != null ? item.getData("parameter_description_de").toString(): "";
				Element noteDE = (Element)(params.get(params.indexOf(param) + 1)) ;
				if(noteDE.getName().equalsIgnoreCase("note")) {
					list.add(noteDE);
				} else if(noteDE.getChildren().size() == 0) {			
					//setNoteText(noteDE, paramNoteDE);

					Element note = new Element("note");
					Utils.setAttribute("language", "de", note);										
					setNoteText(note, paramNoteDE);
					list.add(note);
				}
			}
				 */
				/*if(params.size() > (params.indexOf(param) + 2)) {
				String paramNoteEN = item.getData("parameter_description_en") != null ? item.getData("parameter_description_en").toString(): "";
				Element noteEN = (Element)(params.get(params.indexOf(param) + 2)) ;
				if(!noteEN.getName().equalsIgnoreCase("note") && noteEN.getChildren().size() == 0 ) {			
					setNoteText(noteEN, paramNoteEN);				
				}
			}*/
				/*String name = item.getText(0);
			String value = item.getText(1) != null ? item.getText(1) : "";


			String paramNoteDE = item.getData("parameter_description_de") != null ? item.getData("parameter_description_de").toString(): "";
			String paramNoteEN = item.getData("parameter_description_en") != null ? item.getData("parameter_description_en").toString(): "";

			setParam(name, value, paramNoteDE, "", "de");
			setParam(name, value, paramNoteEN, "", "en");

				 */

			}

			params.removeAll(params);
			//params.addAll(list); 
			params.addAll((java.util.ArrayList)list.clone());
			table.removeAll();
			fillParams(table);
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("..error in DetailsListener.refreshParams(). : " + e.getMessage());
		}
	}

	/*public void addParam(String name, String value, String note, String noteText, String language) {
		//neues Element
		Element param = new Element("param");
		Utils.setAttribute("name", name, param);
		Utils.setAttribute("value", value, param);	
		if(noteText != null || noteText.trim().length() > 0) {
			//org.jdom.CDATA txt = new org.jdom.CDATA(noteText); 
			org.jdom.Text txt = new org.jdom.Text(noteText);
			param.addContent(txt);
		}
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
	}*/

	public void deleteParameter(Table table, int index) {

		String name = table.getItem(index).getText(0);
		for(int i = 0; i < params.size(); i++) {
			Element p = (Element)params.get(i);

			if(Utils.getAttributeValue("name", p).equalsIgnoreCase(name)) {
				params.remove(i);

				if(i == params.size())//i ist der letze Element un hat keinen node knoten
					break;

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
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
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
				List note = process.getChildren("note");
				if(note.size() == 0) {
					noteDE = createNote(process, "DE");
					noteEN = createNote(process, "EN");
				} else { 
					setGlobaleNote(note);
				}
				params_=process.getChild("params");
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

		this.state = newState;
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

	public Element getParentElement() {
		if(params_ != null && params_.getParentElement() != null)
			return this.params_.getParentElement();
		else
			return null;
	}


	/**
	 * Wird nur in Wizzard aufgerufen
	 * @return the params_
	 */
	public Element getParams() {
		if(params_ == null)
			params_ = new Element("params");
		return params_;
	}

	//selektierte Datensatz wird eine Zeile nach oben verschoben
	public void changeUp(Table table) {				

		int index = table.getSelectionIndex();

		if(index < 0)//nichts ist selektiert
			return;

		if(index == 0)//ist bereits ganz oben
			return;

		TableItem item = table.getSelection()[0];				
		String name = item.getText(0);
		String value = item.getText(1);	
		String text =  item.getText(2);
		Element param = (Element)item.getData();	

		table.remove(index);
		TableItem newItem = new TableItem(table, SWT.NONE, index -1);
		newItem.setText(0, name);
		newItem.setText(1, value);	
		newItem.setText(2, text);
		newItem.setData(param);	
		refreshParams(table);
		table.select(index-1);
	}


	//selektierte Datensatz wird eine Zeile unten  verschoben
	public void changeDown(Table table) {
		int index = table.getSelectionIndex();

		if(index < 0)//nichts ist selektiert
			return;

		if(index == table.getItemCount()-1)//ist bereits ganz oben
			return;

		TableItem item = table.getSelection()[0];				
		String name = item.getText(0);
		String value = item.getText(1);	
		String text =  item.getText(2);
		Element param = (Element)item.getData();	

		table.remove(index);
		TableItem newItem = new TableItem(table, SWT.NONE, index +1);
		newItem.setText(0, name);
		newItem.setText(1, value);	
		newItem.setText(2, text);
		newItem.setData(param);	
		refreshParams(table);
		table.select(index+1);
	}
	//////////////////////////////////////////////////////////ab hier Aufrufe von Job Chain



	/**
	 * Wenn der State vom Details sich ändert, dann wird ggf. auch in der 
	 * Details der state geändert.
	 */
	public static void changeDetailsState(String oldstate, String newstate, String jobchainname, SchedulerDom _dom) {
		try {
			DetailsListener detailListener = new DetailsListener(jobchainname, 
					oldstate, 
					null,  
					Editor.JOB_CHAINS, 
					null, 
					_dom.isLifeElement() || _dom.isDirectory(), 
					_dom.getFilename());

			//Document d = detailListener.getDoc();
			XPath x = XPath.newInstance("//process[@state='"+ oldstate + "']");				 
			List listOfElement = x.selectNodes(detailListener.getDoc());

			XPath xnew = XPath.newInstance("//process[@state='"+ newstate + "']");				 
			List listOfElementnew = xnew.selectNodes(detailListener.getDoc());
			if(listOfElementnew.isEmpty()) {
				if(!listOfElement.isEmpty()) {
					//System.out.println("hier ändern");
					Element process = (Element)listOfElement.get(0);
					process.setAttribute("state", newstate);
					detailListener.save();
					MainWindow.getContainer().getCurrentTab().setData("ftp_details_parameter_file", detailListener.getConfigurationFilename());
					MainWindow.saveFTP(new java.util.HashMap());
				}
			}
		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
		}
	}
	
	/**
	 * Wenn der Jobname vom Details sich ändert, dann wird auch in der Job Chain Node Parameter Datei der Attribut job chainname angepasst
	 */
	public static void changeDetailsJobChainname(String jobChainNewName, String jobchainName, SchedulerDom _dom) {
		try {
			DetailsListener detailListener = new DetailsListener(jobchainName, 
					null, 
					null,  
					Editor.JOB_CHAINS, 
					null, 
					_dom.isLifeElement() || _dom.isDirectory(), 
					_dom.getFilename());


			XPath x = XPath.newInstance("settings/job_chain[@name='"+ jobchainName +"']");				 
			List listOfElement = x.selectNodes(detailListener.getDoc());
			if(!listOfElement.isEmpty()) {
					Element jobchain = (Element)listOfElement.get(0);
					jobchain.setAttribute("name", jobChainNewName);
					detailListener.save();
					//MainWindow.getContainer().getCurrentTab().setData("ftp_details_parameter_file", detailListener.getConfigurationFilename());
					//MainWindow.saveFTP(new java.util.HashMap());

			}
		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
		}
	}

	public static void deleteDetailsState(String state, String jobchainname, SchedulerDom dom) {
		try {



			String parent = "";
			if(dom.isDirectory()) {
				parent = dom.getFilename()!= null	&& new File(dom.getFilename()).getParent() != null ? new File(dom.getFilename()).getParent() : Options.getSchedulerHotFolder() + "/config";  
			} else {
				parent = dom.getFilename()!= null	&& new File(dom.getFilename()).getParent() != null ? new File(dom.getFilename()).getParent() : Options.getSchedulerData() + "/config";
			}
			if(!new File(parent, jobchainname + ".config.xml").exists())
				return;




			if(state == null || state.length() == 0)
				return;

			DetailsListener detailListener = new DetailsListener(jobchainname, 
					state, 
					null,  
					Editor.JOB_CHAINS, 
					null, 
					dom.isLifeElement() || dom.isDirectory(), 
					dom.getFilename());


			XPath x = XPath.newInstance("//process[@state='"+ state + "']");				 
			List listOfElement = x.selectNodes(detailListener.getDoc());

			if(!listOfElement.isEmpty()) {

				Element process = (Element)listOfElement.get(0);
				process.detach();
				detailListener.save();			
			}
		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
		}
	}

	
	private static void addMonitoring(Element job, SchedulerDom dom) {
		if(job == null)
			return;
		Element monitor = new Element("monitor");
		Utils.setAttribute("name", "configuration_monitor", monitor);
		Utils.setAttribute("ordering", "0", monitor);

		Element script = new Element("script");
		Utils.setAttribute("java_class", "sos.scheduler.managed.configuration.ConfigurationOrderMonitor", script);
		Utils.setAttribute("language", "java", script);

		monitor.addContent(script);
		job.addContent(monitor);
		dom.setChanged(true);
		if(dom.isDirectory() || dom.isLifeElement()) 
			dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.MODIFY);

	}
	public static void addMonitoring2Job(String jobChainname, String state, SchedulerDom dom, ISchedulerUpdate  update) {
		try {

			//FAll 1: Es existiert eine globale Details Parameter. D.h. alle jobs in der Jobkette bekommen einen Monitoring			
			String sel = "//job_chain[@name='"+ jobChainname + "']/job_chain_node[@job!='']";
			if(state != null) {	
				//FAll 2: Es gibt eine Details Parameter. D.h. nur der eine job in der Jobkette mit der state bekommt einen Monitoring	
				sel = "//job_chain[@name='"+ jobChainname + "']/job_chain_node[@state='"+state+"']";

			}			
			XPath x = XPath.newInstance(sel);				 
			List listOfElement = x.selectNodes(dom.getDoc());

			if(!listOfElement.isEmpty()) {
				for(int i = 0; i < listOfElement.size(); i++) {

					Element jobChainNode = (Element)listOfElement.get(i);
					//jobname in der Jobkette ermitteln 
					String jobname = Utils.getAttributeValue("job", jobChainNode);
					String hotFolderfilename = "";
                    File hotFolderfile = null;
                    
					if ( new File(Options.getSchedulerHotFolder(), jobname + ".job.xml").exists()) {
					   hotFolderfile = new File(Options.getSchedulerHotFolder(), jobname + ".job.xml");
					}else {
					   hotFolderfile = new File(new File(dom.getFilename()).getParent(), new File(jobname).getName() + ".job.xml");
					}
					
					hotFolderfilename = hotFolderfile.getCanonicalPath();
					//Unterscheiden, ob Hot Folder Element. Wenn ja, dann Hot Folder Datei öffnen. Wenn das Hot Folder Element bereits offen ist, dann verändern
					List listOfElement2  = null;

					if(dom.isLifeElement() || new File(jobname).getParent() != null ) {

						if(!hotFolderfile.exists()) {
							openFilePerFTP(hotFolderfilename);
							if(!new File(hotFolderfilename).exists()) {
								sos.scheduler.editor.app.MainWindow.message("Could not add Monitoring Job, cause Hot Folder File " + hotFolderfilename + " not exist.", SWT.ICON_WARNING);
								continue;
							}
						}

						XPath x2 = null;
						//Es ist ein Hot Folder oder der Job ist woanders abgelegt
						sos.scheduler.editor.app.TabbedContainer tab = ((sos.scheduler.editor.app.TabbedContainer)MainWindow.getContainer());
						String pathFromHotFolderDirectory = new File(hotFolderfilename).getParent();
						if(tab.getFilelist() != null && 
								(tab.getFilelist().contains(hotFolderfilename) ||  
										tab.getFilelist().contains(pathFromHotFolderDirectory)		)) {
							//Hot Folder oder Hot Folder Element ist in einem Tabraiter offen oder							
							SchedulerForm form = null;

							if(tab.getFilelist().contains(hotFolderfilename)) {
								form = (SchedulerForm)tab.getEditor(hotFolderfilename);//hot folder element
								x2 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
							} else {
								form = (SchedulerForm)tab.getEditor(pathFromHotFolderDirectory);//hot folder
								x2 = XPath.newInstance("//job[@name='"+new File(jobname).getName()+"']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
							}

							SchedulerDom currdom = (SchedulerDom)form.getDom();

							listOfElement2 = x2.selectNodes(currdom.getDoc());
							if(listOfElement2.isEmpty()) {							
								XPath x3 = null;
								XPath x4 = null;
								if(tab.getFilelist().contains(hotFolderfilename)) {
									x3 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
									x4 = XPath.newInstance("//job");; 
								} else {
									x3 = XPath.newInstance("//job[@name='"+new File(jobname).getName()+"']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
									x4 = XPath.newInstance("//job[@name='"+new File(jobname).getName()+"']");
								}
								List listOfElement3  = x3.selectNodes(currdom.getDoc());
								if(listOfElement3.isEmpty()) {									
									List listOfElement4  = x4.selectNodes(currdom.getDoc());
									Element job = (Element)listOfElement4.get(0);
									addMonitoring(job, currdom);

									if(currdom.isLifeElement())
										form.getTree().setSelection(new org.eclipse.swt.widgets.TreeItem[] { form.getTree().getItem(0) });
									else if(currdom.isDirectory())
										form.selectTreeItem(SchedulerListener.JOBS , SchedulerListener.JOB + new File(jobname).getName());

									currdom.setChanged(true);
									if(form != null) {

										form.updateJob(job);
										form.updateJob();
										form.update();
									}
									currdom.setChanged(true);
									form.dataChanged();
									dom.setChanged(true);

									if(tab.getFilelist().contains(hotFolderfilename)) {
										form.dataChanged(tab.getFolderTab(hotFolderfilename));
									} else {
										form.dataChanged(tab.getFolderTab(pathFromHotFolderDirectory));
									}

								}
							}

						} else {
//							Hot Folder Element ist nicht offen in einem Tabraiter 
							SchedulerDom currDom = new SchedulerDom(SchedulerDom.LIVE_JOB);
							currDom.read(hotFolderfilename);

							if(x2==null)
								x2 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");

							listOfElement2 = x2.selectNodes(currDom.getDoc());
							if(listOfElement2.isEmpty()) {							
								XPath x3 = XPath.newInstance("//job");
								List listOfElement3  = x3.selectNodes(currDom.getDoc());
								if(!listOfElement3.isEmpty()) {
									Element job = (Element)listOfElement3.get(0); 
									addMonitoring(job, currDom);
									currDom.writeElement(currDom.getFilename(), currDom.getDoc());
									MainWindow.getContainer().getCurrentTab().setData("ftp_details_parameter_file", hotFolderfilename);
									MainWindow.saveFTP(new java.util.HashMap());
								}

							}
						}
					} else {
						XPath x2 = XPath.newInstance("//job[@name='"+ jobname + "']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
						listOfElement2 = x2.selectNodes(dom.getDoc());
						if(listOfElement2.isEmpty()) {							
							XPath x3 = XPath.newInstance("//jobs/job[@name='"+ jobname + "']");
							List listOfElement3  = x3.selectNodes(dom.getDoc());
							if(!listOfElement3.isEmpty()) {
								Element job = (Element)listOfElement3.get(0);
								addMonitoring(job, dom);

								if(update != null) {
									update.updateJobs();	
									//update.updateJob(jobname);
								}
								dom.setChanged(true);
							}
						}
					}
				}
			}


		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("Could not to be add Monitoring Job to Jobchain " + jobChainname + ", cause: " + e.getMessage(), SWT.ICON_ERROR);
		}
	}

	/**
	 * Ein neuer Job wurde den Jobkette hinzugefügt. Es wird jetzt überprüft, ob die Details Konfigurationsdatei
	 * globale Parameter hat. Wenn ja, dann wird diesem Job monitoring hinzugefügt  
	 * Details der state geändert.
	 */

	public static boolean existDetailsParameter(String state , 
			String jobchainname, 
			String jobname, 
			SchedulerDom dom, 
			ISchedulerUpdate  update, 
			boolean global,
			String orderid){
		try { 
			DetailsListener detailListener = new DetailsListener(jobchainname, 
					state, 
					orderid,  
					Editor.JOB_CHAINS, 
					null, 
					dom.isLifeElement() || dom.isDirectory(), 
					dom.getFilename());

			//Document d = detailListener.getDoc();
			XPath x = null;
			if(global)
				x = XPath.newInstance("//order/params/param");
			else
				x = XPath.newInstance("//process[@state='"+ state + "']/params/param");
			
			List listOfElement = x.selectNodes(detailListener.getDoc());

			return !listOfElement.isEmpty();



		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("error in DetailsListener.existDetailsParameter, cause: " + e.getMessage(), SWT.ICON_ERROR);
			return false;
		}
	}
	/**
	 * Ein neuer Job wurde den Jobkette hinzugefügt. Es wird jetzt überprüft, ob die Details Konfigurationsdatei
	 * globale Parameter hat. Wenn ja, dann wird diesem Job monitoring hinzugefügt  
	 * Details der state geändert.
	 */

	public static void checkDetailsParameter(String state , String jobchainname, String jobname, SchedulerDom dom, ISchedulerUpdate  update){
		try { 
			DetailsListener detailListener = new DetailsListener(jobchainname, 
					state, 
					null,  
					Editor.JOB_CHAINS, 
					null, 
					dom.isLifeElement() || dom.isDirectory(), 
					dom.getFilename());

			//Document d = detailListener.getDoc();
			XPath x = XPath.newInstance("//order/params/param");				 
			List listOfElement = x.selectNodes(detailListener.getDoc());

			if(!listOfElement.isEmpty()){


				String hotFolderfilename = new File(Options.getSchedulerHotFolder(), jobname + ".job.xml").getCanonicalPath();
				//Unterscheiden, ob Hot Folder Element. Wenn ja, dann Hot Folder Datei öffnen. Wenn der Hot Folder Element bereits offen ist, dann verändern
				List listOfElement2  = null;

				if(dom.isLifeElement() || new File(jobname).getParent() != null ) {

					if(!new File(hotFolderfilename).exists()) {
						sos.scheduler.editor.app.MainWindow.message("Could not add Monitoring Job, cause Hot Folder File " + hotFolderfilename + " not exist.", SWT.ICON_WARNING);
						return;
					}

					XPath x2 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
					//Es ist ein Hot Folder oder der Job ist woanders abgelegt
					sos.scheduler.editor.app.TabbedContainer tab = ((sos.scheduler.editor.app.TabbedContainer)MainWindow.getContainer());
					if(tab.getFilelist() != null && tab.getFilelist().contains(hotFolderfilename)) {
						//Hot Folder Element ist in einem Tabraiter offen	
						//org.eclipse.swt.custom.CTabItem f = tab.getFolderTab(hotFolderfilename);
						SchedulerForm form =(SchedulerForm)tab.getEditor(hotFolderfilename);
						SchedulerDom currdom = (SchedulerDom)form.getDom();

						listOfElement2 = x2.selectNodes(currdom.getDoc());
						if(listOfElement2.isEmpty()) {							
							XPath x3 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
							List listOfElement3  = x3.selectNodes(currdom.getDoc());
							if(listOfElement3.isEmpty()) {
								x3 = XPath.newInstance("//job");
								listOfElement3  = x3.selectNodes(currdom.getDoc());
								Element job = (Element)listOfElement3.get(0);
								addMonitoring(job, currdom);

								form.getTree().setSelection(new org.eclipse.swt.widgets.TreeItem[] { form.getTree().getItem(0) });

								currdom.setChanged(true);
								if(form != null) {

									form.updateJob();
									form.update();
								}
								currdom.setChanged(true);
								form.dataChanged();
								dom.setChanged(true);


								form.dataChanged(tab.getFolderTab(hotFolderfilename));

							}
						}

					} else {
//						Hot Folder Element ist nicht offen in einem Tabraiter 
						SchedulerDom currDom = new SchedulerDom(SchedulerDom.LIVE_JOB);
						currDom.read(hotFolderfilename);
						listOfElement2 = x2.selectNodes(currDom.getDoc());
						if(listOfElement2.isEmpty()) {							
							//XPath x3 = XPath.newInstance("//jobs/job[@name='"+ jobname + "']");
							//XPath x3 = XPath.newInstance("//job[@name='"+ jobname + "']");
							XPath x3 = XPath.newInstance("//job");
							List listOfElement3  = x3.selectNodes(currDom.getDoc());
							if(!listOfElement3.isEmpty()) {
								Element job = (Element)listOfElement3.get(0); 
								addMonitoring(job, currDom);
								currDom.writeElement(currDom.getFilename(), currDom.getDoc());
							}

						}
					}
				} else {
					XPath x2 = XPath.newInstance("//job[@name='"+ jobname + "']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor']");
					listOfElement2 = x2.selectNodes(dom.getDoc());
					if(listOfElement2.isEmpty()) {							
						XPath x3 = XPath.newInstance("//jobs/job[@name='"+ jobname + "']");
						List listOfElement3  = x3.selectNodes(dom.getDoc());
						if(!listOfElement3.isEmpty()) {
							Element job = (Element)listOfElement3.get(0);
							addMonitoring(job, dom);

							if(update != null)
								update.updateJobs();	
						}
					}
				}


			}
		} catch (Exception e)  {

			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
		}

	}
	

	

}
