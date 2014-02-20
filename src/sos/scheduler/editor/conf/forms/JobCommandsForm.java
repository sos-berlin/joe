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

import com.sos.joe.interfaces.ISchedulerUpdate;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobCommandsListener;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class JobCommandsForm extends SOSJOEMessageCodes implements IUpdateLanguage {

    private JobCommandsListener listener       = null;

    private Group               commandsGroup  = null;

    private Table               table          = null;

    private Button              bNewCommands   = null;

    private Button              bRemoveCommand = null;

    private Label               label          = null;

    private SchedulerDom        _dom           = null;

    public JobCommandsForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate update, SchedulerListener mainListener) {

        super(parent, style);
        _dom = dom;
        listener = new JobCommandsListener(dom, job, update);
        initialize();
        setToolTipText();
        listener.fillTable(table);

        commandsGroup.setEnabled(Utils.isElementEnabled("job", dom, job));

    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(656, 400));
    }

    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;

        commandsGroup = JOE_G_JobCommand_Commands.Control(new Group(this, SWT.NONE));
        commandsGroup.setLayout(gridLayout);

        createTable();

        bNewCommands = JOE_B_JobCommand_NewCommand.Control(new Button(commandsGroup, SWT.NONE));
        bNewCommands.setLayoutData(gridData);
        getShell().setDefaultButton(bNewCommands);
        bNewCommands.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newCommands(table);
                bRemoveCommand.setEnabled(true);
            }
        });

        label = new Label(commandsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(gridData4);

        bRemoveCommand = JOE_B_JobCommand_RemoveCommand.Control(new Button(commandsGroup, SWT.NONE));
        bRemoveCommand.setEnabled(false);
        bRemoveCommand.setLayoutData(gridData1);
        bRemoveCommand.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemoveCommand.setEnabled(listener.deleteCommands(table));
            }
        });
    }

    /**
     * This method initializes table
     */
    private void createTable() {

        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
        gridData2.widthHint = 204;
        table = JOE_Tbl_JobCommand_Table.Control(new Table(commandsGroup, SWT.BORDER | SWT.FULL_SELECTION));
        table.addMouseListener(new MouseAdapter() {
            public void mouseDoubleClick(final MouseEvent e) {
                if (table.getSelectionCount() > 0)
                    ContextMenu.goTo(table.getSelection()[0].getText(0), _dom, Editor.JOB_COMMAND);
            }
        });
        table.setHeaderVisible(true);
        table.setLayoutData(gridData2);
        table.setLinesVisible(true);

        TableColumn tableColumn = JOE_TCl_JobCommand_Exitcode.Control(new TableColumn(table, SWT.NONE));
        tableColumn.setWidth(240);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemoveCommand.setEnabled(true);
            }
        });

    }

    public void setToolTipText() {
        //
    }

} // @jve:decl-index=0:visual-constraint="10,10"
