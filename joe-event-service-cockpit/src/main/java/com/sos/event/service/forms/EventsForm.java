package com.sos.event.service.forms;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.event.service.listeners.EventsListener;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.Events.ActionsDom;

public class EventsForm extends SOSJOEMessageCodes implements IUnsaved {

    private EventsListener listener = null;
    private Group actionsGroup = null;
    private Text txtLogic = null;
    private Text txtGroup = null;
    private Text txtGroupLogic = null;
    private Combo cboEventClass = null;
    private Table table = null;
    private Button butApply = null;
    private Button butNew = null;
    private Button butRemove = null;
    private ActionsDom _dom = null;
    private Button butEventGroupOperation = null;
    private Button butEventsOperation = null;

    public EventsForm(Composite parent, int style, ActionsDom dom, Element action, ActionsForm _gui) {
        super(parent, style);
        listener = new EventsListener(dom, action, _gui);
        _dom = dom;
        initialize();
    }

    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
        txtLogic.setText(listener.getLogic());
        listener.fillEvents(table);
        cboEventClass.setItems(listener.getEventClasses());
        actionsGroup.setText(JOE_G_EventsForm_Action.params(listener.getActionname())); // Generated
        butApply.setEnabled(false);
        txtLogic.setFocus();
    }

    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        actionsGroup = new Group(this, SWT.NONE);
        actionsGroup.setLayout(gridLayout);
        final Label lblLogic = JOE_L_EventsForm_Logic.Control(new Label(actionsGroup, SWT.NONE));
        txtLogic = JOE_T_EventsForm_Logic.Control(new Text(actionsGroup, SWT.BORDER));
        txtLogic.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                listener.setLogic(txtLogic.getText());
            }
        });
        txtLogic.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butEventsOperation = JOE_B_EventsForm_Operation.Control(new Button(actionsGroup, SWT.NONE));
        butEventsOperation.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                ArrayList list = new ArrayList();
                list.addAll(listener.getGroups());
                LogicOperationDialog logicOperationDialog = new LogicOperationDialog(SWT.NONE);
                logicOperationDialog.open(txtLogic, list);
            }
        });
        final Label label = new Label(actionsGroup, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        label.setLayoutData(gridData_1);
        label.setText("label");
        new Label(actionsGroup, SWT.NONE);
        final Group group = JOE_G_EventsForm_EventsGroup.Control(new Group(actionsGroup, SWT.NONE));
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
        group.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 4;
        group.setLayout(gridLayout_1);
        @SuppressWarnings("unused")
        final Label groupLabel = JOE_L_EventsForm_Group.Control(new Label(group, SWT.NONE));
        txtGroup = JOE_T_EventsForm_Group.Control(new Text(group, SWT.BORDER));
        txtGroup.setBackground(SWTResourceManager.getColor(255, 255, 217));
        txtGroup.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(txtGroup)) {
                    apply();
                }
            }
        });
        txtGroup.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
                butEventGroupOperation.setEnabled(!txtGroup.getText().isEmpty());
            }
        });
        txtGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        butApply = JOE_B_EventsForm_Apply.Control(new Button(group, SWT.NONE));
        butApply.setEnabled(false);
        butApply.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                apply();
            }
        });
        butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Label logicLabel = JOE_L_EventsForm_Logic.Control(new Label(group, SWT.NONE));
        txtGroupLogic = JOE_T_EventsForm_LogicGroup.Control(new Text(group, SWT.BORDER));
        txtGroupLogic.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        txtGroupLogic.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(txtGroup)) {
                    apply();
                }
            }
        });
        txtGroupLogic.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        butEventGroupOperation = JOE_B_EventsForm_Operation.Control(new Button(group, SWT.NONE));
        butEventGroupOperation.setLayoutData(new GridData());
        butEventGroupOperation.setEnabled(false);
        butEventGroupOperation.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                ArrayList list = new ArrayList();
                list.addAll(listener.getEventClassAndId(txtGroup.getText()));
                LogicOperationDialog logicOperationDialog = new LogicOperationDialog(SWT.NONE);
                logicOperationDialog.open(txtGroupLogic, list);
            }
        });
        butNew = JOE_B_EventsForm_New.Control(new Button(group, SWT.NONE));
        butNew.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                txtGroup.setText("");
                txtGroupLogic.setText("");
                cboEventClass.setText("");
                table.deselectAll();
                butApply.setEnabled(false);
                butRemove.setEnabled(false);
                txtGroup.setFocus();
            }
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final Label eventClassLabel = JOE_L_EventsForm_EventClass.Control(new Label(group, SWT.NONE));
        cboEventClass = JOE_Cbo_EventsForm_EventClass.Control(new Combo(group, SWT.BORDER));
        cboEventClass.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        cboEventClass.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(txtGroup)) {
                    apply();
                }
            }
        });
        cboEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
        butRemove = JOE_B_EventsForm_Remove.Control(new Button(group, SWT.NONE));
        butRemove.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int cont = ErrorLog.message(actionsGroup.getShell(), JOE_M_EventsForm_RemoveGroup.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    if (cont == SWT.OK) {
                        listener.removeEvent(table);
                    }
                    txtGroup.setFocus();
                    cboEventClass.setText("");
                    txtGroup.setText("");
                    txtGroupLogic.setText("");
                    cboEventClass.setItems(listener.getEventClasses());
                    txtGroup.setFocus();
                }
            }
        });
        butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        table = JOE_Tbl_EventsForm_Groups.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
        table.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                //
            }
        });
        table.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    TableItem item = table.getSelection()[0];
                    txtGroup.setText(item.getText(0));
                    txtGroupLogic.setText(item.getText(1));
                    cboEventClass.setText(item.getText(2));
                }
                butApply.setEnabled(false);
                butRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
        final TableColumn newColumnTableColumn = JOE_TCl_EventsForm_Group.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn.setWidth(140);
        final TableColumn newColumnTableColumn_1 = JOE_TCl_EventsForm_Logic.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_1.setWidth(136);
        final TableColumn newColumnTableColumn_2 = JOE_TCl_EventsForm_EventClass.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_2.setWidth(189);
    }

    public boolean isUnsaved() {
        return butApply.isEnabled();
    }

    public void apply() {
        if (butApply.isEnabled()) {
            if (!txtGroup.getText().isEmpty()) {
                listener.apply(txtGroup.getText(), cboEventClass.getText(), txtGroupLogic.getText(), table);
            }
            cboEventClass.setText("");
            txtGroup.setText("");
            txtGroupLogic.setText("");
            cboEventClass.setItems(listener.getEventClasses());
            txtGroup.setFocus();
        }
        butApply.setEnabled(false);
    }

}