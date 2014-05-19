package sos.scheduler.editor.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * \class SOSSplashScreen 
 * 
 * \brief SOSSplashScreen - 
 * 
 * \details
 *
 *
 * \code
 *   .... code goes here ...
 * \endcode
 *
 * <p style="text-align:center">
 * <br />---------------------------------------------------------------------------
 * <br /> APL/Software GmbH - Berlin
 * <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
 * <br />---------------------------------------------------------------------------
 * </p>
 * \author KB
 * \version $Id$
 * \see reference
 *
 * Created on 07.02.2012 16:54:49
 */

/**
 * @author 
 *         http://www.tutorials.de/swing-java2d-3d-swt-jface/195899-swt-splashscreen
 *         -thread-problem.html
 * 
 */
public class SOSSplashScreen {

    @SuppressWarnings("unused") private final String conClassName = "SOSSplashScreen";
    private static final String conSVNVersion = "$Id$";
    private static final Logger logger = Logger.getLogger(SOSSplashScreen.class);

    public SOSSplashScreen() {
        //
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Display display = Display.getDefault();
        final Shell shell = new Shell();
        shell.setText("MainWND");

        Method m = SOSSplashScreen.class.getMethod("openApplicationMainWnd", new Class[] { Shell.class });

        SOSSplashScreen.startJOEExecuteLoop(shell, new Runnable() {
            public void run() {
                doSomeTimeconsumingOperation();
            }
        }, new Image(display, SOSSplashScreen.class.getResourceAsStream("/SplashScreenJOE.bmp")), m, 2000);
    }

    public static void startJOEExecuteLoop(final Shell pobjParentShell, final Runnable pobjRunnable, final Image pobjImage2Show, final Method m, final int pintHowLong2Show) throws Exception {

        final Shell splashShell = new Shell(pobjParentShell, SWT.NONE);

        final Display display = splashShell.getDisplay();

        if (pobjImage2Show != null) {
            splashShell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
            splashShell.setLayout(new FillLayout());

            Label label = new Label(splashShell, SWT.BORDER);
            label.setImage(pobjImage2Show);
            splashShell.pack();

            // center the dialog screen to the monitor
            Monitor primary = display.getPrimaryMonitor();
            Rectangle bounds = primary.getBounds();
            Rectangle rect = splashShell.getBounds();
            int x = bounds.x + (bounds.width - rect.width) / 2;
            int y = bounds.y + (bounds.height - rect.height) / 2;
            splashShell.setLocation(x, y);

            splashShell.open();
        }

        display.asyncExec(new Runnable() {
            public void run() {
                // Zeige den SplashScreen auf jeden Fall delay/1000 Sekunden an.
                display.timerExec(pintHowLong2Show, new Runnable() {
                    public void run() {
                        // nun erledige die eigentliche "Initialsierungsroutine"
                        pobjRunnable.run();
                        splashShell.close();
                        splashShell.dispose();
                    }
                });
            }
        });

        while (!splashShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        // splashShell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
        // Lade den eigentlichen Applikations Code -> Hauptfenster...
        m.invoke(null, new Object[] { pobjParentShell });

    }

    private static void doSomeTimeconsumingOperation() {
        System.out.println("Executing some importent initial environment checks...");
        try {
            Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished");
    }

    public static void openApplicationMainWnd(Shell shell) {
        System.out.println("now let#s do the real hard work...");
        shell.open();

        Display d = shell.getDisplay();

        while (!shell.isDisposed()) {
            if (!d.readAndDispatch()) {
                d.sleep();
            }
        }
    }
}
