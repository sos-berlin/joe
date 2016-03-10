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

            JobAssistentTypeForms typeForms = new JobAssistentTypeForms(dom, update);
            typeForms.showTypeForms();

        } catch (Exception e) {
            try {
                new ErrorLog(SOSJOEMessageCodes.JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e));
            } catch (Exception ee) {
            }
            try {
                System.out.println(SOSJOEMessageCodes.JOE_M_0010.params(sos.util.SOSClassUtil.getMethodName(), e));
            } catch (Exception e1) {
            }
        }

    }

}
