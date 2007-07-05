package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.RunTimeListener;

public class RunTimeForm extends Composite implements IUpdateLanguage {
    private Text tFunction;
    private RunTimeListener listener;

    private Group           gRunTime    = null;

    private DateForm        holidayForm = null;

    private PeriodForm      periodForm  = null;

    private Group           gComment    = null;

    private Text            tComment    = null;


    public RunTimeForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate gui) {
        super(parent, style);
        listener = new RunTimeListener(dom, job);
        initialize();
        setToolTipText();

        dom.setInit(true);

        this.gRunTime.setEnabled(Utils.isElementEnabled("job", dom, job));
        
        holidayForm.setObjects(dom, listener.getRunTime(), gui);

        periodForm.setParams(dom, listener.isOnOrder());
        periodForm.setRunOnce(true);
        periodForm.setEnabled(true);
        periodForm.setPeriod(listener.getRunTime());
        tComment.setText(listener.getComment());
        tFunction.setText(listener.getFunction());

        String title = gComment.getText();
        if (dom.isJobDisabled(Utils.getAttributeValue("name", job))) {
            title += " (Cannot be set for disabled Jobs)";
            tComment.setEnabled(false);
        }
        gComment.setText(title);

        dom.setInit(false);
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(576, 518));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout3 = new GridLayout();
        gRunTime = new Group(this, SWT.NONE);
        gRunTime.setText("Run Time");
        createPeriodForm();
        gRunTime.setLayout(gridLayout3);
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, true);
        gridData4.heightHint = 318;

        final Group group = new Group(gRunTime, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);

        final Label functionLabel = new Label(group, SWT.NONE);
        functionLabel.setLayoutData(new GridData());
        functionLabel.setText("Start Time Function");

        tFunction = new Text(group, SWT.BORDER);
        tFunction.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setFunction(tFunction.getText());
        	}
        });
        final GridData gridData10_1_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData10_1_1.widthHint = 243;
        tFunction.setLayoutData(gridData10_1_1);
        holidayForm = new DateForm(gRunTime, SWT.NONE, 0);
        holidayForm.setLayoutData(gridData4);
        createGroup2();
        
    }


 


    /**
     * This method initializes periodForm
     */
    private void createPeriodForm() {
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
        gridData2.widthHint = 151;
        periodForm = new PeriodForm(gRunTime, SWT.NONE);
        periodForm.setLayoutData(gridData2);
    }


    /**
     * This method initializes group
     */
    private void createGroup2() {
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        gComment = new Group(gRunTime, SWT.NONE);
        gComment.setText("Comment");
        gComment.setLayoutData(gridData);
        gComment.setLayout(new GridLayout());
        tComment = new Text(gComment, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.setLayoutData(gridData1);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComment(tComment.getText());
            }
        });
    }


    public void setToolTipText() {

        tComment.setToolTipText(Messages.getTooltip("run_time.comment"));

    }
} // @jve:decl-index=0:visual-constraint="10,10"
