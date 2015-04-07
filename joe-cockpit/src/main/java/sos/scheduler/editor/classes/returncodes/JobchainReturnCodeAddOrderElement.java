package sos.scheduler.editor.classes.returncodes;

import java.util.HashMap;

public class JobchainReturnCodeAddOrderElement {

    private String returnCodes;
    private String jobChain;
    private String orderId;
    private HashMap<String,String> params;
    
    
    public JobchainReturnCodeAddOrderElement() {
        super();
        params = new HashMap<String, String>();
     }


    public String getReturnCodes() {
        return returnCodes;
    }


    public void setReturnCodes(String returnCodes) {
        this.returnCodes = returnCodes;
    }


    public String getJobChain() {
        return jobChain;
    }


    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }


    public String getOrderId() {
        return orderId;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

 
    public HashMap<String, String> getParams() {
        return params;
    }


     
    public void addParam(String name, String value){
        this.params.put(name, value);
     }
    
 
    
    
    
}
