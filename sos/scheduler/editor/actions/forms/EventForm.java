package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import sos.scheduler.editor.app.IUnsaved;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.EventListener;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;


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

	private Text               txtExitCode              = null; 

	private Text               txtHourExpirationPeriod  = null; 
	private Text               txtMinExpirationPeriod   = null;
	private Text               txtSecExpirationPeriod   = null;

	private Text               txtHourExpirationCycle   = null;
	private Text               txtMinExpirationCycle    = null;
	private Text               txtSecExpirationCycle    = null;
	
	private Group              matchingAttributesGroup  = null;

	public EventForm(Composite parent, int style, ActionsDom dom, Element eventGroup, int type_) {

		super(parent, style);           

		type = type_;
		listener = new EventListener(dom, eventGroup, type_);
		initialize();
		setToolTipText();
		txtEventName.setFocus();


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

		group.setTabList(new org.eclipse.swt.widgets.Control[] {
				txtEventName, butApply, txtTitle, butNew  , matchingAttributesGroup, txtComment
		});

		matchingAttributesGroup.setTabList(new org.eclipse.swt.widgets.Control[] {
				cboEventClass, txtEventId, txtJobname, txtJobChain, txtOrderId, txtExitCode  
		});

		
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
		txtEventName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtEventName.selectAll();
			}
		});
		
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
		
		txtTitle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtTitle.selectAll();
			}
		});
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

		matchingAttributesGroup = new Group(group, SWT.NONE);
		matchingAttributesGroup.setText("Matching Attributes");
		matchingAttributesGroup.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 3, 1));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginTop = 5;
		gridLayout_1.numColumns = 4;
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
		cboEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		final Label labeld = new Label(matchingAttributesGroup, SWT.NONE);
		labeld.setText("Event Id");

		txtEventId = new Text(matchingAttributesGroup, SWT.BORDER);
		txtEventId.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtEventId.selectAll();		
			}
		});
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
		txtEventId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		final Label jobNameLabel = new Label(matchingAttributesGroup, SWT.NONE);
		jobNameLabel.setText("Job Name");

		txtJobname = new Text(matchingAttributesGroup, SWT.BORDER);
		txtJobname.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtJobname.selectAll();
			}
		});
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
		txtJobname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		final Label jobChainLabel = new Label(matchingAttributesGroup, SWT.NONE);
		jobChainLabel.setText("Job Chain");

		txtJobChain = new Text(matchingAttributesGroup, SWT.BORDER);
		txtJobChain.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtJobChain.selectAll();
			}
		});
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
		txtJobChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		final Label lblOrderId = new Label(matchingAttributesGroup, SWT.NONE);
		lblOrderId.setText("Order Id");

		txtOrderId = new Text(matchingAttributesGroup, SWT.BORDER);
		txtOrderId.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtOrderId.selectAll();		
			}
		});
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
		txtOrderId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		final Label exitCodeLabel = new Label(matchingAttributesGroup, SWT.NONE);
		exitCodeLabel.setLayoutData(new GridData());
		exitCodeLabel.setText("Exit Code");

		txtExitCode = new Text(matchingAttributesGroup, SWT.BORDER);
		txtExitCode.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtExitCode.selectAll();
			}
		});
		txtExitCode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApply.setEnabled(true);
			}
		});
		txtExitCode.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR )
					apply();
			}
		});
		txtExitCode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

		if(type == Editor.ADD_EVENT_GROUP)
			createExpirationTime(matchingAttributesGroup);
		/*final Label expirationPeriodLabel = new Label(matchingAttributesGroup, SWT.NONE);
		expirationPeriodLabel.setText("Expiration Period");

		final Composite composite = new Composite(matchingAttributesGroup, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.verticalSpacing = 0;
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gridLayout_2.horizontalSpacing = 0;
		gridLayout_2.numColumns = 6;
		composite.setLayout(gridLayout_2);

		txtHourExpirationPeriod = new Text(composite, SWT.BORDER);
		txtHourExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtHourExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtHourExpirationPeriod.setTextLimit(2);

		final Label label = new Label(composite, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		final GridData gridData_2 = new GridData(10, SWT.DEFAULT);
		label.setLayoutData(gridData_2);
		label.setText(":");

		txtMinExpirationPeriod = new Text(composite, SWT.BORDER);
		txtMinExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtMinExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtMinExpirationPeriod.setTextLimit(2);

		final Label label_1 = new Label(composite, SWT.NONE);
		final GridData gridData_2_1 = new GridData(10, SWT.DEFAULT);
		label_1.setLayoutData(gridData_2_1);
		label_1.setAlignment(SWT.CENTER);
		label_1.setText(":");

		txtSecExpirationPeriod = new Text(composite, SWT.BORDER);
		txtSecExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtSecExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtSecExpirationPeriod.setTextLimit(2);

		final Label hhmmssLabel = new Label(composite, SWT.NONE);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_3.horizontalIndent = 5;
		hhmmssLabel.setLayoutData(gridData_3);
		hhmmssLabel.setText("HH:MM:SS");


		final Label expirationCycleLabel = new Label(matchingAttributesGroup, SWT.NONE);
		expirationCycleLabel.setLayoutData(new GridData(96, SWT.DEFAULT));
		expirationCycleLabel.setText("Expiration Cycle");

		final Composite composite_1 = new Composite(matchingAttributesGroup, SWT.NONE);
		composite_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.verticalSpacing = 0;
		gridLayout_3.numColumns = 6;
		gridLayout_3.marginWidth = 0;
		gridLayout_3.marginHeight = 0;
		gridLayout_3.horizontalSpacing = 0;
		composite_1.setLayout(gridLayout_3);

		txtHourExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtHourExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtHourExpirationCycle.setTextLimit(2);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_4.widthHint = 30;
		txtHourExpirationCycle.setLayoutData(gridData_4);

		final Label label_2 = new Label(composite_1, SWT.NONE);
		final GridData gridData_2_2 = new GridData(10, SWT.DEFAULT);
		label_2.setLayoutData(gridData_2_2);
		label_2.setAlignment(SWT.CENTER);
		label_2.setText(":");

		txtMinExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtMinExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtMinExpirationCycle.setTextLimit(2);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_5.widthHint = 30;
		txtMinExpirationCycle.setLayoutData(gridData_5);

		final Label label_1_1 = new Label(composite_1, SWT.NONE);
		final GridData gridData_2_1_1 = new GridData(10, SWT.DEFAULT);
		label_1_1.setLayoutData(gridData_2_1_1);
		label_1_1.setAlignment(SWT.CENTER);
		label_1_1.setText(":");

		txtSecExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtSecExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtSecExpirationCycle.setTextLimit(2);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_6.widthHint = 30;
		txtSecExpirationCycle.setLayoutData(gridData_6);

		final Label hhmmssLabel_1 = new Label(composite_1, SWT.NONE);
		final GridData gridData_3_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_3_1.horizontalIndent = 5;
		hhmmssLabel_1.setLayoutData(gridData_3_1);
		hhmmssLabel_1.setText("HH:MM:SS");
		 */
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
					txtExitCode.setText(item.getText(8));
					if(type==Editor.ADD_EVENT_GROUP) {
						int hour = Utils.getHours(item.getText(9), 0);
						int min = Utils.getMinutes(item.getText(9), 0);
						int sec = Utils.getSeconds(item.getText(9), 0);
						
						if((hour+min+sec) > 0) {
							txtHourExpirationPeriod.setText(String.valueOf(Utils.getHours(item.getText(9), 0)));
							txtMinExpirationPeriod.setText(String.valueOf(Utils.getMinutes(item.getText(9), 0)));
							txtSecExpirationPeriod.setText(String.valueOf(Utils.getSeconds(item.getText(9), 0)));
						}
						
						hour = Utils.getHours(item.getText(10), 0);
						min = Utils.getMinutes(item.getText(10), 0);
						sec = Utils.getSeconds(item.getText(10), 0);
						
						if((hour+min+sec) > 0) {
							txtHourExpirationCycle.setText(String.valueOf(Utils.getHours(item.getText(10), 0)));
							txtMinExpirationCycle.setText(String.valueOf(Utils.getMinutes(item.getText(10), 0)));
							txtSecExpirationCycle.setText(String.valueOf(Utils.getSeconds(item.getText(10), 0)));
						}
					}
				}
				butApply.setEnabled(false);
				butRemove.setEnabled(table.getSelectionCount() > 0);
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.END, GridData.FILL, true, true, 2, 1));

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

		final TableColumn newColumnTableColumn_8 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_8.setWidth(50);
		newColumnTableColumn_8.setText("Exit Code");

		final TableColumn expiration_period = new TableColumn(table, SWT.NONE);
		expiration_period.setWidth(type==Editor.ADD_EVENT_GROUP ? 100 : 0);
		expiration_period.setText("Expiration Period");

		final TableColumn newColumnTableColumn_10 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_10.setWidth(type==Editor.ADD_EVENT_GROUP ? 100 : 0);
		newColumnTableColumn_10.setText("Expiration Cycle");
		

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
		gridData_1.widthHint = 55;
		butRemove.setLayoutData(gridData_1);
		butRemove.setText(" Remove ");
		//gridData_1.heightHint = 18;
	}




	public boolean isUnsaved() {	
		return butApply.isEnabled();		
	}

	public void apply() {		
		try {
			if (butApply.isEnabled()) {
				listener.apply(txtEventName.getText(), txtEventId.getText(), cboEventClass.getText(), txtTitle.getText(), 
						txtJobname.getText(),txtJobChain.getText(), txtOrderId.getText(), txtComment.getText(), txtExitCode.getText(),
						getExpirationPeriod(),
						getExpirationCycle(),
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
		txtExitCode.setText("");
		table.deselectAll();                                
		butApply.setEnabled(false);
		butRemove.setEnabled(false);

		if(type == Editor.ADD_EVENT_GROUP) {
			txtHourExpirationPeriod.setText(""); 
			txtMinExpirationPeriod.setText("");
			txtSecExpirationPeriod.setText("");

			txtHourExpirationCycle.setText("");
			txtMinExpirationCycle.setText("");
			txtSecExpirationCycle.setText("");
		}

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
		txtExitCode.setToolTipText(Messages.getTooltip("event.exit_code"));
		if (type == Editor.ADD_EVENT_GROUP) {
			txtHourExpirationPeriod.setToolTipText(Messages.getTooltip("event.expiration_period.hour"));
			txtMinExpirationPeriod.setToolTipText(Messages.getTooltip("event.expiration_period.minute"));
			txtSecExpirationPeriod.setToolTipText(Messages.getTooltip("event.expiration_period.secound"));

			txtHourExpirationCycle.setToolTipText(Messages.getTooltip("event.expiration_cycle.hour"));
			txtMinExpirationCycle.setToolTipText(Messages.getTooltip("event.expiration_cycle.minute"));
			txtSecExpirationCycle.setToolTipText(Messages.getTooltip("event.expiration_cycle.secound"));

		}
	}

	private void createExpirationTime(Group matchingAttributesGroup) {
		final Label expirationPeriodLabel = new Label(matchingAttributesGroup, SWT.NONE);
		expirationPeriodLabel.setText("Expiration Period");

		final Composite composite = new Composite(matchingAttributesGroup, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.verticalSpacing = 0;
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gridLayout_2.horizontalSpacing = 0;
		gridLayout_2.numColumns = 6;
		composite.setLayout(gridLayout_2);

		txtHourExpirationPeriod = new Text(composite, SWT.BORDER);
		txtHourExpirationPeriod.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtHourExpirationPeriod.selectAll();
			}
		});
		txtHourExpirationPeriod.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 23, txtHourExpirationPeriod);
			}
		});
		txtHourExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtHourExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtHourExpirationPeriod.setTextLimit(2);

		final Label label = new Label(composite, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		final GridData gridData_2 = new GridData(10, SWT.DEFAULT);
		label.setLayoutData(gridData_2);
		label.setText(":");

		txtMinExpirationPeriod = new Text(composite, SWT.BORDER);
		txtMinExpirationPeriod.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtMinExpirationPeriod.selectAll();
			}
		});
		txtMinExpirationPeriod.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 59, txtMinExpirationPeriod);
			}
		});
		txtMinExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtMinExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtMinExpirationPeriod.setTextLimit(2);

		final Label label_1 = new Label(composite, SWT.NONE);
		final GridData gridData_2_1 = new GridData(10, SWT.DEFAULT);
		label_1.setLayoutData(gridData_2_1);
		label_1.setAlignment(SWT.CENTER);
		label_1.setText(":");

		txtSecExpirationPeriod = new Text(composite, SWT.BORDER);
		txtSecExpirationPeriod.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtSecExpirationPeriod.selectAll();
			}
		});
		txtSecExpirationPeriod.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 59, txtSecExpirationPeriod);
			}
		});
		txtSecExpirationPeriod.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtSecExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
		txtSecExpirationPeriod.setTextLimit(2);

		final Label hhmmssLabel = new Label(composite, SWT.NONE);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_3.horizontalIndent = 5;
		hhmmssLabel.setLayoutData(gridData_3);
		hhmmssLabel.setText("HH:MM:SS");


		final Label expirationCycleLabel = new Label(matchingAttributesGroup, SWT.NONE);
		expirationCycleLabel.setLayoutData(new GridData(96, SWT.DEFAULT));
		expirationCycleLabel.setText("Expiration Cycle");

		final Composite composite_1 = new Composite(matchingAttributesGroup, SWT.NONE);
		composite_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.verticalSpacing = 0;
		gridLayout_3.numColumns = 6;
		gridLayout_3.marginWidth = 0;
		gridLayout_3.marginHeight = 0;
		gridLayout_3.horizontalSpacing = 0;
		composite_1.setLayout(gridLayout_3);

		txtHourExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtHourExpirationCycle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtHourExpirationCycle.selectAll();
			}
		});
		txtHourExpirationCycle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 23, txtHourExpirationCycle);
			}
		});
		txtHourExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtHourExpirationCycle.setTextLimit(2);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_4.widthHint = 30;
		txtHourExpirationCycle.setLayoutData(gridData_4);

		final Label label_2 = new Label(composite_1, SWT.NONE);
		final GridData gridData_2_2 = new GridData(10, SWT.DEFAULT);
		label_2.setLayoutData(gridData_2_2);
		label_2.setAlignment(SWT.CENTER);
		label_2.setText(":");

		txtMinExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtMinExpirationCycle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtMinExpirationCycle.selectAll();
			}
		});
		txtMinExpirationCycle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 59, txtMinExpirationCycle);
			}
		});
		txtMinExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtMinExpirationCycle.setTextLimit(2);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_5.widthHint = 30;
		txtMinExpirationCycle.setLayoutData(gridData_5);

		final Label label_1_1 = new Label(composite_1, SWT.NONE);
		final GridData gridData_2_1_1 = new GridData(10, SWT.DEFAULT);
		label_1_1.setLayoutData(gridData_2_1_1);
		label_1_1.setAlignment(SWT.CENTER);
		label_1_1.setText(":");

		txtSecExpirationCycle = new Text(composite_1, SWT.BORDER);
		txtSecExpirationCycle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtSecExpirationCycle.selectAll();
			}
		});
		txtSecExpirationCycle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 59, txtSecExpirationCycle);
			}
		});
		txtSecExpirationCycle.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		txtSecExpirationCycle.setTextLimit(2);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_6.widthHint = 30;
		txtSecExpirationCycle.setLayoutData(gridData_6);

		final Label hhmmssLabel_1 = new Label(composite_1, SWT.NONE);
		final GridData gridData_3_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_3_1.horizontalIndent = 5;
		hhmmssLabel_1.setLayoutData(gridData_3_1);
		hhmmssLabel_1.setText("HH:MM:SS");
	}
	
	private String getExpirationPeriod() {
		
		if(type != Editor.ADD_EVENT_GROUP)
			return "";
		
		return Utils.getTime(txtHourExpirationPeriod.getText(), txtMinExpirationPeriod.getText(), txtSecExpirationPeriod.getText(), false);
		
	}
	
   private String getExpirationCycle() {
		
		if(type != Editor.ADD_EVENT_GROUP)
			return "";
		
		return Utils.getTime(txtHourExpirationCycle.getText(), txtMinExpirationCycle.getText(), txtSecExpirationCycle.getText(), false);
		
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"

