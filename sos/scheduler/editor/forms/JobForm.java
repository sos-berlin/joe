package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.listeners.JobListener;

public class JobForm extends Composite implements IUnsaved, IUpdateLanguage {
	private Combo sPriority;
	private Text sIdleTimeout;
	private Text sTimeout;
	private Text sTasks;
	private JobListener listener;

	private Group group = null;

	private Group gMain = null;

	private Label label = null;

	private Text tName = null;

	private Label label1 = null;

	private Label label3 = null;

	private Label label7 = null;

	private Label label9 = null;

	private Label label11 = null;

	private Label label13 = null;

	private Label label15 = null;

	private Label label17 = null;

	private Text tTitle = null;

	private Text tSpoolerID = null;

	private Combo cProcessClass = null;





	private Composite cOrder = null;

	private Button bOrderYes = null;

	private Button bOrderNo = null;

	private SashForm sashForm = null;

	private Group gJobParameter = null;

	private Table tParameter = null;

	private Button bRemove = null;

	private Label label2 = null;

	private Text tParaName = null;

	private Label label6 = null;

	private Text tParaValue = null;

	private Button bApply = null;

	private Group gDescription = null;

	private Text tFileName = null;

	private Text tDescription = null;

	private Label label4 = null;

	private Label label10 = null;

	private Text tComment = null;

	private Label label8 = null;
	private boolean updateTree=false;

	public JobForm(Composite parent, int style, DomParser dom, Element job,
			IUpdate main) {
		super(parent, style);
		listener = new JobListener(dom, job, main);
		initialize();
		setToolTipText();		
		sashForm.setWeights(new int[] {40, 30, 30});

		dom.setInit(true);
		
		updateTree = false;
		tName.setText(listener.getName());
		updateTree = true;
		tTitle.setText(listener.getTitle());
		tSpoolerID.setText(listener.getSpoolerID());
		String[] classes = listener.getProcessClasses();
		if (classes == null)
			cProcessClass.setEnabled(false);
		else
			cProcessClass.setItems(listener.getProcessClasses());
		int index = cProcessClass.indexOf(listener.getProcessClass());
		if (index >= 0)
			cProcessClass.select(index);
		
		bOrderYes.setSelection(listener.getOrder());
		bOrderNo.setSelection(!listener.getOrder());
		index = sPriority.indexOf(listener.getPriority());
		if (index >= 0)
				sPriority.select(index);
		else {
				int p = Utils.str2int(listener.getPriority(),20);
				if (p == -999) {
					sPriority.setText("");
				}else {
  				if (p < -20) {
	  				p = -20;
		  		}
			  	  sPriority.setText(String.valueOf(p));
			 	}
			}
		
		sTasks.setText(listener.getTasks());
		sTimeout.setText(listener.getTimeout());
		sIdleTimeout.setText(listener.getIdleTimeout());
		listener.fillParams(tParameter);
		tFileName.setText(listener.getInclude());
		tDescription.setText(listener.getDescription());
		tComment.setText(listener.getComment());
		
		dom.setInit(false);
	}

	public void apply() {
		if(isUnsaved())
			addParam();
	}

	public boolean isUnsaved() {
		return bApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(723,566));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(this, SWT.NONE);
		group.setText("Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
		group.setLayout(gridLayout2);
		createSashForm();
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createGroup1() {
		GridData gridData71 = new org.eclipse.swt.layout.GridData();
		gridData71.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData71.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData61 = new org.eclipse.swt.layout.GridData();
		gridData61.horizontalSpan = 3;
		gridData61.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData61.grabExcessVerticalSpace = true;
		gridData61.grabExcessHorizontalSpace = true;
		gridData61.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gMain = new Group(sashForm, SWT.NONE);
		gMain.setText("Main Options");
		gMain.setLayout(gridLayout);
		label = new Label(gMain, SWT.NONE);
		label.setText("Job Name:");
		tName = new Text(gMain, SWT.BORDER);
		tName.setLayoutData(gridData);
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setName(tName.getText(),updateTree);
				group.setText("Job: " + tName.getText() + (listener.isDisabled() ? " (Disabled)" : ""));
			
			}
		});
		label1 = new Label(gMain, SWT.NONE);
		label1.setText("Job Title:");
		tTitle = new Text(gMain, SWT.BORDER);
		tTitle.setLayoutData(gridData1);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
			}
		});
		label3 = new Label(gMain, SWT.NONE);
		label3.setText("Scheduler ID:");
		tSpoolerID = new Text(gMain, SWT.BORDER);
		tSpoolerID.setLayoutData(gridData3);
		tSpoolerID
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setSpoolerID(tSpoolerID.getText());
					}
				});
		label9 = new Label(gMain, SWT.NONE);
		label9.setText("Process Class:");
		createCombo();
		label7 = new Label(gMain, SWT.NONE);
		label7.setText("On Order:");
		createComposite();
		label17 = new Label(gMain, SWT.NONE);
		label17.setText("Priority:");

		sPriority = new Combo(gMain, SWT.NONE);
		sPriority.setItems(new String[] {"idle", "below_normal", "normal", "above_normal", "high"});
		sPriority.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = (Utils.isOnlyDigits(e.text) 
						|| e.text.equals("idle")
						|| e.text.equals("below_normal")
						|| e.text.equals("normal")
						|| e.text.equals("above_normal")
						|| e.text.equals("high"));
				
			}
		});
		final GridData gridData_1 = new GridData(80, SWT.DEFAULT);
		gridData_1.verticalIndent = -1;
		sPriority.setLayoutData(gridData_1);
		sPriority.addModifyListener(new ModifyListener() {
			
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(-20,20,sPriority);
				listener.setPriority(sPriority.getText());
			}
		});
		
		label15 = new Label(gMain, SWT.NONE);
		label15.setText("Tasks:");

		sTasks = new Text(gMain, SWT.BORDER);
		sTasks.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sTasks.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
		sTasks.addModifyListener(new ModifyListener() {
			
			public void modifyText(final ModifyEvent e) {
				listener.setTasks(sTasks.getText());
			}
		});
		
		sTasks.setLayoutData(new GridData(75, SWT.DEFAULT));
		label13 = new Label(gMain, SWT.NONE);
		label13.setText("Timeout:");

		sTimeout = new Text(gMain, SWT.BORDER);
		sTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		
		sTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setTimeout(sTimeout.getText());
			}
		});
		sTimeout.setLayoutData(new GridData(75, SWT.DEFAULT));
		label11 = new Label(gMain, SWT.NONE);
		label11.setText("Idle Timeout:");

		sIdleTimeout = new Text(gMain, SWT.BORDER);
		sIdleTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);

			}
		});
		sIdleTimeout.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setIdleTimeout(sIdleTimeout.getText());
			}
		});
		
		sIdleTimeout.setLayoutData(new GridData(75, SWT.DEFAULT));
		label8 = new Label(gMain, SWT.NONE);
		label8.setText("Comment:");
		label8.setLayoutData(gridData71);
		tComment = new Text(gMain, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tComment.setLayoutData(gridData61);
		tComment.setFont(SWTResourceManager.getFont("Courier New", 8, SWT.NONE));
		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setComment(tComment.getText());
			}
		});
	}

	/**
	 * This method initializes combo
	 * 
	 */
	private void createCombo() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		cProcessClass = new Combo(gMain, SWT.NONE);
		cProcessClass.setLayoutData(gridData4);
		cProcessClass
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.setProcessClass(cProcessClass.getText());
					}
				});
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData15 = new org.eclipse.swt.layout.GridData();
		gridData15.horizontalSpan = 3;
		cOrder = new Composite(gMain, SWT.NONE);
		cOrder.setLayout(new RowLayout());
		cOrder.setLayoutData(gridData15);
		bOrderYes = new Button(cOrder, SWT.RADIO);
		bOrderYes.setText("Yes");
		bOrderYes
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						
					//	sPriority.setSelection(0);
						listener.setOrder(bOrderYes.getSelection());
					}
				});
		bOrderNo = new Button(cOrder, SWT.RADIO);
		bOrderNo.setText("No");
		bOrderNo.setEnabled(true);
		bOrderNo.setSelection(false);
		bOrderNo
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						
						listener.setPriority(sPriority.getText());
						listener.setOrder(!bOrderNo.getSelection());
					}
				});
	}

	/**
	 * This method initializes sashForm
	 * 
	 */
	private void createSashForm() {
		GridData gridData18 = new org.eclipse.swt.layout.GridData();
		gridData18.horizontalSpan = 1;
		gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData18.grabExcessHorizontalSpace = true;
		gridData18.grabExcessVerticalSpace = true;
		gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sashForm = new SashForm(group, SWT.NONE);
		sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
		sashForm.setLayoutData(gridData18);
		createGroup1();
		createGroup2();
		createGroup3();
	}

	/**
	 * This method initializes group2
	 * 
	 */
	private void createGroup2() {
		GridData gridData17 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		GridData gridData16 = new org.eclipse.swt.layout.GridData();
		gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData16.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData13 = new org.eclipse.swt.layout.GridData();
		gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData11 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData11.horizontalIndent = 32;
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
		gJobParameter = new Group(sashForm, SWT.NONE);
		gJobParameter.setText("Job Parameter");
		gJobParameter.setLayout(gridLayout1);
		label2 = new Label(gJobParameter, SWT.NONE);
		final GridData gridData = new GridData();
		label2.setLayoutData(gridData);
		label2.setText("Name:");
		tParaName = new Text(gJobParameter, SWT.BORDER);
		tParaName.setLayoutData(gridData11);
		label6 = new Label(gJobParameter, SWT.NONE);
		label6.setText("Value:");
		tParaValue = new Text(gJobParameter, SWT.BORDER);
		tParaValue.setLayoutData(gridData13);
		bApply = new Button(gJobParameter, SWT.NONE);
		label4 = new Label(gJobParameter, SWT.SEPARATOR | SWT.HORIZONTAL);
		label4.setText("Label");
		label4.setLayoutData(gridData17);
		createTable();
		bRemove = new Button(gJobParameter, SWT.NONE);
		bRemove.setText("Remove");
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData10);
		bRemove
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.deleteParameter(tParameter, tParameter
								.getSelectionIndex());
						tParaName.setText("");
						tParaValue.setText("");
						tParameter.deselectAll();
						bRemove.setEnabled(false);
						bApply.setEnabled(false);
					}
				});
		tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if(e.keyCode == SWT.CR && !tParaName.equals(""))
					addParam();
			}
		});
		tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals(""));
			}
		});
		tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if(e.keyCode == SWT.CR && !tParaName.equals(""))
					addParam();
			}
		});
		tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals(""));
			}
		});
		bApply.setText("&Apply");
		bApply.setLayoutData(gridData16);
		bApply.setEnabled(false);
		bApply
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						addParam();
					}
				});
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable() {
		GridData gridData9 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
		gridData9.horizontalIndent = 32;
		new Label(gJobParameter, SWT.NONE);
		tParameter = new Table(gJobParameter, SWT.BORDER | SWT.FULL_SELECTION);
		tParameter.setHeaderVisible(true);
		tParameter.setLinesVisible(true);
		tParameter.setLayoutData(gridData9);
		tParameter
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						TableItem item = (TableItem) e.item;
						if (item == null)
							return;
						tParaName.setText(item.getText(0));
						tParaValue.setText(item.getText(1));
						bRemove.setEnabled(tParameter.getSelectionCount() > 0);
						bApply.setEnabled(false);
					}
				});
		TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
		tcName.setWidth(150);
		tcName.setText("Name");
		TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
		tcValue.setWidth(450);
		tcValue.setText("Value");
	}

	/**
	 * This method initializes group3
	 * 
	 */
	private void createGroup3() {
		GridData gridData14 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
		gridData14.horizontalIndent = 24;
		GridData gridData12 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData12.horizontalIndent = 24;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gDescription = new Group(sashForm, SWT.NONE);
		gDescription.setText("Job Description");
		gDescription.setLayout(gridLayout3);
		label10 = new Label(gDescription, SWT.NONE);
		label10.setText("Include:");
		tFileName = new Text(gDescription, SWT.BORDER);
		tFileName.setLayoutData(gridData12);
	 
		tFileName
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setInclude(tFileName.getText());
					}
				});
		new Label(gDescription, SWT.NONE);
		tDescription = new Text(gDescription, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tDescription.setFont(SWTResourceManager.getFont("", 10, SWT.NONE));
		tDescription.setLayoutData(gridData14);
		tDescription
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setDescription(tDescription.getText());
					}
				});
	}
	
	private void addParam() {
		listener.saveParameter(tParameter, tParaName.getText(),
				tParaValue.getText());
		tParaName.setText("");
		tParaValue.setText("");
		bRemove.setEnabled(false);
		bApply.setEnabled(false);
		tParameter.deselectAll();
		tParaName.setFocus();
	}

	public void setToolTipText(){
		tName.setToolTipText(Messages.getTooltip("job.name"));
		tTitle.setToolTipText(Messages.getTooltip("job.title"));
		tSpoolerID.setToolTipText(Messages.getTooltip("job.spooler_id"));
		sPriority.setToolTipText(Messages.getTooltip("job.priority"));
		sTasks.setToolTipText(Messages.getTooltip("job.tasks"));
		sTimeout.setToolTipText(Messages.getTooltip("job.timeout"));
		sIdleTimeout.setToolTipText(Messages.getTooltip("job.idle_timeout"));
		tComment.setToolTipText(Messages.getTooltip("job.comment"));
		cProcessClass.setToolTipText(Messages.getTooltip("job.process_class"));
		bOrderYes.setToolTipText(Messages.getTooltip("job.btn_order_yes"));
		bOrderNo.setToolTipText(Messages.getTooltip("job.btn_order_no"));
		tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
		tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
		bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
		bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		tParameter.setToolTipText(Messages.getTooltip("job.param.table"));
		tFileName.setToolTipText(Messages
				.getTooltip("job.description.filename"));
		tDescription.setToolTipText(Messages.getTooltip("job.description"));

	 
  }	
} // @jve:decl-index=0:visual-constraint="10,10"
