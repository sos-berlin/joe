package sos.scheduler.editor.conf.forms;

import java.util.List;
import org.eclipse.swt.SWT;
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
import org.jdom.xpath.XPath;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainNestedListener;
import sos.scheduler.editor.conf.listeners.OrdersListener;


public class JobChainNestedNodesForm extends Composite implements IUnsaved, IUpdateLanguage {


	private Button                    bNewNode                    = null;

	private Table                     tNodes                      = null;

	private Button                    bApplyNode                  = null;

	private Combo                     cErrorState                 = null;

	private Label                     label9                      = null;

	private Combo                     cNextState                  = null;

	private Label                     label8                      = null;

	private Button                    bEndNode                    = null;

	private Button                    bFullNode                   = null;

	private Composite                 cType                       = null;

	private Combo                     cJobChain                   = null;

	private Label                     label7                      = null;

	private Text                      tState                      = null;

	private Label                     label6                      = null;

	private static final String       GROUP_NODES_TITLE           = "Nested Job Chain Nodes";

	private JobChainNestedListener    listener                    = null;

	private Group                     jobChainGroup               = null;

	private Button                    bRemoveNode                 = null;

	private Group                     gNodes                      = null;

	private boolean                   refresh                     = false;

	private Button                    butDetailsJob               = null;

	private Button                    butBrowse                   = null;

	private ISchedulerUpdate          update                      = null;

	private Button                    butUp                       = null;

	private Button                    butDown                     = null;

	private SchedulerDom              dom                         = null;

	private Button                    butGoto                     = null;
	
	public JobChainNestedNodesForm(Composite parent, int style, SchedulerDom dom_, Element jobChain) {

		super(parent, style);
		dom = dom_;
		listener = new JobChainNestedListener(dom, jobChain);
		initialize();
		setToolTipText();
		fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
		boolean existChainNodes = check();
		jobChainGroup.setEnabled(existChainNodes);
		bNewNode.setEnabled(existChainNodes);

	}


	public void apply() {		
		if (bApplyNode.isEnabled())
			applyNode();		
	}


	public boolean isUnsaved() {		
		return bApplyNode.isEnabled();		
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

		jobChainGroup = new Group(this, SWT.NONE);        
		final GridLayout gridLayout = new GridLayout();
		jobChainGroup.setLayout(gridLayout);		

		gNodes = new Group(jobChainGroup, SWT.NONE);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData_2.heightHint = 170;
		gNodes.setLayoutData(gridData_2);
		gNodes.setText("Chain Jain Node");
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginBottom = 5;
		gridLayout_3.marginTop = 5;
		gridLayout_3.numColumns = 4;
		gNodes.setLayout(gridLayout_3);

		label6 = new Label(gNodes, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false, 2, 1));
		label6.setText("State:");

		tState = new Text(gNodes, SWT.BORDER);
		tState.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				boolean valid = listener.isValidState(tState.getText());
				if (!valid)
					tState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else
					tState.setBackground(null);
				bApplyNode.setEnabled(isValidNode()&& valid);
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);				
			}			
		});

		final GridData gridData18 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData18.widthHint = 459;
		tState.setLayoutData(gridData18);

		bApplyNode = new Button(gNodes, SWT.NONE);
		bApplyNode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyNode();
			}
		});		
		final GridData gridData7 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bApplyNode.setLayoutData(gridData7);
		bApplyNode.setEnabled(false);
		bApplyNode.setText("&Apply Chain Node");

		label7 = new Label(gNodes, SWT.NONE);
		label7.setLayoutData(new GridData());
		label7.setText("Job Chain:");

		butGoto = new Button(gNodes, SWT.ARROW | SWT.DOWN);
		butGoto.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
		butGoto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				ContextMenu.goTo(cJobChain.getText(), dom, Editor.JOB_CHAIN);
			}
		});
		butGoto.setAlignment(SWT.RIGHT);
		

		cJobChain = new Combo(gNodes, SWT.BORDER);
		cJobChain.setMenu(new sos.scheduler.editor.app.ContextMenu(cJobChain, dom, sos.scheduler.editor.app.Editor.JOB_CHAIN).getMenu());
		cJobChain.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {        		
				if(refresh) {
					if(listener.getJobChains() != null) {
						cJobChain.setItems(listener.getJobChains());
						refresh = false;
					}
				}
			}
			

		});

		cJobChain.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		cJobChain.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {        		
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		final GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData13.widthHint = 579;
		cJobChain.setLayoutData(gridData13);

		final Composite composite = new Composite(gNodes, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		composite.setLayout(gridLayout_2);

		butBrowse = new Button(composite, SWT.NONE);
		butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB_CHAIN);
				if(jobname != null && jobname.length() > 0)
					cJobChain.setText(jobname);
			}
		});
		butBrowse.setText("Browse");

		if(!listener.get_dom().isLifeElement()) {
		}
		label8 = new Label(gNodes, SWT.NONE);
		label8.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label8.setText("Next State:");

		cNextState = new Combo(gNodes, SWT.BORDER);
		cNextState.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		cNextState.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		final GridData gridData14 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData14.widthHint = 80;
		cNextState.setLayoutData(gridData14);

		bNewNode = new Button(gNodes, SWT.NONE);
		bNewNode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				getShell().setDefaultButton(null);
				tNodes.deselectAll();
				butDetailsJob.setEnabled(false);
				listener.selectNode(null);
				bRemoveNode.setEnabled(false);
				enableNode(true);
				fillNode(true);
				tState.setFocus();
			}
		});
		bNewNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bNewNode.setText("New Chain &Node");

		label9 = new Label(gNodes, SWT.NONE);
		label9.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		label9.setText("Error State:");

		cErrorState = new Combo(gNodes, SWT.BORDER);
		cErrorState.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		cErrorState.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		final GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData15.widthHint = 80;
		cErrorState.setLayoutData(gridData15);
		new Label(gNodes, SWT.NONE);

		cType = new Composite(gNodes, SWT.NONE);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginBottom = 2;
		gridLayout_4.marginHeight = 0;
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 2;
		cType.setLayout(gridLayout_4);
		final GridData gridData5 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		gridData5.widthHint = 387;
		gridData5.heightHint = 35;
		cType.setLayoutData(gridData5);

		bFullNode = new Button(cType, SWT.RADIO);
		bFullNode.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
		bFullNode.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
		bFullNode.setSelection(true);
		bFullNode.setText("Full Node");

		bEndNode = new Button(cType, SWT.RADIO);
		bEndNode.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
		bEndNode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				if (bEndNode.getSelection()) {
					cNextState.setEnabled(false);
					cErrorState.setEnabled(false);
					cJobChain.setEnabled(false);
					cJobChain.setText("");
					cNextState.setText("");
					cErrorState.setText("");
				}


				if (bFullNode.getSelection()) {
					cNextState.setEnabled(true);
					cErrorState.setEnabled(true);
					cJobChain.setEnabled(true);
					if (bApplyNode.getEnabled())
						getShell().setDefaultButton(bApplyNode);
				}
				bApplyNode.setEnabled(isValidNode());



			}
		});
		bEndNode.setText("End Node");
		new Label(gNodes, SWT.NONE);

		tNodes = new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER);		
		tNodes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				selectNodes();				
			}
		});
		tNodes.setLinesVisible(true);
		tNodes.setHeaderVisible(true);
		final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 3);
		gridData4.heightHint = 112;
		tNodes.setLayoutData(gridData4);

		final TableColumn tableColumn3 = new TableColumn(tNodes, SWT.NONE);
		tableColumn3.setWidth(90);
		tableColumn3.setText("State");

		final TableColumn newColumnTableColumn_3 = new TableColumn(tNodes, SWT.NONE);
		newColumnTableColumn_3.setWidth(100);
		newColumnTableColumn_3.setText("Node");

		final TableColumn tableColumn4 = new TableColumn(tNodes, SWT.NONE);
		tableColumn4.setWidth(200);
		tableColumn4.setText("Job Chain");

		final TableColumn tableColumn5 = new TableColumn(tNodes, SWT.NONE);
		tableColumn5.setWidth(90);
		tableColumn5.setText("Next State");

		final TableColumn tableColumn6 = new TableColumn(tNodes, SWT.NONE);
		tableColumn6.setWidth(90);
		tableColumn6.setText("Error State");

		final TableColumn newColumnTableColumn_4 = new TableColumn(tNodes, SWT.NONE);
		newColumnTableColumn_4.setWidth(100);
		newColumnTableColumn_4.setText("On Error");

		final Composite composite_1 = new Composite(gNodes, SWT.NONE);
		composite_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.marginWidth = 0;
		gridLayout_5.marginHeight = 0;
		gridLayout_5.numColumns = 2;
		composite_1.setLayout(gridLayout_5);

		butUp = new Button(composite_1, SWT.NONE);
		butUp.setLayoutData(new GridData());
		butUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					if(index > 0) {											
						listener.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), "", cNextState.getText(), cErrorState.getText(), index);
						selectNodes();					

					}
				}
			}
		});

		butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));

		butDown = new Button(composite_1, SWT.NONE);
		butDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					if(index == tNodes.getItemCount()-1) {
						//System.out.println("Datensatz ist bereits ganz unten.");
					} else if(index >= 0) {
						listener.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), "", cNextState.getText(), cErrorState.getText(), index);
						selectNodes();						
					}
				}
			}
		});
		butDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butDown.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));

		bRemoveNode = new Button(gNodes, SWT.NONE);
		bRemoveNode.setEnabled(false);
		bRemoveNode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					listener.deleteNode(tNodes);
					tNodes.remove(index);
					if (index >= tNodes.getItemCount())
						index--;
					boolean empty = tNodes.getItemCount() == 0;

					fillNode(empty);
					enableNode(!empty);
					bRemoveNode.setEnabled(!empty);
					if (!empty) {
						tNodes.select(index);
						listener.selectNode(tNodes);
					} else {
						listener.selectNode(null);
					}
				}
			}
		});
		bRemoveNode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bRemoveNode.setText("Remove Node");

		butDetailsJob = new Button(gNodes, SWT.NONE);
		butDetailsJob.setEnabled(false);
		butDetailsJob.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(tNodes.getSelectionCount() > 0)
					showDetails(tNodes.getSelection()[0].getText(0));
			}
		});
		butDetailsJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butDetailsJob.setText("Details");

	}


	private void fillChain(boolean enable, boolean isNew) {
		listener.fillChain(tNodes);
		gNodes.setText(GROUP_NODES_TITLE + " for: " + listener.getChainName());
		bNewNode.setEnabled(true);
		enableNode(false);		           
	}


	private void enableNode(boolean enable) {
		bFullNode.setEnabled(enable);
		bEndNode.setEnabled(enable);
		tState.setEnabled(enable);
		cJobChain.setEnabled(enable);
		cNextState.setEnabled(enable);
		cErrorState.setEnabled(enable);
		butBrowse.setEnabled(enable);
		bApplyNode.setEnabled(false);
	}


	private void fillNode(boolean clear) {

		boolean fullNode = listener.isFullNode();
		boolean fileSinkNode = listener.isFileSinkNode();
		boolean endNode = !fullNode && !fileSinkNode;

		bFullNode.setSelection(clear || fullNode);
		bEndNode.setSelection(!clear && endNode);
		cNextState.setEnabled(fullNode);
		cErrorState.setEnabled(fullNode);
		cJobChain.setEnabled(fullNode);

		tState.setText(clear ? "" : listener.getState());

		cJobChain.setItems(listener.getJobChains());
		if (listener.getStates().length > 0) cNextState.setItems(listener.getStates());
		if (listener.getStates().length > 0) cErrorState.setItems(listener.getStates());

		int job = cJobChain.indexOf(listener.getJobChain());
		if (clear || job == -1)
			cJobChain.setText(listener.getJobChain());
		else
			cJobChain.select(job);

		int next = cNextState.indexOf(listener.getNextState());
		if (clear || !fullNode || next == -1)
			cNextState.setText(listener.getNextState());
		else
			cNextState.select(next);

		int error = cErrorState.indexOf(listener.getErrorState());
		if (clear || !fullNode || error == -1)
			cErrorState.setText(listener.getErrorState());
		else
			cErrorState.select(error);


		bApplyNode.setEnabled(false);

	}


	private void applyNode() {

		String msg = "";
		if (!listener.isValidState(tState.getText()))msg = "State already defined.";
		if (!msg.equals("")) {
			MainWindow.message(msg, SWT.ICON_INFORMATION);
		} else {
			//listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), cOnError.getText());
			listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), 
					tState.getText(), 
					cJobChain.getText(), 
					cNextState.getText(), 
					cErrorState.getText()
			);
			listener.fillChain(tNodes);
			bApplyNode.setEnabled(false);
			bRemoveNode.setEnabled(false);            
			listener.selectNode(null);			
			fillNode(true);
			enableNode(false);
		}

	}


	private boolean isValidNode() {
		if (tState.getText().equals("") || bFullNode.getSelection() && cJobChain.getText().equals("")) {
			return false;
		} else {
			return true;
		}
	}


	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}


	private void showDetails(String state) {

		OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
		String[] listOfOrders = ordersListener.getOrderIds();
		boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory(); 

		if(state == null) {
			DetailDialogForm detail = new  DetailDialogForm(listener.getChainName(), listOfOrders, isLifeElement, listener.get_dom().getFilename());
			detail.showDetails();
		} else {
			DetailDialogForm detail = new DetailDialogForm(listener.getChainName(), state, listOfOrders, isLifeElement, listener.get_dom().getFilename());
			detail.showDetails();
		} 

	}


	private void selectNodes() {

		if (tNodes.getSelectionCount() > 0) {        			
			listener.selectNode(tNodes);
			enableNode(true);
			fillNode(false);
			butDetailsJob.setEnabled(true);
		} else
			butDetailsJob.setEnabled(false);
		bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);

	}


	public void setToolTipText() {	
		tState.setToolTipText(Messages.getTooltip("job_chains.node.state"));
		cErrorState.setToolTipText(Messages.getTooltip("job_chains.node.error_state"));
		cJobChain.setToolTipText(Messages.getTooltip("job_chains.node.job"));
		cNextState.setToolTipText(Messages.getTooltip("job_chains.node.next_state"));
		bApplyNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_apply"));
		bFullNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_full_node"));
		bEndNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_end_node"));
		butDetailsJob.setToolTipText(Messages.getTooltip("job_chains.node.details"));
		butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		butDown.setToolTipText(Messages.getTooltip("button_down"));
		butUp.setToolTipText(Messages.getTooltip("button_up"));
		bNewNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_new"));
		tNodes.setToolTipText(Messages.getTooltip("job_chains.chain.node_table"));
		butGoto.setToolTipText(Messages.getTooltip("goto"));
	}

	//ein Job Chain hat entweder job_chain_node ODER job_chain_node.job_chain Kindknoten. 
	private boolean check() {
		try {
			XPath x3 = XPath.newInstance("//job_chain[@name='"+ listener.getChainName() + "']/job_chain_node");				 
			List listOfElement_3 = x3.selectNodes(dom.getDoc());
			if(listOfElement_3.isEmpty())
				return true;
			else 
				return false;
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			return true;
		}

	}

} // @jve:decl-index=0:visual-constraint="10,10"