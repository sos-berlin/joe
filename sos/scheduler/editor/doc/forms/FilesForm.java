package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
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
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.FilesListener;

public class FilesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private FilesListener    listener;

    private DocumentationDom dom;

    private Group            group   = null;

    private Label            label6  = null;

    private Text             tFile   = null;

    private Label            label9  = null;

    private Combo            cOS     = null;

    private Label            label10 = null;

    private Combo            cType   = null;

    private Label            label11 = null;

    private Text             tID     = null;

    private Button           bApply  = null;

    private Label            label   = null;

    private Table            table   = null;

    private Button           bNew    = null;

    private Label            label1  = null;

    private Button           bRemove = null;

    private Button           bNotes  = null;


    public FilesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new FilesListener(dom, parentElement);
        initialize();

        listener.fillFiles(table);
        bRemove.setEnabled(false);
    }


    private void initialize() {
        createGroup();
        setSize(new Point(716, 448));
        setLayout(new FillLayout());

        cOS.setItems(listener.getPlatforms());
        cType.setItems(listener.getTypes());
        setFileStatus(false);
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData11 = new GridData();
        gridData11.verticalSpan = 2; // Generated
        gridData11.verticalAlignment = GridData.BEGINNING; // Generated
        gridData11.horizontalAlignment = GridData.CENTER; // Generated
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.FILL; // Generated
        gridData6.grabExcessHorizontalSpace = true; // Generated
        gridData6.grabExcessVerticalSpace = true; // Generated
        gridData6.horizontalSpan = 5; // Generated
        gridData6.verticalSpan = 3; // Generated
        GridData gridData5 = new GridData();
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        gridData5.horizontalSpan = 6; // Generated
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.verticalSpan = 3; // Generated
        gridData4.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData1 = new GridData();
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.horizontalSpan = 3; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData = new GridData();
        gridData.horizontalSpan = 4; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 6; // Generated
        group = new Group(this, SWT.NONE);
        label6 = new Label(group, SWT.NONE);
        label6.setText("File:"); // Generated
        tFile = new Text(group, SWT.BORDER);
        tFile.setLayoutData(gridData); // Generated
        tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tFile, true);
                setApplyStatus();
            }
        });
        bApply = new Button(group, SWT.NONE);
        bApply.setText("Apply File"); // Generated
        bApply.setLayoutData(gridData4); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyFile();
            }
        });
        label9 = new Label(group, SWT.NONE);
        label9.setText("OS:"); // Generated
        createCOS();
        label10 = new Label(group, SWT.NONE);
        label10.setText("Type:"); // Generated
        createCType();
        group.setLayout(gridLayout2); // Generated
        group.setText("Files"); // Generated
        bNotes = new Button(group, SWT.NONE);
        bNotes.setText("Note..."); // Generated
        bNotes.setLayoutData(gridData11); // Generated
        label11 = new Label(group, SWT.NONE);
        label11.setText("ID:"); // Generated
        tID = new Text(group, SWT.BORDER);
        tID.setLayoutData(gridData1); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.files");
                DocumentationForm.openNoteDialog(dom, listener.getFileElement(), "note", tip, true, !listener
                        .isNewFile(),"File Note");
            }
        });
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label"); // Generated
        label.setLayoutData(gridData5); // Generated
        table = new Table(group, SWT.BORDER);
        table.setHeaderVisible(true); // Generated
        table.setLayoutData(gridData6); // Generated
        table.setLinesVisible(true); // Generated
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    listener.selectFile(table.getSelectionIndex());
                    bRemove.setEnabled(true);
                    setFileStatus(true);
                }
            }
        });
        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(300); // Generated
        tableColumn.setText("File"); // Generated
        TableColumn tableColumn3 = new TableColumn(table, SWT.NONE);
        TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
        TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
        tableColumn1.setWidth(150); // Generated
        tableColumn1.setText("ID"); // Generated
        tableColumn3.setWidth(80); // Generated
        tableColumn3.setText("OS"); // Generated
        tableColumn2.setWidth(80); // Generated
        tableColumn2.setText("Type"); // Generated
        bNew = new Button(group, SWT.NONE);
        bNew.setText("New File"); // Generated
        bNew.setLayoutData(gridData7); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewFile();
                setFileStatus(true);
                table.deselectAll();
            }
        });
        label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setText("Label"); // Generated
        label1.setLayoutData(gridData8); // Generated
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove File"); // Generated
        bRemove.setLayoutData(gridData9); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    listener.removeFile(table.getSelectionIndex());
                    listener.fillFiles(table);
                    setFileStatus(false);
                }
                bRemove.setEnabled(false);
            }
        });
    }


    /**
     * This method initializes cOS
     */
    private void createCOS() {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.grabExcessHorizontalSpace = true; // Generated
        gridData2.verticalAlignment = GridData.CENTER; // Generated
        cOS = new Combo(group, SWT.READ_ONLY);
        cOS.setLayoutData(gridData2); // Generated
        cOS.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setApplyStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    /**
     * This method initializes cType
     */
    private void createCType() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        cType = new Combo(group, SWT.READ_ONLY);
        cType.setLayoutData(gridData3); // Generated
        cType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setApplyStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            apply();
    }


    public boolean isUnsaved() {
        return bApply.getEnabled();
    }


    public void setToolTipText() {
        tFile.setToolTipText(Messages.getTooltip("doc.files.file"));
        bApply.setToolTipText(Messages.getTooltip("doc.files.apply"));
        tID.setToolTipText(Messages.getTooltip("doc.files.id"));
        cOS.setToolTipText(Messages.getTooltip("doc.files.os"));
        cType.setToolTipText(Messages.getTooltip("doc.files.type"));
        bNotes.setToolTipText(Messages.getTooltip("doc.files.notes"));
        table.setToolTipText(Messages.getTooltip("doc.files.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.files.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.files.remove"));
    }


    private void setFileStatus(boolean enabled) {
        tFile.setEnabled(enabled);
        tID.setEnabled(enabled);
        cOS.setEnabled(enabled);
        cType.setEnabled(enabled);
        bNotes.setEnabled(enabled);

        if (enabled) {
            tFile.setText(listener.getFile());
            tID.setText(listener.getID());
            cOS.select(cOS.indexOf(listener.getOS()));
            cType.select(cType.indexOf(listener.getType()));
            Utils.setBackground(tFile, true);
            tFile.setFocus();
        }
        bApply.setEnabled(false);
    }


    private void setApplyStatus() {
        bApply.setEnabled(tFile.getText().length() > 0);
        getShell().setDefaultButton(bApply);
    }


    private void applyFile() {
        listener.applyFile(tFile.getText(), tID.getText(), cOS.getText(), cType.getText());
        bApply.setEnabled(false);
        listener.fillFiles(table);
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
