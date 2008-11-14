package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.ActionListener;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;


public class ActionForm extends Composite implements IUpdateLanguage {
    
	private ActionListener listener     = null;

    private Group       actionsGroup         = null;
    
    private Text        txtName              = null; 
   
   // private ActionsForm gui                 = null;
    
    
    public ActionForm(Composite parent, int style, ActionsDom dom, Element action, ActionsForm _gui) {
        super(parent, style);           
        //gui = _gui;
        listener = new ActionListener(dom, action, _gui);
        initialize();
        setToolTipText();
       
    }


    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
       txtName.setText(listener.getName());

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setText("Action"); // Generated
        actionsGroup.setLayout(gridLayout); // Generated

        final Label nameLabel = new Label(actionsGroup, SWT.NONE);
        nameLabel.setText("Name: ");

        txtName = new Text(actionsGroup, SWT.BORDER);
        txtName.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setName(txtName.getText());
        	}
        });
        txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }
 

    public void setToolTipText() {
    	txtName.setToolTipText(Messages.getTooltip("actions.name"));
    }
    

} // @jve:decl-index=0:visual-constraint="10,10"

