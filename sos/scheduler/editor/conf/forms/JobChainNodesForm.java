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
import com.swtdesigner.SWTResourceManager;

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
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.conf.listeners.OrdersListener;


public class JobChainNodesForm extends Composite implements IUnsaved, IUpdateLanguage {

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

	private Button              butDetailsJob               = null;

	private Button              butBrowse                   = null;

	private ISchedulerUpdate    update                      = null;

	private Combo               cOnError                    = null;

	private Button              butUp                       = null;

	private Button              butDown                     = null;

	private SchedulerDom        dom                         = null;

	private Button              butGoto                     = null;

	public JobChainNodesForm(Composite parent, int style, SchedulerDom dom_, Element jobChain) {

		super(parent, style);
		dom = dom_;
		listener = new JobChainListener(dom, jobChain);
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
		gNodes.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		gNodes.setText("Chain Node");		
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginBottom = 5;
		gridLayout_3.marginTop = 5;
		gridLayout_3.numColumns = 6;
		gNodes.setLayout(gridLayout_3);

		label6 = new Label(gNodes, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label6.setText("State:");
		new Label(gNodes, SWT.NONE);

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
		final GridData gridData18 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
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
		label7.setText("Job:");

		butGoto = new Button(gNodes, SWT.ARROW | SWT.DOWN);
		butGoto.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
		butGoto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				ContextMenu.goTo(cJob.getText(), dom, Editor.JOB);
			}
		});
		butGoto.setAlignment(SWT.RIGHT);
		
		
		cJob = new Combo(gNodes, SWT.BORDER);
	
		cJob.setMenu(new sos.scheduler.editor.app.ContextMenu(cJob, dom, Editor.JOB).getMenu());
		//Utils.goTo(cJob.getText(), listener.get_dom(), sos.scheduler.editor.app.Editor.JOB);
		/*cJob.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event e) {
				//e.doit = cJob.getSelectionCount() > 0;
			}
		});
		*/
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
		butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB);
				if(jobname != null && jobname.length() > 0)
					cJob.setText(jobname);
			}
		});
		butBrowse.setText("Browse");

		if(!listener.get_dom().isLifeElement()) {
			butImportJob = new Button(composite, SWT.NONE);
			final GridData gridData_3 = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData_3.widthHint = 84;
			butImportJob.setLayoutData(gridData_3);
			butImportJob.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(listener.get_dom(), update, Editor.JOB_CHAINS);
					importJobs.setJobname(cJob);
					importJobs.showAllImportJobs("order");
					if (!listener.get_dom().isLifeElement())
						update.updateOrders();
					refresh = true;

				}
			});
			butImportJob.setText("Import Job");
		}
		label8 = new Label(gNodes, SWT.NONE);
		label8.setLayoutData(new GridData());
		label8.setText("Next State:");
		new Label(gNodes, SWT.NONE);

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
		gridData_8.minimumWidth = 35;
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
		new Label(gNodes, SWT.NONE);

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
		final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_12.minimumWidth = 20;
		cOnError.setLayoutData(gridData_12);

		dumm2 = new Button(gNodes, SWT.NONE);
		dumm2.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		dumm2.setVisible(false);
		dumm2.setEnabled(false);
		dumm2.setText("Remove Order File Source");

		cType = new Composite(gNodes, SWT.NONE);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginHeight = 0;
		gridLayout_4.marginWidth = 0;
		gridLayout_4.numColumns = 4;
		cType.setLayout(gridLayout_4);
		final GridData gridData5 = new GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1);
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

		final Composite composite_3 = new Composite(cType, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData_5.widthHint = 238;
		composite_3.setLayoutData(gridData_5);
		final GridLayout gridLayout_7 = new GridLayout();
		gridLayout_7.verticalSpacing = 0;
		gridLayout_7.numColumns = 2;
		gridLayout_7.marginWidth = 0;
		gridLayout_7.marginHeight = 0;
		gridLayout_7.horizontalSpacing = 0;
		composite_3.setLayout(gridLayout_7);

		final Label removeFileLabel = new Label(composite_3, SWT.NONE);
		removeFileLabel.setText("Remove File");

		bRemoveFile = new Button(composite_3, SWT.CHECK);
		final GridData gridData_1 = new GridData();
		gridData_1.horizontalIndent = 5;
		bRemoveFile.setLayoutData(gridData_1);
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
		bRemoveFile.setEnabled(false);

		final Label movweToLabel = new Label(composite_3, SWT.NONE);
		movweToLabel.setText("Move to");

		tMoveTo = new Text(composite_3, SWT.BORDER);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_3.horizontalIndent = 5;
		tMoveTo.setLayoutData(gridData_3);
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
		new Label(gNodes, SWT.NONE);

		tNodes = new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER);		
		tNodes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				selectNodes();				
			}
		});
		tNodes.setLinesVisible(true);
		tNodes.setHeaderVisible(true);
		final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
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

		butUp = new Button(composite_1, SWT.NONE);
		butUp.setLayoutData(new GridData());
		butUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tNodes.getSelectionCount() > 0) {
					int index = tNodes.getSelectionIndex();
					if(index > 0) {											
						listener.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), index);
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
						listener.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText(), index);
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
		gFileOrderSource = new Group(jobChainGroup, SWT.NONE);
		final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_10.heightHint = 169;
		gFileOrderSource.setLayoutData(gridData_10);
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
		gridData_9.minimumHeight = 40;
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

	}





	private void fillChain(boolean enable, boolean isNew) {
		listener.fillFileOrderSource(tFileOrderSource);
		listener.fillChain(tNodes);
		gNodes.setText(GROUP_NODES_TITLE + " for: " + listener.getChainName());
		gFileOrderSource.setText(GROUP_FILEORDERSOURCE_TITLE + " for: " + listener.getChainName());
		bNewFileOrderSource.setEnabled(true);
		bNewNode.setEnabled(true);
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
		if(!listener.get_dom().isLifeElement())
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
		
		if(Utils.isRegExpressions(tRegex.getText())) {
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
		} else {
			MainWindow.message(tRegex.getText() + " is not a Regular expression.", SWT.ICON_INFORMATION);
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
		cJob.setToolTipText(Messages.getTooltip("job_chains.node.job"));
		cNextState.setToolTipText(Messages.getTooltip("job_chains.node.next_state"));
		bApplyNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_apply"));
		bFullNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_full_node"));
		bEndNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_end_node"));
		bFileSink.setToolTipText(Messages.getTooltip("job_chains.node.btn_filesink_node"));
		butDetailsJob.setToolTipText(Messages.getTooltip("job_chains.node.details"));
		butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		butDown.setToolTipText(Messages.getTooltip("button_down"));
		butUp.setToolTipText(Messages.getTooltip("button_up"));
		if(butImportJob != null ) butImportJob.setToolTipText(Messages.getTooltip("jobs.assistent"));
		bNewNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_new"));
		tDelay.setToolTipText(Messages.getTooltip("job_chains.node.delay"));
		cOnError.setToolTipText(Messages.getTooltip("job_chains.node.on_error"));
		bRemoveFile.setToolTipText(Messages.getTooltip("job_chains.node.btn_remove"));
		tMoveTo.setToolTipText(Messages.getTooltip("job_chain.stop_options.move_to"));
		tNodes.setToolTipText(Messages.getTooltip("job_chains.chain.node_table"));
		tDirectory.setToolTipText(Messages.getTooltip("job_chain.monitoring_directory"));
		tDelayAfterError.setToolTipText(Messages.getTooltip("job_chain.delay_after_error"));
		tRegex.setToolTipText(Messages.getTooltip("job_chain.regex"));
		tRepeat.setToolTipText(Messages.getTooltip("job_chain.repeat")); 
		tMax.setToolTipText(Messages.getTooltip("job_chain.max"));
		tNextState.setToolTipText(Messages.getTooltip("job_chain.next_state"));
		tFileOrderSource.setToolTipText(Messages.getTooltip("job_chain.table"));
		bApplyFileOrderSource.setToolTipText(Messages.getTooltip("job_chain.btn_apply"));
		bNewFileOrderSource.setToolTipText(Messages.getTooltip("job_chain.btn_new"));
		bRemoveFileOrderSource.setToolTipText(Messages.getTooltip("job_chain.btn_remove"));
		butGoto.setToolTipText(Messages.getTooltip("goto"));
	}

	//ein Job Chain hat entweder job_chain_node ODER job_chain_node.job_chain Kindknoten.
	private boolean check() {
		try {
			XPath x3 = XPath.newInstance("//job_chain[@name='"+ listener.getChainName() + "']/job_chain_node.job_chain");				 
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