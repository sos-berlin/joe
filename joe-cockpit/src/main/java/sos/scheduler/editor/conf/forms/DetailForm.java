package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
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
import org.eclipse.swt.widgets.Tree;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.WindowsSaver;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.scheduler.editor.conf.listeners.JobChainConfigurationListener;
import sos.scheduler.editor.conf.listeners.JobListener;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IDetailUpdate;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.wizard.forms.JobAssistentImportJobParamsForm;
import com.sos.joe.wizard.forms.JobAssistentImportJobsForm;
import com.sos.joe.xml.jobscheduler.DetailDom;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class DetailForm extends SOSJOEMessageCodes {

    private Button butDown = null;
    private Button butUp = null;
    private String jobChainname = "";
    private DetailsListener detailListener = null;
    private String state = null;
    private Combo comboLanguage = null;
    private Text txtJobchainNote = null;
    private Button butApply = null;
    private Text txtName = null;
    private Text txtValue = null;
    private Table tableParams = null;
    private Button butApplyParam = null;
    private Button butRemove = null;
    private Button cancelButton = null;
    private Text txtParamNote = null;
    private Group parameterGroup = null;
    private Group jobChainGroup = null;
    private boolean isEditable = false;
    private boolean isEditableParam = false;
    private Label statusBar = null;
    private int type = -1;
    private Tree tree = null;
    private Composite parent = null;
    private JobChainConfigurationListener confListener = null;
    private DetailDom dom = null;
    private IDetailUpdate gui = null;
    private Button butXML = null;
    private Button butDocumentation = null;
    private Text paramText = null;
    private Button butText = null;
    private Text txtParamsFile = null;
    private boolean isLifeElement = false;
    private String path = null;
    private String _orderId = null;
    private ISchedulerUpdate update = null;
    private SchedulerDom schedulerDom = null;
    private Text butRefreshWizzardNoteParam = null;
    private JobListener joblistener = null;
    private String jobname = "";
    private String jobDocumentation = null;
    private final WindowsSaver w;

    public DetailForm(Composite parent_, int style, int type_, DetailDom dom_, IDetailUpdate gui_, boolean isLifeElement_, String path_) {
        super(parent_, style);
        w = new WindowsSaver(this.getClass(), getShell(), 643, 600);
        dom = dom_;
        gui = gui_;
        type = type_;
        initialize();
        parent = parent_;
        isLifeElement = isLifeElement_;
        path = path_;
    }

    private void saveWindowPosAndSize() {
        w.saveWindow();
    }

    public DetailForm(Composite parent_, int style, String jobChainname_, String state_, String orderId, int type_, DetailDom dom_,
            IDetailUpdate gui_, boolean isLifeElement_, String path_) {
        super(parent_, style);
        dom = dom_;
        gui = gui_;
        type = type_;
        jobChainname = jobChainname_;
        state = state_;
        _orderId = orderId;
        w = new WindowsSaver(this.getClass(), getShell(), 643, 600);
        w.restoreWindowLocation();
        initialize();
        w.restoreWindowSize();
        parent = parent_;
        isLifeElement = isLifeElement_;
        path = path_;
        open();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        if (type == JOEConstants.JOB_CHAINS) {
            getShell().layout();
            getShell().open();
        }
    }

    private void createGroup() {
        try {
            final GridLayout gridLayout_3 = new GridLayout();
            gridLayout_3.verticalSpacing = 10;
            gridLayout_3.marginWidth = 10;
            gridLayout_3.marginTop = 10;
            gridLayout_3.marginRight = 10;
            gridLayout_3.marginLeft = 10;
            gridLayout_3.marginHeight = 10;
            gridLayout_3.marginBottom = 10;
            gridLayout_3.numColumns = 3;
            final Group composite = JOE_G_DetailForm_MainGroup.control(new Group(this, SWT.NONE));
            composite.addDisposeListener(new DisposeListener() {

                @Override
                public void widgetDisposed(final DisposeEvent e) {
                    if (butApply.isEnabled()) {
                        save();
                    }
                }
            });
            composite.setLayout(new GridLayout());
            final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, true, 3, 1);
            gridData_6.heightHint = 31;
            composite.setLayoutData(gridData_6);
            parameterGroup = JOE_G_DetailForm_ParameterGroup.control(new Group(composite, SWT.NONE));
            parameterGroup.setEnabled(false);
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, true, true);
            gridData_3.heightHint = 239;
            parameterGroup.setLayoutData(gridData_3);
            final GridLayout gridLayout_2 = new GridLayout();
            gridLayout_2.numColumns = 6;
            parameterGroup.setLayout(gridLayout_2);
            @SuppressWarnings("unused")
            final Label nameLabel = JOE_L_Name.control(new Label(parameterGroup, SWT.NONE));
            txtName = JOE_T_DetailForm_Name.control(new Text(parameterGroup, SWT.BORDER));
            txtName.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!"".equals(txtName.getText())
                            && (tableParams.getSelectionCount() == 0 || (tableParams.getSelectionCount() > 0 && !tableParams.getSelection()[0].getText(
                                    0).equalsIgnoreCase(txtName.getText())))) {
                        isEditableParam = true;
                        butApplyParam.setEnabled(isEditableParam);
                        txtValue.setEnabled(true);
                        butText.setEnabled(true);
                        paramText.setText("");
                        txtParamNote.setEnabled(true);
                    } else {
                        butText.setEnabled(false);
                    }
                }
            });
            txtName.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR && !"".equals(txtName.getText())) {
                        addParam();
                    }
                }
            });
            txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            txtName.setFocus();
            final Label valueLabel = JOE_L_Value.control(new Label(parameterGroup, SWT.NONE));
            txtValue = JOE_T_DetailForm_Value.control(new Text(parameterGroup, SWT.BORDER));
            txtValue.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR && !"".equals(txtName.getText())) {
                        addParam();
                    }
                }
            });
            txtValue.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (!"".equals(txtName.getText())
                            && (tableParams.getSelectionCount() == 0 || (tableParams.getSelectionCount() > 0 && !tableParams.getSelection()[0].getText(
                                    1).equalsIgnoreCase(txtValue.getText())))) {
                        isEditableParam = true;
                        butApplyParam.setEnabled(isEditableParam);
                        if (!txtValue.getText().trim().isEmpty()) {
                            butText.setEnabled(false);
                        } else {
                            butText.setEnabled(true);
                        }
                    }
                }
            });
            txtValue.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            butText = JOE_B_DetailForm_Text.control(new Button(parameterGroup, SWT.NONE));
            butText.setEnabled(false);
            butText.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    String ntext = "";
                    if (tableParams.getSelectionCount() > 0) {
                        TableItem item = tableParams.getSelection()[0];
                        ntext = item.getText(2);
                    }
                    String text = sos.scheduler.editor.app.Utils.showClipboard(ntext, getShell(), true, "");
                    if (text != null && !text.trim().equalsIgnoreCase(ntext)) {
                        paramText.setText(text);
                        txtValue.setText("");
                        txtValue.setEnabled(false);
                        butText.setEnabled(true);
                        addParam();
                    } else if (text == null) {
                        txtValue.setEnabled(true);
                        butText.setEnabled(true);
                    } else {
                        txtValue.setEnabled(true);
                        butText.setEnabled(false);
                    }
                    butApply.setEnabled(true);
                }
            });
            butApplyParam = JOE_B_DetailForm_ApplyParam.control(new Button(parameterGroup, SWT.NONE));
            butApplyParam.setEnabled(isEditableParam);
            butApplyParam.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    addParam();
                }
            });
            final GridData gridData_9 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
            butApplyParam.setLayoutData(gridData_9);
            tableParams = JOE_Tbl_DetailForm_Params.control(new Table(parameterGroup, SWT.FULL_SELECTION | SWT.BORDER));
            tableParams.setEnabled(false);
            tableParams.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tableParams.getSelectionCount() > 0) {
                        TableItem item = tableParams.getSelection()[0];
                        txtName.setText(item.getText(0));
                        if (item.getText(1) != null && !item.getText(1).trim().isEmpty()) {
                            paramText.setText("");
                            txtValue.setText(item.getText(1));
                            txtValue.setEnabled(true);
                            butText.setEnabled(false);
                        }
                        if (item.getText(2) != null && !item.getText(2).trim().isEmpty()) {
                            paramText.setText(item.getText(2));
                            txtValue.setText("");
                            txtValue.setEnabled(false);
                            butText.setEnabled(true);
                        }
                        if ("".equals(item.getText(1).trim()) && "".equals(item.getText(2).trim())) {
                            paramText.setText("");
                            txtValue.setText("");
                            txtValue.setEnabled(true);
                            butText.setEnabled(true);
                        }
                        txtParamNote.setText(detailListener.getParamNote(item.getText(0), comboLanguage.getText()));
                        butRemove.setEnabled(true);
                        txtParamNote.setEnabled(true);
                        isEditableParam = false;
                    } else {
                        butRemove.setEnabled(false);
                    }
                }
            });
            tableParams.setLinesVisible(true);
            tableParams.setHeaderVisible(true);
            final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 7);
            tableParams.setLayoutData(gridData_4);
            final TableColumn newColumnTableColumn = JOE_TCl_DetailForm_NameColumn.control(new TableColumn(tableParams, SWT.NONE));
            newColumnTableColumn.setWidth(118);
            newColumnTableColumn.addControlListener(new ControlAdapter() {

                @Override
                public void controlResized(final ControlEvent e) {
                    w.saveTableColumn("tableParams", newColumnTableColumn);
                }
            });
            w.restoreTableColumn("tableParams", newColumnTableColumn, 118);
            final TableColumn newColumnTableColumn_1 = JOE_TCl_DetailForm_ValueColumn.control(new TableColumn(tableParams, SWT.NONE));
            newColumnTableColumn_1.setWidth(150);
            newColumnTableColumn_1.addControlListener(new ControlAdapter() {

                @Override
                public void controlResized(final ControlEvent e) {
                    w.saveTableColumn("tableParams", newColumnTableColumn_1);
                }
            });
            w.restoreTableColumn("tableParams", newColumnTableColumn_1, 150);
            final TableColumn newColumnTableColumn_2 = JOE_TCl_DetailForm_TextColumn.control(new TableColumn(tableParams, SWT.NONE));
            newColumnTableColumn_2.setWidth(100);
            newColumnTableColumn_2.addControlListener(new ControlAdapter() {

                @Override
                public void controlResized(final ControlEvent e) {
                    w.saveTableColumn("tableParams", newColumnTableColumn_2);
                }
            });
            w.restoreTableColumn("tableParams", newColumnTableColumn_2, 100);
            final Button butNew = JOE_B_DetailForm_New.control(new Button(parameterGroup, SWT.NONE));
            butNew.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    txtName.setText("");
                    txtValue.setText("");
                    paramText.setText("");
                    txtValue.setEnabled(true);
                    paramText.setEnabled(true);
                    tableParams.deselectAll();
                    txtParamNote.setText("");
                }
            });
            butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            final Composite composite_2 = JOE_Composite1.control(new Composite(parameterGroup, SWT.NONE));
            final GridData gridData_2_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
            gridData_2_1.heightHint = 67;
            composite_2.setLayoutData(gridData_2_1);
            composite_2.setLayout(new GridLayout());
            butUp = JOE_B_DetailForm_Up.control(new Button(composite_2, SWT.NONE));
            butUp.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    detailListener.changeUp(tableParams);
                }
            });
            butUp.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
            butDown = JOE_B_DetailForm_Down.control(new Button(composite_2, SWT.NONE));
            butDown.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    detailListener.changeDown(tableParams);
                }
            });
            butDown.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
            butDown.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
            final Button parameterButton = JOE_B_DetailForm_Wizard.control(new Button(parameterGroup, SWT.NONE));
            parameterButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    startWizzard();
                }
            });
            parameterButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butRemove = JOE_B_DetailForm_Remove.control(new Button(parameterGroup, SWT.NONE));
            butRemove.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tableParams.getSelectionCount() > 0) {
                        detailListener.deleteParameter(tableParams, tableParams.getSelectionIndex());
                        txtParamNote.setText("");
                        txtName.setText("");
                        txtValue.setText("");
                        tableParams.deselectAll();
                        butRemove.setEnabled(false);
                        txtParamNote.setText("");
                        isEditableParam = false;
                        butApplyParam.setEnabled(isEditableParam);
                        butApply.setEnabled(true);
                        txtName.setFocus();
                        if (gui != null) {
                            gui.updateParam();
                        }
                    }
                }
            });
            final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
            gridData_8.widthHint = 64;
            gridData_8.minimumWidth = 50;
            butRemove.setLayoutData(gridData_8);
            final Button butTemp = JOE_B_DetailForm_TempDocumentation.control(new Button(parameterGroup, SWT.NONE));
            butTemp.setLayoutData(new GridData());
            butTemp.setVisible(false);
            butApply = JOE_B_DetailForm_ApplyDetails.control(new Button(parameterGroup, SWT.NONE));
            butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butApply.setEnabled(isEditable);
            FontData[] fontDatas = butApply.getFont().getFontData();
            FontData data = fontDatas[0];
            butApply.setFont(new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD));
            butApply.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    save();
                }
            });
            cancelButton = JOE_B_Cancel.control(new Button(parameterGroup, SWT.NONE));
            cancelButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    saveWindowPosAndSize();
                    if (butApply.getEnabled()) {
                        int count = MainWindow.message(getShell(), JOE_M_0008.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                        if (count != SWT.OK) {
                            return;
                        }
                    }
                    getShell().dispose();
                }
            });
            txtParamNote =
                    JOE_T_DetailForm_JobChainNote.control(new Text(parameterGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL));
            txtParamNote.setEnabled(false);
            txtParamNote.addVerifyListener(new VerifyListener() {

                @Override
                public void verifyText(final VerifyEvent e) {
                    if (e.keyCode == 8 || e.keyCode == 127) {
                        isEditableParam = true;
                        butApplyParam.setEnabled(isEditableParam);
                    }
                }
            });
            txtParamNote.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    changeParameNote();
                }
            });
            final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
            gridData_5.heightHint = 73;
            txtParamNote.setLayoutData(gridData_5);
            comboLanguage = JOE_Cbo_DetailForm_Language.control(new Combo(parameterGroup, SWT.READ_ONLY));
            comboLanguage.setItems(new String[] { "de", "en" });
            final GridData gridData_7 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
            comboLanguage.setLayoutData(gridData_7);
            comboLanguage.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
                    if (tableParams.getSelectionCount() > 0) {
                        TableItem item = tableParams.getSelection()[0];
                        txtParamNote.setText(detailListener.getParamNote(item.getText(0), comboLanguage.getText()));
                    } else if (txtName.getText() != null && !txtName.getText().isEmpty()) {
                        txtParamNote.setText(detailListener.getParamNote(txtName.getText(), comboLanguage.getText()));
                    } else if (txtParamNote.getText() != null && !txtParamNote.getText().isEmpty()) {
                        txtParamNote.setText("");
                    }
                    isEditable = false;
                    isEditableParam = false;
                    butApplyParam.setEnabled(isEditableParam);
                    butRemove.setEnabled(false);
                }
            });
            comboLanguage.select(0);
            butRefreshWizzardNoteParam = JOE_B_DetailForm_RefreshWizardNoteParam.control(new Text(parameterGroup, SWT.CHECK));
            butRefreshWizzardNoteParam.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    refreshTable();
                }
            });
            butRefreshWizzardNoteParam.setVisible(false);
            butRefreshWizzardNoteParam.setLayoutData(new GridData());
            paramText = JOE_T_DetailForm_Param.control(new Text(parameterGroup, SWT.BORDER));
            paramText.setVisible(false);
            final GridData gridData_14 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
            gridData_14.widthHint = 27;
            paramText.setLayoutData(gridData_14);
            jobChainGroup = JOE_G_DetailForm_NoteGroup.control(new Group(parameterGroup, SWT.NONE));
            jobChainGroup.setEnabled(false);
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.numColumns = 2;
            jobChainGroup.setLayout(gridLayout_1);
            final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 6, 1);
            gridData.horizontalIndent = -1;
            jobChainGroup.setLayoutData(gridData);
            txtJobchainNote =
                    JOE_T_DetailForm_JobChainNote.control(new Text(jobChainGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL));
            txtJobchainNote.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (detailListener != null) {
                        isEditable = true;
                        if (gui != null) {
                            gui.updateNote();
                        }
                        detailListener.setNote(txtJobchainNote.getText(), comboLanguage.getText());
                        butApply.setEnabled(isEditable);
                    }
                }
            });
            final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 2);
            txtJobchainNote.setLayoutData(gridData_2);
            butXML = JOE_B_DetailForm_XML.control(new Button(jobChainGroup, SWT.NONE));
            butXML.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butXML.setEnabled(false);
            butXML.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    try {
                        if (dom != null && dom.isChanged()) {
                            MainWindow.message(JOE_M_0020.label(), SWT.ICON_ERROR);
                            return;
                        }
                        if (dom == null && butApply.isEnabled()) {
                            int c = MainWindow.message(JOE_M_0021.label(), SWT.YES | SWT.NO | SWT.ICON_ERROR);
                            if (c == SWT.YES) {
                                detailListener.save();
                            }
                        }
                        if (type == JOEConstants.JOB_CHAINS) {
                            DetailXMLEditorDialogForm dialog =
                                    new DetailXMLEditorDialogForm(detailListener.getConfigurationFilename(), jobChainname, state, _orderId, type,
                                            isLifeElement, path);
                            dialog.showXMLEditor();
                            getShell().dispose();
                        } else {
                            if (dom != null && dom.getFilename() != null && !dom.getFilename().isEmpty()) {
                                DetailXMLEditorDialogForm dialog = new DetailXMLEditorDialogForm(dom, type, isLifeElement, path);
                                dialog.setConfigurationData(confListener, tree, parent);
                                dialog.showXMLEditor();
                            } else {
                                MainWindow.message(JOE_M_0020.label(), SWT.ICON_ERROR);
                            }
                        }
                    } catch (Exception ex) {
                        new ErrorLog(JOE_M_0002.params(sos.util.SOSClassUtil.getMethodName()), ex);
                    }
                }
            });
            butDocumentation = JOE_B_DetailForm_Documentation.control(new Button(jobChainGroup, SWT.NONE));
            butDocumentation.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
            butDocumentation.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    String filename = null;
                    try {
                        if (type == JOEConstants.JOB_CHAINS) {
                            filename = detailListener.getConfigurationFilename();
                        } else if (dom != null) {
                            filename = dom.getFilename();
                        }
                        if (filename != null && !filename.isEmpty()) {
                            File file = new File(filename);
                            if (file.exists()) {
                                Program prog = Program.findProgram("html");
                                if (prog != null) {
                                    prog.execute(new File(filename).toURL().toString());
                                } else {
                                    String[] split = Options.getBrowserExec(new File(filename).toURL().toString(), Options.getLanguage());
                                    Runtime.getRuntime().exec(split);
                                }
                            } else {
                                MainWindow.message(JOE_M_0013.params(file.getCanonicalPath()), SWT.ICON_ERROR);
                            }
                        } else {
                            MainWindow.message(JOE_M_0012.label(), SWT.ICON_ERROR);
                        }
                    } catch (Exception ex) {
                        new ErrorLog(JOE_M_0011.params(sos.util.SOSClassUtil.getMethodName(), filename, ex.getMessage()));
                    }
                }
            });
            final Label fileLabel = JOE_L_DetailForm_JobDocumentation.control(new Label(parameterGroup, SWT.NONE));
            fileLabel.setLayoutData(new GridData());
            txtParamsFile = JOE_T_DetailForm_ParamsFile.control(new Text(parameterGroup, SWT.BORDER));
            txtParamsFile.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    detailListener.setParamsFileName(txtParamsFile.getText());
                    if (gui != null) {
                        gui.updateNote();
                    }
                    butApply.setEnabled(isEditable);
                }
            });
            txtParamsFile.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
            statusBar = JOE_L_DetailForm_ConfigFile.control(new Label(composite, SWT.BORDER));
            final GridData gridData_11 = new GridData(GridData.FILL, GridData.END, false, false);
            gridData_11.widthHint = 496;
            gridData_11.heightHint = 18;
            statusBar.setLayoutData(gridData_11);
            if (type == JOEConstants.JOB_CHAINS) {
                setEnabled_(false);
            }
            setVisibility();
        } catch (Exception e) {
            new ErrorLog(JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e.toString()), e);
        }
    }

    private void setVisibility() {
        if (type == JOEConstants.DETAILS) {
            cancelButton.setVisible(false);
            statusBar.setVisible(false);
            butApply.setVisible(false);
        }
    }

    private void addParam() {
        if (!txtName.getText().isEmpty()) {
            detailListener.setParam(txtName.getText(), txtValue.getText(), txtParamNote.getText(), paramText.getText(), comboLanguage.getText());
            txtParamNote.setText(detailListener.getParamNote(txtName.getText(), comboLanguage.getText()));
            tableParams.removeAll();
            detailListener.fillParams(tableParams);
            butApply.setEnabled(isEditable);
            txtName.setText("");
            txtValue.setText("");
            isEditableParam = false;
            butApplyParam.setEnabled(isEditableParam);
            txtName.setFocus();
            isEditable = true;
            butApply.setEnabled(isEditable);
            butRemove.setEnabled(false);
            txtParamNote.setText("");
            if (gui != null) {
                gui.updateParam();
            }
            paramText.setText("");
            txtParamNote.setEnabled(false);
        }
    }

    private boolean discardChanges() {
        if (butApply.getEnabled()) {
            int count = MainWindow.message(getShell(), JOE_M_0022.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            if (count != SWT.OK) {
                return false;
            }
        }
        setEnabled_(true);
        return true;
    }

    public void open(String orderId) {
        open();
    }

    public void open() {
        if (!discardChanges()) {
            return;
        }
        if (!initForm()) {
            return;
        }
        isEditable = true;
        txtJobchainNote.setFocus();
        butXML.setEnabled(true);
        butApply.setEnabled(false);
        butApplyParam.setEnabled(false);
        if (detailListener != null && detailListener.getConfigurationFilename() != null) {
            statusBar.setText(JOE_M_0023.params(detailListener.getConfigurationFilename()));
        }
        txtName.setFocus();
    }

    private boolean initForm() {
        tableParams.removeAll();
        txtParamNote.setText("");
        txtName.setText("");
        txtValue.setText("");
        detailListener = new DetailsListener(jobChainname, state, _orderId, type, dom, isLifeElement, path);
        if (detailListener != null && detailListener.hasError()) {
            if (type == JOEConstants.DETAILS) {
                dispose();
            }
            getShell().dispose();
            return false;
        }
        if (!detailListener.getNote(comboLanguage.getText()).isEmpty()) {
            txtJobchainNote.setText(detailListener.getNote(comboLanguage.getText()));
        }
        txtParamsFile.setText(detailListener.getParamsFileName());
        detailListener.fillParams(tableParams);
        butRemove.setEnabled(false);
        return true;
    }

    private void setEnabled_(boolean enabled) {
        jobChainGroup.setEnabled(enabled);
        parameterGroup.setEnabled(enabled);
        comboLanguage.setEnabled(enabled);
        txtJobchainNote.setEnabled(enabled);
        txtName.setEnabled(enabled);
        txtValue.setEnabled(enabled);
        tableParams.setEnabled(enabled);
        txtParamNote.setEnabled(enabled);
        butRemove.setEnabled(enabled);
    }

    public void setTree(Tree tree_) {
        tree = tree_;
    }

    public void setJobChainConfigurationListener(JobChainConfigurationListener confListener_) {
        confListener = confListener_;
    }

    public boolean hasErrors() {
        if (detailListener != null) {
            return detailListener.hasError();
        }
        return false;
    }

    private void changeParameNote() {
        if (detailListener == null) {
            return;
        }
        if (txtParamNote.getText() != null && txtParamNote.getText().isEmpty()) {
            return;
        }
        if (txtName.getText() != null && txtName.getText().isEmpty()) {
            MainWindow.message(getShell(), JOE_M_0024.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            return;
        }
        if (tableParams.getSelectionCount() == 0
                || (tableParams.getSelectionCount() > 0 && !txtParamNote.getText().equalsIgnoreCase(
                        detailListener.getParamNote(tableParams.getSelection()[0].getText(0), comboLanguage.getText())))) {
            detailListener.setParam(txtName.getText(), txtValue.getText(), txtParamNote.getText(), paramText.getText(), comboLanguage.getText());
            isEditableParam = true;
            butApplyParam.setEnabled(isEditableParam);
            isEditable = true;
            butApply.setEnabled(isEditable);
            if (gui != null) {
                gui.updateParamNote();
            }
        }
    }

    public DetailDom getDom() {
        return dom;
    }

    public void onJobAssistentImportJobParamsFormClose() {
        for (int i = 0; i < tableParams.getItemCount(); i++) {
            TableItem item = tableParams.getItem(i);
            detailListener.setParam(item.getText(0), item.getText(1), null, item.getText(2), comboLanguage.getText());
        }
    }

    private void save() {
        saveWindowPosAndSize();
        if (butApplyParam.isEnabled()) {
            addParam();
        }

        detailListener.save();
        if (schedulerDom != null && state != null) {
            DetailsListener.addMonitoring2Job(jobChainname, state, schedulerDom, update);
            update.updateScripts();
            if (MainWindow.getContainer().getCurrentTab().getData("ftp_remote_directory") != null) {
                schedulerDom.setChanged(true);
            }
        }
        if (type == JOEConstants.JOB_CHAINS) {
            isEditable = false;
            butApply.setEnabled(isEditable);
            getShell().dispose();
        } else {
            isEditable = false;
            butApply.setEnabled(isEditable);
        }
    }

    public void setParamsForWizzard(SchedulerDom dom_, ISchedulerUpdate update_) {
        schedulerDom = dom_;
        update = update_;
    }

    public void setParamsForWizzard(SchedulerDom dom_, ISchedulerUpdate update_, String jobname_) {
        schedulerDom = dom_;
        update = update_;
        jobname = jobname_;
        getJobDocumentation();
    }

    private void createTempSchedulerDom() {
        if (schedulerDom == null) {
            schedulerDom = new SchedulerDom();
        }
        CTabFolder folder = new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
        joblistener = new JobListener(schedulerDom, detailListener.getParams().getParentElement(), update);
    }

    private void startWizzard() {
        Utils.startCursor(getShell());
        butApply.setEnabled(true);
        try {
            if (joblistener == null) {
                createTempSchedulerDom();
            }
            getJobDocumentation();
            if (jobDocumentation != null && !jobDocumentation.trim().isEmpty()) {
                JobAssistentImportJobParamsForm paramsForm =
                        new JobAssistentImportJobParamsForm(schedulerDom, joblistener.get_main(), joblistener, tableParams, JOEConstants.PARAMETER);
                paramsForm.showAllImportJobParams(jobDocumentation);
            } else {
                JobAssistentImportJobsForm importParameterForms = new JobAssistentImportJobsForm(joblistener, tableParams, JOEConstants.PARAMETER);
                importParameterForms.setDetailForm(this);
                importParameterForms.showAllImportJobs();
                importParameterForms.setDetailsRefresh(butRefreshWizzardNoteParam);
            }
            butApply.setEnabled(true);
            if (dom != null) {
                dom.setChanged(true);
            }
        } catch (Exception e) {
            new ErrorLog(JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e.getMessage()), e);
        }
        Utils.stopCursor(getShell());
    }

    private void refreshTable() {
        try {
            detailListener.refreshParams(tableParams);
            butApply.setEnabled(true);
        } catch (Exception e) {
            new ErrorLog(JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e.getMessage()), e);
        }
    }

    private void getJobDocumentation() {
        if (jobname != null && !jobname.isEmpty()) {
            try {
                XPath x = XPath.newInstance("//job[@name='" + jobname + "']/description/include");
                List listOfElement = x.selectNodes(schedulerDom.getDoc());
                if (!listOfElement.isEmpty()) {
                    Element include = (Element) listOfElement.get(0);
                    if (include != null) {
                        jobDocumentation = Utils.getAttributeValue("file", include);
                    }
                }
                if (jobDocumentation != null && !jobDocumentation.isEmpty() && txtParamsFile != null) {
                    txtParamsFile.setText(jobDocumentation);
                }
            } catch (Exception e) {
                System.out.println(JOE_M_0010.params("setParamsForWizzard", e.toString()));
            }
        }
    }

}