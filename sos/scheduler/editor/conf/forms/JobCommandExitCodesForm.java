package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobCommandExitCodesListener;

public class JobCommandExitCodesForm extends Composite implements IUnsaved, IUpdateLanguage {
	private Table              tCommands;

	private JobCommandExitCodesListener listener;

	private Group              jobsAndOrdersGroup           = null;

	private Group              gMain           = null;

	private SashForm           sashForm        = null;

	private boolean            updateTree      = false;

	private boolean            event           = false;


	private Combo              cExitcode       = null;

	private Button             bRemoveExitcode = null;


	public JobCommandExitCodesForm(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main)
	throws JDOMException, TransformerException {
		super(parent, style);

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



		if (command.getParentElement() != null ){        	
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
		jobsAndOrdersGroup.setText("Commands for Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
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
		gMain = new Group(sashForm, SWT.NONE);
		gMain.setText("Commands");
		gMain.setLayout(gridLayout);

		cExitcode = new Combo(gMain, SWT.NONE);
		cExitcode.setItems(new String[] {"error", "success", "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS", "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR", "SIGSYS"});
		cExitcode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setExitCode(cExitcode.getText(), updateTree);
				jobsAndOrdersGroup.setText("Job: " + listener.getName() + " " + listener.getExitCode() + " "
						+ (listener.isDisabled() ? " (Disabled)" : ""));
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

		final Button addJobButton = new Button(composite, SWT.NONE);
		addJobButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addJob();
			}
		});
		addJobButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		addJobButton.setText("Add Job");

		final Button addOrderButton = new Button(composite, SWT.NONE);
		addOrderButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		addOrderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addOrder();
			}
		});
		addOrderButton.setText("Add Order");

		final Label exitLabel = new Label(gMain, SWT.NONE);
		exitLabel.setLayoutData(new GridData(73, SWT.DEFAULT));
		exitLabel.setText("Exit  codes");
		new Label(gMain, SWT.NONE);

		tCommands = new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER);
		tCommands.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);
/*
				if (item.getText(0).equals("add_order")) {
					bOrder.setSelection(true);
					bJob.setSelection(false);
					setCommandsEnabled(true);

				}

				if (item.getText(0).equals("order")) {
					bOrder.setSelection(true);
					bJob.setSelection(false);
					setCommandsEnabled(true);

				}

				if (item.getText(0).equals("start_job")) {
					bOrder.setSelection(false);
					bJob.setSelection(true);
					setCommandsEnabled(false);
					//group_2.setEnabled(tCommands.getSelectionCount() == 1);
					tabFolder.setSelection(0);
				}

				if (tCommands.getSelectionCount() > 0) {

					fillCommand();
					listener.fillParams(tCommands, tParameter);
					if(!bOrder.getSelection())
						listener.fillEnvironment(tCommands, tableEnvironment);
					else {
						listener.clearEnvironment();
						tableEnvironment.removeAll();
						tableEnvironment.clearAll();
					}
					bApplyExitcode.setEnabled(false);

				}

				bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);

				//group_2.setEnabled(item.getText(0).equals("start_job"));
				tabFolder.setEnabled(true);
				tabFolder.setSelection(0);
*/
			}
		});
		tCommands.setLinesVisible(true);
		tCommands.setHeaderVisible(true);
		final GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData9.heightHint = 149;
		tCommands.setLayoutData(gridData9);
		listener.fillCommands(tCommands);
		//gMain.setEnabled(tCommands.getItemCount() > 0);
		//group_2.setEnabled(tCommands.getSelectionCount() == 1);

		final TableColumn tcJob = new TableColumn(tCommands, SWT.NONE);
		tcJob.setWidth(167);
		tcJob.setText("Command");

		final TableColumn tcCommand = new TableColumn(tCommands, SWT.NONE);
		tcCommand.setWidth(154);
		tcCommand.setText("Job/Id");

		final TableColumn tcJobchain = new TableColumn(tCommands, SWT.NONE);
		tcJobchain.setWidth(136);
		tcJobchain.setText("Job Chain");

		final TableColumn tcStartAt = new TableColumn(tCommands, SWT.NONE);
		tcStartAt.setWidth(139);
		tcStartAt.setText("Start At");

		bRemoveExitcode = new Button(gMain, SWT.NONE);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		gridData.widthHint = 67;
		bRemoveExitcode.setLayoutData(gridData);
		bRemoveExitcode.setEnabled(false);
		bRemoveExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.deleteCommand(tCommands);                
				//clearFields();
				tCommands.deselectAll();
				bRemoveExitcode.setEnabled(false);
				//bApplyExitcode.setEnabled(false);
				//gMain.setEnabled(tCommands.getItemCount() > 0);
				//group_2.setEnabled(tCommands.getSelectionCount() == 1);
				//tabFolder.setSelection(0);
			}
		});
		bRemoveExitcode.setText("Remove");

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
		int index = tCommands.getSelectionIndex();
		Element e = null;
		if (index == -1) {

				e = new Element("start_job");				
				e.setAttribute("job", "job" + tCommands.getItemCount());
				TableItem item = new TableItem(tCommands, SWT.NONE);
				item.setText(new String[] { "start_job", "job"+tCommands.getItemCount(), "", "" });
		
				listener.addCommand(e);
				
		}
		
	}

	private void addOrder() {
		int index = tCommands.getSelectionIndex();
		Element e = null;
		if (index == -1) {
			
			e = new Element("order");			
			//e.setAttribute("id", "order" + tCommands.getItemCount());
			e.setAttribute("job_chain", "job_chain" + tCommands.getItemCount());		
			e.setAttribute("replace", "yes");
			TableItem item = new TableItem(tCommands, SWT.NONE);
			//item.setText(new String[] { "add_order", tJob.getText(), cJobchain.getText(), tStartAt.getText() });//mo
			item.setText(new String[] { "order", "", "job_chain_" + tCommands.getItemCount(), "" });
			listener.addCommand(e);

		}
	}
	/*private void addCommand() {
		String msg = "";
		if (cJobchain.getText().trim().equals("") && bOrder.getSelection()) {
			msg = "A jobchain must be given for an order";
			cJobchain.setFocus();
		} else {
			if (tJob.getText().trim().equals("") && bJob.getSelection()) {
				msg = "A jobname must be given for a job";
				tJob.setFocus();
			}
		}
		if (!msg.equals("")) {
			MainWindow.message(msg, SWT.ICON_INFORMATION);
		} else {

			Element e = null;
			int index = tCommands.getSelectionIndex();

			if (index == -1) {
				if (bJob.getSelection()) {
					e = new Element("start_job");
					e.setAttribute("at", tStartAt.getText());
					e.setAttribute("job", tJob.getText());
					TableItem item = new TableItem(tCommands, SWT.NONE);
					item.setText(new String[] { "start_job", tJob.getText(), "", tStartAt.getText() });
				} else {
					//e = new Element("add_order");
					e = new Element("order");
					e.setAttribute("at", tStartAt.getText());
					e.setAttribute("id", tJob.getText());
					e.setAttribute("priority", tPriority.getText());
					if (bReplace.getSelection()) {
						e.setAttribute("replace", "yes");
					} else {
						e.setAttribute("replace", "no");
					}
					e.setAttribute("state", tState.getText());
					e.setAttribute("job_chain", cJobchain.getText());
					e.setAttribute("title", tTitle.getText());

					TableItem item = new TableItem(tCommands, SWT.NONE);
					//item.setText(new String[] { "add_order", tJob.getText(), cJobchain.getText(), tStartAt.getText() });//mo
					item.setText(new String[] { "order", tJob.getText(), cJobchain.getText(), tStartAt.getText() });
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				}

				listener.addCommand(e);
				tCommands.setSelection(tCommands.getItemCount() - 1);
				listener.fillParams(tCommands, tParameter);
				if(bOrder.getSelection()) {
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				} else                
					listener.fillEnvironment(tCommands, tableEnvironment);


			} else {

				String cmd = tCommands.getItem(index).getText();
				if (cmd.equals("add_order") && bJob.getSelection() && tCommands.getSelectionIndex() >= 0) {
					listener.setCommandName(bApplyExitcode, "start_job", tJob.getText(), tCommands);
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "start_job");
				}

				if (cmd.equals("order") && bJob.getSelection() && tCommands.getSelectionIndex() >= 0) {
					listener.setCommandName(bApplyExitcode, "start_job", tJob.getText(), tCommands);
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "start_job");
				}

				if (cmd.equals("start_job") && bOrder.getSelection() && tCommands.getSelectionIndex() >= 0) {
					//listener.setCommandName(bNew, "add_order", tJob.getText(), tCommands);//mo
					listener.setCommandName(bNew, "order", tJob.getText(), tCommands);
					//tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "add_order"); //mo
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "order");
					tCommands.getItem(tCommands.getSelectionIndex()).setText(2, "");
					tCommands.getItem(tCommands.getSelectionIndex()).setText(3, tStartAt.getText());
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				}

				if (bJob.getSelection()) {
					listener.setCommandAttribute(bApplyExitcode, "job", tJob.getText(), tCommands);
				} else {
					listener.setCommandAttribute(bApplyExitcode, "id", tJob.getText(), tCommands);
					if (bReplace.getSelection()) {
						listener.setCommandAttribute(bApplyExitcode, "replace", "yes", tCommands);
					} else {
						listener.setCommandAttribute(bApplyExitcode, "replace", "no", tCommands);
					}
				}
				listener.setCommandAttribute(bApplyExitcode, "state", tState.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "title", tTitle.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "priority", tPriority.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "at", tStartAt.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "job_chain", cJobchain.getText(), tCommands);
				tCommands.getItem(index).setText(1, tJob.getText());
				tCommands.getItem(index).setText(3, tStartAt.getText());
				if (bOrder.getSelection()) {
					tCommands.getItem(index).setText(2, cJobchain.getText());
				} else {
					tCommands.getItem(index).setText(2, "");
				}
			}

			bApplyExitcode.setEnabled(false);
		}
	}

*/

	
	public Element getCommand() {
		return listener.getCommand();
	}


	

	public void setToolTipText() {
		cExitcode.setToolTipText(Messages.getTooltip("jobcommand.exitcode"));
	
		//bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		//tParameter.setToolTipText(Messages.getTooltip("jobcommand.param.table"));

	}

	

	
} // @jve:decl-index=0:visual-constraint="10,10"
