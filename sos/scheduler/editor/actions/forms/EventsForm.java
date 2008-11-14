package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
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
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.EventsListener;
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import java.util.ArrayList;


public class EventsForm extends Composite implements IUpdateLanguage {
    
	
	private EventsListener     listener                 = null;

    private Group              actionsGroup             = null;
    
    private Text               txtLogic                 = null; 
   
    private Text               txtGroup                 = null;
    
    private Text               txtGroupLogic            = null;
    
    private Combo              txtEventClass            = null;
    
    private Table              table                    = null;
    
    private Button             butApply                 = null;
    
    private Button             butNew                   = null;
    
    private Button             butRemove                = null; 
    
    private ActionsDom         _dom                     = null;
    
    private Button             butEventGroupOperation   = null;
    
    
    public EventsForm(Composite parent, int style, ActionsDom dom, Element action, ActionsForm _gui) {
    	
        super(parent, style);           
        //gui = _gui;
        listener = new EventsListener(dom, action, _gui);
        _dom = dom;
        initialize();
        setToolTipText();
       
    }


    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
        txtLogic.setText(listener.getLogic());
        listener.fillEvents(table);
        txtEventClass.setItems(listener.getEventClasses());

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setText("Action"); // Generated
        actionsGroup.setLayout(gridLayout); // Generated

        final Label lblLogic = new Label(actionsGroup, SWT.NONE);
        lblLogic.setText("Logic:");

        txtLogic = new Text(actionsGroup, SWT.BORDER);
        txtLogic.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setLogic(txtLogic.getText());
        	}
        });
        txtLogic.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button butEventsOperation = new Button(actionsGroup, SWT.NONE);
        butEventsOperation.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		ArrayList list = new ArrayList();
        		list.add("or ");
        		list.add("and ");
        		list.add("( <key1> and <key2> ) ");
        		list.addAll(listener.getGroups());
        		
        		LogicOperationDialog logicOperationDialog = new LogicOperationDialog(SWT.NONE);
        		logicOperationDialog.open(txtLogic, list);
        	}
        });
        butEventsOperation.setText("Operation");

        final Label label = new Label(actionsGroup, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        //gridData_1.heightHint = 18;
        label.setLayoutData(gridData_1);
        label.setText("label");
        new Label(actionsGroup, SWT.NONE);

        final Group group = new Group(actionsGroup, SWT.NONE);
        group.setText("Events Group");
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
        group.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 4;
        group.setLayout(gridLayout_1);

        final Label groupLabel = new Label(group, SWT.NONE);
        groupLabel.setText("Group: ");

        txtGroup = new Text(group, SWT.BORDER);
        txtGroup.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        txtGroup.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(txtGroup.getText().length() > 0);
        		butEventGroupOperation.setEnabled(txtGroup.getText().length() > 0);
                
        	}
        });
        txtGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

        butApply = new Button(group, SWT.NONE);
        butApply.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		apply();
        	}
        });
        butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butApply.setText("Apply");

        final Label logicLabel = new Label(group, SWT.NONE);
        logicLabel.setText("Logic: ");

        txtGroupLogic = new Text(group, SWT.BORDER);
        txtGroupLogic.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        txtGroupLogic.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        butEventGroupOperation = new Button(group, SWT.NONE);
        butEventGroupOperation.setEnabled(false);
        butEventGroupOperation.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		ArrayList list = new ArrayList();
        		list.add("or ");
        		list.add("and ");
        		list.add("( <key1> and <key2> ) ");
        		list.addAll(listener.getEventClassAndId(txtGroup.getText()));
        		
        		LogicOperationDialog logicOperationDialog = new LogicOperationDialog(SWT.NONE);
        		logicOperationDialog.open(txtGroupLogic, list);
        	}
        });
        butEventGroupOperation.setText("Operation");

        butNew = new Button(group, SWT.NONE);
        butNew.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		    	
                txtGroup.setText("");
                txtGroupLogic.setText("");
                txtEventClass.setText("");
                table.deselectAll();                
                butNew.setToolTipText(Messages.getTooltip("events.button_new"));
                butApply.setEnabled(false);
                butRemove.setEnabled(false);
                txtGroup.setFocus();
        	}
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butNew.setText("New");

        final Label eventClassLabel = new Label(group, SWT.NONE);
        eventClassLabel.setText("Event Class");

        txtEventClass = new Combo(group, SWT.BORDER);
        txtEventClass.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        txtEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

        butRemove = new Button(group, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(table.getSelectionCount() > 0)  {
        			listener.removeEvent(table);
        			txtGroup.setFocus();
        		}
        	}
        });
        butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butRemove.setText("Remove");

        table = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		ContextMenu.goTo(table.getSelection()[0].getText(0), _dom, Editor.EVENTS);
        	}
        });
        table.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(table.getSelectionCount() > 0) {
        			TableItem item = table.getSelection()[0];        			
        			txtGroup.setText(item.getText(0));
        			txtGroupLogic.setText(item.getText(1));
        			txtEventClass.setText(item.getText(2));        			
        		}
                butRemove.setEnabled(table.getSelectionCount() > 0);
        	}
        });
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.setWidth(140);
        newColumnTableColumn.setText("Group");

        final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.setWidth(136);
        newColumnTableColumn_1.setText("Logic");

        final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_2.setWidth(189);
        newColumnTableColumn_2.setText("Event Class");
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
    }
 

    public void setToolTipText() {
    	txtLogic.setToolTipText(Messages.getTooltip("events.logic"));    	
        txtGroup.setToolTipText(Messages.getTooltip("events.group"));
        txtGroupLogic.setToolTipText(Messages.getTooltip("events.group_logic"));
        txtEventClass.setToolTipText(Messages.getTooltip("events.event_class"));
        table.setToolTipText(Messages.getTooltip("events.table"));
        butApply.setToolTipText(Messages.getTooltip("events.button_apply"));
        butNew.setToolTipText(Messages.getTooltip("events.button_new"));        
        butRemove.setToolTipText(Messages.getTooltip("events.button_remove"));
    }
    
    private void apply() {
    	if(txtGroup.getText().length() > 0)
    		listener.apply(txtGroup.getText(), txtEventClass.getText(), txtGroupLogic.getText(), table);
    	
    	txtEventClass.setText("");    	
        txtGroup.setText("");
        txtGroupLogic.setText("");
        
        txtEventClass.setItems(listener.getEventClasses());
        
    	txtGroup.setFocus();
    	
    }

} // @jve:decl-index=0:visual-constraint="10,10"

