package sos.scheduler.editor.classes;

import java.io.File;

import javax.xml.bind.JAXBException;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.objects.JSObjJobChain;
import com.sos.scheduler.model.objects.JobChain;

public class JobChainDiagramCreator {
    
    private File jobChainFile;
    private File outputDirectory;
    private File outputFile;
    private JSObjJobChain jobChain;

    public JobChainDiagramCreator(File jobChainFile_, File outputDirectory_) {
        super();
        outputDirectory = outputDirectory_;
        jobChainFile = jobChainFile_;
    }

    public void createGraphwizFile(boolean showErrorNodes) throws Exception {

        try {

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(JobChain.class);
            jobChain = schedulerObjectFactory.createJobChain();
            jobChain.loadObject(jobChainFile);
            outputFile = new File(jobChain.createGraphVizImageFile(outputDirectory,showErrorNodes));
 
        } catch (JAXBException e) {
			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }

    }
    
    public File getOutfile(){
        return outputFile;
    }

}
