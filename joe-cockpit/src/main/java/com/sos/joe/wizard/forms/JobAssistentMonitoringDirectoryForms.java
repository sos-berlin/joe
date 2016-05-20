package com.sos.joe.wizard.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobAssistentMonitoringDirectoryForms extends JobWizardBaseForm {

    private Element job = null;
    private JobOptionsListener optionlistener = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Shell shellRunOptions = null;
    private Text txtDirectory = null;
    private Text txtRegExp = null;
    private Table tableWatchDirectory = null;
    private Button butApply = null;
    private Button butNewDirectory = null;
    private Button butRemoveDirectory = null;
    private Element jobBackUp = null;
    private boolean closeDialog = false;

    public JobAssistentMonitoringDirectoryForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        optionlistener = new JobOptionsListener(dom, job_);
        job = job_;
        jobBackUp = (Element) job_.clone();
    }

    public void showMonitoringDirectoryForm() {
        shellRunOptions = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        shellRunOptions.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog) {
                    close();
                }
                e.doit = shellRunOptions.isDisposed();
            }
        });
        shellRunOptions.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shellRunOptions.setLayout(gridLayout);
        shellRunOptions.setSize(542, 377);
        shellRunOptions.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_MonitoringDirectory.label());
        final Group jobGroup = SOSJOEMessageCodes.JOE_G_JobAssistent_JobGroup.control(new Group(shellRunOptions, SWT.NONE));
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
        gridData_3.widthHint = 514;
        gridData_3.heightHint = 283;
        jobGroup.setLayoutData(gridData_3);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginTop = 10;
        gridLayout_1.marginRight = 10;
        gridLayout_1.marginLeft = 10;
        gridLayout_1.marginHeight = 10;
        gridLayout_1.marginBottom = 10;
        gridLayout_1.numColumns = 3;
        jobGroup.setLayout(gridLayout_1);
        final Label lblDirectory = SOSJOEMessageCodes.JOE_L_JobAssistent_Directory.control(new Label(jobGroup, SWT.NONE));
        lblDirectory.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        txtDirectory = SOSJOEMessageCodes.JOE_T_JobAssistent_Directory.control(new Text(jobGroup, SWT.BORDER));
        txtDirectory.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        txtDirectory.setFocus();
        final Composite composite = SOSJOEMessageCodes.JOE_Composite1.control(new Composite(jobGroup, SWT.NONE));
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, false, false, 1, 2);
        gridData_4.heightHint = 59;
        gridData_4.widthHint = 72;
        composite.setLayoutData(gridData_4);
        final GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.horizontalSpacing = 0;
        gridLayout_4.marginWidth = 0;
        gridLayout_4.marginHeight = 0;
        composite.setLayout(gridLayout_4);
        butApply = SOSJOEMessageCodes.JOE_B_JobAssistent_ApplyDir.control(new Button(composite, SWT.NONE));
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        butApply.setLayoutData(gridData_2);
        butApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                apply();
            }
        });
        butNewDirectory = SOSJOEMessageCodes.JOE_B_JobAssistent_NewDirectory.control(new Button(composite, SWT.NONE));
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.widthHint = 47;
        butNewDirectory.setLayoutData(gridData);
        butNewDirectory.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                txtDirectory.setText("");
                txtRegExp.setText("");
                txtDirectory.setFocus();
                tableWatchDirectory.deselectAll();
            }
        });
        final Label lblRegExp = SOSJOEMessageCodes.JOE_L_JobAssistent_Regex.control(new Label(jobGroup, SWT.NONE));
        txtRegExp = SOSJOEMessageCodes.JOE_T_JobAssistent_Regex.control(new Text(jobGroup, SWT.BORDER));
        txtRegExp.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtRegExp.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        tableWatchDirectory = SOSJOEMessageCodes.JOE_Tbl_JobAssistent_WatchDirectory.control(new Table(jobGroup, SWT.FULL_SELECTION | SWT.BORDER));
        final GridData gridData_7 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        tableWatchDirectory.setLayoutData(gridData_7);
        optionlistener.fillDirectories(tableWatchDirectory);
        tableWatchDirectory.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (tableWatchDirectory.getSelectionCount() > 0) {
                    butRemoveDirectory.setEnabled(true);
                } else {
                    butRemoveDirectory.setEnabled(false);
                }
            }
        });
        tableWatchDirectory.setEnabled(true);
        tableWatchDirectory.setLinesVisible(true);
        tableWatchDirectory.setHeaderVisible(true);
        final TableColumn newColumnTableColumn =
                SOSJOEMessageCodes.JOE_TCl_JobAssistent_DirectoryColumn.control(new TableColumn(tableWatchDirectory, SWT.NONE));
        newColumnTableColumn.setWidth(156);
        final TableColumn newColumnTableColumn_1 =
                SOSJOEMessageCodes.JOE_TCl_JobAssistent_RegexColumn.control(new TableColumn(tableWatchDirectory, SWT.NONE));
        newColumnTableColumn_1.setWidth(186);
        butRemoveDirectory = SOSJOEMessageCodes.JOE_B_JobAssistent_RemoveDirectory.control(new Button(jobGroup, SWT.NONE));
        butRemoveDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, true));
        butRemoveDirectory.setEnabled(false);
        butRemoveDirectory.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (tableWatchDirectory.getSelectionCount() > 0) {
                    optionlistener.deleteDirectory(tableWatchDirectory.getSelectionIndex());
                }
            }
        });
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        shellRunOptions.setBounds((screen.width - shellRunOptions.getBounds().width) / 2, (screen.height - shellRunOptions.getBounds().height) / 2,
                shellRunOptions.getBounds().width, shellRunOptions.getBounds().height);
        shellRunOptions.open();
        final Composite composite_1 = SOSJOEMessageCodes.JOE_Composite2.control(new Composite(shellRunOptions, SWT.NONE));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginWidth = 0;
        composite_1.setLayout(gridLayout_2);
        {
            butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Close.control(new Button(composite_1, SWT.NONE));
            butCancel.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    close();
                }
            });
        }
        final Composite composite_2 = SOSJOEMessageCodes.JOE_Composite3.control(new Composite(shellRunOptions, SWT.NONE));
        composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.marginRight = 10;
        gridLayout_3.marginWidth = 0;
        gridLayout_3.numColumns = 4;
        composite_2.setLayout(gridLayout_3);
        butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.control(new Button(composite_2, SWT.NONE));
        butFinish.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        butFinish.setVisible(false);
        butFinish.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                closeDialog = true;
                shellRunOptions.dispose();
            }
        });
        Utils.createHelpButton(composite_2, "JOE_M_JobAssistentMonitoringDirectoryForms_Help.label", shellRunOptions).setLayoutData(
                new GridData(GridData.END, GridData.CENTER, false, false));
        butNext = SOSJOEMessageCodes.JOE_B_JobAssistentMonitoringDirectoryForms_Apply.control(new Button(composite_2, SWT.NONE));
        butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_6.widthHint = 54;
        butNext.setLayoutData(gridData_6);
        butNext.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                closeDialog = true;
                shellRunOptions.dispose();
            }
        });
        txtDirectory.setFocus();
        setToolTipText();
        shellRunOptions.layout();
    }

    public void setToolTipText() {
        //
    }

    private void close() {
        int cont = ErrorLog.message(shellRunOptions, SOSJOEMessageCodes.JOE_M_JobAssistent_Close.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            Utils.getElementAsString(jobBackUp);
            job.setContent(jobBackUp.cloneContent());
            shellRunOptions.dispose();
        }
    }

    private void apply() {
        if (txtDirectory.getText() != null && !txtDirectory.getText().trim().isEmpty()) {
            optionlistener.newDirectory();
            optionlistener.applyDirectory(txtDirectory.getText(), txtRegExp.getText());
            optionlistener.fillDirectories(tableWatchDirectory);
        }
        txtDirectory.setFocus();
    }

}