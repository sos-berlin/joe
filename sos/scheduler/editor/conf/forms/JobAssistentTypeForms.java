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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobAssistentTypeForms {
	
	/** Parameter: isStandaloneJob = true -> Standalone Job, sonst Order Job*/
	private boolean isStandaloneJob = true;
	
	
	private Button radStandalonejob = null;	
	private Button radOrderjob = null;	
	private Element job = null;
	private SchedulerDom dom = null;
	private ISchedulerUpdate update = null;
	//private JobsListener listener;
	private Button butCancel = null;
	private Button butShow = null; 
	private Button butNext = null;
	private Text txtOrder = null; 
	
	/**
	 * Konstruktor 
	 * @param dom_ - Type SchedulerDom 
	 * @param update_ - Type ISchedulerUpdate
	 */
	public JobAssistentTypeForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		//listener = new JobsListener(dom, update);
	}
	
	public void showTypeForms(Element job_) {
		try {
			job = job_;
			final Shell jobTypeShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
			jobTypeShell.setParent(MainWindow.getSShell());
			//jobTypeShell.getBounds().x = MainWindow.getSShell().getBounds().x;
			//jobTypeShell.getBounds().y = MainWindow.getSShell().getBounds().y;

			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			jobTypeShell.setLayout(gridLayout);
			jobTypeShell.setSize(515, 305);
			jobTypeShell.setText("Job Type"); 
			
			{
				final Group jobGroup = new Group(jobTypeShell, SWT.NONE);
				jobGroup.setText("Job");
				final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
				gridData_1.heightHint = 207;
				gridData_1.widthHint = 482;
				jobGroup.setLayoutData(gridData_1);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.numColumns = 2;
				jobGroup.setLayout(gridLayout_1);
				
				{
					txtOrder = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
					txtOrder.setEditable(false);
					final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1);
					gridData.widthHint = 452;
					gridData.heightHint = 160;
					txtOrder.setLayoutData(gridData);
					txtOrder.setText(Messages.getString("assistent.type"));
				}
				
				{
					radStandalonejob = new Button(jobGroup, SWT.RADIO);
					radStandalonejob.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							Utils.setAttribute("order", "no", job );
						}
					});
					radStandalonejob.setSelection(true);
					final GridData gridData = new GridData(GridData.CENTER, GridData.END, false, false);
					gridData.widthHint = 158;
					radStandalonejob.setLayoutData(gridData);
					radStandalonejob.setText(Messages.getString("assistent.type.standalonejob"));
				}
				
				{
					radOrderjob = new Button(jobGroup, SWT.RADIO);
					radOrderjob.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							Utils.setAttribute("order", "yes", job );
						}
					});
					radOrderjob.setLayoutData(new GridData());
					radOrderjob.setText(Messages.getString("assistent.order.orderjob"));
				}
			}
			
			/*{
			 final Button butFinish = new Button(jobTypeShell, SWT.NONE);
			 butFinish.addSelectionListener(new SelectionAdapter() {
			 public void widgetSelected(final SelectionEvent e) {
			 listener.newImportJob(job);					
			 }
			 });
			 butFinish.setText("Finish");
			 }*/
			{
				butCancel = new Button(jobTypeShell, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						jobTypeShell.dispose();							
					}
				});
				butCancel.setText("Cancel");
			}
			
			jobTypeShell.open();

			{
				final Composite composite = new Composite(jobTypeShell, SWT.NONE);
				final GridData gridData = new GridData(GridData.END, GridData.FILL, false, false);
				gridData.heightHint = 29;
				composite.setLayoutData(gridData);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.numColumns = 2;
				composite.setLayout(gridLayout_1);
			
				{
					butShow = new Button(composite, SWT.NONE);
					butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							MainWindow.message(jobTypeShell, Utils.getElementAsString(job), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
					final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
					gridData_1.widthHint = 57;
					butNext.setLayoutData(gridData_1);
					butNext.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(radOrderjob.getSelection()) {
								isStandaloneJob = false;						
							} else {
								isStandaloneJob = true;
							}
							
							jobTypeShell.dispose();
							
							Utils.setAttribute("order", isStandaloneJob, job);
							
							ShowAllImportJobsForm importJobs = new ShowAllImportJobsForm(dom, update);
							importJobs.showAllImportJobs((isStandaloneJob ? "standalonejob" : "order"), true, Editor.JOBS);
							
							
						}
					});
					butNext.setText("Next");
				}
			}
			setToolTipText();
			jobTypeShell.layout();
			jobTypeShell.pack();		
		} catch (Exception e) {
			System.err.println("..error in JobAssistentTypeForms.showTypeForm() " + e.getMessage());
		}
	}
	
	public boolean isStandaloneJob() {
		return isStandaloneJob;
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		radOrderjob.setToolTipText(Messages.getTooltip("assistent.radio_order_job"));
		radStandalonejob.setToolTipText(Messages.getTooltip("assistent.radio_standalone_job"));
		
	}
}
