package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.container.JobDocumentation;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobDocumentationForm extends Composite /* implements IUpdateLanguage */ {

	/* Hilfsvariable: setzt die Breite fest*/

	private Button		only4width;
	private JobListener	objDataProvider		= null;
	private Group		group			= null;
	private Group		gMain			= null;
	private Group		gDescription	= null;
	private Text		tFileName		= null;
	private Text		tDescription	= null;
	private Text		tComment		= null;
	private boolean		updateTree		= false;
	private Button		butShow			= null;
	private Button		butOpen			= null;
	private Button		butIsLiveFile	= null;
	private Button		butWizard		= null;
	private boolean		init			= true;

	public JobDocumentationForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
		super(parent, style);
		init = true;
		dom.setInit(true);
		this.setEnabled(Utils.isElementEnabled("job", dom, job));
		objDataProvider = new JobListener(dom, job, main);
		initialize();
//		setToolTipText();
		updateTree = false;
//		initForm();
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
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
//		GridLayout gridLayout2 = new GridLayout();
//		gridLayout2.numColumns = 1;
//		group = new Group(this, SWT.NONE);
//		group.setText("Job: " + objDataProvider.getName() + (objDataProvider.isDisabled() ? " (Disabled)" : ""));
//		group.setLayout(gridLayout2);
//		createSashForm();
		JobDocumentation objJD = new JobDocumentation(this, objDataProvider);
	}

//	private void createSashForm() {
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2;
//		gMain = new Group(group, SWT.NONE);
//		gMain.setText("Comment");
//		final GridData gridData_12 = new GridData(GridData.FILL, GridData.FILL, true, true);
//		gMain.setLayoutData(gridData_12);
//		gMain.setLayout(gridLayout);
////		GridData gridData61 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
//		
//		TextArea txtAreaComment = new TextArea(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
//		tComment = txtAreaComment;
//		txtAreaComment.setDataProvider(objDataProvider);
//		
//		/*
//		tComment = new Text(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
//		tComment.addKeyListener(new KeyAdapter() {
//			public void keyPressed(final KeyEvent e) {
//				if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {  // Ctrl-A
//					tComment.setSelection(0, tComment.getText().length());
//				}
//			}
//		});
//		tComment.addMouseListener(new MouseAdapter() {
//			public void mouseDoubleClick(final MouseEvent e) {
//				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
//				if (text != null)
//					tComment.setText(text);
//
//			}
//		});
//		tComment.setLayoutData(gridData61);
//		tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
//		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
//			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
//				if (init)
//					return;
//				objDataProvider.setComment(tComment.getText());
//			}
//		});
//
//*/
//		final Button button = new Button(gMain, SWT.NONE);
//		final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
//		gridData.widthHint = 28;
//		button.setLayoutData(gridData);
//		button.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
//				if (text != null)
//					tComment.setText(text);
//			}
//		});
//		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
//
//		only4width = new Button(gMain, SWT.CHECK);
//		only4width.setVisible(false);
//		only4width.setLayoutData(new GridData());
//		only4width.setText("in Live Folder");
//		GridLayout gridLayout3 = new GridLayout();
//		gridLayout3.numColumns = 2;
//		gDescription = new Group(group, SWT.NONE);
//		final GridData gridData_11 = new GridData(GridData.FILL, GridData.FILL, true, true);
//		gDescription.setLayoutData(gridData_11);
//		gDescription.setText("Job Description");
//		gDescription.setLayout(gridLayout3);
//
//		GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
//		gridData12.horizontalIndent = -1;
//		tFileName = new Text(gDescription, SWT.BORDER);
//		tFileName.setLayoutData(gridData12);
//
//		tFileName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
//			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
//				if (init)
//					return;
//				objDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
//				if (tFileName.getText() != null && tFileName.getText().length() > 0) {
//					butShow.setEnabled(true);
//					if (tFileName.getText().endsWith(".xml"))
//						butOpen.setEnabled(true);
//					else
//						butOpen.setEnabled(false);
//				}
//				else {
//					butShow.setEnabled(false);
//					butOpen.setEnabled(false);
//				}
//			}
//		});
//
//		butIsLiveFile = new Button(gDescription, SWT.CHECK);
//		butIsLiveFile.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//				if (init)
//					return;
//				objDataProvider.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
//
//				if (tFileName.getText() != null && tFileName.getText().length() > 0) {
//					butShow.setEnabled(true);
//					if (tFileName.getText().endsWith(".xml"))
//						butOpen.setEnabled(true);
//					else
//						butOpen.setEnabled(false);
//				}
//				else {
//					butShow.setEnabled(false);
//					butOpen.setEnabled(false);
//				}
//			}
//		});
//		
//		butIsLiveFile.setLayoutData(new GridData());
//		butIsLiveFile.setText("in Live Folder");
//		GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
//		gridData14.horizontalIndent = -1;
//		
//		TextArea txtAreaDescription = new TextArea(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
//		tDescription = txtAreaDescription;
//		txtAreaDescription.setDataProvider(objDataProvider);
//		
//		/*
////		tDescription = new Text(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
//		tDescription.addKeyListener(new KeyAdapter() {
//			public void keyPressed(final KeyEvent e) {
//				if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {
//					tDescription.setSelection(0, tDescription.getText().length());
//				}
//			}
//		});
//		tDescription.setFont(ResourceManager.getFont("", 10, SWT.NONE));
//		tDescription.setLayoutData(gridData14);
//		tDescription.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
//			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
//				if (init)
//					return;
//				objDataProvider.setDescription(tDescription.getText());
//			}
//		});
//*/
//		
//		butShow = new Button(gDescription, SWT.NONE);
//		butShow.setEnabled(false);
//		butShow.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
//		butShow.addSelectionListener(new SelectionAdapter() {
//			@SuppressWarnings("deprecation")
//			public void widgetSelected(final SelectionEvent e) {
//
//				try {
//					Utils.startCursor(getShell());
//					if (tFileName.getText() != null && tFileName.getText().length() > 0) {
//						String sData = getData(tFileName.getText());
//
//						Program prog = Program.findProgram("html");
//
//						if (prog != null)
//							prog.execute(new File((sData).concat(tFileName.getText())).toURL().toString());
//						else {
//							Runtime.getRuntime().exec(
//									Options.getBrowserExec(new File((sData).concat(tFileName.getText())).toURL().toString(), Options.getLanguage()));
//						}
//					}
//				}
//				catch (Exception ex) {
//					try {
//						new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file "
//								+ tFileName.getText(), ex);
//					}
//					catch (Exception ee) {
//						// tu nichts
//					}
//					MainWindow.message(getShell(), "..could not open file " + tFileName.getText() + " " + ex.getMessage(), SWT.ICON_WARNING | SWT.OK);
//				}
//				finally {
//					Utils.stopCursor(getShell());
//				}
//			}
//		});
//		butShow.setText("Show");
//
//		butOpen = new Button(gDescription, SWT.NONE);
//		butOpen.setEnabled(false);
//		butOpen.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//				Utils.startCursor(getShell());
//				String xmlPath = "";
//				if (tFileName.getText() != null && tFileName.getText().length() > 0) {
//					xmlPath = sos.scheduler.editor.app.Options.getSchedulerData();
//					xmlPath = (xmlPath.endsWith("/") || xmlPath.endsWith("\\") ? xmlPath.concat(tFileName.getText()) : xmlPath.concat("/").concat(
//							tFileName.getText()));
//
//					sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
//					con.openDocumentation(xmlPath);
//					con.setStatusInTitle();
//				}
//				else {
//					MainWindow.message("There is no Documentation " + xmlPath, SWT.ICON_WARNING | SWT.OK);
//				}
//				Utils.stopCursor(getShell());
//			}
//		});
//		butOpen.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
//		butOpen.setText("Open");
//
//		butWizard = new Button(gDescription, SWT.NONE);
//		butWizard.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//				startWizzard(false);
//			}
//		});
//		
//		butWizard.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
//		butWizard.setText("Wizard");
//
//		tComment.setToolTipText(Messages.getTooltip("job.comment"));
//
//		tFileName.setToolTipText(Messages.getTooltip("job.description.filename"));
//		tDescription.setToolTipText(Messages.getTooltip("job.description"));
//		butShow.setToolTipText(Messages.getTooltip("job.param.show"));
//		butOpen.setToolTipText(Messages.getTooltip("job.param.open"));
//
//		updateTree = true;
//
//		butIsLiveFile.setSelection(objDataProvider.isLiveFile());
//		tFileName.setText(objDataProvider.getInclude());
//
//		tDescription.setText(objDataProvider.getDescription());
//		tComment.setText(objDataProvider.getComment());
//		butShow.setEnabled(tFileName.getText().trim().length() > 0);
//		butOpen.setEnabled(tFileName.getText().trim().length() > 0 && tFileName.getText().endsWith(".xml"));
//
//	}
//
	public void initForm() {
////		updateTree = true;
////
////		butIsLiveFile.setSelection(objDataProvider.isLiveFile());
////		tFileName.setText(objDataProvider.getInclude());
////
////		tDescription.setText(objDataProvider.getDescription());
////		tComment.setText(objDataProvider.getComment());
////		butShow.setEnabled(tFileName.getText().trim().length() > 0);
////		butOpen.setEnabled(tFileName.getText().trim().length() > 0 && tFileName.getText().endsWith(".xml"));
////
	}
////
//	public void startWizzard(boolean onlyParams) {
//		Utils.startCursor(getShell());
//		if (objDataProvider.getInclude() != null && objDataProvider.getInclude().trim().length() > 0) {
//			// JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren
//			JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(objDataProvider.get_dom(), objDataProvider.get_main(), objDataProvider,
//					onlyParams ? Editor.JOB : Editor.JOB_WIZARD);
//			
//			if (!onlyParams)
//				paramsForm.setJobForm(this);
//			paramsForm.showAllImportJobParams(objDataProvider.getInclude());
//		}
//		else {
//			// Liste aller Jobdokumentation
//			JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(objDataProvider, Editor.JOB_WIZARD);
//
//			if (!onlyParams)
//				importJobForms.setJobForm(this);
//			importJobForms.showAllImportJobs();
//
//		}
//		if (butWizard != null)
//			butWizard.setToolTipText(Messages.getTooltip("jobs.assistent"));
//		Utils.stopCursor(getShell());
//	}
//
//	public String getData(String filename) {
//
//		String data = ".";
//		if ((objDataProvider.get_dom().isDirectory() || objDataProvider.get_dom().isLifeElement()) && butIsLiveFile.getSelection()) {
//			if (filename.startsWith("/") || filename.startsWith("\\")) {
//				data = Options.getSchedulerHotFolder();
//			}
//			else
//				if (objDataProvider.get_dom().getFilename() != null) {
//					data = new File(objDataProvider.get_dom().getFilename()).getParent();
//				}
//		}
//		else {
//			// normale Konfiguration
//			if (butIsLiveFile.getSelection())
//				data = Options.getSchedulerHotFolder();
//			else
//				data = Options.getSchedulerData();
//		}
//
//		if (!(data.endsWith("\\") || data.endsWith("/")))
//			data = data.concat("/");
//
//		data = data.replaceAll("\\\\", "/");
//
//		return data;
//
//	}
//
} // @jve:decl-index=0:visual-constraint="10,10"
