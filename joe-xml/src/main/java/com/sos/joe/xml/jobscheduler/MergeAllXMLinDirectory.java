package com.sos.joe.xml.jobscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import sos.util.SOSFile;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;

public class MergeAllXMLinDirectory {

    public final static String MASK_JOB = "^.*\\.job\\.xml$";
    public final static String MASK_LOCK = "^.*\\.lock\\.xml$";
    public final static String MASK_PROCESS_CLASS = "^.*\\.process_class\\.xml$";
    public final static String MASK_JOB_CHAIN = "^.*\\.job_chain\\.xml$";
    public final static String MASK_ORDER = "^.*\\.order\\.xml$";
    public final static String MASK_SCHEDULE = "^.*\\.schedule\\.xml$";
    public final static String MASK_MONITOR = "^.*\\.monitor\\.xml$";
    private String path = "";
    private Element config = null;
    private static String encoding = "ISO-8859-1";
    private HashMap<String, String> listOfChanges = null;
    private ArrayList<String> listOfReadOnly = null;
    private ArrayList<String> listOfChangeElementNames = null;

    public MergeAllXMLinDirectory(final String path_) {
        path = path_;
    }

    public MergeAllXMLinDirectory() {
    }

    public String parseDocuments() {
        Element root = null;
        Document parentDoc = null;
        Element jobs = null;
        Element locks = null;
        Element monitors = null;
        Element schedules = null;
        Element processClass = null;
        Element jobChains = null;
        Element orders = null;
        listOfReadOnly = new ArrayList<String>();
        listOfChangeElementNames = new ArrayList<String>();
        try {
            SAXBuilder builder = new SAXBuilder();
            String xml = createConfigurationFile();
            parentDoc = builder.build(new StringReader(xml));
            root = parentDoc.getRootElement();
            if (root != null) {
                config = root.getChild("config");
            }
            addContains(processClass, "process_classes", MASK_PROCESS_CLASS);
            addContains(schedules, "schedules", MASK_SCHEDULE);
            addContains(locks, "locks", MASK_LOCK);
            addContains(jobs, "jobs", MASK_JOB);
            addContains(jobChains, "job_chains", MASK_JOB_CHAIN);
            addContainsForOrder(orders, "commands", MASK_ORDER);
            addContains(monitors, "monitors", MASK_MONITOR);
            return Utils.getElementAsString(parentDoc.getRootElement());
        } catch (Exception e) {
            System.err.println("..error : " + e.getMessage());
            return null;
        }
    }

    protected File getNormalizedFile(final String url) throws Exception {
        try {
            if (url.startsWith("file")) {
                return new java.io.File(java.net.URI.create(url));
            } else {
                return new java.io.File(url);
            }
        } catch (Exception e) {
            throw new Exception("-> ..error : " + e);
        }
    }

    private void addContains(Element parent, final String name, final String mask) {
        SAXBuilder builder = null;
        Document currDocument = null;
        String jobXMLFilename = "";
        try {
            builder = getBuilder(false);
            Vector filelist = SOSFile.getFilelist(getNormalizedFile(path).getAbsolutePath(), mask, java.util.regex.Pattern.CASE_INSENSITIVE);
            Iterator orderIterator = filelist.iterator();
            while (orderIterator.hasNext()) {
                try {
                    jobXMLFilename = orderIterator.next().toString();
                    File jobXMLFile = new File(jobXMLFilename);
                    currDocument = builder.build(jobXMLFile);
                    Element xmlRoot = currDocument.getRootElement();
                    if (xmlRoot != null) {
                        if (parent == null) {
                            parent = new Element(name);
                            config.addContent(parent);
                        }
                        String jobXMLNameWithoutExtension = jobXMLFile.getName().substring(0, jobXMLFile.getName().indexOf("." + xmlRoot.getName() + ".xml"));
                        if (!Utils.getAttributeValue("name", xmlRoot).isEmpty() 
                                && !jobXMLNameWithoutExtension.equalsIgnoreCase(Utils.getAttributeValue("name", xmlRoot))) {
                            listOfChangeElementNames.add(xmlRoot.getName() + "_" + jobXMLNameWithoutExtension);
                            xmlRoot.setAttribute("name", jobXMLNameWithoutExtension);
                        }
                        if (Utils.getAttributeValue("name", xmlRoot).isEmpty()) {
                            xmlRoot.setAttribute("name", jobXMLNameWithoutExtension);
                        }
                        parent.addContent((Element) xmlRoot.clone());
                        if (!new File(jobXMLFilename).canWrite()) {
                            listOfReadOnly.add(getChildElementName(name) + "_" + Utils.getAttributeValue("name", xmlRoot));
                        }
                    }
                } catch (Exception e) {
                    ErrorLog.message(jobXMLFilename + " has error:" + e.toString(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    private void addContainsForOrder(Element parent, final String name, final String mask) {
        SAXBuilder builder = new SAXBuilder();
        Document currDocument = null;
        try {
            Vector<File> filelist = SOSFile.getFilelist(getNormalizedFile(path).getAbsolutePath(), mask, java.util.regex.Pattern.CASE_INSENSITIVE);
            Iterator<File> orderIterator = filelist.iterator();
            while (orderIterator.hasNext()) {
                File xmlFile = orderIterator.next();
                currDocument = builder.build(xmlFile);
                Element xmlRoot = currDocument.getRootElement();
                if (xmlRoot != null) {
                    if (parent == null) {
                        parent = new Element(name);
                        config.addContent(parent);
                    }
                    String xmlNameWithoutExtension = xmlFile.getName().substring(0, xmlFile.getName().indexOf("."
                            + (xmlRoot.getName().equalsIgnoreCase("add_order") ? "order" : xmlRoot.getName() + ".xml")));
                    String[] splitNames = xmlNameWithoutExtension.split(",");
                    String jobChainname = "";
                    String orderId = "";
                    if (splitNames.length == 2) {
                        jobChainname = splitNames[0];
                        orderId = splitNames[1];
                    }
                    if (!Utils.getAttributeValue("job_chain", xmlRoot).isEmpty()
                            && !jobChainname.equalsIgnoreCase(Utils.getAttributeValue("job_chain", xmlRoot))) {
                        listOfChangeElementNames.add(xmlRoot.getName() + "_" + xmlNameWithoutExtension);
                        xmlRoot.setAttribute("job_chain", jobChainname);
                    }
                    if (!Utils.getAttributeValue("id", xmlRoot).isEmpty() && !orderId.equalsIgnoreCase(Utils.getAttributeValue("id", xmlRoot))) {
                        listOfChangeElementNames.add(xmlRoot.getName() + "_" + xmlNameWithoutExtension);
                        xmlRoot.setAttribute("id", orderId);
                    }
                    if (Utils.getAttributeValue("job_chain", xmlRoot).isEmpty()) {
                        xmlRoot.setAttribute("job_chain", jobChainname);
                    }
                    if (Utils.getAttributeValue("id", xmlRoot).isEmpty()) {
                        xmlRoot.setAttribute("id", orderId);
                    }
                    parent.addContent((Element) xmlRoot.clone());
                    if (!xmlFile.canWrite()) {
                        listOfReadOnly.add(getChildElementName(name) + "_" + Utils.getAttributeValue("job_chain", xmlRoot) + ","
                                + Utils.getAttributeValue("id", xmlRoot));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    private String getChildElementName(final String pName) {
        if ("jobs".equals(pName)) {
            return "job";
        } else if ("process_classes".equals(pName)) {
            return "process_class";
        } else if ("locks".equals(pName)) {
            return "lock";
        } else if ("job_chains".equals(pName)) {
            return "job_chain";
        } else {
            return pName;
        }
    }

    private String createConfigurationFile() {
        String xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?> ";
        try {
            xml = xml + "<spooler>  " + "      <config> " + "      </config> " + "    </spooler>";
        } catch (Exception e) {
            System.out.println("..error in MergeAllXMLinDirectory.createConfigurationFile(). Could not create a new configuration file: "
                    + e.getMessage());
        }
        return xml;
    }

    public void saveXMLDirectory(final Document doc, final HashMap<String, String> listOfChanges_) {
        Element jobs = null;
        Element locks = null;
        Element processClass = null;
        Element jobChains = null;
        Element orders = null;
        Element schedules = null;
        Element monitors = null;
        listOfChanges = listOfChanges_;
        try {
            Element root = doc.getRootElement();
            if (root != null) {
                config = root.getChild("config");
            }
            if (config != null) {
                jobs = config.getChild("jobs");
                processClass = config.getChild("process_classes");
                locks = config.getChild("locks");
                jobChains = config.getChild("job_chains");
                orders = config.getChild("commands");
                schedules = config.getChild("schedules");
                monitors = config.getChild("monitors");
            }
            save("job", jobs);
            save("process_class", processClass);
            save("schedule", schedules);
            save("monitor", monitors);
            save("lock", locks);
            save("job_chain", jobChains);
            save("order", orders);
            save("add_order", orders);
            deleteFiles();
            listOfChanges.clear();
        } catch (Exception e) {
            System.out.println("..error in MergeAllXMLinDirectory.save. Could not save file " + e.getMessage());
        }
    }

    private void save(final String name, final Element elem) {
        List list = null;
        if (elem != null) {
            list = elem.getChildren(name);
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Element e = (Element) list.get(i);
            saveLifeElement(name, e);
        }
    }

    public String saveLifeElement(final String name, final Element e, final HashMap<String, String> listOfChanges_,
            final ArrayList<String> listOfChangeElementNames_) {
        listOfChangeElementNames = listOfChangeElementNames_;
        listOfChanges = listOfChanges_;
        return saveLifeElement(name, e);
    }

    public String saveAsLifeElement(String name, final Element e, String filename) {
        String attrName = "";
        if ("add_order".equals(name)) {
            name = "order";
        } else if ("order".equals(name)) {
            if (!filename.endsWith(".order.xml")) {
                if (new File(filename).renameTo(new File(filename + ".order.xml"))) {
                    new File(filename).deleteOnExit();
                }
                filename = filename + ".order.xml";
            }
            String[] file = new File(filename).getName().split(",");
            if (file.length == 2) {
                attrName = (file.length >= 2 ? file[0] : "") + "," + (file.length >= 2 ? file[1] : "");
                attrName = attrName.substring(0, attrName.indexOf(".order.xml"));
            } else {
                attrName = Utils.getAttributeValue("job_chain", e) + "," + file[0];
                filename = filename.replaceAll(new File(filename).getName(), attrName);
                attrName = attrName.substring(0, attrName.indexOf(".order.xml"));
            }
        } else {
            if (!filename.endsWith("." + e.getName() + ".xml")) {
                if (!new File(filename).renameTo(new File(filename + "." + e.getName() + ".xml"))) {
                    new File(filename).deleteOnExit();
                }
                filename = filename + "." + e.getName() + ".xml";
            }
            String fname = new File(filename).getName();
            attrName = fname.substring(0, fname.indexOf("." + e.getName()));
        }
        Element _elem = e;
        if ("order".equals(name)) {
            _elem.removeAttribute("job_chain");
            _elem.removeAttribute("id");
            _elem.removeAttribute("replace");
        } else {
            _elem.removeAttribute("name");
        }
        String xml = Utils.getElementAsString(_elem);
        saveXML(xml, filename);
        if ("order".equals(name)) {
            Utils.setAttribute("job_chain", attrName.substring(0, attrName.indexOf(",")), e);
            Utils.setAttribute("id", attrName.substring(attrName.indexOf(",") + 1), e);
        } else {
            Utils.setAttribute("name", attrName, e);
        }
        return filename;
    }

    public String saveLifeElement(String pstrCurrentTagName, final Element e) {
        String filename = " ";
        String attrName = "";
        boolean ok = false;
        if (isOrderTag(pstrCurrentTagName)) {
            pstrCurrentTagName = "order";
        }
        if ("order".equals(pstrCurrentTagName)) {
            attrName = Utils.getAttributeValue("job_chain", e) + "," + Utils.getAttributeValue("id", e);
        } else {
            attrName = Utils.getAttributeValue("name", e);
        }
        if (attrName != null && attrName.isEmpty()) {
            return "";
        }
        filename = (path.endsWith("/") || path.endsWith("\\") ? path : path.concat("/")) + attrName + "."
                + (pstrCurrentTagName.equalsIgnoreCase("add_order") ? "order" : pstrCurrentTagName) + ".xml";
        if (listOfChanges.containsKey(pstrCurrentTagName + "_" + attrName)) {
            if (listOfChanges.get(pstrCurrentTagName + "_" + attrName).equals(SchedulerDom.DELETE) && !new File(filename).delete()) {
                ErrorLog.message(filename + " could not delete.", SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            } else {
                Element _elem = e;
                if (isOrderTag(pstrCurrentTagName)) {
                    _elem.removeAttribute("job_chain");
                    _elem.removeAttribute("id");
                    _elem.removeAttribute("replace");
                } else {
                    _elem.removeAttribute("name");
                }
                if (isJobTag(pstrCurrentTagName)) {
                    if (_elem.getChild("process") != null) {
                        if (_elem.getChild("process").getAttribute("file") == null) {
                            _elem.removeChild("process");
                        } else {
                            _elem.removeChild("script");
                        }
                    }
                    e.removeAttribute("spooler_id");
                }
                String xml = Utils.getElementAsString(_elem);
                ok = saveXML(xml, filename);
                if (isOrderTag(pstrCurrentTagName)) {
                    Utils.setAttribute("job_chain", attrName.substring(0, attrName.indexOf(",")), e);
                    Utils.setAttribute("id", attrName.substring(attrName.indexOf(",") + 1), e);
                } else {
                    Utils.setAttribute("name", attrName, e);
                }
            }
        }
        if (ok && !new File(filename).exists()) {
            String xml = Utils.getElementAsString(e);
            saveXML(xml, filename);
        }
        deleteFiles();
        return filename;
    }

    private boolean isJobTag(final String pstrTag) {
        return "job".equalsIgnoreCase(pstrTag);
    }

    private boolean isOrderTag(final String pstrTag) {
        return "order".equalsIgnoreCase(pstrTag) || "add_order".equalsIgnoreCase(pstrTag);
    }

    private boolean saveXML(final String xml, String filename) {
        String originalFilename = filename;
        filename = filename + "~";
        boolean saveFile = false;
        try {
            SAXBuilder builder2 = getBuilder(false);
            Document doc = builder2.build(new StringReader(xml));
            SchedulerDom dom = new SchedulerDom(SchedulerDom.DIRECTORY);
            new File(originalFilename).delete();
            saveFile = dom.writeElement(filename, doc);
            if (saveFile && !new File(filename).renameTo(new File(originalFilename))) {
                ErrorLog.message("could not rename file in " + filename, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            }
            return saveFile;
        } catch (Exception e) {
            ErrorLog.message("could not save file " + filename + ". cause:" + e.getMessage(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            return false;
        }
    }

    private String getFileName(String key, String prefix) {
        return (path.endsWith("/") || path.endsWith("\\") ? path : path.concat("/")) + key.substring(prefix.length()) + "."
                + prefix.substring(0, prefix.length() - 1) + ".xml";
    }

    private void deleteFiles() {
        String filename = "";
        String prefix = "";
        Iterator<String> keys1 = listOfChanges.keySet().iterator();
        Iterator<String> values1 = listOfChanges.values().iterator();
        while (keys1.hasNext()) {
            String key = keys1.next();
            String listOfChangesValue = values1.next();
            if (listOfChangesValue == null) {
                listOfChangesValue = "";
            }
            if (listOfChangesValue.equals(SchedulerDom.DELETE)) {
                if (key.startsWith("job_chain_")) {
                    prefix = "job_chain_";
                } else if (key.startsWith("job_")) {
                    prefix = "job_";
                } else if (key.startsWith("process_class_")) {
                    prefix = "process_class_";
                } else if (key.startsWith("lock_")) {
                    prefix = "lock_";
                } else if (key.startsWith("order_")) {
                    prefix = "order_";
                } else if (key.startsWith("schedule_")) {
                    prefix = "schedule_";
                } else if (key.startsWith("monitor_")) {
                    prefix = "monitor_";
                }
                filename = getFileName(key, prefix);
                File f = new File(filename);
                if ("job_chain_".equals(prefix)) {
                    String filenameNodeParameters = filename.replaceAll(".job_chain.xml", ".config.xml");
                    File fNodeParameters = new File(filenameNodeParameters);
                    if (fNodeParameters.exists()) {
                        fNodeParameters.delete();
                    }
                    String filenameGraphvizDiagram = filename.replaceAll(".job_chain.xml", ".dot");
                    File fGraphvizDiagram = new File(filenameGraphvizDiagram);
                    if (fGraphvizDiagram.exists()) {
                        fGraphvizDiagram.delete();
                    }
                    String filenameGraphvizDiagramPng = filename.replaceAll(".job_chain.xml", ".png");
                    File fGraphvizDiagramPng = new File(filenameGraphvizDiagramPng);
                    if (fGraphvizDiagramPng.exists()) {
                        fGraphvizDiagramPng.delete();
                    }
                }
                if (f.exists() && !f.delete()) {
                    ErrorLog.message(filename + " could not delete.", SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                }
            }
        }
    }

    public String getJobname(final String filename) {
        String jobname = "";
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(filename));
            Element root = doc.getRootElement();
            jobname = Utils.getAttributeValue("name", root);
        } catch (Exception e) {
            ErrorLog.message(".. could not get jobname from " + filename + " cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
        return jobname;
    }

    public static Element readElementFromHotHolderFile(final File file) {
        Element elem = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(file);
            elem = doc.getRootElement();
            String name = file.getName().substring(0, file.getName().indexOf("."));
            if ("order".equals(elem.getName()) || "add_order".equals(elem.getName())) {
                String[] split = name.split(",");
                String jobChain = split[0];
                String id = split.length > 1 ? split[1] : "";
                Utils.setAttribute("job_chain", jobChain, elem);
                Utils.setAttribute("id", id, elem);
            } else {
                Utils.setAttribute("name", name, elem);
            }
        } catch (Exception e) {
            ErrorLog.message(".. could not read Element from " + file + " cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
        return elem;
    }

    public ArrayList<String> getListOfReadOnly() {
        return listOfReadOnly;
    }

    public ArrayList<String> getListOfChangeElementNames() {
        return listOfChangeElementNames;
    }

    public void setListOfChangeElementNames(final ArrayList<String> listOfChangeElementNames) {
        this.listOfChangeElementNames = listOfChangeElementNames;
    }

    protected SAXBuilder getBuilder(final boolean validate) throws IOException {
        SAXBuilder builder = new SAXBuilder(validate);
        if (validate) {
            builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", writeSchemaFile());
        }
        return builder;
    }

    protected String[] writeSchemaFile() throws IOException {
        try {
            String[] s = new String[1];
            s[0] = getClass().getResource(Options.getSchema()).toString();
            return s;
        } catch (Exception e) {
            try {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could get schema ", e);
            } catch (Exception ee) {
            }
            throw new IOException("error in writeSchemaFile(). could get schema " + e.toString());
        }
    }

}
