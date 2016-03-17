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
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobChainNestedListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class JobChainNestedNodesForm extends SOSJOEMessageCodes implements IUnsaved {

    private Button butAddMissingNodes;
    private Button bNewNode = null;
    private Table tNodes = null;
    private Button bApplyNode = null;
    private Combo cErrorState = null;
    private Label label9 = null;
    private Combo cNextState = null;
    private Label label8 = null;
    private Button bEndNode = null;
    private Button bFullNode = null;
    private Composite cType = null;
    private Combo cJobChain = null;
    private Label label7 = null;
    private Text tState = null;
    private Label label6 = null;
    private JobChainNestedListener listener = null;
    private Group jobChainGroup = null;
    private Button bRemoveNode = null;
    private Group gNodes = null;
    private boolean refresh = false;
    private Button butDetailsJob = null;
    private Button butBrowse = null;
    private Button butUp = null;
    private Button butDown = null;
    private SchedulerDom dom = null;
    private Button butGoto = null;
    private boolean isInsert = false;
    private ISchedulerUpdate update = null;
    private Button butInsert = null;
    private Button reorderButton = null;

    public JobChainNestedNodesForm(Composite parent, int style, SchedulerDom dom_, Element jobChain) {
        super(parent, style);
        dom = dom_;
        listener = new JobChainNestedListener(dom, jobChain);
        initialize();
        boolean existChainNodes = check();
        jobChainGroup.setEnabled(existChainNodes);
        bNewNode.setEnabled(existChainNodes);
        if (existChainNodes) {
            fillChain(false, false);
        }
        this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
    }

    public void apply() {
        if (bApplyNode.isEnabled()) {
            applyNode();
        }
    }

    public boolean isUnsaved() {
        return bApplyNode.isEnabled();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(676, 464));
    }

    private void createGroup() {
        jobChainGroup = new Group(this, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        jobChainGroup.setLayout(gridLayout);
        gNodes = new Group(jobChainGroup, SWT.NONE);
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_2.heightHint = 170;
        gNodes.setLayoutData(gridData_2);
        gNodes.setText(JOE_M_JCNestedNodesForm_NestedNodes.params(listener.getChainName()));
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.marginBottom = 5;
        gridLayout_3.marginTop = 5;
        gridLayout_3.numColumns = 4;
        gNodes.setLayout(gridLayout_3);
        label6 = JOE_L_JobChainNodes_State.Control(new Label(gNodes, SWT.NONE));
        label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false, 2, 1));
        tState = JOE_T_JobChainNodes_State.Control(new Text(gNodes, SWT.BORDER));
        tState.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                boolean valid = listener.isValidState(tState.getText());
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
        final GridData gridData18 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData18.widthHint = 459;
        tState.setLayoutData(gridData18);
        bApplyNode = JOE_B_JobChainNodes_ApplyNode.Control(new Button(gNodes, SWT.NONE));
        bApplyNode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                applyNode();
            }
        });
        final GridData gridData7 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bApplyNode.setLayoutData(gridData7);
        bApplyNode.setEnabled(false);
        label7 = JOE_L_JCNestedNodesForm_JobChain.Control(new Label(gNodes, SWT.NONE));
        label7.setLayoutData(new GridData());
        butGoto = JOE_B_JobChainNodes_Goto.Control(new Button(gNodes, SWT.ARROW | SWT.DOWN));
        butGoto.setVisible(listener.get_dom() != null && !listener.get_dom().isLifeElement());
        butGoto.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                ContextMenu.goTo(cJobChain.getText(), dom, JOEConstants.JOB_CHAIN);
            }
        });
        butGoto.setAlignment(SWT.RIGHT);
        cJobChain = JOE_Cbo_JCNestedNodesForm_JobChain.Control(new Combo(gNodes, SWT.BORDER));
        cJobChain.setMenu(new sos.scheduler.editor.app.ContextMenu(cJobChain, dom, JOEConstants.JOB_CHAIN).getMenu());
        cJobChain.addMouseListener(new MouseAdapter() {

            public void mouseDown(final MouseEvent e) {
                if (refresh && listener.getJobChains() != null) {
                    cJobChain.setItems(listener.getJobChains());
                    refresh = false;
                }
            }
        });
        cJobChain.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled()) {
                    getShell().setDefaultButton(bApplyNode);
                }
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
        butBrowse = JOE_B_JobChainNodes_Browse.Control(new Button(composite, SWT.NONE));
        butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butBrowse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB_CHAIN);
                if (jobname != null && !jobname.isEmpty()) {
                    cJobChain.setText(jobname);
                }
            }
        });
        label8 = JOE_L_JobChainNodes_NextState.Control(new Label(gNodes, SWT.NONE));
        label8.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        cNextState = JOE_Cbo_JobChainNodes_NextState.Control(new Combo(gNodes, SWT.BORDER));
        cNextState.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled()) {
                    getShell().setDefaultButton(bApplyNode);
                }
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
        bNewNode = JOE_B_JCNestedNodesForm_NewNode.Control(new Button(gNodes, SWT.NONE));
        bNewNode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                isInsert = false;
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
        label9 = JOE_L_JobChainNodes_ErrorState.Control(new Label(gNodes, SWT.NONE));
        label9.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        cErrorState = JOE_Cbo_JobChainNodes_ErrorState.Control(new Combo(gNodes, SWT.BORDER));
        cErrorState.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled()) {
                    getShell().setDefaultButton(bApplyNode);
                }
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
        butInsert = JOE_B_JCNestedNodesForm_Insert.Control(new Button(gNodes, SWT.NONE));
        butInsert.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                isInsert = true;
                String state = tState.getText();
                tState.setText("");
                cErrorState.setText("");
                cJobChain.setText("");
                cNextState.setText(state);
                enableNode(true);
                bFullNode.setSelection(true);
                bEndNode.setSelection(false);
            }
        });
        butInsert.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
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
        bFullNode = JOE_B_JCNestedNodesForm_FullNode.Control(new Button(cType, SWT.RADIO));
        bFullNode.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
        bFullNode.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        bFullNode.setSelection(true);
        bEndNode = JOE_B_JCNestedNodesForm_EndNode.Control(new Button(cType, SWT.RADIO));
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
        new Label(gNodes, SWT.NONE);
        tNodes = JOE_Tbl_JCNestedNodesForm_Nodes.Control(new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER));
        tNodes.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                selectNodes();
            }
        });
        tNodes.setLinesVisible(true);
        tNodes.setHeaderVisible(true);
        final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 4);
        gridData4.heightHint = 112;
        tNodes.setLayoutData(gridData4);
        final TableColumn tableColumn3 = JOE_TCl_JCNestedNodesForm_State.Control(new TableColumn(tNodes, SWT.NONE));
        tableColumn3.setWidth(90);
        final TableColumn newColumnTableColumn_3 = JOE_TCl_JCNestedNodesForm_Node.Control(new TableColumn(tNodes, SWT.NONE));
        newColumnTableColumn_3.setWidth(100);
        final TableColumn tableColumn4 = JOE_TCl_JCNestedNodesForm_JobChain.Control(new TableColumn(tNodes, SWT.NONE));
        tableColumn4.setWidth(200);
        final TableColumn tableColumn5 = JOE_TCl_JCNestedNodesForm_NextState.Control(new TableColumn(tNodes, SWT.NONE));
        tableColumn5.setWidth(90);
        final TableColumn tableColumn6 = JOE_TCl_JCNestedNodesForm_ErrorState.Control(new TableColumn(tNodes, SWT.NONE));
        tableColumn6.setWidth(90);
        final TableColumn newColumnTableColumn_4 = JOE_TCl_JCNestedNodesForm_OnError.Control(new TableColumn(tNodes, SWT.NONE));
        newColumnTableColumn_4.setWidth(100);
        final Composite composite_1 = new Composite(gNodes, SWT.NONE);
        composite_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout_5 = new GridLayout();
        gridLayout_5.marginWidth = 0;
        gridLayout_5.marginHeight = 0;
        gridLayout_5.numColumns = 3;
        composite_1.setLayout(gridLayout_5);
        butUp = JOE_B_Up.Control(new Button(composite_1, SWT.NONE));
        butUp.setLayoutData(new GridData());
        butUp.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    int index = tNodes.getSelectionIndex();
                    if (index > 0) {
                        listener.changeUp(tNodes, true, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), "", cNextState.getText(), cErrorState.getText(), index, bFullNode.getSelection(), reorderButton.getSelection());
                        selectNodes();
                    }
                }
            }
        });
        butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
        butDown = JOE_B_Down.Control(new Button(composite_1, SWT.NONE));
        butDown.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    int index = tNodes.getSelectionIndex();
                    if (index >= 0) {
                        listener.changeUp(tNodes, false, bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), "", cNextState.getText(), cErrorState.getText(), index, bFullNode.getSelection(), reorderButton.getSelection());
                        selectNodes();
                    }
                }
            }
        });
        butDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butDown.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
        reorderButton = JOE_B_JCNestedNodesForm_Reorder.Control(new Button(composite_1, SWT.CHECK));
        butDetailsJob = JOE_B_JobChainForm_Parameter.Control(new Button(gNodes, SWT.NONE));
        butDetailsJob.setEnabled(false);
        butDetailsJob.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    showDetails(tNodes.getSelection()[0].getText(0));
                }
            }
        });
        butDetailsJob.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butAddMissingNodes = JOE_B_JCNestedNodesForm_AddMissingNodes.Control(new Button(gNodes, SWT.NONE));
        butAddMissingNodes.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                try {
                    if (tNodes.getSelectionCount() > 0) {
                        TableItem item = tNodes.getSelection()[0];
                        if (!listener.checkForState(item.getText(3))) {
                            listener.selectNode(null);
                            listener.applyNode(true, item.getText(3), "", "", "", false);
                        }
                        if (!listener.checkForState(item.getText(4))) {
                            listener.selectNode(null);
                            listener.applyNode(true, item.getText(4), "", "", "", false);
                        }
                        listener.fillChain(tNodes);
                        bApplyNode.setEnabled(false);
                        bRemoveNode.setEnabled(false);
                        listener.selectNode(null);
                        fillNode(true);
                        enableNode(false);
                    }
                } catch (Exception ex) {
                    new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), ex);
                }
            }
        });
        butAddMissingNodes.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butAddMissingNodes.setEnabled(false);
        bRemoveNode = JOE_B_JCNestedNodesForm_RemoveNode.Control(new Button(gNodes, SWT.NONE));
        bRemoveNode.setEnabled(false);
        bRemoveNode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    int c = MainWindow.message(getShell(), JOE_M_JCNestedNodesForm_RemoveNode.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    if (c != SWT.YES) {
                        return;
                    }
                    int index = tNodes.getSelectionIndex();
                    listener.deleteNode(tNodes);
                    tNodes.remove(index);
                    if (index >= tNodes.getItemCount()) {
                        index--;
                    }
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
        bRemoveNode.setLayoutData(new GridData(GridData.FILL, GridData.END, false, false));
    }

    private void fillChain(boolean enable, boolean isNew) {
        listener.fillChain(tNodes);
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
        boolean endNode = !fullNode;
        bFullNode.setSelection(clear || fullNode);
        bEndNode.setSelection(!clear && endNode);
        cNextState.setEnabled(fullNode);
        cErrorState.setEnabled(fullNode);
        cJobChain.setEnabled(fullNode);
        tState.setText(clear ? "" : listener.getState());
        cJobChain.setItems(listener.getJobChains());
        if (listener.getStates().length > 0) {
            cNextState.setItems(listener.getStates());
            cErrorState.setItems(listener.getStates());
        }
        int job = cJobChain.indexOf(listener.getJobChain());
        if (clear || job == -1) {
            cJobChain.setText(listener.getJobChain());
        } else {
            cJobChain.select(job);
        }
        int next = cNextState.indexOf(listener.getNextState());
        if (clear || !fullNode || next == -1) {
            cNextState.setText(listener.getNextState());
        } else {
            cNextState.select(next);
        }
        int error = cErrorState.indexOf(listener.getErrorState());
        if (clear || !fullNode || error == -1) {
            cErrorState.setText(listener.getErrorState());
        } else {
            cErrorState.select(error);
        }
        bApplyNode.setEnabled(false);
    }

    private void applyNode() {
        String msg = "";
        if (!listener.isValidState(tState.getText())) {
            msg = JOE_M_JobChain_StateAlreadyDefined.label();
        }
        if (!"".equals(msg)) {
            MainWindow.message(msg, SWT.ICON_INFORMATION);
        } else {
            if (isInsert) {
                listener.applyInsertNode(tState.getText(), cJobChain.getText(), cNextState.getText(), cErrorState.getText(), bFullNode.getSelection());
            } else {
                listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJobChain.getText(), cNextState.getText(), cErrorState.getText(), bFullNode.getSelection());
            }
            isInsert = false;
            listener.fillChain(tNodes);
            bApplyNode.setEnabled(false);
            bRemoveNode.setEnabled(false);
            listener.selectNode(null);
            fillNode(true);
            enableNode(false);
        }
    }

    private boolean isValidNode() {
        return !("".equals(tState.getText()) || bFullNode.getSelection() && "".equals(cJobChain.getText()));
    }

    public void setISchedulerUpdate(ISchedulerUpdate update_) {
        update = update_;
    }

    private void showDetails(String state) {
        boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();
        if (state == null) {
            DetailDialogForm detail = new DetailDialogForm(listener.getChainName(), isLifeElement, listener.get_dom().getFilename());
            detail.showDetails();
        } else {
            DetailDialogForm detail = new DetailDialogForm(listener.getChainName(), state, null, isLifeElement, listener.get_dom().getFilename());
            detail.showDetails();
        }
    }

    private void selectNodes() {
        if (tNodes.getSelectionCount() > 0) {
            listener.selectNode(tNodes);
            enableNode(true);
            fillNode(false);
            butDetailsJob.setEnabled(true);
        } else {
            butDetailsJob.setEnabled(false);
        }
        bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
    }

    private boolean check() {
        try {
            XPath x3 = XPath.newInstance("//job_chain[@name='" + listener.getChainName() + "']/job_chain_node");
            List listOfElement_3 = x3.selectNodes(dom.getDoc());
            XPath x4 = XPath.newInstance("//job_chain[@name='" + listener.getChainName() + "']/file_order_sink");
            List listOfElement_4 = x4.selectNodes(dom.getDoc());
            return listOfElement_3.isEmpty() && listOfElement_4.isEmpty();
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            return true;
        }
    }

}