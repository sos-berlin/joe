package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProfilesForm_ApplyProfile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProfilesForm_NewProfile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProfilesForm_ProfileNotes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ProfilesForm_RemoveProfile;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ProfilesForm_Profiles;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ProfilesForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ProfilesForm_Name;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_ProfilesForm_Profiles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.sos.joe.jobdoc.editor.listeners.ProfilesListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ProfilesForm extends JobDocBaseForm<ProfilesListener> {

    IUpdateTree treeHandler = null;
    private Group group = null;
    private Label label3 = null;
    private Text tName = null;
    private Button bNotes = null;
    private Label label4 = null;
    private Button bNew = null;
    private Label label5 = null;
    private Button bRemove = null;
    private Table tProfiles = null;

    public ProfilesForm(Composite parent, int style, DocumentationDom dom, Element parentElement, IUpdateTree treeHandler) {
        super(parent, style);
        this.dom = dom;
        this.treeHandler = treeHandler;
        listener = new ProfilesListener(dom, parentElement);
        initialize();
    }

    private void initialize() {
        createGroup();
        setProfileStatus(false);
        bRemove.setEnabled(false);
        fillProfiles();
    }

    private void createGroup() {
        GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 3);
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridLayout gridLayout2 = new GridLayout(4, false);
        group = JOE_G_ProfilesForm_Profiles.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout2);
        label3 = JOE_L_Name.Control(new SOSLabel(group, SWT.NONE));
        tName = JOE_T_ProfilesForm_Name.Control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        bNotes = JOE_B_ProfilesForm_ProfileNotes.Control(new Button(group, SWT.NONE));
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = "";
                DocumentationForm.openNoteDialog(dom, listener.getProfileElement(), "note", tip, true, !listener.isNewProfile(),
                        JOE_B_ProfilesForm_ProfileNotes.label());
            }
        });
        bApply = JOE_B_ProfilesForm_ApplyProfile.Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData3);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyProfile();
            }
        });
        label4 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setText("Label");
        label4.setLayoutData(gridData1);
        tProfiles = JOE_Tbl_ProfilesForm_Profiles.Control(new Table(group, SWT.BORDER));
        tProfiles.setHeaderVisible(true);
        tProfiles.setLayoutData(gridData11);
        tProfiles.setLinesVisible(true);
        tProfiles.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tProfiles.getSelectionCount() > 0) {
                    listener.selectProfile(tProfiles.getSelectionIndex());
                    setProfileStatus(true);
                    bRemove.setEnabled(true);
                    bApply.setEnabled(false);
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_ProfilesForm_Name.Control(new TableColumn(tProfiles, SWT.NONE));
        tableColumn.setWidth(450);
        bNew = JOE_B_ProfilesForm_NewProfile.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData4);
        bNew.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.setNewProfile();
                setProfileStatus(true);
                bApply.setEnabled(true);
                bRemove.setEnabled(false);
                getShell().setDefaultButton(bApply);
            }
        });
        label5 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label");
        label5.setLayoutData(gridData6);
        bRemove = JOE_B_ProfilesForm_RemoveProfile.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData5);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tProfiles.getSelectionCount() > 0 && listener.removeProfile(tProfiles.getSelectionIndex())) {
                    setProfileStatus(false);
                    bRemove.setEnabled(false);
                    bApply.setEnabled(false);
                    fillProfiles();
                }
            }
        });
    }

    @Override
    public void apply() {
        if (isUnsaved()) {
            applyProfile();
        }
    }

    @Override
    public boolean isUnsaved() {
        listener.checkSettings();
        return bApply.isEnabled();
    }

    private void applyProfile() {
        listener.applyProfile(tName.getText());
        fillProfiles();
        bRemove.setEnabled(tProfiles.getSelectionCount() > 0);
        bApply.setEnabled(false);
    }

    private void setProfileStatus(boolean enabled) {
        tName.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tName.setFocus();
        }
        bApply.setEnabled(false);
    }

    private void fillProfiles() {
        listener.fillProfiles(tProfiles);
        if (treeHandler != null) {
            treeHandler.fillProfiles();
        }
    }

    @Override
    public void openBlank() {
        //
    }

    @Override
    protected void applySetting() {
        apply();
    }

    @Override
    public boolean applyChanges() {
        apply();
        return false;
    }

}