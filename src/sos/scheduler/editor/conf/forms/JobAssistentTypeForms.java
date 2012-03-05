/**
 * Created on 06.03.2007
 *
 * Wizzard: Typ des Schedulers wird angegeben. Standalone Job oder Order Job
 * 
 *  @author mo
 * 
 */
package sos.scheduler.editor.conf.forms;

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
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;

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
public class JobAssistentTypeForms {
	
	/** Parameter: isStandaloneJob = true -> Standalone Job, sonst Order Job*/
	private boolean          isStandaloneJob  = true;	
	
	private Button           radStandalonejob = null;	
	
	private Button           radOrderjob      = null;		
	
	private SchedulerDom     dom              = null;
	
	private ISchedulerUpdate update           = null;
	
	private Button           butCancel        = null;
	
	private Button           butShow          = null;
	
	private Button           butNext          = null;	 
	
	private Shell            jobTypeShell     = null;
	
	private String           jobType          = ""; 
	
	private Element          jobBackUp        = null;
	
	private int              assistentType    = Editor.JOBS;
	
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
			
			jobTypeShell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);			
						
			jobTypeShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			gridLayout.numColumns = 2;
			jobTypeShell.setLayout(gridLayout);			
			String step = "  [Step 1]";
			jobTypeShell.setText("Job Type" + step); 

			
			
			{
				final Group jobGroup = new Group(jobTypeShell, SWT.NONE);
				jobGroup.setCapture(true);
				jobGroup.setText("Job");
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
					radOrderjob = new Button(jobGroup, SWT.RADIO);					
					final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, true);
					gridData.heightHint = 48;
					radOrderjob.setLayoutData(gridData);
					radOrderjob.setSelection(jobType != null && jobType.length() > 0 && jobType.equalsIgnoreCase("order"));
					radOrderjob.setText(Messages.getString("assistent.order.orderjob"));
				}
				
				{
					radStandalonejob = new Button(jobGroup, SWT.RADIO);
					radStandalonejob.setSelection(jobType == null || jobType.length() == 0 || jobType.equalsIgnoreCase("standalonejob"));					
					final GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, true);					
					radStandalonejob.setLayoutData(gridData);
					radStandalonejob.setText(Messages.getString("assistent.type.standalonejob"));
				}
			}
			
			{
				butCancel = new Button(jobTypeShell, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {						
						close();							
					}
				});
				butCancel.setText("Cancel");
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
					butShow = new Button(composite, SWT.NONE);
					butShow.setVisible(false);
					butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							//dient nur für die Show Funktion 
							Element job = new Element("job");
							Utils.setAttribute("order", isStandaloneJob ? "yes" : "no", job);
							MainWindow.message(jobTypeShell, Utils.getElementAsString(job), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
					butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
					final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);					
					butNext.setLayoutData(gridData_1);
					butNext.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							Utils.startCursor(jobTypeShell);
							
							if(radOrderjob.getSelection()) {
								isStandaloneJob = false;						
							} else {
								isStandaloneJob = true;
							}							
							
							
							if(jobBackUp != null) {
								int cont = MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.discard_changes"), SWT.ICON_QUESTION | SWT.YES |SWT.NO |SWT.CANCEL );
								if(cont == SWT.CANCEL) {
									return;
								}else if(cont != SWT.YES) {											
									JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm( new JobListener(dom, jobBackUp, update) ,null,assistentType);
									importJobs.showAllImportJobs((Utils.getAttributeValue("order", jobBackUp).equals("yes")?"order":"standalonejob")) ;
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
					butNext.setText("Next");
				}

				
				Utils.createHelpButton(composite, "assistent.type", jobTypeShell);
			}
			
			setToolTipText();
			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			
			jobTypeShell.setBounds((screen.width - jobTypeShell.getBounds().width) /2, 
					(screen.height - jobTypeShell.getBounds().height) /2, 
					jobTypeShell.getBounds().width, 
					jobTypeShell.getBounds().height);
			jobTypeShell.open();
			jobTypeShell.layout();
			jobTypeShell.pack();		
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in JobAssistentTypeForms.showTypeForm() " + e.getMessage());
		}
	}
	
	
	
	private void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		radOrderjob.setToolTipText(Messages.getTooltip("assistent.radio_order_job"));
		radStandalonejob.setToolTipText(Messages.getTooltip("assistent.radio_standalone_job"));
		
	}
	
	private void close() {
		int cont = MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK)
			jobTypeShell.dispose();	
	}
}
