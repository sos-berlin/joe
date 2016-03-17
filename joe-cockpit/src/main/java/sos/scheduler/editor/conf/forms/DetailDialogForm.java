package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;

public class DetailDialogForm {

    private String jobChainname = "";
    private String state = null;
    private Shell shell = null;
    private DetailForm dialogForm = null;
    private boolean isLifeElement = false;
    private String path = null;
    private String orderId = null;

    public DetailDialogForm(String jobChainname_, boolean isLifeElement_, String path_) {
        jobChainname = jobChainname_;
        isLifeElement = isLifeElement_;
        path = path_;
    }

    public DetailDialogForm(String jobChainname_, String state_, String orderId_, boolean isLifeElement_, String path_) {
        jobChainname = jobChainname_;
        state = state_;
        this.orderId = orderId_;
        isLifeElement = isLifeElement_;
        path = path_;
    }

    public void showDetails() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shell = new Shell(sos.scheduler.editor.app.MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
        shell.setLayout(new FillLayout());
        shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        shell.setText(SOSJOEMessageCodes.JOE_M_0017.params(jobChainname, state != null && !state.isEmpty() ? SOSJOEMessageCodes.JOE_M_0018.params(state)
                : "", orderId != null && !orderId.isEmpty() ? SOSJOEMessageCodes.JOE_M_0019.params(orderId) : ""));
        final Composite composite = SOSJOEMessageCodes.JOE_Composite1.Control(new Composite(shell, SWT.NONE));
        composite.setLayout(new FillLayout());
        final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
        gridData_6.widthHint = 500;
        gridData_6.heightHint = 500;
        dialogForm = new DetailForm(composite, SWT.NONE, jobChainname, state, orderId, JOEConstants.JOB_CHAINS, null, null, isLifeElement, path);
        if (!dialogForm.hasErrors()) {
            dialogForm.setLayout(new FillLayout());
        }
    }

    public DetailForm getDialogForm() {
        return dialogForm;
    }

}