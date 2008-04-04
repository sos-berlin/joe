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
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SchedulesListener;

public class SchedulesForm extends Composite implements IUpdateLanguage {
	 
	
	private SchedulesListener  listener                = null;
	
	private Group              schedulesGroup          = null;
	
	private static Table       table                   = null;
	
	private Button              bNewSchedule           = null;
	
	private Button              butRemove              = null;
	 
	private Label               label                  = null;
	
	private SchedulerDom        dom                    = null;
	
	
	public SchedulesForm(Composite parent, int style, SchedulerDom dom_, ISchedulerUpdate update) {
		super(parent, style);
		try {
			listener = new SchedulesListener(dom_, update);
			dom = dom_;
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

			butRemove.setText("Remove");
			butRemove.setEnabled(false);
			butRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(Utils.checkElement(table.getSelection()[0].getText(0), dom, sos.scheduler.editor.app.Editor.SCHEDULES, null))//wird der Job woandes verwendet?
						butRemove.setEnabled(listener.deleteSchedule(table));
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
			table = new Table(schedulesGroup, SWT.FULL_SELECTION | SWT.BORDER);
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(table.getSelectionCount() > 0)
						butRemove.setEnabled(true);
					else 
						butRemove.setEnabled(false);
					 
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
		bNewSchedule.setToolTipText(Messages.getTooltip("schedules.btn_add_new"));
		butRemove.setToolTipText(Messages.getTooltip("schedules.btn_remove"));
		table.setToolTipText(Messages.getTooltip("schedules.table"));
		
	}
	
	
	public static Table getTable() {				
		return table;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
