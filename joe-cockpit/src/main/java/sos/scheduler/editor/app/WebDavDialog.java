package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.webdav.lib.WebdavResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.util.SOSString;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobdoc.DocumentationDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.dialog.swtdesigner.SWTResourceManager;

public class WebDavDialog {

    private Button butOpenOrSave = null;
    private Group schedulerGroup = null;
    private Shell schedulerConfigurationShell = null;
    private WebDavDialogListener listener = null;
    private Combo cboConnectname = null;
    private Table table = null;
    private Text txtUrl = null;
    private final SOSString sosString = new SOSString();
    private Text txtFilename = null;
    private Text txtLog = null;
    private String type = "Open";
    private Button butChangeDir = null;
    private Button butRefresh = null;
    private Button butNewFolder = null;
    private Button butRemove = null;
    private TableColumn newColumnTableColumn_1 = null;
    private Button butSite = null;
    private Button butProfiles = null;
    private Button butClose = null;
    public static String OPEN = "Open";
    public static String SAVE_AS = "Save As";
    public static String OPEN_HOT_FOLDER = "Open Hot Folder";
    public static String SAVE_AS_HOT_FOLDER = "Save As Hot Folder";

    public WebDavDialog() {
        listener = new WebDavDialogListener();
    }

    public void showForm(String type_) {
        type = type_;
        schedulerConfigurationShell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
        schedulerConfigurationShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        schedulerConfigurationShell.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    schedulerConfigurationShell.dispose();
                }
            }
        });
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginTop = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.marginBottom = 5;
        schedulerConfigurationShell.setLayout(gridLayout);
        schedulerConfigurationShell.setSize(625, 486);
        schedulerConfigurationShell.setText(type);
        schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
        schedulerGroup.setText("Open");
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        gridData.widthHint = 581;
        gridData.heightHint = 329;
        schedulerGroup.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        gridLayout_1.marginTop = 5;
        gridLayout_1.marginRight = 5;
        gridLayout_1.marginLeft = 5;
        gridLayout_1.marginBottom = 5;
        schedulerGroup.setLayout(gridLayout_1);
        cboConnectname = new Combo(schedulerGroup, SWT.NONE);
        cboConnectname.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (!cboConnectname.getText().equalsIgnoreCase(listener.getCurrProfileName())) {
                    txtUrl.setText("");
                    table.removeAll();
                    txtFilename.setText("");
                    listener.setCurrProfileName(cboConnectname.getText());
                    initForm();
                }
            }
        });
        cboConnectname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        butSite = new Button(schedulerGroup, SWT.NONE);
        butSite.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(schedulerConfigurationShell);
                HashMap h = listener.changeDirectory(cboConnectname.getText(), txtUrl.getText());
                butOpenOrSave.setEnabled(!txtFilename.getText().isEmpty());
                fillTable(h);
                _setEnabled(true);
                Utils.stopCursor(schedulerConfigurationShell);
            }
        });
        butSite.setText("Connect");
        butProfiles = new Button(schedulerGroup, SWT.NONE);
        butProfiles.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(schedulerConfigurationShell);
                WebDavDialogProfiles profiles = new WebDavDialogProfiles(listener);
                profiles.showForm();
                txtUrl.setText(listener.getCurrProfile() != null && listener.getCurrProfile().getProperty("url") != null
                        ? listener.getCurrProfile().getProperty("url") : "");
                Utils.stopCursor(schedulerConfigurationShell);
            }
        });
        butProfiles.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
        butProfiles.setText("Profiles");
        txtUrl = new Text(schedulerGroup, SWT.BORDER);
        txtUrl.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    if (!txtUrl.getText().endsWith("/")) {
                        txtUrl.setText(txtUrl.getText() + "/");
                    }
                    HashMap h = listener.changeDirectory(txtUrl.getText());
                    fillTable(h);
                }
            }
        });
        txtUrl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
        butChangeDir = new Button(schedulerGroup, SWT.NONE);
        butChangeDir.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(schedulerConfigurationShell);
                if (!txtUrl.getText().endsWith("/")) {
                    txtUrl.setText(txtUrl.getText() + "/");
                }
                HashMap h = listener.changeDirectory(cboConnectname.getText(), txtUrl.getText());
                fillTable(h);
                Utils.stopCursor(schedulerConfigurationShell);
            }
        });
        butChangeDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butChangeDir.setText("Change Directory ");
        table = new Table(schedulerGroup, SWT.FULL_SELECTION | SWT.BORDER);
        table.setSortDirection(SWT.DOWN);
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    TableItem item = table.getSelection()[0];
                    if ("file".equals(item.getData("type")) || OPEN_HOT_FOLDER.equalsIgnoreCase(type) || SAVE_AS_HOT_FOLDER.equalsIgnoreCase(type)) {
                        txtFilename.setText(item.getText(0));
                    } else {
                        txtFilename.setText("");
                    }
                }
                butOpenOrSave.setEnabled(!txtFilename.getText().isEmpty());
            }
        });
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                if (table.getSelectionCount() > 0) {
                    TableItem item = table.getSelection()[0];
                    if ("dir".equals(item.getData("type"))) {
                        txtUrl.setText((txtUrl.getText().endsWith("/") ? txtUrl.getText() : txtUrl.getText() + "/") + item.getText() + "/");
                        fillTable(listener.changeDirectory(txtUrl.getText()));
                    } else if ("dir_up".equals(item.getData("type"))) {
                        String[] split = txtUrl.getText().split("/");
                        String parentPath = "";
                        for (int i = 0; i < split.length - 1; i++) {
                            parentPath = parentPath + split[i] + "/";
                        }
                        if (parentPath != null) {
                            txtUrl.setText(parentPath);
                        }
                        fillTable(listener.changeDirectory(parentPath));
                    } else {
                        txtUrl.setText("");
                    }
                }
                txtFilename.setText("");
            }
        });
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 3));
        final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_2.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                sort(newColumnTableColumn_2);
            }
        });
        table.setSortColumn(newColumnTableColumn_2);
        newColumnTableColumn_2.setMoveable(true);
        newColumnTableColumn_2.setWidth(176);
        newColumnTableColumn_2.setText("Name");
        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                sort(newColumnTableColumn);
            }
        });
        newColumnTableColumn.setWidth(117);
        newColumnTableColumn.setText("Size");
        newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                sort(newColumnTableColumn_1);
            }
        });
        newColumnTableColumn_1.setWidth(100);
        newColumnTableColumn_1.setText("Type");
        new Label(schedulerGroup, SWT.NONE);
        butRefresh = new Button(schedulerGroup, SWT.NONE);
        butRefresh.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refresh();
            }
        });
        butRefresh.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butRefresh.setText("Refresh");
        new Label(schedulerGroup, SWT.NONE);
        butNewFolder = new Button(schedulerGroup, SWT.NONE);
        butNewFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                openDialog();
            }
        });
        butNewFolder.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butNewFolder.setText("New Folder");
        new Label(schedulerGroup, SWT.NONE);
        butRemove = new Button(schedulerGroup, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (txtFilename.getText() != null) {
                    listener.removeFile(txtFilename.getText());
                    HashMap h = listener.changeDirectory(txtUrl.getText());
                    fillTable(h);
                }
            }
        });
        butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butRemove.setText("Remove");
        final Label filenameLabel = new Label(schedulerGroup, SWT.NONE);
        if (OPEN_HOT_FOLDER.equalsIgnoreCase(type)) {
            filenameLabel.setText("Folder");
        } else {
            filenameLabel.setText("Filename");
        }
        txtFilename = new Text(schedulerGroup, SWT.BORDER);
        txtFilename.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butOpenOrSave.setEnabled(!txtFilename.getText().isEmpty());
            }
        });
        txtFilename.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        new Label(schedulerGroup, SWT.NONE);
        butOpenOrSave = new Button(schedulerGroup, SWT.NONE);
        butOpenOrSave.setEnabled(false);
        butOpenOrSave.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butOpenOrSave.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(schedulerConfigurationShell);
                if (OPEN.equals(butOpenOrSave.getText()) || OPEN_HOT_FOLDER.equals(butOpenOrSave.getText())) {
                    if (OPEN_HOT_FOLDER.equals(type)) {
                        openHotFolder();
                    } else {
                        openFile();
                    }
                } else {
                    String file = txtUrl.getText() + "/" + txtFilename.getText();
                    saveas(file);
                }
                Utils.stopCursor(schedulerConfigurationShell);
            }
        });
        butOpenOrSave.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        butOpenOrSave.setText(type);
        new Label(schedulerGroup, SWT.NONE);
        new Label(schedulerGroup, SWT.NONE);
        new Label(schedulerGroup, SWT.NONE);
        new Label(schedulerGroup, SWT.NONE);
        butClose = new Button(schedulerGroup, SWT.NONE);
        butClose.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                schedulerConfigurationShell.dispose();
            }
        });
        butClose.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butClose.setText("Close");
        txtLog = new Text(schedulerConfigurationShell, SWT.NONE);
        txtLog.setEditable(false);
        txtLog.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        final Button butLog = new Button(schedulerConfigurationShell, SWT.NONE);
        butLog.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                String text = sos.scheduler.editor.app.Utils.showClipboard(txtLog.getText(), schedulerConfigurationShell, false, "");
                if (text != null) {
                    txtLog.setText(text);
                }
            }
        });
        butLog.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        butLog.setText("Log");
        String selectProfile = Options.getProperty("last_webdav_profile");
        if (selectProfile != null && !selectProfile.isEmpty()) {
            cboConnectname.setText(selectProfile);
            listener.setCurrProfileName(selectProfile);
        }
        initForm();
        schedulerConfigurationShell.layout();
        schedulerConfigurationShell.open();
    }

    private void initForm() {
        try {
            setToolTipText();
            cboConnectname.setItems(listener.getProfileNames());
            if (listener.getProfileNames().length == 0) {
                cboConnectname.setText("");
                txtUrl.setText("");
            } else {
                String profilename = listener.getCurrProfileName() != null ? listener.getCurrProfileName() : listener.getProfileNames()[0];
                listener.setCurrProfileName(profilename);
                cboConnectname.setText(profilename);
            }
            listener.setLogText(txtLog);
            listener.setConnectionsname(cboConnectname);
            listener.setURL(txtUrl);
            txtUrl.setText(listener.getCurrProfile() != null && listener.getCurrProfile().getProperty("url") != null
                    ? listener.getCurrProfile().getProperty("url") : "");
            _setEnabled(false);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            MainWindow.message("could not int WebDav Profiles:" + e.getMessage(), SWT.ICON_WARNING);
        }
    }

    private void fillTable(HashMap h) {
        try {
            table.removeAll();
            java.util.Iterator it = h.keySet().iterator();
            ArrayList files = new ArrayList();
            TableItem item_ = new TableItem(table, SWT.NONE);
            item_.setData("type", "dir_up");
            item_.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory_up.gif"));
            while (it.hasNext()) {
                WebdavResource keys = (WebdavResource) it.next();
                String key = keys.toString();
                key = key.replaceAll("%20", " ");
                key = new File(key).getName();
                if ("dir".equals(h.get(keys))) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, key);
                    item.setText(1, "");
                    item.setText(2, "Folder");
                    item.setData("type", "dir");
                    item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
                } else if (!key.endsWith("_size")) {
                    files.add(key);
                }
            }
            if (!OPEN_HOT_FOLDER.equalsIgnoreCase(type)) {
                for (int i = 0; i < files.size(); i++) {
                    String filename = sosString.parseToString(files.get(i));
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, filename);
                    item.setText(1, sosString.parseToString(h.get(filename + "_size")));
                    item.setText(2, "File");
                    item.setData("type", "file");
                    item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public void saveas(String file) {
        try {
            file = file.replaceAll("\\\\", "/");
            String localfilename = MainWindow.getContainer().getCurrentEditor().getFilename();
            String newFilename = "";
            if (localfilename != null) {
                newFilename = new File(localfilename).getParent() + "/" + new File(file).getName();
            } else {
                newFilename = sosString.parseToString(listener.getCurrProfile().get("localdirectory")) + "/" + new File(file).getName();
            }
            DomParser currdom = null;
            if (MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {
                SchedulerForm form = (SchedulerForm) MainWindow.getContainer().getCurrentEditor();
                currdom = form.getDom();
            } else if (MainWindow.getContainer().getCurrentEditor() instanceof DocumentationForm) {
                DocumentationForm form = (DocumentationForm) MainWindow.getContainer().getCurrentEditor();
                currdom = (DocumentationDom) form.getDom();
            } else if (MainWindow.getContainer().getCurrentEditor() instanceof JobChainConfigurationForm) {
                JobChainConfigurationForm form = (JobChainConfigurationForm) MainWindow.getContainer().getCurrentEditor();
                currdom = form.getDom();
            }
            if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isLifeElement()) {
                File f = new File(newFilename);
                if (f.isFile()) {
                    newFilename = f.getParent();
                }
                localfilename = newFilename;
                currdom.setFilename(new java.io.File(newFilename).getParent());
                String attrName = f.getName().substring(0, f.getName().indexOf("." + currdom.getRoot().getName()));
                if ("order".equals(currdom.getRoot().getName())) {
                    Utils.setAttribute("job_chain", attrName.substring(0, attrName.indexOf(",")), currdom.getRoot());
                    Utils.setAttribute("id", attrName.substring(attrName.indexOf(",") + 1), currdom.getRoot());
                } else {
                    Utils.setAttribute("name", attrName, currdom.getRoot());
                }
                if (MainWindow.getContainer().getCurrentEditor().save()) {
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile_name", listener.getCurrProfileName());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile", listener.getCurrProfile());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_title", "[WebDav::" + listener.getCurrProfileName() + "]");
                    MainWindow.getContainer().getCurrentTab().setData("webdav_remote_directory", txtUrl.getText() + "/" + txtFilename.getText());
                    MainWindow.setSaveStatus();
                }
                currdom.setFilename(new java.io.File(newFilename).getCanonicalPath());
                sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
                SchedulerForm sf = (SchedulerForm) (con.getCurrentEditor());
                sf.updateTree("jobs");
                String name = currdom.getRoot().getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                sf.updateTreeItem(name + ": " + attrName);
            } else if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isDirectory()) {
                if (MainWindow.getContainer().getCurrentEditor().save()) {
                    ArrayList newlist = listener.saveHotFolderAs(localfilename, file);
                    MainWindow.getContainer().getCurrentTab().setData("webdav_hot_folder_elements", newlist);
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile_name", listener.getCurrProfileName());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile", listener.getCurrProfile());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_title", "[WebDav::" + listener.getCurrProfileName() + "]");
                    MainWindow.getContainer().getCurrentTab().setData("webdav_remote_directory", txtUrl.getText() + "/" + txtFilename.getText());
                }
                return;
            } else {
                currdom.setFilename(newFilename);
                if (MainWindow.getContainer().getCurrentEditor().save()) {
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile_name", listener.getCurrProfileName());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_profile", listener.getCurrProfile());
                    MainWindow.getContainer().getCurrentTab().setData("webdav_title", "[WebDav::" + listener.getCurrProfileName() + "]");
                    MainWindow.getContainer().getCurrentTab().setData("webdav_remote_directory", txtUrl.getText() + "/" + txtFilename.getText());
                    MainWindow.setSaveStatus();
                }
            }
            listener.saveAs(localfilename, file);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not save File", e);
            MainWindow.message("could not save File: cause: " + e.getMessage(), SWT.ICON_WARNING);
        } finally {
            schedulerConfigurationShell.dispose();
        }
    }

    public void openHotFolder() {
        try {
            HashMap h = listener.changeDirectory(txtUrl.getText() + "/" + txtFilename.getText() + "/");
            if (listener.hasError()) {
                return;
            }
            java.util.Iterator it = h.keySet().iterator();
            ArrayList nameOfLifeElement = new ArrayList();
            String tempSubHotFolder = txtFilename.getText();
            String localFile = "";
            while (it.hasNext()) {
                WebdavResource key = (WebdavResource) it.next();
                if ("file".equals(h.get(key)) && isLifeElement(sosString.parseToString(key))) {
                    localFile = listener.getCurrProfile().getProperty("localdirectory");
                    if (!localFile.endsWith("/")) {
                        localFile = localFile + "/";
                    }
                    localFile = localFile + tempSubHotFolder + "/";
                    if (!new File(localFile).exists()) {
                        new File(localFile).mkdirs();
                    }
                    String slocalFile = localFile + new File(sosString.parseToString(key)).getName();
                    key.getMethod(new File(slocalFile));
                    nameOfLifeElement.add(slocalFile);
                }
            }
            if (MainWindow.getContainer().openDirectory(localFile) != null) {
                MainWindow.getContainer().getCurrentTab().setData("webdav_profile_name", listener.getCurrProfileName());
                MainWindow.getContainer().getCurrentTab().setData("webdav_profile", listener.getCurrProfile());
                MainWindow.getContainer().getCurrentTab().setData("webdav_title", "[WebDav::" + listener.getCurrProfileName() + "]");
                MainWindow.getContainer().getCurrentTab().setData("webdav_remote_directory", txtUrl.getText() + "/" + txtFilename.getText());
                MainWindow.getContainer().getCurrentTab().setData("webdav_hot_folder_elements", nameOfLifeElement);
                MainWindow.setSaveStatus();
            }
            schedulerConfigurationShell.dispose();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not Open Hot Folder.", e);
            MainWindow.message("could not Open Hot Folder: cause: " + e.getMessage(), SWT.ICON_WARNING);
        }
    }

    public void openFile() {
        String file = listener.getFile(txtUrl.getText() + "//" + txtFilename.getText(), null);
        if (!listener.hasError()) {
            if (MainWindow.getContainer().openQuick(file) != null) {
                MainWindow.getContainer().getCurrentTab().setData("webdav_profile_name", listener.getCurrProfileName());
                MainWindow.getContainer().getCurrentTab().setData("webdav_profile", listener.getCurrProfile());
                MainWindow.getContainer().getCurrentTab().setData("webdav_title", "[WebDav::" + listener.getCurrProfileName() + "]");
                MainWindow.getContainer().getCurrentTab().setData("webdav_remote_directory", txtUrl.getText() + "/" + txtFilename.getText());
                MainWindow.setSaveStatus();
            }
            schedulerConfigurationShell.dispose();
        }
    }

    public WebDavDialogListener getListener() {
        return listener;
    }

    public void refresh() {
        Utils.startCursor(schedulerConfigurationShell);
        if (!txtUrl.getText().endsWith("/")) {
            txtUrl.setText(txtUrl.getText() + "/");
        }
        HashMap h = listener.changeDirectory(txtUrl.getText());
        fillTable(h);
        Utils.stopCursor(schedulerConfigurationShell);
    }

    public void openDialog() {
        final Shell shell = new Shell();
        shell.pack();
        Dialog dialog = new Dialog(shell);
        dialog.setText("Create New Folder");
        dialog.open(this);
    }

    private void _setEnabled(boolean enabled) {
        txtUrl.setEnabled(enabled);
        butChangeDir.setEnabled(enabled);
        butRefresh.setEnabled(enabled);
        butNewFolder.setEnabled(enabled);
        butRemove.setEnabled(enabled);
    }

    private void sort(TableColumn col) {
        try {
            if (table.getSortDirection() == SWT.DOWN) {
                table.setSortDirection(SWT.UP);
            } else {
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(col);
            ArrayList listOfSortData = new ArrayList();
            for (int i = 0; i < table.getItemCount(); i++) {
                TableItem item = table.getItem(i);
                if (!"dir_up".equals(item.getData("type"))) {
                    HashMap hash = new HashMap();
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        hash.put(table.getColumn(j).getText(), item.getText(j));
                    }
                    hash.put("type", item.getData("type"));
                    listOfSortData.add(hash);
                }
            }
            listOfSortData = sos.util.SOSSort.sortArrayList(listOfSortData, col.getText());
            table.removeAll();
            TableItem item_ = new TableItem(table, SWT.NONE);
            item_.setData("type", "dir_up");
            item_.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory_up.gif"));
            TableItem item = null;
            if (table.getSortDirection() == SWT.DOWN) {
                for (int i = 0; i < listOfSortData.size(); i++) {
                    item = new TableItem(table, SWT.NONE);
                    HashMap hash = (HashMap) listOfSortData.get(i);
                    item.setData("type", hash.get("type"));
                    if ("file".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
                    } else if ("dir".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
                    } else if ("dir_up".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory_up.gif"));
                    }
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        item.setText(j, sosString.parseToString(hash.get(table.getColumn(j).getText())));
                    }
                }
            } else {
                for (int i = listOfSortData.size() - 1; i >= 0; i--) {
                    item = new TableItem(table, SWT.NONE);
                    HashMap hash = (HashMap) listOfSortData.get(i);
                    item.setData("type", hash.get("type"));
                    if ("file".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
                    } else if ("dir".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
                    } else if ("dir_up".equals(hash.get("type"))) {
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory_up.gif"));
                    }
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        item.setText(j, sosString.parseToString(hash.get(table.getColumn(j).getText())));
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public void setToolTipText() {
        if (OPEN_HOT_FOLDER.equalsIgnoreCase(type)) {
            butOpenOrSave.setToolTipText(Messages.getTooltip("webdavdialog.btn_open_hot_folder"));
            txtFilename.setToolTipText(Messages.getTooltip("webdavdialog.txt_open_hot_folder"));
        } else if (OPEN.equalsIgnoreCase(type)) {
            butOpenOrSave.setToolTipText(Messages.getTooltip("webdavdialog.btn_open_file"));
            txtFilename.setToolTipText(Messages.getTooltip("webdavdialog.txt_open_file"));
        } else if (SAVE_AS.equalsIgnoreCase(type) || SAVE_AS_HOT_FOLDER.equalsIgnoreCase(type)) {
            butOpenOrSave.setToolTipText(Messages.getTooltip("webdavdialog.btn_save_as"));
            txtFilename.setToolTipText(Messages.getTooltip("webdavdialog.txt_save_as"));
        }
        cboConnectname.setToolTipText(Messages.getTooltip("webdavdialog.profilenames"));
        table.setToolTipText(Messages.getTooltip("webdavdialog.table"));
        txtUrl.setToolTipText(Messages.getTooltip("webdavdialog.directory"));
        txtLog.setToolTipText(Messages.getTooltip("webdavdialog.log"));
        butChangeDir.setToolTipText(Messages.getTooltip("webdavdialog.change_directory"));
        butRefresh.setToolTipText(Messages.getTooltip("webdavdialog.refresh"));
        butNewFolder.setToolTipText(Messages.getTooltip("webdavdialog.new_folder"));
        butRemove.setToolTipText(Messages.getTooltip("webdavdialog.remove"));
        butSite.setToolTipText(Messages.getTooltip("webdavdialog.connect"));
        butClose.setToolTipText(Messages.getTooltip("webdavdialog.close"));
        butProfiles.setToolTipText(Messages.getTooltip("webdavdialog.profiles"));
    }

    private boolean isLifeElement(String filename) {
        return filename.endsWith(".job.xml") || filename.endsWith(".schedule.xml") || filename.endsWith(".job_chain.xml")
                || filename.endsWith(".lock.xml") || filename.endsWith(".process_class.xml") || filename.endsWith(".order.xml");
    }

    public Text getTxtUrl() {
        return txtUrl;
    }

}