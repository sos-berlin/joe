package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ApplicationsForm_ApplyApplication;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ApplicationsForm_NewApplication;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ApplicationsForm_RemoveApplication;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_ApplicationsForm_Reference;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ApplicationsForm_Applications;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ApplicationsForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ApplicationsForm_Reference;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ApplicationsForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ApplicationsForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ApplicationsForm_Reference;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ApplicationsForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ApplicationsForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_ApplicationsForm_Applications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.IUpdateTree;
import com.sos.joe.jobdoc.editor.listeners.ApplicationsListener;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ApplicationsForm extends JobDocBaseForm<ApplicationsListener> {

    private IUpdateTree treeHandler = null;
    private TreeItem tItem = null;
    private Element parentElement = null;
    private Group group = null;
    private Label label = null;
    private Text tName = null;
    private Label label1 = null;
    private Text tID = null;
    private Label label2 = null;
    private Combo cReference = null;
    private Label label3 = null;
    private Table table = null;
    private Button bNew = null;
    private Label label4 = null;
    private Button bRemove = null;

    public ApplicationsForm(Composite parent, int style, DocumentationDom dom, Element parentElement, IUpdateTree treeHandler, TreeItem tItem) {
        super(parent, style);
        this.treeHandler = treeHandler;
        this.tItem = tItem;
        this.parentElement = parentElement;
        listener = new ApplicationsListener(dom, parentElement);
        initialize();
    }

    private void initialize() {
        createGroup();
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        setAppStatus(false);
        fillTable();
    }

    private void createGroup() {
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL;
        gridData8.verticalAlignment = GridData.BEGINNING;
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL;
        gridData7.verticalAlignment = GridData.CENTER;
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL;
        gridData6.verticalAlignment = GridData.CENTER;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 5;
        gridData3.verticalAlignment = GridData.CENTER;
        gridData3.horizontalAlignment = GridData.FILL;
        GridData gridData2 = new GridData();
        gridData2.verticalSpan = 2;
        gridData2.verticalAlignment = GridData.BEGINNING;
        gridData2.horizontalAlignment = GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 4;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalSpan = 3;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        gridData.verticalAlignment = GridData.CENTER;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group = JOE_G_ApplicationsForm_Applications.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout);
        label = JOE_L_Name.Control(new SOSLabel(group, SWT.NONE));
        tName = JOE_T_ApplicationsForm_Name.Control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData);
        tName.addModifyListener(modifyTextListener);
        bApply = JOE_B_ApplicationsForm_ApplyApplication.Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData2);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyApp();
            }
        });
        label1 = JOE_L_ApplicationsForm_ID.Control(new SOSLabel(group, SWT.NONE));
        tID = JOE_T_ApplicationsForm_ID.Control(new Text(group, SWT.BORDER));
        tID.setLayoutData(gridData4);
        tID.addModifyListener(modifyTextListener);
        label2 = JOE_L_ApplicationsForm_Reference.Control(new SOSLabel(group, SWT.NONE));
        createCReference();
        label3 = new SOSLabel(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label3.setLayoutData(gridData3);
        table = JOE_Tbl_ApplicationsForm_Applications.Control(new Table(group, SWT.BORDER));
        table.setHeaderVisible(true);
        table.setLayoutData(gridData1);
        table.setLinesVisible(true);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    if (listener.selectApp(table.getSelectionIndex())) {
                        setAppStatus(true);
                        bRemove.setEnabled(true);
                    }
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_ApplicationsForm_Name.Control(new TableColumn(table, SWT.NONE));
        tableColumn.setWidth(200);
        TableColumn tableColumn1 = JOE_TCl_ApplicationsForm_ID.Control(new TableColumn(table, SWT.NONE));
        tableColumn1.setWidth(180);
        TableColumn tableColumn2 = JOE_TCl_ApplicationsForm_Reference.Control(new TableColumn(table, SWT.NONE));
        tableColumn2.setWidth(180);
        bNew = JOE_B_ApplicationsForm_NewApplication.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData6);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewApp();
                setAppStatus(true);
                bRemove.setEnabled(false);
                table.deselectAll();
            }
        });
        label4 = new SOSLabel(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setLayoutData(gridData7);
        bRemove = JOE_B_ApplicationsForm_RemoveApplication.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData8);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    if (listener.removeApp(table.getSelectionIndex())) {
                        setAppStatus(false);
                        fillTable();
                    }
                    bRemove.setEnabled(false);
                }
            }
        });
    }

    private void createCReference() {
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.verticalAlignment = GridData.CENTER;
        cReference = JOE_Cbo_ApplicationsForm_Reference.Control(new Combo(group, SWT.NONE));
        cReference.setLayoutData(gridData5);
        cReference.addModifyListener(modifyTextListener);
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applyApp();
        }
    }

    @Override
    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    private void setAppStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tID.setEnabled(enabled);
        cReference.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tID.setText(listener.getID());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener.getReference());
            tName.setFocus();
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(false);
    }

    @Override
    protected void setApplyStatus() {
        bApply.setEnabled(!tName.getText().isEmpty());
        Utils.setBackground(tName, true);
    }

    private void applyApp() {
        listener.applyApp(tName.getText(), tID.getText(), cReference.getText());
        bApply.setEnabled(false);
        fillTable();
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }

    private void fillTable() {
        listener.fillApps(table);
        treeHandler.fillApplications(tItem, parentElement, true);
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