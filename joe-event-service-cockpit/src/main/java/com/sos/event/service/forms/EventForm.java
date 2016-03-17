package com.sos.event.service.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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

import com.sos.event.service.listeners.EventListener;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class EventForm extends SOSJOEMessageCodes implements IUnsaved {

    private EventListener listener = null;
    private Group group = null;
    private Text txtEventName = null;
    private int type = -1;
    private Table table = null;
    private Button butApply = null;
    private Button butNew = null;
    private Button butRemove = null;
    private Combo cboEventClass = null;
    private Text txtEventId = null;
    private Text txtJobChain = null;
    private Text txtJobname = null;
    private Text txtOrderId = null;
    private Text txtTitle = null;
    private Text txtComment = null;
    private Text txtExitCode = null;
    private Text txtHourExpirationPeriod = null;
    private Text txtMinExpirationPeriod = null;
    private Text txtSecExpirationPeriod = null;
    private Text txtHourExpirationCycle = null;
    private Text txtMinExpirationCycle = null;
    private Text txtSecExpirationCycle = null;
    private Group matchingAttributesGroup = null;

    public EventForm(final Composite parent, final int style, final ActionsDom dom, final Element eventGroup, final int type_) {
        super(parent, style);
        type = type_;
        listener = new EventListener(dom, eventGroup, type_);
        initialize();
        txtEventName.setFocus();
    }

    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
        listener.fillEvent(table);
        if (type == JOEConstants.EVENT_GROUP) {
            group.setText(JOE_G_EventForm_ActionGroup.params(listener.getActionName(), listener.getEventGroupName()));
        } else if (type == JOEConstants.REMOVE_EVENT_GROUP) {
            group.setText(JOE_G_EventForm_ActionRemoveEvent.params(listener.getActionName()));
        } else {
            group.setText(JOE_G_EventForm_ActionAddEvent.params(listener.getActionName()));
        }
        group.setTabList(new org.eclipse.swt.widgets.Control[] { txtEventName, butApply, txtTitle, butNew, matchingAttributesGroup, txtComment });
        matchingAttributesGroup.setTabList(new org.eclipse.swt.widgets.Control[] { cboEventClass, txtEventId, txtJobname, txtJobChain, txtOrderId,
                txtExitCode });
        cboEventClass.setItems(listener.getEventClasses());
        butApply.setEnabled(false);
    }

    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        group = JOE_G_EventForm_ActionGroup.Control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout);
        final Label lblLogic = JOE_L_EventForm_EventName.Control(new Label(group, SWT.NONE));
        lblLogic.setLayoutData(new GridData());
        txtEventName = JOE_T_EventForm_EventName.Control(new Text(group, SWT.BORDER));
        txtEventName.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtEventName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtEventName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butApply = JOE_B_EventForm_Apply.Control(new Button(group, SWT.NONE));
        butApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                apply();
            }
        });
        butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Label eventTitleLabel = JOE_L_EventForm_EventTitle.Control(new Label(group, SWT.NONE));
        eventTitleLabel.setLayoutData(new GridData());
        txtTitle = JOE_T_EventForm_EventTitle.Control(new Text(group, SWT.BORDER));
        txtTitle.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtTitle.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butNew = JOE_B_EventForm_New.Control(new Button(group, SWT.NONE));
        butNew.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refresh();
            }
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        matchingAttributesGroup = JOE_G_EventForm_MatchingAttributes.Control(new Group(group, SWT.NONE));
        matchingAttributesGroup.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 3, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginTop = 5;
        gridLayout_1.numColumns = 4;
        matchingAttributesGroup.setLayout(gridLayout_1);
        final Label txtEventClass = JOE_L_EventForm_EventClass.Control(new Label(matchingAttributesGroup, SWT.NONE));
        cboEventClass = JOE_Cbo_EventForm_EventClass.Control(new Combo(matchingAttributesGroup, SWT.NONE));
        cboEventClass.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        cboEventClass.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        cboEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        final Label labeld = JOE_L_EventForm_EventID.Control(new Label(matchingAttributesGroup, SWT.NONE));
        txtEventId = JOE_T_EventForm_EventID.Control(new Text(matchingAttributesGroup, SWT.BORDER));
        txtEventId.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtEventId.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtEventId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        final Label jobNameLabel = JOE_L_EventForm_JobName.Control(new Label(matchingAttributesGroup, SWT.NONE));
        txtJobname = JOE_T_EventForm_JobName.Control(new Text(matchingAttributesGroup, SWT.BORDER));
        txtJobname.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtJobname.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtJobname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        final Label jobChainLabel = JOE_L_EventForm_JobChain.Control(new Label(matchingAttributesGroup, SWT.NONE));
        txtJobChain = JOE_T_EventForm_JobChain.Control(new Text(matchingAttributesGroup, SWT.BORDER));
        txtJobChain.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtJobChain.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtJobChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        final Label lblOrderId = JOE_L_EventForm_OrderID.Control(new Label(matchingAttributesGroup, SWT.NONE));
        txtOrderId = JOE_T_EventForm_OrderID.Control(new Text(matchingAttributesGroup, SWT.BORDER));
        txtOrderId.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtOrderId.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtOrderId.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        final Label exitCodeLabel = JOE_L_EventForm_ExitCode.Control(new Label(matchingAttributesGroup, SWT.NONE));
        exitCodeLabel.setLayoutData(new GridData());
        txtExitCode = JOE_T_EventForm_ExitCode.Control(new Text(matchingAttributesGroup, SWT.BORDER));
        txtExitCode.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtExitCode.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    apply();
                }
            }
        });
        txtExitCode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
        if (type == JOEConstants.ADD_EVENT_GROUP) {
            createExpirationTime(matchingAttributesGroup);
        }
        final Label commentLabel = JOE_L_EventForm_Comment.Control(new Label(group, SWT.NONE));
        commentLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        txtComment = JOE_T_EventForm_Comment.Control(new Text(group, SWT.MULTI | SWT.BORDER));
        txtComment.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1);
        gridData.heightHint = 45;
        txtComment.setLayoutData(gridData);
        table = JOE_Tbl_EventForm_Events.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    TableItem item = table.getSelection()[0];
                    txtEventName.setText(item.getText(0));
                    txtEventId.setText(item.getText(1));
                    txtTitle.setText(item.getText(2));
                    cboEventClass.setText(item.getText(3));
                    txtJobname.setText(item.getText(4));
                    txtJobChain.setText(item.getText(5));
                    txtOrderId.setText(item.getText(6));
                    txtComment.setText(item.getText(7));
                    txtExitCode.setText(item.getText(8));
                    if (type == JOEConstants.ADD_EVENT_GROUP) {
                        int hour = Utils.getHours(item.getText(9), 0);
                        int min = Utils.getMinutes(item.getText(9), 0);
                        int sec = Utils.getSeconds(item.getText(9), 0);
                        if (hour + min + sec > 0) {
                            txtHourExpirationPeriod.setText(String.valueOf(Utils.getHours(item.getText(9), 0)));
                            txtMinExpirationPeriod.setText(String.valueOf(Utils.getMinutes(item.getText(9), 0)));
                            txtSecExpirationPeriod.setText(String.valueOf(Utils.getSeconds(item.getText(9), 0)));
                        }
                        hour = Utils.getHours(item.getText(10), 0);
                        min = Utils.getMinutes(item.getText(10), 0);
                        sec = Utils.getSeconds(item.getText(10), 0);
                        if (hour + min + sec > 0) {
                            txtHourExpirationCycle.setText(String.valueOf(Utils.getHours(item.getText(10), 0)));
                            txtMinExpirationCycle.setText(String.valueOf(Utils.getMinutes(item.getText(10), 0)));
                            txtSecExpirationCycle.setText(String.valueOf(Utils.getSeconds(item.getText(10), 0)));
                        }
                    }
                }
                butApply.setEnabled(false);
                butRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        final TableColumn newColumnTableColumn = JOE_L_EventForm_EventName.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn.setWidth(70);
        final TableColumn newColumnTableColumn_1 = JOE_L_EventForm_EventID.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_1.setWidth(70);
        final TableColumn newColumnTableColumn_2 = JOE_L_EventForm_EventTitle.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_2.setWidth(70);
        final TableColumn newColumnTableColumn_3 = JOE_L_EventForm_EventClass.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_3.setWidth(73);
        final TableColumn newColumnTableColumn_4 = JOE_L_EventForm_JobName.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_4.setWidth(70);
        final TableColumn newColumnTableColumn_5 = JOE_L_EventForm_JobChain.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_5.setWidth(70);
        final TableColumn newColumnTableColumn_6 = JOE_L_EventForm_OrderID.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_6.setWidth(70);
        final TableColumn newColumnTableColumn_7 = JOE_L_EventForm_Comment.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_7.setWidth(70);
        final TableColumn newColumnTableColumn_8 = JOE_L_EventForm_ExitCode.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_8.setWidth(50);
        final TableColumn expiration_period = JOE_L_EventForm_ExpirationPeriod.Control(new TableColumn(table, SWT.NONE));
        expiration_period.setWidth(type == JOEConstants.ADD_EVENT_GROUP ? 100 : 0);
        final TableColumn newColumnTableColumn_10 = JOE_L_EventForm_ExpirationCycle.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_10.setWidth(type == JOEConstants.ADD_EVENT_GROUP ? 100 : 0);
        butRemove = JOE_B_EventForm_Remove.Control(new Button(group, SWT.NONE));
        butRemove.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (table != null && table.getSelectionCount() > 0) {
                    int cont = 0;
                    if (type == JOEConstants.EVENT_GROUP) {
                        cont = ErrorLog.message(getShell(), JOE_M_EventForm_RemoveGroup.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    } else {
                        cont = ErrorLog.message(getShell(), JOE_M_EventForm_RemoveCommand.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    }
                    if (cont == SWT.OK) {
                        listener.removeEvent(table);
                    }
                    refresh();
                }
            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
        gridData_1.widthHint = 55;
        butRemove.setLayoutData(gridData_1);
    }

    @Override
    public boolean isUnsaved() {
        return butApply.isEnabled();
    }

    @Override
    public void apply() {
        try {
            if (butApply.isEnabled()) {
                listener.apply(txtEventName.getText(), txtEventId.getText(), cboEventClass.getText(), txtTitle.getText(), txtJobname.getText(), txtJobChain.getText(), txtOrderId.getText(), txtComment.getText(), txtExitCode.getText(), getExpirationPeriod(), getExpirationCycle(), table);
                cboEventClass.setItems(listener.getEventClasses());
                refresh();
            }
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            ErrorLog.message((JOE_E_0002.params("'save Event'") + e.getMessage()), SWT.ICON_WARNING);

        }

    }

    private void refresh() {
        txtEventName.setText("");
        txtEventId.setText("");
        txtTitle.setText("");
        cboEventClass.setText("");
        txtJobname.setText("");
        txtJobChain.setText("");
        txtOrderId.setText("");
        txtComment.setText("");
        txtExitCode.setText("");
        table.deselectAll();
        butApply.setEnabled(false);
        butRemove.setEnabled(false);
        if (type == JOEConstants.ADD_EVENT_GROUP) {
            txtHourExpirationPeriod.setText("");
            txtMinExpirationPeriod.setText("");
            txtSecExpirationPeriod.setText("");
            txtHourExpirationCycle.setText("");
            txtMinExpirationCycle.setText("");
            txtSecExpirationCycle.setText("");
        }
        txtEventName.setFocus();
    }

    private void createExpirationTime(final Group matchingAttributesGroup) {
        final Label expirationPeriodLabel = JOE_L_EventForm_ExpirationPeriod.Control(new Label(matchingAttributesGroup, SWT.NONE));
        final Composite composite = new Composite(matchingAttributesGroup, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.verticalSpacing = 0;
        gridLayout_2.marginWidth = 0;
        gridLayout_2.marginHeight = 0;
        gridLayout_2.horizontalSpacing = 0;
        gridLayout_2.numColumns = 6;
        composite.setLayout(gridLayout_2);
        txtHourExpirationPeriod = JOE_T_EventForm_HourExpirationPeriod.Control(new Text(composite, SWT.BORDER));
        txtHourExpirationPeriod.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 23, txtHourExpirationPeriod);
            }
        });
        txtHourExpirationPeriod.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtHourExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
        txtHourExpirationPeriod.setTextLimit(2);
        final Label label = JOE_L_Colon.Control(new Label(composite, SWT.NONE));
        label.setAlignment(SWT.CENTER);
        final GridData gridData_2 = new GridData(10, SWT.DEFAULT);
        label.setLayoutData(gridData_2);
        txtMinExpirationPeriod = JOE_T_EventForm_MinExpirationPeriod.Control(new Text(composite, SWT.BORDER));
        txtMinExpirationPeriod.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtMinExpirationPeriod);
            }
        });
        txtMinExpirationPeriod.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtMinExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
        txtMinExpirationPeriod.setTextLimit(2);
        final Label label_1 = JOE_L_Colon.Control(new Label(composite, SWT.NONE));
        final GridData gridData_2_1 = new GridData(10, SWT.DEFAULT);
        label_1.setLayoutData(gridData_2_1);
        label_1.setAlignment(SWT.CENTER);
        txtSecExpirationPeriod = JOE_T_EventForm_SecExpirationPeriod.Control(new Text(composite, SWT.BORDER));
        txtSecExpirationPeriod.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtSecExpirationPeriod);
            }
        });
        txtSecExpirationPeriod.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtSecExpirationPeriod.setLayoutData(new GridData(30, SWT.DEFAULT));
        txtSecExpirationPeriod.setTextLimit(2);
        final Label hhmmssLabel = JOE_L_JobAssistent_TimeFormat.Control(new Label(composite, SWT.NONE));
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_3.horizontalIndent = 5;
        hhmmssLabel.setLayoutData(gridData_3);
        final Label expirationCycleLabel = JOE_L_EventForm_ExpirationCycle.Control(new Label(matchingAttributesGroup, SWT.NONE));
        expirationCycleLabel.setLayoutData(new GridData(96, SWT.DEFAULT));
        final Composite composite_1 = new Composite(matchingAttributesGroup, SWT.NONE);
        composite_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.verticalSpacing = 0;
        gridLayout_3.numColumns = 6;
        gridLayout_3.marginWidth = 0;
        gridLayout_3.marginHeight = 0;
        gridLayout_3.horizontalSpacing = 0;
        composite_1.setLayout(gridLayout_3);
        txtHourExpirationCycle = JOE_T_EventForm_HourExpirationCycle.Control(new Text(composite_1, SWT.BORDER));
        txtHourExpirationCycle.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 23, txtHourExpirationCycle);
            }
        });
        txtHourExpirationCycle.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtHourExpirationCycle.setTextLimit(2);
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_4.widthHint = 30;
        txtHourExpirationCycle.setLayoutData(gridData_4);
        final Label label_2 = JOE_L_Colon.Control(new Label(composite_1, SWT.NONE));
        final GridData gridData_2_2 = new GridData(10, SWT.DEFAULT);
        label_2.setLayoutData(gridData_2_2);
        label_2.setAlignment(SWT.CENTER);
        txtMinExpirationCycle = JOE_T_EventForm_MinExpirationCycle.Control(new Text(composite_1, SWT.BORDER));
        txtMinExpirationCycle.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtMinExpirationCycle);
            }
        });
        txtMinExpirationCycle.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtMinExpirationCycle.setTextLimit(2);
        final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_5.widthHint = 30;
        txtMinExpirationCycle.setLayoutData(gridData_5);
        final Label label_1_1 = JOE_L_Colon.Control(new Label(composite_1, SWT.NONE));
        final GridData gridData_2_1_1 = new GridData(10, SWT.DEFAULT);
        label_1_1.setLayoutData(gridData_2_1_1);
        label_1_1.setAlignment(SWT.CENTER);
        txtSecExpirationCycle = JOE_T_EventForm_SecExpirationCycle.Control(new Text(composite_1, SWT.BORDER));
        txtSecExpirationCycle.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                Utils.setBackground(0, 59, txtSecExpirationCycle);
            }
        });
        txtSecExpirationCycle.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtSecExpirationCycle.setTextLimit(2);
        final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_6.widthHint = 30;
        txtSecExpirationCycle.setLayoutData(gridData_6);
        final Label hhmmssLabel_1 = JOE_L_JobAssistent_TimeFormat.Control(new Label(composite_1, SWT.NONE));
        final GridData gridData_3_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData_3_1.horizontalIndent = 5;
        hhmmssLabel_1.setLayoutData(gridData_3_1);
    }

    private String getExpirationPeriod() {
        if (type != JOEConstants.ADD_EVENT_GROUP) {
            return "";
        }
        return Utils.getTime(txtHourExpirationPeriod.getText(), txtMinExpirationPeriod.getText(), txtSecExpirationPeriod.getText(), false);
    }

    private String getExpirationCycle() {
        if (type != JOEConstants.ADD_EVENT_GROUP) {
            return "";
        }
        return Utils.getTime(txtHourExpirationCycle.getText(), txtMinExpirationCycle.getText(), txtSecExpirationCycle.getText(), false);
    }

}