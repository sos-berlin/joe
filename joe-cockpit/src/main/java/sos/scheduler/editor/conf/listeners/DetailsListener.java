package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.SchedulerForm;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.DetailDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class DetailsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetailsListener.class);
    private String jobChainname = null;
    private String state = null;
    private Element noteEN = null;
    private Element noteDE = null;
    private List params = null;
    private Element application = null;
    private Document doc = null;
    private String xmlFilename = null;
    private String orderId = null;
    private int type = -1;
    private String encoding = "ISO-8859-1";
    private DetailDom dom = null;
    private boolean hasError = false;
    private Element params_ = null;
    private boolean isLifeElement = false;
    private String path = null;

    public DetailsListener(String jobChainname_, String state_, String orderId_, int type_, DetailDom dom_, boolean isLifeElement_, String path_) {
        dom = dom_;
        if (dom != null) {
            doc = dom.getDoc();
        }
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

    public void parseDocuments() {
        String xmlPaths = "";
        try {
            if (isLifeElement
                    || (MainWindow.getContainer().getCurrentTab().getData("ftp_title") != null && !MainWindow.getContainer().getCurrentTab().getData(
                            "ftp_title").toString().isEmpty())) {
                if (path != null && !path.isEmpty()) {
                    File f = new File(path);
                    if (f.isFile()) {
                        xmlPaths = f.getParent();
                    } else {
                        xmlPaths = path;
                    }
                } else {
                    xmlPaths = Options.getSchedulerHotFolder();
                }
                xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\")) ? xmlPaths : xmlPaths + "/";
            } else {
                if (path != null && !path.isEmpty()) {
                    File f = new File(path);
                    if (f.isFile()) {
                        xmlPaths = f.getParent();
                    } else {
                        xmlPaths = path;
                    }
                } else {
                    xmlPaths = Options.getSchedulerData();
                    xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths + "config/" : xmlPaths.concat("/config/"));
                }
            }
            String _currOrderId = orderId != null && !orderId.isEmpty() ? "," + orderId : "";
            xmlFilename = new File(xmlPaths, jobChainname + _currOrderId + ".config.xml").getCanonicalPath();
            if (_currOrderId != null && !_currOrderId.isEmpty()) {
                File jobChainConfig = new File(xmlPaths + jobChainname + ".config.xml");
                if (jobChainConfig.exists() && !new File(xmlFilename).exists()) {
                    int c =
                            MainWindow.message("A configuration already exists for this job chain. Should this configuration be used for the order?",
                                    SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    if (c == SWT.YES && !sos.util.SOSFile.copyFile(jobChainConfig.getAbsolutePath(), xmlFilename)) {
                        MainWindow.message("Could not copy configuration File?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            hasError = true;
        }
        Element root = null;
        Element order = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            if (doc == null) {
                File f = null;
                if (xmlFilename != null) {
                    f = new File(xmlFilename);
                }
                if (f == null || !f.exists()) {
                    String xml = createConfigurationFile();
                    doc = builder.build(new StringReader(xml));
                    if (type == JOEConstants.DETAILS) {
                        if (f != null) {
                            f.deleteOnExit();
                        }
                        dom.setDoc(doc);
                    }
                } else {
                    doc = builder.build(new File(xmlFilename));
                    if (type == JOEConstants.DETAILS) {
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
                MainWindow.message(new org.eclipse.swt.widgets.Shell(SWT.NONE), Messages.getString("details.listener.missing_job_chain_node"), SWT.OK);
                LOGGER.info("error: " + Messages.getString("details.listener.missing_job_chain_node"));
                hasError = true;
                return;
            }
            if (state == null || state.isEmpty()) {
                List note = application.getChildren("note");
                setGlobaleNote(note);
            }
            if (application != null) {
                order = application.getChild("order");
            }
            if (order != null) {
                if (state != null && !state.isEmpty()) {
                    params_ = getStateParams(order);
                } else {
                    params_ = order.getChild("params");
                }
            }
            if (params_ != null) {
                params = params_.getChildren();
            } else {
                params = new java.util.ArrayList();
            }
        } catch (Exception e) {
            hasError = true;
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
        }
    }

    public String getNote(String language) {
        if (language == null) {
            return getNoteText(noteEN);
        }
        if ("de".equalsIgnoreCase(language)) {
            return getNoteText(noteDE);
        } else {
            return getNoteText(noteEN);
        }
    }

    public void setNote(String noteText, String language) {
        if ("de".equalsIgnoreCase(language)) {
            if (noteDE == null) {
                noteDE = createNote(language);
            }
            setNoteText(noteDE, noteText);
        } else {
            if (noteEN == null) {
                noteEN = createNote(language);
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
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
        }
        return newNote;
    }

    private void setNoteText(Element note, String text) {
        text = text.replaceAll("<\\?xml version=.1.0. encoding=.ISO-8859-1.\\?>(.*)", "$1");
        Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
        if (div == null) {
            div = new Element("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
            note.addContent(div);
        }
        if (text.indexOf("<") == -1) {
            div.setText(text);
        } else {
            Element newNote = createNewNoteElement("<x>" + text + "</x>");
            if (newNote != null) {
                div.removeContent();
                div.addContent((List) newNote.cloneContent());
            }
        }
    }

    private String getNoteText(Element note) {
        String noteText = "";
        if (note != null) {
            Element div = note.getChild("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"));
            if (div != null) {
                noteText = Utils.noteAsStr(div);
            }
        }
        return noteText;
    }

    public void fillParams(Table tableParams) {
        String name = "";
        String value = "";
        String text = "";
        for (int i = 0; i < params.size(); i++) {
            Element param = (Element) (params.get(i));
            if ("param".equalsIgnoreCase(param.getName())) {
                TableItem item = new TableItem(tableParams, SWT.NONE);
                name = (param.getAttributeValue("name") != null ? param.getAttributeValue("name") : "");
                value = param.getAttributeValue("value") != null ? param.getAttributeValue("value") : "";
                text = param.getTextTrim();
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
            if (dom == null) {
                dom = new DetailDom();
            }
            dom.writeElement(xmlFilename, doc);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            MainWindow.message("Could not save file, cause: " + e.toString(), SWT.ICON_WARNING);
        }
        return f.getAbsolutePath();
    }

    public String getParamNote(String name, String language) {
        for (int i = 0; i < params.size(); i++) {
            Element param = (Element) params.get(i);
            if ("param".equals(param.getName()) && Utils.getAttributeValue("name", param).equalsIgnoreCase(name)) {
                for (int j = 1; j < 3; j++) {
                    if (params.size() <= i + j) {
                        return "";
                    }
                    Element note = (Element) params.get(i + j);
                    if ("param".equals(note.getName())) {
                        break;
                    }
                    if ("note".equals(note.getName()) && Utils.getAttributeValue("language", note).equals(language)) {
                        return getNoteText(note);
                    }
                }
            }
        }
        return "";
    }

    public String getParamsFileName() {
        if (params_ != null) {
            return Utils.getAttributeValue("file", params_);
        } else {
            return "";
        }
    }

    public void setParamsFileName(String filename) {
        if (params_ != null) {
            Utils.setAttribute("file", filename, params_);
        }
    }

    public void setParam(String name, String value, String note, String noteText, String language) {
        try {
            boolean noNote = false;
            for (int i = 0; i < params.size(); i++) {
                Element param = (Element) params.get(i);
                String pName = Utils.getAttributeValue("name", param);
                if (name.equalsIgnoreCase(pName)) {
                    Utils.setAttribute("value", value, param);
                    if (noteText != null && !noteText.trim().isEmpty()) {
                        while (!param.getContent().isEmpty()) {
                            if (param.getContent().get(0) instanceof org.jdom.Text) {
                                param.getContent().remove(0);
                            }
                        }
                        org.jdom.Text txt = new org.jdom.Text(noteText);
                        param.addContent(txt);
                    }
                    if (params.size() > i + 1) {
                        for (int j = 1; j < 3; j++) {
                            Element elNote = (Element) params.get(i + j);
                            if ("param".equals(elNote.getName())) {
                                noNote = true;
                                break;
                            }
                            if (note != null && "note".equalsIgnoreCase(elNote.getName())
                                    && Utils.getAttributeValue("language", elNote).equalsIgnoreCase(language)) {
                                setNoteText(elNote, note);
                            }
                        }
                    } else {
                        noNote = true;
                    }
                    if (noNote) {
                        Element newNoteDE = new Element("note");
                        Utils.setAttribute("language", "de", newNoteDE);
                        Element newNoteEN = new Element("note");
                        Utils.setAttribute("language", "en", newNoteEN);
                        params.add(params.indexOf(param) + 1, newNoteDE);
                        params.add(params.indexOf(param) + 2, newNoteEN);
                        return;
                    }
                    return;
                }
            }
            Element param = new Element("param");
            Utils.setAttribute("name", name, param);
            Utils.setAttribute("value", value, param);
            if (noteText != null && !noteText.trim().isEmpty()) {
                org.jdom.Text txt = new org.jdom.Text(noteText);
                param.addContent(txt);
            }
            Element newNoteDE = new Element("note");
            Utils.setAttribute("language", "de", newNoteDE);
            Element newNoteEN = new Element("note");
            Utils.setAttribute("language", "en", newNoteEN);
            params.add(param);
            params.add(newNoteDE);
            params.add(newNoteEN);
            if ("de".equals(language)) {
                setNoteText(newNoteDE, note);
            } else {
                setNoteText(newNoteEN, note);
            }
        } catch (Exception e) {
            MainWindow.message("Could not add Params cause: " + e.toString(), SWT.ICON_WARNING);
        }
    }

    public void refreshParams(Table table) {
        try {
            java.util.ArrayList list = new java.util.ArrayList();
            for (int i = 0; i < table.getItemCount(); i++) {
                TableItem item = table.getItem(i);
                Element param = (Element) item.getData();
                if (param == null) {
                    param = new Element("param");
                    Utils.setAttribute("name", item.getText(0), param);
                    Utils.setAttribute("value", item.getText(1) != null ? item.getText(1) : "", param);
                    list.add(param);
                    Element notede = new Element("note");
                    Utils.setAttribute("language", "de", notede);
                    String paramNoteDE = item.getData("parameter_description_de") != null ? item.getData("parameter_description_de").toString() : "";
                    setNoteText(notede, paramNoteDE);
                    list.add(notede);
                    Element noteen = new Element("note");
                    Utils.setAttribute("language", "en", noteen);
                    String paramNoteEN = item.getData("parameter_description_en") != null ? item.getData("parameter_description_en").toString() : "";
                    setNoteText(noteen, paramNoteEN);
                    list.add(noteen);
                } else {
                    list.add(param);
                    int index = params.indexOf(param);
                    if (params.size() > index + 1) {
                        Element notede = (Element) params.get(index + 1);
                        if ("note".equals(notede.getName()) && "de".equals(Utils.getAttributeValue("language", notede))) {
                            list.add(notede);
                        }
                    }
                    if (params.size() > index + 2) {
                        Element noteen = (Element) params.get(index + 2);
                        if ("note".equals(noteen.getName()) && "en".equals(Utils.getAttributeValue("language", noteen))) {
                            list.add(noteen);
                        }
                    }
                }
            }
            params.removeAll(params);
            params.addAll((java.util.ArrayList) list.clone());
            table.removeAll();
            fillParams(table);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public void deleteParameter(Table table, int index) {
        String name = table.getItem(index).getText(0);
        for (int i = 0; i < params.size(); i++) {
            Element p = (Element) params.get(i);
            if (Utils.getAttributeValue("name", p).equalsIgnoreCase(name)) {
                params.remove(i);
                if (i == params.size()) {
                    break;
                }
                Element pnde = (Element) params.get(i);
                if ("note".equals(pnde.getName())) {
                    params.remove(i);
                } else {
                    break;
                }
                Element pnen = (Element) params.get(i);
                if ("note".equals(pnen.getName())) {
                    params.remove(i);
                }
            }
        }
        table.remove(index);
    }

    private void setGlobaleNote(List note) {
        for (int i = 0; i < note.size(); i++) {
            Element n = (Element) note.get(i);
            if ("de".equals(n.getAttributeValue("language"))) {
                noteDE = n;
            } else if ("en".equals(n.getAttributeValue("language"))) {
                noteEN = n;
            }
        }
    }

    private String createConfigurationFile() {
        String xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?> ";
        try {
            if (Options.getDetailXSLT() != null && !Options.getDetailXSLT().isEmpty()) {
                xml = xml + "<?xml-stylesheet type=\"text/xsl\" href=\"" + Options.getDetailXSLT() + "\"?> ";
            }
            xml =
                    xml + "<settings>" + "  <job_chain name=\"" + jobChainname + "\"> " + "    <note language=\"de\"/> "
                            + "    <note language=\"en\"/> " + "    <order> " + "      <params/> " + "    </order> " + "  </job_chain> "
                            + "</settings> ";
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return xml;
    }

    private Element getStateParams(Element order) {
        Element params_ = null;
        List processList = order.getChildren("process");
        for (int i = 0; i < processList.size(); i++) {
            Element process = (Element) processList.get(i);
            if (Utils.getAttributeValue("state", process).equalsIgnoreCase(state)) {
                List note = process.getChildren("note");
                if (note.isEmpty()) {
                    noteDE = createNote(process, "DE");
                    noteEN = createNote(process, "EN");
                } else {
                    setGlobaleNote(note);
                }
                params_ = process.getChild("params");
            }
        }
        if (params_ == null) {
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
        if (application != null) {
            Utils.setAttribute("name", jobChainname, application);
        }
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

    public void updateState(String oldState, String newState) {
        Element order = null;
        this.state = newState;
        if (application != null) {
            order = application.getChild("order");
        }
        if (order != null) {
            List pList = order.getChildren("process");
            for (int i = 0; i < pList.size(); i++) {
                Element process = (Element) pList.get(i);
                if (Utils.getAttributeValue("state", process).equalsIgnoreCase(oldState)) {
                    Utils.setAttribute("state", newState, process);
                    state = newState;
                }
            }
        }
    }

    public void deleteState(String state) {
        Element order = null;
        if (application != null) {
            order = application.getChild("order");
        }
        if (order != null) {
            List pList = order.getChildren("process");
            for (int i = 0; i < pList.size(); i++) {
                Element process = (Element) pList.get(i);
                if (Utils.getAttributeValue("state", process).equalsIgnoreCase(state)) {
                    pList.remove(i);
                }
            }
        }
    }

    public boolean isValidState(String state) {
        Element order = null;
        if (application != null) {
            order = application.getChild("order");
        }
        if (order != null) {
            List pList = order.getChildren("process");
            for (int i = 0; i < pList.size(); i++) {
                Element process = (Element) pList.get(i);
                if (Utils.getAttributeValue("state", process).equalsIgnoreCase(state)) {
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
        if (params_ != null && params_.getParentElement() != null) {
            return this.params_.getParentElement();
        } else {
            return null;
        }
    }

    public Element getParams() {
        if (params_ == null) {
            params_ = new Element("params");
        }
        return params_;
    }

    public void changeUp(Table table) {
        int index = table.getSelectionIndex();
        if (index < 0) {
            return;
        }
        if (index == 0) {
            return;
        }
        TableItem item = table.getSelection()[0];
        String name = item.getText(0);
        String value = item.getText(1);
        String text = item.getText(2);
        Element param = (Element) item.getData();
        table.remove(index);
        TableItem newItem = new TableItem(table, SWT.NONE, index - 1);
        newItem.setText(0, name);
        newItem.setText(1, value);
        newItem.setText(2, text);
        newItem.setData(param);
        refreshParams(table);
        table.select(index - 1);
    }

    public void changeDown(Table table) {
        int index = table.getSelectionIndex();
        if (index < 0) {
            return;
        }
        if (index == table.getItemCount() - 1) {
            return;
        }
        TableItem item = table.getSelection()[0];
        String name = item.getText(0);
        String value = item.getText(1);
        String text = item.getText(2);
        Element param = (Element) item.getData();
        table.remove(index);
        TableItem newItem = new TableItem(table, SWT.NONE, index + 1);
        newItem.setText(0, name);
        newItem.setText(1, value);
        newItem.setText(2, text);
        newItem.setData(param);
        refreshParams(table);
        table.select(index + 1);
    }

    public static void changeDetailsState(String oldstate, String newstate, String jobchainname, SchedulerDom _dom) {
        try {
            DetailsListener detailListener =
                    new DetailsListener(jobchainname, oldstate, null, JOEConstants.JOB_CHAINS, null, _dom.isLifeElement() || _dom.isDirectory(),
                            _dom.getFilename());
            XPath x = XPath.newInstance("//process[@state='" + oldstate + "']");
            List listOfElement = x.selectNodes(detailListener.getDoc());
            XPath xnew = XPath.newInstance("//process[@state='" + newstate + "']");
            List listOfElementnew = xnew.selectNodes(detailListener.getDoc());
            if (listOfElementnew.isEmpty() && !listOfElement.isEmpty()) {
                Element process = (Element) listOfElement.get(0);
                process.setAttribute("state", newstate);
                detailListener.save();
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    public static void changeDetailsJobChainname(String newJobChainName, String oldJobchainName, SchedulerDom _dom) {
        try {
            DetailsListener detailListener =
                    new DetailsListener(oldJobchainName, null, null, JOEConstants.JOB_CHAINS, null, _dom.isLifeElement() || _dom.isDirectory(),
                            _dom.getFilename());
            XPath x = XPath.newInstance("settings/job_chain[@name='" + oldJobchainName + "']");
            List listOfElement = x.selectNodes(detailListener.getDoc());
            if (!listOfElement.isEmpty()) {
                Element jobchain = (Element) listOfElement.get(0);
                jobchain.setAttribute("name", newJobChainName);
            }
            detailListener.save();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    public static void deleteDetailsState(String state, String jobchainname, SchedulerDom dom) {
        try {
            String parent = "";
            if (dom.isDirectory()) {
                parent =
                        dom.getFilename() != null && new File(dom.getFilename()).getParent() != null ? new File(dom.getFilename()).getParent()
                                : Options.getSchedulerHotFolder() + "/config";
            } else {
                parent =
                        dom.getFilename() != null && new File(dom.getFilename()).getParent() != null ? new File(dom.getFilename()).getParent()
                                : Options.getSchedulerData() + "/config";
            }
            if (!new File(parent, jobchainname + ".config.xml").exists()) {
                return;
            }
            if (state == null || state.isEmpty()) {
                return;
            }
            DetailsListener detailListener =
                    new DetailsListener(jobchainname, state, null, JOEConstants.JOB_CHAINS, null, dom.isLifeElement() || dom.isDirectory(),
                            dom.getFilename());
            XPath x = XPath.newInstance("//process[@state='" + state + "']");
            List listOfElement = x.selectNodes(detailListener.getDoc());
            if (!listOfElement.isEmpty()) {
                Element process = (Element) listOfElement.get(0);
                process.detach();
                detailListener.save();
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    private static void addMonitoring(Element job, SchedulerDom dom) {
        if (job == null) {
            return;
        }
        Element monitor = new Element("monitor");
        Utils.setAttribute("name", "configuration_monitor", monitor);
        Utils.setAttribute("ordering", "0", monitor);
        Element script = new Element("script");
        Utils.setAttribute("java_class", "com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor", script);
        Utils.setAttribute("language", "java", script);
        monitor.addContent(script);
        job.addContent(monitor);
        dom.setChanged(true);
        if (dom.isDirectory() || dom.isLifeElement()) {
            dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.MODIFY);
        }
    }

    public static void addMonitoring2Job(String jobChainname, String state, SchedulerDom dom, ISchedulerUpdate update) {
        try {
            String sel = "//job_chain[@name='" + jobChainname + "']/job_chain_node[@job!='']";
            if (state != null) {
                sel = "//job_chain[@name='" + jobChainname + "']/job_chain_node[@state='" + state + "']";
            }
            XPath x = XPath.newInstance(sel);
            List listOfElement = x.selectNodes(dom.getDoc());
            if (!listOfElement.isEmpty()) {
                for (int i = 0; i < listOfElement.size(); i++) {
                    Element jobChainNode = (Element) listOfElement.get(i);
                    String jobname = Utils.getAttributeValue("job", jobChainNode);
                    String hotFolderfilename = "";
                    File hotFolderfile = null;
                    if (new File(Options.getSchedulerHotFolder(), jobname + ".job.xml").exists()) {
                        hotFolderfile = new File(Options.getSchedulerHotFolder(), jobname + ".job.xml");
                    } else {
                        hotFolderfile = new File(new File(dom.getFilename()), new File(jobname).getName() + ".job.xml");
                    }
                    hotFolderfilename = hotFolderfile.getCanonicalPath();
                    List listOfElement2 = null;
                    if (new File(dom.getFilename()).getAbsolutePath().equals(hotFolderfile.getParent())) {
                        jobname = new File(jobname).getName();
                    }
                    if (dom.isLifeElement() || (new File(jobname).getParent() != null)) {
                        if (!hotFolderfile.exists() && !new File(hotFolderfilename).exists()) {
                            sos.scheduler.editor.app.MainWindow.message("Could not add Monitoring Job, cause Hot Folder File " + hotFolderfilename
                                    + " not exist.", SWT.ICON_WARNING);
                            continue;
                        }
                        XPath x2 = null;
                        sos.scheduler.editor.app.TabbedContainer tab = (sos.scheduler.editor.app.TabbedContainer) MainWindow.getContainer();
                        String pathFromHotFolderDirectory = new File(hotFolderfilename).getParent();
                        if (tab.getFilelist() != null
                                && (tab.getFilelist().contains(hotFolderfilename) || tab.getFilelist().contains(pathFromHotFolderDirectory))) {
                            SchedulerForm form = null;
                            if (tab.getFilelist().contains(hotFolderfilename)) {
                                form = (SchedulerForm) tab.getEditor(hotFolderfilename);
                                x2 =
                                        XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                            } else {
                                form = (SchedulerForm) tab.getEditor(pathFromHotFolderDirectory);
                                x2 =
                                        XPath.newInstance("//job[@name='" + new File(jobname).getName()
                                                + "']/monitor/script[@java_class='sos.scheduler."
                                                + "managed.configuration.ConfigurationOrderMonitor']");
                            }
                            SchedulerDom currdom = (SchedulerDom) form.getDom();
                            listOfElement2 = x2.selectNodes(currdom.getDoc());
                            if (listOfElement2.isEmpty()) {
                                XPath x3 = null;
                                XPath x4 = null;
                                if (tab.getFilelist().contains(hotFolderfilename)) {
                                    x3 =
                                            XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                                    x4 = XPath.newInstance("//job");
                                } else {
                                    x3 =
                                            XPath.newInstance("//job[@name='" + new File(jobname).getName() + "']/monitor/script[@java_class="
                                                    + "'sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                                    x4 = XPath.newInstance("//job[@name='" + new File(jobname).getName() + "']");
                                }
                                List listOfElement3 = x3.selectNodes(currdom.getDoc());
                                if (listOfElement3.isEmpty()) {
                                    List listOfElement4 = x4.selectNodes(currdom.getDoc());
                                    Element job = (Element) listOfElement4.get(0);
                                    addMonitoring(job, currdom);
                                    if (currdom.isLifeElement()) {
                                        form.getTree().setSelection(new org.eclipse.swt.widgets.TreeItem[] { form.getTree().getItem(0) });
                                    } else if (currdom.isDirectory()) {
                                        form.selectTreeItem(SchedulerListener.JOBS, new File(jobname).getName());
                                    }
                                    currdom.setChanged(true);
                                    if (form != null) {
                                        form.updateJob(job);
                                        form.updateJob();
                                        form.update();
                                    }
                                    currdom.setChanged(true);
                                    form.dataChanged();
                                    dom.setChanged(true);
                                    if (tab.getFilelist().contains(hotFolderfilename)) {
                                        form.dataChanged(tab.getFolderTab(hotFolderfilename));
                                    } else {
                                        form.dataChanged(tab.getFolderTab(pathFromHotFolderDirectory));
                                    }
                                }
                            }
                        } else {
                            SchedulerDom currDom = new SchedulerDom(SchedulerDom.LIVE_JOB);
                            currDom.read(hotFolderfilename);
                            if (x2 == null) {
                                x2 =
                                        XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                            }
                            listOfElement2 = x2.selectNodes(currDom.getDoc());
                            if (listOfElement2.isEmpty()) {
                                XPath x3 = XPath.newInstance("//job");
                                List listOfElement3 = x3.selectNodes(currDom.getDoc());
                                if (!listOfElement3.isEmpty()) {
                                    Element job = (Element) listOfElement3.get(0);
                                    addMonitoring(job, currDom);
                                    currDom.writeElement(currDom.getFilename(), currDom.getDoc());
                                }
                            }
                        }
                    } else {
                        XPath x2 =
                                XPath.newInstance("//job[@name='" + jobname + "']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                        listOfElement2 = x2.selectNodes(dom.getDoc());
                        if (listOfElement2.isEmpty()) {
                            XPath x3 = XPath.newInstance("//jobs/job[@name='" + jobname + "']");
                            List listOfElement3 = x3.selectNodes(dom.getDoc());
                            if (!listOfElement3.isEmpty()) {
                                Element job = (Element) listOfElement3.get(0);
                                addMonitoring(job, dom);
                                if (update != null) {
                                    update.updateJobs();
                                }
                                dom.setChanged(true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message(
                    "Could not to be add Monitoring Job to Jobchain " + jobChainname + ", cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    public static boolean existDetailsParameter(String state, String jobchainname, String jobname, SchedulerDom dom, ISchedulerUpdate update,
            boolean global, String orderid) {
        try {
            DetailsListener detailListener =
                    new DetailsListener(jobchainname, state, orderid, JOEConstants.JOB_CHAINS, null, dom.isLifeElement() || dom.isDirectory(),
                            dom.getFilename());
            XPath x = null;
            if (global) {
                x = XPath.newInstance("//order/params/param");
            } else {
                x = XPath.newInstance("//process[@state='" + state + "']/params/param");
            }
            List listOfElement = x.selectNodes(detailListener.getDoc());
            return !listOfElement.isEmpty();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message("error in DetailsListener.existDetailsParameter, cause: " + e.getMessage(), SWT.ICON_ERROR);
            return false;
        }
    }

    public static void checkDetailsParameter(String state, String jobchainname, String jobname, SchedulerDom dom, ISchedulerUpdate update) {
        try {
            DetailsListener detailListener =
                    new DetailsListener(jobchainname, state, null, JOEConstants.JOB_CHAINS, null, dom.isLifeElement() || dom.isDirectory(),
                            dom.getFilename());
            XPath x = XPath.newInstance("//order/params/param");
            List listOfElement = x.selectNodes(detailListener.getDoc());
            if (!listOfElement.isEmpty()) {
                String hotFolderfilename = new File(Options.getSchedulerHotFolder(), jobname + ".job.xml").getCanonicalPath();
                List listOfElement2 = null;
                if (dom.isLifeElement() || new File(jobname).getParent() != null) {
                    if (!new File(hotFolderfilename).exists()) {
                        sos.scheduler.editor.app.MainWindow.message("Could not add Monitoring Job, cause Hot Folder File " + hotFolderfilename
                                + " not exist.", SWT.ICON_WARNING);
                        return;
                    }
                    XPath x2 = XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                    sos.scheduler.editor.app.TabbedContainer tab = (sos.scheduler.editor.app.TabbedContainer) MainWindow.getContainer();
                    if (tab.getFilelist() != null && tab.getFilelist().contains(hotFolderfilename)) {
                        SchedulerForm form = (SchedulerForm) tab.getEditor(hotFolderfilename);
                        SchedulerDom currdom = (SchedulerDom) form.getDom();
                        listOfElement2 = x2.selectNodes(currdom.getDoc());
                        if (listOfElement2.isEmpty()) {
                            XPath x3 =
                                    XPath.newInstance("//job/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                            List listOfElement3 = x3.selectNodes(currdom.getDoc());
                            if (listOfElement3.isEmpty()) {
                                x3 = XPath.newInstance("//job");
                                listOfElement3 = x3.selectNodes(currdom.getDoc());
                                Element job = (Element) listOfElement3.get(0);
                                addMonitoring(job, currdom);
                                form.getTree().setSelection(new org.eclipse.swt.widgets.TreeItem[] { form.getTree().getItem(0) });
                                currdom.setChanged(true);
                                if (form != null) {
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
                        SchedulerDom currDom = new SchedulerDom(SchedulerDom.LIVE_JOB);
                        currDom.read(hotFolderfilename);
                        listOfElement2 = x2.selectNodes(currDom.getDoc());
                        if (listOfElement2.isEmpty()) {
                            XPath x3 = XPath.newInstance("//job");
                            List listOfElement3 = x3.selectNodes(currDom.getDoc());
                            if (!listOfElement3.isEmpty()) {
                                Element job = (Element) listOfElement3.get(0);
                                addMonitoring(job, currDom);
                                currDom.writeElement(currDom.getFilename(), currDom.getDoc());
                            }
                        }
                    }
                } else {
                    XPath x2 =
                            XPath.newInstance("//job[@name='" + jobname + "']/monitor/script[@java_class='sos.scheduler.managed.configuration.ConfigurationOrderMonitor' or @java_class='com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor']");
                    listOfElement2 = x2.selectNodes(dom.getDoc());
                    if (listOfElement2.isEmpty()) {
                        XPath x3 = XPath.newInstance("//jobs/job[@name='" + jobname + "']");
                        List listOfElement3 = x3.selectNodes(dom.getDoc());
                        if (!listOfElement3.isEmpty()) {
                            Element job = (Element) listOfElement3.get(0);
                            addMonitoring(job, dom);
                            if (update != null) {
                                update.updateJobs();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            sos.scheduler.editor.app.MainWindow.message("Parameter note could not change, cause: " + e.getMessage(), SWT.ICON_ERROR);
        }
    }

}