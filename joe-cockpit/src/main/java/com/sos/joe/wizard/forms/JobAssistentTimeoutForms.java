package com.sos.joe.wizard.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobAssistentTimeoutForms {

    private SchedulerDom dom = null;
    private ISchedulerUpdate update = null;
    private JobListener joblistener = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Button butShow = null;
    private Text txtTimeout = null;
    private Label lblTimeout = null;
    private int assistentType = -1;
    private Shell shellTimeout = null;
    private Button butBack = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    private boolean closeDialog = false;

    public JobAssistentTimeoutForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        update = update_;
        assistentType = assistentType_;
        joblistener = new JobListener(dom, job_, update);
    }

    public void showTimeOutForm() {
        shellTimeout = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        shellTimeout.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog) {
                    close();
                }
                e.doit = shellTimeout.isDisposed();
            }
        });
        shellTimeout.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shellTimeout.setLayout(gridLayout);
        shellTimeout.setSize(350, 164);
        String step = " ";
        if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", joblistener.getJob()))) {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step6of9.label();
        } else {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step6of8.label();
        }
        shellTimeout.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_Timeout.params(step));
        final Group jobGroup = new Group(shellTimeout, SWT.NONE);
        jobGroup.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(Utils.getAttributeValue("name", joblistener.getJob())));
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        jobGroup.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginTop = 10;
        gridLayout_1.marginRight = 10;
        gridLayout_1.marginLeft = 10;
        gridLayout_1.marginHeight = 10;
        gridLayout_1.numColumns = 2;
        jobGroup.setLayout(gridLayout_1);
        lblTimeout = SOSJOEMessageCodes.JOE_L_JobAssistent_Timeout.Control(new Label(jobGroup, SWT.NONE));
        lblTimeout.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, true));
        txtTimeout = SOSJOEMessageCodes.JOE_T_JobAssistent_Timeout.Control(new Text(jobGroup, SWT.BORDER));
        txtTimeout.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (txtTimeout.getText() != null && !txtTimeout.getText().trim().isEmpty()) {
                    joblistener.setTimeout(txtTimeout.getText());
                }
            }
        });
        final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, true, true);
        gridData_1.widthHint = 71;
        txtTimeout.setLayoutData(gridData_1);
        txtTimeout.setText(joblistener.getTimeout());
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        shellTimeout.setBounds((screen.width - shellTimeout.getBounds().width) / 2, (screen.height - shellTimeout.getBounds().height) / 2, 
                shellTimeout.getBounds().width, shellTimeout.getBounds().height);
        shellTimeout.open();
        final Composite composite_1 = new Composite(shellTimeout, SWT.NONE);
        final GridLayout gridLayout_2_1 = new GridLayout();
        gridLayout_2_1.marginWidth = 0;
        composite_1.setLayout(gridLayout_2_1);
        butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(composite_1, SWT.NONE));
        butCancel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        final Composite composite = new Composite(shellTimeout, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginWidth = 0;
        gridLayout_2.numColumns = 5;
        composite.setLayout(gridLayout_2);
        butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite, SWT.NONE));
        butShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                txtTimeout.setFocus();
                Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), shellTimeout, false, null, false, null, false);
            }
        });
        butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.Control(new Button(composite, SWT.NONE));
        butFinish.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                doFinish();
            }
        });
        butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.Control(new Button(composite, SWT.NONE));
        butBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Element job = joblistener.getJob();
                if (job.getChild("process") != null) {
                    JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update, job, assistentType);
                    process.showProcessForm();
                    process.setBackUpJob(jobBackUp, jobForm);
                } else {
                    JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update, job, assistentType);
                    script.showScriptForm();
                    script.setBackUpJob(jobBackUp, jobForm);
                }
                closeDialog = true;
                shellTimeout.dispose();
            }
        });
        butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite, SWT.NONE));
        butNext.setFocus();
        butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        butNext.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(shellTimeout);
                JobAssistentRunOptionsForms runOP = new JobAssistentRunOptionsForms(dom, update, joblistener.getJob(), assistentType);
                runOP.showRunOptionsForm();
                runOP.setBackUpJob(jobBackUp, jobForm);
                closeDialog = true;
                Utils.stopCursor(shellTimeout);
                shellTimeout.dispose();
            }
        });
        Utils.createHelpButton(composite, "JOE_M_JobAssistentTimeoutForms_Help.label", shellTimeout);
        txtTimeout.setFocus();
        shellTimeout.layout();
    }

    public void setToolTipText() {
        //
    }

    private void close() {
        int cont = ErrorLog.message(shellTimeout, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null) {
                joblistener.getJob().setContent(jobBackUp.cloneContent());
            }
            shellTimeout.dispose();
        }
    }

    public void setBackUpJob(Element backUpJob, ScriptJobMainForm jobForm_) {
        if (backUpJob != null) {
            jobBackUp = (Element) backUpJob.clone();
        }
        jobForm = jobForm_;
    }

    private void doFinish() {
        if (assistentType == JOEConstants.JOB_WIZARD) {
            jobForm.initForm();
        } else {
            JobsListener listener = new JobsListener(dom, update);
            listener.newImportJob(joblistener.getJob(), assistentType);
        }
        if (Options.getPropertyBoolean("editor.job.show.wizard")) {
            Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_Finish.label() + "\n\n" + Utils.getElementAsString(joblistener.getJob()), 
                    shellTimeout, false, null, false, null, true);
        }
        closeDialog = true;
        shellTimeout.dispose();
    }

}