package sos.scheduler.editor.conf.listeners;

import org.jdom.Element;

import sos.scheduler.editor.app.IProcessClassDataProvider;
import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainFileWatchingListener implements IProcessClassDataProvider {

    private JobChainListener jobchainDataProvider = null;

    public JobChainFileWatchingListener(JobChainListener jobchainDataProvider_) {
        jobchainDataProvider = jobchainDataProvider_;
    }

    @Override
    public String getProcessClass() {
        return Utils.getAttributeValue("file_watching_process_class", jobchainDataProvider._chain);
    }

    @Override
    public void setProcessClass(final String processClass) {
        if (processClass == "") {
            jobchainDataProvider._chain.removeAttribute("file_watching_process_class");
        } else {
            Utils.setAttribute("file_watching_process_class", processClass, jobchainDataProvider._chain);
        }
        jobchainDataProvider._dom.setChanged(true);
        if (jobchainDataProvider._dom.isDirectory() || jobchainDataProvider._dom.isLifeElement())
            jobchainDataProvider._dom.setChangedForDirectory("job_chain", jobchainDataProvider.getChainName(), SchedulerDom.MODIFY);
    }

    @Override
    public void openXMLAttributeDoc(String pstrTagName, String pstrAttributeName) {
        jobchainDataProvider.openXMLAttributeDoc(pstrTagName, pstrAttributeName);
    }

    @Override
    public void openXMLDoc(String pstrTagName) {
        jobchainDataProvider.openXMLDoc(pstrTagName);
    }

    @Override
    public SchedulerDom get_dom() {
        return jobchainDataProvider.get_dom();
    }

    @Override
    public String[] getProcessClasses() {
        return jobchainDataProvider.getProcessClasses();
    }

}
