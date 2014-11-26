package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;


public class JobAssistentTimeoutOrderForms {

	private SchedulerDom      dom                    = null;

	private ISchedulerUpdate  update                 = null;

	private JobListener       joblistener            = null;

	private Button            butFinish              = null;

	private Button            butCancel              = null;

	private Button            butNext                = null;

	private Button            butShow                = null;	

	private Button            butBack                = null; 

	private Text              txtIdleTimeout         = null;	

	private Text              txtTimeout             = null;

	private Label             lblForceIdleTimeout    = null; 

	private Label             lblTimeout             = null; 

	private Label             lblIdleTimeout         = null;		 	

	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int               assistentType          = -1; 

	private Shell             shellTimeout           = null;

	private Combo             jobname                = null;

	private Element           jobBackUp              = null;  

	private JobMainForm           jobForm      = null;

	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean               closeDialog   = false;      

	
	public JobAssistentTimeoutOrderForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;		
		joblistener = new JobListener(dom, job_, update);			
	}


	public void showTimeOutForm() {

		shellTimeout = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shellTimeout.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				if(!closeDialog)
					close();
				e.doit = shellTimeout.isDisposed();
			}
		});

		shellTimeout.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shellTimeout.setLayout(gridLayout);
		shellTimeout.setSize(409, 190);
		String step = "  ";
		if (Utils.getAttributeValue("order", joblistener.getJob()).equalsIgnoreCase("yes"))
			step = step + " [Step 6 of 9]";
		else 
			step = step + " [Step 6 of 8]";
		shellTimeout.setText("Timeout" + step);				

		{
			final Group jobGroup = new Group(shellTimeout, SWT.NONE);
			jobGroup.setText(" Job: " + Utils.getAttributeValue("name", joblistener.getJob()));
			final GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1);
			gridData.heightHint = 91;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.numColumns = 5;
			jobGroup.setLayout(gridLayout_1);

			{
				lblTimeout = new Label(jobGroup, SWT.NONE);
				lblTimeout.setLayoutData(new GridData());
				lblTimeout.setText("Timeout");
			}
			txtTimeout = new Text(jobGroup, SWT.BORDER);
			txtTimeout.setFocus();
			txtTimeout.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(txtTimeout.getText()!= null && txtTimeout.getText().trim().length() > 0) {
						joblistener.setTimeout(txtTimeout.getText());
					}
				}
			});
			final GridData gridData_3 = new GridData();
			gridData_3.minimumWidth = 70;
			txtTimeout.setLayoutData(gridData_3);
			txtTimeout.setText(joblistener.getTimeout());

			{
				lblIdleTimeout = new Label(jobGroup, SWT.NONE);
				lblIdleTimeout.setAlignment(SWT.RIGHT);
				final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false, 2, 1);
				gridData_1.widthHint = 77;
				lblIdleTimeout.setLayoutData(gridData_1);
				lblIdleTimeout.setText("Idle Timeout");


			}
			txtIdleTimeout = new Text(jobGroup, SWT.BORDER);

			txtIdleTimeout.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {					
					if(txtIdleTimeout.getText()!= null && txtIdleTimeout.getText().trim().length() > 0 ) {
						joblistener.setIdleTimeout(txtIdleTimeout.getText());
					}
				}
			});
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_2.minimumWidth = 70;
			txtIdleTimeout.setLayoutData(gridData_2);
			txtIdleTimeout.setText(joblistener.getIdleTimeout());						
			{
				lblForceIdleTimeout = new Label(jobGroup, SWT.NONE);
				final GridData gridData_1 = new GridData(SWT.DEFAULT, 12);
				lblForceIdleTimeout.setLayoutData(gridData_1);
				lblForceIdleTimeout.setText("Force Idle Timeout");

			}

			final Button noButton = new Button(jobGroup, SWT.RADIO);
			noButton.setEnabled(joblistener.getMintasks()!=null && joblistener.getMintasks().trim().length() > 0);
			noButton.setSelection(!joblistener.getForceIdletimeout());			
			noButton.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, false));
			noButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					joblistener.setForceIdletimeout(false);
				}
			});
			noButton.setText("no");

			final Button yesButton = new Button(jobGroup, SWT.RADIO);
			yesButton.setSelection(joblistener.getForceIdletimeout());
			yesButton.setEnabled(joblistener.getMintasks()!=null && joblistener.getMintasks().trim().length() > 0);
			yesButton.setLayoutData(new GridData());
			yesButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					joblistener.setForceIdletimeout(true);
				}
			});
			yesButton.setText("yes");
			new Label(jobGroup, SWT.NONE);
			new Label(jobGroup, SWT.NONE);


		}

		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shellTimeout.setBounds((screen.width - shellTimeout.getBounds().width) /2, 
				(screen.height - shellTimeout.getBounds().height) /2, 
				shellTimeout.getBounds().width, 
				shellTimeout.getBounds().height);

		shellTimeout.open();

		{
			final Composite composite = new Composite(shellTimeout, SWT.NONE);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 0;
			composite.setLayout(gridLayout_2);
			{
				butCancel = new Button(composite, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						close();
					}
				});
				butCancel.setText("Cancel");
			}
		}

		{
			final Composite composite = new Composite(shellTimeout, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 0;
			gridLayout_2.numColumns = 5;
			composite.setLayout(gridLayout_2);

			{
				butShow = new Button(composite, SWT.NONE);
				butShow.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {												
						Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), shellTimeout, false, null, false, null, false); 
						txtTimeout.setFocus();
					}
				});
				butShow.setText("Show");
			}

			{
				butFinish = new Button(composite, SWT.NONE);
				butFinish.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {

						doFinish();

					}
				});
				butFinish.setText("Finish");
			}

			butBack = new Button(composite, SWT.NONE);
			butBack.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					Element job = joblistener.getJob();
					if(job.getChild("process") != null) {
						JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update, job, assistentType);
						process.showProcessForm();	
						if(jobname != null) 													
							process.setJobname(jobname);

						process.setBackUpJob(jobBackUp, jobForm);
					} else {
						JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update, job, assistentType);
						script.showScriptForm();	
						if(jobname != null) 													
							script.setJobname(jobname);

						script.setBackUpJob(jobBackUp, jobForm);
					}
					closeDialog = true;

					shellTimeout.dispose();
				}
			});
			butBack.setText("Back");
			{
				butNext = new Button(composite, SWT.NONE);
				butNext.setFocus();
				butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
				butNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						Utils.startCursor(shellTimeout);
						JobAssistentRunOptionsForms runOP = new JobAssistentRunOptionsForms(dom, update, joblistener.getJob(), assistentType);
						runOP.showRunOptionsForm();
						if(jobname != null) 													
							runOP.setJobname(jobname);

						runOP.setBackUpJob(jobBackUp, jobForm);
						closeDialog = true;
						Utils.stopCursor(shellTimeout);
						shellTimeout.dispose();

					}
				});
				butNext.setText("Next");
			}

			Utils.createHelpButton(composite, "assistent.timeout.order", shellTimeout);

		}
		txtTimeout.setFocus();
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
		lblForceIdleTimeout.setToolTipText(Messages.getTooltip("assistent.force_idle_timeout"));
		txtTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
		lblTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
	}

	private void close(){
		int cont = MainWindow.message(shellTimeout, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK){
			if(jobBackUp != null)
				joblistener.getJob().setContent(jobBackUp.cloneContent());
			shellTimeout.dispose();
		}
	}

	public void setJobname(Combo jobname) {
		this.jobname = jobname;
	}
	/**
	 * Der Wizzard wurde für ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne Änderungen wieder zurückgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobMainForm jobForm_) {
		if(backUpJob != null)
			jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}

	private void doFinish(){
		if(jobname != null)
			jobname.setText(Utils.getAttributeValue("name",joblistener.getJob()));

		if(assistentType == Editor.JOB_WIZZARD) {															
			jobForm.initForm();		
		} else {

			JobsListener listener = new JobsListener(dom, update);
			listener.newImportJob(joblistener.getJob(), assistentType);

		}

		if(Options.getPropertyBoolean("editor.job.show.wizard"))
			Utils.showClipboard(Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(joblistener.getJob()), shellTimeout, false, null, false, null, true); 

		closeDialog = true;
		shellTimeout.dispose();	

	}
}
