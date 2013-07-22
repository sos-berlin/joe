package sos.scheduler.editor.conf.forms;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_B_JobChainsForm_Details;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_B_JobChainsForm_NewChain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_B_JobChainsForm_RemoveChain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_G_JobChainsForm_JobChains;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainForm_MaxOrders;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_L_JobChainForm_Title;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JobChain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_M_JobChainsForm_RemoveChain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_TCl_JobChainsForm_Name;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_TCl_JobChainsForm_OrdersRecoverable;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_TCl_JobChainsForm_Visible;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_Tbl_JobChainsForm_JobChains;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.classes.ISOSTableMenueListeners;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainListListener;

public class JobChainsForm extends CompositeBaseClass implements IUnsaved, IUpdateLanguage, ISOSTableMenueListeners {
	private final String		conClassName					= "JobChainsForm";
	@SuppressWarnings("unused")
	private final String		conSVNVersion					= "$Id$";
	private static final Logger	logger							= Logger.getLogger(JobChainsForm.class);

	private ISchedulerUpdate	update							= null;
	private JobChainListListener	objDataProvider						= null;
	private final Group				group							= null;
	private static SOSTable		tblJobChainList					= null;
	private Button				bRemoveChain					= null;
	private Button				bNewChain						= null;
	private SashForm			sashForm						= null;
	private Button				butMaintainJobChainParameter	= null;
	private SchedulerDom		_dom							= null;

	/**Hilfsvariable: Wenn Parameter Formular geöffnet wurde muss überprüft werden, ob der Checkbox in der Tabelle - State gesetzt werden soll.*/
	private boolean				checkParameter					= false;

	public JobChainsForm(final Composite parent, final int style, final SchedulerDom dom, final Element config, final ISchedulerUpdate update_) {
		super(parent, style);
		objParent = parent;
		objDataProvider = new JobChainListListener(dom, config, update_);
		_dom = dom;
		initialize();
		update = update_;
		objDataProvider.populateJobChainsTable(tblJobChainList);
	}

	private void initialize() {
//		this.setLayout(new FillLayout());
		createGroup();
	}

	private void createGroup() {
//		group = new Group(this, SWT.NONE);
//
//		final GridLayout gridLayout = new GridLayout();
//		group.setLayout(gridLayout);
//
//		sashForm = new SashForm(group, SWT.VERTICAL);
		sashForm = new SashForm(objParent, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout());

		sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		final Group jobchainsGroup = JOE_G_JobChainsForm_JobChains.Control(new Group(sashForm, SWT.NONE));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 2;
		jobchainsGroup.setLayout(gridLayout_2);

		tblJobChainList = JOE_Tbl_JobChainsForm_JobChains.Control(new SOSTable(jobchainsGroup, SWT.BORDER, this));
		tblJobChainList.initialize();
		tblJobChainList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				if (tblJobChainList.getSelectionCount() > 0)
					ContextMenu.goTo(tblJobChainList.getSelection()[0].getText(0), objDataProvider.get_dom(), Editor.JOB_CHAIN);
			}
		});
		tblJobChainList.getHorizontalBar().setMaximum(0);
		tblJobChainList.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));
		tblJobChainList.setHeaderVisible(true);
		tblJobChainList.setLinesVisible(true);

		tblJobChainList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {

				/*if (e.detail == SWT.CHECK) {
					e.doit = false;
					return;
				}*/
				boolean enabled = true;
				if (tblJobChainList.getSelectionCount() > 0) {
					objDataProvider.selectChain(tblJobChainList.getSelectionIndex());
					Element currElem = objDataProvider.getChain();
					if (currElem != null && !Utils.isElementEnabled("job_chain", objDataProvider.get_dom(), currElem)) {
						enabled = false;
					}

					butMaintainJobChainParameter.setEnabled(true);
				}
				bRemoveChain.setEnabled(tblJobChainList.getSelectionCount() > 0 && enabled);
			}
		});

		tblJobChainList.strTableName = conClassName + "." + "tblJobChainList";
		final TableColumn tableJobChainName = JOE_TCl_JobChainsForm_Name.Control(tblJobChainList.newTableColumn("JobChainName", 150));
		final TableColumn tableTitle = JOE_L_JobChainForm_Title.Control(tblJobChainList.newTableColumn("tableTitle", 150));
		final TableColumn tableMaxOrders = JOE_L_JobChainForm_MaxOrders.Control(tblJobChainList.newTableColumn("maxOrders", 50));
		tableMaxOrders.setAlignment(SWT.CENTER);
		final TableColumn ordersRecoverableTableColumn = JOE_TCl_JobChainsForm_OrdersRecoverable.Control(tblJobChainList.newTableColumn("ordersRecoverableTableColumn", 100));
		ordersRecoverableTableColumn.setAlignment(SWT.CENTER);
		final TableColumn tableJobChainIsVisible = JOE_TCl_JobChainsForm_Visible.Control(tblJobChainList.newTableColumn("tableJobChainIsVisible", 90));
		tableJobChainIsVisible.setAlignment(SWT.CENTER);


		bNewChain = JOE_B_JobChainsForm_NewChain.Control(new Button(jobchainsGroup, SWT.NONE));
		bNewChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		getShell().setDefaultButton(bNewChain);
		bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				createNewJobChain();
			}
		});

		bRemoveChain = JOE_B_JobChainsForm_RemoveChain.Control(new Button(jobchainsGroup, SWT.NONE));
		bRemoveChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bRemoveChain.setEnabled(false);
		bRemoveChain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				int c = MainWindow.message(getShell(), JOE_M_JobChainsForm_RemoveChain.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				if (c != SWT.YES) {
					return;
				}

				if (Utils.checkElement(tblJobChainList.getSelection()[0].getText(0), objDataProvider.get_dom(), sos.scheduler.editor.app.Editor.JOB_CHAINS, null))
					//wird der Job woandes verwendet?
					deleteChain();
			}
		});

		butMaintainJobChainParameter = JOE_B_JobChainsForm_Details.Control(new Button(jobchainsGroup, SWT.NONE));
		butMaintainJobChainParameter.setEnabled(false);
		butMaintainJobChainParameter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				checkParameter = true;
				MaintainJobChainParameter(null);
			}
		});
		butMaintainJobChainParameter.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				if (checkParameter) {
					objDataProvider.populateJobChainsTable(tblJobChainList);
					checkParameter = false;
				}
			}
		});
		butMaintainJobChainParameter.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

	}

	private void deleteChain() {
		if (tblJobChainList.getSelectionCount() > 0) {
			int index = tblJobChainList.getSelectionIndex();
			objDataProvider.deleteChain(index);
			tblJobChainList.remove(index);
			if (index >= tblJobChainList.getItemCount()) {
				index--;
			}
			if (tblJobChainList.getItemCount() > 0) {
				tblJobChainList.select(index);
				objDataProvider.selectChain(index);
			}
		}
		boolean selection = tblJobChainList.getSelectionCount() > 0;
		bRemoveChain.setEnabled(selection);
	}

	private String getName4NewChain () {

		int i = tblJobChainList.getItemCount() + 1;
		String newName = JOE_M_JobChain.label() + i;
		while (objDataProvider.indexOf(newName) >= 0) {
			i++;
			//		  newName = "job_chain" + i;
			newName = JOE_M_JobChain.label() + i;
		}

		return newName;
	}

	private void applyChain() {

		String newName = getName4NewChain();

		objDataProvider.applyChain(newName, true, true);
		int index = tblJobChainList.getSelectionIndex();
		if (index == -1)
			index = tblJobChainList.getItemCount();
		objDataProvider.populateJobChainsTable(tblJobChainList);
		tblJobChainList.select(index);

		bRemoveChain.setEnabled(true);

		getShell().setDefaultButton(bNewChain);
		if (objDataProvider.getChainName() != null && objDataProvider.getChainName().length() > 0) {
			butMaintainJobChainParameter.setEnabled(true);
		}
		else {
			butMaintainJobChainParameter.setEnabled(false);
		}
	}

	@Override
	public void setToolTipText() {
		//
	}

	public void setISchedulerUpdate(final ISchedulerUpdate update_) {
		update = update_;
	}

	// TODO move to objDataProvider. see other programs with same code
	private void MaintainJobChainParameter(final String state) {
		String name = tblJobChainList.getSelection()[0].getText(0);
		if (name != null && name.length() > 0) {
			//OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			//String[] listOfOrders = ordersListener.getOrderIds();
			boolean isLifeElement = objDataProvider.get_dom().isLifeElement() || objDataProvider.get_dom().isDirectory();

			if (state == null) {
				DetailDialogForm detail = new DetailDialogForm(name, isLifeElement, objDataProvider.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(objDataProvider.get_dom(), update);
			}
			else {
				DetailDialogForm detail = new DetailDialogForm(name, state, null, isLifeElement, objDataProvider.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(objDataProvider.get_dom(), update);
			}
		}
		else {
			MainWindow.message(getShell(), JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		}
	}

	@Override
	public void apply() {
		applyChain();
	}

	@Override
	public boolean isUnsaved() {
		return false;
	}

	public void createNewJobChain() {
		objDataProvider.newChain(getName4NewChain());
		applyChain();
		tblJobChainList.deselectAll();
		butMaintainJobChainParameter.setEnabled(false);
	}

	public static SOSTable getTableChains() {
		return tblJobChainList;
	}

	@Override
	public Listener getNewListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	public Listener getDeleteListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	public Listener getCopyListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	public Listener getPasteListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	public Listener getInsertListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	public Listener getCutListener() {
		return tblJobChainList.getDummyListener();
	}

	@Override
	protected void applyInputFields(final boolean flgT) {
		// TODO Auto-generated method stub

	}

	@Override
	public Listener getEditListener() {
		// TODO Auto-generated method stub
		return null;
	}

} // @jve:decl-index=0:visual-constraint="10,10"