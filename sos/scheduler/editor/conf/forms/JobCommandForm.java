package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;
import sos.scheduler.editor.app.Editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobCommandListener;

public class JobCommandForm extends Composite implements IUnsaved, IUpdateLanguage {

	private JobCommandListener listener;

	private Group              jobsAndOrdersGroup           = null;

	private SashForm           sashForm        = null;

	private Group              gDescription    = null;
	//private Composite              gDescription    = null;

	private Label              lblJob         = null;

	private Text               tTitle          = null;

	private Text               tState          = null;

	private Text               tStartAt        = null;

	private Text               tPriority       = null;

	private Combo              cJobchain       = null;

	//private Button             bOrder          = null;

	//private Button             bJob            = null;

	private Button             bReplace        = null;

	private Text               tJob            = null;

	private boolean            updateTree      = false;

	private boolean            event           = false;

	//private Button             bApplyExitcode  = null;

	//private Button             bNew            = null;
	
	private int                 type           = -1;

	private Label               jobchainLabel  = null;

	Label priorityLabel = null;

	Label titleLabel = null;
	
	Label stateLabel = null;

	Label replaceLabel = null;


	public JobCommandForm(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main)
	throws JDOMException, TransformerException {
		super(parent, style);

		listener = new JobCommandListener(dom, command, main);
		if(command.getName().equalsIgnoreCase("start_job")) {
			type = Editor.JOB;
		} else {
			type = Editor.COMMANDS;
		}
		initialize();
		setToolTipText();
		
		//bApplyExitcode.setEnabled(false);
		event = true;



		if (command.getParentElement() != null ){        	
			this.jobsAndOrdersGroup.setEnabled(Utils.isElementEnabled("job", dom, command.getParentElement()));        	
		}

	}


	public void apply() {
		//if (isUnsaved())
			//addParam();
		//addCommand();
	}


	public boolean isUnsaved() {
		return false;
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		//setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.makeColumnsEqualWidth = true;
		gridLayout2.numColumns = 1;
		jobsAndOrdersGroup = new Group(this, SWT.NONE);
		jobsAndOrdersGroup.setText("Commands for Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
		jobsAndOrdersGroup.setLayout(gridLayout2);
		GridData gridData18 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
		sashForm = new SashForm(jobsAndOrdersGroup, SWT.NONE);
		//sashForm.setWeights(new int[] { 1 });
		sashForm.setOrientation(512);
		sashForm.setLayoutData(gridData18);
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gDescription = new Group(sashForm, SWT.NONE);
		gDescription.setText("Jobs and Orders");
		//gDescription =  new Composite(sashForm, SWT.NONE);
		//gDescription.setText("Jobs and orders");
		gDescription.setLayout(gridLayout3);
		/*bJob = new Button(gDescription, SWT.RADIO);
		final GridData gridData_1 = new GridData(SWT.DEFAULT, 41);
		bJob.setLayoutData(gridData_1);
		bJob.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				setCommandsEnabled(false);
				//group_1.setEnabled(true);
				//if (bJob.getSelection()) {
					fillCommand();
					/*tState.setText("");
					tPriority.setText("");
					cJobchain.setText("");
					tTitle.setText("");
					bReplace.setSelection(false);

					listener.setCommandAttribute(bApplyExitcode, "replace", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "title", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "at", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "job_chain", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "priority", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "state", "", tCommands);
					listener.setCommandAttribute(bApplyExitcode, "id", "", tCommands);
					
					//bApplyExitcode.setEnabled(true);                    
				//}
			}
		});
		bJob.setSelection(true);
		bJob.setText("Job");
		bJob.setSelection(true);*/
		/*bOrder = new Button(gDescription, SWT.RADIO);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.widthHint = 217;
		bOrder.setLayoutData(gridData_7);
		
		bOrder.addSelectionListener(new SelectionAdapter() {			
			public void widgetSelected(final SelectionEvent e) {
				setCommandsEnabled(true);
				fillCommand();
				//listener.fillEnvironment(tCommands, tableEnvironment);				

				if (bOrder.getSelection()) {
					bReplace.setSelection(true);
					//listener.setCommandAttribute(bApplyExitcode, "job", "", tCommands);                    
					bApplyExitcode.setEnabled(true);
				}

				listener.clearEnvironment();
				
				//listener.fillEnvironment(tCommands, tableEnvironment);
				
			}
		});
		bOrder.setText("Order");
		bOrder.setSelection(false);
		*/

		jobchainLabel = new Label(gDescription, SWT.NONE);
		final GridData gridData_10 = new GridData();
		jobchainLabel.setLayoutData(gridData_10);
		jobchainLabel.setText("Job chain");

		cJobchain = new Combo(gDescription, SWT.NONE);
		cJobchain.setEnabled(false);
		cJobchain.setItems(listener.getJobChains());		
		cJobchain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setJobChain(cJobchain.getText());
				
				//bApplyExitcode.setEnabled(true);
			}
		});

		final GridData gridData_8 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_8.widthHint = 114;
		cJobchain.setLayoutData(gridData_8);
		lblJob = new Label(gDescription, SWT.NONE);
		lblJob.setLayoutData(new GridData(73, SWT.DEFAULT));
		lblJob.setText("Job / Order ID");

		tJob = new Text(gDescription, SWT.BORDER);
		tJob.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(type == Editor.JOB){
					listener.setJob(tJob.getText());
				} else {
					listener.setOrderId(tJob.getText());
				}
				//bApplyExitcode.setEnabled(true);
			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_3.widthHint = 150;
		tJob.setLayoutData(gridData_3);
		final Label startAtLabel = new Label(gDescription, SWT.NONE);
		startAtLabel.setLayoutData(new GridData());
		startAtLabel.setText("Start at");

		tStartAt = new Text(gDescription, SWT.BORDER);
		tStartAt.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setAt(tStartAt.getText());
				//bApplyExitcode.setEnabled(true);
			}
		});		
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_4.widthHint = 150;
		tStartAt.setLayoutData(gridData_4);

		priorityLabel = new Label(gDescription, SWT.NONE);
		final GridData gridData_11 = new GridData();
		priorityLabel.setLayoutData(gridData_11);
		priorityLabel.setText("Priority");

		tPriority = new Text(gDescription, SWT.BORDER);
		tPriority.setEnabled(false);
		tPriority.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPriority(tPriority.getText());
				//bApplyExitcode.setEnabled(true);
			}
		});
		tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		titleLabel = new Label(gDescription, SWT.NONE);
		titleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		titleLabel.setText("Title");

		tTitle = new Text(gDescription, SWT.BORDER);
		tTitle.setEnabled(false);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
				//bApplyExitcode.setEnabled(true);
			}
		});

		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_5.widthHint = 150;
		tTitle.setLayoutData(gridData_5);

		stateLabel = new Label(gDescription, SWT.NONE);
		stateLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		stateLabel.setText("State");

		tState = new Text(gDescription, SWT.BORDER);
		tState.setEnabled(false);
		tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setState(tState.getText());
				//bApplyExitcode.setEnabled(true);
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.widthHint = 150;
		tState.setLayoutData(gridData_2);

		replaceLabel = new Label(gDescription, SWT.NONE);
		final GridData gridData_12 = new GridData();
		replaceLabel.setLayoutData(gridData_12);
		replaceLabel.setText("Replace");

		bReplace = new Button(gDescription, SWT.CHECK);
		bReplace.setSelection(true);
		bReplace.setEnabled(true);
		bReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setReplace(bReplace.getSelection() ? "yes" : "no");
				//bApplyExitcode.setEnabled(true);
			}
		});
		bReplace.setLayoutData(new GridData());
		//parameter
		//createJobCommandParameter();
		//environment
		//createEnvironment();
		/*
        final TabItem parameterTabItem = new TabItem(tabFolder, SWT.NONE);
        parameterTabItem.setText("Parameter");

        group = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group.setLayout(gridLayout);
        parameterTabItem.setControl(group);
        label2 = new Label(group, SWT.NONE);
        label2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        label2.setText("Name: ");
        tParaName = new Text(group, SWT.BORDER);
        final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_9.widthHint = 200;
        tParaName.setLayoutData(gridData_9);
        tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals("") && tCommands.getSelectionIndex() >= 0)
                    addParam();
            }
        });
        tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals("") && tCommands.getSelectionIndex() >= 0);
                if (tParaName.getText().equals("<from>")) {
                    cSource.setVisible(true);
                    tParaValue.setVisible(false);
                } else {
                    cSource.setVisible(false);
                    tParaValue.setVisible(true);
                }
            }
        });
        label6 = new Label(group, SWT.NONE);
        label6.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        label6.setText("Value: ");

        final Composite composite = new Composite(group, SWT.NONE);
        composite.addControlListener(new ControlAdapter() {
        	public void controlResized(final ControlEvent e) {
        		cSource.setBounds(0, 2, composite.getBounds().width, tParaName.getBounds().height);
                tParaValue.setBounds(0, 2,composite.getBounds().width, tParaName.getBounds().height);
        	}
        });
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        cSource = new Combo(composite, SWT.READ_ONLY);
        cSource.setItems(new String[] { "order", "task" });
        //cSource.setBounds(1, -1,330, 21);
        cSource.setBounds(0, 0,250, 21);


        cSource.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                tParaValue.setText(cSource.getText());
            }
        });
        cSource.setVisible(false);
        tParaValue = new Text(composite, SWT.BORDER);
        //tParaValue.setBounds(0, 0,269, 19);        
        tParaValue.setBounds(0, 0,250, 21);
        tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals("") && tCommands.getSelectionIndex() >= 0)
                    addParam();
            }
        });
        tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals("") && tCommands.getSelectionIndex() >= 0);
            }
        });
        bApply = new Button(group, SWT.NONE);
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        bApply.setLayoutData(gridData_5);
        bApply.setText("&Apply");
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addParam();
            }
        });
        tParameter = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData_3.heightHint = 140;
        tParameter.setLayoutData(gridData_3);
        tParameter.addPaintListener(new PaintListener() {
            public void paintControl(final PaintEvent e) {
            }
        });
        tParameter.setHeaderVisible(true);
        tParameter.setLinesVisible(true);
        tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                tParaName.setText(item.getText(0));
                if (tParaName.getText().equals("<from>"))
                    cSource.setText(item.getText(1));
                tParaValue.setText(item.getText(1));
                bRemove.setEnabled(tParameter.getSelectionCount() > 0);
                bApply.setEnabled(false);
            }
        });
        TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
        tcName.setWidth(252);
        tcName.setText("Name");
        TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
        tcValue.setWidth(249);
        tcValue.setText("Value");
        bRemove = new Button(group, SWT.NONE);
        bRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bRemove.setText("Remove");
        bRemove.setEnabled(false);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.deleteParameter(tParameter, tParameter.getSelectionIndex());
                tParaName.setText("");
                tParaValue.setText("");
                tParameter.deselectAll();
                bRemove.setEnabled(false);
                bApply.setEnabled(false);
            }
        });

        final Composite composite_1 = new Composite(group, SWT.NONE);
        composite_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        composite_1.setLayout(new GridLayout());

        final Button paramButton = new Button(composite_1, SWT.RADIO);
        paramButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false));
        paramButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("");
                tParaValue.setText("");
            }
        });
        paramButton.setSelection(true);
        paramButton.setText("Parameter");

        final Button fromTaskButton = new Button(composite_1, SWT.RADIO);
        fromTaskButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("<from>");
                cSource.setText("task");
            }
        });
        fromTaskButton.setText("from task");

        final Button fromOrderButton = new Button(composite_1, SWT.RADIO);
        final GridData gridData_2 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true);
        fromOrderButton.setLayoutData(gridData_2);
        fromOrderButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("<from>");
                cSource.setText("order");
            }
        });
        fromOrderButton.setText("from order");
		 */
		//environment
		
		/*
        final TabItem environmentTabItem = new TabItem(tabFolder, SWT.NONE);
        environmentTabItem.setText("Environment");

        group_1 = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        group_1.setLayout(gridLayout_1);
        environmentTabItem.setControl(group_1);


        label2_1 = new Label(group_1, SWT.NONE);
        label2_1.setLayoutData(new GridData());
        label2_1.setText("Name: ");

        txtEnvName = new Text(group_1, SWT.BORDER);
        txtEnvName.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butEnvApply.setEnabled(!txtEnvName.getText().equals("") && tCommands.getSelectionIndex() >= 0);                
        	}
        });
        txtEnvName.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEnvName.equals("") && tCommands.getSelectionIndex() >= 0)
                    addEnvironment();
        	}
        });
        final GridData gridData_8 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_8.widthHint = 200;
        txtEnvName.setLayoutData(gridData_8);

        label6_1 = new Label(group_1, SWT.NONE);
        label6_1.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label6_1.setText("Value: ");

        txtEnvValue = new Text(group_1, SWT.BORDER);
        txtEnvValue.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		butEnvApply.setEnabled(!txtEnvName.getText().equals("") && tCommands.getSelectionIndex() >= 0);
        	}
        });
        txtEnvValue.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		if (e.keyCode == SWT.CR && !txtEnvName.equals("") && tCommands.getSelectionIndex() >= 0)
                    addEnvironment();
        	}
        });
        final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_7.minimumHeight = 21;
        gridData_7.widthHint = 228;
        txtEnvValue.setLayoutData(gridData_7);

        butEnvApply = new Button(group_1, SWT.NONE);
        butEnvApply.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		 addEnvironment();
        	}
        });
        final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_6.widthHint = 0;
        butEnvApply.setLayoutData(gridData_6);
        butEnvApply.setEnabled(false);
        butEnvApply.setText("&Apply");

        tableEnvironment = new Table(group_1, SWT.FULL_SELECTION | SWT.BORDER);
        tableEnvironment.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                txtEnvName.setText(item.getText(0));

                txtEnvValue.setText(item.getText(1));
                butEnvRemove.setEnabled(tableEnvironment.getSelectionCount() > 0);
                butEnvApply.setEnabled(false);
        	}
        });
        tableEnvironment.setLinesVisible(true);
        tableEnvironment.setHeaderVisible(true);
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, false, true, 4, 2);
        gridData_4.widthHint = 545;
        tableEnvironment.setLayoutData(gridData_4);

        final TableColumn tcName_1 = new TableColumn(tableEnvironment, SWT.NONE);
        tcName_1.setWidth(252);
        tcName_1.setText("Name");

        final TableColumn tcValue_1 = new TableColumn(tableEnvironment, SWT.NONE);
        tcValue_1.setWidth(249);
        tcValue_1.setText("Value");

        butEnvRemove = new Button(group_1, SWT.NONE);
        butEnvRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		 listener.deleteEnvironment(tableEnvironment, tableEnvironment.getSelectionIndex());
                 txtEnvName.setText("");
                 txtEnvValue.setText("");
                 tableEnvironment.deselectAll();
                 butEnvApply.setEnabled(false);
                 butEnvRemove.setEnabled(false);
        	}
        });
        butEnvRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butEnvRemove.setEnabled(false);
        butEnvRemove.setText("Remove");
		 */
		createSashForm();
	}



	/**
	 * This method initializes group1
	 */
	/*private void createGroup1() {

		createCombo();
		createComposite();
	}*/


	/**
	 * This method initializes combo
	 */
/*	private void createCombo() {
	}
*/

	/**
	 * This method initializes composite
	 */
/*	private void createComposite() {
	}
*/

	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {

		//createGroup1();
		createGroup2();
		//createGroup3();
	}


	/**
	 * This method initializes group2
	 */
	private void createGroup2() {

		/*bApplyExitcode = new Button(gDescription, SWT.NONE);
		bApplyExitcode.setEnabled(false);
		final GridData gridData = new GridData(GridData.FILL, GridData.END, false, false, 1, 2);
		gridData.widthHint = 73;
		bApplyExitcode.setLayoutData(gridData);
		bApplyExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addCommand();
				gMain.setEnabled(tCommands.getItemCount() > 0);
				//group_2.setEnabled(tCommands.getSelectionCount() == 1);                
				tabFolder.setSelection(0);
				bApplyExitcode.setEnabled(false);
				
			}
		});
		bApplyExitcode.setText("Apply");
		bApplyExitcode.setEnabled(false);
		*/
/*
		bNew = new Button(gDescription, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_6.widthHint = 62;
		bNew.setLayoutData(gridData_6);
		bNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				setCommandsEnabled(bOrder.getSelection());
				tCommands.setSelection(-1);
				tParameter.removeAll();
				tableEnvironment.removeAll();                
				tableEnvironment.clearAll();
				listener.clearParams();
				listener.clearEnvironment();
				clearFields();

				bRemove.setEnabled(false);
				tJob.setFocus();
				
			}
		});
		bNew.setText("New");
*/
		//group_2.setEnabled(tCommands.getSelectionCount() == 1);
		
		if(type == Editor.JOB){
			setCommandsEnabled(false);			
		} else {			
			setCommandsEnabled(true);
		}
		clearFields();
		fillCommand();
		

	}


	/*private void addCommand() {
		String msg = "";
		if (cJobchain.getText().trim().equals("") && bOrder.getSelection()) {
			msg = "A jobchain must be given for an order";
			cJobchain.setFocus();
		} else {
			if (tJob.getText().trim().equals("") && bJob.getSelection()) {
				msg = "A jobname must be given for a job";
				tJob.setFocus();
			}
		}
		if (!msg.equals("")) {
			MainWindow.message(msg, SWT.ICON_INFORMATION);
		} else {

			Element e = null;
			int index = tCommands.getSelectionIndex();
			int index = -1;
			if (index == -1) {
				if (bJob.getSelection()) {
					e = new Element("start_job");
					e.setAttribute("at", tStartAt.getText());
					e.setAttribute("job", tJob.getText());
					TableItem item = new TableItem(tCommands, SWT.NONE);
					item.setText(new String[] { "start_job", tJob.getText(), "", tStartAt.getText() });
				} else {
					//e = new Element("add_order");
					e = new Element("order");
					e.setAttribute("at", tStartAt.getText());
					e.setAttribute("id", tJob.getText());
					e.setAttribute("priority", tPriority.getText());
					if (bReplace.getSelection()) {
						e.setAttribute("replace", "yes");
					} else {
						e.setAttribute("replace", "no");
					}
					e.setAttribute("state", tState.getText());
					e.setAttribute("job_chain", cJobchain.getText());
					e.setAttribute("title", tTitle.getText());

					TableItem item = new TableItem(tCommands, SWT.NONE);
					//item.setText(new String[] { "add_order", tJob.getText(), cJobchain.getText(), tStartAt.getText() });//mo
					item.setText(new String[] { "order", tJob.getText(), cJobchain.getText(), tStartAt.getText() });
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				}

				listener.addCommand(e);
				tCommands.setSelection(tCommands.getItemCount() - 1);
				listener.fillParams(tCommands, tParameter);
				if(bOrder.getSelection()) {
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				} else                
					listener.fillEnvironment(tCommands, tableEnvironment);


			} else {

				String cmd = tCommands.getItem(index).getText();
				if (cmd.equals("add_order") && bJob.getSelection() && tCommands.getSelectionIndex() >= 0) {
					listener.setCommandName(bApplyExitcode, "start_job", tJob.getText(), tCommands);
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "start_job");
				}

				if (cmd.equals("order") && bJob.getSelection() && tCommands.getSelectionIndex() >= 0) {
					listener.setCommandName(bApplyExitcode, "start_job", tJob.getText(), tCommands);
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "start_job");
				}

				if (cmd.equals("start_job") && bOrder.getSelection() && tCommands.getSelectionIndex() >= 0) {
					//listener.setCommandName(bNew, "add_order", tJob.getText(), tCommands);//mo
					listener.setCommandName(bNew, "order", tJob.getText(), tCommands);
					//tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "add_order"); //mo
					tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "order");
					tCommands.getItem(tCommands.getSelectionIndex()).setText(2, "");
					tCommands.getItem(tCommands.getSelectionIndex()).setText(3, tStartAt.getText());
					listener.clearEnvironment();
					tableEnvironment.removeAll();
					tableEnvironment.clearAll();
				}

				if (bJob.getSelection()) {
					listener.setCommandAttribute(bApplyExitcode, "job", tJob.getText(), tCommands);
				} else {
					listener.setCommandAttribute(bApplyExitcode, "id", tJob.getText(), tCommands);
					if (bReplace.getSelection()) {
						listener.setCommandAttribute(bApplyExitcode, "replace", "yes", tCommands);
					} else {
						listener.setCommandAttribute(bApplyExitcode, "replace", "no", tCommands);
					}
				}
				listener.setCommandAttribute(bApplyExitcode, "state", tState.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "title", tTitle.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "priority", tPriority.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "at", tStartAt.getText(), tCommands);
				listener.setCommandAttribute(bApplyExitcode, "job_chain", cJobchain.getText(), tCommands);
				tCommands.getItem(index).setText(1, tJob.getText());
				tCommands.getItem(index).setText(3, tStartAt.getText());
				if (bOrder.getSelection()) {
					tCommands.getItem(index).setText(2, cJobchain.getText());
				} else {
					tCommands.getItem(index).setText(2, "");
				}
			}

			bApplyExitcode.setEnabled(false);
		}
	}
*/

	private void clearFields() {
		/*tState.setText("");
		tPriority.setText("");
		cJobchain.setText("");
		tTitle.setText("");
		tJob.setText("");
		tStartAt.setText("");
		*/
		if(type == Editor.JOB){
			
			tState.setVisible(false);
			tPriority.setVisible(false);
			cJobchain.setVisible(false);
			tTitle.setVisible(false);
			bReplace.setVisible(false);
			jobchainLabel.setVisible(false);
			priorityLabel.setVisible(false);
			titleLabel.setVisible(false);			
			stateLabel.setVisible(false);
			replaceLabel.setVisible(false);
			lblJob.setText("Job");
		} else {
			lblJob.setText("Order Id");			
		}
		tJob.setVisible(true);
		tStartAt.setVisible(true);
		
		//bReplace.setSelection(bOrder.getSelection());
		//txtEnvName.setText("");
		//txtEnvValue.setText("");
	}


	private void setCommandsEnabled(boolean b) {
		tState.setEnabled(b);
		tPriority.setEnabled(b);
		cJobchain.setEnabled(b);
		tTitle.setEnabled(b);
		bReplace.setEnabled(b);
	}


	public Element getCommand() {
		return listener.getCommand();
	}


	public void fillCommand() {
		//clearFields();
		if (listener.getCommand() != null) {
			//cExitcode.setText(listener.getExitCode());
			tStartAt.setText(Utils.getAttributeValue("at", listener.getCommand()));
			if (type == Editor.COMMANDS) {
				
				tJob.setText(Utils.getAttributeValue("id", listener.getCommand()));
				tTitle.setText(Utils.getAttributeValue("title", listener.getCommand()));
				tState.setText(Utils.getAttributeValue("state", listener.getCommand()));				
				cJobchain.setText(Utils.getAttributeValue("job_chain", listener.getCommand()));
				tPriority.setText(Utils.getAttributeValue("priority", listener.getCommand()));
				bReplace.setSelection(Utils.getAttributeValue("replace", listener.getCommand()).equals("yes"));
			} else {
				tJob.setText(Utils.getAttributeValue("job", listener.getCommand()));
			}

		}
	}


	public void setToolTipText() {
		tStartAt.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		tTitle.setToolTipText(Messages.getTooltip("jobcommand.title"));
		tPriority.setToolTipText(Messages.getTooltip("jobcommand.priority"));
		tState.setToolTipText(Messages.getTooltip("jobcommand.state"));		
		bReplace.setToolTipText(Messages.getTooltip("jobcommand.replaceorder"));
		cJobchain.setToolTipText(Messages.getTooltip("jobcommand.jobchain"));
		tJob.setToolTipText(Messages.getTooltip("jobcommand.job_order_id"));
		/*tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
		tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
		bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
		bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		tParameter.setToolTipText(Messages.getTooltip("jobcommand.param.table"));
*/
	}

	public void createJobCommandParameter() {
		//cSource.setBounds(1, -1,330, 21);
		//tParaValue.setBounds(0, 0,269, 19);        
	}

	private void  createEnvironment() {
	}
} // @jve:decl-index=0:visual-constraint="10,10"
