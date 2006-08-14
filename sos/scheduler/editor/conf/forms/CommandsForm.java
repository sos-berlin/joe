package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.CommandsListener;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class CommandsForm extends Composite implements IUpdateLanguage {

    private Text              tCommands;

    private CommandsListener  listener;

    private SchedulerListener mainListener;

    private Group             commandsGroup = null;

    private Button            bSave         = null;

    private Label             label         = null;


    public CommandsForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate main) throws Exception {
        super(parent, style);
        listener = new CommandsListener(dom, main);
        initialize();
        setToolTipText();
        tCommands.setText(listener.readCommands());
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
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        commandsGroup = new Group(this, SWT.NONE);
        commandsGroup.setText("Commands");
        commandsGroup.setLayout(gridLayout);
        createTable();
        new Label(commandsGroup, SWT.NONE);

        tCommands = new Text(commandsGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
        tCommands.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        final GridData gridData4_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        tCommands.setLayoutData(gridData4_1);
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bSave = new Button(commandsGroup, SWT.NONE);
        bSave.setText("&Save");
        bSave.setLayoutData(gridData);
        getShell().setDefaultButton(bSave);
        bSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.saveCommands(tCommands.getText());

            }
        });

        label = new Label(commandsGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label");
        label.setLayoutData(gridData4);
        new Label(commandsGroup, SWT.NONE);
        new Label(commandsGroup, SWT.NONE);
    }


    /**
     * This method initializes table
     */
    private void createTable() {

    }


    public void setToolTipText() {
        bSave.setToolTipText(Messages.getTooltip("commands.btn_save"));

    }

} // @jve:decl-index=0:visual-constraint="10,10"
