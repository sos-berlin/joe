package sos.scheduler.editor.app;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public interface IProcessClassDataProvider {

    public void setProcessClass(final String processClass);

    public void openXMLAttributeDoc(final String pstrTagName, final String pstrAttributeName);

    public void openXMLDoc(final String pstrTagName);

    public SchedulerDom get_dom();

    public String getProcessClass();

    public String[] getProcessClasses();

}
