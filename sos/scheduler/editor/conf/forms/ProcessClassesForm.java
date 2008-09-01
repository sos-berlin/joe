/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;

/**
 * @author sky2000
 */
public class ProcessClassesForm extends Composite implements IUnsaved, IUpdateLanguage {


	private ProcessClassesListener listener      = null;

	private Group                  group         = null;

	private static Table           table         = null;

	private Label                  label1        = null;

	private Button                 bRemove       = null;

	private Button                 bNew          = null;

	private Button                 bApply        = null;

	private Text                   tProcessClass = null;

	private Label                  label5        = null;

	private Spinner                sMaxProcesses = null;

	private Label                  label10       = null;

	private Text                   tSpoolerID    = null;

	private Label                  label         = null;

	private Label                  label2        = null;

	private Text                   tRemoteHost   = null;

	private Text                   tRemotePort   = null;

	private SchedulerDom           dom           = null;

	private Button                 ignoreButton  = null; 

	private Button                 butReplace    = null; 



	/**
	 * @param parent
	 * @param style
	 * @throws JDOMException
	 */
	public ProcessClassesForm(Composite parent, int style, SchedulerDom dom_, Element config) throws JDOMException {
		
		super(parent, style);
		dom = dom_;
		listener = new ProcessClassesListener(dom, config);
		initialize();
		setToolTipText();

	}


	public void apply() {
		if (isUnsaved())
			applyClass();
	}


	public boolean isUnsaved() {
		return bApply.isEnabled();
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(694, 294));
		if(dom.isLifeElement()) {
			if(table.getItemCount() > 0)
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
		ignoreButton.setSelection(listener.isIgnoreProcessClasses());


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
		group = new Group(this, SWT.NONE);
		group.setText("Process Classes");
		group.setLayout(gridLayout);
		label1 = new Label(group, SWT.NONE);
		label1.setLayoutData(new GridData(86, SWT.DEFAULT));
		label1.setText("Process Class:");
		tProcessClass = new Text(group, SWT.BORDER);
		bApply = new Button(group, SWT.NONE);
		label5 = new Label(group, SWT.NONE);
		final GridData gridData = new GridData();
		label5.setLayoutData(gridData);
		label5.setText("Max Processes:");
		GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData4.widthHint = 20;
		sMaxProcesses = new Spinner(group, SWT.NONE);
		sMaxProcesses.setMaximum(99999999);
		sMaxProcesses.setLayoutData(gridData4);
		sMaxProcesses.setEnabled(false);
		sMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR)
					applyClass();
			}
		});
		sMaxProcesses.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(true);
			}
		});
		label10 = new Label(group, SWT.NONE);
		final GridData gridData_2 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_2.widthHint = 79;
		label10.setLayoutData(gridData_2);
		label10.setText("Scheduler ID:");
		GridData gridData6 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false);
		tSpoolerID = new Text(group, SWT.BORDER);
		tSpoolerID.setLayoutData(gridData6);
		tSpoolerID.setEnabled(false);
		tSpoolerID.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR)
					applyClass();
			}
		});
		tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(true);
			}
		});
		new Label(group, SWT.NONE);

		final Label remoteExecutionOnLabel = new Label(group, SWT.NONE);
		remoteExecutionOnLabel.setText("Executed by Scheduler on host:");

		tRemoteHost = new Text(group, SWT.BORDER);
		tRemoteHost.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApply.setEnabled(true);
			}
		});
		tRemoteHost.setEnabled(false);
		tRemoteHost.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					applyClass();

			}
		});
		tRemoteHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label portLabel = new Label(group, SWT.NONE);
		final GridData gridData_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData_1.horizontalIndent = 5;
		portLabel.setLayoutData(gridData_1);
		portLabel.setText("at Port");

		tRemotePort = new Text(group, SWT.BORDER);
		tRemotePort.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApply.setEnabled(true);

			}
		});
		tRemotePort.setEnabled(false);
		tRemotePort.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					applyClass();
			}
		});
		tRemotePort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		new Label(group, SWT.NONE);

		butReplace = new Button(group, SWT.CHECK);
		butReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				bApply.setEnabled(true);
			}
		});
		butReplace.setSelection(true);
		butReplace.setEnabled(false);
		butReplace.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 4, 1));
		butReplace.setText("Replace");
		new Label(group, SWT.NONE);
		label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label");
		label.setLayoutData(gridData7);
		createTable();
		bNew = new Button(group, SWT.NONE);
		bNew.setText("&New Process Class");
		bNew.setLayoutData(gridData1);
		getShell().setDefaultButton(bNew);
		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				apply();
				listener.newProcessClass();
				setInput(true);

				bApply.setEnabled(listener.isValidClass(tProcessClass.getText()));                
			}
		});
		GridData gridData8 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);

		label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label2.setText("Label");
		label2.setLayoutData(gridData8);
		bRemove = new Button(group, SWT.NONE);
		bRemove.setText("Remove Process Class");
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData2);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {            	
				if (table.getSelectionCount() > 0) {
					if(Utils.checkElement(table.getSelection()[0].getText(0), dom, sos.scheduler.editor.app.Editor.PROCESS_CLASSES, null)) {
						int index = table.getSelectionIndex();
						listener.removeProcessClass(index);
						table.remove(index);
						if (index >= table.getItemCount())
							index--;
						if (table.getItemCount() > 0) {
							table.select(index);
							listener.selectProcessClass(index);
							setInput(true);
						} else
							setInput(false);
					}
				}
				bRemove.setEnabled(table.getSelectionCount() > 0);
				tProcessClass.setBackground(null);
			}
		});
		tProcessClass.setLayoutData(gridData5);
		tProcessClass.setEnabled(false);
		tProcessClass.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR)
					applyClass();
			}
		});
		tProcessClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				boolean valid = listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement();
				if (valid)
					tProcessClass.setBackground(null);
				else
					tProcessClass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				bApply.setEnabled(valid);
			}
		});
		bApply.setText("&Apply Process Class");
		bApply.setLayoutData(gridData3);
		bApply.setEnabled(false);
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyClass();
			}
		});

		ignoreButton = new Button(group, SWT.CHECK);
		ignoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setIgnoreProcessClasses(ignoreButton.getSelection());
			}
		});
		ignoreButton.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		ignoreButton.setText("Ignore Process Classes");
		ignoreButton.setVisible(!dom.isLifeElement());
	 }


	 /**
	  * This method initializes table
	  */
	 private void createTable() {
		 GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 4, 4);
		 table = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
		 table.setHeaderVisible(true);
		 table.setLayoutData(gridData);
		 table.setLinesVisible(true);
		 table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 Element currElem = listener.getProcessElement(table.getSelectionIndex());
				 if(currElem != null && !Utils.isElementEnabled("process_class", dom, currElem)) {
					 setInput(false);
					 bRemove.setEnabled(false);
					 bApply.setEnabled(false);
				 } else {
					 boolean selection = table.getSelectionCount() > 0;
					 bRemove.setEnabled(selection);
					 if (selection) {
						 listener.selectProcessClass(table.getSelectionIndex());
						 setInput(true);
						 tProcessClass.setBackground(null);
					 }
				 }
			 }
		 });
		 TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		 tableColumn.setWidth(104);
		 tableColumn.setText("Process Class");
		 TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
		 tableColumn1.setWidth(91);
		 tableColumn1.setText("Max Processes");
		 TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
		 tableColumn2.setWidth(85);
		 tableColumn2.setText("Scheduler ID");

		 final TableColumn tableColumn3 = new TableColumn(table, SWT.NONE);
		 tableColumn3.setWidth(100);
		 tableColumn3.setText("Replace");
	 }


	 private void applyClass() {
		 if (!checkRemote())
			 return;

		 boolean _continue = true;
		 if(listener.getProcessClass().length() > 0 &&
				 !listener.getProcessClass().equals(tProcessClass.getText()) &&
				 !Utils.checkElement(listener.getProcessClass(), dom, sos.scheduler.editor.app.Editor.PROCESS_CLASSES, null))
			 _continue = false;

		 if(_continue)
			 listener.applyProcessClass(tProcessClass.getText(), tRemoteHost.getText(),tRemotePort.getText(),sMaxProcesses.getSelection(), tSpoolerID.getText(), butReplace.getSelection());

		 listener.fillTable(table);
		 setInput(false);
		 getShell().setDefaultButton(bNew);
		 tProcessClass.setBackground(null);
		 if(dom.isLifeElement()) {
			 setInput(true);
		 }

	 }


	 private void setInput(boolean enabled) {
		 tProcessClass.setEnabled(enabled);
		 sMaxProcesses.setEnabled(enabled);
		 tSpoolerID.setEnabled(enabled);
		 tRemoteHost.setEnabled(enabled);
		 butReplace.setEnabled(enabled);
		 tRemotePort.setEnabled(enabled);
		 if (enabled) {
			 tProcessClass.setText(listener.getProcessClass());
			 tRemoteHost.setText(listener.getRemoteHost());
			 tRemotePort.setText(listener.getRemotePort());
			 sMaxProcesses.setSelection(listener.getMaxProcesses());
			 tSpoolerID.setText(listener.getSpoolerID());
			 butReplace.setSelection(listener.isReplace());
			 tProcessClass.setFocus();

		 } else {
			 tProcessClass.setText("");
			 tRemoteHost.setText("");
			 tRemotePort.setText("");
			 sMaxProcesses.setSelection(0);
			 tSpoolerID.setText("");
			 butReplace.setSelection(true);
		 }
		 bApply.setEnabled(false);
		 bRemove.setEnabled(table.getSelectionCount() > 0);
		 // tProcessClass.setBackground(null);
	 }


	 public void setToolTipText() {
		 bNew.setToolTipText(Messages.getTooltip("process_classes.btn_new_class"));
		 bRemove.setToolTipText(Messages.getTooltip("process_classes.btn_remove_class"));
		 tProcessClass.setToolTipText(Messages.getTooltip("process_classes.class_entry"));
		 sMaxProcesses.setToolTipText(Messages.getTooltip("process_classes.max_processes_entry"));
		 tSpoolerID.setToolTipText(Messages.getTooltip("process_classes.spooler_id_entry"));
		 bApply.setToolTipText(Messages.getTooltip("process_classes.btn_apply"));
		 table.setToolTipText(Messages.getTooltip("process_classes.table"));
		 tRemoteHost.setToolTipText(Messages.getTooltip("process_classes.remoteHost"));
		 tRemotePort.setToolTipText(Messages.getTooltip("process_classes.remotePort"));
		 ignoreButton.setToolTipText(Messages.getTooltip("process_classes.ignore"));
		 butReplace.setToolTipText(Messages.getTooltip("process_classes.replace"));
	 }

	 private boolean checkRemote() {
		 if(tRemoteHost.getText().trim().length() > 0 && tRemotePort.getText().trim().length() == 0) {
			 MainWindow.message(getShell(), "Missing Scheduler Port.", SWT.ICON_WARNING | SWT.OK );
			 return false;
		 } else if (tRemoteHost.getText().trim().length() == 0 && tRemotePort.getText().trim().length() > 0) {
			 MainWindow.message(getShell(), "Missing Scheduler Host.", SWT.ICON_WARNING | SWT.OK );
			 return false;
		 }    	          
		 return true;
	 }


	public static Table getTable() {
		return table;
	}


} // @jve:decl-index=0:visual-constraint="10,10"
