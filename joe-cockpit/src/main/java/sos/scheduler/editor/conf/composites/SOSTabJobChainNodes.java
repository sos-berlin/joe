package sos.scheduler.editor.conf.composites;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.JobchainNodeReturnCodeDialog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.conf.forms.DetailDialogForm;
import sos.scheduler.editor.conf.forms.JobChainNodesForm;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.conf.listeners.JobChainFileWatchingListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.wizard.forms.JobAssistentImportJobsForm;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.*;

public class SOSTabJobChainNodes extends CTabItem {

    private final Composite composite;
    private final String conClassName = "JobChainNodesForm";
    final String conMethodName = conClassName + "::enclosing_method";
    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(JobChainNodesForm.class);
    private Button bNewNode = null;
    private Table tNodes = null;
    private Button bApplyNode = null;
    private Combo cErrorState = null;
    private Combo cNextState = null;
    @SuppressWarnings("unused")
    private Label label8 = null;
    private Text tMoveTo = null;
    private Button bRemoveFile = null;
    private Button bFileSink = null;
    private Button bEndNode = null;
    private Button bFullNode = null;
    private Combo cJob = null;
    @SuppressWarnings("unused")
    private Label label7 = null;
    private Text tState = null;
    private Label label6 = null;
    private JobChainListener jobchainDataProvider = null;
    private Button bRemoveNode = null;
    private Group gNodes;
    private Text tDelay = null;
    private Button butImportJob = null;
    private Button butReturnCode = null;
    private boolean refresh = false;
    private Button butDetailsJob = null;
    private Button butBrowse = null;
    private Combo cOnError = null;
    private Button butUp = null;
    private Button butDown = null;
    private Button butGoto = null;
    private Button butInsert = null;
    private Button reorderButton = null;
    private Button butAddMissingNodes = null;

    private boolean isInsert = false;
    private boolean checkParameter = false;

    public SOSTabJobChainNodes(final String caption, final CTabFolder parent, JobChainListener listener_) {
        super(parent, SWT.NONE);
        setText(caption);
        jobchainDataProvider = listener_;

        composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        createContents();

        boolean isJobchain = !jobchainDataProvider.isNestedJobchain();
        gNodes.setEnabled(isJobchain);
        bNewNode.setEnabled(isJobchain);
        if (isJobchain) {
            initJobChain();
        }

        composite.setEnabled(Utils.isElementEnabled("job_chain", jobchainDataProvider.getDom(), jobchainDataProvider.getChain()));
        this.setControl(composite);
        composite.layout();
    }

    /** Create contents of the window */
    protected void createContents() {

        try {

            gNodes = new Group(composite, SWT.NONE);

            GridData gd_gNodes = new GridData(SWT.FILL, GridData.FILL, true, true);
            gd_gNodes.heightHint = 379;
            gd_gNodes.widthHint = 300;
            gd_gNodes.minimumHeight = 379;
            gNodes.setLayoutData(gd_gNodes);
            gNodes.setText(JOE_M_JCNodesForm_NodesGroup.params(jobchainDataProvider.getChainName())); // Chain
                                                                                                      // Nodes
                                                                                                      // for
                                                                                                      // 'CHAINNAME'
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

                @Override
                public void modifyText(final ModifyEvent e) {
                    boolean valid = jobchainDataProvider.isValidState(tState.getText());
                    if (!valid) {
                        tState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                    } else {
                        tState.setBackground(null);
                    }
                    bApplyNode.setEnabled(isValidNode() && valid);
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            final GridData gridData18 = new GridData(SWT.FILL, GridData.CENTER, false, false, 3, 1);
            gridData18.widthHint = 300;
            tState.setLayoutData(gridData18);
            bApplyNode = JOE_B_JobChainNodes_ApplyNode.Control(new Button(gNodes, SWT.NONE));
            bApplyNode.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    applyNode();
                }
            });
            bApplyNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            bApplyNode.setEnabled(false);

            label7 = JOE_L_JCNodesForm_Job.Control(new Label(gNodes, SWT.NONE));
            butGoto = JOE_B_JobChainNodes_Goto.Control(new Button(gNodes, SWT.ARROW | SWT.DOWN));
            butGoto.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    ContextMenu.goTo(cJob.getText(), jobchainDataProvider.getDom(), JOEConstants.JOB);
                }
            });
            butGoto.setAlignment(SWT.RIGHT);
            cJob = JOE_Cbo_JCNodesForm_Job.Control(new Combo(gNodes, SWT.BORDER));
            cJob.setVisibleItemCount(9);
            cJob.setMenu(new sos.scheduler.editor.app.ContextMenu(cJob, jobchainDataProvider.getDom(), JOEConstants.JOB).getMenu());

            cJob.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(final MouseEvent e) {
                    if (refresh) {
                        if (jobchainDataProvider.getJobs() != null) {
                            cJob.setItems(jobchainDataProvider.getJobs());
                            refresh = false;
                        }
                    }
                }
            });
            cJob.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            cJob.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            final GridData gridData13 = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
            gridData13.widthHint = 300;
            cJob.setLayoutData(gridData13);

            butBrowse = JOE_B_JobChainNodes_Browse.Control(new Button(gNodes, SWT.NONE));

            GridData gd_butBrowse = new GridData(GridData.FILL, SWT.BOTTOM, false, false);
            butBrowse.setLayoutData(gd_butBrowse);
            butBrowse.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
                    if (jobname != null && jobname.length() > 0) {
                        cJob.setText(jobname);
                    }
                }
            });

            label8 = JOE_L_JobChainNodes_NextState.Control(new Label(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);
            cNextState = JOE_Cbo_JobChainNodes_NextState.Control(new Combo(gNodes, SWT.NONE));
            cNextState.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cNextState.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            cNextState.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });

            @SuppressWarnings("unused")
            final Label delayLabel = JOE_L_JCNodesForm_Delay.Control(new Label(gNodes, SWT.NONE));
            tDelay = JOE_T_JCNodesForm_Delay.Control(new Text(gNodes, SWT.BORDER));
            tDelay.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });

            final GridData gridData_8 = new GridData(SWT.FILL, GridData.CENTER, true, false);
            gridData_8.minimumWidth = 35;
            gridData_8.widthHint = 222;
            tDelay.setLayoutData(gridData_8);

            butImportJob = JOE_B_JCNodesForm_ImportJob.Control(new Button(gNodes, SWT.NONE));
            butImportJob.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            butImportJob.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {

                    JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(jobchainDataProvider.getDom(), jobchainDataProvider.getISchedulerUpdate(), JOEConstants.JOB_CHAINS);
                    importJobs.setJobname(cJob);
                    importJobs.showAllImportJobs("order");
                    if (!jobchainDataProvider.getDom().isLifeElement()) {
                        jobchainDataProvider.getISchedulerUpdate().updateOrders();
                    }
                    refresh = true;
                }
            });

            Label label9 = JOE_L_JobChainNodes_ErrorState.Control(new Label(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);

            cErrorState = JOE_Cbo_JobChainNodes_ErrorState.Control(new Combo(gNodes, SWT.NONE));
            cErrorState.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            cErrorState.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            final GridData gridData15 = new GridData(SWT.FILL, GridData.CENTER, false, false);
            gridData15.widthHint = 80;
            cErrorState.setLayoutData(gridData15);
            @SuppressWarnings("unused")
            final Label onErrorLabel = JOE_L_JCNodesForm_OnError.Control(new Label(gNodes, SWT.NONE));
            cOnError = JOE_Cbo_JCNodesForm_OnError.Control(new Combo(gNodes, SWT.READ_ONLY));
            cOnError.setItems(new String[] { "", JOE_M_JCNodesForm_Setback.label(), JOE_M_JCNodesForm_Suspend.label() });
            cOnError.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            cOnError.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
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

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    isInsert = false;
                    getShell().setDefaultButton(null);
                    tNodes.deselectAll();
                    butDetailsJob.setEnabled(false);
                    jobchainDataProvider.selectNode(null);
                    bRemoveNode.setEnabled(false);
                    enableNode(true);
                    fillNode(true);
                    tState.setFocus();
                }
            });
            bNewNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            butInsert = JOE_B_JCNodesForm_Insert.Control(new Button(gNodes, SWT.NONE));
            butInsert.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    isInsert = true;
                    String state = tState.getText();

                    cNextState.setItems(initStateCombo());
                    cErrorState.setItems(initStateCombo());

                    tState.setText("");
                    tDelay.setText("");
                    cErrorState.setText("");
                    cOnError.setText("");
                    cJob.setText("");
                    enableNode(true);
                    bFullNode.setSelection(true);
                    bEndNode.setSelection(false);
                    cNextState.setText(state);
                }
            });
            butInsert.setLayoutData(new GridData(GridData.FILL, SWT.BOTTOM, false, false));

            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);
            new Label(gNodes, SWT.NONE);

            @SuppressWarnings("unused")
            final Label removeFileLabel = JOE_L_JCNodesForm_RemoveFile.Control(new Label(gNodes, SWT.NONE));
            bRemoveFile = JOE_B_JCNodesForm_RemoveFile.Control(new Button(gNodes, SWT.CHECK));
            final GridData gridData_1 = new GridData();
            gridData_1.horizontalIndent = 5;
            bRemoveFile.setLayoutData(gridData_1);
            bRemoveFile.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (bRemoveFile.getSelection()) {
                        tMoveTo.setText("");
                    }
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            bRemoveFile.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            bRemoveFile.setEnabled(false);
            new Label(gNodes, SWT.NONE);

            bFullNode = JOE_B_JCNodesForm_FullNode.Control(new Button(gNodes, SWT.RADIO));
            bFullNode.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetDefaultSelected(final SelectionEvent e) {
                }
            });
            bFullNode.setSelection(true);
            bEndNode = JOE_B_JCNodesForm_EndNode.Control(new Button(gNodes, SWT.RADIO));
            bEndNode.addSelectionListener(new SelectionAdapter() {

                @Override
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
                        if (bApplyNode.getEnabled()) {
                            getShell().setDefaultButton(bApplyNode);
                        }
                    }
                    bApplyNode.setEnabled(isValidNode());
                }
            });
            bFileSink = JOE_B_JCNodesForm_FileSink.Control(new Button(gNodes, SWT.RADIO));
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData.widthHint = 71;
            bFileSink.setLayoutData(gridData);
            bFileSink.addSelectionListener(new SelectionAdapter() {

                @Override
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

                        if (tState.getText().equals("")) {
                            bApplyNode.setEnabled(false);
                        }
                    }
                }
            });

            @SuppressWarnings("unused")
            final Label moveToLabel = JOE_L_JCNodesForm_MoveTo.Control(new Label(gNodes, SWT.NONE));
            tMoveTo = JOE_T_JCNodesForm_MoveTo.Control(new Text(gNodes, SWT.BORDER));
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_3.horizontalIndent = 5;
            tMoveTo.setLayoutData(gridData_3);
            tMoveTo.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!tMoveTo.getText().equals("")) {
                        bRemoveFile.setSelection(false);
                    }
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled()) {
                        getShell().setDefaultButton(bApplyNode);
                    }
                }
            });
            tMoveTo.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        applyNode();
                    }
                }
            });
            tMoveTo.setEnabled(false);
            new Label(gNodes, SWT.NONE);

            tNodes = JOE_Tbl_JCNodesForm_Nodes.Control(new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER));
            tNodes.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
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

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int index = tNodes.getSelectionIndex();
                        if (index > 0) {
                            jobchainDataProvider.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), index, reorderButton.getSelection());
                            selectNodes();
                        }
                    }
                }
            });
            butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
            butDown = JOE_B_Down.Control(new Button(composite_1, SWT.NONE));
            butDown.setText("");
            butDown.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int index = tNodes.getSelectionIndex();
                        if (index == tNodes.getItemCount() - 1) {
                        } else if (index >= 0) {
                            jobchainDataProvider.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), index, reorderButton.getSelection());
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

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        showDetails(tNodes.getSelection()[0].getText(0), cJob.getText());
                        checkParameter = true;
                    }
                }
            });
            butDetailsJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            butDetailsJob.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(final FocusEvent e) {
                    if (checkParameter) {
                        jobchainDataProvider.fillChain(tNodes);
                        checkParameter = false;
                    }
                }
            });
            butAddMissingNodes = JOE_B_JCNodesForm_AddMissingNodes.Control(new Button(gNodes, SWT.NONE));
            butAddMissingNodes.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    try {
                        if (tNodes.getSelectionCount() > 0) {
                            TableItem item = tNodes.getSelection()[0];
                            if (!jobchainDataProvider.checkForState(item.getText(3))) {
                                jobchainDataProvider.selectNode(null);
                                jobchainDataProvider.applyNode(true, item.getText(3), "", "", "", "", false, "", "");
                            }
                            if (!jobchainDataProvider.checkForState(item.getText(4))) {
                                jobchainDataProvider.selectNode(null);
                                jobchainDataProvider.applyNode(true, item.getText(4), "", "", "", "", false, "", "");
                            }
                            jobchainDataProvider.fillChain(tNodes);
                            bApplyNode.setEnabled(false);
                            bRemoveNode.setEnabled(false);
                            jobchainDataProvider.selectNode(null);
                            fillNode(true);
                            enableNode(false);

                        }
                    } catch (Exception ex) {
                        try {
                            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), ex);
                        } catch (Exception ee) {
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

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    JobchainNodeReturnCodeDialog jobchainNodeReturnCodeDialog = new JobchainNodeReturnCodeDialog(getShell(), 0, jobchainDataProvider);
                    JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements = new JobchainListOfReturnCodeElements(jobchainDataProvider.getJobchainListOfReturnCodeElements());

                    jobchainNodeReturnCodeDialog.setJobchainListOfReturnCodeElements(jobchainListOfReturnCodeElements);
                    jobchainNodeReturnCodeDialog.execute();
                    if (jobchainNodeReturnCodeDialog.isOk()) {
                        jobchainDataProvider.setJobchainListOfReturnCodeElements(jobchainNodeReturnCodeDialog.getJobchainListOfReturnCodeElements());
                        bApplyNode.setEnabled(true);
                    }
                }
            });

            bRemoveNode = JOE_B_JCNodesForm_Remove.Control(new Button(gNodes, SWT.NONE));
            new Label(gNodes, SWT.NONE);
            bRemoveNode.setEnabled(false);

            bRemoveNode.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tNodes.getSelectionCount() > 0) {
                        int c = MainWindow.message(getShell(), JOE_M_JCNodesForm_Remove.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        if (c != SWT.YES) {
                            return;
                        }
                        int index = tNodes.getSelectionIndex();
                        jobchainDataProvider.deleteNode(tNodes);

                        jobchainDataProvider.fillChain(tNodes);

                        if (index >= tNodes.getItemCount()) {
                            index--;
                        }
                        boolean empty = tNodes.getItemCount() == 0;
                        fillNode(empty);
                        enableNode(!empty);
                        bRemoveNode.setEnabled(!empty);
                        if (!empty) {
                            tNodes.select(index);
                            jobchainDataProvider.selectNode(tNodes);
                        } else {
                            jobchainDataProvider.selectNode(null);
                        }
                        jobchainDataProvider.updateSelectedJobChain();
                    }
                }
            });
            bRemoveNode.setLayoutData(new GridData(GridData.FILL, GridData.END, false, false));

        } catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            } catch (Exception ee) {
            }
        }
    }

    private boolean isValidNode() {
        if (tState.getText().equals("") || bFullNode.getSelection() && cJob.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private Shell getShell() {
        return composite.getShell();
    }

    public void applyNode() {
        try {
            cNextState.setVisibleItemCount(5);
            String msg = "";
            if (!jobchainDataProvider.isValidState(tState.getText()))
                msg = JOE_M_JobChain_StateAlreadyDefined.label();
            if (!msg.equals("")) {
                MainWindow.message(msg, SWT.ICON_INFORMATION);
            } else {
                if (isInsert) {
                    jobchainDataProvider.applyInsertNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), cOnError.getText());
                } else {
                    jobchainDataProvider.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), tDelay.getText(), cNextState.getText(), cErrorState.getText(), bRemoveFile.getSelection(), tMoveTo.getText(), cOnError.getText());
                }
                DetailsListener.checkDetailsParameter(tState.getText(), jobchainDataProvider.getChainName(), cJob.getText(), jobchainDataProvider.getDom(), jobchainDataProvider.getISchedulerUpdate());
                jobchainDataProvider.fillChain(tNodes);
                bApplyNode.setEnabled(false);
                bRemoveNode.setEnabled(false);
                jobchainDataProvider.selectNode(null);
                fillNode(true);
                enableNode(false);
            }
            isInsert = false;
            jobchainDataProvider.updateSelectedJobChain();
        } catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            } catch (Exception ee) {
            }
        }
    }

    private String[] initStateCombo() {
        TableItem[] items = tNodes.getItems();
        HashMap<String, String> states = new HashMap<String, String>();
        for (int i = 0; i < items.length; i++) {
            String s = tNodes.getItem(i).getText(0);
            if (!tState.getText().equals(s)) {
                states.put(s, s);
            }
        }

        states.put("success", "succes");
        states.put("error", "error");

        String[] retStates = new String[states.size()];
        int index = 0;
        for (String key : states.keySet()) {
            retStates[index] = key;
            index = index + 1;
        }
        return retStates;
    }

    private void fillNode(boolean clear) {
        try {
            butAddMissingNodes.setEnabled(false);
            boolean fullNode = jobchainDataProvider.isFullNode();
            boolean fileSinkNode = jobchainDataProvider.isFileSinkNode();
            boolean endNode = !fullNode && !fileSinkNode;
            bFullNode.setSelection(clear || fullNode);
            bEndNode.setSelection(!clear && endNode);
            bFileSink.setSelection(!clear && fileSinkNode && jobchainDataProvider.hasFileorderSource());
            tDelay.setEnabled(fullNode);

            cNextState.setEnabled(fullNode);
            cErrorState.setEnabled(fullNode);
            cOnError.setEnabled(fullNode);
            cJob.setEnabled(fullNode);
            tMoveTo.setEnabled(fileSinkNode && jobchainDataProvider.hasFileorderSource());
            bRemoveFile.setEnabled(fileSinkNode && jobchainDataProvider.hasFileorderSource());
            tState.setText(clear ? "" : jobchainDataProvider.getState());
            tDelay.setText(clear ? "" : jobchainDataProvider.getDelay());
            cJob.setItems(jobchainDataProvider.getJobs());

            cNextState.setItems(initStateCombo());
            cErrorState.setItems(initStateCombo());

            tMoveTo.setText(jobchainDataProvider.getMoveTo());
            bRemoveFile.setSelection(jobchainDataProvider.getRemoveFile());
            int job = cJob.indexOf(jobchainDataProvider.getJob());
            if (clear || job == -1) {
                cJob.setText(jobchainDataProvider.getJob());
            } else {
                cJob.select(job);
            }
            int next = cNextState.indexOf(jobchainDataProvider.getNextState());
            if (clear || !fullNode || next == -1) {
                cNextState.setText(jobchainDataProvider.getNextState());
            } else {
                cNextState.select(next);
            }
            int error = cErrorState.indexOf(jobchainDataProvider.getErrorState());
            if (clear || !fullNode || error == -1) {
                cErrorState.setText(jobchainDataProvider.getErrorState());
            } else {
                cErrorState.select(error);
            }
            int onError = cOnError.indexOf(jobchainDataProvider.getOnError());
            if (clear || !fullNode || onError == -1) {
                cOnError.setText(jobchainDataProvider.getOnError());
            } else {
                cOnError.select(onError);
            }
            bApplyNode.setEnabled(false);
        } catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            } catch (Exception ee) {
                // tu nichts
            }
        }
    }

    private void selectNodes() {
        if (bApplyNode.isEnabled()) {
            int c = MainWindow.message(getShell(), JOE_M_ApplyChanges.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            if (c == SWT.YES) {
                applyNode();
            }
        }

        jobchainDataProvider.setJobchainListOfReturnCodeElements(new JobchainListOfReturnCodeElements());

        if (tNodes.getSelectionCount() > 0) {
            jobchainDataProvider.selectNode(tNodes);
            enableNode(true);
            fillNode(false);
            butDetailsJob.setEnabled(true);
            butAddMissingNodes.setEnabled(!jobchainDataProvider.checkForState(tNodes.getSelection()[0].getText(3))
                    || !jobchainDataProvider.checkForState(tNodes.getSelection()[0].getText(4)));
        } else {
            butDetailsJob.setEnabled(false);
            butAddMissingNodes.setEnabled(false);
        }
        bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
    }

    private void showDetails(String state, String jobname) {
        boolean isLifeElement = jobchainDataProvider.getDom().isLifeElement() || jobchainDataProvider.getDom().isDirectory();
        if (state == null) {
            DetailDialogForm detail = new DetailDialogForm(jobchainDataProvider.getChainName(), isLifeElement, jobchainDataProvider.getDom().getFilename());
            detail.showDetails();
            detail.getDialogForm().setParamsForWizzard(jobchainDataProvider.getDom(), jobchainDataProvider.getISchedulerUpdate(), jobname);
        } else {
            DetailDialogForm detail = new DetailDialogForm(jobchainDataProvider.getChainName(), state, null, isLifeElement, jobchainDataProvider.getDom().getFilename());
            detail.showDetails();
            detail.getDialogForm().setParamsForWizzard(jobchainDataProvider.getDom(), jobchainDataProvider.getISchedulerUpdate(), jobname);
        }
    }

    private void enableNode(boolean enable) {
        bFullNode.setEnabled(enable);
        bEndNode.setEnabled(enable);
        bFileSink.setEnabled(enable);
        tState.setEnabled(enable);
        cJob.setEnabled(enable);
        cNextState.setEnabled(enable);
        cErrorState.setEnabled(enable);
        cOnError.setEnabled(enable);
        tDelay.setEnabled(enable);
        if (!jobchainDataProvider.getDom().isLifeElement()) {
            butImportJob.setEnabled(enable);
        }
        butReturnCode.setEnabled(enable);
        butBrowse.setEnabled(enable);
        tMoveTo.setEnabled(enable && jobchainDataProvider.hasFileorderSource());
        bRemoveFile.setEnabled(enable && jobchainDataProvider.hasFileorderSource());
        bApplyNode.setEnabled(false);
    }

    private void initJobChain() {
        jobchainDataProvider.fillChain(tNodes);
        bNewNode.setEnabled(true);
        enableNode(false);
    }

    public void enableFileOrderSourceControls() {
        boolean hasFileOrderSource = jobchainDataProvider.hasFileorderSource();
        tMoveTo.setEnabled(hasFileOrderSource);
        bRemoveFile.setEnabled(hasFileOrderSource);
        bFileSink.setEnabled(hasFileOrderSource);

    }

    public String getState() {
        return tState.getText();
    }

    public boolean hasSelectedNode() {
        return tNodes.getSelectionCount() > 0;
    }

}