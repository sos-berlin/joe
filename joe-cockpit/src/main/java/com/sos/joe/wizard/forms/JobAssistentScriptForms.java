package com.sos.joe.wizard.forms;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.LanguageDescriptorList;

public class JobAssistentScriptForms {

    private SchedulerDom dom = null;
    private ISchedulerUpdate update = null;
    private ScriptListener scriptlistener = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Button butShow = null;
    private Button butBack = null;
    private Table tableInclude = null;
    private Text txtLanguage = null;
    private Text txtJavaClass = null;
    private Label lblClass = null;
    private int assistentType = -1;
    private Shell scriptShell = null;
    private Combo jobname = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    private boolean closeDialog = false;

    public JobAssistentScriptForms(final SchedulerDom dom_, final ISchedulerUpdate update_, final Element job_, final int assistentType_) {
        dom = dom_;
        update = update_;
        assistentType = assistentType_;
        scriptlistener = new ScriptListener(dom, job_, JOEConstants.SCRIPT, update);
    }

    public void showScriptForm() {
        scriptShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        scriptShell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog) {
                    close();
                }
                e.doit = scriptShell.isDisposed();
            }
        });
        scriptShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.marginBottom = 5;
        gridLayout.numColumns = 3;
        scriptShell.setLayout(gridLayout);
        scriptShell.setSize(521, 322);
        String step = " ";
        if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", scriptlistener.getParent()))) {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step5of9.label();
        } else {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step5of8.label();
        }
        scriptShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_Script.params(step));
        final Group jobGroup = new Group(scriptShell, SWT.NONE);
        String strJobName = Utils.getAttributeValue("name", scriptlistener.getParent());
        jobGroup.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(strJobName));
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 3, 1);
        gridData.widthHint = 490;
        gridData.heightHint = 217;
        jobGroup.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.verticalSpacing = 10;
        gridLayout_1.horizontalSpacing = 10;
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginTop = 10;
        gridLayout_1.marginRight = 10;
        gridLayout_1.marginLeft = 10;
        gridLayout_1.marginHeight = 10;
        gridLayout_1.marginBottom = 10;
        gridLayout_1.numColumns = 2;
        jobGroup.setLayout(gridLayout_1);
        final Label lblLanguage = SOSJOEMessageCodes.JOE_L_JobAssistent_Language.Control(new Label(jobGroup, SWT.NONE));
        txtLanguage = SOSJOEMessageCodes.JOE_T_JobAssistent_Language.Control(new Text(jobGroup, SWT.BORDER));
        txtLanguage.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent e) {
                if (txtLanguage.getEnabled()) {
                    String strLangText = txtLanguage.getText();
                    if (LanguageDescriptorList.getLanguageDescriptor(strLangText) == null) {
                        ErrorLog.message(scriptShell, SOSJOEMessageCodes.JOE_M_JobAssistent_UnknownLanguage.label(), SWT.ICON_WARNING | SWT.OK);
                        txtLanguage.setFocus();
                        return;
                    }
                    scriptlistener.setLanguage(scriptlistener.languageAsInt(txtLanguage.getText()));
                }
            }
        });
        txtLanguage.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        txtLanguage.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtLanguage.setEditable(false);
        txtLanguage.setFocus();
        String lan = scriptlistener.getLanguage(scriptlistener.getLanguage());
        if (lan != null && !lan.trim().isEmpty() && scriptlistener.getParent().getChild("description") != null) {
            txtLanguage.setText(lan);
        } else {
            txtLanguage.setEditable(true);
            txtLanguage.setText(lan);
        }
        if (scriptlistener.getComClass() != null && !scriptlistener.getComClass().isEmpty()) {
            lblClass = SOSJOEMessageCodes.JOE_L_JobAssistent_ComClass.Control(new Label(jobGroup, SWT.NONE));
        } else {
            lblClass = SOSJOEMessageCodes.JOE_L_JobAssistent_JavaClass.Control(new Label(jobGroup, SWT.NONE));
        }
        txtJavaClass = SOSJOEMessageCodes.JOE_T_JobAssistent_JavaClass.Control(new Text(jobGroup, SWT.BORDER));
        txtJavaClass.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (txtJavaClass.getEnabled() && txtJavaClass.getText() != null && !txtJavaClass.getText().trim().isEmpty()
                        && "Java Class".equals(lblClass.getText())) {
                    scriptlistener.setJavaClass(txtJavaClass.getText());
                }
            }
        });
        txtJavaClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        txtJavaClass.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtJavaClass.setEditable(false);
        if (scriptlistener.getJavaClass() != null && !scriptlistener.getJavaClass().trim().isEmpty()) {
            txtJavaClass.setText(scriptlistener.getJavaClass());
        }
        if (txtJavaClass.getText() != null && txtJavaClass.getText().isEmpty() && scriptlistener.getParent().getChild("description") == null
                && "java".equals(txtLanguage.getText())) {
            txtJavaClass.setEditable(true);
        }
        if (lblClass != null && "Com Class".equals(lblClass.getText())) {
            final Label lblRessources = SOSJOEMessageCodes.JOE_L_JobAssistent_FileName.Control(new Label(jobGroup, SWT.NONE));
        } else {
            final Label lblRessources = SOSJOEMessageCodes.JOE_L_JobAssistent_Resource.Control(new Label(jobGroup, SWT.NONE));
        }
        final Text txtResource = SOSJOEMessageCodes.JOE_T_JobAssistent_Resource.Control(new Text(jobGroup, SWT.BORDER));
        txtResource.setEditable(false);
        txtResource.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtResource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        if (lblClass != null && "Java Class".equals(lblClass.getText()) && JobListener.getLibrary() != null && !JobListener.getLibrary().isEmpty()) {
            txtResource.setText(JobListener.getLibrary());
        } else if (scriptlistener.getFilename() != null && !scriptlistener.getFilename().trim().isEmpty()) {
            txtResource.setText(scriptlistener.getFilename());
        }
        final Label lblInclude = SOSJOEMessageCodes.JOE_L_JobAssistent_Include.Control(new Label(jobGroup, SWT.NONE));
        lblInclude.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true));
        tableInclude = SOSJOEMessageCodes.JOE_Tbl_JobAssistent_Include.Control(new Table(jobGroup, SWT.BORDER));
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_1.widthHint = 322;
        gridData_1.heightHint = 55;
        tableInclude.setLayoutData(gridData_1);
        tableInclude.setLinesVisible(true);
        tableInclude.setHeaderVisible(true);
        tableInclude.setEnabled(false);
        String[] iElem = scriptlistener.getIncludes();
        for (String in : iElem) {
            TableItem item = new TableItem(tableInclude, SWT.NONE);
            item.setText(in);
        }
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        scriptShell.setBounds((screen.width - scriptShell.getBounds().width) / 2, (screen.height - scriptShell.getBounds().height) / 2,
                scriptShell.getBounds().width, scriptShell.getBounds().height);
        scriptShell.open();
        final Composite composite = new Composite(scriptShell, SWT.NONE);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginWidth = 0;
        gridLayout_2.horizontalSpacing = 0;
        composite.setLayout(gridLayout_2);
        butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(composite, SWT.NONE));
        butCancel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        final Composite composite2 = new Composite(scriptShell, SWT.NONE);
        composite2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false, 2, 1));
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.marginWidth = 0;
        gridLayout_3.numColumns = 5;
        composite2.setLayout(gridLayout_3);
        butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite2, SWT.NONE));
        butShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.showClipboard(Utils.getElementAsString(scriptlistener.getParent()), scriptShell, false, null, false, null, false);
            }
        });
        butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.Control(new Button(composite2, SWT.NONE));
        butFinish.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                doFinish();
            }
        });
        butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.Control(new Button(composite2, SWT.NONE));
        butBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                doBack();
            }
        });
        butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite2, SWT.NONE));
        butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        butNext.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(scriptShell);
                if ("yes".equals(Utils.getAttributeValue("order", scriptlistener.getParent()))) {
                    JobAssistentTimeoutOrderForms timeout = new JobAssistentTimeoutOrderForms(dom, update, scriptlistener.getParent(), assistentType);
                    timeout.showTimeOutForm();
                    if (jobname != null) {
                        timeout.setJobname(jobname);
                    }
                    timeout.setBackUpJob(jobBackUp, jobForm);
                } else {
                    JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update, scriptlistener.getParent(), assistentType);
                    timeout.showTimeOutForm();
                    timeout.setBackUpJob(jobBackUp, jobForm);
                }
                Utils.stopCursor(scriptShell);
                closeDialog = true;
                scriptShell.dispose();
            }
        });
        Utils.createHelpButton(composite2, "JOE_M_JobAssistentScriptForms_Help.label", scriptShell);
        scriptShell.layout();
    }

    public void setToolTipText() {
        //
    }

    private void close() {
        int cont = ErrorLog.message(scriptShell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null) {
                scriptlistener.getParent().setContent(jobBackUp.cloneContent());
            }
            scriptShell.dispose();
        }
    }

    public void setJobname(final Combo jobname) {
        this.jobname = jobname;
    }

    private void doBack() {
        if (scriptlistener.getParent().getChild("description") == null) {
            JobAssistentExecuteForms execute = new JobAssistentExecuteForms(dom, update, scriptlistener.getParent(), assistentType);
            execute.showExecuteForm();
            if (jobname != null) {
                execute.setJobname(jobname);
            }
            execute.setBackUpJob(jobBackUp, jobForm);
        } else {
            JobAssistentTasksForm tasks = new JobAssistentTasksForm(dom, update, scriptlistener.getParent(), assistentType);
            tasks.showTasksForm();
            if (jobname != null) {
                tasks.setJobname(jobname);
            }
            tasks.setBackUpJob(jobBackUp, jobForm);
        }
        closeDialog = true;
        scriptShell.dispose();
    }

    public void setBackUpJob(final Element backUpJob, final ScriptJobMainForm jobForm_) {
        if (backUpJob != null) {
            jobBackUp = (Element) backUpJob.clone();
        }
        jobForm = jobForm_;
    }

    private void doFinish() {
        if (jobname != null) {
            jobname.setText(Utils.getAttributeValue("name", scriptlistener.getParent()));
        }
        if (assistentType == JOEConstants.JOB_WIZARD) {
            jobForm.initForm();
        } else {
            JobsListener listener = new JobsListener(dom, update);
            listener.newImportJob(scriptlistener.getParent(), assistentType);
        }
        if (Options.getPropertyBoolean("editor.job.show.wizard")) {
            Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_Finish.label() + "\n\n" + Utils.getElementAsString(scriptlistener.getParent()),
                    scriptShell, false, null, false, null, true);
        }
        closeDialog = true;
        scriptShell.dispose();
    }

}