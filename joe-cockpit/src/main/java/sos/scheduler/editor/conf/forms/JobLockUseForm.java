package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobLockUseListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class JobLockUseForm extends SOSJOEMessageCodes implements IUnsaved {

    private Combo tLockUse = null;
    private JobLockUseListener listener = null;
    private Group group1 = null;
    private Label lockLabel = null;
    private Label label11 = null;
    private Button bApplyLockUse = null;
    private Table tLockUseTable = null;
    private Button bNewLockUse = null;
    private Button bRemoveLockUse = null;
    private Button bExclusive = null;
    private Button butBrowse = null;

    public JobLockUseForm(Composite parent, int style, SchedulerDom dom, Element job) {
        super(parent, style);
        listener = new JobLockUseListener(dom, job);
        initialize();
        initLockUseTable(true);
        group1.setEnabled(Utils.isElementEnabled("job", dom, job));
    }

    public void apply() {
        if (isUnsaved()) {
            applyLockUse();
        }
    }

    public boolean isUnsaved() {
        return bApplyLockUse.isEnabled();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(678, 425));
    }

    private void createGroup() {
        GridData gridData51 = new org.eclipse.swt.layout.GridData();
        gridData51.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData51.widthHint = 90;
        gridData51.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5;
        group1 = JOE_G_JobLockUseForm_Use.control(new Group(this, SWT.NONE));
        group1.setLayout(gridLayout1);
        lockLabel = JOE_L_JobLockUseForm_Lock.control(new Label(group1, SWT.NONE));
        tLockUse = JOE_Cbo_JobLockUseForm_LockUse.control(new Combo(group1, SWT.NONE));
        tLockUse.setEnabled(false);
        tLockUse.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!"".equals(tLockUse.getText())) {
                    getShell().setDefaultButton(bApplyLockUse);
                }
                bApplyLockUse.setEnabled(listener.isValidLock(tLockUse.getText()));
            }
        });
        tLockUse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        label11 = JOE_L_JobLockUseForm_Exclusive.control(new Label(group1, SWT.NONE));
        label11.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        bExclusive = JOE_B_JobLockUseForm_Exclusive.control(new Button(group1, SWT.CHECK));
        bExclusive.setSelection(true);
        bExclusive.setEnabled(true);
        bExclusive.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                bApplyLockUse.setEnabled(true);
            }
        });
        bApplyLockUse = JOE_B_JobLockUseForm_ApplyLockUse.control(new Button(group1, SWT.NONE));
        bApplyLockUse.setEnabled(false);
        bApplyLockUse.setLayoutData(gridData51);
        bApplyLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyLockUse();
            }
        });
        new Label(group1, SWT.NONE);
        GridData gridData30 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 3);
        this.tLockUseTable = JOE_Tbl_JobLockUseForm_LockUseTable.control(new Table(group1, SWT.BORDER | SWT.FULL_SELECTION));
        this.tLockUseTable.setHeaderVisible(true);
        this.tLockUseTable.setLayoutData(gridData30);
        this.tLockUseTable.setLinesVisible(true);
        this.tLockUseTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tLockUseTable.getSelectionCount() > 0) {
                    listener.selectLockUse(tLockUseTable.getSelectionIndex());
                    initLockUse(true);
                } else {
                    initLockUse(false);
                }
                bRemoveLockUse.setEnabled(tLockUseTable.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn5 = JOE_TCl_JobLockUseForm_Lock.control(new TableColumn(this.tLockUseTable, SWT.NONE));
        tableColumn5.setWidth(300);
        TableColumn tableColumn6 = JOE_TCl_JobLockUseForm_Exclusive.control(new TableColumn(this.tLockUseTable, SWT.NONE));
        tableColumn6.setWidth(70);
        GridData gridData41 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
        bNewLockUse = JOE_B_JobLockUseForm_NewLockUse.control(new Button(group1, SWT.NONE));
        bNewLockUse.setLayoutData(gridData41);
        bNewLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tLockUseTable.deselectAll();
                listener.newLockUse();
                initLockUse(true);
                tLockUse.setFocus();
            }
        });
        new Label(group1, SWT.NONE);
        GridData gridData31 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemoveLockUse = JOE_B_JobLockUseForm_RemoveLockUse.control(new Button(group1, SWT.NONE));
        bRemoveLockUse.setEnabled(false);
        bRemoveLockUse.setLayoutData(gridData31);
        bRemoveLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tLockUseTable.getSelectionCount() > 0) {
                    int index = tLockUseTable.getSelectionIndex();
                    listener.deleteLockUse(index);
                    tLockUseTable.remove(index);
                    if (index >= tLockUseTable.getItemCount()) {
                        index--;
                    }
                    if (tLockUseTable.getItemCount() > 0) {
                        tLockUseTable.setSelection(index);
                        listener.selectLockUse(index);
                        initLockUse(true);
                    } else {
                        initLockUse(false);
                        bRemoveLockUse.setEnabled(false);
                    }
                }
            }
        });
        new Label(group1, SWT.NONE);
        butBrowse = JOE_B_JobLockUseForm_Browse.control(new Button(group1, SWT.NONE));
        butBrowse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_LOCK);
                tLockUse.setEnabled(true);
                if (name != null && !name.isEmpty()) {
                    tLockUseTable.deselectAll();
                    listener.newLockUse();
                    initLockUse(true);
                    tLockUse.setFocus();
                    tLockUse.setText(name);
                }
            }
        });
        butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
    }

    private void initLockUseTable(boolean enabled) {
        tLockUseTable.setEnabled(enabled);
        bNewLockUse.setEnabled(true);
        initLockUse(false);
        listener.fillLockUse(tLockUseTable);
        String[] locks = listener.getLocks();
        if (locks != null) {
            tLockUse.setItems(locks);
        }
    }

    private void initLockUse(boolean enabled) {
        tLockUse.setEnabled(enabled);
        bExclusive.setEnabled(enabled);
        if (enabled) {
            bExclusive.setSelection(listener.getExclusive());
            tLockUse.setText(listener.getLockUse());
        } else {
            tLockUse.setText("");
            bExclusive.setSelection(true);
        }
        bApplyLockUse.setEnabled(false);
    }

    private void applyLockUse() {
        listener.applyLockUse(tLockUse.getText(), bExclusive.getSelection());
        listener.fillLockUse(this.tLockUseTable);
        initLockUse(false);
        getShell().setDefaultButton(null);
    }

}