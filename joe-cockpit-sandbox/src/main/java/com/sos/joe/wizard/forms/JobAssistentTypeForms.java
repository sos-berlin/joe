/**
 * Created on 06.03.2007
 *
 * Wizzard: Typ des Schedulers wird angegeben. Standalone Job oder Order Job
 * 
 *  @author mo
 * 
 */
package com.sos.joe.wizard.forms;
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

import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

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
 * @author mueruevet.oeksuez@sos-berlin.com
 *
 */
public class JobAssistentTypeForms extends JobWizardBaseForm {
	@SuppressWarnings("unused") private final String	conSVNVersion		= "$Id: JobAssistentTypeForms.java 25898 2014-06-20 14:36:54Z kb $";
	private static final String							conTagNameJOB		= "job";
	/** Parameter: isStandaloneJob = true -> Standalone Job, sonst Order Job*/
	private boolean										isStandaloneJob		= true;
	private Button										radStandalonejob	= null;
	private Button										radOrderjob			= null;
	//	private Button										butCancel			= null;
	//	private Button										butShow				= null;
	//	private Button										butNext				= null;
	private Shell										jobTypeShell		= null;
	private String										jobType				= "";
	private Element										jobBackUp			= null;
	private int											assistentType		= JOEConstants.JOBS;

	/**
	 * Konstruktor 
	 * @param dom_ - Type SchedulerDom 
	 * @param update_ - Type ISchedulerUpdate
	 */
	public JobAssistentTypeForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
	}

	public void showTypeForms(String type, Element job, int assistentType_) {
		jobType = type;
		jobBackUp = job;
		assistentType = assistentType_;
		showTypeForms();
	}

	public void showTypeForms() {
		try {
			jobTypeShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
			jobTypeShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			gridLayout.numColumns = 2;
			jobTypeShell.setLayout(gridLayout);
			jobTypeShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobType.params(SOSJOEMessageCodes.JOE_M_JobAssistent_Step1.label()));
			{
				final Group jobGroup = SOSJOEMessageCodes.JOE_G_JobAssistent_JobGroup.Control(new Group(jobTypeShell, SWT.NONE));
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
				butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(jobTypeShell, SWT.NONE));
				butCancel.addSelectionListener(new SelectionAdapter() {
					@Override public void widgetSelected(final SelectionEvent e) {
						close();
					}
				});
			}
			{
				final Composite composite = new Composite(jobTypeShell, SWT.NONE);
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
						@Override public void widgetSelected(final SelectionEvent e) {
							//dient nur für die Show Funktion 
							Element job = new Element(conTagNameJOB);
							Utils.setAttribute("order", isStandaloneJob ? "yes" : "no", job);
							ErrorLog.message(jobTypeShell, Utils.getElementAsString(job), SWT.OK);
						}
					});
				}
				{
					butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite, SWT.NONE));
					butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
					final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
					butNext.setLayoutData(gridData_1);
					butNext.addSelectionListener(new SelectionAdapter() {
						@Override public void widgetSelected(final SelectionEvent e) {
							Utils.startCursor(jobTypeShell);
							if (radOrderjob.getSelection()) {
								isStandaloneJob = false;
							}
							else {
								isStandaloneJob = true;
							}
							if (jobBackUp != null) {
								int cont = ErrorLog.message(jobTypeShell, SOSJOEMessageCodes.JOE_M_JobAssistent_DiscardChanges.label(), SWT.ICON_QUESTION
										| SWT.YES | SWT.NO | SWT.CANCEL);
								if (cont == SWT.CANCEL) {
									return;
								}
								else
									if (cont != SWT.YES) {
										JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(new JobListener(dom, jobBackUp, update), null,
												assistentType);
										importJobs.showAllImportJobs((Utils.getAttributeValue("order", jobBackUp).equals("yes") ? "order" : "standalonejob"));
										jobTypeShell.dispose();
										return;
									}
							}
							JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(dom, update, assistentType);
							importJobs.showAllImportJobs((isStandaloneJob ? "standalonejob" : "order"));
							Utils.stopCursor(jobTypeShell);
							jobTypeShell.dispose();
						}
					});
				}
				Utils.createHelpButton(composite, "JOE_M_JobAssistentTypeForms_Help.label", jobTypeShell);
			}
			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			jobTypeShell.setBounds((screen.width - jobTypeShell.getBounds().width) / 2, (screen.height - jobTypeShell.getBounds().height) / 2,
					jobTypeShell.getBounds().width, jobTypeShell.getBounds().height);
			jobTypeShell.open();
			jobTypeShell.layout();
			jobTypeShell.pack();
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			System.err.println(SOSJOEMessageCodes.JOE_E_0002.params("JobAssistentTypeForms.showTypeForms()") + e.getMessage());
		}
	}

	private void setToolTipText() {
		//		
	}

	private void close() {
		int cont = ErrorLog.message(jobTypeShell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		if (cont == SWT.OK)
			jobTypeShell.dispose();
	}
}
