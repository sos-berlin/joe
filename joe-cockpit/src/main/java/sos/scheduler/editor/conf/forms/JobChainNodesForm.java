package sos.scheduler.editor.conf.forms;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.JobchainNodeReturnCodeDialog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.scheduler.editor.conf.listeners.JobChainListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.wizard.forms.JobAssistentImportJobsForm;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;


public class JobChainNodesForm extends SOSJOEMessageCodes implements IUnsaved {
    private final String                                    conClassName                = "JobChainNodesForm";
    final String                                            conMethodName               = conClassName + "::enclosing_method";
    @SuppressWarnings("unused") private final String        conSVNVersion               = "$Id$";
    @SuppressWarnings("unused") private static final Logger logger                      = Logger.getLogger(JobChainNodesForm.class);
    private Button                                          dumm2                       = null;
    private Button                                          bNewNode                    = null;
    private Table                                           tNodes                      = null;
    private Button                                          bApplyNode                  = null;
    private Text                                            tMoveTo                     = null;
    private Button                                          bRemoveFile                 = null;
    private Combo                                           cErrorState                 = null;
    private Label                                           label9                      = null;
    private Combo                                           cNextState                  = null;
    @SuppressWarnings("unused") private Label               label8                      = null;
    private Button                                          bFileSink                   = null;
    private Button                                          bEndNode                    = null;
    private Button                                          bFullNode                   = null;
    private Composite                                       cType                       = null;
    private Combo                                           cJob                        = null;
    @SuppressWarnings("unused") private Label               label7                      = null;
    private Text                                            tState                      = null;
    private Label                                           label6                      = null;
    private static final String                             GROUP_FILEORDERSOURCE_TITLE = "File Order Sources";
    private Group                                           gFileOrderSource            = null;
    private JobChainListener                                listener                    = null;
    private Group                                           jobChainGroup               = null;
    private Button                                          bNewFileOrderSource         = null;
    private Button                                          bRemoveFileOrderSource      = null;
    private Button                                          bApplyFileOrderSource       = null;
    private Text                                            tDirectory                  = null;
    private Text                                            tDelayAfterError            = null;
    private Text                                            tMax                        = null;
    private Text                                            tNextState                  = null;
    private Text                                            tRegex                      = null;
    private Text                                            tRepeat                     = null;
    private Table                                           tFileOrderSource            = null;
    private Button                                          bRemoveNode                 = null;
    private Group                                           gNodes;
    private Text                                            tDelay                      = null;
    private Button                                          butImportJob                = null;
    private Button                                          butReturnCode                = null; 
    private boolean                                         refresh                     = false;
    private Button                                          butDetailsJob               = null;
    private Button                                          butBrowse                   = null;
    private ISchedulerUpdate                                update                      = null;
    private Combo                                           cOnError                    = null;
    private Button                                          butUp                       = null;
    private Button                                          butDown                     = null;
    private SchedulerDom                                    dom                         = null;
    private Button                                          butGoto                     = null;
    private Button                                          butInsert                   = null;
    private boolean                                         isInsert                    = false;
    private Button                                          reorderButton               = null;
    private Button                                          butAddMissingNodes          = null;
 
    private boolean                                         checkParameter              = false;

 
    public JobChainNodesForm(Composite parent, int style, SchedulerDom dom_, Element jobChain) {
        super(parent, style);
        dom = dom_;
        listener = new JobChainListener(dom, jobChain);
        initialize();
        boolean existChainNodes = check();
        jobChainGroup.setEnabled(existChainNodes);
        bNewNode.setEnabled(existChainNodes);
        if (existChainNodes)
            fillChain(false, false);
        this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
    }
 

    @Override public void apply() {
        if (bApplyNode.isEnabled())
            applyNode();
    }

    @Override public boolean isUnsaved() {
        return bApplyNode.isEnabled();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new Point(776, 664));
    }

    /**
     * This method initializes group
     */
    private void createGroup() {
        try {
            jobChainGroup = new Group(this, SWT.NONE);
            final GridLayout gridLayout = new GridLayout();
            gridLayout.marginHeight = 0;
            jobChainGroup.setLayout(gridLayout);
            gNodes = new Group(jobChainGroup, SWT.NONE);
            GridData gd_gNodes = new GridData(SWT.FILL, GridData.FILL, true, true);
            gd_gNodes.heightHint = 379;
            gd_gNodes.widthHint = 300;
            gd_gNodes.minimumHeight = 379;
            gNodes.setLayoutData(gd_gNodes);
            gNodes.setText(JOE_M_JCNodesForm_NodesGroup.params(listener.getChainName())); // Chain Nodes for 'CHAINNAME'
            final GridLayout gridLayout_3 = new GridLayout();
            gridLayout_3.marginBottom = 5;
            gridLayout_3.marginTop = 5;
            gridLayout_3.numColumns = 6;
            gNodes.setLayout(gridLayout_3);
            label6 = JOE_L_JobChainNodes_State.Control(new Label(gNodes, SWT.NONE));
            label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
            new Label(gNodes, SWT.NONE);
            tState = JOE_T_JobChainNodes_State.Control(new Text(gNodes, SWT.BORDER));
            tState.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    boolean valid = listener.isValidState(tState.getText());
                    if (!valid)
                        tState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                    else
                        tState.setBackground(null);
                    bApplyNode.setEnabled(isValidNode() && valid);
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            final GridData gridData18 = new GridData(SWT.FILL, GridData.CENTER, false, false, 3, 1);
            gridData18.widthHint = 300;
            tState.setLayoutData(gridData18);
            bApplyNode = JOE_B_JobChainNodes_ApplyNode.Control(new Button(gNodes, SWT.NONE));
            bApplyNode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    applyNode();
                }
            });
            bApplyNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            bApplyNode.setEnabled(false);
            label7 = JOE_L_JCNodesForm_Job.Control(new Label(gNodes, SWT.NONE));
            butGoto = JOE_B_JobChainNodes_Goto.Control(new Button(gNodes, SWT.ARROW | SWT.DOWN));
            butGoto.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    ContextMenu.goTo(cJob.getText(), dom, JOEConstants.JOB);
                }
            });
            butGoto.setAlignment(SWT.RIGHT);
            cJob = JOE_Cbo_JCNodesForm_Job.Control(new Combo(gNodes, SWT.BORDER));
            cJob.setVisibleItemCount(9);
            cJob.setMenu(new sos.scheduler.editor.app.ContextMenu(cJob, dom, JOEConstants.JOB).getMenu());
           
    		
            cJob.addMouseListener(new MouseAdapter() {
                @Override public void mouseDown(final MouseEvent e) {
                    if (refresh) {
                        if (listener.getJobs() != null) {
                            cJob.setItems(listener.getJobs());
                            refresh = false;
                        }
                    }
                }
            });
            cJob.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            cJob.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            final GridData gridData13 = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
            gridData13.widthHint = 300;
            cJob.setLayoutData(gridData13);
            final Composite composite = new Composite(gNodes, SWT.NONE);
            composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
            final GridLayout gridLayout_2 = new GridLayout();
            gridLayout_2.marginWidth = 0;
            gridLayout_2.marginHeight = 0;
            gridLayout_2.numColumns = 2;
            composite.setLayout(gridLayout_2);
            butBrowse = JOE_B_JobChainNodes_Browse.Control(new Button(composite, SWT.NONE));
            new Label(composite, SWT.NONE);
            GridData gd_butBrowse = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gd_butBrowse.horizontalSpan = 2;
            butBrowse.setLayoutData(gd_butBrowse);
            butBrowse.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
                    if (jobname != null && jobname.length() > 0)
                        cJob.setText(jobname);
                }
            });
            // }
            label8 = JOE_L_JobChainNodes_NextState.Control(new Label(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);
            cNextState = JOE_Cbo_JobChainNodes_NextState.Control(new Combo(gNodes, SWT.NONE));
            cNextState.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cNextState.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            cNextState.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
         
            @SuppressWarnings("unused") final Label delayLabel = JOE_L_JCNodesForm_Delay.Control(new Label(gNodes, SWT.NONE));
            tDelay = JOE_T_JCNodesForm_Delay.Control(new Text(gNodes, SWT.BORDER));
            tDelay.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            //            tDelay.addKeyListener(new KeyAdapter() {
            //                public void keyPressed(final KeyEvent e) {
            //                }
            //            });
            final GridData gridData_8 = new GridData(SWT.FILL, GridData.CENTER, true, false);
            gridData_8.minimumWidth = 35;
            gridData_8.widthHint = 222;
            tDelay.setLayoutData(gridData_8);
            // if(!listener.get_dom().isLifeElement()) {
            butImportJob = JOE_B_JCNodesForm_ImportJob.Control(new Button(gNodes, SWT.NONE));
            butImportJob.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            butImportJob.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                     
                    JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(listener.get_dom(), update, JOEConstants.JOB_CHAINS);
                    importJobs.setJobname(cJob);
                    importJobs.showAllImportJobs("order");
                    if (!listener.get_dom().isLifeElement())
                        update.updateOrders();
                    refresh = true;
                }
            });
            label9 = JOE_L_JobChainNodes_ErrorState.Control(new Label(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);
             
            cErrorState = JOE_Cbo_JobChainNodes_ErrorState.Control(new Combo(gNodes, SWT.NONE));
            cErrorState.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            cErrorState.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            final GridData gridData15 = new GridData(SWT.FILL, GridData.CENTER, false, false);
            gridData15.widthHint = 80;
            cErrorState.setLayoutData(gridData15);
            @SuppressWarnings("unused") final Label onErrorLabel = JOE_L_JCNodesForm_OnError.Control(new Label(gNodes, SWT.NONE));
            cOnError = JOE_Cbo_JCNodesForm_OnError.Control(new Combo(gNodes, SWT.READ_ONLY));
            cOnError.setItems(new String[] { "", JOE_M_JCNodesForm_Setback.label(), JOE_M_JCNodesForm_Suspend.label() });
            cOnError.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            cOnError.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            final GridData gridData_12 = new GridData(SWT.FILL, GridData.CENTER, true, false);
            gridData_12.widthHint = 195;
            gridData_12.minimumWidth = 20;
            cOnError.setLayoutData(gridData_12);
            bNewNode = JOE_B_JCNodesForm_NewNode.Control(new Button(gNodes, SWT.NONE));
            bNewNode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    isInsert = false;
                    getShell().setDefaultButton(null);
                    tNodes.deselectAll();
                    butDetailsJob.setEnabled(false);
                    listener.selectNode(null);
                    bRemoveNode.setEnabled(false);
                    enableNode(true);
                    fillNode(true);
                    tState.setFocus();
                    cNextState.setVisibleItemCount(0);
                }
            });
            bNewNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
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
            bFullNode = JOE_B_JCNodesForm_FullNode.Control(new Button(cType, SWT.RADIO));
            bFullNode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
            bFullNode.setSelection(true);
            bEndNode = JOE_B_JCNodesForm_EndNode.Control(new Button(cType, SWT.RADIO));
            bEndNode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
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
            bFileSink = JOE_B_JCNodesForm_FileSink.Control(new Button(cType, SWT.RADIO));
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData.widthHint = 71;
            bFileSink.setLayoutData(gridData);
            bFileSink.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
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
            // bFileSink.setEnabled(false);
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
            @SuppressWarnings("unused") final Label removeFileLabel = JOE_L_JCNodesForm_RemoveFile.Control(new Label(composite_3, SWT.NONE));
            bRemoveFile = JOE_B_JCNodesForm_RemoveFile.Control(new Button(composite_3, SWT.CHECK));
            final GridData gridData_1 = new GridData();
            gridData_1.horizontalIndent = 5;
            bRemoveFile.setLayoutData(gridData_1);
            bRemoveFile.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (bRemoveFile.getSelection())
                        tMoveTo.setText("");
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            bRemoveFile.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            bRemoveFile.setEnabled(false);
            @SuppressWarnings("unused") final Label movweToLabel = JOE_L_JCNodesForm_MoveTo.Control(new Label(composite_3, SWT.NONE));
            tMoveTo = JOE_T_JCNodesForm_MoveTo.Control(new Text(composite_3, SWT.BORDER));
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_3.horizontalIndent = 5;
            tMoveTo.setLayoutData(gridData_3);
            tMoveTo.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    if (!tMoveTo.getText().equals(""))
                        bRemoveFile.setSelection(false);
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            });
            tMoveTo.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            tMoveTo.setEnabled(false);
            butInsert = JOE_B_JCNodesForm_Insert.Control(new Button(gNodes, SWT.NONE));
            butInsert.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    isInsert = true;
                    String state = tState.getText();
                    tState.setText("");
                    tDelay.setText("");
                    cErrorState.setText("");
                    cOnError.setText("");
                    cJob.setText("");
                    enableNode(true);
                    bFullNode.setSelection(true);
                    bEndNode.setSelection(false);
                    // nächste status
                    cNextState.setText(state);
                }
            });
            butInsert.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            tNodes = JOE_Tbl_JCNodesForm_Nodes.Control(new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER));
            tNodes.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    selectNodes();
                }
            });
            tNodes.setLinesVisible(true);
            tNodes.setHeaderVisible(true);
            final GridData gridData4 = new GridData(SWT.FILL, GridData.FILL, false, true, 5, 5);
            gridData4.widthHint = 451;
            gridData4.heightHint = 112;
            tNodes.setLayoutData(gridData4);
            final TableColumn tableColumn3 = JOE_TCl_JCNodesForm_State.Control(new TableColumn(tNodes, SWT.NONE));
            tableColumn3.setWidth(90);
            final TableColumn newColumnTableColumn_3 = JOE_TCl_JCNodesForm_Node.Control(new TableColumn(tNodes, SWT.NONE));
            newColumnTableColumn_3.setWidth(100);
            final TableColumn tableColumn4 = JOE_TCl_JCNodesForm_JobDir.Control(new TableColumn(tNodes, SWT.NONE));
            tableColumn4.setWidth(200);
            final TableColumn tableColumn5 = JOE_TCl_JCNodesForm_NextState.Control(new TableColumn(tNodes, SWT.NONE));
            tableColumn5.setWidth(90);
            final TableColumn tableColumn6 = JOE_TCl_JCNodesForm_ErrorState.Control(new TableColumn(tNodes, SWT.NONE));
            tableColumn6.setWidth(90);
            final TableColumn newColumnTableColumn_4 = JOE_TCl_JCNodesForm_OnError.Control(new TableColumn(tNodes, SWT.NONE));
            newColumnTableColumn_4.setWidth(100);
            final Composite composite_1 = new Composite(gNodes, SWT.NONE);
            composite_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            final GridLayout gridLayout_5 = new GridLayout();
            gridLayout_5.marginWidth = 0;
            gridLayout_5.marginHeight = 0;
            gridLayout_5.numColumns = 3;
            composite_1.setLayout(gridLayout_5);
            butUp = JOE_B_Up.Control(new Button(composite_1, SWT.NONE));
            butUp.setText("");
            butUp.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int index = tNodes.getSelectionIndex();
                        if (index > 0) {
                            listener.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(),
                                    tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), index,
                                    reorderButton.getSelection());
                            selectNodes();
                        }
                    }
                }
            });
            butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
            butDown = JOE_B_Down.Control(new Button(composite_1, SWT.NONE));
            butDown.setText("");
            butDown.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int index = tNodes.getSelectionIndex();
                        if (index == tNodes.getItemCount() - 1) {
                            // System.out.println("Datensatz ist bereits ganz unten.");
                        }
                        else
                            if (index >= 0) {
                                listener.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(),
                                        tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), index,
                                        reorderButton.getSelection());
                                selectNodes();
                            }
                    }
                }
            });
            butDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butDown.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
            reorderButton = JOE_B_JCNodesForm_Reorder.Control(new Button(composite_1, SWT.CHECK));
            reorderButton.setSelection(true);
            butDetailsJob = JOE_B_JCNodesForm_Details.Control(new Button(gNodes, SWT.NONE));
            butDetailsJob.setEnabled(false);
            butDetailsJob.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        showDetails(tNodes.getSelection()[0].getText(0), cJob.getText());
                        // tNodes.deselectAll();
                        // selectNodes();
                        // butDetailsJob.setEnabled(false);
                        checkParameter = true;
                    }
                }
            });
            butDetailsJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            butDetailsJob.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(final FocusEvent e) {
                    if (checkParameter) {
                        listener.fillChain(tNodes);
                        checkParameter = false;
                    }
                }
            });
            butAddMissingNodes = JOE_B_JCNodesForm_AddMissingNodes.Control(new Button(gNodes, SWT.NONE));
            butAddMissingNodes.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    try {
                        if (tNodes.getSelectionCount() > 0) {
                            TableItem item = tNodes.getSelection()[0];
                            if (!listener.checkForState(item.getText(3))) {
                                listener.selectNode(null);
                                listener.applyNode(true, item.getText(3), "", "", "", "", false, "", "");
                            }
                            if (!listener.checkForState(item.getText(4))) {
                                listener.selectNode(null);
                                listener.applyNode(true, item.getText(4), "", "", "", "", false, "", "");
                            }
                            listener.fillChain(tNodes);
                            bApplyNode.setEnabled(false);
                            bRemoveNode.setEnabled(false);
                            listener.selectNode(null);
                            fillNode(true);
                            enableNode(false);
                            
                        }
                    }
                    catch (Exception ex) {
                        try {
                            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), ex);
                        }
                        catch (Exception ee) {
                            // tu nichts
                        }
                    }
                }
            });
            butAddMissingNodes.setEnabled(false);
            butAddMissingNodes.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            
            
            butReturnCode = JOE_B_JCNodesForm_ReturnCodes.Control(new Button(gNodes, SWT.NONE));
            butReturnCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            butReturnCode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    JobchainNodeReturnCodeDialog jobchainNodeReturnCodeDialog = new JobchainNodeReturnCodeDialog(getShell(), 0, listener);
                    JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements = new JobchainListOfReturnCodeElements(listener.getJobchainListOfReturnCodeElements());
                   
                    jobchainNodeReturnCodeDialog.setJobchainListOfReturnCodeElements(jobchainListOfReturnCodeElements);
                    jobchainNodeReturnCodeDialog.execute();
                    if (jobchainNodeReturnCodeDialog.isOk()){
                        listener.setJobchainListOfReturnCodeElements(jobchainNodeReturnCodeDialog.getJobchainListOfReturnCodeElements());
                        bApplyNode.setEnabled(true);
                    }
                                
                }
            });

            bRemoveNode = JOE_B_JCNodesForm_Remove.Control(new Button(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);
            bRemoveNode.setEnabled(false);
            
            bRemoveNode.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int c = MainWindow.message(getShell(), JOE_M_JCNodesForm_Remove.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						if (c != SWT.YES){
                            return;
						}
                        int index = tNodes.getSelectionIndex();
                        listener.deleteNode(tNodes);
                        tNodes.remove(index);
						if (index >= tNodes.getItemCount()){
                            index--;
						}
                        boolean empty = tNodes.getItemCount() == 0;
                        fillNode(empty);
                        enableNode(!empty);
                        bRemoveNode.setEnabled(!empty);
                        if (!empty) {
                            tNodes.select(index);
                            listener.selectNode(tNodes);
                        }
                        else {
                            listener.selectNode(null);
                        }
                        listener.updateSelectedJobChain();
                    }
                }
            });
            bRemoveNode.setLayoutData(new GridData(GridData.FILL, GridData.END, false, false));
            gFileOrderSource = new Group(jobChainGroup, SWT.NONE);
            final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_10.heightHint = 220;
            gridData_10.minimumHeight = 150;
            gFileOrderSource.setLayoutData(gridData_10);
            gFileOrderSource.setText(JOE_G_JCNodesForm_FileOrderSources.params(listener.getChainName()));
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.marginTop = 5;
            gridLayout_1.marginBottom = 5;
            gridLayout_1.numColumns = 5;
            gFileOrderSource.setLayout(gridLayout_1);
            final Label directoryLabel = JOE_L_JCNodesForm_Directory.Control(new Label(gFileOrderSource, SWT.NONE));
            directoryLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
            tDirectory = JOE_T_JCNodesForm_Directory.Control(new Text(gFileOrderSource, SWT.BORDER));
            tDirectory.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tDirectory.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                }
            });
            tDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            @SuppressWarnings("unused") final Label delay_after_errorLabel = JOE_L_JCNodesForm_DelayAfterError.Control(new Label(gFileOrderSource, SWT.NONE));
            tDelayAfterError = JOE_T_JCNodesForm_DelayAfterError.Control(new Text(gFileOrderSource, SWT.BORDER));
            tDelayAfterError.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tDelayAfterError.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            bApplyFileOrderSource = JOE_B_JCNodesForm_ApplyFileOrderSource.Control(new Button(gFileOrderSource, SWT.NONE));
            bApplyFileOrderSource.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    applyFileOrderSource();
                }
            });
            bApplyFileOrderSource.setEnabled(false);
            bApplyFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            final Label regexLabel = JOE_L_JCNodesForm_Regex.Control(new Label(gFileOrderSource, SWT.NONE));
            regexLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
            tRegex = JOE_T_JCNodesForm_Regex.Control(new Text(gFileOrderSource, SWT.BORDER));
            tRegex.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tRegex.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            @SuppressWarnings("unused") final Label repeatLabel = JOE_L_JCNodesForm_Repeat.Control(new Label(gFileOrderSource, SWT.NONE));
            tRepeat = JOE_T_JCNodesForm_Repeat.Control(new Text(gFileOrderSource, SWT.BORDER));
            tRepeat.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tRepeat.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            new Label(gFileOrderSource, SWT.NONE);
            @SuppressWarnings("unused") final Label maxLabel = JOE_L_JCNodesForm_Max.Control(new Label(gFileOrderSource, SWT.NONE));
            tMax = JOE_T_JCNodesForm_Max.Control(new Text(gFileOrderSource, SWT.BORDER));
            tMax.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tMax.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            @SuppressWarnings("unused") final Label stateLabel = JOE_L_JCNodesForm_NextState.Control(new Label(gFileOrderSource, SWT.NONE));
            tNextState = JOE_T_JCNodesForm_NextState.Control(new Text(gFileOrderSource, SWT.BORDER));
            tNextState.addModifyListener(new ModifyListener() {
                @Override public void modifyText(final ModifyEvent e) {
                    bApplyFileOrderSource.setEnabled(isValidSource());
                    if (bApplyFileOrderSource.getEnabled())
                        getShell().setDefaultButton(bApplyFileOrderSource);
                }
            });
            tNextState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            dumm2 = JOE_B_JCNodesForm_RemoveFileOrderSource.Control(new Button(gFileOrderSource, SWT.NONE));
            dumm2.setVisible(false);
            dumm2.setEnabled(false);
            tFileOrderSource = JOE_Tbl_JCNodesForm_FileOrderSource.Control(new Table(gFileOrderSource, SWT.BORDER));
            tFileOrderSource.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
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
            final TableColumn newColumnTableColumn = JOE_TCl_JCNodesForm_Directory.Control(new TableColumn(tFileOrderSource, SWT.NONE));
            newColumnTableColumn.setWidth(300);
            final TableColumn newColumnTableColumn_1 = JOE_TCl_JCNodesForm_Regex.Control(new TableColumn(tFileOrderSource, SWT.NONE));
            newColumnTableColumn_1.setWidth(200);
            final TableColumn newColumnTableColumn_2 = JOE_TCl_JCNodesForm_NextState.Control(new TableColumn(tFileOrderSource, SWT.NONE));
            newColumnTableColumn_2.setWidth(100);
            bNewFileOrderSource = JOE_B_JCNodesForm_NewFileOrderSource.Control(new Button(gFileOrderSource, SWT.NONE));
            bNewFileOrderSource.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
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
            bRemoveFileOrderSource = JOE_B_JCNodesForm_RemoveFileOrderSource.Control(new Button(gFileOrderSource, SWT.NONE));
            new Label(gFileOrderSource, SWT.NONE);
            new Label(gFileOrderSource, SWT.NONE);
            bRemoveFileOrderSource.addSelectionListener(new SelectionAdapter() {
                @Override public void widgetSelected(final SelectionEvent e) {
                    if (tFileOrderSource.getSelectionCount() > 0) {
                        //bFileSink.setEnabled(tFileOrderSource.getItemCount() > 0);
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
                        }
                        else {
                            listener.selectFileOrderSource(null);
                        }
                    }
                }
            });
            bRemoveFileOrderSource.setEnabled(false);
            bRemoveFileOrderSource.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true));
        }
        catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
                // tu nichts
            }
        }
    }

    private void fillChain(boolean enable, boolean isNew) {
        listener.fillFileOrderSource(tFileOrderSource);
        listener.fillChain(tNodes);
        bNewFileOrderSource.setEnabled(true);
        bNewNode.setEnabled(true);
        enableNode(false);
        enableFileOrderSource(false);
    }

    private void enableNode(boolean enable) {
        bFullNode.setEnabled(enable);
        bEndNode.setEnabled(enable);
        //bFileSink.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
        bFileSink.setEnabled(enable);
        tState.setEnabled(enable);
        cJob.setEnabled(enable);
        cNextState.setEnabled(enable);
        cErrorState.setEnabled(enable);
        cOnError.setEnabled(enable);
        tDelay.setEnabled(enable);
        if (!listener.get_dom().isLifeElement())
            butImportJob.setEnabled(enable);
        butReturnCode.setEnabled(enable);
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
        try {
            butAddMissingNodes.setEnabled(false);
            boolean fullNode = listener.isFullNode();
            boolean fileSinkNode = listener.isFileSinkNode();
            boolean endNode = !fullNode && !fileSinkNode;
            bFullNode.setSelection(clear || fullNode);
            bEndNode.setSelection(!clear && endNode);
            bFileSink.setSelection(!clear && fileSinkNode && tFileOrderSource.getItemCount() > 0);
            tDelay.setEnabled(fullNode);
            cNextState.setEnabled(fullNode);
            cErrorState.setEnabled(fullNode);
            cOnError.setEnabled(fullNode);
            cJob.setEnabled(fullNode);
            tMoveTo.setEnabled(fileSinkNode && tFileOrderSource.getItemCount() > 0);
            bRemoveFile.setEnabled(fileSinkNode && tFileOrderSource.getItemCount() > 0);
            tState.setText(clear ? "" : listener.getState());
            tDelay.setText(clear ? "" : listener.getDelay());
            cJob.setItems(listener.getJobs());
            if (listener.getStates().length > 0){
                cNextState.setItems(listener.getStates());
            }
            if (listener.getAllStates().length > 0){
                cErrorState.setItems(listener.getAllStates());
            }
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
        catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
                // tu nichts
            }
        }
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
        try {
            cNextState.setVisibleItemCount(5);
            String msg = "";
            if (!listener.isValidState(tState.getText()))
                msg = JOE_M_JobChain_StateAlreadyDefined.label();
            if (!msg.equals("")) {
                MainWindow.message(msg, SWT.ICON_INFORMATION);
            }
            else {
                if (isInsert){
                    listener.applyInsertNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(),
                            cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), cOnError.getText());
                }else{
                    listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(),
                            cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), cOnError.getText());
                }
                DetailsListener.checkDetailsParameter(tState.getText(), listener.getChainName(), cJob.getText(), dom, update);
                listener.fillChain(tNodes);
                bApplyNode.setEnabled(false);
                bRemoveNode.setEnabled(false);
                listener.selectNode(null);
                fillNode(true);
                enableNode(false);
            }
            isInsert = false;
        	listener.updateSelectedJobChain();
        }
        catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
                // tu nichts
            }
        }
    }

    private void applyFileOrderSource() {
        if (Utils.isRegExpressions(tRegex.getText())) {
            listener.applyFileOrderSource(tDirectory.getText(), tRegex.getText(), tNextState.getText(), tMax.getText(), tRepeat.getText(),
                    tDelayAfterError.getText());
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
        else {
            MainWindow.message(JOE_M_NoRegex.params(tRegex.getText()), SWT.ICON_INFORMATION);
        }
    }

    private boolean isValidNode() {
        if (tState.getText().equals("") || bFullNode.getSelection() && cJob.getText().equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isValidSource() {
        if (tDirectory.getText().equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    public void setISchedulerUpdate(ISchedulerUpdate update_) {
        update = update_;
        listener.setISchedulerUpdate(update_);
    }

    private void showDetails(String state, String jobname) {
        boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();
        if (state == null) {
            DetailDialogForm detail = new DetailDialogForm(listener.getChainName(), isLifeElement, listener.get_dom().getFilename());
            detail.showDetails();
            detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update, jobname);
        }
        else {
            DetailDialogForm detail = new DetailDialogForm(listener.getChainName(), state, null, isLifeElement, listener.get_dom().getFilename());
            detail.showDetails();
            detail.getDialogForm().setParamsForWizzard(listener.get_dom(), update, jobname);
        }
    }

    private void selectNodes() {
        if (bApplyNode.isEnabled()) {
            int c = MainWindow.message(getShell(), JOE_M_ApplyChanges.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            if (c == SWT.YES) {
                applyNode();
            }
        }
        
        listener.setJobchainListOfReturnCodeElements(new JobchainListOfReturnCodeElements());
 

        if (tNodes.getSelectionCount() > 0) {
            listener.selectNode(tNodes);
            enableNode(true);
            fillNode(false);
            butDetailsJob.setEnabled(true);
            butAddMissingNodes.setEnabled(!listener.checkForState(tNodes.getSelection()[0].getText(3))
                    || !listener.checkForState(tNodes.getSelection()[0].getText(4)));
        }
        else {
            butDetailsJob.setEnabled(false);
            butAddMissingNodes.setEnabled(false);
        }
        bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
    }

 

    // ein Job Chain hat entweder job_chain_node ODER job_chain_node.job_chain
    // Kindknoten.
    private boolean check() {
        try {
            XPath x3 = XPath.newInstance("//job_chain[@name='" + listener.getChainName() + "']/job_chain_node.job_chain");
            List listOfElement_3 = x3.selectNodes(dom.getDoc());
            if (listOfElement_3.isEmpty())
                return true;
            else
                return false;
        }
        catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
                // tu nichts
            }
            return true;
        }
    }
} // @jve:decl-index=0:visual-constraint="10,10"
