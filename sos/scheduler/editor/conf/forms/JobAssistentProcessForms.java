/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
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

import com.swtdesigner.SWTResourceManager;

public class JobAssistentProcessForms {
	
    private Element           job          = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private JobsListener      listener     = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;		
	
	
	public JobAssistentProcessForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}

	public void showProcessForm(Element job_) {
		job = job_;
		final Shell processShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		processShell.setLayout(gridLayout);
		processShell.setSize(400, 236);
		processShell.setText("Process");

		{
			final Text txtProcess = new Text(processShell, SWT.MULTI);
			txtProcess.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
			gridData.heightHint = 103;
			gridData.widthHint = 384;
			txtProcess.setLayoutData(gridData);
			txtProcess.setText(Messages.getString("assistent.process"));
		}

		{
			final Label lblFile = new Label(processShell, SWT.NONE);
			lblFile.setLayoutData(new GridData());
			lblFile.setText("File");
		}

		{
			final Text txtFile = new Text(processShell, SWT.NONE);
			txtFile.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtFile.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
			gridData.widthHint = 327;
			txtFile.setLayoutData(gridData);
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("file", process) != null &&
					Utils.getAttributeValue("file", process).trim().length() > 0	){
				txtFile.setText(Utils.getAttributeValue("file", process));
			}
		}

		{
			final Label lblParameter = new Label(processShell, SWT.NONE);
			lblParameter.setLayoutData(new GridData());
			lblParameter.setText("Parameter");
		}

		{
			final Text txtParameter = new Text(processShell, SWT.NONE);
			txtParameter.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtParameter.setEditable(false);
			txtParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("param", process) != null &&
					Utils.getAttributeValue("param", process).trim().length() > 0	){
				txtParameter.setText(Utils.getAttributeValue("param", process));
			}
		}

		{
			final Label lblRessources = new Label(processShell, SWT.NONE);
			lblRessources.setLayoutData(new GridData());
			lblRessources.setText("Log File");
			
		}

		{
			final Text txtLog = new Text(processShell, SWT.NONE);
			txtLog.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtLog.setEditable(false);
			txtLog.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("log_file", process) != null &&
					Utils.getAttributeValue("log_file", process).trim().length() > 0	){
				txtLog.setText(Utils.getAttributeValue("log_file", process));
			}
		}

		{
			butFinish = new Button(processShell, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					listener.newImportJob(job);
					processShell.dispose();
				}
			});
			butFinish.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(processShell, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					processShell.dispose();
				}
			});
			butCancel.setText("Cancel");
		}
		{
			butNext = new Button(processShell, SWT.NONE);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					MainWindow.message(processShell, "offen???????", SWT.OK );					
				}
			});
			butNext.setText("Next");
		}
		setToolTipText();
		processShell.open();

		{
			butShow = new Button(processShell, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					MainWindow.message(processShell, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setLayoutData(new GridData());
			butShow.setText("Show");
		}
		processShell.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));				
	}

}
