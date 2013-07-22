/**
 *
 */
package sos.scheduler.editor.classes;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import sos.scheduler.editor.app.ResourceManager;

import com.sos.dialog.classes.FormBase;

/**
 * @author KB
 *
 */
public class SOSTable extends Table implements ISOSTableMenueListeners {

	@SuppressWarnings("unused")
	private final String			conClassName			= "SOSTable";
	private static final String		conSVNVersion			= "$Id$";
	private static final Logger		logger					= Logger.getLogger(FormBase.class);
	public ISOSTableMenueListeners	objListener				= this;

	public String					strTableName			= "";

	private WindowsSaver			objFormPosSizeHandler	= null;
	public Menu						objContextMenu			= null;
	private Composite				objParent				= null;
	private final TableColumnLayout	objTableLayout			= new TableColumnLayout();
	private Table					objTable				= null;

	public SOSTable(final Composite parent, final int style, final ISOSTableMenueListeners pobjListener) {
		super(parent, style | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
		if (pobjListener != null) {
			objListener = pobjListener;
		}
		else {
			objListener = this;
		}
		//		parent.setLayout(objTableLayout);
		objParent = parent;
		objTable = this;
	}

	public void initialize() {
		super.setLayoutDeferred(true);
//		super.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		super.setLinesVisible(true);
		super.setHeaderVisible(true);

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		DragSource source = new DragSource(this, DND.DROP_MOVE | DND.DROP_COPY);
		source.setTransfer(types);

		source.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(final DragSourceEvent event) {
				// Get the selected items in the drag source
				DragSource ds = (DragSource) event.widget;
				Table table = (Table) ds.getControl();
				TableItem[] selection = table.getSelection();

				StringBuffer buff = new StringBuffer();
				for (TableItem element : selection) {
					buff.append(element.getText());
				}

				event.data = buff.toString();
			}
		});

		   // Create the drop target
	    DropTarget target = new DropTarget(this, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
	    target.setTransfer(types);
	    target.addDropListener(new DropTargetAdapter() {
	      @Override
		public void dragEnter(final DropTargetEvent event) {
	        if (event.detail == DND.DROP_DEFAULT) {
	          event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY : DND.DROP_NONE;
	        }

	        // Allow dropping text only
	        for (TransferData dataType : event.dataTypes) {
	          if (TextTransfer.getInstance().isSupportedType(dataType)) {
	            event.currentDataType = dataType;
	          }
	        }
	      }

	      @Override
		public void dragOver(final DropTargetEvent event) {
	         event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	      }
	      @Override
		public void drop(final DropTargetEvent event) {
	        if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
	          // Get the dropped data
	          DropTarget target = (DropTarget) event.widget;
	          Table table = (Table) target.getControl();
	          String data = (String) event.data;

	          // Create a new item in the table to hold the dropped data
	          TableItem item = new TableItem(table, SWT.NONE);
	          item.setText(new String[] { data });
	          table.redraw();
	        }
	      }
	    });
//	    TableColumn column = new TableColumn(this, SWT.NONE);

		//		for (TableColumn tableColumn : getColumns()) {
		//			tableColumn.setMoveable(true);
		//			//		      tableColumn.addListener(SWT.Move, listener);
		//		}
		objContextMenu = new Menu(getShell(), SWT.POP_UP);
		this.setMenu(objContextMenu);
		MenuItem itemNew = new MenuItem(objContextMenu, SWT.PUSH);
		itemNew.addListener(SWT.Selection, objListener.getNewListener());
		setMenuItemText(itemNew, "New", "N", 'N', true);
		itemNew.setImage(getImage("new_text.gif"));
		new MenuItem(objContextMenu, SWT.SEPARATOR);

		{
			Listener objL = objListener.getEditListener();
			if (objL != null) {
				MenuItem itemEdit = new MenuItem(objContextMenu, SWT.PUSH);
				itemEdit.addListener(SWT.Selection, objListener.getEditListener());
				setMenuItemText(itemEdit, "Edit", "E", 'E', true);
				itemEdit.setImage(getImage("copy_edit.gif"));
			}
		}

		{
			Listener objL = objListener.getCopyListener();
			if (objL != null) {
				MenuItem itemCopy = new MenuItem(objContextMenu, SWT.PUSH);
				itemCopy.addListener(SWT.Selection, objListener.getCopyListener());
				setMenuItemText(itemCopy, "Copy", "C", 'C', true);
				itemCopy.setImage(getImage("copy_edit.gif"));
			}
		}

		{
			Listener objL = objListener.getCutListener();
			if (objL != null) {
				MenuItem itemCut = new MenuItem(objContextMenu, SWT.PUSH);
				itemCut.addListener(SWT.Selection, objListener.getCutListener());
				setMenuItemText(itemCut, "Cut", "X", 'X', true);
				itemCut.setImage(getImage("cut_edit.gif"));
			}
		}

		{
			Listener objL = objListener.getPasteListener();
			if (objL != null) {
				MenuItem itemPaste = new MenuItem(objContextMenu, SWT.PUSH);
				itemPaste.addListener(SWT.Selection, objListener.getPasteListener());
				setMenuItemText(itemPaste, "Paste", "V", 'V', true);
				itemPaste.setImage(getImage("delete_obj.gif"));
			}
		}

		{
			Listener objL = objListener.getInsertListener();
			if (objL != null) {
				MenuItem itemInsert = new MenuItem(objContextMenu, SWT.PUSH);
				itemInsert.addListener(SWT.Selection, objListener.getInsertListener());
				setMenuItemText(itemInsert, "Insert", "INS", SWT.INSERT, true);
				itemInsert.setImage(getImage("insert.gif"));
			}
		}

		{
			Listener objL = objListener.getDeleteListener();
			if (objL != null) {
				MenuItem itemDelete = new MenuItem(objContextMenu, SWT.PUSH);
				itemDelete.addListener(SWT.Selection, objL);
				setMenuItemText(itemDelete, "Delete", "DEL", SWT.DEL, true);
				itemDelete.setImage(getImage("delete.gif"));
			}
		}

		{
			Listener objL = getSelectAllListener();
			if (objL != null) {
				MenuItem itemSelectAll = new MenuItem(objContextMenu, SWT.PUSH);
				itemSelectAll.addListener(SWT.Selection, objL);
				setMenuItemText(itemSelectAll, "Select All", "A", 'A', true);
				itemSelectAll.setImage(getImage("selected_mode.gif"));
			}
		}

		// TODO export to excel (POI/HSSF)
		new MenuItem(objContextMenu, SWT.SEPARATOR);

		//         MenuItem itemSelectFont = new MenuItem(objContextMenu, SWT.PUSH);
		//         itemSelectFont.addListener(SWT.Selection, getSelectFontListener());
		//         itemSelectFont.setText("Select Font");

		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				//				addParams();
			}
		});

		super.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					objListener.getDeleteListener();
				}

				if (e.keyCode == 'N' && e.stateMask == SWT.MOD1) {
					objListener.getNewListener();
				}

				if (e.keyCode == SWT.ESC) {
					boolean flgSetVisible = true;
					if (objContextMenu.isVisible()) {
						flgSetVisible = false;
					}
					objContextMenu.setVisible(flgSetVisible);
				}

				if (e.keyCode == 'A' && e.stateMask == SWT.MOD1) {
					objContextMenu.setVisible(true);
				}

			}
		});
		super.setLayoutDeferred(false);
	}

	private String getMenuText(final String pstrText, final String pstrAccelerator) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getMenuText";

		String strRet = pstrText;
		int intLen = pstrAccelerator.trim().length();
		if (intLen > 0) {
			if (intLen == 1) {
				strRet += "\tCtrl+" + pstrAccelerator;
			}
			else {
				strRet += "\t" + pstrAccelerator;
			}
		}
		return strRet;
	} // private String getMenuText

	public void addMenueSeparator() {
		new MenuItem(objContextMenu, SWT.SEPARATOR);
	}

	@Override
	public Listener getCopyListener() {

		return getDummyListener();
	}

	@Override
	public Listener getPasteListener() {

		return getDummyListener();
	}

	@Override
	public Listener getCutListener() {

		return getDummyListener();
	}

	private Listener getSelectAllListener() {
		return getDummyListener();
	}

	public Listener getDummyListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				logger.debug("Dummy Listener invoked ....");
			}
		};
	}

	//	public void customizeTable () {
	//		    layout.addColumnData(new ColumnWeightData(50, 75, true));
	//		    layout.addColumnData(new ColumnWeightData(50, 75, true));
	//		    this.setLayout(layout);
	//	}
	public TableColumn newTableColumn(final String pstrColumnName, final int pintDefaultSize) {
		final TableColumn colR = new TableColumn(this, SWT.NONE);
		colR.setData("caption", pstrColumnName);
		colR.setMoveable(true);
		objFormPosSizeHandler.restoreTableColumn(strTableName, colR, pintDefaultSize);

		colR.addListener(SWT.Move, ColumnMoveListener);

		colR.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				objFormPosSizeHandler.saveTableColumn(strTableName, colR);
				objFormPosSizeHandler.TableColumnOrderSave(objTable);
			}
		});

		//	    objTableLayout.setColumnData(colR, new ColumnWeightData(20, colR.getWidth(), true));
		//	    objParent.setLayout(objTableLayout);
		return colR;
	}

	public void Restore() {
		objFormPosSizeHandler.TableColumnOrderRestore(objTable);
	}

	Listener	ColumnMoveListener	= new Listener() {
										@Override
										public void handleEvent(final Event e) {
											System.out.println("Move " + e.widget);
											objFormPosSizeHandler.TableColumnOrderSave(objTable);
										}
									};

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public Listener getNewListener() {
		return getDummyListener();
	}

	@Override
	public Listener getDeleteListener() {
		return getDummyListener();
	}

	@Override
	public Listener getInsertListener() {
		return getDummyListener();
	}

	public void setMenuItemText(final MenuItem objItem, final String strText, final String strAccelaratorKey, final int intAcc, final boolean flgWithCtrl) {

		objItem.setText(getMenuText(strText, strAccelaratorKey));
		if (flgWithCtrl) {
			objItem.setAccelerator(SWT.MOD1 + intAcc);
		}
		else {
			objItem.setAccelerator(intAcc);
		}
	}

	// TODO find a general BaseClass for this method
	protected Image getImage(final String pstrImageFileName) {
		Image objI = ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/" + pstrImageFileName);

		return objI;
	}

	@Override
	public Listener getEditListener() {
		return getDummyListener();
	}

}
