package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProcessForm_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProcessForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProcessForm_UseProcess;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ProcessForm_EnvironmentVariables;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ProcessForm_Process;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ProcessForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ProcessForm_Log;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ProcessForm_Parameter;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ProcessForm_Value;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ProcessForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ProcessForm_Value;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProcessForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProcessForm_Log;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProcessForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProcessForm_Parameter;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProcessForm_Value;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_ProcessForm_Variables;

import org.eclipse.swt.SWT;
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

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.ProcessListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ProcessForm extends JobDocBaseForm<ProcessListener> {

    private Group group = null;
    private Label label = null;
    private Label label1 = null;
    private Label label2 = null;
    private Text tFile = null;
    private Text tParameter = null;
    private Text tLog = null;
    private Button cUseProcess = null;
    private Group group1 = null;
    private Label label3 = null;
    private Text tName = null;
    private Label label4 = null;
    private Text tValue = null;
    private Label label5 = null;
    private Table tVariables = null;
    private Button bRemove = null;

    public ProcessForm(Composite parent, int style, DocumentationDom dom, Element job) {
        super(parent, style);
        initialize();
        listener = new ProcessListener(dom, job);
        cUseProcess.setSelection(listener.isProcess());
        initValues();
    }

    private void initialize() {
        cUseProcess = JOE_B_ProcessForm_UseProcess.Control(new Button(this, SWT.RADIO));
        cUseProcess.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (cUseProcess.getSelection() != listener.isProcess()) {
                    if (cUseProcess.getSelection()) {
                        listener.setProcess();
                    }
                    initValues();
                }
            }
        });
        createGroup();
    }

    private void createGroup() {
        GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true, true);
        GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData2.horizontalIndent = 7;
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData1.horizontalIndent = 7;
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalIndent = 7;
        GridLayout gridLayout = new GridLayout(2, false);
        group = JOE_G_ProcessForm_Process.Control(new SOSGroup(this, SWT.NONE));
        group.setLayoutData(gridData11);
        group.setLayout(gridLayout);
        label = JOE_L_ProcessForm_File.Control(new SOSLabel(group, SWT.NONE));
        tFile = JOE_T_ProcessForm_File.Control(new Text(group, SWT.BORDER));
        tFile.setLayoutData(gridData);
        tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setFile(tFile.getText());
                if (cUseProcess.getSelection()) {
                    Utils.setBackground(tFile, true);
                }
            }
        });
        label1 = JOE_L_ProcessForm_Parameter.Control(new SOSLabel(group, SWT.NONE));
        tParameter = JOE_T_ProcessForm_Parameter.Control(new Text(group, SWT.BORDER));
        tParameter.setLayoutData(gridData1);
        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setParam(tParameter.getText());
            }
        });
        label2 = JOE_L_ProcessForm_Log.Control(new SOSLabel(group, SWT.NONE));
        tLog = JOE_T_ProcessForm_Log.Control(new Text(group, SWT.BORDER));
        tLog.setLayoutData(gridData2);
        tLog.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setLog(tLog.getText());
            }
        });
        createGroup1();
    }

    private void createGroup1() {
        GridData gridData9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData7 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
        GridData gridData5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridData gridData3 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        GridLayout gridLayout1 = new GridLayout(5, false);
        group1 = JOE_G_ProcessForm_EnvironmentVariables.Control(new SOSGroup(group, SWT.NONE));
        group1.setLayout(gridLayout1);
        group1.setLayoutData(gridData3);
        label3 = JOE_L_Name.Control(new SOSLabel(group1, SWT.NONE));
        tName = JOE_T_ProcessForm_Name.Control(new Text(group1, SWT.BORDER));
        tName.setLayoutData(gridData4);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tName.getText().isEmpty());
                getShell().setDefaultButton(bApply);
            }
        });
        label4 = JOE_L_ProcessForm_Value.Control(new SOSLabel(group1, SWT.NONE));
        tValue = JOE_T_ProcessForm_Value.Control(new Text(group1, SWT.BORDER));
        tValue.setLayoutData(gridData5);
        tValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tName.getText().isEmpty());
                getShell().setDefaultButton(bApply);
            }
        });
        bApply = JOE_B_ProcessForm_Apply.Control(new Button(group1, SWT.NONE));
        bApply.setLayoutData(gridData9);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyParam();
            }
        });
        label5 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label");
        label5.setLayoutData(gridData6);
        tVariables = JOE_Tbl_ProcessForm_Variables.Control(new Table(group1, SWT.BORDER));
        tVariables.setHeaderVisible(true);
        tVariables.setLayoutData(gridData7);
        tVariables.setLinesVisible(true);
        tVariables.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    TableItem item = tVariables.getItem(tVariables.getSelectionIndex());
                    tName.setText(item.getText(0));
                    tValue.setText(item.getText(1));
                    bApply.setEnabled(false);
                    tName.setFocus();
                }
                bRemove.setEnabled(tVariables.getSelectionCount() > 0);
            }
        });
        bRemove = JOE_B_ProcessForm_Remove.Control(new Button(group1, SWT.NONE));
        bRemove.setLayoutData(gridData8);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    listener.removeVariable(tVariables.getItem(tVariables.getSelectionIndex()).getText(0));
                    listener.fillTable(tVariables);
                    tName.setText("");
                    tValue.setText("");
                    bApply.setEnabled(false);
                }
                bRemove.setEnabled(false);
                boolean valid = !"".equals(tName.getText());
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        TableColumn tableColumn = JOE_TCl_ProcessForm_Name.Control(new TableColumn(tVariables, SWT.NONE));
        tableColumn.setWidth(150);
        TableColumn tableColumn1 = JOE_TCl_ProcessForm_Value.Control(new TableColumn(tVariables, SWT.NONE));
        tableColumn1.setWidth(60);
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applyParam();
        }
    }

    @Override
    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    private void initValues() {
        boolean enabled = cUseProcess.getSelection();
        tFile.setEnabled(enabled);
        tParameter.setEnabled(enabled);
        tLog.setEnabled(enabled);
        tName.setEnabled(enabled);
        tValue.setEnabled(enabled);
        bApply.setEnabled(enabled);
        bRemove.setEnabled(false);
        if (enabled) {
            tFile.setText(listener.getFile());
            tParameter.setText(listener.getParam());
            tLog.setText(listener.getLog());
            tName.setText("");
            tValue.setText("");
            tFile.setFocus();
            listener.fillTable(tVariables);
        }
    }

    private void applyParam() {
        listener.applyParam(tName.getText(), tValue.getText());
        tName.setText("");
        tValue.setText("");
        listener.fillTable(tVariables);
        tVariables.deselectAll();
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        tName.setFocus();
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