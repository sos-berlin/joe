package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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
			String[] s = listener.getAllSchedules();
			if(s != null)
				cboCombo.setItems(s);
			
			cboCombo.setText(listener.getSubstitute());
			
			setSize(new org.eclipse.swt.graphics.Point(656, 400));
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.initialize() " + e.getMessage());
		}
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		try {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			scheduleGroup = new Group(this, SWT.NONE);
			scheduleGroup.setText("Schedule");
			scheduleGroup.setLayout(gridLayout);

			final Label nameLabel = new Label(scheduleGroup, SWT.NONE);
			nameLabel.setText("Name");

			txtName = new Text(scheduleGroup, SWT.BORDER);
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
			txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			

			final Label titleLabel = new Label(scheduleGroup, SWT.NONE);
			titleLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
			titleLabel.setText("Title");

			txtTitle = new Text(scheduleGroup, SWT.BORDER);
			txtTitle.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init)
                    	listener.setTitle(txtTitle.getText());
				}
			});
			txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			final Label substitueLabel = new Label(scheduleGroup, SWT.NONE);
			substitueLabel.setText("Substitute");

			cboCombo = new Combo(scheduleGroup, SWT.NONE);
			cboCombo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init)
						listener.setSubstitut(cboCombo.getText());
				}
			});
			cboCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			Label validFromLabel = new Label(scheduleGroup, SWT.NONE);
			validFromLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			validFromLabel.setText("Valid From");

			
			validFrom = new DatePicker(scheduleGroup, SWT.BORDER);
			validFrom.setEditable(true);
			
			validFrom.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init) {
						if (validFrom.getDateText() != null && validFrom.getDateText().length() > 0 ) {
							try {
								if(validFrom.getDateText().length()>= 10 ) {
									//System.out.println(validFrom.getDateText() + "->" + (sos.util.SOSDate.getDate(validFrom.getDateText(), "dd.MM.yyyy")) );
									java.util.Date d = sos.util.SOSDate.getDate(validFrom.getDateText(), "dd.MM.yyyy");
									String ds = sos.util.SOSDate.getDateAsString(d,"yyyy-MM-dd");								
									listener.setValidFrom(ds + " 00:00:00");
								}
							} catch(Exception es) {
								//System.out.println(es.getMessage());
							}
						/*} else if(validFrom.getISODate() != null && validFrom.getISODate().length() > 0) {
							listener.setValidFrom(validFrom.getISODate() + " 00:00:00");
						*/} else {
							validFrom.setISODate("");
							listener.setValidFrom("");
						}
					}
				}

			
		
		});
			validFrom.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

			final Label validToLabel = new Label(scheduleGroup, SWT.NONE);
			validToLabel.setText("Valid To");

			validTo = new DatePicker(scheduleGroup, SWT.BORDER);
			validTo.setEditable(true);
			validTo.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(!init) {
						if (validTo.getDateText() != null && validTo.getDateText().length() > 0 ) {
							try {
								if(validTo.getDateText().length()>= 10 ) {
									//System.out.println(validTo.getDateText() + "->" + (sos.util.SOSDate.getDate(validTo.getDateText(), "dd.MM.yyyy")) );
									java.util.Date d = sos.util.SOSDate.getDate(validTo.getDateText(), "dd.MM.yyyy");
									String ds = sos.util.SOSDate.getDateAsString(d,"yyyy-MM-dd");								
									listener.setValidTo(ds + " 00:00:00");
								}
							} catch(Exception es) {
								//System.out.println(es.getMessage());
							}
						/*} else if(validTo.getISODate() != null && validTo.getISODate().length() > 0) {
							listener.setValidTo(validTo.getISODate() + " 00:00:00");
							*/
						} else {
							validTo.setISODate("");
							listener.setValidTo("");
						}
					}
				}
			});
			validTo.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		} catch (Exception e) {
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
	
	
	
} // @jve:decl-index=0:visual-constraint="10,10"
