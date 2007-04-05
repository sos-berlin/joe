/**
 * Created on 06.03.2007
 *
 * Wizzard: Typ des Schedulers wird angegeben. Standalone Job oder Order Job
 * 
 *  @author mo
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.DatePicker;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.*;

public class JobAssistentRunTimeForms {
	
	private Element          job                = null;
	
	private SchedulerDom     dom                = null;
	
	private ISchedulerUpdate update             = null;
	
	private Button           butCancel          = null;
	
	private Button           butShow            = null;
	
	private Button           butNext            = null;
	
	private Text             txtRunTimeGlobal   = null; 
	
	private Shell            runTimeSingleShell = null;
	
	private Combo            comboMonth         = null;
	
	private DatePicker       txtSpeDay          = null;
	
	private Combo            comboEveryWeekdays = null;
	
	private Button           optEveryDay        = null;
	
	private Button           optEveryWeeksdays  = null;
	
	private Button           optEveryMonths     = null;
	
	private Button           optSpecificDay     = null;
	
	private List             list               = null; 
	
	private Button           butAdd             = null;
	
	private Button           butRemove          = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int              assistentType      = -1; 
	
	private Text             txtDayAtHour       = null;
	
	private Text             txtDayAtMinutes    = null;
	
	private Text             txtDayAtSecound    = null; 
	
	private Text             txtSpeDayHour      = null;
	
	private Text             txtSpeDayAtMinutes = null;
	
	private Text             txtSpeDayAtSecound = null;
	
	private Text             txtWeekAtHour      = null;
	
	private Text             txtWeekAtMinutes   = null;
	
	private Text             txtWeekAtSecound   = null;
	
	private Text             txtMonthAtHour     = null;
	
	private Text             txtMonthAtMinutes  = null;
	
	private Text             txtMonthAtSecound  = null;
	
	private PeriodsListener  periodslistener    = null;
	
	private PeriodListener   periodlistener     = null;	
	
	private DaysListener     weekDayListener    = null;
	
	private DaysListener     monthListener      = null;
	
	private DateListener     speDateListener    = null;
	
	private static String    EVERY_DAY          = "Every Day ";
	
	private static String    SPECIFIC_DAY       = "Specific Day ";		
	
	private static String    WEEK_DAY           = "Week Day ";
	
	private static String    MONTH_DAY          = "Month Day";
	
	private Element          jobBackUp          = null;              		
	
	
	/**
	 * Konstruktor 
	 * @param dom_ - Type SchedulerDom 
	 * @param update_ - Type ISchedulerUpdate
	 */
	public JobAssistentRunTimeForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		job = job_;		
		assistentType = assistentType_;
		jobBackUp = (Element)job_.clone();
		init();
	}
	
	private void init() {
		periodslistener = new PeriodsListener(dom, job.getChild("run_time"));
		periodlistener = new PeriodListener(dom);
		speDateListener = new DateListener(dom, job.getChild("run_time"), 1);
		weekDayListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.WEEKDAYS);
		monthListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.MONTHDAYS);
	}
	
	public void showRunTimeForms() {
		try {
			
			runTimeSingleShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
			runTimeSingleShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			gridLayout.numColumns = 2;
			runTimeSingleShell.setLayout(gridLayout);
			runTimeSingleShell.setSize(553, 489);
			runTimeSingleShell.setText("Run Time/ Single Starts"); 
			
			{
				final Group jobGroup = new Group(runTimeSingleShell, SWT.NONE);
				jobGroup.setText("Job");
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
				gridData_1.heightHint = 390;
				gridData_1.widthHint = 517;
				jobGroup.setLayoutData(gridData_1);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.numColumns = 9;
				gridLayout_1.marginWidth = 10;
				gridLayout_1.marginTop = 10;
				gridLayout_1.marginRight = 10;
				gridLayout_1.marginLeft = 10;
				gridLayout_1.marginHeight = 10;
				gridLayout_1.marginBottom = 10;
				jobGroup.setLayout(gridLayout_1);
				
				{
					txtRunTimeGlobal = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
					txtRunTimeGlobal.setEditable(false);
					final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false, 9, 1);
					gridData.widthHint = 452;
					gridData.heightHint = 139;
					txtRunTimeGlobal.setLayoutData(gridData);
					txtRunTimeGlobal.setText(Messages.getString("assistent.run_time"));
				}
				
				{
					optEveryDay = new Button(jobGroup, SWT.CHECK);
					optEveryDay.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(optEveryDay.getSelection()) {								
								txtDayAtHour.setEnabled(true);
								txtDayAtMinutes.setEnabled(true);
								txtDayAtSecound.setEnabled(true);
								butAdd.setEnabled(true);
							} else {								
								txtDayAtHour.setEnabled(false);
								txtDayAtMinutes.setEnabled(false);
								txtDayAtSecound.setEnabled(false);
							}
						}
					});
					optEveryDay.setText(EVERY_DAY);
				}
				new Label(jobGroup, SWT.NONE);
				
				final Label atLabel = new Label(jobGroup, SWT.NONE);
				atLabel.setAlignment(SWT.RIGHT);
				final GridData gridData_15 = new GridData(GridData.END, GridData.CENTER, false, false);
				gridData_15.widthHint = 28;
				atLabel.setLayoutData(gridData_15);
				atLabel.setText("at");
				
				txtDayAtHour = new Text(jobGroup, SWT.CENTER | SWT.BORDER);				
				txtDayAtHour.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						if(!checkTime(txtDayAtHour.getText(), "hour")) {
							txtDayAtHour.setBackground(Options.getRequiredColor());
							txtDayAtHour.setFocus();
						} else {
							txtDayAtHour.setBackground(null);
						}
					}
				});
				
				final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData.minimumWidth = 25;
				txtDayAtHour.setLayoutData(gridData);
				
				final Label label = new Label(jobGroup, SWT.NONE);
				label.setLayoutData(new GridData());
				label.setText(":");
				
				txtDayAtMinutes = new Text(jobGroup, SWT.CENTER | SWT.BORDER);				
				txtDayAtMinutes.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						if(!checkTime(txtDayAtMinutes.getText(), "minutes")) {
							txtDayAtMinutes.setBackground(Options.getRequiredColor());
							txtDayAtMinutes.setFocus();
						} else {
							txtDayAtMinutes.setBackground(null);
						}
					}
				});
				
				final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_3.widthHint = 12;
				gridData_3.minimumWidth = 25;
				txtDayAtMinutes.setLayoutData(gridData_3);
				
				final Label label_1 = new Label(jobGroup, SWT.NONE);
				label_1.setLayoutData(new GridData());
				label_1.setText(":");
				
				txtDayAtSecound = new Text(jobGroup, SWT.CENTER | SWT.BORDER);				
				txtDayAtSecound.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						if(!checkTime(txtDayAtSecound.getText(), "secound")) {
							txtDayAtSecound.setBackground(Options.getRequiredColor());
							txtDayAtSecound.setFocus();
						} else {
							txtDayAtSecound.setBackground(null);
						}
					}
				});
				final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_4.minimumWidth = 25;
				txtDayAtSecound.setLayoutData(gridData_4);
				
				{
					final Label hhmmssLabel = new Label(jobGroup, SWT.NONE);
					hhmmssLabel.setLayoutData(new GridData());
					hhmmssLabel.setText("hh:mm:ss");
				}
				
				{
					optSpecificDay = new Button(jobGroup, SWT.CHECK);
					optSpecificDay.setLayoutData(new GridData());
					optSpecificDay.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(optSpecificDay.getSelection()) {
								txtSpeDay.setEnabled(true);
								txtSpeDayHour.setEnabled(true);
								txtSpeDayAtMinutes.setEnabled(true);
								txtSpeDayAtSecound.setEnabled(true);
								butAdd.setEnabled(true);
							} else {
								txtSpeDay.setEnabled(false);
								txtSpeDayHour.setEnabled(false);
								txtSpeDayAtMinutes.setEnabled(false);
								txtSpeDayAtSecound.setEnabled(false);
							}
						}
					});
					optSpecificDay.setText(SPECIFIC_DAY);
				}
				
				txtSpeDay = new DatePicker(jobGroup, SWT.NONE);
				final GridData gridData_16 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_16.widthHint = 131;
				txtSpeDay.setLayoutData(gridData_16);
				txtSpeDay.setEnabled(false);
				
				final Label atLabel_1 = new Label(jobGroup, SWT.NONE);
				atLabel_1.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
				atLabel_1.setText("at");
				
				txtSpeDayHour = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtSpeDayHour.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						if(!checkTime(txtSpeDayHour.getText(), "hour")) {
							txtSpeDayHour.setBackground(Options.getRequiredColor());
							txtSpeDayHour.setFocus();
						} else {
							txtSpeDayHour.setBackground(null);
						}
					}
				});
				
				final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_5.minimumWidth = 25;
				gridData_5.widthHint = 0;
				txtSpeDayHour.setLayoutData(gridData_5);
				
				final Label label_2 = new Label(jobGroup, SWT.NONE);
				label_2.setLayoutData(new GridData());
				label_2.setText(":");
				
				txtSpeDayAtMinutes = new Text(jobGroup, SWT.CENTER | SWT.BORDER);				
				
				txtSpeDayAtMinutes.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						
						if(!checkTime(txtSpeDayHour.getText(), "minutes")) {
							txtSpeDayAtMinutes.setBackground(Options.getRequiredColor());
							txtSpeDayAtMinutes.setFocus();
						}else {
							txtSpeDayAtMinutes.setBackground(null);
						}
						
					}
				});
				final GridData gridData_3_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_3_1.widthHint = 7;
				gridData_3_1.minimumWidth = 25;
				txtSpeDayAtMinutes.setLayoutData(gridData_3_1);
				
				final Label label_1_1 = new Label(jobGroup, SWT.NONE);
				label_1_1.setLayoutData(new GridData());
				label_1_1.setText(":");
				
				txtSpeDayAtSecound = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtSpeDayAtSecound.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {							
						if(!checkTime(txtSpeDayAtSecound.getText(), "secound")) {
							txtSpeDayAtSecound.setBackground(Options.getRequiredColor());
							txtSpeDayAtSecound.setFocus();
						} else {
							txtSpeDayAtSecound.setBackground(null);
						}
					}
				});
				
				final GridData gridData_4_1 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_4_1.minimumWidth = 25;
				txtSpeDayAtSecound.setLayoutData(gridData_4_1);
				
				final Label hhmmssLabel_1 = new Label(jobGroup, SWT.NONE);
				hhmmssLabel_1.setLayoutData(new GridData());
				hhmmssLabel_1.setText("hh:mm:ss");
				
				optEveryWeeksdays = new Button(jobGroup, SWT.CHECK);
				optEveryWeeksdays.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(optEveryWeeksdays.getSelection()) {
							butAdd.setEnabled(true);
							comboEveryWeekdays.setEnabled(true);
							txtWeekAtHour.setEnabled(true);
							txtWeekAtMinutes.setEnabled(true);
							txtWeekAtSecound.setEnabled(true);
						} else {
							comboEveryWeekdays.setEnabled(false);
							txtWeekAtHour.setEnabled(false);
							txtWeekAtMinutes.setEnabled(false);
							txtWeekAtSecound.setEnabled(false);
						}
					}
				});
				optEveryWeeksdays.setText(WEEK_DAY);
				
				comboEveryWeekdays = new Combo(jobGroup, SWT.NONE);				
				comboEveryWeekdays.setItems(DaysListener.getWeekdays());
				comboEveryWeekdays.select(0);
				comboEveryWeekdays.setEnabled(false);
				final GridData gridData_17 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_17.widthHint = 148;
				comboEveryWeekdays.setLayoutData(gridData_17);
				
				final Label atLabel_2 = new Label(jobGroup, SWT.NONE);
				atLabel_2.setLayoutData(new GridData(37, SWT.DEFAULT));
				atLabel_2.setAlignment(SWT.RIGHT);
				atLabel_2.setText("at");
				
				txtWeekAtHour = new Text(jobGroup, SWT.CENTER | SWT.BORDER);				
				txtWeekAtHour.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtWeekAtHour.getText(), "hour")) {
							txtWeekAtHour.setBackground(Options.getRequiredColor());
							txtWeekAtHour.setFocus();
						} else {
							txtWeekAtHour.setBackground(null);
						}
					}
				});
				final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_6.minimumWidth = 25;
				gridData_6.widthHint = 0;
				txtWeekAtHour.setLayoutData(gridData_6);
				
				final Label label_3 = new Label(jobGroup, SWT.NONE);
				label_3.setLayoutData(new GridData());
				label_3.setText(":");
				
				txtWeekAtMinutes = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtWeekAtMinutes.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtWeekAtMinutes.getText(), "minutes")) {
							txtWeekAtMinutes.setBackground(Options.getRequiredColor());
							txtWeekAtMinutes.setFocus();
						}else {
							txtWeekAtMinutes.setBackground(null);
						}
					}
				});
				final GridData gridData_11 = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData_11.widthHint = 5;
				gridData_11.minimumWidth = 25;
				
				final GridData gridData_9 = new GridData(11, SWT.DEFAULT);
				gridData_9.minimumWidth = 25;
				
				final GridData gridData_3_2 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_3_2.minimumWidth = 25;
				txtWeekAtMinutes.setLayoutData(gridData_3_2);
				
				final Label label_1_2 = new Label(jobGroup, SWT.NONE);
				label_1_2.setLayoutData(new GridData());
				label_1_2.setText(":");
				
				txtWeekAtSecound = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtWeekAtSecound.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtWeekAtSecound.getText(), "secound")) {
							txtWeekAtSecound.setBackground(Options.getRequiredColor());
							txtWeekAtSecound.setFocus();
						} else {
							txtWeekAtSecound.setBackground(null);
						}
					}
				});
				final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData_12.widthHint = 2;
				gridData_12.minimumWidth = 25;
				
				final GridData gridData_10 = new GridData(10, SWT.DEFAULT);
				gridData_10.minimumHeight = 25;
				
				final GridData gridData_4_2 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_4_2.minimumWidth = 25;
				txtWeekAtSecound.setLayoutData(gridData_4_2);
				
				final Label hhmmssLabel_2 = new Label(jobGroup, SWT.NONE);
				hhmmssLabel_2.setLayoutData(new GridData());
				hhmmssLabel_2.setText("hh:mm:ss");
				
				{
					optEveryMonths = new Button(jobGroup, SWT.CHECK);
					optEveryMonths.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							if(optEveryMonths.getSelection()) {
								butAdd.setEnabled(true);
								comboMonth.setEnabled(true);
								txtMonthAtHour.setEnabled(true);
								txtMonthAtMinutes.setEnabled(true);
								txtMonthAtSecound.setEnabled(true);
							} else {
								comboMonth.setEnabled(false);
								txtMonthAtHour.setEnabled(false);
								txtMonthAtMinutes.setEnabled(false);
								txtMonthAtSecound.setEnabled(false);
							}
						}
					});
					optEveryMonths.setText(MONTH_DAY);
				}
				
				{
					comboMonth = new Combo(jobGroup, SWT.NONE);					
					comboMonth.setItems(DaysListener.getMonthdays());
					comboMonth.select(0);
					comboMonth.setEnabled(false);
					comboMonth.select(0);
					comboMonth.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				}
				
				final Label atLabel_3 = new Label(jobGroup, SWT.NONE);
				atLabel_3.setLayoutData(new GridData(37, SWT.DEFAULT));
				atLabel_3.setAlignment(SWT.RIGHT);
				atLabel_3.setText("at");
				
				txtMonthAtHour = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtMonthAtHour.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtMonthAtHour.getText(), "hour")) {
							txtMonthAtHour.setBackground(Options.getRequiredColor());
							txtMonthAtHour.setFocus();
						} else {
							txtMonthAtHour.setBackground(null);
						}
					}
				});
				final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData_13.widthHint = 11;
				
				final GridData gridData_8 = new GridData(13, SWT.DEFAULT);
				gridData_8.minimumWidth = 25;
				
				
				final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
				gridData_7.minimumWidth = 25;
				gridData_7.widthHint = 0;
				txtMonthAtHour.setLayoutData(gridData_7);
				
				final Label label_4 = new Label(jobGroup, SWT.NONE);
				label_4.setLayoutData(new GridData());
				label_4.setText(":");
				
				txtMonthAtMinutes = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtMonthAtMinutes.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtMonthAtMinutes.getText(), "minutes")) {
							txtMonthAtMinutes.setBackground(Options.getRequiredColor());
							txtMonthAtMinutes.setFocus();
						} else {
							txtMonthAtMinutes.setBackground(null);
						}
					}
				});
				final GridData gridData_14 = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData_14.widthHint = 15;
				gridData_14.minimumHeight = 25;
				
				
				final GridData gridData_3_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_3_3.minimumWidth = 25;
				txtMonthAtMinutes.setLayoutData(gridData_3_3);
				
				final Label label_1_3 = new Label(jobGroup, SWT.NONE);
				label_1_3.setLayoutData(new GridData());
				label_1_3.setText(":");
				
				txtMonthAtSecound = new Text(jobGroup, SWT.CENTER | SWT.BORDER);
				txtMonthAtSecound.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {						
						if(!checkTime(txtMonthAtSecound.getText(), "minutes")) {
							txtMonthAtSecound.setBackground(Options.getRequiredColor());
							txtMonthAtSecound.setFocus();
						}else {
							txtMonthAtSecound.setBackground(null);
						}
					}
				});
				
				final GridData gridData_4_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
				gridData_4_3.minimumWidth = 25;
				txtMonthAtSecound.setLayoutData(gridData_4_3);
				
				final Label hhmmssLabel_3 = new Label(jobGroup, SWT.NONE);
				hhmmssLabel_3.setLayoutData(new GridData());
				hhmmssLabel_3.setText("hh:mm:ss");
				
				list = new List(jobGroup, SWT.BORDER);
				list.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(list.getSelectionIndex()> 0) {
							butRemove.setEnabled(true);
						}
					}
				});
				final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 8, 2);
				gridData_2.heightHint = 104;
				list.setLayoutData(gridData_2);
				
				{
					butAdd = new Button(jobGroup, SWT.NONE);
					butAdd.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							addPeriod();														
						}
					});
					butAdd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
					butAdd.setText("Add");
				}
				
				butRemove = new Button(jobGroup, SWT.NONE);
				butRemove.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(list.getSelectionCount() > 0) {
							delete();														
							list.remove(list.getSelectionIndex());																					
						}
					}
				});
				butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				butRemove.setText("Remove");
			}
			
			
			{
				butCancel = new Button(runTimeSingleShell, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						close();
					}
				});
				butCancel.setText("Close");
			}
			
			
			
			{
				final Composite composite = new Composite(runTimeSingleShell, SWT.NONE);
				final GridData gridData = new GridData(GridData.END, GridData.FILL, false, false);
				gridData.heightHint = 29;
				composite.setLayoutData(gridData);
				final GridLayout gridLayout_1 = new GridLayout();
				gridLayout_1.numColumns = 2;
				composite.setLayout(gridLayout_1);
				
				{
					butShow = new Button(composite, SWT.NONE);
					butShow.setVisible(false);
					butShow.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							MainWindow.message(runTimeSingleShell, Utils.getElementAsString(job), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
					butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
					final GridData gridData_1 = new GridData(GridData.END, GridData.CENTER, false, false);
					gridData_1.widthHint = 57;
					butNext.setLayoutData(gridData_1);
					butNext.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							
							runTimeSingleShell.dispose();														
						}
					});
					butNext.setText("Apply");
				}
			}
			
			
			setEnabled(false);
			setToolTipText();						
			
			fillList();
			
			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			
			runTimeSingleShell.setBounds((screen.width - runTimeSingleShell.getBounds().width) /2, 
					(screen.height - runTimeSingleShell.getBounds().height) /2, 
					runTimeSingleShell.getBounds().width, 
					runTimeSingleShell.getBounds().height);
			runTimeSingleShell.open();
			runTimeSingleShell.layout();
			runTimeSingleShell.pack();		
		} catch (Exception e) {
			System.err.println("..error in JobAssistentRuntimeForms.showRunTimeForms() " + e.getMessage());
		}
	}
	
	private void setEnabled(boolean enabled ) {
		optEveryDay.setSelection(false);
		optSpecificDay.setSelection(false);
		optEveryWeeksdays.setSelection(false);
		optEveryMonths.setSelection(false);
						
		comboEveryWeekdays.setText("");
		comboMonth.setText("");
		
		
		txtSpeDay.setEnabled(false);
		comboEveryWeekdays.setEnabled(false);
		comboMonth.setEnabled(false);
		
		
		txtDayAtHour.setEnabled(false);
		txtDayAtMinutes.setEnabled(false);
		txtDayAtSecound.setEnabled(false);
		
		txtSpeDay.setEnabled(false);
		txtSpeDayHour.setEnabled(false);
		txtSpeDayAtMinutes.setEnabled(false);
		txtSpeDayAtSecound.setEnabled(false);
		
		comboEveryWeekdays.setEnabled(false);
		txtWeekAtHour.setEnabled(false);
		txtWeekAtMinutes.setEnabled(false);
		txtWeekAtSecound.setEnabled(false);
		
		comboMonth.setEnabled(false);
		txtMonthAtHour.setEnabled(false);
		txtMonthAtMinutes.setEnabled(false);
		txtMonthAtSecound.setEnabled(false);
		
		txtDayAtHour.setText("");
		txtDayAtMinutes.setText("");
		txtDayAtSecound.setText("");		
		txtSpeDayHour.setText("");      		
		txtSpeDayAtMinutes.setText(""); 		
		txtSpeDayAtSecound.setText(""); 		
		txtWeekAtHour.setText("");		
		txtWeekAtMinutes.setText(""); 		
		txtWeekAtSecound.setText("");
		txtMonthAtHour.setText("");
		txtMonthAtMinutes.setText("");
		txtMonthAtSecound.setText("");
	}
	
	
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.close"));
		butNext.setToolTipText(Messages.getTooltip("assistent.apply"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		comboMonth.setToolTipText(Messages.getTooltip("assistent.run_time.month"));
		txtSpeDay.setToolTipText(Messages.getTooltip("assistent.run_time.specific_day"));
		comboEveryWeekdays.setToolTipText(Messages.getTooltip("assistent.run_time.week"));
		butAdd.setToolTipText(Messages.getTooltip("assistent.run_time.add"));
		txtDayAtHour.setToolTipText(Messages.getTooltip("assistent.run_time.hours"));
		txtDayAtMinutes.setToolTipText(Messages.getTooltip("assistent.run_time.minutes"));
		txtDayAtSecound.setToolTipText(Messages.getTooltip("assistent.run_time.secounds"));		
		txtSpeDayHour.setToolTipText(Messages.getTooltip("assistent.run_time.hours"));      		
		txtSpeDayAtMinutes.setToolTipText(Messages.getTooltip("assistent.run_time.minutes")); 		
		txtSpeDayAtSecound.setToolTipText(Messages.getTooltip("assistent.run_time.secounds")); 		
		txtWeekAtHour.setToolTipText(Messages.getTooltip("assistent.run_time.hours"));		
		txtWeekAtMinutes.setToolTipText(Messages.getTooltip("assistent.run_time.minutes")); 		
		txtWeekAtSecound.setToolTipText(Messages.getTooltip("assistent.run_time.secounds"));
		txtMonthAtHour.setToolTipText(Messages.getTooltip("assistent.run_time.hours"));		
		txtMonthAtMinutes.setToolTipText(Messages.getTooltip("assistent.run_time.minutes"));
		txtMonthAtSecound.setToolTipText(Messages.getTooltip("assistent.run_time.secounds"));
	}
	
	
	
	private void addPeriod() {
		
		
		
		if(optEveryDay.getSelection()) {
			if(txtDayAtHour.getText().concat(txtDayAtMinutes.getText()).concat(txtDayAtSecound.getText()).trim().length()>0)  {
				String str = EVERY_DAY + "at " +  Utils.getTime(23, txtDayAtHour.getText(), txtDayAtMinutes.getText(), txtDayAtSecound.getText(), false);
				if(!periodExist(str)) {
					savePeriod(EVERY_DAY);
					list.add(str);
				}
			}								
		}
		
		if(optSpecificDay.getSelection()) {
			if(txtSpeDay.getISODate() != null && txtSpeDay.getISODate().trim().length() > 0){				
				savePeriod(SPECIFIC_DAY);
				list.add(SPECIFIC_DAY + txtSpeDay.getISODate() + " at " +  Utils.getTime(23, txtSpeDayHour.getText(), txtSpeDayAtMinutes.getText(), txtSpeDayAtSecound.getText(), false));
			} 
		}
		
		if(optEveryWeeksdays.getSelection()){
			if(comboEveryWeekdays.getText()!= null && comboEveryWeekdays.getText().trim().length() >0){
				
				savePeriod(WEEK_DAY);
				list.add(WEEK_DAY + comboEveryWeekdays.getText() + " at " +  Utils.getTime(23, txtWeekAtHour.getText(), txtWeekAtMinutes.getText(), txtWeekAtSecound.getText(), false));
			}
		}
		
		if(optEveryMonths.getSelection()){
			if(comboMonth.getText() !=null && comboMonth.getText().trim().length() > 0){				
				savePeriod(MONTH_DAY);
				list.add(MONTH_DAY + comboMonth.getText() + " at " +  Utils.getTime(23, txtMonthAtHour.getText(), txtMonthAtMinutes.getText(), txtMonthAtSecound.getText(), false));
			}
		}
		setEnabled(false);
		
	}	
	
	private boolean periodExist(String str) {
		for(int i =0; i < list.getItemCount(); i++) {
			String currStr = list.getItem(i);
			if(currStr.equalsIgnoreCase(str)) {
				MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.run_time.period_exist"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				return true;
			}
		}
		return false;
	}
	
	private void  fillList(){
		
		Element run_time = job.getChild("run_time");
		if(run_time == null) return;
		
		//every day
		java.util.List everyDay = periodslistener.get_list();
		for(int i =0; everyDay != null && i < everyDay.size(); i++) {
			Element period = (Element)everyDay.get(i);
			PeriodListener p = new PeriodListener(dom);
			p.setPeriod(period);
			if(p.getBegin() == null || p.getBegin().trim().length() == 0) {
				list.add(EVERY_DAY + "at " + p.getSingle() );
			}
		}
		
		//specific day		
		java.util.List speDays = speDateListener.get_list();
		for(int i =0; speDays != null && i < speDays.size(); i++) {
			Element speElem = (Element)speDays.get(i);
			int[] da = speDateListener.getDate(i);
			java.util.List periods = speElem.getChildren("period");			
			for (int j =0; periods != null && j < periods.size(); j++) {
				Element period = (Element)periods.get(j);
				PeriodListener p = new PeriodListener(dom);
				p.setPeriod(period);	
				if(p.getBegin() == null || p.getBegin().trim().length() == 0) {
					list.add(SPECIFIC_DAY + Utils.asStr(da[2]) + "-" + Utils.asStr(da[1]) + "-" + Utils.asStr(da[0])   +  " at " + p.getSingle() );
				}
			}
		}
		
		//Week day				
		Element[] weekDays = weekDayListener.getDayElements();
		for(int i =0; weekDays!=null&& i < weekDays.length; i++) {
			Element elWeek = weekDays[i];
			String sWeek = comboEveryWeekdays.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) -1);
			java.util.List periods = elWeek.getChildren("period");			
			for (int j =0; periods != null && j < periods.size(); j++) {
				Element period = (Element)periods.get(j);
				PeriodListener p = new PeriodListener(dom);
				p.setPeriod(period);
				if(p.getBegin() == null || p.getBegin().trim().length() == 0) {
					list.add(WEEK_DAY + sWeek +  " at " + p.getSingle() );
				}
			}	
		}
		
		//Month day		
		Element[] monthDays = monthListener.getDayElements();
		for(int i =0; monthDays!=null&& i < monthDays.length; i++) {
			Element elMonth = monthDays[i];
			String sMonth = comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", elMonth)) -1);
			java.util.List periods = elMonth.getChildren("period");			
			for (int j =0; periods != null && j < periods.size(); j++) {
				Element period = (Element)periods.get(j);
				PeriodListener p = new PeriodListener(dom);
				p.setPeriod(period);	
				if(p.getBegin() == null || p.getBegin().trim().length() == 0) {
					list.add(MONTH_DAY + sMonth +  " at " + p.getSingle() );
				}
			}	
		}
		
		
	}
	
	private void deleteAll() {
		java.util.List everyDay = periodslistener.get_list();
		
		//every day löschen
		for(int i =0; everyDay != null && i < everyDay.size(); i++) {
			periodslistener.removePeriod(i);		
		}
		
		//specific day löschen
		java.util.List speDays = speDateListener.get_list();
		for(int i =0; speDays != null && i < speDays.size(); i++) {
			Element speElem = (Element)speDays.get(i);
			speDateListener.removeDate(i);		
		}
		update.updateDays(3);
		
		//Wochentag löschen
		Element[] weekDays = weekDayListener.getDayElements();
		for(int i =0; weekDays!=null&& i < weekDays.length; i++) {
			Element elWeek = weekDays[i];
			String sWeek = comboEveryWeekdays.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) -1);			
			weekDayListener.deleteDay(sWeek);			
		}
		update.updateDays(DaysListener.WEEKDAYS);
		
		//Monattag löschen
		Element[] monthDays = monthListener.getDayElements();
		for(int i =0; monthDays!=null&& i < monthDays.length; i++) {
			Element elMonth = monthDays[i];
			String sMonth = comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", elMonth)) -1);			
			monthListener.deleteDay(sMonth);			
		}
		update.updateDays(DaysListener.MONTHDAYS);
		
		
	}
	
	private void delete() {
		//list.remove(list.getSelectionIndex());
		String selectedStr = list.getItem(list.getSelectionIndex());
		
		if(selectedStr.startsWith(EVERY_DAY)) {
			//Every day
			deleteEveryDay(selectedStr);
		}else if(selectedStr.startsWith(SPECIFIC_DAY)) {
			//Specific Day entfernen
			deleteSpeDay(selectedStr);
		} else if(selectedStr.startsWith(WEEK_DAY)) {
			//Wochentag entfernen
			deleteWeek(selectedStr);
		} else if(selectedStr.startsWith(MONTH_DAY)) {
			//Monat entfernen
			deleteMonth(selectedStr);
		}
		
	}
	
	private void deleteEveryDay(String selectedStr) {
		java.util.List everyDay = periodslistener.get_list();
		
		for(int i =0; everyDay != null && i < everyDay.size(); i++) {
			Element period = (Element)everyDay.get(i);
			PeriodListener p = new PeriodListener(dom);
			p.setPeriod(period);			
			if(selectedStr.equals(EVERY_DAY + "at " + p.getSingle())) {
				//PeriodsListener _pl = new PeriodsListener(dom, period );
				periodslistener.removePeriod(i);
			}
		}
		//update.updateDays()
	}
	
	
	private void deleteSpeDay(String selectedStr) {
		java.util.List speDays = speDateListener.get_list();
		for(int i =0; speDays != null && i < speDays.size(); i++) {
			Element speElem = (Element)speDays.get(i);
			int[] da = speDateListener.getDate(i);
			java.util.List periods = speElem.getChildren("period");
			if(periods == null ||  periods.size() <=1  ) {
				//es existiert nur maximal eine Periode -> der Element kann gelöscht werden
				speDateListener.removeDate(i);
			} else {
				
				for (int j =0; periods != null && j < periods.size(); j++) {
					Element period = (Element)periods.get(j);
					PeriodListener p = new PeriodListener(dom);
					p.setPeriod(period);				
					if(selectedStr.equals(SPECIFIC_DAY + Utils.asStr(da[2]) + "-" + Utils.asStr(da[1]) + "-" + Utils.asStr(da[0])   +  " at " + p.getSingle() )){
						PeriodsListener _pl = new PeriodsListener(dom, speElem );
						_pl.removePeriod(j);
					}
				}
			}
		}
		update.updateDays(3);
	}
	
	private void deleteWeek(String selectedStr) {
		Element[] weekDays = weekDayListener.getDayElements();
		for(int i =0; weekDays!=null&& i < weekDays.length; i++) {
			Element elWeek = weekDays[i];
			String sWeek = comboEveryWeekdays.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) -1);
			if(selectedStr.indexOf(sWeek) > -1) {
				java.util.List periods = elWeek.getChildren("period");
				if(periods == null ||  periods.size() <=1  ) {
					//es existiert nur maximal eine Periode -> der Element kann gelöscht werden
					weekDayListener.deleteDay(sWeek);
				} else {
					//Der Wochentag hat mehr als eine Periode -> lösche den mit der gleichen Zeit
					for (int j =0; periods != null && j < periods.size(); j++) {
						Element period = (Element)periods.get(j);
						PeriodListener p = new PeriodListener(dom);
						p.setPeriod(period);	
						String time = selectedStr.substring(selectedStr.indexOf("at ") + 3);
						if(p.getSingle().endsWith(time)) {
							PeriodsListener _pl = new PeriodsListener(dom, elWeek );
							_pl.removePeriod(j);
						}							
					}	
				}
				
			}
			
		}
		update.updateDays(DaysListener.WEEKDAYS);
	} 
	
	private void deleteMonth(String selectedStr) {
		Element[] monthDays = monthListener.getDayElements();
		for(int i =0; monthDays!=null&& i < monthDays.length; i++) {
			Element elMonth = monthDays[i];
			String sMonth = comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", elMonth)) -1);
			if(selectedStr.indexOf(sMonth) > -1) {
				java.util.List periods = elMonth.getChildren("period");
				if(periods == null ||  periods.size() <=1  ) {
					//es existiert nur maximal eine Periode -> der Element kann gelöscht werden
					monthListener.deleteDay(sMonth);
				} else {
					//Der Monat hat mehr als eine Periode -> lösche den mit der gleichen Zeit
					for (int j =0; periods != null && j < periods.size(); j++) {
						Element period = (Element)periods.get(j);
						PeriodListener p = new PeriodListener(dom);
						p.setPeriod(period);	
						String time = selectedStr.substring(selectedStr.indexOf("at ") + 3);
						if(p.getSingle().endsWith(time)) {
							PeriodsListener _pl = new PeriodsListener(dom, elMonth );
							_pl.removePeriod(j);
						}							
					}	
				}
				
			}
			
		}
		update.updateDays(DaysListener.MONTHDAYS);
	} 
	
	
	
	private void savePeriod(String which ) {
		
		
		if(which.equals(EVERY_DAY)){
			Element period = periodslistener.getNewPeriod();		
			periodlistener.setPeriod(period);
			periodlistener.setPeriodTime(23, null, "single_start", txtDayAtHour.getText(), txtDayAtMinutes.getText(), txtDayAtSecound.getText());
			periodslistener.applyPeriod(period);
			
		}
		
		if(which.equals(SPECIFIC_DAY)) {
			
			String date = txtSpeDay.getISODate();
			String[] tdate = date.split("-");
			
			
			if (!speDateListener.exists(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0])) ) {
				speDateListener.addDate(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0]));   
			}
			java.util.List lastDate = job.getChild("run_time").getChildren("date");
			Element eDate =  (Element)lastDate.get(lastDate.size()-1);
			PeriodsListener p = new PeriodsListener(dom, eDate);
			Element period = p.getNewPeriod();		
			periodlistener.setPeriod(period);
			periodlistener.setPeriodTime(23, null, "single_start", txtSpeDayHour.getText(), txtSpeDayAtMinutes.getText(), txtSpeDayAtSecound.getText());
			p.applyPeriod(period);
			
			if (update != null)
				update.updateDays(3);
			
		}
		
		if(which.equals(WEEK_DAY)){
			String week = comboEveryWeekdays.getText();									
			Element day = null;						
			Element[] days =  weekDayListener.getDayElements();			
			if(days != null &&days.length > 0) {//gleich 1 bedeutet, das day Element gerade generiert wurde
				//überprüfe, ob der Wochentag bereits ausgewählt wurde
				for (int i =0; i < days.length; i++) {
					Element eday = (Element)days[i];
					if(Utils.str2int(Utils.getAttributeValue("day",eday)) == (comboEveryWeekdays.getSelectionIndex()+1)){
						day = eday;		
						break;
					}
				}
			}			
			if(day == null) {
				weekDayListener.addDay(week);
				day = weekDayListener.getDayElements()[weekDayListener.getDayElements().length - 1];//letzte Element				
			}			
			PeriodsListener p = new PeriodsListener(dom, day);
			Element period = p.getNewPeriod();			
			periodlistener.setPeriod(period);
			periodlistener.setPeriodTime(23, null, "single_start", txtWeekAtHour.getText(), txtWeekAtMinutes.getText(), txtWeekAtSecound.getText());
			p.applyPeriod(period);
			update.updateDays(DaysListener.WEEKDAYS);			
		}
		
		if(which.equals(MONTH_DAY)){
			String month = comboMonth.getText();
			
			Element day = null;			
			
			Element[] days =  monthListener.getDayElements();
			if(days != null && days.length > 0) {//gleich 1 bedeutet, das day Element gerade generiert wurde
				//überprüfe, ob der Wochentag bereits ausgewählt wurde
				for (int i =0; i < days.length; i++) {
					Element eday = (Element)days[i];
					if(Utils.str2int(Utils.getAttributeValue("day",eday)) == (comboMonth.getSelectionIndex()+1)){
						day = eday;		
						break;
					}
				}
			}
			if(day == null) {
				monthListener.addDay(month);
				day = monthListener.getDayElements()[monthListener.getDayElements().length - 1];//letzte Element
			}
			
			PeriodsListener p = new PeriodsListener(dom, day);
			Element period = p.getNewPeriod();
			periodlistener.setPeriod(period);
			periodlistener.setPeriodTime(23, null, "single_start", txtMonthAtHour.getText(), txtMonthAtMinutes.getText(), txtMonthAtSecound.getText());
			p.applyPeriod(period);
			update.updateDays(DaysListener.MONTHDAYS);
			
			
		}
		
	}
	
	private boolean checkTime(String time, String which ) {
		boolean retVal = true;
		if(time == null || time.trim().length()==0){
			return true;
		}
		if(!Utils.isOnlyDigits(time)) {
			MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.no_numeric"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			retVal= false;
		} else {
			int itime = Utils.str2int(time);
			if(which.equals("hour")) {
				Utils.str2int(time);
				if(itime < 0 || itime > 24) {
					MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.no_time"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					retVal= false;
				}
			}else if(which.equals("minutes") || which.equals("secound")) {
				Utils.str2int(time);
				if(itime < 0 || itime > 60) {
					MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.no_time"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					retVal= false;
				}
			} 
			
			if(time.trim().length() > 2) {
				MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.no_time"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				retVal= false;
			}
		}
		return retVal;
	}
	
	private void close() {
		int cont = MainWindow.message(runTimeSingleShell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK) {//Utils.getElementAsString((Element)jobBackUp);			
			job.setContent(jobBackUp.cloneContent());
			runTimeSingleShell.dispose();	
		}
	}
	
}
