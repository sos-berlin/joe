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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.DateListener;
import sos.scheduler.editor.conf.listeners.DaysListener;
import sos.scheduler.editor.conf.listeners.PeriodListener;
import sos.scheduler.editor.conf.listeners.PeriodsListener;

import com.sos.dialog.components.SOSDateTime;
import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

/** @author mo */
public class JobAssistentRunTimeForms extends JobWizardBaseForm {

    private static final String EVERY_DAY = SOSJOEMessageCodes.JOE_M_JobAssistent_EveryDay.label();
    private static final String SPECIFIC_DAY = SOSJOEMessageCodes.JOE_M_JobAssistent_SpecificDay.label();
    private static final String WEEK_DAY = SOSJOEMessageCodes.JOE_M_JobAssistent_Weekday.label();
    private static final String MONTH_DAY = SOSJOEMessageCodes.JOE_M_JobAssistent_Monthday.label();
    private Shell runTimeSingleShell = null;
    private Combo comboMonth = null;
    private SOSDateTime txtSpeDay = null;
    private Combo comboEveryWeekdays = null;
    private Button optEveryDay = null;
    private Button optEveryWeeksdays = null;
    private Button optEveryMonths = null;
    private Button optSpecificDay = null;
    private List list = null;
    private Button butAdd = null;
    private Button butRemove = null;
    private Text txtDayAtHour = null;
    private Text txtDayAtMinutes = null;
    private Text txtDayAtSecound = null;
    private Text txtSpeDayHour = null;
    private Text txtSpeDayAtMinutes = null;
    private Text txtSpeDayAtSecound = null;
    private Text txtWeekAtHour = null;
    private Text txtWeekAtMinutes = null;
    private Text txtWeekAtSecound = null;
    private Text txtMonthAtHour = null;
    private Text txtMonthAtMinutes = null;
    private Text txtMonthAtSecound = null;
    private PeriodsListener periodslistener = null;
    private PeriodListener periodlistener = null;
    private DaysListener weekDayListener = null;
    private DaysListener monthListener = null;
    private DateListener speDateListener = null;
    private Element jobBackUp = null;
    private boolean closeDialog = false;

    public JobAssistentRunTimeForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        update = update_;
        job = job_;
        jobBackUp = (Element) job_.clone();
        init();
    }

    private void init() {
        periodslistener = new PeriodsListener(dom, job.getChild("run_time"));
        periodlistener = new PeriodListener(dom);
        speDateListener = new DateListener(dom, job.getChild("run_time"), 1);
        weekDayListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.WEEKDAYS, false);
        monthListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.MONTHDAYS, false);
    }

    public void showRunTimeForms() {
        try {
            runTimeSingleShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
            runTimeSingleShell.addShellListener(new ShellAdapter() {

                @Override
                public void shellClosed(final ShellEvent e) {
                    if (!closeDialog) {
                        close();
                    }
                    e.doit = runTimeSingleShell.isDisposed();
                }
            });
            runTimeSingleShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
            final GridLayout gridLayout = new GridLayout();
            gridLayout.marginTop = 5;
            gridLayout.marginRight = 5;
            gridLayout.marginLeft = 5;
            gridLayout.marginBottom = 5;
            gridLayout.numColumns = 2;
            runTimeSingleShell.setLayout(gridLayout);
            runTimeSingleShell.setSize(553, 489);
            runTimeSingleShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_RunTimeSingleStarts.label());
            final Group jobGroup = SOSJOEMessageCodes.JOE_G_JobAssistent_JobGroup.Control(new Group(runTimeSingleShell, SWT.NONE));
            final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, true, 2, 1);
            gridData_1.heightHint = 390;
            gridData_1.widthHint = 517;
            jobGroup.setLayoutData(gridData_1);
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.numColumns = 9;
            gridLayout_1.marginWidth = 10;
            gridLayout_1.marginTop = 10;
            gridLayout_1.marginRight = 10;
            gridLayout_1.marginLeft = 10;
            gridLayout_1.marginHeight = 10;
            gridLayout_1.marginBottom = 10;
            jobGroup.setLayout(gridLayout_1);
            optEveryDay = SOSJOEMessageCodes.JOE_B_JobAssistent_EveryDay.Control(new Button(jobGroup, SWT.CHECK));
            optEveryDay.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (optEveryDay.getSelection()) {
                        txtDayAtHour.setEnabled(true);
                        txtDayAtMinutes.setEnabled(true);
                        txtDayAtSecound.setEnabled(true);
                        butAdd.setEnabled(true);
                    } else {
                        txtDayAtHour.setEnabled(false);
                        txtDayAtMinutes.setEnabled(false);
                        txtDayAtSecound.setEnabled(false);
                    }
                }
            });
            new Label(jobGroup, SWT.NONE);
            final Label atLabel = SOSJOEMessageCodes.JOE_L_JobAssistent_At.Control(new Label(jobGroup, SWT.NONE));
            atLabel.setAlignment(SWT.RIGHT);
            final GridData gridData_15 = new GridData(GridData.END, GridData.CENTER, false, false);
            gridData_15.widthHint = 28;
            atLabel.setLayoutData(gridData_15);
            txtDayAtHour = SOSJOEMessageCodes.JOE_T_JobAssistent_AtHour.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtDayAtHour.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtDayAtHour.getText(), "hour")) {
                        txtDayAtHour.setBackground(Options.getRequiredColor());
                        txtDayAtHour.setFocus();
                    } else {
                        txtDayAtHour.setBackground(null);
                    }
                }
            });
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData.minimumWidth = 25;
            txtDayAtHour.setLayoutData(gridData);
            final Label label = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label.setLayoutData(new GridData());
            txtDayAtMinutes = SOSJOEMessageCodes.JOE_T_JobAssistent_AtMinute.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtDayAtMinutes.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtDayAtMinutes.getText(), "minutes")) {
                        txtDayAtMinutes.setBackground(Options.getRequiredColor());
                        txtDayAtMinutes.setFocus();
                    } else {
                        txtDayAtMinutes.setBackground(null);
                    }
                }
            });
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_3.widthHint = 12;
            gridData_3.minimumWidth = 25;
            txtDayAtMinutes.setLayoutData(gridData_3);
            final Label label_1 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_1.setLayoutData(new GridData());
            txtDayAtSecound = SOSJOEMessageCodes.JOE_T_JobAssistent_AtSecond.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtDayAtSecound.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtDayAtSecound.getText(), "secound")) {
                        txtDayAtSecound.setBackground(Options.getRequiredColor());
                        txtDayAtSecound.setFocus();
                    } else {
                        txtDayAtSecound.setBackground(null);
                    }
                }
            });
            final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_4.minimumWidth = 25;
            txtDayAtSecound.setLayoutData(gridData_4);
            final Label hhmmssLabel = SOSJOEMessageCodes.JOE_L_JobAssistent_TimeFormat.Control(new Label(jobGroup, SWT.NONE));
            hhmmssLabel.setLayoutData(new GridData());
            optSpecificDay = SOSJOEMessageCodes.JOE_B_JobAssistent_SpecificDay.Control(new Button(jobGroup, SWT.CHECK));
            optSpecificDay.setLayoutData(new GridData());
            optSpecificDay.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (optSpecificDay.getSelection()) {
                        txtSpeDay.setEnabled(true);
                        txtSpeDayHour.setEnabled(true);
                        txtSpeDayAtMinutes.setEnabled(true);
                        txtSpeDayAtSecound.setEnabled(true);
                        butAdd.setEnabled(true);
                    } else {
                        txtSpeDay.setEnabled(false);
                        txtSpeDayHour.setEnabled(false);
                        txtSpeDayAtMinutes.setEnabled(false);
                        txtSpeDayAtSecound.setEnabled(false);
                    }
                }
            });
            txtSpeDay = SOSJOEMessageCodes.JOE_JobAssistent_SpecificDayDateTime.Control(new SOSDateTime(jobGroup, SWT.NONE));
            final GridData gridData_16 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_16.widthHint = 131;
            txtSpeDay.setLayoutData(gridData_16);
            txtSpeDay.setEnabled(false);
            final Label atLabel_1 = SOSJOEMessageCodes.JOE_L_JobAssistent_At.Control(new Label(jobGroup, SWT.NONE));
            atLabel_1.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            txtSpeDayHour = SOSJOEMessageCodes.JOE_T_JobAssistent_AtHour.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtSpeDayHour.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtSpeDayHour.getText(), "hour")) {
                        txtSpeDayHour.setBackground(Options.getRequiredColor());
                        txtSpeDayHour.setFocus();
                    } else {
                        txtSpeDayHour.setBackground(null);
                    }
                }
            });
            final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_5.minimumWidth = 25;
            gridData_5.widthHint = 0;
            txtSpeDayHour.setLayoutData(gridData_5);
            final Label label_2 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_2.setLayoutData(new GridData());
            txtSpeDayAtMinutes = SOSJOEMessageCodes.JOE_T_JobAssistent_AtMinute.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtSpeDayAtMinutes.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtSpeDayHour.getText(), "minutes")) {
                        txtSpeDayAtMinutes.setBackground(Options.getRequiredColor());
                        txtSpeDayAtMinutes.setFocus();
                    } else {
                        txtSpeDayAtMinutes.setBackground(null);
                    }
                }
            });
            final GridData gridData_3_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_3_1.widthHint = 7;
            gridData_3_1.minimumWidth = 25;
            txtSpeDayAtMinutes.setLayoutData(gridData_3_1);
            final Label label_1_1 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_1_1.setLayoutData(new GridData());
            txtSpeDayAtSecound = SOSJOEMessageCodes.JOE_T_JobAssistent_AtSecond.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtSpeDayAtSecound.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtSpeDayAtSecound.getText(), "secound")) {
                        txtSpeDayAtSecound.setBackground(Options.getRequiredColor());
                        txtSpeDayAtSecound.setFocus();
                    } else {
                        txtSpeDayAtSecound.setBackground(null);
                    }
                }
            });
            final GridData gridData_4_1 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_4_1.minimumWidth = 25;
            txtSpeDayAtSecound.setLayoutData(gridData_4_1);
            final Label hhmmssLabel_1 = SOSJOEMessageCodes.JOE_L_JobAssistent_TimeFormat.Control(new Label(jobGroup, SWT.NONE));
            hhmmssLabel_1.setLayoutData(new GridData());
            optEveryWeeksdays = SOSJOEMessageCodes.JOE_B_JobAssistent_WeekDay.Control(new Button(jobGroup, SWT.CHECK));
            optEveryWeeksdays.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (optEveryWeeksdays.getSelection()) {
                        butAdd.setEnabled(true);
                        comboEveryWeekdays.setEnabled(true);
                        txtWeekAtHour.setEnabled(true);
                        txtWeekAtMinutes.setEnabled(true);
                        txtWeekAtSecound.setEnabled(true);
                    } else {
                        comboEveryWeekdays.setEnabled(false);
                        txtWeekAtHour.setEnabled(false);
                        txtWeekAtMinutes.setEnabled(false);
                        txtWeekAtSecound.setEnabled(false);
                    }
                }
            });
            comboEveryWeekdays = SOSJOEMessageCodes.JOE_Cbo_JobAssistent_WeekDayCombo.Control(new Combo(jobGroup, SWT.NONE));
            comboEveryWeekdays.setItems(DaysListener.getWeekdays());
            comboEveryWeekdays.select(0);
            comboEveryWeekdays.setEnabled(false);
            final GridData gridData_17 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_17.widthHint = 148;
            comboEveryWeekdays.setLayoutData(gridData_17);
            final Label atLabel_2 = SOSJOEMessageCodes.JOE_L_JobAssistent_At.Control(new Label(jobGroup, SWT.NONE));
            atLabel_2.setLayoutData(new GridData(37, SWT.DEFAULT));
            atLabel_2.setAlignment(SWT.RIGHT);
            txtWeekAtHour = SOSJOEMessageCodes.JOE_T_JobAssistent_AtHour.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtWeekAtHour.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtWeekAtHour.getText(), "hour")) {
                        txtWeekAtHour.setBackground(Options.getRequiredColor());
                        txtWeekAtHour.setFocus();
                    } else {
                        txtWeekAtHour.setBackground(null);
                    }
                }
            });
            final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_6.minimumWidth = 25;
            gridData_6.widthHint = 0;
            txtWeekAtHour.setLayoutData(gridData_6);
            final Label label_3 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_3.setLayoutData(new GridData());
            txtWeekAtMinutes = SOSJOEMessageCodes.JOE_T_JobAssistent_AtMinute.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtWeekAtMinutes.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtWeekAtMinutes.getText(), "minutes")) {
                        txtWeekAtMinutes.setBackground(Options.getRequiredColor());
                        txtWeekAtMinutes.setFocus();
                    } else {
                        txtWeekAtMinutes.setBackground(null);
                    }
                }
            });
            final GridData gridData_11 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_11.widthHint = 5;
            gridData_11.minimumWidth = 25;
            final GridData gridData_9 = new GridData(11, SWT.DEFAULT);
            gridData_9.minimumWidth = 25;
            final GridData gridData_3_2 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_3_2.minimumWidth = 25;
            txtWeekAtMinutes.setLayoutData(gridData_3_2);
            final Label label_1_2 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_1_2.setLayoutData(new GridData());
            txtWeekAtSecound = SOSJOEMessageCodes.JOE_T_JobAssistent_AtSecond.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtWeekAtSecound.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtWeekAtSecound.getText(), "secound")) {
                        txtWeekAtSecound.setBackground(Options.getRequiredColor());
                        txtWeekAtSecound.setFocus();
                    } else {
                        txtWeekAtSecound.setBackground(null);
                    }
                }
            });
            final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_12.widthHint = 2;
            gridData_12.minimumWidth = 25;
            final GridData gridData_10 = new GridData(10, SWT.DEFAULT);
            gridData_10.minimumHeight = 25;
            final GridData gridData_4_2 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_4_2.minimumWidth = 25;
            txtWeekAtSecound.setLayoutData(gridData_4_2);
            final Label hhmmssLabel_2 = SOSJOEMessageCodes.JOE_L_JobAssistent_TimeFormat.Control(new Label(jobGroup, SWT.NONE));
            hhmmssLabel_2.setLayoutData(new GridData());
            optEveryMonths = SOSJOEMessageCodes.JOE_B_JobAssistent_MonthDay.Control(new Button(jobGroup, SWT.CHECK));
            optEveryMonths.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (optEveryMonths.getSelection()) {
                        butAdd.setEnabled(true);
                        comboMonth.setEnabled(true);
                        txtMonthAtHour.setEnabled(true);
                        txtMonthAtMinutes.setEnabled(true);
                        txtMonthAtSecound.setEnabled(true);
                    } else {
                        comboMonth.setEnabled(false);
                        txtMonthAtHour.setEnabled(false);
                        txtMonthAtMinutes.setEnabled(false);
                        txtMonthAtSecound.setEnabled(false);
                    }
                }
            });
            comboMonth = SOSJOEMessageCodes.JOE_Cbo_JobAssistent_MonthCombo.Control(new Combo(jobGroup, SWT.NONE));
            comboMonth.setItems(DaysListener.getMonthdays());
            comboMonth.select(0);
            comboMonth.setEnabled(false);
            comboMonth.select(0);
            comboMonth.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            final Label atLabel_3 = SOSJOEMessageCodes.JOE_L_JobAssistent_At.Control(new Label(jobGroup, SWT.NONE));
            atLabel_3.setLayoutData(new GridData(37, SWT.DEFAULT));
            atLabel_3.setAlignment(SWT.RIGHT);
            txtMonthAtHour = SOSJOEMessageCodes.JOE_T_JobAssistent_AtHour.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtMonthAtHour.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtMonthAtHour.getText(), "hour")) {
                        txtMonthAtHour.setBackground(Options.getRequiredColor());
                        txtMonthAtHour.setFocus();
                    } else {
                        txtMonthAtHour.setBackground(null);
                    }
                }
            });
            final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_13.widthHint = 11;
            final GridData gridData_8 = new GridData(13, SWT.DEFAULT);
            gridData_8.minimumWidth = 25;
            final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_7.minimumWidth = 25;
            gridData_7.widthHint = 0;
            txtMonthAtHour.setLayoutData(gridData_7);
            final Label label_4 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_4.setLayoutData(new GridData());
            txtMonthAtMinutes = SOSJOEMessageCodes.JOE_T_JobAssistent_AtMinute.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtMonthAtMinutes.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtMonthAtMinutes.getText(), "minutes")) {
                        txtMonthAtMinutes.setBackground(Options.getRequiredColor());
                        txtMonthAtMinutes.setFocus();
                    } else {
                        txtMonthAtMinutes.setBackground(null);
                    }
                }
            });
            final GridData gridData_14 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_14.widthHint = 15;
            gridData_14.minimumHeight = 25;
            final GridData gridData_3_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_3_3.minimumWidth = 25;
            txtMonthAtMinutes.setLayoutData(gridData_3_3);
            final Label label_1_3 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(jobGroup, SWT.NONE));
            label_1_3.setLayoutData(new GridData());
            txtMonthAtSecound = SOSJOEMessageCodes.JOE_T_JobAssistent_AtSecond.Control(new Text(jobGroup, SWT.CENTER | SWT.BORDER));
            txtMonthAtSecound.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!checkTime(txtMonthAtSecound.getText(), "minutes")) {
                        txtMonthAtSecound.setBackground(Options.getRequiredColor());
                        txtMonthAtSecound.setFocus();
                    } else {
                        txtMonthAtSecound.setBackground(null);
                    }
                }
            });
            final GridData gridData_4_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
            gridData_4_3.minimumWidth = 25;
            txtMonthAtSecound.setLayoutData(gridData_4_3);
            final Label hhmmssLabel_3 = SOSJOEMessageCodes.JOE_L_JobAssistent_TimeFormat.Control(new Label(jobGroup, SWT.NONE));
            hhmmssLabel_3.setLayoutData(new GridData());
            list = new List(jobGroup, SWT.BORDER);
            list.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (list.getSelectionIndex() > 0) {
                        butRemove.setEnabled(true);
                    }
                }
            });
            final GridData newGridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 8, 2);
            list.setLayoutData(newGridData_2);
            butAdd = SOSJOEMessageCodes.JOE_B_JobAssistentRunTimeForms_Add.Control(new Button(jobGroup, SWT.NONE));
            butAdd.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    addPeriod();
                }
            });
            butAdd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butRemove = SOSJOEMessageCodes.JOE_B_JobAssistentRunTimeForms_Remove.Control(new Button(jobGroup, SWT.NONE));
            butRemove.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (list.getSelectionCount() > 0) {
                        delete();
                        list.remove(list.getSelectionIndex());
                    }
                }
            });
            butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Close.Control(new Button(runTimeSingleShell, SWT.NONE));
            butCancel.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    close();
                }
            });
            final Composite composite = new Composite(runTimeSingleShell, SWT.NONE);
            final GridData newGridData = new GridData(GridData.END, GridData.FILL, false, false);
            newGridData.heightHint = 29;
            composite.setLayoutData(newGridData);
            final GridLayout newGridLayout_1 = new GridLayout();
            newGridLayout_1.numColumns = 3;
            composite.setLayout(newGridLayout_1);
            butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite, SWT.NONE));
            butShow.setVisible(false);
            butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            butShow.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    ErrorLog.message(runTimeSingleShell, Utils.getElementAsString(job), SWT.OK);
                }
            });
            Utils.createHelpButton(composite, "JOE_M_JobAssistentRunTimeForms_Help.label", runTimeSingleShell);
            butNext = SOSJOEMessageCodes.JOE_B_JobAssistentRunTimeForms_Apply.Control(new Button(composite, SWT.NONE));
            butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
            final GridData newGridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
            newGridData_1.widthHint = 57;
            butNext.setLayoutData(newGridData_1);
            butNext.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    closeDialog = true;
                    runTimeSingleShell.dispose();
                }
            });
            setEnabled(false);
            setToolTipText();
            fillList();
            java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            runTimeSingleShell.setBounds((screen.width - runTimeSingleShell.getBounds().width) / 2, (screen.height - runTimeSingleShell.getBounds().height) / 2, runTimeSingleShell.getBounds().width, runTimeSingleShell.getBounds().height);
            runTimeSingleShell.open();
            runTimeSingleShell.layout();
            runTimeSingleShell.pack();
        } catch (Exception e) {
            try {
                new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            } catch (Exception ee) {
                // do nothing
            }
            System.err.println(SOSJOEMessageCodes.JOE_E_0002.params("JobAssistentRunTimeForms.showRunTimeForms() ") + e.getMessage());
        }
    }

    private void setEnabled(boolean enabled) {
        optEveryDay.setSelection(false);
        optSpecificDay.setSelection(false);
        optEveryWeeksdays.setSelection(false);
        optEveryMonths.setSelection(false);
        comboEveryWeekdays.setText("");
        comboMonth.setText("");
        txtSpeDay.setEnabled(false);
        comboEveryWeekdays.setEnabled(false);
        comboMonth.setEnabled(false);
        txtDayAtHour.setEnabled(false);
        txtDayAtMinutes.setEnabled(false);
        txtDayAtSecound.setEnabled(false);
        txtSpeDay.setEnabled(false);
        txtSpeDayHour.setEnabled(false);
        txtSpeDayAtMinutes.setEnabled(false);
        txtSpeDayAtSecound.setEnabled(false);
        comboEveryWeekdays.setEnabled(false);
        txtWeekAtHour.setEnabled(false);
        txtWeekAtMinutes.setEnabled(false);
        txtWeekAtSecound.setEnabled(false);
        comboMonth.setEnabled(false);
        txtMonthAtHour.setEnabled(false);
        txtMonthAtMinutes.setEnabled(false);
        txtMonthAtSecound.setEnabled(false);
        txtDayAtHour.setText("");
        txtDayAtMinutes.setText("");
        txtDayAtSecound.setText("");
        txtSpeDayHour.setText("");
        txtSpeDayAtMinutes.setText("");
        txtSpeDayAtSecound.setText("");
        txtWeekAtHour.setText("");
        txtWeekAtMinutes.setText("");
        txtWeekAtSecound.setText("");
        txtMonthAtHour.setText("");
        txtMonthAtMinutes.setText("");
        txtMonthAtSecound.setText("");
    }

    public void setToolTipText() {
    }

    private void addPeriod() {
        if (optEveryDay.getSelection()
                && !txtDayAtHour.getText().concat(txtDayAtMinutes.getText()).concat(txtDayAtSecound.getText()).trim().isEmpty()) {
            String str = EVERY_DAY + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At.label() + " "
                    + Utils.getTime(23, txtDayAtHour.getText(), txtDayAtMinutes.getText(), txtDayAtSecound.getText(), false);
            if (!periodExist(str)) {
                savePeriod(EVERY_DAY);
                list.add(str);
            }
        }
        if (optSpecificDay.getSelection() && txtSpeDay.getISODate() != null && !txtSpeDay.getISODate().trim().isEmpty()) {
            savePeriod(SPECIFIC_DAY);
            list.add(SPECIFIC_DAY + txtSpeDay.getISODate() + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At.label() + " "
                    + Utils.getTime(23, txtSpeDayHour.getText(), txtSpeDayAtMinutes.getText(), txtSpeDayAtSecound.getText(), false));
        }
        if (optEveryWeeksdays.getSelection() && comboEveryWeekdays.getText() != null && !comboEveryWeekdays.getText().trim().isEmpty()) {
            savePeriod(WEEK_DAY);
            list.add(WEEK_DAY + comboEveryWeekdays.getText() + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At.label() + " "
                    + Utils.getTime(23, txtWeekAtHour.getText(), txtWeekAtMinutes.getText(), txtWeekAtSecound.getText(), false));
        }
        if (optEveryMonths.getSelection() && comboMonth.getText() != null && !comboMonth.getText().trim().isEmpty()) {
            savePeriod(MONTH_DAY);
            list.add(MONTH_DAY + comboMonth.getText() + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At.label() + " "
                    + Utils.getTime(23, txtMonthAtHour.getText(), txtMonthAtMinutes.getText(), txtMonthAtSecound.getText(), false));
        }
        setEnabled(false);
    }

    private boolean periodExist(String str) {
        for (int i = 0; i < list.getItemCount(); i++) {
            String currStr = list.getItem(i);
            if (currStr.equalsIgnoreCase(str)) {
                ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_PeriodExists.label(), SWT.ICON_WARNING | SWT.OK
                        | SWT.CANCEL);
                return true;
            }
        }
        return false;
    }

    private void fillList() {
        Element run_time = job.getChild("run_time");
        if (run_time == null) {
            return;
        }
        java.util.List everyDay = periodslistener.get_list();
        for (int i = 0; everyDay != null && i < everyDay.size(); i++) {
            Element period = (Element) everyDay.get(i);
            PeriodListener p = new PeriodListener(dom);
            p.setPeriod(period);
            if (p.getBegin() == null || p.getBegin().trim().isEmpty()) {
                list.add(EVERY_DAY + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At.label() + " " + p.getSingle());
            }
        }
        java.util.List speDays = speDateListener.get_list();
        for (int i = 0; speDays != null && i < speDays.size(); i++) {
            Element speElem = (Element) speDays.get(i);
            int[] da = speDateListener.getDate(i);
            java.util.List periods = speElem.getChildren("period");
            for (int j = 0; periods != null && j < periods.size(); j++) {
                Element period = (Element) periods.get(j);
                PeriodListener p = new PeriodListener(dom);
                p.setPeriod(period);
                if (p.getBegin() == null || p.getBegin().trim().isEmpty()) {
                    list.add(SOSJOEMessageCodes.JOE_M_0029.params(SPECIFIC_DAY, Utils.asStr(da[2]), Utils.asStr(da[1]), Utils.asStr(da[0]), p.getSingle()));
                }
            }
        }
        Element[] weekDays = weekDayListener.getDayElements();
        for (int i = 0; weekDays != null && i < weekDays.length; i++) {
            Element elWeek = weekDays[i];
            String sWeek = comboEveryWeekdays.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) - 1);
            java.util.List periods = elWeek.getChildren("period");
            for (int j = 0; periods != null && j < periods.size(); j++) {
                Element period = (Element) periods.get(j);
                PeriodListener p = new PeriodListener(dom);
                p.setPeriod(period);
                if (p.getBegin() == null || p.getBegin().trim().isEmpty()) {
                    list.add(SOSJOEMessageCodes.JOE_M_0031.params(WEEK_DAY, sWeek, p.getSingle()));
                }
            }
        }
        Element[] monthDays = monthListener.getDayElements();
        for (int i = 0; monthDays != null && i < monthDays.length; i++) {
            Element elMonth = monthDays[i];
            String sMonth = comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", elMonth)) - 1);
            java.util.List periods = elMonth.getChildren("period");
            for (int j = 0; periods != null && j < periods.size(); j++) {
                Element period = (Element) periods.get(j);
                PeriodListener p = new PeriodListener(dom);
                p.setPeriod(period);
                if (p.getBegin() == null || p.getBegin().trim().isEmpty()) {
                    list.add(SOSJOEMessageCodes.JOE_M_0031.params(MONTH_DAY, sMonth, p.getSingle()));
                }
            }
        }
    }

    private void delete() {
        String selectedStr = list.getItem(list.getSelectionIndex());
        if (selectedStr.startsWith(EVERY_DAY)) {
            deleteEveryDay(selectedStr);
        } else if (selectedStr.startsWith(SPECIFIC_DAY)) {
            deleteSpeDay(selectedStr);
        } else if (selectedStr.startsWith(WEEK_DAY)) {
            deleteWeek(selectedStr);
        } else if (selectedStr.startsWith(MONTH_DAY)) {
            deleteMonth(selectedStr);
        }
    }

    private void deleteEveryDay(String selectedStr) {
        java.util.List everyDay = periodslistener.get_list();
        for (int i = 0; everyDay != null && i < everyDay.size(); i++) {
            Element period = (Element) everyDay.get(i);
            PeriodListener p = new PeriodListener(dom);
            p.setPeriod(period);
            if (selectedStr.equals(EVERY_DAY + " " + SOSJOEMessageCodes.JOE_L_JobAssistent_At + " " + p.getSingle())) {
                periodslistener.removePeriod(i);
            }
        }
    }

    private void deleteSpeDay(String selectedStr) {
        java.util.List speDays = speDateListener.get_list();
        for (int i = 0; speDays != null && i < speDays.size(); i++) {
            Element speElem = (Element) speDays.get(i);
            int[] da = speDateListener.getDate(i);
            java.util.List periods = speElem.getChildren("period");
            if (periods == null || periods.isEmpty()) {
                speDateListener.removeDate(i);
            } else {
                for (int j = 0; periods != null && j < periods.size(); j++) {
                    Element period = (Element) periods.get(j);
                    PeriodListener p = new PeriodListener(dom);
                    p.setPeriod(period);
                    if (selectedStr.equals(SOSJOEMessageCodes.JOE_M_0029.params(SPECIFIC_DAY, Utils.asStr(da[2]), Utils.asStr(da[1]), Utils.asStr(da[0]), p.getSingle()))) {
                        PeriodsListener _pl = new PeriodsListener(dom, speElem);
                        _pl.removePeriod(j);
                    }
                }
            }
        }
        update.updateDays(DaysListener.SPECIFIC_DAY);
    }

    private void deleteWeek(String selectedStr) {
        Element[] weekDays = weekDayListener.getDayElements();
        for (int i = 0; weekDays != null && i < weekDays.length; i++) {
            Element elWeek = weekDays[i];
            String sWeek = comboEveryWeekdays.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) - 1);
            if (selectedStr.indexOf(sWeek) > -1) {
                java.util.List periods = elWeek.getChildren("period");
                if (periods == null || periods.isEmpty()) {
                    weekDayListener.deleteDay(sWeek);
                } else {
                    for (int j = 0; periods != null && j < periods.size(); j++) {
                        Element period = (Element) periods.get(j);
                        PeriodListener p = new PeriodListener(dom);
                        p.setPeriod(period);
                        String time = selectedStr.substring(selectedStr.indexOf("at ") + 3);
                        if (p.getSingle().endsWith(time)) {
                            PeriodsListener _pl = new PeriodsListener(dom, elWeek);
                            _pl.removePeriod(j);
                        }
                    }
                }
            }
        }
        update.updateDays(DaysListener.WEEKDAYS);
    }

    private void deleteMonth(String selectedStr) {
        Element[] monthDays = monthListener.getDayElements();
        for (int i = 0; monthDays != null && i < monthDays.length; i++) {
            Element elMonth = monthDays[i];
            String sMonth = comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", elMonth)) - 1);
            if (selectedStr.indexOf(sMonth) > -1) {
                java.util.List periods = elMonth.getChildren("period");
                if (periods == null || periods.isEmpty()) {
                    monthListener.deleteDay(sMonth);
                } else {
                    for (int j = 0; periods != null && j < periods.size(); j++) {
                        Element period = (Element) periods.get(j);
                        PeriodListener p = new PeriodListener(dom);
                        p.setPeriod(period);
                        String time = selectedStr.substring(selectedStr.indexOf("at ") + 3);
                        if (p.getSingle().endsWith(time)) {
                            PeriodsListener _pl = new PeriodsListener(dom, elMonth);
                            _pl.removePeriod(j);
                        }
                    }
                }
            }
        }
        update.updateDays(DaysListener.MONTHDAYS);
    }

    private void savePeriod(String which) {
        if (which.equals(EVERY_DAY)) {
            Element period = periodslistener.getNewPeriod();
            periodlistener.setPeriod(period);
            periodlistener.setPeriodTime(23, null, "single_start", txtDayAtHour.getText(), txtDayAtMinutes.getText(), txtDayAtSecound.getText());
            periodslistener.applyPeriod(period);
        }
        if (which.equals(SPECIFIC_DAY)) {
            String date = txtSpeDay.getISODate();
            String[] tdate = date.split("-");
            if (!speDateListener.exists(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0]))) {
                speDateListener.addDate(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0]));
            }
            java.util.List lastDate = job.getChild("run_time").getChildren("date");
            Element eDate = (Element) lastDate.get(lastDate.size() - 1);
            PeriodsListener p = new PeriodsListener(dom, eDate);
            Element period = p.getNewPeriod();
            periodlistener.setPeriod(period);
            periodlistener.setPeriodTime(23, null, "single_start", txtSpeDayHour.getText(), txtSpeDayAtMinutes.getText(), txtSpeDayAtSecound.getText());
            p.applyPeriod(period);
            if (update != null) {
                update.updateDays(DaysListener.SPECIFIC_DAY);
            }
        }
        if (which.equals(WEEK_DAY)) {
            String week = comboEveryWeekdays.getText();
            Element day = null;
            Element[] days = weekDayListener.getDayElements();
            if (days != null && days.length > 0) {
                for (int i = 0; i < days.length; i++) {
                    Element eday = days[i];
                    if (Utils.str2int(Utils.getAttributeValue("day", eday)) == (comboEveryWeekdays.getSelectionIndex() + 1)) {
                        day = eday;
                        break;
                    }
                }
            }
            if (day == null) {
                weekDayListener.addDay(week);
                day = weekDayListener.getDayElements()[weekDayListener.getDayElements().length - 1];
            }
            PeriodsListener p = new PeriodsListener(dom, day);
            Element period = p.getNewPeriod();
            periodlistener.setPeriod(period);
            periodlistener.setPeriodTime(23, null, "single_start", txtWeekAtHour.getText(), txtWeekAtMinutes.getText(), txtWeekAtSecound.getText());
            p.applyPeriod(period);
            update.updateDays(DaysListener.WEEKDAYS);
        }
        if (which.equals(MONTH_DAY)) {
            String month = comboMonth.getText();
            Element day = null;
            Element[] days = monthListener.getDayElements();
            if (days != null && days.length > 0) {
                for (int i = 0; i < days.length; i++) {
                    Element eday = days[i];
                    if (Utils.str2int(Utils.getAttributeValue("day", eday)) == (comboMonth.getSelectionIndex() + 1)) {
                        day = eday;
                        break;
                    }
                }
            }
            if (day == null) {
                monthListener.addDay(month);
                day = monthListener.getDayElements()[monthListener.getDayElements().length - 1];
            }
            PeriodsListener p = new PeriodsListener(dom, day);
            Element period = p.getNewPeriod();
            periodlistener.setPeriod(period);
            periodlistener.setPeriodTime(23, null, "single_start", txtMonthAtHour.getText(), txtMonthAtMinutes.getText(), txtMonthAtSecound.getText());
            p.applyPeriod(period);
            update.updateDays(DaysListener.MONTHDAYS);
        }
    }

    private boolean checkTime(String time, String which) {
        boolean retVal = true;
        if (time == null || time.trim().isEmpty()) {
            return true;
        }
        if (!Utils.isOnlyDigits(time)) {
            ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            retVal = false;
        } else {
            int itime = Utils.str2int(time);
            if ("hour".equals(which)) {
                Utils.str2int(time);
                if (itime < 0 || itime > 24) {
                    ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoTime.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    retVal = false;
                }
            } else if ("minutes".equals(which) || "second".equals(which)) {
                Utils.str2int(time);
                if (itime < 0 || itime > 60) {
                    ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoTime.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    retVal = false;
                }
            }
            if (time.trim().length() > 2) {
                ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoTime.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                retVal = false;
            }
        }
        return retVal;
    }

    private void close() {
        int cont = ErrorLog.message(runTimeSingleShell, SOSJOEMessageCodes.JOE_M_JobAssistent_Close.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            job.setContent(jobBackUp.cloneContent());
            runTimeSingleShell.dispose();
        }
    }

}
