/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

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

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobAssistentTasksForm {
 
	private Element           job          = null;
	
	private Text              txtTasks     = null;
 
	private Text              txtTask      = null;
	
	private JobsListener      listener     = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;	
		
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	public JobAssistentTasksForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
			
	}
	
	private void init() {
		if(job != null && Utils.getAttributeValue("tasks", job).equals("unbounded")) {
			job.removeAttribute("tasks");			
		}
	}
	
	public void showTasksForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		init();
		final Shell tasksShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		tasksShell.setLayout(gridLayout);
		tasksShell.setSize(404, 287);
		tasksShell.setText("Tasks");
		tasksShell.open();

		{
			final Group jobGroup = new Group(tasksShell, SWT.NONE);
			jobGroup.setText("Job");
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData.heightHint = 234;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);

			{
				txtTasks = new Text(jobGroup, SWT.MULTI);
				final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
				gridData_1.heightHint = 125;
				gridData_1.widthHint = 369;
				txtTasks.setLayoutData(gridData_1);
				txtTasks.setEditable(false);
				txtTasks.setText(Messages.getString("assistent.tasks"));
			}

			final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
			composite_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.numColumns = 2;
			composite_1.setLayout(gridLayout_3);

			{
				final Label tasksLabel = new Label(composite_1, SWT.NONE);
				tasksLabel.setText("Tasks");
			}
			txtTask = new Text(composite_1, SWT.BORDER);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_1.minimumWidth = 100;
			txtTask.setLayoutData(gridData_1);
			txtTask.setText(Utils.getAttributeValue("tasks", job_));
			new Label(jobGroup, SWT.NONE);

			{
				final Composite composite = new Composite(jobGroup, SWT.NONE);
				composite.setLayoutData(new GridData(97, 42));
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butFinish = new Button(composite, SWT.NONE);
					butFinish.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
								Utils.setAttribute("tasks", txtTask.getText(), job);						
							}
							listener.newImportJob(job, assistentType);
						}
					});
					butFinish.setText("Finish");
				}
				{
					butCancel = new Button(composite, SWT.NONE);
					butCancel.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							tasksShell.dispose();
						}
					});
					butCancel.setText("Cancel");
				}
			}

			final Composite composite = new Composite(jobGroup, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.END, GridData.FILL, false, false));
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.numColumns = 2;
			composite.setLayout(gridLayout_2);

			{
				butShow = new Button(composite, SWT.NONE);
				butShow.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
							Utils.setAttribute("tasks", txtTask.getText(), job);						
						}
						MainWindow.message(tasksShell, Utils.getElementAsString(job), SWT.OK );
					}
				});
				butShow.setText("Show");
			}
			{
				butNext = new Button(composite, SWT.NONE);
				butNext.setLayoutData(new GridData());
				butNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
							Utils.setAttribute("tasks", txtTask.getText(), job);						
						}
						if(job.getChild("process") != null) {
							JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update);
							process.showProcessForm(job, assistentType);						
						} else {
							JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update);
							script.showScriptForm(job, assistentType);						
						}
						tasksShell.dispose();
					}
				});
				butNext.setText("Next");
			}
		}

		{
			if(Utils.getAttributeValue("tasks", job_) != null && 
					!Utils.getAttributeValue("tasks", job_).equals("unbounded")) {
			}
		}
		setToolTipText();
		tasksShell.layout();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtTask.setToolTipText(Messages.getTooltip("assistent.task"));
		
	}
}
