package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.ScriptsListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

;
public class ScriptsForm extends SOSJOEMessageCodes {

    private ScriptsListener listener = null;
    private Group scriptsGroup = null;
    private static Table table = null;
    private Button butRemove = null;
    private Label label = null;
    private SchedulerDom dom = null;
    private Button butNew = null;
    private static String MONITOR = JOE_M_ScriptsForm_Monitor.label();

    public ScriptsForm(Composite parent, int style, SchedulerDom pobjXMLDom, ISchedulerUpdate update, Element elem) {
        super(parent, style);
        try {
            this.dom = pobjXMLDom;
            listener = new ScriptsListener(pobjXMLDom, update, elem);
            initialize();
            listener.fillTable(table);
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
        }
    }

    private void initialize() {
        try {
            this.setLayout(new FillLayout());
            createGroup();
            setSize(new org.eclipse.swt.graphics.Point(656, 400));
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
        }
    }

    private void createGroup() {
        try {
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 2;
            scriptsGroup = new Group(this, SWT.NONE);
            scriptsGroup.setText(sos.scheduler.editor.conf.listeners.SchedulerListener.MONITORS);
            scriptsGroup.setLayout(gridLayout);
            if (Utils.isElementEnabled("job", dom, listener.getParent())) {
                scriptsGroup.setEnabled(true);
            } else {
                scriptsGroup.setEnabled(false);
            }
            createTable();
            butNew = JOE_B_ScriptsForm_New.Control(new Button(scriptsGroup, SWT.NONE));
            butNew.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    addMonitor();
                }
            });
            butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butRemove = JOE_B_ScriptsForm_Remove.Control(new Button(scriptsGroup, SWT.NONE));
            butRemove.setEnabled(false);
            butRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    int c = MainWindow.message(getShell(), JOE_M_ScriptsForm_RemoveMonitor.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    if (c != SWT.YES) {
                        return;
                    }
                    butRemove.setEnabled(listener.delete(table));
                    table.deselectAll();
                }
            });
            butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            label = new Label(scriptsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
            label.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
        }
    }

    private void createTable() {
        try {
            GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
            gridData2.widthHint = 425;
            table = JOE_Tbl_ScriptsForm_PrePostProcessing.Control(new Table(scriptsGroup, SWT.FULL_SELECTION | SWT.BORDER));
            table.addMouseListener(new MouseAdapter() {

                public void mouseDoubleClick(final MouseEvent e) {
                    if (table.getSelectionCount() > 0) {
                        ContextMenu.goTo(Utils.getAttributeValue("name", listener.getParent()) + "_@_" + table.getSelection()[0].getText(0), dom, 
                                JOEConstants.MONITOR);
                    }
                }
            });
            table.setHeaderVisible(true);
            table.setLayoutData(gridData2);
            table.setLinesVisible(true);
            table.addMouseListener(new MouseListener() {

                @Override
                public void mouseUp(MouseEvent e) {
                }

                @Override
                public void mouseDown(MouseEvent e) {
                }

                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    if (!table.isDisposed()) {
                        int index = table.getSelectionIndex();
                        if (index >= 0) {
                            String strName = table.getSelection()[0].getText(0);
                            ContextMenu.goTo(strName, dom, JOEConstants.MONITOR);
                        }
                    }
                }
            });
            table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    if (table.getSelectionCount() > 0) {
                        if (Utils.isElementEnabled("job", dom, (Element) e.item.getData())) {
                            butRemove.setEnabled(true);
                        } else {
                            butRemove.setEnabled(false);
                            return;
                        }
                    }
                }
            });
            TableColumn tableColumn1 = JOE_TCl_ScriptsForm_Name.Control(new TableColumn(table, SWT.NONE));
            tableColumn1.setWidth(280);
            TableColumn tableColumn2 = JOE_TCl_ScriptsForm_Ordering.Control(new TableColumn(table, SWT.NONE));
            tableColumn2.setWidth(280);
        } catch (Exception e) {
            new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
        }
    }

    private void addMonitor() {
        listener.save(table, MONITOR + table.getItemCount(), String.valueOf(table.getItemCount()), null);
        butRemove.setEnabled(false);
        table.deselectAll();
    }

    public static Table getTable() {
        return table;
    }
    
}