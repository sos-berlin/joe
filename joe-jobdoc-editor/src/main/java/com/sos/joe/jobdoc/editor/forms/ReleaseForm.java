package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_DBResources_Notes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ReleaseForm_Releases;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ReleaseForm_Created;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ReleaseForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ReleaseForm_Modified;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ReleaseForm_Title;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_ReleaseForm_Changes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_ReleaseForm_Created;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_ReleaseForm_Modified;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ReleaseForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ReleaseForm_Title;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.dialog.components.SOSDateTime;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.jobdoc.editor.listeners.ReleaseListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ReleaseForm extends JobDocBaseForm<ReleaseListener> implements IUnsaved {

    private static final Logger LOGGER = Logger.getLogger(ReleaseForm.class);
    private Group group = null;
    private Label label = null;
    private Label label1 = null;
    private Text tTitle = null;
    private Text tID = null;
    @SuppressWarnings("unused")
    private Label label2 = null;
    private Label label3 = null;
    private SOSDateTime created = null;
    private SOSDateTime modified = null;
    private NoteForm fNote = null;
    private NoteForm fChanges = null;

    public ReleaseForm(Composite parent, int style, DocumentationDom dom, Element release) {
        super(parent, style);
        this.dom = dom;
        listener = new ReleaseListener(dom, release);
        initialize();
    }

    private void initialize() {
        createGroup();
        try {
            setReleaseStatus(false);
            created.setDate(listener.getCreated());
            modified.setDate(listener.getModified());
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        fNote.setParams(dom, listener.getRelease(), "note", true, true);
        fNote.setTitle(JOE_B_DBResources_Notes.label());
        fChanges.setParams(dom, listener.getRelease(), "changes", true, true);
        fChanges.setTitle(JOE_M_ReleaseForm_Changes.label());
        tID.setText(listener.getID());
        tTitle.setText(listener.getTitle());
    }

    private void createGroup() {
        GridLayout gridLayout = new GridLayout(4, false);
        group = JOE_G_ReleaseForm_Releases.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout);
        label1 = JOE_L_ReleaseForm_ID.control(new SOSLabel(group, SWT.NONE));
        label1.setLayoutData(new GridData());
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tID = JOE_T_ReleaseForm_ID.control(new Text(group, SWT.BORDER));
        tID.setLayoutData(gridData3);
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tID, true);
                listener.setId(tID.getText());
            }
        });
        label2 = JOE_L_ReleaseForm_Created.control(new SOSLabel(group, SWT.NONE));
        createCreated();
        label = JOE_L_ReleaseForm_Title.control(new SOSLabel(group, SWT.NONE));
        label.setLayoutData(new GridData());
        GridData gridData21 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tTitle = JOE_T_ReleaseForm_Title.control(new Text(group, SWT.BORDER));
        tTitle.setLayoutData(gridData21);
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tTitle, false);
                listener.setTitle(tTitle.getText());
            }
        });
        label3 = JOE_L_ReleaseForm_Modified.control(new SOSLabel(group, SWT.NONE));
        label3.setLayoutData(new GridData());
        createModified();
        createComposite();
        createGroup1();
    }

    private void createGroup1() {
        GridData gridData5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData5.widthHint = 486;
        final Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
        final GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);
        fNote = new NoteForm(composite, SWT.NONE);
        fNote.setTitle(JOE_B_DBResources_Notes.label());
        fNote.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        fChanges = new NoteForm(composite, SWT.NONE);
        fChanges.setTitle(JOE_M_ReleaseForm_Changes.label());
        fChanges.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    }

    private void createComposite() {
    }

    private void createCreated() {
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        created = JOE_ReleaseForm_Created.control(new SOSDateTime(group, SWT.NONE));
        created.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.setCreated(created.getISODate());
            }
        });
        created.setLayoutData(gridData6);
    }

    private void createModified() {
        GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        modified = JOE_ReleaseForm_Modified.control(new SOSDateTime(group, SWT.NONE));
        modified.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.setModified(modified.getISODate());
            }
        });
        modified.setLayoutData(gridData13);
    }

    @Override
    public void apply() {
        applyRelease();
    }

    @Override
    public boolean isUnsaved() {
        return false;
    }

    private void setReleaseStatus(boolean enabled) throws ParseException {
        if (enabled) {
            tTitle.setText(listener.getTitle());
            if ("".equals(listener.getCreated())) {
                created.setDate(new Date());
            } else {
                created.setDate(listener.getCreated());
            }
            if ("".equals(listener.getModified())) {
                modified.setDate(new Date());
            } else {
                modified.setDate(listener.getModified());
            }
            tID.setText(listener.getID());
            tID.setFocus();
        }
    }

    private void applyRelease() {
        listener.applyRelease(tTitle.getText(), tID.getText(), created.getISODate(), modified.getISODate());
    }

    @Override
    public void openBlank() {

    }

    @Override
    protected void applySetting() {

    }

    @Override
    public boolean applyChanges() {
        return false;
    }

}