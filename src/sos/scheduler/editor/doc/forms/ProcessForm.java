package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.ProcessListener;

public class ProcessForm extends Composite implements IUnsaved, IUpdateLanguage {

    private ProcessListener listener    = null;

    private Group           group       = null;

    private Label           label       = null;

    private Label           label1      = null;

    private Label           label2      = null;

    private Text            tFile       = null;

    private Text            tParameter  = null;

    private Text            tLog        = null;

    private Button          cUseProcess = null;

    private Group           group1      = null;

    private Label           label3      = null;

    private Text            tName       = null;

    private Label           label4      = null;

    private Text            tValue      = null;

    private Button          bApply      = null;

    private Label           label5      = null;

    private Table           tVariables  = null;

    private Button          bRemove     = null;


    public ProcessForm(Composite parent, int style, DocumentationDom dom, Element job) {
        super(parent, style);
        initialize();
        setToolTipText();
        listener = new ProcessListener(dom, job);
        cUseProcess.setSelection(listener.isProcess());
        initValues();
    }


    private void initialize() {
        cUseProcess = new Button(this, SWT.RADIO);
        cUseProcess.setText("Use process (this will disable the script element and delete its content!)"); // TODO i18n siehe JobScriptForm
        cUseProcess.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (cUseProcess.getSelection() != listener.isProcess()) {
                    if (cUseProcess.getSelection())
                        listener.setProcess();
                    initValues();
                }
            }
        });
        createGroup();
        setSize(new Point(623, 421));
        setLayout(new GridLayout());
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.grabExcessHorizontalSpace = true; // Generated
        gridData11.grabExcessVerticalSpace = true; // Generated
        gridData11.verticalAlignment = GridData.FILL; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.grabExcessHorizontalSpace = true; // Generated
        gridData2.horizontalIndent = 7; // Generated
        gridData2.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.horizontalIndent = 7; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessVerticalSpace = false; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.horizontalIndent = 7; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Process"); // Generated
        group.setLayoutData(gridData11); // Generated
        group.setLayout(gridLayout); // Generated
        label = new Label(group, SWT.NONE);
        label.setText("File:"); // Generated
        tFile = new Text(group, SWT.BORDER);
        tFile.setLayoutData(gridData); // Generated
        tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setFile(tFile.getText());
                if (cUseProcess.getSelection())
                    Utils.setBackground(tFile, true);
            }
        });
        label1 = new Label(group, SWT.NONE);
        label1.setText("Parameter:"); // Generated
        tParameter = new Text(group, SWT.BORDER);
        tParameter.setLayoutData(gridData1); // Generated
        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setParam(tParameter.getText());
            }
        });
        label2 = new Label(group, SWT.NONE);
        label2.setText("Log:"); // Generated
        tLog = new Text(group, SWT.BORDER);
        tLog.setLayoutData(gridData2); // Generated
        tLog.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setLog(tLog.getText());
            }
        });
        createGroup1();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalSpan = 4; // Generated
        gridData7.verticalAlignment = GridData.FILL; // Generated
        gridData7.grabExcessHorizontalSpace = true; // Generated
        gridData7.grabExcessVerticalSpace = true; // Generated
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalSpan = 5; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.grabExcessHorizontalSpace = true; // Generated
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.grabExcessHorizontalSpace = true; // Generated
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 2; // Generated
        gridData3.verticalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.grabExcessVerticalSpace = true; // Generated
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        group1 = new Group(group, SWT.NONE);
        group1.setText("Environment Variables"); // Generated
        group1.setLayout(gridLayout1); // Generated
        group1.setLayoutData(gridData3); // Generated
        label3 = new Label(group1, SWT.NONE);
        label3.setText("Name:"); // Generated
        tName = new Text(group1, SWT.BORDER);
        tName.setLayoutData(gridData4); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(tName.getText().length() > 0);
                getShell().setDefaultButton(bApply);
            }
        });
        label4 = new Label(group1, SWT.NONE);
        label4.setText("Value:"); // Generated
        tValue = new Text(group1, SWT.BORDER);
        tValue.setLayoutData(gridData5); // Generated
        tValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(tName.getText().length() > 0);
                getShell().setDefaultButton(bApply);
            }
        });
        bApply = new Button(group1, SWT.NONE);
        bApply.setText("Apply"); // Generated
        bApply.setLayoutData(gridData9); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyParam();
            }
        });
        label5 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label"); // Generated
        label5.setLayoutData(gridData6); // Generated
        tVariables = new Table(group1, SWT.BORDER);
        tVariables.setHeaderVisible(true); // Generated
        tVariables.setLayoutData(gridData7); // Generated
        tVariables.setLinesVisible(true); // Generated
        tVariables.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    TableItem item = tVariables.getItem(tVariables.getSelectionIndex());
                    tName.setText(item.getText(0));
                    tValue.setText(item.getText(1));
                    bApply.setEnabled(false);
                    tName.setFocus();
                }
                bRemove.setEnabled(tVariables.getSelectionCount() > 0);
            }
        });
        bRemove = new Button(group1, SWT.NONE);
        bRemove.setText("Remove"); // Generated
        bRemove.setLayoutData(gridData8); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    listener.removeVariable(tVariables.getItem(tVariables.getSelectionIndex()).getText(0));
                    listener.fillTable(tVariables);
                    tName.setText("");
                    tValue.setText("");
                    bApply.setEnabled(false);
                }
                bRemove.setEnabled(false);
                boolean valid = (!tName.getText().equals(""));
                if (valid) {
                    getShell().setDefaultButton(bApply);
                }
                bApply.setEnabled(valid);
            }
        });
        TableColumn tableColumn = new TableColumn(tVariables, SWT.NONE);
        tableColumn.setWidth(150); // Generated
        tableColumn.setText("Name"); // Generated
        TableColumn tableColumn1 = new TableColumn(tVariables, SWT.NONE);
        tableColumn1.setWidth(60); // Generated
        tableColumn1.setText("Value"); // Generated
    }


    public void apply() {
        if (isUnsaved())
            applyParam();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    public void setToolTipText() {
        cUseProcess.setToolTipText(Messages.getTooltip("doc.process.useProcess"));
        tFile.setToolTipText(Messages.getTooltip("doc.process.file"));
        tParameter.setToolTipText(Messages.getTooltip("doc.process.params"));
        tLog.setToolTipText(Messages.getTooltip("doc.process.log"));
        tName.setToolTipText(Messages.getTooltip("doc.process.env.name"));
        tValue.setToolTipText(Messages.getTooltip("doc.process.env.value"));
        bApply.setToolTipText(Messages.getTooltip("doc.process.env.apply"));
        tVariables.setToolTipText(Messages.getTooltip("doc.process.env.table"));
        bRemove.setToolTipText(Messages.getTooltip("doc.process.env.remove"));
    }


    private void initValues() {
        boolean enabled = cUseProcess.getSelection();
        tFile.setEnabled(enabled);
        tParameter.setEnabled(enabled);
        tLog.setEnabled(enabled);
        tName.setEnabled(enabled);
        tValue.setEnabled(enabled);
        bApply.setEnabled(enabled);
        bRemove.setEnabled(false);

        if (enabled) {
            tFile.setText(listener.getFile());
            tParameter.setText(listener.getParam());
            tLog.setText(listener.getLog());
            tName.setText("");
            tValue.setText("");
            tFile.setFocus();
            listener.fillTable(tVariables);
        }
    }


    private void applyParam() {
        listener.applyParam(tName.getText(), tValue.getText());
        tName.setText("");
        tValue.setText("");
        listener.fillTable(tVariables);
        tVariables.deselectAll();
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        tName.setFocus();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
