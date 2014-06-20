package sos.scheduler.editor.conf.forms;
import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.JobCommandExitCodesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandExitCodesForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	private Table						tCommands			= null;
	private JobCommandExitCodesListener	listener			= null;
	private Group						jobsAndOrdersGroup	= null;
	private Group						gMain				= null;
	private SashForm					sashForm			= null;
	private boolean						updateTree			= false;
	private boolean						event				= false;
	private Combo						cExitcode			= null;
	private Button						bRemoveExitcode		= null;
	private Button						addJobButton		= null;
	private Button						addOrderButton		= null;
	private SchedulerDom				_dom				= null;

	public JobCommandExitCodesForm(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main) throws JDOMException,
			TransformerException {
		super(parent, style);
		_dom = dom;
		listener = new JobCommandExitCodesListener(dom, command, main);
		initialize();
		setToolTipText();
		dom.setInit(true);
		listener.fillCommands(tCommands);
		updateTree = false;
		cExitcode.setText(listener.getExitCode());
		updateTree = true;
		dom.setInit(false);
		event = true;
		if (command.getParentElement() != null) {
			this.jobsAndOrdersGroup.setEnabled(Utils.isElementEnabled("job", dom, command.getParentElement()));
		}
	}

	public void apply() {
		if (isUnsaved())
			addParam();
		//addCommand();
	}

	public boolean isUnsaved() {
		return false;
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		cExitcode.setFocus();
		//setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.makeColumnsEqualWidth = true;
		gridLayout2.numColumns = 1;
		jobsAndOrdersGroup = new Group(this, SWT.NONE);
		String strT = JOE_M_JobAssistent_JobGroup.params(listener.getName()) + " " + listener.getExitCode();
		if (listener.isDisabled())
			strT += " " + JOE_M_JobCommand_Disabled.label();
		jobsAndOrdersGroup.setText(strT);
		jobsAndOrdersGroup.setLayout(gridLayout2);
		GridData gridData18 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
		sashForm = new SashForm(jobsAndOrdersGroup, SWT.NONE);
		//sashForm.setWeights(new int[] { 1 });
		sashForm.setOrientation(512);
		sashForm.setLayoutData(gridData18);
		//parameter
		//createJobCommandParameter();
		//environment
		//createEnvironment();		
		createSashForm();
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		createCombo();
		createComposite();
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
	}

	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		createGroup1();
		createGroup2();
		createGroup3();
	}

	/**
	 * This method initializes group2
	 */
	private void createGroup2() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		//gDescription =  new Composite(sashForm, SWT.NONE);
		//gDescription.setText("Jobs and orders");
		gMain = JOE_G_JobCommands_Commands.Control(new Group(sashForm, SWT.NONE));
		gMain.setLayout(gridLayout);
		cExitcode = JOE_Cbo_JobCommands_Exitcode.Control(new Combo(gMain, SWT.NONE));
		cExitcode.setItems(new String[] { "error", "success", "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE",
				"SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP",
				"SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS" });
		cExitcode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setExitCode(cExitcode.getText(), updateTree);
				if (event) {
					listener.setExitCode(cExitcode.getText(), true);
				}
				// bApplyExitcode.setEnabled(!cExitcode.getText().equals(""));
			}
		});
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		cExitcode.setLayoutData(gridData_9);
		final Composite composite = new Composite(gMain, SWT.NONE);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData_1.widthHint = 63;
		composite.setLayoutData(gridData_1);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.horizontalSpacing = 0;
		gridLayout_1.verticalSpacing = 0;
		gridLayout_1.marginHeight = 0;
		gridLayout_1.marginWidth = 0;
		composite.setLayout(gridLayout_1);
		addJobButton = JOE_B_JobCommands_AddJob.Control(new Button(composite, SWT.NONE));
		addJobButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addJob();
			}
		});
		addJobButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		addOrderButton = JOE_B_JobCommands_AddOrder.Control(new Button(composite, SWT.NONE));
		addOrderButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		addOrderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addOrder();
			}
		});
		final Label exitLabel = JOE_L_JobCommands_ExitCodes.Control(new Label(gMain, SWT.NONE));
		exitLabel.setLayoutData(new GridData(73, SWT.DEFAULT));
		new Label(gMain, SWT.NONE);
		tCommands = JOE_Tbl_JobCommands_Commands.Control(new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER));
		tCommands.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				if (tCommands.getSelectionCount() > 0) {
					String name = tCommands.getSelection()[0].getText(0) + ": " + tCommands.getSelection()[0].getText(1)
							+ tCommands.getSelection()[0].getText(2);
					ContextMenu.goTo(name, _dom, JOEConstants.JOB_COMMAND_EXIT_CODES);
				}
			}
		});
		tCommands.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);
				cExitcode.setFocus();
			}
		});
		tCommands.setLinesVisible(true);
		tCommands.setHeaderVisible(true);
		final GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData9.heightHint = 149;
		tCommands.setLayoutData(gridData9);
		listener.fillCommands(tCommands);
		final TableColumn tcJob = JOE_TCl_JobCommands_Command.Control(new TableColumn(tCommands, SWT.NONE));
		tcJob.setWidth(167);
		final TableColumn tcCommand = JOE_TCl_JobCommands_JobID.Control(new TableColumn(tCommands, SWT.NONE));
		tcCommand.setWidth(154);
		final TableColumn tcJobchain = JOE_TCl_JobCommands_JobChain.Control(new TableColumn(tCommands, SWT.NONE));
		tcJobchain.setWidth(136);
		final TableColumn tcStartAt = JOE_TCl_JobCommands_StartAt.Control(new TableColumn(tCommands, SWT.NONE));
		tcStartAt.setWidth(139);
		bRemoveExitcode = JOE_B_JobCommands_Remove.Control(new Button(gMain, SWT.NONE));
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		gridData.widthHint = 67;
		bRemoveExitcode.setLayoutData(gridData);
		bRemoveExitcode.setEnabled(false);
		bRemoveExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.deleteCommand(tCommands);
				tCommands.deselectAll();
				bRemoveExitcode.setEnabled(false);
			}
		});
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
		createTable();
	}

	private void addParam() {
	}

	private void addJob() {
		//int index = tCommands.getSelectionIndex();
		Element e = null;
		//if (index == -1) {
		e = new Element("start_job");
		e.setAttribute("job", "job" + tCommands.getItemCount());
		TableItem item = new TableItem(tCommands, SWT.NONE);
		item.setText(new String[] { "start_job", "job" + tCommands.getItemCount(), "", "" });
		listener.addCommand(e);
		//		}
	}

	private void addOrder() {
		//int index = tCommands.getSelectionIndex();
		Element e = null;
		//if (index == -1) {
		e = new Element("order");
		e.setAttribute("job_chain", "job_chain" + tCommands.getItemCount());
		e.setAttribute("replace", "yes");
		TableItem item = new TableItem(tCommands, SWT.NONE);
		item.setText(new String[] { "order", "", "job_chain_" + tCommands.getItemCount(), "" });
		listener.addCommand(e);
		//		}
	}

	public Element getCommand() {
		return listener.getCommand();
	}

	public void setToolTipText() {
		//		
	}
} // @jve:decl-index=0:visual-constraint="10,10"
