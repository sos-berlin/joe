package sos.scheduler.editor.conf.forms;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.app.ContextMenu;


public class JobForm extends Composite implements IUpdateLanguage {
	
	
	
	private Combo       sPriority         = null;
	
	private Text        sIdleTimeout      = null;
	
	private Text        sTimeout          = null;
	
	private Text        sTasks            = null;
	
	private Text        tIgnoreSignals    = null; 
	
	private JobListener listener          = null;
	
	private Group       group             = null;
	
	private Group       gMain             = null;
	
	private Label       label             = null;
	
	private Text        tName             = null;
	
	private Label       label1            = null;
	
	private Label       label3            = null;
	
	private Label       label7            = null;
	
	private Label       label9            = null;
	
	private Label       label11           = null;
	
	private Label       label13           = null;
	
	private Label       label15           = null;
	
	private Label       label17           = null;
	
	private Text        tTitle            = null;
	
	private Text        tSpoolerID        = null;
	
	private Combo       cProcessClass     = null;
	
	private Text        tMintasks         = null;
	
	private Composite   cOrder            = null;
	
	private Button      bOrderYes         = null;
	
	private Button      bOrderNo          = null;	
	
	private Group       gDescription      = null;
	
	private Text        tFileName         = null;
	
	private Text        tDescription      = null;
			
	private Text        tComment          = null;
	
	private Label       label8            = null;
	
	private boolean     updateTree        = false;
	
	private Button      bForceIdletimeout = null;
	
	private Button      bStopOnError      = null;
	
	private Combo       cSignals          = null;
	
	private Button      butBrowse         = null;
	
	private Button      butShow           = null;
	
	private Button      butOpen           = null;
	
	private Text        txtJavaOptions    = null; 
	
	private Button      butReplace        = null; 
	
	private Button      butTemporary      = null; 
	
	private Combo       comVisible        = null; 
	
	private Button      butIsLiveFile     = null;
	
	private Button      butWizzard        = null; 
	
	private Button      addButton         = null;
	
	private boolean     init              = true;
	
	private Button      butGoto           = null;
	
	
	public JobForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
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
		//sosString = new SOSString(); 
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(this, SWT.NONE);
		group.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				if(isVisible()) 				
					Utils.checkElement(listener.getName(), listener.get_dom(), Editor.JOB, "CLOSE");
			
			}
		});
		group.setText("Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
		group.setLayout(gridLayout2);
		createSashForm();
	}
	

	
	
	
	
	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gMain = new Group(group, SWT.NONE);
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gMain.setLayoutData(gridData_12);
		gMain.setText("Main Options");
		gMain.setLayout(gridLayout);
		GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
		
		label = new Label(gMain, SWT.NONE);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label.setText("Job Name:");
		
		tName = new Text(gMain, SWT.BORDER);
		tName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {				
				if(!init)//während der initialiserung sollen keine überprüfungen stattfinden
					e.doit = Utils.checkElement(listener.getName(), listener.get_dom(), Editor.JOB, null);								
			}
		});
		tName.setLayoutData(gridData);
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				checkName();
				listener.setName(tName.getText(), updateTree);
				group.setText("Job: " + tName.getText() + (listener.isDisabled() ? " (Disabled)" : ""));

			}

		});
		label1 = new Label(gMain, SWT.NONE);
		label1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label1.setText("Job Title:");
		GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
		tTitle = new Text(gMain, SWT.BORDER);
		tTitle.setLayoutData(gridData1);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
			}
		});
		label3 = new Label(gMain, SWT.NONE);
		label3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label3.setText("Scheduler ID:");
		label3.setVisible(!listener.get_dom().isLifeElement() && !listener.get_dom().isDirectory());
		GridData gridData3 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
		tSpoolerID = new Text(gMain, SWT.BORDER);
		tSpoolerID.setLayoutData(gridData3);        
		tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setSpoolerID(tSpoolerID.getText());
			}
		});
		
		tSpoolerID.setVisible(!listener.get_dom().isLifeElement()  && !listener.get_dom().isDirectory());
		label9 = new Label(gMain, SWT.NONE);
		label9.setLayoutData(new GridData());
		label9.setText("Process Class:");		
		GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
//		gridData4.widthHint = 197;

		 
		butGoto = new Button(gMain, SWT.ARROW | SWT.DOWN);
		butGoto.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
		butGoto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				ContextMenu.goTo(cProcessClass.getText(), listener.get_dom(), Editor.PROCESS_CLASSES);
			}
		});
		butGoto.setAlignment(SWT.RIGHT);
		
		cProcessClass = new Combo(gMain, SWT.NONE);
		cProcessClass.setMenu(new ContextMenu(cProcessClass, listener.get_dom(), Editor.PROCESS_CLASSES).getMenu());
		
		cProcessClass.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setProcessClass(cProcessClass.getText());
			}
		});
		cProcessClass.setLayoutData(gridData4);
		cProcessClass.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setProcessClass(cProcessClass.getText());
			}
		});
		
		butBrowse = new Button(gMain, SWT.NONE);
		butBrowse.setLayoutData(new GridData());
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
				if(name != null && name.length() > 0)
					cProcessClass.setText(name);
			}
		});
		butBrowse.setText("Browse");
		label17 = new Label(gMain, SWT.NONE);
		final GridData gridData_7 = new GridData(41, SWT.DEFAULT);
		label17.setLayoutData(gridData_7);
		label17.setText("Priority:");
		
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
				Utils.setBackground(-20, 20, sPriority);
				listener.setPriority(sPriority.getText());
			}
		});
		label7 = new Label(gMain, SWT.NONE);
		label7.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label7.setText("On Order:");
		GridData gridData15 = new GridData(107, SWT.DEFAULT);
		cOrder = new Composite(gMain, SWT.NONE);
		cOrder.setLayout(new RowLayout());
		cOrder.setLayoutData(gridData15);
		bOrderYes = new Button(cOrder, SWT.RADIO);
		bOrderYes.setText("Yes");
		bOrderYes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				sIdleTimeout.setEnabled(bOrderYes.getSelection());
				if (!sIdleTimeout.getEnabled()) {
					sIdleTimeout.setText("");
				}
				tMintasks.setEnabled(bOrderYes.getSelection());
				if (!tMintasks.getEnabled()) {
					tMintasks.setText("");
				}
				bForceIdletimeout.setEnabled(bOrderYes.getSelection());
				if (!bForceIdletimeout.getEnabled()) {
					bForceIdletimeout.setSelection(false);
				}
				listener.setOrder(bOrderYes.getSelection());
				
			}
		});
		bOrderNo = new Button(cOrder, SWT.RADIO);
		bOrderNo.setText("No");
		bOrderNo.setEnabled(true);
		bOrderNo.setSelection(false);
		bOrderNo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				listener.setPriority(sPriority.getText());
				listener.setOrder(!bOrderNo.getSelection());
			}
		});
		
		final Composite composite_1 = new Composite(gMain, SWT.NONE);
		final GridData gridData_15 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		composite_1.setLayoutData(gridData_15);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.verticalSpacing = 0;
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gridLayout_2.horizontalSpacing = 0;
		gridLayout_2.numColumns = 2;
		composite_1.setLayout(gridLayout_2);
		
		final Label force_idle_timeoutLabel = new Label(composite_1, SWT.NONE);
		final GridData gridData_13 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		force_idle_timeoutLabel.setLayoutData(gridData_13);
		force_idle_timeoutLabel.setText("Force Idle Timeout");
		
		bForceIdletimeout = new Button(composite_1, SWT.CHECK);
		final GridData gridData_14 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_14.horizontalIndent = 5;
		bForceIdletimeout.setLayoutData(gridData_14);
		bForceIdletimeout.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setForceIdletimeout(bForceIdletimeout.getSelection());
			}
		});
		
		final Composite composite = new Composite(gMain, SWT.NONE);
		final GridData gridData_8 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_8.horizontalIndent = 10;
		composite.setLayoutData(gridData_8);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginHeight = 0;
		gridLayout_1.marginWidth = 0;
		gridLayout_1.verticalSpacing = 0;
		gridLayout_1.horizontalSpacing = 0;
		gridLayout_1.numColumns = 2;
		composite.setLayout(gridLayout_1);
		
		final Label stop_on_errorLabel = new Label(composite, SWT.NONE);
		stop_on_errorLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		stop_on_errorLabel.setText("Stop On Error");
		
		bStopOnError = new Button(composite, SWT.CHECK);
		final GridData gridData_16 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_16.horizontalIndent = 5;
		//gridData_16.widthHint = 17;
		bStopOnError.setLayoutData(gridData_16);
		bStopOnError.setSelection(true);
		bStopOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setStopOnError(bStopOnError.getSelection());
			}
		});

		final Label minMaskLabel = new Label(gMain, SWT.NONE);
		minMaskLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		minMaskLabel.setText("Min Tasks");
		
		tMintasks = new Text(gMain, SWT.BORDER);
		tMintasks.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		tMintasks.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setMintasks(tMintasks.getText());
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData_2.widthHint = 75;
		tMintasks.setLayoutData(gridData_2);
		
		label15 = new Label(gMain, SWT.NONE);
		label15.setLayoutData(new GridData());
		label15.setText("Tasks:");

		final Composite composite_3 = new Composite(gMain, SWT.NONE);
		final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		composite_3.setLayoutData(gridData_10);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginWidth = 0;
		gridLayout_4.marginHeight = 0;
		gridLayout_4.verticalSpacing = 0;
		composite_3.setLayout(gridLayout_4);
		
		sTasks = new Text(composite_3, SWT.BORDER);
		final GridData gridData_17 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		sTasks.setLayoutData(gridData_17);
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
				listener.setTasks(sTasks.getText());
			}
		});
		label13 = new Label(gMain, SWT.NONE);
		label13.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label13.setText("Timeout:");
		
		sTimeout = new Text(gMain, SWT.BORDER);
		sTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		
		sTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setTimeout(sTimeout.getText());
			}
		});
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		//gridData_9.widthHint = 75;
		sTimeout.setLayoutData(gridData_9);
		label11 = new Label(gMain, SWT.NONE);
		label11.setLayoutData(new GridData());
		label11.setText("Idle Timeout:");

		final Composite composite_4 = new Composite(gMain, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
//		gridData_6.widthHint = 224;
		composite_4.setLayoutData(gridData_6);
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.verticalSpacing = 0;
		gridLayout_5.marginWidth = 0;
		gridLayout_5.marginHeight = 0;
		composite_4.setLayout(gridLayout_5);
		
		sIdleTimeout = new Text(composite_4, SWT.BORDER);
		final GridData gridData_21 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		sIdleTimeout.setLayoutData(gridData_21);
		
		sIdleTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
				
			}
		});
		sIdleTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setIdleTimeout(sIdleTimeout.getText());
			}
		});
		
		final Label ignore_signalLabel = new Label(gMain, SWT.NONE);
		ignore_signalLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		ignore_signalLabel.setText("Ignore Signals:");
		
		tIgnoreSignals = new Text(gMain, SWT.BORDER);
		tIgnoreSignals.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setIgnoreSignal(tIgnoreSignals.getText());
			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
//		gridData_3.widthHint = 48;
		tIgnoreSignals.setLayoutData(gridData_3);
		
		addButton = new Button(gMain, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tIgnoreSignals.getText().equals("")){
					tIgnoreSignals.setText(cSignals.getText());
				}else {
					tIgnoreSignals.setText( tIgnoreSignals.getText() + " " + cSignals.getText());
				}
			}
		});
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		addButton.setLayoutData(gridData_5);
		addButton.setText("<- Add <-");
		
		cSignals = new Combo(gMain, SWT.NONE);
		//cSignals.setItems(new String[] {"error", "success", "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS."});
		cSignals.setItems(new String[] {"SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS."});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1);
		cSignals.setLayoutData(gridData_4);

		final Label java_optionsLabel = new Label(gMain, SWT.NONE);
		java_optionsLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		java_optionsLabel.setText("Java Options:");

		txtJavaOptions = new Text(gMain, SWT.BORDER);
		txtJavaOptions.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setJavaOptions(txtJavaOptions.getText());
			}
		});
		txtJavaOptions.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));

		final Label visibleLabel = new Label(gMain, SWT.NONE);
		visibleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		visibleLabel.setText("Visible:");

		comVisible = new Combo(gMain, SWT.READ_ONLY);
		comVisible.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		comVisible.setItems(new String[] { "", "yes", "no", "never" });
		comVisible.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setVisivle(comVisible.getText());
			}
		});

		final Composite composite_6 = new Composite(gMain, SWT.NONE);
		composite_6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		final GridLayout gridLayout_7 = new GridLayout();
		gridLayout_7.verticalSpacing = 0;
		gridLayout_7.marginWidth = 0;
		gridLayout_7.marginHeight = 0;
		gridLayout_7.horizontalSpacing = 0;
		gridLayout_7.numColumns = 2;
		composite_6.setLayout(gridLayout_7);

		final Label temporaryLabel = new Label(composite_6, SWT.NONE);
		temporaryLabel.setLayoutData(new GridData());
		temporaryLabel.setText("Temporary");

		butTemporary = new Button(composite_6, SWT.CHECK);
		final GridData gridData_23 = new GridData();
		gridData_23.horizontalIndent = 5;
		butTemporary.setLayoutData(gridData_23);
		butTemporary.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setTemporary(butTemporary.getSelection());
			}
		});

		final Composite composite_5 = new Composite(gMain, SWT.NONE);
		final GridData gridData_22 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_22.horizontalIndent = 10;
		composite_5.setLayoutData(gridData_22);
		final GridLayout gridLayout_6 = new GridLayout();
		gridLayout_6.verticalSpacing = 0;
		gridLayout_6.marginWidth = 0;
		gridLayout_6.marginHeight = 0;
		gridLayout_6.horizontalSpacing = 0;
		gridLayout_6.numColumns = 2;
		composite_5.setLayout(gridLayout_6);

		final Label replaceLabel = new Label(composite_5, SWT.NONE);
		replaceLabel.setText("Replace");

		butReplace = new Button(composite_5, SWT.CHECK);
		final GridData gridData_24 = new GridData();
		gridData_24.horizontalIndent = 5;
		butReplace.setLayoutData(gridData_24);
		butReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setReplace(butReplace.getSelection());
			}
		});
		
		butReplace.setSelection(true);

		final Composite composite_2 = new Composite(gMain, SWT.NONE);
		final GridData gridData_20 = new GridData(GridData.FILL, GridData.BEGINNING, false, true, 2, 1);
		gridData_20.minimumHeight = 20;
		composite_2.setLayoutData(gridData_20);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.horizontalSpacing = 0;
		gridLayout_3.marginHeight = 0;
		gridLayout_3.marginWidth = 0;
		gridLayout_3.verticalSpacing = 0;
		gridLayout_3.numColumns = 2;
		composite_2.setLayout(gridLayout_3);
		label8 = new Label(composite_2, SWT.NONE);
		label8.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label8.setText("Comment:");

		final Button button = new Button(composite_2, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
		GridData gridData61 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, true, 4, 1);
		tComment = new Text(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
		tComment.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if(e.keyCode==97 && e.stateMask == SWT.CTRL){
        			tComment.setSelection(0, tComment.getText().length());
				}
			}
		});
		tComment.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);				
				
			}
		});
		tComment.setLayoutData(gridData61);
		tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setComment(tComment.getText());
			}
		});
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 3;
		gDescription = new Group(group, SWT.NONE);
		final GridData gridData_11 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gDescription.setLayoutData(gridData_11);
		gDescription.setText("Job Description");
		gDescription.setLayout(gridLayout3);

		butIsLiveFile = new Button(gDescription, SWT.CHECK);
		butIsLiveFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setInclude(tFileName.getText(), butIsLiveFile.getSelection());
				
				if(tFileName.getText()!= null && tFileName.getText().length() > 0) {
					butShow.setEnabled(true);
					if(tFileName.getText().endsWith(".xml"))
						butOpen.setEnabled(true);
					else
						butOpen.setEnabled(false);
				} else {
					butShow.setEnabled(false);
					butOpen.setEnabled(false);
				}
			}
		});
        butIsLiveFile.setLayoutData(new GridData());
        butIsLiveFile.setText("from Hot Folder");
        
		GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData12.horizontalIndent = -1;
		tFileName = new Text(gDescription, SWT.BORDER);
		tFileName.setLayoutData(gridData12);
		
		
		tFileName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setInclude(tFileName.getText(), butIsLiveFile.getSelection());				
				if(tFileName.getText()!= null && tFileName.getText().length() > 0 ) {					
					butShow.setEnabled(true);
					if(tFileName.getText().endsWith(".xml"))				
						butOpen.setEnabled(true);
					else
						butOpen.setEnabled(false);
				} else {
					butShow.setEnabled(false);
					butOpen.setEnabled(false);
				}
			}
		});
		
		butShow = new Button(gDescription, SWT.NONE);
		butShow.setEnabled(false);
		butShow.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butShow.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				try {
					Utils.startCursor(getShell());
					if (tFileName.getText() != null && tFileName.getText().length() > 0) {
						String sHome = getHome(tFileName.getText());
						
						Program prog = Program.findProgram("html");
						
						if (prog != null)
							prog.execute(new File((sHome).concat(tFileName.getText())).toURL().toString());
						else {
							Runtime.getRuntime().exec(Options.getBrowserExec(new File((sHome).concat(tFileName.getText())).toURL().toString(), Options.getLanguage()));
						} 
					} 
				} catch (Exception ex) {
					try {
						new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file " + tFileName.getText() , ex);
					} catch(Exception ee) {
						//tu nichts
					}
					MainWindow.message(getShell(), "..could not open file " + tFileName.getText() + " " + ex.getMessage(), SWT.ICON_WARNING | SWT.OK );
				} finally {
					Utils.stopCursor(getShell());
				}
			}
		});
		butShow.setText("Show");
		new Label(gDescription, SWT.NONE);
		GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		gridData14.horizontalIndent = -1;
		tDescription = new Text(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tDescription.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if(e.keyCode==97 && e.stateMask == SWT.CTRL){
					tDescription.setSelection(0, tDescription.getText().length());
				}
			}
		});
		tDescription.setFont(ResourceManager.getFont("", 10, SWT.NONE));
		tDescription.setLayoutData(gridData14);
		tDescription.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setDescription(tDescription.getText());
			}
		});
		
		butOpen = new Button(gDescription, SWT.NONE);
		butOpen.setEnabled(false);
		butOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Utils.startCursor(getShell());
				String xmlPath = "";
				if(tFileName.getText() != null && tFileName.getText().length() > 0) {
					xmlPath = sos.scheduler.editor.app.Options.getSchedulerHome() ;
					xmlPath = (xmlPath.endsWith("/") || xmlPath.endsWith("\\") ? xmlPath.concat(tFileName.getText()) : xmlPath.concat("/").concat(tFileName.getText()));
					
					
					sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();        		
					con.openDocumentation(xmlPath);
					con.setStatusInTitle();
				} else {        			
					MainWindow.message("There is no Documentation " + xmlPath, SWT.ICON_WARNING | SWT.OK);
				}
				Utils.stopCursor(getShell());
			}
		});
		butOpen.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butOpen.setText("Open");
		new Label(gDescription, SWT.NONE);
		
		butWizzard = new Button(gDescription, SWT.NONE);
		butWizzard.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				startWizzard(false);
				
			}
		});
		butWizzard.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butWizzard.setText("Wizzard");
		new Label(gDescription, SWT.NONE);
		new Label(gDescription, SWT.NONE);		
		
	}

	public void initForm(){
		tName.setText(listener.getName());
		updateTree = true;
		tTitle.setText(listener.getTitle());
		tSpoolerID.setText(listener.getSpoolerID());
		
		
		
		String process_class = "";
		if(listener.getProcessClass() != null && listener.getProcessClass().length() > 0)
			process_class= listener.getProcessClass();
		
		String[] classes = listener.getProcessClasses();
		if(classes != null)
			cProcessClass.setItems(classes);
		
		cProcessClass.setText(process_class);
						
		int index = 0;
		
		bOrderYes.setSelection(listener.getOrder());
		bOrderNo.setSelection(!listener.getOrder());
		bStopOnError.setSelection(listener.getStopOnError());
		bForceIdletimeout.setSelection(listener.getForceIdletimeout());
		sIdleTimeout.setEnabled(bOrderYes.getSelection());
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
		butIsLiveFile.setSelection(listener.isLiveFile());
		tFileName.setText(listener.getInclude());
		
		//tURL.setText(listener.getInclude());
		tDescription.setText(listener.getDescription());
		tComment.setText(listener.getComment());
		butReplace.setSelection(listener.getReplace());
		butTemporary.setSelection(listener.getTemporary());
		comVisible.setText(listener.getVisible());
		
		
	}
	
	
	
	
	public void startWizzard(boolean onlyParams) {
		Utils.startCursor(getShell());
		if(listener.getInclude()!= null && listener.getInclude().trim().length() > 0) {
			//JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren        						
			//JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), listener, parForm.getTParameter(), onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			//JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), listener.getJob(), onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), listener, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			if(!onlyParams)
				paramsForm.setJobForm(this);
			paramsForm.showAllImportJobParams(listener.getInclude());        			
		} else { 
			//Liste aller Jobdokumentation 
			//JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(listener, parForm.getTParameter(), onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			//JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(listener, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(listener, Editor.JOB_WIZZARD);
			
			
			if(!onlyParams)
				importJobForms.setJobForm(this);
			importJobForms.showAllImportJobs();						
			
		}
		if (butWizzard != null) butWizzard.setToolTipText(Messages.getTooltip("jobs.assistent"));
		Utils.stopCursor(getShell());
	}
	
	
	/*public Table getTParameter() {
		return parForm.getTParameter();
	}*/
	
	public String getHome(String filename) {
		
		
		String home = ".";
		if((listener.get_dom().isDirectory() || listener.get_dom().isLifeElement()) && butIsLiveFile.getSelection()) {
			if(filename.startsWith("/") || filename.startsWith("\\")) {
				home = Options.getSchedulerHotFolder();
			} else if(listener.get_dom().getFilename() != null){
			     home = new File(listener.get_dom().getFilename()).getParent(); 
			}
		} else {
			//normale Konfiguration
			if(butIsLiveFile.getSelection())
				home = Options.getSchedulerHotFolder();
			else
				home = Options.getSchedulerHome();	
		}
		
		if(!(home.endsWith("\\") || home.endsWith("/")))
			home = home.concat("/");
		
		home = home.replaceAll("\\\\", "/");
		
		return home;
		/*String sHome = sos.scheduler.editor.app.Options.getSchedulerHome();
		if(!(sHome.endsWith("\\") || sHome.endsWith("/")))
			sHome = sHome.concat("/");
		
		sHome = sHome.replaceAll("\\\\", "/");
		return sHome;
		*/
	}
	
	private void checkName(){
	  if(Utils.existName(tName.getText(), listener.getJob(), "job")) {		  
		  tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
	  } else {
		  tName.setBackground(null);
	  }
	}
	
	public void setToolTipText() {
		tName.setToolTipText(Messages.getTooltip("job.name"));
		tTitle.setToolTipText(Messages.getTooltip("job.title"));
		tSpoolerID.setToolTipText(Messages.getTooltip("job.spooler_id"));
		sPriority.setToolTipText(Messages.getTooltip("job.priority"));
		sTasks.setToolTipText(Messages.getTooltip("job.tasks"));
		tIgnoreSignals.setToolTipText(Messages.getTooltip("job.ignore_signal"));
		tMintasks.setToolTipText(Messages.getTooltip("job.mintasks"));
		bForceIdletimeout.setToolTipText(Messages.getTooltip("job.forceIdleTimeout"));        
		bStopOnError.setToolTipText(Messages.getTooltip("job.stop_on_error"));
		sTimeout.setToolTipText(Messages.getTooltip("job.timeout"));
		sIdleTimeout.setToolTipText(Messages.getTooltip("job.idle_timeout"));
		tComment.setToolTipText(Messages.getTooltip("job.comment"));
		cProcessClass.setToolTipText(Messages.getTooltip("job.process_class"));
		bOrderYes.setToolTipText(Messages.getTooltip("job.btn_order_yes"));
		bOrderNo.setToolTipText(Messages.getTooltip("job.btn_order_no"));		
		
		tFileName.setToolTipText(Messages.getTooltip("job.description.filename"));
		tDescription.setToolTipText(Messages.getTooltip("job.description"));		
		butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		butShow.setToolTipText(Messages.getTooltip("job.param.show"));        
		butOpen.setToolTipText(Messages.getTooltip("job.param.open"));
		txtJavaOptions.setToolTipText(Messages.getTooltip("job.java_options"));
		
		butReplace.setToolTipText(Messages.getTooltip("job.replace"));
		comVisible.setToolTipText(Messages.getTooltip("job.visible"));
		butTemporary.setToolTipText(Messages.getTooltip("job.temporary"));
		
		cSignals.setToolTipText(Messages.getTooltip("job.ignore_signal_list"));
		addButton.setToolTipText(Messages.getTooltip("job.add_ignore_signal"));

		butGoto.setToolTipText(Messages.getTooltip("goto"));
	}
	
	
	
} // @jve:decl-index=0:visual-constraint="10,10"
