package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.util.SOSString;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;

public class HotFolderDialog {

    public static final int SCHEDULER_CLUSTER = 1;
    public static final int SCHEDULER_HOST = 2;
    private Button butOK = null;
    private Button cancelButton = null;
    private Group schedulerGroup = null;
    private Tree tree = null;
    private int type = -1;
    private String sType = "";
    private SOSString sosString = null;
    private Shell schedulerConfigurationShell = null;
    private Button butRename = null;
    private Text txtName = null;
    private Text txtPort = null;
    private Button butAdd = null;
    private final String SCHEDULER_CLUSTER_MASK = "^[^#]+$";
    private final String SCHEDULER_HOST_MASK = "^[^#]+#\\d{1,5}$";

    public HotFolderDialog() {
        sosString = new SOSString();
    }

    public void showForm(int type_) {

        type = type_;
        schedulerConfigurationShell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);

        schedulerConfigurationShell.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    schedulerConfigurationShell.dispose();
                }
            }
        });

        schedulerConfigurationShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.marginBottom = 5;
        gridLayout.numColumns = 6;
        schedulerConfigurationShell.setLayout(gridLayout);
        schedulerConfigurationShell.setSize(425, 486);

        sType = "";
        if (type == SCHEDULER_HOST)
            // sType = "Host";
            sType = SOSJOEMessageCodes.JOE_M_0006.label();
        else if (type == SCHEDULER_CLUSTER)
            // sType = "Cluster";
            sType = SOSJOEMessageCodes.JOE_M_0007.label();

        schedulerConfigurationShell.setText(SOSJOEMessageCodes.JOE_M_HotFolderDialog_SchedulerGroup.params(sType));

        {
            schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
            // schedulerGroup.setText("Open Scheduler " + sType +
            // " Configuration");
            schedulerGroup.setText(SOSJOEMessageCodes.JOE_M_HotFolderDialog_SchedulerGroup.params(sType));
            final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 6, 1);
            gridData.widthHint = 581;
            gridData.heightHint = 329;
            schedulerGroup.setLayoutData(gridData);

            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.verticalSpacing = 10;
            gridLayout_1.horizontalSpacing = 10;
            gridLayout_1.marginWidth = 10;
            gridLayout_1.marginTop = 10;
            gridLayout_1.marginRight = 10;
            gridLayout_1.marginLeft = 10;
            gridLayout_1.marginHeight = 10;
            gridLayout_1.marginBottom = 10;
            schedulerGroup.setLayout(gridLayout_1);

            createTree();

            // final Tree tree = new Tree(schedulerGroup, SWT.BORDER);

            final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
            tree.setLayoutData(gridData_1);

        }

        cancelButton = SOSJOEMessageCodes.JOE_B_HotFolderDialog_Cancel.Control(new Button(schedulerConfigurationShell, SWT.NONE));
        cancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                schedulerConfigurationShell.dispose();
            }
        });
        // cancelButton.setText("Cancel");

        txtName = SOSJOEMessageCodes.JOE_T_HotFolderDialog_Name.Control(new Text(schedulerConfigurationShell, SWT.BORDER));
        txtName.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = (e.text.indexOf("#") == -1);
            }
        });
        txtName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                setButtonRenameEnable();
            }
        });
        txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        if (type == SCHEDULER_HOST) {
            txtPort = SOSJOEMessageCodes.JOE_T_HotFolderDialog_Port.Control(new Text(schedulerConfigurationShell, SWT.BORDER));
            txtPort.addVerifyListener(new VerifyListener() {

                @Override
                public void verifyText(final VerifyEvent e) {
                    if (type == SCHEDULER_CLUSTER)
                        e.doit = e.text.indexOf("#") == -1;
                    else
                        e.doit = (e.text.indexOf("#") == -1) && Utils.isOnlyDigits(e.text);
                    ;
                }
            });
            txtPort.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    setButtonRenameEnable();
                }
            });
            txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        }

        butAdd = SOSJOEMessageCodes.JOE_B_HotFolderDialog_Add.Control(new Button(schedulerConfigurationShell, SWT.NONE));
        butAdd.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                addItem();
            }
        });
        butAdd.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        // butAdd.setText("Add");

        butRename = SOSJOEMessageCodes.JOE_B_HotFolderDialog_Rename.Control(new Button(schedulerConfigurationShell, SWT.NONE));
        butRename.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    if (tree.getSelectionCount() > 0 && !tree.getSelection()[0].getText().equals(sType) && txtName.getText().trim().length() > 0) {

                        String changeName = sosString.parseToString(tree.getSelection()[0].getData());
                        File f = new File(sosString.parseToString(tree.getSelection()[0].getData()));
                        // String path = f.getParent().endsWith("/") ||
                        // f.getParent().endsWith("\\") ? f.getParent() :
                        // f.getParent() + "/";
                        String path = f.getParent();

                        if (type == SCHEDULER_HOST && txtName.getText().length() > 0 && txtPort.getText().length() == 0) {
                            // host ändern
                            changeHost(path);
                            return;
                        } else if (type == SCHEDULER_HOST && txtName.getText().length() > 0 && txtPort.getText().length() > 0
                                && tree.getSelection()[0].getParentItem().getText().equals(sType)) {
                            // ein host wurde selektiert und ein neuer port
                            // wurde eingegeben
                            // dann soll diese neu hinzugefügt werden
                            addItem();
                            return;
                        } else {
                            path = new File(path, txtName.getText()).getCanonicalPath();
                        }

                        if (type == SCHEDULER_HOST)
                            path = new File(path, "#" + txtPort.getText()).getCanonicalPath();

                        if (f.renameTo(new File(path))) {
                            if (type == SCHEDULER_HOST) {
                                // port ändern
                                tree.getSelection()[0].getParentItem().setText(txtName.getText());
                                tree.getSelection()[0].setText(txtPort.getText());
                                tree.getSelection()[0].setData(path);
                            } else {
                                // scheduler id ändern
                                // String changeName =
                                // tree.getSelection()[0].getText();
                                tree.getSelection()[0].setText(txtName.getText());
                                tree.getSelection()[0].setData(path);

                                TreeItem _item = tree.getSelection()[0];
                                changeSubTreedata(path, _item, changeName);
                                /*
                                 * for(int i = 0; i < _item.getItemCount(); i++)
                                 * { TreeItem cItem = _item.getItem(i); String
                                 * data =
                                 * sosString.parseToString(cItem.getData());
                                 * //hier data ändern //data =
                                 * data.replaceAll(changeName, path); data =
                                 * data.substring(changeName.length()); data =
                                 * path + data; } }
                                 */
                            }
                            butRename.setEnabled(false);
                            butAdd.setEnabled(false);
                            txtName.setText("");
                        } else {
                            // MainWindow.message("could not rename configuration: ",
                            // SWT.ICON_INFORMATION);
                            MainWindow.message(SOSJOEMessageCodes.JOE_M_0005.params(""), SWT.ICON_INFORMATION);
                            schedulerConfigurationShell.setFocus();
                        }

                    }
                } catch (Exception ex) {
                    new ErrorLog(SOSJOEMessageCodes.JOE_E_0007.params(sos.util.SOSClassUtil.getMethodName()), ex);
                    MainWindow.message(SOSJOEMessageCodes.JOE_M_0005.params(ex.getMessage()), SWT.ICON_ERROR);
                    schedulerConfigurationShell.setFocus();
                }
            }
        });
        butRename.setEnabled(false);
        butRename.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
        // butRename.setText("Rename");
        {
            butOK = SOSJOEMessageCodes.JOE_B_HotFolderDialog_Open.Control(new Button(schedulerConfigurationShell, SWT.NONE));
            butOK.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    openDirectory();
                }
            });
            butOK.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            butOK.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
            // butOK.setText("Open");
        }
        // setToolTipText();

        schedulerConfigurationShell.layout();
        schedulerConfigurationShell.open();
    }

    private void createTree() {

        String mask = "";
        try {

            tree = SOSJOEMessageCodes.JOE_HotFolderDialog_Tree.Control(new Tree(schedulerGroup, SWT.BORDER));

            tree.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {

                    if (tree.getSelectionCount() > 0 && !tree.getSelection()[0].getText().equals(sType)) {
                        if (type == SCHEDULER_CLUSTER) {
                            txtName.setText(tree.getSelection()[0].getText());

                        } else if (type == SCHEDULER_HOST) {
                            if (tree.getSelection()[0].getParentItem().getText().equalsIgnoreCase(sType)) {
                                // host wurde selektiert
                                txtName.setText(tree.getSelection()[0].getText());
                                txtPort.setText("");

                            } else {
                                // port ist selektiert
                                txtName.setText(tree.getSelection()[0].getParentItem().getText());
                                txtPort.setText(tree.getSelection()[0].getText());

                            }
                        }
                    } else {
                        txtName.setText("");
                        if (txtPort != null)
                            txtPort.setText("");
                        butRename.setEnabled(false);

                    }
                    butAdd.setEnabled(false);
                }
            });
            tree.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDoubleClick(final MouseEvent e) {
                    openDirectory();
                }
            });

            tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            if (type == SCHEDULER_CLUSTER) {
                mask = SCHEDULER_CLUSTER_MASK;
            } else if (type == SCHEDULER_HOST) {
                mask = SCHEDULER_HOST_MASK;
            }

            String path = new File(Options.getSchedulerData(), "config/remote").getCanonicalPath();

            File p = new File(path);
            if (!p.exists()) {
                p.mkdirs();
            }
            java.util.Vector filelist = sos.util.SOSFile.getFolderlist(path, mask, java.util.regex.Pattern.CASE_INSENSITIVE, false);

            TreeItem rItem = new TreeItem(tree, SWT.NONE);
            rItem.setText(sType);
            rItem.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));

            createTreeItem(rItem, filelist, false);

            rItem.setExpanded(true);

        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            MainWindow.message(SOSJOEMessageCodes.JOE_E_0003.params(e.getMessage()), SWT.ICON_ERROR);
            schedulerConfigurationShell.setFocus();
        }

    }

    private void createTreeItem(TreeItem parentItem, java.util.Vector filelist, boolean sub) {
        try {
            Iterator fileIterator = filelist.iterator();
            String filename = "";

            while (fileIterator.hasNext()) {
                filename = sosString.parseToString(fileIterator.next());

                File f = new File(filename);
                if (!f.isDirectory())
                    continue;

                if (f.getName().equals("_all"))
                    continue;

                String name = f.getName();

                if (type == SCHEDULER_CLUSTER) {
                    TreeItem item = new TreeItem(parentItem, SWT.NONE);

                    name = f.getName();
                    item.setText(name);
                    item.setData(filename);
                    item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));

                    java.util.Vector subFilelist = sos.util.SOSFile.getFolderlist(filename, SCHEDULER_CLUSTER_MASK, java.util.regex.Pattern.CASE_INSENSITIVE, false);
                    createTreeItem(item, subFilelist, false);

                } else {

                    if (sub) {
                        // ports von host bestimmen
                        TreeItem item = new TreeItem(parentItem, SWT.NONE);

                        name = f.getName().substring(f.getName().indexOf("#") + 1);
                        item.setText(name);
                        item.setData(filename);
                        item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
                    } else {
                        // host bestimmen und ports bilden
                        HashMap names = new HashMap();// alle Hostname
                                                      // aufschreiben
                        if (sosString.parseToString(name).length() > 0) {
                            String sname = name.substring(0, f.getName().indexOf("#"));
                            names.put(sname, null);
                        }
                        while (fileIterator.hasNext()) {
                            filename = sosString.parseToString(fileIterator.next());
                            f = new File(filename);
                            String sname = new File(filename).getName().substring(0, f.getName().indexOf("#"));
                            names.put(sname, null);
                        }

                        String path = new File(Options.getSchedulerData(), "config/remote").getCanonicalPath();
                        Iterator hostIterator = names.keySet().iterator();
                        while (hostIterator.hasNext()) {
                            String sname = sosString.parseToString(hostIterator.next());
                            TreeItem newItem = new TreeItem(parentItem, SWT.NONE);
                            newItem.setText(sname);
                            // newItem.setData(sname);
                            newItem.setData(new File(path, sname).getCanonicalPath());// test

                            newItem.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
                            // String mask = "^" + sname + ".*\\.scheduler$";
                            String mask = "^" + sname + "#";
                            java.util.Vector subFilelist = sos.util.SOSFile.getFolderlist(path, mask, java.util.regex.Pattern.CASE_INSENSITIVE, true);
                            createTreeItem(newItem, subFilelist, true);
                            newItem.setExpanded(true);
                        }
                        // break;
                    }

                }

            }
        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            MainWindow.message(SOSJOEMessageCodes.JOE_E_0003.params(e.getMessage()), SWT.ICON_ERROR);
            schedulerConfigurationShell.setFocus();
        }

    }

    private void openDirectory() {
        try {
            Utils.startCursor(schedulerConfigurationShell);
            if (tree.getSelectionCount() > 0) {
                String path = sosString.parseToString(tree.getSelection()[0].getData());
                if ((tree.getSelection()[0].getItemCount() > 0 && type == SCHEDULER_HOST && !tree.getSelection()[0].getText().equals(sType))
                        || type == SCHEDULER_CLUSTER && tree.getSelection()[0].getText().equals(sType)) {
                    // host wurde ausgewählt -> enzsprechende Ports öffnen
                    for (int i = 0; i < tree.getSelection()[0].getItemCount(); i++) {
                        path = sosString.parseToString(tree.getSelection()[0].getItem(i).getData());
                        if (MainWindow.getContainer().openDirectory(path) != null)
                            MainWindow.setSaveStatus();
                    }

                } else if (!tree.getSelection()[0].getText().equals(sType)) {
                    if (MainWindow.getContainer().openDirectory(path) != null)
                        MainWindow.setSaveStatus();
                }
                schedulerConfigurationShell.close();
            }
        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            MainWindow.message(SOSJOEMessageCodes.JOE_E_0002.params(e.getMessage()), SWT.ICON_ERROR);
            schedulerConfigurationShell.setFocus();
        } finally {
            Utils.stopCursor(schedulerConfigurationShell);
        }

    }

    private void setButtonRenameEnable() {
        if (tree.getSelectionCount() > 0 && !tree.getSelection()[0].getText().equals(sType)) {
            if (type == SCHEDULER_HOST) {
                butRename.setEnabled(true);
                if (txtName.getText().length() > 0 && txtPort.getText().length() > 0) {
                    txtName.setEditable(false);
                } else {
                    txtName.setEditable(true);
                }
            } else {
                if (txtName.getText().length() > 0) {
                    butRename.setEnabled(true);
                } else {
                    butRename.setEnabled(false);
                }
            }
        } else {
            txtName.setEditable(true);
        }
        butAdd.setEnabled(txtName.getText().length() > 0);
    }

    private void changeHost(String path) {
        // host ändern
        try {

            String filename = "";

            tree.getSelection()[0].setText(txtName.getText());
            tree.getSelection()[0].setData(new File(path, txtName.getText()).getCanonicalPath());

            for (int i = 0; i < tree.getSelection()[0].getItemCount(); i++) {
                TreeItem item = tree.getSelection()[0].getItem(i);
                filename = item.getData().toString();
                File _f = new File(filename);
                // String newFilename = path + txtName.getText() +
                // _f.getName().substring(_f.getName().indexOf("#"));
                String newFilename = new File(path, txtName.getText() + _f.getName().substring(_f.getName().indexOf("#"))).getCanonicalPath();
                File _newF = new File(newFilename);
                // System.out.println("rename: " + filename + " in " +
                // newFilename);
                if (!_f.renameTo(_newF)) {
                    MainWindow.message(SOSJOEMessageCodes.JOE_E_0004.params(filename, newFilename), SWT.ICON_INFORMATION);
                }
                item.setData(newFilename);
            }

        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()) + " " + SOSJOEMessageCodes.JOE_E_0005.label(), e);
            MainWindow.message(SOSJOEMessageCodes.JOE_E_0005.label() + e.getMessage(), SWT.ICON_ERROR);
        }
    }

    private void addItem() {
        try {

            String name = "";

            if (type == SCHEDULER_HOST && (txtName.getText().length() == 0 || txtPort.getText().length() == 0)) {
                // MainWindow.message("missing host and port", SWT.NONE);
                MainWindow.message(SOSJOEMessageCodes.JOE_M_0001.label(), SWT.NONE);
                schedulerConfigurationShell.setFocus();
                return;
            }

            if (type == SCHEDULER_CLUSTER) {
                name = txtName.getText();
            } else {
                name = txtName.getText() + "#" + txtPort.getText();
            }
            String path = "";
            /*
             * if (tree.getSelectionCount() > 0 &&
             * tree.getSelection()[0].getData() != null) path =
             * sosString.parseToString(tree.getSelection()[0].getData()) ; else
             */
            path = new File(Options.getSchedulerData(), "config/remote/").getCanonicalPath();

            // path = (path.endsWith("/") || path.endsWith("\\") ? path : path +
            // "/") + name;
            path = new File(path, name).getCanonicalPath();

            File newFile = new File(path);
            if (newFile.exists()) {
                MainWindow.message(SOSJOEMessageCodes.JOE_M_0002.label(), SWT.NONE);
                schedulerConfigurationShell.setFocus();
                return;
            }

            TreeItem item = null;

            if (type == SCHEDULER_CLUSTER) {
                if (tree.getSelectionCount() > 0)
                    item = new TreeItem(tree.getSelection()[0], SWT.NONE);
                else
                    item = new TreeItem(tree.getItems()[0], SWT.NONE);
                item.setData(path);
                item.setExpanded(true);
            }

            if (type == SCHEDULER_HOST) {
                // herausfinden, ob bereits ein host mit der selben Namen
                // existiert
                for (int i = 0; i < tree.getItem(0).getItemCount(); i++) {
                    if (tree.getItem(0).getItem(i).getText().equalsIgnoreCase(txtName.getText())) {
                        item = tree.getItem(0).getItem(i);
                        break;
                    }
                }
                if (item == null) {
                    // ein Host Item existiert nicht, also einen neuen erstellen
                    item = new TreeItem(tree.getItems()[0], SWT.NONE);
                }

                TreeItem itemPort = new TreeItem(item, SWT.NONE);
                itemPort.setData(path);
                itemPort.setText(txtPort.getText());
                itemPort.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
            }
            item.setText(txtName.getText());

            item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/folder.png"));
            item.setExpanded(true);

            txtName.setFocus();
            txtName.setSelection(0, txtName.getText().length());

            newFile = new File(path);
            if (!newFile.exists()) {
                if (!new File(path).mkdirs()) {
                    // MainWindow.message("could not crate new Remote Directory "
                    // , SWT.ICON_ERROR);
                    MainWindow.message(SOSJOEMessageCodes.JOE_M_0003.label(), SWT.ICON_ERROR);
                    schedulerConfigurationShell.setFocus();
                }
            }
            tree.setSelection(new TreeItem[] { item });

            txtName.setText("");
        } catch (Exception ex) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()) + " " + SOSJOEMessageCodes.JOE_M_0004.params(sType), ex);
            MainWindow.message(SOSJOEMessageCodes.JOE_M_0004.params(sType) + ex.getMessage(), SWT.ICON_ERROR);
            schedulerConfigurationShell.setFocus();
        }
    }

    private void changeSubTreedata(String path, TreeItem _item, String changeName) throws Exception {

        try {
            for (int i = 0; i < _item.getItemCount(); i++) {
                TreeItem cItem = _item.getItem(i);
                String data = sosString.parseToString(cItem.getData());

                data = data.substring(changeName.length());
                data = path + data;
                cItem.setData(data);
                if (cItem.getItemCount() > 0)
                    changeSubTreedata(path, cItem, changeName);
            }
        } catch (Exception e) {
            throw new Exception(SOSJOEMessageCodes.JOE_E_0006.label() + e.toString(), e);
        }
    }

}
