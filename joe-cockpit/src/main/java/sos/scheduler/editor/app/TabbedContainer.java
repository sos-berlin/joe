package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.classes.JoeLockFolder;
import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;

import com.sos.event.service.forms.ActionsForm;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IEditorAdapter;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class TabbedContainer implements IContainer, IEditorAdapter {

    private static final String IMAGE_EDITOR_SMALL_PNG = "/sos/scheduler/editor/editor-small.png";
    private static final Logger LOGGER = LoggerFactory.getLogger(TabbedContainer.class);
    private static final String NEW_SCHEDULER_TITLE = "Unknown";
    private static final String NEW_DOCUMENTATION_TITLE = "Unknown";
    private static final String NEW_DETAIL_TITLE = "Unknown";
    private CTabFolder folder = null;
    private final ArrayList<String> filelist = new ArrayList<String>();
    private String strTitleText = "";

    class TabData {

        protected String title = "";
        protected String caption = "";
        protected int cnt = 0;

        public TabData(String title, String caption) {
            this.title = title;
            this.caption = caption;
        }
    }

    public TabbedContainer(Composite parent) {
        folder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
        folder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        initialize();
    }

    public void setSaveStatus() {
        MainWindow.setSaveStatus();
    }

    private void initialize() {
        folder.setSimple(false);
        folder.setSize(new Point(690, 478));
        folder.setLayout(new FillLayout());
        folder.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setWindowTitle();
                MainWindow.setMenuStatus();
                MainWindow.shellActivated_();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        folder.addCTabFolder2Listener(new CTabFolder2Adapter() {

            @Override
            public void close(CTabFolderEvent event) {
                IEditor editor = (IEditor) ((CTabItem) (event.item)).getControl();
                if (editor.hasChanges()) {
                    event.doit = editor.close();
                }
                if (event.doit) {
                    filelist.remove(editor.getFilename());
                    if (editor.getFilename() != null) {
                        JoeLockFolder joeLockFolder = new JoeLockFolder(editor.getFilename());
                        joeLockFolder.unLockFolder();
                    }
                }
            }
        });
        folder.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(final TraverseEvent e) {
                //
            }
        });
    }

    @Override
    public SchedulerForm newScheduler() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
        scheduler.openBlank();
        CTabItem tab = newItem(scheduler, NEW_SCHEDULER_TITLE);
        tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
        return scheduler;
    }

    @Override
    public SchedulerForm newScheduler(int type) {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, type);
        scheduler.openBlank(type);
        CTabItem tab = newItem(scheduler, NEW_SCHEDULER_TITLE);
        tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
        return scheduler;
    }

    @Override
    public JobChainConfigurationForm newDetails() {
        JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);
        detailForm.openBlank();
        CTabItem tab = newItem(detailForm, NEW_DETAIL_TITLE);
        tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
        return detailForm;
    }

    @Override
    public JobChainConfigurationForm openDetails() {
        JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);
        if (detailForm.open(filelist)) {
            CTabItem tab = newItem(detailForm, detailForm.getFilename());
            tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
            return detailForm;
        } else {
            return null;
        }
    }

    public JobChainConfigurationForm openDetails(String filename) {
        JobChainConfigurationForm detailForm = new JobChainConfigurationForm(this, folder, SWT.NONE);
        if (detailForm.open(filename, filelist)) {
            CTabItem tab = newItem(detailForm, detailForm.getFilename());
            tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
            return detailForm;
        } else {
            return null;
        }
    }

    public ActionsForm openActions(String filename) {
        ActionsForm actionsForm = new ActionsForm(this, folder, SWT.NONE);
        if (actionsForm.open(filename, filelist)) {
            CTabItem tab = newItem(actionsForm, actionsForm.getFilename());
            tab.setImage(new Image(tab.getDisplay(), getClass().getResourceAsStream(IMAGE_EDITOR_SMALL_PNG)));
            return actionsForm;
        } else {
            return null;
        }
    }

    @Override
    public SchedulerForm openScheduler() {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
        if (scheduler.open(filelist)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource(IMAGE_EDITOR_SMALL_PNG));
            return scheduler;
        } else {
            return null;
        }
    }

    @Override
    public SchedulerForm openScheduler(String filename) {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE);
        if (scheduler.open(filename, filelist)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource(IMAGE_EDITOR_SMALL_PNG));
            return scheduler;
        } else {
            return null;
        }
    }

    @Override
    public DocumentationForm newDocumentation() {
        DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
        doc.openBlank();
        newItem(doc, NEW_DOCUMENTATION_TITLE);
        return doc;
    }

    @Override
    public DocumentationForm openDocumentation() {
        try {
            DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
            if (doc.open(filelist)) {
                newItem(doc, doc.getFilename());
                return doc;
            } else {
                return null;
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            return null;
        }
    }

    @Override
    public DocumentationForm openDocumentation(String filename) {
        try {
            DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
            if (doc.open(filename, filelist)) {
                newItem(doc, doc.getFilename());
                return doc;
            } else {
                return null;
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            return null;
        }
    }

    @Override
    public String openDocumentationName() {
        try {
            DocumentationForm doc = new DocumentationForm(this, folder, SWT.NONE);
            if (doc.open(filelist)) {
                return doc.getFilename();
            } else {
                return null;
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            return null;
        }
    }

    private String shortCaption(String caption) {
        File f = new File(caption);
        if (caption.length() > 30 && f.getParentFile() != null && f.getParentFile().getParentFile() != null) {
            String s = "..." + f.getParentFile().getParentFile().getName() + "/" + f.getParentFile().getName() + "/" + f.getName();
            if (s.length() > 30) {
                return caption;
            } else {
                return s;
            }
        } else {
            return caption;
        }
    }

    @Override
    public void setTitleText(final String pstrTitle) {
        strTitleText = pstrTitle;
    }

    private CTabItem newItem(Control control, String filename) {
        CTabItem tab = new CTabItem(folder, SWT.NONE);
        tab.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(final DisposeEvent e) {
                MainWindow.getSShell().setText(strTitleText);
                MainWindow.setSaveStatus();
            }
        });
        tab.setControl(control);
        folder.setSelection(folder.indexOf(tab));
        String actFilename = Utils.getFileFromURL(filename);
        tab.setData(new TabData(actFilename, strTitleText));
        String title = setSuffix(tab, actFilename);
        TabData t = (TabData) tab.getData();
        t.caption = shortCaption(title);
        tab.setToolTipText(filename);
        tab.setText(title);
        if (!NEW_DOCUMENTATION_TITLE.equals(filename) && !NEW_SCHEDULER_TITLE.equals(filename)) {
            filelist.add(filename);
        }
        return tab;
    }

    @Override
    public CTabItem getCurrentTab() {
        if (folder.getItemCount() == 0) {
            return null;
        } else {
            return folder.getItem(folder.getSelectionIndex());
        }
    }

    public CTabItem getFolderTab(String filename) {
        if (folder.getItemCount() == 0) {
            return null;
        } else {
            for (int i = 0; i < folder.getItemCount(); i++) {
                if (filelist.get(i).equals(filename)) {
                    return folder.getItem(i);
                }
            }
            return null;
        }
    }

    @Override
    public IEditor getCurrentEditor() {
        if (folder.getItemCount() == 0) {
            return null;
        } else {
            return (IEditor) getCurrentTab().getControl();
        }
    }

    public IEditor getEditor(String filename) {
        if (folder.getItemCount() == 0) {
            return null;
        } else {
            return (IEditor) getFolderTab(filename).getControl();
        }
    }

    @Override
    public void setStatusInTitle() {
        if (folder.getItemCount() == 0) {
            return;
        }
        CTabItem tab = getCurrentTab();
        TabData t = (TabData) tab.getData();
        String title = t.caption;
        if (tab.getData("ftp_profile_name") != null && !tab.getData("ftp_profile_name").toString().isEmpty() && tab.getData("ftp_remote_directory") != null && !tab.getData(
                "ftp_remote_directory").toString().isEmpty()) {
            title = tab.getData("ftp_remote_directory").toString();
        }
        if (tab.getData("webdav_profile_name") != null && !tab.getData("webdav_profile_name").toString().isEmpty() && tab.getData("webdav_remote_directory") != null && !tab
                .getData("webdav_remote_directory").toString().isEmpty()) {
            title = tab.getData("webdav_remote_directory").toString();
        }
        tab.setText(getCurrentEditor().hasChanges() == false ? title : "*" + title);
        setWindowTitle();
        MainWindow.setMenuStatus();
    }

    public void setStatusInTitle(CTabItem tab) {
        if (folder.getItemCount() == 0) {
            return;
        }
        TabData t = (TabData) tab.getData();
        String title = t.caption;
        if (tab.getData("ftp_profile_name") != null && !tab.getData("ftp_profile_name").toString().isEmpty() && tab.getData("ftp_remote_directory") != null && !tab.getData(
                "ftp_remote_directory").toString().isEmpty()) {
            title = tab.getData("ftp_remote_directory").toString();
        }
        if (tab.getData("webdav_profile_name") != null && !tab.getData("webdav_profile_name").toString().isEmpty() && tab.getData("webdav_remote_directory") != null && !tab
                .getData("webdav_remote_directory").toString().isEmpty()) {
            title = tab.getData("webdav_remote_directory").toString();
        }
        tab.setText(getCurrentEditor().hasChanges() == false ? title : "*" + title);
        setWindowTitle();
        MainWindow.setMenuStatus();
    }

    @Override
    public void setNewFilename(String oldFilename) {
        if (folder.getItemCount() == 0) {
            return;
        }
        String filename = getCurrentEditor().getFilename();
        CTabItem tab = getCurrentTab();
        if (oldFilename != null) {
            filelist.remove(oldFilename);
            filelist.add(filename);
        }
        String title = setSuffix(tab, Utils.getFileFromURL(filename));
        if (tab.getData("ftp_remote_directory") != null && !tab.getData("ftp_remote_directory").toString().isEmpty() && tab.getData("ftp_profile_name") != null && !tab.getData(
                "ftp_profile_name").toString().isEmpty()) {
            title = tab.getData("ftp_remote_directory").toString();
        }
        tab.setText(title);
        tab.setToolTipText(filename);
        tab.setData(new TabData(Utils.getFileFromURL(filename), shortCaption(title)));
        setWindowTitle();
    }

    public void setNewFilename(String oldFilename, String newFilename) {
        if (folder.getItemCount() == 0) {
            return;
        }
        CTabItem tab = getCurrentTab();
        if (oldFilename != null) {
            filelist.remove(oldFilename);
            filelist.add(newFilename);
        }
        String title = setSuffix(tab, Utils.getFileFromURL(newFilename));
        tab.setText(title);
        tab.setToolTipText(newFilename);
        tab.setData(new TabData(Utils.getFileFromURL(newFilename), shortCaption(title)));
        setWindowTitle();
    }

    private void setWindowTitle() {
        Shell shell = folder.getShell();
        String ftp = getCurrentTab().getData("ftp_title") != null ? getCurrentTab().getData("ftp_title").toString() + "\\" : "";
        String webdav = getCurrentTab().getData("webdav_title") != null ? getCurrentTab().getData("webdav_title").toString() + "\\" : "";
        shell.setText(strTitleText + webdav + ftp + " " + getCurrentTab().getText());
    }

    private String setSuffix(CTabItem tab, String title) {
        int sameTitles = getSameTitles(title);
        TabData t = (TabData) tab.getData();
        if (t != null) {
            t.cnt = sameTitles;
            if (sameTitles > 0) {
                title = title + "(" + (sameTitles + 1) + ")";
            }
        }
        return title;
    }

    private boolean isFreeIndex(int index, String title) {
        boolean found = false;
        CTabItem[] items = folder.getItems();
        int i = 0;
        while (i < items.length && !found) {
            TabData t = (TabData) items[i].getData();
            if (items[i].getData() != null && t.title.equals(title) && t.cnt == index && !items[i].equals(getCurrentTab())) {
                found = true;
            }
            i++;
        }
        return !found;
    }

    private int getSameTitles(String title) {
        int cnt = -1;
        int i = 0;
        while (cnt == -1) {
            if (isFreeIndex(i, title)) {
                cnt = i;
            }
            i++;
        }
        return cnt;
    }

    @Override
    public boolean closeAll() {
        boolean doit = true;
        for (int i = 0; i < folder.getItemCount(); i++) {
            CTabItem tab = folder.getItem(i);
            folder.setSelection(i);
            if (((IEditor) tab.getControl()).close()) {
                String s = ((IEditor) tab.getControl()).getFilename();
                if (s != null) {
                    JoeLockFolder joeLockFolder = new JoeLockFolder(s);
                    joeLockFolder.unLockFolder();
                }
                tab.dispose();
                i--;
            } else {
                doit = false;
            }
        }
        return doit;
    }

    public void closeCurrentTab() {
        if (folder.getSelectionIndex() > -1) {
            CTabItem tab = folder.getSelection();
            tab.dispose();
        } else {
            getCurrentTab().dispose();
        }
    }

    @Override
    public SchedulerForm openDirectory(String filename) {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, SchedulerDom.DIRECTORY);
        if (scheduler.openDirectory(filename, filelist)) {

            JoeLockFolder joeLockFolder = new JoeLockFolder(scheduler.getFilename());
            if (joeLockFolder.isFolderLocked()) {
                joeLockFolder.getDataFromFile(new File(scheduler.getFilename()));
                String m = String.format("The folder %s is open.\n\nUser: %s \nDate %s\n\n Do you want to take over", scheduler.getFilename(), joeLockFolder.getUserFromFile(),
                        joeLockFolder.getSinceFromFile());
                int c = MainWindow.message(m, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (c != SWT.YES) {
                    CTabItem tab = newItem(scheduler, "***");
                    tab.dispose();
                    return null;
                }
            }

            joeLockFolder.lockFolder();
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            Options.setLastFolderName(scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource(IMAGE_EDITOR_SMALL_PNG));
            return scheduler;
        } else {
            return null;
        }
    }

    public SchedulerForm openLiveElement(String filename, int type) {
        SchedulerForm scheduler = new SchedulerForm(this, folder, SWT.NONE, type);
        if (scheduler.open(filename, filelist, type)) {
            CTabItem tab = newItem(scheduler, scheduler.getFilename());
            tab.setImage(ResourceManager.getImageFromResource(IMAGE_EDITOR_SMALL_PNG));
            return scheduler;
        } else {
            return null;
        }
    }

    @Override
    public Composite openQuick(String xmlFilename) {
        final String methodName = "TabbedContainer::openQuick";
        LOGGER.trace(String.format("Enter procedure %1$s ", methodName));
        try {
            if (xmlFilename != null && !xmlFilename.isEmpty()) {
                SAXBuilder builder = new SAXBuilder();
                org.jdom.Document doc = builder.build(new File(xmlFilename));
                org.jdom.Element root = doc.getRootElement();
                String strRootName = root.getName();
                if ("description".equalsIgnoreCase(strRootName)) {
                    return openDocumentation(xmlFilename);
                } else if ("spooler".equalsIgnoreCase(strRootName)) {
                    return openScheduler(xmlFilename);
                } else if ("actions".equalsIgnoreCase(strRootName)) {
                    return openActions(xmlFilename);
                } else if ("job".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIVE_JOB);
                } else if ("job_chain".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIVE_JOB_CHAIN);
                } else if ("process_class".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIFE_PROCESS_CLASS);
                } else if ("lock".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIFE_LOCK);
                } else if ("order".equalsIgnoreCase(strRootName) || "add_order".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIFE_ORDER);
                } else if ("schedule".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIFE_SCHEDULE);
                } else if ("monitor".equalsIgnoreCase(strRootName)) {
                    return openLiveElement(xmlFilename, SchedulerDom.LIFE_MONITOR);
                }
                MainWindow.message("Unknown root Element: " + root.getName() + " from filename " + xmlFilename, SWT.NONE);
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file " + xmlFilename, e);
            MainWindow.message("could not open file cause" + e.getMessage(), SWT.NONE);
        }
        return null;
    }

    @Override
    public org.eclipse.swt.widgets.Composite openQuick() {
        final String methodName = "TabbedContainer::openQuick";
        LOGGER.trace(String.format("Enter procedure %1$s ", methodName));
        String xmlFilename = "";
        try {
            FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);
            fdialog.setFilterPath(Options.getLastDirectory());
            fdialog.setText("Open");
            fdialog.setFilterExtensions(new String[] { "*.xml" });
            xmlFilename = fdialog.open();
            return openQuick(xmlFilename);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open File ", e);
            MainWindow.message("could not open file cause" + e.getMessage(), SWT.NONE);
        }
        return null;
    }

    @Override
    public ActionsForm newActions() {
        ActionsForm actions = new ActionsForm(this, folder, SWT.NONE);
        actions.openBlank();
        newItem(actions, NEW_DOCUMENTATION_TITLE);
        return actions;
    }

    public ArrayList getFilelist() {
        return filelist;
    }

}