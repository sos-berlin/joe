package sos.scheduler.editor.conf.forms;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobsForm extends Composite implements IUpdateLanguage {

	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id$";

	private static Logger		logger			= Logger.getLogger(ScriptForm.class);
	@SuppressWarnings("unused")
	private final String		conClassName	= "JobsForm";

	private JobsListener		listener		= null;
	private Group				group			= null;
	private static Table		table			= null;
	private Button				bNewJob			= null;
	private Button				bRemoveJob		= null;
	private Label				label			= null;
	private SchedulerDom		dom				= null;
	private ISchedulerUpdate	update			= null;
	private Button				butAssistent	= null;
	private Button				newOrderJob		= null;
	private Composite objParent = null;
	
	public JobsForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		try {
			this.dom = dom;
			this.update = update;
			this.objParent = parent;
			listener = new JobsListener(dom, update);
			initialize();
			setToolTipText();
			listener.fillTable(table);
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println("..error in JobsForm.init() " + e.getMessage());
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
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println("..error in JobsForm.initialize() " + e.getMessage());
		}
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			group = new Group(this, SWT.NONE);
			group.setText("Jobs");// TODO lang "Jobs"
			group.setLayout(gridLayout);
			createTable();
			bNewJob = new Button(group, SWT.NONE);
			bNewJob.setText("New Standalone Job");// TODO lang "New Standalone Job"
			bNewJob.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false));
			getShell().setDefaultButton(bNewJob);
			bNewJob.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					listener.newJob(table, false);
					bRemoveJob.setEnabled(true);
				}
			});

			newOrderJob = new Button(group, SWT.NONE);
			newOrderJob.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					listener.newJob(table, true);
					bRemoveJob.setEnabled(true);
				}
			});
			newOrderJob.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			newOrderJob.setText("New Order Job");// TODO lang "New Order Job"

			butAssistent = new Button(group, SWT.NONE);
			butAssistent.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					try {
						Utils.startCursor(getShell());
						JobAssistentForm assitent = new JobAssistentForm(dom, update);
						assitent.startJobAssistant();
					}
					catch (Exception ex) {
						try {
							new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not start assistent.", ex);
						}
						catch (Exception ee) {
							// tu nichts
						}
						System.out.println("..error " + ex.getMessage());
					}
					finally {
						Utils.stopCursor(getShell());
					}
				}
			});
			butAssistent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butAssistent.setText("Job Wizard");// TODO lang "Job Wizard"
			bRemoveJob = new Button(group, SWT.NONE);
			bRemoveJob.setText("Remove Job");// TODO lang "Remove Job"
			bRemoveJob.setEnabled(false);
			bRemoveJob.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

					int c = MainWindow.message(getShell(), "Do you want remove the job?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (c != SWT.YES)
						return;

					if (Utils.checkElement(table.getSelection()[0].getText(1), dom, sos.scheduler.editor.app.Editor.JOBS, null))// wird der
																																// Job
																																// woandes
																																// verwendet?
						bRemoveJob.setEnabled(listener.deleteJob(table));
				}
			});
			bRemoveJob.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

			label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			label.setText("Label");// TODO lang "Label"
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println("..error in JobsForm.createGroup() " + e.getMessage());
		}
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
			table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
			table.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					if (table.getSelectionCount() > 0)
						ContextMenu.goTo(table.getSelection()[0].getText(1), dom, Editor.JOB);
				}
			});
			table.setHeaderVisible(true);
			table.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 5));
			table.setLinesVisible(true);
			TableColumn tableColumn5 = new TableColumn(table, SWT.NONE);
			tableColumn5.setWidth(60);
			tableColumn5.setText("Disabled");// TODO lang "Disabled"
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if (Utils.isElementEnabled("job", dom, (Element) e.item.getData())) {
						bRemoveJob.setEnabled(true);
					}
					else {
						bRemoveJob.setEnabled(false);
						return;
					}
					if (e.detail == SWT.CHECK) {
						TableItem item = (TableItem) e.item;
						if (!listener.hasJobComment((Element) item.getData())) {
							listener.setJobEnabled((Element) item.getData(), !item.getChecked());
						}
						else {
							MainWindow.message(Messages.getString("MainListener.cannotDisable"), SWT.ICON_INFORMATION | SWT.OK);
							item.setChecked(false);
						}
					}
				}
			});

			table.addMouseListener(new MouseListener() {

				@Override
				public void mouseUp(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					int index = table.getSelectionIndex();
					if (index >= 0) {
//						TableItem item = table.getItem(index);
//						Element objElement = (Element) item.getData();

						String strName = table.getSelection()[0].getText(1);
						ContextMenu.goTo(strName, dom, Editor.JOB);
//						new JobMainForm(objParent, SWT.NONE, dom, objElement, update);

					}
				}
			});

			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setWidth(100);
			tableColumn.setText("Name");// TODO lang "Name"
			TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
			tableColumn1.setWidth(200);
			tableColumn1.setText("Title");// TODO lang "Title"
			TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
			tableColumn2.setWidth(100);
			tableColumn2.setText("Scheduler ID");// TODO lang "Scheduler ID"
			TableColumn tableColumn3 = new TableColumn(table, SWT.NONE);
			tableColumn3.setWidth(100);
			tableColumn3.setText("Process Class");// TODO lang "Process Class"
			TableColumn tableColumn4 = new TableColumn(table, SWT.NONE);
			tableColumn4.setWidth(40);
			tableColumn4.setText("Order");// TODO lang "Order"
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println("..error in JobsForm.createTable() " + e.getMessage());
		}
	}

	public void setToolTipText() {
		bNewJob.setToolTipText(Messages.getTooltip("jobs.btn_add_new"));
		newOrderJob.setToolTipText(Messages.getTooltip("jobs.btn_add_new"));
		bRemoveJob.setToolTipText(Messages.getTooltip("jobs.btn_remove"));
		table.setToolTipText(Messages.getTooltip("jobs.table"));
		butAssistent.setToolTipText(Messages.getTooltip("jobs.assistent"));

	}

	public static Table getTable() {
		return table;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
