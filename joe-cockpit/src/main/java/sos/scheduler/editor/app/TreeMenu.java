package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.conf.forms.JobChainsForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class TreeMenu {

    private static final String EDIT_XML = "Edit XML";
    private static final String SHOW_XML = "Show XML";
    private static final String COPY = "Copy";
    private static final String COPY_TO_CLIPBOARD = "XML to Clipboard";
    private static final String PASTE = "Paste";
    private static final String DELETE_HOT_HOLDER_FILE = "Delete Hot Folder File";
    private static final String NEW = "New";
    private static final String DELETE = "Delete";
    private static Element _copy = null;
    private static int _type = -1;
    private DomParser _dom = null;
    private Tree _tree = null;
    private Menu _menu = null;
    private SchedulerForm _gui = null;

    public TreeMenu(Tree tree, DomParser dom, SchedulerForm gui) {
        _tree = tree;
        _dom = dom;
        _gui = gui;
        createMenu();
    }

    public int message(String message, int style) {
        MessageBox mb = new MessageBox(_tree.getShell(), style);
        mb.setMessage(message);
        return mb.open();
    }

    private Element getElement() {
        if (_tree.getSelectionCount() > 0) {
            Element retVal = (Element) _tree.getSelection()[0].getData("copy_element");
            if (retVal == null) {
                return null;
            }
            if (((SchedulerDom) _dom).isDirectory()) {
                return (Element) Utils.getHotFolderParentElement(retVal).clone();
            }
            return (Element) retVal.clone();
        }
        return null;
    }

    private boolean isCopyItem(int type) {
        return type == JOEConstants.JOB || type == JOEConstants.JOB_CHAIN || type == JOEConstants.ORDER || type == JOEConstants.SCHEDULE
                || type == JOEConstants.LOCKS || type == JOEConstants.MONITOR || type == JOEConstants.PARAMETER || type == JOEConstants.RUNTIME
                || type == JOEConstants.JOB_OPTION || type == JOEConstants.MONITORS || type == JOEConstants.OPTIONS || type == JOEConstants.LOCKUSE
                || type == JOEConstants.MONITORUSE || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_DOCUMENTATION
                || type == JOEConstants.RUNTIME || type == JOEConstants.PROCESS_CLASSES || type == JOEConstants.SETTINGS;
    }

    private void createMenu() {
        _menu = new Menu(_tree.getShell(), SWT.POP_UP);
        MenuItem item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getCopyListener());
        item.setText(TreeMenu.COPY);
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getPasteListener());
        item.setText(TreeMenu.PASTE);
        item = new MenuItem(_menu, SWT.SEPARATOR);
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getNewItemSelection());
        item.setText(TreeMenu.NEW);
        item.setEnabled(false);
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getDeleteSelection());
        item.setText(TreeMenu.DELETE);
        item.setEnabled(false);
        if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement()) {
            item = new MenuItem(_menu, SWT.PUSH);
            item.addListener(SWT.Selection, getDeleteHoltFolderFileListener());
            item.setText(TreeMenu.DELETE_HOT_HOLDER_FILE);
        }
        item = new MenuItem(_menu, SWT.SEPARATOR);
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getClipboardListener());
        item.setText(TreeMenu.COPY_TO_CLIPBOARD);
        _menu.addListener(SWT.Show, new Listener() {

            @Override
            public void handleEvent(Event e) {
                if (_copy == null) {
                    disableMenu();
                }
                if (_tree.getSelectionCount() > 0) {
                    Element element = getElement();
                    if (element != null) {
                        getItem(TreeMenu.EDIT_XML).setEnabled(true);
                        getItem(TreeMenu.SHOW_XML).setEnabled(true);
                        getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true);
                        if (_dom instanceof SchedulerDom) {
                            SchedulerDom curDom = (SchedulerDom) _dom;
                            if (curDom.isLifeElement()) {
                                getItem(TreeMenu.DELETE_HOT_HOLDER_FILE).setEnabled(true);
                            }
                            if (curDom.isDirectory()) {
                                String elemName = getElement().getName();
                                if ("config".equals(elemName)) {
                                    getItem(TreeMenu.EDIT_XML).setEnabled(false);
                                }
                            }
                        }
                        getItem(TreeMenu.COPY).setEnabled(isCopyItem(((TreeData) _tree.getSelection()[0].getData()).getType()));
                        if (_copy != null) {
                            MenuItem _paste = getItem(TreeMenu.PASTE);
                            _paste.setEnabled(true);
                        }
                        if (!getParentItemName().isEmpty()) {
                            getItem(TreeMenu.NEW).setEnabled(true);
                        }
                        getItem(TreeMenu.DELETE).setEnabled(false);
                        if (_dom instanceof SchedulerDom && !((SchedulerDom) _dom).isLifeElement() && getItemElement() != null) {
                            TreeData data = (TreeData) _tree.getSelection()[0].getData();
                            _type = ((TreeData) _tree.getSelection()[0].getData()).getType();
                            boolean del = false;
                            if (_type == JOEConstants.JOB || _type == JOEConstants.JOB_CHAIN || _type == JOEConstants.SCHEDULE
                                    || _type == JOEConstants.ORDER || _type == JOEConstants.MONITOR) {
                                del = true;
                            }
                            getItem(TreeMenu.DELETE).setEnabled(del);
                        }
                    }
                }
            }
        });
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.EDIT_XML);
        item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getXMLListener());
        item.setText(TreeMenu.SHOW_XML);
    }

    public Menu getMenu() {
        return _menu;
    }

    private void disableMenu() {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++) {
            items[i].setEnabled(false);
        }
    }

    private String getXML() {
        Element element = getElement();
        String xml = "";
        if (element != null) {
            try {
                xml = _dom.getXML(element);
            } catch (JDOMException ex) {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
        }
        return xml;
    }

    private String getXML(Element element) {
        String xml = "";
        if (element != null) {
            try {
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
                    xml = _dom.getXML(Utils.getHotFolderParentElement(element));
                } else {
                    xml = _dom.getXML(element);
                }
            } catch (JDOMException ex) {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
                message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
                return null;
            }
        }
        return xml;
    }

    private Listener getXMLListener() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                MenuItem i = (MenuItem) e.widget;
                Element element = null;
                String xml = null;
                if (i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
                    if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isLifeElement() || ((SchedulerDom) _dom).isDirectory())) {
                        element = getElement();
                    } else {
                        element = _dom.getRoot().getChild("config");
                    }
                    if (element != null) {
                        xml = getXML(element);
                    }
                } else {
                    element = getElement();
                    if (element != null) {
                        xml = getXML(element);
                    }
                }
                if (element != null) {
                    if (xml == null) {
                        return;
                    }
                    String selectStr = Utils.getAttributeValue("name", getElement());
                    selectStr = selectStr == null || selectStr.isEmpty() ? getElement().getName() : selectStr;
                    String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML), selectStr);
                    if (newXML != null) {
                        applyXMLChange(newXML);
                    }
                }
            }
        };
    }

    private void applyXMLChange(String newXML) {
        String newName = "";
        String oldname = "";
        try {
            if (_dom instanceof SchedulerDom) {
                newName = getHotFolderName(newXML);
                if (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
                    String enco = " ";
                    if (newXML.indexOf("?>") > -1) {
                        enco = newXML.substring(0, newXML.indexOf("?>") + "?>".length());
                        newXML = newXML.substring(newXML.indexOf("?>") + "?>".length());
                    }
                    String xml = "";
                    if (((SchedulerDom) _dom).isDirectory()) {
                        xml = Utils.getElementAsString(_dom.getRoot().getChild("config"));
                    } else {
                        xml = Utils.getElementAsString(_dom.getRoot());
                    }
                    String oldxml = Utils.getElementAsString(getElement());
                    oldname = getHotFolderName(oldxml);
                    int iPosBegin = 0;
                    if (oldxml.indexOf("\r\n") > -1) {
                        iPosBegin = xml.indexOf(oldxml.substring(0, oldxml.indexOf("\r\n")));
                    } else {
                        iPosBegin = xml.indexOf(oldxml);
                    }
                    if (iPosBegin == -1) {
                        iPosBegin = 0;
                    }
                    int iPosEnd = xml.indexOf("</" + getElement().getName() + ">", iPosBegin);
                    if (iPosEnd == -1) {
                        iPosEnd = xml.indexOf("/>", iPosBegin) + "/>".length();
                    } else {
                        iPosEnd = iPosEnd + ("</" + getElement().getName() + ">").length();
                    }
                    newXML = xml.substring(0, iPosBegin) + newXML + xml.substring(iPosEnd);
                    if (((SchedulerDom) _dom).isLifeElement()) {
                        newXML = enco + newXML;
                    } else {
                        newXML = enco + "<spooler>" + newXML + "</spooler>";
                    }
                } else if (!((SchedulerDom) _dom).isLifeElement()) {
                    newXML = newXML.replaceAll("\\?>", "?><spooler>") + "</spooler>";
                }
            }
            _dom.readString(newXML, false);
            _gui.update();
            _dom.setChanged(true);
            Element elem = null;
            if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
                elem = getElement();
                if (!"".equals(newName) && !Utils.getAttributeValue("name", elem).equals(newName)
                        && ("order".equals(elem.getName()) || "add_order".equals(elem.getName()))) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(),
                            Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else if (!"".equals(newName) && !Utils.getAttributeValue("name", elem).equals(newName)) {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    ((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
                } else {
                    ((SchedulerDom) _dom).setChangedForDirectory(elem, SchedulerDom.NEW);
                }
            }
            if (_dom instanceof SchedulerDom && ((SchedulerDom) (_dom)).isLifeElement() && oldname != null && newName != null
                    && !oldname.equals(newName) && _dom.getFilename() != null) {
                String parent = "";
                if (_dom.getFilename() != null && new File(_dom.getFilename()).getParent() != null) {
                    parent = new File(_dom.getFilename()).getParent();
                }
                File oldFilename = new File(parent, oldname + "." + getElement().getName() + ".xml");
                File newFilename = null;
                if (oldFilename != null && oldFilename.getParent() != null) {
                    newFilename = new File(oldFilename.getParent(), newName + "." + getElement().getName() + ".xml");
                } else {
                    newFilename = new File(parent, newName + "." + getElement().getName() + ".xml");
                }
                int c = MainWindow.message(MainWindow.getSShell(), "Do you want really rename Hot Folder File from " + oldFilename + " to " + newFilename + "?",
                        SWT.ICON_WARNING | SWT.YES | SWT.NO);
                if (c == SWT.YES) {
                    _gui.updateJob(newName);
                    if (_dom.getFilename() != null) {
                        oldFilename.renameTo(newFilename);
                        _dom.setFilename(newFilename.getCanonicalPath());
                    }
                    if (MainWindow.getContainer().getCurrentEditor().applyChanges()) {
                        MainWindow.getContainer().getCurrentEditor().save();
                        MainWindow.setSaveStatus();
                    }
                } else {
                    return;
                }
            }
            _gui.update();
            _gui.updateTree("main");
            refreshTree("main");
        } catch (Exception de) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), de);
            MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING);
        }
    }

    private Listener getCopyListener() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    _copy = (Element) element.clone();
                    _type = ((TreeData) _tree.getSelection()[0].getData()).getType();
                }
            }
        };
    }

    private Listener getNewItemSelection() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                String name = getParentItemName();
                if (name.equals(SchedulerListener.JOBS)) {
                    sos.scheduler.editor.conf.listeners.JobsListener listeners =
                            new sos.scheduler.editor.conf.listeners.JobsListener((SchedulerDom) _dom, _gui);
                    listeners.newJob(sos.scheduler.editor.conf.forms.JobsForm.getTable(), false);
                } else if (name.equals(SchedulerListener.MONITORS)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    org.eclipse.swt.widgets.Table table = sos.scheduler.editor.conf.forms.ScriptsForm.getTable();
                    sos.scheduler.editor.conf.listeners.ScriptsListener listener =
                            new sos.scheduler.editor.conf.listeners.ScriptsListener((SchedulerDom) _dom, _gui, data.getElement());
                    listener.save(table, "monitor" + table.getItemCount(), String.valueOf(table.getItemCount()), null);
                } else if (name.equals(SchedulerListener.JOB_CHAINS)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.JobChainsListener listeners =
                            new sos.scheduler.editor.conf.listeners.JobChainsListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listeners.newChain();
                    int i = 1;
                    if (data.getElement().getChild("job_chains") != null) {
                        i = data.getElement().getChild("job_chains").getChildren("job_chain").size() + 1;
                    }
                    listeners.applyChain("job_chain" + i, true, true);
                    listeners.fillChains(JobChainsForm.getTableChains());
                } else if (name.equals(SchedulerListener.SCHEDULES)) {
                    sos.scheduler.editor.conf.listeners.SchedulesListener listener =
                            new sos.scheduler.editor.conf.listeners.SchedulesListener((SchedulerDom) _dom, _gui);
                    listener.newSchedule(sos.scheduler.editor.conf.forms.SchedulesForm.getTable());
                } else if (name.equals(SchedulerListener.ORDERS)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.OrdersListener listener =
                            new sos.scheduler.editor.conf.listeners.OrdersListener((SchedulerDom) _dom, _gui, data.getElement());
                    listener.newCommands(sos.scheduler.editor.conf.forms.OrdersForm.getTable());
                } else if (name.equals(SchedulerListener.WEB_SERVICES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    sos.scheduler.editor.conf.listeners.WebservicesListener listener =
                            new sos.scheduler.editor.conf.listeners.WebservicesListener((SchedulerDom) _dom, data.getElement(), _gui);
                    listener.newService(sos.scheduler.editor.conf.forms.WebservicesForm.getTable());
                } else if (name.equals(SchedulerListener.LOCKS)) {
                    try {
                        TreeData data = (TreeData) _tree.getSelection()[0].getData();
                        sos.scheduler.editor.conf.listeners.LocksListener listener =
                                new sos.scheduler.editor.conf.listeners.LocksListener((SchedulerDom) _dom, data.getElement());
                        listener.newLock();
                        int i = 1;
                        if (data.getElement().getChild("locks") != null) {
                            i = data.getElement().getChild("locks").getChildren("lock").size() + 1;
                        }
                        listener.applyLock("lock_" + i, 0, true);
                        listener.fillTable(sos.scheduler.editor.conf.forms.LocksForm.getTable());
                        listener.selectLock(i - 1);
                    } catch (Exception es) {
                        new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                    }
                } else if (name.equals(SchedulerListener.PROCESS_CLASSES)) {
                    TreeData data = (TreeData) _tree.getSelection()[0].getData();
                    try {
                        sos.scheduler.editor.conf.listeners.ProcessClassesListener listener =
                                new sos.scheduler.editor.conf.listeners.ProcessClassesListener((SchedulerDom) _dom, data.getElement());
                        listener.newProcessClass();
                        int i = 1;
                        if (data.getElement().getChild("process_classes") != null) {
                            i = data.getElement().getChild("process_classes").getChildren("process_class").size() + 1;
                        }
                        listener.applyProcessClass("processClass_" + i, "", 0, -1);
                        listener.fillProcessClassesTable(sos.scheduler.editor.conf.forms.ProcessClassesForm.getTable());
                        listener.selectProcessClass(i - 1);
                    } catch (Exception es) {
                        new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), es);
                    }
                }
            }
        };
    }

    private Listener getDeleteSelection() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                Element elem = getItemElement();
                if (elem == null) {
                    return;
                }
                String name = elem.getName();
                int c = MainWindow.message("Do you want remove the " + name + "?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (c != SWT.YES) {
                    return;
                }
                _dom.setChanged(true);
                if ("job".equals(name)) {
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1 && ((TreeData) parentItem.getData()).getElement().getChild("jobs") != null) {
                        ((TreeData) parentItem.getData()).getElement().getChild("jobs").detach();
                    }
                    _gui.refreshTree();
                    _gui.updateCMainForm();
                    _dom.setChanged(true);
                } else if ("monitor".equals(name)) {
                    ((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem.getParentElement()), SchedulerDom.MODIFY);
                    elem.detach();
                    _gui.updateJobs();
                } else if ("job_chain".equals(name)) {
                    ((SchedulerDom) _dom).setChangedForDirectory("job_chain", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1) {
                        ((TreeData) parentItem.getData()).getElement().getChild("job_chains").detach();
                    }
                    _gui.updateJobChains();
                    _gui.updateCMainForm();
                } else if ("schedule".equals(name)) {
                    ((SchedulerDom) _dom).setChangedForDirectory("schedule", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    if (parentItem.getItemCount() == 1) {
                        ((TreeData) parentItem.getData()).getElement().getChild("schedules").detach();
                    }
                    _gui.updateSchedules();
                    _gui.updateCMainForm();
                } else if ("order".equals(name) || "add_order".equals(name)) {
                    ((SchedulerDom) _dom).setChangedForDirectory("order",
                            Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    _gui.updateOrders();
                    _gui.updateCMainForm();
                } else if ("web_service".equals(name)) {
                    elem.detach();
                    TreeItem parentItem = _tree.getSelection()[0].getParentItem();
                    _tree.setSelection(new TreeItem[] { parentItem });
                    _gui.updateWebServices();
                    _gui.updateCMainForm();
                }
            }
        };
    }

    private Listener getDeleteHoltFolderFileListener() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                String filename = _dom.getFilename();
                if (filename == null) {
                    MainWindow.message("file name is null. Could not remove file.", SWT.ICON_WARNING | SWT.OK);
                    return;
                }
                int ok = MainWindow.message("Do you want really remove live file: " + filename, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                if (ok == SWT.CANCEL || ok == SWT.NO) {
                    return;
                }
                if (!new java.io.File(filename).delete()) {
                    MainWindow.message("could not remove live file", SWT.ICON_WARNING | SWT.OK);
                }
                if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement()) {
                    sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
                    con.getCurrentTab().dispose();
                }
            }
        };
    }

    private Listener getClipboardListener() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                Element element = getElement();
                if (element != null) {
                    String xml = getXML();
                    if (xml == null) {
                        return;
                    }
                    if (!"config".equals(element.getName()) && xml.indexOf("<?xml") > -1) {
                        xml = xml.substring(xml.indexOf("?>") + 2);
                    }
                    Utils.copyClipboard(xml, _tree.getDisplay());
                }
            }
        };
    }

    private Listener getPasteListener() {
        return new Listener() {

            @Override
            public void handleEvent(Event e) {
                if (_tree.getSelectionCount() == 0) {
                    return;
                }
                TreeData data = (TreeData) _tree.getSelection()[0].getData();
                boolean override = false;
                if (_tree.getSelection()[0].getData("override_attributes") != null) {
                    override = "true".equals(_tree.getSelection()[0].getData("override_attributes"));
                }
                if (_tree.getSelection()[0].getData("key") instanceof String) {
                    String key = _tree.getSelection()[0].getData("key").toString();
                    paste(key, data, override);
                } else if (_tree.getSelection()[0].getData("key") instanceof ArrayList) {
                    ArrayList keys = (ArrayList) _tree.getSelection()[0].getData("key");
                    for (int i = 0; i < keys.size(); i++) {
                        paste(keys.get(i).toString(), data, override);
                    }
                }
                }
        };
    }

    private boolean existJobname(Element jobs, String jobname) {
        boolean retVal = false;
        List list = jobs.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element job = (Element) list.get(i);
            if (Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    private void refreshTree(String whichItem) {
        sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
        SchedulerForm sf = (SchedulerForm) (con.getCurrentEditor());
        sf.updateTree(whichItem);
    }

    private MenuItem getItem(String name) {
        MenuItem[] items = _menu.getItems();
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item.getText().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private boolean isParent(String key, String elemname) {
        String[] split = key.split("_@_");
        if (split.length > 1) {
            key = split[0];
        }
        String[] s = null;
        if (_dom.getDomOrders().get(key) != null) {
            s = _dom.getDomOrders().get(key);
        } else if (sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key) != null) {
            return true;
        } else {
            s = getPossibleParent(key);
        }
        for (int i = 0; s != null && i < s.length; i++) {
            if (s[i].equalsIgnoreCase(elemname)) {
                return true;
            }
        }
        return false;
    }

    private String[] getPossibleParent(String key) {
        if ("jobs".equals(key) || "monitor".equals(key)) {
            return new String[] { "job" };
        } else if ("schedules".equals(key)) {
            return new String[] { "schedule" };
        } else if ("job_chains".equals(key)) {
            return new String[] { "job_chain" };
        } else {
            return new String[] {};
        }
    }

    private void paste(String key, TreeData data, boolean overrideAttributes) {
        try {
            if (_type != data.getType()) {
                if (_type != JOEConstants.JOB && _type != JOEConstants.JOB_CHAIN && _type != JOEConstants.SCHEDULE && _type != JOEConstants.ORDER
                        && _type != JOEConstants.MONITOR) {
                    return;
                }
                pasteChild(key, data);
                return;
            }
            Element target = _copy;
            boolean isLifeElement = _dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement();
            if (key.equalsIgnoreCase(target.getName()) && !isLifeElement) {
                Element currElem = data.getElement();
                removeAttributes(currElem);
                copyAttr(currElem, _copy.getAttributes());
            } else {
                Element currElem = data.getElement();
                Element copyElement = _copy;
                String[] split = null;
                split = key.split("_@_");
                if (split.length > 1) {
                    key = split[split.length - 1];
                }
                List ce = null;
                if (key.equals(copyElement.getName())) {
                    removeAttributes(currElem);
                    currElem.removeContent();
                    copyAttr(currElem, copyElement.getAttributes());
                    ce = copyElement.getChildren();
                } else {
                    for (int i = 0; i < split.length - 1; i++) {
                        copyElement = copyElement.getChild(split[i]);
                        if (currElem.getChild(split[i]) == null) {
                            currElem = currElem.addContent(new Element(split[i]));
                        }
                        currElem = data.getElement().getChild(split[i]);
                    }
                    ce = copyElement.getChildren(key);
                }
                for (int i = 0; i < ce.size(); i++) {
                    Element a = (Element) ce.get(i);
                    Element cloneElement = (Element) a.clone();
                    List currElemContent = null;
                    if (_tree.getSelection()[0].getData("max_occur") != null && "1".equals(_tree.getSelection()[0].getData("max_occur"))) {
                        if (currElem.getChild(key) != null) {
                            currElemContent = currElem.getChild(key).cloneContent();
                        }
                        currElem.removeChild(key);
                    }
                    if (!"".equals(Utils.getAttributeValue("name", cloneElement))
                            && existJobname(currElem, Utils.getAttributeValue("name", cloneElement))) {
                        String append =
                                "copy(" + (cloneElement.getChildren("job").size() + currElem.getChildren().size() + 1) + ")of_"
                                        + Utils.getAttributeValue("name", cloneElement);
                        cloneElement.setAttribute("name", append);
                    }
                    currElem.addContent(cloneElement);
                    if (currElemContent != null) {
                        cloneElement.addContent(currElemContent);
                    }
                    if (overrideAttributes) {
                        copyElement = cloneElement;
                        currElem = currElem.getChild(key);
                    }
                }
                if (overrideAttributes) {
                    removeAttributes(currElem);
                    copyAttr(currElem, copyElement.getAttributes());
                }
            }
            updateTreeView(data);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    private void pasteChild(String key, TreeData data) {
        if ("monitor".equalsIgnoreCase(key) && _type != data.getType()) {
            data.getElement().addContent((Element) _copy.clone());
        } else if (!isParent(key, _copy.getName())) {
            return;
        } else {
            String[] split = key.split("_@_");
            Element elem = data.getElement();
            for (int i = 0; i < split.length - 1; i++) {
                if (data.getElement().getChild(split[i]) == null) {
                    data.getElement().addContent(new Element(split[i]));
                }
                elem = data.getElement().getChild(split[i]);
            }
            Element copyClone = (Element) _copy.clone();
            if (!"".equals(Utils.getAttributeValue("name", _copy)) && existJobname(elem, Utils.getAttributeValue("name", _copy))) {
                String append =
                        "copy(" + (copyClone.getChildren("job").size() + elem.getChildren().size() + 1) + ")of_"
                                + Utils.getAttributeValue("name", copyClone);
                copyClone.setAttribute("name", append);
            } else if (!"".equals(Utils.getAttributeValue("id", _copy))) {
                String append = "copy(" + (elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("id", copyClone);
                copyClone.setAttribute("id", append);
            }

            _dom.setChanged(true);
            Utils.setChangedForDirectory(copyClone, (SchedulerDom) _dom);

            elem.addContent(copyClone);
        }
        updateTreeView(data);
    }

    private void removeAttributes(Element elem) {
        List l = elem.getAttributes();
        for (int i = 0; i < l.size(); i++) {
            elem.removeAttribute((org.jdom.Attribute) l.get(i));
        }
    }

    private void copyAttr(Element elem, List attr) {
        for (int i = 0; i < attr.size(); i++) {
            org.jdom.Attribute a = (org.jdom.Attribute) attr.get(i);
            elem.setAttribute(a.getName(), a.getValue());
        }
    }

    private void updateTreeView(TreeData data) {
        if (_type == JOEConstants.SPECIFIC_WEEKDAYS) {
            _gui.updateSpecificWeekdays();
        }
        _gui.update();
        if ("Jobs".equals(_tree.getSelection()[0].getText())) {
            _gui.updateJobs();
        }
        if (_type == JOEConstants.JOB && !_tree.getSelection()[0].getText().endsWith("Jobs")) {
            _gui.updateJob();
        }
        if (_type == JOEConstants.SCHEDULES) {
            _gui.updateSchedules();
        }
        if (_type == JOEConstants.ORDERS) {
            _gui.updateOrders();
        }
        if (_type == JOEConstants.JOB_CHAINS || _type == JOEConstants.JOB_CHAIN) {
            _gui.updateJobChains();
        }
        _gui.expandItem(_tree.getSelection()[0].getText());
        _gui.updateTreeItem(_tree.getSelection()[0].getText());
        _gui.updateTree("");
        refreshTree("main");
        if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
            ((SchedulerDom) _dom).setChangedForDirectory(data.getElement(), SchedulerDom.NEW);
        }
        _dom.setChanged(true);
    }

    private String getParentItemName() {
        if (_tree.getSelectionCount() > 0) {
            String name = _tree.getSelection()[0].getText();
            if (name.equals(SchedulerListener.JOBS) || name.equals(SchedulerListener.JOB_CHAINS) || name.equals(SchedulerListener.MONITORS)
                    || name.equals(SchedulerListener.ORDERS) || name.equals(SchedulerListener.WEB_SERVICES)
                    || name.equals(SchedulerListener.SCHEDULES)) {
                return name;
            }
            if (_dom instanceof SchedulerDom && !((SchedulerDom) _dom).isLifeElement()
                    && (name.equals(SchedulerListener.LOCKS) || name.equals(SchedulerListener.PROCESS_CLASSES))) {
                return name;
            }
        }
        return "";
    }

    private Element getItemElement() {
        if (_tree.getSelectionCount() > 0) {
            Element elem = ((TreeData) (_tree.getSelection()[0].getData())).getElement();
            String name = elem.getName();
            if ("job".equals(name) || "job_chain".equals(name) || "order".equals(name) || "add_order".equals(name) || "web_service".equals(name)
                    || "monitor".equals(name) || "schedule".equals(name)) {
                return elem;
            }
        }
        return null;
    }

    private String getHotFolderName(String newXML) {
        String newName = "";
        if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
            if ("order".equals(getElement().getName()) || "add_order".equals(getElement().getName())) {
                int i1 = -1;
                int i2 = -1;
                i1 = newXML.indexOf("id=\"");
                i2 = newXML.indexOf("\"", i1 + "id=\"".length());
                if (i1 > +"id=\"".length() && i2 > i1) {
                    newName = newXML.substring(i1 + "id=\"".length(), i2) + ",";
                }
                i1 = -1;
                i2 = -1;
                i1 = newXML.indexOf("job_chain=\"");
                i2 = newXML.indexOf("\"", i1 + "job_chain=\"".length());
                if (i1 > +"job_chain=\"".length() && i2 > i1) {
                    newName = newName + newXML.substring(i1 + +"job_chain=\"".length(), i2);
                }
            } else {
                int i1 = newXML.indexOf("name=\"") + "name=\"".length();
                int i2 = newXML.indexOf("\"", i1);
                newName = newXML.substring(i1, i2);
            }
        }
        return newName;
    }

}