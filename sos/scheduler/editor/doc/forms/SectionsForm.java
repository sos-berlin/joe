package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.IUpdateTree;
import sos.scheduler.editor.doc.listeners.DocumentationListener;
import sos.scheduler.editor.doc.listeners.SectionsListener;

public class SectionsForm extends Composite implements IUnsaved, IUpdateLanguage {
    private SectionsListener listener      = null;

    IUpdateTree              treeHandler   = null;

    TreeItem                 tItem         = null;

    Element                  parentElement = null;

    private Group            group         = null;

    private Label            label5        = null;

    private Label            label6        = null;

    private Label            label7        = null;

    private Table            tSections     = null;

    private Text             tName         = null;

    private Text             tID           = null;

    private Label            label8        = null;

    private Combo            cReference    = null;

    private Button           bApply        = null;

    private Button           bNew          = null;

    private Label            label         = null;

    private Button           bRemove       = null;


    public SectionsForm(Composite parent, int style, DocumentationDom dom, Element parentElement,
            IUpdateTree treeHandler, TreeItem tItem) {
        super(parent, style);
        initialize();
        this.treeHandler = treeHandler;
        this.tItem = tItem;
        this.parentElement = parentElement;
        listener = new SectionsListener(dom, parentElement);

        fillTable();
        setSectionStatus(false);
    }


    private void initialize() {
        createGroup();
        setSize(new Point(717, 476));
        setLayout(new FillLayout());

        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData5 = new GridData();
        gridData5.verticalSpan = 2; // Generated
        gridData5.verticalAlignment = GridData.BEGINNING; // Generated
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalSpan = 5; // Generated
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.grabExcessHorizontalSpace = true; // Generated
        gridData2.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData1 = new GridData();
        gridData1.verticalSpan = 3; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.verticalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.grabExcessVerticalSpace = true; // Generated
        gridData1.horizontalSpan = 4; // Generated
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated
        group = new Group(this, SWT.NONE);
        label5 = new Label(group, SWT.NONE);
        label5.setText("Name:"); // Generated
        tName = new Text(group, SWT.BORDER);
        tName.setLayoutData(gridData); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        bApply = new Button(group, SWT.NONE);
        bApply.setText("Apply Section"); // Generated
        bApply.setLayoutData(gridData5); // Generated
        label8 = new Label(group, SWT.NONE);
        label8.setText("Reference:"); // Generated
        createCReference();
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySection();
            }
        });
        label6 = new Label(group, SWT.NONE);
        label6.setText("ID:"); // Generated
        tID = new Text(group, SWT.BORDER);
        tID.setLayoutData(gridData2); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        label7 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label7.setText("Label"); // Generated
        label7.setLayoutData(gridData4); // Generated
        tSections = new Table(group, SWT.BORDER);
        tSections.setHeaderVisible(true); // Generated
        tSections.setLayoutData(gridData1); // Generated
        tSections.setLinesVisible(true); // Generated
        tSections.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSections.getSelectionCount() > 0) {
                    if (listener.selectSection(tSections.getSelectionIndex())) {
                        setSectionStatus(true);
                        bRemove.setEnabled(true);
                    }
                }
            }
        });
        bNew = new Button(group, SWT.NONE);
        bNew.setText("New Section"); // Generated
        bNew.setLayoutData(gridData6); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewSection();
                setSectionStatus(true);
                bRemove.setEnabled(false);
                tSections.deselectAll();
            }
        });
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label"); // Generated
        label.setLayoutData(gridData7); // Generated
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove Section"); // Generated
        bRemove.setLayoutData(gridData8); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSections.getSelectionCount() > 0) {
                    listener.removeSection(tSections.getSelectionIndex());
                    setSectionStatus(false);
                    tSections.deselectAll();
                    fillTable();
                }
            }
        });
        TableColumn tableColumn3 = new TableColumn(tSections, SWT.NONE);
        tableColumn3.setWidth(200); // Generated
        tableColumn3.setText("Name"); // Generated
        TableColumn tableColumn5 = new TableColumn(tSections, SWT.NONE);
        TableColumn tableColumn4 = new TableColumn(tSections, SWT.NONE);
        tableColumn4.setWidth(180); // Generated
        tableColumn4.setText("ID"); // Generated
        tableColumn5.setWidth(180); // Generated
        tableColumn5.setText("Reference"); // Generated
        group.setLayout(gridLayout1); // Generated
        group.setText("Sections"); // Generated
    }


    /**
     * This method initializes cReference
     */
    private void createCReference() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        cReference = new Combo(group, SWT.NONE);
        cReference.setLayoutData(gridData3); // Generated
        cReference.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setApplyStatus();
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            applySection();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    public void setToolTipText() {
        tName.setToolTipText(Messages.getTooltip("doc.sections.name"));
        bApply.setToolTipText(Messages.getTooltip("doc.sections.apply"));
        tID.setToolTipText(Messages.getTooltip("doc.sections.id"));
        cReference.setToolTipText(Messages.getTooltip("doc.sections.reference"));
        tSections.setToolTipText(Messages.getTooltip("doc.sections.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.sections.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.sections.remove"));
    }


    private void setSectionStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tID.setEnabled(enabled);
        cReference.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tID.setText(listener.getID());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener
                    .getReference());
            tName.setFocus();
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(false);
    }


    private void setApplyStatus() {
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
        if (treeHandler != null)
            treeHandler.fillSections(tItem, parentElement, true);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
