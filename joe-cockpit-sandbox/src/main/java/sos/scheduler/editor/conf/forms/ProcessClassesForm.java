/**
 * 
 */
package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.classes.IntegerField;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

/**
 */
public class ProcessClassesForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	private ProcessClassesListener	listener		= null;
	//	final String					JOE_L_at_port				= "JOE_L_at_port";				// "at Port";
	//	final String					JOE_L_Apply					= "JOE_L_Apply";				// "Apply";
	//	final String					JOE_L_Remove_Process_Class	= "JOE_L_Remove_Process_Class"; // "Remove Process Class";
	private Group					group;
	private static Table			table			= null;
	private Label					label1			= null;
	private Button					bRemove			= null;
	private Button					bNew			= null;
	private Button					bApply			= null;
	private Text					tProcessClass	= null;
	private Label					label5			= null;
	private Text					tMaxProcesses	= null;
	private Label					label			= null;
	private Label					label2			= null;
	private Text					tRemoteHost		= null;
	private Text					tRemotePort		= null;
	private SchedulerDom			dom				= null;

	private TreeData			objTreeData		= null;

	public ProcessClassesForm(final Composite parent, final TreeData pobjTreeData) {
		super(parent, SWT.None);
		objTreeData = pobjTreeData;
	}


	/**
	 * @param parent
	 * @param style
	 * @throws JDOMException
	 */
@Deprecated	public ProcessClassesForm(final Composite parent, final int style, final SchedulerDom dom_, final Element config) throws JDOMException {
		super(parent, style);
		dom = dom_;
		listener = new ProcessClassesListener(dom, config);
		initialize();
		setToolTipText();
	}

	@Override public void apply() {
		if (isUnsaved())
			applyClass();
	}

	@Override public boolean isUnsaved() {
		return bApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(694, 294));
		if (dom.isLifeElement()) {
			if (table.getItemCount() > 0)
				table.setSelection(0);
			listener.selectProcessClass(0);
			setInput(true);
			tProcessClass.setBackground(null);
			setEnabled(true);
			table.setVisible(false);
			bNew.setVisible(false);
			bRemove.setVisible(false);
			label2.setVisible(false);
			label.setVisible(false);
		}
		listener.fillTable(table);
		new Label(group, SWT.NONE);
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData7 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		gridData7.heightHint = 10;
		GridData gridData5 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		gridData5.widthHint = 97;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		group = JOE_G_ProcessClassesForm_ProcessClasses.Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout);
		label1 = JOE_L_ProcessClassesForm_ProcessClass.Control(new Label(group, SWT.NONE));
		label1.setLayoutData(new GridData(86, SWT.DEFAULT));
		tProcessClass = JOE_T_ProcessClassesForm_ProcessClass.Control(new Text(group, SWT.BORDER));
		tProcessClass.addTraverseListener(new TraverseListener() {
			@Override public void keyTraversed(final TraverseEvent e) {
				if (!listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement()) {
					e.doit = false;
					return;
				}
				traversed(e);
				/*if (e.keyCode == SWT.CR) {		
					e.doit = false;
					applyClass();
					//setInput(false);
					//bNew.setEnabled(!bApply.getEnabled());
				}*/
			}
		});
		bApply = JOE_B_ProcessClassesForm_Apply.Control(new Button(group, SWT.NONE));
		label5 = JOE_L_ProcessClassesForm_MaxProcesses.Control(new Label(group, SWT.NONE));
		GridData gridData4 = new GridData(GridData.FILL, SWT.FILL, false, false);
		gridData4.widthHint = 20;
		tMaxProcesses = JOE_T_ProcessClassesForm_MaxProcesses.Control(new IntegerField(group, SWT.BORDER));
		tMaxProcesses.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent arg0) {
				bApply.setEnabled(true);
			}
		});
		tMaxProcesses.addTraverseListener(new TraverseListener() {
			@Override public void keyTraversed(final TraverseEvent e) {
				traversed(e);
			}
		});
		tMaxProcesses.setLayoutData(gridData4);
		tMaxProcesses.setEnabled(false);
		tMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override public void keyPressed(final org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyClass();
					bNew.setEnabled(!bApply.getEnabled());
				}
			}
		});
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		@SuppressWarnings("unused") final Label remoteExecutionOnLabel = JOE_L_ProcessClassesForm_remoteExecution.Control(new Label(group, SWT.NONE));
		tRemoteHost = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
		tRemoteHost.addTraverseListener(new TraverseListener() {
			@Override public void keyTraversed(final TraverseEvent e) {
				traversed(e);
				/*if (e.keyCode == SWT.CR) {
					applyClass();

					//bNew.setEnabled(!bApply.getEnabled());
				}*/
			}
		});
		tRemoteHost.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent e) {
				bApply.setEnabled(true);
			}
		});
		tRemoteHost.setEnabled(false);
		tRemoteHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		final Label portLabel = JOE_L_ProcessClassesForm_Port.Control(new Label(group, SWT.NONE));
		final GridData gridData_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData_1.horizontalIndent = 5;
		portLabel.setLayoutData(gridData_1);
		tRemotePort = JOE_T_ProcessClassesForm_Port.Control(new IntegerField(group, SWT.BORDER));
		tRemotePort.addTraverseListener(new TraverseListener() {
			@Override public void keyTraversed(final TraverseEvent e) {
				traversed(e);
			}
		});
		tRemotePort.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent e) {
				bApply.setEnabled(true);
			}
		});
		tRemotePort.setEnabled(false);
		tRemotePort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		new Label(group, SWT.NONE);
		label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		// label.setText("Label");
		label.setLayoutData(gridData7);
		createTable();
		bNew = JOE_B_ProcessClassesForm_NewProcessClass.Control(new Button(group, SWT.NONE));
		bNew.setLayoutData(gridData1);
		getShell().setDefaultButton(bNew);
		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				listener.newProcessClass();
				setInput(true);
				bApply.setEnabled(listener.isValidClass(tProcessClass.getText()));
				// bNew.setEnabled(false);
			}
		});
		label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		// label2.setText("Label");
		label2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bRemove = JOE_B_ProcessClassesForm_RemoveProcessClass.Control(new Button(group, SWT.NONE));
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData2);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				if (table.getSelectionCount() > 0) {
					if (Utils.checkElement(table.getSelection()[0].getText(0), dom, JOEConstants.PROCESS_CLASSES, null)) {
						int index = table.getSelectionIndex();
						listener.removeProcessClass(index);
						table.remove(index);
						if (index >= table.getItemCount())
							index--;
						if (table.getItemCount() > 0) {
							table.select(index);
							listener.selectProcessClass(index);
							setInput(true);
						}
						else
							setInput(false);
					}
				}
				bRemove.setEnabled(table.getSelectionCount() > 0);
				tProcessClass.setBackground(null);
				// bNew.setEnabled(true);
			}
		});
		tProcessClass.setLayoutData(gridData5);
		tProcessClass.setEnabled(false);
		tProcessClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
				boolean valid = listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement();
				if (valid)
					tProcessClass.setBackground(null);
				else
					tProcessClass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				bApply.setEnabled(valid);
			}
		});
		bApply.setLayoutData(gridData3);
		bApply.setEnabled(false);
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				applyClass();
			}
		});
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
		table = JOE_Tbl_ProcessClassesForm_ProcessClasses.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 4));
		table.setLinesVisible(true);
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				Element currElem = listener.getProcessElement(table.getSelectionIndex());
				if (currElem != null && !Utils.isElementEnabled("process_class", dom, currElem)) {
					setInput(false);
					bRemove.setEnabled(false);
					bApply.setEnabled(false);
				}
				else {
					boolean selection = table.getSelectionCount() > 0;
					bRemove.setEnabled(selection);
					if (selection) {
						listener.selectProcessClass(table.getSelectionIndex());
						setInput(true);
						tProcessClass.setBackground(null);
					}
				}
				// bNew.setEnabled(!bApply.getEnabled());
			}
		});
		TableColumn tableColumn = JOE_TCl_ProcessClassesForm_ProcessClass.Control(new TableColumn(table, SWT.NONE));
		tableColumn.setWidth(104);
		TableColumn tableColumn1 = JOE_TCl_ProcessClassesForm_MaxProcesses.Control(new TableColumn(table, SWT.NONE));
		tableColumn1.setWidth(91);
		TableColumn tableColumn2 = JOE_TCl_ProcessClassesForm_RemoteExecution.Control(new TableColumn(table, SWT.NONE));
		tableColumn2.setWidth(355);
	}

	private void applyClass() {
		if (!checkRemote())
			return;
		boolean _continue = true;
		if (listener.getProcessClass().length() > 0 && !listener.getProcessClass().equals(tProcessClass.getText())
				&& !Utils.checkElement(listener.getProcessClass(), dom, JOEConstants.PROCESS_CLASSES, null))
			_continue = false;
		if (_continue)
			try {
				Integer.parseInt(tMaxProcesses.getText());
			}
			catch (NumberFormatException e) {
				tMaxProcesses.setText("1");
			}
		listener.applyProcessClass(tProcessClass.getText(), tRemoteHost.getText(), tRemotePort.getText(), Integer.parseInt(tMaxProcesses.getText()));
		listener.fillTable(table);
		setInput(false);
		getShell().setDefaultButton(bNew);
		tProcessClass.setBackground(null);
		if (dom.isLifeElement()) {
			setInput(true);
		}
	}

	private void setInput(final boolean enabled) {
		tProcessClass.setEnabled(enabled);
		tMaxProcesses.setEnabled(enabled);
		tRemoteHost.setEnabled(enabled);
		tRemotePort.setEnabled(enabled);
		if (enabled) {
			tProcessClass.setText(listener.getProcessClass());
			tRemoteHost.setText(listener.getRemoteHost());
			tRemotePort.setText(listener.getRemotePort());
			tMaxProcesses.setText(String.valueOf(listener.getMaxProcesses()));
			tProcessClass.setFocus();
		}
		else {
			tProcessClass.setText("");
			tRemoteHost.setText("");
			tRemotePort.setText("");
			tMaxProcesses.setText("");
		}
		bApply.setEnabled(false);
		bRemove.setEnabled(table.getSelectionCount() > 0);
	}

	@Override public void setToolTipText() {
		//
	}

	private boolean checkRemote() {
		if (tRemoteHost.getText().trim().length() > 0 && tRemotePort.getText().trim().length() == 0) {
			MainWindow.message(getShell(), JOE_M_ProcessClassesForm_MissingPort.label(), SWT.ICON_WARNING | SWT.OK);
			return false;
		}
		else
			if (tRemoteHost.getText().trim().length() == 0 && tRemotePort.getText().trim().length() > 0) {
				MainWindow.message(getShell(), JOE_M_ProcessClassesForm_MissingHost.label(), SWT.ICON_WARNING | SWT.OK);
				return false;
			}
		return true;
	}

	public static Table getTable() {
		return table;
	}

	private void traversed(final TraverseEvent e) {
		if (e.keyCode == SWT.CR) {
			e.doit = false;
			applyClass();
			// setInput(false);
			// bNew.setEnabled(!bApply.getEnabled());
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"
