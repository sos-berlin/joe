package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandFormAddOrder extends JobCommandForm {

    public JobCommandFormAddOrder(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main) throws JDOMException,
            TransformerException {
        super(parent, style, dom, command, main);
    }

    protected void tJobModifyListener() {
        listener.setOrderId(tJob.getText());
    }

    protected void clearFields() {
        bBrowse.setVisible(false);
        tJobchain.setFocus();
        tJob.setVisible(true);
        tStartAt.setVisible(true);
    }

    protected void setCommandsEnabled() {
        tState.setEnabled(true);
        cboEndstate.setEnabled(true);
        tPriority.setEnabled(true);
        tJobchain.setEnabled(true);
        tTitle.setEnabled(true);
        bReplace.setEnabled(true);
    }

    protected void fillCommand() {
        if (listener.getCommand() != null) {
            tStartAt.setText(Utils.getAttributeValue("at", listener.getCommand()));
            tJobchain.setText(Utils.getAttributeValue("job_chain", listener.getCommand()));
            tJob.setText(Utils.getAttributeValue("id", listener.getCommand()));
            tTitle.setText(Utils.getAttributeValue("title", listener.getCommand()));
            tState.setItems(listener.getStates());
            tState.setText(Utils.getAttributeValue("state", listener.getCommand()));
            cboEndstate.setItems(listener.getStates());
            cboEndstate.setText(Utils.getAttributeValue("end_state", listener.getCommand()));
            tPriority.setText(Utils.getAttributeValue("priority", listener.getCommand()));
            bReplace.setSelection("yes".equals(Utils.getAttributeValue("replace", listener.getCommand())));
        }
    }

}