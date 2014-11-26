package sos.scheduler.editor.actions.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;

import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.JobCommandNamesListener;

public class JobCommandNamesForm extends Composite implements IUnsaved, IUpdateLanguage {


	private Table                         tCommands                    = null;

	private JobCommandNamesListener   listener                     = null;

	private Group                         gMain                        = null;

	private boolean                       event                        = false;

	private Button                        bRemoveExitcode              = null;

	private Button                        addJobButton                 = null;

	private Button                        addOrderButton               = null;

	private Text                          txtName                      = null;

	private Text                          txtHost                      = null;

	private Text                          txtPort                      = null;

	private ActionsDom                   _dom                          = null;


	public JobCommandNamesForm(Composite parent, int style, ActionsDom dom, Element command, ActionsForm main)	
	throws JDOMException, TransformerException {
		super(parent, style);

		listener = new JobCommandNamesListener(dom, command, main);
		_dom = dom;
		initialize();
		setToolTipText();

		dom.setInit(true);

		listener.fillCommands(tCommands);		

		dom.setInit(false);		
		event = true;

	}


	public void apply() {
		//if (isUnsaved())
		//	addParam();		
	}


	public boolean isUnsaved() {
		return false;
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();	
		txtName.setText(listener.getName());
		txtHost.setText(listener.getHost());
		txtPort.setText(listener.getPort());
		txtName.setFocus();
	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
		gMain = new Group(this, SWT.NONE);
		gMain.setText("Command: " + listener.getName());
		gMain.setLayout(gridLayout2);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		final Label nameLabel = new Label(gMain, SWT.NONE);
		nameLabel.setLayoutData(new GridData());
		nameLabel.setText("Name: ");

		txtName = new Text(gMain, SWT.BORDER);
		txtName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtName.selectAll();
			}
		});
		txtName.setBackground(SWTResourceManager.getColor(255, 255, 217));
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {

				if (event) {
					listener.setName(txtName.getText());					
				}
			}
		});
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_1.widthHint = 288;
		txtName.setLayoutData(gridData_1);

		addJobButton = new Button(gMain, SWT.NONE);
		addJobButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
		addJobButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addJob();
			}
		});
		addJobButton.setText("Add Job");

		final Label schedulerHostLabel = new Label(gMain, SWT.NONE);
		schedulerHostLabel.setLayoutData(new GridData());
		schedulerHostLabel.setText("Scheduler Host:");

		txtHost = new Text(gMain, SWT.BORDER);
		txtHost.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtHost.selectAll();
			}
		});
		txtHost.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (event) {
					listener.setHost(txtHost.getText());					
				}
			}
		});
		txtHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		addOrderButton = new Button(gMain, SWT.NONE);
		addOrderButton.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false, 2, 1));
		addOrderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addOrder();
			}
		});
		addOrderButton.setText("Add Order");

		final Label schedulerPortLabel = new Label(gMain, SWT.NONE);
		schedulerPortLabel.setLayoutData(new GridData());
		schedulerPortLabel.setText("Scheduler Port: ");

		txtPort = new Text(gMain, SWT.BORDER);		
		txtPort.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtPort.selectAll();
			}
		});
		txtPort.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtPort.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (event) {
					listener.setPort(txtPort.getText());					
				}
			}
		});

		txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		new Label(gMain, SWT.NONE);
		new Label(gMain, SWT.NONE);

		final Label label = new Label(gMain, SWT.HORIZONTAL | SWT.SEPARATOR);
		label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
		label.setText("label");
		new Label(gMain, SWT.NONE);

		tCommands = new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER);
		tCommands.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				String str =  tCommands.getSelection()[0].getText(2).length() > 0 ? tCommands.getSelection()[0].getText(2) : tCommands.getSelection()[0].getText(1);
				ContextMenu.goTo(tCommands.getSelection()[0].getText(0) + ": " + str, _dom, Editor.JOB_COMMAND_EXIT_CODES);
			}
		});
		tCommands.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);

			}
		});
		tCommands.setLinesVisible(true);
		tCommands.setHeaderVisible(true);
		final GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, false, true, 3, 1);
		gridData9.widthHint = 545;
		tCommands.setLayoutData(gridData9);
		listener.fillCommands(tCommands);

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
		final GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bRemoveExitcode.setLayoutData(gridData);
		bRemoveExitcode.setEnabled(false);
		bRemoveExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				if(tCommands != null && tCommands.getSelectionCount() > 0)  {
					int cont = MainWindow.message(getShell(), "If you really want to delete this command?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );

					if(cont == SWT.OK) {				        				
						listener.deleteCommand(tCommands);
						tCommands.deselectAll();
						bRemoveExitcode.setEnabled(false);

					} 


				}





			}
		});
		bRemoveExitcode.setText("Remove");
	}


	private void addJob() {
		Element e = null;

		e = new Element("start_job");				
		e.setAttribute("job", "job" + tCommands.getItemCount());
		TableItem item = new TableItem(tCommands, SWT.NONE);
		item.setText(new String[] { "start_job", "job"+tCommands.getItemCount(), "", "" });

		listener.addCommand(e);

	}

	private void addOrder() {
		Element e = null;

		e = new Element("order");			
		e.setAttribute("job_chain", "job_chain" + tCommands.getItemCount());		
		e.setAttribute("replace", "yes");
		TableItem item = new TableItem(tCommands, SWT.NONE);
		item.setText(new String[] { "order", "", "job_chain_" + tCommands.getItemCount(), "" });
		listener.addCommand(e);

	}



	public Element getCommand() {
		return listener.getCommand();
	}




	public void setToolTipText() {
		addJobButton.setToolTipText(Messages.getTooltip("jobcommand.exitcode.but_add_job"));
		addOrderButton.setToolTipText(Messages.getTooltip("jobcommand.exitcode.but_add_order"));
		bRemoveExitcode.setToolTipText(Messages.getTooltip("jobcommand.exitcode.but_remove_exit_codes"));
		tCommands.setToolTipText(Messages.getTooltip("jobcommand.exitcode.list"));
		txtName.setToolTipText(Messages.getTooltip("jobcommand.exitcode.name"));
		txtHost.setToolTipText(Messages.getTooltip("jobcommand.exitcode.host"));
		txtPort.setToolTipText(Messages.getTooltip("jobcommand.exitcode.port"));
	}




} // @jve:decl-index=0:visual-constraint="10,10"
