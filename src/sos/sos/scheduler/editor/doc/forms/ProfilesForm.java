package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.IUpdateTree;
import sos.scheduler.editor.doc.listeners.ProfilesListener;

public class ProfilesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ProfilesListener listener    = null;

    private DocumentationDom dom         = null;

    IUpdateTree              treeHandler = null;

    private Group            group       = null;

    private Label            label3      = null;

    private Text             tName       = null;

    private Button           bNotes      = null;

    private Button           bApply      = null;

    private Label            label4      = null;

    private Button           bNew        = null;

    private Label            label5      = null;

    private Button           bRemove     = null;

    private Table            tProfiles   = null;


    public ProfilesForm(Composite parent, int style, DocumentationDom dom, Element parentElement,
            IUpdateTree treeHandler) {
        super(parent, style);
        this.dom = dom;
        this.treeHandler = treeHandler;
        listener = new ProfilesListener(dom, parentElement);
        initialize();
    }


    private void initialize() {
        createGroup();
        setSize(new Point(561, 401));
        setLayout(new FillLayout());

        setProfileStatus(false);
        bRemove.setEnabled(false);
        fillProfiles();
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData11 = new GridData();
        gridData11.horizontalSpan = 3; // Generated
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.verticalAlignment = GridData.FILL; // Generated
        gridData11.grabExcessHorizontalSpace = true; // Generated
        gridData11.grabExcessVerticalSpace = true; // Generated
        gridData11.verticalSpan = 3; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 4; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 4; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Profiles"); // Generated
        group.setLayout(gridLayout2); // Generated
        label3 = new Label(group, SWT.NONE);
        label3.setText("Name:"); // Generated
        tName = new Text(group, SWT.BORDER);
        tName.setLayoutData(gridData); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        bNotes = new Button(group, SWT.NONE);
        bNotes.setText("Profile Note..."); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.profile");
                DocumentationForm.openNoteDialog(dom, listener.getProfileElement(), "note", tip, true, !listener
                        .isNewProfile(),"Profile Note");
            }
        });
        bApply = new Button(group, SWT.NONE);
        bApply.setText("Apply Profile"); // Generated
        bApply.setLayoutData(gridData3); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyProfile();
            }
        });
        label4 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setText("Label"); // Generated
        label4.setLayoutData(gridData1); // Generated
        tProfiles = new Table(group, SWT.BORDER);
        tProfiles.setHeaderVisible(true); // Generated
        tProfiles.setLayoutData(gridData11); // Generated
        tProfiles.setLinesVisible(true); // Generated
        tProfiles.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tProfiles.getSelectionCount() > 0) {
                    listener.selectProfile(tProfiles.getSelectionIndex());
                    setProfileStatus(true);
                    bRemove.setEnabled(true);
                    bApply.setEnabled(false);
                }
            }
        });
        TableColumn tableColumn = new TableColumn(tProfiles, SWT.NONE);
        tableColumn.setWidth(450); // Generated
        tableColumn.setText("Name"); // Generated
        bNew = new Button(group, SWT.NONE);
        bNew.setText("New Profile"); // Generated
        bNew.setLayoutData(gridData4); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewProfile();
                setProfileStatus(true);
                bApply.setEnabled(true);
                bRemove.setEnabled(false);
                getShell().setDefaultButton(bApply);
            }
        });
        label5 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label"); // Generated
        label5.setLayoutData(gridData6); // Generated
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove Profile"); // Generated
        bRemove.setLayoutData(gridData5); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tProfiles.getSelectionCount() > 0) {
                    if (listener.removeProfile(tProfiles.getSelectionIndex())) {
                        setProfileStatus(false);
                        bRemove.setEnabled(false);
                        bApply.setEnabled(false);
                        fillProfiles();
                    }

                }
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            applyProfile();
    }


    public boolean isUnsaved() {
        listener.checkSettings();
        return bApply.isEnabled();
    }


    public void setToolTipText() {
        tName.setToolTipText(Messages.getTooltip("doc.profiles.name"));
        bNotes.setToolTipText(Messages.getTooltip("doc.profiles.notes"));
        bApply.setToolTipText(Messages.getTooltip("doc.profiles.apply"));
        tProfiles.setToolTipText(Messages.getTooltip("doc.profiles.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.profiles.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.profiles.remove"));
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
        if (treeHandler != null)
            treeHandler.fillProfiles();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
