package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.conf.listeners.HttpDirectoriesListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class HttpDirectoriesForm extends SOSJOEMessageCodes implements IUnsaved {

    private HttpDirectoriesListener listener = null;
    private Group httpDirectoriesGroup = null;
    private Text tUrlPath = null;
    private Text tPath = null;
    private Table tHttpDirectory = null;
    private Button bApplyHttpDirectory = null;
    private Button bRemoveHttpDirectory = null;

    public HttpDirectoriesForm(Composite parent, int style, SchedulerDom dom, Element config) {
        super(parent, style);
        listener = new HttpDirectoriesListener(dom, config);
        initialize();
        listener.fillHttpDirectoryTable(tHttpDirectory);
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(653, 468));
        tUrlPath.setFocus();
    }

    private void createGroup() {
        httpDirectoriesGroup = JOE_G_HttpDirectoriesForm_DirectoriesGroup.control(new Group(this, SWT.NONE));
        createGroup1();
        GridLayout gridLayout = new GridLayout();
        httpDirectoriesGroup.setLayout(gridLayout);
        new Label(httpDirectoriesGroup, SWT.NONE);
    }

    private void createGroup1() {
        final Group group_1 = JOE_G_HttpDirectoriesForm_Group1.control(new Group(httpDirectoriesGroup, SWT.NONE));
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
        gridData_2.heightHint = 427;
        gridData_2.widthHint = 525;
        group_1.setLayoutData(gridData_2);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group_1.setLayout(gridLayout);
        final Label urlPathLabel = JOE_L_HttpDirectoriesForm_URLPath.control(new Label(group_1, SWT.NONE));
        tUrlPath = JOE_T_HttpDirectoriesForm_URLPath.control(new Text(group_1, SWT.BORDER));
        tUrlPath.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (!tUrlPath.getText().isEmpty() && (tUrlPath.getText().charAt(0) != '/')) {
                    tUrlPath.setText("/" + tUrlPath.getText());
                    tUrlPath.setSelection(2);
                }
                if (e.keyCode == SWT.CR && !"".equals(tUrlPath.getText())) {
                    applyHttpDirectory();
                }
            }
        });
        tUrlPath.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                bApplyHttpDirectory.setEnabled(!"".equals(tUrlPath.getText()));
            }
        });
        tUrlPath.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final Label pathLabel = JOE_L_HttpDirectoriesForm_Path.control(new Label(group_1, SWT.NONE));
        tPath = JOE_T_HttpDirectoriesForm_Path.control(new Text(group_1, SWT.BORDER));
        tPath.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(tUrlPath.getText())) {
                    applyHttpDirectory();
                }
            }
        });
        tPath.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                bApplyHttpDirectory.setEnabled(!"".equals(tPath.getText()));
            }
        });
        tPath.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        bApplyHttpDirectory = JOE_B_HttpDirectoriesForm_Apply.control(new Button(group_1, SWT.NONE));
        bApplyHttpDirectory.setEnabled(false);
        bApplyHttpDirectory.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                applyHttpDirectory();
            }
        });
        bApplyHttpDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        tHttpDirectory = JOE_Tbl_HttpDirectoriesForm_DirectoriesTable.control(new Table(group_1, SWT.FULL_SELECTION | SWT.BORDER));
        tHttpDirectory.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tHttpDirectory.getSelectionCount() > 0) {
                    TableItem item = tHttpDirectory.getItem(tHttpDirectory.getSelectionIndex());
                    tUrlPath.setText(item.getText(0));
                    tPath.setText(item.getText(1));
                    bApplyHttpDirectory.setEnabled(false);
                    tUrlPath.setFocus();
                }
                bRemoveHttpDirectory.setEnabled(tHttpDirectory.getSelectionCount() > 0);
            }
        });
        tHttpDirectory.setLinesVisible(true);
        tHttpDirectory.setHeaderVisible(true);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        gridData_1.minimumHeight = 100;
        gridData_1.horizontalIndent = 4;
        tHttpDirectory.setLayoutData(gridData_1);
        final TableColumn urlPathTableColumn = JOE_TCl_HttpDirectoriesForm_URLPath.control(new TableColumn(tHttpDirectory, SWT.NONE));
        urlPathTableColumn.setWidth(150);
        final TableColumn pathTableColumn = JOE_TCl_HttpDirectoriesForm_Path.control(new TableColumn(tHttpDirectory, SWT.NONE));
        pathTableColumn.setWidth(250);
        bRemoveHttpDirectory = JOE_B_HttpDirectoriesForm_Remove.control(new Button(group_1, SWT.NONE));
        bRemoveHttpDirectory.setEnabled(false);
        bRemoveHttpDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bRemoveHttpDirectory.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tHttpDirectory.getSelectionCount() > 0) {
                    tHttpDirectory.remove(tHttpDirectory.getSelectionIndex());
                    tHttpDirectory.deselectAll();
                    tUrlPath.setText("");
                    tPath.setText("");
                    bApplyHttpDirectory.setEnabled(false);
                    listener.applyHttpDirectory(tHttpDirectory.getItems());
                }
                bRemoveHttpDirectory.setEnabled(false);
            }
        });
    }

    private void applyHttpDirectory() {
        String path = tPath.getText();
        String urlPath = tUrlPath.getText();
        TableItem[] items = tHttpDirectory.getItems();
        boolean found = false;
        for (int i = 0; i < items.length; i++) {
            if (items[i].getText(0).equals(urlPath)) {
                items[i].setText(1, path);
                found = true;
            }
        }
        if (!found) {
            TableItem item = new TableItem(tHttpDirectory, SWT.NONE);
            item.setText(new String[] { urlPath, path });
        }
        tHttpDirectory.deselectAll();
        tPath.setText("");
        tUrlPath.setText("");
        bRemoveHttpDirectory.setEnabled(false);
        bApplyHttpDirectory.setEnabled(false);
        tPath.setFocus();
        listener.applyHttpDirectory(tHttpDirectory.getItems());
    }

    public boolean isUnsaved() {
        return false;
    }

    public void apply() {
        applyHttpDirectory();
    }

}