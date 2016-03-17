package sos.scheduler.editor.app;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

import com.sos.event.service.forms.ActionsForm;
import com.sos.event.service.listeners.ActionsListener;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Events.ActionsDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ContextMenu {

    private SchedulerDom _dom = null;
    private Combo _combo = null;
    private Menu _menu = null;
    private static final String GOTO = "Goto";
    private static final String DELETE = "Delete";
    private static int _type = -1;

    public ContextMenu(final Combo combo, final SchedulerDom dom, final int type) {
        _combo = combo;
        _dom = dom;
        _type = type;
        createMenu();
    }

    public int message(final String message, final int style) {
        MessageBox mb = new MessageBox(_combo.getShell(), style);
        mb.setMessage(message);
        return mb.open();
    }

    private void createMenu() {
        _menu = new Menu(_combo.getShell(), SWT.POP_UP);
        MenuItem item = new MenuItem(_menu, SWT.PUSH);
        item.addListener(SWT.Selection, getListener());
        if (_type == JOEConstants.SCRIPT) {
            item.setText(ContextMenu.DELETE);
        } else {
            item.setText(ContextMenu.GOTO);
        }
        _menu.addListener(SWT.Show, new Listener() {

            @Override
            public void handleEvent(final Event e) {
                MenuItem item = null;
                if (_type == JOEConstants.SCRIPT) {
                    item = getItem(ContextMenu.DELETE);
                } else {
                    item = getItem(ContextMenu.GOTO);
                }
                if (item != null) {
                    item.setEnabled(true);
                }
            }
        });
    }

    public Menu getMenu() {
        return _menu;
    }

    private Listener getListener() {
        return new Listener() {

            @Override
            public void handleEvent(final Event e) {
                if (_type == JOEConstants.SCRIPT) {
                    delete(_combo, _dom, _type);
                } else {
                    goTo(_combo.getText(), _dom, _type);
                }
            }
        };
    }

    private MenuItem getItem(final String name) {
        MenuItem[] items = _menu.getItems();
        for (MenuItem item : items) {
            if (item.getText().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private static String removeTitle(String s) {
        if (s.contains(" - ")) {
            int p = s.indexOf(" - ");
            s = s.substring(0, p);
        }
        if (s.startsWith("*")) {
            s = s.substring(1);
        }
        return s;
    }

    public static void goTo(final String name, com.sos.joe.xml.DomParser _dom, final int type) {
        try {
            if (name == null || name.isEmpty()) {
                return;
            }
            if (type == JOEConstants.JOB) {
                XPath x3 = XPath.newInstance("//job[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    TreeItem tree = f.getTree().getItems()[0];
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().equals(SchedulerListener.JOBS)) {
                            TreeItem[] jobsItem = item.getItems();
                            for (TreeItem jItem : jobsItem) {
                                String strName = jItem.getText();
                                strName = removeTitle(strName);
                                if (strName.equals(name)) {
                                    tree.getParent().setSelection(new TreeItem[] { jItem });
                                    f.updateTreeItem(jItem.getText());
                                    f.updateTree("jobs");
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.MONITOR) {
                String[] split = name.split("_@_");
                String jobname = split[0];
                String monitorname = split[1];
                XPath x3 = XPath.newInstance("//job[@name='" + jobname + "']/monitor[@name='" + monitorname + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    if (tree.getSelection()[0].getText().equals(SchedulerListener.MONITORS)) {
                        TreeItem[] monitorsItem = tree.getSelection()[0].getItems();
                        for (TreeItem monitor : monitorsItem) {
                            String strName = monitor.getText();
                            strName = removeTitle(strName);
                            if (strName.equals(monitorname)) {
                                tree.setSelection(new TreeItem[] { monitor });
                                f.updateTreeItem(monitorname);
                                f.updateTree("monitor");
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < tree.getItemCount(); i++) {
                            TreeItem item = tree.getItem(i);
                            if (item.getText().equals(jobname)) {
                                TreeItem[] jobsItem = item.getItems();
                                for (TreeItem jItem : jobsItem) {
                                    if ("Monitor".equals(jItem.getText())) {
                                        TreeItem[] monitorsItem = jItem.getItems();
                                        for (TreeItem monitor : monitorsItem) {
                                            if (monitor.getText().equals(monitorname)) {
                                                tree.setSelection(new TreeItem[] { monitor });
                                                f.updateTreeItem(monitorname);
                                                f.updateTree("monitor");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.JOB_CHAIN) {
                XPath x3 = XPath.newInstance("//job_chain[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    TreeItem tree = f.getTree().getItems()[0];
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().equals(SchedulerListener.JOB_CHAINS)) {
                            TreeItem[] jobsItem = item.getItems();
                            for (TreeItem jItem : jobsItem) {
                                String strName = jItem.getText();
                                strName = removeTitle(strName);
                                if (strName.equals(name)) {
                                    tree.getParent().setSelection(new TreeItem[] { jItem });
                                    f.updateTreeItem(jItem.getText());
                                    f.updateTree("");
                                    jItem.setExpanded(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.PROCESS_CLASSES) {
                XPath x3 = XPath.newInstance("//process_class[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    TreeItem tree = f.getTree().getItems()[0];
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().endsWith("Process Classes")) {
                            tree.getParent().setSelection(new TreeItem[] { item });
                            f.updateTreeItem(item.getText());
                            f.updateTree("");
                            break;
                        }
                    }
                }
            } else if (type == JOEConstants.SCHEDULE) {
                XPath x3 = XPath.newInstance("//schedule[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    TreeItem tree = f.getTree().getItems()[0];
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().equals(SchedulerListener.SCHEDULES)) {
                            TreeItem[] items = item.getItems();
                            for (TreeItem jItem : items) {
                                String strName = jItem.getText();
                                strName = removeTitle(strName);
                                if (strName.equals(name)) {
                                    tree.getParent().setSelection(new TreeItem[] { jItem });
                                    f.updateTreeItem(jItem.getText());
                                    f.updateTree("");
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.ORDER) {
                XPath x3 = XPath.newInstance("//order[@id='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (listOfElement_3.isEmpty()) {
                    x3 = XPath.newInstance("//add_order[@id='" + name + "']");
                    listOfElement_3 = x3.selectNodes(_dom.getDoc());
                }
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    TreeItem tree = f.getTree().getItems()[0];
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().equals(SchedulerListener.ORDERS)) {
                            TreeItem[] items = item.getItems();
                            for (TreeItem jItem : items) {
                                String strName = jItem.getText();
                                strName = removeTitle(strName);
                                if (strName.equals(name)) {
                                    tree.getParent().setSelection(new TreeItem[] { jItem });
                                    f.updateTreeItem(jItem.getText());
                                    f.updateTree("");
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.WEBSERVICE) {
                XPath x3 = XPath.newInstance("//web_service[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if (item.getText().equals(SchedulerListener.HTTP_SERVER)) {
                            for (int k = 0; k < item.getItemCount(); k++) {
                                TreeItem httpItem = item.getItem(k);
                                if (httpItem.getText().equals(SchedulerListener.WEB_SERVICES)) {
                                    TreeItem[] items = httpItem.getItems();
                                    for (TreeItem jItem : items) {
                                        if (jItem.getText().equals("Web Service: " + name)) {
                                            tree.setSelection(new TreeItem[] { jItem });
                                            f.updateTreeItem(jItem.getText());
                                            f.updateTree("");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.ACTIONS) {
                XPath x3 = XPath.newInstance("//action[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    for (int i = 0; i < tree.getItemCount(); i++) {
                        TreeItem item = tree.getItem(i);
                        if ("Actions".equals(item.getText())) {
                            TreeItem[] jobsItem = item.getItems();
                            for (TreeItem jItem : jobsItem) {
                                if (jItem.getText().endsWith(ActionsListener.ACTION_PREFIX + name)) {
                                    tree.setSelection(new TreeItem[] { jItem });
                                    f.updateTreeItem(jItem.getText());
                                    f.updateTree("");
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.EVENTS) {
                XPath x3 = XPath.newInstance("//event_group[@group='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    if (tree.getSelectionCount() > 0) {
                        TreeItem itemp = tree.getSelection()[0];
                        for (int i = 0; i < itemp.getItemCount(); i++) {
                            TreeItem item = itemp.getItem(i);
                            if (item.getText().endsWith(ActionsListener.GROUP_PREFIX + name)) {
                                tree.setSelection(new TreeItem[] { item });
                                f.updateTreeItem(item.getText());
                                f.updateTree("");
                                break;
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.ACTION_COMMANDS) {
                XPath x3 = XPath.newInstance("//command[@name='" + name + "']");
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    if (tree.getSelectionCount() > 0) {
                        TreeItem itemp = tree.getSelection()[0];
                        for (int i = 0; i < itemp.getItemCount(); i++) {
                            TreeItem item = itemp.getItem(i);
                            if (item.getText().endsWith(ActionsListener.COMMAND_PREFIX + name)) {
                                tree.setSelection(new TreeItem[] { item });
                                f.updateTreeItem(item.getText());
                                f.updateTree("");
                                break;
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.JOB_COMMAND_EXIT_CODES
                    && sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor() instanceof ActionsForm) {
                XPath x3 = null;
                String job = "";
                if (name.startsWith("start_job")) {
                    job = name.substring("start_job: ".length());
                    x3 = XPath.newInstance("//command/start_job[@job='" + job + "']");
                } else {
                    String child = name.substring(0, name.indexOf(": "));
                    job = name.substring(child.length() + 2);
                    x3 = XPath.newInstance("//command/" + child + "[@job_chain='" + job + "']");
                }
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    if (tree.getSelectionCount() > 0) {
                        TreeItem itemp = tree.getSelection()[0];
                        for (int i = 0; i < itemp.getItemCount(); i++) {
                            TreeItem item = itemp.getItem(i);
                            if (item.getText().equals(name)) {
                                tree.setSelection(new TreeItem[] { item });
                                f.updateTreeItem(item.getText());
                                f.updateTree("");
                                break;
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.JOB_COMMAND_EXIT_CODES
                    && sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {
                XPath x3 = null;
                String job = "";
                if (name.startsWith("start_job")) {
                    job = name.substring("start_job: ".length());
                    x3 = XPath.newInstance("//commands/start_job[@job='" + job + "']");
                } else {
                    String child = name.substring(0, name.indexOf(": "));
                    job = name.substring(child.length() + 2);
                    x3 = XPath.newInstance("//commands/" + child + "[@job_chain='" + job + "']");
                }
                List listOfElement_3 = x3.selectNodes(_dom.getDoc());
                if (!listOfElement_3.isEmpty()) {
                    SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                    if (f == null) {
                        return;
                    }
                    Tree tree = f.getTree();
                    if (tree.getSelectionCount() > 0) {
                        TreeItem itemp = tree.getSelection()[0];
                        for (int i = 0; i < itemp.getItemCount(); i++) {
                            TreeItem item = itemp.getItem(i);
                            if (item.getText().equals(name)) {
                                tree.setSelection(new TreeItem[] { item });
                                f.updateTreeItem(item.getText());
                                f.updateTree("");
                                break;
                            }
                        }
                    }
                }
            } else if (type == JOEConstants.JOB_COMMAND) {
                SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
                if (f == null) {
                    return;
                }
                Tree tree = f.getTree();
                if (tree.getSelectionCount() > 0) {
                    TreeItem itemp = tree.getSelection()[0];
                    for (int i = 0; i < itemp.getItemCount(); i++) {
                        TreeItem item = itemp.getItem(i);
                        if (item.getText().equals(name)) {
                            tree.setSelection(new TreeItem[] { item });
                            f.updateTreeItem(item.getText());
                            f.updateTree("");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void delete(final Combo combo, final com.sos.joe.xml.DomParser _dom, final int type) {
        try {
            if (combo.getData("favorites") == null) {
                return;
            }
            if (type == JOEConstants.SCRIPT) {
                String prefix = "monitor_favorite_";
                String name = combo.getText();
                String lan = "";
                if (combo.getData("favorites") != null) {
                    lan = ((HashMap) combo.getData("favorites")).get(name) + "_";
                }
                name = prefix + lan + name;
                Options.removeProperty(name);
                combo.remove(combo.getText());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}