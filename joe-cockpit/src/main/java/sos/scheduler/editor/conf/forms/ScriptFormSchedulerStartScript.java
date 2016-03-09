package sos.scheduler.editor.conf.forms;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.jdom.Element;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptFormSchedulerStartScript extends ScriptForm {

    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(ScriptJobMainForm.class);
    @SuppressWarnings("unused")
    private final String conClassName = "ScriptFormSchedulerStartScript";
    private Group headerComposite = null;

    public ScriptFormSchedulerStartScript(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style, dom, job, main);
        initialize();
    }

    public void initForm() {
    }

    protected String getPredefinedFunctionNames() {
        return "spooler_process_before;spooler_process_after;spooler_task_before;spooler_task_after;";
    }

    protected String[] getScriptLanguages() {
        return objDataProvider._languagesJob;
    }

    protected void createGroup() {

        GridLayout gridLayoutMainOptionsGroup = new GridLayout();
        gridLayoutMainOptionsGroup.numColumns = 1;
        objMainOptionsGroup = new Group(this, SWT.NONE);
        objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());

        objMainOptionsGroup.setLayout(gridLayoutMainOptionsGroup);
        objMainOptionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 1;

        headerComposite = new Group(objMainOptionsGroup, SWT.NONE);
        headerComposite.setLayout(gridLayout);
        headerComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        createLanguageSelector(headerComposite);
        createScriptTabForm(objMainOptionsGroup);

    }

}
