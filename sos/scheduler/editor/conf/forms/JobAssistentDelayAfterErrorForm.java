package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import org.eclipse.swt.widgets.Combo;

public class JobAssistentDelayAfterErrorForm {
	
	private Element           job             = null;
	
	private Text              txtDelayAfterError= null;
	
	private Text              txtErrorCount    = null;
	
	private SchedulerDom      dom              = null;
	
	private ISchedulerUpdate  update           = null;
	
	private Button            butFinish        = null;
	
	private Button            butCancel        = null;
	
	private Button            butNext          = null;
	
	private Button            butShow          = null;	
	
	private Label             lblOftenSetBack  = null; 
	
	private Label             lblLongWait      = null;
	
	private Text              txtHour          = null;
	
	private Text              txtMin           = null;
	
	private Text              txtSecound       = null;
	
	private Text              txtStop          = null; 
	
	private JobOptionsListener optionlistener  = null;
	
	private Shell              shellSetBack    = null; 
	
	private Combo              jobname         = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int                assistentType   = -1; 
	
	/** Anzahl der Error Delays */
	private int                sizeOfErrorDelays= 0;
	
	/** Hilfsvariable*/
	private boolean            modify          = false;
	
	private Button             butBack         = null; 
	
	private Element           jobBackUp        = null;
	
	private JobForm           jobForm          = null;
	 
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean               closeDialog   = false;   
	
	
	public JobAssistentDelayAfterErrorForm(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;		
		optionlistener = new JobOptionsListener(dom, job_);
		assistentType = assistentType_;
		job = job_;	
	}		
	
	public void showDelayAfterErrorForm() {
		
		shellSetBack = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shellSetBack.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				if(!closeDialog)
					close();
				e.doit = shellSetBack.isDisposed();
			}
		});
		shellSetBack.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shellSetBack.setLayout(gridLayout);
		shellSetBack.setSize(546, 342);
		
		String step = "";
		if (Utils.getAttributeValue("order", job).equalsIgnoreCase("yes"))
			step = step + "  [Step 8 of 9]";
		else 
			step = step + "  [Step 8 of 8]";
		shellSetBack.setText("Delay After Error" + step);
		
		
		final Group jobGroup = new Group(shellSetBack, SWT.NONE);
		jobGroup.setText(" Job: " + Utils.getAttributeValue("name", job));
		final GridData gridData_2 = new GridData(GridData.END, GridData.CENTER, false, false, 2, 1);
		gridData_2.heightHint = 246;
		gridData_2.widthHint = 514;
		jobGroup.setLayoutData(gridData_2);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 10;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginRight = 10;
		gridLayout_1.marginLeft = 10;
		gridLayout_1.marginHeight = 10;
		gridLayout_1.marginBottom = 10;
		gridLayout_1.numColumns = 3;
		jobGroup.setLayout(gridLayout_1);
		
		{
			txtDelayAfterError = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 3, 1);
			gridData.horizontalIndent = -1;
			gridData.heightHint = 118;
			gridData.widthHint = 475;
			txtDelayAfterError.setLayoutData(gridData);
			txtDelayAfterError.setEditable(false);
			txtDelayAfterError.setText(Messages.getString("assistent.delay_after_error"));
		}
		
		
		{
			lblLongWait = new Label(jobGroup, SWT.NONE);
			lblLongWait.setLayoutData(new GridData());
			lblLongWait.setText(Messages.getString("assistent.delay_after_error.time"));
			
		}
		
		final Composite composite = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		gridData_4.widthHint = 100;
		composite.setLayoutData(gridData_4);
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.marginWidth = 0;
		gridLayout_5.numColumns = 3;
		composite.setLayout(gridLayout_5);
		
		java.util.List l = optionlistener.getErrorDelays();
		sizeOfErrorDelays = l.size();
		int hour = 0;
		int min = 0;
		int sec = 0;
		String errorCount = null;
		String stopCount = null;
		
		//die Letzen delayafterError holen
		for (int i = 0; i < 2; i++) {
			if(sizeOfErrorDelays != 0 && sizeOfErrorDelays >= sizeOfErrorDelays-i) {
				Element e1 = (Element)(l.get(sizeOfErrorDelays-i-1));
				String delayOrStop = optionlistener.getDelay(e1);
				if(delayOrStop.equals("stop")) {
					stopCount = Utils.getAttributeValue("error_count", e1);			
				} else {
					errorCount = Utils.getAttributeValue("error_count", e1);
					hour = Utils.getHours(delayOrStop, -999);
					min = Utils.getMinutes(delayOrStop, -999);
					sec = Utils.getSeconds(delayOrStop, -999);
					
				}
			}
		}
		
		
		{
			txtHour = new Text(composite, SWT.BORDER);
			txtHour.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtHour.setLayoutData(new GridData(17, SWT.DEFAULT));
			txtHour.setText(hour == 0? "" : Utils.getIntegerAsString(hour));
			txtHour.addModifyListener(new ModifyListener() {
				
				public void modifyText(final ModifyEvent e) {
					Utils.setBackground(0, 23, txtHour);
					if (!Utils.isNumeric( txtHour.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
					modify = true;
				}
			});
		}
		
		{
			txtMin = new Text(composite, SWT.BORDER);
			txtMin.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtMin.setLayoutData(new GridData(17, SWT.DEFAULT));
			txtMin.setText(min == 0? "" : Utils.getIntegerAsString(min));
			txtMin.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					Utils.setBackground(0, 59, txtMin);					
					if (!Utils.isNumeric( txtMin.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
					modify = true;
				}
			});
		}
		
		{
			txtSecound = new Text(composite, SWT.BORDER);
			txtSecound.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtSecound.setLayoutData(new GridData(16, SWT.DEFAULT));
			txtSecound.setText(sec==0? "" : Utils.getIntegerAsString(sec));
			txtSecound.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					Utils.setBackground(0, 59, txtSecound);
					if (!Utils.isNumeric( txtSecound.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
					modify = true;
				}
			});
		}
		
		{
			final Label hhmmssLabel = new Label(jobGroup, SWT.NONE);
			hhmmssLabel.setLayoutData(new GridData(64, SWT.DEFAULT));
			hhmmssLabel.setText("hh:mm:ss");
		}
		
		{
			lblOftenSetBack = new Label(jobGroup, SWT.NONE);
			lblOftenSetBack.setLayoutData(new GridData());
			lblOftenSetBack.setText(Messages.getString("assistent.delay_after_error.error_count"));
		}
		txtErrorCount = new Text(jobGroup, SWT.BORDER);
		txtErrorCount.setText(errorCount!=null?errorCount:"");
		txtErrorCount.addModifyListener(new ModifyListener() {			
			public void modifyText(final ModifyEvent e) {
				modify = true;
			}
		});
		final GridData gridData_1 = new GridData(50, SWT.DEFAULT);
		txtErrorCount.setLayoutData(gridData_1);
		txtErrorCount.setFocus();
		
		new Label(jobGroup, SWT.NONE);
		
		{
			final Label lnlNumberOfMaxErros = new Label(jobGroup, SWT.NONE);
			lnlNumberOfMaxErros.setLayoutData(new GridData());
			lnlNumberOfMaxErros.setText(Messages.getString("assistent.delay_after_error.stop_count"));
		}
		
		txtStop = new Text(jobGroup, SWT.BORDER);
		txtStop.setText(stopCount!=null?stopCount:"");
		txtStop.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				modify = true;
			}
		});
		
		final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_3.widthHint = 50;
		txtStop.setLayoutData(gridData_3);
		
		new Label(jobGroup, SWT.NONE);
		
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shellSetBack.setBounds((screen.width - shellSetBack.getBounds().width) /2, 
				(screen.height - shellSetBack.getBounds().height) /2, 
				shellSetBack.getBounds().width, 
				shellSetBack.getBounds().height);
		
		shellSetBack.open();
		
		final Composite composite_1 = new Composite(shellSetBack, SWT.NONE);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginWidth = 0;
		composite_1.setLayout(gridLayout_3);
		{
			butCancel = new Button(composite_1, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
				}
			});
			butCancel.setText("Cancel");
		}
		
		final Composite composite_2 = new Composite(shellSetBack, SWT.NONE);
		composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 4;
		composite_2.setLayout(gridLayout_4);
		
		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(!check()) return;
					refreshElement(false);					
					Utils.showClipboard(Utils.getElementAsString(job), shellSetBack);
					txtErrorCount.setFocus();
				}
			});
			butShow.setText("Show");
		}
		
		{
			butFinish = new Button(composite_2, SWT.NONE);
			butFinish.setLayoutData(new GridData(40, SWT.DEFAULT));
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(!check()) return;
					
					if(jobname != null)
						jobname.setText(Utils.getAttributeValue("name",job));
					
					refreshElement(true);
					
					if (Utils.getAttributeValue("order", job).equalsIgnoreCase("yes")) {
						MainWindow.message(shellSetBack,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(job), SWT.OK );						
					} else { 
						MainWindow.message(shellSetBack,  Messages.getString("assistent.end") + "\n\n" + Utils.getElementAsString(job), SWT.OK );						
					}
					closeDialog = true;
					shellSetBack.dispose();					
				}
			});
			butFinish.setText("Finish");
			if(!Utils.getAttributeValue("order", job).equals("yes"))
				butFinish.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
		}
		
		butBack = new Button(composite_2, SWT.NONE);
		butBack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				refreshElement(false);
				JobAssistentRunOptionsForms runOP = new JobAssistentRunOptionsForms(dom, update, job, assistentType);
				runOP.showRunOptionsForm();
				if(jobname != null) 													
					runOP.setJobname(jobname);
				if(jobBackUp != null)
					runOP.setBackUpJob(jobBackUp, jobForm);
				closeDialog = true;
				shellSetBack.dispose();
			}
		});
		butBack.setText("Back");
		{
			butNext = new Button(composite_2, SWT.NONE);
			butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));			
			final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData.widthHint = 38;
			butNext.setLayoutData(gridData);
			butNext.setEnabled(Utils.getAttributeValue("order", job).equals("yes"));
			butNext.addSelectionListener(new SelectionAdapter() {				
				
				public void widgetSelected(final SelectionEvent e) {
					
					refreshElement(false);
					
					JobAssistentDelayOrderAfterSetbackForm delay = new JobAssistentDelayOrderAfterSetbackForm(dom, update, job, assistentType);
					delay.showDelayOrderAfterSetbackForm();
					if(jobname != null) 													
						delay.setJobname(jobname);
					if(jobBackUp != null)
						delay.setBackUpJob(jobBackUp, jobForm);
					closeDialog = true;
					shellSetBack.dispose();
					
				}
			});
			butNext.setText("Next");
		}
		setToolTipText();
		shellSetBack.layout();
		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtErrorCount.setToolTipText(Messages.getTooltip("assistent.delay_after_error.error_count"));
		txtHour.setToolTipText(Messages.getTooltip("tooltip.assistent.delay_after_error.delay.hours"));
		txtMin.setToolTipText(Messages.getTooltip("tooltip.assistent.delay_after_error.delay.minutes"));
		txtSecound.setToolTipText(Messages.getTooltip("tooltip.assistent.delay_after_error.delay.seconds"));
		txtStop.setToolTipText(Messages.getTooltip("tooltip.assistent.delay_after_error.btn_stop"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
	}
	
	private void refreshElement(boolean apply) {

		if(modify) {
			if(optionlistener.getErrorDelays().size() > 0 ) {
				if(sizeOfErrorDelays != optionlistener.getErrorDelays().size()) {
					optionlistener.deleteErrorDelay(optionlistener.getErrorDelays().size()-1);				
					optionlistener.deleteErrorDelay(optionlistener.getErrorDelays().size()-1);				
				} 
			}
			
			if(txtErrorCount.getText() != null && txtErrorCount.getText().trim().length() > 0 ) {							
				String delay = Utils.getTime(txtHour.getText(), txtMin.getText(), txtSecound.getText(), true);
				optionlistener.newErrorDelay();						
				optionlistener.applyErrorDelay(txtErrorCount.getText(), delay);
			}
			if(txtStop.getText() != null && txtStop.getText().length() > 0) {
				optionlistener.newErrorDelay();
				optionlistener.applyErrorDelay(txtStop.getText(), "stop");
			}		
		}
		
		modify = false;
		
		if(apply){
			if(assistentType == Editor.JOB_WIZZARD) {															
				jobForm.initForm();		
			} else {
				
				JobsListener listener = new JobsListener(dom, update);
				listener.newImportJob(job, assistentType);
				
			}
		}
	}
	
	private boolean check() {
		String sTime = (txtHour.getText() != null && !txtHour.getText().equals("00")? txtHour.getText() : "")
		.concat(txtMin.getText() != null && !txtMin.getText().equals("00")? txtMin.getText() : "")
		.concat(txtSecound.getText() != null && !txtSecound.getText().equals("00")? txtSecound.getText() : "");
		
		String errorCount = (txtErrorCount.getText() != null ? txtErrorCount.getText() : "");
		
		if(sTime.length() == 0 && errorCount.length() > 0 ) {
			MainWindow.message(shellSetBack, Messages.getString("assistent.delay_after_error.time.missing"), SWT.OK );
			return false;
		}
		
		if(sTime.length() > 0 && errorCount.length() == 0  ) {
			MainWindow.message(shellSetBack, Messages.getString("assistent.delay_after_error.error_count.missing"), SWT.OK );
			return false;
		}
		return true;
	}
	
	private void close() {
		int cont = MainWindow.message(shellSetBack, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK){
			if(jobBackUp != null)
				job.setContent(jobBackUp.cloneContent());
			shellSetBack.dispose();
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
	public void setBackUpJob(Element backUpJob, JobForm jobForm_) {
		jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}
	
}
