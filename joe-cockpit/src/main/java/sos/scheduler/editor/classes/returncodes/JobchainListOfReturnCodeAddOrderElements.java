package sos.scheduler.editor.classes.returncodes;

import java.util.ArrayList;
import java.util.Iterator;

public class JobchainListOfReturnCodeAddOrderElements {

    private ArrayList<JobchainReturnCodeAddOrderElement> jobchainListOfReturnCodeElements = null;
    private Iterator<JobchainReturnCodeAddOrderElement> it = null;

    public JobchainListOfReturnCodeAddOrderElements() {
        super();
        jobchainListOfReturnCodeElements = new ArrayList<JobchainReturnCodeAddOrderElement>();
    }

    public ArrayList<JobchainReturnCodeAddOrderElement> getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }

    public void add(JobchainReturnCodeAddOrderElement jobchainReturnCodeElement) {
        jobchainListOfReturnCodeElements.add(jobchainReturnCodeElement);
    }

    public void clear() {
        jobchainListOfReturnCodeElements.clear();
    }

    public void reset() {
        it = null;
    }

    public boolean hasNext() {
        if (it == null) {
            it = jobchainListOfReturnCodeElements.listIterator();
        }
        return (it.hasNext());
    }

    public int size() {
        return jobchainListOfReturnCodeElements.size();
    }

    public JobchainReturnCodeAddOrderElement getNext() {

        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

}
