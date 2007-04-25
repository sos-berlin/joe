/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;



import java.util.List;

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
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobAssistentDelayOrderAfterSetbackForm {
 
	private Element            job              = null;
	
	private Text               txtDelayOrder    = null;
 
	private Text               txtSetBack       = null;
		
	private SchedulerDom       dom              = null;
	
	private ISchedulerUpdate   update           = null;
	
	private Button             butFinish        = null;
	
	private Button             butCancel        = null;
	
	private Button             butNext          = null;
	
	private Button             butShow          = null;	
		
	private Label              lblOftenSetBack  = null; 
	
	private Label              lblLongWait      = null;
	
	private Text               txtHour          = null;
	
	private Text               txtMin           = null;
	 
	private Text               txtSecound       = null;	
	
	private JobOptionsListener optionlistener   = null;
	 
	private Button             noButton         = null;
	
	private Button             yesButton        = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int                assistentType    = -1; 
	
	private Shell              shellSetBack     = null; 
	
	private Combo              jobname          = null;
	
	/** Hilfsvariable*/
	private boolean            modify           = false;
	
	private int                sizeOfSetbacks   = 0;
	
	private Button             butBack          = null;
	
	private Element            jobBackUp        = null;
	
	private JobForm           jobForm           = null;
	
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean               closeDialog   = false;      
	
	public JobAssistentDelayOrderAfterSetbackForm(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
			
		optionlistener = new JobOptionsListener(dom, job_);
		assistentType = assistentType_;
		job = job_;	
	}		
	
	public void showDelayOrderAfterSetbackForm() {
		
		
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
		shellSetBack.setSize(538, 306);
		String step = "  ";		
		step = step + " [Step 9 of 9]";		
		shellSetBack.setText("Delay Order After Setback" + step);
		

		final Group jobGroup = new Group(shellSetBack, SWT.NONE);
		jobGroup.setText(" Job: " + Utils.getAttributeValue("name", job));
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_2.heightHint = 215;
		gridData_2.widthHint = 507;
		jobGroup.setLayoutData(gridData_2);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 10;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginRight = 10;
		gridLayout_1.marginLeft = 10;
		gridLayout_1.marginHeight = 10;
		gridLayout_1.marginBottom = 10;
		gridLayout_1.numColumns = 5;
		jobGroup.setLayout(gridLayout_1);

		List l = optionlistener.getSetbacks();
		sizeOfSetbacks = l.size();
		int hour = 0;
		int min = 0;
		int sec = 0;
		String setback = "";
		boolean ismax = false;
		
		if(l.size()> 0) {
			Element e = (Element)l.get(l.size()-1);
			ismax= Utils.isAttributeValue("is_maximum", e);
			setback = Utils.getAttributeValue("setback_count", e);
			String delay = Utils.getAttributeValue("delay", e);
			hour = Utils.getHours(delay, -999);
			min = Utils.getMinutes(delay, -999);
			sec = Utils.getSeconds(delay, -999);
		}
		
		{
			txtDelayOrder = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 5, 1);
			gridData.horizontalIndent = -3;
			gridData.heightHint = 102;
			gridData.widthHint = 360;
			txtDelayOrder.setLayoutData(gridData);
			txtDelayOrder.setEditable(false);
			txtDelayOrder.setText(Messages.getString("assistent.delay_order_after_setback"));
		}

		{
			lblLongWait = new Label(jobGroup, SWT.NONE);
			lblLongWait.setLayoutData(new GridData());
			lblLongWait.setText(Messages.getString("assistent.delay_order_after_setback.time"));
			
		}

		
		{
			txtHour = new Text(jobGroup, SWT.BORDER);
			txtHour.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtHour.setText(hour == 0? "" : Utils.getIntegerAsString(hour));
			final GridData gridData = new GridData(17, SWT.DEFAULT);
			gridData.minimumWidth = 17;
			txtHour.setLayoutData(gridData);
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
			txtMin = new Text(jobGroup, SWT.BORDER);
			txtMin.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtMin.setText(min == 0? "" : Utils.getIntegerAsString(min));
			final GridData gridData = new GridData(17, SWT.DEFAULT);
			gridData.minimumWidth = 17;
			txtMin.setLayoutData(gridData);
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
			txtSecound = new Text(jobGroup, SWT.BORDER);
			txtSecound.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData.widthHint = 17;
			gridData.minimumWidth = 17;
			txtSecound.setLayoutData(gridData);
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
			hhmmssLabel.setLayoutData(new GridData(57, SWT.DEFAULT));
			hhmmssLabel.setText("hh:mm:ss");
		}

		{
			lblOftenSetBack = new Label(jobGroup, SWT.NONE);
			lblOftenSetBack.setLayoutData(new GridData());
			lblOftenSetBack.setText(Messages.getString("assistent.delay_order_after_setback.setback_count"));
		}
		txtSetBack = new Text(jobGroup, SWT.BORDER);
		txtSetBack.setText(setback!=null?setback:"");
		txtSetBack.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				modify = true;
			}
		});
		
		final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 4, 1);
		gridData_1.minimumWidth = 50;
		gridData_1.widthHint = 55;
		txtSetBack.setLayoutData(gridData_1);
		txtSetBack.setFocus();

		{
			final Label lblNumberOfSetBack = new Label(jobGroup, SWT.NONE);
			lblNumberOfSetBack.setText(Messages.getString("assistent.delay_order_after_setback.is_maximum"));
		}

		noButton = new Button(jobGroup, SWT.RADIO);
		noButton.setSelection(!ismax);
		noButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				modify = true;
			}
		});
		
		
		noButton.setLayoutData(new GridData());
		noButton.setText("no");

		yesButton = new Button(jobGroup, SWT.RADIO);
		yesButton.setSelection(ismax);
		yesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				modify = true;
			}
		});
		
		yesButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
		yesButton.setText("yes");

		
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shellSetBack.setBounds((screen.width - shellSetBack.getBounds().width) /2, 
				        (screen.height - shellSetBack.getBounds().height) /2, 
				        shellSetBack.getBounds().width, 
				        shellSetBack.getBounds().height);
		
		
		shellSetBack.open();

		final Composite composite_1 = new Composite(shellSetBack, SWT.NONE);
		composite_1.setLayoutData(new GridData());
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
		final GridData gridData_3 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_3.widthHint = 143;
		composite_2.setLayoutData(gridData_3);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 4;
		composite_2.setLayout(gridLayout_4);

		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					refreshElement(false);
					txtSetBack.setFocus();					
					Utils.showClipboard(Utils.getElementAsString(job), shellSetBack);
				}
			});
			butShow.setText("Show");
		}
		{
			butNext = new Button(composite_2, SWT.NONE);
			butNext.setVisible(false);			
			butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData.widthHint = 3;
			butNext.setLayoutData(gridData);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					refreshElement(false);
					shellSetBack.dispose();					
				}
			});
			butNext.setText("Next");
		}

		butBack = new Button(composite_2, SWT.NONE);
		butBack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				refreshElement(false);
				JobAssistentDelayAfterErrorForm derror = new JobAssistentDelayAfterErrorForm(dom, update, job, assistentType);
				derror.showDelayAfterErrorForm();
				if(jobname != null) 													
					derror.setJobname(jobname);
				if(jobBackUp != null)
					derror.setBackUpJob(jobBackUp, jobForm);
				closeDialog = true;
				shellSetBack.dispose();				
			}
		});
		butBack.setText("Back");

		{
			butFinish = new Button(composite_2, SWT.NONE);
			butFinish.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData.widthHint = 53;
			butFinish.setLayoutData(gridData);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
					refreshElement(true);
					if(jobname != null)
						jobname.setText(Utils.getAttributeValue("name",job));	
					MainWindow.message(shellSetBack,  Messages.getString("assistent.end") + "\n\n" + Utils.getElementAsString(job), SWT.OK );
					
					closeDialog = true;
					shellSetBack.dispose();
					
					
				}
			});
			butFinish.setText("Finish");
		}
		setToolTipText();
		shellSetBack.layout();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtSetBack.setToolTipText(Messages.getTooltip("assistent.delay_order_after_setback.setback_count"));
		txtHour.setToolTipText(Messages.getTooltip("assistent.delay_order_after_setback.delay.hours"));
		txtMin.setToolTipText(Messages.getTooltip("assistent.delay_order_after_setback.delay.minutes"));
		txtSecound.setToolTipText(Messages.getTooltip("assistent.delay_order_after_setback.delay.seconds"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
		
	}
	
	private void refreshElement(boolean apply) {
		if(modify) {
			
			if(optionlistener.getSetbacks().size() > 0 ) {
				if(sizeOfSetbacks != optionlistener.getSetbacks().size()) {					
					optionlistener.deleteSetbackDelay(optionlistener.getSetbacks().size()-1);										
				} 
			}			
			
			if(txtSetBack.getText() != null && txtSetBack.getText().trim().length() > 0 ) {			
				optionlistener.newSetbackDelay();			
				String delay = Utils.getTime(txtHour.getText(), txtMin.getText(), txtSecound.getText(), true);
				optionlistener.applySetbackDelay(txtSetBack.getText(),  yesButton.getSelection() , delay);
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
