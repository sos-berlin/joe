package sos.scheduler.editor.conf.forms;


import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSClassUtil;

public class JobAssistentForm {

	private SchedulerDom dom = null;
	private ISchedulerUpdate update = null;


	public JobAssistentForm(final SchedulerDom dom_, final ISchedulerUpdate update_) {
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
//				new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
				new ErrorLog(SOSJOEMessageCodes.JOE_M_0010.params(SOSClassUtil.getMethodName(), e));
			} catch(Exception ee) {

			}
//			System.out.println("error in JobAssistent.startJobAssistent: " + e.getMessage() );
			try {
				System.out.println(SOSJOEMessageCodes.JOE_M_0010.params(SOSClassUtil.getMethodName(), e));
			}
			catch (Exception e1) {
			}
		}
	}
}
