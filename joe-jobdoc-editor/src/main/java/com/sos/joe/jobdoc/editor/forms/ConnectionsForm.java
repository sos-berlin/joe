package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ConnectionsForm_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ConnectionsForm_New;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ConnectionsForm_Notes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ConnectionsForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ConnectionsForm_Connections;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ConnectionsForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ConnectionsForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_ConnectionsForm_Connections;

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

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.IUpdateTree;
import com.sos.joe.jobdoc.editor.listeners.ConnectionsListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ConnectionsForm extends JobDocBaseForm<ConnectionsListener> {

    private IUpdateTree treeHandler = null;
    private DocumentationDom dom = null;
    private Group group = null;
    private Label label = null;
    private Text tName = null;
    private Button bNotes = null;
    private Label label1 = null;
    private Button bNew = null;
    private Label label2 = null;
    private Button bRemove = null;
    private Table tConnections = null;

    public ConnectionsForm(Composite parent, int style, DocumentationDom dom, Element parentElement, IUpdateTree treeHandler) {
        super(parent, style);
        this.treeHandler = treeHandler;
        this.dom = dom;
        listener = new ConnectionsListener(dom, parentElement);
        initialize();
    }

    private void initialize() {
        createGroup();
        setConnectionStatus(false);
        bRemove.setEnabled(false);
        fillTable();
    }

    private void createGroup() {
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 3;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalSpan = 3;
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL;
        gridData6.verticalAlignment = GridData.BEGINNING;
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.verticalAlignment = GridData.CENTER;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.horizontalSpan = 4;
        gridData3.verticalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.verticalAlignment = GridData.CENTER;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        group = JOE_G_ConnectionsForm_Connections.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout);
        label = JOE_L_Name.control(new SOSLabel(group, SWT.NONE));
        tName = JOE_T_ConnectionsForm_Name.control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        bNotes = JOE_B_ConnectionsForm_Notes.control(new Button(group, SWT.NONE));
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DocumentationForm.openNoteDialog(dom, listener.getConnectionElement(), "note", null, true, !listener.isNewConnection(),
                        JOE_B_ConnectionsForm_Notes.label());
            }
        });
        bApply = JOE_B_ConnectionsForm_Apply.control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData2);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyConnection();
            }
        });
        label1 = new SOSLabel(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setLayoutData(gridData3);
        tConnections = JOE_Tbl_ConnectionsForm_Connections.control(new Table(group, SWT.BORDER));
        tConnections.setHeaderVisible(true);
        tConnections.setLayoutData(gridData1);
        tConnections.setLinesVisible(true);
        tConnections.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tConnections.getSelectionCount() > 0) {
                    listener.selectConnection(tConnections.getSelectionIndex());
                    setConnectionStatus(true);
                    bRemove.setEnabled(true);
                    bApply.setEnabled(false);
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_ConnectionsForm_Name.control(new TableColumn(tConnections, SWT.NONE));
        tableColumn.setWidth(400);
        bNew = JOE_B_ConnectionsForm_New.control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData4);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewConnection();
                setConnectionStatus(true);
                bApply.setEnabled(true);
                bRemove.setEnabled(false);
                getShell().setDefaultButton(bApply);
            }
        });
        label2 = new SOSLabel(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setLayoutData(gridData5);
        bRemove = JOE_B_ConnectionsForm_Remove.control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData6);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tConnections.getSelectionCount() > 0 && listener.removeConnection(tConnections.getSelectionIndex())) {
                    setConnectionStatus(false);
                    bRemove.setEnabled(false);
                    bApply.setEnabled(false);
                    fillTable();
                }
            }
        });
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applyConnection();
        }
    }

    @Override
    public boolean isUnsaved() {
        listener.checkSettings();
        return bApply.isEnabled();
    }

    private void applyConnection() {
        listener.applyConnection(tName.getText());
        fillTable();
        bRemove.setEnabled(tConnections.getSelectionCount() > 0);
        bApply.setEnabled(false);
    }

    private void setConnectionStatus(boolean enabled) {
        tName.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tName.setFocus();
        }
        bApply.setEnabled(false);
    }

    private void fillTable() {
        listener.fillConnections(tConnections);
        if (treeHandler != null) {
            treeHandler.fillConnections();
        }
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
        apply();
        return true;
    }

}