package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBResources_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBResources_New;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBResources_Notes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBResources_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_DBResources_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_DBResources_Resources;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_DBResources_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_ApplyChanges;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_DBResources_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_DBResources_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_DBResources_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_DBResources_Resources;

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
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.jobdoc.editor.listeners.DatabaseResourceListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DatabaseResourcesForm extends JobDocBaseForm<DatabaseResourceListener> {

    private Group group1 = null;
    private Label label1 = null;
    private Text tName = null;
    private Label label2 = null;
    private Combo cType = null;
    private Button bNotes = null;
    private Table tResources = null;
    private Button bApply = null;
    private Button bNewRes = null;
    private Button bRemoveRes = null;
    private Label label4 = null;
    private Label label5 = null;
    private NoteForm fNote = null;

    public DatabaseResourcesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new DatabaseResourceListener(dom, parentElement);
        initialize();
    }

    private void initialize() {
        createGroup();
        cType.setItems(listener.getTypes());
        setDatabaseStatus(true);
    }

    private void createGroup() {
        createSashForm();
    }

    private void createGroup1() {
        GridData gridData14 = new GridData();
        gridData14.horizontalAlignment = GridData.FILL;
        gridData14.verticalAlignment = GridData.BEGINNING;
        GridData gridData13 = new GridData();
        gridData13.horizontalAlignment = GridData.FILL;
        gridData13.verticalAlignment = GridData.CENTER;
        GridData gridData12 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData12.widthHint = 90;
        GridData gridData10 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 4);
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL;
        gridData9.verticalAlignment = GridData.CENTER;
        GridData gridData5 = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
        GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true, true);
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5;
        group1 = JOE_G_DBResources_Resources.Control(new SOSGroup(this, SWT.NONE));
        group1.setLayout(gridLayout1);
        group1.setLayoutData(gridData11);
        label1 = JOE_L_Name.Control(new SOSLabel(group1, SWT.NONE));
        tName = JOE_T_DBResources_Name.Control(new Text(group1, SWT.BORDER));
        tName.setLayoutData(gridData3);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tName, true);
                setApplyResStatus();
            }
        });
        createCType();
        bApply = JOE_B_DBResources_Apply.Control(new Button(group1, SWT.NONE));
        bApply.setLayoutData(gridData12);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyResource();
            }
        });
        label5 = new SOSLabel(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setLayoutData(gridData5);
        tResources = JOE_Tbl_DBResources_Resources.Control(new Table(group1, SWT.FULL_SELECTION | SWT.BORDER));
        tResources.setHeaderVisible(true);
        tResources.setLayoutData(gridData10);
        tResources.setLinesVisible(true);
        tResources.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tResources.getSelectionCount() > 0) {
                    listener.setResource(tResources.getSelectionIndex());
                    if (fNote.isUnsaved()) {
                        int ok = ErrorLog.message(JOE_M_ApplyChanges.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                        if (ok == SWT.CANCEL) {
                            return;
                        }
                        if (ok == SWT.NO) {
                            return;
                        } else if (ok == SWT.YES) {
                            fNote.apply();
                        }
                    }
                    fNote.setEnabled(true);
                    fNote.setParams(dom, listener.getResource(), "note", true, true);
                    fNote.setTitle("Note");
                    setResourceStatus(true);
                    bRemoveRes.setEnabled(true);
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_DBResources_Name.Control(new TableColumn(tResources, SWT.NONE));
        tableColumn.setWidth(250);
        TableColumn tableColumn1 = JOE_TCl_DBResources_Type.Control(new TableColumn(tResources, SWT.NONE));
        tableColumn1.setWidth(60);
        bNewRes = JOE_B_DBResources_New.Control(new Button(group1, SWT.NONE));
        bNewRes.setLayoutData(gridData13);
        bNewRes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewResource();
                fNote.setParams(dom, listener.getResource(), "note", true, true);
                fNote.setTitle("Note");
                fNote.setEnabled(false);
                setResourceStatus(true);
                tResources.deselectAll();
            }
        });
        label4 = new SOSLabel(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setLayoutData(gridData9);
        bRemoveRes = JOE_B_DBResources_Remove.Control(new Button(group1, SWT.NONE));
        bRemoveRes.setLayoutData(gridData14);
        bRemoveRes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tResources.getSelectionCount() > 0) {
                    listener.removeResource(tResources.getSelectionIndex());
                    setResourceStatus(false);
                    listener.fillResources(tResources);
                }
                bRemoveRes.setEnabled(false);
            }
        });
        bNotes = JOE_B_DBResources_Notes.Control(new Button(group1, SWT.NONE));
        bNotes.setLayoutData(new GridData());
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DocumentationForm.openNoteDialog(dom, listener.getResource(), "note", null, true, !listener.isNewDatabase(),
                        JOE_B_DBResources_Notes.label());
            }
        });
        fNote = new NoteForm(group1, SWT.NONE);
        fNote.setTitle(JOE_B_DBResources_Notes.label());
        fNote.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 5, 1));
        fNote.setEnabled(false);
    }

    private void createCType() {
        GridData gridData4 = new GridData();
        gridData4.widthHint = 100;
        label2 = JOE_L_DBResources_Type.Control(new SOSLabel(group1, SWT.NONE));
        label2.setLayoutData(new GridData());
        cType = JOE_Cbo_DBResources_Type.Control(new Combo(group1, SWT.READ_ONLY));
        cType.setLayoutData(gridData4);
        cType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setApplyResStatus();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
    }

    private void createSashForm() {
        createComposite();
        createComposite1();
    }

    private void createComposite() {
        createGroup1();
    }

    private void createComposite1() {
        //
    }

    @Override
    public void apply() {
        if (bApply.isEnabled()) {
            applyResource();
        }
    }

    @Override
    public boolean isUnsaved() {
        return false;
    }

    private void setDatabaseStatus(boolean enabled) {
        tResources.setEnabled(enabled);
        bNewRes.setEnabled(enabled);
        setResourceStatus(false);
        if (enabled) {
            listener.fillResources(tResources);
        }
    }

    private void setResourceStatus(boolean enabled) {
        tName.setEnabled(enabled);
        cType.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        bRemoveRes.setEnabled(false);
        if (enabled) {
            tName.setText(listener.getName());
            cType.select(cType.indexOf(listener.getType()));
            tName.setFocus();
        }
        bApply.setEnabled(false);
    }

    private void setApplyResStatus() {
        bApply.setEnabled(!tName.getText().isEmpty());
        getShell().setDefaultButton(bApply);
    }

    private void applyResource() {
        listener.applyResource(tName.getText(), cType.getText());
        listener.fillResources(tResources);
        bRemoveRes.setEnabled(tResources.getSelectionCount() > 0);
        bApply.setEnabled(false);
        setResourceStatus(false);
        fNote.setEnabled(true);
        fNote.setParams(dom, listener.getResource(), "note", true, true);
        fNote.setTitle("Note");
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