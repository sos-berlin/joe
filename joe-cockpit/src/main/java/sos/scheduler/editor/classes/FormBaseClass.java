package sos.scheduler.editor.classes;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;

 
public class FormBaseClass {

	@SuppressWarnings("unused")
	private final String		conClassName	= "FormBaseClass";
	private static final Logger	LOGGER			= Logger.getLogger(FormBaseClass.class);

	protected JOEListener		objJobDataProvider	= null;
	protected Composite			objParent		= null;
	protected Shell				shell			= null;
	protected Cursor			objLastCursor	= null;
	protected FormBaseClass		objParentForm	= this;

    protected final int intComboBoxStyle = SWT.NONE;

	protected FormBaseClass(Composite parent, int style) {
        shell = parent.getShell();
	}

	public FormBaseClass(Composite pParentComposite, JOEListener pobjDataProvider) {
        super();
		objParent = pParentComposite;
		shell = pParentComposite.getShell();
		objJobDataProvider = pobjDataProvider;
		GridLayout grdL = new GridLayout();
		pParentComposite.setLayout(new GridLayout( ));
		setResizableV(pParentComposite);
 ;
	}

	protected void setResizableV(Control objControl) {
		boolean flgGrapVerticalspace = true;
		objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, flgGrapVerticalspace));
	}

	protected void setStatusLine(final String pstrText) {
		final int delay = 2000;
		final Display display = shell.getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				Editor.objMainWindow.setStatusLine(pstrText);
				try {
					Thread.sleep(delay);
				} 
				catch (InterruptedException e) {
					LOGGER.error(e.getMessage(),e);
				}
				Editor.objMainWindow.setStatusLine("");
			}
		});

	}

	protected void showWaitCursor() {
		if (!shell.isDisposed()) {
			objLastCursor = shell.getCursor();
		}
		shell.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_WAIT));
	}

	protected void restoreCursor() {
		if (!shell.isDisposed())
			if (objLastCursor == null) {
				shell.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_ARROW));
			}
			else {
				shell.setCursor(objLastCursor);
			}
	}

	protected IContainer getContainer() {
		return MainWindow.getContainer();
	}


	protected void MsgWarning(final String pstrMsgText) {
		MainWindow.message(pstrMsgText, SWT.ICON_WARNING);
		this.setStatusLine(pstrMsgText);
	}

	protected void Enable(Control objC, boolean flgStatus) {
		if (objC != null) {
			objC.setEnabled(flgStatus);
		}
	}

    protected Shell getShell() {
        return shell;
    }

}
