package sos.scheduler.editor.conf.container;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_E_0002;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.dialog.classes.WindowsSaver;

public class JobSourceViewer extends FormBaseClass <JOEListener> {

	// TODO für die reine XML-Anzeige einfach ein Browser-Control verwenden und dort links verwenden, wie im WiKi
	@SuppressWarnings("unused")
	private final String	conSVNVersion		= "$Id$";
	private final String	conClassName		= "JobSourceViewer";
	private TextArea		txtArea4XMLSource	= null;

	public JobSourceViewer() {
		super();
		objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
		objFormPosSizeHandler.setKey(conClassName);
	}

	//	public JobSourceViewer(final Composite pParentComposite, final JobListener pobjDataProvider) {
	public JobSourceViewer(final Composite pParentComposite, final JOEListener pobjDataProvider) {
		super(pParentComposite, pobjDataProvider);
		objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
		objFormPosSizeHandler.setKey(conClassName);
		createGroup();
	}

	@Override
	public void refreshContent() {
		txtArea4XMLSource.refreshContent();
	}

	@Override
	public void createGroup() {

		try {
			createGroup2();
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
	}

	private void createGroup2() {
		try {
			showWaitCursor();
			txtArea4XMLSource = new TextArea(objParent, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
			txtArea4XMLSource.setEditable(false);
			txtArea4XMLSource.setDataProvider(objJobDataProvider, enuSourceTypes.xmlSource);
			txtArea4XMLSource.setFormHandler(objFormPosSizeHandler);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
		finally {
			restoreCursor();
		}
	}
}
