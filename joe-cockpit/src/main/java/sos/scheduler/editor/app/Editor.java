package sos.scheduler.editor.app;

import com.sos.dialog.SOSSplashScreen;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;

import java.io.InputStream;
import java.lang.reflect.Method;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class Editor {

    private final static String conSVNVersion = "$Id$";
    private final static Logger LOGGER = LoggerFactory.getLogger(JOEConstants.class);
    @SuppressWarnings("unused")
    private final String conClassName = "Editor";
    public static MainWindow objMainWindow = null;

    private static MainWindow window = null;
    private static Display display = null;

    public static void main(final String[] args) {
        try {
            display = Display.getDefault();
            window = new MainWindow();
            window.createSShell();
            final Shell shell = MainWindow.getSShell();
            ErrorLog.setSShell(shell);
            Method objApplicationMainMethod = SOSSplashScreen.class.getMethod("openApplicationMainWnd", new Class[] { Shell.class });
            Image objImage4Splash = null;
            if (Options.showSplashScreen() == true) {
                InputStream img = JOEConstants.class.getResourceAsStream("/SplashScreenJOE.bmp");
                if (img == null) {
                    System.out.println("'/SplashScreenJOE.bmp' not found in resources.");
                } else {
                    objImage4Splash = new Image(display, img);
                }
            }
            SOSSplashScreen.startDialogExecuteLoop(shell, new Runnable() {

                @Override
                public void run() {
                    doSomeTimeconsumingOperation();
                }
            }, objImage4Splash, objApplicationMainMethod, 2000);
        } catch (Exception e) {
            try {
                LOGGER.error("sudden death", e);
                LOGGER.error(e.getMessage(), e);
                new ErrorLog("error in " + SOSClassUtil.getMethodName() + "cause: " + e.toString(), e);
            } catch (Exception ee) {
            }
        }
    }

    private static void doSomeTimeconsumingOperation() {
        try {
            LOGGER.debug(conSVNVersion);
            window.OpenLastFolder();
            objMainWindow = window;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void openApplicationMainWnd(final Shell shell) {
        MainWindow.getSShell().open();
        MainWindow.getSShell().update();
        while (!MainWindow.getSShell().isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Exception e) {
                MainWindow.getSShell().redraw();
            }
        }
        if (display.isDisposed() == false) {
            display.dispose();
        }
    }
}
