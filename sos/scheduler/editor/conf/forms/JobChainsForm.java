package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

public class JobChainsForm extends Composite implements IUnsaved, IUpdateLanguage {
    private static final String GROUP_NODES_TITLE = "Chain Nodes";

    private JobChainsListener   listener;

    private Group               group             = null;

    private Composite           cChains           = null;

    private Table               tChains           = null;

    private Button              bRemoveChain      = null;

    private Label               label             = null;

    private Text                tName             = null;

    private Button              bRecoverable      = null;

    private Button              bVisible          = null;

    private Group               gNodes            = null;

    private Table               tNodes            = null;

    private Button              bRemoveNode       = null;

    private Label               label6            = null;

    private Label               label7            = null;

    private CCombo              cJob              = null;

    private Label               label8            = null;

    private CCombo              cNextState        = null;

    private Label               label9            = null;

    private CCombo              cErrorState       = null;

    private Button              bApplyNode        = null;

    private Composite           cType             = null;

    private Button              bFullNode         = null;

    private Button              bEndNode          = null;

    private Label               label1            = null;

    private Button              bNewChain         = null;

    private Button              bNewNode          = null;

    private Text                tState            = null;

    private Button              bApplyChain       = null;

    private Label               label2            = null;

    private Label               label3            = null;

    private Label               label4            = null;

    private Label               label5            = null;


    public JobChainsForm(Composite parent, int style, SchedulerDom dom, Element config) {
        super(parent, style);
        listener = new JobChainsListener(dom, config);
        initialize();
        setToolTipText();
        fillChain(false, false);
        listener.fillChains(tChains);
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
        group = new Group(this, SWT.NONE);
        group.setText("Job Chains");
        createComposite();
        group.setLayout(new GridLayout());
        createGroup1();
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        GridData gridData71 = new org.eclipse.swt.layout.GridData();
        gridData71.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData71.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData61 = new org.eclipse.swt.layout.GridData();
        gridData61.horizontalSpan = 5;
        gridData61.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData61.heightHint = 10;
        gridData61.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData16 = new org.eclipse.swt.layout.GridData();
        gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData16.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData10 = new org.eclipse.swt.layout.GridData();
        gridData10.grabExcessHorizontalSpace = true;
        gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData8 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        gridData8.heightHint = 312;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
        gridData3.grabExcessHorizontalSpace = false;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalSpan = 1;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        cChains = new Composite(group, SWT.NONE);
        label = new Label(cChains, SWT.NONE);
        label.setText("Chain Name:");
        tName = new Text(cChains, SWT.BORDER);
        tName.setLayoutData(gridData10);
        bRecoverable = new Button(cChains, SWT.CHECK);
        bRecoverable.setSelection(true);
        bVisible = new Button(cChains, SWT.CHECK);
        bVisible.setSelection(true);
        bApplyChain = new Button(cChains, SWT.NONE);
        label2 = new Label(cChains, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData61);
        createTable();
        cChains.setLayoutData(gridData8);
        cChains.setLayout(gridLayout);
        bNewChain = new Button(cChains, SWT.NONE);
        bNewChain.setText("New Job &Chain");
        bNewChain.setLayoutData(gridData16);
        getShell().setDefaultButton(bNewChain);
        label3 = new Label(cChains, SWT.SEPARATOR | SWT.HORIZONTAL);
        label3.setText("Label");
        label3.setLayoutData(gridData71);
        bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newChain();
                tChains.deselectAll();
                fillChain(true, true);
                bApplyChain.setEnabled(false);
                tName.setFocus();
            }
        });
        bRemoveChain = new Button(cChains, SWT.NONE);
        bRemoveChain.setText("Remove Job Chain");
        bRemoveChain.setEnabled(false);
        bRemoveChain.setLayoutData(gridData1);
        bRemoveChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                deleteChain();
            }
        });
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
        bRecoverable.setText("Orders Recoverable");
        bRecoverable.setLayoutData(gridData3);
        bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApplyChain);
                bApplyChain.setEnabled(true);
            }
        });
        bVisible.setText("Visible");
        bApplyChain.setText("A&pply Job Chain");
        bApplyChain.setLayoutData(gridData);
        bApplyChain.setEnabled(false);
        bApplyChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyChain();
            }
        });
        bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApplyChain);
                bApplyChain.setEnabled(true);
            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalSpan = 4;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalSpan = 3;
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        tChains = new Table(cChains, SWT.FULL_SELECTION | SWT.BORDER);
        tChains.setHeaderVisible(true);
        tChains.setLayoutData(gridData2);
        tChains.setLinesVisible(true);
        tChains.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tChains.getSelectionCount() > 0) {
                    listener.selectChain(tChains.getSelectionIndex());
                    fillChain(true, false);
                    bApplyChain.setEnabled(false);
                }
                bRemoveChain.setEnabled(tChains.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn = new TableColumn(tChains, SWT.NONE);
        tableColumn.setWidth(200);
        tableColumn.setText("Name");
        TableColumn tableColumn1 = new TableColumn(tChains, SWT.NONE);
        tableColumn1.setWidth(150);
        tableColumn1.setText("Orders Recoverable");
        TableColumn tableColumn2 = new TableColumn(tChains, SWT.NONE);
        tableColumn2.setWidth(150);
        tableColumn2.setText("Visible");
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData18.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData17 = new org.eclipse.swt.layout.GridData();
        gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData17.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData12 = new org.eclipse.swt.layout.GridData();
        gridData12.horizontalSpan = 7;
        gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData12.heightHint = 10;
        gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData15 = new org.eclipse.swt.layout.GridData();
        gridData15.widthHint = 80;
        gridData15.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData15.grabExcessHorizontalSpace = true;
        gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData14 = new org.eclipse.swt.layout.GridData();
        gridData14.widthHint = 80;
        gridData14.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData14.grabExcessHorizontalSpace = true;
        gridData14.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData13 = new org.eclipse.swt.layout.GridData();
        gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData13.grabExcessHorizontalSpace = true;
        gridData13.horizontalSpan = 3;
        gridData13.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData11 = new org.eclipse.swt.layout.GridData();
        gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData11.verticalSpan = 1;
        gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData9 = new org.eclipse.swt.layout.GridData();
        gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData9.grabExcessHorizontalSpace = true;
        gridData9.grabExcessVerticalSpace = true;
        gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData7 = new org.eclipse.swt.layout.GridData();
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData7.verticalSpan = 2;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 7;
        gNodes = new Group(group, SWT.NONE);
        gNodes.setText("Chain Nodes");
        gNodes.setLayout(gridLayout1);
        gNodes.setLayoutData(gridData9);
        label6 = new Label(gNodes, SWT.NONE);
        label6.setText("State:");
        tState = new Text(gNodes, SWT.BORDER);
        tState.setLayoutData(gridData18);
        label7 = new Label(gNodes, SWT.NONE);
        label7.setText("Job:");
        cJob = new CCombo(gNodes, SWT.BORDER);
        cJob.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    applyNode();
                }
            }
        });
        bApplyNode = new Button(gNodes, SWT.NONE);
        label1 = new Label(gNodes, SWT.NONE);
        label1.setText("Type:");
        createComposite1();
        label8 = new Label(gNodes, SWT.NONE);
        label8.setText("Next State:");
        cNextState = new CCombo(gNodes, SWT.BORDER);
        cNextState.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    applyNode();
                }

            }
        });
        label9 = new Label(gNodes, SWT.NONE);
        label9.setText("Error State:");
        cErrorState = new CCombo(gNodes, SWT.BORDER);
        cErrorState.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    applyNode();
                }

            }
        });
        cErrorState.setLayoutData(gridData15);
        label4 = new Label(gNodes, SWT.SEPARATOR | SWT.HORIZONTAL);
        label4.setText("Label");
        label4.setLayoutData(gridData12);
        createTable1();
        bNewNode = new Button(gNodes, SWT.NONE);
        bNewNode.setText("New Chain &Node");
        bNewNode.setLayoutData(gridData6);
        label5 = new Label(gNodes, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setText("Label");
        label5.setLayoutData(gridData17);
        bNewNode.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(null);
                tNodes.deselectAll();
                listener.selectNode(-1);
                bRemoveNode.setEnabled(false);
                enableNode(true);
                fillNode(true);
                tState.setFocus();
            }
        });
        bRemoveNode = new Button(gNodes, SWT.NONE);
        bRemoveNode.setText("Remove Chain Node");
        bRemoveNode.setEnabled(false);
        bRemoveNode.setLayoutData(gridData11);
        bRemoveNode.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    int index = tNodes.getSelectionIndex();
                    listener.deleteNode(index);
                    tNodes.remove(index);
                    if (index >= tNodes.getItemCount())
                        index--;
                    boolean empty = tNodes.getItemCount() == 0;

                    fillNode(empty);
                    enableNode(!empty);
                    bRemoveNode.setEnabled(!empty);
                    if (!empty) {
                        tNodes.select(index);
                        listener.selectNode(index);
                    } else {
                        listener.selectNode(-1);
                    }
                }
            }
        });
        tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = listener.isValidState(tState.getText());
                bApplyNode.setEnabled(valid);
                if (!valid)
                    tState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                else
                    tState.setBackground(null);
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled())
                    getShell().setDefaultButton(bApplyNode);
            }
        });
        cJob.setLayoutData(gridData13);
        cJob.setEditable(true);
        cJob.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled())
                    getShell().setDefaultButton(bApplyNode);
            }
        });
        cNextState.setLayoutData(gridData14);
        cNextState.setEditable(true);
        cNextState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled())
                    getShell().setDefaultButton(bApplyNode);
            }
        });
        cErrorState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                getShell().setDefaultButton(bApplyNode);
                bApplyNode.setEnabled(isValidNode());
                if (bApplyNode.getEnabled())
                    getShell().setDefaultButton(bApplyNode);
            }
        });
        bApplyNode.setLayoutData(gridData7);
        bApplyNode.setText("&Apply Chain Node");
        bApplyNode.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyNode();
            }
        });
    }


    /**
     * This method initializes table1
     */
    private void createTable1() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.horizontalSpan = 6;
        gridData4.verticalSpan = 3;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        tNodes = new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER);
        tNodes.setHeaderVisible(true);
        tNodes.setLinesVisible(true);
        tNodes.setLayoutData(gridData4);
        tNodes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tNodes.getSelectionCount() > 0) {
                    listener.selectNode(tNodes.getSelectionIndex());
                    enableNode(true);
                    fillNode(false);
                }
                bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn3 = new TableColumn(tNodes, SWT.NONE);
        tableColumn3.setText("State");
        tableColumn3.setWidth(90);
        TableColumn tableColumn4 = new TableColumn(tNodes, SWT.NONE);
        tableColumn4.setText("Job Name");
        tableColumn4.setWidth(200);
        TableColumn tableColumn5 = new TableColumn(tNodes, SWT.NONE);
        tableColumn5.setText("Next State");
        tableColumn5.setWidth(90);
        TableColumn tableColumn6 = new TableColumn(tNodes, SWT.NONE);
        tableColumn6.setText("Error State");
        tableColumn6.setWidth(90);
    }


    /**
     * This method initializes composite1
     */
    private void createComposite1() {
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.horizontalSpan = 1;
        cType = new Composite(gNodes, SWT.NONE);
        cType.setLayout(new RowLayout());
        cType.setLayoutData(gridData5);
        bFullNode = new Button(cType, SWT.RADIO);
        bFullNode.setText("Full Node");
        bFullNode.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bFullNode.getSelection()) {
                    cNextState.setEnabled(true);
                    cErrorState.setEnabled(true);
                    cJob.setEnabled(true);
                    bApplyNode.setEnabled(isValidNode());
                    if (bApplyNode.getEnabled())
                        getShell().setDefaultButton(bApplyNode);
                }
            }
        });
        bEndNode = new Button(cType, SWT.RADIO);
        bEndNode.setText("End Node");
        bEndNode.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (bEndNode.getSelection()) {
                    cNextState.setEnabled(false);
                    cErrorState.setEnabled(false);
                    cJob.setEnabled(false);
                    cJob.setText("");
                    cNextState.setText("");
                    cErrorState.setText("");
                    if (tState.getText().equals(""))
                        bApplyNode.setEnabled(false);
                }
            }
        });
    }


    private void fillChain(boolean enable, boolean isNew) {
        tName.setEnabled(enable);
        bRecoverable.setEnabled(enable);
        bVisible.setEnabled(enable);

        tName.setText(enable ? listener.getChainName() : "");
        bRecoverable.setSelection(enable ? listener.getRecoverable() || isNew : true);
        bVisible.setSelection(enable ? listener.getVisible() || isNew : true);

        tName.setBackground(null);
        bApplyChain.setEnabled(enable);

        if (enable && !isNew) {
            listener.fillChain(tNodes);
            gNodes.setText(GROUP_NODES_TITLE + " for: " + listener.getChainName());
            bNewNode.setEnabled(true);
        } else
            bNewNode.setEnabled(false);

        enableNode(false);
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
        fillChain(selection, false);
    }


    private void enableNode(boolean enable) {
        bFullNode.setEnabled(enable);
        bEndNode.setEnabled(enable);
        tState.setEnabled(enable);
        cJob.setEnabled(enable);
        cNextState.setEnabled(enable);
        cErrorState.setEnabled(enable);
        bApplyNode.setEnabled(false);
    }


    private void fillNode(boolean clear) {
        boolean fullNode = listener.isFullNode();
        bFullNode.setSelection(clear || fullNode);
        bEndNode.setSelection(!clear && !fullNode);
        cNextState.setEnabled(fullNode);
        cErrorState.setEnabled(fullNode);
        cJob.setEnabled(fullNode);

        tState.setText(clear ? "" : listener.getState());

        cJob.setItems(listener.getJobs());
        cNextState.setItems(listener.getStates());
        cErrorState.setItems(listener.getStates());

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

        bApplyNode.setEnabled(false);
    }


    private void applyChain() {
        listener.applyChain(tName.getText(), bRecoverable.getSelection(), bVisible.getSelection());
        int index = tChains.getSelectionIndex();
        if (index == -1)
            index = tChains.getItemCount();
        listener.fillChains(tChains);
        tChains.select(index);
        fillChain(true, false);
        bRemoveChain.setEnabled(true);
        bApplyChain.setEnabled(false);
        getShell().setDefaultButton(bNewChain);
    }


    private void applyNode() {
        String msg = "";

        if (bFullNode.getSelection() && cJob.getText().equals("")) {
            msg = "Please set the name of the job for this state";
        }

        if (bFullNode.getSelection() && (cNextState.getText() + cErrorState.getText()).equals("")) {
            // msg = "Please set the next/error-state this state";
        }

        if (!msg.equals("")) {
            MainWindow.message(msg, SWT.ICON_INFORMATION);
        } else {
            listener.applyNode(tState.getText(), cJob.getText(), cNextState.getText(), cErrorState.getText());
            listener.fillChain(tNodes);
            bApplyNode.setEnabled(false);
            bRemoveNode.setEnabled(false);
            listener.selectNode(-1);
            fillNode(true);
            enableNode(false);
        }
    }


    private boolean isValidNode() {
        if (tState.getText().equals("") || bFullNode.getSelection() && cJob.getText().equals("") // ||
        // bFullNode.getSelection()
        // && (cNextState.getText() + cErrorState.getText()).equals("")
        ) {
            return false;
        } else {
            return true;
        }
    }


    public void setToolTipText() {
        tName.setToolTipText(Messages.getTooltip("job_chains.chain.name"));
        bNewChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_new"));
        bRemoveChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_remove"));
        bRecoverable.setToolTipText(Messages.getTooltip("job_chains.chain.orders_recoverable"));
        bVisible.setToolTipText(Messages.getTooltip("job_chains.chain.visible"));
        bApplyChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_apply"));
        tChains.setToolTipText(Messages.getTooltip("job_chains.table"));
        tState.setToolTipText(Messages.getTooltip("job_chains.node.state"));
        cErrorState.setToolTipText(Messages.getTooltip("job_chains.node.error_state"));
        bNewNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_new"));
        bRemoveNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_remove"));
        cJob.setToolTipText(Messages.getTooltip("job_chains.node.job"));
        cNextState.setToolTipText(Messages.getTooltip("job_chains.node.next_state"));
        bApplyNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_apply"));
        tNodes.setToolTipText(Messages.getTooltip("job_chains.chain.node_table"));
        bFullNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_full_node"));
        bEndNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_end_node"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
