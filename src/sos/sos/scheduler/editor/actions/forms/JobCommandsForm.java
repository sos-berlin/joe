package sos.scheduler.editor.actions.forms;

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
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.JobCommandsListener;


public class JobCommandsForm extends Composite implements IUpdateLanguage {

    private JobCommandsListener listener       = null;

    private Group               commandsGroup  = null;

    private Table               table          = null;

    private Button              bNewCommands   = null;

    private Button              bRemoveCommand = null;

    private Label               label          = null;
    
    private ActionsDom          _dom           = null;
    

                                     
  public JobCommandsForm(Composite parent, int style, ActionsDom dom, Element action,  ActionsForm gui) {
        super(parent, style);        
  
        listener = new JobCommandsListener(dom, action, gui);
        _dom = dom;
        initialize();
        setToolTipText();
        listener.fillTable(table);       
    }
  


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(656, 400));
    }


    /**
     * This method initializes group
     */
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
        commandsGroup = new Group(this, SWT.NONE);
        commandsGroup.setText("Commands");
        commandsGroup.setLayout(gridLayout);
        createTable();
        bNewCommands = new Button(commandsGroup, SWT.NONE);
        bNewCommands.setText("&New Command");
        bNewCommands.setLayoutData(gridData);
        getShell().setDefaultButton(bNewCommands);

        label = new Label(commandsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData4);
        bNewCommands.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newCommands(table);
                bRemoveCommand.setEnabled(true);
            }
        });
        bRemoveCommand = new Button(commandsGroup, SWT.NONE);
        bRemoveCommand.setText("Remove Command");
        bRemoveCommand.setEnabled(false);
        bRemoveCommand.setLayoutData(gridData1);
        bRemoveCommand.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
        	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
        		if(table != null && table.getSelectionCount() > 0)  {
        			int cont = sos.scheduler.editor.app.MainWindow.message(getShell(), "If you really want to delete this command?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );

        			if(cont == SWT.OK) {				        				
        				bRemoveCommand.setEnabled(listener.deleteCommands(table));        				
        			} 
        		}
        	}
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData2.widthHint = 204;
        table = new Table(commandsGroup, SWT.BORDER | SWT.FULL_SELECTION);
        table.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		ContextMenu.goTo(table.getSelection()[0].getText(0), _dom, Editor.ACTION_COMMANDS);
        	}
        });
        table.setHeaderVisible(true);
        table.setLayoutData(gridData2);
        table.setLinesVisible(true);
        TableColumn commandnameTableColumn = new TableColumn(table, SWT.NONE);
        commandnameTableColumn.setWidth(151);
        commandnameTableColumn.setText("Command");

        final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.setWidth(100);
        newColumnTableColumn_1.setText("Host");
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemoveCommand.setEnabled(true);
            }
        });

        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.setWidth(100);
        newColumnTableColumn.setText("Port");

    }


    public void setToolTipText() {
        bNewCommands.setToolTipText(Messages.getTooltip("jobcommands.btn_add_new"));
        bRemoveCommand.setToolTipText(Messages.getTooltip("jobcommands.btn_remove"));
        table.setToolTipText(Messages.getTooltip("jobcommands.table"));
    }

} // @jve:decl-index=0:visual-constraint="10,10"
