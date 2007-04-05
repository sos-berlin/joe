/**
 * Created on 06.03.2007
 *
 * Wizzard: Typ des Schedulers wird angegeben. Standalone Job oder Order Job
 * 
 *  @author mo
 * 
 */
package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import sos.scheduler.editor.app.DatePicker;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.DaysListener;
import sos.scheduler.editor.conf.listeners.*;


public class JobAssistentPeriodForms {
	
	private List                   list                        = null;
	
	private Element                job                         = null;
	
	private SchedulerDom           dom                         = null;
	
	private ISchedulerUpdate       update                      = null;
	
	private Button                 butCancel                   = null;
	
	
	private Button                 butNext                     = null;
	
	private Shell                  jobTypeShell                = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int                    assistentType               = -1;		
	
	private PeriodForm             periodForm                  = null;	
	
	private PeriodForm             periodFormWeekDay           = null;
	
	private PeriodForm             periodFormMonthDay          = null;
	
	private PeriodForm             periodFormSpecificDay       = null;
	
	private PeriodsListener        periodsListener             = null;	
	
	private PeriodsListener        periodsListenerWeekDay      = null; 
	
	private PeriodsListener        periodsListenerMonthDay     = null;
	
	private PeriodsListener        periodsListenerSpecDay      = null;
	
	private HashMap                pListForWeekDays            = null;
	
	private HashMap                pListForMonthDays           = null;
	
	private TabFolder              tabFolder                   = null;
	
	private DaysListener           weekDayListener             = null;	
	
	private DaysListener           monthListener               = null;
	
	private DateListener           speDateListener             = null;
	
	private Combo                  comboWeekDay                = null;
	
	private Combo                  comboMonth                  = null;		
	
	private DatePicker             txtSpeDay                   = null;
	
	private Element                jobBackUp                   = null;
	
    public static String           EVERY_DAY                   = "Every Day ";	
    
    public static String           SPECIFIC_DAY                = "Specific Day ";			
    
    public static String           WEEK_DAY                    = "Week Day ";	
    
    public static String           MONTH_DAY                   = "Month Day ";    
    
    public static String           SPECIFIC_WEEK_DAY           = "Specific Weekday ";
	
    private Button                 addPeriodButton             = null;
    
    private Button                 removeButton                = null;
    
    private TabItem                everyDayTabItem             = null;
    
    private TabItem                weekdayTabItem              = null;
    
    private TabItem                monthDayTabItem             = null;
    
    private TabItem                specificDayTabItem          = null;
    
    private Button                 newPeriodButton             = null;
    
    private String                  bApply                       = "";                      
    
    	
	public JobAssistentPeriodForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		job = job_;
		jobBackUp = (Element)job_.clone();		
		assistentType = assistentType_;
		init();
	}
	
	private void init() {
		periodsListener = new PeriodsListener(dom, job.getChild("run_time"));		
		speDateListener = new DateListener(dom, job.getChild("run_time"), 1);
		weekDayListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.WEEKDAYS);
		periodsListenerSpecDay  = new PeriodsListener(dom, job.getChild("run_time"));
		monthListener = new DaysListener(dom, job.getChild("run_time"), DaysListener.MONTHDAYS);
	}
	
	public void showPeriodeForms() {
		try {
			
			jobTypeShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
			jobTypeShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			pListForWeekDays = new HashMap();
			pListForMonthDays = new HashMap();
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			gridLayout.numColumns = 2;
			jobTypeShell.setLayout(gridLayout);
			jobTypeShell.setSize(500, 661);
			jobTypeShell.setText("Run Time/ Periods"); 

			final Group jobGroup = new Group(jobTypeShell, SWT.NONE);
			jobGroup.setText("Job: " + Utils.getAttributeValue("name", job));
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
			gridData_2.heightHint = 261;
			jobGroup.setLayoutData(gridData_2);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.marginWidth = 10;
			gridLayout_2.marginTop = 10;
			gridLayout_2.marginRight = 10;
			gridLayout_2.marginLeft = 10;
			gridLayout_2.marginHeight = 10;
			gridLayout_2.marginBottom = 10;
			jobGroup.setLayout(gridLayout_2);

			final Text txtGlobal = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_1.widthHint = 419;
			gridData_1.heightHint = 232;
			txtGlobal.setLayoutData(gridData_1);
			txtGlobal.setEditable(false);
			txtGlobal.setText(Messages.getString("assistent.period"));
			
			
			{
				tabFolder = new TabFolder(jobTypeShell, SWT.NONE);				
				
				tabFolder.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
					    
														
						
						discardChanges();
						if(comboWeekDay != null)
							comboWeekDay.setText("");
						
						if(comboMonth != null)
							comboMonth.setText("");
						
						if(txtSpeDay!= null)
							txtSpeDay.setData(null);
						
					}
				});
				
				final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData.heightHint = 208;
				gridData.widthHint = 364;
				tabFolder.setLayoutData(gridData);
				
				{
					everyDayTabItem = new TabItem(tabFolder, SWT.NONE);
					
					everyDayTabItem.setText(JobAssistentPeriodForms.EVERY_DAY );
					
					{
						final Group group = new Group(tabFolder, SWT.NONE);
						group.setLayout(new GridLayout());
						everyDayTabItem.setControl(group);
						

						{
							newPeriodButton = new Button(group, SWT.NONE);
							newPeriodButton.setFocus();
							newPeriodButton.addSelectionListener(new SelectionAdapter() {
								
								public void widgetSelected(final SelectionEvent e) {
									
									Element period = periodsListener.getNewPeriod();
									periodForm.setPeriod(period);
									periodForm.setEnabled(true);
									addPeriodButton.setEnabled(true);
									bApply = JobAssistentPeriodForms.EVERY_DAY;
								}
							});
							newPeriodButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
							newPeriodButton.setText("New Period");
						}
						createPeriodForm(JobAssistentPeriodForms.EVERY_DAY, group, everyDayTabItem);
					}
				}
				
				{
					weekdayTabItem = new TabItem(tabFolder, SWT.NONE);
					
					weekdayTabItem.setText(JobAssistentPeriodForms.WEEK_DAY);
					
					{
						final Group group = new Group(tabFolder, SWT.NONE);
						final GridLayout gridLayout_1 = new GridLayout();
						group.setLayout(gridLayout_1);
						weekdayTabItem.setControl(group);
						
						{
							comboWeekDay = new Combo(group, SWT.NONE);
							comboWeekDay.setItems(DaysListener.getWeekdays());							
							createPeriodForm(JobAssistentPeriodForms.WEEK_DAY, group, weekdayTabItem);								
							comboWeekDay.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									
									discardChanges();
									if(comboWeekDay.getText() == null || comboWeekDay.getText().length() == 0)
									  return;
									getListener();	
									addPeriodButton.setEnabled(true);
									bApply = JobAssistentPeriodForms.WEEK_DAY;
								}
							});
							comboWeekDay.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
						}
					}
				}
				
				{
					monthDayTabItem = new TabItem(tabFolder, SWT.NONE);
					monthDayTabItem.setText(MONTH_DAY);
					{
						final Group group = new Group(tabFolder, SWT.NONE);
						group.setLayout(new GridLayout());
						monthDayTabItem.setControl(group);
						{
							comboMonth = new Combo(group, SWT.NONE);
							comboMonth.setItems(DaysListener.getMonthdays());
							comboMonth.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(final SelectionEvent e) {
									discardChanges();
									if(comboMonth.getText() == null || comboMonth.getText().length() == 0)
										  return;
									getMonthListener();
									addPeriodButton.setEnabled(true);
									bApply=JobAssistentPeriodForms.MONTH_DAY;
								}
							});
							comboMonth.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
						}
						createPeriodForm(JobAssistentPeriodForms.MONTH_DAY, group, monthDayTabItem);						
						
						
					}
				}
				
				{
					specificDayTabItem = new TabItem(tabFolder, SWT.NONE);
					specificDayTabItem.setText(SPECIFIC_DAY);
					{
						final Group group = new Group(tabFolder, SWT.NONE);
						group.setLayout(new GridLayout());
						specificDayTabItem.setControl(group);
						txtSpeDay = new DatePicker(group, SWT.NONE);
						txtSpeDay.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
						txtSpeDay.addModifyListener(new ModifyListener() {
							public void modifyText(final ModifyEvent e) {
								discardChanges();
								String date = txtSpeDay.getISODate();
								if(date == null|| date.length()==0) {
									MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.period.date"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
									return;
								}
								String[] tdate = date.split("-");
								if (!speDateListener.exists(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0])) ) {
									speDateListener.addDate(Utils.str2int(tdate[2]), Utils.str2int(tdate[1]), Utils.str2int(tdate[0]));   
								}
								java.util.List lastDate = job.getChild("run_time").getChildren("date");
								Element eDate =  (Element)lastDate.get(lastDate.size()-1);
								
								periodsListenerSpecDay = new PeriodsListener(dom, eDate);
								Element period = periodsListenerSpecDay.getNewPeriod();										
								periodFormSpecificDay.setPeriod(period);
								
								periodFormSpecificDay.setEnabled(true);
								txtSpeDay.setEnabled(true);
								addPeriodButton.setEnabled(true);
								bApply=JobAssistentPeriodForms.SPECIFIC_DAY;
							}
						});
												
						
						createPeriodForm(JobAssistentPeriodForms.SPECIFIC_DAY, group, specificDayTabItem);
					}
				}
			}
			tabFolder.setSelection(0);

			addPeriodButton = new Button(jobTypeShell, SWT.NONE);
			addPeriodButton.setEnabled(false);
			addPeriodButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			addPeriodButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					apply();
				}
			});
			addPeriodButton.setText("Add Period");
			
			{
				list = new List(jobTypeShell, SWT.V_SCROLL | SWT.BORDER);
				list.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(list.getSelectionIndex() > -1) {
							removeButton.setEnabled(true);
						}
					}
				});
				final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
				gridData.widthHint = 335;
				list.setLayoutData(gridData);
			}

			removeButton = new Button(jobTypeShell, SWT.NONE);
			removeButton.setEnabled(false);
			removeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(list.getSelectionCount() > 0) {
						delete();														
						list.remove(list.getSelectionIndex());		
						removeButton.setEnabled(false);
					}
				}
			});
			removeButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			removeButton.setText("Remove");
			{
				butCancel = new Button(jobTypeShell, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						close();					
					}
				});
				butCancel.setText("Close");
			}
			
			{
				butNext = new Button(jobTypeShell, SWT.NONE);
				butNext.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
				butNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						discardChanges();
						jobTypeShell.dispose();
						//apply();
					}								
				});
				butNext.setText("Apply");
				periodForm.setApplyButton(butNext);
			}
			
			
			
			
			
			fillList();
			
			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			
			jobTypeShell.setBounds((screen.width - jobTypeShell.getBounds().width) /2, 
					(screen.height - jobTypeShell.getBounds().height) /2, 
					jobTypeShell.getBounds().width, 
					jobTypeShell.getBounds().height);
			
			jobTypeShell.open();
			
			setToolTipText();
			jobTypeShell.layout();
			jobTypeShell.pack();		
		} catch (Exception e) {
			System.err.println("..error in JobAssistentPeriodFormss.showPeriodeForms() " + e.getMessage());
		}
	}
	
	/**
	 * This method initializes periodForm
	 */
	private void createPeriodForm(String which, Group group, TabItem item) {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		
		if(which.equalsIgnoreCase(JobAssistentPeriodForms.EVERY_DAY)) {			
			periodForm = new PeriodForm(group, SWT.NONE, true);
			periodForm.setBounds(0, 0,481, 216);						
			periodForm.setParams(dom, periodsListener.isOnOrder());
			periodForm.setLayoutData(gridData);
			periodForm.setEnabled(false);
			item.setControl(group);
		} else if(which.equalsIgnoreCase(JobAssistentPeriodForms.WEEK_DAY)) {
			
			periodFormWeekDay= new PeriodForm(group, SWT.NONE, true);
			periodFormWeekDay.setBounds(0, 0,481, 216);						
			periodFormWeekDay.setParams(dom, periodsListener.isOnOrder());
			periodFormWeekDay.setLayoutData(gridData);			
			periodFormWeekDay.setEnabled(false);
			item.setControl(group);
			
		} else if(which.equalsIgnoreCase(JobAssistentPeriodForms.MONTH_DAY)) {
			periodFormMonthDay= new PeriodForm(group, SWT.NONE, true);
			periodFormMonthDay.setBounds(0, 0,481, 216);						
			periodFormMonthDay.setParams(dom, periodsListener.isOnOrder());
			periodFormMonthDay.setLayoutData(gridData);			
			periodFormMonthDay.setEnabled(false);
			item.setControl(group);
		} else if(which.equalsIgnoreCase(JobAssistentPeriodForms.SPECIFIC_DAY)) {			
			periodFormSpecificDay = new PeriodForm(group, SWT.NONE, true);
			periodFormSpecificDay.setBounds(0, 0,481, 216);						
			periodFormSpecificDay.setParams(dom, periodsListener.isOnOrder());
			periodFormSpecificDay.setLayoutData(gridData);
			periodFormSpecificDay.setEnabled(false);
			item.setControl(group);
		}
		
	}
	
	
	private void getListener() {
		String week = comboWeekDay.getText();
		
		if(pListForWeekDays.get(week) != null && pListForWeekDays.get(week) instanceof PeriodsListener) {
			periodsListenerWeekDay = (PeriodsListener)pListForWeekDays.get(week);		
		} else {
			Element day = getDayElement();
			periodsListenerWeekDay = new PeriodsListener(dom, day);
			pListForWeekDays.put(week, periodsListenerWeekDay);
		}
		Element period = periodsListenerWeekDay.getNewPeriod();	
		periodFormWeekDay.setPeriod(period);		
		periodFormWeekDay.setEnabled(true);
	}
	
	private void getMonthListener() {
		String month = comboMonth.getText();
		
		if(pListForMonthDays.get(month) != null && pListForMonthDays.get(month) instanceof PeriodsListener) {
			periodsListenerMonthDay = (PeriodsListener)pListForMonthDays.get(month);			
		} else {
			Element day = getMonthElement();
			periodsListenerMonthDay = new PeriodsListener(dom, day);
			pListForMonthDays.put(month, periodsListenerMonthDay);
		}
		Element period = periodsListenerMonthDay.getNewPeriod();	
		periodFormMonthDay.setPeriod(period);		
		periodFormMonthDay.setEnabled(true);
	}
	
	private Element getMonthElement(){
		String month = comboMonth.getText();
		Element day = null;						
		Element[] days =  monthListener.getDayElements();			
		if(days != null &&days.length > 0) {//gleich 1 bedeutet, das day Element gerade generiert wurde
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
			periodsListenerMonthDay = new PeriodsListener(dom, day);						
		}	
		return day;
	}
	
	private Element getDayElement(){
		String week = comboWeekDay.getText();
		Element day = null;						
		Element[] days =  weekDayListener.getDayElements();			
		if(days != null &&days.length > 0) {//gleich 1 bedeutet, das day Element gerade generiert wurde
			//überprüfe, ob der Wochentag bereits ausgewählt wurde
			for (int i =0; i < days.length; i++) {
				Element eday = (Element)days[i];
				if(Utils.str2int(Utils.getAttributeValue("day",eday)) == (comboWeekDay.getSelectionIndex()+1)){
					day = eday;		
					break;
				}
			}
		}			
		if(day == null) {
			weekDayListener.addDay(week);
			day = weekDayListener.getDayElements()[weekDayListener.getDayElements().length - 1];//letzte Element
			periodsListenerWeekDay = new PeriodsListener(dom, day);						
		}	
		return day;
	}
	
	
	
	private void apply() {
		bApply = "";
		addPeriodButton.setEnabled(false);
		if(tabFolder.getSelection()[0].getText().equals(JobAssistentPeriodForms.EVERY_DAY)) {								
			Element period  = periodForm.getPeriod();			
			periodsListener.applyPeriod(period);
			periodForm.setPeriod(period);
			periodForm.setEnabled(false);
			
			list.add(JobAssistentPeriodForms.EVERY_DAY + "at " + periodForm.getListener().getBegin() + "-" + periodForm.getListener().getEnd());
			
		} else if(tabFolder.getSelection()[0].getText().equals(JobAssistentPeriodForms.WEEK_DAY)) {
			if(comboWeekDay.getSelectionIndex() == -1 || comboWeekDay.getText().length()==0) {
				MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.period.date"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				return;
			}
			Element period = periodFormWeekDay.getPeriod();//.getDayElements()[weekDayListener.getDayElements().length - 1];//letzte Element
			periodFormWeekDay.setEnabled(false);
			periodsListenerWeekDay.applyPeriod(period);
			update.updateDays(DaysListener.WEEKDAYS);
			
			list.add(JobAssistentPeriodForms.WEEK_DAY + comboWeekDay.getText() + " at " + periodFormWeekDay.getListener().getBegin() + "-" + periodFormWeekDay.getListener().getEnd());
		
		} else if(tabFolder.getSelection()[0].getText().equals(JobAssistentPeriodForms.MONTH_DAY)) {
			if(comboMonth.getSelectionIndex() == -1 || comboMonth.getText().length()==0) {
				MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.period.date"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				return;
			}
			Element period = periodFormMonthDay.getPeriod();//.getDayElements()[weekDayListener.getDayElements().length - 1];//letzte Element
			periodFormMonthDay.setEnabled(false);
			periodsListenerMonthDay.applyPeriod(period);
			update.updateDays(DaysListener.MONTHDAYS);	
			
			list.add(JobAssistentPeriodForms.MONTH_DAY + comboMonth.getText() + " at " + periodFormMonthDay.getListener().getBegin() + "-" + periodFormMonthDay.getListener().getEnd());
			
		} else if(tabFolder.getSelection()[0].getText().equals(JobAssistentPeriodForms.SPECIFIC_DAY)) {		
			
			Element period  = periodFormSpecificDay.getPeriod();			
			periodsListenerSpecDay.applyPeriod(period);			
			periodFormSpecificDay.setEnabled(false);			
			update.updateDays(3);
			
			list.add(JobAssistentPeriodForms.SPECIFIC_DAY + txtSpeDay.getISODate() + " at " + periodFormSpecificDay.getListener().getBegin() + "-" + periodFormSpecificDay.getListener().getEnd());
			
		}
	}
	
	private void  fillList(){
		
		Element run_time = job.getChild("run_time");
		if(run_time == null) return;
		
		//every day
		java.util.List everyDay = periodsListener.get_list();
		for(int i =0; everyDay != null && i < everyDay.size(); i++) {
			Element period = (Element)everyDay.get(i);
			PeriodListener p = new PeriodListener(dom);			
			p.setPeriod(period);
			if(p.getSingle() == null || p.getSingle().trim().length() == 0) {				
				list.add(JobAssistentPeriodForms.EVERY_DAY + "at " + p.getBegin() + "-" + p.getEnd() );
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
				if(p.getSingle() == null || p.getSingle().trim().length() == 0) {													
					list.add(JobAssistentPeriodForms.SPECIFIC_DAY + Utils.asStr(da[2]) + "-" + Utils.asStr(da[1]) + "-" + Utils.asStr(da[0])   +  " at " + p.getSingle() );
				}
			}
		}
				
		//Week day				
		Element[] weekDays = weekDayListener.getDayElements();
		for(int i =0; weekDays!=null&& i < weekDays.length; i++) {
			Element elWeek = weekDays[i];
			String sWeek = comboWeekDay.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) -1);
			java.util.List periods = elWeek.getChildren("period");			
			for (int j =0; periods != null && j < periods.size(); j++) {
			Element period = (Element)periods.get(j);
			PeriodListener p = new PeriodListener(dom);
			p.setPeriod(period);
			if(p.getSingle() == null || p.getSingle().trim().length() == 0) {
				list.add(JobAssistentPeriodForms.WEEK_DAY + sWeek +  " at " + p.getBegin() + "-" + p.getEnd() );
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
				if(p.getSingle() == null || p.getSingle().trim().length() == 0) {
					list.add(JobAssistentPeriodForms.MONTH_DAY + sMonth +  " at " + p.getBegin() + "-" + p.getEnd() );
				}
			}	
		}
		
		
	}
	
	private void delete() {
		//list.remove(list.getSelectionIndex());
		String selectedStr = list.getItem(list.getSelectionIndex());
		
		if(selectedStr.startsWith(JobAssistentPeriodForms.EVERY_DAY)) {
			//Every day
			deleteEveryDay(selectedStr);
		}else if(selectedStr.startsWith(JobAssistentPeriodForms.SPECIFIC_DAY)) {
			//Specific Day entfernen
			deleteSpeDay(selectedStr);
		} else if(selectedStr.startsWith(JobAssistentPeriodForms.WEEK_DAY)) {
			//Wochentag entfernen
			deleteWeek(selectedStr);
		} else if(selectedStr.startsWith(JobAssistentPeriodForms.MONTH_DAY)) {
			//Monat entfernen
			deleteMonth(selectedStr);
		}
			
	}
	
	private void deleteEveryDay(String selectedStr) {
		java.util.List everyDay = periodsListener.get_list();
		
		for(int i =0; everyDay != null && i < everyDay.size(); i++) {
			Element period = (Element)everyDay.get(i);
			PeriodListener p = new PeriodListener(dom);
			p.setPeriod(period);			
			if(selectedStr.equals(JobAssistentPeriodForms.EVERY_DAY + "at " + p.getSingle())) {
				//PeriodsListener _pl = new PeriodsListener(dom, period );
				periodsListener.removePeriod(i);
			}
		}		
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
					if(selectedStr.equals(JobAssistentPeriodForms.SPECIFIC_DAY + Utils.asStr(da[2]) + "-" + Utils.asStr(da[1]) + "-" + Utils.asStr(da[0])   +  " at " + p.getSingle() )){
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
			String sWeek = comboWeekDay.getItem(Utils.str2int(Utils.getAttributeValue("day", elWeek)) -1);
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

	private void discardChanges() {
		
		if(bApply!=null && bApply.equals(JobAssistentPeriodForms.SPECIFIC_DAY)) {
			if(txtSpeDay.getISODate() != null && txtSpeDay.getISODate().length() > 0) {
				periodsListenerSpecDay.removePeriod(periodsListener.get_list().size() - 1);
				periodFormSpecificDay.setEnabled(false);
			}
		} 
		
		if(bApply!=null && bApply.equals(JobAssistentPeriodForms.WEEK_DAY)) {
			if(comboWeekDay.getText() != null && comboWeekDay.getText().length() > 0) {
				Element day = weekDayListener.getDayElements()[weekDayListener.getDayElements().length-1];
				java.util.List l = day.getChildren("period");
				if(l.size() == 0)
					weekDayListener.deleteDay(comboWeekDay.getItem(Utils.str2int(Utils.getAttributeValue("day", day))-1) );
			}
			periodFormWeekDay.setEnabled(false);
		} 
		
		if(bApply!=null && bApply.equals(JobAssistentPeriodForms.MONTH_DAY)) {
			if(comboMonth.getText() != null && comboMonth.getText().length() > 0) {
				Element day = monthListener.getDayElements()[monthListener.getDayElements().length-1];
				java.util.List l = day.getChildren("period");
				if(l.size() == 0)
					monthListener.deleteDay(comboMonth.getItem(Utils.str2int(Utils.getAttributeValue("day", day))-1) );
			}
			periodFormMonthDay.setEnabled(false);
		} else {
			if(periodForm != null)
				periodForm.setEnabled(false);
		}
		
		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.close"));
		butNext.setToolTipText(Messages.getTooltip("assistent.apply"));
		addPeriodButton.setToolTipText(Messages.getTooltip("assistent.period.add"));
		removeButton.setToolTipText(Messages.getTooltip("assistent.period.remove"));
		everyDayTabItem.setToolTipText(Messages.getTooltip("assistent.period.tabfolder.every_day"));
		specificDayTabItem.setToolTipText(Messages.getTooltip("assistent.period.tabfolder.specific_day"));
		weekdayTabItem.setToolTipText(Messages.getTooltip("assistent.period.tabfolder.week_day"));
		monthDayTabItem.setToolTipText(Messages.getTooltip("assistent.period.tabfolder.month_day"));
		newPeriodButton.setToolTipText(Messages.getTooltip("assistent.period.new_period"));
		comboWeekDay.setToolTipText(Messages.getTooltip("assistent.period.new_week_day_period"));
		comboMonth.setToolTipText(Messages.getTooltip("assistent.period.new_month_day_period"));
		txtSpeDay.setToolTipText(Messages.getTooltip("assistent.period.new_specific_day_period"));
	}
	
	private void close() {
		int cont = MainWindow.message(jobTypeShell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK) {Utils.getElementAsString((Element)jobBackUp);
			
			job.setContent(jobBackUp.cloneContent());
			jobTypeShell.dispose();	
		}	
	}
}
