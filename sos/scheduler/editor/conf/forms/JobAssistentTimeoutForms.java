package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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


public class JobAssistentTimeoutForms {
	
	private Element           job          = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private JobsListener      listener     = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;				
	
	private Text              txtIdleTimeout = null;
	
	private Combo             comboForceIdleTimeout = null;
	
	private Text              txtTimeout     = null;
	
	private Label             lblForceIdleTimeout    = null; 
	
	private Label             lblTimeout       = null; 
	
	private Label             lblIdleTimeout  = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	public JobAssistentTimeoutForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}



	public void showTimeOutForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		final Shell shellTimeout = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		shellTimeout.setLayout(gridLayout);
		shellTimeout.setSize(523, 378);
		shellTimeout.setText("Timeout");

		{
			final Group jobGroup = new Group(shellTimeout, SWT.NONE);
			jobGroup.setText("Job");
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData.heightHint = 327;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);

			{
				Text txtTime = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
				gridData_1.heightHint = 175;
				gridData_1.widthHint = 448;
				txtTime.setLayoutData(gridData_1);
				txtTime.setEditable(false);
				txtTime.setText(Messages.getString("assistent.timeout"));
			}

			{
				lblForceIdleTimeout = new Label(jobGroup, SWT.NONE);
				lblForceIdleTimeout.setText("Force Idle Timeout");
			}

			{
				comboForceIdleTimeout = new Combo(jobGroup, SWT.NONE);
				comboForceIdleTimeout.setItems(new String[] {"yes", "no"});
				comboForceIdleTimeout.setLayoutData(new GridData(94, SWT.DEFAULT));
				comboForceIdleTimeout.setText(Utils.getAttributeValue("force_idle_timeout", job));
				comboForceIdleTimeout.select(1);
			}
			

			{
				lblTimeout = new Label(jobGroup, SWT.NONE);
				lblTimeout.setText("Timeout");
			}
			txtTimeout = new Text(jobGroup, SWT.BORDER);
			txtTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtTimeout.setText(Utils.getAttributeValue("timeout", job));

			{
				lblIdleTimeout = new Label(jobGroup, SWT.NONE);
				lblIdleTimeout.setText("Idle Timeout");
				
			}
			txtIdleTimeout = new Text(jobGroup, SWT.BORDER);
			txtIdleTimeout.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			txtIdleTimeout.setText(Utils.getAttributeValue("idle_timeout", job));

			{
				final Composite composite = new Composite(jobGroup, SWT.NONE);
				composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.marginWidth = 0;
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butFinish = new Button(composite, SWT.NONE);
					butFinish.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							//listener.newImportJob(job);
							if(comboForceIdleTimeout.getSelectionIndex() > 0 ) {
								job.setAttribute("force_idle_timeout", comboForceIdleTimeout.getItem(comboForceIdleTimeout.getSelectionIndex()));
							} else {
								job.removeAttribute("force_idle_timeout");
							}
							if(txtIdleTimeout.getText() != null && txtIdleTimeout.getText().length() > 0 ) {
								job.setAttribute("idle_timeout", txtIdleTimeout.getText());						 
							} else {
								job.removeAttribute("idle_timeout");
							}
							if(txtTimeout.getText() != null && txtTimeout.getText().length() > 0 ) {
								job.setAttribute("timeout", txtTimeout.getText());						 
							} else {
								job.removeAttribute("timeout");
							}
							listener.newImportJob(job, assistentType);
							shellTimeout.dispose();
						}
					});
					butFinish.setText("Finish");
				}
				{
					butCancel = new Button(composite, SWT.NONE);
					butCancel.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							shellTimeout.dispose();
						}
					});
					butCancel.setText("Cancel");
				}
			}

			{
				final Composite composite = new Composite(jobGroup, SWT.NONE);
				composite.setLayoutData(new GridData(GridData.END, GridData.FILL, false, false));
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.marginWidth = 0;
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butShow = new Button(composite, SWT.NONE);
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(comboForceIdleTimeout.getSelectionIndex() > 0 ) {
								job.setAttribute("force_idle_timeout", comboForceIdleTimeout.getItem(comboForceIdleTimeout.getSelectionIndex()));
							} else {
								job.removeAttribute("force_idle_timeout");
							}
							if(txtIdleTimeout.getText() != null && txtIdleTimeout.getText().length() > 0 ) {
								job.setAttribute("idle_timeout", txtIdleTimeout.getText());						 
							} else {
								job.removeAttribute("idle_timeout");
							}
							if(txtTimeout.getText() != null && txtTimeout.getText().length() > 0 ) {
								job.setAttribute("timeout", txtTimeout.getText());						 
							} else {
								job.removeAttribute("timeout");
							}
							MainWindow.message(shellTimeout, Utils.getElementAsString(job), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
					butNext.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(comboForceIdleTimeout.getSelectionIndex() > 0 ) {
								job.setAttribute("force_idle_timeout", comboForceIdleTimeout.getItem(comboForceIdleTimeout.getSelectionIndex()));
							} else {
								job.removeAttribute("force_idle_timeout");
							}
							if(txtIdleTimeout.getText() != null && txtIdleTimeout.getText().length() > 0 ) {
								job.setAttribute("idle_timeout", txtIdleTimeout.getText());						 
							} else {
								job.removeAttribute("idle_timeout");
							}
							if(txtTimeout.getText() != null && txtTimeout.getText().length() > 0 ) {
								job.setAttribute("timeout", txtTimeout.getText());						 
							} else {
								job.removeAttribute("timeout");
							}
							JobAssistentRunOptionsForms runOptions = new JobAssistentRunOptionsForms(dom, update);
							runOptions.showRunOptionsForm(job, assistentType);
							shellTimeout.dispose();
							//MainWindow.message(shellTimeout, "run options??????", SWT.OK );					
						}
					});
					butNext.setText("Next");
				}
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("language", script) != null &&
					Utils.getAttributeValue("language", script).trim().length() > 0	){
			}
		}

		{
			if(job != null) {
				if(Utils.getAttributeValue("timeout", job) != null &&
						Utils.getAttributeValue("timeout", job).trim().length() > 0	){
				}
				if(Utils.getAttributeValue("idle_timeout", job) != null &&
						Utils.getAttributeValue("idle_timeout", job).trim().length() > 0	){
					txtIdleTimeout.setText(Utils.getAttributeValue("idle_timeout", job));
				}
				if(Utils.getAttributeValue("force_idle_timeout", job) != null &&
						Utils.getAttributeValue("force_idle_timeout", job).trim().length() > 0	){
				}
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("filename", script) != null &&
					Utils.getAttributeValue("filename", script).trim().length() > 0	){
			}
		}

		{
			if(job != null){
				Element script = job.getChild("script");				 
				if(script!= null) {
					java.util.List includes = script.getChildren("include");
					for (int i = 0; i < includes.size(); i++) {
						Element include = (Element)includes.get(i);
						if( Utils.getAttributeValue("file", include) != null &&
								Utils.getAttributeValue("file", include).length() > 0) {
						}
					}
					
				}
				
			}
		}
		
		shellTimeout.open();
		setToolTipText();
		shellTimeout.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));			
		txtIdleTimeout.setToolTipText(Messages.getTooltip("assistent.idle_timeout"));
		lblIdleTimeout.setToolTipText(Messages.getTooltip("assistent.idle_timeout"));
		comboForceIdleTimeout.setToolTipText(Messages.getTooltip("assistent.force_idle_timeout"));
		lblForceIdleTimeout.setToolTipText(Messages.getTooltip("assistent.force_idle_timeout"));
		txtTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
		lblTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
	}
}
