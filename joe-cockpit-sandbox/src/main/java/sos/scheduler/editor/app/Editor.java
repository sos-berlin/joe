package sos.scheduler.editor.app;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sos.util.SOSClassUtil;

import com.sos.JSHelper.Logging.Log4JHelper;
import com.sos.dialog.SOSSplashScreen;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en") public class Editor {
	private final static String								conSVNVersion	= "$Id$";
	private static Logger									logger			= Logger.getLogger(JOEConstants.class);
	@SuppressWarnings("unused") private final String		conClassName	= "Editor";
	@SuppressWarnings("unused") private static Log4JHelper	objLogger		= null;
	public static JOEMainWindow								objMainWindow	= null;
	private static JOEMainWindow							window			= null;
	private static Display									display			= null;

	public static Image getSplashImage() {
		Image objImage4Splash = null;
		InputStream img = Editor.class.getResourceAsStream("/SplashScreenJOE.bmp");
		if (img == null) {
			System.out.println("'/SplashScreenJOE.bmp' not found in resources.");
		}
		else {
			objImage4Splash = new Image(display, img);
		}
		return objImage4Splash;
	}

	public static void main(final String[] args) {
		try {
			display = Display.getDefault();
			window = new JOEMainWindow();
			window.createSShell();
			final Shell shell = JOEMainWindow.getSShell();
			ErrorLog.setSShell(shell);
			Method objApplicationMainMethod = SOSSplashScreen.class.getMethod("openApplicationMainWnd", new Class[] { Shell.class });
			Image objImage4Splash = null;
			if (Options.showSplashScreen() == true) {
				objImage4Splash = getSplashImage();
			}
			SOSSplashScreen.startDialogExecuteLoop(shell, new Runnable() {
				@Override public void run() {
					doSomeTimeconsumingOperation();
				}
			}, objImage4Splash, objApplicationMainMethod, 2000);
		}
		catch (Exception e) {
			try {
				logger.fatal("sudden death", e);
				e.printStackTrace();
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + "cause: " + e.toString(), e);
			}
			catch (Exception ee) {
			}
		}
	}

	private static void doSomeTimeconsumingOperation() {
		try {
			objLogger = new Log4JHelper("./JOE-log4j.properties");
			logger = Logger.getRootLogger();
			logger.debug(conSVNVersion);
			window.OpenLastFolder();
			objMainWindow = window;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openApplicationMainWnd(final Shell shell) {
		JOEMainWindow.getSShell().open();
		JOEMainWindow.getSShell().update();
		while (!JOEMainWindow.getSShell().isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			catch (Exception e) {
				JOEMainWindow.getSShell().redraw();
			}
		}
		if (display.isDisposed() == false) {
			display.dispose();
		}
	}
}
