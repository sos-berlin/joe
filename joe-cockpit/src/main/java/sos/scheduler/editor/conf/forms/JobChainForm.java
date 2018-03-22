package sos.scheduler.editor.conf.forms;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.composites.JobChainDiagramComposite;
import sos.scheduler.editor.conf.composites.ProcessClassSelector;
import sos.scheduler.editor.conf.listeners.JobChainFileWatchingListener;
import sos.scheduler.editor.conf.listeners.JobChainListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainForm extends SOSJOEMessageCodes implements IUnsaved {

    private final String conClassName = "JobChainForm";
    final String conMethodName = conClassName + "::enclosing_method";
    private static final Logger LOGGER = Logger.getLogger(JobChainForm.class);
    private JobChainListener jobchainDataProvider = null;
    private JobChainFileWatchingListener jobChainFileWatchingListener;
    private Group jobChainGroup = null;
    private Label chainNameLabel = null;
    private Text tName = null;
    private Button bRecoverable = null;
    private Button bVisible = null;
    private Button butDetails = null;
    private ISchedulerUpdate update = null;
    private Button butDistributed = null;
    private Text txtTitle = null;
    private boolean init = false;
    private boolean changeJobChainName = true;
    private Text sMaxorders;
    private ProcessClassSelector processClassSelectorJobChain;
    private ProcessClassSelector processClassSelectorFileWatcher;
    private JobChainDiagramComposite jobChainDiagramComposite = null;
    private Label lblProcessClass = null;

    public JobChainForm(Composite parent, int style, SchedulerDom dom, Element jobChain) {
        super(parent, style);
        init = true;
        jobchainDataProvider = new JobChainListener(dom, jobChain);
        jobChainFileWatchingListener = new JobChainFileWatchingListener(jobchainDataProvider);
        initialize();
        fillChain();
        this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
        init = false;
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
        setSize(new org.eclipse.swt.graphics.Point(676, 464));
        tName.setFocus();
    }

    private void createGroup() {
        jobChainGroup = new Group(this, SWT.NONE);
        String strJobChainName = "";
        if (jobchainDataProvider.getChainName() != null) {
            strJobChainName = jobchainDataProvider.getChainName();
        }
        jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 4;
        jobChainGroup.setLayout(gridLayout);
        chainNameLabel = JOE_L_JobChainForm_ChainName.control(new Label(jobChainGroup, SWT.NONE));
        chainNameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        new Label(jobChainGroup, SWT.NONE);
        tName = JOE_T_JobChainForm_ChainName.control(new Text(jobChainGroup, SWT.BORDER));
        final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 1, 1);
        gridData_4.widthHint = 273;
        tName.setLayoutData(gridData_4);
        tName.setText(jobchainDataProvider.getChainName());
        tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (init) {
                    return;
                }
                String newName = tName.getText().trim();
                boolean setSelection = true;
				 
                try {
                    setSelection =
                            !Utils.elementIsUsed(jobchainDataProvider.getChainName(), jobchainDataProvider.get_dom(), JOEConstants.JOB_CHAIN, null).isEmpty();
                } catch (JDOMException e1) {
                    setSelection = false;
                }
                if (Utils.checkElement(jobchainDataProvider.getChainName(), jobchainDataProvider.get_dom(), JOEConstants.JOB_CHAIN, null)) {
                    if (setSelection) {
                        tName.setSelection(newName.length());
                    }
                    boolean existname = Utils.existName(newName, jobchainDataProvider.getChain(), "job_chain");
                    if (existname) {
                        tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                    } else {
                        tName.setBackground(null);
                    }
                    if (update != null) {
                        update.updateTreeItem(JOE_M_JobChainForm_JobChain.params(newName));
                    }
                    jobchainDataProvider.setChainName(newName);
                    String strJobChainName = "";
                    if (jobchainDataProvider.getChainName() != null) {
                        strJobChainName = jobchainDataProvider.getChainName();
                    }
                    jobChainGroup.setText(JOE_M_JobChainForm_JobChain.params(strJobChainName));
                    changeJobChainName = true;
                }
            }
        });
        butDetails = JOE_B_JobChainForm_Parameter.control(new Button(jobChainGroup, SWT.NONE));
        butDetails.setEnabled(true);
        butDetails.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (jobchainDataProvider.getDom().isChanged() && changeJobChainName) {
                    if (jobchainDataProvider.getDom().getFilename() == null) {
                        MainWindow.message(JOE_M_JobChainForm_SaveChain.label(), SWT.ICON_WARNING);
                    } else {
                        MainWindow.message(JOE_M_JobChainForm_ChainNameChanged.label(), SWT.ICON_WARNING);
                    }
                    return;
                } else {
                    changeJobChainName = false;
                }
                showDetails(null);
            }
        });
        final Label titleLabel = JOE_L_JobChainForm_Title.control(new Label(jobChainGroup, SWT.NONE));
        new Label(jobChainGroup, SWT.NONE);
        txtTitle = JOE_T_JobChainForm_Title.control(new Text(jobChainGroup, SWT.BORDER));
        txtTitle.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                jobchainDataProvider.setTitle(txtTitle.getText());
            }
        });
        txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
        new Label(jobChainGroup, SWT.NONE);
        Label lblMaxOrders = JOE_L_JobChainForm_MaxOrders.control(new Label(jobChainGroup, SWT.NONE));
        new Label(jobChainGroup, SWT.NONE);
        sMaxorders = JOE_T_JobChainForm_MaxOrders.control(new Text(jobChainGroup, SWT.BORDER));
        sMaxorders.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                if (init) {
                    return;
                }
                jobchainDataProvider.setMaxorders(sMaxorders.getText().trim());
            }
        });
        GridData gd_sMaxorders = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_sMaxorders.minimumWidth = 60;
        sMaxorders.setLayoutData(gd_sMaxorders);
        new Label(jobChainGroup, SWT.NONE);
        processClassSelectorJobChain = new ProcessClassSelector(jobchainDataProvider, jobChainGroup, 1, SWT.NONE);
        processClassSelectorJobChain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1));
        processClassSelectorJobChain.setLabel("Process Class Job Chain");
        processClassSelectorFileWatcher = new ProcessClassSelector(jobChainFileWatchingListener, jobChainGroup, 1, SWT.NONE);
        processClassSelectorFileWatcher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1));
        processClassSelectorFileWatcher.setLabel("Process Class Filewatcher");
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button = new Button(jobChainGroup, SWT.CHECK);
        bRecoverable = JOE_B_JobChainForm_Recoverable.control(button);
        bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bRecoverable.setSelection(true);
        bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (init) {
                    return;
                }
                jobchainDataProvider.setRecoverable(bRecoverable.getSelection());
            }
        });
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button_1 = new Button(jobChainGroup, SWT.CHECK);
        butDistributed = JOE_B_JobChainForm_Distributed.control(button_1);
        butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        butDistributed.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (init) {
                    return;
                }
                jobchainDataProvider.setDistributed(butDistributed.getSelection());
            }
        });
        butDistributed.setSelection(jobchainDataProvider.isDistributed());
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        new Label(jobChainGroup, SWT.NONE);
        Button button_2 = new Button(jobChainGroup, SWT.CHECK);
        bVisible = JOE_B_JobChainForm_Visible.control(button_2);
        bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bVisible.setSelection(true);
        bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (init) {
                    return;
                }
                jobchainDataProvider.setVisible(bVisible.getSelection());

            }
        });
        new Label(jobChainGroup, SWT.NONE);
        if (Options.isShowDiagram()) {
            jobChainDiagramComposite = new JobChainDiagramComposite(jobChainGroup, 420);
            GridData gd_jobChainDiagramComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 2);
            gd_jobChainDiagramComposite.heightHint = 226;
            jobChainDiagramComposite.setLayoutData(gd_jobChainDiagramComposite);
            try {
                String xml = Utils.getElementAsString(jobchainDataProvider.getChain());
                File inputFile = new File(jobchainDataProvider.getDom().getFilename(), jobchainDataProvider.getChainName() + ".job_chain.xml~");
                jobChainDiagramComposite.jobChainDiagram(xml, inputFile);
            } catch (Exception e1) {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e1);
            }
            jobChainDiagramComposite.layout();
        }
    }

    private void fillChain() {
        tName.setEnabled(true);
        bRecoverable.setEnabled(true);
        bVisible.setEnabled(true);
        tName.setText(jobchainDataProvider.getChainName());
        txtTitle.setText(jobchainDataProvider.getTitle());
        bRecoverable.setSelection(jobchainDataProvider.getRecoverable());
        bVisible.setSelection(jobchainDataProvider.getVisible());
        tName.setBackground(null);
        sMaxorders.setText(jobchainDataProvider.getMaxOrders());
        jobchainDataProvider.setVisible(bVisible.getSelection());
        jobchainDataProvider.setDistributed(butDistributed.getSelection());
        jobchainDataProvider.setRecoverable(bRecoverable.getSelection());
    }

    public void setISchedulerUpdate(ISchedulerUpdate update_) {
        update = update_;
    }

    private void showDetails(String state) {
        if (tName.getText() != null && !tName.getText().isEmpty()) {
            boolean isLifeElement = jobchainDataProvider.getDom().isLifeElement() || jobchainDataProvider.getDom().isDirectory();
            if (state == null) {
                DetailDialogForm detail = new DetailDialogForm(tName.getText(), isLifeElement, jobchainDataProvider.getDom().getFilename());
                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(jobchainDataProvider.getDom(), update);
            } else {
                DetailDialogForm detail =
                        new DetailDialogForm(tName.getText(), state, null, isLifeElement, jobchainDataProvider.getDom().getFilename());
                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(jobchainDataProvider.getDom(), update);
            }
        } else {
            MainWindow.message(getShell(), JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        }
    }

    public void removeTimer() {
        if (jobChainDiagramComposite != null) {
            jobChainDiagramComposite.removeTimer();
        }
    }

    public void setToolTipText() {
        //
    }

}
