package sos.scheduler.editor.conf.forms;


import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.Options;

public class JobAssistentForm {
	
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
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("error in JobAssistent.startJobAssistent: " + e.getMessage() );
		}
		
		
	}
	
}
