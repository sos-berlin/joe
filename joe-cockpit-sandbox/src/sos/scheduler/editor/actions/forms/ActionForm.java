package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.joe.interfaces.IUpdateLanguage;

import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.ActionListener;
import sos.scheduler.editor.app.SOSJOEMessageCodes;

public class ActionForm extends SOSJOEMessageCodes implements IUpdateLanguage {

	private ActionListener	listener		= null;
	private Group			actionsGroup	= null;
	private Text			txtName			= null;

	public ActionForm(final Composite parent, final int style, final ActionsDom dom, final Element action, final ActionsForm _gui) {
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
		txtName.setFocus();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		actionsGroup = JOE_G_ActionForm_Action.Control(new Group(this, SWT.NONE));
		actionsGroup.setLayout(gridLayout); // Generated

		@SuppressWarnings("unused")
		final Label nameLabel = JOE_L_Name.Control(new Label(actionsGroup, SWT.NONE));

		txtName = JOE_T_ActionForm_Name.Control(new Text(actionsGroup, SWT.BORDER));
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				listener.setName(txtName.getText());
			}
		});
		txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
	}

	@Override
	public void setToolTipText() {
		//
	}

} // @jve:decl-index=0:visual-constraint="10,10"

