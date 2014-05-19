package com.sos.joe.objects.jobchain.forms;

import org.apache.log4j.Logger;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;

import com.sos.joe.interfaces.ISchedulerUpdate;
import com.sos.joe.interfaces.IUnsaved;
import com.sos.joe.interfaces.IUpdateLanguage;
import com.sos.joe.objects.jobchain.JobChainListener;
import com.sos.scheduler.model.objects.JSObjJobChain;

public class JobChainForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {

    private final String        conClassName       = "JobChainForm";
    final String                conMethodName      = conClassName + "::enclosing_method";
    @SuppressWarnings("unused")
    private final String        conSVNVersion      = "$Id$";
    private static final Logger logger             = Logger.getLogger(JobChainForm.class);

    private final JobChainListener    listener           = null;
    private Group               jobChainGroup      = null;
    private Label               chainNameLabel     = null;
    private Text                tName              = null;
    private Button              bRecoverable       = null;
    private Button              bVisible           = null;
    private Button              butDetails         = null;
    private ISchedulerUpdate    update             = null;
    private Button              butDistributed     = null;
    private Text                txtTitle           = null;
    private final boolean             init               = false;
    private boolean             changeJobChainName = true;
    private Text                sMaxorders;

	private JSObjJobChain		objJobChain							= null;
	@SuppressWarnings("unused")
	private TreeData			objTreeData							= null;

	public JobChainForm(final Composite parent, final int style, final TreeData pobjTreeData) {
		super(parent, style);
		objTreeData = pobjTreeData;
		objJobChain = (JSObjJobChain) pobjTreeData.getObject();
		objJobChain.setInit(true);
		initialize();
		setToolTipText();
		InitializeAllFormControls(false, false);
		objJobChain.setInit(false);
	}

//@Deprecated
//    public JobChainForm(final Composite parent, final int style, final SchedulerDom dom, final Element jobChain) {
//        super(parent, style);
//        init = true;
//        listener = new JobChainListener(dom, jobChain);
//        initialize();
//        fillChain(false, false);
//        this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
//        init = false;
//    }

	private void InitializeAllFormControls(final boolean enable, final boolean isNew) {
//		tbxJobChainName.setEnabled(true);
//		chkJobChainIsRecoverable.setEnabled(true);
//		chkJobChainIsVisible.setEnabled(true);
//		tbxJobChainName.setText(objJobChain.getObjectName());
//		tbxJobChainTitle.setText(objJobChain.getTitle());
//		chkJobChainIsRecoverable.setSelection(objJobChain.isRecoverable());
//		chkJobChainIsVisible.setSelection(objJobChain.isVisible());
//		tbxJobChainName.setBackground(null);
//		sMaxorders.setText(objJobChain.getMaxOrders());
	}

    @Override
	public void apply() {
    }

    @Override
	public boolean isUnsaved() {
        return false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        tName.setFocus();
    }

    /**
     * This method initializes group
     */
    private void createGroup() {
        jobChainGroup = new Group(this, SWT.NONE);
//      jobChainGroup.setText(Messages.getLabel("JobChain") + ": " + (listener.getChainName() != null ? listener.getChainName() : ""));
        String strJobChainName = "";
        if (listener.getChainName() != null)
        	strJobChainName = listener.getChainName();
        jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));

        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 3;
        jobChainGroup.setLayout(gridLayout);

        chainNameLabel = JOE_L_JobChainForm_ChainName.Control(new Label(jobChainGroup, SWT.NONE));
        chainNameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));

        tName = JOE_T_JobChainForm_ChainName.Control(new Text(jobChainGroup, SWT.BORDER));
        tName.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                if (!init) {// während der initialiserung sollen keine überprüfungen stattfinden
                    // String name = listener.getChainName();
                    e.doit = Utils.checkElement(listener.getChainName(), listener.get_dom(), Editor.JOB_CHAIN, null);
                    /*System.out.println(e.doit);
                    if(e.doit) {
                    	init = true;
                    	name = name.substring(0, e.start) + e.text + name.substring(e.start,  e.end);
                    	tName.setText(name);
                    	listener.setChainName(name);
                    	init = false;
                    }
                    */
                }
            }
        });
        tName.addFocusListener(new FocusAdapter() {
            @Override
			public void focusGained(final FocusEvent e) {
                // tName.selectAll();
            }
        });
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 1, 1);
        gridData_4.widthHint = 273;

        tName.setLayoutData(gridData_4);
        tName.setText(listener.getChainName());
        tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (init)
                    return;
                String newName = tName.getText().trim();
                boolean existname = listener.isUniqueState(newName);
                if (existname)
                    tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                else {
                    // getShell().setDefaultButton(bApplyChain);
                    tName.setBackground(null);
                }
                if (update != null)
//                  update.updateTreeItem("Job Chain: " + newName);
                	update.updateTreeItem(JOE_M_JobChainForm_JobChain.params(newName));
                listener.setChainName(newName);
                String strJobChainName = "";
                if (listener.getChainName() != null)
                	strJobChainName = listener.getChainName();
                jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
//                jobChainGroup.setText("Job Chain: " + (listener.getChainName() != null ? listener.getChainName() : ""));
                changeJobChainName = true;
            }
        });

        butDetails = JOE_B_JobChainForm_Parameter.Control(new Button(jobChainGroup, SWT.NONE));
        butDetails.setEnabled(true);
        butDetails.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                if (listener.get_dom().isChanged() && changeJobChainName) {
                    if (listener.get_dom().getFilename() == null) {
//                        MainWindow.message(Messages.getLabel("jobchain.must.saved"), SWT.ICON_WARNING);
                    	MainWindow.message(JOE_M_JobChainForm_SaveChain.label(), SWT.ICON_WARNING);
                    } else {
//                        MainWindow.message(Messages.getLabel("jobchain.name.changed"), SWT.ICON_WARNING);
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

        @SuppressWarnings("unused")
		final Label titleLabel = JOE_L_JobChainForm_Title.Control(new Label(jobChainGroup, SWT.NONE));

        txtTitle = JOE_T_JobChainForm_Title.Control(new Text(jobChainGroup, SWT.BORDER));
        txtTitle.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent e) {
                if (init)
                    return;
                listener.setTitle(txtTitle.getText());
            }
        });
        txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));

//        Format
        new Label(jobChainGroup, SWT.NONE);

        @SuppressWarnings("unused")
		Label lblMaxOrders = JOE_L_JobChainForm_MaxOrders.Control(new Label(jobChainGroup, SWT.NONE));

        sMaxorders = JOE_T_JobChainForm_MaxOrders.Control(new Text(jobChainGroup, SWT.BORDER));
        sMaxorders.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent arg0) {
                if (init)
                    return;
                int maxOrders;
                try {
                    maxOrders = Integer.parseInt(sMaxorders.getText().trim());
                }
                catch (NumberFormatException e) {
                    maxOrders = 0;
                }
                listener.setMaxorders(maxOrders);
            }
        });
        GridData gd_sMaxorders = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_sMaxorders.minimumWidth = 60;
        sMaxorders.setLayoutData(gd_sMaxorders);

//        Format
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        //		new Label(jobChainGroup, SWT.NONE);

        bRecoverable = JOE_B_JobChainForm_Recoverable.Control(new Button(jobChainGroup, SWT.CHECK));
        bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bRecoverable.setSelection(true);
        bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                if (init)
                    return;
                listener.setRecoverable(bRecoverable.getSelection());
            }
        });

//        Format
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);

        butDistributed = JOE_B_JobChainForm_Distributed.Control(new Button(jobChainGroup, SWT.CHECK));
        butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        butDistributed.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(final SelectionEvent e) {
                if (init)
                    return;
                listener.setDistributed(butDistributed.getSelection());
                // getShell().setDefaultButton(bApplyChain);
                // bApplyChain.setEnabled(true);
            }
        });
        butDistributed.setSelection(listener.isDistributed());

//        Format
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);

        bVisible = JOE_B_JobChainForm_Visible.Control(new Button(jobChainGroup, SWT.CHECK));
        bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bVisible.setSelection(true);
        bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                if (init)
                    return;
                listener.setVisible(bVisible.getSelection());
                // getShell().setDefaultButton(bApplyChain);
                // bApplyChain.setEnabled(true);
            }
        });

//        Format
        new Label(jobChainGroup, SWT.NONE);

//        if (!listener.get_dom().isLifeElement()) {
//        }
    }

    private void fillChain(final boolean enable, final boolean isNew) {
        tName.setEnabled(true);
        bRecoverable.setEnabled(true);
        bVisible.setEnabled(true);

        tName.setText(listener.getChainName());
        txtTitle.setText(listener.getTitle());

        bRecoverable.setSelection(listener.getRecoverable());
        bVisible.setSelection(listener.getVisible());

        tName.setBackground(null);

        sMaxorders.setText(String.valueOf(listener.getMaxOrders()));
    }

    public void setISchedulerUpdate(final ISchedulerUpdate update_) {
        update = update_;
    }

    private void showDetails(final String state) {
        if (tName.getText() != null && tName.getText().length() > 0) {
            // OrdersListener ordersListener = new OrdersListener(listener.get_dom(), update);
            // String[] listOfOrders = ordersListener.getOrderIds();
            // DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
            // detail.showDetails();
            boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();

            if (state == null) {
                // DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders, isLifeElement,
                // listener.get_dom().getFilename());
                DetailDialogForm detail = new DetailDialogForm(tName.getText(), isLifeElement, listener.get_dom().getFilename());

                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
            }
            else {
                // DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, listOfOrders, isLifeElement,
                // listener.get_dom().getFilename());
                DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, null, isLifeElement, listener.get_dom().getFilename());

                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
            }

        }
        else {
            MainWindow.message(getShell(), JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        }

    }

    @Override
	public void setToolTipText() {
//
    }

} // @jve:decl-index=0:visual-constraint="10,10"