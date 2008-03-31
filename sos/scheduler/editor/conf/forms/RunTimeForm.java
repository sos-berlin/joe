package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.RunTimeListener;

public class RunTimeForm extends Composite implements IUpdateLanguage {
    
	private Text            tFunction   = null;
	
    private RunTimeListener listener    = null;

    private Group           gRunTime    = null;

    private DateForm        holidayForm = null;

    private PeriodForm      periodForm  = null;

    private Group           gComment    = null;

    private Text            tComment    = null;        

    private Combo           comSchedule = null; 

    private Button          butBrowse   = null;
    
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
        gridData4.heightHint = 348;

        final Group group = new Group(gRunTime, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        group.setLayout(gridLayout);

        final Label functionLabel = new Label(group, SWT.NONE);
        functionLabel.setLayoutData(new GridData());
        functionLabel.setText("Start Time Function:");

        tFunction = new Text(group, SWT.BORDER);
        tFunction.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setFunction(tFunction.getText());
        	}
        });
        final GridData gridData10_1_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData10_1_1.widthHint = 243;
        tFunction.setLayoutData(gridData10_1_1);

        final Group group_1 = new Group(gRunTime, SWT.NONE);
        group_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 3;
        group_1.setLayout(gridLayout_2);

        final Label scheduleLabel = new Label(group_1, SWT.NONE);
        scheduleLabel.setText("Schedule:");

        comSchedule = new Combo(group_1, SWT.NONE);
        comSchedule.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		listener.setSchedule(comSchedule.getText());
        	}
        });
        comSchedule.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        comSchedule.setItems(listener.getSchedules());
        comSchedule.setText(listener.getSchedule());
        comSchedule.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setSchedule(comSchedule.getText());
        	}
        });

        butBrowse = new Button(group_1, SWT.NONE);
        butBrowse.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_SCHEDULE);
				if(name != null && name.length() > 0)
					comSchedule.setText(name);
        	}
        });
        butBrowse.setText("Browse");
        
        holidayForm = new DateForm(gRunTime, SWT.NONE, 0);
        holidayForm.setLayoutData(gridData4);
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        gComment = new Group(gRunTime, SWT.NONE);
        gComment.setText("Comment");
        gComment.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        gComment.setLayout(gridLayout_1);

        final Button button = new Button(gComment, SWT.NONE);
        button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
		
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
        tComment = new Text(gComment, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.setLayoutData(gridData1);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComment(tComment.getText());
            }
        });
        new Label(gComment, SWT.NONE);
        createGroup2();
        
    }


 


    /**
     * This method initializes periodForm
     */
    private void createPeriodForm() {
    	GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
    	gridData2.widthHint = 151;

    	periodForm = new PeriodForm(gRunTime, SWT.NONE, Editor.RUNTIME);
    	periodForm.setLayoutData(gridData2);
    }


    /**
     * This method initializes group
     */
    private void createGroup2() {
    }


    public void setToolTipText() {

        tComment.setToolTipText(Messages.getTooltip("run_time.comment"));
        butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
        
        tFunction.setToolTipText(Messages.getTooltip("run_time.start_time_function"));
        comSchedule.setToolTipText(Messages.getTooltip("run_time.combo_schedule"));
    }
    
    
    
} // @jve:decl-index=0:visual-constraint="10,10"
