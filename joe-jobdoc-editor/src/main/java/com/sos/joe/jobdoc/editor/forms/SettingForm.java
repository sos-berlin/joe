package com.sos.joe.jobdoc.editor.forms;
import static com.sos.dialog.Globals.MsgHandler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.jobdoc.editor.listeners.SettingListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;


public class SettingForm extends JobDocBaseForm <SettingListener> {
	private Group								group1		= null;
	@SuppressWarnings("unused") private Label	label6		= null;
	@SuppressWarnings("unused") private Label	label7		= null;
	private Text								tName		= null;
	private Text								tDefault	= null;
	@SuppressWarnings("unused") private Label	label8		= null;
	private Text								tID			= null;
	@SuppressWarnings("unused") private Label	label9		= null;
	private Combo								cReference	= null;
	@SuppressWarnings("unused") private Label	label10		= null;
	private Button								cRequired	= null;
	@SuppressWarnings("unused") private Label	label13		= null;
	private Button								bNotes		= null;
	private Combo								cType		= null;
	private Label								label		= null;
	private Table								tSettings	= null;
	private Button								bNew		= null;
	private Label								label1		= null;
	private Button								bRemove		= null;

	public SettingForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
		super(parent, style);
		this.dom = dom;
		listener = new SettingListener(dom, parentElement);
		initialize();
	}

	private void initialize() {
		createGroup1();
//		this.setLayout(new FillLayout()); // Generated
//		setSize(new Point(825, 445));
		bApply.setEnabled(false);
		bRemove.setEnabled(false);
		cType.setItems(listener.getTypes());
		setSettingStatus(false);
		listener.fillSettings(tSettings);
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		GridData gridData4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		GridData gridData2 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
		GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
		GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 4);
		GridData gridData14 = new GridData(GridData.END, GridData.CENTER, false, false);
		GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
		GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
		GridLayout gridLayout1 = new GridLayout(6, false);
		group1 = MsgHandler.newMsg("JOE_G_SettingForm_Settings").Control(new SOSGroup(this, SWT.NONE));
		group1.setLayout(gridLayout1); // Generated
		label6 = MsgHandler.newMsg("JOE_L_Name").Control(new SOSLabel(group1, SWT.NONE));
		tName = MsgHandler.newMsg("JOE_T_SettingForm_Name").Control(new Text(group1, SWT.BORDER));
		tName.setLayoutData(gridData3); // Generated
		tName.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent e) {
				applySettingStatus();
			}
		});
		bApply = MsgHandler.newMsg("JOE_B_SettingForm_Apply").Control(new Button(group1, SWT.NONE));
		bApply.setLayoutData(gridData); // Generated
		bApply.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				applySetting();
			}
		});
		label7 = MsgHandler.newMsg("JOE_L_SettingForm_DefaultValue").Control(new SOSLabel(group1, SWT.NONE));
		tDefault = MsgHandler.newMsg("JOE_T_SettingForm_DefaultValue").Control(new Text(group1, SWT.BORDER));
		tDefault.setLayoutData(gridData11); // Generated
		tDefault.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent e) {
				applySettingStatus();
			}
		});
		label13 = MsgHandler.newMsg("JOE_L_SettingForm_Type").Control(new SOSLabel(group1, SWT.NONE));
		createCType();
		label9 = MsgHandler.newMsg("JOE_L_SettingForm_Reference").Control(new SOSLabel(group1, SWT.NONE));
		createCReference();
		label8 = MsgHandler.newMsg("JOE_L_SettingForm_ID").Control(new SOSLabel(group1, SWT.NONE));
		tID = MsgHandler.newMsg("JOE_T_SettingForm_ID").Control(new Text(group1, SWT.BORDER));
		tID.setLayoutData(gridData12); // Generated
		tID.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent e) {
				applySettingStatus();
			}
		});
		label10 = MsgHandler.newMsg("JOE_L_SettingForm_Required").Control(new SOSLabel(group1, SWT.NONE));
		cRequired = MsgHandler.newMsg("JOE_B_SettingForm_Required").Control(new Button(group1, SWT.CHECK));
		cRequired.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				applySettingStatus();
			}
		});
		cRequired.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				applySettingStatus();
			}
		});
		bNotes = MsgHandler.newMsg("JOE_B_SettingForm_Notes").Control(new Button(group1, SWT.NONE));
		bNotes.setLayoutData(gridData14); // Generated
		bNotes.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				//                String tip = Messages.getTooltip("doc.note.text.setting");
				String tip = "";
				//                DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener
				//                        .isNewSetting(),"Settings Note");
				DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener.isNewSetting(),
						MsgHandler.newMsg("JOE_B_SettingForm_Notes").label());
			}
		});
		label = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label"); // Generated
		label.setLayoutData(gridData1); // Generated
		tSettings = MsgHandler.newMsg("JOE_Tbl_SettingForm_Settings").Control(new Table(group1, SWT.BORDER));
		tSettings.setHeaderVisible(true); // Generated
		tSettings.setLayoutData(gridData2); // Generated
		tSettings.setLinesVisible(true); // Generated
		tSettings.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				if (tSettings.getSelectionCount() > 0) {
					if (listener.selectSetting(tSettings.getSelectionIndex())) {
						setSettingStatus(true);
						bRemove.setEnabled(true);
					}
				}
			}
		});
		TableColumn tableColumn = MsgHandler.newMsg("JOE_TCl_SettingForm_Name").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn.setWidth(150); // Generated
		TableColumn tableColumn31 = MsgHandler.newMsg("JOE_TCl_SettingForm_Default").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn31.setWidth(120); // Generated
		TableColumn tableColumn3 = MsgHandler.newMsg("JOE_TCl_SettingForm_Type").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn3.setWidth(80); // Generated
		TableColumn tableColumn4 = MsgHandler.newMsg("JOE_TCl_SettingForm_Required").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn4.setWidth(70); // Generated
		TableColumn tableColumn2 = MsgHandler.newMsg("JOE_TCl_SettingForm_Reference").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn2.setWidth(110); // Generated
		TableColumn tableColumn1 = MsgHandler.newMsg("JOE_TCl_SettingForm_ID").Control(new TableColumn(tSettings, SWT.NONE));
		tableColumn1.setWidth(120); // Generated
		bNew = MsgHandler.newMsg("JOE_B_SettingForm_New").Control(new Button(group1, SWT.NONE));
		bNew.setLayoutData(gridData5); // Generated
		bNew.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				listener.setNewSetting();
				setSettingStatus(true);
				bRemove.setEnabled(false);
				tSettings.deselectAll();
			}
		});
		label1 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label1.setText("Label"); // Generated
		label1.setLayoutData(gridData6); // Generated
		bRemove = MsgHandler.newMsg("JOE_B_SettingForm_Remove").Control(new Button(group1, SWT.NONE));
		bRemove.setLayoutData(gridData4); // Generated
		bRemove.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				if (tSettings.getSelectionCount() > 0) {
					if (listener.removeSetting(tSettings.getSelectionIndex())) {
						setSettingStatus(false);
						bRemove.setEnabled(false);
						tSettings.deselectAll();
						listener.fillSettings(tSettings);
					}
				}
			}
		});
	}

	/**
	 * This method initializes cReference
	 */
	private void createCReference() {
		GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
		cReference = MsgHandler.newMsg("JOE_Cbo_SettingForm_Reference").Control(new Combo(group1, SWT.NONE));
		cReference.setLayoutData(gridData13); // Generated
		cReference.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent e) {
				applySettingStatus();
			}
		});
	}

	private void createCType() {
		GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		cType = MsgHandler.newMsg("JOE_Cbo_SettingForm_Type").Control(new Combo(group1, SWT.READ_ONLY));
		cType.setLayoutData(gridData15); // Generated
		cType.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) {
				applySettingStatus();
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void setSettingStatus(boolean enabled) {
		tName.setEnabled(enabled);
		tDefault.setEnabled(enabled);
		tID.setEnabled(enabled);
		cRequired.setEnabled(enabled);
		cReference.setEnabled(enabled);
		cType.setEnabled(enabled);
		bNotes.setEnabled(enabled);
		if (enabled) {
			tName.setText(listener.getName());
			tDefault.setText(listener.getDefaultValue());
			tID.setText(listener.getID());
			cRequired.setSelection(listener.isRequired());
			DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener.getReference());
			cType.select(cType.indexOf(listener.getType()));
			tName.setFocus();
			getShell().setDefaultButton(bApply);
		}
		bApply.setEnabled(false);
	}

	private void applySettingStatus() {
		bApply.setEnabled(tName.getText().length() > 0);
		Utils.setBackground(tName, true); 
	}

	@Override protected void applySetting() {
		listener.applySetting(tName.getText(), tDefault.getText(), tID.getText(), cRequired.getSelection(), cReference.getText(), cType.getText());
		listener.fillSettings(tSettings);
		setSettingStatus(tSettings.getSelectionCount() > 0);
		bRemove.setEnabled(tSettings.getSelectionCount() > 0);
	}

	@Override public void openBlank() {
		// TODO Auto-generated method stub
		
	}

	@Override public boolean applyChanges() {
		return false;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
