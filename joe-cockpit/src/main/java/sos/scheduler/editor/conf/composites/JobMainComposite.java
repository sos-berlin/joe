package sos.scheduler.editor.conf.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;

public class JobMainComposite extends SOSJOEMessageCodes {

    private final int intNoOfLabelColumns = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobMainComposite.class);
    private JobListener jobDataProvider = null;
    private Group gMain = null;
    private Text tbxJobName = null;
    private Label lblJobTitlelabel1 = null;
    private Text tbxJobTitle = null;
    private boolean init = true;
    private Label label = null;
    private GridLayout gridLayout = null;
    private String isUsed;

    public JobMainComposite(Group parent, int style, JobListener objDataProvider_) {
        super(parent, style);
        jobDataProvider = objDataProvider_;
        createGroup(parent);
    }

    private void createGroup(final Group parent) {
        gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 6;
        gMain = JOE_G_JobMainComposite_MainOptions.control(new Group(parent, SWT.NONE));
        gMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        gMain.setLayout(gridLayout);
        label = JOE_L_JobMainComposite_JobName.control(new Label(gMain, SWT.NONE));
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.END, false, false, intNoOfLabelColumns, 1));
        tbxJobName = JOE_T_JobMainComposite_JobName.control(new Text(gMain, SWT.BORDER));
        tbxJobName.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                //
            }
        });
        tbxJobName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
        tbxJobName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                checkName();
                jobDataProvider.setJobName(tbxJobName.getText(), true);
                parent.setText(jobDataProvider.getJobNameAndTitle());
            }
        });
        lblJobTitlelabel1 = JOE_L_JobMainComposite_JobTitle.control(new Label(gMain, SWT.NONE));
        lblJobTitlelabel1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, intNoOfLabelColumns, 1));
        tbxJobTitle = JOE_T_JobMainComposite_JobTitle.control(new Text(gMain, SWT.BORDER));
        tbxJobTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));
        tbxJobTitle.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                jobDataProvider.setTitle(tbxJobTitle.getText());
            }
        });
        ProcessClassSelector processClassSelector = new ProcessClassSelector(jobDataProvider, gMain, SWT.NONE);
        processClassSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1));
    }

    public void init() {
        init = true;
        try {
            isUsed = Utils.elementIsUsed(jobDataProvider.getJobName(), jobDataProvider.get_dom(), JOEConstants.JOB, null);
        } catch (JDOMException e) {
            LOGGER.error(e.getMessage(), e);
        }
        tbxJobName.setText(jobDataProvider.getJobName());
        tbxJobTitle.setText(jobDataProvider.getTitle());
        tbxJobName.setFocus();
        init = false;
    }

    private void checkName() {
        if (!isUsed.isEmpty()) {
            int c = MainWindow.message(isUsed, SWT.YES | SWT.NO | SWT.ICON_WARNING);
            if (c != SWT.YES) {
                isUsed = "";
                tbxJobName.setText(jobDataProvider.getJobName());
                tbxJobName.setSelection(tbxJobName.getText().length());
            } else {
                tbxJobName.setSelection(tbxJobName.getText().length());
            }
            isUsed = "";
        }
        if (Utils.existName(tbxJobName.getText(), jobDataProvider.getJob(), "job")) {
            tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
        } else {
            tbxJobName.setBackground(null);
        }
    }

    @Override
    protected void checkSubclass() {
        //
    }

    public Group getgMain() {
        return gMain;
    }

}