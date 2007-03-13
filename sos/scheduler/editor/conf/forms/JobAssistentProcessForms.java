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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
	
	private Text              txtFile      = null;
	
	private Text              txtParameter = null;
	
	private Text              txtLog       = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	public JobAssistentProcessForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}

	public void showProcessForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		final Shell processShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		processShell.setLayout(gridLayout);
		processShell.setSize(429, 366);
		processShell.setText("Process");

		{
			final Group jobGroup = new Group(processShell, SWT.NONE);
			jobGroup.setText("Job");
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
			gridData.heightHint = 318;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);

			{
				final Text txtProcess = new Text(jobGroup, SWT.MULTI);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
				gridData_1.heightHint = 175;
				gridData_1.widthHint = 394;
				txtProcess.setLayoutData(gridData_1);
				txtProcess.setEditable(false);
				txtProcess.setText(Messages.getString("assistent.process"));
			}

			{
				final Label lblFile = new Label(jobGroup, SWT.NONE);
				lblFile.setText("File");
			}
			txtFile = new Text(jobGroup, SWT.NONE);
			txtFile.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			txtFile.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtFile.setEditable(false);
			if(job != null) {
				Element process = job.getChild("process");
				if(process != null && Utils.getAttributeValue("file", process) != null &&
						Utils.getAttributeValue("file", process).trim().length() > 0	){
					txtFile.setText(Utils.getAttributeValue("file", process));
				}
			}

			{
				final Label lblParameter = new Label(jobGroup, SWT.NONE);
				lblParameter.setText("Parameter");
			}
			txtParameter = new Text(jobGroup, SWT.NONE);
			txtParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtParameter.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtParameter.setEditable(false);
			if(job != null) {
				Element process = job.getChild("process");
				if(process != null && Utils.getAttributeValue("param", process) != null &&
						Utils.getAttributeValue("param", process).trim().length() > 0	){
					txtParameter.setText(Utils.getAttributeValue("param", process));
				}
			}

			{
				final Label lblRessources = new Label(jobGroup, SWT.NONE);
				lblRessources.setLayoutData(new GridData(SWT.DEFAULT, 28));
				lblRessources.setText("Log File");
				
			}
			txtLog = new Text(jobGroup, SWT.NONE);
			txtLog.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtLog.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
			txtLog.setEditable(false);
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("log_file", process) != null &&
					Utils.getAttributeValue("log_file", process).trim().length() > 0	){
				txtLog.setText(Utils.getAttributeValue("log_file", process));
			}

			final Composite composite = new Composite(jobGroup, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.numColumns = 2;
			composite.setLayout(gridLayout_2);

			{
				butFinish = new Button(composite, SWT.NONE);
				butFinish.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {					
						listener.newImportJob(job, assistentType);
						processShell.dispose();
					}
				});
				butFinish.setText("Finish");
			}
			{
				butCancel = new Button(composite, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						processShell.dispose();
					}
				});
				butCancel.setText("Cancel");
			}

			final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
			composite_1.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.numColumns = 2;
			composite_1.setLayout(gridLayout_3);
			{
				butNext = new Button(composite_1, SWT.NONE);
				butNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update);
						timeout.showTimeOutForm(job, assistentType);
						processShell.dispose();					
					}
				});
				butNext.setText("Next");
			}

			{
				butShow = new Button(composite_1, SWT.NONE);
				butShow.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {					
						MainWindow.message(processShell, Utils.getElementAsString(job), SWT.OK );
					}
				});
				butShow.setText("Show");
			}
		}

		{
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("file", process) != null &&
					Utils.getAttributeValue("file", process).trim().length() > 0	){
			}
		}

		{
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("param", process) != null &&
					Utils.getAttributeValue("param", process).trim().length() > 0	){
			}
		}

		{
			Element process = job.getChild("process");
			if(process != null && Utils.getAttributeValue("log_file", process) != null &&
					Utils.getAttributeValue("log_file", process).trim().length() > 0	){
			}
		}
		
		processShell.open();
		setToolTipText();
		processShell.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtFile.setToolTipText(Messages.getTooltip("assistent.process_file"));
		txtParameter.setToolTipText(Messages.getTooltip("assistent.process_parameter"));
		txtLog.setToolTipText(Messages.getTooltip("assistent.process_log"));
	}

}
