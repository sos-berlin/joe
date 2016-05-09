package sos.scheduler.editor.classes.returncodes;

import java.util.ArrayList;
import java.util.Iterator;

public class JobchainListOfReturnCodeNextStateElements {

    private ArrayList<JobchainReturnCodeNextStateElement> jobchainListOfReturnCodeElements = null;
    private Iterator<JobchainReturnCodeNextStateElement> it = null;

    public JobchainListOfReturnCodeNextStateElements() {
        super();
        jobchainListOfReturnCodeElements = new ArrayList<JobchainReturnCodeNextStateElement>();
    }

    public ArrayList<JobchainReturnCodeNextStateElement> getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }

    public void add(JobchainReturnCodeNextStateElement jobchainReturnCodeElement) {
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
        return it.hasNext();
    }

    public int size() {
        return jobchainListOfReturnCodeElements.size();
    }

    public JobchainReturnCodeNextStateElement getNext() {
        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

}