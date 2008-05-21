package sos.scheduler.editor.conf.forms;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.DetailDom;
import sos.scheduler.editor.conf.IDetailUpdate;
import sos.scheduler.editor.conf.listeners.JobChainConfigurationListener;


public class DetailForm extends Composite implements IUpdateLanguage {
	
	private   String                            jobChainname      = "";
	
	private   DetailsListener                   detailListener    = null;
	
	private   String                            state             = null;
	
	private   Combo                             comboLanguage     = null; 
	
	private   Text                              txtJobchainNote   = null; 
	
	private   Button                            butApply          = null; 
	
	private   Text                              txtName           = null; 
	
	private   Text                              txtValue          = null;
	
	private   Table                             tableParams       = null;
	
	private   Button                            butApplyParam     = null; 
	
	private   Button                            butRemove         = null; 
	
	private   Button                            cancelButton      = null; 
	
	private   Text                              txtParamNote      = null; 
	
	private   String[]                          listOfOrderIds    = null;
	
	private   Combo                             comboOrderId      = null; 
	
	private   Group                             parameterGroup    = null; 
	
	private   Group                             jobChainGroup     = null; 
	
	private   Button                            butOpen           = null; 
	
	/** Hifsvariable, wann der butApply enabled bzw. disabled gesetzt werden soll*/
	private   boolean                          isEditable        = false;
	
	/** Hifsvariable, wann der butApplyParam enabled bzw. disabled gesetzt werden soll*/
	private   boolean                           isEditableParam   = false;
	
	private   Label                             statusBar         = null;
	
	/** wer hat ihn aufgerufen*/
	private   int                                type              = -1;
	
	private   Label                              orderIdLabel      = null; 
	
	private   Text                               txtJobChainname   = null;
	
	private   Label                              lblChainname      = null;
	
	private   Button                              butNewState       = null;
	
	private   Tree                                tree              = null;
	
	private   Label                               lblState          = null; 
	
	private   Text                                txtState          = null; 
	
	private   Composite                           parent            = null;
	
	private   JobChainConfigurationListener       confListener     = null;
	
	private   DetailDom                           dom              = null;
	
	private   IDetailUpdate                       gui              = null;
	
	private   Button                              butRemoveState   = null;
	
	private   Button                              butXML           = null; 
		
	private   Button                              butDocumentation = null; 
	
	private   Text                                paramText        = null;
	
	private   Button                              butText          = null;
	
	private   Text                                txtParamsFile    = null;
	
	private   boolean                             isLifeElement    = false;
	
	private   String                              path             = null;           
	
	/*
	public DetailForm(Composite parent_, int style, int type_) {
		
		super(parent_, style);
		type = type_; 
		
		initialize();
		
		setToolTipText();		
		parent = parent_;
		
	}
	*/
	public DetailForm(Composite parent_, 
			int style, 
			int type_,
			DetailDom  dom_,
			IDetailUpdate gui_,
			boolean isLifeElement_,
			String path_) {		
		super(parent_, style);
		dom = dom_;
		gui = gui_;		
		type = type_; 
		initialize();
		setToolTipText();		
		parent = parent_;
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	
	public DetailForm(Composite parent_, 
			int style, 
			String jobChainname_, 
			String state_, 
			String[] listOfOrderIds_, 
			int type_, 
			DetailDom  dom_,
			IDetailUpdate gui_,
			boolean isLifeElement_,
			String path_) {		
		
		super(parent_, style);		
		dom = dom_;
		gui = gui_;		
		type = type_;		
		jobChainname = jobChainname_;
		state = state_;
		listOfOrderIds = listOfOrderIds_;
		initialize();
		setToolTipText();
		parent = parent_;
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	
	private void initialize() {
		this.setLayout(new FillLayout());				
		createGroup();   
		getShell().layout();
		getShell().open();
	}
	
	
	private void createGroup() {
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.verticalSpacing = 10;
		gridLayout_3.marginWidth = 10;
		gridLayout_3.marginTop = 10;
		gridLayout_3.marginRight = 10;
		gridLayout_3.marginLeft = 10;
		gridLayout_3.marginHeight = 10;
		gridLayout_3.marginBottom = 10;
		gridLayout_3.numColumns = 3;
		
		final Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, true, 3, 1);
		gridData_6.heightHint = 31;
		composite.setLayoutData(gridData_6);
		
		final Group group = new Group(composite, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		group.setLayout(gridLayout);
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		if(jobChainname != null)
			group.setText(jobChainname);
		
		lblChainname = new Label(group, SWT.NONE);
		lblChainname.setLayoutData(new GridData());
		lblChainname.setText("Job Chain Name:");
		
		txtJobChainname = new Text(group, SWT.BORDER);
		txtJobChainname.setText(jobChainname!=null?jobChainname:"");
		txtJobChainname.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {				
				
				boolean fillMain = false;//hilfsvariable
				
				if(gui != null ) 
					gui.updateJobChainname(txtJobChainname.getText());
				
				if(jobChainname == null || jobChainname.length() == 0) {
					fillMain = true;				
				} 
				jobChainname = txtJobChainname.getText();
				if(jobChainname != null && jobChainname.length()>0) {					
					butNewState.setEnabled(true);
					setEnabled_(true);
					
				} 
				
				group.setText(txtJobChainname.getText());				
				if(tree != null && tree.getSelectionCount() > 0) {					
					TreeItem item = tree.getSelection()[0];					
					if(item.getText(0).startsWith("Job Chain:")) {
						item.setText("Job Chain: " + txtJobChainname.getText());
					    item.setData(new TreeData(Editor.DETAILS, new org.jdom.Element("details"), Options.getHelpURL("details")));
						
					}
				}
				
				if(fillMain){					
					confListener.setJobChainname(jobChainname);
				} else {					
					confListener.setJobChainname(jobChainname);
				}
				
				if(detailListener != null) {
					detailListener.setJobChainname(txtJobChainname.getText());
				}
			}
		});
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		gridData_12.widthHint = 105;
		txtJobChainname.setLayoutData(gridData_12);
		
		butOpen = new Button(group, SWT.NONE);
		butOpen.setVisible(true);
		butOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				open();
				
			}
		});
		final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_10.widthHint = 62;
		gridData_10.minimumWidth = 60;
		butOpen.setLayoutData(gridData_10);
		butOpen.setText("Open");
		
		final Label lnagugaeLabel = new Label(group, SWT.NONE);
		lnagugaeLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		lnagugaeLabel.setText("Language: ");
		
		comboLanguage = new Combo(group, SWT.NONE);
		comboLanguage.setItems(new String[] {"de", "en"});
		comboLanguage.setLayoutData(new GridData());
		comboLanguage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
				if(tableParams.getSelectionCount() > 0) {
					TableItem item = tableParams.getSelection()[0];
					txtParamNote.setText(detailListener.getParamNote(item.getText(0), comboLanguage.getText()));				
				} else if (txtName.getText() != null && txtName.getText().length() > 0) {
					txtParamNote.setText(detailListener.getParamNote(txtName.getText(), comboLanguage.getText()));
				} else if(txtParamNote.getText() != null && txtParamNote.getText().length()>0) {
					txtParamNote.setText("");
				}
				isEditable=false;
				isEditableParam = false;
				butApply.setEnabled(isEditable);
				butApplyParam.setEnabled(isEditableParam);
				butRemove.setEnabled(false);
				
			}
		});
		comboLanguage.select(0);
		
		orderIdLabel = new Label(group, SWT.NONE);
		orderIdLabel.setAlignment(SWT.RIGHT);
		final GridData gridData_1 = new GridData(50, SWT.DEFAULT);
		orderIdLabel.setLayoutData(gridData_1);
		orderIdLabel.setText("Order Id:");
		
		comboOrderId = new Combo(group, SWT.NONE);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_7.widthHint = 95;
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
		comboOrderId.setFocus();
		
		butApply = new Button(group, SWT.NONE);		
		butApply.setEnabled(isEditable);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				detailListener.save();
				
				txtState.setEnabled(false);
				if(type == Editor.JOB_CHAINS) {
					getShell().dispose();
				} else {
					isEditable = false;
					butApply.setEnabled(isEditable);
				}
							
			}
		});
		butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butApply.setText("Apply Details");
		
		lblState = new Label(group, SWT.NONE);
		lblState.setLayoutData(new GridData(27, SWT.DEFAULT));
		lblState.setText("State");
		
		txtState = new Text(group, SWT.BORDER);
		if( state != null ) {
			txtState.setText(state);
		}
		txtState.setEnabled(false);
		txtState.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = isState();
			}
		});
		if(tree != null) {
			TreeItem item = tree.getSelection()[0];
			if(item.getText(0).startsWith("State: "))
				txtState.setEnabled(true);
		}
		txtState.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				boolean valid = detailListener.isValidState(txtState.getText());
				if (!valid)
					txtState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else {
					txtState.setBackground(null);
					if(tree != null && tree.getSelectionCount() > 0) {					
						TreeItem item = tree.getSelection()[0];
						if(item.getText(0).startsWith("State: "))
							item.setText("State: " + txtState.getText());
						
						if(gui!=null)
							gui.updateState(txtState.getText());
						
						state = txtState.getText();
						detailListener.updateState(item.getData() != null ? item.getData().toString(): "", txtState.getText());
						item.setData(state);					
					}
				}
			}
		});
		final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_13.widthHint = 130;
		txtState.setLayoutData(gridData_13);
		
		butRemoveState = new Button(group, SWT.NONE);
		butRemoveState.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {				
				deleteState();
			}
		});
		butRemoveState.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		butRemoveState.setText("Remove State");
		
		butNewState = new Button(group, SWT.NONE);
		butNewState.setEnabled(jobChainname != null && jobChainname.length() > 0? true : false);
		
		butNewState.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(tree != null) {					
					TreeItem item = new TreeItem(tree.getTopItem(), SWT.NONE);	
					state = String.valueOf(tree.getTopItem().getItemCount());
					item.setText("State: " + state);
					item.setExpanded(true);
					item.setData(state);
					tree.setSelection(new TreeItem[] {item});
					txtState.setText(state);
					txtState.setEnabled(true);
					txtState.setFocus();
					
					if(gui!=null)
						gui.updateState(txtState.getText());		        	
					confListener.treeSelection(tree, parent);
					
				}
				
			}
		});
		butNewState.setLayoutData(new GridData());
		butNewState.setText("New State");
		
		cancelButton = new Button(group, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butApply.getEnabled()) {
					int count = MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("detailform.close"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					if(count != SWT.OK) {
						return;
					}
				}
				getShell().dispose();
			}
		});
		cancelButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		cancelButton.setText("Cancel");
		
		jobChainGroup = new Group(group, SWT.NONE);
		jobChainGroup.setEnabled(false);
		jobChainGroup.setText("Note");
		jobChainGroup.setText("Note");
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		jobChainGroup.setLayout(gridLayout_1);
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 5, 1);
		gridData.heightHint = 159;
		gridData.widthHint = 471;
		gridData.horizontalIndent = -1;
		jobChainGroup.setLayoutData(gridData);
		
		txtJobchainNote = new Text(jobChainGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
		//txtJobchainNote = new Text(jobChainGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);		
		txtJobchainNote.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				/*if(e.text.equals("<") ) {
					tag = e.text;	
				} else if(e.text.equals(">")) {
					tag=tag + e.text + tag.replaceAll("<", "</") + e.text;
					System.out.println(tag); 
				} else if(tag.length() > 0) {
					tag = tag.concat(e.text);
				}*/
			    
			}
			
		
		});
		txtJobchainNote.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				//if(tag.length() == 0) {
					if(detailListener != null) {
						isEditable=true;
						if(gui!=null ) //&& txtJobchainNote.getText().length()) // && !detailListener.getNote(comboLanguage.getText()).equalsIgnoreCase(txtJobchainNote.getText()))
							gui.updateNote();
						detailListener.setNote(txtJobchainNote.getText(), comboLanguage.getText());
						butApply.setEnabled(isEditable);
					}
				//}
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
		gridData_2.widthHint = 405;
		gridData_2.heightHint = 127;
		txtJobchainNote.setLayoutData(gridData_2);
		
		
		parameterGroup = new Group(group, SWT.NONE);
		parameterGroup.setEnabled(false);
		parameterGroup.setText("Detail Parameter");
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 1);
		gridData_3.heightHint = 239;
		parameterGroup.setLayoutData(gridData_3);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 6;
		parameterGroup.setLayout(gridLayout_2);

		final Label fileLabel = new Label(parameterGroup, SWT.NONE);
		fileLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
		fileLabel.setText("File");

		txtParamsFile = new Text(parameterGroup, SWT.BORDER);
		txtParamsFile.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				detailListener.setParamsFileName(txtParamsFile.getText()); 
			}
		});
		txtParamsFile.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));
		//txtParamsFile.setText(detailListener.getParamsFileName());
		
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
					txtValue.setEnabled(true);
					butText.setEnabled(true);
					paramText.setText("");
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
					if(txtValue.getText().trim().length() > 0)
						butText.setEnabled(false);
					else 
						butText.setEnabled(true);
				}	
			}
		});
		txtValue.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		butText = new Button(parameterGroup, SWT.NONE);
		butText.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String ntext = "";
				if(tableParams.getSelectionCount() > 0 ) {
					TableItem item = tableParams.getSelection()[0];
					ntext = item.getText(2); // != null && item.getText(2).length() > 0 ? item.getText(2) : "<![CDATA[  " + item.getText(2) + "  ]]>";
				} /*else {
					ntext =  "<![CDATA[    ]]>";
				}*/
				
				
				
				String text = sos.scheduler.editor.app.Utils.showClipboard(ntext, getShell(), true, "");				
				if(text != null && !text.trim().equalsIgnoreCase(ntext)) {					
					//item.setText(2, text);
					paramText.setText(text);
					txtValue.setText("");
					txtValue.setEnabled(false);
					butText.setEnabled(true);
					addParam();
				} else if (text == null)  {					
					txtValue.setEnabled(true);
					butText.setEnabled(true);
				} else {
					txtValue.setEnabled(true);
					butText.setEnabled(false);
				}
				butApply.setEnabled(true);
					
			}
		});
		butText.setText("Text");
		
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
						
					//param value ist angegeben
					if(item.getText(1) != null && item.getText(1).trim().length() > 0) {
						paramText.setText("");
						txtValue.setText(item.getText(1));
						txtValue.setEnabled(true);						
						butText.setEnabled(false);
					}
					
					//param Textknoten ist angegeben
					if(item.getText(2) != null && item.getText(2).trim().length() > 0) {
						paramText.setText(item.getText(2));
						txtValue.setText("");
						txtValue.setEnabled(false);
						butText.setEnabled(true);
					}
					
					if(item.getText(1).trim().equals("") && item.getText(2).trim().equals("")) {
						paramText.setText("");
						txtValue.setText("");
						txtValue.setEnabled(true);
						butText.setEnabled(true);
					}
						
							
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
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
		gridData_4.heightHint = 157;
		gridData_4.widthHint = 413;
		tableParams.setLayoutData(gridData_4);
		
		final TableColumn newColumnTableColumn = new TableColumn(tableParams, SWT.NONE);
		newColumnTableColumn.setWidth(181);
		newColumnTableColumn.setText("Name");
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(tableParams, SWT.NONE);
		newColumnTableColumn_1.setWidth(150);
		newColumnTableColumn_1.setText("Value");

		final TableColumn newColumnTableColumn_2 = new TableColumn(tableParams, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("Text");

		final Button butNew = new Button(parameterGroup, SWT.NONE);
		butNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtName.setText("");
				txtValue.setText("");
				paramText.setText("");
				txtValue.setEnabled(true);
				paramText.setEnabled(true);
				tableParams.deselectAll();
				txtParamNote.setText("");
			}
		});
		butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNew.setText("New");
		
		butRemove = new Button(parameterGroup, SWT.NONE);
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(tableParams.getSelectionCount() > 0) {
					detailListener.deleteParameter(tableParams, tableParams.getSelectionIndex());
					txtParamNote.setText("");
					txtName.setText("");
					txtValue.setText("");
					tableParams.deselectAll();
					butRemove.setEnabled(false);
					txtParamNote.setText("");
					isEditableParam = false;
					butApplyParam.setEnabled(isEditableParam);
					butApply.setEnabled(isEditable);
					txtName.setFocus();
					if(gui!=null)
						gui.updateParam();
				}
			}
		});
		final GridData gridData_8 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		gridData_8.widthHint = 64;
		gridData_8.minimumWidth = 50;
		butRemove.setLayoutData(gridData_8);
		butRemove.setText("Remove");

		paramText = new Text(parameterGroup, SWT.BORDER);
		paramText.setVisible(false);
		final GridData gridData_14 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		gridData_14.widthHint = 27;
		paramText.setLayoutData(gridData_14);
		
		
		txtParamNote = new Text(parameterGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);		
		txtParamNote.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if(e.keyCode == 8 || e.keyCode == 127) {
					isEditableParam = true;
					butApplyParam.setEnabled(isEditableParam);
				}
			}
		});
		txtParamNote.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				changeParameNote();
				
				
				
			}
			
		});
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 1);
		gridData_5.heightHint = 73;
		txtParamNote.setLayoutData(gridData_5);
		new Label(parameterGroup, SWT.NONE);

		butXML = new Button(group, SWT.NONE);
		butXML.setLayoutData(new GridData());
		butXML.setEnabled(false);
		
		butXML.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				try {
					if(type == Editor.JOB_CHAINS) {
						DetailXMLEditorDialogForm dialog = new DetailXMLEditorDialogForm(detailListener.getConfigurationFilename(), jobChainname, state, listOfOrderIds, comboOrderId.getText(), type, isLifeElement, path);
						
						dialog.showXMLEditor();
						
						getShell().dispose();
					} else {
						if(dom != null && dom.getFilename() != null && dom.getFilename().length() > 0) {
							DetailXMLEditorDialogForm dialog = new DetailXMLEditorDialogForm(dom, type, isLifeElement, path);
							dialog.setConfigurationData(confListener, tree, parent);
							dialog.showXMLEditor();
						} else {
							//MainWindow.message(sos.scheduler.editor.app.Messages.getString("detailform.save_before_open_xml_editor"), SWT.ICON_ERROR);	
							MainWindow.message("Please save jobchain configuration file before opening XML Editor.", SWT.ICON_ERROR);
						}
					}
					
				} catch (Exception ex) {
					try {
						System.out.println("..error in " + sos.util.SOSClassUtil.getMethodName() + ": " +ex.getMessage());
		    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
		    		} catch(Exception ee) {
		    			//tu nichts
		    		}
		    		
					
				}
			}
		});
		butXML.setText("Open XML");
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);

		butDocumentation = new Button(group, SWT.NONE);
		butDocumentation.setLayoutData(new GridData());
		butDocumentation.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String filename = null;
				try {
					
					if(type == Editor.JOB_CHAINS) {
						filename = detailListener.getConfigurationFilename();
					} else {
						if(dom!=null) {
							filename = dom.getFilename();
						}
					}
        			if (filename != null && filename.length() > 0) {
        				File file = new File(filename);
        				if(file.exists()) {
        					//Runtime.getRuntime().exec("cmd /C START iExplore ".concat(filename));
        					
        					Program prog = Program.findProgram("html");
        		            if (prog != null)
        		                prog.execute(new File(filename).toURL().toString());
        		            else {
        		            	 // String test = "rundll32 url.dll FileProtocolHandler file://C:/scheduler/config/configuration_mci_chandon_incoming_ipm.xml";
        		            	  //Runtime.getRuntime().exec(test.split(" "));
        		            	String[] split = Options.getBrowserExec(new File(filename).toURL().toString(), Options.getLanguage());
        		            	Runtime.getRuntime().exec(split);
        		            	//Runtime.getRuntime().exec(Options.getBrowserExec(new File(filename).toURL().toString(), Options.getLanguage()));
        		            	
        		            }        					 
        					
        				} else
        					MainWindow.message("Missing documentation " + file.getCanonicalPath() , SWT.ICON_ERROR);
        			} else {
        				MainWindow.message("Please save jobchain configuration before opening documentation." , SWT.ICON_ERROR);
        				
        			}
        		} catch (Exception ex) {
        			try {
             			System.out.println("..could not open file " + filename + " " + ex.getMessage());						
		    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "..could not open file " + filename, ex);
		    		} catch(Exception ee) {
		    			//tu nichts
		    		}
		    		
   
        		}
			}
		});
		butDocumentation.setText("Documentation");
		
		
		
		statusBar = new Label(group, SWT.BORDER);		
		final GridData gridData_11 = new GridData(GridData.FILL, GridData.END, false, false, 5, 1);
		gridData_11.widthHint = 496;
		gridData_11.heightHint = 18;
		statusBar.setLayoutData(gridData_11);
		statusBar.setText("Configurations File:");
		setToolTipText();
		
		if(type == Editor.JOB_CHAINS)
			setEnabled_(false);
		setVisibility();
	}
	
	private void setVisibility() {
		if(type==Editor.DETAILS) {
			comboOrderId.setVisible(false);
			cancelButton.setVisible(false);
			orderIdLabel.setVisible(false);
			butOpen.setVisible(false);
			statusBar.setVisible(false);
			butApply.setVisible(false);
		} else if(type==Editor.JOB_CHAINS) {
			txtJobChainname.setVisible(false);
			lblChainname.setVisible(false);
			butNewState.setVisible(false);
			lblState.setVisible(false); 			
			txtState.setVisible(false); 	
			butRemoveState.setVisible(false);
		}		
		
	}
	
	
	
	private void addParam() {
		
		if(txtName.getText().length()> 0) {
			if(tableParams.getSelectionCount() > 0) {
				//if(tableParams.getSelection()[0].getText(2).length() > 0)
					//paramText.setText(tableParams.getSelection()[0].getText(2)); 
				detailListener.deleteParameter(tableParams, tableParams.getSelectionIndex());				
			}
			detailListener.setParam(txtName.getText(), txtValue.getText(), txtParamNote.getText(), paramText.getText(), comboLanguage.getText());
			txtParamNote.setText(detailListener.getParamNote(txtName.getText(),  comboLanguage.getText()));
			tableParams.removeAll();
			detailListener.fillParams(tableParams);			
			butApply.setEnabled(isEditable);			
			txtName.setText("");
			txtValue.setText("");				
			isEditableParam = false;
			butApplyParam.setEnabled(isEditableParam);
			txtName.setFocus();
			isEditableParam=false;
			butApplyParam.setEnabled(isEditableParam);
			isEditable = true;
			butApply.setEnabled(isEditable);
			butRemove.setEnabled(false);
			txtParamNote.setText("");	
			if(gui!=null)
				gui.updateParam();
			paramText.setText("");
		}
	}
	
	private boolean discardChanges() {
		if(butApply.getEnabled()) {
			int count = MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("detailform.open"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			if(count != SWT.OK) {
				return false;
			}
		}
		setEnabled_(true);
		return true;
	}
	
	public void open(String orderId){
		if(orderId != null && orderId.length() > 0)
			comboOrderId.setText(orderId);
		open();
	}
	
	public void open(){
		
		
		if(!discardChanges()) 
			return;
		
		
		if(!initForm()) 
			return;
		
		
		isEditable = true;
		if(type == Editor.DETAILS) {
			if(tree != null && tree.getSelection()[0].getText().startsWith("State: ")) {
				txtState.setFocus();
			} else {
				txtJobChainname.setFocus();
			}
		} else {
			txtJobchainNote.setFocus();
		}
		butXML.setEnabled(true);
		butApply.setEnabled(false);
		butApplyParam.setEnabled(false);
		
		if(detailListener!= null && detailListener.getConfigurationFilename()!=null) {
			statusBar.setText("Configurations File: " + detailListener.getConfigurationFilename());
		}
		
	}
	private boolean initForm() {
		
		tableParams.removeAll();
		txtParamNote.setText("");		
		txtName.setText("");
		txtValue.setText("");
		
		detailListener = new DetailsListener(jobChainname, state, comboOrderId.getText(), type, dom, isLifeElement, path);
		if(detailListener != null && detailListener.hasError()) {
			if(type == Editor.DETAILS)
				dispose();
			getShell().dispose();
			return false;
		}
		if(state != null && state.length() > 0) {
			butRemoveState.setEnabled(true);
			txtState.setEnabled(true);
		} else { 
			butRemoveState.setEnabled(false);
			txtState.setEnabled(false);
		}
		
		if(detailListener.getNote(comboLanguage.getText()).length() > 0)
			txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
		
		txtParamsFile.setText(detailListener.getParamsFileName());
		
		detailListener.fillParams(tableParams);
		butRemove.setEnabled(false);
		return true;
		
	}	
	
	private void setEnabled_(boolean enabled){
		jobChainGroup.setEnabled(enabled);
		parameterGroup.setEnabled(enabled);		 
		comboLanguage.setEnabled(enabled); 
		txtJobchainNote.setEnabled(enabled); txtName.setEnabled(enabled); 
		txtValue.setEnabled(enabled); 
		tableParams.setEnabled(enabled); 
		txtParamNote.setEnabled(enabled);		
		butRemove.setEnabled(enabled);
		
	}
	
	public void setTree(Tree tree_) {
		tree = tree_;
	}
	
	public void setJobChainConfigurationListener(JobChainConfigurationListener confListener_) {
		confListener = confListener_;		
	}
	
	private boolean isState() {
		if(tree != null && tree.getSelectionCount() > 0) {			
			if(tree.getSelection()[0].equals(tree.getItem(0))) {
				MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("detailform.chose_state"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );				
				return false;
			}			
		}
		return true;
	}
	
	private void deleteState() {
		if (!isState())
			return;
		int count = MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("detailform.remove_state"), SWT.ICON_QUESTION | SWT.YES | SWT.NO |SWT.CANCEL );
		if(count == SWT.YES) {
			detailListener.deleteState(txtState.getText());
			confListener.deleteState(txtState.getText(), tree.getTopItem());					
			//tree.setSelection(new TreeItem[] {tree.getItems()[0]});
			//tree.setSelection(new TreeItem[] {tree.getTopItem()});
			//confListener.treeFillState(tree.getTopItem());
			
			//if(gui!=null)
			//	gui.updateState(txtState.getText());		        	
			//confListener.treeSelection(tree, parent);
		}
	}
	
	public boolean hasErrors() {
		if(detailListener!= null) {
			return detailListener.hasError();
		}
		return false;
	}
	
	private void changeParameNote() {
		if(detailListener == null)
			return;
		
		//Wert auf leer zurücksetzen
		if(txtParamNote.getText()!= null && txtParamNote.getText().length()==0)
			return;
		
		if(txtName.getText() != null && txtName.getText().length() == 0) {
			MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("tooltip.detail.param.missing_param_name_for_note"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );					
			return;	
		}
		
		if(  tableParams.getSelectionCount() == 0  ||
				( tableParams.getSelectionCount() > 0 && !txtParamNote.getText().equalsIgnoreCase(detailListener.getParamNote(tableParams.getSelection()[0].getText(0), comboLanguage.getText())))){
			
			isEditableParam=true;
			butApplyParam.setEnabled(isEditableParam);
			isEditable = true;
			butApply.setEnabled(isEditable);
			if(gui!=null)
				gui.updateParamNote();
		}
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
		txtJobChainname.setToolTipText(Messages.getTooltip("detail.new_jobchain_name"));		
		butNewState.setToolTipText(Messages.getTooltip("detail.new_state"));
		txtState.setToolTipText(Messages.getTooltip("detail.state"));
		butRemoveState.setToolTipText(Messages.getTooltip("detail.remove_state"));
		butXML.setToolTipText(Messages.getTooltip("detail.xml_configuration"));
		butDocumentation.setToolTipText(Messages.getTooltip("detail.open_documentation"));
	}
} 
