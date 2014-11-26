package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.app.ContextMenu;


public class JobMainForm extends Composite implements IUpdateLanguage {
			
	
	private JobListener listener          = null;
	
	private Group       group             = null;
	
	private Group       gMain             = null;
	
	private Label       label             = null;
	
	private Text        tName             = null;
	
	private Label       label1            = null;
		
	private Label       label7            = null;
	
	private Label       label9            = null;
	
	private Combo       tTitle            = null;
	
	private Combo       cProcessClass     = null;
	
	private Composite   cOrder            = null;
	
	private Button      bOrderYes         = null;
	
	private Button      bOrderNo          = null;	
	
	private boolean     updateTree        = false;
	
	private Button      bStopOnError      = null;
	
	private Button      butBrowse         = null;
	
	private boolean     init              = true;
	
	private Button      butGoto           = null;

	private int _style = 0;
	
	private ExecuteForm executeForm = null;
	
	
	public JobMainForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
		super(parent, style);
		init = true;
		_style = style;
		
		dom.setInit(true);
		
		this.setEnabled(Utils.isElementEnabled("job", dom, job));
		
		listener = new JobListener(dom, job, main);
		
		initialize();   
		setToolTipText();
		
		updateTree = false;
		
		initForm();
		
		dom.setInit(false);
		tName.setFocus();
		init = false;
	}
	
	
	public void apply() {
		//if (isUnsaved())
			//addParam();
	}
	
	
	public boolean isUnsaved() {
		//return bApply.isEnabled();
		return false;
	}
	
	
	private void initialize() {
		//sosString = new SOSString(); 
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(723, 566));
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(this, SWT.NONE);		
		group.setText("Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
		group.setLayout(gridLayout2);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 2;
		gridLayout.numColumns = 6;
		gMain = new Group(group, SWT.NONE);
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gMain.setLayoutData(gridData_12);
		gMain.setText("Main Options");
		gMain.setLayout(gridLayout);
		GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false);
		
		label = new Label(gMain, SWT.NONE);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label.setText("Job Name:");
		
		tName = new Text(gMain, SWT.BORDER);
		
		tName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				//tName.selectAll();
			}
		});
		tName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {									
				if(!init)//während der initialiserung sollen keine überprüfungen stattfinden
					e.doit = Utils.checkElement(listener.getName(), listener.get_dom(), Editor.JOB, null);								
			}
		});
		tName.setLayoutData(gridData);
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(init) return;
				
				checkName();
				listener.setName(tName.getText(), updateTree);
				group.setText("Job: " + tName.getText() + (listener.isDisabled() ? " (Disabled)" : ""));

			}

		});
		label7 = new Label(gMain, SWT.NONE);
		label7.setLayoutData(new GridData());
		label7.setText("Order Job:");
		GridData gridData15 = new GridData();
		cOrder = new Composite(gMain, SWT.NONE);
		cOrder.setLayout(new RowLayout());
		cOrder.setLayoutData(gridData15);
		bOrderYes = new Button(cOrder, SWT.RADIO);
		
		bOrderYes.setText("Yes");
		bOrderYes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(init) return;																					
				
				if(!bOrderYes.getSelection())
					return;
				
				listener.setOrder(bOrderYes.getSelection());
				
				boolean _deleteRuntimeAttribute = false;
				
				
				Element job = listener.getJob();
				if(listener.getOrder() && job != null && job.getChild("run_time") != null) {
					if(sos.scheduler.editor.app.Utils.getAttributeValue("single_start", job.getChild("run_time")).length() > 0 ||
							sos.scheduler.editor.app.Utils.getAttributeValue("let_run", job.getChild("run_time")).length() > 0||
							sos.scheduler.editor.app.Utils.getAttributeValue("once", job.getChild("run_time")).length() > 0) {




						if(isVisible()) {					
							_deleteRuntimeAttribute = Utils.checkElement(listener.getName(), listener.get_dom(), Editor.JOB, "change_order");
						}

						if(_deleteRuntimeAttribute) {

							listener.getJob().getChild("run_time").removeAttribute("single_start");
							listener.getJob().getChild("run_time").removeAttribute("let_run");
							listener.getJob().getChild("run_time").removeAttribute("once");					
						}
					}
				}
				
			}
		});
		bOrderNo = new Button(cOrder, SWT.RADIO);
		
		bOrderNo.setText("No");
		bOrderNo.setEnabled(true);
		bOrderNo.setSelection(false);
		bOrderNo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(init) return;
				//listener.setPriority(sPriority.getText());
				
				if(listener.getOrder() && !Utils.checkElement(listener.getName(), listener.get_dom(), Editor.JOB, "change_order")){
					//e.doit = false;
					init = true;
					bOrderNo.setSelection(false);					
					bOrderYes.setSelection(true);
					init = false;
					return;
				}			
				
				listener.setOrder(!bOrderNo.getSelection());
			}
		});
		new Label(gMain, SWT.NONE);
		label1 = new Label(gMain, SWT.NONE);
		label1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label1.setText("Job Title:");
		GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		tTitle = new Combo(gMain, SWT.BORDER);
		tTitle.setLayoutData(gridData1);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(init) return;
				listener.setTitle(tTitle.getText());
			}
		});
		
		
		tTitle.setItems(Options.getJobTitleList());
		
		new Label(gMain, SWT.NONE);
		label9 = new Label(gMain, SWT.NONE);
		label9.setLayoutData(new GridData());
		label9.setText("Process Class:");		
		GridData gridData4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
	 
		butGoto = new Button(gMain, SWT.ARROW | SWT.DOWN);
		butGoto.setLayoutData(new GridData());
		butGoto.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
		butGoto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(cProcessClass.getText().length() > 0)
					ContextMenu.goTo(cProcessClass.getText(), listener.get_dom(), Editor.PROCESS_CLASSES);
			}
		});
		butGoto.setAlignment(SWT.RIGHT);
		
		cProcessClass = new Combo(gMain, SWT.NONE);
		cProcessClass.setMenu(new ContextMenu(cProcessClass, listener.get_dom(), Editor.PROCESS_CLASSES).getMenu());
		
		cProcessClass.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				
				if(init) return;
				
				listener.setProcessClass(cProcessClass.getText());
			}
		});
		cProcessClass.setLayoutData(gridData4);
		cProcessClass.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				if(init) return;
				listener.setProcessClass(cProcessClass.getText());
			}
		});
		
		butBrowse = new Button(gMain, SWT.NONE);
		butBrowse.setLayoutData(new GridData());
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
				if(name != null && name.length() > 0)
					cProcessClass.setText(name);
			}
		});
		butBrowse.setText("Browse");
		
		final Label stop_on_errorLabel = new Label(gMain, SWT.NONE);
		stop_on_errorLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		stop_on_errorLabel.setText("Stop On Error");
		
		bStopOnError = new Button(gMain, SWT.CHECK);
		bStopOnError.setLayoutData(new GridData());
		//gridData_16.widthHint = 17;
		bStopOnError.setSelection(true);
		bStopOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(init) return;
				listener.setStopOnError(bStopOnError.getSelection());
			}
		});

		if(executeForm == null) {
			executeForm = new ExecuteForm(group, _style, listener.get_dom(), listener.getJob(), listener.get_main());
			executeForm.setLayout(new FillLayout());
			executeForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		}
	}
	
	

	public void initForm(){
		tName.setText(listener.getName());
		updateTree = true;
		tTitle.setText(listener.getTitle());
		
		String process_class = "";
		if(listener.getProcessClass() != null && listener.getProcessClass().length() > 0)
			process_class= listener.getProcessClass();
		
		String[] classes = listener.getProcessClasses();
		if(classes != null)
			cProcessClass.setItems(classes);
		
		cProcessClass.setText(process_class);
							
		bOrderYes.setSelection(listener.getOrder());
		bOrderNo.setSelection(!listener.getOrder());
		bStopOnError.setSelection(listener.getStopOnError());
		
		
	}
			
	private void checkName(){
	  if(Utils.existName(tName.getText(), listener.getJob(), "job")) {		  
		  tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
	  } else {
		  tName.setBackground(null);
	  }
	}
	
	public void setToolTipText() {
		tName.setToolTipText(Messages.getTooltip("job.name"));
		tTitle.setToolTipText(Messages.getTooltip("job.title"));
		bStopOnError.setToolTipText(Messages.getTooltip("job.stop_on_error"));
		cProcessClass.setToolTipText(Messages.getTooltip("job.process_class"));
		bOrderYes.setToolTipText(Messages.getTooltip("job.btn_order_yes"));
		bOrderNo.setToolTipText(Messages.getTooltip("job.btn_order_no"));		
		butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));

		butGoto.setToolTipText(Messages.getTooltip("goto"));
	}
	
	
	
} // @jve:decl-index=0:visual-constraint="10,10"
