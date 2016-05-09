package sos.scheduler.editor.conf.forms;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jdom.Element;
import org.joda.time.DateTimeZone;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.RunTimeListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class RunTimeForm extends SOSJOEMessageCodes {

    private RunTimeListener listener = null;
    private Group gRunTime = null;
    private PeriodForm periodForm = null;
    private Group gComment = null;
    private Text tComment = null;
    private Combo comSchedule = null;
    private Button butBrowse = null;
    private ISchedulerUpdate _gui = null;
    private Group groupSchedule = null;
    private Element runTimeBackUpElem = null;
    private boolean init = false;
    private CCombo cbTimeZone;

    public RunTimeForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate gui) {
        super(parent, style);
        init = true;
        _gui = gui;
        listener = new RunTimeListener(dom, job, _gui);
        initialize();
        dom.setInit(true);
        this.gRunTime.setEnabled(Utils.isElementEnabled("job", dom, job));
        periodForm.setParams(dom, listener.isOnOrder());
        periodForm.setRunOnce(true);
        periodForm.setEnabled(true);
        periodForm.setPeriod(listener.getRunTime());
        tComment.setText(listener.getComment());
        String title = gComment.getText();
        gComment.setText(title);
        dom.setInit(false);
        setEnabled();
        init = false;
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(576, 518));
    }

    private void createGroup() {
        GridLayout gridLayout3 = new GridLayout(2, false);
        gRunTime = JOE_G_RunTimeForm_RunTime.Control(new Group(this, SWT.NONE));
        gRunTime.setLayout(gridLayout3);
        Label lblTimezone = new Label(gRunTime, SWT.NONE);
        lblTimezone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimezone.setText("Timezone");
        cbTimeZone = new CCombo(gRunTime, SWT.BORDER);
        cbTimeZone.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        cbTimeZone.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                listener.setTimeZone(cbTimeZone.getText());
                _gui.updateFont();
                _gui.updateRunTime();
            }
        });
        Set<String> setOfTimeZones = DateTimeZone.getAvailableIDs();
        cbTimeZone.setItems(setOfTimeZones.toArray(new String[setOfTimeZones.size()]));
        cbTimeZone.setText(listener.getTimeZone());
        createPeriodForm();
        String s = listener.getFunction();
        if (s != null && !s.isEmpty()) {
            Label lblStartFunction = new Label(gRunTime, SWT.NONE);
            lblStartFunction.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
            lblStartFunction.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
            lblStartFunction.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
            lblStartFunction.setText(JOE_M_StartTimeFunctionDetected.paramsNoKey(s));
        } else {
            new Label(gRunTime, SWT.NONE);
        }
        groupSchedule = JOE_G_RunTimeForm_Schedule.Control(new Group(gRunTime, SWT.NONE));
        groupSchedule.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        groupSchedule.setLayout(gridLayout_2);
        comSchedule = JOE_Cbo_RunTimeForm_Schedule.Control(new Combo(groupSchedule, SWT.NONE));
        comSchedule.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (init) {
                    return;
                }
                listener.setSchedule(comSchedule.getText());
                _gui.updateFont();
            }
        });
        comSchedule.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        comSchedule.setItems(listener.getSchedules());
        comSchedule.setText(listener.getSchedule());
        comSchedule.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (init) {
                    return;
                }
                setEnabled();
                listener.setSchedule(comSchedule.getText());
                _gui.updateFont();
                _gui.updateRunTime();
            }
        });
        new Label(gRunTime, SWT.NONE);
        butBrowse = JOE_B_RunTimeForm_Browse.Control(new Button(groupSchedule, SWT.NONE));
        butBrowse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_SCHEDULE);
                if (name != null && !name.isEmpty()) {
                    comSchedule.setText(name);
                }
            }
        });
        gComment = JOE_G_RunTimeForm_Comment.Control(new Group(gRunTime, SWT.NONE));
        gComment.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        gComment.setLayout(gridLayout_1);
        tComment = JOE_T_RunTimeForm_Comment.Control(new Text(gComment, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL));
        tComment.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {
                    tComment.setSelection(0, tComment.getText().length());
                }
            }
        });
        tComment.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true));
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                if (init) {
                    return;
                }
                listener.setComment(tComment.getText());
            }
        });
        final Button button = JOE_B_RunTimeForm_Comment.Control(new Button(gComment, SWT.NONE));
        button.setAlignment(SWT.UP);
        final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true);
        gridData_1.widthHint = 29;
        button.setLayoutData(gridData_1);
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
                if (text != null) {
                    tComment.setText(text);
                }
            }
        });
        button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
        setEnabled();
    }

    private void createPeriodForm() {
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
        gridData2.widthHint = 151;
        gridData2.horizontalSpan = 2;
        periodForm = new PeriodForm(gRunTime, SWT.NONE, JOEConstants.RUNTIME);
        periodForm.setLayoutData(gridData2);
        periodForm.setSchedulerUpdate(_gui);
    }

    private void setEnabled() {
        if (init) {
            if (!comSchedule.getText().trim().isEmpty()) {
                groupSchedule.setEnabled(true);
                periodForm.setEnabled(false);
            } else {
                groupSchedule.setEnabled(true);
                periodForm.setEnabled(true);
            }
            return;
        }
        boolean enable = true;
        if (!comSchedule.getText().trim().isEmpty()) {
            groupSchedule.setEnabled(true);
            enable = false;
        } else {
            if (runTimeBackUpElem != null) {
                Element e = listener.getRunTime();
                e.removeAttribute("schedule");
                e.setContent(runTimeBackUpElem.cloneContent());
                for (int i = 0; i < runTimeBackUpElem.getAttributes().size(); i++) {
                    org.jdom.Attribute attr = (org.jdom.Attribute) runTimeBackUpElem.getAttributes().get(i);
                    e.setAttribute(attr.getName(), attr.getValue(), e.getNamespace());
                }
                runTimeBackUpElem = null;
            }
            groupSchedule.setEnabled(true);
        }
        if (!enable && runTimeBackUpElem == null) {
            runTimeBackUpElem = (Element) listener.getRunTime().clone();
            listener.getRunTime().removeContent();
            listener.getRunTime().getAttributes().clear();
        }
        periodForm.setEnabled(enable);
    }

}