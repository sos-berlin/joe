package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;
import org.eclipse.swt.widgets.Combo;
import com.swtdesigner.SWTResourceManager;

public class JobAssistentExecuteForms {
	
	private SchedulerDom      dom            = null;
	
	private ISchedulerUpdate  update         = null;
	
	private Button            butFinish      = null;
	
	private Button            butCancel      = null;
	
	private Button            butNext        = null;
	
	private Button            butShow        = null;		
	
	private Button            butBack        = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int               assistentType  = -1; 
	
	private Shell             shell          = null;
	
	private Combo             jobname        = null;
	
	private Button            butScript      = null;
	
	private Button            butProcess     = null;
	
	private Combo             comLanguage    = null; 
	
	private Element           job            = null;
	
	private Text              txtInclude     = null;
	
	private Element           jobBackUp    = null;  
	
	private JobForm           jobForm      = null;
	
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean               closeDialog   = false;         
	
	
	public JobAssistentExecuteForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;
		job = job_;				
	}
	
	
	public void showExecuteForm() {
		
		shell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				if(!closeDialog)
					close();
				e.doit = shell.isDisposed();
			}
		});
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		shell.setSize(521, 307);
		
		shell.setText("Execute");
		{
			final Group jobGroup = new Group(shell, SWT.NONE);
			jobGroup.setText( "Job: " + Utils.getAttributeValue("name", job));
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 3, 1);
			gridData.widthHint = 490;
			gridData.heightHint = 203;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.verticalSpacing = 10;
			gridLayout_1.horizontalSpacing = 10;
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			gridLayout_1.numColumns = 5;
			jobGroup.setLayout(gridLayout_1);
			
			{
				Text txtScript = new Text(jobGroup, SWT.MULTI | SWT.WRAP);				
				txtScript.setEditable(false);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false, 5, 1);
				gridData_1.horizontalIndent = -3;
				gridData_1.heightHint = 97;
				gridData_1.widthHint = 441;
				txtScript.setLayoutData(gridData_1);				
				txtScript.setText(Messages.getString("assistent.script_or_execute"));
			}
			
			butProcess = new Button(jobGroup, SWT.RADIO);
			butProcess.setLayoutData(new GridData());
			butProcess.setText("Process");
			butProcess.setSelection(job.getChild("process")!=null);
			new Label(jobGroup, SWT.NONE);
			new Label(jobGroup, SWT.NONE);
			new Label(jobGroup, SWT.NONE);
			new Label(jobGroup, SWT.NONE);
			
			butScript = new Button(jobGroup, SWT.RADIO);
			butScript.setSelection(job.getChild("script")!=null);
			butScript.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(butScript.getSelection()) {
						comLanguage.setEnabled(true);
					} else {
						comLanguage.setEnabled(false);
					}
				}
			});
			butScript.setLayoutData(new GridData(69, SWT.DEFAULT));
			butScript.setText("Script");
			
			{
				final Label lblLanguage = new Label(jobGroup, SWT.NONE);
				lblLanguage.setLayoutData(new GridData());
				lblLanguage.setText("Language");
			}
			
			comLanguage = new Combo(jobGroup, SWT.NONE);
			comLanguage.setItems(ScriptListener._languages);
			comLanguage.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(comLanguage.getSelectionIndex() > 1) {
						txtInclude.setEditable(true);
					} else {
						txtInclude.setEditable(false);
					}
				}
			});
			
			txtInclude = new Text(jobGroup, SWT.BORDER);
			txtInclude.setEditable(false);
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData_2.widthHint = 296;
			txtInclude.setLayoutData(gridData_2);
			
			comLanguage.select(0);
			comLanguage.setEnabled(true);
			final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
			gridData_4.widthHint = 60;
			comLanguage.setLayoutData(gridData_4);
			if(job.getChild("script")!=null) {
				ScriptListener scriptlistener = new ScriptListener(dom, job, Editor.SCRIPT, update);
				comLanguage.select(scriptlistener.getLanguage());
				if(scriptlistener.getLanguage() > 1) {
					txtInclude.setEditable(true);
					if(scriptlistener.getIncludes().length > 0) {
						txtInclude.setText(scriptlistener.getIncludes()[scriptlistener.getIncludes().length-1]);
					}
				} else {
					txtInclude.setEditable(false);
				}
			} else {
				comLanguage.setEnabled(false);
			}
			
			final Label includeLabel = new Label(jobGroup, SWT.NONE);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_1.widthHint = 45;
			includeLabel.setLayoutData(gridData_1);
			includeLabel.setText("Include");
		
		}
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shell.setBounds((screen.width - shell.getBounds().width) /2, 
				(screen.height - shell.getBounds().height) /2, 
				shell.getBounds().width, 
				shell.getBounds().height);
		
		shell.open();
		
		{
			final Composite composite = new Composite(shell, SWT.NONE);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 0;
			gridLayout_2.horizontalSpacing = 0;
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
			final Composite composite = new Composite(shell, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false, 2, 1));
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 0;
			gridLayout_2.numColumns = 4;
			composite.setLayout(gridLayout_2);
			
			{
				butShow = new Button(composite, SWT.NONE);
				butShow.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						refreshJob();
						Utils.showClipboard(Utils.getElementAsString(job), shell);						
					}
				});
				butShow.setText("Show");
			}
			
			{
				butFinish = new Button(composite, SWT.NONE);
				butFinish.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						refreshElement(true);
						closeDialog = true;
						shell.dispose();						
					}
				});
				butFinish.setText("Finish");
			}
			
			butBack = new Button(composite, SWT.NONE);
			butBack.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					refreshJob();
					JobAssistentTasksForm tasks = new JobAssistentTasksForm(dom, update,  job, assistentType);											
					tasks.showTasksForm();	
					if(jobname != null) 													
						tasks.setJobname(jobname);
					//if(jobBackUp != null)
						tasks.setBackUpJob(jobBackUp, jobForm);
					closeDialog = true;
					shell.dispose();
				}
			});
			butBack.setText("Back");
			{
				butNext = new Button(composite, SWT.NONE);
				butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
				butNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						refreshElement(false);
						closeDialog = true;
						shell.dispose();
					}
				});
				butNext.setText("Next");
			}
		}
		setToolTipText();
		shell.layout();		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
		comLanguage.setToolTipText(Messages.getTooltip("assistent.script_or_execute.script"));
		butProcess.setToolTipText(Messages.getTooltip("assistent.script_or_execute.process"));
		butScript.setToolTipText(Messages.getTooltip("assistent.script_or_execute.language"));
		txtInclude.setToolTipText(Messages.getTooltip("assistent.script_or_execute.include"));
	}
	
	private void close() {
		int cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK) {
			if(jobBackUp != null)
				job.setContent(jobBackUp.cloneContent());
			shell.dispose();
		}
	}
	
	public void setJobname(Combo jobname) {
		this.jobname = jobname;
	}		
	
	private void refreshJob() {
		
		if(butProcess.getSelection()) {
			
			ExecuteListener executeListener = new ExecuteListener(dom, job);
			executeListener.setExecutable(true);						
			
		} else {
			
			ExecuteListener executeListener = new ExecuteListener(dom, job);
			executeListener.setExecutable(false);
			
			ScriptListener scriptlistener = new ScriptListener(dom, job, Editor.SCRIPT, update);
			
			if(comLanguage.getSelectionIndex() > -1) {							
				scriptlistener.setLanguage(comLanguage.getSelectionIndex());
			}
			
			if(comLanguage.getSelectionIndex() > 1 && txtInclude.getText() != null && txtInclude.getText().trim().length() > 0) {
				//wurde eine neue include Datei angegeben
				if(scriptlistener.getIncludes().length == 0 || !scriptlistener.getIncludes()[scriptlistener.getIncludes().length-1].equals(txtInclude.getText()))
					scriptlistener.addInclude(txtInclude.getText());
			}
			
		}
		
	} 
	private void refreshElement(boolean apply) {
		refreshJob();
		if(!apply){
			if(butProcess.getSelection()) {
				
				JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update, job, assistentType);			
				process.showProcessForm();	
				if(jobname != null) 													
					process.setJobname(jobname);
				//if(jobBackUp != null)
					process.setBackUpJob(jobBackUp, jobForm);
				
			} else {
				
				JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update, job, assistentType);
				script.showScriptForm();	
				if(jobname != null) 													
					script.setJobname(jobname);
				//if(jobBackUp != null)
					script.setBackUpJob(jobBackUp, jobForm);
			}
		}
		
		if(apply) {
			if(assistentType == Editor.JOB_WIZZARD) {															
				jobForm.initForm();	
			} else {
				if(jobname != null)
					jobname.setText(Utils.getAttributeValue("name",job));			
				JobsListener listener = new JobsListener(dom, update);
				listener.newImportJob(job, assistentType);
			}
			//MainWindow.message(shell,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(job), SWT.OK );
			Utils.showClipboard(Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(job), shell);
			
			
		}
	}
	
	/**
	 * Der Wizzard wurde für ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne Änderungen wieder zurückgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobForm jobForm_) {
		if(backUpJob != null)
			jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}
	
}
