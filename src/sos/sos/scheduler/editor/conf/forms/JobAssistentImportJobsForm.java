package sos.scheduler.editor.conf.forms;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.ParameterListener;

import com.swtdesigner.SWTResourceManager;

/**
 * Job Wizzard.
 * 
 * Liste der Standalone Jobs bzw. Auftragsgesteuerte Jobs.
 * 
 * Es werden alle Standalone Jobs oder Auftragsgesteuerte Jobs zur Auswahl gestellt.
 * 
 * Die Kriterien stehen in der Job Dokumentation.
 * Das bedeutet alle Job Dokumentationen aus der Verzeichnis <SCHEDULER_DATA>/jobs/*.xml werden parsiert.
 * 
 * Folgen Funktionen können hier ausgeführt werden:
 * 
 * 
 * show: 
 * 		zeigt den Job mit den Informationen aus der ausgewählten Jobdokumentation aus der Liste im seperaten Fenster als XML.
 * 
 * next: 
 * 		geht in das nächste Wizzard Formular Parameter. 
 * 		Hier werden alle Parameter der ausgewählten Jobdokumentation aus der Liste übergeben.
 * 
 * finish: 
 * 		Generiert einen Job. Übernimmt die Einstellungen der ausgewählten Job aus der Liste. 
 *      Alle Defaulteinstellungen des Jobs werden hier mit übernommen.
 * 
 * Help Button: 
 * 		Öffnet einen Dialog mit Hilfetext
 * 
 * Description: 
 * 		Öffnet einen neuen IE mit der ausgewählten JobDocumentation
 * 
 * Back: 
 * 		geht einen Formular zurück
 * 
 * Cancel: 
 * 		beendet den Wizzard
 * 
 * Der Aufbau eines Jobs kann aus der Dokumentation <SCHEDULER_>\config\html\doc\de\xml.xml entnommen werden. 
 * 
 * @author mueruevet.oeksuez@sos-berlin.com
 *
 * @version $Id$
 */
public class JobAssistentImportJobsForm {
	private Shell													shell				= null;
	private Text													txtTitle			= null;
	private Text													txtPath				= null;
	private Tree													tree				= null;
	private String													xmlPaths			= null;
	private Text													txtJobname			= null;
	private JobsListener											listener			= null;
	private JobListener												joblistener			= null;
	private SchedulerDom											dom					= null;
	private ISchedulerUpdate										update				= null;
	/** Parameter: Tabelle aus der JobForm*/
	private Table													tParameter			= null;
	private Button													butImport			= null;
	private Button													butParameters		= null;
	private Button													butdescription		= null;
	private Button													butCancel			= null;
	private Button													butShow				= null;
	private Button													butBack				= null;
	private String													jobType				= "";
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int														assistentType		= -1;
	private Combo													jobname				= null;
	private Element													jobBackUp			= null;
	private JobMainForm												jobForm				= null;
	private sos.scheduler.editor.conf.listeners.ParameterListener	paramListener		= null;
	private Text													refreshDetailsText	= null;
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean													closeDialog			= false;
	private boolean													flagBackUpJob		= true;
	private JobDocumentationForm									jobDocForm			= null;

	/**
	 * Konstruktor
	 * 
	 * @param dom_ SchedulerDom
	 * @param update_ ISchedulerUpdate
	 * @param assistentType_ int
	 */
	public JobAssistentImportJobsForm(SchedulerDom dom_, ISchedulerUpdate update_, int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;
		listener = new JobsListener(dom, update);
	}

	/**
	 * Konstruktor
	 * 
	 * @param listener_
	 * @param assistentType_
	 */
	public JobAssistentImportJobsForm(JobListener listener_, int assistentType_) {
		jobBackUp = (Element) listener_.getJob().clone();
		joblistener = listener_;
		dom = joblistener.get_dom();
		update = joblistener.get_main();
		listener = new JobsListener(dom, update);
		assistentType = assistentType_;
		paramListener = new ParameterListener(dom, joblistener.getJob(), update, assistentType);
	}

	/**
	 * Konstruktor
	 * 
	 * @param listener_
	 * @param tParameter_
	 * @param assistentType_
	 */
	public JobAssistentImportJobsForm(JobListener listener_, Table tParameter_, int assistentType_) {
		jobBackUp = (Element) listener_.getJob().clone();
		joblistener = listener_;
		dom = joblistener.get_dom();
		update = joblistener.get_main();
		listener = new JobsListener(dom, update);
		tParameter = tParameter_;
		assistentType = assistentType_;
		paramListener = new ParameterListener(dom, joblistener.getJob(), update, assistentType);
	}

	/**
	 * Jobname setzen
	 * @param jobname
	 */
	public void setJobname(Combo jobname) {
		this.jobname = jobname;
	}

	/**
	 * Alle vorhandenen Job Dokumentation aus der <SCHEDULER_DATA>/jobs/*.xml
	 * parsieren und in die Tabelle Schreiben. Folgende Informationen werden bei der Parsierung ausgelesen:
	 * Name, Title, Filename, Job-Meta-Element
	 * 
	 * @return ArrayList - Liste aller Jobs. EIn Eintrag der Liste entspricht einen HashMap. Der HasMap hat die 
	 * Informationen wie Name, Title, Filename und Job Element
	 */
	public ArrayList parseDocuments() {
		String xmlFilename = "";
		xmlPaths = sos.scheduler.editor.app.Options.getSchedulerData();
		xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat("jobs") : xmlPaths.concat("/jobs"));
		ArrayList listOfDoc = null;
		try {
			listOfDoc = new ArrayList();
			if (!new File(xmlPaths).exists()) {
				MainWindow.message(shell, "Missing Directory for Job Description: " + xmlPaths, SWT.ICON_WARNING | SWT.OK);
				return listOfDoc;
			}
			java.util.Vector filelist = sos.util.SOSFile.getFilelist(xmlPaths, "^.*\\.xml$", java.util.regex.Pattern.CASE_INSENSITIVE, true);
			Iterator fileIterator = filelist.iterator();
			while (fileIterator.hasNext()) {
				xmlFilename = fileIterator.next().toString();
				SAXBuilder builder = new SAXBuilder();
				Document doc = builder.build(new File(xmlFilename));
				Element root = doc.getRootElement();
				List listMainElements = root.getChildren();
				HashMap h = null;
				for (int i = 0; i < listMainElements.size(); i++) {
					Element elMain = (Element) (listMainElements.get(i));
					if (elMain.getName().equalsIgnoreCase("job")) {
						h = new HashMap();
						h.put("name", elMain.getAttributeValue("name"));
						h.put("title", elMain.getAttributeValue("title"));
						h.put("filename", xmlFilename);
						h.put("job", elMain);
						listOfDoc.add(h);
					}
				}
			}
		}
		catch (Exception ex) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
			}
			catch (Exception ee) {
				// tu nichts
			}
			ex.printStackTrace();
		}
		return listOfDoc;
	}

	public void showAllImportJobs(String type_) {
		jobType = type_;
		showAllImportJobs();
	}

	public void showAllImportJobs(JobListener joblistener_) {
		joblistener = joblistener_;
		jobBackUp = (Element) joblistener_.getJob().clone();
		jobType = (joblistener.getOrder() ? "order" : "standalonejob");
		showAllImportJobs();
	}

	public void showAllImportJobs() {
		try {
			shell = new Shell(MainWindow.getSShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
			shell.addShellListener(new ShellAdapter() {
				public void shellClosed(final ShellEvent e) {
					if (!closeDialog)
						close();
					e.doit = shell.isDisposed();
				}
			});
			shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginHeight = 0;
			shell.setLayout(gridLayout);
			String step = "";
			if (jobType.equalsIgnoreCase("order"))
				step = "  [Step 2 of 9]";
			else
				step = "  [Step 2 of 8]";
			shell.setText("Import Jobs" + step);
			final Group jobGroup = new Group(shell, SWT.NONE);
			jobGroup.setText("Job");
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.marginWidth = 10;
			gridLayout_3.marginTop = 5;
			gridLayout_3.marginBottom = 10;
			gridLayout_3.marginHeight = 10;
			gridLayout_3.marginLeft = 10;
			gridLayout_3.numColumns = 3;
			jobGroup.setLayout(gridLayout_3);
			final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData_6.minimumWidth = 400;
			jobGroup.setLayoutData(gridData_6);
			Composite composite;
			final Label jobnameLabel_1 = new Label(jobGroup, SWT.NONE);
			jobnameLabel_1.setLayoutData(new GridData());
			jobnameLabel_1.setText("Jobname");
			{
				txtJobname = new Text(jobGroup, SWT.BORDER);
				txtJobname.setFocus();
				final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
				txtJobname.setLayoutData(gridData);
				if (listener != null)
					txtJobname.setBackground(Options.getRequiredColor());
				if (joblistener != null) {
					if (joblistener.getJob().getName().equals("start_job")) {
						txtJobname.setText(Utils.getAttributeValue("job", joblistener.getJob()));
					}
					else
						if (joblistener.getJob().getName().equals("order")) {
							txtJobname.setText(" ");
						}
						else
							txtJobname.setText(joblistener.getName());
				}
				else {
					txtJobname.setText("");
				}
			}
			new Label(jobGroup, SWT.NONE);
			final Label titelLabel = new Label(jobGroup, SWT.NONE);
			titelLabel.setLayoutData(new GridData());
			titelLabel.setText("Titel");
			txtTitle = new Text(jobGroup, SWT.BORDER);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData.widthHint = 420;
			txtTitle.setLayoutData(gridData);
			if (joblistener != null) {
				txtTitle.setText(joblistener.getTitle());
			}
			new Label(jobGroup, SWT.NONE);
			final Label pathLabel = new Label(jobGroup, SWT.NONE);
			pathLabel.setLayoutData(new GridData());
			pathLabel.setText("Path");
			txtPath = new Text(jobGroup, SWT.BORDER);
			txtPath.setEditable(false);
			if (joblistener != null) {
				txtPath.setText(joblistener.getInclude());
			}
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_1.widthHint = 420;
			txtPath.setLayoutData(gridData_1);
			new Label(jobGroup, SWT.NONE);
			final Composite composite_3 = new Composite(jobGroup, SWT.NONE);
			final GridData gridData_7 = new GridData(103, SWT.DEFAULT);
			composite_3.setLayoutData(gridData_7);
			final GridLayout gridLayout_4 = new GridLayout();
			gridLayout_4.marginWidth = 0;
			composite_3.setLayout(gridLayout_4);
			butCancel = new Button(composite_3, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
				}
			});
			butCancel.setText("Cancel");
			composite = new Composite(jobGroup, SWT.NONE);
			final GridData gridData_8 = new GridData(GridData.END, GridData.CENTER, false, false);
			composite.setLayoutData(gridData_8);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 0;
			gridLayout_2.verticalSpacing = 0;
			gridLayout_2.numColumns = 6;
			composite.setLayout(gridLayout_2);
			{
				butdescription = new Button(composite, SWT.NONE);
				butdescription.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
							if (txtPath.getText() != null && txtPath.getText().length() > 0) {
								Program prog = Program.findProgram("html");
								if (prog != null)
									prog.execute(new File(txtPath.getText()).toURL().toString());
								else {
									Runtime.getRuntime().exec(Options.getBrowserExec(new File(txtPath.getText()).toURL().toString(), Options.getLanguage()));
								}
							}
							else {
								MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("no_jobdescription"), SWT.ICON_WARNING | SWT.OK);
							}
						}
						catch (Exception ex) {
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ;could not open description " + txtJobname.getText(), ex);
							}
							catch (Exception ee) {
								// tu nichts
							}
							System.out.println("..could not open description " + txtJobname.getText() + " " + ex);
						}
					}
				});
				butdescription.setText("Description");
			}
			butShow = new Button(composite, SWT.NONE);
			butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					HashMap attr = getJobFromDescription();
					JobAssistentImportJobParamsForm defaultParams = new JobAssistentImportJobParamsForm();
					ArrayList listOfParams = defaultParams.parseDocuments(txtPath.getText(), "");
					attr.put("params", listOfParams);
					Element job = null;
					if (flagBackUpJob) {
						if (assistentType == Editor.JOB_WIZZARD) {
							// Starten der Wizzard für bestehende Job. Die Einstzellungen im Jobbeschreibungen mergen mit backUpJob wenn
							// assistentype = Editor.Job_Wizzard
							Element currJob = (Element) (joblistener.getJob().clone());
							job = listener.createJobElement(attr, currJob);
						}
						else {
							job = listener.createJobElement(attr);
						}
					}
					else {
						job = (Element) (jobBackUp.clone());
					}
					Utils.showClipboard(Utils.getElementAsString(job), shell, false, null, false, null, false);
					job.removeChildren("param");
				}
			});
			butShow.setText("Show");
			{
				butImport = new Button(composite, SWT.NONE);
				butImport.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						try {
							if (!check())
								return;
							HashMap h = getJobFromDescription();
							if (jobname != null)
								jobname.setText(txtJobname.getText());
							JobAssistentImportJobParamsForm defaultParams = new JobAssistentImportJobParamsForm();
							ArrayList listOfParams = defaultParams.parseDocuments(txtPath.getText(), "required");
							h.put("params", listOfParams);
							if (assistentType == Editor.JOB_WIZZARD) {
								// Starten der Wizzard für bestehende Job. Die Einstzellungen im Jobbeschreibungen mergen mit backUpJob wenn
								// assistentype = Editor.Job_Wizzard
								Element job = joblistener.getJob();
								job = job.setContent(listener.createJobElement(h, joblistener.getJob()).cloneContent());
								if (jobForm != null)// diese Zeile löschen
									jobForm.initForm();
								if (jobDocForm != null)
									jobDocForm.initForm();
							}
							else
								if (assistentType == Editor.PARAMETER) {
									// Starten der Wizzard für bestehende Job. Die Einstzellungen im Jobbeschreibungen mergen mit backUpJob
									// wenn assistentype = Editor.Job_Wizzard
									// joblistener.getJob().setContent(listener.createJobElement(h, joblistener.getJob()).cloneContent());
									Element job = joblistener.getJob();
									if (job.getName().equals("job")) {
										job = job.setContent(listener.createJobElement(h, joblistener.getJob()).cloneContent());
										paramListener.fillParams(tParameter);
									}
									else
										paramListener.fillParams(listOfParams, tParameter, false);
								}
								else {
									if (listener.existJobname(txtJobname.getText())) {
										MainWindow.message(shell, Messages.getString("assistent.error.job_name_exist"), SWT.OK);
										txtJobname.setFocus();
										return;
									}
									Element job = null;
									if (flagBackUpJob) {
										job = listener.createJobElement(h);
									}
									else {
										job = joblistener.getJob();
										job = job.setContent(jobBackUp.cloneContent());
									}
									listener.newImportJob(job, assistentType);
									if (Options.getPropertyBoolean("editor.job.show.wizard"))
										Utils.showClipboard(Utils.getElementAsString(job), shell, false, null, false, null, true);
								}
							closeDialog = true;
							// Event auslösen
							if (refreshDetailsText != null)
								refreshDetailsText.setText("X");
							shell.dispose();
						}
						catch (Exception ex) {
							try {
								new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
							}
							catch (Exception ee) {
								// tu nichts
							}
							System.err.print(ex.getMessage());
						}
					}
				});
			}
			butImport.setText("Finish");
			butBack = new Button(composite, SWT.NONE);
			butBack.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					JobAssistentTypeForms typeForms = new JobAssistentTypeForms(dom, update);
					typeForms.showTypeForms(jobType, jobBackUp, assistentType);
					closeDialog = true;
					shell.dispose();
				}
			});
			butBack.setText("Back");
			butParameters = new Button(composite, SWT.NONE);
			butParameters.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			butParameters.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butParameters.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					Utils.startCursor(shell);
					if (!check())
						return;
					HashMap attr = getJobFromDescription();
					if (assistentType == Editor.JOB_WIZZARD || assistentType == Editor.JOB) {
						Element job = listener.createJobElement(attr, joblistener.getJob());
						JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(joblistener.get_dom(), joblistener.get_main(), job,
								assistentType);
						paramsForm.setBackUpJob(jobBackUp, jobForm);
						paramsForm.setJobForm(jobForm);
						paramsForm.showAllImportJobParams(txtPath.getText());
					}
					else
						if (assistentType == Editor.PARAMETER) {
							JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(joblistener.get_dom(), joblistener.get_main(),
									joblistener, tParameter, assistentType);
							paramsForm.showAllImportJobParams(txtPath.getText());
						}
						else {
							if (assistentType != Editor.JOB_WIZZARD && listener.existJobname(txtJobname.getText())) {
								MainWindow.message(shell, Messages.getString("assistent.error.job_name_exist"), SWT.OK);
								txtJobname.setFocus();
								return;
							}
							Element job = null;
							if (flagBackUpJob) {
								if (jobBackUp != null && assistentType != Editor.JOB_WIZZARD) {
									int cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.discard_changes"),
											SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
									if (cont == SWT.CANCEL) {
										return;
									}
									else
										if (cont != SWT.YES) {
											job = joblistener.getJob().setContent(jobBackUp.cloneContent());
										}
								}
							}
							else {
								// der backUpJob wurde nicht verändert
								job = joblistener.getJob().setContent(jobBackUp.cloneContent());
							}
							if (job == null) {
								job = listener.createJobElement(attr);
							}
							JobAssistentImportJobParamsForm paramsForm = null;
							if (assistentType == Editor.JOB_WIZZARD) {
								paramsForm = new JobAssistentImportJobParamsForm(dom, update, joblistener, assistentType);
							}
							else {
								paramsForm = new JobAssistentImportJobParamsForm(dom, update, job, assistentType);
							}
							paramsForm.showAllImportJobParams(txtPath.getText());
							if (jobname != null)
								paramsForm.setJobname(jobname);
							paramsForm.setBackUpJob(jobBackUp, jobForm);
						}
					closeDialog = true;
					Utils.stopCursor(shell);
					shell.dispose();
				}
			});
			butParameters.setText("Next");
			Utils.createHelpButton(composite, "assistent.import_jobs", shell);
			if (assistentType == Editor.JOB) {
				this.butImport.setVisible(true);
				butParameters.setText("Import Parameters");
			}
			if (assistentType == Editor.JOB_WIZZARD) {
				txtJobname.setEnabled(false);
				txtTitle.setEnabled(true);
				butShow.setEnabled(true);
				butBack.setEnabled(true);
			}
			else
				if (assistentType == Editor.JOB) {
					txtJobname.setEnabled(false);
					txtTitle.setEnabled(false);
					butShow.setEnabled(false);
					butBack.setEnabled(false);
				}
				else
					if (assistentType == Editor.JOB_CHAINS) {
						txtJobname.setEnabled(true);
						txtTitle.setEnabled(true);
						butShow.setEnabled(true);
						butBack.setEnabled(false);
					}
					else {
						txtJobname.setEnabled(true);
						txtTitle.setEnabled(true);
						butShow.setEnabled(true);
						butBack.setEnabled(true);
					}
			if (joblistener != null) {
				if (joblistener.getJob().getName().equals("start_job") || joblistener.getJob().getName().equals("process")
						|| joblistener.getJob().getName().equals("order") || joblistener.getJob().getName().equals("config")) {
					txtJobname.setEnabled(false);
				}
			}
			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			shell.setBounds((screen.width - shell.getBounds().width) / 2, (screen.height - shell.getBounds().height) / 2, shell.getBounds().width,
					shell.getBounds().height);
			final Group jobnamenGroup = new Group(shell, SWT.NONE);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginTop = 5;
			gridLayout_1.marginRight = 5;
			gridLayout_1.marginLeft = 5;
			jobnamenGroup.setLayout(gridLayout_1);
			jobnamenGroup.setText("Jobs");
			final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, true, true);
			gridData_3.heightHint = 154;
			jobnamenGroup.setLayoutData(gridData_3);
			jobnamenGroup.getBounds().height = 100;
			tree = new Tree(jobnamenGroup, SWT.FULL_SELECTION | SWT.BORDER);
			tree.setHeaderVisible(true);
			tree.getBounds().height = 100;
			tree.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					String strT = txtTitle.getText();
					if (strT.trim().equalsIgnoreCase("")) {
					txtTitle.setText(tree.getSelection()[0].getText(1));
					}
					txtPath.setText(tree.getSelection()[0].getText(2));
					txtJobname.setFocus();
					flagBackUpJob = true;
				}
			});
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
			tree.setLayoutData(gridData_2);
			TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
			column1.setText("Name");
			column1.setWidth(165);
			TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
			column2.setText("Title");
			column2.setWidth(200);
			TreeColumn column3 = new TreeColumn(tree, SWT.LEFT);
			column3.setText("Filename");
			column3.setWidth(209);
			try {
				createTreeItems();
			}
			catch (Exception e) {
				try {
					new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
				}
				catch (Exception ee) {
					// tu nichts
				}
				System.err.print(e.getMessage());
			}
			if (joblistener != null) {
				selectTree();
			}
			setToolTipText();
			shell.layout();
			shell.pack();
			shell.open();
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println("error in JobAssistentImportJobsForm.showAllImportJob(): " + e.getMessage());
		}
	}

	private void createTreeItems() throws Exception {
		try {
			// ermöglicht das Startet der Wizzard ohne Jobbeschreibung
			final TreeItem newItemTreeItem_ = new TreeItem(tree, SWT.NONE);
			newItemTreeItem_.setText(0, "no job description");
			newItemTreeItem_.setText(1, "..");
			newItemTreeItem_.setText(2, "..");
			Element j = new Element("job");
			Utils.setAttribute("order", (jobType.equals("order") ? "yes" : "no"), j);
			newItemTreeItem_.setData(j);
			
			ArrayList listOfDoc = parseDocuments();
			
			String filename = "";
			String lastParent = "";
			TreeItem parentItemTreeItem = null;
			boolean loop = true;
			for (int i = 0; i < listOfDoc.size(); i++) {
				HashMap h = (HashMap) listOfDoc.get(i);
				loop = true;
				if (jobType != null && jobType.equals("order")) {
					Element job = (Element) h.get("job");
					if (!(Utils.getAttributeValue("order", job).equals("yes") || Utils.getAttributeValue("order", job).equals("both"))) {
						loop = false;
					}
				}
				else
					if (jobType != null && jobType.equals("standalonejob")) {
						Element job = (Element) h.get("job");
						if (!(Utils.getAttributeValue("order", job).equals("no") || Utils.getAttributeValue("order", job).equals("both"))) {
							loop = false;
						}
					}
				if (loop) {
					filename = h.get("filename").toString();
					if (new File(filename).getParentFile().equals(new File(xmlPaths))) {
						final TreeItem newItemTreeItem = new TreeItem(tree, SWT.NONE);
						newItemTreeItem.setText(0, h.get("name").toString());
						newItemTreeItem.setText(1, h.get("title").toString());
						newItemTreeItem.setText(2, filename);
						newItemTreeItem.setData(h.get("job"));
					}
					else {
						if (!lastParent.equalsIgnoreCase(new File(filename).getParentFile().getPath())) {
							if (!new File(lastParent).getName().equals(tree.getItems()[tree.getItems().length - 1].getText())) {
								parentItemTreeItem = new TreeItem(tree, SWT.NONE);
								parentItemTreeItem.setText(0, new File(filename).getParentFile().getName());
								parentItemTreeItem.setData(h.get("job"));
								lastParent = new File(filename).getParentFile().getPath();
							}
							else {
								parentItemTreeItem = new TreeItem(parentItemTreeItem, SWT.NONE);
								parentItemTreeItem.setText(0, new File(filename).getParentFile().getName());
								parentItemTreeItem.setData(h.get("job"));
								lastParent = new File(filename).getParentFile().getPath();
							}
						}
						final TreeItem newItemTreeItem = new TreeItem(parentItemTreeItem, SWT.NONE);
						newItemTreeItem.setText(0, h.get("name").toString());
						newItemTreeItem.setText(1, h.get("title").toString());
						newItemTreeItem.setText(2, filename);
						newItemTreeItem.setData(h.get("job"));
					}
				}
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.out.println("error in JobAssistentImportJobsForm.createTreeIteam(): " + e.getMessage());
		}
	}

	public void setToolTipText() {
		butImport.setToolTipText(Messages.getTooltip("butImport"));
		butParameters.setToolTipText(Messages.getTooltip("butParameters"));
		butdescription.setToolTipText(Messages.getTooltip("butdescription"));
		tree.setToolTipText(Messages.getTooltip("tree"));
		txtJobname.setToolTipText(Messages.getTooltip("jobname"));
		txtTitle.setToolTipText(Messages.getTooltip("jobtitle"));
		txtPath.setToolTipText(Messages.getTooltip("jobdescription"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
		if (butCancel != null)
			butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		if (butShow != null)
			butShow.setToolTipText(Messages.getTooltip("assistent.show"));
	}

	/**
	 * Felder und Attribute werden aus der Jobdokumnetation genommen und in eine hashMap gepackt.
	 * @return HashMap
	 */
	private HashMap getJobFromDescription() {
		HashMap h = new HashMap();
		try {
			// elMain ist ein Job Element der Jobbeschreibung
			if (tree.getSelection().length == 0)
				return h;
			Element elMain = (Element) tree.getSelection()[0].getData();
			// Attribute der Job bestimmen
			if (jobType != null && jobType.trim().length() > 0)
				h.put("order", jobType.equalsIgnoreCase("order") ? "yes" : "no");
			h.put("tasks", elMain.getAttributeValue("tasks"));
			h.put("name", txtJobname.getText());
			h.put("title", txtTitle.getText());
			// relativen pfad bestimmen
			String sData = sos.scheduler.editor.app.Options.getSchedulerData().replaceAll("\\\\", "/");
			String currPath = txtPath.getText().replaceAll("\\\\", "/");
			if (new File(currPath).getPath().indexOf(new File(sData).getPath()) > -1) {
				h.put("filename", currPath.substring(sData.length() + 1));
			}
			else {
				h.put("filename", txtPath.getText());
			}
			// Element script
			Element script = elMain.getChild("script", elMain.getNamespace());
			if (script != null) {
				// hilfsvariable: es gibt script informationen
				h.put("script", "script");
				if (script.getAttributeValue("language") != null)
					h.put("script_language", script.getAttributeValue("language"));
				if (script.getAttributeValue("java_class") != null)
					h.put("script_java_class", script.getAttributeValue("java_class"));
				if (script.getAttributeValue("com_class") != null)
					h.put("script_com_class", script.getAttributeValue("com_class"));
				if (script.getAttributeValue("filename") != null)
					h.put("script_filename", script.getAttributeValue("filename"));
				if (script.getAttributeValue("use_engine") != null)
					h.put("script_use_engine", script.getAttributeValue("use_engine"));
				// script includes bestimmen
				List comClassInclude = script.getChildren("include", elMain.getNamespace());
				ArrayList listOfIncludeFilename = new ArrayList();
				for (int i = 0; i < comClassInclude.size(); i++) {
					Element inc = (Element) comClassInclude.get(i);
					listOfIncludeFilename.add(inc.getAttribute("file").getValue());
				}
				h.put("script_include_file", listOfIncludeFilename);
				// welche Library wurde hier verwendet? interne verwendung
				if (script.getAttributeValue("resource") != null) {
					String lib = script.getAttributeValue("resource");
					if (lib.length() > 0) {
						Element rese = elMain.getParentElement().getChild("resources", elMain.getNamespace());
						if (rese != null) {
							List r = rese.getChildren("file", elMain.getNamespace());
							if (r != null) {
								for (int i = 0; i < r.size(); i++) {
									Element res = (Element) r.get(i);
									if (Utils.getAttributeValue("id", res) != null && Utils.getAttributeValue("id", res).equals(lib)) {
										if (Utils.getAttributeValue("file", res) != null)
											h.put("library", Utils.getAttributeValue("file", res));
										JobListener.setLibrary(Utils.getAttributeValue("file", res));
									}
								}
							}
						}
					}
				}
			}
			// Element monitor
			Element monitor = elMain.getChild("monitor", elMain.getNamespace());
			if (monitor != null) {
				// hilfsvariable: es gibt Monitor Informationen
				h.put("monitor", "monitor");
				Element mon_script = monitor.getChild("script", elMain.getNamespace());
				if (mon_script != null) {
					if (mon_script.getAttributeValue("language") != null)
						h.put("monitor_script_language", mon_script.getAttributeValue("language"));
					if (mon_script.getAttributeValue("java_class") != null)
						h.put("monitor_script_java_class", mon_script.getAttributeValue("java_class"));
					if (mon_script.getAttributeValue("com_class") != null)
						h.put("monitor_script_com_class", mon_script.getAttributeValue("com_class"));
					if (mon_script.getAttributeValue("filename") != null)
						h.put("monitor_script_filename", mon_script.getAttributeValue("filename"));
					if (mon_script.getAttributeValue("use_engine") != null)
						h.put("monitor_script_use_engine", mon_script.getAttributeValue("use_engine"));
					// script monitor includes bestimmen
					List comClassInclude = mon_script.getChildren("include", elMain.getNamespace());
					ArrayList listOfIncludeFilename = new ArrayList();
					for (int i = 0; i < comClassInclude.size(); i++) {
						Element inc = (Element) comClassInclude.get(i);
						listOfIncludeFilename.add(inc.getAttribute("file").getValue());
					}
					h.put("monitor_script_include_file", listOfIncludeFilename);
				}
			}
			// Element process aus der Dokumentation zu Execute aus der Konfiguration
			Element process = elMain.getChild("process", elMain.getNamespace());
			if (process != null) {
				h.put("process", "process"); // hilfsvariable: es gibt proces Informationen
				if (process.getAttributeValue("file") != null)
					h.put("process_file", process.getAttributeValue("file"));
				if (process.getAttributeValue("param") != null)
					h.put("process_param", process.getAttributeValue("param"));
				if (process.getAttributeValue("log") != null)
					h.put("process_log", process.getAttributeValue("log"));
				// environment Variablen bestimmen
				Element environment = process.getChild("environment", elMain.getNamespace());
				if (environment != null) {
					List listOfEnvironment = environment.getChildren("variable", elMain.getNamespace());
					ArrayList listOfIncludeFilename = new ArrayList();
					for (int i = 0; i < listOfEnvironment.size(); i++) {
						HashMap hEnv = new HashMap();
						Element env = (Element) listOfEnvironment.get(i);
						hEnv.put("name", (env.getAttribute("name") != null ? env.getAttribute("name").getValue() : ""));
						hEnv.put("value", (env.getAttribute("value") != null ? env.getAttribute("value").getValue() : ""));
						listOfIncludeFilename.add(hEnv);
					}
					h.put("process_environment", listOfIncludeFilename);
				}
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.out.println("..error in JobAssistentImportJobsForm.getJobFromDescription() " + e.getMessage());
		}
		return h;
	}

	private void close() {
		int cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		if (cont == SWT.OK) {
			if (jobBackUp != null)
				joblistener.getJob().setContent(jobBackUp.cloneContent());
			shell.dispose();
		}
	}

	private void selectTree() {
		if (joblistener != null && (joblistener.getInclude() == null || joblistener.getInclude().length() == 0)) {
			TreeItem[] si = new TreeItem[1];
			si[0] = tree.getItem(0);
			tree.setSelection(si);
			return;
		}
		if (tree != null) {
			for (int i = 0; i < tree.getItemCount(); i++) {
				TreeItem item = tree.getItem(i);
				if (item.getText(2) != null) {
					String it = new File(item.getText(2)).getName();
					String in = new File(joblistener.getInclude()).getName();
					if (it.endsWith(in)) {
						TreeItem[] si = new TreeItem[1];
						si[0] = item;
						tree.setSelection(si);
						flagBackUpJob = false;
						break;
					}
				}
			}
		}
	}

	/**
	 * Der Wizzard wurde für ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne Änderungen wieder zurückgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobMainForm jobForm_) {
		if (backUpJob != null)
			jobBackUp = (Element) backUpJob.clone();
		if (jobForm_ != null)
			jobForm = jobForm_;
		if (backUpJob != null)
			selectTree();
	}

	public void setJobForm(JobMainForm jobForm_) {
		jobForm = jobForm_;
	}

	public void setJobForm(JobDocumentationForm jobDocForm_) {
		jobDocForm_ = jobDocForm;
	}

	private boolean check() {
		if (tree.getSelectionCount() == 0) {
			MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.error.no_job_selected"), SWT.ICON_WARNING | SWT.OK);
			txtJobname.setFocus();
			return false;
		}
		if (assistentType != Editor.JOB && (joblistener != null && !joblistener.getJob().getName().equals("config"))) {
			if (txtJobname.isEnabled()) {
				if (txtJobname.getText() == null || txtJobname.getText().length() == 0) {
					MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.error.no_jobname"), SWT.ICON_WARNING | SWT.OK);
					txtJobname.setFocus();
					return false;
				}
			}
			if (txtJobname.getText().concat(".xml").equalsIgnoreCase(new File(txtPath.getText()).getName())) {
				int cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.error.edit_jobname"), SWT.ICON_QUESTION | SWT.YES
						| SWT.NO);
				if (cont == SWT.YES) {
					txtJobname.setFocus();
					return false;
				}
			}
		}
		return true;
	}

	// Details hat einen anderen Aufbau der Parameter Description.
	// Beim generieren der Parameter mit Wizzard müssen die Parameterdescriptchen anders aufgebaut werden.
	public void setDetailsRefresh(Text refreshDetailsText_) {
		refreshDetailsText = refreshDetailsText_;
	}
}
