package sos.scheduler.editor.conf.container;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;

public class JobDelayAfterError extends FormBaseClass {

    private JobOptionsListener objJobDataProvider = null;
    private Group group = null;
    private Table tErrorDelay = null;
    private Button bNewDelay = null;
    private Label label4 = null;
    private Text sErrorCount = null;
    private Text sErrorHours = null;
    private Label label14 = null;
    private Text sErrorMinutes = null;
    private Label label17 = null;
    private Text sErrorSeconds = null;
    private Button bRemoveDelay = null;
    private Button bApply = null;
    private Composite composite = null;
    private Button bStop = null;
    private Button bDelay = null;
    private Label label8 = null;
    private Label label5 = null;
    private Label label6 = null;

    public JobDelayAfterError(Composite pParentComposite, JobOptionsListener pobjDataProvider) {
        super(pParentComposite, pobjDataProvider);
        objJobDataProvider = pobjDataProvider;
        createGroup();
        initErrorDelays(objJobDataProvider.isErrorDelay());
        initForm();
    }

    private void createGroup() {
        GridData gridData23 = new org.eclipse.swt.layout.GridData();
        gridData23.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData23.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData22 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 12, 1);
        gridData22.heightHint = 10;
        GridData gridData21 = new org.eclipse.swt.layout.GridData();
        gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData21.widthHint = 90;
        gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData18 = new org.eclipse.swt.layout.GridData();
        gridData18.widthHint = 60;
        GridData gridData17 = new org.eclipse.swt.layout.GridData();
        gridData17.widthHint = 25;
        GridData gridData16 = new org.eclipse.swt.layout.GridData();
        gridData16.widthHint = 25;
        GridData gridData15 = new org.eclipse.swt.layout.GridData();
        gridData15.widthHint = 25;
        GridData gridData13 = new org.eclipse.swt.layout.GridData();
        gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData13.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData12 = new org.eclipse.swt.layout.GridData();
        gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData12.verticalSpan = 1;
        gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 12;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        group = SOSJOEMessageCodes.JOE_G_JobOptionsForm_DelayAfterError.control(new Group(objParent, SWT.NONE));
        group.setLayoutData(gridData1);
        group.setLayout(gridLayout3);
        label4 = SOSJOEMessageCodes.JOE_L_JobOptionsForm_ErrorCount.control(new Label(group, SWT.NONE));
        sErrorCount = SOSJOEMessageCodes.JOE_T_JobOptionsForm_ErrorCount.control(new Text(group, SWT.BORDER));
        sErrorCount.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        composite = new Composite(group, SWT.NONE);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));
        composite.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.END, GridData.CENTER, true, false));
        bStop = SOSJOEMessageCodes.JOE_B_JobOptionsForm_Stop.control(new Button(group, SWT.RADIO));
        bStop.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
                switchDelay(!bStop.getSelection());
            }
        });
        bDelay = SOSJOEMessageCodes.JOE_B_JobOptionsForm_Delay.control(new Button(group, SWT.RADIO));
        bDelay.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bApply.setEnabled(true);
                switchDelay(bDelay.getSelection());
            }
        });
        sErrorHours = SOSJOEMessageCodes.JOE_T_JobOptionsForm_ErrorHours.control(new Text(group, SWT.BORDER));
        sErrorHours.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label14 = SOSJOEMessageCodes.JOE_L_Colon.control(new Label(group, SWT.NONE));
        sErrorMinutes = SOSJOEMessageCodes.JOE_T_JobOptionsForm_ErrorMinutes.control(new Text(group, SWT.BORDER));
        sErrorMinutes.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label17 = SOSJOEMessageCodes.JOE_L_Colon.control(new Label(group, SWT.NONE));
        sErrorSeconds = SOSJOEMessageCodes.JOE_T_JobOptionsForm_ErrorSeconds.control(new Text(group, SWT.BORDER));
        sErrorSeconds.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label8 = SOSJOEMessageCodes.JOE_L_JobOptionsForm_DelayFormat.control(new Label(group, SWT.NONE));
        bApply = SOSJOEMessageCodes.JOE_B_JobOptionsForm_ApplyDelay.control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData21);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyDelay();
            }
        });
        label5 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label5.setLayoutData(gridData22);
        createTable();
        bNewDelay = SOSJOEMessageCodes.JOE_B_JobOptionsForm_NewDelayAfterError.control(new Button(group, SWT.NONE));
        bNewDelay.setLayoutData(gridData13);
        bNewDelay.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tErrorDelay.deselectAll();
                objJobDataProvider.newErrorDelay();
                initErrorDelay(true);
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
                sErrorCount.setFocus();
            }
        });
        label6 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label6.setLayoutData(gridData23);
        bRemoveDelay = SOSJOEMessageCodes.JOE_B_JobOptionsForm_RemoveDelay.control(new Button(group, SWT.NONE));
        bRemoveDelay.setEnabled(false);
        bRemoveDelay.setLayoutData(gridData12);
        bRemoveDelay.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tErrorDelay.getSelectionCount() > 0) {
                    int index = tErrorDelay.getSelectionIndex();
                    objJobDataProvider.deleteErrorDelay(index);
                    tErrorDelay.remove(index);
                    if (index >= tErrorDelay.getItemCount()) {
                        index--;
                    }
                    if (tErrorDelay.getItemCount() > 0) {
                        tErrorDelay.setSelection(index);
                        objJobDataProvider.selectErrorDelay(index);
                        initErrorDelay(true);
                    } else {
                        initErrorDelay(false);
                        bRemoveDelay.setEnabled(false);
                    }
                }
            }
        });
        sErrorCount.setLayoutData(gridData18);
        sErrorCount.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
            }
        });
        sErrorHours.setLayoutData(gridData17);
        sErrorHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(0, 23, sErrorHours);
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
            }
        });
        sErrorMinutes.setLayoutData(gridData16);
        sErrorMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(0, 59, sErrorMinutes);
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
            }
        });
        sErrorSeconds.setLayoutData(gridData15);
        sErrorSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if ((Utils.str2int(sErrorHours.getText()) > 0) || (Utils.str2int(sErrorMinutes.getText()) > 0)) {
                    Utils.setBackground(0, 59, sErrorSeconds);
                } else {
                    sErrorSeconds.setBackground(null);
                }
                getShell().setDefaultButton(bApply);
                bApply.setEnabled(true);
            }
        });
    }

    private void switchDelay(boolean enabled) {
        sErrorHours.setEnabled(enabled);
        sErrorMinutes.setEnabled(enabled);
        sErrorSeconds.setEnabled(enabled);
    }

    private void applyDelay() {
        boolean found = false;
        TableItem[] errorDelay = tErrorDelay.getItems();
        int sel = tErrorDelay.getSelectionIndex();
        int maximum = 0;
        int maxErrorDelay = 0;
        int maxAktErrorDelay = 0;
        for (int i = 0; i < errorDelay.length; i++) {
            if (i != sel && !"stop".equalsIgnoreCase(errorDelay[i].getText(1)) && maxAktErrorDelay < Utils.str2int(errorDelay[i].getText(0))) {
                maxAktErrorDelay = Utils.str2int(errorDelay[i].getText(0));
            }
        }
        for (int i = 0; i < errorDelay.length; i++) {
            if (i != sel && maxErrorDelay < Utils.str2int(errorDelay[i].getText(0)) && "stop".equalsIgnoreCase(errorDelay[i].getText(1))) {
                maxErrorDelay = Utils.str2int(errorDelay[i].getText(0));
            }
        }
        for (int i = 0; i < errorDelay.length; i++) {
            if ("stop".equalsIgnoreCase(errorDelay[i].getText(1)) && sel != i) {
                maximum = maximum + 1;
            }
        }
        if (bStop.getSelection()) {
            maximum = maximum + 1;
        }
        for (int i = 0; i < errorDelay.length; i++) {
            if (errorDelay[i].getText(0).equals(sErrorCount.getText()) && sel != i) {
                found = true;
            }
        }
        if (bStop.getSelection() && Utils.str2int(sErrorCount.getText()) > maxErrorDelay) {
            maxErrorDelay = Utils.str2int(sErrorCount.getText());
        }
        if (Utils.str2int(sErrorCount.getText()) > maxAktErrorDelay) {
            maxAktErrorDelay = Utils.str2int(sErrorCount.getText());
        }
        if (found) {
            MainWindow.message(SOSJOEMessageCodes.JOE_M_0036.label(), SWT.ICON_INFORMATION);
            sErrorCount.setFocus();
        } else {
            if (maxErrorDelay > 0 && maxErrorDelay < Utils.str2int(sErrorCount.getText()) || maxAktErrorDelay > Utils.str2int(sErrorCount.getText())
                    && bStop.getSelection()) {
                MainWindow.message(SOSJOEMessageCodes.JOE_M_0037.label(), SWT.ICON_INFORMATION);
                sErrorCount.setFocus();
            } else {
                if (maximum > 1) {
                    MainWindow.message(SOSJOEMessageCodes.JOE_M_0038.label(), SWT.ICON_INFORMATION);
                } else {
                    if ("".equals(sErrorCount.getText())) {
                        MainWindow.message(SOSJOEMessageCodes.JOE_M_0039.label(), SWT.ICON_INFORMATION);
                        sErrorCount.setFocus();
                    } else {
                        String delay = Utils.getTime(sErrorHours.getText(), sErrorMinutes.getText(), sErrorSeconds.getText(), true);
                        if (bStop.getSelection()) {
                            delay = "stop";
                        }
                        objJobDataProvider.applyErrorDelay(sErrorCount.getText(), delay);
                        objJobDataProvider.fillTable(tErrorDelay);
                        sortTable(tErrorDelay);
                        objJobDataProvider.newErrorDelays(tErrorDelay);
                        initErrorDelay(false);
                        getShell().setDefaultButton(null);
                    }
                }
            }
        }
    }

    private void createTable() {
        tErrorDelay = SOSJOEMessageCodes.JOE_Tbl_JobOptionsForm_ErrorDelay.control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
        tErrorDelay.setSortDirection(SWT.UP);
        tErrorDelay.setHeaderVisible(true);
        tErrorDelay.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 11, 3));
        tErrorDelay.setLinesVisible(true);
        tErrorDelay.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tErrorDelay.getSelectionCount() > 0) {
                    objJobDataProvider.selectErrorDelay(tErrorDelay.getSelectionIndex());
                    initErrorDelay(true);
                    sErrorCount.selectAll();
                } else {
                    initErrorDelay(false);
                }
                bRemoveDelay.setEnabled(tErrorDelay.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn = SOSJOEMessageCodes.JOE_TCl_JobOptionsForm_ErrorCount.control(new TableColumn(tErrorDelay, SWT.NONE));
        tErrorDelay.setSortColumn(tableColumn);
        tableColumn.setWidth(150);
        TableColumn tableColumn1 = SOSJOEMessageCodes.JOE_TCl_JobOptionsForm_Delayhhmmss.control(new TableColumn(tErrorDelay, SWT.NONE));
        tableColumn1.setWidth(250);
    }

    private void sortTable(Table t) {
        TableItem[] items = t.getItems();
        Collator collator = Collator.getInstance(Locale.getDefault());
        int index = 0;
        for (int i = 1; i < items.length; i++) {
            String value1 = items[i].getText(index);
            for (int j = 0; j < i; j++) {
                String value2 = items[j].getText(index);
                if (collator.compare(value1, value2) < 0) {
                    String[] values = { items[i].getText(0), items[i].getText(1) };
                    items[i].dispose();
                    TableItem item = new TableItem(t, SWT.NONE, j);
                    item.setText(values);
                    items = t.getItems();
                    break;
                }
            }
        }
    }

    private void initErrorDelays(boolean enabled) {
        bNewDelay.setEnabled(true);
        bStop.setEnabled(enabled);
        bDelay.setEnabled(enabled);
        initErrorDelay(false);
        objJobDataProvider.fillTable(tErrorDelay);
        bRemoveDelay.setEnabled(false);
    }

    private void initErrorDelay(boolean enabled) {
        bStop.setEnabled(enabled);
        bDelay.setEnabled(enabled);
        sErrorCount.setEnabled(enabled);
        sErrorHours.setEnabled(enabled);
        sErrorMinutes.setEnabled(enabled);
        sErrorSeconds.setEnabled(enabled);
        if (enabled) {
            boolean isStop = objJobDataProvider.isStop();
            bStop.setSelection(isStop);
            bDelay.setSelection(!isStop);
            if (isStop) {
                sErrorHours.setEnabled(false);
                sErrorMinutes.setEnabled(false);
                sErrorSeconds.setEnabled(false);
            } else {
                sErrorHours.setText(Utils.fill(2, objJobDataProvider.getErrorCountHours()));
                sErrorMinutes.setText(Utils.fill(2, objJobDataProvider.getErrorCountMinutes()));
                sErrorSeconds.setText(Utils.fill(2, objJobDataProvider.getErrorCountSeconds()));
            }
            sErrorCount.setText(objJobDataProvider.getErrorCount());
        }
        bApply.setEnabled(false);
    }

    private void initForm() {
        objJobDataProvider.fillTable(tErrorDelay);
    }

}