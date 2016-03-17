package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobMainOptionForm extends SOSJOEMessageCodes {

    private Combo sPriority = null;
    private Text sIdleTimeout = null;
    private Text sTimeout = null;
    private Text sTasks = null;
    private Text tIgnoreSignals = null;
    private JobListener listener = null;
    private Group group = null;
    private Group gMain = null;
    private Label label3 = null;
    private Text tSpoolerID = null;
    private Label label11 = null;
    private Label label13 = null;
    private Label label15 = null;
    private Label label17 = null;
    private Text tMintasks = null;
    private boolean updateTree = false;
    private Button bForceIdletimeout = null;
    private Combo cSignals = null;
    private Text txtJavaOptions = null;
    private Combo comVisible = null;
    private Button addButton = null;
    private boolean init = true;
    private Text txtWarnIfLongerThan = null;
    private Text txtWarnIfShorterThan = null;

    public JobMainOptionForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style);
        init = true;
        dom.setInit(true);
        this.setEnabled(Utils.isElementEnabled("job", dom, job));
        listener = new JobListener(dom, job, main);

        initialize();
        updateTree = false;
        initForm();
        dom.setInit(false);
        init = false;
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        // return bApply.isEnabled();
        return false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(723, 566));
        if (tSpoolerID.isVisible())
            tSpoolerID.setFocus();
        else
            txtJavaOptions.setFocus();
    }

    /** This method initializes group */
    private void createGroup() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        group = new Group(this, SWT.NONE);
        String strT = JOE_M_JobAssistent_JobGroup.params(listener.getJobName());
        if (listener.isDisabled())
            strT += " " + JOE_M_JobCommand_Disabled.label();
        group.setText(strT);
        group.setLayout(gridLayout2);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gMain = new Group(group, SWT.NONE);
        final GridData gridData_12 = new GridData(SWT.FILL, GridData.FILL, true, true);
        gridData_12.heightHint = 353;
        gMain.setLayoutData(gridData_12);
        gMain.setText(JOE_M_JobMainOptionForm_MainOptions.params(listener.getJobName()));
        gMain.setLayout(gridLayout);
        label3 = JOE_L_JobMainOptionForm_SchedulerID.Control(new Label(gMain, SWT.NONE));
        label3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        label3.setVisible(!listener.get_dom().isLifeElement() && !listener.get_dom().isDirectory());
        tSpoolerID = JOE_T_JobMainOptionForm_SchedulerID.Control(new Text(gMain, SWT.BORDER));
        tSpoolerID.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (init)
                    return;
                listener.setSpoolerID(tSpoolerID.getText());
            }
        });
        tSpoolerID.setVisible(!listener.get_dom().isLifeElement() && !listener.get_dom().isDirectory());
        final Label java_optionsLabel = JOE_L_JobMainOptionForm_JavaOptions.Control(new Label(gMain, SWT.NONE));
        java_optionsLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        txtJavaOptions = JOE_T_JobMainOptionForm_JavaOptions.Control(new Text(gMain, SWT.BORDER));
        txtJavaOptions.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setJavaOptions(txtJavaOptions.getText());
            }
        });
        txtJavaOptions.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        final Label ignore_signalLabel = JOE_L_JobMainOptionForm_IgnoreSignals.Control(new Label(gMain, SWT.NONE));
        ignore_signalLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        tIgnoreSignals = JOE_T_JobMainOptionForm_IgnoreSignals.Control(new Text(gMain, SWT.BORDER));
        tIgnoreSignals.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setIgnoreSignal(tIgnoreSignals.getText());
            }
        });
        // gridData_3.widthHint = 48;
        tIgnoreSignals.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        addButton = JOE_B_JobMainOptionForm_Add.Control(new Button(gMain, SWT.NONE));
        addButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;
                if (tIgnoreSignals.getText().equals("")) {
                    tIgnoreSignals.setText(cSignals.getText());
                } else {
                    tIgnoreSignals.setText(tIgnoreSignals.getText() + " " + cSignals.getText());
                }
            }
        });
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        cSignals = JOE_Cbo_JobMainOptionForm_Signals.Control(new Combo(gMain, SWT.NONE));
        cSignals.setItems(new String[] { "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL",
                "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN",
                "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS." });
        cSignals.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        label17 = JOE_L_JobMainOptionForm_Priority.Control(new Label(gMain, SWT.NONE));
        final GridData gridData_7 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        gridData_7.widthHint = 41;
        label17.setLayoutData(gridData_7);
        sPriority = JOE_Cbo_JobMainOptionForm_Priority.Control(new Combo(gMain, SWT.NONE));
        sPriority.setItems(new String[] { "idle", "below_normal", "normal", "above_normal", "high" });
        sPriority.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = (Utils.isOnlyDigits(e.text) || e.text.equals("idle") || e.text.equals("below_normal") || e.text.equals("normal")
                        || e.text.equals("above_normal") || e.text.equals("high"));
            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_1.verticalIndent = -1;
        sPriority.setLayoutData(gridData_1);
        sPriority.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                Utils.setBackground(-20, 20, sPriority);
                listener.setPriority(sPriority.getText());
            }
        });
        new Label(gMain, SWT.NONE);
        new Label(gMain, SWT.NONE);
        final Label visibleLabel = JOE_L_JobMainOptionForm_Visible.Control(new Label(gMain, SWT.NONE));
        visibleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        comVisible = JOE_Cbo_JobMainOptionForm_Visible.Control(new Combo(gMain, SWT.READ_ONLY));
        comVisible.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        comVisible.setItems(new String[] { "", "yes", "no", "never" });
        comVisible.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setVisible(comVisible.getText());
            }
        });
        new Label(gMain, SWT.NONE);
        new Label(gMain, SWT.NONE);
        // gridData_16.widthHint = 17;
        final Label minMaskLabel = JOE_L_JobMainOptionForm_MinTasks.Control(new Label(gMain, SWT.NONE));
        minMaskLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        tMintasks = JOE_T_JobMainOptionForm_MinTasks.Control(new Text(gMain, SWT.BORDER));
        tMintasks.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        tMintasks.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setMintasks(tMintasks.getText());
            }
        });
        tMintasks.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        new Label(gMain, SWT.NONE);
        new Label(gMain, SWT.NONE);
        label15 = JOE_L_JobMainOptionForm_Tasks.Control(new Label(gMain, SWT.NONE));
        label15.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        sTasks = JOE_T_JobMainOptionForm_Tasks.Control(new Text(gMain, SWT.BORDER));
        sTasks.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        sTasks.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        sTasks.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        sTasks.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setTasks(sTasks.getText());
            }
        });
        new Label(gMain, SWT.NONE);
        new Label(gMain, SWT.NONE);
        label13 = JOE_L_JobMainOptionForm_Timeout.Control(new Label(gMain, SWT.NONE));
        label13.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        sTimeout = JOE_T_JobMainOptionForm_Timeout.Control(new Text(gMain, SWT.BORDER));
        sTimeout.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                // e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        sTimeout.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setTimeout(sTimeout.getText());
            }
        });
        // gridData_9.widthHint = 75;
        sTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Label hhmmssLabel = JOE_L_JobAssistent_TimeFormat.Control(new Label(gMain, SWT.NONE));
        hhmmssLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        label11 = JOE_L_JobMainOptionForm_IdleTimeout.Control(new Label(gMain, SWT.NONE));
        label11.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        sIdleTimeout = JOE_T_JobMainOptionForm_IdleTimeout.Control(new Text(gMain, SWT.BORDER));
        sIdleTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        sIdleTimeout.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                // e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        sIdleTimeout.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setIdleTimeout(sIdleTimeout.getText());
            }
        });
        final Label hhmmssLabel_1 = JOE_L_JobMainOptionForm_IdleTimeoutFormat.Control(new Label(gMain, SWT.NONE));
        hhmmssLabel_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        @SuppressWarnings("unused")
        final Label warnIfLongerLabel = JOE_L_JobMainOptionForm_WarnIfLonger.Control(new Label(gMain, SWT.NONE));
        txtWarnIfLongerThan = JOE_T_JobMainOptionForm_WarnIfLonger.Control(new Text(gMain, SWT.BORDER));
        txtWarnIfLongerThan.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
            }
        });
        txtWarnIfLongerThan.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setWarnIfLongerThan(txtWarnIfLongerThan.getText());
            }
        });
        txtWarnIfLongerThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final Label hhmmssLabel_1_1 = JOE_L_JobMainOptionForm_WarnIfLongerFormat.Control(new Label(gMain, SWT.NONE));
        hhmmssLabel_1_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        @SuppressWarnings("unused")
        final Label warnIfShorterLabel = JOE_L_JobMainOptionForm_WarnIfShorter.Control(new Label(gMain, SWT.NONE));
        txtWarnIfShorterThan = JOE_T_JobMainOptionForm_WarnIfShorter.Control(new Text(gMain, SWT.BORDER));
        txtWarnIfShorterThan.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setWarnIfShorterThan(txtWarnIfShorterThan.getText());
            }
        });
        txtWarnIfShorterThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final Label hhmmssOrPercentageLabel = JOE_L_JobMainOptionForm_WarnIfShorterFormat.Control(new Label(gMain, SWT.NONE));
        hhmmssOrPercentageLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        final Label force_idle_timeoutLabel = JOE_L_JobMainOptionForm_ForceIdleTimeout.Control(new Label(gMain, SWT.NONE));
        force_idle_timeoutLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
        bForceIdletimeout = JOE_B_JobMainOptionForm_ForceIdleTimeout.Control(new Button(gMain, SWT.CHECK));
        bForceIdletimeout.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
        bForceIdletimeout.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;
                listener.setForceIdletimeout(bForceIdletimeout.getSelection());
            }
        });
    }

    public void initForm() {
        updateTree = true;
        tSpoolerID.setText(listener.getSpoolerID());
        int index = 0;
        bForceIdletimeout.setSelection(listener.getForceIdletimeout());
        index = sPriority.indexOf(listener.getPriority());
        if (index >= 0)
            sPriority.select(index);
        else {
            int p = Utils.str2int(listener.getPriority(), 20);
            if (p == -999) {
                sPriority.setText("");
            } else {
                if (p < -20) {
                    p = -20;
                }
                sPriority.setText(String.valueOf(p));
            }
        }
        sTasks.setText(listener.getTasks());
        if (listener.getMintasks() != null)
            tMintasks.setText(listener.getMintasks());
        if (listener.getPriority() != null)
            sPriority.setText(listener.getPriority());
        tIgnoreSignals.setText(listener.getIgnoreSignal());
        sTimeout.setText(listener.getTimeout());
        sIdleTimeout.setText(listener.getIdleTimeout());
        comVisible.setText(listener.getVisible());
        txtWarnIfLongerThan.setText(listener.getWarnIfLongerThan());
        txtWarnIfShorterThan.setText(listener.getWarnIfShorterThan());
        txtJavaOptions.setText(listener.getJavaOptions());
    }

} // @jve:decl-index=0:visual-constraint="10,10"
