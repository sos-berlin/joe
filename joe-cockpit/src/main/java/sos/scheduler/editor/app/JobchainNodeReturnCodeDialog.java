package sos.scheduler.editor.app;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeAddOrderElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeNextStateElement;
import sos.scheduler.editor.conf.container.JobChainNodeReturnCodeAddOrder;
import sos.scheduler.editor.conf.container.JobChainNodeReturnCodeNextState;
import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class JobchainNodeReturnCodeDialog extends SOSJOEMessageCodes {

    protected CTabFolder tabFolder = null;
    protected Group mainGroup = null;
    private static final Logger LOGGER = Logger.getLogger(ScriptJobMainForm.class);
    private Composite tabItemNextStateComposite = null;
    private Composite tabControlComposite = null;
    private Composite tabItemAddOrderComposite = null;
    private CTabItem tabItemAddOrder = null;
    private CTabItem tabItemNextState = null;
    private JobChainNodeReturnCodeAddOrder jobChainNodeReturnCodeAddOrder = null;
    private JobChainNodeReturnCodeNextState jobChainNodeReturnCodeNextState = null;

    protected Object result;
    protected Shell shell;
    private Shell parent;

    private JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements;

    private boolean ok;
    private JobChainListener listener = null;

    public JobchainNodeReturnCodeDialog(Shell parent_, int style, JobChainListener listener_) {
        super(parent_, style);
        listener = listener_;
        parent = parent_;
    }

    public void execute() {
        Display display = Display.getDefault();
        Shell shell = open(display);

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void saveReturnCodeList() {
        jobchainListOfReturnCodeElements = new JobchainListOfReturnCodeElements();
        jobChainNodeReturnCodeNextState.getJobchainListOfReturnCodeNextStateElements().reset();
        while (jobChainNodeReturnCodeNextState.getJobchainListOfReturnCodeNextStateElements().hasNext()) {
            JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement = jobChainNodeReturnCodeNextState
                    .getJobchainListOfReturnCodeNextStateElements().getNext();
            jobchainListOfReturnCodeElements.add(jobchainReturnCodeNextStateElement);
        }

        if (jobChainNodeReturnCodeAddOrder.getJobchainListOfReturnCodeAddOrderElements() != null) {
            while (jobChainNodeReturnCodeAddOrder.getJobchainListOfReturnCodeAddOrderElements().hasNext()) {
                JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = jobChainNodeReturnCodeAddOrder
                        .getJobchainListOfReturnCodeAddOrderElements().getNext();
                jobchainListOfReturnCodeElements.add(jobchainReturnCodeAddOrderElement);
            }
        }
    }

    private Shell open(final Display display) {

        final Shell dialogShell = new Shell(parent, SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        dialogShell.setMinimumSize(new Point(1100, 680));
        dialogShell.setLocation(parent.getLocation().x + 40, parent.getLocation().y + 40);

        dialogShell.setSize(1100, 680);
        
        GridLayout gridLayoutShell = new GridLayout(2,false);
         
        dialogShell.setLayout(gridLayoutShell);
        dialogShell.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 3));
        dialogShell.pack();
        createGroup(dialogShell);

        final Composite compositeButtons = new Composite(dialogShell, SWT.NONE);
        final GridData gridDataButtons = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridDataButtons.widthHint = 80;
        compositeButtons.setLayoutData(gridDataButtons);
        final GridLayout gridLayoutButtons = new GridLayout(1,false);
        compositeButtons.setLayout(gridLayoutButtons);
        
        Button btnOk = JOE_B_Apply.Control(new Button(compositeButtons, SWT.NONE));
        btnOk.setSize(68, 25);
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        btnOk.addSelectionListener(new SelectionAdapter() { 

            @Override
            public void widgetSelected(SelectionEvent e) {
                jobChainNodeReturnCodeNextState.saveNextStateList();
                jobChainNodeReturnCodeAddOrder.saveAddOrderList();
                saveReturnCodeList();
                ok = true;
                dialogShell.dispose();
            }
        });

     
        Button btnCancelButton = JOE_B_Cancel.Control(new Button(compositeButtons, SWT.NONE));
        btnCancelButton.setSize(68, 25);
        btnCancelButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        btnCancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (jobchainListOfReturnCodeElements != null) {
                    jobchainListOfReturnCodeElements.clear();
                }
                ok = false;
                dialogShell.dispose();
            }
        });

        initTables();

        dialogShell.open();
        return dialogShell;
    }

    public boolean isOk() {
        return ok;
    }

    private void initTables() {
        jobChainNodeReturnCodeAddOrder.initTable(jobchainListOfReturnCodeElements);
        jobChainNodeReturnCodeNextState.initTable(jobchainListOfReturnCodeElements);
    }

    private void createGroup(Composite parent) {

        GridLayout gridLayoutJobChainNode = new GridLayout();
        gridLayoutJobChainNode.numColumns = 1;
        mainGroup = new Group(parent, SWT.NONE);
        mainGroup.setText(listener.getDescription());
        mainGroup.setLayout(gridLayoutJobChainNode);
        mainGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
 
        createScriptTabForm();
    }

    public JobchainListOfReturnCodeElements getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }

    public void setJobchainListOfReturnCodeElements(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        this.jobchainListOfReturnCodeElements = jobchainListOfReturnCodeElements;
    }

    protected void createScriptTabForm() {
        tabFolder = new CTabFolder(mainGroup, SWT.None);
        tabFolder.setLayout(new GridLayout());
        setResizableV(tabFolder);
        tabFolder.setSimple(true);
        tabFolder.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int intIndex = tabFolder.getSelectionIndex();
                Options.setLastTabItemIndex(intIndex);
                LOGGER.debug(JOE_M_ScriptForm_ItemIndex.params(tabFolder.getSelectionIndex()));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        createTabPages();
        tabFolder.setSelection(0);
    }

    private void createTabItemAddOrder(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        jobChainNodeReturnCodeAddOrder = new JobChainNodeReturnCodeAddOrder(pParentComposite, listener);
        pParentComposite.layout();
    }

    private void createTabItemNextState(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        jobChainNodeReturnCodeNextState = new JobChainNodeReturnCodeNextState(pParentComposite, listener);
        pParentComposite.layout();
    }

    private void createTabPages() {
        tabControlComposite = new Composite(tabFolder, SWT.NONE);
        tabControlComposite.setLayout(new GridLayout());
        setResizableV(tabControlComposite);

        tabItemAddOrder = JOE_L_ReturnCodesForm_Add_Order.Control(new CTabItem(tabFolder, SWT.NONE));
        tabItemAddOrderComposite = new Composite(tabFolder, SWT.NONE);
        tabItemAddOrderComposite.setLayout(new GridLayout());
        setResizableV(tabItemAddOrderComposite);
        tabItemAddOrder.setControl(tabItemAddOrderComposite);

        tabItemNextState = JOE_L_JCNodesForm_NextState.Control(new CTabItem(tabFolder, SWT.NONE));
        tabItemNextStateComposite = new Composite(tabFolder, SWT.NONE);
        tabItemNextStateComposite.setLayout(new GridLayout());
        setResizableV(tabItemNextStateComposite);
        tabItemNextState.setControl(tabItemNextStateComposite);
        createTabItemNextState(tabItemNextStateComposite);
        createTabItemAddOrder(tabItemAddOrderComposite);
    }
}
