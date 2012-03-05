package sos.scheduler.editor.app;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
 
import com.sos.JSHelper.Logging.Log4JHelper;
import com.sos.i18n.annotation.I18NResourceBundle;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class Editor /* extends I18NBase */ {
	private final static String	conSVNVersion			= "$Id$";
	private static Logger		logger					= Logger.getLogger(Editor.class);
	@SuppressWarnings("unused")
	private final String		conClassName			= "Editor";
	@SuppressWarnings("unused")
	private static Log4JHelper	objLogger				= null;

	public final static int		CONFIG					= 1;
	public final static int		BASE					= 2;
	public final static int		SECURITY				= 3;
	public final static int		CLUSTER					= 4;
	public final static int		PROCESS_CLASSES			= 5;
	public final static int		LOCKS					= 6;
	public final static int		SCRIPT					= 7;
	public final static int		WEBSERVICES				= 8;
	public final static int		HTTPDIRECTORIES			= 9;
	public final static int		HOLIDAYS				= 10;
	public final static int		JOBS					= 11;
	public final static int		JOB_CHAINS				= 12;
	public final static int		COMMANDS				= 13;
	public final static int		ORDER					= 14;
	public final static int		ORDERS					= 15;
	public final static int		JOB						= 16;
	public final static int		EXECUTE					= 17;
	public final static int		MONITOR					= 18;
	public final static int		OPTIONS					= 19;
	public final static int		LOCKUSE					= 20;
	public final static int		RUNTIME					= 21;
	public final static int		JOB_COMMANDS			= 22;
	public final static int		JOB_COMMAND				= 23;
	public final static int		WEEKDAYS				= 24;
	public final static int		MONTHDAYS				= 25;
	public final static int		ULTIMOS					= 26;
	public final static int		SPECIFIC_WEEKDAYS		= 27;
	public final static int		DAYS					= 28;
	public final static int		PERIODS					= 29;
	public final static int		EVERYDAY				= 30;
	public final static int		SPECIFIC_MONTHS			= 31;
	public final static int		DOC_JOB					= 50;
	public final static int		DOC_PROCESS				= 51;
	public final static int		DOC_SCRIPT				= 52;
	public final static int		DOC_MONITOR				= 53;
	public final static int		DOC_RELEASES			= 54;
	public final static int		DOC_RESOURCES			= 56;
	public final static int		DOC_DATABASES			= 57;
	public final static int		DOC_FILES				= 58;
	public final static int		DOC_DOCUMENTATION		= 59;
	public final static int		DOC_CONFIGURATION		= 60;
	public final static int		DOC_PARAMS				= 61;
	public final static int		DOC_PAYLOAD				= 63;
	public final static int		DOC_SECTIONS			= 65;
	public final static int		DOC_SETTINGS			= 66;
	public final static int		DOC_SECTION_SETTINGS	= 67;
	public final static int		DOC_PROFILES			= 68;
	public final static int		DOC_CONNECTIONS			= 69;
	public final static int		DOC_APPLICATIONS		= 70;
	public final static int		JOB_WIZARD				= 71;
	public final static int		DETAILS					= 72;
	public final static int		JOB_CHAIN				= 73;
	public final static int		HTTP_AUTHENTICATION		= 74;
	public final static int		PARAMETER				= 75;
	public final static int		JOB_COMMAND_EXIT_CODES	= 76;
	public final static int		SCHEDULES				= 77;
	public final static int		SCHEDULE				= 78;
	public final static int		MONITORS				= 79;
	public final static int		WEBSERVICE				= 80;
	public final static int		HTTP_SERVER				= 81;
	public final static int		JOB_CHAIN_NODES			= 82;
	public final static int		JOB_CHAIN_NESTED_NODES	= 83;
	public final static int		DOC_RELEASE				= 84;
	public final static int		DOC_RELEASE_AUTHOR		= 85;
	public final static int		DOC_DATABASES_RESOURCE	= 86;
	public final static int		SETTINGS				= 87;
	public final static int		ACTIONS					= 88;
	public final static int		ACTION					= 89;
	public final static int		EVENTS					= 90;
	public final static int		EVENT_GROUP				= 91;
	public final static int		ACTION_COMMANDS			= 92;
	public final static int		ADD_EVENT_GROUP			= 93;
	public final static int		REMOVE_EVENT_GROUP		= 94;
	public final static int		JOB_OPTION				= 95;
	public final static int		JOB_DOCUMENTATION		= 96;
	public static String		SCHEDULER_ENCODING		= "";
	public static String		DOCUMENTATION_ENCODING	= "utf-8";

	public static void main(String[] args) {
		try {
			objLogger = new Log4JHelper("./JOE-log4j.properties");

			logger = Logger.getRootLogger();
			logger.debug(conSVNVersion);

			Display display = Display.getDefault();
			MainWindow window = new MainWindow();
			window.createSShell();
			MainWindow.getSShell().open();
			MainWindow.getSShell().update();
			window.OpenLastFolder();
			// window.getSShell().pack(true);
//			if (args.length > 0) {
//				window.openFile(args[0]);
//			}
			while (!MainWindow.getSShell().isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
		catch (Exception e) {
			try {
				logger.fatal("sudden death", e);
				e.printStackTrace();
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "cause: " + e.toString(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
	}
}
