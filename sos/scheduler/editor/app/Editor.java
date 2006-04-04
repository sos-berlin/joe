package sos.scheduler.editor.app;


import org.eclipse.swt.widgets.Display;

import sos.scheduler.editor.forms.MainWindow;

// TODO mussfelder markieren
// TODO toolbar
// TODO job options delay nach error count sortierten - bsp. in date



public class Editor {
	public final static int CONFIG = 1;
	public final static int BASE = 2;
	public final static int SECURITY = 3;
	public final static int PROCESS_CLASSES = 4;
	public final static int SCRIPT = 5;
	public final static int WEBSERVICES = 6;
	public final static int HOLIDAYS = 7;
	public final static int JOBS = 8;
	public final static int JOB_CHAINS = 9;
	public final static int JOB = 10;
	public final static int EXECUTE = 11;
	public final static int MONITOR = 12;
	public final static int OPTIONS = 13;
	public final static int RUNTIME = 14;
	public final static int WEEKDAYS = 15;
	public final static int MONTHDAYS = 16;
	public final static int ULTIMOS = 17;
	public final static int DAYS = 18;
	public final static int PERIODS = 19;
	public final static int EVERYDAY = 20;

	
	
	// TODO evtl. statisch auslagern
	DomParser _dom = new DomParser();
	
	DomParser getDom() {
		return _dom;
	}
	
	public static void main(String[] args) {
		Editor app = new Editor();
		
		Display display = Display.getDefault();
		MainWindow window = new MainWindow(app.getDom());
		app.getDom().setDataChangedListener(window);
		window.createSShell();
		
		window.getSShell().open();
		window.getSShell().update();
	//	window.getSShell().pack(true);

		if(args.length > 0)
			window.openFile(args[0]);
		
		while (!window.getSShell().isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
