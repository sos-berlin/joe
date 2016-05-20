package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.conf.listeners.SecurityListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class SecurityForm extends SOSJOEMessageCodes implements IUnsaved {

    private SecurityListener listener = null;
    private Group group = null;
    private Table table = null;
    private Button cIgnoreUnknownHosts = null;
    private Label lblHost = null;
    private Text tHost = null;
    private Label label4 = null;
    private Button bApply = null;
    private Button bRemove = null;
    private Button bNew = null;
    private CCombo cLevel = null;
    private Label label1 = null;
    private Label label2 = null;

    public SecurityForm(Composite parent, int style, SchedulerDom dom, Element config) throws JDOMException {
        super(parent, style);
        listener = new SecurityListener(dom, config);
        initialize();
        listener.fillTable(table);
        cIgnoreUnknownHosts.setSelection(listener.getIgnoreUnknownHosts());
        cLevel.setItems(listener.getLevels());
    }

    public void apply() {
        if (isUnsaved()) {
            applyHost();
        }
    }

    public boolean isUnsaved() {
        return bApply.isEnabled();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(611, 355));
    }

    private void createGroup() {
        GridData gridData8 = new org.eclipse.swt.layout.GridData();
        gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData7 = new org.eclipse.swt.layout.GridData();
        gridData7.horizontalSpan = 5;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData7.heightHint = 10;
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.widthHint = 80;
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalSpan = 5;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group = JOE_G_SecurityForm_Security.control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout);
        cIgnoreUnknownHosts = JOE_B_SecurityForm_IgnoreUnkownHosts.control(new Button(group, SWT.CHECK));
        cIgnoreUnknownHosts.setLayoutData(gridData1);
        cIgnoreUnknownHosts.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setIgnoreUnknownHosts(cIgnoreUnknownHosts.getSelection());
            }
        });
        lblHost = JOE_L_SecurityForm_Host.control(new Label(group, SWT.NONE));
        lblHost.setLayoutData(new org.eclipse.swt.layout.GridData());
        tHost = JOE_T_SecurityForm_Host.control(new Text(group, SWT.BORDER));
        tHost.setEnabled(false);
        tHost.setLayoutData(gridData5);
        tHost.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(tHost.getText())) {
                    applyHost();
                }
            }
        });
        tHost.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!"".equals(tHost.getText()));
            }
        });
        label4 = JOE_L_SecurityForm_AccessLevel.control(new Label(group, SWT.NONE));
        cLevel = JOE_Cbo_SecurityForm_AccessLevel.control(new CCombo(group, SWT.BORDER | SWT.READ_ONLY));
        cLevel.setEditable(false);
        cLevel.setLayoutData(gridData6);
        cLevel.setEnabled(false);
        cLevel.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !"".equals(tHost.getText())) {
                    applyHost();
                }
            }
        });
        cLevel.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!"".equals(tHost.getText()));
            }
        });
        bApply = JOE_B_SecurityForm_ApplyHost.control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData3);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyHost();
            }
        });
        label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setLayoutData(gridData7);
        createTable();
        bNew = JOE_B_SecurityForm_NewHost.control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData2);
        getShell().setDefaultButton(bNew);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newHost();
                setInput(true);
            }
        });
        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setLayoutData(gridData8);
        bRemove = JOE_B_SecurityForm_RemoveHost.control(new Button(group, SWT.NONE));
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData4);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int index = table.getSelectionIndex();
                    listener.removeHost(index);
                    table.remove(index);
                    if (index >= table.getItemCount()) {
                        index--;
                    }
                    if (table.getItemCount() > 0) {
                        table.select(index);
                        listener.selectHost(index);
                        setInput(true);
                    } else {
                        setInput(false);
                    }
                }
                bRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
    }

    private void createTable() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 4;
        gridData.verticalSpan = 3;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        table = JOE_Tbl_SecurityForm_Hosts.control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
        table.setHeaderVisible(true);
        table.setLayoutData(gridData);
        table.setLinesVisible(true);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean selection = table.getSelectionCount() > 0;
                bRemove.setEnabled(selection);
                if (selection) {
                    listener.selectHost(table.getSelectionIndex());
                    setInput(true);
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_SecurityForm_Host.control(new TableColumn(table, SWT.NONE));
        tableColumn.setWidth(250);
        TableColumn tableColumn1 = JOE_TCl_SecurityForm_AccessLevel.control(new TableColumn(table, SWT.NONE));
        tableColumn1.setWidth(200);
    }

    private void applyHost() {
        listener.applyHost(tHost.getText(), cLevel.getText());
        listener.fillTable(table);
        setInput(false);
        getShell().setDefaultButton(bNew);
    }

    private void setInput(boolean enabled) {
        tHost.setEnabled(enabled);
        cLevel.setEnabled(enabled);
        if (enabled) {
            tHost.setText(listener.getHost());
            int index = listener.getLevelIndex(listener.getLevel());
            if (index == -1) {
                index = 0;
                listener.applyHost(listener.getHost(), cLevel.getItem(index));
            }
            cLevel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            cLevel.select(index);
            tHost.setFocus();
        } else {
            tHost.setText("");
            cLevel.setBackground(null);
            cLevel.select(0);
        }
        bApply.setEnabled(false);
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }

}