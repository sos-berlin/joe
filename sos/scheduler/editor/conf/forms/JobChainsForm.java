package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;
import sos.scheduler.editor.conf.listeners.OrdersListener;

public class JobChainsForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	private static final String GROUP_NODES_TITLE = "Chain Nodes";
	
	private static final String GROUP_FILEORDERSOURCE_TITLE = "File Order Sources";
	
	private ISchedulerUpdate update = null;
	
	private JobChainsListener   listener;
	
	private Group               group             = null;
	
	private Table               tChains           = null;
	
	private Button              bRemoveChain      = null;
	
	private Button              bNewChain         = null;  
	
	private SashForm            sashForm               = null;
	
	private boolean             refresh                = false;
	
	private Button              butDetails             = null; 
	
	
	public JobChainsForm(Composite parent, int style, SchedulerDom dom, Element config, ISchedulerUpdate update_) {
		super(parent, style);
		listener = new JobChainsListener(dom, config, update_);
		initialize();
		setToolTipText();
		//fillChain(false, false);
		listener.fillChains(tChains);
		//sashForm.setWeights(new int[] { 20, 50, 30 });
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(676, 464));
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		group = new Group(this, SWT.NONE);        
		
		final GridLayout gridLayout = new GridLayout();
		group.setLayout(gridLayout);
		
		sashForm = new SashForm(group, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout());
		
		sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		final Group jobchainsGroup = new Group(sashForm, SWT.NONE);
		jobchainsGroup.setText("Job Chains");
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 2;
		jobchainsGroup.setLayout(gridLayout_2);
		tChains = new Table(jobchainsGroup, SWT.FULL_SELECTION | SWT.BORDER);
		tChains.getHorizontalBar().setMaximum(0);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		tChains.setLayoutData(gridData_2);
		tChains.setHeaderVisible(true);
		tChains.setLinesVisible(true);
		tChains.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tChains.getSelectionCount() > 0) {
					listener.selectChain(tChains.getSelectionIndex());
					/*fillChain(true, false);
					bRemoveNode.setEnabled(false);
					bRemoveFileOrderSource.setEnabled(false);
					bApplyChain.setEnabled(false);
					butDetailsJob.setEnabled(false);
					*/
					butDetails.setEnabled(true);
				}
				bRemoveChain.setEnabled(tChains.getSelectionCount() > 0);
			}
		});
		TableColumn tableColumn1 = new TableColumn(tChains, SWT.NONE);
		tableColumn1.setWidth(150);
		tableColumn1.setText("Orders Recoverable");
		TableColumn tableColumn_2 = new TableColumn(tChains, SWT.NONE);
		tableColumn_2.setWidth(104);
		tableColumn_2.setText("Name");
		TableColumn tableColumn2 = new TableColumn(tChains, SWT.NONE);
		tableColumn2.setWidth(90);
		tableColumn2.setText("Visible");
		bNewChain = new Button(jobchainsGroup, SWT.NONE);
		bNewChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bNewChain.setText("New Job &Chain");
		getShell().setDefaultButton(bNewChain);
		bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.newChain();
				applyChain();
				tChains.deselectAll();
				butDetails.setEnabled(false);   
				
				/*fillChain(true, true);
				bApplyChain.setEnabled(false);
				enableFileOrderSource(true);              
				tName.setFocus();
				*/
			}
		});
		bRemoveChain = new Button(jobchainsGroup, SWT.NONE);
		bRemoveChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bRemoveChain.setText("Remove Job Chain");
		bRemoveChain.setEnabled(false);
		bRemoveChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				deleteChain();
			}
		});
		
		butDetails = new Button(jobchainsGroup, SWT.NONE);
		butDetails.setEnabled(false);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				showDetails(null);
			}
		});
		butDetails.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butDetails.setText("Details");
		//group.setTabList(new Control[] {cChains, fileOrderSourceGroup, gNodes, fileOrderSinkGroup, label_2});
		
	}
	
	
	
	
	
	private void deleteChain() {
		if (tChains.getSelectionCount() > 0) {
			int index = tChains.getSelectionIndex();
			listener.deleteChain(index);
			tChains.remove(index);	
			if (index >= tChains.getItemCount())
				index--;
			if (tChains.getItemCount() > 0) {
				tChains.select(index);
				listener.selectChain(index);
			}
		}
		boolean selection = tChains.getSelectionCount() > 0;
		bRemoveChain.setEnabled(selection);	
	}
	
	
	private void applyChain() {
        listener.applyChain("job_chain" + (tChains.getItemCount() + 1), true, true);
        int index = tChains.getSelectionIndex();
        if (index == -1)
            index = tChains.getItemCount();
        listener.fillChains(tChains);
        tChains.select(index);
        
        bRemoveChain.setEnabled(true);
        
        getShell().setDefaultButton(bNewChain);
        if(listener.getChainName() != null && listener.getChainName().length() > 0) {
        	butDetails.setEnabled(true);
        }  else {
        	butDetails.setEnabled(false);
        }
    }
    
	
	  
	
	
	public void setToolTipText() {
		bNewChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_new"));
		bRemoveChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_remove"));
		tChains.setToolTipText(Messages.getTooltip("job_chains.table"));
		butDetails.setToolTipText(Messages.getTooltip("job_chains.chain.details"));
		
		
	}
	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}
	
	private void showDetails(String state) {
		String name = tChains.getSelection()[0].getText(0);
		if(name != null && name.length() > 0) {
			OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			String[] listOfOrders = ordersListener.getOrderIds();
			//DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
			//detail.showDetails();
			if(state == null) {
				DetailDialogForm detail = new DetailDialogForm(name, listOfOrders);
				detail.showDetails();
			} else {
				DetailDialogForm detail = new DetailDialogForm(name, state, listOfOrders);
				detail.showDetails();
			} 
			
		} else {
			MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		}
		
	}
	
	public void apply() {
        
            applyChain();
        
    }


    public boolean isUnsaved() {
        return false;
    }

} // @jve:decl-index=0:visual-constraint="10,10"