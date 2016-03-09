package sos.scheduler.editor.app;

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeAddOrderElements;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeNextStateElements;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeAddOrderElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeNextStateElement;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;

import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class JobchainNodeReturnCodeDialog extends SOSJOEMessageCodes {

    protected Object result;
    protected Shell shell;
    private Shell parent;

    private JobchainListOfReturnCodeNextStateElements jobchainListOfReturnCodeNextStateElements;
    private JobchainListOfReturnCodeAddOrderElements jobchainListOfReturnCodeAddOrderElements;
    private JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements;

    private Table tableJobchainNodeReturnCodesNextState;
    private Button btNewJobchainNodeReturnNextState;
    private Button btOkJobchainNodeReturnCodeNextState;
    private Button btRemoveJobchainNodeReturnCodeNextState;

    private Button btOkJobchainNodeReturnCodeAddOrder;
    private Button btNewJobchainNodeReturnAddOrder;
    private Button btRemoveJobchainNodeReturnCodeAddOrder;

    private Text tReturnCodesNextState;
    private Combo cNextState = null;
    private Table tableJobchainNodeReturnCodesAddOrder;
    private Combo cJobChain;
    private Text tOrderId;
    private Table tableParams;

    private Group grpGroup;
    private Text tParamName;
    private Text tParamValue;

    private Button btAddParam;
    private Button btRemoveParam;
    private Button btNewParam;
    private Text tReturnCodesAddOrder;
    private JobChainListener listener = null;

    private boolean ok;

    /** Create the dialog.
     * 
     * @param parent
     * @param style */
    public JobchainNodeReturnCodeDialog(Shell parent_, int style, JobChainListener listener_) {
        super(parent_, style);
        listener = listener_;
        parent = parent_;
    }

    public void execute() {
        Display display = Display.getDefault();
        Shell shell = open(display);

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void saveReturnCodeList() {
        jobchainListOfReturnCodeElements = new JobchainListOfReturnCodeElements();
        jobchainListOfReturnCodeNextStateElements.reset();
        while (jobchainListOfReturnCodeNextStateElements.hasNext()) {
            JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement = jobchainListOfReturnCodeNextStateElements.getNext();
            jobchainListOfReturnCodeElements.add(jobchainReturnCodeNextStateElement);
        }

        if (jobchainListOfReturnCodeAddOrderElements != null) {
            while (jobchainListOfReturnCodeAddOrderElements.hasNext()) {
                JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = jobchainListOfReturnCodeAddOrderElements.getNext();
                jobchainListOfReturnCodeElements.add(jobchainReturnCodeAddOrderElement);
            }
        }
    }

    private void saveNextStateList() {
        addReturnCodeNextState();
        jobchainListOfReturnCodeNextStateElements = new JobchainListOfReturnCodeNextStateElements();
        for (int i = 0; i < tableJobchainNodeReturnCodesNextState.getItemCount(); i++) {
            TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[i];
            JobchainReturnCodeNextStateElement jobchainReturnCodeElement = new JobchainReturnCodeNextStateElement();
            jobchainReturnCodeElement.setReturnCodes(item.getText(0));
            jobchainReturnCodeElement.setNextState(item.getText(1));
            jobchainListOfReturnCodeNextStateElements.add(jobchainReturnCodeElement);
        }
    }

    private void saveAddOrderList() {
        addReturnCodeAddOrder();
        jobchainListOfReturnCodeAddOrderElements = new JobchainListOfReturnCodeAddOrderElements();
        for (int i = 0; i < tableJobchainNodeReturnCodesAddOrder.getItemCount(); i++) {
            TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[i];
            JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = (JobchainReturnCodeAddOrderElement) item.getData();
            jobchainListOfReturnCodeAddOrderElements.add(jobchainReturnCodeAddOrderElement);
        }
    }

    private Shell open(final Display display) {

        final Shell dialogShell = new Shell(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        dialogShell.setMinimumSize(new Point(900, 900));
        dialogShell.setLocation(parent.getLocation().x + 40, parent.getLocation().y + 40);

        dialogShell.setSize(900, 900);
        dialogShell.setLayout(new GridLayout(4, false));
        dialogShell.pack();

        createGroup(dialogShell);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Button btnOk = JOE_B_Apply.Control(new Button(grpGroup, SWT.NONE));
        GridData gd_btnOk = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnOk.widthHint = 58;
        btnOk.setLayoutData(gd_btnOk);
        btnOk.setSize(68, 25);

        btnOk.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                saveNextStateList();
                saveAddOrderList();
                saveReturnCodeList();
                ok = true;
                dialogShell.dispose();
            }
        });

        Button btnCancelButton = JOE_B_Cancel.Control(new Button(grpGroup, SWT.NONE));

        btnCancelButton.setSize(48, 25);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btnCancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (jobchainListOfReturnCodeElements != null) {
                    jobchainListOfReturnCodeElements.clear();
                }
                ok = false;
                dialogShell.dispose();
            }
        });

        initTables();

        dialogShell.open();
        return dialogShell;
    }

    public boolean isOk() {
        return ok;
    }

    private void initTables() {
        jobchainListOfReturnCodeElements.reset();
        while (jobchainListOfReturnCodeElements.hasNext()) {
            JobchainReturnCodeElement jobchainReturnCodeElement = jobchainListOfReturnCodeElements.getNext();
            JobchainListOfReturnCodeAddOrderElements jobchainListOfReturnCodeAddOrderElements = jobchainReturnCodeElement.getJobchainListOfReturnCodeAddOrderElements();
            JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement = jobchainReturnCodeElement.getJobchainReturnCodeNextStateElement();
            if (jobchainReturnCodeNextStateElement != null) {
                TableItem item = new TableItem(tableJobchainNodeReturnCodesNextState, SWT.NONE);
                item.setText(0, jobchainReturnCodeNextStateElement.getReturnCodes());
                item.setText(1, jobchainReturnCodeNextStateElement.getNextState());
            }
            if (jobchainListOfReturnCodeAddOrderElements != null) {
                jobchainListOfReturnCodeAddOrderElements.reset();
                while (jobchainListOfReturnCodeAddOrderElements.hasNext()) {
                    JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = jobchainListOfReturnCodeAddOrderElements.getNext();
                    TableItem item = new TableItem(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
                    item.setText(0, jobchainReturnCodeAddOrderElement.getReturnCodes());
                    item.setText(1, jobchainReturnCodeAddOrderElement.getJobChain());
                    item.setText(2, jobchainReturnCodeAddOrderElement.getOrderId());
                    item.setData(jobchainReturnCodeAddOrderElement);
                    tableParams.removeAll();

                }
            }
        }
    }

    private void addReturnCodeAddOrder() {

        if (tReturnCodesAddOrder.getText().length() > 0 && cJobChain.getText().length() > 0) {
            JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = new JobchainReturnCodeAddOrderElement();
            jobchainReturnCodeAddOrderElement.setJobChain(cJobChain.getText());
            jobchainReturnCodeAddOrderElement.setOrderId(tOrderId.getText());
            jobchainReturnCodeAddOrderElement.setReturnCodes(tReturnCodesAddOrder.getText());

            for (int i = 0; i < tableParams.getItemCount(); i++) {
                TableItem item = tableParams.getItems()[i];
                jobchainReturnCodeAddOrderElement.addParam(item.getText(0), item.getText(1));
            }

            if (tableJobchainNodeReturnCodesAddOrder.getSelectionIndex() >= 0) {
                TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[tableJobchainNodeReturnCodesAddOrder.getSelectionIndex()];
                item.setText(0, tReturnCodesAddOrder.getText());
                item.setText(1, cJobChain.getText());
                item.setText(2, tOrderId.getText());
                item.setData(jobchainReturnCodeAddOrderElement);
                clearInputFieldsAddOrder();
                tReturnCodesAddOrder.setFocus();

            } else {
                TableItem item = new TableItem(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
                item.setText(0, tReturnCodesAddOrder.getText());
                item.setText(1, cJobChain.getText());
                item.setText(2, tOrderId.getText());
                item.setData(jobchainReturnCodeAddOrderElement);
                clearInputFieldsAddOrder();
                tReturnCodesAddOrder.setFocus();
            }
        }
        tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
        btAddParam.setEnabled(false);

    }

    private void addReturnCodeNextState() {
        if (tReturnCodesNextState.getText().length() > 0 && cNextState.getText().length() > 0) {
            if (tableJobchainNodeReturnCodesNextState.getSelectionIndex() >= 0) {
                TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[tableJobchainNodeReturnCodesNextState.getSelectionIndex()];
                item.setText(0, tReturnCodesNextState.getText());
                item.setText(1, cNextState.getText());
                tReturnCodesNextState.setText("");
                cNextState.setText("");
                tReturnCodesNextState.setFocus();

            } else {
                for (int i = 0; i < tableJobchainNodeReturnCodesNextState.getItemCount(); i++) {
                    TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[i];
                    if ((item.getText(0).equals(tReturnCodesNextState.getText()) && tReturnCodesNextState.getText().length() > 0)) {
                        item.setText(0, tReturnCodesNextState.getText());
                        item.setText(1, cNextState.getText());
                        tReturnCodesNextState.setText("");
                        cNextState.setText("");
                        tReturnCodesNextState.setFocus();

                    }
                }
                if (tReturnCodesNextState.getText().length() > 0) {

                    TableItem item = new TableItem(tableJobchainNodeReturnCodesNextState, SWT.NONE);
                    item.setText(0, tReturnCodesNextState.getText());
                    item.setText(1, cNextState.getText());
                    tReturnCodesNextState.setText("");
                    cNextState.setText("");
                    tReturnCodesNextState.setFocus();

                }
            }
            tableJobchainNodeReturnCodesNextState.setSelection(-1);

        }
    }

    /** This method initializes group */
    private void createGroup(Composite parent) {

        grpGroup = new Group(parent, SWT.NONE);
        grpGroup.setText(JOE_G_ReturnCodesForm_ReturnCodes.params(listener.getState()));

        GridData gd_grpGroup = new GridData(SWT.FILL, SWT.CENTER, false, true, 4, 1);
        gd_grpGroup.widthHint = 880;
        gd_grpGroup.heightHint = 900;
        grpGroup.setLayoutData(gd_grpGroup);
        GridLayout gl_grpGroup = new GridLayout(5, false);
        gl_grpGroup.horizontalSpacing = 6;
        gl_grpGroup.verticalSpacing = 2;
        grpGroup.setLayout(gl_grpGroup);

        createTableNextStates();

        GridData gd_btOkJobchainNodeReturnCodeNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_btOkJobchainNodeReturnCodeNextState.widthHint = 140;

        btOkJobchainNodeReturnCodeNextState = JOE_B_ReturnCodesForm_Add_Next_State.Control(new Button(grpGroup, SWT.NONE));
        ;
        btOkJobchainNodeReturnCodeNextState.setLayoutData(gd_btOkJobchainNodeReturnCodeNextState);
        btOkJobchainNodeReturnCodeNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addReturnCodeNextState();
            }
        });

        GridData gd_btNewJobchainNodeReturnNextState = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
        gd_btNewJobchainNodeReturnNextState.widthHint = 100;

        btNewJobchainNodeReturnNextState = JOE_B_New.Control(new Button(grpGroup, SWT.NONE));
        btNewJobchainNodeReturnNextState.setEnabled(false);
        btNewJobchainNodeReturnNextState.setLayoutData(gd_btNewJobchainNodeReturnNextState);
        btNewJobchainNodeReturnNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableJobchainNodeReturnCodesNextState.setSelection(-1);
                tReturnCodesNextState.setText("");
                cNextState.setText("");
                tReturnCodesNextState.setFocus();
                btNewJobchainNodeReturnNextState.setEnabled(false);
            }
        });

        GridData gd_btRemoveJobchainNodeReturnCodeNextState = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
        gd_btRemoveJobchainNodeReturnCodeNextState.widthHint = 100;

        btRemoveJobchainNodeReturnCodeNextState = JOE_B_Remove.Control(new Button(grpGroup, SWT.NONE));
        btRemoveJobchainNodeReturnCodeNextState.setLayoutData(gd_btRemoveJobchainNodeReturnCodeNextState);
        btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);

        btRemoveJobchainNodeReturnCodeNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesNextState.getSelectionCount() > 0) {
                    int index = tableJobchainNodeReturnCodesNextState.getSelectionIndex();
                    tableJobchainNodeReturnCodesNextState.remove(index);
                    tableJobchainNodeReturnCodesNextState.setSelection(-1);
                    tReturnCodesNextState.setText("");
                    cNextState.setText("");
                    tReturnCodesNextState.setFocus();
                    btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);
                }
                btRemoveJobchainNodeReturnCodeNextState.setEnabled(tableJobchainNodeReturnCodesNextState.getSelectionCount() > 0);
            }
        });

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lbSchedulerReturnCodesNextState = new Label(grpGroup, SWT.NONE);
        lbSchedulerReturnCodesNextState.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerReturnCodesNextState.setText("Return Codes");

        tReturnCodesNextState = new Text(grpGroup, SWT.BORDER);
        GridData gd_tReturnCodesNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_tReturnCodesNextState.widthHint = 250;
        tReturnCodesNextState.setLayoutData(gd_tReturnCodesNextState);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lbState = new Label(grpGroup, SWT.NONE);
        lbState.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        lbState.setText("State");

        cNextState = new Combo(grpGroup, SWT.BORDER);
        GridData gd_tNextState = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tNextState.widthHint = 250;
        cNextState.setLayoutData(gd_tNextState);
        if (listener.getAllStates().length > 0) {
            cNextState.setItems(listener.getAllStates());
        }
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblAddOrders = JOE_L_ReturnCodesForm_Add_Order.Control(new Label(grpGroup, SWT.NONE));

        lblAddOrders.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        createTableAddOrder();

        btOkJobchainNodeReturnCodeAddOrder = JOE_B_ReturnCodesForm_Add_Order.Control(new Button(grpGroup, SWT.NONE));
        btOkJobchainNodeReturnCodeAddOrder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addReturnCodeAddOrder();
            }
        });
        btOkJobchainNodeReturnCodeAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        btNewJobchainNodeReturnAddOrder = JOE_B_New.Control(new Button(grpGroup, SWT.NONE));
        btNewJobchainNodeReturnAddOrder.setEnabled(false);
        GridData gd_btNewJobchainNodeReturnAddOrder = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_btNewJobchainNodeReturnAddOrder.widthHint = 100;
        btNewJobchainNodeReturnAddOrder.setLayoutData(gd_btNewJobchainNodeReturnAddOrder);
        btNewJobchainNodeReturnAddOrder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
                btAddParam.setEnabled(false);
                clearInputFieldsAddOrder();

                tReturnCodesAddOrder.setFocus();
                btNewJobchainNodeReturnNextState.setEnabled(false);
            }
        });

        btRemoveJobchainNodeReturnCodeAddOrder = JOE_B_Remove.Control(new Button(grpGroup, SWT.NONE));
        btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(false);
        btRemoveJobchainNodeReturnCodeAddOrder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tableJobchainNodeReturnCodesAddOrder.getSelectionCount() > 0) {
                    int index = tableJobchainNodeReturnCodesAddOrder.getSelectionIndex();
                    if (index >= 0) {
                        tableJobchainNodeReturnCodesAddOrder.remove(index);
                        tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
                        btAddParam.setEnabled(false);
                        clearInputFieldsAddOrder();

                        tReturnCodesNextState.setFocus();
                        btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);
                    }
                }
                btRemoveJobchainNodeReturnCodeNextState.setEnabled(tableJobchainNodeReturnCodesNextState.getSelectionCount() > 0);
            }
        });
        GridData gd_btRemoveJobchainNodeReturnCodeAddOrder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btRemoveJobchainNodeReturnCodeAddOrder.widthHint = 109;
        btRemoveJobchainNodeReturnCodeAddOrder.setLayoutData(gd_btRemoveJobchainNodeReturnCodeAddOrder);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblNewLabel = new Label(grpGroup, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("ReturnCodes");

        tReturnCodesAddOrder = new Text(grpGroup, SWT.BORDER);
        tReturnCodesAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(grpGroup, SWT.NONE);

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblJobChain = JOE_L_OrderForm_JobChain.Control(new Label(grpGroup, SWT.NONE));
        lblJobChain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        cJobChain = new Combo(grpGroup, SWT.BORDER);
        cJobChain.setItems(listener.getJobChains());

        GridData gd_tJobChain = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tJobChain.widthHint = 250;
        cJobChain.setLayoutData(gd_tJobChain);

        Button btnBrowse = JOE_B_JobChainNodes_Browse.Control(new Button(grpGroup, SWT.NONE));
        GridData gd_btnBrowse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btnBrowse.widthHint = 80;
        btnBrowse.setLayoutData(gd_btnBrowse);
        btnBrowse.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                String jobChain = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB_CHAIN);
                if (jobChain != null && jobChain.length() > 0) {
                    cJobChain.setText(jobChain);
                }
            }
        });

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblOrderId = new Label(grpGroup, SWT.NONE);
        lblOrderId.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblOrderId.setText("Order ID");

        tOrderId = new Text(grpGroup, SWT.BORDER);
        GridData gd_tOrderId = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
        gd_tOrderId.widthHint = 250;
        tOrderId.setLayoutData(gd_tOrderId);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblName = new Label(grpGroup, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        tParamName = new Text(grpGroup, SWT.BORDER);
        GridData gd_tParamName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tParamName.widthHint = 254;
        tParamName.setLayoutData(gd_tParamName);

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblValue = JOE_L_ParameterForm_Value.Control(new Label(grpGroup, SWT.NONE));
        lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        tParamValue = new Text(grpGroup, SWT.BORDER);
        tParamValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        btAddParam = JOE_B_ReturnCodesForm_Add_Param.Control(new Button(grpGroup, SWT.NONE));
        btAddParam.setEnabled(false);
        btAddParam.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tParamName.getText().length() > 0) {
                    if (tableParams.getSelectionIndex() >= 0) {
                        TableItem item = tableParams.getItems()[tableParams.getSelectionIndex()];
                        item.setText(0, tParamName.getText());
                        item.setText(1, tParamValue.getText());
                        tParamName.setText("");
                        tParamValue.setText("");
                        tParamName.setFocus();

                    } else {
                        for (int i = 0; i < tableParams.getItemCount(); i++) {
                            TableItem item = tableParams.getItems()[i];
                            if ((item.getText(0).equals(tParamName.getText()) && tParamName.getText().length() > 0)) {
                                item.setText(0, tParamName.getText());
                                item.setText(1, tParamValue.getText());
                                tParamName.setText("");
                                tParamValue.setText("");
                                tParamName.setFocus();

                            }
                        }
                        if (tParamName.getText().length() > 0) {

                            TableItem item = new TableItem(tableParams, SWT.NONE);
                            item.setText(0, tParamName.getText());
                            item.setText(1, tParamValue.getText());
                            tParamName.setText("");
                            tParamValue.setText("");
                            tParamName.setFocus();

                        }
                    }
                    tableJobchainNodeReturnCodesNextState.setSelection(-1);

                }
            }
        });
        btAddParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        tableParams = new Table(grpGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_tableParams = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 7);
        gd_tableParams.widthHint = 238;
        tableParams.setLayoutData(gd_tableParams);
        tableParams.setHeaderVisible(true);
        tableParams.setLinesVisible(true);

        tableParams.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableParams.getSelectionIndex() >= 0) {
                    TableItem item = tableParams.getItems()[tableParams.getSelectionIndex()];
                    tParamName.setText(item.getText(0));
                    tParamValue.setText(item.getText(1));
                    btRemoveParam.setEnabled(true);
                    btNewParam.setEnabled(true);
                }

            }
        });

        TableColumn tblclmnName = new TableColumn(tableParams, SWT.NONE);
        tblclmnName.setWidth(260);
        tblclmnName.setText("Name");

        TableColumn tblclmnValue = new TableColumn(tableParams, SWT.NONE);
        tblclmnValue.setWidth(330);
        tblclmnValue.setText(JOE_L_ParameterForm_Value.label());
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btNewParam = JOE_B_ReturnCodesForm_New_Param.Control(new Button(grpGroup, SWT.NONE));
        ;
        btNewParam.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tParamName.setText("");
                tParamValue.setText("");
                tParamName.setFocus();
            }
        });
        btNewParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btRemoveParam = JOE_B_ReturnCodesForm_Remove_Param.Control(new Button(grpGroup, SWT.NONE));
        ;
        btRemoveParam.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tableParams.getSelectionCount() > 0) {
                    int index = tableParams.getSelectionIndex();
                    tableParams.remove(index);
                    tableParams.setSelection(-1);
                    tParamName.setText("");
                    tParamValue.setText("");
                    tParamName.setFocus();
                    btRemoveParam.setEnabled(false);
                }
                btRemoveParam.setEnabled(tableParams.getSelectionCount() > 0);
            }
        });
        btRemoveParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

    }

    private void clearInputFieldsAddOrder() {
        tReturnCodesAddOrder.setText("");
        cJobChain.setText("");
        tOrderId.setText("");
        tableParams.clearAll();

    }

    private void createTableAddOrder() {

        tableJobchainNodeReturnCodesAddOrder = new Table(grpGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_tableJobchainNodeReturnCodesAddOrder = new GridData(SWT.FILL, SWT.FILL, false, true, 5, 4);
        gd_tableJobchainNodeReturnCodesAddOrder.heightHint = 82;
        gd_tableJobchainNodeReturnCodesAddOrder.widthHint = 720;
        tableJobchainNodeReturnCodesAddOrder.setLayoutData(gd_tableJobchainNodeReturnCodesAddOrder);
        tableJobchainNodeReturnCodesAddOrder.setHeaderVisible(true);
        tableJobchainNodeReturnCodesAddOrder.setLinesVisible(true);

        tableJobchainNodeReturnCodesAddOrder.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesAddOrder.getSelectionIndex() >= 0) {
                    TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[tableJobchainNodeReturnCodesAddOrder.getSelectionIndex()];
                    JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = (JobchainReturnCodeAddOrderElement) item.getData();
                    tReturnCodesAddOrder.setText(jobchainReturnCodeAddOrderElement.getReturnCodes());
                    cJobChain.setText(jobchainReturnCodeAddOrderElement.getJobChain());
                    tOrderId.setText(jobchainReturnCodeAddOrderElement.getOrderId());

                    tableParams.removeAll();
                    btAddParam.setEnabled(true);

                    for (Entry<String, String> entry : jobchainReturnCodeAddOrderElement.getParams().entrySet()) {
                        TableItem itemParam = new TableItem(tableParams, SWT.NONE);
                        itemParam.setText(0, entry.getKey());
                        itemParam.setText(1, entry.getValue());
                    }

                    btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(true);
                    btNewJobchainNodeReturnAddOrder.setEnabled(true);
                }
            }
        });

        TableColumn tblclmnReturnCodes = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnReturnCodes.setWidth(250);
        tblclmnReturnCodes.setText("Return Codes");

        TableColumn tblclmnJobChain = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnJobChain.setWidth(300);
        tblclmnJobChain.setText(JOE_TCl_JobCommands_JobChain.label());

        TableColumn tblclmnOrderId = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnOrderId.setWidth(300);
        tblclmnOrderId.setText("Order ID");

    }

    /** This method initializes table */
    private void createTableNextStates() {

        Label lblNextState = JOE_L_JobChainNodes_NextState.Control(new Label(grpGroup, SWT.NONE));

        lblNextState.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        tableJobchainNodeReturnCodesNextState = new Table(grpGroup, SWT.FULL_SELECTION | SWT.BORDER);
        tableJobchainNodeReturnCodesNextState.setHeaderVisible(true);
        tableJobchainNodeReturnCodesNextState.setLinesVisible(true);
        GridData gd_tableJobchainNodeReturnCodesNextState = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
        gd_tableJobchainNodeReturnCodesNextState.widthHint = 720;
        gd_tableJobchainNodeReturnCodesNextState.heightHint = 112;
        tableJobchainNodeReturnCodesNextState.setLayoutData(gd_tableJobchainNodeReturnCodesNextState);
        tableJobchainNodeReturnCodesNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesNextState.getSelectionIndex() >= 0) {
                    TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[tableJobchainNodeReturnCodesNextState.getSelectionIndex()];
                    tReturnCodesNextState.setText(item.getText(0));
                    cNextState.setText(item.getText(1));
                    btRemoveJobchainNodeReturnCodeNextState.setEnabled(true);
                    btNewJobchainNodeReturnNextState.setEnabled(true);
                }

            }
        });
        TableColumn tableColumnHost = new TableColumn(tableJobchainNodeReturnCodesNextState, SWT.NONE);
        tableColumnHost.setWidth(250);
        tableColumnHost.setText("Return Codes");

        TableColumn tableColumnPort = new TableColumn(tableJobchainNodeReturnCodesNextState, SWT.NONE);
        tableColumnPort.setWidth(600);
        tableColumnPort.setText(JOE_L_JobChainNodes_State.label());

    }

    public JobchainListOfReturnCodeElements getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }

    public void setJobchainListOfReturnCodeElements(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        this.jobchainListOfReturnCodeElements = jobchainListOfReturnCodeElements;
    }

}
