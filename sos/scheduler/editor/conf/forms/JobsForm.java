package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
	
	
	private JobsListener    listener                     = null;
	
	private Group           group                        = null;
	
	private static Table    table                        = null;
	
	private Button           bNewJob                     = null;
	
	private Button           bRemoveJob                  = null;
	
	private Label            label                       = null;
		
	private SchedulerDom     dom                         = null;
	
	private ISchedulerUpdate update                      = null;
	
	private Button           butAssistent                = null;
	
	
	public JobsForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		try {						
			this.dom = dom;
			this.update = update;
			listener = new JobsListener(dom, update);
			initialize();
			setToolTipText();
			listener.fillTable(table);
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in JobsForm.init() " + e.getMessage());
		}
	}
	
	
	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
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
			
			butAssistent = new Button(group, SWT.NONE);
			butAssistent.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					try {
						Utils.startCursor(getShell());
						JobAssistentForm assitent = new JobAssistentForm(dom, update);
						assitent.startJobAssistant();
					} catch (Exception ex) {
						try {
							new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not start assistent." , ex);
						} catch(Exception ee) {
							//tu nichts
						}
						System.out.println("..error " + ex.getMessage());
					} finally {
						Utils.stopCursor(getShell());
					}
				}
			});
			butAssistent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butAssistent.setText("Job Wizard");
			bRemoveJob = new Button(group, SWT.NONE);			
			bRemoveJob.setText("Remove Job");
			bRemoveJob.setEnabled(false);
			bRemoveJob.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(Utils.checkElement(table.getSelection()[0].getText(1), dom, sos.scheduler.editor.app.Editor.JOBS, null))//wird der Job woandes verwendet?
						bRemoveJob.setEnabled(listener.deleteJob(table));
				}
			});
			bRemoveJob.setLayoutData(new GridData());
			
			label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			label.setText("Label");
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in JobsForm.createGroup() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes table
	 */
	private void createTable() {
		try {
			GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 4);
			table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
			table.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					if(table.getSelectionCount() > 0)
						ContextMenu.goTo(table.getSelection()[0].getText(1), dom, Editor.JOB);					
				}
			});
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);
			TableColumn tableColumn5 = new TableColumn(table, SWT.NONE);
			tableColumn5.setWidth(60);
			tableColumn5.setText("Disabled");
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if (Utils.isElementEnabled("job", dom, (Element) e.item.getData())) {
						bRemoveJob.setEnabled(true);
					} else {
						bRemoveJob.setEnabled(false);
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
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in JobsForm.createTable() " + e.getMessage());
		}
	}
	
	
	public void setToolTipText() {
		bNewJob.setToolTipText(Messages.getTooltip("jobs.btn_add_new"));
		bRemoveJob.setToolTipText(Messages.getTooltip("jobs.btn_remove"));
		table.setToolTipText(Messages.getTooltip("jobs.table"));
		butAssistent.setToolTipText(Messages.getTooltip("jobs.assistent"));
		
	}
	
	
	public static Table getTable() {				
		return table;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
