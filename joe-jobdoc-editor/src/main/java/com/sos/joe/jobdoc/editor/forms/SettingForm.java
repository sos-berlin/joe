package com.sos.joe.jobdoc.editor.forms;

import static com.sos.dialog.Globals.MsgHandler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.jobdoc.editor.listeners.SettingListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class SettingForm extends JobDocBaseForm<SettingListener> {

    private Group group1 = null;
    private Label label6 = null;
    private Label label7 = null;
    private Text tName = null;
    private Text tDefault = null;
    private Label label8 = null;
    private Text tID = null;
    private Label label9 = null;
    private Combo cReference = null;
    private Label label10 = null;
    private Button cRequired = null;
    private Label label13 = null;
    private Button bNotes = null;
    private Combo cType = null;
    private Label label = null;
    private Table tSettings = null;
    private Button bNew = null;
    private Label label1 = null;
    private Button bRemove = null;

    public SettingForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new SettingListener(dom, parentElement);
        initialize();
    }

    private void initialize() {
        createGroup1();
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        cType.setItems(listener.getTypes());
        setSettingStatus(false);
        listener.fillSettings(tSettings);
    }

    private void createGroup1() {
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData2 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 4);
        GridData gridData14 = new GridData(GridData.END, GridData.CENTER, false, false);
        GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
        GridLayout gridLayout1 = new GridLayout(6, false);
        group1 = MsgHandler.newMsg("JOE_G_SettingForm_Settings").control(new SOSGroup(this, SWT.NONE));
        group1.setLayout(gridLayout1);
        label6 = MsgHandler.newMsg("JOE_L_Name").control(new SOSLabel(group1, SWT.NONE));
        tName = MsgHandler.newMsg("JOE_T_SettingForm_Name").control(new Text(group1, SWT.BORDER));
        tName.setLayoutData(gridData3);
        tName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                applySettingStatus();
            }
        });
        bApply = MsgHandler.newMsg("JOE_B_SettingForm_Apply").control(new Button(group1, SWT.NONE));
        bApply.setLayoutData(gridData);
        bApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                applySetting();
            }
        });
        label7 = MsgHandler.newMsg("JOE_L_SettingForm_DefaultValue").control(new SOSLabel(group1, SWT.NONE));
        tDefault = MsgHandler.newMsg("JOE_T_SettingForm_DefaultValue").control(new Text(group1, SWT.BORDER));
        tDefault.setLayoutData(gridData11);
        tDefault.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                applySettingStatus();
            }
        });
        label13 = MsgHandler.newMsg("JOE_L_SettingForm_Type").control(new SOSLabel(group1, SWT.NONE));
        createCType();
        label9 = MsgHandler.newMsg("JOE_L_SettingForm_Reference").control(new SOSLabel(group1, SWT.NONE));
        createCReference();
        label8 = MsgHandler.newMsg("JOE_L_SettingForm_ID").control(new SOSLabel(group1, SWT.NONE));
        tID = MsgHandler.newMsg("JOE_T_SettingForm_ID").control(new Text(group1, SWT.BORDER));
        tID.setLayoutData(gridData12);
        tID.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                applySettingStatus();
            }
        });
        label10 = MsgHandler.newMsg("JOE_L_SettingForm_Required").control(new SOSLabel(group1, SWT.NONE));
        cRequired = MsgHandler.newMsg("JOE_B_SettingForm_Required").control(new Button(group1, SWT.CHECK));
        cRequired.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                applySettingStatus();
            }
        });
        cRequired.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                applySettingStatus();
            }
        });
        bNotes = MsgHandler.newMsg("JOE_B_SettingForm_Notes").control(new Button(group1, SWT.NONE));
        bNotes.setLayoutData(gridData14);
        bNotes.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String tip = "";
                DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener.isNewSetting(),
                        MsgHandler.newMsg("JOE_B_SettingForm_Notes").label());
            }
        });
        label = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData1);
        tSettings = MsgHandler.newMsg("JOE_Tbl_SettingForm_Settings").control(new Table(group1, SWT.BORDER));
        tSettings.setHeaderVisible(true);
        tSettings.setLayoutData(gridData2);
        tSettings.setLinesVisible(true);
        tSettings.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tSettings.getSelectionCount() > 0 && listener.selectSetting(tSettings.getSelectionIndex())) {
                    setSettingStatus(true);
                    bRemove.setEnabled(true);
                }
            }
        });
        TableColumn tableColumn = MsgHandler.newMsg("JOE_TCl_SettingForm_Name").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn.setWidth(150);
        TableColumn tableColumn31 = MsgHandler.newMsg("JOE_TCl_SettingForm_Default").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn31.setWidth(120);
        TableColumn tableColumn3 = MsgHandler.newMsg("JOE_TCl_SettingForm_Type").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn3.setWidth(80);
        TableColumn tableColumn4 = MsgHandler.newMsg("JOE_TCl_SettingForm_Required").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn4.setWidth(70);
        TableColumn tableColumn2 = MsgHandler.newMsg("JOE_TCl_SettingForm_Reference").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn2.setWidth(110);
        TableColumn tableColumn1 = MsgHandler.newMsg("JOE_TCl_SettingForm_ID").control(new TableColumn(tSettings, SWT.NONE));
        tableColumn1.setWidth(120);
        bNew = MsgHandler.newMsg("JOE_B_SettingForm_New").control(new Button(group1, SWT.NONE));
        bNew.setLayoutData(gridData5);
        bNew.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.setNewSetting();
                setSettingStatus(true);
                bRemove.setEnabled(false);
                tSettings.deselectAll();
            }
        });
        label1 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setText("Label");
        label1.setLayoutData(gridData6);
        bRemove = MsgHandler.newMsg("JOE_B_SettingForm_Remove").control(new Button(group1, SWT.NONE));
        bRemove.setLayoutData(gridData4);
        bRemove.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tSettings.getSelectionCount() > 0 && listener.removeSetting(tSettings.getSelectionIndex())) {
                    setSettingStatus(false);
                    bRemove.setEnabled(false);
                    tSettings.deselectAll();
                    listener.fillSettings(tSettings);
                }
            }
        });
    }

    private void createCReference() {
        GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
        cReference = MsgHandler.newMsg("JOE_Cbo_SettingForm_Reference").control(new Combo(group1, SWT.NONE));
        cReference.setLayoutData(gridData13);
        cReference.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                applySettingStatus();
            }
        });
    }

    private void createCType() {
        GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cType = MsgHandler.newMsg("JOE_Cbo_SettingForm_Type").control(new Combo(group1, SWT.READ_ONLY));
        cType.setLayoutData(gridData15);
        cType.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                applySettingStatus();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }
        });
    }

    private void setSettingStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tDefault.setEnabled(enabled);
        tID.setEnabled(enabled);
        cRequired.setEnabled(enabled);
        cReference.setEnabled(enabled);
        cType.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tDefault.setText(listener.getDefaultValue());
            tID.setText(listener.getID());
            cRequired.setSelection(listener.isRequired());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener.getReference());
            cType.select(cType.indexOf(listener.getType()));
            tName.setFocus();
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(false);
    }

    private void applySettingStatus() {
        bApply.setEnabled(!tName.getText().isEmpty());
        Utils.setBackground(tName, true);
    }

    @Override
    protected void applySetting() {
        listener.applySetting(tName.getText(), tDefault.getText(), tID.getText(), cRequired.getSelection(), cReference.getText(), cType.getText());
        listener.fillSettings(tSettings);
        setSettingStatus(tSettings.getSelectionCount() > 0);
        bRemove.setEnabled(tSettings.getSelectionCount() > 0);
    }

    @Override
    public void openBlank() {
        // TO DO Auto-generated method stub
    }

    @Override
    public boolean applyChanges() {
        return false;
    }

}