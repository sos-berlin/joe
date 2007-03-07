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
	
	public JobAssistentTasksForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
			
	}
	
	public void showTasksForm(Element job_) {
		job = job_;
		final Shell tasksShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		tasksShell.setLayout(gridLayout);
		tasksShell.setSize(400, 185);
		tasksShell.setText("Tasks");
		tasksShell.open();

		{
			txtTasks = new Text(tasksShell, SWT.MULTI);
			txtTasks.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
			gridData.heightHint = 87;
			gridData.widthHint = 383;
			txtTasks.setLayoutData(gridData);
			txtTasks.setText(Messages.getString("assistent.tasks"));
		}

		{
			final Label tasksLabel = new Label(tasksShell, SWT.NONE);
			tasksLabel.setLayoutData(new GridData());
			tasksLabel.setText("Tasks");
		}

		{
			txtTask = new Text(tasksShell, SWT.BORDER);
			txtTask.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			if(Utils.getAttributeValue("tasks", job_) != null && 
					!Utils.getAttributeValue("tasks", job_).equals("unbounded")) {
				txtTask.setText(Utils.getAttributeValue("tasks", job_));
			}
		}

		{
			butFinish = new Button(tasksShell, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
						Utils.setAttribute("tasks", txtTask.getText(), job);						
					}
					listener.newImportJob(job);
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(tasksShell, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					tasksShell.dispose();
				}
			});
			butCancel.setLayoutData(new GridData());
			butCancel.setText("Cancel");
		}
		{
			butNext = new Button(tasksShell, SWT.NONE);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
						Utils.setAttribute("tasks", txtTask.getText(), job);						
					}
					if(job.getChild("process") != null) {
						JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update);
						process.showProcessForm(job);						
					} else {
						JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update);
						script.showScriptForm(job);						
					}
					tasksShell.dispose();
				}
			});
			butNext.setLayoutData(new GridData());
			butNext.setText("Next");
		}

		{
			butShow = new Button(tasksShell, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtTask.getText() != null && txtTask.getText().trim().length() > 0) {
						Utils.setAttribute("tasks", txtTask.getText(), job);						
					}
					MainWindow.message(tasksShell, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setLayoutData(new GridData());
			butShow.setText("Show");
		}
		tasksShell.layout();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("tooltip.assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("tooltip.assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("tooltip.assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("tooltip.assistent.finish"));
		txtTask.setToolTipText(Messages.getTooltip("tooltip.assistent.task"));
		
	}
}
