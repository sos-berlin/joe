package com.sos.joe.wizard.forms;


import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobAssistentForm extends JobWizardBaseForm { 
	
	private SchedulerDom dom = null;
	private ISchedulerUpdate update = null;
	
	
	public JobAssistentForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;	
	}	
		
	
	public void startJobAssistant() {
		try {
			if(Options.isShowWizardInfo()) { 
				JobAssistentInfoForms info = new JobAssistentInfoForms(dom, update);
				info.showInfoForm();
			} else {
				JobAssistentTypeForms typeForms = new JobAssistentTypeForms(dom, update);												
				typeForms.showTypeForms();
			}
			
		} catch (Exception e){
			try {
//				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
				new ErrorLog(SOSJOEMessageCodes.JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e));
			} catch(Exception ee) {
				//tu nichts
			}
//			System.out.println("error in JobAssistent.startJobAssistent: " + e.getMessage() );
			try {
				System.out.println(SOSJOEMessageCodes.JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e));
			}
			catch (Exception e1) {
				//tu nichts
			}
		}
		
		
	}
	
}
