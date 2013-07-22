package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobProcessFile extends FormBaseClass {

    @SuppressWarnings("unused")
    private final String conClassName       = "JobProcessFile";
    @SuppressWarnings("unused")
    private final String conSVNVersion      = "$Id$";

    private boolean      init               = true;
    private JobListener  objJobDataProvider = null;
    private Text         tExecuteFile       = null;
    private Text         tParameter         = null;
    private Text         tLogFile           = null;
    private Button       bIgnoreSignal      = null;
    private Button       bIgnoreError       = null;

    public JobProcessFile(final Composite pParentComposite, final JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;

        init = true;
        createGroup();
        initForm();
        init = false;
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent() {
    }

    private void initForm() {
        tExecuteFile.setText(objJobDataProvider.getFile());
        tLogFile.setText(objJobDataProvider.getLogFile());
        tParameter.setText(objJobDataProvider.getParam());
        bIgnoreError.setSelection(objJobDataProvider.isIgnoreError());
        bIgnoreSignal.setSelection(objJobDataProvider.isIgnoreSignal());
        tExecuteFile.setFocus();
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

        Group gExecutable = SOSJOEMessageCodes.JOE_G_JobProcessFile_RunExecutable.Control(new Group(objParent, SWT.NONE));
        gExecutable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gExecutable.setLayout(gridLayout);

        @SuppressWarnings("unused")
		Label label1 = SOSJOEMessageCodes.JOE_L_JobProcessFile_File.Control(new Label(gExecutable, SWT.NONE));

        tExecuteFile = SOSJOEMessageCodes.JOE_T_JobProcessFile_File.Control(new Text(gExecutable, SWT.BORDER));
        tExecuteFile.setLayoutData(gridData12);
        tExecuteFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setFile(tExecuteFile.getText());
                }
            }
        });

        @SuppressWarnings("unused")
		Label label3 = SOSJOEMessageCodes.JOE_L_JobProcessFile_Parameter.Control(new Label(gExecutable, SWT.NONE));

        tParameter = SOSJOEMessageCodes.JOE_T_JobProcessFile_Parameter.Control(new Text(gExecutable, SWT.BORDER));
        tParameter.setLayoutData(gridData2);
        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (!init)
                    objJobDataProvider.setParam(tParameter.getText());
            }
        });

        @SuppressWarnings("unused")
		Label label4 = SOSJOEMessageCodes.JOE_L_JobProcessFile_LogFile.Control(new Label(gExecutable, SWT.NONE));

        tLogFile = SOSJOEMessageCodes.JOE_T_JobProcessFile_LogFile.Control(new Text(gExecutable, SWT.BORDER));
        tLogFile.setLayoutData(gridData3);
        tLogFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (!init)
                    objJobDataProvider.setLogFile(tLogFile.getText());
            }
        });

        Label label5 = SOSJOEMessageCodes.JOE_L_JobProcessFile_Ignore.Control(new Label(gExecutable, SWT.NONE));
        label5.setLayoutData(gridData61);

        bIgnoreSignal = SOSJOEMessageCodes.JOE_B_JobProcessFile_IgnoreSignal.Control(new Button(gExecutable, SWT.CHECK));
        bIgnoreSignal.setLayoutData(gridData21);
        bIgnoreSignal.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                if (!init)
                    objJobDataProvider.setIgnoreSignal(bIgnoreSignal.getSelection());
            }
        });

        bIgnoreError = SOSJOEMessageCodes.JOE_B_JobProcessFile_IgnoreError.Control(new Button(gExecutable, SWT.CHECK));
        bIgnoreError.setLayoutData(gridData41);
        bIgnoreError.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
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

        objParent.layout();
    }
}
