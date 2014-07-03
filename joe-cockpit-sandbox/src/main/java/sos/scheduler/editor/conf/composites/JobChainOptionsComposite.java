package sos.scheduler.editor.conf.composites;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_JobChainForm_Distributed;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_JobChainForm_Recoverable;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_JobChainForm_Visible;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_JobChainForm_MaxOrders;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_JobChainForm_MaxOrders;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.conf.composites.CompositeBaseAbstract.enuOperationMode;

import com.sos.joe.objects.jobchain.JobChainListener;

public class JobChainOptionsComposite extends CompositeBaseClass {
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id$";

	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(JobChainOptionsComposite.class);
	@SuppressWarnings("unused")
	private final String		conClassName	= "JobChainOptionsComposite";

	private JobChainListener	objDataProvider	= null;

	private Text				sMaxorders		= null;
	private Button				bRecoverable	= null;
	private Button				butDistributed	= null;
	private Button				bVisible		= null;

	private Composite				objMainControl;

	public JobChainOptionsComposite(final Composite parent, final JobChainListener pobjDataProvider) {
		super(parent, SWT.NONE);
		objDataProvider = pobjDataProvider;
		objParent = parent;
		createGroup(parent);
		init();
}

	public JobChainOptionsComposite(final Group parent, final int style, final JobChainListener pobjDataProvider) {
		super(parent, style);
		objDataProvider = pobjDataProvider;
		objParent = parent;

		createGroup(parent);
		init();
	}

	private void createGroup(final Composite parent) {
		init = true;
		objMainControl = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		objMainControl.setLayout(gridLayout);
		objMainControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		@SuppressWarnings("unused")
		Label lblMaxOrders = JOE_L_JobChainForm_MaxOrders.Control(new Label(objMainControl, SWT.NONE));

		sMaxorders = JOE_T_JobChainForm_MaxOrders.Control(new Text(objMainControl, SWT.BORDER));
		sMaxorders.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent arg0) {
				if (init) {
					return;
				}
				int maxOrders;
				try {
					maxOrders = Integer.parseInt(sMaxorders.getText().trim());
				}
				catch (NumberFormatException e) {
					maxOrders = 0;
				}
				objDataProvider.setMaxorders(maxOrders);
			}
		});
		GridData gd_sMaxorders = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
		gd_sMaxorders.minimumWidth = 60;
		sMaxorders.setLayoutData(gd_sMaxorders);

		new Label(objMainControl, SWT.NONE);
		new Label(objMainControl, SWT.NONE);

		bRecoverable = JOE_B_JobChainForm_Recoverable.Control(new Button(objMainControl, SWT.CHECK));
		bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bRecoverable.setSelection(true);
		bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				if (init)
					return;
				objDataProvider.setRecoverable(bRecoverable.getSelection());
			}
		});

		new Label(objMainControl, SWT.NONE);
		new Label(objMainControl, SWT.NONE);

		butDistributed = JOE_B_JobChainForm_Distributed.Control(new Button(objMainControl, SWT.CHECK));
		butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		butDistributed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (init)
					return;
				objDataProvider.setDistributed(butDistributed.getSelection());
				// getShell().setDefaultButton(bApplyChain);
				// bApplyChain.setEnabled(true);
			}
		});
		butDistributed.setSelection(objDataProvider.isDistributed());

		new Label(objMainControl, SWT.NONE);
		new Label(objMainControl, SWT.NONE);

		bVisible = JOE_B_JobChainForm_Visible.Control(new Button(objMainControl, SWT.CHECK));
		bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bVisible.setSelection(true);
		bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				if (init)
					return;
				objDataProvider.setVisible(bVisible.getSelection());
			}
		});

		new Label(objMainControl, SWT.NONE);
	}

	public void init() {
		init = true;
		bRecoverable.setSelection(objDataProvider.getRecoverable());
		bVisible.setSelection(objDataProvider.getVisible());
		sMaxorders.setText(String.valueOf(objDataProvider.getMaxOrders()));
		butDistributed.setSelection(objDataProvider.isDistributed());
		init = false;
	}

	@Override
	protected void applyInputFields(final boolean flgT, final enuOperationMode OperationMode) {
		// TODO Auto-generated method stub

	}
}
