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

	private Button		only4width;

	private Group		group			= null;
	private Group		gMain			= null;
	private Group		gDescription	= null;
	private Text		tFileName		= null;
	// private Text tDescription = null;
	// private Text tComment = null;
	private StyledText	tDescription	= null;
	private StyledText	tComment		= null;
	private Button		butShow			= null;
	private Button		butOpen			= null;
	private Button		butIsLiveFile	= null;
	private Button		butWizard		= null;
	private boolean		init			= true;

	public JobDocumentation(Composite pParentComposite, JobListener pobjDataProvider) {
		super(pParentComposite, pobjDataProvider);
		createGroup();
//		objParent.setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}

	public void apply() {
		// if (isUnsaved())
		// addParam();
	}

	public boolean isUnsaved() {
		// return bApply.isEnabled();
		return false;
	}

	private void createGroup() {
		// objParent.setLayout(new FillLayout());
		showWaitCursor();
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(objParent, SWT.NONE);
		group.setText("Job: " + objDataProvider.getJobName() + (objDataProvider.isDisabled() ? " (Disabled)" : ""));
		group.setLayout(gridLayout2);
		createSashForm();
		setResizableV(group);
		RestoreCursor();
	}

	private void createSashForm() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gMain = new Group(group, SWT.NONE);
		gMain.setText("Comment");
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gMain.setLayoutData(gridData_12);
		gMain.setLayout(gridLayout);

		TextArea txtAreaComment = new TextArea(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
		txtAreaComment.setDataProvider(objDataProvider, enuSourceTypes.xmlComment);
		tComment = txtAreaComment;

		only4width = new Button(gMain, SWT.CHECK);
		only4width.setVisible(false);
		only4width.setLayoutData(new GridData());
		only4width.setText("in Live Folder");
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gDescription = new Group(group, SWT.NONE);
		final GridData gridData_11 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gDescription.setLayoutData(gridData_11);
		gDescription.setText("Job Description");
		gDescription.setLayout(gridLayout3);

		GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData12.horizontalIndent = -1;
		tFileName = new Text(gDescription, SWT.BORDER);
		tFileName.setLayoutData(gridData12);

		tFileName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (init) {
					return;
				}
				showWaitCursor();
				objDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
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
				RestoreCursor();
			}
		});

		butIsLiveFile = new Button(gDescription, SWT.CHECK);
		butIsLiveFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (init)
					return;

				showWaitCursor();
				objDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());

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
				RestoreCursor();
			}
		});
		butIsLiveFile.setLayoutData(new GridData());
		butIsLiveFile.setText("in Live Folder");
		GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		gridData14.horizontalIndent = -1;

		TextArea txtAreaDescription = new TextArea(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tDescription = txtAreaDescription;
		txtAreaDescription.setDataProvider(objDataProvider, enuSourceTypes.JobDocu);

		butShow = new Button(gDescription, SWT.NONE);
		butShow.setEnabled(false);
		butShow.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butShow.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				try {
					showWaitCursor();
					if (tFileName.getText() != null && tFileName.getText().length() > 0) {
						String sData = getData(tFileName.getText());

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
						new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file "
								+ tFileName.getText(), ex);
					}
					catch (Exception ee) {
					}
					MainWindow.message(getShell(), "..could not open file " + tFileName.getText() + " " + ex.getMessage(), SWT.ICON_WARNING | SWT.OK);
				}
				finally {
					RestoreCursor();
				}
			}
		});
		butShow.setText("Show");

		butOpen = new Button(gDescription, SWT.NONE);
		butOpen.setEnabled(false);
		butOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String xmlPath = "";
				try {
					showWaitCursor();
					if (tFileName.getText() != null && tFileName.getText().length() > 0) {
						xmlPath = sos.scheduler.editor.app.Options.getSchedulerData();
						xmlPath = (xmlPath.endsWith("/") || xmlPath.endsWith("\\") ? xmlPath.concat(tFileName.getText()) : xmlPath.concat("/").concat(
								tFileName.getText()));

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
					RestoreCursor();
				}
			}
		});
		butOpen.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butOpen.setText("Open");

		butWizard = new Button(gDescription, SWT.NONE);
		butWizard.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				startWizzard(false);
			}
		});

		butWizard.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butWizard.setText("Wizard");

		tComment.setToolTipText(Messages.getTooltip("job.comment"));

		tFileName.setToolTipText(Messages.getTooltip("job.description.filename"));
		tDescription.setToolTipText(Messages.getTooltip("job.description"));
		butShow.setToolTipText(Messages.getTooltip("job.param.show"));
		butOpen.setToolTipText(Messages.getTooltip("job.param.open"));

		butIsLiveFile.setSelection(objDataProvider.isLiveFile());
		tFileName.setText(objDataProvider.getInclude());

		tDescription.setText(objDataProvider.getDescription());
		tComment.setText(objDataProvider.getComment());
		butShow.setEnabled(tFileName.getText().trim().length() > 0);
		butOpen.setEnabled(tFileName.getText().trim().length() > 0 && tFileName.getText().endsWith(".xml"));
	}

	public void startWizzard(boolean onlyParams) {
		try {
			showWaitCursor();
			if (objDataProvider.getInclude() != null && objDataProvider.getInclude().trim().length() > 0) {
				// JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren
				JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(objDataProvider.get_dom(), objDataProvider.get_main(),
						objDataProvider, onlyParams ? Editor.JOB : Editor.JOB_WIZARD);

				if (!onlyParams)
					paramsForm.setJobForm(this);
				paramsForm.showAllImportJobParams(objDataProvider.getInclude());
			}
			else {
				JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(objDataProvider, Editor.JOB_WIZARD);

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
			RestoreCursor();
		}
	}

	public String getData(String filename) {

		String data = ".";
		if ((objDataProvider.get_dom().isDirectory() || objDataProvider.get_dom().isLifeElement()) && butIsLiveFile.getSelection()) {
			if (filename.startsWith("/") || filename.startsWith("\\")) {
				data = Options.getSchedulerHotFolder();
			}
			else
				if (objDataProvider.get_dom().getFilename() != null) {
					data = new File(objDataProvider.get_dom().getFilename()).getParent();
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