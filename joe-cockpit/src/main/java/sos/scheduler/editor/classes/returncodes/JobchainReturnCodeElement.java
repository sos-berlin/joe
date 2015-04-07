package sos.scheduler.editor.classes.returncodes;

 
public class JobchainReturnCodeElement {
    private String returnCodes;
    private JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement;
    private JobchainListOfReturnCodeAddOrderElements  jobchainListOfReturnCodeAddOrderElements;

    public void setJobchainListOfReturnCodeAddOrderElements(JobchainListOfReturnCodeAddOrderElements jobchainListOfReturnCodeAddOrderElements) {
        this.jobchainListOfReturnCodeAddOrderElements = jobchainListOfReturnCodeAddOrderElements;
    }
 
    public void setJobchainReturnCodeNextStateElement(JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement) {
        this.jobchainReturnCodeNextStateElement = jobchainReturnCodeNextStateElement;
    }

    public void setReturnCodes(String returnCodes) {
        this.returnCodes = returnCodes;
    }

    public String getReturnCodes() {
        return returnCodes;
    }
    
    public JobchainReturnCodeElement() {
        super();
    }

    public JobchainListOfReturnCodeAddOrderElements getJobchainListOfReturnCodeAddOrderElements() {
        return jobchainListOfReturnCodeAddOrderElements;
    }

 
    public JobchainReturnCodeNextStateElement getJobchainReturnCodeNextStateElement() {
        return jobchainReturnCodeNextStateElement;
    } 
    
    public void addJobchainReturnCodeAddOrderElement(JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement){
       if (jobchainListOfReturnCodeAddOrderElements == null){
           jobchainListOfReturnCodeAddOrderElements = new JobchainListOfReturnCodeAddOrderElements();
       }
       jobchainListOfReturnCodeAddOrderElements.add(jobchainReturnCodeAddOrderElement);

    }

    
}