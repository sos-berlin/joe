/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;

/**
 * @author sky2000
 */
public class ProcessClassesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ProcessClassesListener listener;

    private Group                  group         = null;

    private Table                  table         = null;

    private Label                  label1        = null;

    private Button                 bRemove       = null;

    private Button                 bNew          = null;

    private Button                 bApply        = null;

    private Text                   tProcessClass = null;

    private Label                  label5        = null;

    private Spinner                sMaxProcesses = null;

    private Label                  label10       = null;

    private Text                   tSpoolerID    = null;

    private Label                  label         = null;

    private Label                  label2        = null;


    /**
     * @param parent
     * @param style
     * @throws JDOMException
     */
    public ProcessClassesForm(Composite parent, int style, SchedulerDom dom, Element config) throws JDOMException {
        super(parent, style);
        listener = new ProcessClassesListener(dom, config);

        initialize();
        setToolTipText();

        listener.fillTable(table);
    }


    public void apply() {
        if (isUnsaved())
            applyClass();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(694, 294));

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData8 = new org.eclipse.swt.layout.GridData();
        gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData7 = new org.eclipse.swt.layout.GridData();
        gridData7.horizontalSpan = 7;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData7.heightHint = 10;
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData4 = new GridData(20, SWT.DEFAULT);
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 7;
        group = new Group(this, SWT.NONE);
        group.setText("Process Classes");
        group.setLayout(gridLayout);
        label1 = new Label(group, SWT.NONE);
        label1.setText("Process Class:");
        tProcessClass = new Text(group, SWT.BORDER);
        label5 = new Label(group, SWT.NONE);
        final GridData gridData = new GridData();
        gridData.horizontalIndent = 5;
        label5.setLayoutData(gridData);
        label5.setText("Max Processes:");
        sMaxProcesses = new Spinner(group, SWT.NONE);
        label10 = new Label(group, SWT.NONE);
        label10.setText("Scheduler ID:");
        tSpoolerID = new Text(group, SWT.BORDER);
        bApply = new Button(group, SWT.NONE);
        label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData7);
        createTable();
        bNew = new Button(group, SWT.NONE);
        bNew.setText("&New Process Class");
        bNew.setLayoutData(gridData1);
        getShell().setDefaultButton(bNew);

        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData8);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                apply();
                listener.newProcessClass();
                setInput(true);
                bApply.setEnabled(listener.isValidClass(tProcessClass.getText()));
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove Process Class");
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData2);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    int index = table.getSelectionIndex();
                    listener.removeProcessClass(index);
                    table.remove(index);
                    if (index >= table.getItemCount())
                        index--;
                    if (table.getItemCount() > 0) {
                        table.select(index);
                        listener.selectProcessClass(index);
                        setInput(true);
                    } else
                        setInput(false);
                }
                bRemove.setEnabled(table.getSelectionCount() > 0);
                tProcessClass.setBackground(null);
            }
        });
        tProcessClass.setLayoutData(gridData5);
        tProcessClass.setEnabled(false);
        tProcessClass.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR)
                    applyClass();
            }
        });
        tProcessClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = listener.isValidClass(tProcessClass.getText());
                if (valid)
                    tProcessClass.setBackground(null);
                else
                    tProcessClass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                bApply.setEnabled(valid);
            }
        });
        sMaxProcesses.setMaximum(99999999);
        sMaxProcesses.setLayoutData(gridData4);
        sMaxProcesses.setEnabled(false);
        sMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR)
                    applyClass();
            }
        });
        sMaxProcesses.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
            }
        });
        tSpoolerID.setLayoutData(gridData6);
        tSpoolerID.setEnabled(false);
        tSpoolerID.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR)
                    applyClass();
            }
        });
        tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
            }
        });
        bApply.setText("&Apply Process Class");
        bApply.setLayoutData(gridData3);
        bApply.setEnabled(false);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyClass();
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
        gridData.horizontalSpan = 6;
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
                    listener.selectProcessClass(table.getSelectionIndex());
                    setInput(true);
                    tProcessClass.setBackground(null);
                }
            }
        });
        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(200);
        tableColumn.setText("Process Class");
        TableColumn tableColumn1 = new TableColumn(table, SWT.NONE);
        tableColumn1.setWidth(150);
        tableColumn1.setText("Max Processes");
        TableColumn tableColumn2 = new TableColumn(table, SWT.NONE);
        tableColumn2.setWidth(150);
        tableColumn2.setText("Scheduler ID");
    }


    private void applyClass() {
        listener.applyProcessClass(tProcessClass.getText(), sMaxProcesses.getSelection(), tSpoolerID.getText());
        listener.fillTable(table);
        setInput(false);
        getShell().setDefaultButton(bNew);
        tProcessClass.setBackground(null);
    }


    private void setInput(boolean enabled) {
        tProcessClass.setEnabled(enabled);
        sMaxProcesses.setEnabled(enabled);
        tSpoolerID.setEnabled(enabled);
        if (enabled) {
            tProcessClass.setText(listener.getProcessClass());
            sMaxProcesses.setSelection(listener.getMaxProcesses());
            tSpoolerID.setText(listener.getSpoolerID());
            tProcessClass.setFocus();
        } else {
            tProcessClass.setText("");
            sMaxProcesses.setSelection(0);
            tSpoolerID.setText("");
        }
        bApply.setEnabled(false);
        bRemove.setEnabled(table.getSelectionCount() > 0);
        // tProcessClass.setBackground(null);
    }


    public void setToolTipText() {
        bNew.setToolTipText(Messages.getTooltip("process_classes.btn_new_class"));
        bRemove.setToolTipText(Messages.getTooltip("process_classes.btn_remove_class"));
        tProcessClass.setToolTipText(Messages.getTooltip("process_classes.class_entry"));
        sMaxProcesses.setToolTipText(Messages.getTooltip("process_classes.max_processes_entry"));
        tSpoolerID.setToolTipText(Messages.getTooltip("process_classes.spooler_id_entry"));
        bApply.setToolTipText(Messages.getTooltip("process_classes.btn_apply"));
        table.setToolTipText(Messages.getTooltip("process_classes.table"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
