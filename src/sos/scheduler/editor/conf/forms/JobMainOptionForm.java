package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;


public class JobMainOptionForm extends Composite implements IUpdateLanguage {
			
	private Combo       sPriority         = null;
	
	private Text        sIdleTimeout      = null;
	
	private Text        sTimeout          = null;
	
	private Text        sTasks            = null;
	
	private Text        tIgnoreSignals    = null; 
	
	private JobListener listener          = null;
	
	private Group       group             = null;
	
	private Group       gMain             = null;
	
	private Label       label3            = null;
	
	private Text        tSpoolerID        = null;
	
	private Label       label11           = null;
	
	private Label       label13           = null;
	
	private Label       label15           = null;
	
	private Label       label17           = null;
		
	private Text        tMintasks         = null;
	
	private boolean     updateTree        = false;
	
	private Button      bForceIdletimeout = null;	
	
	private Combo       cSignals          = null;
	
	private Text        txtJavaOptions    = null; 
	
	
	
	private Combo       comVisible        = null; 
	
	private Button      addButton         = null;
	
	private boolean     init              = true;
	
	private Text        txtWarnIfLongerThan = null;
	
	private Text        txtWarnIfShorterThan = null;
	
	
	public JobMainOptionForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
		super(parent, style);
		init = true;
		
		dom.setInit(true);
		
		this.setEnabled(Utils.isElementEnabled("job", dom, job));
		
		listener = new JobListener(dom, job, main);
		
		initialize();   
		setToolTipText();
	
		updateTree = false;
		
		initForm();
		
		dom.setInit(false);
		init = false;
	}
	
	
	public void apply() {
		//if (isUnsaved())
			//addParam();
	}
	
	
	public boolean isUnsaved() {
		//return bApply.isEnabled();
		return false;
	}
	
	
	private void initialize() {		 
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(723, 566));
		if(tSpoolerID.isVisible())
			tSpoolerID.setFocus();
		else
			txtJavaOptions.setFocus();
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(this, SWT.NONE);		
		group.setText("Job: " + listener.getJobName() + (listener.isDisabled() ? " (Disabled)" : "")); //TODO lang "Job: "....
		group.setLayout(gridLayout2);
		createSashForm();
	}
	
	
	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gMain = new Group(group, SWT.NONE);
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData_12.heightHint = 353;
		gMain.setLayoutData(gridData_12);
		gMain.setText("Main Options for Job: " + listener.getJobName()); //TODO lang "Main Options for Job: "
		gMain.setLayout(gridLayout);
		label3 = new Label(gMain, SWT.NONE);
		label3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label3.setText("Scheduler ID:"); //TODO lang "Scheduler ID:"
		label3.setVisible(!listener.get_dom().isLifeElement() && !listener.get_dom().isDirectory());
		GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		tSpoolerID = new Text(gMain, SWT.BORDER);
		tSpoolerID.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tSpoolerID.selectAll();
			}
		});
		tSpoolerID.setLayoutData(gridData3);        
		tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(init) return;
				listener.setSpoolerID(tSpoolerID.getText());
			}
		});
		
		tSpoolerID.setVisible(!listener.get_dom().isLifeElement() && !listener.get_dom().isDirectory());

		final Label java_optionsLabel = new Label(gMain, SWT.NONE);
		java_optionsLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		java_optionsLabel.setText("Java Options:"); //TODO lang "Java Options"

		txtJavaOptions = new Text(gMain, SWT.BORDER);
		txtJavaOptions.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtJavaOptions.selectAll();
			}
		});
		txtJavaOptions.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setJavaOptions(txtJavaOptions.getText());
			}
		});
		txtJavaOptions.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
		
		final Label ignore_signalLabel = new Label(gMain, SWT.NONE);
		ignore_signalLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		ignore_signalLabel.setText("Ignore Signals:"); //TODO lang "Ignore Signals:"
		
		tIgnoreSignals = new Text(gMain, SWT.BORDER);
		tIgnoreSignals.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tIgnoreSignals.selectAll();
			}
		});
		tIgnoreSignals.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setIgnoreSignal(tIgnoreSignals.getText());
			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
//		gridData_3.widthHint = 48;
		tIgnoreSignals.setLayoutData(gridData_3);
		
		addButton = new Button(gMain, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(init) return;
				if (tIgnoreSignals.getText().equals("")){
					tIgnoreSignals.setText(cSignals.getText());
				}else {
					tIgnoreSignals.setText( tIgnoreSignals.getText() + " " + cSignals.getText());
				}
			}
		});
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		addButton.setLayoutData(gridData_5);
		addButton.setText("<- Add <-"); //TODO lang "<- Add <-"
		
		cSignals = new Combo(gMain, SWT.NONE);	
		cSignals.setItems(new String[] {"SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS."});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		cSignals.setLayoutData(gridData_4);
		label17 = new Label(gMain, SWT.NONE);
		final GridData gridData_7 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_7.widthHint = 41;
		label17.setLayoutData(gridData_7);
		label17.setText("Priority:"); //TODO lang "Priority:"
		
		sPriority = new Combo(gMain, SWT.NONE);
		sPriority.setItems(new String[] { "idle", "below_normal", "normal", "above_normal", "high" });
		sPriority.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = (Utils.isOnlyDigits(e.text) || e.text.equals("idle") || e.text.equals("below_normal")
						|| e.text.equals("normal") || e.text.equals("above_normal") || e.text.equals("high"));
				
			}
		});
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_1.verticalIndent = -1;
		sPriority.setLayoutData(gridData_1);
		sPriority.addModifyListener(new ModifyListener() {
			
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				Utils.setBackground(-20, 20, sPriority);
				listener.setPriority(sPriority.getText());
			}
		});
		new Label(gMain, SWT.NONE);
		new Label(gMain, SWT.NONE);

		final Label visibleLabel = new Label(gMain, SWT.NONE);
		visibleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		visibleLabel.setText("Visible:"); //TODO lang "Visible:"

		comVisible = new Combo(gMain, SWT.READ_ONLY);
		comVisible.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		comVisible.setItems(new String[] { "", "yes", "no", "never" });
		comVisible.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setVisible(comVisible.getText());
			}
		});
		new Label(gMain, SWT.NONE);
		new Label(gMain, SWT.NONE);
		//gridData_16.widthHint = 17;

		final Label minMaskLabel = new Label(gMain, SWT.NONE);
		minMaskLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		minMaskLabel.setText("Min Tasks"); //TODO lang "Min Tasks"
		
		tMintasks = new Text(gMain, SWT.BORDER);
		tMintasks.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tMintasks.selectAll();		
			}
		});
		tMintasks.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		tMintasks.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setMintasks(tMintasks.getText());
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		tMintasks.setLayoutData(gridData_2);
		new Label(gMain, SWT.NONE);
		new Label(gMain, SWT.NONE);
		
		label15 = new Label(gMain, SWT.NONE);
		label15.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label15.setText("Tasks:"); //TODO lang "Tasks:"
		
		sTasks = new Text(gMain, SWT.BORDER);
		sTasks.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				sTasks.selectAll();		
			}
		});
		sTasks.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		sTasks.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sTasks.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
		sTasks.addModifyListener(new ModifyListener() {
			
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setTasks(sTasks.getText());
			}
		});
		new Label(gMain, SWT.NONE);
		new Label(gMain, SWT.NONE);
		label13 = new Label(gMain, SWT.NONE);
		label13.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label13.setText("Timeout:"); //TODO lang "Timeout:"
		
		sTimeout = new Text(gMain, SWT.BORDER);
		sTimeout.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				sTimeout.selectAll();
			}
		});
		sTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				//e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		
		sTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setTimeout(sTimeout.getText());
			}
		});
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData_9.widthHint = 75;
		sTimeout.setLayoutData(gridData_9);

		final Label hhmmssLabel = new Label(gMain, SWT.NONE);
		hhmmssLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		hhmmssLabel.setText("HH:MM:SS "); //TODO lang "HH:MM:SS "
		label11 = new Label(gMain, SWT.NONE);
		label11.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label11.setText("Idle Timeout:"); //TODO lang "Idle Timeout:"
		
		sIdleTimeout = new Text(gMain, SWT.BORDER);
		sIdleTimeout.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				sIdleTimeout.selectAll();
			}
		});
		sIdleTimeout.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		
		sIdleTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				//e.doit = Utils.isOnlyDigits(e.text);
				
			}
		});
		sIdleTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setIdleTimeout(sIdleTimeout.getText());
			}
		});

		final Label hhmmssLabel_1 = new Label(gMain, SWT.NONE);
		hhmmssLabel_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		hhmmssLabel_1.setText("HH:MM:SS or HH:MM or SS never"); //TODO lang "HH:MM:SS or HH:MM or SS never"

		final Label warnIfLongerLabel = new Label(gMain, SWT.NONE);
		warnIfLongerLabel.setText("Warn if longer than:"); //TODO lang "Warn if longer than:"

		txtWarnIfLongerThan = new Text(gMain, SWT.BORDER);
		txtWarnIfLongerThan.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
			}
		});
		txtWarnIfLongerThan.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtWarnIfLongerThan.selectAll();
			}
		});
		txtWarnIfLongerThan.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setWarnIfLongerThan(txtWarnIfLongerThan.getText());
			}
		});
		txtWarnIfLongerThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label hhmmssLabel_1_1 = new Label(gMain, SWT.NONE);
		hhmmssLabel_1_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		hhmmssLabel_1_1.setText("HH:MM:SS or Percentage"); //TODO lang "HH:MM:SS or Percentage"

		final Label warnIfShorterLabel = new Label(gMain, SWT.NONE);
		warnIfShorterLabel.setLayoutData(new GridData());
		warnIfShorterLabel.setText("Warn if shorter than:"); //TODO lang "Warn if shorter than:"

		txtWarnIfShorterThan = new Text(gMain, SWT.BORDER);
		txtWarnIfShorterThan.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtWarnIfShorterThan.selectAll();
			}
		});
		txtWarnIfShorterThan.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(init) return;
				listener.setWarnIfShorterThan(txtWarnIfShorterThan.getText());
			}
		});
		txtWarnIfShorterThan.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label hhmmssOrPercentageLabel = new Label(gMain, SWT.NONE);
		hhmmssOrPercentageLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		hhmmssOrPercentageLabel.setText("HH:MM:SS or Percentage"); //TODO lang "HH:MM:SS or Percentage"
		
		final Label force_idle_timeoutLabel = new Label(gMain, SWT.NONE);
		force_idle_timeoutLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		force_idle_timeoutLabel.setText("Force Idle Timeout"); //TODO lang "Force Idle Timeout"
		
		bForceIdletimeout = new Button(gMain, SWT.CHECK);
		bForceIdletimeout.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
		bForceIdletimeout.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(init) return;
				listener.setForceIdletimeout(bForceIdletimeout.getSelection());
			}
		});
		
	}

	public void initForm(){
		updateTree = true;
		
		tSpoolerID.setText(listener.getSpoolerID());
		
		int index = 0;
		bForceIdletimeout.setSelection(listener.getForceIdletimeout());
		index = sPriority.indexOf(listener.getPriority());
		if (index >= 0)
			sPriority.select(index);
		else {
			int p = Utils.str2int(listener.getPriority(), 20);
			if (p == -999) {
				sPriority.setText("");
			} else {
				if (p < -20) {
					p = -20;
				}
				sPriority.setText(String.valueOf(p));
			}
		}
		
		sTasks.setText(listener.getTasks());
		if (listener.getMintasks()!= null) tMintasks.setText(listener.getMintasks());
		if(listener.getPriority()!= null) sPriority.setText(listener.getPriority());
		tIgnoreSignals.setText(listener.getIgnoreSignal());
		sTimeout.setText(listener.getTimeout());
		sIdleTimeout.setText(listener.getIdleTimeout());
		comVisible.setText(listener.getVisible());
		txtWarnIfLongerThan.setText(listener.getWarnIfLongerThan());
		txtWarnIfShorterThan.setText(listener.getWarnIfShorterThan());
		txtJavaOptions.setText(listener.getJavaOptions());
	}
	
	
	public void setToolTipText() {
		sPriority.setToolTipText(Messages.getTooltip("job.priority"));
		sTasks.setToolTipText(Messages.getTooltip("job.tasks"));
		tIgnoreSignals.setToolTipText(Messages.getTooltip("job.ignore_signal"));
		tMintasks.setToolTipText(Messages.getTooltip("job.mintasks"));
		bForceIdletimeout.setToolTipText(Messages.getTooltip("job.forceIdleTimeout"));        
		sTimeout.setToolTipText(Messages.getTooltip("job.timeout"));
		sIdleTimeout.setToolTipText(Messages.getTooltip("job.idle_timeout"));
		txtJavaOptions.setToolTipText(Messages.getTooltip("job.java_options"));		
		comVisible.setToolTipText(Messages.getTooltip("job.visible"));
		cSignals.setToolTipText(Messages.getTooltip("job.ignore_signal_list"));
		addButton.setToolTipText(Messages.getTooltip("job.add_ignore_signal"));
		tSpoolerID.setToolTipText(Messages.getTooltip("job.spooler_id"));
		txtWarnIfLongerThan.setToolTipText(Messages.getTooltip("job.warn_if_longer_than"));
		txtWarnIfShorterThan.setToolTipText(Messages.getTooltip("job.warn_if_shorter_than"));
	}
	
	
	
} // @jve:decl-index=0:visual-constraint="10,10"
