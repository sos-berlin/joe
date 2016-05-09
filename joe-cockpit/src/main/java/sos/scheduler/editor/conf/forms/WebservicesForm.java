package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import sos.scheduler.editor.conf.listeners.WebservicesListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class WebservicesForm extends SOSJOEMessageCodes implements IUnsaved {

    private WebservicesListener listener = null;
    private Group group = null;
    private static Table tServices = null;
    private Button bRemove = null;
    private Button bNew = null;
    private Label label8 = null;
    private SchedulerDom _dom = null;

    public WebservicesForm(Composite parent, int style, SchedulerDom dom, Element config, ISchedulerUpdate main) {
        super(parent, style);
        _dom = dom;
        listener = new WebservicesListener(dom, config, main);
        initialize();
        listener.fillTable(tServices);
    }

    public void apply() {
        //
    }

    public boolean isUnsaved() {
        return false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(653, 468));
    }

    private void createGroup() {
        GridData gridData19 = new org.eclipse.swt.layout.GridData();
        gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData19.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalSpan = 1;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group = JOE_G_WebservicesForm_WebServices.Control(new Group(this, SWT.NONE));
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        tServices = JOE_Tbl_WebservicesForm_Services.Control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
        tServices.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                if (tServices.getSelectionCount() > 0) {
                    ContextMenu.goTo(tServices.getSelection()[0].getText(0), _dom, JOEConstants.WEBSERVICE);
                }
            }
        });
        tServices.setHeaderVisible(true);
        tServices.setLayoutData(gridData1);
        tServices.setLinesVisible(true);
        tServices.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean selection = tServices.getSelectionCount() > 0;
                bRemove.setEnabled(selection);
                if (selection) {
                    listener.selectService(tServices.getSelectionIndex());
                }
            }
        });
        TableColumn tableColumn = JOE_TCl_WebservicesForm_Name.Control(new TableColumn(tServices, SWT.NONE));
        tableColumn.setWidth(150);
        TableColumn tableColumn1 = JOE_TCl_WebservicesForm_URL.Control(new TableColumn(tServices, SWT.NONE));
        tableColumn1.setWidth(150);
        TableColumn tableColumn2 = JOE_TCl_WebservicesForm_JobChain.Control(new TableColumn(tServices, SWT.NONE));
        tableColumn2.setWidth(100);
        GridData gridData5 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bNew = JOE_B_WebservicesForm_New.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData5);
        getShell().setDefaultButton(bNew);
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newService(tServices);
            }
        });
        group.setLayout(gridLayout);
        label8 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label8.setText("Label");
        label8.setLayoutData(gridData19);
        bRemove = JOE_B_WebservicesForm_Remove.Control(new Button(group, SWT.NONE));
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tServices.getSelectionCount() > 0) {
                    int index = tServices.getSelectionIndex();
                    listener.removeService(index);
                    tServices.remove(index);
                    if (index >= tServices.getItemCount()) {
                        index--;
                    }
                    if (tServices.getItemCount() > 0) {
                        tServices.select(index);
                        listener.selectService(index);
                    }
                }
                bRemove.setEnabled(tServices.getSelectionCount() > 0);
            }
        });
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData3);
    }

    public static Table getTable() {
        return tServices;
    }

}