package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import sos.scheduler.editor.app.IUnsaved;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.EventListener;
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import java.util.ArrayList;


public class EventForm extends Composite implements IUnsaved, IUpdateLanguage  {


	private EventListener     listener                  = null;

	private Group              group                    = null;

	private Text               txtEventName             = null; 

	private int                type                     = -1;

	private Table              table                    = null;

	private Button             butApply                 = null;

	private Button             butNew                   = null;

	private Button             butRemove                = null;

	private Combo              cboEventClass            = null;

	private Text               txtEventId               = null;

	private Text               txtJobChain              = null;

	private Text               txtJobname               = null;

	private Text               txtOrderId               = null;

	private Text               txtTitle                 = null;

	private Text               txtComment               = null;


	public EventForm(Composite parent, int style, ActionsDom dom, Element eventGroup, int type_) {

		super(parent, style);           

		type = type_;
		listener = new EventListener(dom, eventGroup, type_);
		initialize();
		setToolTipText();


	}

	private void initialize() {

		createGroup();
		setSize(new Point(696, 462));
		setLayout(new FillLayout());

		listener.fillEvent(table);
		if(type == Editor.EVENT_GROUP)
			group.setText(" Action: " + listener.getActionName() + "  Group: " + listener.getEventGroupName() );
		else if(type == Editor.REMOVE_EVENT_GROUP)
			group.setText(" Action: " + listener.getActionName() + " Remove Event " );
		else
			group.setText(" Action: " + listener.getActionName() + " Add Event " );

		cboEventClass.setItems(listener.getEventClasses());
		butApply.setEnabled(false);
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		group = new Group(this, SWT.NONE);
		group.setText("Action:  Group:"); 
		group.setLayout(gridLayout);


		final Label lblLogic = new Label(group, SWT.NONE);
		lblLogic.setLayoutData(new GridData());
		lblLogic.setText("Event Name");

		txtEventName = new Text(group, SWT.BORDER);
		txtEventName.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtEventName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtEventName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		butApply = new Button(group, SWT.NONE);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				apply();
			}
		});
		butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butApply.setText("Apply");

		final Label eventTitleLabel = new Label(group, SWT.NONE);
		eventTitleLabel.setLayoutData(new GridData());
		eventTitleLabel.setText("Event Title");

		txtTitle = new Text(group, SWT.BORDER);
		txtTitle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtTitle.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		butNew = new Button(group, SWT.NONE);
		butNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				refresh();
			}
		});
		butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNew.setText("New");

		final Group matchingAttributesGroup = new Group(group, SWT.NONE);
		matchingAttributesGroup.setText("Matching Attributes");
		matchingAttributesGroup.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 3, 1));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginTop = 5;
		gridLayout_1.numColumns = 2;
		matchingAttributesGroup.setLayout(gridLayout_1);

		final Label txtEventClass = new Label(matchingAttributesGroup, SWT.NONE);
		txtEventClass.setText("Event Class");

		cboEventClass = new Combo(matchingAttributesGroup, SWT.NONE);
		cboEventClass.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		cboEventClass.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		cboEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label labeld = new Label(matchingAttributesGroup, SWT.NONE);
		labeld.setText("Event Id");

		txtEventId = new Text(matchingAttributesGroup, SWT.BORDER);
		txtEventId.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtEventId.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtEventId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label jobNameLabel = new Label(matchingAttributesGroup, SWT.NONE);
		jobNameLabel.setText("Job Name");

		txtJobname = new Text(matchingAttributesGroup, SWT.BORDER);
		txtJobname.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtJobname.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtJobname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label jobChainLabel = new Label(matchingAttributesGroup, SWT.NONE);
		jobChainLabel.setText("Job Chain");

		txtJobChain = new Text(matchingAttributesGroup, SWT.BORDER);
		txtJobChain.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtJobChain.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtJobChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label lblOrderId = new Label(matchingAttributesGroup, SWT.NONE);
		lblOrderId.setText("Order Id");

		txtOrderId = new Text(matchingAttributesGroup, SWT.BORDER);
		txtOrderId.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtOrderId.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtOrderId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label commentLabel = new Label(group, SWT.NONE);
		commentLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		commentLabel.setText("Comment");

		txtComment = new Text(group, SWT.MULTI | SWT.BORDER);
		txtComment.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1);
		gridData.heightHint = 45;
		txtComment.setLayoutData(gridData);

		table = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(table.getSelectionCount() > 0) {
					TableItem item = table.getSelection()[0];
					txtEventName.setText(item.getText(0));
					txtEventId.setText(item.getText(1));
					txtTitle.setText(item.getText(2));
					cboEventClass.setText(item.getText(3));        			
					txtJobname.setText(item.getText(4));
					txtJobChain.setText(item.getText(5));
					txtOrderId.setText(item.getText(6));
					txtComment.setText(item.getText(7));
				}
				butApply.setEnabled(false);
				butRemove.setEnabled(table.getSelectionCount() > 0);
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(70);
		newColumnTableColumn.setText("Event Name");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(70);
		newColumnTableColumn_1.setText("Event Id");

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(70);
		newColumnTableColumn_2.setText("Event Title");

		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(73);
		newColumnTableColumn_3.setText("Event Class");

		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(70);
		newColumnTableColumn_4.setText("Jobname");

		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(70);
		newColumnTableColumn_5.setText("Jobchain");

		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(70);
		newColumnTableColumn_6.setText("Order Id");

		final TableColumn newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_7.setWidth(70);
		newColumnTableColumn_7.setText("Comment");

		butRemove = new Button(group, SWT.NONE);
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(table != null && table.getSelectionCount() > 0)  {
					int cont = 0;
					if(type == Editor.EVENT_GROUP)
						cont = MainWindow.message(getShell(), "Do you really want to delete this group?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					else {
						cont = MainWindow.message(getShell(), "Do you really want to delete this command?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					}
					if(cont == SWT.OK) {				        				
						listener.removeEvent(table);
					} 

					refresh();
				}
			}
		});
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
		gridData_1.widthHint = 47;
		butRemove.setLayoutData(gridData_1);
		butRemove.setText("Remove");
		//gridData_1.heightHint = 18;
	}




	public boolean isUnsaved() {	
		return butApply.isEnabled();		
	}

	public void apply() {		
		try {
			if (butApply.isEnabled()) {
				listener.apply(txtEventName.getText(), txtEventId.getText(), cboEventClass.getText(), txtTitle.getText(), 
						txtJobname.getText(),txtJobChain.getText(), txtOrderId.getText(), txtComment.getText(), 
						table);
				cboEventClass.setItems(listener.getEventClasses());
				refresh();
			}
		} catch(Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; error while save Event, cause: ", e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("error while save Event, cause: "  + e.getMessage(), SWT.ICON_WARNING);

		}

	}

	private void refresh() {
		txtEventName.setText("");
		txtEventId.setText("");
		txtTitle.setText("");
		cboEventClass.setText("");
		txtJobname.setText("");
		txtJobChain.setText("");
		txtOrderId.setText("");
		txtComment.setText("");
		table.deselectAll();                                
		butApply.setEnabled(false);
		butRemove.setEnabled(false);

		txtEventName.setFocus();
	}


	public void setToolTipText() {
		txtEventName.setToolTipText(Messages.getTooltip("event.name"));		
		butNew.setToolTipText(Messages.getTooltip("event.but_new"));
		txtTitle.setToolTipText(Messages.getTooltip("event.title"));		 
		butApply.setToolTipText(Messages.getTooltip("event.but_apply"));
		cboEventClass.setToolTipText(Messages.getTooltip("event.event_class"));
		txtEventId.setToolTipText(Messages.getTooltip("event.event_id")); 
		txtJobname.setToolTipText(Messages.getTooltip("event.job_name"));
		txtJobChain.setToolTipText(Messages.getTooltip("event.job_chain"));
		txtOrderId.setToolTipText(Messages.getTooltip("event.order_id"));
		txtComment.setToolTipText(Messages.getTooltip("event.comment"));
		table.setToolTipText(Messages.getTooltip("event.table"));
		butRemove.setToolTipText(Messages.getTooltip("event.but_remove"));


	}

} // @jve:decl-index=0:visual-constraint="10,10"

