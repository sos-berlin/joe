package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;


public class JobChainsForm extends Composite implements IUnsaved, IUpdateLanguage {


	private ISchedulerUpdate    update            = null;

	private JobChainsListener   listener          = null;

	private Group               group             = null;

	private static Table        tChains           = null;

	private Button              bRemoveChain      = null;

	private Button              bNewChain         = null;  

	private SashForm            sashForm          = null;
 
	private Button              butDetails        = null; 

	private SchedulerDom        _dom              = null;
	
	/**Hilfsvariable: Wenn Parameter Formular geöffnet wurde muss überprüft werden, ob der Checkbox in der Tabelle - State gesetzt werden soll.*/
	private boolean             checkParameter             = false;



	public JobChainsForm(Composite parent, int style, SchedulerDom dom, Element config, ISchedulerUpdate update_) {
		super(parent, style);
		
		listener = new JobChainsListener(dom, config, update_);		
		_dom = dom;
		
		initialize();
		setToolTipText();
		update = update_;
		listener.fillChains(tChains);

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
		tChains = new Table(jobchainsGroup, SWT.BORDER);
		tChains.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				if(tChains.getSelectionCount() > 0)
					ContextMenu.goTo(tChains.getSelection()[0].getText(0), _dom, Editor.JOB_CHAIN);
			}
		});
		
		
		tChains.getHorizontalBar().setMaximum(0);
		tChains.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));
		tChains.setHeaderVisible(true);
		tChains.setLinesVisible(true);
		tChains.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				/*if (e.detail == SWT.CHECK) {
					e.doit = false;
					return;
				}*/
				boolean enabled = true;
				if (tChains.getSelectionCount() > 0) {
					listener.selectChain(tChains.getSelectionIndex());
					Element currElem = listener.getChain();
					if(currElem != null && !Utils.isElementEnabled("job_chain", _dom, currElem)) {
						enabled = false;
					}

					butDetails.setEnabled(true);
				}
				bRemoveChain.setEnabled(tChains.getSelectionCount() > 0 && enabled);
			}

		});
		
		
		TableColumn tableColumn1 = new TableColumn(tChains, SWT.NONE);
		tableColumn1.setWidth(150);
		
		tableColumn1.setText("Name");
		
		TableColumn ordersRecoverableTableColumn = new TableColumn(tChains, SWT.NONE);
		ordersRecoverableTableColumn.setWidth(104);
		ordersRecoverableTableColumn.setText("Orders Recoverable");
		TableColumn tableColumn2 = new TableColumn(tChains, SWT.NONE);
		tableColumn2.setWidth(90);
		tableColumn2.setText("Visible");
		bNewChain = new Button(jobchainsGroup, SWT.NONE);
		bNewChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bNewChain.setText("New Job &Chain");
		getShell().setDefaultButton(bNewChain);
		bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				createNewJobChain();
				/*listener.newChain();
				applyChain();
				tChains.deselectAll();
				butDetails.setEnabled(false);*/
			}
		});
		bRemoveChain = new Button(jobchainsGroup, SWT.NONE);
		bRemoveChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bRemoveChain.setText("Remove Job Chain");
		bRemoveChain.setEnabled(false);
		bRemoveChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				
				int c = MainWindow.message(getShell(), "Do you want remove the jobchain?", SWT.ICON_QUESTION | SWT.YES | SWT.NO );
				if(c != SWT.YES)
					return;
				
				
				if(Utils.checkElement(tChains.getSelection()[0].getText(0), listener.get_dom(), sos.scheduler.editor.app.Editor.JOB_CHAINS, null))//wird der Job woandes verwendet?
					deleteChain();
			}
		});

		butDetails = new Button(jobchainsGroup, SWT.NONE);
		butDetails.setEnabled(false);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				checkParameter = true;
				showDetails(null);
			}
		});
		butDetails.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				
				if(checkParameter) {
					listener.fillChains(tChains);
					checkParameter = false;
				}
					
			}
		});
		butDetails.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butDetails.setText("Parameter");

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
	
       int i = tChains.getItemCount() + 1;
	   String newName = "job_chain" + i;
 	   while (listener.indexOf(newName) >= 0) {
 		   i++;
		   newName = "job_chain" + i;
	   }
 	    
	
		listener.applyChain(newName, true, true);
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
			//OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			//String[] listOfOrders = ordersListener.getOrderIds();
			boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();
			
			if(state == null) {
				DetailDialogForm detail = new DetailDialogForm(name, isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(_dom, update);				
			} else {
				DetailDialogForm detail = new DetailDialogForm(name, state, null, isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
				detail.getDialogForm().setParamsForWizzard(_dom, update);
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

	public void createNewJobChain() {
		listener.newChain();
		applyChain();
		tChains.deselectAll();
		butDetails.setEnabled(false);
	}

	public static Table getTableChains() {
		return tChains;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"