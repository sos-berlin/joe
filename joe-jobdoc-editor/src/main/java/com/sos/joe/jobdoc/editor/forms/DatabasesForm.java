package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBForm_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBForm_New;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBForm_Required;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_DBForm_Databases;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_DBForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_DBForm_Required;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_DBForm_Database;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_DBForm_Databases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSComposite;
import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.DatabasesListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DatabasesForm extends JobDocBaseForm<DatabasesListener> {

    private Group group = null;
    private Composite composite1 = null;
    private Button cRequired = null;
    private Table tDatabases = null;
    private Button bRemove = null;
    private Label label = null;
    private Button bNew = null;
    private Label label6 = null;
    private Text tDB = null;
    private DocumentationForm _gui = null;

    public DatabasesForm(Composite parent, int style, DocumentationDom dom, Element parentElement, DocumentationForm gui) {
        super(parent, style);
        this.dom = dom;
        _gui = gui;
        listener = new DatabasesListener(dom, parentElement);
        initialize();
        listener.fillDatabases(tDatabases);
    }

    private void initialize() {
        createGroup();
        bRemove.setEnabled(false);
        setDatabaseStatus(false);
    }

    private void createGroup() {
        group = JOE_G_DBForm_Databases.Control(new SOSGroup(this, SWT.NONE));
        createSashForm();
    }

    private void createSashForm() {
        createComposite1();
    }

    private void createComposite1() {
        GridData gridData8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData8.widthHint = 90;
        GridData gridData2 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
        gridData2.widthHint = 90;
        GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 5;
        composite1 = new SOSComposite(group, SWT.NONE);
        composite1.setLayout(gridLayout3);
        label6 = JOE_L_Name.Control(new SOSLabel(composite1, SWT.NONE));
        tDB = JOE_T_DBForm_Database.Control(new Text(composite1, SWT.BORDER));
        tDB.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        tDB.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        new Label(composite1, SWT.NONE);
        cRequired = JOE_B_DBForm_Required.Control(new Button(composite1, SWT.CHECK));
        cRequired.setLayoutData(new GridData());
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        bApply = JOE_B_DBForm_Apply.Control(new Button(composite1, SWT.NONE));
        bApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyDatabase();
            }
        });
        label = new Label(composite1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
        tDatabases = JOE_Tbl_DBForm_Databases.Control(new Table(composite1, SWT.FULL_SELECTION | SWT.BORDER));
        TableColumn tableColumn2 = JOE_TCl_DBForm_Name.Control(new TableColumn(tDatabases, SWT.NONE));
        tableColumn2.setWidth(300);
        TableColumn tableColumn3 = JOE_TCl_DBForm_Required.Control(new TableColumn(tDatabases, SWT.NONE));
        tableColumn3.setWidth(173);
        bNew = JOE_B_DBForm_New.Control(new Button(composite1, SWT.NONE));
        bNew.setLayoutData(gridData8);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewDatabase();
                setDatabaseStatus(true);
                tDatabases.deselectAll();
                bApply.setEnabled(true);
                tDB.setFocus();
            }
        });
        tDatabases.setLayoutData(gridData1);
        tDatabases.setHeaderVisible(true);
        tDatabases.setLinesVisible(true);
        tDatabases.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tDatabases.getSelectionCount() > 0) {
                    listener.setDatabase(tDatabases.getSelectionIndex());
                    setDatabaseStatus(true);
                    bRemove.setEnabled(true);
                }
            }
        });
        bRemove = JOE_B_DBForm_Remove.Control(new Button(composite1, SWT.NONE));
        bRemove.setLayoutData(gridData2);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tDatabases.getSelectionCount() > 0) {
                    listener.removeDatabase(tDatabases.getSelectionIndex());
                    _gui.updateDatabaseResource();
                    setDatabaseStatus(false);
                    listener.fillDatabases(tDatabases);
                }
                bRemove.setEnabled(false);
            }
        });
    }

    @Override
    public void apply() {
        if (bApply.isEnabled()) {
            applyDatabase();
        }
    }

    @Override
    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    private void setDatabaseStatus(boolean enabled) {
        tDB.setEnabled(enabled);
        cRequired.setEnabled(enabled);
        if (enabled) {
            tDB.setText(listener.getDBName());
            cRequired.setSelection(listener.isRequired());
        }
        bApply.setEnabled(false);
    }

    private void applyDatabase() {
        listener.applyDatabase(tDB.getText(), cRequired.getSelection());
        listener.fillDatabases(tDatabases);
        bRemove.setEnabled(tDatabases.getSelectionCount() > 0);
        _gui.updateDatabaseResource();
        tDB.setText("");
        cRequired.setSelection(false);
        bApply.setEnabled(false);
        tDatabases.deselectAll();
        tDB.setEnabled(false);
        tDB.setFocus();
    }

    @Override
    public void openBlank() {
        // 
    }

    @Override
    protected void applySetting() {
        // 
    }

    @Override
    public boolean applyChanges() {
        return false;
    }
    
}
