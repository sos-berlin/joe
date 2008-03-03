package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;

public class ExecuteForm extends Composite implements IUnsaved, IUpdateLanguage {
    
	
	private ExecuteListener listener      = null;

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

    private Button          bNoExecute    = null;

    private Label           label9        = null;
    
    private ISchedulerUpdate update       = null;


    public ExecuteForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate update_) {
        super(parent, style);
        
        update = update_;
        
        initialize();
        setToolTipText();

        listener = new ExecuteListener(dom, job);

        scriptForm.setAttributes(dom, job, Editor.EXECUTE);
        fillForm();
        
        this.group.setEnabled(Utils.isElementEnabled("job", dom, job));
         

    }


    public void apply() {
        //if (bApply.isEnabled())
        //    applyVariable();
        if (scriptForm.isUnsaved())
            scriptForm.apply();
    }


    public boolean isUnsaved() {
        return scriptForm.isUnsaved(); //|| bApply.isEnabled();
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
        GridData gridData10 = new GridData();
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData17 = new org.eclipse.swt.layout.GridData();
        gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData17.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout4 = new GridLayout();
        gridLayout4.numColumns = 2;
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
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        scriptForm = new ScriptForm(group, SWT.NONE, update);
        scriptForm.setLayoutData(gridData1);
    }


    /**
     * This method initializes group1
     */
    private void createGroup12() {
        createTable();
    }


    /**
     * This method initializes table
     */
    private void createTable() {
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

            //listener.fillEnvironmentTable(tVariables);
            //bApply.setEnabled(!tName.getText().equals(""));
            //bRemove.setEnabled(tVariables.getSelectionCount() > 0);
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

    }
} // @jve:decl-index=0:visual-constraint="10,10"
