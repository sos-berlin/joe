package com.sos.joe.jobdoc.editor.forms;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ResourcesForm_Memory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ResourcesForm_MemoryNotes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ResourcesForm_Space;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ResourcesForm_SpaceNotes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_ResourcesForm_Memory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_ResourcesForm_Space;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ResourcesForm_Memory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ResourcesForm_Resources;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ResourcesForm_Space;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ResourcesForm_Minimum;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ResourcesForm_Unit;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ResourcesForm_Memory;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ResourcesForm_Space;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.ResourcesListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;
 
public class ResourcesForm extends JobDocBaseForm<ResourcesListener> {
//	private ResourcesListener					listener		= null;
//	private DocumentationDom					dom				= null;
	private Group								group			= null;
	private Button								cMemory			= null;
	private Button								cSpace			= null;
	private Group								group1			= null;
	private Group								group2			= null;
	@SuppressWarnings("unused") private Label	label			= null;
	@SuppressWarnings("unused") private Label	label1			= null;
	private Text								tMemory			= null;
	private Combo								cbMemory		= null;
	private Combo								cbSpace			= null;
	private Text								tSpace			= null;
	@SuppressWarnings("unused") private Label	label2			= null;
	@SuppressWarnings("unused") private Label	label3			= null;
	private Button								bMemoryNotes	= null;
	private Button								bSpaceNotes		= null;

	public ResourcesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
		super(parent, style);
		this.dom = dom;
		listener = new ResourcesListener(dom, parentElement);
		initialize();
	}

	private void initialize() {
		createGroup();
		cbMemory.setItems(listener.getUnits());
		cbSpace.setItems(listener.getUnits());
		setSpaceStatus(listener.isSpace());
		setMemoryStatus(listener.isMemory());
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData3 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		GridData gridData2 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		GridLayout gridLayout = new GridLayout(2, false);
		group = JOE_G_ResourcesForm_Resources.Control(new SOSGroup(this, SWT.NONE));
		group.setLayout(gridLayout); // Generated
		cMemory = JOE_B_ResourcesForm_Memory.Control(new Button(group, SWT.CHECK));
		cMemory.setLayoutData(gridData2); // Generated
		cMemory.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setMemory(cMemory.getSelection());
				setMemoryStatus(cMemory.getSelection());
			}
		});
		createGroup1();
		cSpace = JOE_B_ResourcesForm_Space.Control(new Button(group, SWT.CHECK));
		cSpace.setLayoutData(gridData3); // Generated
		cSpace.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setSpace(cSpace.getSelection());
				setSpaceStatus(cSpace.getSelection());
			}
		});
		createGroup2();
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridData gridData10 = new GridData();
		gridData10.widthHint = 150; // Generated
		GridLayout gridLayout1 = new GridLayout(5, false);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		group1 = JOE_G_ResourcesForm_Memory.Control(new SOSGroup(group, SWT.NONE));
		group1.setLayoutData(gridData); // Generated
		group1.setLayout(gridLayout1); // Generated
		label = JOE_L_ResourcesForm_Minimum.Control(new SOSLabel(group1, SWT.NONE));
		tMemory = JOE_T_ResourcesForm_Memory.Control(new Text(group1, SWT.BORDER));
		tMemory.setLayoutData(gridData11); // Generated
		tMemory.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setMemory(tMemory.getText());
				Utils.setBackground(tMemory, cMemory.getSelection());
			}
		});
		tMemory.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			@Override
			public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		label2 = JOE_L_ResourcesForm_Unit.Control(new SOSLabel(group1, SWT.NONE));
		createCbMemory();
		bMemoryNotes = JOE_B_ResourcesForm_MemoryNotes.Control(new Button(group1, SWT.NONE));
		bMemoryNotes.setLayoutData(gridData10); // Generated
		bMemoryNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//                String tip = Messages.getTooltip("doc.note.text.memory");
				String tip = "";
				//                DocumentationForm.openNoteDialog(dom, listener.getMemoryElement(), "note", tip, true,"Memory Note");
				DocumentationForm.openNoteDialog(dom, listener.getMemoryElement(), "note", tip, true, JOE_B_ResourcesForm_MemoryNotes.label());
			}
		});
	}

	/**
	 * This method initializes group2
	 */
	private void createGroup2() {
		GridData gridData6 = new GridData();
		gridData6.widthHint = 150; // Generated
		GridData gridData9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridLayout gridLayout2 = new GridLayout(5, false);
		GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, false);
		group2 = JOE_G_ResourcesForm_Space.Control(new SOSGroup(group, SWT.NONE));
		group2.setLayoutData(gridData1); // Generated
		group2.setLayout(gridLayout2); // Generated
		label1 = JOE_L_ResourcesForm_Minimum.Control(new SOSLabel(group2, SWT.NONE));
		tSpace = JOE_T_ResourcesForm_Space.Control(new Text(group2, SWT.BORDER));
		tSpace.setLayoutData(gridData9); // Generated
		tSpace.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setSpace(tSpace.getText());
				Utils.setBackground(tSpace, cSpace.getSelection());
			}
		});
		tSpace.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			@Override
			public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		label3 = JOE_L_ResourcesForm_Unit.Control(new SOSLabel(group2, SWT.NONE));
		createCbSpace();
		bSpaceNotes = JOE_B_ResourcesForm_SpaceNotes.Control(new Button(group2, SWT.NONE));
		bSpaceNotes.setLayoutData(gridData6); // Generated
		bSpaceNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//            	String tip = Messages.getTooltip("doc.note.text.space");
				String tip = "";
				//                DocumentationForm.openNoteDialog(dom, listener.getSpaceElement(), "note", tip, true,"Space Note");
				DocumentationForm.openNoteDialog(dom, listener.getSpaceElement(), "note", tip, true, JOE_B_ResourcesForm_SpaceNotes.label());
			}
		});
	}

	/**
	 * This method initializes cbMemory
	 */
	private void createCbMemory() {
		GridData gridData4 = new GridData();
		gridData4.widthHint = 90; // Generated
		cbMemory = JOE_Cbo_ResourcesForm_Memory.Control(new Combo(group1, SWT.NONE));
		cbMemory.setLayoutData(gridData4); // Generated
		cbMemory.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setMemoryUnit(cbMemory.getText());
			}

			@Override
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes cbSpace
	 */
	private void createCbSpace() {
		GridData gridData5 = new GridData();
		gridData5.widthHint = 90; // Generated
		cbSpace = JOE_Cbo_ResourcesForm_Space.Control(new Combo(group2, SWT.NONE));
		cbSpace.setLayoutData(gridData5); // Generated
		cbSpace.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setSpaceUnit(cbSpace.getText());
			}

			@Override
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	@Override
	public void apply() {
	}

	@Override
	public boolean isUnsaved() {
		return false;
	}

	private void setMemoryStatus(boolean enabled) {
		tMemory.setEnabled(enabled);
		cbMemory.setEnabled(enabled);
		bMemoryNotes.setEnabled(enabled);
		if (enabled) {
			if (tMemory.getText().length() > 0)
				listener.setMemory(tMemory.getText());
			tMemory.setText(listener.getMemory());
			cbMemory.select(cbMemory.indexOf(listener.getMemoryUnit()));
			cMemory.setSelection(true);
		}
		Utils.setBackground(tMemory, enabled);
		tMemory.setFocus();
	}

	private void setSpaceStatus(boolean enabled) {
		tSpace.setEnabled(enabled);
		cbSpace.setEnabled(enabled);
		bSpaceNotes.setEnabled(enabled);
		if (enabled) {
			if (tSpace.getText().length() > 0)
				listener.setSpace(tSpace.getText());
			tSpace.setText(listener.getSpace());
			cbSpace.select(cbSpace.indexOf(listener.getSpaceUnit()));
			cSpace.setSelection(true);
		}
		Utils.setBackground(tSpace, enabled);
		tSpace.setFocus();
	}

	@Override
	public void openBlank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void applySetting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyChanges() {
		// TODO Auto-generated method stub
		return false;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
