package com.sos.joe.wizard.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;
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

public class JobAssistentDelayAfterErrorForm extends JobWizardBaseForm {

    private Element job = null;
    private Text txtErrorCount = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Button butShow = null;
    private Label lblOftenSetBack = null;
    private Label lblLongWait = null;
    private Text txtHour = null;
    private Text txtMin = null;
    private Text txtSecound = null;
    private Text txtStop = null;
    private JobOptionsListener optionlistener = null;
    private Shell shellSetBack = null;
    private Combo jobname = null;
    private int assistentType = -1;
    private int sizeOfErrorDelays = 0;
    private boolean modify = false;
    private Button butBack = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    private boolean closeDialog = false;

    public JobAssistentDelayAfterErrorForm(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        update = update_;
        optionlistener = new JobOptionsListener(dom, job_);
        assistentType = assistentType_;
        job = job_;
    }

    public void showDelayAfterErrorForm() {
        shellSetBack = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        shellSetBack.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog) {
                    close();
                }
                e.doit = shellSetBack.isDisposed();
            }
        });
        shellSetBack.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shellSetBack.setLayout(gridLayout);
        shellSetBack.setSize(546, 221);
        String step = " ";
        if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", job))) {
            step = step + SOSJOEMessageCodes.JOE_M_JobAssistent_Step8of9.label();
        } else {
            step = SOSJOEMessageCodes.JOE_M_JobAssistent_Step8of8.label();
        }
        shellSetBack.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_DelayAfterError.params(step));
        final Group jobGroup = new Group(shellSetBack, SWT.NONE);
        String strJobName = Utils.getAttributeValue("name", job);
        jobGroup.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(strJobName));
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        jobGroup.setLayoutData(gridData_2);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginTop = 10;
        gridLayout_1.marginRight = 10;
        gridLayout_1.marginLeft = 10;
        gridLayout_1.marginHeight = 10;
        gridLayout_1.marginBottom = 10;
        gridLayout_1.numColumns = 3;
        jobGroup.setLayout(gridLayout_1);
        lblLongWait = SOSJOEMessageCodes.JOE_L_JobAssistent_JobWait.control(new Label(jobGroup, SWT.NONE));
        lblLongWait.setLayoutData(new GridData());
        final Composite composite = SOSJOEMessageCodes.JOE_Cmp_JobAssistent_Time.control(new Composite(jobGroup, SWT.NONE));
        final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        gridData_4.widthHint = 100;
        composite.setLayoutData(gridData_4);
        final GridLayout gridLayout_5 = new GridLayout();
        gridLayout_5.marginWidth = 0;
        gridLayout_5.numColumns = 3;
        composite.setLayout(gridLayout_5);
        java.util.List l = optionlistener.getErrorDelays();
        sizeOfErrorDelays = l.size();
        int hour = 0;
        int min = 0;
        int sec = 0;
        String errorCount = null;
        String stopCount = null;
        for (int i = 0; i < 2; i++) {
            if (sizeOfErrorDelays != 0 && sizeOfErrorDelays >= sizeOfErrorDelays - i) {
                Element e1 = (Element) (l.get(sizeOfErrorDelays - i - 1));
                String delayOrStop = optionlistener.getDelay(e1);
                if ("stop".equals(delayOrStop)) {
                    stopCount = Utils.getAttributeValue("error_count", e1);
                } else {
                    errorCount = Utils.getAttributeValue("error_count", e1);
                    hour = Utils.getHours(delayOrStop, -999);
                    min = Utils.getMinutes(delayOrStop, -999);
                    sec = Utils.getSeconds(delayOrStop, -999);
                }
            }
        }
        txtHour = SOSJOEMessageCodes.JOE_T_JobAssistent_Hour.control(new Text(composite, SWT.BORDER));
        txtHour.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtHour.setLayoutData(new GridData(17, SWT.DEFAULT));
        txtHour.setText(hour == 0 ? "" : Utils.getIntegerAsString(hour));
        txtHour.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 23, txtHour);
                if (!Utils.isNumeric(txtHour.getText())) {
                    ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.OK);
                }
                modify = true;
            }
        });
        txtMin = SOSJOEMessageCodes.JOE_T_JobAssistent_Min.control(new Text(composite, SWT.BORDER));
        txtMin.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtMin.setLayoutData(new GridData(17, SWT.DEFAULT));
        txtMin.setText(min == 0 ? "" : Utils.getIntegerAsString(min));
        txtMin.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtMin);
                if (!Utils.isNumeric(txtMin.getText())) {
                    ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.OK);
                }
                modify = true;
            }
        });
        txtSecound = SOSJOEMessageCodes.JOE_T_JobAssistent_Sec.control(new Text(composite, SWT.BORDER));
        txtSecound.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtSecound.setLayoutData(new GridData(16, SWT.DEFAULT));
        txtSecound.setText(sec == 0 ? "" : Utils.getIntegerAsString(sec));
        txtSecound.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtSecound);
                if (!Utils.isNumeric(txtSecound.getText())) {
                    ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.OK);
                }
                modify = true;
            }
        });
        final Label hhmmssLabel = SOSJOEMessageCodes.JOE_L_JobAssistent_TimeFormat.control(new Label(jobGroup, SWT.NONE));
        hhmmssLabel.setLayoutData(new GridData(64, SWT.DEFAULT));
        lblOftenSetBack = SOSJOEMessageCodes.JOE_L_JobAssistent_OftenSetBack.control(new Label(jobGroup, SWT.NONE));
        lblOftenSetBack.setLayoutData(new GridData());
        txtErrorCount = SOSJOEMessageCodes.JOE_T_JobAssistent_ErrorCount.control(new Text(jobGroup, SWT.BORDER));
        txtErrorCount.setText(errorCount != null ? errorCount : "");
        txtErrorCount.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                modify = true;
            }
        });
        final GridData gridData_1 = new GridData(50, SWT.DEFAULT);
        txtErrorCount.setLayoutData(gridData_1);
        txtErrorCount.setFocus();
        new Label(jobGroup, SWT.NONE);
        final Label lnlNumberOfMaxErros = SOSJOEMessageCodes.JOE_L_JobAssistent_MaxErrors.control(new Label(jobGroup, SWT.NONE));
        lnlNumberOfMaxErros.setLayoutData(new GridData());
        txtStop = SOSJOEMessageCodes.JOE_T_JobAssistent_MaxErrors.control(new Text(jobGroup, SWT.BORDER));
        txtStop.setText(stopCount != null ? stopCount : "");
        txtStop.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                modify = true;
            }
        });
        final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gridData_3.widthHint = 50;
        txtStop.setLayoutData(gridData_3);
        new Label(jobGroup, SWT.NONE);
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        shellSetBack.setBounds((screen.width - shellSetBack.getBounds().width) / 2, (screen.height - shellSetBack.getBounds().height) / 2,
                shellSetBack.getBounds().width, shellSetBack.getBounds().height);
        shellSetBack.open();
        final Composite composite_1 = SOSJOEMessageCodes.JOE_Cmp_JobAssistent_Cancel.control(new Composite(shellSetBack, SWT.NONE));
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.marginWidth = 0;
        composite_1.setLayout(gridLayout_3);
        butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.control(new Button(composite_1, SWT.NONE));
        butCancel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        final Composite composite_2 = SOSJOEMessageCodes.JOE_Cmp_JobAssistent_Show.control(new Composite(shellSetBack, SWT.NONE));
        composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        final GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.marginWidth = 0;
        gridLayout_4.numColumns = 5;
        composite_2.setLayout(gridLayout_4);
        butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.control(new Button(composite_2, SWT.NONE));
        butShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (!check()) {
                    return;
                }
                refreshElement(false);
                if (Options.getPropertyBoolean("editor.job.show.wizard")) {
                    Utils.showClipboard(Utils.getElementAsString(job), shellSetBack, false, null, false, null, false);
                }
                txtErrorCount.setFocus();
            }
        });
        butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.control(new Button(composite_2, SWT.NONE));
        butFinish.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        butFinish.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (!check()) {
                    return;
                }
                Utils.startCursor(shellSetBack);
                if (jobname != null) {
                    jobname.setText(Utils.getAttributeValue("name", job));
                }
                refreshElement(true);
                if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", job))) {
                    if (Options.getPropertyBoolean("editor.job.show.wizard")) {
                        Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_Finish.label() + "\n\n" + Utils.getElementAsString(job),
                                shellSetBack, false, null, false, null, true);
                    }
                } else {
                    if (Options.getPropertyBoolean("editor.job.show.wizard")) {
                        Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_EndWizard.label() + "\n\n" + Utils.getElementAsString(job),
                                shellSetBack, false, null, false, null, true);
                    }
                }
                closeDialog = true;
                Utils.stopCursor(shellSetBack);
                shellSetBack.dispose();
            }
        });
        if (!"yes".equals(Utils.getAttributeValue("order", job))) {
            butFinish.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        }
        butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.control(new Button(composite_2, SWT.NONE));
        butBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refreshElement(false);
                JobAssistentRunOptionsForms runOP = new JobAssistentRunOptionsForms(dom, update, job, assistentType);
                runOP.showRunOptionsForm();
                if (jobname != null) {
                    runOP.setJobname(jobname);
                }
                runOP.setBackUpJob(jobBackUp, jobForm);
                closeDialog = true;
                shellSetBack.dispose();
            }
        });
        butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.control(new Button(composite_2, SWT.NONE));
        butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.widthHint = 45;
        butNext.setLayoutData(gridData);
        butNext.setEnabled("yes".equals(Utils.getAttributeValue("order", job)));
        butNext.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(shellSetBack);
                refreshElement(false);
                JobAssistentDelayOrderAfterSetbackForm delay = new JobAssistentDelayOrderAfterSetbackForm(dom, update, job, assistentType);
                delay.showDelayOrderAfterSetbackForm();
                if (jobname != null) {
                    delay.setJobname(jobname);
                }
                delay.setBackUpJob(jobBackUp, jobForm);
                closeDialog = true;
                Utils.stopCursor(shellSetBack);
                shellSetBack.dispose();
            }
        });
        Utils.createHelpButton(composite_2, "JOE_M_JobAssistentDelayAfterErrorForm_Help.label", shellSetBack);
        shellSetBack.layout();
    }

    public void setToolTipText() {
        //
    }

    private void refreshElement(boolean apply) {
        if (modify) {
            if (!optionlistener.getErrorDelays().isEmpty() && sizeOfErrorDelays != optionlistener.getErrorDelays().size()) {
                optionlistener.deleteErrorDelay(optionlistener.getErrorDelays().size() - 1);
                optionlistener.deleteErrorDelay(optionlistener.getErrorDelays().size() - 1);
            }
            if (txtErrorCount.getText() != null && !txtErrorCount.getText().trim().isEmpty()) {
                String delay = Utils.getTime(txtHour.getText(), txtMin.getText(), txtSecound.getText(), true);
                optionlistener.newErrorDelay();
                optionlistener.applyErrorDelay(txtErrorCount.getText(), delay);
            }
            if (txtStop.getText() != null && !txtStop.getText().isEmpty()) {
                optionlistener.newErrorDelay();
                optionlistener.applyErrorDelay(txtStop.getText(), "stop");
            }
        }
        modify = false;
        if (apply) {
            if (assistentType == JOEConstants.JOB_WIZARD) {
                jobForm.initForm();
            } else {
                JobsListener listener = new JobsListener(dom, update);
                listener.newImportJob(job, assistentType);
            }
        }
    }

    private boolean check() {
        String sTime =
                (txtHour.getText() != null && !"00".equals(txtHour.getText()) ? txtHour.getText() : "").concat(
                        txtMin.getText() != null && !"00".equals(txtMin.getText()) ? txtMin.getText() : "").concat(
                        txtSecound.getText() != null && !"00".equals(txtSecound.getText()) ? txtSecound.getText() : "");
        String errorCount = txtErrorCount.getText() != null ? txtErrorCount.getText() : "";
        if (sTime.isEmpty() && !errorCount.isEmpty()) {
            ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_TimeMissing.label(), SWT.OK);
            return false;
        }
        if (!sTime.isEmpty() && errorCount.isEmpty()) {
            ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_ErrorCountMissing.label(), SWT.OK);
            return false;
        }
        return true;
    }

    private void close() {
        int cont = ErrorLog.message(shellSetBack, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null) {
                job.setContent(jobBackUp.cloneContent());
            }
            shellSetBack.dispose();
        }
    }

    public void setJobname(Combo jobname) {
        this.jobname = jobname;
    }

    public void setBackUpJob(Element backUpJob, ScriptJobMainForm jobForm_) {
        if (backUpJob != null) {
            jobBackUp = (Element) backUpJob.clone();
        }
        jobForm = jobForm_;
    }

}