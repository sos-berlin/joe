package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;

public class JobDotNetAPI extends FormBaseClass {

    private boolean init = true;
    private JobListener objJobDataProvider = null;
    private Text tDotNetClass = null;
    private Text tDll = null;

    public JobDotNetAPI(Composite pParentComposite, JobListener pobjJobDataProvider, JobDotNetAPI that) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;
        init = true;
        createGroup();
        init = false;
        getValues(that);
    }

    public void apply() {
        //
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent() {
        //
    }

    private void getValues(JobDotNetAPI that) {
        if (that == null) {
            return;
        }
        this.tDotNetClass.setText(that.tDotNetClass.getText());
        this.tDll.setText(that.tDll.getText());
    }

    private void createGroup() {
        showWaitCursor();
        Group gScript = new Group(objParent, SWT.NONE);
        GridLayout lGridLayout = new GridLayout();
        lGridLayout.numColumns = 2;
        gScript.setLayout(lGridLayout);
        setResizableV(gScript);
        GridData labelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);

        final Label lbDll = SOSJOEMessageCodes.JOE_L_JobDotNetAPI_DLL.control(new Label(gScript, SWT.NONE));
        lbDll.setLayoutData(labelGridData);
        tDll = SOSJOEMessageCodes.JOE_T_JobDotNetAPI_DLL.control(new Text(gScript, SWT.BORDER));
        tDll.setText(objJobDataProvider.getDotNetDll());
        GridData gdDll = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tDll.setLayoutData(gdDll);
        tDll.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setDotNetDll(tDll.getText());
                }
            }
        });

        
        Label lbDotNetClass = SOSJOEMessageCodes.JOE_L_JobDotNet_API_Class.control(new Label(gScript, SWT.NONE));
        lbDotNetClass.setLayoutData(labelGridData);
        tDotNetClass = SOSJOEMessageCodes.JOE_T_JobDotNetAPI_Class.control(new Text(gScript, SWT.BORDER));
        tDotNetClass.setEnabled(true);
        tDotNetClass.setText(objJobDataProvider.getDotNetClass());
        GridData gdClass = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tDotNetClass.setLayoutData(gdClass);
        tDotNetClass.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setDotNetClass(tDotNetClass.getText());
                }
            }
        });

               
        gScript.setVisible(true);
        gScript.redraw();
        gScript.layout();

        restoreCursor();
    }

    public Text getDotNetClass() {
        return tDotNetClass;
    }

    public Text getDotNetDll() {
        return tDll;
    }

}