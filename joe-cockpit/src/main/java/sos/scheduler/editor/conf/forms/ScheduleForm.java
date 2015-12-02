package sos.scheduler.editor.conf.forms;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.joda.time.DateTime;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.ScheduleListener;

import com.sos.dialog.components.SOSDateTime;
import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScheduleForm extends SOSJOEMessageCodes   {
	private ScheduleListener	listener		= null;
	private Group				scheduleGroup	= null;
	private SchedulerDom		dom				= null;
	private Text				txtName			= null;
	private boolean				init			= true;
	private Text				txtTitle		= null;
	private SOSDateTime			validFromDate	= null;
	private SOSDateTime			validToDate		= null;
	private SOSDateTime			validFromTime	= null;
	private SOSDateTime			validToTime		= null;
	private Combo				cboCombo		= null;
	private Label               scheduleFormmessage = null;

	public ScheduleForm(Composite parent, int style, SchedulerDom dom, org.jdom.Element schedule_, ISchedulerUpdate update) {
		super(parent, style);
		try {
			init = true;
			this.dom = dom;
			listener = new ScheduleListener(dom, update, schedule_);
			initialize();
			init = false;
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("ScheduleForm.init() ") + e.getMessage());
		}
	}

	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();
			txtName.setText(listener.getName());
			txtTitle.setText(listener.getTitle());
			String d = listener.getValidFrom();
			validFromDate.setDate(d);
			validFromTime.setTime(d);
			d = listener.getValidTo();
			validToDate.setDate(d);
			validToTime.setTime(d);
			String[] s = listener.getAllSchedules();
			if (s != null)
				cboCombo.setItems(s);
			cboCombo.setText(listener.getSubstitute());
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
			txtName.setFocus();
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("ScheduleForm.initialize() ") + e.getMessage());
		}
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 3;
			scheduleGroup = JOE_G_ScheduleForm_Schedule.Control(new Group(this, SWT.NONE));
			scheduleGroup.setLayout(gridLayout);
			@SuppressWarnings("unused") final Label nameLabel = JOE_L_Name.Control(new Label(scheduleGroup, SWT.NONE));
			txtName = JOE_T_ScheduleForm_Name.Control(new Text(scheduleGroup, SWT.BORDER));
			txtName.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					if (!init)// während der initialiserung sollen keine
								// überprüfungen stattfinden
						e.doit = Utils.checkElement(txtName.getText(), dom, JOEConstants.SCHEDULE, null);
				}
			});
			txtName.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!init && !existScheduleName())
						listener.setName(txtName.getText());
				}
			});
			txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			final Label titleLabel = JOE_L_ScheduleForm_Title.Control(new Label(scheduleGroup, SWT.NONE));
			titleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
			txtTitle = JOE_T_ScheduleForm_Title.Control(new Text(scheduleGroup, SWT.BORDER));
			txtTitle.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!init)
						listener.setTitle(txtTitle.getText());
				}
			});
			txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			new Label(scheduleGroup, SWT.NONE);
			new Label(scheduleGroup, SWT.NONE);
			new Label(scheduleGroup, SWT.NONE);
			@SuppressWarnings("unused") final Label substitueLabel = JOE_L_ScheduleForm_Substitute.Control(new Label(scheduleGroup, SWT.NONE));
			cboCombo = JOE_Cbo_ScheduleForm_Substitute.Control(new Combo(scheduleGroup, SWT.NONE));
			cboCombo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!init)
						listener.setSubstitut(cboCombo.getText());
				}
			});
			cboCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			Label validFromLabel = JOE_L_ScheduleForm_ValidFrom.Control(new Label(scheduleGroup, SWT.NONE));
			validFromLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			validFromDate = JOE_ScheduleForm_ValidFromDate.Control(new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN));
			validFromDate.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setValidDateFrom();
				}
			});
			validFromTime = JOE_ScheduleForm_ValidFromTime.Control(new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.TIME | SWT.DROP_DOWN));
			validFromTime.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setValidDateFrom();
				}
			});
			@SuppressWarnings("unused") final Label validToLabel = JOE_L_ScheduleForm_ValidTo.Control(new Label(scheduleGroup, SWT.NONE));
			validToDate = JOE_ScheduleForm_ValidToDate.Control(new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN));
			validToDate.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setValidDateTo();
				}
			});
			validToTime = JOE_ScheduleForm_ValidToTime.Control(new SOSDateTime(scheduleGroup, SWT.BORDER | SWT.TIME | SWT.DROP_DOWN));
			validToTime.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setValidDateTo();
				}
			});
			
			new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
            new Label(scheduleGroup, SWT.NONE);
			 
            scheduleFormmessage = new Label(scheduleGroup, SWT.BORDER);
            scheduleFormmessage.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false,3,1));
	        scheduleFormmessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
	        scheduleFormmessage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	        scheduleFormmessage.setBounds(10, 267, 430, 23);
	        scheduleFormmessage.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		}
		catch (Exception e) {
			try {
				new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			System.err.println(JOE_E_0002.params("ScheduleForm.createGroup() ") + e.getMessage());
		}
	}



	private boolean existScheduleName() {
		boolean retVal = false;
		if (!dom.isLifeElement()) {
			if (listener.getSchedule() != null && listener.getSchedule().getParentElement() != null) {
				Element parent = listener.getSchedule().getParentElement();
				java.util.List l = parent.getChildren("schedule");
				for (int i = 0; i < l.size(); i++) {
					Element el = (Element) l.get(i);
					if (Utils.getAttributeValue("name", el).equals(txtName.getText())) {
						retVal = true;
						break;
					}
				}
			}
		}
		if (retVal)
			txtName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		else
			txtName.setBackground(null);
		return retVal;
	}
	private void checkDates(){
	    Calendar calendarNow = GregorianCalendar.getInstance(); 
        calendarNow.setTime( new Date());
	    
	    Calendar calendarTo = GregorianCalendar.getInstance(); 
        calendarTo.setTime( validToDate.getDate());
       
        calendarTo.set(Calendar.HOUR_OF_DAY, (int) validToTime.getHours());  
        calendarTo.set(Calendar.MINUTE, (int) validToTime.getMinutes());  
        calendarTo.set(Calendar.SECOND, (int) validToTime.getSeconds());
        
        Calendar calendarFrom = GregorianCalendar.getInstance(); 
        calendarFrom.setTime( validFromDate.getDate());
       
        calendarFrom.set(Calendar.HOUR_OF_DAY, (int) validFromTime.getHours());  
        calendarFrom.set(Calendar.MINUTE, (int) validFromTime.getMinutes());  
        calendarFrom.set(Calendar.SECOND, (int) validFromTime.getSeconds());  
          
		 if (calendarNow.after(calendarFrom)){
             scheduleFormmessage.setText(JOE_E_ScheduleForm_ValidFromTo_001.label());
		 }else{
    		  if (calendarFrom.after(calendarTo)) {
    			  scheduleFormmessage.setText(JOE_E_ScheduleForm_ValidFromTo_002.label());
    		  }else{
    			  scheduleFormmessage.setText("");
    		  }
		 }
	}

	private void setValidDateTo() {
		if (!init) {
			try {
				checkDates();
				listener.setValidTo(validToDate.getISODate() + " " + validToTime.getISOTime());
			}
			catch (Exception es) {
				es.printStackTrace();
			}
		}
	}

	private void setValidDateFrom() {
		if (!init) {
			try {
				checkDates();
				listener.setValidFrom(validFromDate.getISODate() + " " + validFromTime.getISOTime());
			}
			catch (Exception es) {
				es.printStackTrace();
			}
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"
