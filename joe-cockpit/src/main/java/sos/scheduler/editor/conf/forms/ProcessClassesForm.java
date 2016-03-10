/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.IntegerField;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

/** @author sky2000 */
public class ProcessClassesForm extends SOSJOEMessageCodes implements IUnsaved {

    private ProcessClassesListener listener = null;
    // final String JOE_L_at_port = "JOE_L_at_port"; // "at Port";
    // final String JOE_L_Apply = "JOE_L_Apply"; // "Apply";
    // final String JOE_L_Remove_Process_Class = "JOE_L_Remove_Process_Class";
    // // "Remove Process Class";
    private Group group;
    private Table tableRemoteScheduler = null;
    private static Table tableProcessClasses = null;
    private Button btRemove = null;
    private Button btNew = null;
    private Button btNewRemoteScheduler = null;
    private Button btApply = null;
    private Button btOkRemoteScheduler = null;
    private Button btRemoveRemoteScheduler = null;
    private Text tProcessClass = null;
    private Text tMaxProcesses = null;
    private Label label = null;
    private Text tRemoteHost = null;
    private Text tRemotePort = null;
    private Text tRemoteSchedulerHost = null;
    private Text tRemoteSchedulerPort = null;
    private SchedulerDom dom = null;

    /** @param parent
     * @param style
     * @throws JDOMException */
    public ProcessClassesForm(Composite parent, int style, SchedulerDom dom_, Element config) throws JDOMException {
        super(parent, style);
        dom = dom_;
        listener = new ProcessClassesListener(dom, config);
        initialize();
    }

    public void apply() {
        if (isUnsaved())
            applyClass();
    }

    public boolean isUnsaved() {
        return btApply.isEnabled();
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(694, 294));
        if (dom.isLifeElement()) {
            if (tableProcessClasses.getItemCount() > 0)
                tableProcessClasses.setSelection(0);
            listener.selectProcessClass(0);
            setInput(true);
            tProcessClass.setBackground(null);
            setEnabled(true);
            tableProcessClasses.setVisible(false);
            btNew.setVisible(false);
            btRemove.setVisible(false);
            label.setVisible(false);
        }
        listener.fillProcessClassesTable(tableProcessClasses);
        new Label(group, SWT.NONE);

    }

    /** This method initializes group */
    private void createGroup() {

        group = JOE_G_ProcessClassesForm_ProcessClasses.Control(new Group(this, SWT.NONE));
        group.setLayout(new GridLayout(5, false));

        Label lbProcessClass = JOE_L_ProcessClassesForm_ProcessClass.Control(new Label(group, SWT.NONE));
        lbProcessClass.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        tProcessClass = JOE_T_ProcessClassesForm_ProcessClass.Control(new Text(group, SWT.BORDER));
        tProcessClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
        tProcessClass.setEnabled(false);

        tProcessClass.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                if (!listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement()) {
                    e.doit = false;
                    return;
                }
                traversed(e);
            }
        });
        tProcessClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement();
                if (valid)
                    tProcessClass.setBackground(null);
                else
                    tProcessClass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                btApply.setEnabled(valid);
            }
        });

        btApply = JOE_B_ProcessClassesForm_Apply.Control(new Button(group, SWT.NONE));
        btApply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btApply.setEnabled(false);
        btApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyClass();
            }
        });

        Label lbMaxProcesses = JOE_L_ProcessClassesForm_MaxProcesses.Control(new Label(group, SWT.NONE));
        lbMaxProcesses.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        final Label lbHost = JOE_L_ProcessClassesForm_remoteExecution.Control(new Label(group, SWT.NONE));
        lbHost.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        new Label(group, SWT.NONE);

        final Label lbPort = JOE_L_ProcessClassesForm_Port.Control(new Label(group, SWT.NONE));
        lbPort.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        new Label(group, SWT.NONE);

        tMaxProcesses = JOE_T_ProcessClassesForm_MaxProcesses.Control(new IntegerField(group, SWT.BORDER));
        tMaxProcesses.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false, 1, 1));
        tMaxProcesses.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                btApply.setEnabled(true);
            }
        });
        tMaxProcesses.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tMaxProcesses.setEnabled(false);
        tMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    applyClass();
                    btNew.setEnabled(!btApply.getEnabled());
                }
            }
        });

        tRemoteHost = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
        tRemoteHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        tRemoteHost.setEnabled(false);

        tRemoteHost.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tRemoteHost.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });

        Label label = new Label(group, SWT.NONE);
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setText(":");

        tRemotePort = JOE_T_ProcessClassesForm_Port.Control(new IntegerField(group, SWT.BORDER));
        tRemotePort.setEnabled(false);
        tRemotePort.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

        tRemotePort.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tRemotePort.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });

        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        createTableRemoteScheduler();

        GridData gridData3 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
        gridData3.widthHint = 100;
        btNewRemoteScheduler = JOE_B_ProcessClassesForm_NewRemoteScheduler.Control(new Button(group, SWT.NONE));
        btNewRemoteScheduler.setLayoutData(gridData3);
        btNewRemoteScheduler.setEnabled(false);
        btNewRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableRemoteScheduler.setSelection(-1);
                tRemoteSchedulerHost.setText("");
                tRemoteSchedulerPort.setText("");
                tRemoteSchedulerHost.setFocus();
                btNewRemoteScheduler.setEnabled(false);
            }
        });

        Label lbSeperator = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbSeperator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

        GridData gridData4 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
        gridData4.widthHint = 100;
        btRemoveRemoteScheduler = JOE_B_ProcessClassesForm_RemoveRemoteScheduler.Control(new Button(group, SWT.NONE));
        btRemoveRemoteScheduler.setLayoutData(gridData4);
        btRemoveRemoteScheduler.setEnabled(false);

        btRemoveRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableRemoteScheduler.getSelectionCount() > 0) {
                    int index = tableRemoteScheduler.getSelectionIndex();
                    tableRemoteScheduler.remove(index);
                    tableRemoteScheduler.setSelection(-1);
                    tRemoteSchedulerHost.setText("");
                    tRemoteSchedulerPort.setText("");
                    tRemoteSchedulerHost.setFocus();
                    btRemoveRemoteScheduler.setEnabled(false);
                    btApply.setEnabled(true);
                }
                btRemoveRemoteScheduler.setEnabled(tableRemoteScheduler.getSelectionCount() > 0);
            }
        });

        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        Label lbSchedulerRemoteHost = new Label(group, SWT.NONE);
        lbSchedulerRemoteHost.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerRemoteHost.setText("Host");
        new Label(group, SWT.NONE);

        Label lbSchedulerRemotePort = new Label(group, SWT.NONE);
        lbSchedulerRemotePort.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerRemotePort.setText("Port");
        new Label(group, SWT.NONE);

        GridData gridData2 = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gridData2.widthHint = 100;
        btOkRemoteScheduler = JOE_B_ProcessClassesForm_ApplyRemoteScheduler.Control(new Button(group, SWT.NONE));
        btOkRemoteScheduler.setLayoutData(gridData2);
        btOkRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyRemoteSchedulerEntry();
            }
        });

        tRemoteSchedulerHost = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
        tRemoteSchedulerHost.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
        tRemoteSchedulerHost.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });

        Label lblNewLabel_2 = new Label(group, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText(":");

        tRemoteSchedulerPort = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
        tRemoteSchedulerPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        tRemoteSchedulerPort.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });
        new Label(group, SWT.NONE);

        // newline
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        createTableProcessClasses();

        btNew = JOE_B_ProcessClassesForm_NewProcessClass.Control(new Button(group, SWT.NONE));
        btNew.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        getShell().setDefaultButton(btNew);
        btNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newProcessClass();
                setInput(true);
                btApply.setEnabled(listener.isValidClass(tProcessClass.getText()));
            }
        });
        Label lbSeperator2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbSeperator2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        btRemove = JOE_B_ProcessClassesForm_RemoveProcessClass.Control(new Button(group, SWT.NONE));
        btRemove.setEnabled(false);
        btRemove.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        btRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableProcessClasses.getSelectionCount() > 0) {
                    if (Utils.checkElement(tableProcessClasses.getSelection()[0].getText(0), dom, JOEConstants.PROCESS_CLASSES, null)) {
                        int index = tableProcessClasses.getSelectionIndex();
                        listener.removeProcessClass(index);
                        tableProcessClasses.remove(index);
                        if (index >= tableProcessClasses.getItemCount())
                            index--;
                        if (tableProcessClasses.getItemCount() > 0) {
                            tableProcessClasses.select(index);
                            listener.selectProcessClass(index);
                            setInput(true);
                        } else
                            setInput(false);
                    }
                }
                btRemove.setEnabled(tableProcessClasses.getSelectionCount() > 0);
                tProcessClass.setBackground(null);
                // bNew.setEnabled(true);
            }
        });

    }

    private void applyRemoteSchedulerEntry() {
        if (tRemoteSchedulerHost.getText().length() > 0 && tRemoteSchedulerPort.getText().length() > 0) {
            if (tableRemoteScheduler.getSelectionIndex() >= 0) {
                TableItem item = tableRemoteScheduler.getItems()[tableRemoteScheduler.getSelectionIndex()];
                item.setText(0, tRemoteSchedulerHost.getText());
                item.setText(1, tRemoteSchedulerPort.getText());
                tRemoteSchedulerHost.setText("");
                tRemoteSchedulerPort.setText("");
                tRemoteSchedulerHost.setFocus();
                btApply.setEnabled(true);
            } else {
                for (int i = 0; i < tableRemoteScheduler.getItemCount(); i++) {
                    TableItem item = tableRemoteScheduler.getItems()[i];
                    if ((item.getText(0).equals(tRemoteSchedulerHost.getText()) && tRemoteSchedulerHost.getText().length() > 0 && (item.getText(1).equals(tRemoteSchedulerPort.getText())))) {
                        item.setText(0, tRemoteSchedulerHost.getText());
                        item.setText(1, tRemoteSchedulerPort.getText());
                        tRemoteSchedulerHost.setText("");
                        tRemoteSchedulerPort.setText("");
                        tRemoteSchedulerHost.setFocus();

                        btApply.setEnabled(true);
                    }
                }
                if (tRemoteSchedulerHost.getText().length() > 0) {

                    TableItem item = new TableItem(tableRemoteScheduler, SWT.NONE);
                    item.setText(0, tRemoteSchedulerHost.getText());
                    item.setText(1, tRemoteSchedulerPort.getText());
                    tRemoteSchedulerHost.setText("");
                    tRemoteSchedulerPort.setText("");
                    tRemoteSchedulerHost.setFocus();

                    btApply.setEnabled(true);
                }
            }
        }
    }

    /** This method initializes table */
    private void createTableRemoteScheduler() {

        tableRemoteScheduler = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
        tableRemoteScheduler.setHeaderVisible(true);
        tableRemoteScheduler.setLinesVisible(true);
        GridData gd_table = new GridData(SWT.FILL, SWT.TOP, true, false, 4, 4);
        gd_table.heightHint = 112;
        tableRemoteScheduler.setLayoutData(gd_table);
        tableRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableRemoteScheduler.getSelectionIndex() >= 0) {
                    TableItem item = tableRemoteScheduler.getItems()[tableRemoteScheduler.getSelectionIndex()];
                    tRemoteSchedulerHost.setText(item.getText(0));
                    tRemoteSchedulerPort.setText(item.getText(1));
                    btRemoveRemoteScheduler.setEnabled(true);
                    btNewRemoteScheduler.setEnabled(true);
                }

            }
        });
        TableColumn tableColumnHost = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnHost.setWidth(400);
        tableColumnHost.setText("Host");

        TableColumn tableColumnPort = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnPort.setWidth(100);
        tableColumnPort.setText("Port");

    }

    private boolean checkChange() {
        int ok = ErrorLog.message(Messages.getString("MainListener.apply_changes"), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
        return ok == SWT.YES;
    }

    /** This method initializes table */
    private void createTableProcessClasses() {
        tableProcessClasses = JOE_Tbl_ProcessClassesForm_ProcessClasses.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
        tableProcessClasses.setHeaderVisible(true);
        tableProcessClasses.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 4));
        tableProcessClasses.setLinesVisible(true);
        tableProcessClasses.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (btApply.isEnabled() && checkChange()) {
                    applyClass();
                }
                Element currElem = listener.selectProcessClass(tableProcessClasses.getSelectionIndex());
                if (currElem != null && !Utils.isElementEnabled("process_class", dom, currElem)) {
                    setInput(false);
                    btRemove.setEnabled(false);
                    btApply.setEnabled(false);
                } else {
                    boolean selection = tableProcessClasses.getSelectionCount() > 0;
                    btRemove.setEnabled(selection);
                    if (selection) {
                        btNewRemoteScheduler.setEnabled(true);
                        listener.selectProcessClass(tableProcessClasses.getSelectionIndex());
                        setInput(true);
                        tProcessClass.setBackground(null);
                    }
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_ProcessClassesForm_ProcessClass.Control(new TableColumn(tableProcessClasses, SWT.NONE));
        tableColumn.setWidth(104);
        TableColumn tableColumn1 = JOE_TCl_ProcessClassesForm_MaxProcesses.Control(new TableColumn(tableProcessClasses, SWT.NONE));
        tableColumn1.setWidth(91);
        TableColumn tableColumn2 = JOE_TCl_ProcessClassesForm_RemoteExecution.Control(new TableColumn(tableProcessClasses, SWT.NONE));
        tableColumn2.setWidth(355);
    }

    private void applyClass() {
        if (!checkRemote())
            return;
        boolean _continue = true;
        if (listener.getProcessClass().length() > 0 && !listener.getProcessClass().equals(tProcessClass.getText())
                && !Utils.checkElement(listener.getProcessClass(), dom, JOEConstants.PROCESS_CLASSES, null))
            _continue = false;
        if (_continue)
            try {
                Integer.parseInt(tMaxProcesses.getText());
            } catch (NumberFormatException e) {
                tMaxProcesses.setText("1");
            }
        applyRemoteSchedulerEntry();
        listener.applyRemoteSchedulerTable(tableRemoteScheduler);
        listener.applyProcessClass(tProcessClass.getText(), tRemoteHost.getText(), tRemotePort.getText(), Integer.parseInt(tMaxProcesses.getText()));
        listener.fillProcessClassesTable(tableProcessClasses);

        getShell().setDefaultButton(btNew);
        tProcessClass.setBackground(null);
        btApply.setEnabled(false);
        if (dom.isLifeElement()) {
            setInput(true);
        }
    }

    private void setInput(boolean enabled) {
        tProcessClass.setEnabled(enabled);
        tMaxProcesses.setEnabled(enabled);
        tRemoteHost.setEnabled(enabled);
        tRemotePort.setEnabled(enabled);

        if (enabled) {
            tProcessClass.setText(listener.getProcessClass());
            tRemoteHost.setText(listener.getRemoteHost());
            tRemotePort.setText(listener.getRemotePort());
            tMaxProcesses.setText(String.valueOf(listener.getMaxProcesses()));
            tProcessClass.setFocus();
            listener.fillRemoteSchedulerTable(tableRemoteScheduler);

        } else {
            tProcessClass.setText("");
            tRemoteHost.setText("");
            tRemotePort.setText("");
            tMaxProcesses.setText("");
        }
        btApply.setEnabled(false);
        btRemove.setEnabled(tableProcessClasses.getSelectionCount() > 0);
    }

    private boolean checkRemote() {
        if (tRemoteHost.getText().trim().length() > 0 && tRemotePort.getText().trim().length() == 0) {
            MainWindow.message(getShell(), JOE_M_ProcessClassesForm_MissingPort.label(), SWT.ICON_WARNING | SWT.OK);
            return false;
        } else if (tRemoteHost.getText().trim().length() == 0 && tRemotePort.getText().trim().length() > 0) {
            MainWindow.message(getShell(), JOE_M_ProcessClassesForm_MissingHost.label(), SWT.ICON_WARNING | SWT.OK);
            return false;
        }
        return true;
    }

    public static Table getTable() {
        return tableProcessClasses;
    }

    private void traversed(final TraverseEvent e) {
        if (e.keyCode == SWT.CR) {
            e.doit = false;
            applyClass();

        }
    }
} // @jve:decl-index=0:visual-constraint="10,10"
