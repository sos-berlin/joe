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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.DatePicker;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.ReleasesListener;

public class ReleasesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ReleasesListener listener     = null;

    private DocumentationDom dom          = null;

    private Group            group        = null;

    private Table            tReleases    = null;

    private Button           bNew         = null;

    private Button           bRemove      = null;

    private Label            label        = null;

    private Label            label1       = null;

    private Text             tTitle       = null;

    private Text             tID          = null;

    private Label            label2       = null;

    private Label            label3       = null;

    private Button           bApply       = null;

    private Group            group1       = null;

    private Label            label4       = null;

    private Text             tName        = null;

    private Label            label5       = null;

    private Text             tEmail       = null;

    private Button           bApplyAuthor = null;

    private Label            label6       = null;

    private Table            tAuthors     = null;

    private Button           bRemoveAutho = null;

    private Label            label7       = null;

    private Composite        composite    = null;

    private Button           bNotes       = null;

    private Button           bChanges     = null;

    private DatePicker       created      = null;

    private DatePicker       modified     = null;


    public ReleasesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        initialize();
        setToolTipText();

        this.dom = dom;
        listener = new ReleasesListener(dom, parentElement);
        listener.fillReleases(tReleases);
    }


    private void initialize() {
        createGroup();
        setSize(new Point(801, 462));
        setLayout(new FillLayout());

        setReleaseStatus(false);
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData51 = new GridData();
        gridData51.horizontalSpan = 5; // Generated
        gridData51.verticalAlignment = GridData.CENTER; // Generated
        gridData51.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.verticalSpan = 4; // Generated
        gridData4.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData = new GridData();
        gridData.verticalSpan = 2; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.grabExcessVerticalSpace = true; // Generated
        gridData.horizontalSpan = 4; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Releases"); // Generated
        group.setLayout(gridLayout); // Generated
        label1 = new Label(group, SWT.NONE);
        label1.setLayoutData(new GridData());
        label1.setText("ID:"); // Generated
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tID = new Text(group, SWT.BORDER);
        tID.setLayoutData(gridData3); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tID, true);
                setApplyStatus();
            }
        });
        label2 = new Label(group, SWT.NONE);
        label2.setText("Created:"); // Generated
        createCreated();
        bApply = new Button(group, SWT.NONE);
        bApply.setText("Apply Release"); // Generated
        bApply.setLayoutData(gridData4); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyRelease();
            }
        });
        label = new Label(group, SWT.NONE);
        label.setLayoutData(new GridData());
        label.setText("Title:"); // Generated
        GridData gridData21 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tTitle = new Text(group, SWT.BORDER);
        tTitle.setLayoutData(gridData21); // Generated
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tTitle, false);
                setApplyStatus();
            }
        });
        label3 = new Label(group, SWT.NONE);
        label3.setText("Modified:"); // Generated
        createModified();
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        Label filler = new Label(group, SWT.NONE);
        createComposite();
        createGroup1();
        label7 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label7.setText("Label"); // Generated
        label7.setLayoutData(gridData51); // Generated
        tReleases = new Table(group, SWT.BORDER);
        tReleases.setHeaderVisible(true); // Generated
        tReleases.setLayoutData(gridData); // Generated
        tReleases.setLinesVisible(true); // Generated
        tReleases.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                    if (listener.selectRelease(tReleases.getSelectionIndex())) {
                        setReleaseStatus(true);
                        bRemove.setEnabled(true);
                    } else
                        tReleases.deselectAll();
                }
            }
        });
        TableColumn tableColumn = new TableColumn(tReleases, SWT.NONE);
        tableColumn.setWidth(250); // Generated
        tableColumn.setText("Title"); // Generated
        TableColumn tableColumn5 = new TableColumn(tReleases, SWT.NONE);
        tableColumn5.setWidth(90); // Generated
        tableColumn5.setText("Created"); // Generated
        TableColumn tableColumn6 = new TableColumn(tReleases, SWT.NONE);
        tableColumn6.setWidth(90); // Generated
        tableColumn6.setText("Modified"); // Generated
        TableColumn tableColumn1 = new TableColumn(tReleases, SWT.NONE);
        tableColumn1.setWidth(60); // Generated
        tableColumn1.setText("ID"); // Generated
        bNew = new Button(group, SWT.NONE);
        bNew.setText("New Release"); // Generated
        bNew.setLayoutData(gridData1); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newRelease();
                setReleaseStatus(true);
                tReleases.deselectAll();
                tID.setFocus();
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove Release"); // Generated
        bRemove.setLayoutData(gridData2); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                    setReleaseStatus(false);
                    listener.removeRelease();
                    bRemove.setEnabled(false);
                    if(tReleases.getSelectionCount() > 0)
                    	tReleases.remove(tReleases.getSelectionCount());
                    tReleases.deselectAll();
                    Utils.setBackground(tReleases, true);
                }
            }
        });
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData5 = new GridData();
        gridData5.horizontalSpan = 4; // Generated
        gridData5.verticalAlignment = GridData.FILL; // Generated
        gridData5.grabExcessHorizontalSpace = true; // Generated
        gridData5.grabExcessVerticalSpace = true; // Generated
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData12 = new GridData();
        gridData12.horizontalAlignment = GridData.FILL; // Generated
        gridData12.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.horizontalSpan = 4; // Generated
        gridData11.grabExcessHorizontalSpace = true; // Generated
        gridData11.grabExcessVerticalSpace = true; // Generated
        gridData11.verticalAlignment = GridData.FILL; // Generated
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = GridData.FILL; // Generated
        gridData10.horizontalSpan = 5; // Generated
        gridData10.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.grabExcessHorizontalSpace = true; // Generated
        gridData8.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        gridData7.grabExcessHorizontalSpace = true; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated
        group1 = new Group(group, SWT.NONE);
        group1.setText("Authors"); // Generated
        group1.setLayoutData(gridData5); // Generated
        group1.setLayout(gridLayout1); // Generated
        label4 = new Label(group1, SWT.NONE);
        label4.setText("Name:"); // Generated
        tName = new Text(group1, SWT.BORDER);
        tName.setLayoutData(gridData7); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (tName.getText().length() > 0 && tEmail.getText().length() > 0) {
                    bApplyAuthor.setEnabled(true);
                    getShell().setDefaultButton(bApplyAuthor);
                }
                Utils.setBackground(tName, bApplyAuthor.isEnabled());
            }
        });
        label5 = new Label(group1, SWT.NONE);
        label5.setText("eMail:"); // Generated
        tEmail = new Text(group1, SWT.BORDER);
        tEmail.setLayoutData(gridData8); // Generated
        tEmail.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (tName.getText().length() > 0 && tEmail.getText().length() > 0) {
                    bApplyAuthor.setEnabled(true);
                    getShell().setDefaultButton(bApplyAuthor);
                }
                Utils.setBackground(tEmail, bApplyAuthor.isEnabled());
            }
        });
        bApplyAuthor = new Button(group1, SWT.NONE);
        bApplyAuthor.setText("Apply"); // Generated
        bApplyAuthor.setLayoutData(gridData9); // Generated
        bApplyAuthor.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyAuthor(tName.getText(), tEmail.getText());
                bApplyAuthor.setEnabled(false);
                tName.setText("");
                tEmail.setText("");
                tAuthors.deselectAll();
                tName.setFocus();
                setApplyStatus();
            }
        });
        label6 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label6.setText("Label"); // Generated
        label6.setLayoutData(gridData10); // Generated
        tAuthors = new Table(group1, SWT.BORDER);
        tAuthors.setHeaderVisible(true); // Generated
        tAuthors.setLinesVisible(true); // Generated
        tAuthors.setLayoutData(gridData11); // Generated
        tAuthors.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tAuthors.getSelectionCount() > 0) {
                    TableItem author = tAuthors.getItem(tAuthors.getSelectionIndex());
                    tName.setText(author.getText(0));
                    tEmail.setText(author.getText(1));
                    bRemoveAutho.setEnabled(true);
                    tName.setFocus();
                }
                bApplyAuthor.setEnabled(false);
            }
        });
        bRemoveAutho = new Button(group1, SWT.NONE);
        bRemoveAutho.setText("Remove"); // Generated
        bRemoveAutho.setLayoutData(gridData12); // Generated
        bRemoveAutho.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tAuthors.getSelectionCount() > 0) {
                    tAuthors.remove(tAuthors.getSelectionIndex());
                    tName.setText("");
                    tEmail.setText("");
                    bApplyAuthor.setEnabled(false);
                    bRemoveAutho.setEnabled(false);
                    tAuthors.deselectAll();
                    Utils.setBackground(tAuthors, true);
                }
            }
        });
        TableColumn tableColumn2 = new TableColumn(tAuthors, SWT.NONE);
        tableColumn2.setText("Name"); // Generated
        tableColumn2.setWidth(250); // Generated
        TableColumn tableColumn11 = new TableColumn(tAuthors, SWT.NONE);
        tableColumn11.setText("eMail"); // Generated
        tableColumn11.setWidth(60); // Generated
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 4; // Generated
        GridData gridData14 = new GridData();
        gridData14.horizontalAlignment = GridData.FILL; // Generated
        gridData14.verticalAlignment = GridData.FILL; // Generated
        composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(gridData14); // Generated
        composite.setLayout(fillLayout); // Generated
        bNotes = new Button(composite, SWT.NONE);
        bNotes.setText("Note..."); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.releases");
                DocumentationForm.openNoteDialog(dom, listener.getRelease(), "note", tip, true, !listener
                        .isNewRelease(),"Release Note");
            }
        });
        bChanges = new Button(composite, SWT.NONE);
        bChanges.setText("Changes..."); // Generated
        bChanges.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.changes");
                DocumentationForm.openNoteDialog(dom, listener.getRelease(), "changes", tip, true, !listener
                        .isNewRelease(),"Changes");
            }
        });
    }


    /**
     * This method initializes created
     */
    private void createCreated() {
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.grabExcessHorizontalSpace = false; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        created = new DatePicker(group, SWT.NONE);
        created.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        created.setLayoutData(gridData6); // Generated
    }


    /**
     * This method initializes modified
     */
    private void createModified() {
        GridData gridData13 = new GridData();
        gridData13.horizontalAlignment = GridData.FILL; // Generated
        gridData13.verticalAlignment = GridData.CENTER; // Generated
        modified = new DatePicker(group, SWT.NONE);
        modified.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        modified.setLayoutData(gridData13); // Generated
    }


    public void apply() {
        applyRelease();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    public void setToolTipText() {
        tTitle.setToolTipText(Messages.getTooltip("doc.releases.title"));
        created.setToolTipText(Messages.getTooltip("doc.releases.created"));
        bApply.setToolTipText(Messages.getTooltip("doc.releases.apply"));
        tID.setToolTipText(Messages.getTooltip("doc.releases.id"));
        modified.setToolTipText(Messages.getTooltip("doc.releases.modified"));
        bNotes.setToolTipText(Messages.getTooltip("doc.releases.notes"));
        bChanges.setToolTipText(Messages.getTooltip("doc.releases.changes"));
        tName.setToolTipText(Messages.getTooltip("doc.releases.author.name"));
        tEmail.setToolTipText(Messages.getTooltip("doc.releases.author.email"));
        bApplyAuthor.setToolTipText(Messages.getTooltip("doc.releases.author.apply"));
        tAuthors.setToolTipText(Messages.getTooltip("doc.releases.author.list"));
        bRemoveAutho.setToolTipText(Messages.getTooltip("doc.releases.author.remove"));
        tReleases.setToolTipText(Messages.getTooltip("doc.releases.list"));
        bNew.setToolTipText(Messages.getTooltip("doc.releases.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.releases.remove"));

    }


    private void setReleaseStatus(boolean enabled) {
        tTitle.setEnabled(enabled);
        created.setEnabled(enabled);
        bApply.setEnabled(false);
        tID.setEnabled(enabled);
        modified.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        bChanges.setEnabled(enabled);
        tName.setEnabled(enabled);
        tEmail.setEnabled(enabled);
        bApplyAuthor.setEnabled(false);
        tAuthors.setEnabled(enabled);
        bRemoveAutho.setEnabled(false);

        if (enabled) {
            tTitle.setText(listener.getTitle());

            if (listener.getCreated().equals(""))
                created.setNow();
            else
                created.setISODate(listener.getCreated());
            if (listener.getModified().equals(""))
                modified.setNow();
            else
                modified.setISODate(listener.getModified());

            tID.setText(listener.getID());
            listener.fillAuthors(tAuthors);
            tID.setFocus();
        }
    }


    private void applyAuthor(String name, String email) {
        name = name.trim();
        email = email.trim();
        for (int i = 0; i < tAuthors.getItemCount(); i++) {
            TableItem author = tAuthors.getItem(i);
            if (author.getText(0).equalsIgnoreCase(name)) {
                author.setText(0, name);
                author.setText(1, email);
                Utils.setBackground(tAuthors, true);
                return;
            }
        }

        // else new item
        TableItem author = new TableItem(tAuthors, SWT.NONE);
        author.setText(0, name);
        author.setText(1, email);
        Utils.setBackground(tAuthors, true);
    }


    private void setApplyStatus() {
        boolean enabled =   tID.getText().length() > 0 && tAuthors.getItemCount() > 0;
        bApply.setEnabled(enabled);
    }


    private void applyRelease() {
        listener.applyRelease(tTitle.getText(), tID.getText(), created.getISODate(), modified.getISODate(), tAuthors
                .getItems());
        bApply.setEnabled(false);
        listener.fillReleases(tReleases);
        bRemove.setEnabled(tReleases.getSelectionCount() > 0);
        Utils.setBackground(tReleases, true);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
