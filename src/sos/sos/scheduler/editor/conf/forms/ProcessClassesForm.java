/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import sos.scheduler.editor.classes.IntegerField;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

/**
 * @author sky2000
 */
public class ProcessClassesForm extends Composite implements IUnsaved, IUpdateLanguage {


	private ProcessClassesListener listener      = null;

	private Group                  group         = null;
	private Group group_1;

	private static Table           table         = null;

	private Label                  label1        = null;

	private Button                 bRemove       = null;

	private Button                 bNew          = null;

	private Button                 bApply        = null;

	private Text                   tProcessClass = null;

	private Label                  label5        = null;

	private Text                   tMaxProcesses = null;
 
	private Label                  label         = null;

	private Label                  label2        = null;

	private Text                   tRemoteHost   = null;

	private Text                   tRemotePort   = null;

	private SchedulerDom           dom           = null;
 



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
		new Label(group_1, SWT.NONE);
		

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
		group_1 = new Group(this, SWT.NONE);
		group_1.setText("Process Classes");
		group_1.setLayout(gridLayout);
		label1 = new Label(group_1, SWT.NONE);
		label1.setLayoutData(new GridData(86, SWT.DEFAULT));
		label1.setText("Process Class:");
		tProcessClass = new Text(group_1, SWT.BORDER);
		tProcessClass.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				//tProcessClass.selectAll();
			}
		});
		tProcessClass.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {
				if(!listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement()) {
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
		bApply = new Button(group_1, SWT.NONE);
		label5 = new Label(group_1, SWT.NONE);
		label5.setText("Max Processes:");
		GridData gridData4 = new GridData(GridData.FILL, SWT.FILL, false, false);
		gridData4.widthHint = 20;
		tMaxProcesses = new IntegerField(group_1, SWT.BORDER);
		tMaxProcesses.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				bApply.setEnabled(true);
			}
		});
		tMaxProcesses.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {
				traversed(e);
			}			
		});
		tMaxProcesses.setLayoutData(gridData4);
		tMaxProcesses.setEnabled(false);
		tMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyClass();
					
					bNew.setEnabled(!bApply.getEnabled());
				}
			}
		});
		 
	 
		 
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		 
				new Label(group_1, SWT.NONE);
		
				final Label remoteExecutionOnLabel = new Label(group_1, SWT.NONE);
				remoteExecutionOnLabel.setText("Executed by Scheduler on host:");
		
				tRemoteHost = new Text(group_1, SWT.BORDER);
				tRemoteHost.addTraverseListener(new TraverseListener() {
					public void keyTraversed(final TraverseEvent e) {
						traversed(e);
						/*if (e.keyCode == SWT.CR) {
							applyClass();

							//bNew.setEnabled(!bApply.getEnabled());
						}*/
					}
				});
				tRemoteHost.addFocusListener(new FocusAdapter() {
					public void focusGained(final FocusEvent e) {
						tRemoteHost.selectAll();
					}
				});
				tRemoteHost.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						bApply.setEnabled(true);
					}
				});
				tRemoteHost.setEnabled(false);
				tRemoteHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
				final Label portLabel = new Label(group_1, SWT.NONE);
				final GridData gridData_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
				gridData_1.horizontalIndent = 5;
				portLabel.setLayoutData(gridData_1);
				portLabel.setText("at Port");
		
				tRemotePort = new IntegerField(group_1, SWT.BORDER);
				tRemotePort.addTraverseListener(new TraverseListener() {
					public void keyTraversed(final TraverseEvent e) {
						traversed(e);
						 
					}
				});
				tRemotePort.addFocusListener(new FocusAdapter() {
					public void focusGained(final FocusEvent e) {
						tRemotePort.selectAll();
					}
				});
				tRemotePort.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						bApply.setEnabled(true);

					}
				});
				tRemotePort.setEnabled(false);
				tRemotePort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		new Label(group_1, SWT.NONE);
		label = new Label(group_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label");
		label.setLayoutData(gridData7);
		createTable();
		bNew = new Button(group_1, SWT.NONE);
		bNew.setText("&New Process Class");
		bNew.setLayoutData(gridData1);
		getShell().setDefaultButton(bNew);
		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 
				listener.newProcessClass();
				setInput(true);

				bApply.setEnabled(listener.isValidClass(tProcessClass.getText()));     
				//bNew.setEnabled(false);
			}
		});

		label2 = new Label(group_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label2.setText("Label");
		label2.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false));
		bRemove = new Button(group_1, SWT.NONE);
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
				//bNew.setEnabled(true);
			}
		});
		tProcessClass.setLayoutData(gridData5);
		tProcessClass.setEnabled(false);
		 
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

		
		 
	 }


	 /**
	  * This method initializes table
	  */
	 private void createTable() {
		 table = new Table(group_1, SWT.FULL_SELECTION | SWT.BORDER);
		 table.setHeaderVisible(true);
		 table.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 4, 4));
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
				 //bNew.setEnabled(!bApply.getEnabled());
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
			 listener.applyProcessClass(tProcessClass.getText(), tRemoteHost.getText(),tRemotePort.getText(),Integer.parseInt(tMaxProcesses.getText()));

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
		 tMaxProcesses.setEnabled(enabled);
		 tRemoteHost.setEnabled(enabled);
		 tRemotePort.setEnabled(enabled);
		 
		 if (enabled) {
			 tProcessClass.setText(listener.getProcessClass());
			 tRemoteHost.setText(listener.getRemoteHost());
			 tRemotePort.setText(listener.getRemotePort());
			 tMaxProcesses.setSelection(listener.getMaxProcesses());
			 tProcessClass.setFocus();

		 } else {
			 tProcessClass.setText("");
			 tRemoteHost.setText("");
			 tRemotePort.setText("");
			 tMaxProcesses.setSelection(0);
		 }
		 
		 bApply.setEnabled(false);
		 bRemove.setEnabled(table.getSelectionCount() > 0);
 
	 }


	 public void setToolTipText() {
		 bNew.setToolTipText(Messages.getTooltip("process_classes.btn_new_class"));
		 bRemove.setToolTipText(Messages.getTooltip("process_classes.btn_remove_class"));
		 tProcessClass.setToolTipText(Messages.getTooltip("process_classes.class_entry"));
		 tMaxProcesses.setToolTipText(Messages.getTooltip("process_classes.max_processes_entry"));
		 bApply.setToolTipText(Messages.getTooltip("process_classes.btn_apply"));
		 table.setToolTipText(Messages.getTooltip("process_classes.table"));
		 tRemoteHost.setToolTipText(Messages.getTooltip("process_classes.RemoteHost"));
		 tRemotePort.setToolTipText(Messages.getTooltip("process_classes.RemotePort"));
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

	
	 private void traversed(final TraverseEvent e) {
		 	 
		 if (e.keyCode == SWT.CR) {		
				e.doit = false;
				applyClass();
				//setInput(false);
				//bNew.setEnabled(!bApply.getEnabled());
			}
	 }

} // @jve:decl-index=0:visual-constraint="10,10"
