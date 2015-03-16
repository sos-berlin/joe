package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobMonitorUseListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class JobMonitorUseForm extends SOSJOEMessageCodes implements IUnsaved {
	private Combo				tMonitorUse		= null;
	private JobMonitorUseListener	listener	= null;
	private Group				group1			= null;
	private Label				monitorLabel	= null;
	private Label				label11			= null;
	private Button				bApplyMonitorUse	= null;
	private Table				tMonitorUseTable	= null;
	private Button				bNewMonitorUse		= null;
	private Button				bRemoveMontiorUse	= null;
	private Text				edOrdering   		= null;
	private Button				butBrowse		= null;

	public JobMonitorUseForm(Composite parent, int style, SchedulerDom dom, Element job) {
		super(parent, style);
		listener = new JobMonitorUseListener(dom, job);
		initialize();
		initMonitorUseTable(true);
		setToolTipText();
		group1.setEnabled(Utils.isElementEnabled("job", dom, job));
	}

	public void apply() {
		if (isUnsaved())
			applyMonitorUse();
	}

	public boolean isUnsaved() {
		return bApplyMonitorUse.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(678, 425));
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup() {
		GridData gridData51 = new org.eclipse.swt.layout.GridData();
		gridData51.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData51.widthHint = 90;
		gridData51.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
		group1 = JOE_G_JobMonitorUseForm_Use.Control(new Group(this, SWT.NONE));
		group1.setLayout(gridLayout1);
		monitorLabel = JOE_L_JobMonitorUseForm_Monitor.Control(new Label(group1, SWT.NONE));
		tMonitorUse = JOE_Cbo_JobMonitorUseForm_MonitorUse.Control(new Combo(group1, SWT.NONE));
		tMonitorUse.setEnabled(false);
		tMonitorUse.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (!tMonitorUse.getText().equals(""))
					getShell().setDefaultButton(bApplyMonitorUse);
				bApplyMonitorUse.setEnabled(listener.isValidMonitor(tMonitorUse.getText()));
			}
		});
		tMonitorUse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		label11 = JOE_L_JobMonitorUseForm_Ordering.Control(new Label(group1, SWT.NONE));
		label11.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		edOrdering = JOE_T_JobMonitorUseForm_Ordering.Control(new Text(group1, SWT.BORDER));
		edOrdering.setEnabled(true);
		 
		
		edOrdering.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                bApplyMonitorUse.setEnabled(true);
            }
        });
		bApplyMonitorUse = JOE_B_JobMonitorUseForm_ApplyMonitorUse.Control(new Button(group1, SWT.NONE));
		bApplyMonitorUse.setEnabled(false);
		bApplyMonitorUse.setLayoutData(gridData51);
		bApplyMonitorUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyMonitorUse();
			}
		});
		new Label(group1, SWT.NONE);
		GridData gridData30 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 3);
		this.tMonitorUseTable = JOE_Tbl_JobMonitorUseForm_MonitorUseTable.Control(new Table(group1, SWT.BORDER | SWT.FULL_SELECTION));
		this.tMonitorUseTable.setHeaderVisible(true);
		this.tMonitorUseTable.setLayoutData(gridData30);
		this.tMonitorUseTable.setLinesVisible(true);
		this.tMonitorUseTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tMonitorUseTable.getSelectionCount() > 0) {
					listener.selectMonitorUse(tMonitorUseTable.getSelectionIndex());
					initMonitorUse(true);
				}
				else
					initMonitorUse(false);
				bRemoveMontiorUse.setEnabled(tMonitorUseTable.getSelectionCount() > 0);
			}
		});
		TableColumn tableColumn5 = JOE_TCl_JobMonitorUseForm_Monitor.Control(new TableColumn(this.tMonitorUseTable, SWT.NONE));
		tableColumn5.setWidth(300);
		TableColumn tableColumn6 = JOE_TCl_JobMonitorUseForm_Ordering.Control(new TableColumn(this.tMonitorUseTable, SWT.NONE));
		tableColumn6.setWidth(70);
		GridData gridData41 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
		bNewMonitorUse = JOE_B_JobMonitorUseForm_NewMonitorUse.Control(new Button(group1, SWT.NONE));
		bNewMonitorUse.setLayoutData(gridData41);
		bNewMonitorUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				tMonitorUseTable.deselectAll();
				listener.newMonitorUse();
				initMonitorUse(true);
				tMonitorUse.setFocus();
			}
		});
		new Label(group1, SWT.NONE);
		GridData gridData31 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bRemoveMontiorUse = JOE_B_JobMonitorUseForm_RemoveMonitorUse.Control(new Button(group1, SWT.NONE));
		bRemoveMontiorUse.setEnabled(false);
		bRemoveMontiorUse.setLayoutData(gridData31);
		bRemoveMontiorUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tMonitorUseTable.getSelectionCount() > 0) {
					int index = tMonitorUseTable.getSelectionIndex();
					listener.deleteMonitorUse(index);
					tMonitorUseTable.remove(index);
					if (index >= tMonitorUseTable.getItemCount())
						index--;
					if (tMonitorUseTable.getItemCount() > 0) {
						tMonitorUseTable.setSelection(index);
						listener.selectMonitorUse(index);
						initMonitorUse(true);
					}
					else {
						initMonitorUse(false);
						bRemoveMontiorUse.setEnabled(false);
					}
				}
			}
		});
		new Label(group1, SWT.NONE);
		butBrowse = JOE_B_JobMonitorUseForm_Browse.Control(new Button(group1, SWT.NONE));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_MONITOR);
				tMonitorUse.setEnabled(true);
				if (name != null && name.length() > 0) {
					tMonitorUseTable.deselectAll();
					listener.newMonitorUse();
					initMonitorUse(true);
					tMonitorUse.setFocus();
					tMonitorUse.setText(name);
				}
			}
		});
		butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
	}

	// monitor.use
	private void initMonitorUseTable(boolean enabled) {
		tMonitorUseTable.setEnabled(enabled);
		bNewMonitorUse.setEnabled(true);
		initMonitorUse(false);
		listener.fillMonitorUse(tMonitorUseTable);
		String[] monitors = listener.getMonitors();
		if (monitors != null)
			tMonitorUse.setItems(monitors);
	}

	private void initMonitorUse(boolean enabled) {
		tMonitorUse.setEnabled(enabled);
		edOrdering.setEnabled(enabled);
		if (enabled) {
		    edOrdering.setText(listener.getOrdering());
			tMonitorUse.setText(listener.getMonitorUse());
		}
		else {
			tMonitorUse.setText("");
			edOrdering.setText("");
		}
		bApplyMonitorUse.setEnabled(false);
	}

	private void applyMonitorUse() {
		listener.applyMonitorUse(tMonitorUse.getText(), edOrdering.getText());
		listener.fillMonitorUse(this.tMonitorUseTable);
		initMonitorUse(false);
		getShell().setDefaultButton(null);
	}

	public void setToolTipText() {
		//
	}
}  
