package com.sos.joe.xml.jobscheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import sos.util.SOSFile;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.Utils;

public class SchedulerDom extends DomParser {

    private static final String[] CONFIG_ELEMENTS = { "base", "params", "security", "plugins", "cluster", "process_classes", "schedules", "locks", "script",
            "http_server", "holidays", "jobs", "job_chains", "orders", "commands" };
    private static final String[] JOB_ELEMENTS = { "settings", "description", "lock.use", "params", "environment", "script", "process", "monitor.use",
            "monitor", "start_when_directory_changed", "delay_after_error", "delay_order_after_setback", "run_time", "commands" };
    private static final String[] RUNTIME_ELEMENTS = { "period", "at", "date", "weekdays", "monthdays", "ultimos", "month", "holidays" };
    private static final String[] JOBCHAIN_ELEMENTS = { "file_order_source", "job_chain_node", "job_chain_node.job_chain", "job_chain_node.end",
            "file_order_sink" };
    private static final String[] MONITOR_ELEMENTS = { "script" };
    private static final String[] HOLIDAYS_ELEMENTS = { "include", "weekdays", "holiday" };
    private static final String[] PARAMS_ELEMENTS = { "include","copy_params","param" };
    private static final String[] HTTP_SERVER = { "web_service", "http.authentication", "http_directory" };
    private static final String[] COMMANDS_ELEMENTS = { "add_order", "order", "start_job" };
    private static final String[] ORDER_ELEMENTS = { "params", "environment" };
    private static final String[] SETTINGS_ELEMENTS = { "mail_on_error", "mail_on_warning", "mail_on_success", "mail_on_process", "mail_on_delay_after_error",
            "log_mail_to", "log_mail_cc", "log_mail_bcc", "log_level", "history", "history_on_process", "history_with_log" };
    private static final String[] CONFIG_ELEMENTS_DIRECTORY = { "process_classes", "schedules", "locks", "jobs", "job_chains", "commands", "monitors" };
    private String styleSheet = "";
    private ArrayList<String> listOfReadOnlyFiles = null;
    private ArrayList<String> listOfChangeElementNames = null;
    private boolean isDirectory = false;
    private HashMap<String, Long> hotFolderFiles = null;
    private HashMap<String, String> changedForDirectory = new HashMap<String, String>();
    public static final String MODIFY = "modify";
    public static final String DELETE = "delete";
    public static final String NEW = "new";
    public static final int CONFIGURATION = 0;
    public static final int DIRECTORY = 1;
    public static final int LIVE_JOB = 2;
    public static final int LIVE_JOB_CHAIN = 3;
    public static final int LIFE_PROCESS_CLASS = 4;
    public static final int LIFE_LOCK = 5;
    public static final int LIFE_ORDER = 6;
    public static final int LIFE_ADD_ORDER = 7;
    public static final int LIFE_SCHEDULE = 8;
    public static final int LIFE_MONITOR = 9;

    public SchedulerDom() {
        super(new String[] { conSchema_SCHEDULER_EDITOR_SCHEMA }, new String[] { Options.getSchema() }, Options.getXSLT());
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
        putDomOrder("monitor", MONITOR_ELEMENTS);
        putDomOrder("settings", SETTINGS_ELEMENTS);
        initScheduler();
    }

    public SchedulerDom(int type) {
        super(new String[] { conSchema_SCHEDULER_EDITOR_SCHEMA }, new String[] { Options.getSchema() }, Options.getXSLT());
        if (type == DIRECTORY) {
            putDomOrder("config", CONFIG_ELEMENTS_DIRECTORY);
            putDomOrder("job", JOB_ELEMENTS);
            putDomOrder("run_time", RUNTIME_ELEMENTS);
            putDomOrder("job_chain", JOBCHAIN_ELEMENTS);
            putDomOrder("commands", COMMANDS_ELEMENTS);
            putDomOrder("params", PARAMS_ELEMENTS);
            putDomOrder("schedule", RUNTIME_ELEMENTS);
            putDomOrder("monitor", MONITOR_ELEMENTS);
            putDomOrder("holidays", HOLIDAYS_ELEMENTS);
            putDomOrder("settings", SETTINGS_ELEMENTS);
            putDomOrder("start_job", ORDER_ELEMENTS);
            isDirectory = true;
            initScheduler();
        } else if (type == LIVE_JOB) {
            putDomOrder("commands", COMMANDS_ELEMENTS);
            putDomOrder("job", JOB_ELEMENTS);
            putDomOrder("run_time", RUNTIME_ELEMENTS);
            putDomOrder("params", PARAMS_ELEMENTS);
            putDomOrder("holidays", HOLIDAYS_ELEMENTS);
            putDomOrder("settings", SETTINGS_ELEMENTS);
            putDomOrder("start_job", ORDER_ELEMENTS);
            initScheduler(type);
        } else if (type == LIVE_JOB_CHAIN) {
            putDomOrder("job_chain", JOBCHAIN_ELEMENTS);
            initScheduler(type);
        } else if (type == LIFE_ORDER) {
            putDomOrder("commands", COMMANDS_ELEMENTS);
            putDomOrder("run_time", RUNTIME_ELEMENTS);
            putDomOrder("params", PARAMS_ELEMENTS);
            putDomOrder("holidays", HOLIDAYS_ELEMENTS);
            putDomOrder("start_job", ORDER_ELEMENTS);
            initScheduler(type);
        } else if (type == LIFE_PROCESS_CLASS) {
            putDomOrder("config", new String[] { "process_classes" });
            initScheduler(type);
        } else if (type == LIFE_LOCK) {
            putDomOrder("config", new String[] { "locks" });
            initScheduler(type);
        } else if (type == LIFE_SCHEDULE) {
            putDomOrder("config", new String[] { "schedules" });
            putDomOrder("run_time", RUNTIME_ELEMENTS);
            putDomOrder("holidays", HOLIDAYS_ELEMENTS);
            initScheduler(type);
        } else if (type == LIFE_MONITOR) {
            putDomOrder("config", new String[] { "monitors" });
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
        if (type == LIFE_ORDER) {
            Element order = new Element("order");
            order.setAttribute("job_chain", "job_chain1");
            order.setAttribute("id", "id");
            setDoc(new Document(order));
        } else {
            Element elem = null;
            if (type == LIVE_JOB) {
                elem = new Element("job");
                elem.setAttribute("name", "job1");
            } else if (type == LIVE_JOB_CHAIN) {
                elem = new Element("job_chain");
                elem.setAttribute("name", "job_chain1");
            } else if (type == LIFE_PROCESS_CLASS) {
                elem = new Element("process_class");
                elem.setAttribute("name", "process_class1");
            } else if (type == LIFE_LOCK) {
                elem = new Element("lock");
                elem.setAttribute("name", "lock1");
            } else if (type == LIFE_ORDER) {
                elem = new Element("job_chain");
                elem.setAttribute("name", "job_chain1");
            } else if (type == LIFE_SCHEDULE) {
                elem = new Element("schedule");
                elem.setAttribute("name", "schedule1");
            }
            if (type == LIFE_MONITOR) {
                elem = new Element("monitor");
                elem.setAttribute("name", "monitor0");
            }
            setDoc(new Document(elem));
        }
    }

    public boolean read(String filename) throws JDOMException, IOException {
        return read(filename, Options.isValidate());
    }

    public boolean read(String filename, boolean validate) throws JDOMException, IOException {
        StringReader sr = new StringReader(readFile(filename));
        Document doc = getBuilder(false).build(sr);
        sr.close();
        if (doc.getDescendants() != null) {
            Iterator descendants = doc.getDescendants();
            findStyleSheet(descendants);
        }
        if (!validate && !doc.hasRootElement()) {
            return false;
        }
        setDoc(doc);
        setComments(getDoc().getContent(), null);
        setChanged(false);
        setFilename(filename);
        return true;
    }

    public boolean readString(String str, boolean validate) throws JDOMException, IOException {
        StringReader sr = new StringReader(str);
        Document doc = getBuilder(validate).build(sr);
        sr.close();
        if (!validate && (!doc.hasRootElement() || !"spooler".equals(doc.getRootElement().getName()))) {
            return false;
        }
        setDoc(doc);
        setComments(getDoc().getContent(), null);
        setChanged(false);
        return true;
    }

    public boolean isEnabled(Element e) {
        String enabledAttr = Utils.getAttributeValue("enabled", e);
        boolean enabled = "yes".equalsIgnoreCase(enabledAttr) || enabledAttr.isEmpty();
        return enabled;
    }

    private String readFile(String filename) throws IOException {
        String encoding = DEFAULT_ENCODING;
        String line = null;
        StringBuilder sb = new StringBuilder();
        Pattern p3 = Pattern.compile("<?xml.+encoding\\s*=\\s*\"([^\"]+)\"");
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            while ((line = br.readLine()) != null) {
                Matcher m3 = p3.matcher(line);
                if (m3.find()) {
                    encoding = m3.group(1);
                }
                sb.append(line + "\n");
            }
            String str = new String(sb.toString().getBytes(), encoding);
            JOEConstants.SCHEDULER_ENCODING = encoding;
            setFilename(filename);
            return str;
        } finally {
            br.close();
        }
    }

    public void write(String filename) throws IOException, JDOMException {
        String encoding = JOEConstants.SCHEDULER_ENCODING;
        if ("".equals(encoding)) {
            encoding = DEFAULT_ENCODING;
        }
        reorderDOM();
        FormatHandler handler = new FormatHandler(this);
        handler.setStyleSheet(styleSheet);
        handler.setEnconding(encoding);
        SAXOutputter saxo = new SAXOutputter(handler);
        saxo.setReportNamespaceDeclarations(false);
        saxo.output(getDoc());
        try {
            getBuilder(true).build(new StringReader(handler.getXML()));
        } catch (JDOMException e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
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
        deorderDOM();
    }

    public boolean writeElement(String filename, Document doc) throws IOException, JDOMException {
        String encoding = JOEConstants.SCHEDULER_ENCODING;
        if ("".equals(encoding)) {
            encoding = DEFAULT_ENCODING;
        }
        reorderDOM(doc.getRootElement());
        FormatHandler handler = new FormatHandler(this);
        handler.setStyleSheet(styleSheet);
        handler.setEnconding(encoding);
        SAXOutputter saxo = new SAXOutputter(handler);
        saxo.setReportNamespaceDeclarations(false);
        saxo.output(doc);
        try {
            getBuilder(true).build(new StringReader(handler.getXML()));
        } catch (JDOMException e) {
            int res = ErrorLog.message("Element is not valid. Should it still be saved?" + "\n" + e.getMessage(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
            if (res == SWT.NO) {
                return false;
            }
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);
        writer.write(handler.getXML());
        writer.close();
        setChanged(false);
        deorderDOM();
        return true;
    }

    public String getXML(Element element) throws JDOMException {
        reorderDOM(element);
        FormatHandler handler = new FormatHandler(this);
        handler.setStyleSheet(styleSheet);
        handler.setEnconding(DEFAULT_ENCODING);
        SAXOutputter saxo = new SAXOutputter(handler);
        saxo.setReportNamespaceDeclarations(false);
        saxo.output(element);
        deorderDOM();
        return handler.getXML();
    }

    private void setComments(List content, Element plastElement) {
        Element lastElement = plastElement;
        if (content != null) {
            String comment = null;
            for (Iterator it = content.iterator(); it.hasNext();) {
                Object o = it.next();
                if (o instanceof Comment) {
                    comment = ((Comment) o).getText();
                    if (lastElement != null) {
                        lastElement.setAttribute("__comment__", comment.trim());
                    }
                } else {
                    if (o instanceof Element) {
                        Element e = (Element) o;
                        lastElement = e;
                        setComments(e.getContent(), lastElement);
                    } else if (!(o instanceof Text)) {
                        comment = null;
                    }
                }
            }
        }
    }

    public void setChangedForDirectory(Element _parent, String what) {
        Element parent = Utils.getRunTimeParentElement(_parent);
        if (parent != null) {
            if ("order".equals(parent.getName()) || "add_order".equals(parent.getName())) {
                setChangedForDirectory(parent.getName(), Utils.getAttributeValue("job_chain", parent) + "," + Utils.getAttributeValue("id", parent), what);
            } else {
                setChangedForDirectory(parent.getName(), Utils.getAttributeValue("name", parent), what);
            }
        }
    }

    @Deprecated
    public void setChangedForDirectory(String which, String name, String what) {
        if (!isChanged()) {
            return;
        }
        changedForDirectory.put(which + "_" + name, what);
        String filename = which + "." + name + ".xml";
        if (DELETE.equals(what)) {
            return;
        }
    }

    public HashMap getChangedJob() {
        return changedForDirectory;
    }

    public void clearChangedJob() {
        changedForDirectory.clear();
    }

    private void findStyleSheet(Iterator descendants) {
        while (descendants != null && descendants.hasNext()) {
            Object o = descendants.next();
            if (o instanceof ProcessingInstruction) {
                ProcessingInstruction h = (ProcessingInstruction) o;
                try {
                    styleSheet = "<?" + h.getTarget() + " " + h.getValue() + "?>";
                } catch (Exception e) {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
                }
            }
        }
    }

    public ArrayList<String> getListOfReadOnlyFiles() {
        return listOfReadOnlyFiles;
    }

    public void setListOfReadOnlyFiles(ArrayList<String> listOfReadOnlyFiles) {
        this.listOfReadOnlyFiles = listOfReadOnlyFiles;
    }

    public ArrayList<String> getListOfChangeElementNames() {
        return listOfChangeElementNames;
    }

    public void setListOfChangeElementNames(ArrayList<String> listOfChangeElementNames) {
        this.listOfChangeElementNames = listOfChangeElementNames;
        for (int i = 0; i < listOfChangeElementNames.size(); i++) {
            changedForDirectory.put(listOfChangeElementNames.get(i), MODIFY);
        }
    }

    public boolean isLifeElement() {
        return !getRoot().getName().equals("spooler");
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void readFileLastModified() {
        try {
            if (!isDirectory) {
                super.readFileLastModified();
            } else {
                if (getFilename() == null) {
                    this.setLastModifiedFile(0);
                    return;
                }
                long lastModified = 0;
                File f = new File(getFilename());
                if (f.exists() && f.isDirectory()) {
                    ArrayList<File> listOfhotFolderFiles = getHoltFolderFiles(f);
                    hotFolderFiles = new HashMap<String, Long>();
                    for (int i = 0; i < listOfhotFolderFiles.size(); i++) {
                        File fFile = listOfhotFolderFiles.get(i);
                        hotFolderFiles.put(fFile.getName(), fFile.lastModified());
                        lastModified = lastModified + fFile.lastModified();
                    }
                    this.setLastModifiedFile(lastModified);
                } else {
                    this.setLastModifiedFile(0);
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public ArrayList<File> getHoltFolderFiles(File f) {
        ArrayList<File> listOfhotFolderFiles = new ArrayList<File>();
        try {
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_JOB, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_JOB_CHAIN, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_LOCK, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_ORDER, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_PROCESS_CLASS, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_SCHEDULE, java.util.regex.Pattern.CASE_INSENSITIVE));
            listOfhotFolderFiles.addAll(SOSFile.getFilelist(f.getCanonicalPath(), MergeAllXMLinDirectory.MASK_MONITOR, java.util.regex.Pattern.CASE_INSENSITIVE));
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return listOfhotFolderFiles;
    }

    public HashMap<String, Long> getHotFolderFiles() {
        return hotFolderFiles;
    }

}