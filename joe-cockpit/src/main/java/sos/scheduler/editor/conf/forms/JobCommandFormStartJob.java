package sos.scheduler.editor.conf.forms;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandFormStartJob extends JobCommandForm {

    public JobCommandFormStartJob(Composite parent, int style, SchedulerDom dom, Element command, ISchedulerUpdate main) throws JDOMException,
            TransformerException {
        super(parent, style, dom, command, main);
    }

    protected void tJobModifyListener() {
        listener.setJob(tJob.getText());
    }

    protected void setCommandsEnabled() {
        tState.setEnabled(false);
        cboEndstate.setEnabled(false);
        tPriority.setEnabled(false);
        tJobchain.setEnabled(false);
        tTitle.setEnabled(false);
        bReplace.setEnabled(false);
    }

    protected void clearFields() {
        tState.setVisible(false);
        cboEndstate.setVisible(false);
        tPriority.setVisible(false);
        tJobchain.setVisible(false);
        tTitle.setVisible(false);
        bReplace.setVisible(false);
        jobchainLabel.setVisible(false);
        priorityLabel.setVisible(false);
        titleLabel.setVisible(false);
        stateLabel.setVisible(false);
        endStateLabel.setVisible(false);
        replaceLabel.setVisible(false);
        bBrowseJobChain.setVisible(false);

        tJob.setFocus();
        tJob.setVisible(true);
        tStartAt.setVisible(true);
    }

    protected void fillCommand() {
        if (listener.getCommand() != null) {
            tStartAt.setText(Utils.getAttributeValue("at", listener.getCommand()));
            tJob.setText(Utils.getAttributeValue("job", listener.getCommand()));
        }
    }

}
