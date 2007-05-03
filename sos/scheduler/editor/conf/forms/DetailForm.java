package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Group;

public class DetailForm {
	
	private String           jobChainname      = "";
	
	private DetailsListener  detailListener    = null;
	
	private String           state             = null;
	
	private Combo            comboLanguage     = null; 
	
	private Text             txtJobchainNote   = null; 
	
	private Button           butApply          = null; 
	
	private Text             txtName           = null; 
	
	private Text             txtValue          = null;
	
	private Table            tableParams       = null;
	
	private Button           butApplyParam     = null; 
	
	private Button           butRemove         = null; 
	
	private Button           cancelButton      = null; 
	
	private Text             txtParamNote      = null; 
	
	private Shell            shell             = null; 
	
	private String[]         listOfOrderIds    = null;
	
	private Combo            comboOrderId      = null; 
	
	private Group            parameterGroup    = null; 
	
	private Group            jobChainGroup     = null; 
	
	private Button           butOpen           = null; 
	
	/** Hifsvariable, wann der butApply enabled bzw. disabled gesetzt werden soll*/
	private boolean          isEditable        = false;
	
	/** Hifsvariable, wann der butApplyParam enabled bzw. disabled gesetzt werden soll*/
	private boolean          isEditableParam   = false;
	
	private Label            statusBar         = null;
	
	
	
	public DetailForm(String jobChainname_, String[] listOfOrderIds_) {
		jobChainname = jobChainname_;
		listOfOrderIds = listOfOrderIds_;		
	}
	
	public DetailForm(String jobChainname_, String state_, String[] listOfOrderIds_) {
		jobChainname = jobChainname_;
		state = state_;
		listOfOrderIds = listOfOrderIds_;
	}
	
	public void showDetails() {
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell= new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER );
		
		shell.setSize(531, 613);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				
			}
		});
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.verticalSpacing = 10;
		gridLayout_3.marginWidth = 10;
		gridLayout_3.marginTop = 10;
		gridLayout_3.marginRight = 10;
		gridLayout_3.marginLeft = 10;
		gridLayout_3.marginHeight = 10;
		gridLayout_3.marginBottom = 10;
		gridLayout_3.numColumns = 3;
		shell.setLayout(gridLayout_3);
		
		shell.setText("Details for JobChain: " + jobChainname + (state != null && state.length()> 0 ? " State: " + state: ""));
		
		final Composite composite = new Composite(shell, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		gridData_6.heightHint = 31;
		composite.setLayoutData(gridData_6);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 5;
		composite.setLayout(gridLayout_4);
		
		final Label lnagugaeLabel = new Label(composite, SWT.NONE);
		lnagugaeLabel.setText("Language: ");
		
		comboLanguage = new Combo(composite, SWT.NONE);
		comboLanguage.setItems(new String[] {"de", "en"});
		comboLanguage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
				if(tableParams.getSelectionCount() > 0) {
					TableItem item = tableParams.getSelection()[0];
					txtParamNote.setText(detailListener.getParamNote(item.getText(0), comboLanguage.getText()));				
				} else if (txtName.getText() != null && txtName.getText().length() > 0) {
					txtParamNote.setText(detailListener.getParamNote(txtName.getText(), comboLanguage.getText()));
				}
				
			}
		});
		comboLanguage.select(0);
		
		final Label orderIdLabel = new Label(composite, SWT.NONE);
		orderIdLabel.setAlignment(SWT.RIGHT);
		final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_1.widthHint = 128;
		orderIdLabel.setLayoutData(gridData_1);
		orderIdLabel.setText("Order Id:");
		
		comboOrderId = new Combo(composite, SWT.NONE);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_7.widthHint = 175;
		comboOrderId.setLayoutData(gridData_7);
		
		if(listOfOrderIds != null)
			comboOrderId.setItems(listOfOrderIds);
		comboOrderId.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR ){
					open();
				}
			}
		});
		
		butOpen = new Button(composite, SWT.NONE);
		butOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				open();
				
			}
		});
		final GridData gridData_10 = new GridData(46, SWT.DEFAULT);
		gridData_10.minimumWidth = 60;
		butOpen.setLayoutData(gridData_10);
		butOpen.setText("Open");
		
		jobChainGroup = new Group(shell, SWT.NONE);
		jobChainGroup.setEnabled(false);
		jobChainGroup.setText("Note");
		jobChainGroup.setText("Note");
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		jobChainGroup.setLayout(gridLayout_1);
		final GridData gridData = new GridData(GridData.FILL, GridData.END, false, false, 3, 1);
		gridData.heightHint = 176;
		gridData.widthHint = 471;
		gridData.horizontalIndent = -1;
		jobChainGroup.setLayoutData(gridData);
		
		txtJobchainNote = new Text(jobChainGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP);		
		txtJobchainNote.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(detailListener != null) {
					detailListener.setNote(txtJobchainNote.getText(), comboLanguage.getText());
					butApply.setEnabled(isEditable);
				}
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 2);
		gridData_2.widthHint = 405;
		gridData_2.heightHint = 144;
		txtJobchainNote.setLayoutData(gridData_2);
		
		
		parameterGroup = new Group(shell, SWT.NONE);
		parameterGroup.setEnabled(false);
		parameterGroup.setText("Detail Parameter");
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, false, 3, 1);
		gridData_3.heightHint = 239;
		parameterGroup.setLayoutData(gridData_3);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 5;
		parameterGroup.setLayout(gridLayout_2);
		
		final Label nameLabel = new Label(parameterGroup, SWT.NONE);
		nameLabel.setText("Name");
		
		txtName = new Text(parameterGroup, SWT.BORDER);
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if( !txtName.getText().equals("") &&  
						( tableParams.getSelectionCount() == 0  ||
								( tableParams.getSelectionCount() > 0 && !tableParams.getSelection()[0].getText(0).equalsIgnoreCase(txtName.getText())))){
					isEditableParam=true;
					butApplyParam.setEnabled(isEditableParam);
				}								
			}
		});
		txtName.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				
				if (e.keyCode == SWT.CR && !txtName.getText().equals("")){
					addParam();
				}
				
				
			}
		});
		
		txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label valueLabel = new Label(parameterGroup, SWT.NONE);
		valueLabel.setText("Value");
		
		txtValue = new Text(parameterGroup, SWT.BORDER);
		txtValue.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtName.getText().equals("")){
					addParam();
				}
			}
		});
		txtValue.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {				
				if( !txtName.getText().equals("") &&  
						( tableParams.getSelectionCount() == 0  ||
								( tableParams.getSelectionCount() > 0 && !tableParams.getSelection()[0].getText(1).equalsIgnoreCase(txtValue.getText())))){
					isEditableParam=true;
					butApplyParam.setEnabled(isEditableParam);
				}	
			}
		});
		txtValue.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		butApplyParam = new Button(parameterGroup, SWT.NONE);
		butApplyParam.setEnabled(isEditableParam);
		butApplyParam.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addParam();
				
			}
		});
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		butApplyParam.setLayoutData(gridData_9);
		butApplyParam.setText("Apply");
		
		tableParams = new Table(parameterGroup, SWT.FULL_SELECTION | SWT.BORDER);
		tableParams.setEnabled(false);
		tableParams.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(tableParams.getSelectionCount() > 0) {
					
					TableItem item = tableParams.getSelection()[0];
					txtName.setText(item.getText(0));
					txtValue.setText(item.getText(1));
					txtParamNote.setText(detailListener.getParamNote(item.getText(0), comboLanguage.getText()));
					butRemove.setEnabled(true);
					isEditableParam=false;
				} else {
					butRemove.setEnabled(false);
				}
			}
		});
		tableParams.setLinesVisible(true);
		tableParams.setHeaderVisible(true);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
		gridData_4.heightHint = 157;
		gridData_4.widthHint = 393;
		tableParams.setLayoutData(gridData_4);
		
		final TableColumn newColumnTableColumn = new TableColumn(tableParams, SWT.NONE);
		newColumnTableColumn.setWidth(181);
		newColumnTableColumn.setText("name");
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(tableParams, SWT.NONE);
		newColumnTableColumn_1.setWidth(242);
		newColumnTableColumn_1.setText("value");
		
		butRemove = new Button(parameterGroup, SWT.NONE);
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(tableParams.getSelectionCount() > 0) {
					detailListener.deleteParameter(tableParams, tableParams.getSelectionIndex());
					txtParamNote.setText("");
					txtName.setText("");
					txtValue.setText("");
					tableParams.deselectAll();
					txtParamNote.setText("");
					butApply.setEnabled(isEditable);
					txtName.setFocus();
				}
			}
		});
		final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 3);
		gridData_8.minimumWidth = 50;
		butRemove.setLayoutData(gridData_8);
		butRemove.setText("Remove");
		
		
		txtParamNote = new Text(parameterGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP);
		txtParamNote.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(detailListener == null)
					return;
				
				//Wert auf leer zurücksetzen
				if(txtParamNote.getText()!= null && txtParamNote.getText().length()==0)
					return;
				
				if(txtName.getText() != null && txtName.getText().length() == 0) {
					MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("tooltip.detail.param.missing_param_name_for_note"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );					
					return;	
				}
				
				
				if(txtName.getText() != null && txtName.getText().length()>0)
					detailListener.setParam(txtName.getText(), txtValue.getText(), txtParamNote.getText(), comboLanguage.getText());
				
				//butApplyParam.setEnabled(isEditableParam);
				if(  tableParams.getSelectionCount() == 0  ||
						( tableParams.getSelectionCount() > 0 && !txtParamNote.getText().equalsIgnoreCase(detailListener.getParamNote(tableParams.getSelection()[0].getText(0), comboLanguage.getText())))){
					
					isEditableParam=true;
					butApplyParam.setEnabled(isEditableParam);
				}
				
			}
			
		});
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 4, 1);
		gridData_5.heightHint = 73;
		txtParamNote.setLayoutData(gridData_5);
		
		cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butApply.getEnabled()) {
					int count = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("detailform.close"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					if(count != SWT.OK) {
						return;
					}
				}
				shell.dispose();
			}
		});
		cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		cancelButton.setText("Cancel");
		
		butApply = new Button(shell, SWT.NONE);		
		butApply.setEnabled(isEditable);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				detailListener.save();
				shell.dispose();
			}
		});
		butApply.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		butApply.setText("Apply Details");

 

		statusBar = new Label(shell, SWT.BORDER | SWT.SHADOW_NONE);		
		final GridData gridData_11 = new GridData(GridData.BEGINNING, GridData.END, false, false, 3, 1);
		gridData_11.widthHint = 496;
		gridData_11.heightHint = 18;
		statusBar.setLayoutData(gridData_11);
		statusBar.setText("Configurations File:");
		
		shell.layout();
		setToolTipText();
		
		setEnabled(false);
		comboOrderId.setFocus();
		shell.open();
	}
	
	private void initForm() {
		
		tableParams.removeAll();
		txtParamNote.setText("");		
		txtName.setText("");
		txtValue.setText("");
		
		
		detailListener = new DetailsListener(jobChainname, state, comboOrderId.getText());
		
		txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
		detailListener.fillParams(tableParams);
		butRemove.setEnabled(false);
	}
	
	public void setToolTipText() {
		
		comboLanguage.setToolTipText(Messages.getTooltip("detail.language"));
		txtJobchainNote.setToolTipText(Messages.getTooltip("detail.detail_note")); 
		butApply.setToolTipText(Messages.getTooltip("detail.apply")); 
		txtName.setToolTipText(Messages.getTooltip("detail.param.name")); 
		txtValue.setToolTipText(Messages.getTooltip("detail.param.value"));
		tableParams.setToolTipText(Messages.getTooltip("detail.param.table"));
		butApplyParam.setToolTipText(Messages.getTooltip("detail.param.apply")); 
		butRemove.setToolTipText(Messages.getTooltip("detail.param.remove")); 
		cancelButton.setToolTipText(Messages.getTooltip("detail.cancel")); 
		txtParamNote.setToolTipText(Messages.getTooltip("detail.param.note")); 
		comboOrderId.setToolTipText(Messages.getTooltip("detail.param.order_ids"));
		butOpen.setToolTipText(Messages.getTooltip("detail.param.open_configuration_file"));
		statusBar.setToolTipText(Messages.getTooltip("detail.status_bar_for_configuration_filename"));
	}
	
	private void setEnabled(boolean enabled){
		jobChainGroup.setEnabled(enabled);
		parameterGroup.setEnabled(enabled);		 
		comboLanguage.setEnabled(enabled); 
		txtJobchainNote.setEnabled(enabled); txtName.setEnabled(enabled); 
		txtValue.setEnabled(enabled); 
		tableParams.setEnabled(enabled); 
		txtParamNote.setEnabled(enabled);		
		butRemove.setEnabled(enabled);
	}
	
	private void open(){
		if(butApply.getEnabled()) {
			int count = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("detailform.open"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			if(count != SWT.OK) {
				return;
			}
		}
		setEnabled(true);
		
		initForm();
		isEditable = true;
		txtJobchainNote.setFocus();
		butApply.setEnabled(false);
		butApplyParam.setEnabled(false);
		if(detailListener!= null && detailListener.getConfigurationFilename()!=null) {
			statusBar.setText("Configurations File: " + detailListener.getConfigurationFilename());
		}
	}
	
	private void addParam() {
		if(txtName.getText().length()> 0) {
			detailListener.setParam(txtName.getText(), txtValue.getText(), txtParamNote.getText(), comboLanguage.getText());
			txtParamNote.setText(detailListener.getParamNote(txtName.getText(),  comboLanguage.getText()));
			tableParams.removeAll();
			detailListener.fillParams(tableParams);			
			butApply.setEnabled(isEditable);			
			txtName.setText("");
			txtValue.setText("");			
			isEditableParam = false;
			butApplyParam.setEnabled(isEditableParam);
			txtName.setFocus();
			
		}
	}
}
