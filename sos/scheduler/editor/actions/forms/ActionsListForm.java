package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.ActionsListListener;
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.util.SOSString;


public class ActionsListForm extends Composite implements IUpdateLanguage {
    
	private ActionsListListener listener     = null;

    private Group       actionsGroup         = null;
    
    private Table       list                 = null; 
        
    private Button      butRemove            = null;
    
    private Button      butNew               = null;
    
    private ActionsForm gui                 = null;
    
    private ActionsDom  _dom                 = null;
 
    public ActionsListForm(Composite parent, int style, ActionsDom dom, Element actions, ActionsForm _gui) {
        super(parent, style);
        gui = _gui;
        _dom = dom;
        listener = new ActionsListListener(dom, actions);
        initialize();
        setToolTipText();
       
    }


    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
        listener.fillActions(list);

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setText("Actions"); // Generated
        actionsGroup.setLayout(gridLayout); // Generated

        list = new Table(actionsGroup, SWT.BORDER);
        list.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		ContextMenu.goTo(list.getSelection()[0].getText(0), _dom, Editor.ACTIONS);
        	}
        });
        list.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {        		
        		butRemove.setEnabled(list.getSelectionCount() > 0);
        	}
        });
        list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));

        butNew = new Button(actionsGroup, SWT.NONE);
        butNew.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		TableItem item = new TableItem(list, SWT.NONE);
        		item.setText("New Action " + list.getItemCount());
        		listener.newAction("New Action " + list.getItemCount());
        		listener.fillActions(list);
        		butRemove.setEnabled(false);
        		gui.updateActions();
        		
        	}
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butNew.setText("New");

        butRemove = new Button(actionsGroup, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		listener.removeAction(list);
        		
        		butRemove.setEnabled(false);
        		gui.updateActions();
        		
        	}
        });
        butRemove.setText("Remove");
        new Label(actionsGroup, SWT.NONE);
    }
 

    public void setToolTipText() {
        butNew.setToolTipText(Messages.getTooltip("actions.but_new"));
        butRemove.setToolTipText(Messages.getTooltip("actions.but_remove"));
        list.setToolTipText(Messages.getTooltip("actions.list"));
    }
    

} // @jve:decl-index=0:visual-constraint="10,10"

