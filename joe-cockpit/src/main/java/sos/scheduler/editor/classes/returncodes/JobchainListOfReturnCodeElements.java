package sos.scheduler.editor.classes.returncodes;

import java.util.ArrayList;
import java.util.Iterator;

public class JobchainListOfReturnCodeElements {
    private ArrayList <JobchainReturnCodeElement>  jobchainListOfReturnCodeElements= null; 
    private Iterator <JobchainReturnCodeElement> it = null;
    
    
    
    public JobchainListOfReturnCodeElements() {
        super();
        jobchainListOfReturnCodeElements = new ArrayList<JobchainReturnCodeElement>();
    }

    public JobchainListOfReturnCodeElements(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements_) {
        super();
        jobchainListOfReturnCodeElements = new ArrayList<JobchainReturnCodeElement>();
        jobchainListOfReturnCodeElements_.reset();
        
        while (jobchainListOfReturnCodeElements_.hasNext()) {
          JobchainReturnCodeElement jobchainReturnCodeElement = jobchainListOfReturnCodeElements_.getNext();
          add(jobchainReturnCodeElement);
        }
        
    }
    
   
    

    public ArrayList<JobchainReturnCodeElement> getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }


    private void add(JobchainReturnCodeElement jobchainReturnCodeElement){
        jobchainListOfReturnCodeElements.add(jobchainReturnCodeElement);
    }
    
    private JobchainReturnCodeElement getJobchainReturnCodeElement(String returnCodes){
        reset();
        while (this.hasNext()){
            JobchainReturnCodeElement element = getNext();
            if (element.getReturnCodes().equals(returnCodes)){
                return element;
             }               
        }
        JobchainReturnCodeElement jobchainReturnCodeElement = new JobchainReturnCodeElement();
        jobchainReturnCodeElement.setReturnCodes(returnCodes);
        add(jobchainReturnCodeElement);

        return jobchainReturnCodeElement;
    }
    
    public void add(JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement){

        JobchainReturnCodeElement jobchainReturnCodeElement = getJobchainReturnCodeElement(jobchainReturnCodeNextStateElement.getReturnCodes());
        jobchainReturnCodeElement.setJobchainReturnCodeNextStateElement(jobchainReturnCodeNextStateElement);
        
    }

    public void add(JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement){

        JobchainReturnCodeElement jobchainReturnCodeElement = getJobchainReturnCodeElement(jobchainReturnCodeAddOrderElement.getReturnCodes());
        jobchainReturnCodeElement.addJobchainReturnCodeAddOrderElement(jobchainReturnCodeAddOrderElement);
        
    }
    
    public void clear(){
        jobchainListOfReturnCodeElements.clear();
    }
    
    public void reset(){
        it = null;
    }
    
    public boolean hasNext(){
        if (it==null){
            it  = jobchainListOfReturnCodeElements.listIterator();
        }
        return ( it.hasNext());
    }
    
    public int size(){
        return jobchainListOfReturnCodeElements.size();
    }
    
    public JobchainReturnCodeElement getNext(){
       
        if (it.hasNext()){
            return it.next();
        }else{
            return null;
        }
    }
    
}
