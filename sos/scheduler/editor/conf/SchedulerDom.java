package sos.scheduler.editor.conf;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.output.SAXOutputter;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;


public class SchedulerDom extends DomParser {


	private static final String[]   CONFIG_ELEMENTS            = { "base", "params", "security", "cluster", "process_classes", "schedules", "locks", "script", "http_server",
		"holidays", "jobs", "job_chains", "orders", "commands" };

	private static final String[]   JOB_ELEMENTS               = { "description", "lock.use", "params", "environment", "script", "process", "monitor",
		"start_when_directory_changed", "delay_after_error", "delay_order_after_setback", "run_time", "commands" };        

	private static final String[]   RUNTIME_ELEMENTS           = { "period", "at", "date", "weekdays", "monthdays", "ultimos", "month" , "holidays"};        

	private static final String[]   JOBCHAIN_ELEMENTS          = { "file_order_source", "job_chain_node", "job_chain_node.job_chain", "job_chain_node.end", "file_order_sink"};

	private static final String[]   HOLIDAYS_ELEMENTS          = { "include" , "weekdays", "holiday"};

	private static final String[]   PARAMS_ELEMENTS            = { "param", "copy_params", "include"};

	private              ArrayList  _disabled                  = new ArrayList();

	private              HashMap    changedForDirectory        = new HashMap();    

	public static final  String     MODIFY                     = "modify";

	public static final  String     DELETE                     = "delete"; 

	public static final  String     NEW                        = "new";

	private static final String[]   CONFIG_ELEMENTS_DIRECTORY  = { "process_classes", "schedules", "locks", "jobs", "job_chains", "commands"};

	public static final  int        CONFIGURATION              = 0;

	private static final String[]   HTTP_SERVER                = { "web_service", "http.authentication", "http_directory"};       

	private              String     styleSheet                 = "";

	private static final String[]   COMMANDS_ELEMENTS          = { "add_order", "order", "start_job"};

	private static final String[]   ORDER_ELEMENTS             = { "params", "environment"};

	/** life Dateien: Schreibheschützte Dateien*/
	private              ArrayList  listOfReadOnlyFiles        = null;

	/** life Dateien: Wenn dateiname ungleich der Element Attribute Name ist, dann wird der Dateiname als Element name-Attribut gesetzt*/
	private              ArrayList   listOfChangeElementNames  = null;		

	public static final  int         DIRECTORY                 = 1;

	public static final  int         LIFE_JOB                  = 2;

	public static final  int         LIFE_JOB_CHAIN            = 3;

	public static final  int         LIFE_PROCESS_CLASS        = 4;

	public static final  int         LIFE_LOCK                 = 5;

	public static final  int         LIFE_ORDER                = 6;

	public static final  int         LIFE_ADD_ORDER            = 7;

	public static final  int         LIFE_SCHEDULE             = 8;

	private              boolean     isDirectory               = false;


	public SchedulerDom() {

		super(new String[] { "scheduler_editor_schema" }, new String[] { Options.getSchema() }, Options.getXSLT());
		putDomOrder("config", CONFIG_ELEMENTS); 
		putDomOrder("job", JOB_ELEMENTS);
		putDomOrder("run_time", RUNTIME_ELEMENTS);
		putDomOrder("job_chain", JOBCHAIN_ELEMENTS);
		putDomOrder("http_server", HTTP_SERVER);
		putDomOrder("commands", COMMANDS_ELEMENTS);              
		putDomOrder("start_job", ORDER_ELEMENTS);
		putDomOrder("holidays", HOLIDAYS_ELEMENTS);
		putDomOrder("params", PARAMS_ELEMENTS);
		putDomOrder("schedule", RUNTIME_ELEMENTS);
		initScheduler();

	}

	public SchedulerDom(int type) {

		super(new String[] { "scheduler_editor_schema" }, new String[] { Options.getSchema() }, Options.getXSLT());

		if (type == DIRECTORY) {
			putDomOrder("config", CONFIG_ELEMENTS_DIRECTORY);
			putDomOrder("job", JOB_ELEMENTS);
			putDomOrder("run_time", RUNTIME_ELEMENTS);
			putDomOrder("job_chain", JOBCHAIN_ELEMENTS);			
			putDomOrder("commands", COMMANDS_ELEMENTS);
			putDomOrder("params", PARAMS_ELEMENTS);
			putDomOrder("schedule", RUNTIME_ELEMENTS);
			putDomOrder("holidays", HOLIDAYS_ELEMENTS);
			isDirectory = true;
			initScheduler();
		} else if(type==LIFE_JOB) {
			putDomOrder("job", JOB_ELEMENTS);
			putDomOrder("run_time", RUNTIME_ELEMENTS);
			putDomOrder("params", PARAMS_ELEMENTS);
			putDomOrder("holidays", HOLIDAYS_ELEMENTS);
			initScheduler(type);
		} else if(type==LIFE_JOB_CHAIN) {
			//putDomOrder("job_chain", CONFIG_ELEMENTS_DIRECTORY);
			putDomOrder("job_chain", JOBCHAIN_ELEMENTS);			
			initScheduler(type);
		} else if(type==LIFE_ORDER) {
			putDomOrder("commands", COMMANDS_ELEMENTS);
			putDomOrder("run_time", RUNTIME_ELEMENTS);
			putDomOrder("params", PARAMS_ELEMENTS);
			putDomOrder("holidays", HOLIDAYS_ELEMENTS);
			initScheduler(type);
		} else if(type==LIFE_PROCESS_CLASS) {
			putDomOrder("config", new String[]{ "process_classes" });
			initScheduler(type);
		} else if(type==LIFE_LOCK) {
			putDomOrder("config", new String[]{"locks"});
			initScheduler(type);
		} else if(type==LIFE_SCHEDULE) {
			putDomOrder("config", new String[]{"schedules"});
			putDomOrder("run_time", RUNTIME_ELEMENTS);
			putDomOrder("holidays", HOLIDAYS_ELEMENTS);
			initScheduler(type);
		} else {
			new SchedulerDom();
			initScheduler();
		}


	}

	public void initScheduler() {
		Element config = new Element("config");
		setDoc(new Document(new Element("spooler").addContent(config)));
		Element processClasses = new Element("process_classes");
		Element defaultClass = new Element("process_class");
		defaultClass.setAttribute("max_processes", "10");
		config.addContent(processClasses.addContent(defaultClass));
	}


	public void initScheduler(int type) {
		if(type==LIFE_ORDER) {
			Element order = new Element("order");
			order.setAttribute("job_chain", "job_chain1");
			order.setAttribute("id", "id");
			setDoc(new Document(order));
			//setFilename("job1.job.xml");
		}else {
			Element elem = null;

			if(type==LIFE_JOB) {
				elem = new Element("job");
				elem.setAttribute("name", "job1");
			} else if(type==LIFE_JOB_CHAIN) {
				elem = new Element("job_chain");
				elem.setAttribute("name", "job_chain1");
			} else if(type==LIFE_PROCESS_CLASS) {
				elem = new Element("process_class");
				elem.setAttribute("name", "process_class1");
			} else if(type==LIFE_LOCK) {
				elem = new Element("lock");
				elem.setAttribute("name", "lock1");
			} else if(type==LIFE_ORDER) {
				elem = new Element("job_chain");
				elem.setAttribute("name", "job_chain1");
			} else if(type==LIFE_SCHEDULE) {
				elem = new Element("schedule");
				elem.setAttribute("name", "schedule1");
			} 

			setDoc(new Document(elem));

		} 

	}

	public boolean read(String filename) throws JDOMException, IOException {    	
		return read(filename, Options.isValidate());
	}


	public boolean read(String filename, boolean validate) throws JDOMException, IOException {

		StringReader sr = new StringReader(readFile(filename));

		Document doc = getBuilder(validate).build(sr);        


		sr.close();
		//doc.getRootElement().getChild("config").getChild("jobs").getChild("job").getChild("params").getChild("param")
		if(doc.getDescendants() != null) {
			Iterator descendants = doc.getDescendants();
			findStyleSheet(descendants);
		}


		//if (!validate && (!doc.hasRootElement() || !doc.getRootElement().getName().equals("spooler")))
		if (!validate && !doc.hasRootElement())
			return false;

		setDoc(doc);

		// set comments as attributes
		setComments(getDoc().getContent());

		setChanged(false);
		setFilename(filename);
		return true;
	}

	public boolean readString(String str, boolean validate) throws JDOMException, IOException {

		StringReader sr = new StringReader(str);
		Document doc = getBuilder(validate).build(sr);

		sr.close();

		if (!validate && (!doc.hasRootElement() || !doc.getRootElement().getName().equals("spooler")))
			return false;

		setDoc(doc);

		// set comments as attributes
		setComments(getDoc().getContent());

		setChanged(false);        
		return true;
	}


	/*public boolean read_2(String filename) throws JDOMException, IOException {

        StringReader sr = new StringReader(readFile(filename));
        Document doc = getBuilder(false).build(sr);
        sr.close();

        setDoc(doc);

        // set comments as attributes
        setComments(getDoc().getContent());

        setChanged(false);
        setFilename(filename);
        return true;
    }*/

	private String readFile(String filename) throws IOException {
		_disabled = new ArrayList();

		String encoding = DEFAULT_ENCODING;
		String line = null;
		StringBuffer sb = new StringBuffer();
		boolean disabled = false;

		Pattern p1 = Pattern.compile("<!--\\s*disabled\\s*=\\s*\"([^\"]+)\"");
		Pattern p2 = Pattern.compile("-->");
		Pattern p3 = Pattern.compile("<?xml.+encoding\\s*=\\s*\"([^\"]+)\"");
		Pattern p4 = Pattern.compile("<!--\\s*disabled");

		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
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

				sb.append(line + "\n");
			}

			String str = new String(sb.toString().getBytes(), encoding);
			Editor.SCHEDULER_ENCODING = encoding;

			setFilename(filename);
			return str;
		} finally {
			br.close();
		}

	}


	public void write(String filename) throws IOException, JDOMException {

		String encoding = Editor.SCHEDULER_ENCODING;
		if (encoding.equals(""))
			encoding = DEFAULT_ENCODING;
		reorderDOM();

		FormatHandler handler = new FormatHandler(this);
		handler.setStyleSheet(styleSheet);
		handler.setEnconding(encoding);
		handler.setDisableJobs(isJobsDisabled());


		SAXOutputter saxo = new SAXOutputter(handler);     

		saxo.output(getDoc());

		//Document doc  = null;
		try {
			getBuilder(true).build(new StringReader(handler.getXML()));
		} catch (JDOMException e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			int res = MainWindow.message(Messages.getString("MainListener.outputInvalid",
					new String[] { e.getMessage() }), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			if (res == SWT.NO)
				return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		writer.write(handler.getXML());
		writer.close();

		// FileOutputStream stream = new FileOutputStream(new File(filename));
		// XMLOutputter out = new XMLOutputter(getFormat());
		// out.output(_doc, stream);
		// stream.close();

		setFilename(filename);
		setChanged(false);

		deorderDOM();

	}


	public void writeElement(String filename, Document doc) throws IOException, JDOMException {

		String encoding = Editor.SCHEDULER_ENCODING;
		if (encoding.equals(""))
			encoding = DEFAULT_ENCODING;

		reorderDOM(doc.getRootElement());

		FormatHandler handler = new FormatHandler(this);
		handler.setStyleSheet(styleSheet);
		handler.setEnconding(encoding);
		handler.setDisableJobs(isJobsDisabled());
		SAXOutputter saxo = new SAXOutputter(handler);
		//saxo.output(getDoc());
		saxo.output(doc);

		try {
			getBuilder(false).build(new StringReader(handler.getXML()));
		} catch (JDOMException e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			int res = MainWindow.message(Messages.getString("MainListener.outputInvalid",
					new String[] { e.getMessage() }), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			if (res == SWT.NO)
				return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		writer.write(handler.getXML());
		writer.close();

		// FileOutputStream stream = new FileOutputStream(new File(filename));
		// XMLOutputter out = new XMLOutputter(getFormat());
		// out.output(_doc, stream);
		// stream.close();

		//setFilename(filename);


		setChanged(false);
		deorderDOM();
	}


	public String getXML(Element element) throws JDOMException {

		reorderDOM(element);



		FormatHandler handler = new FormatHandler(this);
		handler.setStyleSheet(styleSheet);
		handler.setEnconding(DEFAULT_ENCODING);
		handler.setDisableJobs(isJobsDisabled());
		SAXOutputter saxo = new SAXOutputter(handler);
		saxo.output(element);

		deorderDOM();
		return handler.getXML();

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


	public boolean isJobDisabled(String name) {
		return _disabled.contains(name);
	}


	public boolean isJobsDisabled() {
		if(isLifeElement()) {
			return false;
		}
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

	public void setChangedForDirectory(Element _parent, String what) {
		Element parent = Utils.getRunTimeParentElement(_parent);
		if(parent != null) {
			if(parent.getName().equals("order")) {
				setChangedForDirectory(parent.getName(), Utils.getAttributeValue("job_chain",parent)+","+Utils.getAttributeValue("id",parent), what);
			} else {
				setChangedForDirectory(parent.getName(), Utils.getAttributeValue("name",parent), what);
			}

		}
		/*if(_parent != null) {
    		if(_parent.getName().equals("schedule")){
    			setChangedForDirectory(_parent.getName(), Utils.getAttributeValue("name",_parent), what);
    		} else if(_parent.getParentElement().getName().equals("order")) {
    			setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_parent.getParentElement())+","+Utils.getAttributeValue("id",_parent.getParentElement()), what);
    		} else {
    			setChangedForDirectory(_parent.getParentElement().getName(), Utils.getAttributeValue("name",_parent.getParentElement()), what);
    		}
    	}*/
	}

	/*
	 * what is: NEW or MODIFY or DELETE
	 */
	public void setChangedForDirectory(String which, String name, String what) {    	
		changedForDirectory.put(which + "_" + name, what);
	}

	public HashMap getChangedJob() {
		return changedForDirectory;
	}

	public void clearChangedJob() {
		changedForDirectory.clear();
	}

	private void findStyleSheet(Iterator descendants) {
		while(descendants != null && descendants.hasNext()) {
			Object o = descendants.next(); 
			if (o instanceof ProcessingInstruction) {
				ProcessingInstruction h = (ProcessingInstruction)o;
				try {
					styleSheet =  "<?" + h.getTarget() + " " + h.getValue() + "?>";
				} catch(Exception e) {
					try {
						new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
					} catch(Exception ee) {
						//tu nichts
					}

					System.out.println("error in SchedulerDom write: " + e.getMessage());        		
				}
			}           
		}		 
	}

	public ArrayList getListOfReadOnlyFiles() {
		return listOfReadOnlyFiles;
	}

	public void setListOfReadOnlyFiles(ArrayList listOfReadOnlyFiles) {
		this.listOfReadOnlyFiles = listOfReadOnlyFiles;

	}

	public ArrayList getListOfChangeElementNames() {		
		return listOfChangeElementNames;
	}

	public void setListOfChangeElementNames(ArrayList listOfChangeElementNames) {
		this.listOfChangeElementNames = listOfChangeElementNames;
		for(int i = 0; i < listOfChangeElementNames.size(); i++) {
			changedForDirectory.put(listOfChangeElementNames.get(i), MODIFY);
		}
	}

	public boolean isLifeElement() {

		return !getRoot().getName().equals("spooler") ; 		
	}

	/*public static ArrayList getListOfEmptyElementNames() {
		return listOfEmptyElementNames;
	}

	public static void setListOfEmptyElementNames(ArrayList listOfEmptyElementNames) {
		SchedulerDom.listOfEmptyElementNames = listOfEmptyElementNames;
	}*/

	public boolean isDirectory() {
		return isDirectory;
	}

}
