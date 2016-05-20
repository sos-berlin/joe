/**
 *
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.listeners.BaseListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class SchedulerBaseFileForm extends SOSJOEMessageCodes implements IUnsaved {

    private static final String IMAGE_ICON_OPEN_GIF = "/sos/scheduler/editor/icon_open.gif";
    private static final String IMAGE_ICON_EDIT_GIF = "/sos/scheduler/editor/icon_edit.gif";
    private BaseListener listener = null;
    private Group group = null;
    private Label label1 = null;
    private Text tFile = null;
    private Button bApply = null;
    private Button bNew = null;
    private Button bRemove = null;
    private Label label = null;
    private Label label2 = null;
    private Label label3 = null;
    private Text tComment = null;
    private Table table = null;
    private Button butOpen = null;
    private Button button = null;
    private Button butOpenFileDialog = null;

    public SchedulerBaseFileForm(final Composite parent, final int style, final SchedulerDom dom) throws JDOMException {
        super(parent, style);
        listener = new BaseListener(dom);
        initialize();
        listener.fillTable(table);
    }

    @Override
    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applyFile();
        }
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(657, 329));
    }

    private void createGroup() {
        GridData gridData21 = new GridData();
        gridData21.horizontalAlignment = GridData.FILL;
        gridData21.verticalAlignment = GridData.CENTER;
        GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
        gridData11.heightHint = 10;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.verticalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.verticalSpan = 1;
        gridData2.verticalAlignment = GridData.BEGINNING;
        GridData gridData1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.verticalSpacing = 5;
        gridLayout.horizontalSpacing = 5;
        group = JOE_G_BaseForm_BaseFiles.control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout);
        label1 = JOE_L_BaseForm_BaseFile.control(new Label(group, SWT.NONE));
        tFile = JOE_T_BaseForm_BaseFile.control(new Text(group, SWT.BORDER));
        bApply = JOE_B_BaseForm_Apply.control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData1);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                applyFile();
            }
        });
        GridData gridData8 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        label3 = JOE_L_BaseForm_BaseComment.control(new Label(group, SWT.NONE));
        label3.setLayoutData(gridData8);
        GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, false, false);
        gridData9.heightHint = 80;
        tComment = JOE_T_BaseForm_BaseComment.control(new Text(group, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL));
        tComment.setLayoutData(gridData9);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.setEnabled(false);
        tComment.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {
                    tComment.setSelection(0, tComment.getText().length());
                }
            }
        });
        tComment.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                bApply.setEnabled(!"".equals(tFile.getText()));
                button.setEnabled(!"".equals(tFile.getText()));
            }
        });
        final Composite composite = JOE_Cmp_BaseForm_CommentOpen.control(new Composite(group, SWT.NONE));
        composite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        gridLayout_1.horizontalSpacing = 0;
        composite.setLayout(gridLayout_1);
        button = JOE_B_BaseForm_Comment.control(new Button(composite, SWT.NONE));
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, true);
        button.setLayoutData(gridData);
        button.setEnabled(false);
        button.setImage(ResourceManager.getImageFromResource(IMAGE_ICON_EDIT_GIF));
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
                if (text != null) {
                    tComment.setText(text);
                }
            }
        });
        butOpenFileDialog = JOE_B_BaseForm_OpenFileDialog.control(new Button(composite, SWT.NONE));
        butOpenFileDialog.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, true));
        butOpenFileDialog.setEnabled(false);
        butOpenFileDialog.setImage(ResourceManager.getImageFromResource(IMAGE_ICON_OPEN_GIF));
        butOpenFileDialog.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                openFileDialog();
            }
        });
        label = JOE_Sep_BaseForm_S1.control(new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_OUT));
        label.setLayoutData(gridData11);
        createTable();
        bNew = JOE_B_BaseForm_NewBaseFile.control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData3);
        bNew.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                listener.newBaseFile();
                setInput(true);
            }
        });
        getShell().setDefaultButton(bNew);
        butOpen = JOE_B_BaseForm_OpenBaseFile.control(new Button(group, SWT.NONE));
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butOpen.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                openBaseElement();
            }
        });
        label2 = JOE_Sep_BaseForm_S2.control(new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL));
        label2.setLayoutData(gridData21);
        bRemove = JOE_B_BaseForm_RemoveBaseFile.control(new Button(group, SWT.NONE));
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData2);
        bRemove.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int index = table.getSelectionIndex();
                    listener.removeBaseFile(index);
                    table.remove(index);
                    if (index >= table.getItemCount()) {
                        index--;
                    }
                    if (table.getItemCount() > 0) {
                        table.select(index);
                        listener.selectBaseFile(index);
                        setInput(true);
                    } else {
                        setInput(false);
                    }
                }
                bRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
        tFile.setEnabled(false);
        tFile.setLayoutData(gridData4);
        tFile.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(!"".equals(tFile.getText()));
                button.setEnabled(!"".equals(tFile.getText()));
            }
        });
        tFile.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(tFile.getText())) {
                    applyFile();
                }
            }
        });
    }

    private void createTable() {
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 2, 4);
        table = JOE_Tbl_BaseForm_BaseTable.control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                openBaseElement();
            }
        });
        table.setHeaderVisible(true);
        table.setLayoutData(gridData);
        table.setLinesVisible(true);
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    listener.selectBaseFile(table.getSelectionIndex());
                    setInput(true);
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_BaseForm_BaseFiles.control(new TableColumn(table, SWT.NONE));
        tableColumn.setWidth(300);
        TableColumn tableColumn1 = JOE_TCl_BaseForm_BaseComment.control(new TableColumn(table, SWT.NONE));
        table.setSortColumn(tableColumn1);
        tableColumn1.setWidth(300);
    }

    private void applyFile() {
        listener.applyBaseFile(tFile.getText(), tComment.getText());
        listener.fillTable(table);
        setInput(false);
        getShell().setDefaultButton(bNew);
    }

    private void setInput(final boolean enabled) {
        tFile.setEnabled(enabled);
        tComment.setEnabled(enabled);
        butOpenFileDialog.setEnabled(enabled);
        if (enabled) {
            tFile.setText(listener.getFile());
            tComment.setText(listener.getComment());
            tFile.setFocus();
        } else {
            tFile.setText("");
            tComment.setText("");
        }
        bApply.setEnabled(false);
        button.setEnabled(false);
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }

    private void openFileDialog() {
        sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
        String currPath = "";
        String sep = System.getProperty("file.separator");
        if (con.getCurrentEditor().getFilename() != null && !con.getCurrentEditor().getFilename().isEmpty()) {
            currPath = new java.io.File(con.getCurrentEditor().getFilename()).getParent();
        } else {
            currPath = Options.getSchedulerData() + sep + "config";
        }
        currPath = currPath.replace("/".toCharArray()[0], sep.toCharArray()[0]);
        currPath = currPath.replace("\\".toCharArray()[0], sep.toCharArray()[0]);
        FileDialog fdialog = JOE_FD_BaseForm_OpenBaseFile.control(new FileDialog(MainWindow.getSShell(), SWT.OPEN));
        fdialog.setFilterPath(currPath);
        fdialog.setFilterExtensions(new String[] { "*.xml", "*.sosdoc", "*.*" });
        String fname = fdialog.open();
        if (fname == null) {
            return;
        }
        fname = fname.replaceAll("/", sep);
        if (fname.toLowerCase().startsWith(currPath.toLowerCase())) {
            fname = fname.substring(currPath.length() + 1);
        }
        tFile.setText(fname);
    }

    private void openBaseElement() {
        String currPath = "";
        String sep = System.getProperty("file.separator");
        if (tFile.getText() != null && !tFile.getText().isEmpty()) {
            sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
            if (con.getCurrentEditor().getFilename() != null && !con.getCurrentEditor().getFilename().isEmpty()) {
                currPath = new java.io.File(con.getCurrentEditor().getFilename()).getParent();
            } else {
                currPath = Options.getSchedulerData() + sep + "config";
            }
            if (!(currPath.endsWith("/") || currPath.endsWith("\\"))) {
                currPath = currPath.concat(sep);
            }
            if (tFile.getText().toLowerCase().startsWith(currPath.toLowerCase())) {
                currPath = tFile.getText();
            } else {
                currPath = currPath.concat(tFile.getText());
            }
            con.openScheduler(currPath);
            con.setStatusInTitle();
        } else {
            MainWindow.message("There is no Basefile defined.", SWT.ICON_WARNING | SWT.OK);
        }
    }

}