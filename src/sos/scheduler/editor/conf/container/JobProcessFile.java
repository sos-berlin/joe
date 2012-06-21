package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.app.Messages;

 
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class  JobProcessFile extends FormBaseClass {

    @SuppressWarnings("unused")
    private final String    conClassName            = "JobProcessFile";
    @SuppressWarnings("unused")
    private final String    conSVNVersion           = "$Id$";

    private boolean init = true;
    private JobListener objJobDataProvider = null;
    private Text tExecuteFile = null;
    private Text tParameter = null;
    private Text tLogFile = null;
    private Button bIgnoreSignal = null;
    private Button bIgnoreError = null;
    
    public JobProcessFile(Composite pParentComposite, JobListener pobjJobDataProvider,JobProcessFile that) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;
        
        init = true;
        createGroup();
        getValues(that);
        init = false;
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent () {
    }
    
    private void getValues(JobProcessFile that){
        if (that == null){
            return;
        }
        
        this.tExecuteFile.setText(that.tExecuteFile.getText());
        this.tParameter.setText(that.tParameter.getText());
        this.tLogFile.setText(that.tLogFile.getText());
        
        this.bIgnoreSignal.setSelection(that.bIgnoreSignal.getSelection());
        this.bIgnoreError.setSelection(that.bIgnoreError.getSelection());
    }
    
    private void createGroup() {
        objParent.setLayout(new GridLayout());

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
        Group gExecutable = new Group(objParent, SWT.NONE);
        gExecutable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gExecutable.setText("Run Executable");
        gExecutable.setLayout(gridLayout);
        Label label1 = new Label(gExecutable, SWT.NONE);
        label1.setText("File");
        tExecuteFile = new Text(gExecutable, SWT.BORDER);
        tExecuteFile.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                tExecuteFile.selectAll();
            }
        });
        tExecuteFile.setLayoutData(gridData12);
        tExecuteFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setFile(tExecuteFile.getText());
                }
            }
        });
        Label label3 = new Label(gExecutable, SWT.NONE);
        label3.setText("Parameter:   ");
        tParameter = new Text(gExecutable, SWT.BORDER);
        tParameter.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                tParameter.selectAll();
            }
        });
        tParameter.setLayoutData(gridData2);
        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (!init)
                    objJobDataProvider.setParam(tParameter.getText());
            }
        });
        Label label4 = new Label(gExecutable, SWT.NONE);
        label4.setText("Log file:");
        tLogFile = new Text(gExecutable, SWT.BORDER);
        tLogFile.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                tLogFile.selectAll();
            }
        });
        tLogFile.setLayoutData(gridData3);
        tLogFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (!init)
                    objJobDataProvider.setLogFile(tLogFile.getText());
            }
        });
        Label label5 = new Label(gExecutable, SWT.NONE);
        label5.setText("Ignore:");
        label5.setLayoutData(gridData61);
        bIgnoreSignal = new Button(gExecutable, SWT.CHECK);
        bIgnoreSignal.setText("Signal");
        bIgnoreSignal.setLayoutData(gridData21);
        bIgnoreSignal.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (!init)
                    objJobDataProvider.setIgnoreSignal(bIgnoreSignal.getSelection());
            }
        });
        bIgnoreError = new Button(gExecutable, SWT.CHECK);
        bIgnoreError.setText("Error");
        bIgnoreError.setLayoutData(gridData41);
        bIgnoreError.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (!init)
                    objJobDataProvider.setIgnoreError(bIgnoreError.getSelection());
            }
        });

        boolean enabled = true;
        tExecuteFile.setEnabled(enabled);
        tLogFile.setEnabled(enabled);
        tParameter.setEnabled(enabled);
        bIgnoreError.setEnabled(enabled);
        bIgnoreSignal.setEnabled(enabled);

        tExecuteFile.setToolTipText(Messages.getTooltip("process.file"));
        tParameter.setToolTipText(Messages.getTooltip("process.param"));
        tLogFile.setToolTipText(Messages.getTooltip("process.log_file"));
        bIgnoreSignal.setToolTipText(Messages.getTooltip("process.ignore_signal"));
        bIgnoreError.setToolTipText(Messages.getTooltip("process.ignore_error"));

        objParent.layout();        
    }
     
}
