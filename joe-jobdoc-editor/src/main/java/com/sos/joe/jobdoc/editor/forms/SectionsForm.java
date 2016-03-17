package com.sos.joe.jobdoc.editor.forms;

import static com.sos.dialog.Globals.MsgHandler;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.jobdoc.editor.listeners.SectionsListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class SectionsForm extends JobDocBaseForm<SectionsListener> {

    IUpdateTree treeHandler = null;
    TreeItem tItem = null;
    Element parentElement = null;
    private Group group = null;
    private Label label5 = null;
    private Label label6 = null;
    private Label label7 = null;
    private Table tSections = null;
    private Text tName = null;
    private Text tID = null;
    private Label label8 = null;
    private Combo cReference = null;
    private Button bNew = null;
    private Label label = null;
    private Button bRemove = null;

    public SectionsForm(Composite parent, int style, DocumentationDom pobjDom, Element parentElement, IUpdateTree treeHandler, TreeItem tItem) {
        super(parent, style);
        initialize();
        dom = pobjDom;
        this.treeHandler = treeHandler;
        this.tItem = tItem;
        this.parentElement = parentElement;
        listener = new SectionsListener(dom, parentElement);
        fillTable();
        setSectionStatus(false);
    }

    private void initialize() {
        createGroup();
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
    }

    private void createGroup() {
        GridData gridData8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 2);
        GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
        GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
        GridLayout gridLayout1 = new GridLayout(5, false);
        group = MsgHandler.newMsg("JOE_G_SectionsForm_Sections").Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout1);
        label5 = MsgHandler.newMsg("JOE_L_Name").Control(new SOSLabel(group, SWT.NONE));
        tName = MsgHandler.newMsg("JOE_T_SectionsForm_Name").Control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        bApply = MsgHandler.newMsg("JOE_B_SectionsForm_Apply").Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData5); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySection();
            }
        });
        label8 = MsgHandler.newMsg("JOE_L_SectionsForm_Reference").Control(new SOSLabel(group, SWT.NONE));
        createCReference();
        label6 = MsgHandler.newMsg("JOE_L_SectionsForm_ID").Control(new SOSLabel(group, SWT.NONE));
        tID = MsgHandler.newMsg("JOE_T_SectionsForm_ID").Control(new Text(group, SWT.BORDER));
        tID.setLayoutData(gridData2);
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        label7 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label7.setText("Label");
        label7.setLayoutData(gridData4);
        tSections = MsgHandler.newMsg("JOE_Tbl_SectionsForm_Sections").Control(new Table(group, SWT.BORDER));
        tSections.setHeaderVisible(true);
        tSections.setLayoutData(gridData1);
        tSections.setLinesVisible(true);
        tSections.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSections.getSelectionCount() > 0 && listener.selectSection(tSections.getSelectionIndex())) {
                    setSectionStatus(true);
                    bRemove.setEnabled(true);
                }
            }
        });
        bNew = MsgHandler.newMsg("JOE_B_SectionsForm_New").Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData6); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewSection();
                setSectionStatus(true);
                bRemove.setEnabled(false);
                tSections.deselectAll();
            }
        });
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData7);
        bRemove = MsgHandler.newMsg("JOE_B_SectionsForm_Remove").Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData8);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSections.getSelectionCount() > 0) {
                    listener.removeSection(tSections.getSelectionIndex());
                    setSectionStatus(false);
                    tSections.deselectAll();
                    fillTable();
                }
            }
        });
        TableColumn tableColumn3 = MsgHandler.newMsg("JOE_TCl_SectionsForm_Name").Control(new TableColumn(tSections, SWT.NONE));
        tableColumn3.setWidth(200);
        TableColumn tableColumn5 = MsgHandler.newMsg("JOE_TCl_SectionsForm_Reference").Control(new TableColumn(tSections, SWT.NONE));
        tableColumn5.setWidth(180);
        TableColumn tableColumn4 = MsgHandler.newMsg("JOE_TCl_SectionsForm_ID").Control(new TableColumn(tSections, SWT.NONE));
        tableColumn4.setWidth(180);
    }

    private void createCReference() {
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cReference = MsgHandler.newMsg("JOE_Cbo_SectionsForm_Reference").Control(new Combo(group, SWT.NONE));
        cReference.setLayoutData(gridData3);
        cReference.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setApplyStatus();
            }
        });
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applySection();
        }
    }

    private void setSectionStatus(boolean enabled) {
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
        bApply.setEnabled(tName.getText().length() > 0);
        Utils.setBackground(tName, true);
    }

    private void applySection() {
        listener.applySection(tName.getText(), tID.getText(), cReference.getText());
        bApply.setEnabled(false);
        fillTable();
        bRemove.setEnabled(tSections.getSelectionCount() > 0);
    }

    private void fillTable() {
        listener.fillSections(tSections);
        if (treeHandler != null) {
            treeHandler.fillSections(tItem, parentElement, true);
        }
    }

    @Override
    protected void applySetting() {
        applySetting();

    }

    @Override
    public void openBlank() {

    }

    @Override
    public boolean applyChanges() {
        return false;
    }

    @Override
    public boolean open(Collection files) {
        return false;
    }

}