package sos.scheduler.editor.conf.forms;

//import org.eclipse.draw2d.*;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.app.TreeData;

import com.sos.joe.objects.job.JobListener;
import com.sos.joe.objects.job.forms.ScriptForm;
import com.sos.scheduler.model.LanguageDescriptorList;
import com.sos.scheduler.model.objects.JSObjJob;

public class ScriptFormSchedulerStartScript extends ScriptForm  {

	@SuppressWarnings("unused") private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused") private final Logger logger = Logger.getLogger(this.getClass());
	

	private Group			headerComposite	= null;

	private TreeData				objTreeData			= null;

	public ScriptFormSchedulerStartScript(final Composite parent, final TreeData pobjTreeData) {
		super(parent, pobjTreeData);
		objDataProvider = new JobListener(pobjTreeData);
		objDataProvider._languages = JSObjJob.ValidLanguages4Job;
		objTreeData = pobjTreeData;
		objDataProvider._languages = LanguageDescriptorList.getLanguages4Monitor();
		initialize();
	}


//	public ScriptFormSchedulerStartScript(final Composite parent, final int style, final SchedulerDom dom, final Element job, final ISchedulerUpdate main) {
//		super(parent, style, dom, job, main);
//		initialize();
//	}
//
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
