package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.DatabasesListener;

public class DatabasesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private DatabasesListener listener   = null;

    private DocumentationDom  dom        = null;

    private Group             group      = null;

    private Group             group1     = null;

    private Label             label1     = null;

    private Text              tName      = null;

    private Label             label2     = null;

    private Combo             cType      = null;

    private SashForm          sashForm   = null;

    private Composite         composite  = null;

    private Composite         composite1 = null;

    private Button            cRequired  = null;

    private Button            bApply     = null;

    private Table             tDatabases = null;

    private Button            bRemove    = null;

    private Label             label      = null;

    private Button            bNew       = null;

    private Label             label3     = null;

    private Button            bNotes     = null;

    private Table             tResources = null;

    private Button            bApplyRes  = null;

    private Button            bNewRes    = null;

    private Button            bRemoveRes = null;

    private Label             label4     = null;

    private Label             label5     = null;

    private Label             label6     = null;

    private Text              tDB        = null;


    public DatabasesForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }


    public DatabasesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new DatabasesListener(dom, parentElement);
        initialize();
        listener.fillDatabases(tDatabases);
    }


    private void initialize() {
        createGroup();

        setSize(new Point(636, 477));
        setLayout(new FillLayout());

        sashForm.setWeights(new int[] { 65, 35 });
        Options.loadSash("databases", sashForm);

        cType.setItems(listener.getTypes());
        bRemove.setEnabled(false);
        setDatabaseStatus(false);

        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        group = new Group(this, SWT.NONE);
        group.setText("Databases"); // Generated
        group.setLayout(new FillLayout()); // Generated
        createSashForm();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData14 = new GridData();
        gridData14.horizontalAlignment = GridData.FILL; // Generated
        gridData14.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData13 = new GridData();
        gridData13.horizontalAlignment = GridData.FILL; // Generated
        gridData13.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData12 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData12.widthHint = 90;
        GridData gridData10 = new GridData();
        gridData10.horizontalSpan = 5; // Generated
        gridData10.horizontalAlignment = GridData.FILL; // Generated
        gridData10.verticalAlignment = GridData.FILL; // Generated
        gridData10.grabExcessHorizontalSpace = true; // Generated
        gridData10.grabExcessVerticalSpace = true; // Generated
        gridData10.verticalSpan = 3; // Generated
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalSpan = 6; // Generated
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 6; // Generated
        group1 = new Group(composite, SWT.NONE);        
        group1.setLayout(gridLayout1); // Generated
        group1.setLayoutData(gridData11); // Generated
        group1.setText("Resources"); // Generated
        label1 = new Label(group1, SWT.NONE);
        label1.setText("Name:"); // Generated
        tName = new Text(group1, SWT.BORDER);
        tName.setLayoutData(gridData3); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tName, true);
                setApplyResStatus();
            }
        });
        label2 = new Label(group1, SWT.NONE);
        label2.setText("Type:"); // Generated
        createCType();
        bNotes = new Button(group1, SWT.NONE);
        bNotes.setText("Note..."); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.databases");
                DocumentationForm.openNoteDialog(dom, listener.getResource(), "note", tip, true, !listener
                        .isNewDatabase(),"Resource Note");
            }
        });
        bApplyRes = new Button(group1, SWT.NONE);
        bApplyRes.setText("Apply"); // Generated
        bApplyRes.setLayoutData(gridData12); // Generated
        bApplyRes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyResource();
            }
        });
        label5 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label"); // Generated
        label5.setLayoutData(gridData5); // Generated
        tResources = new Table(group1, SWT.BORDER);
        tResources.setHeaderVisible(true); // Generated
        tResources.setLayoutData(gridData10); // Generated
        tResources.setLinesVisible(true); // Generated
        tResources.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tResources.getSelectionCount() > 0) {
                    listener.setResource(tResources.getSelectionIndex());
                    setResourceStatus(true);
                    bRemoveRes.setEnabled(true);
                }
            }
        });
        TableColumn tableColumn = new TableColumn(tResources, SWT.NONE);
        tableColumn.setWidth(250); // Generated
        tableColumn.setText("Name"); // Generated
        TableColumn tableColumn1 = new TableColumn(tResources, SWT.NONE);
        tableColumn1.setWidth(60); // Generated
        tableColumn1.setText("Type"); // Generated
        bNewRes = new Button(group1, SWT.NONE);
        bNewRes.setText("New"); // Generated
        bNewRes.setLayoutData(gridData13); // Generated
        bNewRes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewResource();
                setResourceStatus(true);
                tResources.deselectAll();
            }
        });
        label4 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setText("Label"); // Generated
        label4.setLayoutData(gridData9); // Generated
        bRemoveRes = new Button(group1, SWT.NONE);
        bRemoveRes.setText("Remove"); // Generated
        bRemoveRes.setLayoutData(gridData14); // Generated
        bRemoveRes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tResources.getSelectionCount() > 0) {
                    listener.removeResource(tResources.getSelectionIndex());
                    setResourceStatus(false);
                    listener.fillResources(tResources);
                }
                bRemoveRes.setEnabled(false);
            }
        });
    }


    /**
     * This method initializes cType
     */
    private void createCType() {
        GridData gridData4 = new GridData();
        gridData4.widthHint = 100; // Generated
        cType = new Combo(group1, SWT.READ_ONLY);
        cType.setLayoutData(gridData4); // Generated
        cType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setApplyResStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        sashForm = new SashForm(group, SWT.NONE);
        sashForm.setOrientation(SWT.VERTICAL); // Generated
        createComposite();
        createComposite1();
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        GridData gridData71 = new GridData();
        gridData71.horizontalAlignment = GridData.FILL; // Generated
        gridData71.grabExcessHorizontalSpace = true; // Generated
        gridData71.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData6 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData6.widthHint = 90; // Generated
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.CENTER; // Generated
        gridData.horizontalSpan = 4; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 4; // Generated
        composite = new Composite(sashForm, SWT.NONE);
        label6 = new Label(composite, SWT.NONE);
        label6.setText("Name:"); // Generated
        tDB = new Text(composite, SWT.BORDER);
        tDB.setLayoutData(gridData71); // Generated
        tDB.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        cRequired = new Button(composite, SWT.CHECK);
        cRequired.setText("Is Required"); // Generated
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        bApply = new Button(composite, SWT.NONE);
        bApply.setText("Apply DB"); // Generated
        bApply.setLayoutData(gridData6); // Generated
        createGroup1();
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyDatabase();
            }
        });
        composite.setLayout(gridLayout2); // Generated
        label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label"); // Generated
        label.setLayoutData(gridData); // Generated
    }


    /**
     * This method initializes composite1
     */
    private void createComposite1() {
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.widthHint = 90; // Generated
        gridData8.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.widthHint = 90; // Generated
        gridData2.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.grabExcessVerticalSpace = true; // Generated
        gridData1.verticalSpan = 3; // Generated
        gridData1.verticalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 2; // Generated
        composite1 = new Composite(sashForm, SWT.NONE);
        composite1.setLayout(gridLayout3); // Generated
        tDatabases = new Table(composite1, SWT.BORDER);
        TableColumn tableColumn2 = new TableColumn(tDatabases, SWT.NONE);
        tableColumn2.setWidth(300); // Generated
        tableColumn2.setText("Name"); // Generated
        TableColumn tableColumn3 = new TableColumn(tDatabases, SWT.NONE);
        tableColumn3.setWidth(60); // Generated
        tableColumn3.setText("Required"); // Generated
        bNew = new Button(composite1, SWT.NONE);
        bNew.setText("New DB"); // Generated
        bNew.setLayoutData(gridData8); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewDatabase();
                setDatabaseStatus(true);
                tDatabases.deselectAll();
                bApply.setEnabled(true);
            }
        });
        label3 = new Label(composite1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label3.setText("Label"); // Generated
        label3.setLayoutData(gridData7); // Generated
        tDatabases.setLayoutData(gridData1); // Generated
        tDatabases.setHeaderVisible(true); // Generated
        tDatabases.setLinesVisible(true); // Generated
        tDatabases.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tDatabases.getSelectionCount() > 0) {
                    listener.setDatabase(tDatabases.getSelectionIndex());
                    setDatabaseStatus(true);
                    bRemove.setEnabled(true);
                }
            }
        });
        bRemove = new Button(composite1, SWT.NONE);
        bRemove.setText("Remove DB"); // Generated
        bRemove.setLayoutData(gridData2); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tDatabases.getSelectionCount() > 0) {
                    listener.removeDatabase(tDatabases.getSelectionIndex());
                    setDatabaseStatus(false);
                    listener.fillDatabases(tDatabases);
                }
                bRemove.setEnabled(false);
            }
        });
    }


    public void apply() {
        if (bApplyRes.isEnabled())
            applyResource();
        if (bApply.isEnabled())
            applyDatabase();
    }


    public boolean isUnsaved() {
        Options.saveSash("databases", sashForm.getWeights());

        return bApplyRes.isEnabled() || bApply.isEnabled();
    }


    public void setToolTipText() {
        tDB.setToolTipText(Messages.getTooltip("doc.databases.name"));
        cRequired.setToolTipText(Messages.getTooltip("doc.databases.required"));
        bApply.setToolTipText(Messages.getTooltip("doc.databases.apply"));
        tDatabases.setToolTipText(Messages.getTooltip("doc.databases.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.databases.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.databases.remove"));
        tName.setToolTipText(Messages.getTooltip("doc.databases.resources.name"));
        cType.setToolTipText(Messages.getTooltip("doc.databases.resources.type"));
        bNotes.setToolTipText(Messages.getTooltip("doc.databases.resources.notes"));
        bApplyRes.setToolTipText(Messages.getTooltip("doc.databases.resources.apply"));
        tResources.setToolTipText(Messages.getTooltip("doc.databases.resources.table"));
        bNewRes.setToolTipText(Messages.getTooltip("doc.databases.resources.new"));
        bRemoveRes.setToolTipText(Messages.getTooltip("doc.databases.resources.remove"));
    }


    private void setDatabaseStatus(boolean enabled) {
        tDB.setEnabled(enabled);
        cRequired.setEnabled(enabled);
        tResources.setEnabled(enabled);
        bNewRes.setEnabled(enabled);
        setResourceStatus(false);

        if (enabled) {
            tDB.setText(listener.getDBName());
            cRequired.setSelection(listener.isRequired());
            listener.fillResources(tResources);
        }
        bApply.setEnabled(false);
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
        bApplyRes.setEnabled(false);
    }


    private void setApplyResStatus() {
        bApplyRes.setEnabled(tName.getText().length() > 0);
        getShell().setDefaultButton(bApplyRes);
    }


    private void applyResource() {
        listener.applyResource(tName.getText(), cType.getText());
        listener.fillResources(tResources);
        bRemoveRes.setEnabled(tResources.getSelectionCount() > 0);
        bApplyRes.setEnabled(false);
        getShell().setDefaultButton(bApply);
    }


    private void applyDatabase() {
        listener.applyDatabase(tDB.getText(), cRequired.getSelection());
        listener.fillDatabases(tDatabases);
        bRemove.setEnabled(tDatabases.getSelectionCount() > 0);
        bApply.setEnabled(false);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
