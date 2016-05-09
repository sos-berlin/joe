package com.sos.event.service.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.event.service.listeners.JobCommandNamesListener;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class JobCommandNamesForm extends SOSJOEMessageCodes implements IUnsaved {

    private Table tCommands = null;
    private JobCommandNamesListener listener = null;
    private Group gMain = null;
    private boolean event = false;
    private Button bRemoveExitcode = null;
    private Button addJobButton = null;
    private Button addOrderButton = null;
    private Text txtName = null;
    private Text txtHost = null;
    private Text txtPort = null;
    private ActionsDom _dom = null;

    public JobCommandNamesForm(Composite parent, int style, ActionsDom dom, Element command, ActionsForm main) throws JDOMException,
            TransformerException {
        super(parent, style);
        listener = new JobCommandNamesListener(dom, command, main);
        _dom = dom;
        initialize();
        dom.setInit(true);
        listener.fillCommands(tCommands);
        dom.setInit(false);
        event = true;
    }

    public void apply() {
        //
    }

    public boolean isUnsaved() {
        return false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        txtName.setText(listener.getName());
        txtHost.setText(listener.getHost());
        txtPort.setText(listener.getPort());
        txtName.setFocus();
    }

    private void createGroup() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 4;
        gMain = new Group(this, SWT.NONE);
        gMain.setText(JOE_G_JobCommandNamesForm_Command.params(listener.getName()));
        gMain.setLayout(gridLayout2);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        final Label nameLabel = JOE_L_JobCommandNamesForm_Name.Control(new Label(gMain, SWT.NONE));
        nameLabel.setLayoutData(new GridData());
        txtName = JOE_T_JobCommandNamesForm_Name.Control(new Text(gMain, SWT.BORDER));
        txtName.setBackground(SWTResourceManager.getColor(255, 255, 217));
        txtName.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (event) {
                    listener.setName(txtName.getText());
                }
            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_1.widthHint = 288;
        txtName.setLayoutData(gridData_1);
        addJobButton = JOE_B_JobCommandNamesForm_AddJob.Control(new Button(gMain, SWT.NONE));
        addJobButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
        addJobButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                addJob();
            }
        });
        final Label schedulerHostLabel = JOE_L_JobCommandNamesForm_SchedulerHost.Control(new Label(gMain, SWT.NONE));
        schedulerHostLabel.setLayoutData(new GridData());
        txtHost = JOE_T_JobCommandNamesForm_SchedulerHost.Control(new Text(gMain, SWT.BORDER));
        txtHost.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (event) {
                    listener.setHost(txtHost.getText());
                }
            }
        });
        txtHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        addOrderButton = JOE_B_JobCommandNamesForm_AddOrder.Control(new Button(gMain, SWT.NONE));
        addOrderButton.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false, 2, 1));
        addOrderButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                addOrder();
            }
        });
        final Label schedulerPortLabel = JOE_L_JobCommandNamesForm_SchedulerPort.Control(new Label(gMain, SWT.NONE));
        schedulerPortLabel.setLayoutData(new GridData());
        txtPort = JOE_T_JobCommandNamesForm_SchedulerPort.Control(new Text(gMain, SWT.BORDER));
        txtPort.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        txtPort.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (event) {
                    listener.setPort(txtPort.getText());
                }
            }
        });
        txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(gMain, SWT.NONE);
        new Label(gMain, SWT.NONE);
        final Label label = new Label(gMain, SWT.HORIZONTAL | SWT.SEPARATOR);
        label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        new Label(gMain, SWT.NONE);
        tCommands = JOE_Tbl_JobCommandNamesForm_Commands.Control(new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER));
        tCommands.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                String str =
                        !tCommands.getSelection()[0].getText(2).isEmpty() ? tCommands.getSelection()[0].getText(2)
                                : tCommands.getSelection()[0].getText(1);
            }
        });
        tCommands.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null) {
                    return;
                }
                bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);
            }
        });
        tCommands.setLinesVisible(true);
        tCommands.setHeaderVisible(true);
        final GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, false, true, 3, 1);
        gridData9.widthHint = 545;
        tCommands.setLayoutData(gridData9);
        listener.fillCommands(tCommands);
        final TableColumn tcJob = JOE_TCl_JobCommandNamesForm_Command.Control(new TableColumn(tCommands, SWT.NONE));
        tcJob.setWidth(167);
        final TableColumn tcCommand = JOE_TCl_JobCommandNamesForm_JobID.Control(new TableColumn(tCommands, SWT.NONE));
        tcCommand.setWidth(154);
        final TableColumn tcJobchain = JOE_TCl_JobCommandNamesForm_JobChain.Control(new TableColumn(tCommands, SWT.NONE));
        tcJobchain.setWidth(136);
        final TableColumn tcStartAt = JOE_TCl_JobCommandNamesForm_StartAt.Control(new TableColumn(tCommands, SWT.NONE));
        tcStartAt.setWidth(139);
        bRemoveExitcode = JOE_B_JobCommandNamesForm_RemoveExitCode.Control(new Button(gMain, SWT.NONE));
        final GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemoveExitcode.setLayoutData(gridData);
        bRemoveExitcode.setEnabled(false);
        bRemoveExitcode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tCommands != null && tCommands.getSelectionCount() > 0) {
                    int cont = ErrorLog.message(getShell(), JOE_M_EventForm_RemoveCommand.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                    if (cont == SWT.OK) {
                        listener.deleteCommand(tCommands);
                        tCommands.deselectAll();
                        bRemoveExitcode.setEnabled(false);
                    }
                }
            }
        });
    }

    private void addJob() {
        Element e = null;
        e = new Element("start_job");
        e.setAttribute("job", "job" + tCommands.getItemCount());
        TableItem item = new TableItem(tCommands, SWT.NONE);
        item.setText(new String[] { "start_job", "job" + tCommands.getItemCount(), "", "" });
        listener.addCommand(e);
    }

    private void addOrder() {
        Element e = null;
        e = new Element("order");
        e.setAttribute("job_chain", "job_chain" + tCommands.getItemCount());
        e.setAttribute("replace", "yes");
        TableItem item = new TableItem(tCommands, SWT.NONE);
        item.setText(new String[] { "order", "", "job_chain_" + tCommands.getItemCount(), "" });
        listener.addCommand(e);
    }

    public Element getCommand() {
        return listener.getCommand();
    }

}
