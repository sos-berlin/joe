package com.sos.event.service.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import com.sos.event.service.listeners.JobCommandsListener;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.Events.ActionsDom;

public class JobCommandsForm extends SOSJOEMessageCodes {

    private JobCommandsListener listener = null;
    private Group commandsGroup = null;
    private Table table = null;
    private Button bNewCommands = null;
    private Button bRemoveCommand = null;
    private Label label = null;
    private ActionsDom _dom = null;

    public JobCommandsForm(Composite parent, int style, ActionsDom dom, Element action, ActionsForm gui) {
        super(parent, style);
        listener = new JobCommandsListener(dom, action, gui);
        _dom = dom;
        initialize();
        listener.fillTable(table);
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(656, 400));
    }

    private void createGroup() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        commandsGroup = JOE_G_ActionsJobCommandsForm_Commands.Control(new Group(this, SWT.NONE));
        commandsGroup.setLayout(gridLayout);
        createTable();
        bNewCommands = JOE_B_ActionsJobCommandsForm_NewCommand.Control(new Button(commandsGroup, SWT.NONE));
        bNewCommands.setLayoutData(gridData);
        getShell().setDefaultButton(bNewCommands);
        label = new Label(commandsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(gridData4);
        bNewCommands.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newCommands(table);
                bRemoveCommand.setEnabled(true);
            }
        });
        bRemoveCommand = JOE_B_ActionsJobCommandsForm_RemoveCommand.Control(new Button(commandsGroup, SWT.NONE));
        bRemoveCommand.setEnabled(false);
        bRemoveCommand.setLayoutData(gridData1);
        bRemoveCommand.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table != null && table.getSelectionCount() > 0) {
                    int cont = ErrorLog.message(getShell(), JOE_M_EventForm_RemoveCommand.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    if (cont == SWT.OK) {
                        bRemoveCommand.setEnabled(listener.deleteCommands(table));
                    }
                }
            }
        });
    }

    private void createTable() {
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData2.widthHint = 204;
        table = JOE_Tbl_ActionsJobCommandsForm_Commands.Control(new Table(commandsGroup, SWT.BORDER | SWT.FULL_SELECTION));
        table.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                //
            }
        });
        table.setHeaderVisible(true);
        table.setLayoutData(gridData2);
        table.setLinesVisible(true);
        TableColumn commandnameTableColumn = JOE_TCl_ActionsJobCommandsForm_Command.Control(new TableColumn(table, SWT.NONE));
        commandnameTableColumn.setWidth(151);
        final TableColumn newColumnTableColumn_1 = JOE_TCl_ActionsJobCommandsForm_Host.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn_1.setWidth(100);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemoveCommand.setEnabled(true);
            }
        });
        final TableColumn newColumnTableColumn = JOE_TCl_ActionsJobCommandsForm_Port.Control(new TableColumn(table, SWT.NONE));
        newColumnTableColumn.setWidth(100);
    }

}