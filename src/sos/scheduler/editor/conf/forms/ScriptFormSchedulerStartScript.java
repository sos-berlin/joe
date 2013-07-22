package sos.scheduler.editor.conf.forms;

//import org.eclipse.draw2d.*;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.classes.LanguageDescriptorList;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class ScriptFormSchedulerStartScript extends ScriptForm implements IUpdateLanguage {

	@SuppressWarnings("unused")
	private final String	conSVNVersion	= "$Id$";

	@SuppressWarnings("unused")
	private static Logger	logger			= Logger.getLogger(ScriptJobMainForm.class);
	@SuppressWarnings("unused")
	private final String	conClassName	= "ScriptFormSchedulerStartScript";
	private Group			headerComposite	= null;

	public ScriptFormSchedulerStartScript(final Composite parent, final int style, final SchedulerDom dom, final Element job, final ISchedulerUpdate main) {
		super(parent, style, dom, job, main);
		initialize();
	}

	@Override
	public void initForm() {
	}

	@Override
	protected String getPredefinedFunctionNames() {
		return "spooler_process_before;spooler_process_after;spooler_task_before;spooler_task_after;";
	}

	@Override
	protected String[] getScriptLanguages() {
		return LanguageDescriptorList.getLanguages4APIJobs();
	}

	@Override
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
