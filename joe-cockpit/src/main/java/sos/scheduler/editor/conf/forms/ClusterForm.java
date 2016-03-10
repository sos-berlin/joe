package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.conf.listeners.ClusterListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ClusterForm extends SOSJOEMessageCodes implements IUnsaved {

    private ClusterListener listener;
    private int type;
    private Group gScript = null;
    private Label label1 = null;
    private Text tWarnTimeout = null;
    private Text tTimeout = null;
    private Text tOwnTimeout = null;
    private Label label3 = null;
    private Label label14 = null;

    public ClusterForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    public ClusterForm(Composite parent, int style, SchedulerDom dom, Element element) {
        super(parent, style);
        initialize();
        setAttributes(dom, element, type);
    }

    public void setAttributes(SchedulerDom dom, Element element, int type) {
        listener = new ClusterListener(dom, element);
        tOwnTimeout.setText(listener.getHeartbeatOwnTimeout());
        tWarnTimeout.setText(listener.getHeartbeatWarnTimeout());
        tTimeout.setText(listener.getHeartbeatTimeout());
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(604, 427));
        tTimeout.setFocus();
    }

    /** This method initializes group */
    private void createGroup() {
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.minimumWidth = 60;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gScript = JOE_G_ClusterForm_Cluster.Control(new Group(this, SWT.NONE));
        // gScript.setText("Cluster");
        gScript.setLayout(gridLayout);
        label14 = JOE_L_ClusterForm_HeartbeatTimeout.Control(new Label(gScript, SWT.NONE));
        // label14.setText("Heartbeat Timeout");
        createComposite();
        label1 = JOE_L_ClusterForm_HeartbeatOwnTimeout.Control(new Label(gScript, SWT.NONE));
        // label1.setText("Heartbeat Own Timeout");
        tOwnTimeout = JOE_T_ClusterForm_HeartbeatOwnTimeout.Control(new Text(gScript, SWT.BORDER));
        // tOwnTimeout.addFocusListener(new FocusAdapter() {
        // public void focusGained(final FocusEvent e) {
        // tOwnTimeout.selectAll();
        // }
        // });
        tOwnTimeout.setLayoutData(gridData);
        tOwnTimeout.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setHeartbeatOwnTimeout(tOwnTimeout.getText());
            }
        });
        label3 = JOE_L_ClusterForm_HeartbeatWarnTimeout.Control(new Label(gScript, SWT.NONE));
        // label3.setText("Heartbeat Warn Timeout");
        tWarnTimeout = JOE_T_ClusterForm_HeartbeatWarnTimeout.Control(new Text(gScript, SWT.BORDER));
        // tWarnTimeout.addFocusListener(new FocusAdapter() {
        // public void focusGained(final FocusEvent e) {
        // tWarnTimeout.selectAll();
        // }
        // });
        tWarnTimeout.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                listener.setHeartbeatWarnTimeout(tWarnTimeout.getText());
            }
        });
        final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gridData_1.minimumWidth = 60;
        tWarnTimeout.setLayoutData(gridData_1);
    }

    /** This method initializes composite */
    private void createComposite() {
        tTimeout = JOE_T_ClusterForm_HeartbeatTimeout.Control(new Text(gScript, SWT.BORDER));
        // tTimeout.addFocusListener(new FocusAdapter() {
        // public void focusGained(final FocusEvent e) {
        // tTimeout.selectAll();
        // }
        // });
        tTimeout.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                listener.setHeartbeatTimeout(tTimeout.getText());
            }
        });
        final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gridData.minimumWidth = 60;
        tTimeout.setLayoutData(gridData);
    }

    public boolean isUnsaved() {
        return false;
    }

    @Override
    public void apply() {
        // TODO Auto-generated method stub

    }

} // @jve:decl-index=0:visual-constraint="10,10"
