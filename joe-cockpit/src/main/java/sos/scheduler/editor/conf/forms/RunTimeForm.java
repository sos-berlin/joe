package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.RunTimeListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;

public class RunTimeForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	private Text				tFunction				= null;
	private RunTimeListener		listener				= null;
	private Group				gRunTime				= null;
	private PeriodForm			periodForm				= null;
	private Group				gComment				= null;
	private Text				tComment				= null;
	private Combo				comSchedule				= null;
	private Button				butBrowse				= null;
	private ISchedulerUpdate	_gui					= null;
	private Group				groupStartTimeFuction	= null;
	private Group				groupSchedule			= null;
	private Element				runTimeBackUpElem		= null;
	private boolean				init					= false;

	public RunTimeForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate gui) {
		super(parent, style);
		init = true;
		_gui = gui;
		listener = new RunTimeListener(dom, job, _gui);
		initialize();
		setToolTipText();
		dom.setInit(true);
		this.gRunTime.setEnabled(Utils.isElementEnabled("job", dom, job));
		periodForm.setParams(dom, listener.isOnOrder());
		periodForm.setRunOnce(true);
		periodForm.setEnabled(true);
		periodForm.setPeriod(listener.getRunTime());
		tComment.setText(listener.getComment());
		tFunction.setText(listener.getFunction());
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

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout3 = new GridLayout();
		gRunTime = JOE_G_RunTimeForm_RunTime.Control(new Group(this, SWT.NONE));
		gRunTime.setLayout(gridLayout3);
		createPeriodForm();
		groupStartTimeFuction = JOE_G_RunTimeForm_StartTimeFunction.Control(new Group(gRunTime, SWT.NONE));
		groupStartTimeFuction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		final GridLayout gridLayout = new GridLayout();
		groupStartTimeFuction.setLayout(gridLayout);
		tFunction = JOE_T_RunTimeForm_StartTimeFunction.Control(new Text(groupStartTimeFuction, SWT.BORDER));
		tFunction.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (init){
					return;
				}
				setEnabled();
				listener.setFunction(tFunction.getText());
				_gui.updateFont();
				_gui.updateRunTime();
			}
		});
		final GridData gridData10_1_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData10_1_1.widthHint = 243;
		tFunction.setLayoutData(gridData10_1_1);
		groupSchedule = JOE_G_RunTimeForm_Schedule.Control(new Group(gRunTime, SWT.NONE));
		groupSchedule.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 2;
		groupSchedule.setLayout(gridLayout_2);
		comSchedule = JOE_Cbo_RunTimeForm_Schedule.Control(new Combo(groupSchedule, SWT.NONE));
		comSchedule.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (init){
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
				if (init){
					return;
				}
				setEnabled();
				listener.setSchedule(comSchedule.getText());
				_gui.updateFont();
				_gui.updateRunTime();
			}
		});
		butBrowse = JOE_B_RunTimeForm_Browse.Control(new Button(groupSchedule, SWT.NONE));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String name = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_SCHEDULE);
				if (name != null && name.length() > 0){
					comSchedule.setText(name);
				}
			}
		});
		gComment = JOE_G_RunTimeForm_Comment.Control(new Group(gRunTime, SWT.NONE));
		gComment.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true));
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
				if (init){
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
				if (text != null){
					tComment.setText(text);
				}
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
		setEnabled();
	}

	/**
	 * This method initializes periodForm
	 */
	private void createPeriodForm() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
		gridData2.widthHint = 151;
		periodForm = new PeriodForm(gRunTime, SWT.NONE, JOEConstants.RUNTIME);
		periodForm.setLayoutData(gridData2);
		periodForm.setSchedulerUpdate(_gui);
	}

	public void setToolTipText() {
		//        
	}

	private void setEnabled() {
		if (init) {
			//initialisierung
			if (comSchedule.getText().trim().length() > 0) {
				groupSchedule.setEnabled(true);
				groupStartTimeFuction.setEnabled(false);
				periodForm.setEnabled(false);
			} else
				if (tFunction.getText().trim().length() > 0) {
					groupSchedule.setEnabled(false);
					groupStartTimeFuction.setEnabled(true);
					periodForm.setEnabled(false);
				} else {
					groupSchedule.setEnabled(true);
					groupStartTimeFuction.setEnabled(true);
					periodForm.setEnabled(true);
				}
			return;
		}
		boolean enable = true;
		if (comSchedule.getText().trim().length() > 0) {
			groupSchedule.setEnabled(true);
			groupStartTimeFuction.setEnabled(false);
			enable = false;
		}
		else
			if (tFunction.getText().trim().length() > 0) {
				groupSchedule.setEnabled(false);
				groupStartTimeFuction.setEnabled(true);
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
				groupStartTimeFuction.setEnabled(true);
			}
		if (!enable) {
			if (runTimeBackUpElem == null) {
				runTimeBackUpElem = (Element) listener.getRunTime().clone();
				listener.getRunTime().removeContent();
				listener.getRunTime().getAttributes().clear();
			}
		}
		periodForm.setEnabled(enable);
		 
	}
	 
} // @jve:decl-index=0:visual-constraint="10,10"
