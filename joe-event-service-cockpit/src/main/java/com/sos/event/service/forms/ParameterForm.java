/**
 * 
 */
package com.sos.event.service.forms;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.util.SOSString;

import com.sos.event.service.listeners.ParameterListener;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class ParameterForm extends SOSJOEMessageCodes implements IUnsaved {

    private Button butDown_1 = null;
    private Button butUp_1 = null;
    private Label label4_3 = null;
    private ParameterListener listener = null;
    private Group gJobParameter = null;
    private Table tParameter = null;
    private Button bRemove = null;
    private Label label2 = null;
    private Text tParaName = null;
    private Label label6 = null;
    private Text tParaValue = null;
    private Button bApply = null;
    private Text txtParameterDescription = null;
    private Table tableEnvironment = null;
    private Text txtEnvName = null;
    private Text txtEnvValue = null;
    private Button butEnvApply = null;
    private Button butEnvRemove = null;
    private Text txtIncludeFilename = null;
    private Text txtIncludeNode = null;
    private Table tableIncludeParams = null;
    private Button butIncludesApply = null;
    private Button butOpenInclude = null;
    private Button butRemoveInclude = null;
    private CTabFolder tabFolder = null;
    private SOSString sosString = null;
    private ActionsDom dom = null;
    private CTabItem parameterTabItem = null;
    private CTabItem environmentTabItem = null;
    private int type = -1;
    private Combo cSource = null;
    private CTabItem parameterJobCmdTabItem = null;
    private Group group = null;
    private String includeFile = null;
    private Button butNewIncludes = null;
    private Button butIsLifeFile = null;
    // private Button butDown = null;
    // private Button butUp = null;
    private Button butNewParam = null;
    private Button butNewEnvironment = null;
    private boolean isRemoteConnection = false;

    /** @param parent
     * @param style
     * @throws JDOMException */
    public ParameterForm(Composite parent, int style, ActionsDom _dom, Element parentElem, ActionsForm main, int type_) throws JDOMException {
        super(parent, style);
        dom = _dom;
        type = type_;
        listener = new ParameterListener(dom, parentElem, main, type_);
        initialize();
        setToolTipText();
    }

    private void initialize() {
        sosString = new SOSString();
        try {
            isRemoteConnection = false; // sosString.parseToString(MainWindow.getContainer().getCurrentTab().getData("ftp_title")).length()
                                        // > 0;
        } catch (Exception e) {
        }
        this.setLayout(new GridLayout());
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 1;
        gJobParameter = JOE_G_ActionsParameterForm_Parameter.Control(new Group(this, SWT.NONE));
        gJobParameter.setLayout(gridLayout2);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gJobParameter.setLayoutData(gridData_1);
        createParameterGroup();
        getDescription();
        initForm();
    }

    public void apply() {
        if (isUnsaved())
            addParam();
    }

    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    /** This method initializes group2 */
    public void createParameterGroup() {
        tabFolder = new CTabFolder(gJobParameter, SWT.BORDER);
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData_2.heightHint = 203;
        gridData_2.widthHint = 760;
        tabFolder.setLayoutData(gridData_2);
        // Parameter
        if (type == JOEConstants.JOB_COMMANDS)
            createJobCommandParameter();
        // Includes
        if (type != JOEConstants.WEBSERVICE)
            createIncludes();
        tabFolder.setSelection(0);
        if (tParaName != null)
            tParaName.setFocus();
        setToolTipText();
    }

    private void addParam() {
        if (!tParaName.getText().equals(""))
            listener.saveParameter(tParameter, tParaName.getText().trim(), tParaValue.getText());
        tParaName.setText("");
        tParaValue.setText("");
        bRemove.setEnabled(false);
        tParameter.deselectAll();
        tParaName.setFocus();
    }

    private void addInclude() {
        listener.saveIncludeParams(tableIncludeParams, txtIncludeFilename.getText().trim(), txtIncludeNode.getText(), (type == JOEConstants.JOB
                || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS && butIsLifeFile.getSelection() ? butIsLifeFile.getSelection() : false));
        txtIncludeFilename.setText("");
        txtIncludeNode.setText("");
        butIncludesApply.setEnabled(false);
        butRemoveInclude.setEnabled(false);
        butOpenInclude.setEnabled(false);
        tableIncludeParams.deselectAll();
        txtIncludeFilename.setFocus();
        if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS)
            butIsLifeFile.setSelection(false);
    }

    public void initForm() {
        tParameter.removeAll();
        if (includeFile != null && includeFile.trim().length() > 0) {
            if (new File(Options.getSchedulerData().endsWith("/") || Options.getSchedulerData().endsWith("\\") ? Options.getSchedulerData()
                    : Options.getSchedulerData() + "/" + includeFile).exists())
                listener.getAllParameterDescription();
        }
        listener.fillParams(tParameter);
        listener.fillEnvironment(tableEnvironment);
        listener.fillIncludeParams(tableIncludeParams);
    }

    public Table getTParameter() {
        return tParameter;
    }

    private void createIncludes() {
        final CTabItem includesTabItem = JOE_TI_ActionsParameterForm_Includes.Control(new CTabItem(tabFolder, SWT.BORDER));
        final Group group_3 = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 5;
        group_3.setLayout(gridLayout_2);
        includesTabItem.setControl(group_3);
        if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS) {
            butIsLifeFile = JOE_B_ActionsParameterForm_IsLifeFile.Control(new Button(group_3, SWT.CHECK));
            butIsLifeFile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
                }
            });
        } else {
            @SuppressWarnings("unused")
            final Label lblNode_ = JOE_L_ActionsParameterForm_File.Control(new Label(group_3, SWT.NONE));
        }
        txtIncludeFilename = JOE_T_ActionsParameterForm_IncludeFilename.Control(new Text(group_3, SWT.BORDER));
        txtIncludeFilename.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
                if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS)
                    butIsLifeFile.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
            }
        });
        txtIncludeFilename.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !txtIncludeFilename.equals(""))
                    addInclude();
            }
        });
        txtIncludeFilename.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        @SuppressWarnings("unused")
        final Label lblNode = JOE_L_ActionsParameterForm_Node.Control(new Label(group_3, SWT.NONE));
        txtIncludeNode = JOE_T_ActionsParameterForm_IncludeNode.Control(new Text(group_3, SWT.BORDER));
        txtIncludeNode.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
                if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS)
                    butIsLifeFile.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
            }
        });
        txtIncludeNode.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !txtIncludeFilename.equals(""))
                    addInclude();
            }
        });
        txtIncludeNode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butIncludesApply = JOE_B_ActionsParameterForm_IncludesApply.Control(new Button(group_3, SWT.NONE));
        butIncludesApply.setEnabled(false);
        butIncludesApply.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                addInclude();
            }
        });
        butIncludesApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        label4_3 = new Label(group_3, SWT.HORIZONTAL | SWT.SEPARATOR);
        label4_3.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
        // label4_3.setText("Label");
        tableIncludeParams = JOE_Tbl_ActionsParameterForm_IncludeParams.Control(new Table(group_3, SWT.FULL_SELECTION | SWT.BORDER));
        tableIncludeParams.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
        tableIncludeParams.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                txtIncludeFilename.setText(item.getText(0));
                txtIncludeNode.setText(item.getText(1));
                if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS)
                    butIsLifeFile.setSelection(item.getText(2).equalsIgnoreCase("live_file"));
                butRemoveInclude.setEnabled(tableIncludeParams.getSelectionCount() > 0);
                butIncludesApply.setEnabled(false);
                butOpenInclude.setEnabled(true && !isRemoteConnection);
            }
        });
        tableIncludeParams.setLinesVisible(true);
        tableIncludeParams.setHeaderVisible(true);
        tableIncludeParams.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3));
        final TableColumn colParamColums = JOE_TCl_ActionsParameterForm_File.Control(new TableColumn(tableIncludeParams, SWT.NONE));
        colParamColums.setWidth(250);
        final TableColumn newColumnTableColumn_1 = JOE_TCl_ActionsParameterForm_Node.Control(new TableColumn(tableIncludeParams, SWT.NONE));
        newColumnTableColumn_1.setWidth(400);
        final TableColumn newColumnTableColumn = JOE_TCl_ActionsParameterForm_FileLifeFile.Control(new TableColumn(tableIncludeParams, SWT.NONE));
        newColumnTableColumn.setWidth(100);
        if (type != JOEConstants.JOB && type != JOEConstants.COMMANDS && type != JOEConstants.JOB_COMMANDS) {
            newColumnTableColumn.setWidth(200);
            newColumnTableColumn.setResizable(false);
        }
        butNewIncludes = JOE_B_ActionsParameterForm_NewIncludes.Control(new Button(group_3, SWT.NONE));
        butNewIncludes.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                tableIncludeParams.deselectAll();
                txtIncludeFilename.setText("");
                txtIncludeNode.setText("");
                if (type == JOEConstants.JOB || type == JOEConstants.COMMANDS || type == JOEConstants.JOB_COMMANDS)
                    butIsLifeFile.setSelection(false);
                butIncludesApply.setEnabled(false);
                butOpenInclude.setEnabled(false);
                butRemoveInclude.setEnabled(false);
                txtIncludeFilename.setFocus();
            }
        });
        butNewIncludes.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        // Include-Datei im Editor öffnen
        // butOpenInclude = new Button(group_3, SWT.NONE); //
        // butOpenInclude.addSelectionListener(new SelectionAdapter() {
        // public void widgetSelected(final SelectionEvent e) {
        // // createParameterTabItem();
        // }
        // });
        // butOpenInclude.setEnabled(false);
        // butOpenInclude.setLayoutData(new GridData(GridData.FILL,
        // GridData.CENTER, false, false));
        // butOpenInclude.setText("Open");
        butRemoveInclude = JOE_B_ActionsParameterForm_RemoveInclude.Control(new Button(group_3, SWT.NONE));
        butRemoveInclude.setEnabled(false);
        butRemoveInclude.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.deleteIncludeParams(tableIncludeParams, tableIncludeParams.getSelectionIndex());
                txtIncludeFilename.setText("");
                txtIncludeNode.setText("");
                tableIncludeParams.deselectAll();
                butIncludesApply.setEnabled(false);
                butRemoveInclude.setEnabled(false);
                txtIncludeFilename.setFocus();
            }
        });
        butRemoveInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {

            public void close(final CTabFolderEvent e) {
                if (e.item.equals(parameterTabItem) || e.item.equals(environmentTabItem) || e.item.equals(includesTabItem)) {
                    e.doit = false;
                }
            }
        });
        tabFolder.setSelection(0);
        txtIncludeFilename.setFocus();
    }

    public void createJobCommandParameter() {
        parameterJobCmdTabItem = JOE_TI_ActionsParameterForm_Parameter.Control(new CTabItem(tabFolder, SWT.BORDER));
        group = new Group(tabFolder, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group.setLayout(gridLayout);
        parameterJobCmdTabItem.setControl(group);
        label2 = JOE_L_ActionsParameterForm_Name.Control(new Label(group, SWT.NONE));
        label2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        tParaName = JOE_T_ActionsParameterForm_Name.Control(new Text(group, SWT.BORDER));
        final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_9.widthHint = 200;
        tParaName.setLayoutData(gridData_9);
        tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals(""))
                    addParam();
            }
        });
        tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals(""));
                if (tParaName.getText().equals("<from>")) {
                    cSource.setVisible(true);
                    tParaValue.setVisible(false);
                } else {
                    cSource.setVisible(false);
                    tParaValue.setVisible(true);
                }
            }
        });
        label6 = JOE_L_ActionsParameterForm_Value.Control(new Label(group, SWT.NONE));
        label6.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Composite composite = new Composite(group, SWT.NONE);
        composite.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                cSource.setBounds(0, 2, composite.getBounds().width, tParaName.getBounds().height);
                tParaValue.setBounds(0, 2, composite.getBounds().width, tParaName.getBounds().height);
            }
        });
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        cSource = JOE_Cbo_ActionsParameterForm_Source.Control(new Combo(composite, SWT.READ_ONLY));
        cSource.setItems(new String[] { "order", "task" });
        cSource.setBounds(0, 0, 250, 21);
        cSource.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                tParaValue.setText(cSource.getText());
            }
        });
        cSource.setVisible(false);
        tParaValue = JOE_T_ActionsParameterForm_Value.Control(new Text(composite, SWT.BORDER));
        tParaValue.setBounds(0, 0, 250, 21);
        tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tParaName.equals(""))
                    addParam();
            }
        });
        tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tParaName.getText().equals(""));
            }
        });
        bApply = JOE_B_ActionsParameterForm_Apply.Control(new Button(group, SWT.NONE));
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        bApply.setLayoutData(gridData_5);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addParam();
            }
        });
        tParameter = JOE_Tbl_ActionsParameterForm_Parameter.Control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, true, 4, 5);
        gridData_3.widthHint = 342;
        gridData_3.heightHint = 140;
        tParameter.setLayoutData(gridData_3);
        tParameter.addPaintListener(new PaintListener() {

            public void paintControl(final PaintEvent e) {
            }
        });
        tParameter.setHeaderVisible(true);
        tParameter.setLinesVisible(true);
        tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null)
                    return;
                tParaName.setText(item.getText(0));
                if (tParaName.getText().equals("<from>"))
                    cSource.setText(item.getText(1));
                tParaValue.setText(item.getText(1));
                bRemove.setEnabled(tParameter.getSelectionCount() > 0);
                bApply.setEnabled(false);
            }
        });
        TableColumn tcName = JOE_TCl_ActionsParameterForm_Name.Control(new TableColumn(tParameter, SWT.NONE));
        tcName.setWidth(252);
        TableColumn tcValue = JOE_TCl_ActionsParameterForm_Value.Control(new TableColumn(tParameter, SWT.NONE));
        tcValue.setWidth(500);
        butNewParam = JOE_B_ActionsParameterForm_NewParam.Control(new Button(group, SWT.NONE));
        butNewParam.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("");
                tParaValue.setText("");
                bRemove.setEnabled(false);
                tParameter.deselectAll();
                tParaName.setFocus();
            }
        });
        butNewParam.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Composite composite_2 = new Composite(group, SWT.NONE);
        final GridData gridData_2_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
        gridData_2_1.heightHint = 67;
        composite_2.setLayoutData(gridData_2_1);
        composite_2.setLayout(new GridLayout());
        butUp_1 = JOE_B_Up.Control(new Button(composite_2, SWT.NONE));
        butUp_1.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.changeUp(tParameter);
            }
        });
        butUp_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butUp_1.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
        butDown_1 = JOE_B_Down.Control(new Button(composite_2, SWT.NONE));
        butDown_1.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.changeDown(tParameter);
            }
        });
        butDown_1.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
        butDown_1.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
        bRemove = JOE_B_ActionsParameterForm_Remove.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bRemove.setEnabled(false);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.deleteParameter(tParameter, tParameter.getSelectionIndex());
                tParaName.setText("");
                tParaValue.setText("");
                tParameter.deselectAll();
                bRemove.setEnabled(false);
                bApply.setEnabled(false);
                tParaName.setFocus();
            }
        });
        final Composite composite_1 = new Composite(group, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, true);
        gridData.widthHint = 74;
        composite_1.setLayoutData(gridData);
        composite_1.setLayout(new GridLayout());
        final Button paramButton = JOE_B_ActionsParameterForm_Parameter.Control(new Button(composite_1, SWT.RADIO));
        paramButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        paramButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("");
                tParaValue.setText("");
                tParaName.setFocus();
            }
        });
        paramButton.setSelection(true);
        final Button fromTaskButton = JOE_B_ActionsParameterForm_FromTask.Control(new Button(composite_1, SWT.RADIO));
        fromTaskButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        fromTaskButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("<from>");
                cSource.setText("task");
                bApply.setFocus();
            }
        });
        final Button fromOrderButton = JOE_B_ActionsParameterForm_FromOrder.Control(new Button(composite_1, SWT.RADIO));
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
        fromOrderButton.setLayoutData(gridData_2);
        fromOrderButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                tParaName.setText("<from>");
                cSource.setText("order");
                bApply.setFocus();
            }
        });
    }

    private void getDescription() {
        Element desc = listener.getParent().getChild("description");
        if (desc != null) {
            Element include = desc.getChild("include");
            includeFile = Utils.getAttributeValue("file", include);
        }
    }

    public void setToolTipText() {
        if (txtParameterDescription != null) {
            txtParameterDescription.setToolTipText(Messages.getTooltip("job.param.description"));
        }
        if (txtEnvName != null) {
            tableEnvironment.setToolTipText(Messages.getTooltip("job.environment.table"));
            txtEnvName.setToolTipText(Messages.getTooltip("job.environment.name"));
            txtEnvValue.setToolTipText(Messages.getTooltip("job.environment.value"));
            butEnvApply.setToolTipText(Messages.getTooltip("job.environment.btn_apply"));
            butEnvRemove.setToolTipText(Messages.getTooltip("job.environment.btn_remove"));
            butNewEnvironment.setToolTipText(Messages.getTooltip("job.environment.new"));
        }
        // butOpenInclude.setToolTipText(Messages.getTooltip("parameter.includetable_open.name"));
    }
} // @jve:decl-index=0:visual-constraint="10,10"
