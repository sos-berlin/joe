package sos.scheduler.editor.conf.container;
import java.text.Collator;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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

import sos.scheduler.editor.app.MainWindow;
import com.sos.joe.xml.Utils;
import sos.scheduler.editor.classes.FormBaseClass;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.objects.job.JobListener;

public class JobSetback extends FormBaseClass <JobListener>  {

	private final String conClassName = this.getClass().getSimpleName();
	private static final String conSVNVersion = "$Id$";
	private final Logger logger = Logger.getLogger(this.getClass());

    private Group              group              = null;

    @SuppressWarnings("unused")
	private Label              label2             = null;
    @SuppressWarnings("unused")
	private Label              label7             = null;
    @SuppressWarnings("unused")
	private Label              label9             = null;
    private Button             bNewSetback        = null;
    private Label              label30            = null;
    private Label              label31            = null;
    @SuppressWarnings("unused")
	private Label              label10            = null;

    private Table              tSetback           = null;
    private Text               sSetBackCount      = null;
    private Button             bIsMaximum         = null;
    private Text               sSetBackHours      = null;
    private Text               sSetBackMinutes    = null;
    private Text               sSetBackSeconds    = null;
    private Button             bApplySetback      = null;
    private Button             bRemoveSetback     = null;

    public JobSetback(final Composite pParentComposite, final JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;

		logger.debug(conClassName + "\n" + conSVNVersion);

        createGroup();
        initForm();
    }

    private void initForm() {
        objJobDataProvider.fillSetbacks(tSetback);
    }

    @Override
	public void createGroup() {
        GridData gridData29 = new org.eclipse.swt.layout.GridData();
        gridData29.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData29.widthHint = 90;
        gridData29.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData28 = new org.eclipse.swt.layout.GridData();
        gridData28.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData28.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData27 = new org.eclipse.swt.layout.GridData();
        gridData27.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData27.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData26 = new org.eclipse.swt.layout.GridData();
        gridData26.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData26.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData25 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 11, 1);
        gridData25.heightHint = 10;
        GridData gridData10 = new org.eclipse.swt.layout.GridData();
        gridData10.widthHint = 25;
        gridData10.horizontalSpan = 1;
        GridData gridData9 = new org.eclipse.swt.layout.GridData();
        gridData9.widthHint = 25;
        GridData gridData8 = new org.eclipse.swt.layout.GridData();
        gridData8.widthHint = 25;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.horizontalSpan = 1;
        gridData6.grabExcessHorizontalSpace = true;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 11;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

        group = SOSJOEMessageCodes.JOE_G_JobOptionsForm_DelayOrderAfterSetBack.Control(new Group(objParent, SWT.NONE));
        group.setLayout(gridLayout2);
        group.setLayoutData(gridData2);

        label2 = SOSJOEMessageCodes.JOE_L_JobOptionsForm_SetBackCount.Control(new Label(group, SWT.NONE));

        sSetBackCount = SOSJOEMessageCodes.JOE_T_JobOptionsForm_SetBackCount.Control(new Text(group, SWT.BORDER));
        sSetBackCount.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        sSetBackCount.setLayoutData(new GridData(48, SWT.DEFAULT));

        bIsMaximum = SOSJOEMessageCodes.JOE_B_JobOptionsForm_IsMax.Control(new Button(group, SWT.CHECK));
        bIsMaximum.setLayoutData(gridData6);
        bIsMaximum.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApplySetback);
                /*sSetBackHours.setEnabled(!bIsMaximum.getSelection());
                sSetBackMinutes.setEnabled(!bIsMaximum.getSelection());
                sSetBackSeconds.setEnabled(!bIsMaximum.getSelection());
                */
                bApplySetback.setEnabled(true);
            }
        });

        @SuppressWarnings("unused")
		final Label delayLabel = SOSJOEMessageCodes.JOE_L_JobOptionsForm_Delay.Control(new Label(group, SWT.NONE));

        sSetBackHours = SOSJOEMessageCodes.JOE_T_JobOptionsForm_SetBackHours.Control(new Text(group, SWT.BORDER));
        sSetBackHours.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });

        label7 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(group, SWT.NONE));

        sSetBackCount.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                getShell().setDefaultButton(bApplySetback);
                bApplySetback.setEnabled(true);
            }
        });

        sSetBackMinutes = SOSJOEMessageCodes.JOE_T_JobOptionsForm_SetBackMinutes.Control(new Text(group, SWT.BORDER));
        sSetBackMinutes.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label9 = SOSJOEMessageCodes.JOE_L_Colon.Control(new Label(group, SWT.NONE));

        sSetBackSeconds = SOSJOEMessageCodes.JOE_T_JobOptionsForm_SetBackMinutes.Control(new Text(group, SWT.BORDER));
        sSetBackSeconds.addVerifyListener(new VerifyListener() {
            @Override
			public void verifyText(final VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });

        label10 = SOSJOEMessageCodes.JOE_L_JobSetback_TimeFormat.Control(new Label(group, SWT.NONE));

        bApplySetback = SOSJOEMessageCodes.JOE_B_JobOptionsForm_ApplyDelay.Control(new Button(group, SWT.NONE));
        bApplySetback.setEnabled(false);
        bApplySetback.setLayoutData(gridData29);
        bApplySetback.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                applySetback();
            }
        });

        label31 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label31.setText("Label");
        label31.setLayoutData(gridData25);

        createTable();

        bNewSetback = SOSJOEMessageCodes.JOE_B_JobOptionsForm_NewSetBack.Control(new Button(group, SWT.NONE));
        bNewSetback.setEnabled(true);
        bNewSetback.setLayoutData(gridData28);
        bNewSetback.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                tSetback.deselectAll();
                objJobDataProvider.newSetbackDelay();
                initSetback(true);
                getShell().setDefaultButton(bApplySetback);
                bApplySetback.setEnabled(true);
                sSetBackCount.setFocus();
            }
        });

        label30 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label30.setText("Label");
        label30.setLayoutData(gridData26);

        bRemoveSetback = SOSJOEMessageCodes.JOE_B_JobOptionsForm_RemoveSetback.Control(new Button(group, SWT.NONE));
        bRemoveSetback.setEnabled(false);
        bRemoveSetback.setLayoutData(gridData27);
        bRemoveSetback.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                if (tSetback.getSelectionCount() > 0) {
                    int index = tSetback.getSelectionIndex();
                    objJobDataProvider.deleteSetbackDelay(index);
                    tSetback.remove(index);
                    if (index >= tSetback.getItemCount())
                        index--;
                    if (tSetback.getItemCount() > 0) {
                        tSetback.setSelection(index);
                        objJobDataProvider.selectSetbackDelay(index);
                        initSetback(true);
                    }
                    else {
                        initSetback(false);
                        bRemoveSetback.setEnabled(false);
                    }
                }
            }
        });

        sSetBackHours.setLayoutData(gridData8);
        sSetBackHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(0, 23, sSetBackHours);
                getShell().setDefaultButton(bApplySetback);
                bApplySetback.setEnabled(true);
            }
        });

        sSetBackMinutes.setLayoutData(gridData9);
        sSetBackMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(0, 59, sSetBackMinutes);
                getShell().setDefaultButton(bApplySetback);
                bApplySetback.setEnabled(true);
            }
        });

        sSetBackSeconds.setLayoutData(gridData10);
        sSetBackSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            @Override
			public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
                if (Utils.str2int(sSetBackHours.getText()) > 0 || Utils.str2int(sSetBackMinutes.getText()) > 0) {
                    Utils.setBackground(0, 59, sSetBackSeconds);
                }
                else {
                    sSetBackSeconds.setBackground(null);
                }
                getShell().setDefaultButton(bApplySetback);
                bApplySetback.setEnabled(true);
            }
        });
    }

    private void createTable() {
        tSetback = SOSJOEMessageCodes.JOE_Tbl_JobOptionsForm_ErrorDelay.Control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
        tSetback.setSortDirection(SWT.UP);
        tSetback.setHeaderVisible(true);
        tSetback.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 10, 3));
        tSetback.setLinesVisible(true);
        tSetback.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                if (tSetback.getSelectionCount() > 0) {
                    objJobDataProvider.selectSetbackDelay(tSetback.getSelectionIndex());
                    initSetback(true);
                    sSetBackCount.selectAll();
                }
                else
                    initSetback(false);
                bRemoveSetback.setEnabled(tSetback.getSelectionCount() > 0);
            }
        });

        TableColumn tableColumn2 = SOSJOEMessageCodes.JOE_TCl_JobOptionsForm_SetBackCount.Control(new TableColumn(tSetback, SWT.NONE));
        tSetback.setSortColumn(tableColumn2);
        tableColumn2.setWidth(150);

        TableColumn tableColumn3 = SOSJOEMessageCodes.JOE_TCl_JobOptionsForm_IsMax.Control(new TableColumn(tSetback, SWT.NONE));
        tableColumn3.setWidth(80);

        TableColumn tableColumn4 = SOSJOEMessageCodes.JOE_TCl_JobOptionsForm_Delayhhmmss.Control(new TableColumn(tSetback, SWT.NONE));
        tableColumn4.setWidth(250);
    }

    private void initSetback(final boolean enabled) {
        sSetBackCount.setEnabled(enabled);
        bIsMaximum.setEnabled(enabled);
        sSetBackHours.setEnabled(enabled);
        sSetBackMinutes.setEnabled(enabled);
        sSetBackSeconds.setEnabled(enabled);

        if (enabled) {
            bIsMaximum.setSelection(objJobDataProvider.isMaximum());
            /*
                        if (bIsMaximum.getSelection()) {
                            sSetBackHours.setEnabled(false);
                            sSetBackMinutes.setEnabled(false);
                            sSetBackSeconds.setEnabled(false);
                        } else {*/
            sSetBackHours.setText(Utils.fill(2, objJobDataProvider.getSetbackCountHours()));
            sSetBackMinutes.setText(Utils.fill(2, objJobDataProvider.getSetbackCountMinutes()));
            if (!(objJobDataProvider.getSetbackCountHours() + objJobDataProvider.getSetbackCountMinutes()).equals("")) {
                sSetBackSeconds.setText(Utils.fill(2, objJobDataProvider.getSetbackCountSeconds()));
            }
            else {
                sSetBackSeconds.setText(objJobDataProvider.getSetbackCountSeconds());
            }
            // }
            sSetBackCount.setText(objJobDataProvider.getSetbackCount());
        }

        bApplySetback.setEnabled(false);
    }

    private void sortTable(final Table t) {

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

    private void applySetback() {
        int maximum = 0;
        int maximumMax = 0;
        int maxSetback = 0;

        int sel = tSetback.getSelectionIndex();
        TableItem[] setback = tSetback.getItems();

        if (sSetBackCount.getText().equals("0"))
            MainWindow.message(SOSJOEMessageCodes.JOE_M_ZeroNotAllowed.label(), SWT.ICON_INFORMATION);
        else {

            for (int i = 0; i < setback.length; i++) {

                if (setback[i].getText(1).equalsIgnoreCase("Yes") && sel != i) {
                    maximum = maximum + 1;
                }
            }

            if (bIsMaximum.getSelection())
                maximum = maximum + 1;

            boolean found = false;
            for (int i = 0; i < setback.length; i++) {
                if (setback[i].getText(0).equals(sSetBackCount.getText()) && sel != i) {
                    found = true;
                }
            }

            for (int i = 0; i < setback.length; i++) {
                if (i != sel && maximumMax < Utils.str2int(setback[i].getText(0)) && setback[i].getText(1).equalsIgnoreCase("yes")) {
                    maximumMax = Utils.str2int(setback[i].getText(0));
                }
            }

            for (int i = 0; i < setback.length; i++) {
                if (i != sel && !setback[i].getText(1).equalsIgnoreCase("yes") && maxSetback < Utils.str2int(setback[i].getText(0))) {
                    maxSetback = Utils.str2int(setback[i].getText(0));
                }
            }

            if (bIsMaximum.getSelection() && Utils.str2int(sSetBackCount.getText()) > maximumMax) {
                maximumMax = Utils.str2int(sSetBackCount.getText());
            }

            if (Utils.str2int(sSetBackCount.getText()) > maxSetback) {
                maxSetback = Utils.str2int(sSetBackCount.getText());
            }

            if (maximum > 1) {
                MainWindow.message(SOSJOEMessageCodes.JOE_M_0032.label(), SWT.ICON_INFORMATION);
                sSetBackCount.setFocus();
            }
            else {
                if (found) {
                    MainWindow.message(SOSJOEMessageCodes.JOE_M_0033.label(), SWT.ICON_INFORMATION);
                    sSetBackCount.setFocus();
                }
                else {
                    if (sSetBackCount.getText().equals("")) {
                        MainWindow.message(SOSJOEMessageCodes.JOE_M_0034.label(), SWT.ICON_INFORMATION);
                        sSetBackCount.setFocus();
                    }
                    else {
                        if (maximumMax > 0 && maximumMax < Utils.str2int(sSetBackCount.getText()) || maxSetback > Utils.str2int(sSetBackCount.getText()) && bIsMaximum.getSelection()) {
                            MainWindow.message(SOSJOEMessageCodes.JOE_M_0035.label(), SWT.ICON_INFORMATION);
                            sSetBackCount.setFocus();
                        }
                        else {
                            String delay = sSetBackSeconds.getText();
                            if (!(sSetBackMinutes.getText() + sSetBackHours.getText()).equals("")) {
                                delay = Utils.getTime(sSetBackHours.getText(), sSetBackMinutes.getText(), sSetBackSeconds.getText(), true);

                            }

                            if (delay.equals("00") || delay.equals(""))
                                delay = "0";

                            objJobDataProvider.applySetbackDelay(sSetBackCount.getText(), bIsMaximum.getSelection(), delay);
                            objJobDataProvider.fillSetbacks(tSetback);
                            initSetback(false);
                            getShell().setDefaultButton(null);
                            sortTable(tSetback);
                            objJobDataProvider.newSetbacks(tSetback);
                        }
                    }
                }
            }

        }
    }

}
