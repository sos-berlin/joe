package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

// import Options;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeNextStateElements;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeNextStateElement;
import sos.scheduler.editor.conf.listeners.JobChainListener;

public class JobChainNodeReturnCodeNextState extends FormBaseClass {

    protected Object result;
    protected Shell shell;

    private Table tableJobchainNodeReturnCodesNextState;
    private Button btNewJobchainNodeReturnNextState;
    private Button btOkJobchainNodeReturnCodeNextState;
    private Button btRemoveJobchainNodeReturnCodeNextState;
    private Text tReturnCodesNextState;
    private Combo cNextState = null;
    private Group grpGroup;
    private JobChainListener listener = null;
    private JobchainListOfReturnCodeNextStateElements jobchainListOfReturnCodeNextStateElements;

    public JobChainNodeReturnCodeNextState(Composite pParentComposite, JobChainListener listener) {
        super(pParentComposite, listener);
        objJobDataProvider = listener;
        this.listener = listener;
        createGroup(pParentComposite);
    }

    private void createGroup(Composite parent) {
        grpGroup = new Group(parent, SWT.NONE);
        grpGroup.setText(SOSJOEMessageCodes.JOE_G_ReturnCodesForm_ReturnCodes.params(listener.getState()));

        GridData gd_grpGroup = new GridData(SWT.FILL, SWT.CENTER, false, true, 4, 1);
        gd_grpGroup.widthHint = 950;
        gd_grpGroup.heightHint = 680;
        grpGroup.setLayoutData(gd_grpGroup);
        GridLayout gl_grpGroup = new GridLayout(5, false);
        grpGroup.setLayout(gl_grpGroup);

        createTableNextStates();

        GridData gd_btOkJobchainNodeReturnCodeNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_btOkJobchainNodeReturnCodeNextState.widthHint = 140;

        btOkJobchainNodeReturnCodeNextState = SOSJOEMessageCodes.JOE_B_ReturnCodesForm_Add_Next_State.Control(new Button(grpGroup, SWT.NONE));

        btOkJobchainNodeReturnCodeNextState.setLayoutData(gd_btOkJobchainNodeReturnCodeNextState);
        btOkJobchainNodeReturnCodeNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addReturnCodeNextState();
            }
        });

        Label lbSchedulerReturnCodesNextState = new Label(grpGroup, SWT.NONE);
        lbSchedulerReturnCodesNextState.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerReturnCodesNextState.setText("Return Codes");

        tReturnCodesNextState = new Text(grpGroup, SWT.BORDER);
        GridData gd_tReturnCodesNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_tReturnCodesNextState.widthHint = 250;
        tReturnCodesNextState.setLayoutData(gd_tReturnCodesNextState);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        GridData gd_btNewJobchainNodeReturnNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_btNewJobchainNodeReturnNextState.widthHint = 100;

        btNewJobchainNodeReturnNextState = SOSJOEMessageCodes.JOE_B_New.Control(new Button(grpGroup, SWT.NONE));
        btNewJobchainNodeReturnNextState.setEnabled(false);
        btNewJobchainNodeReturnNextState.setLayoutData(gd_btNewJobchainNodeReturnNextState);
        btNewJobchainNodeReturnNextState.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableJobchainNodeReturnCodesNextState.setSelection(-1);
                tReturnCodesNextState.setText("");
                cNextState.setText("");
                tReturnCodesNextState.setFocus();
                btNewJobchainNodeReturnNextState.setEnabled(false);
                btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);
            }
        });

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

        GridData gd_btRemoveJobchainNodeReturnCodeNextState = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_btRemoveJobchainNodeReturnCodeNextState.widthHint = 100;

        btRemoveJobchainNodeReturnCodeNextState = SOSJOEMessageCodes.JOE_B_Remove.Control(new Button(grpGroup, SWT.NONE));
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
                    btNewJobchainNodeReturnNextState.setEnabled(false);
                    btRemoveJobchainNodeReturnCodeNextState.setEnabled(false);
                }
            }
        });
    }

    private void createTableNextStates() {

        Label lblNextState = SOSJOEMessageCodes.JOE_L_JobChainNodes_NextState.Control(new Label(grpGroup, SWT.NONE));

        lblNextState.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);
        new Label(grpGroup, SWT.NONE);

        tableJobchainNodeReturnCodesNextState = new Table(grpGroup, SWT.FULL_SELECTION | SWT.BORDER);
        tableJobchainNodeReturnCodesNextState.setHeaderVisible(true);
        tableJobchainNodeReturnCodesNextState.setLinesVisible(true);
        GridData gd_tableJobchainNodeReturnCodesNextState = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
        gd_tableJobchainNodeReturnCodesNextState.widthHint = 900;
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
        tableColumnPort.setText(SOSJOEMessageCodes.JOE_L_JobChainNodes_State.label());

    }

    public void saveNextStateList() {
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

    public void initTable(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        jobchainListOfReturnCodeElements.reset();
        while (jobchainListOfReturnCodeElements.hasNext()) {
            JobchainReturnCodeElement jobchainReturnCodeElement = jobchainListOfReturnCodeElements.getNext();
            JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement = jobchainReturnCodeElement.getJobchainReturnCodeNextStateElement();
            if (jobchainReturnCodeNextStateElement != null) {
                TableItem item = new TableItem(tableJobchainNodeReturnCodesNextState, SWT.NONE);
                item.setText(0, jobchainReturnCodeNextStateElement.getReturnCodes());
                item.setText(1, jobchainReturnCodeNextStateElement.getNextState());
            }
        }
    }

    public JobchainListOfReturnCodeNextStateElements getJobchainListOfReturnCodeNextStateElements() {
        return jobchainListOfReturnCodeNextStateElements;
    }

}