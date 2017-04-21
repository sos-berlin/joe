package sos.scheduler.editor.conf.forms;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.TabbedContainer;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class SchedulerForm extends SOSJOEMessageCodes implements ISchedulerUpdate, IEditor {

    private SchedulerDom dom = null;
    private SchedulerListener listener = null;
    private IContainer container = null;
    private TreeItem selection = null;
    private SashForm sashForm = null;
    private Group gTree = null;
    private Tree tree = null;
    private Composite cMainForm = null;
    private boolean editOrders=false;
    
    public SchedulerForm(final IContainer container1, final Composite parent, final int style) {
        super(parent, style);
        container = container1;
        dom = new SchedulerDom();
        dom.setDataChangedListener(this);
        listener = new SchedulerListener(this, dom);
    }

    public SchedulerForm(final IContainer container1, final Composite parent, final int style, final int type) {
        super(parent, style);
        container = container1;
        dom = new SchedulerDom(type);
        dom.setDataChangedListener(this);
        listener = new SchedulerListener(this, dom);
    }

    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 0;
        fillLayout.marginWidth = 5;
        fillLayout.marginHeight = 5;
        setSize(new Point(783, 450));
        setLayout(fillLayout);
        createSashForm();
    }

    private void createSashForm() {
        sashForm = new SashForm(this, SWT.NONE);
        createGTree();
        createCMainForm();
        sashForm.setWeights(new int[] { 176, 698 });
        Options.loadSash("main", sashForm);
    }

    private void createGTree() {
        gTree = JOE_G_SchedulerForm_SchedulerElements.control(new Group(sashForm, SWT.NONE));
        gTree.setLayout(new FillLayout());
        tree = new Tree(gTree, SWT.BORDER);
        tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
        tree.addListener(SWT.MenuDetect, new Listener() {

            @Override
            public void handleEvent(final Event e) {
                e.doit = tree.getSelectionCount() > 0;
            }
        });
        tree.addListener(SWT.MouseDown, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                Point point = new Point(event.x, event.y);
                TreeItem item = tree.getItem(point);
            }
        });
        Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
        final DragSource source = new DragSource(tree, operations);
        source.setTransfer(types);
        final TreeItem[] dragSourceItem = new TreeItem[1];
        source.addDragListener(new DragSourceListener() {

            @Override
            public void dragStart(final DragSourceEvent event) {
                TreeItem[] selection = tree.getSelection();
                if (selection.length > 0 && selection[0].getItemCount() == 0) {
                    event.doit = true;
                    dragSourceItem[0] = selection[0];
                } else {
                    event.doit = false;
                }
            }

            @Override
            public void dragSetData(final DragSourceEvent event) {
                event.data = dragSourceItem[0].getText();
            }

            @Override
            public void dragFinished(final DragSourceEvent event) {
                if (event.detail == DND.DROP_MOVE) {
                    dragSourceItem[0].dispose();
                }
                dragSourceItem[0] = null;
            }
        });
        tree.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (tree.getSelectionCount() > 0) {
                    if (selection == null) {
                        selection = tree.getItem(0);
                    }
                    e.doit = listener.treeSelection(tree, cMainForm);
                    if (!e.doit) {
                        tree.setSelection(new TreeItem[] { selection });
                    } else {
                        selection = tree.getSelection()[0];
                    }
                }
            }
        });
    }

    private void createCMainForm() {
        cMainForm = new Composite(sashForm, SWT.NONE);
        cMainForm.setLayout(new FillLayout());
    }

    public Shell getSShell() {
        return this.getShell();
    }

    @Override
    public void dataChanged() {
        container.setStatusInTitle();
    }

    public void dataChanged(final CTabItem tab) {
        ((TabbedContainer) container).setStatusInTitle(tab);
    }

    @Override
    public void updateExitCodesCommand() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillExitCodesCommands(item, data.getElement(), true);
            item.setExpanded(true);
            if (item.getItemCount() > 0) {
                item.getItems()[item.getItemCount() - 1].setExpanded(true);
            }
        }
    }

    @Override
    public void updateCommands() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
        }
    }

    @Override
    public void updateDays(final int type) {
        updateDays(type, null);
    }

    @Override
    public void updateDays(final int type, final String name) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillDays(item, data.getElement(), type, true, name);
            item.setExpanded(true);
        }
    }

    @Override
    public void updateJob() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillJob(item, data.getElement(), true);
        }
    }

    @Override
    public void updateJob(final Element elem) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            String job = Utils.getAttributeValue("name", elem);
            item.setText(job);
            TreeData data = (TreeData) item.getData();
            data.setElement(elem);
            listener.treeFillJob(item, data.getElement(), true);
            listener.treeSelection(tree, cMainForm);
        }
    }

    @Override
    public void updateJob(final String job) {
        TreeItem item = tree.getSelection()[0];
        TreeData data = (TreeData) item.getData();
        if (data != null) {
            org.jdom.Element element = data.getElement();
            listener.setColorOfJobTreeItem(element, item);
            item.setText(job);
        }
    }

    @Override
    public void updateJobs() {
        if (!tree.getSelection()[0].getText().startsWith(SchedulerListener.JOBS) && !tree.getSelection()[0].getText().startsWith("Steps")) {
            updateJobs_();
        } else if (tree.getSelectionCount() > 0) {
            listener.treeFillJobs(tree.getSelection()[0]);
        }
    }

    @Override
    public void expandItem(final String name) {
        listener.treeExpandJob(tree.getSelection()[0], name);
    }

    private void updateJobs_() {
        if (tree.getSelectionCount() > 0) {
            for (int i = 0; i < tree.getItemCount(); i++) {
                TreeItem ti = tree.getItem(i);
                if ("Jobs".equalsIgnoreCase(ti.getText())) {
                    listener.treeFillJobs(ti);
                }
            }
        }
    }

    @Override
    public void updateOrder(final String s) {
        TreeItem item = tree.getSelection()[0];
        item.setText(s);
    }

    @Override
    public void updateOrders() {
        for (int i = 0; i < listener.mainTreeItems.size(); i++) {
            TreeItem t = listener.mainTreeItems.get(i);
            if (!t.isDisposed()) {
                TreeData td = (TreeData) t.getData();
                if (td.getType() == JOEConstants.ORDERS) {
                    listener.treeFillOrders(t, true);
                }
            }
        }
    }

    @Override
    public boolean applyChanges() {
        Control[] c = cMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }

    @Override
    public void openBlank() {
        initialize();
        listener.treeFillMain(tree, cMainForm);
    }

    public void refreshTree() {
        listener.treeFillMain(tree, cMainForm);
    }

    public void openBlank(final int type) {
        initialize();
        listener.treeFillMain(tree, cMainForm, type);
    }

    public boolean openDirectory(final String filename, final Collection files) {
        boolean res = IOUtils.openFile(filename, files, dom);
        if (res) {
            if (dom.getListOfChangeElementNames() != null && !dom.getListOfChangeElementNames().isEmpty()) {
                dom.setChanged(true);
            }
            initialize();
            listener.treeFillMain(tree, cMainForm, SchedulerDom.DIRECTORY);
        }
        return res;
    }

    @Override
    public boolean open(final Collection files) {
        boolean res = IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm);
        }
        return res;
    }

    public boolean open(final String filename, final Collection files) {
        boolean res = IOUtils.openFile(filename, files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm);
        }
        return res;
    }

    public boolean open(final String filename, final Collection files, final int type) {
        boolean res = IOUtils.openFile(filename, files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm, type);
        }
        return res;
    }

    @Override
    public boolean save() {
        boolean res = true;
        if (dom.isDirectory()) {
            res = MainWindow.saveDirectory(dom, false, SchedulerDom.DIRECTORY, null, container);
        } else if (dom.isLifeElement()) {
            int type = -1;
            if ("job".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIVE_JOB;
            } else if ("job_chain".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIVE_JOB_CHAIN;
            } else if ("process_class".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIFE_PROCESS_CLASS;
            } else if ("lock".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIFE_LOCK;
            } else if ("order".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIFE_ORDER;
            } else if ("add_order".equals(dom.getRoot().getName())) {
                type = SchedulerDom.LIFE_ADD_ORDER;
            }
            res = MainWindow.saveDirectory(dom, false, type, dom.getRoot().getName(), container);
        } else {
            res = IOUtils.saveFile(dom, false);
        }
        if (res) {
            container.setNewFilename(null);
            setReChangedTreeItemText();
        }
        if ("spooler".equals(dom.getRoot().getName())) {
            Utils.setResetElement(dom.getFileName(), dom.getRoot().getChild("config"));
        } else {
            Utils.setResetElement(dom.getFileName(), dom.getRoot());
        }
        return res;
    }

    @Override
    public boolean saveAs() {
        String old = dom.getFilename();
        boolean res = IOUtils.saveFile(dom, true);
        if (dom.isLifeElement()) {
            updateLifeElement();
        }
        if (res) {
            container.setNewFilename(old);
            setReChangedTreeItemText();
        }
        return res;
    }

    @Override
    public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }

    @Override
    public boolean hasChanges() {
        Options.saveSash("main", sashForm.getWeights());
        return dom.isChanged();
    }
 
    @Override
    public String getHelpKey() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            if (item.getData("key") != null) {
                return Options.getHelpURL(item.getData("key").toString());
            } else {
                TreeData data = (TreeData) item.getData();
                if (data != null && data.getHelpKey() != null) {
                    return data.getHelpKey();
                }
            }
        }
        return null;
    }

    @Override
    public String getFilename() {
        return dom.getFilename();
    }

    @Override
    public void updateTree(final String which) {
        if ("convert".equalsIgnoreCase(which)) {
            if (dom.isLifeElement()) {
                listener.treeFillMainForLifeElement(tree, cMainForm, false);
            } else {
                listener.treeFillMain(tree, cMainForm);
            }
        } else {
            if ("main".equalsIgnoreCase(which)) {
                if (dom.isLifeElement()) {
                    listener.treeFillMainForLifeElement(tree, cMainForm, true);
                } else {
                    listener.treeFillMain(tree, cMainForm);
                }
            } else {
                listener.treeSelection(tree, cMainForm);
            }
        }
    }

    public void selectTreeItem(final String parent, final String child) {
        for (int i = 0; i < tree.getItemCount(); i++) {
            TreeItem pItem = tree.getItem(i);
            if (pItem.getText().equals(parent)) {
                for (int j = 0; j < pItem.getItemCount(); j++) {
                    TreeItem cItem = pItem.getItem(j);
                    if (cItem.getText().equals(child)) {
                        tree.setSelection(new TreeItem[] { cItem });
                        break;
                    }
                }
            }
        }
    }

    public String getTreeSelection() {
        if (tree.getSelectionCount() > 0) {
            return tree.getSelection()[0].getText();
        } else {
            return "Config";
        }
    }

    private void selectJobChain(TreeItem parent, String selectedJobchainName) {
        TreeItem[] jobchains = parent.getItems();
        for (int i = 0; i < jobchains.length; i++) {
            TreeItem t = jobchains[i];
            if (t.getText().equals(selectedJobchainName)) {
                tree.setSelection(t.getItem(0));
                t.getItem(0).setExpanded(true);
            }
        }
    }

    @Override
    public void updateJobChains() {
        String selectedJobchainName = tree.getSelection()[0].getParentItem().getText();
        for (int i = 0; i < listener.mainTreeItems.size(); i++) {
            TreeItem t = listener.mainTreeItems.get(i);
            if (!t.isDisposed()) {
                TreeData td = (TreeData) t.getData();
                if (td.getType() == JOEConstants.JOB_CHAINS) {
                    listener.treeFillJobChains(t);
                    selectJobChain(t, selectedJobchainName);
                }
            }
        }
    }

    @Override
    public void updateSelectedJobChain() {
        TreeItem selectedJobchain = tree.getSelection()[0];
        listener.treeFillJobChainNodes(selectedJobchain);

    }

    @Override
    public void updateJobChain(final String newName, final String oldName) {
        if (newName.equals(oldName)) {
            return;
        }
        if (dom.isLifeElement()) {
            TreeItem item = tree.getSelection()[0];
            item.setText(newName);
        }
        TreeItem item = tree.getSelection()[0];
        if ("Job Chains".equals(item.getText())) {
            TreeItem[] items = item.getItems();
            for (TreeItem it : items) {
                if (it.getText().equals(oldName)) {
                    it.setText(newName);
                }
            }
        } else {
            TreeItem[] parent = tree.getItems();
            for (TreeItem element : parent) {
                if ("Job Chains".equals(element.getText())) {
                    TreeItem[] items = element.getItems();
                    for (TreeItem it : items) {
                        if (it.getText().equals(oldName)) {
                            it.setText(newName);
                        }
                    }
                }
            }
        }
    }

    public void updateLifeElement() {
        TreeItem item = tree.getItem(0);
        tree.setSelection(new TreeItem[] { item });
        TreeData data = (TreeData) item.getData();
        org.jdom.Element elem = data.getElement();
        if ("job".equals(elem.getName())) {
            updateJob(Utils.getAttributeValue("name", elem));
            updateJob();
        } else if ("job_chain".equals(elem.getName())) {
            updateJobChain(item.getText(0), Utils.getAttributeValue("name", elem));
        } else if ("add_order".equals(elem.getName()) || "order".equals(elem.getName())) {
            updateOrder(Utils.getAttributeValue("id", elem));
        } else if ("config".equals(elem.getName())) {
            if (elem.getChild("process_classes") != null) {
                Element process_classes = elem.getChild("process_classes");
                Element pc = process_classes.getChild("process_class");
                pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
            }
            if (elem.getChild("locks") != null) {
                Element locks = elem.getChild("locks");
                Element pc = locks.getChild("lock");
                pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
            }
        }
        listener.treeSelection(tree, cMainForm);
    }

    @Override
    public void updateSpecificWeekdays() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillSpecificWeekdays(item, data.getElement(), true);
        }
    }

    public Tree getTree() {
        return tree;
    }

    public SchedulerDom getDom() {
        return dom;
    }

    @Override
    public void updateSchedules() {
        if (tree.getSelectionCount() > 0) {
            listener.treeFillSchedules(tree.getSelection()[0]);
        }
    }

    @Override
    public void updateWebServices() {
        if (tree.getSelectionCount() > 0) {
            listener.treeFillWebServices(tree.getSelection()[0]);
        }
    }

    @Override
    public void updateScripts() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            org.jdom.Element elem = data.getElement();
            listener.treeFillMonitorScripts(item, elem, false);
        }
    }

    @Override
    public void updateTreeItem(final String s) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            if (item.getParentItem() != null && "Monitor".equals(item.getParentItem().getText()) && "".equals(s)) {
                item.setText("<empty>");
            } else {
                item.setText(s);
            }
        }
    }

    @Override
    public void updateFont() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            updateFont(item);
        }
    }

    @Override
    public void updateFont(final TreeItem item) {
        FontData[] fontDatas = item.getFont().getFontData();
        FontData data = fontDatas[0];
        boolean isBold = false;
        TreeData data_ = (TreeData) item.getData();
        if (data_ == null || data_.getElement() == null) {
            return;
        }
        int type = data_.getType();
        Element elem = data_.getElement();
        if (type == JOEConstants.EVERYDAY) {
            if (!elem.getChildren("period").isEmpty() || !elem.getChildren("at").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.DAYS) {
            if (!elem.getChildren("date").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.WEEKDAYS) {
            if (item.getData("key") != null && "holidays_@_weekdays".equals(item.getData("key"))) {
                if (elem.getChild("holidays") != null && !elem.getChild("holidays").getChildren("weekdays").isEmpty()) {
                    isBold = true;
                }
            } else {
                if (!elem.getChildren("weekdays").isEmpty()) {
                    isBold = true;
                }
            }
        } else if (type == JOEConstants.MONTHDAYS) {
            if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("day").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.ULTIMOS) {
            if (!elem.getChildren("ultimos").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.SPECIFIC_WEEKDAYS) {
            if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("weekday").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.SPECIFIC_MONTHS) {
            if (!elem.getChildren("month").isEmpty()) {
                isBold = true;
            }
        } else if (type == JOEConstants.RUNTIME) {
            elem = elem.getChild("run_time");
            if (elem != null && !elem.getAttributes().isEmpty()) {
                isBold = true;
            }
        }
        int intStyle = SWT.NONE;
        if (isBold) {
            intStyle = SWT.BOLD;
            Font f = SWTResourceManager.getFont(data.getName(), data.getHeight(), intStyle);
            item.setFont(f);
        }
    }

    public SchedulerListener getListener() {
        return listener;
    }

    @Override
    public void updateRunTime() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            item.removeAll();
            TreeData data = (TreeData) item.getData();
            listener.treeFillRunTimes(item, data.getElement(), !Utils.isElementEnabled("job", dom, data.getElement()), "run_time");
        }
    }

    public void updateCMainForm() {
        listener.treeSelection(tree, cMainForm);
    }

    public void setChangedTreeItemText(final String key1) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            if (!dom.isDirectory()) {
                return;
            }
            if (!dom.getChangedJob().containsKey(key1)) {
                return;
            }
            if (item.getText().endsWith(SchedulerListener.LOCKS) || item.getText().endsWith(SchedulerListener.PROCESS_CLASSES)) {
                if (!item.getText().startsWith("*")) {
                    item.setText("*" + item.getText());
                }
                return;
            }
            if (dom.getChangedJob().get(key1).equals(SchedulerDom.NEW) && !key1.startsWith("process_class")) {
                int i = item.getItemCount() - 1;
                if (i < 0) {
                    i = 0;
                }
                item = item.getItem(i);
                if (!item.getText().startsWith("*")) {
                    item.setText("*" + item.getText());
                }
                return;
            }
            while (item != null && item.getParentItem() != null) {
                String sParent = item.getParentItem().getText();
                if (sParent.equals(SchedulerListener.JOBS) || sParent.equals(SchedulerListener.ORDERS)
                        || sParent.equals(SchedulerListener.JOB_CHAINS) || sParent.equals(SchedulerListener.SCHEDULES)
                        || item.getText().equals(SchedulerListener.LOCKS) || item.getText().equals(SchedulerListener.PROCESS_CLASSES)) {
                    if (!item.getText().startsWith("*")) {
                        item.setText("*" + item.getText());
                    }
                    item = null;
                } else {
                    item = item.getParentItem();
                }
            }
        }
    }

    public void setReChangedTreeItemText() {
        for (int i = 0; i < tree.getItemCount(); i++) {
            TreeItem item = tree.getItem(i);
            setChangedItemText(item);
        }
    }

    private void setChangedItemText(final TreeItem item) {
        if (item.getText().startsWith("*")) {
            item.setText(item.getText().substring(1));
        }
        for (int j = 0; j < item.getItemCount(); j++) {
            TreeItem cItem = item.getItem(j);
            setChangedItemText(cItem);
        }
    }

    
    public boolean isEditOrders() {
        return editOrders;
    }

    
    public void setEditOrders(boolean editOrders) {
        this.editOrders = editOrders;
    }
}