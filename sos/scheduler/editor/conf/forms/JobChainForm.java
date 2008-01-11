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
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.conf.listeners.OrdersListener;

public class JobChainForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	private Button              dumm2                       = null;
	
	private Button              bNewNode                    = null;
	
	private Table               tNodes                      = null;
	
	private Button              bApplyNode                  = null;
	
	private Text                tMoveTo                     = null;
	
	private Button              bRemoveFile                 = null;
	
	private Combo               cErrorState                 = null;
	
	private Label               label9                      = null;
	
	private Combo               cNextState                  = null;
	
	private Label               label8                      = null;
	
	private Button              bFileSink                   = null;
	
	private Button              bEndNode                    = null;
	
	private Button              bFullNode                   = null;
	
	private Composite           cType                       = null;
	
	private Combo               cJob                        = null;
	
	private Label               label7                      = null;
	
	private Text                tState                      = null;
	
	private Label               label6                      = null;
	
	private static final String GROUP_NODES_TITLE           = "Chain Nodes";
	
	private static final String GROUP_FILEORDERSOURCE_TITLE = "File Order Sources"; 
	
	private Group               gFileOrderSource            = null;
	
	private JobChainListener    listener                    = null;
	
	private Group               jobChainGroup               = null;

	private Label               label                       = null;
	
	private Text                tName                       = null;
	
	private Button              bRecoverable                = null;
	
	private Button              bVisible                    = null;
		
	private Button              bApplyChain                 = null;
	
	private SashForm            sashForm                    = null;
	
	private Button              bNewFileOrderSource         = null;
	
	private Button              bRemoveFileOrderSource      = null;
	
	private Button              bApplyFileOrderSource       = null;
	
	private Text                tDirectory                  = null;
	
	private Text                tDelayAfterError            = null;
	
	private Text                tMax                        = null;
	
	private Text                tNextState                  = null;
	
	private Text                tRegex                      = null;
	
	private Text                tRepeat                     = null;
	
	private Table               tFileOrderSource            = null;
	
	private Button              bRemoveNode                 = null;
	
	private Group               gNodes                      = null;
	
	private Text                tDelay                      = null;
	
	private Button              butImportJob                = null;
	
	private boolean             refresh                     = false;
	
	private Button              butDetails                  = null;
	
	private Button              butDetailsJob               = null;
	
	private Button              butBrowse                   = null;
	
	private ISchedulerUpdate    update                      = null;
	
	private Combo               cOnError                    = null;
	
	
	
	
	
	public JobChainForm(Composite parent, int style, SchedulerDom dom, Element jobChain) {
		super(parent, style);
		listener = new JobChainListener(dom, jobChain);
		initialize();
		setToolTipText();
		fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
		
		////listener.fillChains(tChains);
		//sashForm.setWeights(new int[] { 20, 50, 30 });
	}
	
	
	public void apply() {
		if (bApplyChain.isEnabled())
			applyChain();
		if (bApplyNode.isEnabled())
			applyNode();
	}
	
	
	public boolean isUnsaved() {
		return bApplyChain.isEnabled() || bApplyNode.isEnabled();
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
		jobChainGroup.setText("Job Chain:" + (listener.getChainName() != null ? listener.getChainName() : ""));
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 4;
		jobChainGroup.setLayout(gridLayout);
		label = new Label(jobChainGroup, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		label.setLayoutData(gridData_6);
		label.setText("Chain Name:");
		tName = new Text(jobChainGroup, SWT.BORDER);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		gridData_4.widthHint = 273;
		tName.setLayoutData(gridData_4);
		tName.setText(listener.getChainName());
		tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				boolean valid = listener.isValidChain(tName.getText());
				if (!valid)
					tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else {
					getShell().setDefaultButton(bApplyChain);
					tName.setBackground(null);
				}
				bApplyChain.setEnabled(valid && !tName.equals(""));
				
			}
		});
		bRecoverable = new Button(jobChainGroup, SWT.CHECK);
		final GridData gridData_5 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		bRecoverable.setLayoutData(gridData_5);
		bRecoverable.setSelection(true);
		bRecoverable.setText("Orders Recoverable");
		bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getShell().setDefaultButton(bApplyChain);
				bApplyChain.setEnabled(true);
			}
		});
		bApplyChain = new Button(jobChainGroup, SWT.NONE);
		bApplyChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bApplyChain.setText("A&pply Job Chain");
		bApplyChain.setEnabled(false);
		bApplyChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyChain();
			}
		});
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		bVisible = new Button(jobChainGroup, SWT.CHECK);
		bVisible.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bVisible.setSelection(true);
		bVisible.setText("Visible");
		bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				getShell().setDefaultButton(bApplyChain);
				bApplyChain.setEnabled(true);
			}
		});
		
		butDetails = new Button(jobChainGroup, SWT.NONE);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		gridData_7.widthHint = 142;
		butDetails.setLayoutData(gridData_7);
		butDetails.setEnabled(true);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				showDetails(null);
			}
		});
		butDetails.setText("Details");
		
		sashForm = new SashForm(jobChainGroup, SWT.VERTICAL);
		sashForm.setLayout(new GridLayout());
		
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
		gridData_2.widthHint = 575;
		sashForm.setLayoutData(gridData_2);
		
		gNodes = new Group(sashForm, SWT.NONE);
		
		gNodes.setText("Chain Node");
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginBottom = 5;
		gridLayout_3.marginTop = 5;
		gridLayout_3.numColumns = 5;
		gNodes.setLayout(gridLayout_3);
		
		label6 = new Label(gNodes, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
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
		new Label(gNodes, SWT.NONE);
		new Label(gNodes, SWT.NONE);
		
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
		label7.setText("Job:");
		
		cJob = new Combo(gNodes, SWT.BORDER);
		cJob.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {        		
				if(refresh) {
					if(listener.getJobs() != null) {
						cJob.setItems(listener.getJobs());
						refresh = false;
					}
				}
			}
			
		});
		
		
		cJob.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		cJob.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {        		
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		final GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
		gridData13.widthHint = 579;
		cJob.setLayoutData(gridData13);

		final Composite composite = new Composite(gNodes, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 2;
		composite.setLayout(gridLayout_2);
		
		butBrowse = new Button(composite, SWT.NONE);
		butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB);
				if(jobname != null && jobname.length() > 0)
					cJob.setText(jobname);
			}
		});
		butBrowse.setText("Browse");
		
		butImportJob = new Button(composite, SWT.NONE);
		final GridData gridData_3 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_3.widthHint = 84;
		butImportJob.setLayoutData(gridData_3);
		butImportJob.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(listener.get_dom(), update, Editor.JOB_CHAINS);
				importJobs.setJobname(cJob);
				importJobs.showAllImportJobs("order");
				update.updateOrders();
				refresh = true;
				
			}
		});
		butImportJob.setText("Import Job");
		
		label8 = new Label(gNodes, SWT.NONE);
		label8.setLayoutData(new GridData());
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
		
		final Label delayLabel = new Label(gNodes, SWT.NONE);
		delayLabel.setLayoutData(new GridData());
		delayLabel.setText("Delay:");
		
		tDelay = new Text(gNodes, SWT.BORDER);
		tDelay.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		tDelay.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
			}
		});
		final GridData gridData_8 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_8.widthHint = 186;
		tDelay.setLayoutData(gridData_8);
		
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
		label9.setLayoutData(new GridData());
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

		final Label onErrorLabel = new Label(gNodes, SWT.NONE);
		onErrorLabel.setText("On Error:");

		cOnError = new Combo(gNodes, SWT.READ_ONLY);
		cOnError.setItems(new String[] {"", "setback", "suspend"});
		cOnError.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
			}
		});
		cOnError.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		cOnError.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		dumm2 = new Button(gNodes, SWT.NONE);
		dumm2.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		dumm2.setVisible(false);
		dumm2.setEnabled(false);
		dumm2.setText("Remove Order File Source");
		
		cType = new Composite(gNodes, SWT.NONE);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginHeight = 10;
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 3;
		cType.setLayout(gridLayout_4);
		final GridData gridData5 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false, 2, 2);
		gridData5.widthHint = 387;
		gridData5.heightHint = 35;
		cType.setLayoutData(gridData5);
		
		bFullNode = new Button(cType, SWT.RADIO);
		bFullNode.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
		bFullNode.setSelection(true);
		bFullNode.setText("Full Node");
		
		bEndNode = new Button(cType, SWT.RADIO);
		bEndNode.setLayoutData(new GridData());
		bEndNode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				if (bFileSink.getSelection()) {
					cNextState.setEnabled(false);
					cErrorState.setEnabled(false);
					cOnError.setEnabled(false);
					tDelay.setEnabled(false);
					cJob.setEnabled(false);
					cJob.setText("");
					cNextState.setText("");
					cErrorState.setText("");
					cOnError.setText("");
					tMoveTo.setEnabled(true);
					bRemoveFile.setEnabled(true);
					
					
				}
				
				if (bEndNode.getSelection()) {
					cNextState.setEnabled(false);
					cErrorState.setEnabled(false);
					cOnError.setEnabled(false);
					tDelay.setEnabled(false);
					cJob.setEnabled(false);
					cJob.setText("");
					cNextState.setText("");
					cErrorState.setText("");
					cOnError.setText("");
					tMoveTo.setEnabled(false);
					bRemoveFile.setEnabled(false);
				}
				
				
				if (bFullNode.getSelection()) {
					tMoveTo.setEnabled(false);
					bRemoveFile.setEnabled(false);
					cNextState.setEnabled(true);
					cErrorState.setEnabled(true);
					cOnError.setEnabled(true);
					cJob.setEnabled(true);
					tDelay.setEnabled(true);
					if (bApplyNode.getEnabled())
						getShell().setDefaultButton(bApplyNode);
				}
				bApplyNode.setEnabled(isValidNode());
				
				
				
			}
		});
		bEndNode.setText("End Node");
		
		bFileSink = new Button(cType, SWT.RADIO);
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.widthHint = 71;
		bFileSink.setLayoutData(gridData);
		bFileSink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (bFileSink.getSelection()) {
					cNextState.setEnabled(false);
					cErrorState.setEnabled(false);
					cOnError.setEnabled(false);
					cJob.setEnabled(false);
					cJob.setText("");
					cNextState.setText("");					
					cErrorState.setText("");
					cOnError.setText("");
					tMoveTo.setEnabled(true);
					bRemoveFile.setEnabled(true);
					
					if (tState.getText().equals(""))
						bApplyNode.setEnabled(false);
				}
			}
		});
		bFileSink.setEnabled(false);
		bFileSink.setText("File Sink");
		
		final Label removeFileLabel = new Label(gNodes, SWT.NONE);
		removeFileLabel.setLayoutData(new GridData(68, SWT.DEFAULT));
		removeFileLabel.setText("Remove File");
		
		bRemoveFile = new Button(gNodes, SWT.CHECK);
		bRemoveFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (bRemoveFile.getSelection())tMoveTo.setText(""); 
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
				
			}
		});
		bRemoveFile.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		bRemoveFile.setLayoutData(new GridData(24, SWT.DEFAULT));
		bRemoveFile.setEnabled(false);
		new Label(gNodes, SWT.NONE);
		
		final Label movweToLabel = new Label(gNodes, SWT.NONE);
		movweToLabel.setLayoutData(new GridData());
		movweToLabel.setText("Move to");
		
		tMoveTo = new Text(gNodes, SWT.BORDER);
		tMoveTo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (!tMoveTo.getText().equals(""))bRemoveFile.setSelection(false);
				bApplyNode.setEnabled(isValidNode());
				if (bApplyNode.getEnabled())
					getShell().setDefaultButton(bApplyNode);
				
			}
		});
		tMoveTo.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					applyNode();
				}
			}
		});
		tMoveTo.setEnabled(false);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_1.widthHint = 235;
		gridData_1.minimumWidth = 80;
		tMoveTo.setLayoutData(gridData_1);
		new Label(gNodes, SWT.NONE);
		
		tNodes = new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER);		
		tNodes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				selectNodes();
				/*if (tNodes.getSelectionCount() > 0) {        			
					listener.selectNode(tNodes);
					enableNode(true);
					fillNode(false);
					butDetailsJob.setEnabled(true);
				} else
					butDetailsJob.setEnabled(false);
				bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
				*/
			}
		});
		tNodes.setLinesVisible(true);
		tNodes.setHeaderVisible(true);
		final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3);
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
		tableColumn4.setText("Job/Dir");
		
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

		final Button upButton = new Button(composite_1, SWT.NONE);
		upButton.setLayoutData(new GridData());
		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					
					if(index == 0) {
						//System.out.println("Datensatz ist bereits ganz oben.");
						//MainWindow.message(Messages.getString("job_chain.top_of_table"), SWT.ICON_INFORMATION);
					} else if(index > 0) {						
						org.eclipse.swt.widgets.TableItem tmp = ((org.eclipse.swt.widgets.TableItem[])tNodes.getSelection().clone())[0];
						
						listener.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), index);
						
						selectNodes();					
						
					}
				}
			}
		});
		upButton.setText("Up");

		final Button downButton = new Button(composite_1, SWT.NONE);
		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					if(index == tNodes.getItemCount()-1) {
						//System.out.println("Datensatz ist bereits ganz unten.");
					} else if(index >= 0) {
						org.eclipse.swt.widgets.TableItem tmp = ((org.eclipse.swt.widgets.TableItem[])tNodes.getSelection().clone())[0];						
						listener.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), index);
						selectNodes();						
					}
				}
			}
		});
		downButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		downButton.setText("Down");
		
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
		
		
		//gFileOrderSource = new Group(sashForm, SWT.VERTICAL);
		
		gFileOrderSource = new Group(jobChainGroup, SWT.NONE);
		
		
		gFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
		gFileOrderSource.setText("File Order Source");
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginTop = 5;
		gridLayout_1.marginBottom = 5;
		gridLayout_1.numColumns = 5;
		gFileOrderSource.setLayout(gridLayout_1);
		
		final Label directoryLabel = new Label(gFileOrderSource, SWT.NONE);
		directoryLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		directoryLabel.setText("Directory");
		
		tDirectory = new Text(gFileOrderSource, SWT.BORDER);
		tDirectory.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
			}
		});
		tDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				
			}
		});
		tDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label delay_after_errorLabel = new Label(gFileOrderSource, SWT.NONE);
		delay_after_errorLabel.setText("Delay after error");
		
		
		tDelayAfterError = new Text(gFileOrderSource, SWT.BORDER);
		tDelayAfterError.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
				
			}
		});
		tDelayAfterError.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		bApplyFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
		bApplyFileOrderSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyFileOrderSource();
			}
		});
		bApplyFileOrderSource.setEnabled(false);
		bApplyFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bApplyFileOrderSource.setText("Apply File Order Source");
		
		final Label regexLabel = new Label(gFileOrderSource, SWT.NONE);
		regexLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		regexLabel.setText("Regex");
		
		
		tRegex = new Text(gFileOrderSource, SWT.BORDER);
		tRegex.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
			}
		});
		tRegex.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label repeatLabel = new Label(gFileOrderSource, SWT.NONE);
		repeatLabel.setText("Repeat");
		
		tRepeat = new Text(gFileOrderSource, SWT.BORDER);
		tRepeat.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
				
			}
		});
		tRepeat.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		new Label(gFileOrderSource, SWT.NONE);
		
		final Label maxLabel = new Label(gFileOrderSource, SWT.NONE);
		maxLabel.setText("Max");
		
		tMax = new Text(gFileOrderSource, SWT.BORDER);
		tMax.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
				
			}
		});
		tMax.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label stateLabel = new Label(gFileOrderSource, SWT.NONE);
		stateLabel.setText("Next state");
		
		tNextState = new Text(gFileOrderSource, SWT.BORDER);
		tNextState.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bApplyFileOrderSource.setEnabled(isValidSource());
				if (bApplyFileOrderSource.getEnabled())
					getShell().setDefaultButton(bApplyFileOrderSource);
				
			}
		});
		tNextState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		tFileOrderSource = new Table(gFileOrderSource, SWT.BORDER);
		tFileOrderSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tFileOrderSource.getSelectionCount() > 0) {
					listener.selectFileOrderSource(tFileOrderSource);
					bApplyFileOrderSource.setEnabled(false);
					fillFileOrderSource(false); 
					enableFileOrderSource(true);              
				}
				bRemoveFileOrderSource.setEnabled(tFileOrderSource.getSelectionCount() > 0);
			}
			
		});
		tFileOrderSource.setLinesVisible(true);
		tFileOrderSource.setHeaderVisible(true);
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
		gridData_9.heightHint = 138;
		tFileOrderSource.setLayoutData(gridData_9);
		
		final TableColumn newColumnTableColumn = new TableColumn(tFileOrderSource, SWT.NONE);
		newColumnTableColumn.setWidth(300);
		newColumnTableColumn.setText("Directory");
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(tFileOrderSource, SWT.NONE);
		newColumnTableColumn_1.setWidth(200);
		newColumnTableColumn_1.setText("Regex");
		
		
		final TableColumn newColumnTableColumn_2 = new TableColumn(tFileOrderSource, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("Next State");
		
		
		bNewFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
		bNewFileOrderSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {	
				getShell().setDefaultButton(null);
				tFileOrderSource.deselectAll();
				listener.selectFileOrderSource(null);
				bRemoveFileOrderSource.setEnabled(false);
				fillFileOrderSource(true);
				enableFileOrderSource(true);
				tDirectory.setFocus();
			}
		});
		bNewFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bNewFileOrderSource.setText("New File Order Source");
		
		bRemoveFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
		bRemoveFileOrderSource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tFileOrderSource.getSelectionCount() > 0) {
					bFileSink.setEnabled(tFileOrderSource.getItemCount() > 0);
					tMoveTo.setEnabled(tFileOrderSource.getItemCount() > 0);
					bRemoveFile.setEnabled(tFileOrderSource.getItemCount() > 0);
					
					int index = tFileOrderSource.getSelectionIndex();
					listener.deleteFileOrderSource(tFileOrderSource);
					tFileOrderSource.remove(index);
					if (index >= tFileOrderSource.getItemCount())
						index--;
					boolean empty = tFileOrderSource.getItemCount() == 0;
					
					fillFileOrderSource(empty);
					enableFileOrderSource(!empty);           
					bRemoveFileOrderSource.setEnabled(!empty);
					if (!empty) {
						tFileOrderSource.select(index);
						listener.selectFileOrderSource(tFileOrderSource);
					} else {
						listener.selectFileOrderSource(null);
					}
				}
			}
		});
		bRemoveFileOrderSource.setEnabled(false);
		bRemoveFileOrderSource.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true));
		bRemoveFileOrderSource.setText("Remove Order File Source");
		//group.setTabList(new Control[] {cChains, fileOrderSourceGroup, gNodes, fileOrderSinkGroup, label_2});
		
	}
	
	
	
	
	
	private void fillChain(boolean enable, boolean isNew) {
		tName.setEnabled(true);
		bRecoverable.setEnabled(true);
		bVisible.setEnabled(true);
		
		tName.setText(listener.getChainName());
		bRecoverable.setSelection(listener.getRecoverable() );
		bVisible.setSelection(listener.getVisible());
		
		tName.setBackground(null);
		bApplyChain.setEnabled(enable);
		
		//if (enable && !isNew) {
		listener.fillFileOrderSource(tFileOrderSource);
		listener.fillChain(tNodes);
		gNodes.setText(GROUP_NODES_TITLE + " for: " + listener.getChainName());
		gFileOrderSource.setText(GROUP_FILEORDERSOURCE_TITLE + " for: " + listener.getChainName());
		bNewFileOrderSource.setEnabled(true);
		bNewNode.setEnabled(true);
		/*} else {
		 bNewNode.setEnabled(false);
		 bNewFileOrderSource.setEnabled(false);
		 }*/
		
		enableNode(false);
		enableFileOrderSource(false);           
		
		
	}
	
	
	private void enableNode(boolean enable) {
		bFullNode.setEnabled(enable);
		bEndNode.setEnabled(enable);
		bFileSink.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
		
		tState.setEnabled(enable);
		cJob.setEnabled(enable);
		cNextState.setEnabled(enable);
		cErrorState.setEnabled(enable);
		cOnError.setEnabled(enable);
		tDelay.setEnabled(enable);
		butImportJob.setEnabled(enable);
		butBrowse.setEnabled(enable);
		tMoveTo.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
		bRemoveFile.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
		
		
		
		bApplyNode.setEnabled(false);
	}
	
	private void enableFileOrderSource(boolean enable) {
		tDirectory.setEnabled(enable);
		tMax.setEnabled(enable);
		tRepeat.setEnabled(enable);
		tDelayAfterError.setEnabled(enable);
		tNextState.setEnabled(enable);
		tRegex.setEnabled(enable);
		bApplyFileOrderSource.setEnabled(false);
	}
	
	
	
	private void fillNode(boolean clear) {
		boolean fullNode = listener.isFullNode();
		boolean fileSinkNode = listener.isFileSinkNode();
		boolean endNode = !fullNode && !fileSinkNode;
		
		bFullNode.setSelection(clear || fullNode);
		bEndNode.setSelection(!clear && endNode);
		bFileSink.setSelection(!clear && fileSinkNode  && tFileOrderSource.getItemCount() > 0);
		
		tDelay.setEnabled(fullNode);
		cNextState.setEnabled(fullNode);
		cErrorState.setEnabled(fullNode);
		cOnError.setEnabled(fullNode);
		cJob.setEnabled(fullNode);
		
		tMoveTo.setEnabled(fileSinkNode  && tFileOrderSource.getItemCount() > 0);
		bRemoveFile.setEnabled(fileSinkNode  && tFileOrderSource.getItemCount() > 0);
		
		tState.setText(clear ? "" : listener.getState());
		tDelay.setText(clear ? "" : listener.getDelay());
		
		cJob.setItems(listener.getJobs());
		if (listener.getStates().length > 0) cNextState.setItems(listener.getStates());
		if (listener.getStates().length > 0) cErrorState.setItems(listener.getStates());
		
		tMoveTo.setText(listener.getMoveTo());
		bRemoveFile.setSelection(listener.getRemoveFile());
		
		int job = cJob.indexOf(listener.getJob());
		if (clear || job == -1)
			cJob.setText(listener.getJob());
		else
			cJob.select(job);
		
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
		
		int onError = cOnError.indexOf(listener.getOnError());
		if (clear || !fullNode || onError == -1)
			cOnError.setText(listener.getOnError());
		else
			cOnError.select(onError);
		
		
		bApplyNode.setEnabled(false);
		
	}
	
	private void fillFileOrderSource(boolean clear) {
		
		tDirectory.setText(clear ? "" : listener.getFileOrderSource("directory"));
		tRegex.setText(clear ? "" : listener.getFileOrderSource("regex"));
		tMax.setText(clear ? "" : listener.getFileOrderSource("max"));
		tDelayAfterError.setText(clear ? "" : listener.getFileOrderSource("delay_after_error"));
		tRepeat.setText(listener.getFileOrderSource(clear ? "" : "repeat"));
		tNextState.setText(listener.getFileOrderSource(clear ? "" : "next_state"));
		
		bApplyFileOrderSource.setEnabled(false);
	}
	
	
	
	private void applyChain() {
		String oldJobChainname = listener.getChainName();
		listener.applyChain(tName.getText(), bRecoverable.getSelection(), bVisible.getSelection());
		update.updateJobChain(tName.getText(), oldJobChainname); 
		fillChain(true, false);
		bApplyChain.setEnabled(false);
		if(listener.getChainName() != null && listener.getChainName().length() > 0) {
			butDetails.setEnabled(true);
		}  else {
			butDetails.setEnabled(false);
		}
	}
	
	
	
	private void applyNode() {
		String msg = "";
		if (!listener.isValidState(tState.getText()))msg = "State already defined.";
		if (!msg.equals("")) {
			MainWindow.message(msg, SWT.ICON_INFORMATION);
		} else {
			listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), cOnError.getText());
			listener.fillChain(tNodes);
			bApplyNode.setEnabled(false);
			bRemoveNode.setEnabled(false);            
			listener.selectNode(null);			
			fillNode(true);
			enableNode(false);
		}
	}
	
	private void applyFileOrderSource() {
		String msg = "";
		
		if (!msg.equals("")) {
			MainWindow.message(msg, SWT.ICON_INFORMATION);
		} else {
			listener.applyFileOrderSource(tDirectory.getText(),tRegex.getText(), tNextState.getText(), tMax.getText(), tRepeat.getText(),tDelayAfterError.getText());
			listener.fillFileOrderSource(tFileOrderSource);
			bApplyFileOrderSource.setEnabled(false);
			bRemoveFileOrderSource.setEnabled(false);
			
			bFileSink.setEnabled(bFullNode.getEnabled());
			tMoveTo.setEnabled(bFullNode.getEnabled());
			bRemoveFile.setEnabled(bFullNode.getEnabled());
			
			listener.selectFileOrderSource(null);
			fillFileOrderSource(true);
			enableFileOrderSource(false);
		}
	}
	
	private boolean isValidNode() {
		if (tState.getText().equals("") || bFullNode.getSelection() && cJob.getText().equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isValidSource() {
		if (tDirectory.getText().equals("") ) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
	
	public void setToolTipText() {
		tName.setToolTipText(Messages.getTooltip("job_chains.chain.name"));
		bRecoverable.setToolTipText(Messages.getTooltip("job_chains.chain.orders_recoverable"));
		bVisible.setToolTipText(Messages.getTooltip("job_chains.chain.visible"));
		bApplyChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_apply"));
		tState.setToolTipText(Messages.getTooltip("job_chains.node.state"));
		cErrorState.setToolTipText(Messages.getTooltip("job_chains.node.error_state"));
		cJob.setToolTipText(Messages.getTooltip("job_chains.node.job"));
		cNextState.setToolTipText(Messages.getTooltip("job_chains.node.next_state"));
		bApplyNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_apply"));
		bFullNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_full_node"));
		bEndNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_end_node"));
		bFileSink.setToolTipText(Messages.getTooltip("job_chains.node.btn_filesink_node"));
		butDetails.setToolTipText(Messages.getTooltip("job_chains.chain.details"));
		butDetailsJob.setToolTipText(Messages.getTooltip("job_chains.node.details"));
		butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		
		
	}
	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}
	
	private void showDetails(String state) {
		if(tName.getText() != null && tName.getText().length() > 0) {
			OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			String[] listOfOrders = ordersListener.getOrderIds();
			//DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
			//detail.showDetails();
			if(state == null) {
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
				detail.showDetails();
			} else {
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, listOfOrders);
				detail.showDetails();
			} 
			
		} else {
			MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
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
} // @jve:decl-index=0:visual-constraint="10,10"