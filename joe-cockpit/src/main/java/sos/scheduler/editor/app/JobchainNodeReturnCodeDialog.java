package sos.scheduler.editor.app;

import java.util.Map.Entry;

 
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
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

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;

public class JobchainNodeReturnCodeDialog extends Dialog {

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
    private Text tNextState = null;
    private Table tableJobchainNodeReturnCodesAddOrder;
    private Text tJobChain;
    private Text tOrderId;
    private Table tableParams;

    private Group grpGroup;
    private Text tParamName;
    private Text tParamValue;

    private Button btAddParam;
    private Button btRemoveParam;
    private Button btNewParam;
    private Text tReturnCodesAddOrder;
    
    private boolean ok;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public JobchainNodeReturnCodeDialog(Shell parent_, int style) {
        super(parent_, style);
        setText("Exit Codes");
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
        dialogShell.setMinimumSize(new Point(900, 700));

        dialogShell.setSize(1024, 700);
        dialogShell.setLayout(new GridLayout(4, false));
        dialogShell.pack();

        createGroup(dialogShell);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Button btnOk = new Button(grpGroup, SWT.NONE);
        GridData gd_btnOk = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btnOk.widthHint = 58;
        btnOk.setLayoutData(gd_btnOk);
        btnOk.setSize(68, 25);
        btnOk.setText("Ok");
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

        Button btnCancelButton = new Button(grpGroup, SWT.NONE);
        btnCancelButton.setSize(48, 25);
        btnCancelButton.setText("Cancel");
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btnCancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (jobchainListOfReturnCodeElements != null){
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

        if (tReturnCodesAddOrder.getText().length() > 0 && tJobChain.getText().length() > 0) {
            JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = new JobchainReturnCodeAddOrderElement();
            jobchainReturnCodeAddOrderElement.setJobChain(tJobChain.getText());
            jobchainReturnCodeAddOrderElement.setOrderId(tOrderId.getText());
            jobchainReturnCodeAddOrderElement.setReturnCodes(tReturnCodesAddOrder.getText());
           
            for (int i = 0; i < tableParams.getItemCount(); i++) {
                TableItem item = tableParams.getItems()[i];
                jobchainReturnCodeAddOrderElement.addParam(item.getText(0),item.getText(1));
            }

            if (tableJobchainNodeReturnCodesAddOrder.getSelectionIndex() >= 0) {
                TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[tableJobchainNodeReturnCodesAddOrder.getSelectionIndex()];
                item.setText(0, tReturnCodesAddOrder.getText());
                item.setText(1, tJobChain.getText());
                item.setText(2, tOrderId.getText());
                item.setData(jobchainReturnCodeAddOrderElement);
                clearInputFieldsAddOrder();
                tReturnCodesAddOrder.setFocus();

            } else {
                TableItem item = new TableItem(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
                item.setText(0, tReturnCodesAddOrder.getText());
                item.setText(1, tJobChain.getText());
                item.setText(2, tOrderId.getText());
                item.setData(jobchainReturnCodeAddOrderElement);
                clearInputFieldsAddOrder();
                tReturnCodesAddOrder.setFocus();
            }
             
            
            
        }
        tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
    }

    private void addReturnCodeNextState() {
        if (tReturnCodesNextState.getText().length() > 0 && tNextState.getText().length() > 0) {
            if (tableJobchainNodeReturnCodesNextState.getSelectionIndex() >= 0) {
                TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[tableJobchainNodeReturnCodesNextState.getSelectionIndex()];
                item.setText(0, tReturnCodesNextState.getText());
                item.setText(1, tNextState.getText());
                tReturnCodesNextState.setText("");
                tNextState.setText("");
                tReturnCodesNextState.setFocus();

            } else {
                for (int i = 0; i < tableJobchainNodeReturnCodesNextState.getItemCount(); i++) {
                    TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[i];
                    if ((item.getText(0).equals(tReturnCodesNextState.getText()) && tReturnCodesNextState.getText().length() > 0)) {
                        item.setText(0, tReturnCodesNextState.getText());
                        item.setText(1, tNextState.getText());
                        tReturnCodesNextState.setText("");
                        tNextState.setText("");
                        tReturnCodesNextState.setFocus();

                    }
                }
                if (tReturnCodesNextState.getText().length() > 0) {

                    TableItem item = new TableItem(tableJobchainNodeReturnCodesNextState, SWT.NONE);
                    item.setText(0, tReturnCodesNextState.getText());
                    item.setText(1, tNextState.getText());
                    tReturnCodesNextState.setText("");
                    tNextState.setText("");
                    tReturnCodesNextState.setFocus();

                }
            }
            tableJobchainNodeReturnCodesNextState.setSelection(-1);

        }
    }

    /**
     * This method initializes group
     */
    private void createGroup(Composite parent) {

        // group = JOE_G_ProcessClassesForm_ProcessClasses.Control(new
        // Group(this, SWT.NONE));
        grpGroup = new Group(parent, SWT.NONE);
        GridData gd_grpGroup = new GridData(SWT.FILL, SWT.CENTER, false, true, 4, 1);
        gd_grpGroup.widthHint = 871;
        gd_grpGroup.heightHint = 622;
        grpGroup.setLayoutData(gd_grpGroup);
        GridLayout gl_grpGroup = new GridLayout(4, false);
        gl_grpGroup.horizontalSpacing = 6;
        gl_grpGroup.verticalSpacing = 2;
        grpGroup.setLayout(gl_grpGroup);

        createTableNextStates();

        GridData gd_btOkJobchainNodeReturnCodeNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_btOkJobchainNodeReturnCodeNextState.widthHint = 100;

        btOkJobchainNodeReturnCodeNextState = new Button(grpGroup, SWT.NONE);
        btOkJobchainNodeReturnCodeNextState.setText("Add Next State");
        btOkJobchainNodeReturnCodeNextState.setLayoutData(gd_btOkJobchainNodeReturnCodeNextState);
        btOkJobchainNodeReturnCodeNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addReturnCodeNextState();
            }
        });

        GridData gd_btNewJobchainNodeReturnNextState = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
        gd_btNewJobchainNodeReturnNextState.widthHint = 100;
        // btNewRemoteScheduler =
        // JOE_B_ProcessClassesForm_NewRemotScheduler.Control(new Button(group,
        // SWT.NONE));
        btNewJobchainNodeReturnNextState = new Button(grpGroup, SWT.NONE);
        btNewJobchainNodeReturnNextState.setEnabled(false);
        btNewJobchainNodeReturnNextState.setText("New");
        btNewJobchainNodeReturnNextState.setLayoutData(gd_btNewJobchainNodeReturnNextState);
        btNewJobchainNodeReturnNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableJobchainNodeReturnCodesNextState.setSelection(-1);
                tReturnCodesNextState.setText("");
                tNextState.setText("");
                tReturnCodesNextState.setFocus();
                btNewJobchainNodeReturnNextState.setEnabled(false);
            }
        });

        GridData gd_btRemoveJobchainNodeReturnCodeNextState = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
        gd_btRemoveJobchainNodeReturnCodeNextState.widthHint = 100;
        // btRemoveRemoteScheduler =
        // JOE_B_ProcessClassesForm_RemoveRemotScheduler.Control(new
        // Button(group, SWT.NONE));
        btRemoveJobchainNodeReturnCodeNextState = new Button(grpGroup, SWT.NONE);
        btRemoveJobchainNodeReturnCodeNextState.setText("Remove");
        btRemoveJobchainNodeReturnCodeNextState.setLayoutData(gd_btRemoveJobchainNodeReturnCodeNextState);
        btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);

        btRemoveJobchainNodeReturnCodeNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesNextState.getSelectionCount() > 0) {
                    int index = tableJobchainNodeReturnCodesNextState.getSelectionIndex();
                    tableJobchainNodeReturnCodesNextState.remove(index);
                    tableJobchainNodeReturnCodesNextState.setSelection(-1);
                    tReturnCodesNextState.setText("");
                    tNextState.setText("");
                    tReturnCodesNextState.setFocus();
                    btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);
                }
                btRemoveJobchainNodeReturnCodeNextState.setEnabled(tableJobchainNodeReturnCodesNextState.getSelectionCount() > 0);
            }
        });
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

        Label lbSchedulerRemotePort = new Label(grpGroup, SWT.NONE);
        lbSchedulerRemotePort.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerRemotePort.setText("State");

        tNextState = new Text(grpGroup, SWT.BORDER);
        GridData gd_tNextState = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tNextState.widthHint = 250;
        tNextState.setLayoutData(gd_tNextState);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblNewLabel_1 = new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        Label lblAddOrders = new Label(grpGroup, SWT.NONE);
        lblAddOrders.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        lblAddOrders.setText("Add Orders");
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        createTableAddOrder();

        btOkJobchainNodeReturnCodeAddOrder = new Button(grpGroup, SWT.NONE);
        btOkJobchainNodeReturnCodeAddOrder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addReturnCodeAddOrder();
            }
        });
        btOkJobchainNodeReturnCodeAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btOkJobchainNodeReturnCodeAddOrder.setText("Add Order");

        btNewJobchainNodeReturnAddOrder = new Button(grpGroup, SWT.NONE);
        btNewJobchainNodeReturnAddOrder.setEnabled(false);
        GridData gd_btNewJobchainNodeReturnAddOrder = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_btNewJobchainNodeReturnAddOrder.widthHint = 92;
        btNewJobchainNodeReturnAddOrder.setLayoutData(gd_btNewJobchainNodeReturnAddOrder);
        btNewJobchainNodeReturnAddOrder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
                clearInputFieldsAddOrder();

                tReturnCodesAddOrder.setFocus();
                btNewJobchainNodeReturnNextState.setEnabled(false);
            }
        });
        btNewJobchainNodeReturnAddOrder.setText("New");

        btRemoveJobchainNodeReturnCodeAddOrder = new Button(grpGroup, SWT.NONE);
        btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(false);
        btRemoveJobchainNodeReturnCodeAddOrder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tableJobchainNodeReturnCodesAddOrder.getSelectionCount() > 0) {
                    int index = tableJobchainNodeReturnCodesAddOrder.getSelectionIndex();
                    if (index >= 0) {
                        tableJobchainNodeReturnCodesAddOrder.remove(index);
                        tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
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
        btRemoveJobchainNodeReturnCodeAddOrder.setText("Remove");
        new Label(grpGroup, SWT.NONE);

        Label lblNewLabel = new Label(grpGroup, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("ReturnCodes");

        tReturnCodesAddOrder = new Text(grpGroup, SWT.BORDER);
        tReturnCodesAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblName = new Label(grpGroup, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        tParamName = new Text(grpGroup, SWT.BORDER);
        GridData gd_tParamName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tParamName.widthHint = 254;
        tParamName.setLayoutData(gd_tParamName);

        Label lblJobChain = new Label(grpGroup, SWT.NONE);
        lblJobChain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJobChain.setText("Job Chain");

        tJobChain = new Text(grpGroup, SWT.BORDER);
        GridData gd_tJobChain = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tJobChain.widthHint = 250;
        tJobChain.setLayoutData(gd_tJobChain);

        Label lblValue = new Label(grpGroup, SWT.NONE);
        lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblValue.setText("Value");

        tParamValue = new Text(grpGroup, SWT.BORDER);
        tParamValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

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

        btAddParam = new Button(grpGroup, SWT.NONE);
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
        btAddParam.setText("Add Param");

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
        tblclmnName.setWidth(162);
        tblclmnName.setText("Name");

        TableColumn tblclmnValue = new TableColumn(tableParams, SWT.NONE);
        tblclmnValue.setWidth(95);
        tblclmnValue.setText("Value");
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btNewParam = new Button(grpGroup, SWT.NONE);
        btNewParam.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tParamName.setText("");
                tParamValue.setText("");
                tParamName.setFocus();
            }
        });
        btNewParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btNewParam.setText("New Param");
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btRemoveParam = new Button(grpGroup, SWT.NONE);
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
        btRemoveParam.setText("Remove Param");
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
        tJobChain.setText("");
        tOrderId.setText("");
        tableParams.clearAll();

    }

    private void createTableAddOrder() {

        tableJobchainNodeReturnCodesAddOrder = new Table(grpGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_tableJobchainNodeReturnCodesAddOrder = new GridData(SWT.FILL, SWT.FILL, false, true, 4, 4);
        gd_tableJobchainNodeReturnCodesAddOrder.heightHint = 82;
        gd_tableJobchainNodeReturnCodesAddOrder.widthHint = 782;
        tableJobchainNodeReturnCodesAddOrder.setLayoutData(gd_tableJobchainNodeReturnCodesAddOrder);
        tableJobchainNodeReturnCodesAddOrder.setHeaderVisible(true);
        tableJobchainNodeReturnCodesAddOrder.setLinesVisible(true);

        tableJobchainNodeReturnCodesAddOrder.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesAddOrder.getSelectionIndex() >= 0) {
                    TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[tableJobchainNodeReturnCodesAddOrder.getSelectionIndex()];
                    JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = (JobchainReturnCodeAddOrderElement) item.getData();
                    tReturnCodesAddOrder.setText(jobchainReturnCodeAddOrderElement.getReturnCodes());
                    tJobChain.setText(jobchainReturnCodeAddOrderElement.getJobChain());
                    tOrderId.setText(jobchainReturnCodeAddOrderElement.getOrderId());
                    
                    tableParams.removeAll();
                    for (Entry<String, String> entry : jobchainReturnCodeAddOrderElement.getParams().entrySet()) {
                        TableItem itemParam = new TableItem(tableParams, SWT.NONE);
                        itemParam.setText(0,entry.getKey());
                        itemParam.setText(1,entry.getValue());
                    }

                    btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(true);
                    btNewJobchainNodeReturnAddOrder.setEnabled(true);
                }
           }
        });

        TableColumn tblclmnReturnCodes = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnReturnCodes.setWidth(203);
        tblclmnReturnCodes.setText("Return Codes");

        TableColumn tblclmnJobChain = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnJobChain.setWidth(344);
        tblclmnJobChain.setText("Job Chain");

        TableColumn tblclmnOrderId = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnOrderId.setWidth(233);
        tblclmnOrderId.setText("Order ID");

    }

    /**
     * This method initializes table
     */
    private void createTableNextStates() {

        Label lblNextState = new Label(grpGroup, SWT.NONE);
        lblNextState.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        lblNextState.setText("Next State");
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        tableJobchainNodeReturnCodesNextState = new Table(grpGroup, SWT.FULL_SELECTION | SWT.BORDER);
        tableJobchainNodeReturnCodesNextState.setHeaderVisible(true);
        tableJobchainNodeReturnCodesNextState.setLinesVisible(true);
        GridData gd_tableJobchainNodeReturnCodesNextState = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 3);
        gd_tableJobchainNodeReturnCodesNextState.widthHint = 832;
        gd_tableJobchainNodeReturnCodesNextState.heightHint = 112;
        tableJobchainNodeReturnCodesNextState.setLayoutData(gd_tableJobchainNodeReturnCodesNextState);
        tableJobchainNodeReturnCodesNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableJobchainNodeReturnCodesNextState.getSelectionIndex() >= 0) {
                    TableItem item = tableJobchainNodeReturnCodesNextState.getItems()[tableJobchainNodeReturnCodesNextState.getSelectionIndex()];
                    tReturnCodesNextState.setText(item.getText(0));
                    tNextState.setText(item.getText(1));
                    btRemoveJobchainNodeReturnCodeNextState.setEnabled(true);
                    btNewJobchainNodeReturnNextState.setEnabled(true);
                }

            }
        });
        TableColumn tableColumnHost = new TableColumn(tableJobchainNodeReturnCodesNextState, SWT.NONE);
        tableColumnHost.setWidth(567);
        tableColumnHost.setText("Return Codes");

        TableColumn tableColumnPort = new TableColumn(tableJobchainNodeReturnCodesNextState, SWT.NONE);
        tableColumnPort.setWidth(261);
        tableColumnPort.setText("State");

    }

    public JobchainListOfReturnCodeElements getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }

    public void setJobchainListOfReturnCodeElements(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        this.jobchainListOfReturnCodeElements = jobchainListOfReturnCodeElements;
    }

}
