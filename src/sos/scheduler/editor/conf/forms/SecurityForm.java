/**
 * 
 */
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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SecurityListener;

/**
 * @author sky2000
 */
public class SecurityForm extends Composite implements IUnsaved, IUpdateLanguage {
	
    private SecurityListener listener            = null;

    private Group            group               = null;

    private Table            table               = null;

    private Button           cIgnoreUnknownHosts = null;

    private Label            label               = null;

    private Text             tHost               = null;

    private Label            label4              = null;

    private Button           bApply              = null;

    private Button           bRemove             = null;

    private Button           bNew                = null;

    private CCombo           cLevel              = null;

    private Label            label1              = null;

    private Label            label2              = null;


    /**
     * @param parent
     * @param style
     * @throws JDOMException
     */
    public SecurityForm(Composite parent, int style, SchedulerDom dom, Element config) throws JDOMException {
        super(parent, style);
        listener = new SecurityListener(dom, config);

        initialize();
        setToolTipText();

        listener.fillTable(table);
        cIgnoreUnknownHosts.setSelection(listener.getIgnoreUnknownHosts());
        cLevel.setItems(listener.getLevels());
    }


    public void apply() {
        if (isUnsaved())
            applyHost();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(611, 355));

    }


    /**
     * This method initializes group
     */
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
        group = new Group(this, SWT.NONE);
        group.setText("Security");
        group.setLayout(gridLayout);
        cIgnoreUnknownHosts = new Button(group, SWT.CHECK);
        cIgnoreUnknownHosts.setText("Ignore unknown hosts");
        cIgnoreUnknownHosts.setLayoutData(gridData1);
        label = new Label(group, SWT.NONE);
        tHost = new Text(group, SWT.BORDER);
        label4 = new Label(group, SWT.NONE);
        label4.setText("Access Level:");
        cLevel = new CCombo(group, SWT.BORDER | SWT.READ_ONLY);
        bApply = new Button(group, SWT.NONE);
        label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setText("Label");
        label1.setLayoutData(gridData7);
        cIgnoreUnknownHosts.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setIgnoreUnknownHosts(cIgnoreUnknownHosts.getSelection());
            }
        });
        createTable();
        bNew = new Button(group, SWT.NONE);
        bNew.setLayoutData(gridData2);
        bNew.setText("&New Host");
        getShell().setDefaultButton(bNew);
        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData8);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newHost();
                setInput(true);
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setEnabled(false);
        bRemove.setText("Remove Host");
        bRemove.setLayoutData(gridData4);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int index = table.getSelectionIndex();
                    listener.removeHost(index);
                    table.remove(index);
                    if (index >= table.getItemCount())
                        index--;
                    if (table.getItemCount() > 0) {
                        table.select(index);
                        listener.selectHost(index);
                        setInput(true);
                    } else
                        setInput(false);
                }
                bRemove.setEnabled(table.getSelectionCount() > 0);
            }
        });
        label.setText("Host:");
        label.setLayoutData(new org.eclipse.swt.layout.GridData());
        tHost.setEnabled(false);
        tHost.setLayoutData(gridData5);
        tHost.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tHost.getText().equals(""))
                    applyHost();
            }
        });
        tHost.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tHost.getText().equals(""));
            }
        });
        cLevel.setEditable(false);
        cLevel.setLayoutData(gridData6);
        cLevel.setEnabled(false);
        cLevel.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR && !tHost.getText().equals(""))
                    applyHost();
            }
        });
        cLevel.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(!tHost.getText().equals(""));
            }
        });
        bApply.setText("&Apply Host");
        bApply.setLayoutData(gridData3);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyHost();
            }
        });
    }


    /**
     * This method initializes table
     */
    private void createTable() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 4;
        gridData.verticalSpan = 3;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        table = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
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
        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(250);
        tableColumn.setText("Host");
        TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
        tableColumn1.setWidth(200);
        tableColumn1.setText("Access Level");
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


    public void setToolTipText() {
        cIgnoreUnknownHosts.setToolTipText(Messages.getTooltip("security.ignore_unknown_hosts"));
        bNew.setToolTipText(Messages.getTooltip("security.btn_new_host"));
        bRemove.setToolTipText(Messages.getTooltip("security.btn_remove_host"));
        tHost.setToolTipText(Messages.getTooltip("security.host_entry"));
        cLevel.setToolTipText(Messages.getTooltip("security.level_choice"));
        bApply.setToolTipText(Messages.getTooltip("security.btn_apply"));
        table.setToolTipText(Messages.getTooltip("security.table"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
