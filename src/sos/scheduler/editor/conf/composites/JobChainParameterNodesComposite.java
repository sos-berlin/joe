package sos.scheduler.editor.conf.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.classes.CompositeBaseClass;
import sos.scheduler.editor.conf.listeners.JobChainListener;

public class JobChainParameterNodesComposite extends CompositeBaseClass {
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id$";

	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(JobChainParameterNodesComposite.class);
	@SuppressWarnings("unused")
	private final String		conClassName	= "JobChainParameterNodesComposite";

	@SuppressWarnings("unused")
	private JobChainListener	objDataProvider	= null;

	private Composite				objMainControl;

	public JobChainParameterNodesComposite(final Composite parent, final JobChainListener pobjDataProvider) {
		super(parent, SWT.NONE);
		objDataProvider = pobjDataProvider;
		objParent = parent;
		createGroup(parent);
		init();
}

	public JobChainParameterNodesComposite(final Group parent, final int style, final JobChainListener pobjDataProvider) {
		super(parent, style);
		objDataProvider = pobjDataProvider;
		objParent = parent;

		createGroup(parent);
		init();
	}

	private void createGroup(final Composite parent) {
		init = true;
		objMainControl = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		objMainControl.setLayout(gridLayout);
		objMainControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

	}

	public void init() {
		init = true;
		init = false;
	}

	@Override
	protected void applyInputFields(final boolean flgT) {
	}
}
