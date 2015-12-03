package sos.scheduler.editor.conf.forms;
import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.jdom.JDOMException;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobCommandListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public abstract class JobCommandForm extends SOSJOEMessageCodes implements IUnsaved {
	protected JobCommandListener	listener			= null;
	private Group				jobsAndOrdersGroup	= null;
	private SashForm			sashForm			= null;
	private Group				gDescription		= null;
	private Label				lblJob				= null;
	protected Text				tTitle				= null;
	protected Combo				tState				= null;
	protected Text				tStartAt			= null;
	protected Text				tPriority			= null;
	protected Text				tJobchain			= null;
    protected Button            bReplace            = null;
    protected Button            bBrowse             = null;
    protected Button            bBrowseJobChain     = null;
	protected Text				tJob				= null;
	protected Label				jobchainLabel		= null;
	protected Label				priorityLabel		= null;
	protected Label				titleLabel			= null;
	protected Label				stateLabel			= null;
	protected Label				replaceLabel		= null;
	protected Combo				cboEndstate			= null;
	protected Label				endStateLabel		= null;
	private boolean				event				= false;

	abstract void tJobModifyListener();
	abstract void clearFields();
    abstract void setCommandsEnabled();
    abstract void fillCommand();

	
	public JobCommandForm(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main) throws JDOMException, TransformerException {
		super(parent, style);
		listener = new JobCommandListener(dom, command, main);
		 
		initialize();
		event = true;
		if (command.getParentElement() != null) {
			this.jobsAndOrdersGroup.setEnabled(Utils.isElementEnabled("job", dom, command.getParentElement()));
		}
	}

	public void apply() {}

	public boolean isUnsaved() {
		return false;
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
	}

 

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.makeColumnsEqualWidth = true;
		gridLayout2.numColumns = 1;
		jobsAndOrdersGroup = new Group(this, SWT.NONE);
		String strT = JOE_M_JobCommand_CommandsForJob.params(listener.getName());
		if (listener.isDisabled()){
            strT += " " + JOE_M_JobCommand_Disabled.label();
		}
		jobsAndOrdersGroup.setText(strT);
		jobsAndOrdersGroup.setLayout(gridLayout2);
		GridData gridData18 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		sashForm = new SashForm(jobsAndOrdersGroup, SWT.NONE);
		sashForm.setOrientation(512);
		sashForm.setLayoutData(gridData18);
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 3;
		gDescription = JOE_G_JobCommand_JobsAndOrders.Control(new Group(sashForm, SWT.NONE));
		gDescription.setLayout(gridLayout3);
		jobchainLabel = JOE_L_JobCommand_JobChain.Control(new Label(gDescription, SWT.NONE));
		final GridData gridData_10 = new GridData();
		jobchainLabel.setLayoutData(gridData_10);
		
		
         
		
		tJobchain = JOE_Cbo_JobCommand_JobChain.Control(new Text(gDescription, SWT.BORDER));
        final GridData gridData_jobchain = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_jobchain.widthHint = 150;
        tJobchain.setLayoutData(gridData_jobchain);
		tJobchain.setEnabled(false);
		tJobchain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!event){
                    return;
				}
				listener.setJobChain(tJobchain.getText());
				String curstate = Utils.getAttributeValue("state", listener.getCommand());
				tState.setItems(listener.getStates());
				tState.setText(curstate);
				String curEndstate = Utils.getAttributeValue("end_state", listener.getCommand());
				cboEndstate.setItems(listener.getStates());
				cboEndstate.setText(curEndstate);
			}
		});
		
	    bBrowseJobChain = JOE_B_JobChainNodes_Browse.Control(new Button(gDescription, SWT.NONE));
	    GridData gd_butBrowseJobchain = new GridData(GridData.END, GridData.CENTER, false, false);
	    gd_butBrowseJobchain.widthHint = 50;
	    bBrowseJobChain.setLayoutData(gd_butBrowseJobchain);
	    bBrowseJobChain.addSelectionListener(new SelectionAdapter() {
	        @Override public void widgetSelected(final SelectionEvent e) {
	             String jobChain = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB_CHAIN);
	             if (jobChain != null && jobChain.length() > 0){
                     tJobchain.setText(jobChain);
	             }
	         }
	     });
	     
	        
	      
		final GridData gridData_8 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_8.widthHint = 114;
		tJobchain.setLayoutData(gridData_8);
		lblJob = JOE_L_JobCommand_JobOrderID.Control(new Label(gDescription, SWT.NONE));
		lblJob.setLayoutData(new GridData(73, SWT.DEFAULT));
		tJob = JOE_T_JobCommand_Job.Control(new Text(gDescription, SWT.BORDER));
		tJob.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
			    tJobModifyListener();
			}
		});
		
	    bBrowse = JOE_B_JobChainNodes_Browse.Control(new Button(gDescription, SWT.NONE));
        GridData gd_butBrowse = new GridData(GridData.END, GridData.CENTER, false, false);
        gd_butBrowse.widthHint = 50;
        bBrowse.setLayoutData(gd_butBrowse);
        bBrowse.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(final SelectionEvent e) {
                String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
                if (jobname != null && jobname.length() > 0){
                    tJob.setText(jobname);
                }
            }
        });
     
		
		
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_3.widthHint = 150;
		tJob.setLayoutData(gridData_3);
		final Label startAtLabel = JOE_L_JobCommand_StartAt.Control(new Label(gDescription, SWT.NONE));
		startAtLabel.setLayoutData(new GridData());
		tStartAt = JOE_T_JobCommand_StartAt.Control(new Text(gDescription, SWT.BORDER));
		tStartAt.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setAt(tStartAt.getText());
			}
		});
		
	      new Label(gDescription, SWT.NONE);

		  
		
		
		
		
		
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_4.widthHint = 150;
		tStartAt.setLayoutData(gridData_4);
		priorityLabel = JOE_L_JobCommand_Priority.Control(new Label(gDescription, SWT.NONE));
		final GridData gridData_11 = new GridData();
		priorityLabel.setLayoutData(gridData_11);
		tPriority = JOE_T_JobCommand_Priority.Control(new Text(gDescription, SWT.BORDER));
		tPriority.setEnabled(false);
		tPriority.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPriority(tPriority.getText());
			}
		});

		new Label(gDescription, SWT.NONE);

		tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		titleLabel = JOE_L_JobCommand_Title.Control(new Label(gDescription, SWT.NONE));
		titleLabel.setLayoutData(new GridData());
		tTitle = JOE_T_JobCommand_Title.Control(new Text(gDescription, SWT.BORDER));
		tTitle.setEnabled(false);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
			}
		});

		new Label(gDescription, SWT.NONE);

		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_5.widthHint = 150;
		tTitle.setLayoutData(gridData_5);
		stateLabel = JOE_L_JobCommand_State.Control(new Label(gDescription, SWT.NONE));
		stateLabel.setLayoutData(new GridData());
		tState = JOE_T_JobCommand_State.Control(new Combo(gDescription, SWT.BORDER));
		tState.setEnabled(false);
		tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (event)
					listener.setState(tState.getText());
			}
		});
		
	      new Label(gDescription, SWT.NONE);

		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.widthHint = 150;
		tState.setLayoutData(gridData_2);
		endStateLabel = JOE_L_JobCommand_EndState.Control(new Label(gDescription, SWT.NONE));
		endStateLabel.setLayoutData(new GridData());
		cboEndstate = JOE_Cbo_JobCommand_EndState.Control(new Combo(gDescription, SWT.NONE));
		cboEndstate.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (event)
					listener.setEndState(cboEndstate.getText());
			}
		});
		cboEndstate.setEnabled(false);
		cboEndstate.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
	      new Label(gDescription, SWT.NONE);

		
		replaceLabel = JOE_L_JobCommand_Replace.Control(new Label(gDescription, SWT.NONE));
		final GridData gridData_12 = new GridData();
		replaceLabel.setLayoutData(gridData_12);
		bReplace = JOE_B_JobCommand_Replace.Control(new Button(gDescription, SWT.CHECK));
		bReplace.setSelection(true);
		bReplace.setEnabled(true);
		bReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setReplace(bReplace.getSelection() ? "yes" : "no");
			}
		});
		bReplace.setLayoutData(new GridData());
	      new Label(gDescription, SWT.NONE);

		createSashForm();
	}

	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		createGroup2();
	}

	/**
	 * This method initializes group2
	 */
	private void createGroup2() {
        setCommandsEnabled();
		clearFields();
		fillCommand();
	}

 

	public Element getCommand() {
		return listener.getCommand();
	}

	
} // @jve:decl-index=0:visual-constraint="10,10"
