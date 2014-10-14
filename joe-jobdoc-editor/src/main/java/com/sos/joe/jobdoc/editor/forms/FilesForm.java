package com.sos.joe.jobdoc.editor.forms;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_FilesForm_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_FilesForm_New;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_FilesForm_Notes;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_FilesForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_FilesForm_OS;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_FilesForm_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_FilesForm_Files;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_FilesForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_FilesForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_FilesForm_OS;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_FilesForm_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_FilesForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_FilesForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_FilesForm_OS;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_FilesForm_Type;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_FilesForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_FilesForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_FilesForm_Files;

import org.eclipse.swt.SWT;
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
import com.sos.joe.jobdoc.editor.listeners.FilesListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;
 
public class FilesForm extends JobDocBaseForm<FilesListener> {
	private Group								group	= null;
	@SuppressWarnings("unused") private Label	label6	= null;
	private Text								tFile	= null;
	@SuppressWarnings("unused") private Label	label9	= null; 
	private Combo								cOS		= null;
	@SuppressWarnings("unused") private Label	label10	= null;
	private Combo								cType	= null;
	@SuppressWarnings("unused") private Label	label11	= null;
	private Text								tID		= null;
	private Label								label	= null;
	private Table								table	= null;
	private Button								bNew	= null;
	private Label								label1	= null;
	private Button								bRemove	= null;
	private Button								bNotes	= null;

	public FilesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
		super(parent, style);
		this.dom = dom;
		listener = new FilesListener(dom, parentElement);
		initialize();
		listener.fillFiles(table);
		bRemove.setEnabled(false);
	}

	private void initialize() {
		createGroup();
		cOS.setItems(listener.getPlatforms());
		cType.setItems(listener.getTypes());
		setFileStatus(false);
		setToolTipText();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.FILL; // Generated
		gridData9.verticalAlignment = GridData.BEGINNING; // Generated
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL; // Generated
		gridData8.verticalAlignment = GridData.CENTER; // Generated
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL; // Generated
		gridData7.verticalAlignment = GridData.CENTER; // Generated
		GridData gridData6 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3);
		GridData gridData5 = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		GridData gridData4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 5; // Generated
		group = JOE_G_FilesForm_Files.Control(new SOSGroup(this, SWT.NONE));
		group.setLayout(gridLayout2); // Generated
		label6 = JOE_L_FilesForm_File.Control(new SOSLabel(group, SWT.NONE));
		tFile = JOE_T_FilesForm_File.Control(new Text(group, SWT.BORDER));
		tFile.setLayoutData(gridData); // Generated
		tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				Utils.setBackground(tFile, true);
				setApplyStatus();
			}
		});
		bApply = JOE_B_FilesForm_Apply.Control(new Button(group, SWT.NONE));
		bApply.setLayoutData(gridData4); // Generated
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyFile();
			}
		});
		label9 = JOE_L_FilesForm_OS.Control(new SOSLabel(group, SWT.NONE));
		createCOS();
		label10 = JOE_L_FilesForm_Type.Control(new SOSLabel(group, SWT.NONE));
		createCType();
		GridData gridData11 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bNotes = JOE_B_FilesForm_Notes.Control(new Button(group, SWT.NONE));
		bNotes.setLayoutData(gridData11); // Generated
		bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//                String tip = Messages.getTooltip("doc.note.text.files");
				//                DocumentationForm.openNoteDialog(dom, listener.getFileElement(), "note", tip, true, !listener
				//                        .isNewFile(),"File Note");
				DocumentationForm.openNoteDialog(dom, listener.getFileElement(), "note", null, true, !listener.isNewFile(), JOE_B_FilesForm_Notes.label());
			}
		});
		label11 = JOE_L_FilesForm_ID.Control(new SOSLabel(group, SWT.NONE));
		tID = JOE_T_FilesForm_ID.Control(new Text(group, SWT.BORDER));
		tID.setLayoutData(gridData1); // Generated
		tID.addModifyListener(modifyTextListener);

		new Label(group, SWT.NONE);
		label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label"); // Generated
		label.setLayoutData(gridData5); // Generated
		table = JOE_Tbl_FilesForm_Files.Control(new Table(group, SWT.BORDER));
		table.setHeaderVisible(true); // Generated
		table.setLayoutData(gridData6); // Generated
		table.setLinesVisible(true); // Generated
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (table.getSelectionCount() > 0) {
					listener.selectFile(table.getSelectionIndex());
					bRemove.setEnabled(true);
					setFileStatus(true);
				}
			}
		});
		TableColumn tableColumn = JOE_TCl_FilesForm_File.Control(new TableColumn(table, SWT.NONE));
		tableColumn.setWidth(300); // Generated
		@SuppressWarnings("unused") TableColumn tableColumn3 = JOE_TCl_FilesForm_OS.Control(new TableColumn(table, SWT.NONE));
		TableColumn tableColumn2 = JOE_TCl_FilesForm_Type.Control(new TableColumn(table, SWT.NONE));
		tableColumn2.setWidth(80); // Generated
		TableColumn tableColumn1 = JOE_TCl_FilesForm_ID.Control(new TableColumn(table, SWT.NONE));
		tableColumn1.setWidth(150); // Generated
		bNew = JOE_B_FilesForm_New.Control(new Button(group, SWT.NONE));
		bNew.setLayoutData(gridData7); // Generated
		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setNewFile();
				setFileStatus(true);
				table.deselectAll();
			}
		});
		label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label1.setText("Label"); // Generated
		label1.setLayoutData(gridData8); // Generated
		bRemove = JOE_B_FilesForm_Remove.Control(new Button(group, SWT.NONE));
		bRemove.setLayoutData(gridData9); // Generated
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (table.getSelectionCount() > 0) {
					listener.removeFile(table.getSelectionIndex());
					listener.fillFiles(table);
					setFileStatus(false);
				}
				bRemove.setEnabled(false);
			}
		});
	}

	/**
	 * This method initializes cOS
	 */
	private void createCOS() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL; // Generated
		gridData2.grabExcessHorizontalSpace = true; // Generated
		gridData2.verticalAlignment = GridData.CENTER; // Generated
		cOS = JOE_Cbo_FilesForm_OS.Control(new Combo(group, SWT.READ_ONLY));
		cOS.setLayoutData(gridData2); // Generated
		cOS.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				setApplyStatus();
			}

			@Override
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes cType
	 */
	private void createCType() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL; // Generated
		gridData3.grabExcessHorizontalSpace = true; // Generated
		gridData3.verticalAlignment = GridData.CENTER; // Generated
		cType = JOE_Cbo_FilesForm_Type.Control(new Combo(group, SWT.READ_ONLY));
		cType.setLayoutData(gridData3); // Generated
		cType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				setApplyStatus();
			}

			@Override
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	@Override
	public void apply() {
		if (isUnsaved())
			apply();
	}

	@Override
	public boolean isUnsaved() {
		return bApply.getEnabled();
	}

	@Override
	public void setToolTipText() {
		//
	}

	private void setFileStatus(boolean enabled) {
		tFile.setEnabled(enabled);
		tID.setEnabled(enabled);
		cOS.setEnabled(enabled);
		cType.setEnabled(enabled);
		bNotes.setEnabled(enabled);
		if (enabled) {
			tFile.setText(listener.getFile());
			tID.setText(listener.getID());
			cOS.select(cOS.indexOf(listener.getOS()));
			cType.select(cType.indexOf(listener.getType()));
			Utils.setBackground(tFile, true);
			tFile.setFocus();
		}
		bApply.setEnabled(false);
	}

	@Override
	protected void setApplyStatus() {
		bApply.setEnabled(tFile.getText().length() > 0);
		getShell().setDefaultButton(bApply);
	}

	private void applyFile() {
		listener.applyFile(tFile.getText(), tID.getText(), cOS.getText(), cType.getText());
		bApply.setEnabled(false);
		listener.fillFiles(table);
		bRemove.setEnabled(table.getSelectionCount() > 0);
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
