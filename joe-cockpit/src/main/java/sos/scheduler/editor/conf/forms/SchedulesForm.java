package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.SchedulesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class SchedulesForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	private SchedulesListener	listener		= null;
	private Group				schedulesGroup	= null;
	private static Table		table			= null;
	private Button				bNewSchedule	= null;
	private Button				butRemove		= null;
	private Label				label			= null;
	private SchedulerDom		dom				= null;

	public SchedulesForm(Composite parent, int style, SchedulerDom dom_, ISchedulerUpdate update) {
		super(parent, style);
		try {
			listener = new SchedulesListener(dom_, update);
			dom = dom_;
			initialize();
			setToolTipText();
			listener.fillTable(table);
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("SchedulesForm.init() ") + e.getMessage());
		}
	}

	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("SchedulesForm.initialize() ") + e.getMessage());
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
			schedulesGroup = JOE_G_SchedulesForm_Schedules.Control(new Group(this, SWT.NONE));
			schedulesGroup.setLayout(gridLayout);
			createTable();
			bNewSchedule = JOE_B_SchedulesForm_NewSchedule.Control(new Button(schedulesGroup, SWT.NONE));
			bNewSchedule.setLayoutData(gridData);
			getShell().setDefaultButton(bNewSchedule);
			bNewSchedule.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					listener.newScheduler(table);
					butRemove.setEnabled(true);
				}
			});
			butRemove = JOE_B_SchedulesForm_Remove.Control(new Button(schedulesGroup, SWT.NONE));
			butRemove.setEnabled(false);
			butRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					int c = MainWindow.message(getShell(), JOE_M_SchedulesForm_RemoveSchedule.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (c != SWT.YES)
						return;
					if (Utils.checkElement(table.getSelection()[0].getText(0), dom, JOEConstants.SCHEDULES, null))// wird
						// der
						// Job
						// woandes
						// verwendet?
						butRemove.setEnabled(listener.deleteSchedule(table));
				}
			});
			butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			label = new Label(schedulesGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			//            label.setText("Label");
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("SchedulesForm.createGroup() ") + e.getMessage());
		}
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
			GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
			table = JOE_Tbl_SchedulesForm_Schedules.Control(new Table(schedulesGroup, SWT.FULL_SELECTION | SWT.BORDER));
			table.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					if (table.getSelectionCount() > 0)
						ContextMenu.goTo(table.getSelection()[0].getText(0), dom, JOEConstants.SCHEDULE);
				}
			});
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if (table.getSelectionCount() > 0)
						butRemove.setEnabled(true);
					else
						butRemove.setEnabled(false);
				}
			});
			TableColumn tableColumn = JOE_TCl_SchedulesForm_Name.Control(new TableColumn(table, SWT.NONE));
			tableColumn.setWidth(385);
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("SchedulesForm.createTable() ") + e.getMessage());
		}
	}

	public void setToolTipText() {
		//
	}

	public static Table getTable() {
		return table;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
