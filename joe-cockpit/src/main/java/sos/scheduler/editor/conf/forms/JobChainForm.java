package sos.scheduler.editor.conf.forms;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainForm extends SOSJOEMessageCodes implements IUnsaved {
	private final String								conClassName		= "JobChainForm";
	final String										conMethodName		= conClassName + "::enclosing_method";
	@SuppressWarnings("unused") private final String	conSVNVersion		= "$Id$";
	private static final Logger							logger				= Logger.getLogger(JobChainForm.class);
    private JobChainListener                            listener            = null;
    private JobListener                                 jobListener         = null;
	private Group										jobChainGroup		= null;
	private Label										chainNameLabel		= null;
	private Text										tName				= null;
	private Button										bRecoverable		= null;
	private Button										bVisible			= null;
	private Button										butDetails			= null;
	private ISchedulerUpdate							update				= null;
	private Button										butDistributed		= null;
	private Text										txtTitle			= null;
	private boolean										init				= false;
	private boolean										changeJobChainName	= true;
    private Text                                        sMaxorders;
    @SuppressWarnings("unused") private Label           lblProcessClass     = null;
    private Combo                                       cProcessClass       = null;
    private Button                                      butBrowse           = null;
    private Button                                      butShowProcessClass = null;


	public JobChainForm(Composite parent, int style, SchedulerDom dom, Element jobChain) {
		super(parent, style);
		init = true;
        listener = new JobChainListener(dom, jobChain);
        jobListener = new JobListener(dom, jobChain, update);
		initialize();
		fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
		init = false;
	}

	public void apply() {
	}

	public boolean isUnsaved() {
		return false;
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(676, 464));
		tName.setFocus();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		jobChainGroup = new Group(this, SWT.NONE);
		String strJobChainName = "";
		if (listener.getChainName() != null){
            strJobChainName = listener.getChainName();
        }		
		jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 4;
		jobChainGroup.setLayout(gridLayout);
		chainNameLabel = JOE_L_JobChainForm_ChainName.Control(new Label(jobChainGroup, SWT.NONE));
		chainNameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));

        new Label(jobChainGroup, SWT.NONE);


		tName = JOE_T_JobChainForm_ChainName.Control(new Text(jobChainGroup, SWT.BORDER));
		tName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if (!init) {
					e.doit = Utils.checkElement(listener.getChainName(), listener.get_dom(), JOEConstants.JOB_CHAIN, null);
				}
			}
		});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 1, 1);
		gridData_4.widthHint = 273;
		tName.setLayoutData(gridData_4);
		tName.setText(listener.getChainName());
		tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (init){
                    return;
				}
				String newName = tName.getText().trim();
				boolean existname = Utils.existName(newName, listener.getChain(), "job_chain");
				if (existname)
					tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else {
					tName.setBackground(null);
				}
				if (update != null){
                    update.updateTreeItem(JOE_M_JobChainForm_JobChain.params(newName));
				}
				
				listener.setChainName(newName);
				String strJobChainName = "";
				if (listener.getChainName() != null)
					strJobChainName = listener.getChainName();
				jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
				changeJobChainName = true;
			}
		});
		butDetails = JOE_B_JobChainForm_Parameter.Control(new Button(jobChainGroup, SWT.NONE));
		butDetails.setEnabled(true);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (listener.get_dom().isChanged() && changeJobChainName) {
					if (listener.get_dom().getFilename() == null) {
						MainWindow.message(JOE_M_JobChainForm_SaveChain.label(), SWT.ICON_WARNING);
					}
					else {
						MainWindow.message(JOE_M_JobChainForm_ChainNameChanged.label(), SWT.ICON_WARNING);
					}
					return;
				}
				else {
					changeJobChainName = false;
				}
				showDetails(null);
			}
		});
		@SuppressWarnings("unused") final Label titleLabel = JOE_L_JobChainForm_Title.Control(new Label(jobChainGroup, SWT.NONE));

        new Label(jobChainGroup, SWT.NONE);

		txtTitle = JOE_T_JobChainForm_Title.Control(new Text(jobChainGroup, SWT.BORDER));
		txtTitle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (init){
					return;
    			}
				listener.setTitle(txtTitle.getText());
			}
		});
		txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
		new Label(jobChainGroup, SWT.NONE);
		@SuppressWarnings("unused") Label lblMaxOrders = JOE_L_JobChainForm_MaxOrders.Control(new Label(jobChainGroup, SWT.NONE));

        new Label(jobChainGroup, SWT.NONE);

		sMaxorders = JOE_T_JobChainForm_MaxOrders.Control(new Text(jobChainGroup, SWT.BORDER));
		sMaxorders.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (init){
					return;
				}
				
				listener.setMaxorders(sMaxorders.getText().trim());
				}
		});
		GridData gd_sMaxorders = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sMaxorders.minimumWidth = 60;
		sMaxorders.setLayoutData(gd_sMaxorders);
        new Label(jobChainGroup, SWT.NONE);
       

        
        
        
        @SuppressWarnings("unused") Label lblProcessClass = JOE_L_JobChainForm_ProcessClass.Control(new Label(jobChainGroup, SWT.NONE));
        butShowProcessClass = JOE_B_JobMainComposite_ShowProcessClass.Control(new Button(jobChainGroup, SWT.ARROW | SWT.DOWN));
        butShowProcessClass.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
        butShowProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, listener.get_dom(), JOEConstants.PROCESS_CLASSES);
                }
            }
        });
        butShowProcessClass.setAlignment(SWT.RIGHT);
        butShowProcessClass.setVisible(true);

        Combo combo = new Combo(jobChainGroup, SWT.NONE);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        cProcessClass = JOE_Cbo_JobMainComposite_ProcessClass.Control(combo);
        cProcessClass.setMenu(new ContextMenu(cProcessClass, listener.get_dom(), JOEConstants.PROCESS_CLASSES).getMenu());
        cProcessClass.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                listener.setProcessClass(cProcessClass.getText());
            }
        });
        cProcessClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
        cProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (init) {
                    return;
                }
                listener.setProcessClass(cProcessClass.getText());
            }
        });
         
        cProcessClass.addMouseListener(new MouseListener() {
            @Override public void mouseUp(MouseEvent arg0) {
            }

            @Override public void mouseDown(MouseEvent arg0) {
            }

            @Override public void mouseDoubleClick(MouseEvent arg0) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, listener.get_dom(), JOEConstants.PROCESS_CLASSES);
                }
            }
        });
        butBrowse = JOE_B_JobMainComposite_BrowseProcessClass.Control(new Button(jobChainGroup, SWT.NONE));
        butBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
                if (name != null && name.length() > 0)
                    cProcessClass.setText(name);
            }
        });
      
      
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button = new Button(jobChainGroup, SWT.CHECK);
        bRecoverable = JOE_B_JobChainForm_Recoverable.Control(button);

		bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bRecoverable.setSelection(true);
		bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (init){
                    return;
				}
				
				listener.setRecoverable(bRecoverable.getSelection());
			}
		});
		
		new Label(jobChainGroup, SWT.NONE);
        
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button_1 = new Button(jobChainGroup, SWT.CHECK);
        butDistributed = JOE_B_JobChainForm_Distributed.Control(button_1);

		butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		butDistributed.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
                if (init){
                    return;
                }
				listener.setDistributed(butDistributed.getSelection());
			}
		});
		butDistributed.setSelection(listener.isDistributed());

        new Label(jobChainGroup, SWT.NONE);

        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button_2 = new Button(jobChainGroup, SWT.CHECK);
        bVisible = JOE_B_JobChainForm_Visible.Control(button_2);
		bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bVisible.setSelection(true);
		bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (init){
                    return;
                }
				listener.setVisible(bVisible.getSelection());
				 
			}
		});
        new Label(jobChainGroup, SWT.NONE);
	 
	}

	private void fillChain(boolean enable, boolean isNew) {
		tName.setEnabled(true);
		bRecoverable.setEnabled(true);
		bVisible.setEnabled(true);
		tName.setText(listener.getChainName());
		txtTitle.setText(listener.getTitle());
		bRecoverable.setSelection(listener.getRecoverable());
		bVisible.setSelection(listener.getVisible());
		tName.setBackground(null);
		sMaxorders.setText(listener.getMaxOrders());
        cProcessClass.setItems(jobListener.getProcessClasses());
        cProcessClass.setText(listener.getProcessClass());
	}

	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}

	private void showDetails(String state) {
		if (tName.getText() != null && tName.getText().length() > 0) {
			boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();
			if (state == null) {
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
			}
			else {
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, null, isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
			}
		}
		else {
			MainWindow.message(getShell(), JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		}
	}

<<<<<<< HEAD
	public void setToolTipText() {
		//
	}
} // @jve:decl-index=0:visual-constraint="10,10"
=======
} // @jve:decl-index=0:visual-constraint="10,10"
>>>>>>> origin/release/1.8
