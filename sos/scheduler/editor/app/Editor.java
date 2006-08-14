package sos.scheduler.editor.app;

import org.eclipse.swt.widgets.Display;

public class Editor {
    public final static int CONFIG                 = 1;

    public final static int BASE                   = 2;

    public final static int SECURITY               = 3;

    public final static int PROCESS_CLASSES        = 4;

    public final static int SCRIPT                 = 5;

    public final static int WEBSERVICES            = 6;

    public final static int HOLIDAYS               = 7;

    public final static int JOBS                   = 8;

    public final static int JOB_CHAINS             = 9;

    public final static int COMMANDS               = 10;

    public final static int ORDER                  = 11;

    public final static int ORDERS                 = 12;

    public final static int JOB                    = 13;

    public final static int EXECUTE                = 14;

    public final static int MONITOR                = 15;

    public final static int OPTIONS                = 16;

    public final static int RUNTIME                = 17;

    public final static int JOB_COMMANDS           = 18;

    public final static int JOB_COMMAND            = 19;

    public final static int WEEKDAYS               = 20;

    public final static int MONTHDAYS              = 21;

    public final static int ULTIMOS                = 22;

    public final static int DAYS                   = 23;

    public final static int PERIODS                = 24;

    public final static int EVERYDAY               = 25;

    public final static int DOC_JOB                = 50;

    public final static int DOC_PROCESS            = 51;

    public final static int DOC_SCRIPT             = 52;

    public final static int DOC_MONITOR            = 53;

    public final static int DOC_RELEASES           = 54;

    public final static int DOC_RESOURCES          = 56;

    public final static int DOC_DATABASES          = 57;

    public final static int DOC_FILES              = 58;

    public final static int DOC_DOCUMENTATION      = 59;

    public final static int DOC_CONFIGURATION      = 60;

    public final static int DOC_PARAMS             = 61;

    public final static int DOC_PAYLOAD            = 63;

    public final static int DOC_SECTIONS           = 65;

    public final static int DOC_SETTINGS           = 66;

    public final static int DOC_SECTION_SETTINGS   = 67;

    public final static int DOC_PROFILES           = 68;

    public final static int DOC_CONNECTIONS        = 69;

    public final static int DOC_APPLICATIONS       = 70;

    public static String    SCHEDULER_ENCODING     = "";

    public static String    DOCUMENTATION_ENCODING = "utf-8";


    public static void main(String[] args) {
        Display display = Display.getDefault();
        MainWindow window = new MainWindow();
        window.createSShell();

        MainWindow.getSShell().open();
        MainWindow.getSShell().update();
        // window.getSShell().pack(true);

        // if(args.length > 0)
        // window.openFile(args[0]);

        while (!MainWindow.getSShell().isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

}
