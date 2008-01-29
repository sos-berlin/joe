package sos.scheduler.editor.app;

import org.eclipse.swt.widgets.Display;
 
public class Editor {
    public final static int CONFIG                 = 1;

    public final static int BASE                   = 2;

    public final static int SECURITY               = 3;
    
    public final static int CLUSTER                = 4;

    public final static int PROCESS_CLASSES        = 5;

    public final static int LOCKS                  = 6;

    public final static int SCRIPT                 = 7;

    public final static int WEBSERVICES            = 8;
    
    public final static int HTTPDIRECTORIES        = 9;

    public final static int HOLIDAYS               = 10;

    public final static int JOBS                   = 11;

    public final static int JOB_CHAINS             = 12;

    public final static int COMMANDS               = 13;

    public final static int ORDER                  = 14;

    public final static int ORDERS                 = 15;

    public final static int JOB                    = 16;

    public final static int EXECUTE                = 17;

    public final static int MONITOR                = 18;

    public final static int OPTIONS                = 19;

    public final static int LOCKUSE                = 20;

    public final static int RUNTIME                = 21;

    public final static int JOB_COMMANDS           = 22;

    public final static int JOB_COMMAND            = 23;

    public final static int WEEKDAYS               = 24;

    public final static int MONTHDAYS              = 25;

    public final static int ULTIMOS                = 26;

    public final static int SPECIFIC_WEEKDAYS      = 27;

    public final static int DAYS                   = 28;

    public final static int PERIODS                = 29;

    public final static int EVERYDAY               = 30;
    
    public final static int SPECIFIC_MONTHS        = 31;

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
    
    public final static int JOB_WIZZARD            = 71;
    
    public final static int DETAILS                = 72;
    
    public final static int JOB_CHAIN              = 73;

    public final static int HTTP_AUTHENTICATION    = 74;
    
    public final static int PARAMETER              = 75;
    
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
