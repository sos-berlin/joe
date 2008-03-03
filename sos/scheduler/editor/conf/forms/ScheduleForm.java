package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
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
		
	private Text            text                                = null;
	
	private boolean         init                                = true;
	
	
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
			scheduleGroup = new Group(this, SWT.NONE);
			scheduleGroup.setText("Schedule");
			scheduleGroup.setLayout(gridLayout);

			text = new Text(scheduleGroup, SWT.BORDER);
			text.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
                    if(!init && !existScheduleName())
                    	listener.setName(text.getText());
				}
			});
			text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			text.setText(listener.getName());
		} catch (Exception e) {
			System.err.println("..error in ScheduleForm.createGroup() " + e.getMessage());
		}
	}
	
	
	
	
	public void setToolTipText() {
		text.setToolTipText(Messages.getTooltip("schedule.name"));
	}
	
	
	private boolean existScheduleName() {
		boolean retVal = false;
		if(!dom.isLifeElement()) {
			if(listener.getSchedule() != null && listener.getSchedule().getParentElement() != null) {
				Element parent = listener.getSchedule().getParentElement();
				java.util.List l = parent.getChildren("schedule");
				for(int i =0; i < l.size(); i++) {					
					 Element el = (Element)l.get(i);
					 if(Utils.getAttributeValue("name", el).equals(text.getText())) {
						 retVal = true;						 
						 break;
					 }					
				}
			}
		}
		if(retVal)
			text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		else
			text.setBackground(null);
		return retVal;
	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
