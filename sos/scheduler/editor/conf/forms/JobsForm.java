package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.TabbedContainer;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobsForm extends Composite implements IUpdateLanguage {
	
	private JobsListener listener;
	
	private Group           group                       = null;
	
	private static Table    table                       = null;
	
	private Button           bNewJob                     = null;
	
	private Button           bRemoveJob                  = null;
	
	private Label            label                       = null;
	
	private Button           importJobFromButton         = null;
	
	private Composite        parent                      = null;
	
	private int              style                       = 0;
	
	private SchedulerDom     dom                         = null;
	
	private ISchedulerUpdate update                      = null;
	
	private Button           butAssistent                = null;
	
	
	public JobsForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		try {
			
			this.parent = parent;
			this.style = style;
			this.dom = dom;
			this.update = update;
			listener = new JobsListener(dom, update);
			initialize();
			setToolTipText();
			listener.fillTable(table);
		} catch (Exception e) {
			System.err.println("..error in JobsForm.init() " + e.getMessage());
		}
	}
	
	
	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		} catch (Exception e) {
			System.err.println("..error in JobsForm.initialize() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			group = new Group(this, SWT.NONE);
			group.setText("Jobs");
			group.setLayout(gridLayout);
			createTable();
			bNewJob = new Button(group, SWT.NONE);
			bNewJob.setText("&New Job");
			bNewJob.setLayoutData(gridData);
			getShell().setDefaultButton(bNewJob);
			bNewJob.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					listener.newJob(table);
					bRemoveJob.setEnabled(true);
				}
			});
			GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);        
			
			importJobFromButton = new Button(group, SWT.NONE);        
			importJobFromButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {        		
					//String doc = MainWindow.getContainer().openDocumentationName();        		        		        		        
					//System.out.println("Hallo "  + doc);
					try {
						listener.openImportJobs(dom, update);
					} catch (Exception ex) {
						System.out.println("..error " + ex.getMessage());
					}
					
				}
			});
			importJobFromButton.setLayoutData(gridData1);
			importJobFromButton.setText("Import Job");
			
			butAssistent = new Button(group, SWT.NONE);
			butAssistent.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					try {
						listener.startJobAssistent(parent, style, dom, update);
					} catch (Exception ex) {
						System.out.println("..error " + ex.getMessage());
					}
				}
			});
			butAssistent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butAssistent.setText("Assistent");
			bRemoveJob = new Button(group, SWT.NONE);
			//bRemoveJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			bRemoveJob.setText("Remove Job");
			bRemoveJob.setEnabled(false);
			bRemoveJob.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					bRemoveJob.setEnabled(listener.deleteJob(table));
				}
			});
			bRemoveJob.setLayoutData(new GridData());
			
			label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			label.setText("Label");
		} catch (Exception e) {
			System.err.println("..error in JobsForm.createGroup() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
			GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 5);
			table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);
			TableColumn tableColumn5 = new TableColumn(table, SWT.NONE);
			tableColumn5.setWidth(60);
			tableColumn5.setText("Disabled");
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if (e.detail == SWT.CHECK) {
						TableItem item = (TableItem) e.item;
						if (!listener.hasJobComment((Element) item.getData())) {
							listener.setJobDisabled(item.getText(1), item.getChecked());
						} else {
							MainWindow.message(Messages.getString("MainListener.cannotDisable"), SWT.ICON_INFORMATION
									| SWT.OK);
							item.setChecked(false);
						}
					} else
						bRemoveJob.setEnabled(true);
				}
			});
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setWidth(100);
			tableColumn.setText("Name");
			TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
			tableColumn1.setWidth(200);
			tableColumn1.setText("Title");
			TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
			tableColumn2.setWidth(100);
			tableColumn2.setText("Scheduler ID");
			TableColumn tableColumn3 = new TableColumn(table, SWT.NONE);
			tableColumn3.setWidth(100);
			tableColumn3.setText("Process Class");
			TableColumn tableColumn4 = new TableColumn(table, SWT.NONE);
			tableColumn4.setWidth(40);
			tableColumn4.setText("Order");
		} catch (Exception e) {
			System.err.println("..error in JobsForm.createTable() " + e.getMessage());
		}
	}
	
	
	public void setToolTipText() {
		bNewJob.setToolTipText(Messages.getTooltip("jobs.btn_add_new"));
		bRemoveJob.setToolTipText(Messages.getTooltip("jobs.btn_remove"));
		table.setToolTipText(Messages.getTooltip("jobs.table"));
		importJobFromButton.setToolTipText(Messages.getTooltip("butImport"));
		butAssistent.setToolTipText(Messages.getTooltip("jobs.assistent"));
		
	}
	
	
	public static Table getTable() {		
		return table;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
