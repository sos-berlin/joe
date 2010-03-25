package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ArrayList;
import java.util.jar.Manifest;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import sos.util.SOSString;
import sos.connection.*;


public class MainListener {

	//private     MainWindow         _gui           = null;

	private     IContainer         _container     = null;

	private     SOSString          sosString      = new SOSString();

	private     SOSConnection      sosConnection  = null;    

	public MainListener(MainWindow gui, IContainer container) {
		//_gui = gui;
		_container = container;
	}


	public void openHelp(String helpKey) {
		String lang = Options.getLanguage();
		String url = helpKey;

		try {
			//TODO: überprüfen, ob Datei wirklich existiert
			url = new File(url).toURL().toString();

			Program prog = Program.findProgram("html");
			if (prog != null)
				prog.execute(url);
			else {             	
				Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));

				//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler javascript:location.href='file://c:/scheduler/config/html/doc/en/xml/job.xml'");

			}
		} catch (Exception e) {

			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "; " + Messages.getString("MainListener.cannot_open_help", new String[] { url, lang,
						e.getMessage() }), e);
			} catch(Exception ee) {
				//tu nichts
			}

			e.printStackTrace();
			MainWindow.message(Messages.getString("MainListener.cannot_open_help", new String[] { url, lang,
					e.getMessage() }), SWT.ICON_ERROR | SWT.OK);
		}
	}


	public void showAbout() {
		TextDialog dialog = new TextDialog(MainWindow.getSShell());
		dialog.setText("About Job Scheduler Editor");
		String message = Messages.getString("MainListener.aboutText", Options.getVersion() + "\nSchema-Version:"
				+ Options.getSchemaVersion() + "\n" +
				"SVN " + getSVNVersion()		
		);
		dialog.setContent(message, SWT.CENTER);
		dialog.getStyledText().setEditable(false);
		StyleRange bold = new StyleRange();
		bold.start = 0;
		bold.length = message.lastIndexOf("\n");
		
		bold.fontStyle = SWT.BOLD;
		//dialog.getStyledText().setStyleRange(bold);
		dialog.setVisibleApplyButton(false);
		dialog.setShowWizzardInfo(false);
		//dialog.setSize(new org.eclipse.swt.graphics.Point(100, 200));
		dialog.open(false);
	}

	public String getSVNVersion()  {
		String svnVersion ="";
		try {


			Manifest manifest = null;			
			String classContainer = getClass().getProtectionDomain().getCodeSource().getLocation().toString();			
			java.net.URL manifestUrl = new java.net.URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
			
			if(classContainer.contains(".jar")) {
				manifest = new Manifest(manifestUrl.openStream());
			} else {
				manifest = new Manifest(new java.net.URL(classContainer + "/META-INF/MANIFEST.MF").openStream());
			}
			if(manifest != null) {
				java.util.jar.Attributes atr = manifest.getMainAttributes();
				Iterator it = atr.keySet().iterator();
				while(it.hasNext()) {		
					String key = it.next().toString();
					if(key.contains("Implementation-Version")){
						String value = atr.getValue(key);
						svnVersion = svnVersion + key +"="+ value ;
					}
				}
			}
			
		} catch(Exception e) {
			MainWindow.message("could not read SVN-Version " , SWT.ICON_WARNING | SWT.OK);
		}
		return svnVersion;
	}

	public void setLanguages(Menu menu) {
		boolean found = false;
		MenuItem defaultItem = null;
		HashMap langs = Languages.getLanguages();
		Iterator it = langs.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			String val = (String) langs.get(key);

			MenuItem item = new MenuItem(menu, SWT.RADIO);
			item.setText(key);
			item.setData(val);
			if (Options.getLanguage().equals(val)) {
				found = true;
				item.setSelection(true);
			}

			if (Options.getDefault("editor.language").equals(val))
				defaultItem = item;

			item.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					MenuItem item = (MenuItem) e.widget;
					if (item.getSelection()) {
						String lang = (String) item.getData();
						Options.setLanguage(lang);
						loadMessages();
					}
				}


				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
		}

		if (!found) {
			String def = Options.getDefault("editor.language");
			MainWindow.message("The language " + Options.getLanguage() + " was not found - setting to " + def,
					SWT.ICON_WARNING | SWT.OK);
			Options.setLanguage(def);
			if (defaultItem != null)
				defaultItem.setSelection(true);
		}
	}


	public void loadMessages() {
		if (!Messages.setResource(new Locale(Options.getLanguage()))) {
			MainWindow.message("The resource bundle " + Messages.getBundle() + " for the language "
					+ Options.getLanguage() + " was not found!", SWT.ICON_ERROR | SWT.OK);
		}
		_container.updateLanguages();
	}

	public void resetInfoDialog() {
		Options.setShowWizardInfo(true);	
	}

	public void loadOptions() {
		String msg = Options.loadOptions(getClass());
		if (msg != null)
			MainWindow.message("No options file " + Options.getDefaultOptionFilename() + " found - using defaults!\n"
					+ msg, SWT.ICON_ERROR | SWT.OK);
	}


	public void saveOptions() {
		String msg = Options.saveProperties();
		if (msg != null)
			MainWindow.message("Options cannot be saved!\n" + msg, SWT.ICON_ERROR | SWT.OK);
	}

	public void loadJobTitels() {

		String titleFile = Options.getProperty("title_file");
		String iniFile = Options.getProperty("ini_file");
		try {
			if(sosString.parseToString(titleFile).length() == 0 || sosString.parseToString(iniFile).length() == 0)
				return;


			String home = new File(Options.getDefaultOptionFilename()).getParent();
			home = home.endsWith("/") || home.endsWith("\\") ? home : home + "/";
			iniFile = home + iniFile; 
			ArrayList jobTitleList = new ArrayList();

			try {
				getConnection(iniFile);
				jobTitleList = sosConnection.getArray(titleFile);
			} catch(Exception e) {
				throw new Exception("Could not get the connection to database, cause: " + e.toString());
			}


			String[] titles = new String[jobTitleList.size()];
			for(int i = 0; i<jobTitleList.size(); i++) {            	 
				HashMap hash = (HashMap)jobTitleList.get(i);    			
				titles [i] = sosString.parseToString(hash, "description");
			}
			Options.setJobTitleList(titles);

		} catch (Exception e) {    		
			try {
				System.out.println("error while read job descrition " + sos.util.SOSClassUtil.getMethodName());
				new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch (Exception ee){
				//tu nichts
			}

			return;
		} 


	}
	/*   public void loadJobTitels() {

    	String titleFile = Options.getProperty("title_file");
    	String iniFile = Options.getProperty("ini_file");
    	String sIniFile = ""; 
    	try {
    		if(iniFile != null) {
    			String home = Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome()+ "/";  
    			iniFile = home + "config/" + iniFile;

    			SOSProfileSettings settings = new SOSProfileSettings(iniFile);
    			sIniFile = " " + settings.getSection("spooler").getProperty("db") + " ";

    		}
    	} catch (Exception e) {    		
    		try {
    			new ErrorLog("could not Read Setting from " +iniFile + " " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch (Exception ee){
    			//tu nichts
    		}
    		System.out.println("could not Read Setting from " +iniFile + e);
    		return;
    	}


    	if(titleFile == null && titleFile.length() == 0)
    		return;

    	 sos.hostware.File inFile = null;


         //String inFileName = "-in -type=(summary_id,description) xml -encoding=iso-8859-1 -tag=summary_descriptions -record-tag=summary_description -lower-tags | j:/log/mo/dowjones/dowjones_summary_descriptions.xml";
         //String inFileName = "-in jdbc -class=com.sybase.jdbc3.jdbc.SybDriver jdbc:sybase:Tds:wilma:4112/scheduler -user=scheduler -password=scheduler select SUMMARY_ID, DESCRIPTION from DOWJONES_SUMMARY_DESCRIPTIONS";

    	 String inFileName = "-in " + sIniFile + titleFile;
    	// System.out.println(inFileName);

    	 ArrayList jobTitleList = new ArrayList();    	       

         try {

             inFile = new sos.hostware.File();
             inFile.open(inFileName);
             //System.out.println(inFile.field_count());

             while (!inFile.eof()) {
                 Record record = inFile.get();

                 for(int i=0; i<record.field_count(); i++) {
                	 //System.out.println("record: " + i + ", field: " + record.field_name(i) + ", value: " + record.string(i));
                	 if(record.field_name(i) != null && record.field_name(i).toLowerCase().equals("description")) {
                		 jobTitleList.add(record.string(i));

                	 }
                 }
                 record.destruct();


             }

             String[] titles = new String[jobTitleList.size()];
             for(int i = 0; i<jobTitleList.size(); i++)            	 
            	 titles [i] = jobTitleList.get(i) != null ? jobTitleList.get(i).toString(): "";

             Options.setJobTitleList(titles);

         } catch (Exception e) {
             System.out.println(e);
         } finally {        	
             if (inFile != null) try { if (inFile.opened()) inFile.close(); inFile.destruct(); } catch (Exception ex) {} // ignore this error
         }

    }
	 */

	public void loadHolidaysTitel() {

		try {
			HashMap holidaysDescription = loadHolidaysDescription("holiday_description_file");
			HashMap holidayFile = loadHolidaysDescription("holiday_file");



			HashMap filenames = new HashMap();

			String home = Options.getSchedulerNormalizedHotFolder();

			Iterator desc = holidaysDescription.keySet().iterator();    	
			while(desc.hasNext()) {

				String holidayId = desc.next().toString();
				if(!holidayId.startsWith("holiday_id")) {
					String xml = "<holidays>";
					holidayId = holidaysDescription.get(holidayId).toString();
					String filename = home + holidayId + ".holidays.xml";
					Iterator files = holidayFile.keySet().iterator();
					while(files.hasNext()) {
						String date = files.next().toString();
						if( holidayFile.get(date) != null && 
								holidayFile.get(date).toString().equalsIgnoreCase(holidayId)) {
							xml = xml + "<holiday date=\"" + date.substring(date.indexOf("_") +1) + "\"/>";
						}    			
					}
					xml = xml + "</holidays>";
					filenames.put("file_" + holidayId, filename);
					IOUtils.saveXML(xml, filename);
				}
			}

			holidaysDescription.putAll(filenames);
			Options.setHolidaysDescription(holidaysDescription);

		} catch(Exception e) {
			try {
				System.out.println("error while read holidays description " + sos.util.SOSClassUtil.getMethodName());
				new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch (Exception ee){
				//tu nichts
			}
		} finally {
			disconnect();
		}

	}

	public HashMap loadHolidaysDescription(String propertyName) {
		HashMap holidaysDescription = new HashMap();
		String holidayDescriptionFile = Options.getProperty(propertyName);
		String iniFile = Options.getProperty("ini_file");

		try {


			if(sosString.parseToString(holidayDescriptionFile).length() == 0 ||
					sosString.parseToString(iniFile).length() == 0)
				return new HashMap();


			String home = new File(Options.getDefaultOptionFilename()).getParent();
			home = home.endsWith("/") || home.endsWith("\\") ? home : home + "/";
			iniFile = home + iniFile; 
			ArrayList holidayList = new ArrayList();

			try {
				getConnection(iniFile);
				holidayList = sosConnection.getArray(holidayDescriptionFile);
			} catch(Exception e) {
				throw new Exception("Could not get the connection to database, cause: " + e.toString());
			}

			String holidayId = "";
			String field2 = "";

			for(int i = 0; i < holidayList.size(); i++) {

				HashMap hash = (HashMap)holidayList.get(i);
				if(sosString.parseToString(hash, "holiday_id").length() > 0) {
					holidayId = sosString.parseToString(hash, "holiday_id");
				} 

				if(sosString.parseToString(hash, "description").length() > 0) {    					
					field2 = sosString.parseToString(hash, "description");
					//merke: holiday_id_<id>, description    					
					holidaysDescription.put("holiday_id_"+holidayId, field2);
					//merke: description, <id> 
					holidaysDescription.put(field2, holidayId);   
					holidayId = "";
					field2 = "";
				}
				if(sosString.parseToString(hash, "holiday_date").length() >0) {				
					field2 = sosString.parseToString(hash, "holiday_date");
					//merke: <id>+_+<datum>, id -> datum ist nicht eindeutig, deshalb kommt der der prefix id
					holidaysDescription.put(holidayId + "_" + field2, holidayId);    					
					holidayId = "";
					field2 = "";
				}
			}
		} catch (Exception e) {    		
			try {
				System.out.println("error while read holidays description " + sos.util.SOSClassUtil.getMethodName());
				new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch (Exception ee){
				//tu nichts
			}


		}
		return holidaysDescription;

	}


	//holiday_file
	/* public HashMap loadHolidaysDescription(String propertyName) {
    	HashMap holidaysDescription = new HashMap();
    	//System.out.println("******************"+ propertyName +"*****************************");
    	//get description
    	//String holidayDescriptionFile = Options.getProperty("holiday_description_file");
    	String holidayDescriptionFile = Options.getProperty(propertyName);

    	String iniFile = Options.getProperty("ini_file");
    	String sIniFile = ""; 
    	try {
    		if(iniFile != null) {
    			String home = Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome() : Options.getSchedulerHome()+ "/";  
    			iniFile = home + "config/" + iniFile;

    			SOSProfileSettings settings = new SOSProfileSettings(iniFile);
    			sIniFile = " " + settings.getSection("spooler").getProperty("db") + " ";

    		}
    	} catch (Exception e) {    		
    		//MainWindow.message("could not Read Setting from " +iniFile, SWT.ICON_ERROR | SWT.OK);
    		System.out.println("could not Read Setting from " +iniFile + e);
    		return new HashMap();
    	}


    	if(holidayDescriptionFile == null && holidayDescriptionFile.length() == 0)
    		return new HashMap();

    	sos.hostware.File inFile = null;

    	String inFileName = "-in " + sIniFile + holidayDescriptionFile;
    	//System.out.println(inFileName);

    	try {

    		inFile = new sos.hostware.File();
    		inFile.open(inFileName);             
    		String holidayId = "";
    		String field2 = "";

    		while (!inFile.eof()) {
    			Record record = inFile.get();
    			for(int i=0; i<record.field_count(); i++) {

    				//System.out.println("record: " + i + ", field: " + record.field_name(i) + ", value: " + record.string(i));

    				if(record.field_name(i) != null && record.field_name(i).toLowerCase().equals("holiday_id")) {
    					holidayId = record.string(i);
    				} else  if(record.field_name(i) != null && record.field_name(i).toLowerCase().equals("description")) {    					
    					field2 = record.string(i);
    					//merke: holiday_id_<id>, description    					
    					holidaysDescription.put("holiday_id_"+holidayId, field2);
    					//merke: description, <id> 
    					holidaysDescription.put(field2, holidayId);   
    					holidayId = "";
    					field2 = "";
    				//} else  if(record.field_name(i) != null && record.field_name(i).toLowerCase().equals("holiday_date")) {
    				} else {
    					field2 = record.string(i);
    					//merke: <id>+_+<datum>, id -> datum ist nicht eindeutig, deshalb kommt der der prefix id
    					holidaysDescription.put(holidayId + "_" + field2, holidayId);    					
    					holidayId = "";
    					field2 = "";
    				}


    			}
    			record.destruct();
    		}



    	} catch (Exception e) {
    		System.out.println(e);
    	} finally {
    		if (inFile != null) try { if (inFile.opened()) inFile.close(); inFile.destruct(); } catch (Exception ex) {} // ignore this error
    	}
    	return holidaysDescription;

    }
	 */
	/**
	 * DB Initialisierung
	 */
	private void getConnection (String iniFile) throws Exception {

		try { 
			if(sosConnection != null)        		  
				return;

			//sosConnection = SOSConnection.createInstance( iniFile, new sos.util.SOSStandardLogger(sos.util.SOSStandardLogger.INFO)) ;
			sosConnection = SOSConnection.createInstance( iniFile, ErrorLog.getLogger()) ;
			sosConnection.connect();


		} catch (Exception e) {
			try {
				System.out.println("error while read job descrition " + sos.util.SOSClassUtil.getMethodName());
				new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch (Exception ee){
				//tu nichts
			}
		}
	}

	public void disconnect() {
		try {
			if(sosConnection != null) sosConnection.disconnect(); 
		} catch (Exception e) {
			//tu nichts    	
		}
	}

}
