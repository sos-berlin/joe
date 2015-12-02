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
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.OrdersListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

//import sos.scheduler.editor.conf.listeners.SchedulerListener;
public class OrdersForm extends SOSJOEMessageCodes  {
	private OrdersListener	listener;
	// private SchedulerListener mainListener;
	private Group			ordersGroup		= null;
	private static Table	table			= null;
	private Button			bNewOrder		= null;
	private Button			bRemoveOrder	= null;
	private Label			label			= null;
	private SchedulerDom	_dom			= null;

	// public OrdersForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update, SchedulerListener mainListener) {
	public OrdersForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) {
		super(parent, style);
		_dom = dom;
		// this.mainListener = mainListener;
		listener = new OrdersListener(dom, update);
		initialize();
		listener.fillTable(table);
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(656, 400));
	}

	private void createGroup() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		ordersGroup = JOE_G_OrdersForm_Orders.Control(new Group(this, SWT.NONE));
		ordersGroup.setLayout(gridLayout);
		createTable();
		bNewOrder = JOE_B_OrdersForm_NewOrder.Control(new Button(ordersGroup, SWT.NONE));
		bNewOrder.setLayoutData(gridData);
		getShell().setDefaultButton(bNewOrder);
		bNewOrder.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.newCommands(table);
				bRemoveOrder.setEnabled(true);
			}
		});
		label = new Label(ordersGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		//		label.setText("Label");
		label.setLayoutData(gridData4);
		bRemoveOrder = JOE_B_OrdersForm_RemoveOrder.Control(new Button(ordersGroup, SWT.NONE));
		bRemoveOrder.setEnabled(false);
		bRemoveOrder.setLayoutData(gridData1);
		bRemoveOrder.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//				int c = MainWindow.message(getShell(), "Do you want to remove the order?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				int c = MainWindow.message(getShell(), JOE_M_OrdersForm_RemoveOrder.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				if (c != SWT.YES)
					return;
				bRemoveOrder.setEnabled(listener.deleteCommands(table));
			}
		});
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		gridData2.widthHint = 204;
		table = JOE_Tbl_OrdersForm_Table.Control(new Table(ordersGroup, SWT.BORDER | SWT.FULL_SELECTION));
		table.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				if (table.getSelectionCount() > 0)
					ContextMenu.goTo(table.getSelection()[0].getText(0), _dom, JOEConstants.ORDER);
			}
		});
		table.setHeaderVisible(true);
		table.setLayoutData(gridData2);
		table.setLinesVisible(true);
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				boolean enabled = true;
				if (table.getSelectionIndex() > -1) {
					Element currElem = (Element) table.getSelection()[0].getData();
					if (currElem != null && !Utils.isElementEnabled("commands", _dom, currElem)) {
						enabled = false;
					}
					bRemoveOrder.setEnabled(enabled);
				}
			}
		});
		TableColumn tableColumn = JOE_TCl_OrdersForm_OrderNameID.Control(new TableColumn(table, SWT.NONE));
		tableColumn.setWidth(240);
	}


	public static Table getTable() {
		return table;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
