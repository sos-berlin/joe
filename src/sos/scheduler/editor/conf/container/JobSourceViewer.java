package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobSourceViewer extends FormBaseClass {

	@SuppressWarnings("unused")
	private final String	conSVNVersion			= "$Id$";

	private TextArea	txtArea4XMLSource	= null;

	public JobSourceViewer(Composite pParentComposite, JobListener pobjDataProvider) {
		super(pParentComposite, pobjDataProvider);
		createGroup();
	}

	public void apply() {
		// if (isUnsaved())
		// addParam();
	}

	public boolean isUnsaved() {
		return false;
	}

	public void refreshContent () {
		txtArea4XMLSource.refreshContent();
	}
	
	private void createGroup() {
		showWaitCursor();

		Group gSourceViewer = new Group(objParent, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
		gridData_5.heightHint = 100;
		gridData_5.minimumHeight = 30;
		gSourceViewer.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gSourceViewer.setLayout(gridLayout_2);
		gSourceViewer.setText(Messages.getLabel("job.executable.label"));

		txtArea4XMLSource = new TextArea(gSourceViewer, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		txtArea4XMLSource.setDataProvider(objDataProvider, enuSourceTypes.xmlSource);

		RestoreCursor();
	}

}
