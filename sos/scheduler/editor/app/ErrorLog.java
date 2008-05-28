package sos.scheduler.editor.app;
/**
 * 
 */

/**
 * @author mo
 *
 */

import sos.util.SOSStandardLogger;
import sos.util.SOSString;
import sos.util.SOSClassUtil;

public class ErrorLog extends Exception {

	private  static  SOSStandardLogger logger = null;  
	
	public ErrorLog(String msg) {
		super();
		try {

			init();	
			logger.info(msg);	
			
		} catch(Exception ex){			
			System.out.println(ex.getMessage());			
		}
	}
	
	public ErrorLog(String msg, Exception e) {
		super();		
		
		try {
			
			init();	
			logger.info(msg);
			
			if(logger.getLogLevel() > 6)
				logger.info(getErrorMessage(e));
					
		} catch(Exception ex){			
			System.out.println(ex.getMessage());			
		}
				
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws ErrorLog{
		// TODO Auto-generated method stub

		try {

			throw new Exception("Testerror");

		} catch(Exception e) {

			
			new ErrorLog("mein Testerror", e);

		}
	}
	
	private void init() {
		String filename = "";
		try {
			if(logger != null)
				return;
			//filename = "C:/temp/editor.log";
			filename = sos.scheduler.editor.app.Options.getSchedulerHome();
			if(filename.endsWith("/") || filename.endsWith("\\"))
				filename = filename + "logs";
			else 
				filename = filename + "/logs";
			
			if(!new java.io.File(filename).exists())
				new java.io.File(filename).mkdirs();
			
			filename = filename+ "/scheduler_editor.log";
				
			if(logger == null)
				logger = new SOSStandardLogger(filename, 9);
			
		} catch(Exception e) {
			try {
				if(logger != null)
					logger.debug("error in " + SOSClassUtil.getMethodName() + ", cause: " + e.getMessage());
			} catch(Exception f) {				

			}
		} finally {
			
		}
	}

	public String getErrorMessage(Exception ex) {
		String s = "";

		try {
			Throwable tr = ex.getCause();

			if(ex.toString() != null)
				s = ex.toString();

			while (tr != null){
				if(s.indexOf(tr.toString()) == -1)
					s = (s.length() > 0 ? s + ", " : "") + tr.toString();
				tr = tr.getCause();
			}


		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return s;
	}
}
