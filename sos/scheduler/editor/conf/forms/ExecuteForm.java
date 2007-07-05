package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;

public class ExecuteForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ExecuteListener listener;

    private Group           group         = null;

    private Group           gExecutable   = null;

    private Label           label1        = null;

    private Text            tExecuteFile  = null;

    private Button          bIgnoreError  = null;

    private Label           label3        = null;

    private Text            tParameter    = null;

    private Label           label4        = null;

    private Text            tLogFile      = null;

    private Button          bIgnoreSignal = null;

    private Button          bExecutable   = null;

    private Button          bScript       = null;

    private ScriptForm      scriptForm    = null;

    private Label           label5        = null;

    private Group           gEnvironment  = null;

    private Table           tVariables    = null;

    private Label           label         = null;

    private Text            tName         = null;

    private Label           label7        = null;

    private Text            tValue        = null;

    private Button          bApply        = null;

    private Button          bRemove       = null;

    private Label           label2        = null;

    private Button          bNoExecute    = null;

    private Label           label9        = null;


    public ExecuteForm(Composite parent, int style, SchedulerDom dom, Element job) {
        super(parent, style);
        
        
        initialize();
        setToolTipText();

        listener = new ExecuteListener(dom, job);

        scriptForm.setAttributes(dom, job, Editor.EXECUTE);
        fillForm();
        
        this.group.setEnabled(Utils.isElementEnabled("job", dom, job));
         

    }


    public void apply() {
        if (bApply.isEnabled())
            applyVariable();
        if (scriptForm.isUnsaved())
            scriptForm.apply();
    }


    public boolean isUnsaved() {
        return scriptForm.isUnsaved() || bApply.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(729, 532));

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData10 = new org.eclipse.swt.layout.GridData();
        gridData10.horizontalSpan = 2;
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData17 = new org.eclipse.swt.layout.GridData();
        gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData17.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout4 = new GridLayout();
        gridLayout4.numColumns = 3;
        group = new Group(this, SWT.NONE);
        group.setText("Execute");
        bNoExecute = new Button(group, SWT.RADIO);
        bNoExecute.setSelection(true);
        bNoExecute.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bNoExecute.getSelection()) {
                    scriptForm.setLanguage(ScriptListener.NONE);
                    listener.setNothing();
                    fillForm();
                }
            }
        });
        label9 = new Label(group, SWT.NONE);
        label9.setText("No Execute");
        label9.setLayoutData(gridData10);
        bExecutable = new Button(group, SWT.RADIO);
        bExecutable.setLayoutData(gridData18);
        bExecutable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bExecutable.getSelection()) {
                    scriptForm.setLanguage(ScriptListener.NONE);
                    listener.setExecutable(true);
                    fillForm();
                }
            }
        });
        createGroup1();
        group.setLayout(gridLayout4);
        createGroup12();
        bScript = new Button(group, SWT.RADIO);
        bScript.setLayoutData(gridData17);
        bScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bScript.getSelection()) {
                    listener.setExecutable(false);
                    scriptForm.setLanguage(ScriptListener.JAVA);
                    fillForm();
                }
            }
        });
        createScriptForm();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData61 = new org.eclipse.swt.layout.GridData();
        gridData61.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData61.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData41 = new org.eclipse.swt.layout.GridData();
        gridData41.grabExcessHorizontalSpace = false;
        gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData41.horizontalSpan = 2;
        GridData gridData21 = new org.eclipse.swt.layout.GridData();
        gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData21.grabExcessHorizontalSpace = false;
        gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData12 = new org.eclipse.swt.layout.GridData();
        gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData12.grabExcessHorizontalSpace = true;
        gridData12.horizontalSpan = 3;
        gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalSpan = 3;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalSpan = 3;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gExecutable = new Group(group, SWT.NONE);
        gExecutable.setText("Run Executable");
        gExecutable.setLayout(gridLayout);
        gExecutable.setLayoutData(gridData);
        label1 = new Label(gExecutable, SWT.NONE);
        label1.setText("File");
        tExecuteFile = new Text(gExecutable, SWT.BORDER);
        tExecuteFile.setLayoutData(gridData12);
        tExecuteFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setFile(tExecuteFile.getText());
            }
        });
        label3 = new Label(gExecutable, SWT.NONE);
        label3.setText("Parameter:   ");
        tParameter = new Text(gExecutable, SWT.BORDER);
        tParameter.setLayoutData(gridData2);
        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setParam(tParameter.getText());
            }
        });
        label4 = new Label(gExecutable, SWT.NONE);
        label4.setText("Log file:");
        tLogFile = new Text(gExecutable, SWT.BORDER);
        tLogFile.setLayoutData(gridData3);
        tLogFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setLogFile(tLogFile.getText());
            }
        });
        label5 = new Label(gExecutable, SWT.NONE);
        label5.setText("Ignore:");
        label5.setLayoutData(gridData61);
        bIgnoreSignal = new Button(gExecutable, SWT.CHECK);
        bIgnoreSignal.setText("Signal");
        bIgnoreSignal.setLayoutData(gridData21);
        bIgnoreSignal.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setIgnoreSignal(bIgnoreSignal.getSelection());
            }
        });
        bIgnoreError = new Button(gExecutable, SWT.CHECK);
        bIgnoreError.setText("Error");
        bIgnoreError.setLayoutData(gridData41);
        bIgnoreError.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setIgnoreError(bIgnoreError.getSelection());
            }
        });
    }


    /**
     * This method initializes scriptForm
     */
    private void createScriptForm() {
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.horizontalSpan = 2;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        scriptForm = new ScriptForm(group, SWT.NONE);
        scriptForm.setLayoutData(gridData1);
    }


    /**
     * This method initializes group1
     */
    private void createGroup12() {
        GridData gridData11 = new org.eclipse.swt.layout.GridData();
        gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData8 = new org.eclipse.swt.layout.GridData();
        gridData8.horizontalSpan = 5;
        gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData7 = new org.eclipse.swt.layout.GridData();
        gridData7.grabExcessHorizontalSpace = true;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5;
        GridData gridData9 = new org.eclipse.swt.layout.GridData();
        gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gEnvironment = new Group(group, SWT.NONE);
        gEnvironment.setText("Environment Variables");
        label = new Label(gEnvironment, SWT.NONE);
        label.setText("Name");
        tName = new Text(gEnvironment, SWT.BORDER);
        tName.setLayoutData(gridData11);
        label7 = new Label(gEnvironment, SWT.NONE);
        label7.setText("Value");
        tValue = new Text(gEnvironment, SWT.BORDER);
        tValue.setLayoutData(gridData7);
        bApply = new Button(gEnvironment, SWT.NONE);
        label2 = new Label(gEnvironment, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData8);
        createTable();
        gEnvironment.setLayout(gridLayout1);
        gEnvironment.setLayoutData(gridData9);
        bRemove = new Button(gEnvironment, SWT.NONE);
        bRemove.setText("Remove");
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData5);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    int index = tVariables.getSelectionIndex();
                    listener.removeVariable(tVariables.getItem(index).getText(0));
                    tVariables.remove(index);

                    tName.setText("");
                    tValue.setText("");
                }
                bRemove.setEnabled(false);
                bApply.setEnabled(false);
            }
        });
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tName.getText().equals(""));
            }
        });
        tName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tName.getText().equals(""))
                    applyVariable();
            }
        });
        tValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tName.getText().equals(""))
                    applyVariable();
            }
        });
        tValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tName.getText().equals(""));
            }
        });
        bApply.setText("&Apply");
        bApply.setLayoutData(gridData6);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyVariable();
            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalSpan = 4;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.heightHint = 60;
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        tVariables = new Table(gEnvironment, SWT.BORDER | SWT.FULL_SELECTION);
        tVariables.setHeaderVisible(true);
        tVariables.setLayoutData(gridData4);
        tVariables.setLinesVisible(true);
        tVariables.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tVariables.getSelectionCount() > 0) {
                    TableItem item = tVariables.getSelection()[0];
                    tName.setText(item.getText(0));
                    tValue.setText(item.getText(1));
                }
                bRemove.setEnabled(tVariables.getSelectionCount() > 0);
                bApply.setEnabled(false);
            }
        });
        TableColumn tableColumn = new TableColumn(tVariables, SWT.NONE);
        tableColumn.setWidth(80);
        tableColumn.setText("Name");
        TableColumn tableColumn1 = new TableColumn(tVariables, SWT.NONE);
        tableColumn1.setWidth(150);
        tableColumn1.setText("Value");
    }


    private void fillForm() {
        setEnabled(false);
        scriptForm.setFullEnabled(false);

        if (listener.isScript()) {
            bNoExecute.setSelection(false);
            bExecutable.setSelection(false);
            bScript.setSelection(true);

            scriptForm.setFullEnabled(true);
        } else if (listener.isExecutable()) {
            bNoExecute.setSelection(false);
            bExecutable.setSelection(true);
            bScript.setSelection(false);

            setEnabled(true);

            if (!tExecuteFile.getText().equals("") && listener.getFile().equals(""))
                listener.setFile(tExecuteFile.getText());
            tExecuteFile.setText(listener.getFile());

            if (!tLogFile.getText().equals("") && listener.getLogFile().equals(""))
                listener.setLogFile(tLogFile.getText());
            tLogFile.setText(listener.getLogFile());

            if (!tParameter.getText().equals("") && listener.getParam().equals(""))
                listener.setParam(tParameter.getText());
            tParameter.setText(listener.getParam());

            if (bIgnoreError.getSelection() && !listener.isIgnoreError())
                listener.setIgnoreError(true);
            bIgnoreError.setSelection(listener.isIgnoreError());

            if (bIgnoreSignal.getSelection() && !listener.isIgnoreSignal())
                listener.setIgnoreSignal(true);
            bIgnoreSignal.setSelection(listener.isIgnoreSignal());

            listener.fillEnvironmentTable(tVariables);
            bApply.setEnabled(!tName.getText().equals(""));
            bRemove.setEnabled(tVariables.getSelectionCount() > 0);
            tExecuteFile.setFocus();
        } else {
            bNoExecute.setSelection(true);
            bExecutable.setSelection(false);
            bScript.setSelection(false);
        }

    }


    public void setEnabled(boolean enabled) {
        tExecuteFile.setEnabled(enabled);
        tLogFile.setEnabled(enabled);
        tParameter.setEnabled(enabled);
        bIgnoreError.setEnabled(enabled);
        bIgnoreSignal.setEnabled(enabled);
        tVariables.setEnabled(enabled);
        tName.setEnabled(enabled);
        tValue.setEnabled(enabled);
        bRemove.setEnabled(enabled);
        bApply.setEnabled(enabled);
    }


    private void applyVariable() {
        if (!tName.getText().equals("")) {
            listener.applyVariable(tName.getText(), tValue.getText());
            listener.fillEnvironmentTable(tVariables);
        }
        tName.setText("");
        tValue.setText("");
        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        tVariables.deselectAll();
        tName.setFocus();
    }


    public void setToolTipText() {
        bNoExecute.setToolTipText(Messages.getTooltip("execute.choose_none"));
        bExecutable.setToolTipText(Messages.getTooltip("execute.choose_executable"));
        bScript.setToolTipText(Messages.getTooltip("execute.choose_script"));
        tExecuteFile.setToolTipText(Messages.getTooltip("process.file"));
        tParameter.setToolTipText(Messages.getTooltip("process.param"));
        tLogFile.setToolTipText(Messages.getTooltip("process.log_file"));
        bIgnoreSignal.setToolTipText(Messages.getTooltip("process.ignore_signal"));
        bIgnoreError.setToolTipText(Messages.getTooltip("process.ignore_error"));
        tName.setToolTipText(Messages.getTooltip("process.environment.name"));
        tValue.setToolTipText(Messages.getTooltip("process.environment.value"));
        bRemove.setToolTipText(Messages.getTooltip("process.environment.btn_remove"));
        bApply.setToolTipText(Messages.getTooltip("process.environment.btn_apply"));
        tVariables.setToolTipText(Messages.getTooltip("process.environment.table"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
