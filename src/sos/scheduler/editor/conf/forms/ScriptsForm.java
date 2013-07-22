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
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.WindowsSaver;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.PreProstProcessingListener;
import sos.util.SOSClassUtil;

;
public class ScriptsForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	private PreProstProcessingListener	listener		= null;
	private Group			scriptsGroup	= null;
	private Table	        table			= null;
	private Button			butRemove		= null;
	private Label			label			= null;
	private SchedulerDom	dom				= null;
	private Button			butNew			= null;
	private static String	MONITOR			= JOE_M_ScriptsForm_Monitor.label();

	private WindowsSaver			objFormPosSizeHandler	= null;
//	objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);

	public ScriptsForm(final Composite parent, final int style, final SchedulerDom pobjXMLDom, final ISchedulerUpdate update, final Element elem) {
		super(parent, style);
		try {
			dom = pobjXMLDom;
			listener = new PreProstProcessingListener(pobjXMLDom, update, elem);
			objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
			initialize();
			setToolTipText();
			listener.fillTable(table);
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			System.err.println(JOE_E_0002.params("ScriptsForm.init() ") + e.getMessage());
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
				new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			System.err.println(JOE_E_0002.params("ScriptsForm.initialize() ") + e.getMessage());
		}
	}

	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			scriptsGroup = new Group(this, SWT.NONE);
			scriptsGroup.setText(sos.scheduler.editor.conf.listeners.SchedulerListener.MONITOR);
			scriptsGroup.setLayout(gridLayout);
			if (Utils.isElementEnabled("job", dom, listener.getParent())) {
				scriptsGroup.setEnabled(true);
			}
			else {
				scriptsGroup.setEnabled(false);
			}
			createTable();
			butRemove = JOE_B_ScriptsForm_Remove.Control(new Button(scriptsGroup, SWT.NONE));
			butRemove.setEnabled(false);
			butRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				@Override
				public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
					int c = MainWindow.message(getShell(), JOE_M_ScriptsForm_RemoveMonitor.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (c != SWT.YES)
						return;
					butRemove.setEnabled(listener.delete(table));
					table.deselectAll();
					// txtName.setText("");
					// txtOrdering.setText("");
				}
			});
			butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

			label = new Label(scriptsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
//			label.setText("Label");
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			System.err.println(JOE_E_0002.params("ScriptsForm.createGroup() ") + e.getMessage());
		}
	}

	private void createTable() {
		try {
			GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
			gridData2.widthHint = 425;
			table = JOE_Tbl_ScriptsForm_PrePostProcessing.Control(new Table(scriptsGroup, SWT.FULL_SELECTION | SWT.BORDER));
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(final MouseEvent e) {
					if (table.getSelectionCount() > 0)
						ContextMenu.goTo(Utils.getAttributeValue("name", listener.getParent()) + "_@_" + table.getSelection()[0].getText(0), dom,
								Editor.MONITOR);
				}
			});
			table.setHeaderVisible(true);
			table.setLayoutData(gridData2);
			table.setLinesVisible(true);

			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				@Override
				public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
					if (table.getSelectionCount() > 0) {

						if (Utils.isElementEnabled("job", dom, (Element) e.item.getData())) {
							butRemove.setEnabled(true);
						}
						else {
							butRemove.setEnabled(false);
							return;
						}
					}
				}
			});

			TableColumn tableColumn1 = JOE_TCl_ScriptsForm_Name.Control(new TableColumn(table, SWT.NONE));
			tableColumn1.setWidth(281);

			TableColumn tableColumn2 = JOE_TCl_ScriptsForm_Ordering.Control(new TableColumn(table, SWT.NONE));
			tableColumn2.setWidth(205);

			butNew = JOE_B_ScriptsForm_New.Control(new Button(scriptsGroup, SWT.NONE));
			butNew.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					addMonitor();
				}
			});
			butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
			}
			System.err.println(JOE_E_0002.params("ScriptsForm.createTable() ") + e.getMessage());
		}
	}

	@Override
	public void setToolTipText() {
//
	}

	private void addMonitor() {
		// if(table.getSelectionCount() > 0)
		// tabell, alte name, ordering,neue name
		// listener.save(table, table.getSelection()[0].getText(0), txtOrdering.getText(),txtName.getText() );
		// else
		// listener.save(table, txtName.getText(), txtOrdering.getText(), null );
		// txtName.setText("");
		// txtOrdering.setText("");
		// listener.save(table, "monitor" + table.getItemCount() , String.valueOf(table.getItemCount()), null );
		listener.save(table, MONITOR + table.getItemCount(), String.valueOf(table.getItemCount()), null);
		butRemove.setEnabled(false);
		table.deselectAll();
		// txtName.setFocus();
	}

//	public static Table getTable() {
//		return table;
//	}
} // @jve:decl-index=0:visual-constraint="10,10"
