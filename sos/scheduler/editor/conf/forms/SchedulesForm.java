package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SchedulesListener;

public class SchedulesForm extends Composite implements IUpdateLanguage {
	 
	private SchedulesListener listener;
	
	private Group           schedulesGroup                       = null;
	
	private static Table    table                       = null;
	
	private Button           bNewSchedule                     = null;
	
	private Button           butRemove                  = null;
	
	private Label            label                       = null;
	
	private SchedulerDom     dom                         = null;
		
	
	public SchedulesForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		try {
			
			//this.parent = parent;
			//this.style = style;
			this.dom = dom;			
			listener = new SchedulesListener(dom, update);
			initialize();
			setToolTipText();
			listener.fillTable(table);
		} catch (Exception e) {
			System.err.println("..error in SchedulesForm.init() " + e.getMessage());
		}
	}
	
	
	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		} catch (Exception e) {
			System.err.println("..error in SchedulesForm.initialize() " + e.getMessage());
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
			schedulesGroup = new Group(this, SWT.NONE);
			schedulesGroup.setText("Schedules");
			schedulesGroup.setLayout(gridLayout);
			createTable();
			bNewSchedule = new Button(schedulesGroup, SWT.NONE);
			bNewSchedule.setText("&New Schedule");
			bNewSchedule.setLayoutData(gridData);
			getShell().setDefaultButton(bNewSchedule);
			bNewSchedule.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					listener.newScheduler(table);
					butRemove.setEnabled(true);
				}
			});
			butRemove = new Button(schedulesGroup, SWT.NONE);
			//bRemoveJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			butRemove.setText("Remove");
			butRemove.setEnabled(false);
			butRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					butRemove.setEnabled(listener.deleteJob(table));
				}
			});
			butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			
			label = new Label(schedulesGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			label.setText("Label");
		} catch (Exception e) {
			System.err.println("..error in SchedulesForm.createGroup() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
			GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
			table = new Table(schedulesGroup, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					/*if (Utils.isElementEnabled("job", dom, (Element) e.item.getData())) {
						butRemove.setEnabled(true);
					} else {
						butRemove.setEnabled(false);
						return;
					}
					if (e.detail == SWT.CHECK) {						
						TableItem item = (TableItem) e.item;
						if (!listener.hasJobComment((Element) item.getData())) {
							listener.setJobDisabled(item.getText(1), item.getChecked());
						} else {
							MainWindow.message(Messages.getString("MainListener.cannotDisable"), SWT.ICON_INFORMATION
									| SWT.OK);
							item.setChecked(false);
						}
					}
					*/ 
				}
			});
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setWidth(385);
			tableColumn.setText("Name");
		} catch (Exception e) {
			System.err.println("..error in SchedulesForm.createTable() " + e.getMessage());
		}
	}
	
	
	public void setToolTipText() {
		bNewSchedule.setToolTipText(Messages.getTooltip("jobs.btn_add_new"));
		butRemove.setToolTipText(Messages.getTooltip("jobs.btn_remove"));
		table.setToolTipText(Messages.getTooltip("jobs.table"));
		
	}
	
	
	public static Table getTable() {				
		return table;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
