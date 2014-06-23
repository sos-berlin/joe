package com.sos.joe.globals.messages;
/**
 *
 */

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.joe.globals.options.Options;

public class ErrorLog extends Exception {

	private static final long	serialVersionUID	= -4414810697191992062L;
	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id: ErrorLog.java 20985 2013-09-04 09:13:12Z ur $";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());

	public ErrorLog(final String msg) {
		super();
		try {

			init();
			message(msg, SWT.ERROR);
			logger.info(msg);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public ErrorLog(final String msg, final Exception e) {
		super();

		try {
			init();
			JobSchedulerException objJSE = new JobSchedulerException(msg, e);
			String strMsg = msg + "\n" + objJSE.ExceptionText();
			message(strMsg, SWT.ERROR);
			logger.error(strMsg);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

    public ErrorLog(final String application, String msg, final Exception e) {
        super();

        try {
            init();
            JobSchedulerException objJSE = new JobSchedulerException(msg, e);
            String strMsg = msg + "\n" + objJSE.ExceptionText();
            message(application, strMsg, SWT.ERROR);
            logger.error(strMsg);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
	//	/**
	//	 * @param args
	//	 */
	//	public static void main(final String[] args) throws ErrorLog{
	//
	//		try {
	//			throw new Exception("Testerror");
	//		} catch(Exception e) {
	//			new ErrorLog("mein Testerror", e);
	//		}
	//	}
	//
	private void init() {
		String filename = "";
		try {
			if (logger != null) {
				return;
			}
			filename = Options.getSchedulerData();
			if (filename.endsWith("/") || filename.endsWith("\\"))
				filename = filename + "logs";
			else
				filename = filename + "/logs";

			if (!new java.io.File(filename).exists())
				new java.io.File(filename).mkdirs();

			filename = filename + "/scheduler_editor.log";

//			if (logger == null)
//				logger = new SOSStandardLogger(filename, SOSStandardLogger.DEBUG1);

		}
		catch (Exception e) {
			try {
				if (logger != null)
					logger.debug("error in " + SOSClassUtil.getMethodName() + ", cause: " + e.getMessage());
			}
			catch (Exception f) {

			}
		}
		finally {

		}
	}

	public String getErrorMessage(final Exception ex) {
		String s = "";

		try {
			Throwable tr = ex.getCause();

			if (ex.toString() != null)
				s = ex.toString();

			while (tr != null) {
				if (s.indexOf(tr.toString()) == -1)
					s = (s.length() > 0 ? s + ", " : "") + tr.toString();
				tr = tr.getCause();
			}

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return s;
	}

	public static SOSStandardLogger getLogger() {
//		if (logger == null)
//			init();
//		return logger;
		return null;
	}
	
	private static Shell		sShell									= null;													// @jve:decl-index=0:visual-constraint="3,1"

	public static Shell getSShell() {
		return sShell;
	}


	public static int message(String message, int style) {
		return message(getSShell(), message, style);
	}

	public static int message(String application, String message, int style) {
        return message(getSShell(), application, message, style);
    }
	// /**
	// * Erzeugt einen Confirm-Dialog, wenn der Button zum schlieﬂen des Fensters
	// * bet‰tigt wird.
	// *
	// * @see org.eclipse.jface.window.Window#handleShellCloseEvent()
	// */
	// @Override
	// protected void handleShellCloseEvent () {
	// if (MessageDialog.openConfirm(null, "Best‰tigung",
	// "Wollen Sie das Programm beenden?")) {
	// super.handleShellCloseEvent();
	// }
	// }
	public static int message(Shell shell, String pstrMessage, int style) {
		MessageBox mb = new MessageBox(shell, style);
		if (mb == null) {
			return -1;
		}
		if (pstrMessage == null) {
			pstrMessage = "??????";
		}
		mb.setMessage(pstrMessage);
		String title = Messages.getLabel("message");
		if ((style & SWT.ICON_ERROR) != 0)
			title = Messages.getLabel("error");
		else {
			if ((style & SWT.ICON_INFORMATION) != 0)
				title = Messages.getLabel("information");
			else
				if ((style & SWT.ICON_QUESTION) != 0)
					title = Messages.getLabel("question");
				else
					if ((style & SWT.ICON_WARNING) != 0)
						title = Messages.getLabel("warning");
		}
		mb.setText("JOE: " + title);
		return mb.open();
	}
 
	public static int message(Shell shell, String application, String pstrMessage, int style) {
        MessageBox mb = new MessageBox(shell, style);
        if (mb == null) {
            return -1;
        }
        if (pstrMessage == null) {
            pstrMessage = "??????";
        }
        mb.setMessage(pstrMessage);
        String title = Messages.getLabel("message");
        if ((style & SWT.ICON_ERROR) != 0)
            title = Messages.getLabel("error");
        else {
            if ((style & SWT.ICON_INFORMATION) != 0)
                title = Messages.getLabel("information");
            else
                if ((style & SWT.ICON_QUESTION) != 0)
                    title = Messages.getLabel("question");
                else
                    if ((style & SWT.ICON_WARNING) != 0)
                        title = Messages.getLabel("warning");
        }
        mb.setText(application + ": "  + title);
        return mb.open();
    }

	public static void setSShell(Shell pobjShell) {
		sShell = pobjShell;
		
	}

}
