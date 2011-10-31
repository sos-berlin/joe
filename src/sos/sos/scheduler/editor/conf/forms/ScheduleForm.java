package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import sos.scheduler.editor.app.DatePicker;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ScheduleListener;
import org.jdom.Element;
import sos.scheduler.editor.app.Utils;



public class ScheduleForm extends Composite implements IUpdateLanguage {

	private ScheduleListener listener                           = null;

	private Group           scheduleGroup                       = null;

	private SchedulerDom    dom                                 = null;

	private Text            txtName                             = null;

	private boolean         init                                = true;

	private Text            txtTitle                            = null;

	private DatePicker      validFrom                           = null;

	private DatePicker      validTo                             = null;

	private Combo           cboCombo                            = null;

	/**
	 * Beim Ersetzen eines Schedules kann man die Uhrzeit angeben. 
	 * Der Editor belegt mit 00:00:00 vor. 
	 * Beim Bis Datum muss aber 24:00 stehen, damit der komplette Tag gültig ist
	 */
	private Text            txtHourFrom                         = null;	
	private Text            txtMinuteFrom                       = null;
	private Text            txtSecondFrom                       = null;	
	private Text            txtHourTo                           = null;	
	private Text            txtMinuteTo                         = null;
	private Text            txtSecondTo                         = null;



	public ScheduleForm(Composite parent, int style, SchedulerDom dom, org.jdom.Element schedule_, ISchedulerUpdate update) {
		super(parent, style);
		try {
			init = true;
			this.dom = dom;			
			listener = new  ScheduleListener (dom, update, schedule_);
			initialize();
			setToolTipText();
			init = false;

		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in ScheduleForm.init() " + e.getMessage());
		}
	}


	private void initialize() {
		try {
			this.setLayout(new FillLayout());
			createGroup();

			txtName.setText(listener.getName());
			txtTitle.setText(listener.getTitle());
			validFrom.setISODate(listener.getValidFrom());
			validTo.setISODate(listener.getValidTo());

			//zeit from setzen 
			if(listener.getValidFrom().length() >= 12) {
				String time = listener.getValidFrom().split(" ")[1];

				String h = String.valueOf(Utils.getHours(time, 24));
				String m = String.valueOf(Utils.getMinutes(time, 0));
				String s = String.valueOf(Utils.getSeconds(time, 0));

				txtHourFrom.setEnabled(true);
				txtMinuteFrom.setEnabled(true);
				txtSecondFrom.setEnabled(true);

				txtHourFrom.setText(Utils.fill(2, h));
				txtMinuteFrom.setText(Utils.fill(2, m));
				txtSecondFrom.setText(Utils.fill(2, s));

			}
			//zeit valid to setzen
			if(listener.getValidTo().length() >= 12) {
				String time = listener.getValidTo().split(" ")[1];

				String h = String.valueOf(Utils.getHours(time, 24));
				String m = String.valueOf(Utils.getMinutes(time, 0));
				String s = String.valueOf(Utils.getSeconds(time, 0));

				txtHourTo.setEnabled(true);
				txtMinuteTo.setEnabled(true);
				txtSecondTo.setEnabled(true);

				txtHourTo.setText(Utils.fill(2, h));
				txtMinuteTo.setText(Utils.fill(2, m));
				txtSecondTo.setText(Utils.fill(2, s));

			}

			String[] s = listener.getAllSchedules();
			if(s != null)
				cboCombo.setItems(s);

			cboCombo.setText(listener.getSubstitute());

			setSize(new org.eclipse.swt.graphics.Point(656, 400));
			txtName.setFocus();
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in ScheduleForm.initialize() " + e.getMessage());
		}
	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 3;
			scheduleGroup = new Group(this, SWT.NONE);
			scheduleGroup.setText("Schedule");
			scheduleGroup.setLayout(gridLayout);

			final Label nameLabel = new Label(scheduleGroup, SWT.NONE);
			nameLabel.setText("Name");

			txtName = new Text(scheduleGroup, SWT.BORDER);
			txtName.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtName.selectAll();
				}
			});
			txtName.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					if(!init)//während der initialiserung sollen keine überprüfungen stattfinden
						e.doit = Utils.checkElement(txtName.getText(), dom, sos.scheduler.editor.app.Editor.SCHEDULE, null);
				}
			});
			txtName.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init && !existScheduleName())
						listener.setName(txtName.getText());
				}
			});
			txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));


			final Label titleLabel = new Label(scheduleGroup, SWT.NONE);
			titleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
			titleLabel.setText("Title");

			txtTitle = new Text(scheduleGroup, SWT.BORDER);
			txtTitle.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtTitle.selectAll();
				}
			});
			txtTitle.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init)
						listener.setTitle(txtTitle.getText());
				}
			});
			txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

			final Label substitueLabel = new Label(scheduleGroup, SWT.NONE);
			substitueLabel.setText("Substitute");

			cboCombo = new Combo(scheduleGroup, SWT.NONE);
			cboCombo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init)
						listener.setSubstitut(cboCombo.getText());
				}
			});
			cboCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

			Label validFromLabel = new Label(scheduleGroup, SWT.NONE);
			validFromLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			validFromLabel.setText("Valid From");


			validFrom = new DatePicker(scheduleGroup, SWT.BORDER);
 			validFrom.setEditable(true);



			validFrom.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateFrom();					
				}



			});
			final GridData gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = SWT.FILL;
			validFrom.setLayoutData(gridData);

			final Composite composite_1_1 = new Composite(scheduleGroup, SWT.NONE);
			composite_1_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 6;
			composite_1_1.setLayout(gridLayout_1);

			txtHourFrom = new Text(composite_1_1, SWT.CENTER | SWT.BORDER);
			txtHourFrom.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtHourFrom.selectAll();
				}
			});
			txtHourFrom.setTextLimit(2);
			txtHourFrom.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtHourFrom.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateFrom();
				}
			});
			txtHourFrom.setEnabled(false);
			final GridData gridData_1_1_1 = new GridData(GridData.FILL, GridData.FILL, false, false);
			gridData_1_1_1.minimumWidth = 30;
			gridData_1_1_1.widthHint = 30;
			txtHourFrom.setLayoutData(gridData_1_1_1);

			final Label label_2_1 = new Label(composite_1_1, SWT.NONE);
			label_2_1.setText(":");

			txtMinuteFrom = new Text(composite_1_1, SWT.CENTER | SWT.BORDER);
			txtMinuteFrom.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtMinuteFrom.selectAll();
				}
			});
			txtMinuteFrom.setTextLimit(2);
			txtMinuteFrom.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtMinuteFrom.setEnabled(false);
			txtMinuteFrom.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateFrom();
				}
			});
			final GridData gridData_2_1_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_2_1_1.minimumWidth = 30;
			gridData_2_1_1.widthHint = 30;
			txtMinuteFrom.setLayoutData(gridData_2_1_1);

			final Label label_1_1_1 = new Label(composite_1_1, SWT.NONE);
			label_1_1_1.setText(":");

			txtSecondFrom = new Text(composite_1_1, SWT.CENTER | SWT.BORDER);
			txtSecondFrom.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtSecondFrom.selectAll();
				}
			});
			txtSecondFrom.setTextLimit(2);
			txtSecondFrom.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtSecondFrom.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateFrom();
				}
			});
			txtSecondFrom.setEnabled(false);
			txtSecondFrom.setLayoutData(new GridData(30, SWT.DEFAULT));

			final Label hhmmssLabel_1_1 = new Label(composite_1_1, SWT.NONE);
			hhmmssLabel_1_1.setText("hh:mm:ss");

			final Label validToLabel = new Label(scheduleGroup, SWT.NONE);
			validToLabel.setText("Valid To");

			validTo = new DatePicker(scheduleGroup, SWT.BORDER);
			validTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			validTo.setEditable(true);
			validTo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateTo();					
				}
			});

			final Composite composite_1 = new Composite(scheduleGroup, SWT.NONE);
			composite_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.numColumns = 6;
			composite_1.setLayout(gridLayout_2);

			txtHourTo = new Text(composite_1, SWT.CENTER | SWT.BORDER);
			txtHourTo.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtHourTo.selectAll();
				}
			});
			txtHourTo.setTextLimit(2);
			txtHourTo.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtHourTo.setEnabled(false);
			txtHourTo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateTo();
				}
			});
			final GridData gridData_1_1 = new GridData(GridData.FILL, GridData.FILL, false, false);
			gridData_1_1.minimumWidth = 30;
			gridData_1_1.widthHint = 30;
			txtHourTo.setLayoutData(gridData_1_1);

			final Label label_2 = new Label(composite_1, SWT.NONE);
			label_2.setText(":");

			txtMinuteTo = new Text(composite_1, SWT.CENTER | SWT.BORDER);
			txtMinuteTo.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtMinuteTo.selectAll();
				}
			});
			txtMinuteTo.setTextLimit(2);
			txtMinuteTo.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtMinuteTo.setEnabled(false);
			txtMinuteTo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateTo();
				}
			});
			final GridData gridData_2_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_2_1.minimumWidth = 30;
			gridData_2_1.widthHint = 30;
			txtMinuteTo.setLayoutData(gridData_2_1);

			final Label label_1_1 = new Label(composite_1, SWT.NONE);
			label_1_1.setText(":");

			txtSecondTo = new Text(composite_1, SWT.CENTER | SWT.BORDER);
			txtSecondTo.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					txtSecondTo.selectAll();
				}
			});
			txtSecondTo.setTextLimit(2);
			txtSecondTo.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent e) {
					e.doit = Utils.isOnlyDigits(e.text);
				}
			});
			txtSecondTo.setEnabled(false);
			txtSecondTo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setValidDateTo();
				}
			});
			txtSecondTo.setLayoutData(new GridData(30, SWT.DEFAULT));

			final Label hhmmssLabel_1 = new Label(composite_1, SWT.NONE);
			hhmmssLabel_1.setText("hh:mm:ss");
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.err.println("..error in ScheduleForm.createGroup() " + e.getMessage());
		}
	}


	public void setToolTipText() {
		txtName.setToolTipText(Messages.getTooltip("schedule.name"));
		txtTitle.setToolTipText(Messages.getTooltip("schedule.title"));		
		validFrom.setToolTipText(Messages.getTooltip("schedule.valid_from"));		
		validTo.setToolTipText(Messages.getTooltip("schedule.valid_to"));		
		cboCombo.setToolTipText(Messages.getTooltip("schedule.subtitute")); 
	}


	private boolean existScheduleName() {
		boolean retVal = false;
		if(!dom.isLifeElement()) {
			if(listener.getSchedule() != null && listener.getSchedule().getParentElement() != null) {
				Element parent = listener.getSchedule().getParentElement();
				java.util.List l = parent.getChildren("schedule");
				for(int i =0; i < l.size(); i++) {					
					Element el = (Element)l.get(i);
					if(Utils.getAttributeValue("name", el).equals(txtName.getText())) {
						retVal = true;						 
						break;
					}					
				}
			}
		}
		if(retVal)
			txtName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		else
			txtName.setBackground(null);
		return retVal;
	}


	private void setFromTime() {

		if(validFrom.getDateText().length() >= 10) {

			txtHourFrom.setEnabled(true);
			txtMinuteFrom.setEnabled(true);
			txtSecondFrom.setEnabled(true);

			if(txtHourFrom.getText().length() == 0)
				txtHourFrom.setText("00");

			if(txtMinuteFrom.getText().length() == 0)
				txtMinuteFrom.setText("00");

			if(txtSecondFrom.getText().length() == 0)
				txtSecondFrom.setText("00");
		} else {
			txtHourFrom.setEnabled(false);
			txtMinuteFrom.setEnabled(false);
			txtSecondFrom.setEnabled(false);

		}
	}

	private void setToTime() {
		if(validTo.getDateText().length()>= 10 ) {

			txtHourTo.setEnabled(true);
			txtMinuteTo.setEnabled(true);
			txtSecondTo.setEnabled(true);


			if(txtHourTo.getText().length() == 0)
				txtHourTo.setText("24");

			if(txtMinuteTo.getText().length() == 0)
				txtMinuteTo.setText("00");

			if(txtSecondTo.getText().length() == 0)
				txtSecondTo.setText("00");
		} else {

			txtHourTo.setEnabled(false);
			txtMinuteTo.setEnabled(false);
			txtSecondTo.setEnabled(false);

		}
	}

	private void setValidDateTo() {
		if(!init) {
			if (validTo.getDateText() != null && validTo.getDateText().length() > 0 ) {
				try {
					if(validTo.getDateText().length()>= 10 ) {
						java.util.Date d = sos.util.SOSDate.getDate(validTo.getDateText(), "dd.MM.yyyy");
						String ds = sos.util.SOSDate.getDateAsString(d,"yyyy-MM-dd");
						setToTime();

						String timeTo = Utils.getTime(24, txtHourTo.getText(), txtMinuteTo.getText(), txtSecondTo.getText(), false);						
						Utils.setBackground(0, 24, txtHourTo);
						Utils.setBackground(0, 59, txtMinuteTo);
						Utils.setBackground(0, 59, txtSecondTo);

						listener.setValidTo(ds + " " + timeTo);

						validFrom.validto(d);

					}
				} catch(Exception es) {
					//System.out.println(es.getMessage());
				}

			} else {
				validTo.setISODate("");
				listener.setValidTo("");
			}
		}
	}

	private void setValidDateFrom() {

		if(!init) {
			if (validFrom.getDateText() != null && validFrom.getDateText().length() > 0 ) {
				try {
					if(validFrom.getDateText().length()>= 10 ) {

						java.util.Date d = sos.util.SOSDate.getDate(validFrom.getDateText(), "dd.MM.yyyy");
						String ds = sos.util.SOSDate.getDateAsString(d,"yyyy-MM-dd");
						setFromTime();

						Utils.setBackground(0, 24, txtHourFrom);
						Utils.setBackground(0, 59, txtMinuteFrom);
						Utils.setBackground(0, 59, txtSecondFrom);

						String timeFrom = Utils.getTime(24, txtHourFrom.getText(), txtMinuteFrom.getText(), txtSecondFrom.getText(), false);
						listener.setValidFrom(ds + " " + timeFrom);
						validTo.validFrom(d);

					}
				} catch(Exception es) {
					//System.out.println(es.getMessage());
				}
			} else {
				validFrom.setISODate("");
				listener.setValidFrom("");
			}
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
