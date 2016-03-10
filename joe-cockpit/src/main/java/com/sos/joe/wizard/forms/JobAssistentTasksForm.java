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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
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
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobAssistentTasksForm {

    private Text txtTask = null;
    private JobListener joblistener = null;
    private SchedulerDom dom = null;
    private ISchedulerUpdate update = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Button butShow = null;
    private Text txtMinTasks = null;
    /** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent */
    private int assistentType = -1;
    private Shell tasksShell = null;
    private Combo jobname = null;
    private Button butBack = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    /** Hilsvariable für das Schliessen des Dialogs. Das wird gebraucht wenn das
     * Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird . */
    private boolean closeDialog = false;

    public JobAssistentTasksForm(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        update = update_;
        assistentType = assistentType_;
        joblistener = new JobListener(dom, job_, update);
    }

    private void init() {
        Element job = joblistener.getJob();
        if (job != null && Utils.getAttributeValue("tasks", job).equals("unbounded")) {
            job.removeAttribute("tasks");
        }
    }

    public void showTasksForm() {
        init();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        tasksShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        tasksShell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog)
                    close();
                e.doit = tasksShell.isDisposed();
            }
        });
        tasksShell.setLayout(gridLayout);
        tasksShell.setSize(473, 166);
        tasksShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        String step = " ";
        if (Utils.getAttributeValue("order", joblistener.getJob()).equalsIgnoreCase("yes"))
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step4of9.label();
        else
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step4of8.label();
        tasksShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_Tasks.params(step));
        {
            if (Utils.getAttributeValue("tasks", joblistener.getJob()) != null && Utils.getAttributeValue("tasks", joblistener.getJob()).equals("unbounded")) {
                joblistener.setTasks("");
            }
        }
        {
            final Group jobGroup = new Group(tasksShell, SWT.NONE);
            jobGroup.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(Utils.getAttributeValue("name", joblistener.getJob())));
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, true, 2, 1);
            jobGroup.setLayoutData(gridData);
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.marginWidth = 10;
            gridLayout_1.marginTop = 10;
            gridLayout_1.marginRight = 10;
            gridLayout_1.marginLeft = 10;
            gridLayout_1.marginHeight = 10;
            jobGroup.setLayout(gridLayout_1);
            final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
            composite_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            final GridLayout gridLayout_3 = new GridLayout();
            gridLayout_3.marginWidth = 0;
            gridLayout_3.marginRight = 5;
            gridLayout_3.numColumns = 4;
            composite_1.setLayout(gridLayout_3);
            {
                final Label tasksLabel = SOSJOEMessageCodes.JOE_L_JobAssistent_Tasks.Control(new Label(composite_1, SWT.NONE));
                final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
                gridData_1.widthHint = 57;
                tasksLabel.setLayoutData(gridData_1);
            }
            txtTask = SOSJOEMessageCodes.JOE_T_JobAssistent_Tasks.Control(new Text(composite_1, SWT.BORDER));
            txtTask.setFocus();
            txtTask.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
                        if (Utils.isNumeric(txtTask.getText())) {
                            joblistener.setTasks(txtTask.getText());
                        } else {
                            ErrorLog.message(tasksShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.ICON_WARNING | SWT.OK);
                        }
                        txtTask.setFocus();
                    }
                }
            });
            txtTask.setFocus();
            final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_5.widthHint = 112;
            gridData_5.minimumWidth = 100;
            txtTask.setLayoutData(gridData_5);
            txtTask.setText(joblistener.getTasks());
            final Label minimumTasksLabel = SOSJOEMessageCodes.JOE_L_JobAssistent_MinimumTasks.Control(new Label(composite_1, SWT.RIGHT));
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_3.widthHint = 95;
            minimumTasksLabel.setLayoutData(gridData_3);
            txtMinTasks = SOSJOEMessageCodes.JOE_T_JobAssistent_MinimumTasks.Control(new Text(composite_1, SWT.BORDER));
            txtMinTasks.setText(joblistener.getMintasks());
            txtMinTasks.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (txtMinTasks.getText() != null && txtMinTasks.getText().trim().length() > 0) {
                        if (Utils.isNumeric(txtMinTasks.getText())) {
                            joblistener.setMintasks(txtMinTasks.getText());
                        } else {
                            ErrorLog.message(tasksShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoNum.label(), SWT.ICON_WARNING | SWT.OK);
                        }
                    }
                }
            });
            final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_4.widthHint = 89;
            txtMinTasks.setLayoutData(gridData_4);
        }
        {
            final Composite composite = new Composite(tasksShell, SWT.NONE);
            composite.setLayoutData(new GridData());
            final GridLayout gridLayout_2 = new GridLayout();
            gridLayout_2.marginWidth = 0;
            composite.setLayout(gridLayout_2);
            {
                butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(composite, SWT.NONE));
                butCancel.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        close();
                    }
                });
            }
        }
        final Composite composite_2 = new Composite(tasksShell, SWT.NONE);
        final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData.widthHint = 250;
        composite_2.setLayoutData(gridData);
        final GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.marginWidth = 0;
        gridLayout_4.numColumns = 5;
        composite_2.setLayout(gridLayout_4);
        {
            butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite_2, SWT.NONE));
            butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            butShow.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (!checkTasks())
                        return;
                    Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), tasksShell, false, null, false, null, false);
                    txtTask.setFocus();
                }
            });
        }
        {
            butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.Control(new Button(composite_2, SWT.NONE));
            butFinish.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            butFinish.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    doFinish();
                }
            });
        }
        butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.Control(new Button(composite_2, SWT.NONE));
        butBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(dom, update, joblistener.getJob(), assistentType);
                paramsForm.showAllImportJobParams(joblistener.getInclude());
                if (jobname != null)
                    paramsForm.setJobname(jobname);
                // if(jobBackUp != null)
                paramsForm.setBackUpJob(jobBackUp, jobForm);
                closeDialog = true;
                tasksShell.dispose();
            }
        });
        {
            butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite_2, SWT.NONE));
            butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
            butNext.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            butNext.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    doNext();
                }
            });
        }
        Utils.createHelpButton(composite_2, "JOE_M_JobAssistentTasksForm_Help.label", tasksShell);
        tasksShell.layout();
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        tasksShell.setBounds((screen.width - tasksShell.getBounds().width) / 2, (screen.height - tasksShell.getBounds().height) / 2, tasksShell.getBounds().width, tasksShell.getBounds().height);
        tasksShell.open();
    }

    public void setToolTipText() {
        //
    }

    /** Überprüfung min_tasks darf tasks nicht überschreiten */
    private boolean checkTasks() {
        if ((txtTask.getText() != null && txtTask.getText().trim().length() > 0)
                && (txtMinTasks.getText() != null && txtMinTasks.getText().trim().length() > 0)) {
            if (Integer.parseInt(txtMinTasks.getText()) > Integer.parseInt(txtTask.getText())) {
                ErrorLog.message(tasksShell, SOSJOEMessageCodes.JOE_M_JobAssistent_MinTasksTooLarge.label(), SWT.ICON_WARNING | SWT.OK);
                return false;
            }
        }
        if ((txtTask.getText() != null && txtTask.getText().trim().length() == 0)
                && (txtMinTasks.getText() != null && txtMinTasks.getText().trim().length() > 0)) {
            if (Integer.parseInt(txtMinTasks.getText()) > 1) {
                // ErrorLog.message(tasksShell,
                // sos.scheduler.editor.app.Messages.getString("min_task_to_great"),
                // SWT.ICON_WARNING | SWT.OK );
                ErrorLog.message(tasksShell, SOSJOEMessageCodes.JOE_M_JobAssistent_MinTasksTooLarge.label(), SWT.ICON_WARNING | SWT.OK);
                return false;
            }
        }
        return true;
    }

    private void close() {
        int cont = ErrorLog.message(tasksShell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null)
                joblistener.getJob().setContent(jobBackUp.cloneContent());
            tasksShell.dispose();
        }
    }

    private void doNext() {
        Utils.startCursor(tasksShell);
        if (!checkTasks())
            return;
        Element job = joblistener.getJob();
        if (job.getChild("description") == null) {
            // Wizzard ohne Jobbeschreibung wurde aufgerufen.
            JobAssistentExecuteForms execute = new JobAssistentExecuteForms(dom, update, job, assistentType);
            execute.showExecuteForm();
            if (jobname != null)
                execute.setJobname(jobname);
            execute.setBackUpJob(jobBackUp, jobForm);
        } else if (job.getChild("process") != null) {
            JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update, job, assistentType);
            process.showProcessForm();
            if (jobname != null)
                process.setJobname(jobname);
            process.setBackUpJob(jobBackUp, jobForm);
        } else {
            JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update, job, assistentType);
            script.showScriptForm();
            if (jobname != null)
                script.setJobname(jobname);
            script.setBackUpJob(jobBackUp, jobForm);
        }
        closeDialog = true;
        Utils.stopCursor(tasksShell);
        tasksShell.dispose();
    }

    /** Der Wizzard wurde über den JobChain Dialog aufgerufen.
     * 
     * @param jobname */
    public void setJobname(Combo jobname) {
        this.jobname = jobname;
    }

    /** Der Wizzard wurde für ein bestehende Job gestartet. Beim verlassen der
     * Wizzard ohne Speichern, muss der bestehende Job ohne Änderungen wieder
     * zurückgesetz werden.
     * 
     * @param backUpJob */
    public void setBackUpJob(Element backUpJob, ScriptJobMainForm jobForm_) {
        if (backUpJob != null)
            jobBackUp = (Element) backUpJob.clone();
        if (jobForm_ != null)
            jobForm = jobForm_;
    }

    private void doFinish() {
        if (!checkTasks())
            return;
        if (assistentType == JOEConstants.JOB_WIZARD) {
            jobForm.initForm();
        } else {
            JobsListener j = new JobsListener(dom, update);
            j.newImportJob(joblistener.getJob(), assistentType);
        }
        if (Options.getPropertyBoolean("editor.job.show.wizard"))
            Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_Finish.label() + "\n\n" + Utils.getElementAsString(joblistener.getJob()), tasksShell, false, null, false, null, true);
        if (jobname != null)
            jobname.setText(Utils.getAttributeValue("name", joblistener.getJob()));
        closeDialog = true;
        tasksShell.dispose();
    }
}
