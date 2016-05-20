package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobCommandExitCodesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandExitCodesForm extends SOSJOEMessageCodes implements IUnsaved {

    private Table tCommands = null;
    private JobCommandExitCodesListener listener = null;
    private Group jobsAndOrdersGroup = null;
    private Group gMain = null;
    private SashForm sashForm = null;
    private boolean updateTree = false;
    private boolean event = false;
    private Combo cExitcode = null;
    private Button bRemoveExitcode = null;
    private Button addJobButton = null;
    private Button addOrderButton = null;
    private SchedulerDom _dom = null;

    public JobCommandExitCodesForm(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main) throws JDOMException,
            TransformerException {
        super(parent, style);
        _dom = dom;
        listener = new JobCommandExitCodesListener(dom, command, main);
        initialize();
        dom.setInit(true);
        listener.fillCommands(tCommands);
        updateTree = false;
        cExitcode.setText(listener.getExitCode());
        updateTree = true;
        dom.setInit(false);
        event = true;
        if (command.getParentElement() != null) {
            this.jobsAndOrdersGroup.setEnabled(Utils.isElementEnabled("job", dom, command.getParentElement()));
        }
    }

    public void apply() {
        if (isUnsaved()) {
            addParam();
        }
    }

    public boolean isUnsaved() {
        return false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        cExitcode.setFocus();
    }

    private void createGroup() {
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.makeColumnsEqualWidth = true;
        gridLayout2.numColumns = 1;
        jobsAndOrdersGroup = new Group(this, SWT.NONE);
        String strT = JOE_M_JobAssistent_JobGroup.params(listener.getName()) + " " + listener.getExitCode();
        if (listener.isDisabled()) {
            strT += " " + JOE_M_JobCommand_Disabled.label();
        }
        jobsAndOrdersGroup.setText(strT);
        jobsAndOrdersGroup.setLayout(gridLayout2);
        GridData gridData18 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
        sashForm = new SashForm(jobsAndOrdersGroup, SWT.NONE);
        sashForm.setOrientation(512);
        sashForm.setLayoutData(gridData18);
        createSashForm();
    }

    private void createGroup1() {
        createCombo();
        createComposite();
    }

    private void createCombo() {
        //
    }

    private void createComposite() {
        //
    }

    private void createSashForm() {
        createGroup1();
        createGroup2();
        createGroup3();
    }

    private void createGroup2() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gMain = JOE_G_JobCommands_Commands.control(new Group(sashForm, SWT.NONE));
        gMain.setLayout(gridLayout);
        cExitcode = JOE_Cbo_JobCommands_Exitcode.control(new Combo(gMain, SWT.NONE));
        cExitcode.setItems(new String[] { "error", "success", "SIGHUP", "SIGINT", "SIGQUIT", "SIGILL", "SIGTRAP", "SIGABRT", "SIGIOT", "SIGBUS",
                "SIGFPE", "SIGKILL", "SIGUSR1", "SIGSEGV", "SIGUSR2", "SIGPIPE", "SIGALRM", "SIGTERM", "SIGSTKFLT", "SIGCHLD", "SIGCONT", "SIGSTOP",
                "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGURG", "SIGXCPU", "SIGXFSZ", "SIGVTALRM", "SIGPROF", "SIGWINCH", "SIGPOLL", "SIGIO", "SIGPWR",
                "SIGSYS" });
        cExitcode.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                listener.setExitCode(cExitcode.getText(), updateTree);
                if (event) {
                    listener.setExitCode(cExitcode.getText(), true);
                }
            }
        });
        final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cExitcode.setLayoutData(gridData_9);
        final Composite composite = new Composite(gMain, SWT.NONE);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridData_1.widthHint = 63;
        composite.setLayoutData(gridData_1);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.horizontalSpacing = 0;
        gridLayout_1.verticalSpacing = 0;
        gridLayout_1.marginHeight = 0;
        gridLayout_1.marginWidth = 0;
        composite.setLayout(gridLayout_1);
        addJobButton = JOE_B_JobCommands_AddJob.control(new Button(composite, SWT.NONE));
        addJobButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                addJob();
            }
        });
        addJobButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        addOrderButton = JOE_B_JobCommands_AddOrder.control(new Button(composite, SWT.NONE));
        addOrderButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        addOrderButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                addOrder();
            }
        });
        final Label exitLabel = JOE_L_JobCommands_ExitCodes.control(new Label(gMain, SWT.NONE));
        exitLabel.setLayoutData(new GridData(73, SWT.DEFAULT));
        new Label(gMain, SWT.NONE);
        tCommands = JOE_Tbl_JobCommands_Commands.control(new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER));
        tCommands.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                if (tCommands.getSelectionCount() > 0) {
                    String name =
                            tCommands.getSelection()[0].getText(0) + ": " + tCommands.getSelection()[0].getText(1)
                                    + tCommands.getSelection()[0].getText(2);
                    ContextMenu.goTo(name, _dom, JOEConstants.JOB_COMMAND_EXIT_CODES);
                }
            }
        });
        tCommands.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item == null) {
                    return;
                }
                bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);
                cExitcode.setFocus();
            }
        });
        tCommands.setLinesVisible(true);
        tCommands.setHeaderVisible(true);
        final GridData gridData9 = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData9.heightHint = 149;
        tCommands.setLayoutData(gridData9);
        listener.fillCommands(tCommands);
        final TableColumn tcJob = JOE_TCl_JobCommands_Command.control(new TableColumn(tCommands, SWT.NONE));
        tcJob.setWidth(167);
        final TableColumn tcCommand = JOE_TCl_JobCommands_JobID.control(new TableColumn(tCommands, SWT.NONE));
        tcCommand.setWidth(154);
        final TableColumn tcJobchain = JOE_TCl_JobCommands_JobChain.control(new TableColumn(tCommands, SWT.NONE));
        tcJobchain.setWidth(136);
        final TableColumn tcStartAt = JOE_TCl_JobCommands_StartAt.control(new TableColumn(tCommands, SWT.NONE));
        tcStartAt.setWidth(139);
        bRemoveExitcode = JOE_B_JobCommands_Remove.control(new Button(gMain, SWT.NONE));
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        gridData.widthHint = 67;
        bRemoveExitcode.setLayoutData(gridData);
        bRemoveExitcode.setEnabled(false);
        bRemoveExitcode.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.deleteCommand(tCommands);
                tCommands.deselectAll();
                bRemoveExitcode.setEnabled(false);
            }
        });
    }

    private void createTable() {
        //
    }

    private void createGroup3() {
        createTable();
    }

    private void addParam() {
        //
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
        String newJobChainName = "job_chain" + tCommands.getItemCount();
        e = new Element("order");
        e.setAttribute("job_chain", newJobChainName);
        e.setAttribute("replace", "yes");
        TableItem item = new TableItem(tCommands, SWT.NONE);
        item.setText(new String[] { "order", "", newJobChainName, "" });
        listener.addCommand(e);
    }

    public Element getCommand() {
        return listener.getCommand();
    }

}