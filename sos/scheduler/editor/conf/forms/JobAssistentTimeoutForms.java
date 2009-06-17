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


public class JobAssistentTimeoutForms {
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private JobListener       joblistener  = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;				
	
	private Text              txtTimeout   = null;
	
	private Label             lblTimeout   = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int               assistentType= -1; 
	
	private Shell             shellTimeout = null;
	
	private Button            butBack      = null; 
	
	private Element           jobBackUp    = null;  
	
	private JobMainForm           jobForm      = null;
	
	/** Hilsvariable f�r das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog �ber den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean           closeDialog  = false;        
	
	public JobAssistentTimeoutForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
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
		shellTimeout.setSize(521, 221);
		String step = "  ";
		if (Utils.getAttributeValue("order", joblistener.getJob()).equalsIgnoreCase("yes"))
			step = step + " [Step 6 of 9]";
		else 
			step = step + " [Step 6 of 8]";
		shellTimeout.setText("Timeout" + step);
		
		{
			final Group jobGroup = new Group(shellTimeout, SWT.NONE);
			jobGroup.setText(" Job: " + Utils.getAttributeValue("name", joblistener.getJob()));
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 2, 1);
			gridData.heightHint = 133;
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
				gridData_1.horizontalIndent = -3;
				gridData_1.heightHint = 71;
				gridData_1.widthHint = 448;
				txtTime.setLayoutData(gridData_1);
				txtTime.setEditable(false);
				
				txtTime.setText(Messages.getString("assistent.timeout"));
				
			}
			
			
			{
				lblTimeout = new Label(jobGroup, SWT.NONE);
				lblTimeout.setLayoutData(new GridData());
				lblTimeout.setText("Timeout");
			}
			txtTimeout = new Text(jobGroup, SWT.BORDER);
			
			txtTimeout.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(txtTimeout.getText()!= null && txtTimeout.getText().trim().length() > 0) {
						joblistener.setTimeout(txtTimeout.getText());
					}
					
				}
			});
			txtTimeout.setLayoutData(new GridData(71, SWT.DEFAULT));
			txtTimeout.setText(joblistener.getTimeout());
			
			
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
			gridLayout_2.numColumns = 4;
			composite.setLayout(gridLayout_2);
			
			{
				butShow = new Button(composite, SWT.NONE);
				butShow.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						txtTimeout.setFocus();						
						Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), shellTimeout);
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
						//if(jobBackUp != null)
							process.setBackUpJob(jobBackUp, jobForm);	
					} else {
						JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update, job, assistentType);
						script.showScriptForm();	
						//if(jobBackUp != null)
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
						//if(jobBackUp != null)
							runOP.setBackUpJob(jobBackUp, jobForm);
						closeDialog = true;
						Utils.stopCursor(shellTimeout);
						shellTimeout.dispose();
						
					}
				});
				butNext.setText("Next");
			}
		}
		setToolTipText();
		txtTimeout.setFocus();
		shellTimeout.layout();		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));			
		txtTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
		lblTimeout.setToolTipText(Messages.getTooltip("assistent.lbltimeout"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
	}
	
	private void close() {
		int cont = MainWindow.message(shellTimeout, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK){
			if(jobBackUp != null)
				joblistener.getJob().setContent(jobBackUp.cloneContent());
			shellTimeout.dispose();
		}
	}
	
	/**
	 * Der Wizzard wurde f�r ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne �nderungen wieder zur�ckgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobMainForm jobForm_) {
		if(backUpJob != null)
			jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}
	
	private void doFinish() {
		
		if(assistentType == Editor.JOB_WIZZARD) {															
			jobForm.initForm();	
			
		} else {
			
			JobsListener listener = new JobsListener(dom, update);
			listener.newImportJob(joblistener.getJob(), assistentType);
			
		}
		
		//MainWindow.message(shellTimeout,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(joblistener.getJob()), SWT.OK );
		if(Options.getPropertyBoolean("editor.job.show.wizard"))
			Utils.showClipboard(Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(joblistener.getJob()), shellTimeout);
		
		closeDialog = true;
		shellTimeout.dispose();
		
	}
}
