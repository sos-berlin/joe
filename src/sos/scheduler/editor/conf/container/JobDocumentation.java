package sos.scheduler.editor.conf.container;

import java.io.File;

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

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.forms.JobAssistentImportJobParamsForm;
import sos.scheduler.editor.conf.forms.JobAssistentImportJobsForm;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobDocumentation extends FormBaseClass {
    @SuppressWarnings("unused")
    private final String            conSVNVersion       = "$Id$";

    private Group       group              = null;
    private Group       gMain              = null;
    private Group       gDescription       = null;
    private Text        tFileName          = null;
    private StyledText  tDescription       = null;
    private StyledText  tComment           = null;
    private Button      butShow            = null;
    private Button      butOpen            = null;
    private Button      butIsLiveFile      = null;
    private Button      butWizard          = null;
    private boolean     init               = true;
    private JobListener objJobDataProvider = null;
    private TextArea    txtAreaDescription = null;

    public JobDocumentation(Composite pParentComposite, JobListener pobjDataProvider) {
        super(pParentComposite, pobjDataProvider);
        objJobDataProvider = (JobListener) pobjDataProvider;
        showWaitCursor();
        createGroup(pParentComposite);
        initForm();
        restoreCursor();
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        // return bApply.isEnabled();
        return false;
    }

    private void initForm() {

        this.tFileName.setText(objJobDataProvider.getInclude());
        this.butIsLiveFile.setSelection(objJobDataProvider.isLiveFile());
    }

    private void createGroup(Composite objParent) {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        group = new Group(objParent, SWT.NONE);
        group.setText("Job: " + objJobDataProvider.getJobName() + (objJobDataProvider.isDisabled() ? " (Disabled)" : ""));
        group.setLayout(gridLayout2);

        group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        group.setLayout(gridLayout2);

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gMain = new Group(group, SWT.NONE);
        gMain.setText("Comment");
        gMain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gMain.setLayout(gridLayout);

        TextArea txtAreaComment = new TextArea(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
        txtAreaComment.setDataProvider(objJobDataProvider, enuSourceTypes.xmlComment);
        tComment = txtAreaComment;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 2;
        gDescription = new Group(group, SWT.NONE);
        gDescription.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        gDescription.setText("Job Description");
        gDescription.setLayout(gridLayout3);

        GridData gridData12 = new GridData(SWT.FILL, GridData.CENTER, true, false);
        gridData12.horizontalIndent = -1;
        tFileName = new Text(gDescription, SWT.BORDER);
        tFileName.setLayoutData(gridData12);

        tFileName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                showWaitCursor();
                objJobDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
                if (tFileName.getText() != null && tFileName.getText().length() > 0) {
                    butShow.setEnabled(true);
                    if (tFileName.getText().endsWith(".xml"))
                        butOpen.setEnabled(true);
                    else
                        butOpen.setEnabled(false);
                }
                else {
                    butShow.setEnabled(false);
                    butOpen.setEnabled(false);
                }
                restoreCursor();
            }
        });

        butIsLiveFile = new Button(gDescription, SWT.CHECK);
        butIsLiveFile.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;

                showWaitCursor();
                objJobDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());

                if (tFileName.getText() != null && tFileName.getText().length() > 0) {
                    butShow.setEnabled(true);
                    if (tFileName.getText().endsWith(".xml"))
                        butOpen.setEnabled(true);
                    else
                        butOpen.setEnabled(false);
                }
                else {
                    butShow.setEnabled(false);
                    butOpen.setEnabled(false);
                }
                restoreCursor();
            }
        });
        butIsLiveFile.setText("in Live Folder");
        GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData14.horizontalIndent = -1;

        txtAreaDescription = new TextArea(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        GridData gd_txtAreaDescription = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3);
        gd_txtAreaDescription.heightHint = 91;
        txtAreaDescription.setLayoutData(gd_txtAreaDescription);
        tDescription = txtAreaDescription;
        txtAreaDescription.setDataProvider(objJobDataProvider, enuSourceTypes.JobDocu);
        tDescription.setToolTipText(Messages.getTooltip("job.description"));

        tDescription.setText(objJobDataProvider.getDescription());

        butShow = new Button(gDescription, SWT.NONE);
        butShow.setEnabled(false);
        butShow.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butShow.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                try {
                    showWaitCursor();
                    if (tFileName.getText() != null && tFileName.getText().length() > 0) {
                        String sData = "";// getData(tFileName.getText());

                        Program prog = Program.findProgram("html");

                        String strFileName = new File((sData).concat(tFileName.getText())).toURI().toURL().toString();
                        if (prog != null)
                            prog.execute(strFileName);
                        else {
                            Runtime.getRuntime().exec(Options.getBrowserExec(strFileName, Options.getLanguage()));
                        }
                    }
                }
                catch (Exception ex) {
                    try {
                        new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file " + tFileName.getText(), ex);
                    }
                    catch (Exception ee) {
                    }
                    MainWindow.message(getShell(), "..could not open file " + tFileName.getText() + " " + ex.getMessage(), SWT.ICON_WARNING | SWT.OK);
                }
                finally {
                    restoreCursor();
                }
            }
        });
        butShow.setText("Show");
        butShow.setToolTipText(Messages.getTooltip("job.param.show"));
        butShow.setEnabled(tFileName.getText().trim().length() > 0);

        butOpen = new Button(gDescription, SWT.NONE);
        butOpen.setEnabled(false);
        butOpen.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String xmlPath = "";
                try {
                    showWaitCursor();
                    if (tFileName.getText() != null && tFileName.getText().length() > 0) {
                        xmlPath = sos.scheduler.editor.app.Options.getSchedulerData();
                        xmlPath = (xmlPath.endsWith("/") || xmlPath.endsWith("\\") ? xmlPath.concat(tFileName.getText()) : xmlPath.concat("/").concat(tFileName.getText()));

                        IContainer con = getContainer();
                        con.openDocumentation(xmlPath);
                        con.setStatusInTitle();
                    }
                    else {
                        MainWindow.message("There is no Documentation " + xmlPath, SWT.ICON_WARNING | SWT.OK);
                    }
                }
                catch (Exception e1) {
                }
                finally {
                    restoreCursor();
                }
            }
        });
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butOpen.setText("Open");
        butOpen.setToolTipText(Messages.getTooltip("job.param.open"));
        butOpen.setEnabled(tFileName.getText().trim().length() > 0 && tFileName.getText().endsWith(".xml"));

        tComment.setToolTipText(Messages.getTooltip("job.comment"));

        tFileName.setToolTipText(Messages.getTooltip("job.description.filename"));

        butIsLiveFile.setSelection(objJobDataProvider.isLiveFile());
        tFileName.setText(objJobDataProvider.getInclude());

        butWizard = new Button(gDescription, SWT.NONE);
        butWizard.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                startWizzard(false);
            }
        });

        butWizard.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butWizard.setText("Wizard");
        tComment.setText(objJobDataProvider.getComment());
    }

    public void startWizzard(boolean onlyParams) {
        try {
            showWaitCursor();
            if (objJobDataProvider.getInclude() != null && objJobDataProvider.getInclude().trim().length() > 0) {
                // JobDokumentation ist bekannt -> d.h Parameter aus dieser
                // Jobdoku extrahieren
                JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(objJobDataProvider.get_dom(), objJobDataProvider.get_main(), objJobDataProvider, onlyParams ? Editor.JOB : Editor.JOB_WIZARD);

                if (!onlyParams)
                    paramsForm.setJobForm(this);
                paramsForm.showAllImportJobParams(objJobDataProvider.getInclude());
            }
            else {
                JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(objJobDataProvider, Editor.JOB_WIZARD);

                if (!onlyParams) {
                    importJobForms.setJobForm(this);
                }
                importJobForms.showAllImportJobs();

            }
            if (butWizard != null) {
                butWizard.setToolTipText(Messages.getTooltip("jobs.assistent"));
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            restoreCursor();
        }
    }

    public String getData(String filename) {

        String data = ".";
        if ((objJobDataProvider.get_dom().isDirectory() || objJobDataProvider.get_dom().isLifeElement()) && butIsLiveFile.getSelection()) {
            if (filename.startsWith("/") || filename.startsWith("\\")) {
                data = Options.getSchedulerHotFolder();
            }
            else if (objJobDataProvider.get_dom().getFilename() != null) {
                data = new File(objJobDataProvider.get_dom().getFilename()).getParent();
            }
        }
        else {
            if (butIsLiveFile.getSelection())
                data = Options.getSchedulerHotFolder();
            else
                data = Options.getSchedulerData();
        }

        if (!(data.endsWith("\\") || data.endsWith("/")))
            data = data.concat("/");

        data = data.replaceAll("\\\\", "/");

        return data;
    }

} // @jve:decl-index=0:visual-constraint="10,10"
