package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.EventListener;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;


public class EventForm extends Composite implements IUpdateLanguage {
    
	
	private EventListener     listener                  = null;

    private Group              actionsGroup             = null;
       
    private Text               txtEventId               = null;
    
    private Text               txtTitle                 = null;
    
    private Text               txtEventClass            = null;
    
    private Table              table                    = null;
    
    private Button             butApply                 = null;
    
    private Button             butNew                   = null;
    
    private Button             butRemove                = null; 
    
    private Text               txtJobChaon              = null;
    
    private Text               txtOrderId               = null;
    
    private Text               txtJobname               = null;
    
    private Text               txtComment               = null;

    private int                type                     = -1;
    
    public EventForm(Composite parent, int style, ActionsDom dom, Element eventGroup, ActionsForm _gui, int type_) {
        super(parent, style);           
        //gui = _gui;
        type = type_;
        listener = new EventListener(dom, eventGroup, _gui, type_);
        initialize();
        setToolTipText();       
    }


    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new GridLayout());
        //if(table != null)
        	listener.fillEvent(table);

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        /*GridLayout gridLayout = new GridLayout();
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setText("Event"); // Generated
        actionsGroup.setLayout(gridLayout); // Generated
*/
        final Group group = new Group(this, SWT.NONE);
        group.setText("Events Group");
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.heightHint = 303;
        group.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 3;
        group.setLayout(gridLayout_1);

        final Label groupLabel = new Label(group, SWT.NONE);
        groupLabel.setText("Event Id:");

        txtEventId = new Text(group, SWT.BORDER);
        txtEventId.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtEventId.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
                
        	}
        });
        txtEventId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        butApply = new Button(group, SWT.NONE);
        butApply.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		apply();
        	}
        });
        butApply.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butApply.setText("Apply");

        final Label logicLabel = new Label(group, SWT.NONE);
        logicLabel.setText("Event Title: ");
        logicLabel.setVisible(type != Editor.REMOVE_EVENT_GROUP);
        txtTitle = new Text(group, SWT.BORDER);
        txtTitle.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtTitle.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
txtTitle.setVisible(type != Editor.REMOVE_EVENT_GROUP);
        
        butNew = new Button(group, SWT.NONE);
        butNew.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		    	
                refresh();
        	}
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butNew.setText("New");

        final Label eventClassLabel = new Label(group, SWT.NONE);
        eventClassLabel.setText("Event Class:");

        txtEventClass = new Text(group, SWT.BORDER);
        txtEventClass.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtEventClass.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        new Label(group, SWT.NONE);

        final Label jobNameLabel = new Label(group, SWT.NONE);
        jobNameLabel.setText("Job Name:");

        txtJobname = new Text(group, SWT.BORDER);
        txtJobname.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtJobname.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtJobname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(group, SWT.NONE);

        final Label jobChainLabel = new Label(group, SWT.NONE);
        jobChainLabel.setText("Job Chain: ");

        txtJobChaon = new Text(group, SWT.BORDER);
        txtJobChaon.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtJobChaon.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtJobChaon.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(group, SWT.NONE);

        final Label orderIdLabel = new Label(group, SWT.NONE);
        orderIdLabel.setText("Order Id: ");

        txtOrderId = new Text(group, SWT.BORDER);
        txtOrderId.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtOrderId.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtOrderId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(group, SWT.NONE);

        final Label commandLabel = new Label(group, SWT.NONE);
        commandLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        commandLabel.setText("Comment:");
        commandLabel.setVisible(type != Editor.REMOVE_EVENT_GROUP);
        
        txtComment = new Text(group, SWT.MULTI | SWT.BORDER | SWT.WRAP);
        txtComment.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtEventId.getText().length() > 0);
        	}
        });
        txtComment.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEventId.equals(""))
        			apply();
        	}
        });
        txtComment.setVisible(type != Editor.REMOVE_EVENT_GROUP);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridData_1.heightHint = 38;
        txtComment.setLayoutData(gridData_1);
        new Label(group, SWT.NONE);

        //if(type != Editor.REMOVE_EVENT_GROUP || type != Editor.ADD_EVENT_GROUP) {
        	table = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
        	table.addSelectionListener(new SelectionAdapter() {
        		public void widgetSelected(final SelectionEvent e) {
        			if(table.getSelectionCount() > 0) {
        				TableItem item = table.getSelection()[0];        			
        				txtEventId.setText(item.getText(0));
        				txtTitle.setText(item.getText(1));
        				txtEventClass.setText(item.getText(2));        			
        				txtJobname.setText(item.getText(3));
        				txtJobChaon.setText(item.getText(4));
        				txtOrderId.setText(item.getText(5));
        				txtComment.setText(item.getText(6));
        			}
        			butRemove.setEnabled(table.getSelectionCount() > 0);
        		}
        	});
        	table.setLinesVisible(true);
        	table.setHeaderVisible(true);
        	table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

        	final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn.setWidth(83);
        	newColumnTableColumn.setText("Event Id");

        	final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_1.setWidth(67);
        	newColumnTableColumn_1.setText("Event Title");

        	final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_2.setWidth(77);
        	newColumnTableColumn_2.setText("Event Class");

        	final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_3.setWidth(58);
        	newColumnTableColumn_3.setText("Jobname");

        	final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_4.setWidth(45);
        	newColumnTableColumn_4.setText("JobChain");

        	final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_5.setWidth(100);
        	newColumnTableColumn_5.setText("Order Id");

        	final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
        	newColumnTableColumn_6.setWidth(100);
        	newColumnTableColumn_6.setText("Comment");
        //}
        butRemove = new Button(group, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(table != null && table.getSelectionCount() > 0)  {
        			listener.removeEvent(table);
        			 refresh();
        		}
        	}
        });
        butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butRemove.setText("Remove");
    }
 

    public void setToolTipText() {
        txtEventId.setToolTipText(Messages.getTooltip("event.event_id"));
        txtTitle.setToolTipText(Messages.getTooltip("event.title"));
        txtEventClass.setToolTipText(Messages.getTooltip("event.event_class"));
        //if(table != null)
        	table.setToolTipText(Messages.getTooltip("event.table"));
        butApply.setToolTipText(Messages.getTooltip("event.button_apply"));
        butNew.setToolTipText(Messages.getTooltip("event.button_new"));        
        butRemove.setToolTipText(Messages.getTooltip("event.button_remove"));                
        txtJobChaon.setToolTipText(Messages.getTooltip("event.job_chain"));        
        txtOrderId.setToolTipText(Messages.getTooltip("event.order_id"));        
        txtJobname.setToolTipText(Messages.getTooltip("event.jobname"));   
        txtComment.setToolTipText(Messages.getTooltip("event.comment"));
    }
    
    private void apply() {
    	if(txtEventId.getText().length() > 0)
    		listener.apply(txtEventId.getText(), txtEventClass.getText(), txtTitle.getText(), 
    				       txtJobname.getText(),txtJobChaon.getText(), txtOrderId.getText(), txtComment.getText(), 
    				       table);
    	refresh();
    }

    private void refresh() {
    	txtEventId.setText("");
        txtTitle.setText("");
        txtEventClass.setText("");
        txtJobname.setText("");
        txtJobChaon.setText("");
        txtOrderId.setText("");
        txtComment.setText("");
        table.deselectAll();                                
        butApply.setEnabled(false);
        butRemove.setEnabled(false);
         
        txtEventId.setFocus();
    }
} // @jve:decl-index=0:visual-constraint="10,10"

