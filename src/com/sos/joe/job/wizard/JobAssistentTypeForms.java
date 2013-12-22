/**
 * Created on 06.03.2007
 *
 * Wizzard: Typ des Schedulers wird angegeben. Standalone Job oder Order Job
 *
 *  @author mo
 *
 */
package com.sos.joe.job.wizard;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSClassUtil;

import com.sos.dialog.classes.WindowsSaver;
import com.sos.joe.objects.job.JobListener;
import com.swtdesigner.SWTResourceManager;

/**
 * Job Wizzard.
 *
 * Auswahl zwischen der Standalone Jobs und Auftragsgesteuerte Jobs.
 *
 * Es werden im nächsten Dialog (Job Auswahl Dialoge) entsprechend Standalone Jobs oder
 * Auftragsgesteuerte Jobs zur Auswahl gestellt.
 *
 * Die Kriterien stehen in der Job Dokumentation.
 * Das bedeutet alle Job Dokumentationen aus der Verzeichnis <SCHEDULER_HOME>/jobs/*.xml werden parsiert.
 *
 *
 */
public class JobAssistentTypeForms extends FormBaseClass<JobListener> {
	private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private final Logger		logger				= Logger.getLogger(this.getClass());
	@SuppressWarnings("unused")
	private final String		conSVNVersion		= "$Id: JobAssistentTypeForms.java 20744 2013-07-22 10:26:13Z kb $";

	private static final String	conTagNameJOB		= "job";

	/** Parameter: isStandaloneJob = true -> Standalone Job, sonst Order Job*/
	private boolean				isStandaloneJob		= true;
	private Button				radStandalonejob	= null;
	private Button				radOrderjob			= null;
	private SchedulerDom		dom					= null;
	private ISchedulerUpdate	update				= null;
	private Button				butCancel			= null;
	private Button				butShow				= null;
	private Button				butNext				= null;
	@SuppressWarnings("hiding")
	private Shell				shell				= null;
	private String				jobType				= "";
	private Element				jobBackUp			= null;
	private int					assistentType		= Editor.JOBS;

	/**
	 * Konstruktor
	 * @param dom_ - Type SchedulerDom
	 * @param update_ - Type ISchedulerUpdate
	 */
	public JobAssistentTypeForms(final SchedulerDom dom_, final ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
	}

	public void showTypeForms(final String type, final Element job, final int assistentType_) {
		jobType = type;
		jobBackUp = job;
		assistentType = assistentType_;
		showTypeForms();
	}

	public void showTypeForms() {
		try {
			shell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
			shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));

			objFormPosSizeHandler = new WindowsSaver(this.getClass(), shell, 400, 200);
			objFormPosSizeHandler.setKey(conClassName);
			objFormPosSizeHandler.restoreWindow();

			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			gridLayout.numColumns = 2;
			shell.setLayout(gridLayout);
			shell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobType.params(SOSJOEMessageCodes.JOE_M_JobAssistent_Step1.label()));
			{
				final Group jobGroup = SOSJOEMessageCodes.JOE_G_JobAssistent_JobGroup.Control(new Group(shell, SWT.NONE));
				jobGroup.setCapture(true);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, true, 2, 1);
				gridData_1.heightHint = 99;
				gridData_1.verticalIndent = -1;
				jobGroup.setLayoutData(gridData_1);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.horizontalSpacing = 15;
				gridLayout_1.marginWidth = 10;
				gridLayout_1.marginHeight = 0;
				gridLayout_1.numColumns = 2;
				jobGroup.setLayout(gridLayout_1);

				{
					radOrderjob = SOSJOEMessageCodes.JOE_B_JobAssistent_OrderJob.Control(new Button(jobGroup, SWT.RADIO));
					final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, true);
					gridData.heightHint = 48;
					radOrderjob.setLayoutData(gridData);
					radOrderjob.setSelection(jobType != null && jobType.length() > 0 && jobType.equalsIgnoreCase("order"));
				}

				{
					radStandalonejob = SOSJOEMessageCodes.JOE_B_JobAssistent_StandaloneJob.Control(new Button(jobGroup, SWT.RADIO));
					radStandalonejob.setSelection(jobType == null || jobType.length() == 0 || jobType.equalsIgnoreCase("standalonejob"));
					final GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, true);
					radStandalonejob.setLayoutData(gridData);
				}
			}

			{
				butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(shell, SWT.NONE));
				butCancel.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						close();
					}
				});
			}

			{
				final Composite composite = new Composite(shell, SWT.NONE);
				final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
				composite.setLayoutData(gridData);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.marginHeight = 0;
				gridLayout_1.marginWidth = 0;
				gridLayout_1.numColumns = 3;
				composite.setLayout(gridLayout_1);

				{
					butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite, SWT.NONE));
					butShow.setVisible(false);
					butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
					butShow.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							//dient nur für die Show Funktion
							Element job = new Element(conTagNameJOB);
							Utils.setAttribute("order", isStandaloneJob ? "yes" : "no", job);
							MainWindow.message(shell, Utils.getElementAsString(job), SWT.OK);
						}
					});
				}
				{
					butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite, SWT.NONE));
					butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
					final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
					butNext.setLayoutData(gridData_1);
					butNext.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							Utils.startCursor(shell);

							if (radOrderjob.getSelection()) {
								isStandaloneJob = false;
							}
							else {
								isStandaloneJob = true;
							}

							if (jobBackUp != null) {
								int cont = MainWindow.message(shell, SOSJOEMessageCodes.JOE_M_JobAssistent_DiscardChanges.label(), SWT.ICON_QUESTION | SWT.YES
										| SWT.NO | SWT.CANCEL);
								if (cont == SWT.CANCEL) {
									return;
								}
								else
									if (cont != SWT.YES) {
										JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(new JobListener(dom, jobBackUp, update), null,
												assistentType);
										importJobs.showAllImportJobs(Utils.getAttributeValue("order", jobBackUp).equals("yes") ? "order" : "standalonejob");
										shell.dispose();
										return;
									}
							}
							JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(dom, update, assistentType);
							importJobs.showAllImportJobs(isStandaloneJob ? "standalonejob" : "order");
							Utils.stopCursor(shell);

							shell.dispose();

						}
					});
				}

				Utils.createHelpButton(composite, "JOE_M_JobAssistentTypeForms_Help.label", shell);
			}

			shell.setDefaultButton(butNext);
			//			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			//
			//			shell.setBounds((screen.width - shell.getBounds().width) /2,
			//					(screen.height - shell.getBounds().height) /2,
			//					shell.getBounds().width,
			//					shell.getBounds().height);
			shell.open();
			shell.layout();
			objFormPosSizeHandler.restoreWindow();
			//			shell.pack();
		}
		catch (Exception e) {
			try {
				new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			System.err.println(SOSJOEMessageCodes.JOE_E_0002.params("JobAssistentTypeForms.showTypeForms()") + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void setToolTipText() {
		//
	}

	private void close() {
		int cont = MainWindow.message(shell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		if (cont == SWT.OK)
			shell.dispose();
	}

	@Override
	public void createGroup() {
		// TODO Auto-generated method stub

	}
}
