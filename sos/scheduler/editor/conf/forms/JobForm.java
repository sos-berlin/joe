package sos.scheduler.editor.conf.forms;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
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

public class JobForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	
    private Label       label4_1          = null;
    
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

    private SashForm    sashForm          = null;

    private Group       gJobParameter     = null;

    private Table       tParameter        = null;

    private Button      bRemove           = null;

    private Label       label2            = null;

    private Text        tParaName         = null;

    private Label       label6            = null;

    private Text        tParaValue        = null;

    private Button      bApply            = null;

    private Group       gDescription      = null;

    private Text        tFileName         = null;

    private Text        tDescription      = null;

    private Label       label4            = null;

    private Label       label10           = null;

    private Text        tComment          = null;

    private Label       label8            = null;

    private boolean     updateTree        = false;

    private Button      bForceIdletimeout = null;
    
    private Button      bStopOnError      = null;
    
    private Combo       cSignals          = null;
    
    private Text        txtParameterDescription  = null;
    
    private Button      butBrowse         = null;
    
    private Button      butShow           = null;
    
    private Button      butOpen           = null;
    
    private Table       tableEnvironment  = null; 
    
    private Text        txtEnvName        = null;
    
    private Text        txtEnvValue       = null;
    
    private Button      butEnvApply       = null; 
    
    private Button      butEnvRemove      = null; 

    public JobForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style);
                
        this.setEnabled(Utils.isElementEnabled("job", dom, job));
        
        listener = new JobListener(dom, job, main);
        initialize();   
        setToolTipText();
        //sashForm.setWeights(new int[] { 40, 30, 30 });
        //Options.loadSash("job_form", sashForm);
        

        dom.setInit(true);

        updateTree = false;
        
        initForm();

        dom.setInit(false);
    }

    
    public void apply() {
        if (isUnsaved())
            addParam();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    private void initialize() {
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
        group.setText("Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
        group.setLayout(gridLayout2);
        createSashForm();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
        gridData.widthHint = 312;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        gMain = new Group(sashForm, SWT.NONE);
        gMain.setText("Main Options");
        gMain.setLayout(gridLayout);
        label = new Label(gMain, SWT.NONE);
        label.setText("Job Name:");
        tName = new Text(gMain, SWT.BORDER);
        tName.setLayoutData(gridData);
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setName(tName.getText(), updateTree);
                group.setText("Job: " + tName.getText() + (listener.isDisabled() ? " (Disabled)" : ""));

            }
        });
        GridData gridData71 = new org.eclipse.swt.layout.GridData(GridData.END, GridData.BEGINNING, false, false);
        label8 = new Label(gMain, SWT.NONE);
        label8.setText("Comment:");
        label8.setLayoutData(gridData71);
        GridData gridData61 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 8);
        gridData61.widthHint = 375;
        tComment = new Text(gMain, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
        tComment.setLayoutData(gridData61);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComment(tComment.getText());
            }
        });
        label1 = new Label(gMain, SWT.NONE);
        label1.setLayoutData(new GridData());
        label1.setText("Job Title:");
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
        tTitle = new Text(gMain, SWT.BORDER);
        tTitle.setLayoutData(gridData1);
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setTitle(tTitle.getText());
            }
        });
        label3 = new Label(gMain, SWT.NONE);
        label3.setLayoutData(new GridData());
        label3.setText("Scheduler ID:");
        if(listener.get_dom().isLifeElement() ||  listener.get_dom().isDirectory()) {
        	label3.setVisible(false);
        }
        GridData gridData3 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
        tSpoolerID = new Text(gMain, SWT.BORDER);
        tSpoolerID.setLayoutData(gridData3);        
        tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setSpoolerID(tSpoolerID.getText());
            }
        });
        if(listener.get_dom().isLifeElement() ||  listener.get_dom().isDirectory() ) {
        	tSpoolerID.setVisible(false);
        }
        createCombo();
        label9 = new Label(gMain, SWT.NONE);
        label9.setLayoutData(new GridData());
        label9.setText("Process Class:");
        GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        cProcessClass = new Combo(gMain, SWT.NONE);
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
        label17.setLayoutData(new GridData(41, SWT.DEFAULT));
        label17.setText("Priority:");

        sPriority = new Combo(gMain, SWT.NONE);
        sPriority.setItems(new String[] { "idle", "below_normal", "normal", "above_normal", "high" });
        sPriority.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                e.doit = (Utils.isOnlyDigits(e.text) || e.text.equals("idle") || e.text.equals("below_normal")
                        || e.text.equals("normal") || e.text.equals("above_normal") || e.text.equals("high"));

            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_1.widthHint = 80;
        gridData_1.verticalIndent = -1;
        sPriority.setLayoutData(gridData_1);
        sPriority.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(-20, 20, sPriority);
                listener.setPriority(sPriority.getText());
            }
        });
        label7 = new Label(gMain, SWT.NONE);
        label7.setLayoutData(new GridData());
        label7.setText("On Order:");
        GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, false, false);
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
        createComposite();

        final Composite composite = new Composite(gMain, SWT.NONE);
        final GridData gridData_8 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        gridData_8.widthHint = 113;
        composite.setLayoutData(gridData_8);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        composite.setLayout(gridLayout_1);

        final Label stop_on_errorLabel = new Label(composite, SWT.NONE);
        stop_on_errorLabel.setText("Stop On Error");

        bStopOnError = new Button(composite, SWT.CHECK);
        bStopOnError.setSelection(true);
        bStopOnError.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  listener.setStopOnError(bStopOnError.getSelection());
        	}
        });

        final Label minTaskLabel = new Label(gMain, SWT.NONE);
        minTaskLabel.setLayoutData(new GridData());
        minTaskLabel.setText("Min Tasks");

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
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_2.widthHint = 75;
        tMintasks.setLayoutData(gridData_2);

        label15 = new Label(gMain, SWT.NONE);
        label15.setLayoutData(new GridData());
        label15.setText("Tasks:");

        sTasks = new Text(gMain, SWT.BORDER);
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

        final GridData gridData_6 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        gridData_6.widthHint = 75;
        sTasks.setLayoutData(gridData_6);
        label13 = new Label(gMain, SWT.NONE);
        label13.setLayoutData(new GridData());
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
        gridData_9.widthHint = 75;
        sTimeout.setLayoutData(gridData_9);
        label11 = new Label(gMain, SWT.NONE);
        label11.setLayoutData(new GridData());
        label11.setText("Idle Timeout:");

        sIdleTimeout = new Text(gMain, SWT.BORDER);

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

        final GridData gridData_7 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        gridData_7.widthHint = 75;
        sIdleTimeout.setLayoutData(gridData_7);

        final Label ignore_signalLabel = new Label(gMain, SWT.NONE);
        ignore_signalLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        ignore_signalLabel.setText("Ignore Signals");

        tIgnoreSignals = new Text(gMain, SWT.BORDER);
        tIgnoreSignals.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setIgnoreSignal(tIgnoreSignals.getText());
        	}
        });
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
        gridData_3.widthHint = 48;
        tIgnoreSignals.setLayoutData(gridData_3);

        final Button addButton = new Button(gMain, SWT.NONE);
        addButton.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if (tIgnoreSignals.getText().equals("")){
      		      tIgnoreSignals.setText(cSignals.getText());
        		}else {
        		    tIgnoreSignals.setText( tIgnoreSignals.getText() + " " + cSignals.getText());
        		}
        	}
        });
        final GridData gridData_5 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
        gridData_5.widthHint = 59;
        addButton.setLayoutData(gridData_5);
        addButton.setText("<- Add <-");

        cSignals = new Combo(gMain, SWT.NONE);
        //cSignals.setItems(new String[] {"error", "success", "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS."});
        cSignals.setItems(new String[] {"SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS."});
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1);
        gridData_4.widthHint = 134;
        cSignals.setLayoutData(gridData_4);
    }


    /**
     * This method initializes combo
     */
    private void createCombo() {
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {

        final Composite composite = new Composite(gMain, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);

        final Label force_idle_timeoutLabel = new Label(composite, SWT.NONE);
        force_idle_timeoutLabel.setText("Force Idle Timeout");

        bForceIdletimeout = new Button(composite, SWT.CHECK);
        bForceIdletimeout.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                listener.setForceIdletimeout(bForceIdletimeout.getSelection());
            }
        });
    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalSpan = 1;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData18.grabExcessHorizontalSpace = true;
        gridData18.grabExcessVerticalSpace = true;
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        sashForm = new SashForm(group, SWT.VERTICAL);
        /*sashForm.addDisposeListener(new DisposeListener() {
        	public void widgetDisposed(final DisposeEvent e) {
        		Options.saveSash("job_form",  sashForm.getWeights());
        		
        		listener.get_dom().isChanged();
        	}
        });
        */
       /* sashForm.addControlListener(new ControlAdapter() {
        	public void controlResized(final ControlEvent e) {        		
        		Options.saveSash("job_form",  sashForm.getWeights());        			
        	}
        });
        */
        //sashForm.setWeights(new int[] { 1 });
        sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
        sashForm.setLayoutData(gridData18);
        
        createGroup1();
        createGroup2();
        createGroup3();
        //Options.loadSash("job_form", sashForm);
        
    }


    /**
     * This method initializes group2
     */
    private void createGroup2() {
        GridLayout gridLayout1 = new GridLayout();
        gJobParameter = new Group(sashForm, SWT.NONE);
        
        gJobParameter.setText("Job Parameter");
        gJobParameter.setLayout(gridLayout1);
        //gridLayout1.marginHeight =400;
        
        final TabFolder tabFolder = new TabFolder(gJobParameter, SWT.NONE);
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_2.heightHint = 203;
        gridData_2.widthHint = 760;
        tabFolder.setLayoutData(gridData_2);

        final TabItem parameterTabItem = new TabItem(tabFolder, SWT.NONE);
        parameterTabItem.setText("Parameter");

        final Group group_1 = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group_1.setLayout(gridLayout);
        parameterTabItem.setControl(group_1);
        label2 = new Label(group_1, SWT.NONE);
        label2.setText("Name: ");
        tParaName = new Text(group_1, SWT.BORDER);
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tParaName.setLayoutData(gridData_4);
        tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals(""))
                    addParam();
            }
        });
        tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().trim().equals(""));
            }
        });
        label6 = new Label(group_1, SWT.NONE);
        label6.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label6.setText("Value: ");
        tParaValue = new Text(group_1, SWT.BORDER);
        final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tParaValue.setLayoutData(gridData_9);
        tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.getText().trim().equals(""))
                    addParam();
            }
        });
        tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals(""));
            }
        });
        bApply = new Button(group_1, SWT.NONE);
        final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_7.widthHint = 36;
        bApply.setLayoutData(gridData_7);
        bApply.setText("&Apply");
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addParam();
            }
        });
        label4 = new Label(group_1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
        label4.setText("Label");
        tParameter = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData_1.heightHint = 85;
        tParameter.setLayoutData(gridData_1);
        tParameter.setHeaderVisible(true);
        tParameter.setLinesVisible(true);
        tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                tParaName.setText(item.getText(0));
                tParaValue.setText(item.getText(1));
                bRemove.setEnabled(tParameter.getSelectionCount() > 0);
                txtParameterDescription.setText(listener.getParameterDescription(item.getText(0)));                
                bApply.setEnabled(false);
            }
        });
        TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
        tcName.setWidth(132);
        tcName.setText("Name");
        TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
        tcValue.setWidth(450);
        tcValue.setText("Value");

        final Button butImport = new Button(group_1, SWT.NONE);
        butImport.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butImport.setText("import");
        butImport.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		startWizzard(true);
        	}
        });
        butImport.setText("Import");
        
        bRemove = new Button(group_1, SWT.NONE);
        final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
        bRemove.setLayoutData(gridData_8);
        bRemove.setText("Remove");
        bRemove.setEnabled(false);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.deleteParameter(tParameter, tParameter.getSelectionIndex());
                tParaName.setText("");
                tParaValue.setText("");
                tParameter.deselectAll();
                bRemove.setEnabled(false);
                bApply.setEnabled(false);
            }
        });

        txtParameterDescription = new Text(group_1, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);        
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, true, 4, 1);
        gridData.heightHint = 51;
        txtParameterDescription.setLayoutData(gridData);
        txtParameterDescription.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tParaName.setFocus();
        	}
        });
        
        txtParameterDescription.setEditable(false);
        txtParameterDescription.setBackground(SWTResourceManager.getColor(247, 247, 247));        
        new Label(group_1, SWT.NONE);

        final TabItem environmentTabItem = new TabItem(tabFolder, SWT.NONE);
        environmentTabItem.setText("Environment");

        final Group group_2 = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        group_2.setLayout(gridLayout_1);
        environmentTabItem.setControl(group_2);

        final Label nameLabel = new Label(group_2, SWT.NONE);
        nameLabel.setText("Name: ");

        txtEnvName = new Text(group_2, SWT.BORDER);
        txtEnvName.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		 butEnvApply.setEnabled(!txtEnvName.getText().trim().equals(""));
        	}
        });
        txtEnvName.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEnvName.equals(""))
                    addEnvironment();
        	}
        });
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        txtEnvName.setLayoutData(gridData_5);

        final Label valueLabel = new Label(group_2, SWT.NONE);
        valueLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        valueLabel.setText("Value: ");

        txtEnvValue = new Text(group_2, SWT.BORDER);
        txtEnvValue.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		 butEnvApply.setEnabled(!txtEnvName.getText().trim().equals(""));
        	}
        });
        txtEnvValue.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEnvName.equals(""))
                    addEnvironment();
        	}
        });
        txtEnvValue.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        butEnvApply = new Button(group_2, SWT.NONE);
        butEnvApply.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		addEnvironment();
        	}
        });
        final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        butEnvApply.setLayoutData(gridData_6);
        butEnvApply.setText("Apply");

        label4_1 = new Label(group_2, SWT.HORIZONTAL | SWT.SEPARATOR);
        label4_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
        label4_1.setText("Label");

        tableEnvironment = new Table(group_2, SWT.FULL_SELECTION | SWT.BORDER);
        tableEnvironment.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                txtEnvName.setText(item.getText(0));
                txtEnvValue.setText(item.getText(1));
                butEnvRemove.setEnabled(tableEnvironment.getSelectionCount() > 0);                                
                butEnvApply.setEnabled(false);
        	}
        });
        tableEnvironment.setLinesVisible(true);
        tableEnvironment.setHeaderVisible(true);
        tableEnvironment.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));

        final TableColumn colEnvName = new TableColumn(tableEnvironment, SWT.NONE);
        colEnvName.setWidth(205);
        colEnvName.setText("Name");

        final TableColumn colEnvValue = new TableColumn(tableEnvironment, SWT.NONE);
        colEnvValue.setWidth(522);
        colEnvValue.setText("Value");

        butEnvRemove = new Button(group_2, SWT.NONE);
        butEnvRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		listener.deleteEnvironment(tableEnvironment, tableEnvironment.getSelectionIndex());
                txtEnvName.setText("");
                txtEnvValue.setText("");
                tableEnvironment.deselectAll();
                butEnvApply.setEnabled(false);
                butEnvRemove.setEnabled(false);
        	}
        });
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        butEnvRemove.setLayoutData(gridData_3);
        butEnvRemove.setText("Remove");
        createTable();
    }


    /**
     * This method initializes table
     */
    private void createTable() {
    }


    /**
     * This method initializes group3
     */
    private void createGroup3() {
        GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData12.widthHint = 355;
        gridData12.horizontalIndent = 24;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.horizontalSpacing = 6;
        gridLayout3.numColumns = 3;
        gDescription = new Group(sashForm, SWT.NONE);
        gDescription.setText("Job Description");
        gDescription.setLayout(gridLayout3);
        label10 = new Label(gDescription, SWT.NONE);
        label10.setText("Include:");
        tFileName = new Text(gDescription, SWT.BORDER);
        tFileName.setLayoutData(gridData12);
                

        tFileName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setInclude(tFileName.getText());
                if(tFileName.getText()!= null && tFileName.getText().length() > 0) {
                	butShow.setEnabled(true);
                	butOpen.setEnabled(true);
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
        			if (tFileName.getText() != null && tFileName.getText().length() > 0) {
        				String sHome = sos.scheduler.editor.app.Options.getSchedulerHome();
        				if(!(sHome.endsWith("\\") || sHome.endsWith("/")))
        					sHome = sHome.concat("/");
        				
        				sHome = sHome.replaceAll("\\\\", "/");
        				
        				Program prog = Program.findProgram("html");
        				
    		            if (prog != null)
    		                prog.execute(new File((sHome).concat(tFileName.getText())).toURL().toString());
    		            else {
    		            	Runtime.getRuntime().exec(Options.getBrowserExec(new File((sHome).concat(tFileName.getText())).toURL().toString(), Options.getLanguage()));
    		            } 
        			} 
        		} catch (Exception ex) {

        			MainWindow.message(getShell(), "..could not open file " + tFileName.getText() + " " + ex.getMessage(), SWT.ICON_WARNING | SWT.OK );
        		}
        	}
        });
        butShow.setText("Show");
        /*tURL.addMouseListener(new MouseAdapter() {
        	public void mouseDown(final MouseEvent e) {
        		System.out.println("hier wird der URL geöffnet");
        		try {
        			if (urlLabel.getText() != null && urlLabel.getText().length() > 0) {
        				Process p =Runtime.getRuntime().exec("cmd /C START iExplore ".concat(tURL.getText()));
        			}
        		} catch (Exception ex) {
        			System.out.println("..could not open file " + urlLabel.getText() + " " + ex.getMessage());
        		}
        		
        		
        	}
        });
        */
        new Label(gDescription, SWT.NONE);
        GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData14.horizontalIndent = 24;
        tDescription = new Text(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
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
        	}
        });
        butOpen.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butOpen.setText("Open");
        new Label(gDescription, SWT.NONE);

        final Button butWizzard = new Button(gDescription, SWT.NONE);
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


    private void addParam() {
        listener.saveParameter( tParameter, tParaName.getText().trim(), tParaValue.getText());
        tParaName.setText("");
        tParaValue.setText("");
        bRemove.setEnabled(false);
        bApply.setEnabled(false);
        tParameter.deselectAll();
        tParaName.setFocus();
    }

    private void addEnvironment() {
    	   listener.saveEnvironment( tableEnvironment, txtEnvName.getText().trim(), txtEnvValue.getText());
    	   txtEnvName.setText("");
    	   txtEnvValue.setText("");
           butEnvRemove.setEnabled(false);
           butEnvApply.setEnabled(false);
           tableEnvironment.deselectAll();
           txtEnvName.setFocus();
           
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
        
        
        /*String[] classes = listener.getProcessClasses();
        
        if (classes == null)
            cProcessClass.setEnabled(false);
        else
            cProcessClass.setItems(classes);
        int index = cProcessClass.indexOf(listener.getProcessClass());
        if (index >= 0)
            cProcessClass.select(index);
        */
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
        tParameter.removeAll();
        if(listener.getInclude() != null && listener.getInclude().trim().length() > 0) {
        	if(new File(listener.getInclude()).exists())
        		listener.getAllParameterDescription();
        }
        listener.fillParams(tParameter);
        listener.fillEnvironment(tableEnvironment);
        tFileName.setText(listener.getInclude());
        //tURL.setText(listener.getInclude());
        tDescription.setText(listener.getDescription());
        tComment.setText(listener.getComment());
        
       

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
        tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
        tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
        bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
        bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
        tParameter.setToolTipText(Messages.getTooltip("job.param.table"));
        tFileName.setToolTipText(Messages.getTooltip("job.description.filename"));
        tDescription.setToolTipText(Messages.getTooltip("job.description"));
        txtParameterDescription.setToolTipText(Messages.getTooltip("job.param.description"));
        butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
        butShow.setToolTipText(Messages.getTooltip("job.param.show"));        
        butOpen.setToolTipText(Messages.getTooltip("job.param.open"));
        
        tableEnvironment.setToolTipText(Messages.getTooltip("job.environment.table"));        
        txtEnvName.setToolTipText(Messages.getTooltip("job.environment.name"));        
        txtEnvValue.setToolTipText(Messages.getTooltip("job.environment.value"));        
        butEnvApply.setToolTipText(Messages.getTooltip("job.environment.btn_apply"));         
        butEnvRemove.setToolTipText(Messages.getTooltip("job.environment.btn_remove")); 
    }
    
    
    private void startWizzard(boolean onlyParams) {
    	if(listener.getInclude()!= null && listener.getInclude().trim().length() > 0) {
    		//JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren        			
    		JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), listener, tParameter, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
    		if(!onlyParams)
    			paramsForm.setJobForm(this);
    		paramsForm.showAllImportJobParams(listener.getInclude());        			
    	} else { 
    		//Liste aller Jobdokumentation 
    		JobAssistentImportJobsForm importJobForms = new JobAssistentImportJobsForm(listener, tParameter, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
    		if(!onlyParams)
    			importJobForms.setJobForm(this);
    		importJobForms.showAllImportJobs();
    	}
    }


	public Table getTParameter() {
		return tParameter;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
