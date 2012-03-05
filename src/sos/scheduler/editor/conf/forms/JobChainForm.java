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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import org.eclipse.swt.widgets.Spinner;

public class JobChainForm extends Composite implements IUnsaved, IUpdateLanguage {


	private JobChainListener    listener                    = null;

	private Group               jobChainGroup               = null;

	private Label               chainNameLabel              = null;

	private Text                tName                       = null;
 
	private Button              bRecoverable                = null;

	private Button              bVisible                    = null;

    private Button              butDetails                  = null;

	private ISchedulerUpdate    update                      = null;

	private Button              butDistributed              = null; 

	private Text                txtTitle                    = null; 
	
	private boolean             init                        = false;
	
	private boolean             changeJobChainName          = true;
	private Spinner             sMaxorder                   = null;
	private Text sMaxorders;


	public JobChainForm(Composite parent, int style, SchedulerDom dom, Element jobChain) {
		super(parent, style);
		init = true;
		listener = new JobChainListener(dom, jobChain);
		initialize();
		setToolTipText();
		fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
		init = false;

	}


	public void apply() {
	}


	public boolean isUnsaved() {
		return false;
	}


	private void initialize() {

		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(676, 464));
		tName.setFocus();
	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		
		 
		
		
		jobChainGroup = new Group(this, SWT.NONE);        
		jobChainGroup.setText("Job Chain:" + (listener.getChainName() != null ? listener.getChainName() : ""));

		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 3;
		jobChainGroup.setLayout(gridLayout);
		chainNameLabel = new Label(jobChainGroup, SWT.NONE);
		chainNameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		chainNameLabel.setText("Chain Name ");
		tName = new Text(jobChainGroup, SWT.BORDER);
		tName.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				if(!init) {//während der initialiserung sollen keine überprüfungen stattfinden
					//String name =  listener.getChainName();				
					e.doit = Utils.checkElement(listener.getChainName(), listener.get_dom(), Editor.JOB_CHAIN, null);
					/*System.out.println(e.doit);
					if(e.doit) {
						init = true; 
						name = name.substring(0, e.start) + e.text + name.substring(e.start,  e.end);
						tName.setText(name); 
						listener.setChainName(name);
						init = false;
					}
					*/
				}
			}
		});
		tName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				//tName.selectAll();
			}
		});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 1, 1);
		gridData_4.widthHint = 273;
		tName.setLayoutData(gridData_4);
		tName.setText(listener.getChainName());
		tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(init) return;
				
			    String newName = tName.getText().trim();
			    
				boolean existname = Utils.existName(newName, listener.getChain(), "job_chain");
				if (existname)
					tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else {
					//getShell().setDefaultButton(bApplyChain);
					tName.setBackground(null);
				}				

				if(update != null)
					update.updateTreeItem("Job Chain: " + newName);
				
				listener.setChainName(newName);

				jobChainGroup.setText("Job Chain:" + (listener.getChainName() != null ? listener.getChainName() : ""));								
				changeJobChainName = true;
			}
		});

		butDetails = new Button(jobChainGroup, SWT.NONE);
		butDetails.setEnabled(true);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {	
				if(listener.get_dom().isChanged() && changeJobChainName){
					if(listener.get_dom().getFilename() == null)
						MainWindow.message("Please save your Jobchain before selecting the Jobchain Node Parameters.", SWT.ICON_WARNING);
					else 
						MainWindow.message("The Jobchain Name has been changed.\nPlease save your changes before selecting the Jobchain Node Parameters.", SWT.ICON_WARNING);
					
					return;
				} else {
					changeJobChainName = false;
				}
				showDetails(null);
			}
		});
		butDetails.setText("Parameter");
		
		final Label titleLabel = new Label(jobChainGroup, SWT.NONE);
		titleLabel.setText("Title");

		txtTitle = new Text(jobChainGroup, SWT.BORDER);
		txtTitle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtTitle.selectAll();
			}
		});
		txtTitle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				
				if(init) return;
				listener.setTitle(txtTitle.getText());

			}
		});
		txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
		new Label(jobChainGroup, SWT.NONE);
		
		Label lblMaxOrders = new Label(jobChainGroup, SWT.NONE);
		lblMaxOrders.setText("Max Orders");
		
		sMaxorders = new Text(jobChainGroup, SWT.BORDER);
		
		sMaxorders.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
			   if(init) return;
			   int maxOrders;
			   try {
				  maxOrders = Integer.parseInt(sMaxorders.getText().trim());
			   }catch (NumberFormatException e) {
				  maxOrders = 0;
			   }
			   listener.setMaxorders(maxOrders);
			}
		});
		GridData gd_sMaxorders = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sMaxorders.minimumWidth = 60;
		sMaxorders.setLayoutData(gd_sMaxorders);
				new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		
		bRecoverable = new Button(jobChainGroup, SWT.CHECK);
		bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bRecoverable.setSelection(true);
		bRecoverable.setText("Orders Recoverable");
		bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {				
				//getShell().setDefaultButton(bApplyChain);
				//bApplyChain.setEnabled(true);
				//mo neu
				if(init) return;
				listener.setRecoverable(bRecoverable.getSelection());

			}
		});
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		
		
				butDistributed = new Button(jobChainGroup, SWT.CHECK);
				butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
				butDistributed.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(init) return;
						listener.setDistributed(butDistributed.getSelection());
						//getShell().setDefaultButton(bApplyChain);
						//bApplyChain.setEnabled(true);
					}
				});
				butDistributed.setText("Distributed");
				butDistributed.setSelection(listener.isDistributed());
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		
		
		bVisible = new Button(jobChainGroup, SWT.CHECK);
		bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bVisible.setSelection(true);
		bVisible.setText("Visible");
		bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(init) return;
				listener.setVisible(bVisible.getSelection());
				//getShell().setDefaultButton(bApplyChain);
				//bApplyChain.setEnabled(true);
			}
		});
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		 
		 

		if(!listener.get_dom().isLifeElement()) {
		}
		
	}





	private void fillChain(boolean enable, boolean isNew) {
		tName.setEnabled(true);
		bRecoverable.setEnabled(true);
		bVisible.setEnabled(true);

		tName.setText(listener.getChainName());
		txtTitle.setText(listener.getTitle());

		bRecoverable.setSelection(listener.getRecoverable() );
		bVisible.setSelection(listener.getVisible());

		tName.setBackground(null);
		
		sMaxorders.setText(String.valueOf(listener.getMaxOrders()));
	
	}



	
	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}

	private void showDetails(String state) {
		if(tName.getText() != null && tName.getText().length() > 0) {
			//OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			//String[] listOfOrders = ordersListener.getOrderIds();
			//DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
			//detail.showDetails();
			boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory(); 

			if(state == null) {
				//DetailDialogForm detail = new  DetailDialogForm(tName.getText(), listOfOrders, isLifeElement, listener.get_dom().getFilename());
				DetailDialogForm detail = new  DetailDialogForm(tName.getText(), isLifeElement, listener.get_dom().getFilename());
							
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
			} else {
				//DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, listOfOrders, isLifeElement, listener.get_dom().getFilename());
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, null, isLifeElement, listener.get_dom().getFilename());
				
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update);
			} 
			

		} else {
			MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		}

	}

	public void setToolTipText() {

		tName.setToolTipText(Messages.getTooltip("job_chains.chain.name"));
		bRecoverable.setToolTipText(Messages.getTooltip("job_chains.chain.orders_recoverable"));
		bVisible.setToolTipText(Messages.getTooltip("job_chains.chain.visible"));
		//bApplyChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_apply"));
		butDetails.setToolTipText(Messages.getTooltip("job_chains.chain.details"));
		butDistributed.setToolTipText(Messages.getTooltip("job_chains.distributed"));
		txtTitle.setToolTipText(Messages.getTooltip("job_chain.title"));
	}


} // @jve:decl-index=0:visual-constraint="10,10"