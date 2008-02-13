package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class ScheduleForm extends Composite implements IUpdateLanguage {
	
	private JobsListener listener;
	
	private Group           scheduleGroup                       = null;
	
	private SchedulerDom     dom                         = null;
	
	private ISchedulerUpdate update                      = null;
	
	
	
	public ScheduleForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		try {
			
			//this.parent = parent;
			//this.style = style;
			this.dom = dom;
			this.update = update;
			listener = new JobsListener(dom, update);
			initialize();
			setToolTipText();
			//listener.fillTable(table);
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.init() " + e.getMessage());
		}
	}
	
	
	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.initialize() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			scheduleGroup = new Group(this, SWT.NONE);
			scheduleGroup.setText("Schedule");
			scheduleGroup.setLayout(gridLayout);

			final Text text = new Text(scheduleGroup, SWT.BORDER);
			text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			createTable();
			//bRemoveJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.createGroup() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.createTable() " + e.getMessage());
		}
	}
	
	
	public void setToolTipText() {
		
	}
	
	
	
} // @jve:decl-index=0:visual-constraint="10,10"
