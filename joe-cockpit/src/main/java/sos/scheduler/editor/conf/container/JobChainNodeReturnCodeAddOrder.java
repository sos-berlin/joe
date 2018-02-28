package sos.scheduler.editor.conf.container;

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
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
import org.eclipse.wb.swt.SWTResourceManager;

// import Options;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeAddOrderElements;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeAddOrderElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeElement;

public class JobChainNodeReturnCodeAddOrder extends FormBaseClass {

    protected Object result;

    private Button btOkJobchainNodeReturnCodeAddOrder;
    private Button btNewJobchainNodeReturnAddOrder;
    private Button btRemoveJobchainNodeReturnCodeAddOrder;

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
    private JobchainListOfReturnCodeAddOrderElements jobchainListOfReturnCodeAddOrderElements;

    public JobChainNodeReturnCodeAddOrder(Composite pParentComposite, JobChainListener listener) {
        super(pParentComposite, listener);
        objJobDataProvider = listener;
        this.listener = listener;
        createGroup(pParentComposite);
    }

    private void createGroup(Composite parent) {
        grpGroup = new Group(parent, SWT.NONE);
        grpGroup.setText(SOSJOEMessageCodes.JOE_G_ReturnCodesForm_ReturnCodes.params(listener.getState()));

        GridData gd_grpGroup = new GridData(SWT.FILL, SWT.CENTER, false, true, 5, 1);
        gd_grpGroup.widthHint = 950;
        gd_grpGroup.heightHint = 680;
        grpGroup.setLayoutData(gd_grpGroup);
        GridLayout gl_grpGroup = new GridLayout(5, false);
 
        grpGroup.setLayout(gl_grpGroup);

        Label lblAddOrders = SOSJOEMessageCodes.JOE_L_ReturnCodesForm_Add_Order.Control(new Label(grpGroup, SWT.NONE));

        lblAddOrders.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        createTableAddOrder();

        btOkJobchainNodeReturnCodeAddOrder = SOSJOEMessageCodes.JOE_B_ReturnCodesForm_Add_Order.Control(new Button(grpGroup, SWT.NONE));
        btOkJobchainNodeReturnCodeAddOrder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addReturnCodeAddOrder();
            }
        });
        btOkJobchainNodeReturnCodeAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        Label lblNewLabel = new Label(grpGroup, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("ReturnCodes");

        tReturnCodesAddOrder = new Text(grpGroup, SWT.BORDER);
        tReturnCodesAddOrder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btNewJobchainNodeReturnAddOrder = SOSJOEMessageCodes.JOE_B_New.Control(new Button(grpGroup, SWT.NONE));
        btNewJobchainNodeReturnAddOrder.setEnabled(false);
        GridData gd_btNewJobchainNodeReturnAddOrder = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btNewJobchainNodeReturnAddOrder.widthHint = 100;
        btNewJobchainNodeReturnAddOrder.setLayoutData(gd_btNewJobchainNodeReturnAddOrder);
        btNewJobchainNodeReturnAddOrder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableJobchainNodeReturnCodesAddOrder.setSelection(-1);
                btAddParam.setEnabled(false);
                clearInputFieldsAddOrder();
                btNewJobchainNodeReturnAddOrder.setEnabled(false);
                btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(false);
                tReturnCodesAddOrder.setFocus();

            }
        });

        Label lblJobChain = SOSJOEMessageCodes.JOE_L_OrderForm_JobChain.Control(new Label(grpGroup, SWT.NONE));
        lblJobChain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        cJobChain = new Combo(grpGroup, SWT.BORDER);
        cJobChain.setItems(listener.getJobChains());

        GridData gd_tJobChain = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_tJobChain.widthHint = 250;
        cJobChain.setLayoutData(gd_tJobChain);

        Button btnBrowse = SOSJOEMessageCodes.JOE_B_JobChainNodes_Browse.Control(new Button(grpGroup, SWT.NONE));
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

        btRemoveJobchainNodeReturnCodeAddOrder = SOSJOEMessageCodes.JOE_B_Remove.Control(new Button(grpGroup, SWT.NONE));
        btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(false);
        GridData gd_btRemoveJobchainNodeReturnAddOrder = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_btRemoveJobchainNodeReturnAddOrder.widthHint = 100;
        btRemoveJobchainNodeReturnCodeAddOrder.setLayoutData(gd_btRemoveJobchainNodeReturnAddOrder);
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
                        tReturnCodesAddOrder.setFocus();

                        btNewJobchainNodeReturnAddOrder.setEnabled(false);
                        btRemoveJobchainNodeReturnCodeAddOrder.setEnabled(false);
                    }
                }
            }
        });

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
        Label lblValue = SOSJOEMessageCodes.JOE_L_ParameterForm_Value.Control(new Label(grpGroup, SWT.NONE));
        lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        tParamValue = new Text(grpGroup, SWT.BORDER);
        tParamValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btAddParam = SOSJOEMessageCodes.JOE_B_ReturnCodesForm_Add_Param.Control(new Button(grpGroup, SWT.NONE));
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
                }
            }
        });
        btAddParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        new Label(grpGroup, SWT.NONE);
        tableParams = new Table(grpGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_tableParams = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 5);
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
        tblclmnValue.setText(SOSJOEMessageCodes.JOE_L_ParameterForm_Value.label());
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        btNewParam = SOSJOEMessageCodes.JOE_B_ReturnCodesForm_New_Param.Control(new Button(grpGroup, SWT.NONE));
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

        btRemoveParam = SOSJOEMessageCodes.JOE_B_ReturnCodesForm_Remove_Param.Control(new Button(grpGroup, SWT.NONE));
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

    private void createTableAddOrder() {

        tableJobchainNodeReturnCodesAddOrder = new Table(grpGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData gd_tableJobchainNodeReturnCodesAddOrder = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 5);
        gd_tableJobchainNodeReturnCodesAddOrder.heightHint = 120;
        gd_tableJobchainNodeReturnCodesAddOrder.widthHint = 900;
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
        tblclmnJobChain.setText(SOSJOEMessageCodes.JOE_TCl_JobCommands_JobChain.label());

        TableColumn tblclmnOrderId = new TableColumn(tableJobchainNodeReturnCodesAddOrder, SWT.NONE);
        tblclmnOrderId.setWidth(300);
        tblclmnOrderId.setText("Order ID");

    }

    private void clearInputFieldsAddOrder() {
        tReturnCodesAddOrder.setText("");
        cJobChain.setText("");
        tOrderId.setText("");
        tableParams.clearAll();

    }

    public void saveAddOrderList() {
        addReturnCodeAddOrder();
        jobchainListOfReturnCodeAddOrderElements = new JobchainListOfReturnCodeAddOrderElements();
        for (int i = 0; i < tableJobchainNodeReturnCodesAddOrder.getItemCount(); i++) {
            TableItem item = tableJobchainNodeReturnCodesAddOrder.getItems()[i];
            JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = (JobchainReturnCodeAddOrderElement) item.getData();
            jobchainListOfReturnCodeAddOrderElements.add(jobchainReturnCodeAddOrderElement);
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

    public void initTable(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        jobchainListOfReturnCodeElements.reset();
        while (jobchainListOfReturnCodeElements.hasNext()) {
            JobchainReturnCodeElement jobchainReturnCodeElement = jobchainListOfReturnCodeElements.getNext();
            JobchainListOfReturnCodeAddOrderElements jobchainListOfReturnCodeAddOrderElements = jobchainReturnCodeElement
                    .getJobchainListOfReturnCodeAddOrderElements();

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

    public JobchainListOfReturnCodeAddOrderElements getJobchainListOfReturnCodeAddOrderElements() {
        return jobchainListOfReturnCodeAddOrderElements;
    }
}