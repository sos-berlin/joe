package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.listeners.DetailXMLEditorListener;
import sos.scheduler.editor.conf.listeners.JobChainConfigurationListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.jobscheduler.DetailDom;

public class DetailXMLEditorDialogForm {

    private String xmlFilename = null;
    private Text txtXML = null;
    private DetailXMLEditorListener listener = null;
    private Button butApply = null;
    private Shell shell = null;
    private String state = null;
    private String jobChainname = null;
    private String orderId = null;
    private DetailDom dom = null;
    private int type = -1;
    private Composite parent = null;
    private JobChainConfigurationListener confListener = null;
    private Tree tree = null;
    private boolean isLifeElement = false;
    private String path = null;

    public DetailXMLEditorDialogForm(String xmlFilename_, String jobChainname_, String state_, String orderId_, int type_, boolean isLifeElement_,
            String path_) {
        jobChainname = jobChainname_;
        state = state_;
        xmlFilename = xmlFilename_;
        orderId = orderId_;
        type = type_;
        isLifeElement = isLifeElement_;
        path = path_;
    }

    public DetailXMLEditorDialogForm(DetailDom dom_, int type_, boolean isLifeElement_, String path_) {
        dom = dom_;
        xmlFilename = dom.getFilename();
        type = type_;
        isLifeElement = isLifeElement_;
        path = path_;
    }

    public void showXMLEditor() {
        shell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(final ShellEvent e) {
                close();
                e.doit = shell.isDisposed();
            }
        });
        shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.marginBottom = 5;
        shell.setLayout(gridLayout);
        shell.setSize(693, 743);
        shell.setText(SOSJOEMessageCodes.JOE_M_0009.params(xmlFilename));
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        shell.setBounds((screen.width - shell.getBounds().width) / 2, (screen.height - shell.getBounds().height) / 2, shell.getBounds().width, shell.getBounds().height);
        final Group jobGroup = SOSJOEMessageCodes.JOE_G_DetailXMLEditorDialogForm_JobGroup.Control(new Group(shell, SWT.NONE));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        jobGroup.setLayout(gridLayout_1);
        final GridData gridDataJobGroup = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        gridDataJobGroup.minimumWidth = 10;
        gridDataJobGroup.minimumHeight = 10;
        gridDataJobGroup.widthHint = 663;
        gridDataJobGroup.heightHint = 685;
        jobGroup.setLayoutData(gridDataJobGroup);
        txtXML = SOSJOEMessageCodes.JOE_T_DetailXMLEditorDialogForm_XML.Control(new Text(jobGroup, SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL));
        txtXML.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                butApply.setEnabled(true);
            }
        });
        final GridData gridDataTxtXml = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 2);
        gridDataTxtXml.heightHint = 672;
        gridDataTxtXml.widthHint = 553;
        txtXML.setLayoutData(gridDataTxtXml);
        txtXML.setEnabled(true);
        txtXML.setEditable(true);
        butApply = SOSJOEMessageCodes.JOE_B_DetailXMLEditorDialogForm_Apply.Control(new Button(jobGroup, SWT.NONE));
        butApply.setEnabled(false);
        butApply.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.saveXML(txtXML.getText());
                if (type == JOEConstants.DETAILS) {
                    confListener.treeFillMain(tree, parent);
                    shell.setFocus();
                }
                butApply.setEnabled(false);
                shell.close();
            }
        });
        final GridData gridDataBtnApply = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        gridDataBtnApply.widthHint = 62;
        butApply.setLayoutData(gridDataBtnApply);
        final Button butClose = SOSJOEMessageCodes.JOE_B_DetailXMLEditorDialogForm_Close.Control(new Button(jobGroup, SWT.NONE));
        butClose.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        butClose.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        if (type == JOEConstants.JOB_CHAINS) {
            listener = new DetailXMLEditorListener(xmlFilename);
        } else {
            listener = new DetailXMLEditorListener(dom);
        }
        try {
            txtXML.setText(listener.readCommands());
        } catch (Exception e) {
            try {
                System.err.println(SOSJOEMessageCodes.JOE_E_0002.params("showXMLEditor", e.toString()));
                new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params("showXMLEditor", e.toString()));
            } catch (Exception ee) {
                // do nothing
            }
        }
        setToolTipText();
        shell.open();
        shell.layout();
        butApply.setEnabled(false);
    }

    public void setToolTipText() {
        //
    }

    private boolean closeDialog() {
        int cont = -1;
        boolean retVal = false;
        if (butApply.isEnabled()) {
            cont = MainWindow.message(shell, SOSJOEMessageCodes.JOE_M_0008.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            if (cont == SWT.OK) {
                shell.dispose();
                retVal = true;
            }
        } else {
            retVal = true;
        }
        return retVal;
    }

    private void openDetailForm() {
        DetailDialogForm detail = new DetailDialogForm(jobChainname, state, orderId, isLifeElement, path);
        detail.showDetails();
        detail.getDialogForm().open(orderId);
    }

    private void close() {
        if (closeDialog()) {
            if (type == JOEConstants.JOB_CHAINS) {
                openDetailForm();
            } else if (type == JOEConstants.DETAILS) {
                confListener.treeSelection(tree, parent);
                dom.setChanged(true);
            }
            shell.dispose();
        }
    }

    public void setConfigurationData(JobChainConfigurationListener confListener_, Tree tree_, Composite parent_) {
        confListener = confListener_;
        tree = tree_;
        parent = parent_;
    }

}