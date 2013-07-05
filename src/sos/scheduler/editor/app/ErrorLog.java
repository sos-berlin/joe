package sos.scheduler.editor.app;
/**
 *
 */

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;

import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;

import com.sos.JSHelper.Exceptions.JobSchedulerException;

public class ErrorLog extends Exception {

	private static final long	serialVersionUID	= -4414810697191992062L;
	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());

	public ErrorLog(final String msg) {
		super();
		try {

			init();
			MainWindow.message(msg, SWT.ERROR);
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
			MainWindow.message(strMsg, SWT.ERROR);
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
			filename = sos.scheduler.editor.app.Options.getSchedulerData();
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
}
