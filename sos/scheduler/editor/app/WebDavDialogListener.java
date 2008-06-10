package sos.scheduler.editor.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;
import org.apache.webdav.lib.WebdavResource;
import org.apache.webdav.lib.WebdavResources;
import org.eclipse.swt.SWT;
import sos.settings.SOSProfileSettings;
import sos.util.SOSString;
import sos.util.SOSUniqueID;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class WebDavDialogListener {

	private              SOSString              sosString                     = null;

	private              String[]               profileNames                  = null;

	private              HashMap                profiles                      = null;

	private              Properties             currProfile                   = null;

	private              String                 currProfileName               = null;

	private              SOSProfileSettings     settings                      = null;

	public static        String                 PREFIX                        = "webdav_profile ";

	private              String                 configFile                    = "";

	private              Combo                  cboConnectname                = null;

	private              Text                   logtext                       = null;

	private              String                 password                      = "";

	private              boolean                hasError                      = false;

	private              Text                   txtURL                       = null;		

	
	public WebDavDialogListener(java.util.Properties profile, String profilename) {
		sosString = new SOSString();
		currProfile = profile;
		currProfileName = profilename; 
		profiles = new HashMap();
		profiles.put(profilename,profile);
	}

	public WebDavDialogListener() {

		sosString = new SOSString();

		String sep = "/";
		try {		

			configFile = Options.getSchedulerHome();

			configFile = configFile.endsWith("/") || configFile.endsWith("\\") ? configFile : configFile + sep;
			configFile = configFile + "config" + sep + "factory.ini";

			if(!new File(configFile).exists()) {
				new File(configFile).createNewFile();
			}

			settings = new SOSProfileSettings(configFile);
			ArrayList l = settings.getSections();
			profileNames = convert(settings.getSections().toArray());

			profiles = new HashMap();
			for(int i = 0; i < l.size(); i++) {
				String section = sosString.parseToString(l.get(i));

				if(section.toLowerCase().startsWith(WebDavDialogListener.PREFIX)) {
					String sectionWithoutPrefix = section.substring(WebDavDialogListener.PREFIX.length());
					Properties prop = settings.getSection(section);
					profiles.put(sectionWithoutPrefix, prop);
					if( currProfileName == null) {
						init(sectionWithoutPrefix);
						//currProfileName = sectionWithoutPrefix;
						//currProfile = prop;
					}
				}

			}

		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() +  "; could not read Profiles from " + configFile, e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true; 
			MainWindow.message("could not read Profiles from " + configFile, SWT.ICON_WARNING);
		}
	}

	private void init(String profile) {

		currProfileName = profile;
		currProfile = (Properties)profiles.get(profile);

	}

	private String[] convert(Object[] obj) {
		ArrayList str = new ArrayList();
		String[] retVal = null;
		try {						
			for(int i = 0; i < obj.length; i++) {
				if(sosString.parseToString(obj[i]).startsWith(WebDavDialogListener.PREFIX)) {
					str.add(sosString.parseToString(obj[i]).substring(WebDavDialogListener.PREFIX.length()));
				}
			}
			retVal = new String[str.size()];
			for(int i = 0; i < str.size(); i++) {
				retVal[i] = str.get(i).toString();
			}

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			//System.out.println("..error in webdavDilagProfiles: " + e.getMessage());
			hasError = true;
		}
		return retVal;
	}

	public String[] getProfileNames() {
		try {
			profileNames = convert(settings.getSections().toArray());		
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			//System.out.println(e.getMessage());
		}
		return profileNames;
	}

	public void setProfileNames(String[] profileNames) {
		this.profileNames = profileNames;
	}

	public HashMap getProfiles() {
		return profiles;
	}

	public void setProfiles(String key, Properties profile) {
		try {
			settings.getSections().add(WebDavDialogListener.PREFIX + key);
			this.profiles.put(key, profile);
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
			System.out.println("error in webdavDialoListener: " + e.getMessage());
		}
	}

	public Properties getCurrProfile() {
		return currProfile;
	}

	public void setCurrProfile(Properties currProfile) {
		this.currProfile = currProfile;
	}

	public String getCurrProfileName() {
		return currProfileName;
	}

	public void setCurrProfileName(String currProfileName) {
		this.currProfileName = currProfileName;
		init(currProfileName);
	}


	public void removeProfile(String profilename) {
		try {
			deleteProfile(profilename);
			getProfiles().remove(profilename);
			removeFromProfilenames(profilename);
			settings.getSections().remove(WebDavDialogListener.PREFIX +profilename);
			currProfile = new Properties();
			currProfileName = "";
		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "could not remove Profile: " + profilename, e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("could not remove Profile: " + profilename + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);
		}

	}



	public HashMap changeDirectory(String profile, String directory) {

		try {
			/*if(isLoggedIn && !currProfileName.equals(profile))
				disconnect();

			if(!isLoggedIn)				
				connect(profile);

			if(!isLoggedIn)//connect war nicht erfolgreich
				return new HashMap();
			 */
			init(profile);

			if(!directory.endsWith("/"))
				directory = directory+ "/";

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not change Directory [" + directory + "] ", e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			MainWindow.message("could not change Directory [" + directory + "] cause:" + e.getMessage() ,  SWT.ICON_WARNING);
		}

		return changeDirectory(directory );


	}


	public String getFile(String url, String subFolder) {

		File fn = null;
		try { 		



			
			WebdavResource wdr = connect(url);
			//Datei holen            			
			fn = new File(currProfile.getProperty("localdirectory") + "//" +  new File(url).getName());
			boolean retVal = wdr.getMethod(fn);

			hasError = !retVal;
			wdr.close();

			if(logtext != null)  logtext.append("..webdav server reply [getfile] [status= "  + wdr.getStatusMessage() );
			
			return fn.getCanonicalPath();
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ;could not get File [" + url + "]", e);
			} catch(Exception ee) {
				//tu nichts
			}
			if(logtext != null)  logtext.append("could not get File [" + url + "] :" + e.getMessage() );
			hasError = true;
		}
		return null;
	}

	/*public HashMap cdUP() {		
		return changeDirectory("..");		
	}
	 */
	public void removeFile(String file) {
		WebdavResource wdr = null;
		try   {

			wdr = connect(file);
			if(wdr.deleteMethod()){
				if (logtext != null)  logtext.append("..webdav server reply [delete file=" + file + "], [status= "  + wdr.getStatusMessage());
				
			}else {
				hasError = true;
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not delete file [" + file+ "], [status= "  + wdr.getStatusMessage());
				if(logtext != null)  logtext.append("..webdav server reply [could not delete] , [status= "  + wdr.getStatusMessage());
			}

			wdr.close();
		} catch (Exception e) {
			
			try {				
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not delete file [" + file+ "]", e);
				if(wdr != null)
					wdr.close();
			} catch(Exception ee) {
				//tu nichts
			}
			if(logtext != null)  logtext.append("could not delete file [" + file+ "] cause:" + e.getMessage() );
			hasError = true;
		}
	}

	private WebdavResource connect(String url){

		WebdavResource wdr = null;
		String user     = null;
		String password = null;
		String host     = null;
		String port     = null;

		try {

			//selfsigned; wenn selbssignierte Certifikate berücksichtigt werden sollen
			//es gilt jetzt nur die offizielle
			//System.getProperties().put("javax.net.ssl.trustStore","C:/scheduler/webdav_keystore/.keystore");
			//System.getProperties().put("javax.net.ssl.trustStorePassword","changeit");


			user = sosString.parseToString(currProfile.get("user"));
			password = sosString.parseToString(currProfile.get("password"));
			
			String key = Options.getProperty("profile.timestamp." + currProfileName);

			if(key != null && key.length() > 8) {
				key = key.substring(key.length()-8);
			}
			if(password.length() > 0 && sosString.parseToString(key).length() > 0) {
				password = SOSCrypt.decrypt(key, password);
			}
			if(password.length() == 0 ) {
				Shell shell = new Shell();
				shell.pack();					
				Dialog dialog = new Dialog(shell);		
				dialog.setText("Password");
				dialog.open(this);

				while (!shell.isDisposed()) {
					if (!shell.getDisplay().readAndDispatch())
						shell.getDisplay().sleep();
				}
				//shell.getDisplay().dispose();
				//shell.dispose();
				password = getPassword();	
			}


			HttpURL hrl = new HttpURL(url);			

			hrl.setUserinfo(user,password);
			wdr = new WebdavResource(hrl);

			wdr.setDebug(9);
			if(logtext != null)  logtext.append("..webdav server reply [connect] [status= "  + wdr.getStatusMessage() );
		} catch (Exception ex) {

			hasError = true;

			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; error in webdav server init with [host=" + host + "], [port="+ port + "].", ex);
			} catch(Exception ee) {
				//tu nichts
			}

			if(logtext != null)  logtext.append("..error in webdav server init with [host=" + host + "], [port="+ port + "], cause: " + getErrorMessage(ex) + "\n");
		}
		return wdr;
	}

	public HashMap changeDirectory(String directory) {	
		HashMap listnames = new HashMap();
		try {
			
			WebdavResource wdr = connect(directory);

			if(wdr == null)
				throw new Exception ("could not connect to WebDav Server");

			WebdavResources e = wdr.getChildResources();
			java.util.Enumeration en = e.getResources();
			while(en.hasMoreElements()) {
				WebdavResource cr = (WebdavResource)en.nextElement();	            	
				System.out.println(cr);
				if(cr.isCollection())
					listnames.put(cr, "dir");
				else
					listnames.put(cr, "file");
			}
			if(logtext != null)  logtext.append("..webdav server reply [changeDirectory] [status= "  + wdr.getStatusMessage() );
		} catch (Exception e) {

			MainWindow.message("could not change Directory [" + directory + "] cause:" + e.getMessage(), SWT.ICON_WARNING);

			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not change Directory [" + directory + "]", e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			if(logtext != null)  logtext.append("could not change Directory [" + directory + "] cause:" + e.getMessage() );
		}
		return listnames;

	}


	public void setLogText(org.eclipse.swt.widgets.Text text) {
		logtext = text;
	}

	public void saveAs(String source, String url) {

		source = source.replaceAll("\\\\", "/");
		boolean retVal = false;
		WebdavResource wdr = null;
		try {

			wdr = connect(url);                    

			if(wdr.putMethod(new java.io.File(source))) {
				retVal = true;	            	
			}
			hasError = !retVal;

			if(logtext != null)  {
				if(hasError)
					logtext.append(source + " could not transfer to " + url );
				else
					logtext.append(source + " transfer to " + url );
			}
			if(logtext != null)  logtext.append("..webdav server reply: saveas[url=" + url +  "], [status= "  + wdr.getStatusMessage());
		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..webdav server reply, [status= "  + wdr.getStatusMessage() + "]", e);
			} catch(Exception ee) {
				//tu nichts
			}
			if(logtext != null)  logtext.append("..webdav server reply: ");
			hasError = true;
		}
	}

	public void saveHotFolderAs(String source, String target, ArrayList listOfHotFolderElements, HashMap changes) {
		try {

			source = source.endsWith("/") ? source : source + "/"; 
			target = target.endsWith("/") ? target : target + "/";

			if(listOfHotFolderElements != null) {
				for(int i = 0; i < listOfHotFolderElements.size(); i++) {
					String filename = sosString.parseToString(listOfHotFolderElements.get(i));
					filename = new File(filename).getName();

					String hotElementname = "";					
					String attrname = "";					
					hotElementname = filename.substring(0, filename.lastIndexOf(".xml"));
					hotElementname = hotElementname.substring(hotElementname.lastIndexOf(".")+1);					
					attrname = filename.substring(0, filename.indexOf("." + hotElementname  + ".xml"));


					if(changes.containsKey(hotElementname + "_" + attrname) && changes.get(hotElementname + "_" + attrname).equals("delete"))
						removeFile(target + filename);						
				}
			}

			File fSource = new File(source);
			if(!fSource.exists())
				throw new Exception (source + " not exist." );

			if(!fSource.isDirectory())
				throw new Exception (source + " is not a directory." );

			String[] files =  fSource.list();
			for(int i = 0; i < files.length; i++) {
				String sourcefile = source + files[i];
				String targetFile = target + new File(sourcefile).getName();

				String hotElementname = "";					
				String attrname = "";				
				hotElementname = new File(sourcefile).getName().substring(0, new File(sourcefile).getName().lastIndexOf(".xml"));
				hotElementname = hotElementname.substring(hotElementname.lastIndexOf(".")+1);				
				attrname = new File(sourcefile).getName().substring(0, new File(sourcefile).getName().indexOf("." + hotElementname  + ".xml"));

				if(changes.containsKey(hotElementname + "_" + attrname) && 
						(changes.get(hotElementname + "_" + attrname).equals("modify") ||
								changes.get(hotElementname + "_" + attrname).equals("new"))) 
					saveAs(sourcefile, targetFile);
			}


		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..webdav server reply: "  , e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			if(logtext != null)  logtext.append("..webdav server reply: " );
		}
	}

	/**
	 * alles speichern
	 * @param source
	 * @param target
	 * @param listOfHotFolderElements
	 */
	public ArrayList saveHotFolderAs(String source, String target) {
		ArrayList list = new ArrayList();
		try {

			source = source.endsWith("/") ? source : source + "/"; 
			target = target.endsWith("/") ? target : target + "/";


			File fSource = new File(source);
			if(!fSource.exists())
				throw new Exception (source + " not exist." );

			if(!fSource.isDirectory())
				throw new Exception (source + " is not a directory." );



			String[] files =  fSource.list();
			for(int i = 0; i < files.length; i++) {
				String sourcefile = source + files[i];
				//String targetFile = new File(sourcefile).getName();
				String targetFile = target + new File(sourcefile).getName();	
				//System.out.println("source: " + sourcefile + ", target: " + targetFile);
				saveAs(sourcefile, targetFile);
				//list.add(target +targetFile);
				list.add(targetFile);
			}
			

		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..webdav server reply: " , e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			if(logtext != null)  logtext.append("..webdav server reply: "  );
		}
		return list;
	}
	public static byte[] getBytesFromFile(File file) throws IOException {

		byte[] bytes = null;
		try {
			InputStream is = new FileInputStream(file);

			long length = file.length();

			bytes = new byte[(int)length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "+file.getName());
			}        
			is.close();

		} catch(Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
		}
		return bytes;
	}

	public void deleteProfile(String profilename) throws Exception {
		try {
			setCurrProfileName(profilename);
			//java.util.Properties profile = getCurrProfile();
			String filename = configFile;


			byte[] b = getBytesFromFile(new File(filename));
			String s = new String(b);
			//System.out.println(s);

			int pos1 = s.indexOf("[profile "+profilename+"]");
			int pos2 = s.indexOf("[", pos1+1);

			if(pos1 == -1 ) {
				//System.out.println("profile nicht gefunden");
				pos1 = s.length();
				pos2 = -1;
				return;
			}

			if(pos2 == -1)
				pos2 = s.length();

			String s2 = s.substring(0, pos1) + s.substring(pos2);

			java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.wrap(s2.getBytes());

			java.io.File file = new java.io.File(filename);

			boolean append = false;

			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(file, append).getChannel();

			wChannel.write(bbuf);

			wChannel.close();

		} catch (java.io.IOException e) {

			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not delete profile=" + profilename, e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			throw new Exception (e.getMessage());

		} finally {

			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			txtURL.setText(currProfile.getProperty("url"));
		}


	}

	public void saveProfile(boolean savePassword) {
		try {

			java.util.Properties profile = getCurrProfile();
			String filename = configFile;
			String profilename = currProfileName;

			byte[] b = getBytesFromFile(new File(filename));
			String s = new String(b);
			//System.out.println(s);

			int pos1 = s.indexOf("[profile "+profilename+"]");
			int pos2 = s.indexOf("[", pos1+1);

			if(pos1 == -1 ) {
				//System.out.println("profile nicht gefunden");
				pos1 = s.length();
				pos2 = -1;
			}

			if(pos2 == -1)
				pos2 = s.length();

			String s2 = s.substring(0, pos1);

			s2 = s2 + "[" + PREFIX + profilename + "]\n\n";
			s2 = s2 + "url=" + sosString.parseToString(profile.get("url")) + "\n";
			s2 = s2 + "user=" + sosString.parseToString(profile.get("user")) + "\n";
			try {
				if(savePassword && sosString.parseToString(profile.get("password")).length() > 0) {				
					String pass = String.valueOf(SOSUniqueID.get());

					Options.setProperty("profile.timestamp." + profilename, pass);
					Options.saveProperties();

					if(pass.length() > 8) {
						pass = pass.substring(pass.length()-8);
					}


					String encrypt =  SOSCrypt.encrypt(pass , sosString.parseToString(profile.get("password")));
					s2 = s2 + "password=" +encrypt + "\n";

					profile.put("password", encrypt);

					this.password =encrypt;

					getProfiles().put(profilename, profile);
				}
			} catch(Exception e) {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..could not encrypt.", e);
				throw e;
			}			

			s2 = s2 + "localdirectory=" + sosString.parseToString(profile.get("localdirectory")) + "\n";    		 
			s2 = s2 + "save_password=" + sosString.parseToString(profile.get("save_password")) + "\n";
			s2 = s2 + "protocol=" + sosString.parseToString(profile.get("protocol")) + "\n";
			s2 = s2 + "use_proxy=" + sosString.parseToString(profile.get("use_proxy")) + "\n";
			s2 = s2 + "proxy_server=" + sosString.parseToString(profile.get("proxy_server")) + "\n";
			s2 = s2 + "proxy_port=" + sosString.parseToString(profile.get("proxy_port")) + "\n";			    		     		
			s2 = s2 + "\n\n";	

			s2 = s2 + s.substring(pos2);

			// System.out.println("+++++++++++++++++++++++++++++++++++");			
			// System.out.println(s2);
			// System.out.println("+++++++++++++++++++++++++++++++++++");

			java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.wrap(s2.getBytes());


			java.io.File file = new java.io.File(filename);

			boolean append = false;

			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(file, append).getChannel();

			wChannel.write(bbuf);

			wChannel.close();

			/*} catch (java.io.IOException e) {
			hasError = true;
			MainWindow.message("could not save configurations File: " + configFile + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);
			 */
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not save configurations File: " + configFile , e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			MainWindow.message("could not save configurations File: " + configFile + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);    		 
		} finally {

			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			txtURL.setText(currProfile.getProperty("url"));
		}


	}	

	public void setConnectionsname(Combo cboConnectname_) {
		cboConnectname = cboConnectname_;
	}

	public void setURL(Text txtURL) {
		this.txtURL = txtURL;
	}

	public void removeFromProfilenames(String profileName) {
		ArrayList l = new ArrayList() ;		 
		for(int i = 0; i < profileNames.length ; i++) {
			if(!profileNames[i].equalsIgnoreCase(profileName)) {
				l.add(WebDavDialogListener.PREFIX + profileNames[i]);
			}
		}

		profileNames = convert(l.toArray());
	}

	/*public boolean isLoggedIn() {
		return isLoggedIn;
	}*/


	public boolean mkDirs(String path) {
		try {		

			if(!path.endsWith("/"))
				path = path + "/";

			WebdavResource wr = connect(path);
			if (wr.mkcolMethod()) {
				
				if(logtext != null)  logtext.append("..webdav server reply [mkdir] [path=" + path + "], [status= "  + wr.getStatusMessage() );
			} else {
				throw new Exception("..webdav server reply [mkdir failed] [path=" + path+ "]: "  );				
			}


		} catch(Exception e) {
			hasError = true;
			if(logtext != null)  logtext.append("..could not create Directory [" + path + "] cause:" + e.getMessage() );
		}
		return true;
	}

	//password nicht ini Datei vorhanden. D.h. in Pop Up Fenster nachfragen
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasError() {
		return hasError;
	}

	public String getErrorMessage(Exception ex) {
		Throwable tr = ex.getCause();

		String s = "";

		if(ex.toString() != null)
			s = ex.toString();

		while (tr != null){
			if(s.indexOf(tr.toString()) == -1)
				s = (s.length() > 0 ? s + ", " : "") + tr.toString();
			tr = tr.getCause();
		}
		return s;
	}

	/*public boolean putFile(String target, String url) {
		boolean retVal = false;		
		try {

            WebdavResource wdr = connect(url);                    

            if(wdr.putMethod(new java.io.File(target))) {
            	retVal = true;
            	System.out.println("OK");
            }
            return retVal;

        } catch(Exception e) {
        	try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..webdav server reply: "  , e);
			} catch(Exception ee) {
				//tu nichts
			}
			hasError = true;
			if(logtext != null)  logtext.append("..webdav server reply: " + e.getMessage() );
			return retVal;
        } 

	}
	 */
	/*	public String[] listFiles () {
		String[] retVal = null;
		try
		{


			WebdavResource wdr = connect("");        

			retVal = wdr.list();
			wdr.close();

		} catch(MalformedURLException mue) {
			mue.printStackTrace();
		} catch(HttpException he){
			he.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} 

			if(retVal == null)
				retVal = new String[0]; 

			return retVal;

	}
	 */
}

