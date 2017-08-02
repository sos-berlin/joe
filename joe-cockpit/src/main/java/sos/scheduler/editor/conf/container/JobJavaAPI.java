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

public class JobJavaAPI extends FormBaseClass {

    private boolean init = true;
    private JobListener objJobDataProvider = null;
    private Text tClasspath = null;
    private Text txtJavaOptions = null;
    private Text tbxClassName = null;

    public JobJavaAPI(Composite pParentComposite, JobListener pobjJobDataProvider, JobJavaAPI that) {
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

    private void getValues(JobJavaAPI that) {
        if (that == null) {
            return;
        }
        this.tClasspath.setText(that.tClasspath.getText());
        this.txtJavaOptions.setText(that.txtJavaOptions.getText());
        this.tbxClassName.setText(that.tbxClassName.getText());
    }

    private void createGroup() {
        showWaitCursor();
        Group gScript = new Group(objParent, SWT.NONE);
        GridLayout lgridLayout = new GridLayout();
        lgridLayout.numColumns = 2;
        gScript.setLayout(lgridLayout);
        setResizableV(gScript);
        Label lblClassNameLabel = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Classname.control(new Label(gScript, SWT.NONE));
        GridData labelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);
        lblClassNameLabel.setLayoutData(labelGridData);
        tbxClassName = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Classname.control(new Text(gScript, SWT.BORDER));
        tbxClassName.setEnabled(true);
        tbxClassName.setText(objJobDataProvider.getJavaClass());
        GridData gd_tClass = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tbxClassName.setLayoutData(gd_tClass);
        tbxClassName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setJavaClass(tbxClassName.getText());
                }
            }
        });


        Label lblNewLabel_1 = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Classpath.control(new Label(gScript, SWT.NONE));
        lblNewLabel_1.setLayoutData(labelGridData);
        tClasspath = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Classpath.control(new Text(gScript, SWT.BORDER));
        tClasspath.setEnabled(true);
        tClasspath.setText(objJobDataProvider.getClasspath());
        GridData gd_tClasspath = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tClasspath.setLayoutData(gd_tClasspath);
        tClasspath.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setClasspath(tClasspath.getText());
                }
            }
        });


        final Label java_optionsLabel = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Options.control(new Label(gScript, SWT.NONE));
        java_optionsLabel.setLayoutData(labelGridData);
        txtJavaOptions = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Options.control(new Text(gScript, SWT.BORDER));
        txtJavaOptions.setText(objJobDataProvider.getJavaOptions());
        txtJavaOptions.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!init) {
                    objJobDataProvider.setJavaOptions(txtJavaOptions.getText());
                }
            }
        });
        txtJavaOptions.setLayoutData(gd_tClass);
        gScript.setVisible(true);
        gScript.redraw();
        restoreCursor();
    }

    public Text getTClasspath() {
        return tClasspath;
    }

    public Text getTbxClassName() {
        return tbxClassName;
    }

}