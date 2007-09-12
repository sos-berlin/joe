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

public class SchedulerDom extends DomParser {
    private static final String[] CONFIG_ELEMENTS     = { "base", "security", "cluster", "process_classes", "locks", "script", "http_server",
            "holidays", "jobs", "job_chains","orders", "commands" };

    private static final String[] JOB_ELEMENTS        = { "description", "params", "script", "process", "monitor",
            "start_when_directory_changed", "delay_after_error", "delay_order_after_setback", "run_time", "commands" };        

    private static final String[] RUNTIME_ELEMENTS    = { "period", "at", "date", "weekdays", "monthdays", "ultimos", "holidays" };
    
    private static final String[] JOBCHAIN_ELEMENTS   = { "file_order_source", "job_chain_node", "file_order_sink"};

    private ArrayList             _disabled           = new ArrayList();
    
    private HashMap               changedForDirectory = new HashMap();    
    
    public static final String    MODIFY              = "modify";
    
    public static final String    DELETE              = "delete";
    
    public static final String    NEW                 = "new";

    private static final String[] CONFIG_ELEMENTS_DIRECTORY  = { "process_classes", "locks", "jobs", "job_chains"};

    public static final int       CONFIGURATION       = 0;
    
    public static final int       DIRECTORY           = 1;
    
    /** Schreibheschützte Dateien*/
    private ArrayList             listOfReadOnlyFiles = null;
          
    private static final String[] HTTP_SERVER         = { "web_service", "http.authentication", "http_directory"};       
    
    private String                 styleSheet         = "";
       
    public SchedulerDom() {
        super(new String[] { "scheduler_editor_schema" }, new String[] { Options.getSchema() }, Options.getXSLT());

        putDomOrder("config", CONFIG_ELEMENTS);
        putDomOrder("job", JOB_ELEMENTS);
        putDomOrder("run_time", RUNTIME_ELEMENTS);
        putDomOrder("job_chain", JOBCHAIN_ELEMENTS);
        putDomOrder("http_server", HTTP_SERVER);

        initScheduler();
    }

    public SchedulerDom(int type) {
    	super(new String[] { "scheduler_editor_schema" }, new String[] { Options.getSchema() }, Options.getXSLT());
    	
    	if (type == DIRECTORY) {
    		putDomOrder("config", CONFIG_ELEMENTS_DIRECTORY);
    		putDomOrder("job", JOB_ELEMENTS);
    		putDomOrder("run_time", RUNTIME_ELEMENTS);
    		putDomOrder("job_chain", CONFIG_ELEMENTS_DIRECTORY);
    	} else {
    		new SchedulerDom();
    	}
    	
    	initScheduler();
    }

    public void initScheduler() {
        Element config = new Element("config");
        setDoc(new Document(new Element("spooler").addContent(config)));
        Element processClasses = new Element("process_classes");
        Element defaultClass = new Element("process_class");
        defaultClass.setAttribute("max_processes", "10");
        config.addContent(processClasses.addContent(defaultClass));
    }


    public boolean read(String filename) throws JDOMException, IOException {    	
        return read(filename, Options.isValidate());
    }


    public boolean read(String filename, boolean validate) throws JDOMException, IOException {

        StringReader sr = new StringReader(readFile(filename));
        Document doc = getBuilder(validate).build(sr);
        sr.close();
                
        if(doc.getDescendants() != null) {
        	Iterator descendants = doc.getDescendants();
        	findStyleSheet(descendants);
        }
        
        if (!validate && (!doc.hasRootElement() || !doc.getRootElement().getName().equals("spooler")))
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

        setChanged(true);        
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
        

        try {
            getBuilder(true).build(new StringReader(handler.getXML()));
        } catch (JDOMException e) {
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
    }


    public String getXML(Element element) throws JDOMException {
    	
        reorderDOM(element);

        FormatHandler handler = new FormatHandler(this);
        handler.setStyleSheet(styleSheet);
        handler.setEnconding(DEFAULT_ENCODING);
        handler.setDisableJobs(isJobsDisabled());
        SAXOutputter saxo = new SAXOutputter(handler);
        saxo.output(element);

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
    
    public void setChangedForDirectory(String which, String name, String what) {    	
    	changedForDirectory.put(which + "_" + name, what);
    }

    public HashMap getChangedJob() {
    	return changedForDirectory;
    }
    
    public void clearChangedJob() {
    	changedForDirectory.clear();
    }

	public ArrayList getListOfReadOnlyFiles() {
		return listOfReadOnlyFiles;
	}

	public void setListOfReadOnlyFiles(ArrayList listOfReadOnlyFiles) {
		this.listOfReadOnlyFiles = listOfReadOnlyFiles;
	}
    
	private void findStyleSheet(Iterator descendants) {
		while(descendants != null && descendants.hasNext()) {
        	Object o = descendants.next(); 
        	if (o instanceof ProcessingInstruction) {
        		ProcessingInstruction h = (ProcessingInstruction)o;
        		try {
        			styleSheet =  "<?" + h.getTarget() + " " + h.getValue() + "?>";
        		} catch(Exception e) {
        			System.out.println("error in SchedulerDom write: " + e.getMessage());        		
        		}
        	}           
        }		 
	}
}
