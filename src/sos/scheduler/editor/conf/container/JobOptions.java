package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import org.jdom.Element;

import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobOptions extends FormBaseClass {

    private Combo        cboHistoryWithLog    = null;
    private Combo        cboHistoryOnProcess  = null;
    private Combo        cboHistory           = null;

    private Combo        logLevel             = null;
    private Label        lblJobChainJob       = null;
    private Composite    cOrder               = null;
    private Button       bOrderYes            = null;
    private Button       bOrderNo             = null;
    private Button       bStopOnError         = null;
    private Group        gOptionsGroup;
    private Group        gMainOptionsGroup;

    private Combo        sPriority            = null;
    private Text         sIdleTimeout         = null;
    private Text         sTimeout             = null;
    private Text         sTasks               = null;
    private Text         tIgnoreSignals       = null;

    private Label        label11              = null;
    private Label        label13              = null;
    private Label        label15              = null;
    private Label        label17              = null;
    private Text         tMintasks            = null;
    private Button       bForceIdletimeout    = null;
    private Combo        cSignals             = null;
    private Combo        comVisible           = null;
    private Button       addButton            = null;
    private Text         txtWarnIfLongerThan  = null;
    private Text         txtWarnIfShorterThan = null;

    @SuppressWarnings("unused")
    private final String conClassName         = "JobOptions";
    @SuppressWarnings("unused")
    private final String conSVNVersion        = "$Id$";

    private boolean      init                 = true;
    private JobListener  objJobDataProvider   = null;

    public JobOptions(Composite pParentComposite, JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;

        init = true;
        createGroup();
        createMainOptionsGroup();
        initForm();
        init = false;
    }

    private void createGroup() {

        gOptionsGroup = new Group(objParent, SWT.NONE);
        final GridData gridDataGroup = new GridData(GridData.FILL, GridData.FILL, true, false, 8, 5);
        gridDataGroup.heightHint = 180;
        gridDataGroup.minimumHeight = 30;
        gOptionsGroup.setLayoutData(gridDataGroup);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 4;
        gOptionsGroup.setLayout(gridLayout_2);
        gOptionsGroup.setText(Messages.getLabel("Options"));

        lblJobChainJob = new Label(gOptionsGroup, SWT.NONE);
        lblJobChainJob.setText(Messages.getLabel("OrderJob"));
        lblJobChainJob.setToolTipText(Messages.getTooltip("OrderJob"));
        lblJobChainJob.addHelpListener(new HelpListener() {
            @Override
            public void helpRequested(HelpEvent objHelpEvent) {
                MainWindow.message(Messages.getString("OrderJob.Help"), SWT.ICON_INFORMATION);
            }
        });

        lblJobChainJob.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    MainWindow.message("F1 gedrückt", SWT.ICON_INFORMATION);
                }

            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        cOrder = new Composite(gOptionsGroup, SWT.NONE);
        cOrder.setLayout(new RowLayout());
        bOrderYes = new Button(cOrder, SWT.RADIO);

        bOrderYes.setText(Messages.getLabel("yes.order"));
        bOrderYes.setToolTipText(Messages.getTooltip("yes.order"));
        bOrderYes.setSelection(objJobDataProvider.getOrder());

        bOrderYes.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (init || bOrderYes.getSelection() == false) {
                    return;
                }

                objJobDataProvider.setOrder(bOrderYes.getSelection());

                boolean _deleteRuntimeAttribute = false;

                Element job = objJobDataProvider.getJob();
                if (objJobDataProvider.getOrder() && job != null && job.getChild("run_time") != null) {
                    if (sos.scheduler.editor.app.Utils.getAttributeValue("single_start", job.getChild("run_time")).length() > 0 || sos.scheduler.editor.app.Utils.getAttributeValue("let_run", job.getChild("run_time")).length() > 0
                            || sos.scheduler.editor.app.Utils.getAttributeValue("once", job.getChild("run_time")).length() > 0) {

                        if (bOrderYes.isVisible()) {
                            _deleteRuntimeAttribute = Utils.checkElement(objJobDataProvider.getJobName(), objJobDataProvider.get_dom(), Editor.JOB, "change_order");
                        }

                        if (_deleteRuntimeAttribute) {
                            objJobDataProvider.getJob().getChild("run_time").removeAttribute("single_start");
                            objJobDataProvider.getJob().getChild("run_time").removeAttribute("let_run");
                            objJobDataProvider.getJob().getChild("run_time").removeAttribute("once");
                        }
                    }
                }
            }
        });
        bOrderNo = new Button(cOrder, SWT.RADIO);

        bOrderNo.setText(Messages.getLabel("no.order"));
        bOrderNo.setToolTipText(Messages.getTooltip("no.order"));
        bOrderNo.setEnabled(true);
        bOrderNo.setSelection(!objJobDataProvider.getOrder());

        bOrderNo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (init)
                    return;

                if (objJobDataProvider.getOrder() && !Utils.checkElement(objJobDataProvider.getJobName(), objJobDataProvider.get_dom(), Editor.JOB, "change_order")) {
                    init = true;
                    bOrderNo.setSelection(false);
                    bOrderYes.setSelection(true);
                    init = false;
                    return;
                }

                objJobDataProvider.setOrder(!bOrderNo.getSelection());
            }
        });

        new Label(gOptionsGroup, SWT.NONE);
        new Label(gOptionsGroup, SWT.NONE);

        final Label stop_on_errorLabel = new Label(gOptionsGroup, SWT.NONE);
        stop_on_errorLabel.setText(Messages.getLabel("StopOnError"));
        stop_on_errorLabel.setToolTipText(Messages.getTooltip("StopOnError"));

        bStopOnError = new Button(gOptionsGroup, SWT.CHECK);
        bStopOnError.setToolTipText(Messages.getTooltip("StopOnError"));
        bStopOnError.setSelection(objJobDataProvider.getStopOnError());

        // http://www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml#attribute_stop_on_error

        bStopOnError.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (objJobDataProvider.Check4HelpKey(event.keyCode, "job", "stop_on_error")) {
                    event.doit = true;
                    return;
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        // gridData_16.widthHint = 17;
        bStopOnError.setSelection(true);
        bStopOnError.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (init) {
                    return;
                }
                objJobDataProvider.setStopOnError(bStopOnError.getSelection());
            }
        });

        new Label(gOptionsGroup, SWT.NONE);
        new Label(gOptionsGroup, SWT.NONE);

        final Label logLevelLabel = new Label(gOptionsGroup, SWT.NONE);
        logLevelLabel.setText(Messages.getLabel("LogLevel"));

        logLevel = new Combo(gOptionsGroup, intComboBoxStyle);
        GridData gd_LogLevel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_LogLevel.minimumWidth = 150;
        logLevel.setLayoutData(gd_LogLevel);
        logLevel.setText(objJobDataProvider.getValue("log_level"));

        logLevel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("log_level", logLevel.getText());
            }
        });
        logLevel.setItems(new String[] { "info", "debug1", "debug2", "debug3", "debug4", "debug5", "debug6", "debug7", "debug8", "debug9", "" });

        // ------

        new Label(gOptionsGroup, SWT.NONE);

        final Label label_2 = new Label(gOptionsGroup, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
        gridData_4.heightHint = 8;
        label_2.setLayoutData(gridData_4);

        // -----
        final Label historyLabel = new Label(gOptionsGroup, SWT.NONE);
        historyLabel.setText(Messages.getLabel("history"));

        cboHistory = new Combo(gOptionsGroup, intComboBoxStyle);
        cboHistory.setText(objJobDataProvider.getHistory());
        cboHistory.setItems(new String[] { "yes", "no", "" });

        cboHistory.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setHistory(cboHistory.getText());
            }
        });
        GridData gd_cboHistory = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_cboHistory.minimumWidth = 150;
        cboHistory.setLayoutData(gd_cboHistory);

        new Label(gOptionsGroup, SWT.NONE);
        new Label(gOptionsGroup, SWT.NONE);
        // ----
        final Label historyOnProcessLabel = new Label(gOptionsGroup, SWT.NONE);
        historyOnProcessLabel.setText(Messages.getLabel("HistoryOnProcess"));

        cboHistoryOnProcess = new Combo(gOptionsGroup, intComboBoxStyle);
        cboHistoryOnProcess.setText(objJobDataProvider.getHistoryOnProcess());

        cboHistoryOnProcess.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                boolean isDigit = true;
                char[] c = cboHistoryOnProcess.getText().toCharArray();
                for (int i = 0; i < c.length; i++) {
                    isDigit = Character.isDigit(c[i]);
                    if (!isDigit)
                        break;
                }
                if (cboHistoryOnProcess.getText().equals("yes") || cboHistoryOnProcess.getText().equals("no") || isDigit)
                    objJobDataProvider.setHistoryOnProcess(cboHistoryOnProcess.getText());
            }
        });

        cboHistoryOnProcess.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });

        cboHistoryOnProcess.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                cboHistoryOnProcess.setText(objJobDataProvider.getValue("history_on_process"));
            }
        });

        GridData gd_cboHistoryOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_cboHistoryOnProcess.minimumWidth = 150;
        cboHistoryOnProcess.setLayoutData(gd_cboHistoryOnProcess);

        new Label(gOptionsGroup, SWT.NONE);
        new Label(gOptionsGroup, SWT.NONE);

        // -----
        final Label historyWithLogLabel = new Label(gOptionsGroup, SWT.NONE);
        historyWithLogLabel.setText(Messages.getLabel("HistoryWithLog"));

        cboHistoryWithLog = new Combo(gOptionsGroup, intComboBoxStyle);
        cboHistoryWithLog.setText(objJobDataProvider.getHistoryWithLog());
        cboHistoryWithLog.setItems(new String[] { "yes", "no", "gzip", "" });

        cboHistoryWithLog.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setHistoryWithLog(cboHistoryWithLog.getText());
            }
        });
        GridData gd_cboHistoryWithLog = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_cboHistoryWithLog.minimumWidth = 150;
        cboHistoryWithLog.setLayoutData(gd_cboHistoryWithLog);

        // ---
        cboHistoryOnProcess.setItems(new String[] { "0", "1", "2", "3", "4", "" });

        objParent.layout();
    }

    private void createMainOptionsGroup() {

        gMainOptionsGroup = new Group(objParent, SWT.NONE);
        final GridData gridDataGroup = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 4);
        gridDataGroup.heightHint = 100;
        gridDataGroup.minimumHeight = 30;
        gMainOptionsGroup.setLayoutData(gridDataGroup);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 4;
        gMainOptionsGroup.setLayout(gridLayout_2);

        gMainOptionsGroup.setText("");
        final GridData gridData_12 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_12.heightHint = 353;

        final Label ignore_signalLabel = new Label(gMainOptionsGroup, SWT.NONE);
        ignore_signalLabel.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        ignore_signalLabel.setText("Ignore Signals"); //TODO lang "Ignore Signals:"

        tIgnoreSignals = new Text(gMainOptionsGroup, SWT.BORDER);
        tIgnoreSignals.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                tIgnoreSignals.selectAll();
            }
        });
        tIgnoreSignals.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setIgnoreSignal(tIgnoreSignals.getText());
            }
        });
        //      gridData_3.widthHint = 48;
        tIgnoreSignals.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

        addButton = new Button(gMainOptionsGroup, SWT.NONE);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;
                if (tIgnoreSignals.getText().equals("")) {
                    tIgnoreSignals.setText(cSignals.getText());
                }
                else {
                    tIgnoreSignals.setText(tIgnoreSignals.getText() + " " + cSignals.getText());
                }
            }
        });
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        addButton.setText("<- Add <-"); //TODO lang "<- Add <-"

        cSignals = new Combo(gMainOptionsGroup, SWT.NONE);
        cSignals.setItems(new String[] { "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD",
                "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS." });
        cSignals.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

        label17 = new Label(gMainOptionsGroup, SWT.NONE);
        final GridData gridData_7 = new GridData(SWT.LEFT, GridData.CENTER, false, false);
        gridData_7.widthHint = 41;
        label17.setLayoutData(gridData_7);
        label17.setText("Priority"); //TODO lang "Priority:"

        sPriority = new Combo(gMainOptionsGroup, SWT.NONE);
        sPriority.setItems(new String[] { "idle", "below_normal", "normal", "above_normal", "high" });
        sPriority.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = (Utils.isOnlyDigits(e.text) || e.text.equals("idle") || e.text.equals("below_normal") || e.text.equals("normal") || e.text.equals("above_normal") || e.text.equals("high"));

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
                objJobDataProvider.setPriority(sPriority.getText());
            }
        });
        new Label(gMainOptionsGroup, SWT.NONE);
        new Label(gMainOptionsGroup, SWT.NONE);

        final Label visibleLabel = new Label(gMainOptionsGroup, SWT.NONE);
        visibleLabel.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        visibleLabel.setText("Visible"); //TODO lang "Visible:"

        comVisible = new Combo(gMainOptionsGroup, SWT.READ_ONLY);
        comVisible.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        comVisible.setItems(new String[] { "yes", "no", "never", "" });
        comVisible.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setVisible(comVisible.getText());
            }
        });
        new Label(gMainOptionsGroup, SWT.NONE);
        new Label(gMainOptionsGroup, SWT.NONE);
        //gridData_16.widthHint = 17;

        final Label minMaskLabel = new Label(gMainOptionsGroup, SWT.NONE);
        minMaskLabel.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        minMaskLabel.setText("Min Tasks"); //TODO lang "Min Tasks"

        tMintasks = new Text(gMainOptionsGroup, SWT.BORDER);
        tMintasks.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                tMintasks.selectAll();
            }
        });
        tMintasks.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        tMintasks.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setMintasks(tMintasks.getText());
            }
        });
        tMintasks.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        new Label(gMainOptionsGroup, SWT.NONE);
        new Label(gMainOptionsGroup, SWT.NONE);

        label15 = new Label(gMainOptionsGroup, SWT.NONE);
        label15.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        label15.setText("Tasks"); //TODO lang "Tasks:"

        sTasks = new Text(gMainOptionsGroup, SWT.BORDER);
        sTasks.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                sTasks.selectAll();
            }
        });
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
                objJobDataProvider.setTasks(sTasks.getText());
            }
        });
        new Label(gMainOptionsGroup, SWT.NONE);
        new Label(gMainOptionsGroup, SWT.NONE);
        label13 = new Label(gMainOptionsGroup, SWT.NONE);
        label13.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        label13.setText("Timeout"); //TODO lang "Timeout:"

        sTimeout = new Text(gMainOptionsGroup, SWT.BORDER);
        sTimeout.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                sTimeout.selectAll();
            }
        });
        sTimeout.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
            }
        });

        sTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setTimeout(sTimeout.getText());
            }
        });
        //gridData_9.widthHint = 75;
        sTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        final Label hhmmssLabel = new Label(gMainOptionsGroup, SWT.NONE);
        hhmmssLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        hhmmssLabel.setText("HH:MM:SS "); //TODO lang "HH:MM:SS "
        label11 = new Label(gMainOptionsGroup, SWT.NONE);
        label11.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        label11.setText("Idle Timeout"); //TODO lang "Idle Timeout:"

        sIdleTimeout = new Text(gMainOptionsGroup, SWT.BORDER);
        sIdleTimeout.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                sIdleTimeout.selectAll();
            }
        });
        sIdleTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        sIdleTimeout.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {

            }
        });
        sIdleTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setIdleTimeout(sIdleTimeout.getText());
            }
        });

        final Label hhmmssLabel_1 = new Label(gMainOptionsGroup, SWT.NONE);
        hhmmssLabel_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        hhmmssLabel_1.setText("HH:MM:SS or HH:MM or SS never"); //TODO lang "HH:MM:SS or HH:MM or SS never"

        final Label warnIfLongerLabel = new Label(gMainOptionsGroup, SWT.NONE);
        warnIfLongerLabel.setText("Warn if longer than"); //TODO lang "Warn if longer than:"

        txtWarnIfLongerThan = new Text(gMainOptionsGroup, SWT.BORDER);
        txtWarnIfLongerThan.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
            }
        });
        txtWarnIfLongerThan.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                txtWarnIfLongerThan.selectAll();
            }
        });
        txtWarnIfLongerThan.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setWarnIfLongerThan(txtWarnIfLongerThan.getText());
            }
        });
        txtWarnIfLongerThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label hhmmssLabel_1_1 = new Label(gMainOptionsGroup, SWT.NONE);
        hhmmssLabel_1_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        hhmmssLabel_1_1.setText("HH:MM:SS or Percentage"); //TODO lang "HH:MM:SS or Percentage"

        final Label warnIfShorterLabel = new Label(gMainOptionsGroup, SWT.NONE);
        warnIfShorterLabel.setText("Warn if shorter than"); //TODO lang "Warn if shorter than:"

        txtWarnIfShorterThan = new Text(gMainOptionsGroup, SWT.BORDER);
        txtWarnIfShorterThan.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                txtWarnIfShorterThan.selectAll();
            }
        });
        txtWarnIfShorterThan.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                objJobDataProvider.setWarnIfShorterThan(txtWarnIfShorterThan.getText());
            }
        });
        txtWarnIfShorterThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label hhmmssOrPercentageLabel = new Label(gMainOptionsGroup, SWT.NONE);
        hhmmssOrPercentageLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        hhmmssOrPercentageLabel.setText("HH:MM:SS or Percentage"); //TODO lang "HH:MM:SS or Percentage"

        final Label force_idle_timeoutLabel = new Label(gMainOptionsGroup, SWT.NONE);
        force_idle_timeoutLabel.setLayoutData(new GridData(SWT.LEFT, GridData.CENTER, false, false));
        force_idle_timeoutLabel.setText("Force Idle Timeout"); //TODO lang "Force Idle Timeout"

        bForceIdletimeout = new Button(gMainOptionsGroup, SWT.CHECK);
        bForceIdletimeout.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
        bForceIdletimeout.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;
                objJobDataProvider.setForceIdletimeout(bForceIdletimeout.getSelection());
            }
        });

        objParent.layout();
    }

    public void initForm() {

        bOrderYes.setSelection(objJobDataProvider.getOrder());
        bOrderNo.setSelection(!objJobDataProvider.getOrder());
        bStopOnError.setSelection(objJobDataProvider.getStopOnError());

        logLevel.setText(objJobDataProvider.getValue("log_level"));

        cboHistory.setText(objJobDataProvider.getValue("history"));
        cboHistoryOnProcess.setText(objJobDataProvider.getValue("history_on_process"));
        cboHistoryWithLog.setText(objJobDataProvider.getValue("history_with_log"));

        int index = 0;
        bForceIdletimeout.setSelection(objJobDataProvider.getForceIdletimeout());
        index = sPriority.indexOf(objJobDataProvider.getPriority());
        if (index >= 0)
            sPriority.select(index);
        else {
            int p = Utils.str2int(objJobDataProvider.getPriority(), 20);
            if (p == -999) {
                sPriority.setText("");
            }
            else {
                if (p < -20) {
                    p = -20;
                }
                sPriority.setText(String.valueOf(p));
            }
        }

        sTasks.setText(objJobDataProvider.getTasks());
        if (objJobDataProvider.getMintasks() != null)
            tMintasks.setText(objJobDataProvider.getMintasks());
        if (objJobDataProvider.getPriority() != null)
            sPriority.setText(objJobDataProvider.getPriority());
        tIgnoreSignals.setText(objJobDataProvider.getIgnoreSignal());
        sTimeout.setText(objJobDataProvider.getTimeout());
        sIdleTimeout.setText(objJobDataProvider.getIdleTimeout());

        comVisible.setText(objJobDataProvider.getVisible());

        txtWarnIfLongerThan.setText(objJobDataProvider.getWarnIfLongerThan());
        txtWarnIfShorterThan.setText(objJobDataProvider.getWarnIfShorterThan());
        bForceIdletimeout.setSelection(objJobDataProvider.getForceIdletimeout());

    }

    public Button getbOrderYes() {
        return bOrderYes;
    }

    public Button getbOrderNo() {
        return bOrderNo;
    }

}
