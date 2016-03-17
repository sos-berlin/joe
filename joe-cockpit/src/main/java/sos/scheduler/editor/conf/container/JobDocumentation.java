package sos.scheduler.editor.conf.container;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.composites.TextArea;
import sos.scheduler.editor.conf.composites.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.wizard.forms.JobAssistentImportJobParamsForm;
import com.sos.joe.wizard.forms.JobAssistentImportJobsForm;

public class JobDocumentation extends FormBaseClass {

    private static final Logger LOGGER = Logger.getLogger(JobDocumentation.class);
    private Group group = null;
    private Group gMain = null;
    private Group gDescription = null;
    private Text tFileName = null;
    private StyledText tDescription = null;
    private StyledText tComment = null;
    private Button butShow = null;
    private Button butOpen = null;
    private Button butIsLiveFile = null;
    private Button butWizard = null;
    private boolean init = true;
    private JobListener objJobDataProvider = null;
    private TextArea txtAreaDescription = null;

    public JobDocumentation(Composite pParentComposite, JobListener pobjDataProvider) {
        super(pParentComposite, pobjDataProvider);
        objJobDataProvider = pobjDataProvider;
        init = true;
        showWaitCursor();
        createGroup(pParentComposite);
        initForm();
        init = false;
        restoreCursor();
    }

    public boolean isUnsaved() {
        return false;
    }

    private void initForm() {
        this.tFileName.setText(objJobDataProvider.getInclude());
        butShow.setEnabled(!tFileName.getText().trim().isEmpty());
        butOpen.setEnabled(!tFileName.getText().trim().isEmpty());
        this.butIsLiveFile.setSelection(objJobDataProvider.isLiveFile());
    }

    private String getJobsDirectoryName() {
        String jobsDirectoryName = "";
        if (new File(tFileName.getText()).exists()) {
            jobsDirectoryName = tFileName.getText();
        } else {
            if (butIsLiveFile.getSelection()) {
                jobsDirectoryName = Options.getSchedulerNormalizedHotFolder();
            } else {
                jobsDirectoryName = Options.getSchedulerNormalizedData();
            }
        }
        return jobsDirectoryName;
    }

    private void createGroup(Composite objParent) {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        group = new Group(objParent, SWT.NONE);
        String strM = SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(objJobDataProvider.getJobName())
                + (objJobDataProvider.isDisabled() ? SOSJOEMessageCodes.JOE_M_JobCommand_Disabled.label() : "");
        group.setText(strM);
        group.setLayout(gridLayout2);
        group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        group.setLayout(gridLayout2);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gMain = SOSJOEMessageCodes.JOE_G_ConfigForm_Comment.Control(new Group(group, SWT.NONE));
        gMain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gMain.setLayout(gridLayout);
        TextArea txtAreaComment = new TextArea(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
        txtAreaComment.setDataProvider(objJobDataProvider, enuSourceTypes.xmlComment);
        tComment = txtAreaComment;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 2;
        gDescription = SOSJOEMessageCodes.JOE_G_JobDocumentation_JobDescription.Control(new Group(group, SWT.NONE));
        gDescription.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gDescription.setLayout(gridLayout3);
        GridData gridData12 = new GridData(SWT.FILL, GridData.CENTER, true, false);
        gridData12.horizontalIndent = -1;
        tFileName = SOSJOEMessageCodes.JOE_T_JobDocumentation_FileName.Control(new Text(gDescription, SWT.BORDER));
        tFileName.setLayoutData(gridData12);
        tFileName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                showWaitCursor();
                objJobDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
                if (tFileName.getText() != null && !tFileName.getText().isEmpty()) {
                    butShow.setEnabled(true);
                    if (tFileName.getText().endsWith(".xml")) {
                        butOpen.setEnabled(true);
                    } else {
                        butOpen.setEnabled(false);
                    }
                } else {
                    butShow.setEnabled(false);
                    butOpen.setEnabled(false);
                }
                restoreCursor();
            }
        });
        butIsLiveFile = SOSJOEMessageCodes.JOE_B_JobDocumentation_IsLiveFile.Control(new Button(gDescription, SWT.CHECK));
        butIsLiveFile.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (init) {
                    return;
                }
                showWaitCursor();
                objJobDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());

                if (tFileName.getText() != null && !tFileName.getText().isEmpty()) {
                    butShow.setEnabled(true);
                    if (tFileName.getText().endsWith(".xml")) {
                        butOpen.setEnabled(true);
                    } else {
                        butOpen.setEnabled(false);
                    }
                } else {
                    butShow.setEnabled(false);
                    butOpen.setEnabled(false);
                }
                restoreCursor();
            }
        });
        GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData14.horizontalIndent = -1;
        txtAreaDescription = new TextArea(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        GridData gd_txtAreaDescription = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3);
        gd_txtAreaDescription.heightHint = 91;
        txtAreaDescription.setLayoutData(gd_txtAreaDescription);
        tDescription = txtAreaDescription;
        txtAreaDescription.setDataProvider(objJobDataProvider, enuSourceTypes.JobDocu);
        tDescription.setToolTipText(SOSJOEMessageCodes.JOE_M_0050.label());
        tDescription.setText(objJobDataProvider.getDescription());
        butShow = SOSJOEMessageCodes.JOE_B_JobDocumentation_Show.Control(new Button(gDescription, SWT.NONE));
        butShow.setEnabled(false);
        butShow.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    showWaitCursor();
                    if (tFileName.getText() != null && !tFileName.getText().isEmpty()) {
                        String jobsDirectoryName = getJobsDirectoryName();
                        Program prog = Program.findProgram("html");
                        String strFileName = new File((jobsDirectoryName).concat(tFileName.getText())).toURI().toURL().toString();
                        if (prog != null) {
                            prog.execute(strFileName);
                        } else {
                            Runtime.getRuntime().exec(Options.getBrowserExec(strFileName, Options.getLanguage()));
                        }
                    }
                } catch (Exception ex) {
                    try {
                        new ErrorLog(SOSJOEMessageCodes.JOE_M_0011.params(sos.util.SOSClassUtil.getMethodName(), tFileName.getText(), ex));
                    } catch (Exception ee) {
                    }
                    MainWindow.message(getShell(), SOSJOEMessageCodes.JOE_M_0011.params("widgetSelected()", tFileName.getText(), ex.getMessage()), SWT.ICON_WARNING
                            | SWT.OK);
                } finally {
                    restoreCursor();
                }
            }
        });
        butShow.setEnabled(!tFileName.getText().trim().isEmpty());
        butOpen = SOSJOEMessageCodes.JOE_B_JobDocumentation_Open.Control(new Button(gDescription, SWT.NONE));
        butOpen.setEnabled(false);
        butOpen.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                String jobsDirectoryName = "";
                try {
                    showWaitCursor();
                    if (tFileName.getText() != null && !tFileName.getText().isEmpty()) {
                        jobsDirectoryName = getJobsDirectoryName();
                        IContainer con = getContainer();
                        con.openDocumentation(jobsDirectoryName.concat(tFileName.getText()));
                        con.setStatusInTitle();
                    } else {
                        MainWindow.message(SOSJOEMessageCodes.JOE_M_JobDocumentation_NoDoc.params(jobsDirectoryName), SWT.ICON_WARNING | SWT.OK);
                    }
                } catch (Exception e1) {
                } finally {
                    restoreCursor();
                }
            }
        });
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butOpen.setEnabled(!tFileName.getText().trim().isEmpty() && tFileName.getText().endsWith(".xml"));
        tComment.setToolTipText(SOSJOEMessageCodes.JOE_M_0051.label());
        butIsLiveFile.setSelection(objJobDataProvider.isLiveFile());
        tFileName.setText(objJobDataProvider.getInclude());
        butWizard = SOSJOEMessageCodes.JOE_B_ParameterForm_Wizard.Control(new Button(gDescription, SWT.NONE));
        butWizard.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                startWizzard(false);
            }
        });
        butWizard.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        tComment.setText(objJobDataProvider.getComment());
    }

    private void startWizzard(boolean onlyParams) {
        try {
            showWaitCursor();
            if (objJobDataProvider.getInclude() != null && !objJobDataProvider.getInclude().trim().isEmpty()) {
                JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(objJobDataProvider.get_dom(), objJobDataProvider.get_main(), objJobDataProvider, onlyParams ? JOEConstants.JOB
                        : JOEConstants.JOB_WIZARD);
                paramsForm.showAllImportJobParams(objJobDataProvider.getInclude());
            } else {
                JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(objJobDataProvider, JOEConstants.JOB_WIZARD);
                if (!onlyParams) {
                    importJobForms.setJobForm(this);
                }
                importJobForms.showAllImportJobs();

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            restoreCursor();
        }
    }

}