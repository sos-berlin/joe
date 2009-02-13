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
import sos.scheduler.editor.actions.listeners.EventsListener;
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import java.util.ArrayList;


public class EventsForm extends Composite implements IUnsaved, IUpdateLanguage  {
    
	
	private EventsListener     listener                 = null;

    private Group              actionsGroup             = null;
    
    private Text               txtLogic                 = null; 
   
    private Text               txtGroup                 = null;
    
    private Text               txtGroupLogic            = null;
    
    private Combo              cboEventClass            = null;
    
    private Table              table                    = null;
    
    private Button             butApply                 = null;
    
    private Button             butNew                   = null;
    
    private Button             butRemove                = null; 
    
    private ActionsDom         _dom                     = null;
    
    private Button             butEventGroupOperation   = null;
    
    
    public EventsForm(Composite parent, int style, ActionsDom dom, Element action, ActionsForm _gui) {
    	
        super(parent, style);           
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
        cboEventClass.setItems(listener.getEventClasses());
        actionsGroup.setText("Action: " + listener.getActionname()); // Generated
        butApply.setEnabled(false);
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setText("Action"); 
        actionsGroup.setLayout(gridLayout); 

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
        txtGroup.setBackground(SWTResourceManager.getColor(255, 255, 217));
        
        txtGroup.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        txtGroup.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(true);
        		butEventGroupOperation.setEnabled(txtGroup.getText().length() > 0);
                
        	}
        });
        txtGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

        butApply = new Button(group, SWT.NONE);
        butApply.setEnabled(false);
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
        txtGroupLogic.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(true);
        	}
        });
        txtGroupLogic.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        txtGroupLogic.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        butEventGroupOperation = new Button(group, SWT.NONE);
        butEventGroupOperation.setLayoutData(new GridData());
        butEventGroupOperation.setEnabled(false);
        butEventGroupOperation.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		ArrayList list = new ArrayList();        		
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
                cboEventClass.setText("");
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

        cboEventClass = new Combo(group, SWT.BORDER);
        cboEventClass.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butApply.setEnabled(true);
        	}
        });
        cboEventClass.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtGroup.equals(""))
        			apply();
        	}
        });
        cboEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

        butRemove = new Button(group, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if(table.getSelectionCount() > 0)  {
        			int cont = MainWindow.message(actionsGroup.getShell(), "If you really want to delete this group?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
        			if(cont == SWT.OK) {				        				
        				listener.removeEvent(table);
        			} 
        			
        			txtGroup.setFocus();
        			cboEventClass.setText("");    	
        	        txtGroup.setText("");
        	        txtGroupLogic.setText("");
        	        
        	        cboEventClass.setItems(listener.getEventClasses());
        	        
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
        			cboEventClass.setText(item.getText(2));  
        			
        		}
        		butApply.setEnabled(false);
        		
                butRemove.setEnabled(table.getSelectionCount() > 0);
        	}
        });
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));

        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.setWidth(140);
        newColumnTableColumn.setText("Group");

        final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.setWidth(136);
        newColumnTableColumn_1.setText("Logic");

        final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_2.setWidth(189);
        newColumnTableColumn_2.setText("Event Class");
    }
 

    public void setToolTipText() {
    	txtLogic.setToolTipText(Messages.getTooltip("events.logic"));    	
        txtGroup.setToolTipText(Messages.getTooltip("events.group"));
        txtGroupLogic.setToolTipText(Messages.getTooltip("events.group_logic"));
        cboEventClass.setToolTipText(Messages.getTooltip("events.event_class"));
        table.setToolTipText(Messages.getTooltip("events.table"));
        butApply.setToolTipText(Messages.getTooltip("events.button_apply"));
        butNew.setToolTipText(Messages.getTooltip("events.button_new"));        
        butRemove.setToolTipText(Messages.getTooltip("events.button_remove"));
        butRemove.setToolTipText(Messages.getTooltip("events.button_operation"));
    }
    
   

	public boolean isUnsaved() {		
		return butApply.isEnabled();		
	}
	
	public void apply() {		
		if (butApply.isEnabled()) {
			if(txtGroup.getText().length() > 0)
				listener.apply(txtGroup.getText(), cboEventClass.getText(), txtGroupLogic.getText(), table);

			cboEventClass.setText("");    	
			txtGroup.setText("");
			txtGroupLogic.setText("");

			cboEventClass.setItems(listener.getEventClasses());

			txtGroup.setFocus();
		}
	}

    
} // @jve:decl-index=0:visual-constraint="10,10"

