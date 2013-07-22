package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import sos.net.SOSFTP;
import sos.net.SOSFTPS;
import sos.net.SOSFileTransfer;
import sos.settings.SOSProfileSettings;
import sos.util.SOSClassUtil;
import sos.util.SOSString;
import sos.util.SOSUniqueID;

//import com.trilead.ssh2.SFTPv3DirectoryEntry;

public class FTPDialogListener {

	private              SOSString              sosString                     = null;

	private              String[]               profileNames                  = null;

	private              HashMap                profiles                      = null;

	private              Properties             currProfile                   = null;

	private              String                 currProfileName               = null;

	private              SOSProfileSettings     settings                      = null;

	private final              String                 prefix                        = "profile ";

	private              File                 configFile                    = null;

	private              SOSFileTransfer        ftpClient                     = null;

	private              boolean                isLoggedIn                    = false;

	private final        int                    ERROR_CODE                    = 300;

	//private              SOSFTP                 sosftp                        = null;

	private              Combo                  cboConnectname                = null;

	private              Text                   logtext                       = null;

	private              String                 workingDirectory              = "";

	private              String                 password                      = "";

	private              boolean                hasError                      = false;

	//sFTP mit publickey und Passphares hat nicht geklappt, Rückfall auf nur Password
	private              boolean                tryAgain                      = false;

	private              Text                   txtPath                       = null;


	public FTPDialogListener(final java.util.Properties profile, final String profilename) {
		sosString = new SOSString();
		currProfile = profile;
		currProfileName = profilename;
		profiles = new HashMap();
		profiles.put(profilename,profile);
	}

	public FTPDialogListener() {

		sosString = new SOSString();
 		try {

			configFile = new File(Options.getSchedulerData(), "factory.ini");

			if(!configFile.exists()) {
				configFile.createNewFile();
			}

			settings = new SOSProfileSettings(configFile.getAbsolutePath());
			ArrayList l = settings.getSections();
			profileNames = convert(settings.getSections().toArray());

			profiles = new HashMap();
			for(int i = 0; i < l.size(); i++) {
				String section = sosString.parseToString(l.get(i));

				if(section.toLowerCase().startsWith(prefix)) {
					String sectionWithoutPrefix = section.substring(prefix.length());
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
				new ErrorLog("error in " + SOSClassUtil.getMethodName() +  "; could not read Profiles from " + configFile, e);
			} catch(Exception ee) {

			}
			hasError = true;
			MainWindow.message("could not read Profiles from " + configFile, SWT.ICON_WARNING);
		}
	}

	private void init(final String profile) {

		currProfileName = profile;
		currProfile = (Properties)profiles.get(profile);



	}

	private String[] convert(final Object[] obj) {
		ArrayList str = new ArrayList();
		String[] retVal = null;
		try {
			for (Object element : obj) {
				if(sosString.parseToString(element).startsWith(prefix)) {
					str.add(sosString.parseToString(element).substring(prefix.length()));
				}
			}
			retVal = new String[str.size()];
			for(int i = 0; i < str.size(); i++) {
				retVal[i] = str.get(i).toString();
			}

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {

			}
			//System.out.println("..error in FTPDilagProfiles: " + e.getMessage());
			hasError = true;
		}
		return retVal;
	}

	public String[] getProfileNames() {
		try {
			profileNames = convert(settings.getSections().toArray());
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
			}
			//System.out.println(e.getMessage());
		}
		return profileNames;
	}

	public void setProfileNames(final String[] profileNames) {
		this.profileNames = profileNames;
	}

	public HashMap getProfiles() {
		return profiles;
	}

	public void setProfiles(final String key, final Properties profile) {
		try {
			settings.getSections().add(prefix + key);
			profiles.put(key, profile);
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {

			}
			System.out.println("error in FTPDialoListener: " + e.getMessage());
		}
	}

	public Properties getCurrProfile() {
		return currProfile;
	}

	public void setCurrProfile(final Properties currProfile) {
		this.currProfile = currProfile;
	}

	public String getCurrProfileName() {
		return currProfileName;
	}

	public void setCurrProfileName(final String currProfileName) {
		this.currProfileName = currProfileName;
		init(currProfileName);
	}


	public void removeProfile(final String profilename) {
		try {
			deleteProfile(profilename);
			getProfiles().remove(profilename);
			removeFromProfilenames(profilename);
			settings.getSections().remove(prefix +profilename);
			currProfile = new Properties();
			currProfileName = "";
		} catch(Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + "could not remove Profile: " + profilename, e);
			} catch(Exception ee) {

			}
			MainWindow.message("could not remove Profile: " + profilename + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);
		}

	}



	public HashMap changeDirectory(final String profile, final String directory) {

		try {
			if(isLoggedIn && !currProfileName.equals(profile))
				disconnect();

			if(!isLoggedIn)
				connect(profile);

			if(!isLoggedIn)//connect war nicht erfolgreich
				return new HashMap();

			init(profile);

			//ftpClient.changeWorkingDirectory(directory));
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not change Directory [" + directory + "] ", e);
			} catch(Exception ee) {

			}
			hasError = true;
			MainWindow.message("could not change Directory [" + directory + "] cause:" + e.getMessage() ,  SWT.ICON_WARNING);
		}

		return changeDirectory(directory);


	}


	public String getFile(String filename, final String subFolder) {
		String targetfile = null;
		boolean deleteTmpFile = false;//wenn locahdirectory nicht angegeben ist, dann temp Verzeichnis bilden und diese anschliessend löschen
		try {

			targetfile = sosString.parseToString(currProfile.get("localdirectory" ));
			if(targetfile.length() == 0){
				targetfile = System.getProperty("java.io.tmpdir");
				deleteTmpFile = true;
			}

			targetfile = targetfile.replaceAll("\\\\", "/");
			if(subFolder != null)
				targetfile = targetfile.endsWith("/") ||  targetfile.endsWith("\\") ? targetfile + subFolder:  targetfile + "/" + subFolder;

			File file = new File(targetfile);
			if(!file.exists()) {
				file.mkdirs();
			}

			if(deleteTmpFile)
				file.deleteOnExit();

			targetfile = (targetfile.endsWith("/") ||  targetfile.endsWith("\\") ? targetfile :  targetfile + "/")+ new java.io.File(filename).getName();

			if(ftpClient instanceof SOSFTP) {
				if(filename.startsWith("./")) {
					filename = "./" + filename.substring(workingDirectory.length());
				}
			}

			long l = ftpClient.getFile(filename, targetfile, false);




			if (l == -1)
				throw new Exception (" could not get file [filename=" + filename+"], [target=" + targetfile+"], cause " + ftpClient.getReplyString());

			if(logtext != null)  logtext.append("..ftp server reply [getfile] [size= " + l + "] :"  + ftpClient.getReplyString() );

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ;could not get File [" + filename + "]", e);
			} catch(Exception ee) {

			}
			if(logtext != null)  logtext.append("could not get File [" + filename + "] :" + e.getMessage() );
			hasError = true;
		}
		return targetfile;
	}

	public HashMap cdUP() {
		return changeDirectory("..");
	}

	public void removeFile(final String file) {
		try   {
			if(ftpClient != null && ftpClient.delete(file)) {
				if(logtext != null)  logtext.append("..ftp server reply [delete] [file=" + file+"]: "  + ftpClient.getReplyString() );
			} else {
				if(logtext != null)  logtext.append("..ftp server reply [could not delete] [file=" + file+"]: "  + ftpClient.getReplyString() );
				hasError = true;
			}
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not delete file [" + file+ "]", e);
			} catch(Exception ee) {

			}
			if(logtext != null)
				logtext.append("could not delete file [" + file+ "] cause:" + e.getMessage() );
			hasError = true;
		}
	}

	public HashMap changeDirectory(String directory) {

		HashMap listnames = new HashMap();
		//String curWD = workingDirectory; //hilsvariable. Um im Fehlerfall wird dieser zurückgesetzt

		try {
			directory = directory.replaceAll("\\\\", "/");

			if(directory == null || directory.length() == 0)
				directory = sosString.parseToString(currProfile.get("root")).length() > 0 ? sosString.parseToString(currProfile.get("root")) : ".";

			if(!(directory.startsWith(".") || directory.startsWith("/")))
				directory = "./"+ directory;

			if(ftpClient instanceof SOSFTP) {

				if(directory.startsWith("./")) {

					if(workingDirectory.length() >= 0 && workingDirectory.equalsIgnoreCase(directory)) {
						directory = ".";
					}else if(workingDirectory.length() >= directory.length() && workingDirectory.startsWith(directory)) {

						String curWorkingDirectory = workingDirectory.substring(directory.length());
						curWorkingDirectory = curWorkingDirectory.endsWith("/") ? curWorkingDirectory.substring(0, curWorkingDirectory.length()) : curWorkingDirectory;
						int countOfcdUP = curWorkingDirectory.split("/").length;
						for(int i = 0; i < countOfcdUP; i++)
							cdUP();

					} else if (directory.startsWith(workingDirectory) || workingDirectory.length()==0 ){

						String curWorkingDirectory = directory;
						directory =  "./" + directory.substring(workingDirectory.length());
						workingDirectory = curWorkingDirectory;

					} else {

						String curWorkingDirectory = workingDirectory;
						int countOfcdUP = curWorkingDirectory.split("/").length;
						if(curWorkingDirectory.endsWith("/") ||
								curWorkingDirectory.startsWith("./")||
								curWorkingDirectory.startsWith("/"));
						countOfcdUP--;
						for(int i = 0; i < countOfcdUP; i++) {
							cdUP();
						}

						workingDirectory = directory;

					}
				} else if(directory.equals("..")) {


					int pos = workingDirectory.endsWith("/") ? workingDirectory.lastIndexOf("/", 1) : workingDirectory.lastIndexOf("/");
					if(pos == -1)
						pos = directory.length();
					if(!workingDirectory.equals("") && workingDirectory.length() >= pos)
						workingDirectory = workingDirectory.substring(0, pos);
					else
						workingDirectory = "";

				} else {
					workingDirectory = "";

				}
				hasError = false;
			}

			if(ftpClient.changeWorkingDirectory(directory)) {

				if(logtext != null)  logtext.append("..ftp server reply [cd] [directory ftp_file_path=" + directory +"]: "  + ftpClient.getReplyString() );



				java.util.Vector onlyfiles= ftpClient.nList(".");
				String[] filesAndDirs = ftpClient.listNames(".");

				//String[] filesAndDirs = ftpClient.listNames("../..");

				for (String filesAndDir : filesAndDirs) {
					String fd = filesAndDir;
					if(onlyfiles.contains(fd)){
						listnames.put(fd, "file");
						listnames.put(fd + "_size", String.valueOf(ftpClient.size(fd)));

						//test
						//com.trilead.ssh2.SFTPv3Client s = new com.trilead.ssh2.SFTPv3Client()):
						//FTPFile[] listFiles  = ftpClient.listFiles( directory );

						//Vector files = ftpClient.ls(workingDirectory);
						//java.util.Vector v = ftpClient.nList("/home/test/temp/test/folder1");
						//SFTPv3DirectoryEntry entry = (SFTPv3DirectoryEntry)(v.get(0));

						//Integer cc = entry.attributes.permissions;

					} else {
						if(!fd.equals(".") && !fd.equals(".."))
							listnames.put(fd, "dir");
					}
				}
				hasError = false;
			} else {
				throw new Exception("..ftp server reply [cd] [directory ftp_file_path=" + directory + "]: "  + ftpClient.getReplyString());
			}
		} catch (Exception e) {

			MainWindow.message("could not change Directory [" + directory + "] cause:" + e.getMessage(), SWT.ICON_WARNING);

			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not change Directory [" + directory + "]", e);
			} catch(Exception ee) {

			}
			hasError = true;
			if(logtext != null)  logtext.append("could not change Directory [" + directory + "] cause:" + e.getMessage() );
		}
		return listnames;
	}

	public void disconnect() {
		if(isLoggedIn && ftpClient != null) {
			try {
				workingDirectory = "";
				if(ftpClient.isConnected())
					ftpClient.disconnect();
			} catch(Exception e) {

				try {
					new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not disconnect FTP Server cause.", e);
				} catch(Exception ee) {

				}

				if(logtext != null) {
					logtext.append("could not disconnect FTP Server cause: " + e.getMessage());
				}


			}
		}
		isLoggedIn = false;
	}

	public boolean isConnect() {

		if(isLoggedIn && ftpClient != null)
			return true;
		else
			return false;
	}

	public void connect(final String profile) {


		hasError = false;
		workingDirectory = "";
		setPassword("");

		if(profile == null || profile.length() == 0 )
			return;

		if(isLoggedIn && ftpClient != null) {
			if(logtext != null) if(logtext != null)  logtext.append("..ftp is connected");
			return;
		}

		SOSFTP                 sosftp                        = null;

		Properties prop = (Properties)getProfiles().get(profile);
		String protocol = "ftp";
		String host = "";
		int    port = 22;
		isLoggedIn = false;
		String user = "";
		String password = "";

		/** The FTP server will always reply the ftp error codes,
		 * see http://www.the-eggman.com/seminars/ftp_error_codes.html */

		String account= "";

		String authenticationFilename = "";
		String authenticationMethod = "publickey";
		String proxyHost = "";
		String proxyPassword = "";
		String proxyUser= "";
		//boolean      passiveMode                     = false;
		String       transferMode                    = "binary";

		int proxyPort = 21;
		try {

			protocol               = sosString.parseToString(prop.get("protocol")).length() > 0 ?   sosString.parseToString(prop.get("protocol")) : "ftp";
			host                   = sosString.parseToString(prop.get("host"));
			user                   = sosString.parseToString(prop.get("user"));
			authenticationMethod   = sosString.parseToString(prop.get("auth_method")).length() > 0 ?   sosString.parseToString(prop.get("auth_method")) : "publickey";
			String sPort           = sosString.parseToString(prop.get("port"));
			if(sPort.length() > 0)
				port = Integer.parseInt(sosString.parseToString(prop.get("port")));

			if(authenticationMethod.length() > 0 && authenticationMethod.equals("both"))
				authenticationMethod = "publickey";


			authenticationFilename = sosString.parseToString(prop.get("auth_file")).length() > 0 ?   sosString.parseToString(prop.get("auth_file")) : "";

			if (host == null || host.length() == 0) throw new Exception("no host was specified");
			if (user == null || user.length() == 0) throw new Exception("no user was specified");



			password      = sosString.parseToString(prop.get("password"));

			try {

				String key = Options.getProperty("profile.timestamp." + profile);

				//Options.setProperty("profile.timestamp." + profilename, pass);
				//Options.saveProperties();

				if(key != null && key.length() > 8) {
					key = key.substring(key.length()-8);
				}

				if(password.length() > 0 && sosString.parseToString(key).length() > 0) {

					password = SOSCrypt.decrypt(key, password);

				}
			} catch(Exception e) {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; ..could not encrypt.", e);
				throw e;
			}

			if(tryAgain) {
				authenticationMethod = "password";
				authenticationFilename = "";
			}


			if(password.length() == 0 && !protocol.equalsIgnoreCase("sftp") ||
					tryAgain && sosString.parseToString(password).length() == 0) {
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



			if(prop.get("transfermode") != null)
				transferMode  = sosString.parseToString(prop.get("transfermode"));

			if (protocol.equalsIgnoreCase("ftp")){
				//SOSFTP sosftp = new SOSFTP(host, port);
				sosftp = new SOSFTP(host);

				ftpClient = sosftp;
				if(logtext != null)  logtext.append("..ftp server reply [init] [host=" + host + "], [port="+ port + "]: " + ftpClient.getReplyString() );


				if (account != null && account.length() > 0) {
					isLoggedIn = sosftp.login(user, password, account);
					if(logtext != null)  logtext.append("..ftp server reply [login] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString() );
				} else {

					isLoggedIn = sosftp.login(user, password);
					if(logtext != null)  logtext.append("..ftp server reply [login] [user=" + user + "]: " + ftpClient.getReplyString());
				}

				if (!isLoggedIn || sosftp.getReplyCode() > ERROR_CODE) {
					throw new Exception("..ftp server reply [login failed] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString() );
				}

			} else if (protocol.equalsIgnoreCase("sftp")) {
				try {
					Class sftpClass;
					try{
						//sftpClass = Class.forName("sos.stacks.ganymed.SOSSFTP");
						sftpClass = Class.forName("sos.net.SOSSFTP");
						Constructor con = sftpClass.getConstructor(new Class[]{String.class,int.class});
						ftpClient = (SOSFileTransfer) con.newInstance(new Object[]{host, new Integer(port)});
					} catch (Exception e){
						//if(logtext != null)  logtext.append("Failed to initialize SOSSFTP class, need recent sos.stacks.jar and trilead jar. "+e);
						//throw new Exception("Failed to initialize SOSSFTP class, need recent sos.stacks.jar and trilead jar. "+e,e);
						if(logtext != null)  logtext.append("Failed to initialize SOSSFTP class, need recent sos.net.jar and trilead jar. "+e);
						throw new Exception("Failed to initialize SOSSFTP class, need recent sos.net.jar and trilead jar. "+e,e);

					}
					Class[] stringParam = new Class[]{String.class};
					Method method= sftpClass.getMethod("setAuthenticationFilename", stringParam);
					method.invoke(ftpClient, new Object[]{authenticationFilename});
					method= sftpClass.getMethod("setAuthenticationMethod", stringParam);
					method.invoke(ftpClient, new Object[]{authenticationMethod});
					method= sftpClass.getMethod("setPassword", stringParam);
					method.invoke(ftpClient, new Object[]{password});
					method= sftpClass.getMethod("setProxyHost", stringParam);
					method.invoke(ftpClient, new Object[]{proxyHost});
					method= sftpClass.getMethod("setProxyPassword", stringParam);
					method.invoke(ftpClient, new Object[]{proxyPassword});
					method= sftpClass.getMethod("setProxyPort", new Class[]{int.class});
					method.invoke(ftpClient, new Object[]{new Integer(proxyPort)});
					method= sftpClass.getMethod("setProxyUser", stringParam);
					method.invoke(ftpClient, new Object[]{proxyUser});
					method= sftpClass.getMethod("setUser", stringParam);
					method.invoke(ftpClient, new Object[]{user});

					method= sftpClass.getMethod("connect", new Class[]{});
					method.invoke(ftpClient, new Object[]{});
					isLoggedIn = true;
					//try{
					if(logtext != null)  logtext.append("..sftp server logged in [user=" + user + "], [host=" + host + "]" );

					//}catch (Exception e){
					//	throw new Exception("..sftp server login failed [user=" + user + "], [host=" + host + "]: " + e );
					//}
					//System.out.println("sftp hat geklappt?" + ftpClient.isConnected());
				} catch(Exception e1){
					//System.out.println("sftp hat nicht geklappt?, weil: " + e1.toString());
					if(sosString.parseToString(prop.get("auth_method")).equalsIgnoreCase("both") && tryAgain == false)
						tryAgain = true;
					else
						tryAgain = false;
					throw new Exception("..sftp server login failed [user=" + user + "], [host=" + host + "]: " + e1, e1 );

				}

			} else if (protocol.equalsIgnoreCase("ftps")){

				try{

					if ( proxyHost != null && proxyPort != 0) {
						System.getProperties().setProperty("proxyHost", proxyHost);
						System.getProperties().setProperty("proxyPort", String.valueOf(proxyPort) );
						System.getProperties().setProperty("proxySet", "true");
					}


					SOSFTPS sosftps = new SOSFTPS(host, port);
					ftpClient = sosftps;
					if(logtext != null)  logtext.append("..ftp server reply [init] [host=" + host + "], [port="+ port + "]: " + ftpClient.getReplyString() );
					isLoggedIn = sosftps.login(user, password);
					if(logtext != null)  logtext.append("..ftp server reply [login] [user=" + user + "]: " + ftpClient.getReplyString());

					if (!isLoggedIn || sosftps.getReplyCode() > ERROR_CODE) {
						throw new Exception("..ftp server reply [login failed] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString() );
					}
					isLoggedIn = true;
				}catch (Exception e){
					throw new Exception("..ftps server login failed [user=" + user + "], [host=" + host + "]: " + e );
				}

			} else{
				throw new Exception("Unknown protocol: "+protocol);
			}

			if (ftpClient instanceof SOSFTP){
				sosftp = (SOSFTP) ftpClient;
				/*if (passiveMode) {
					sosftp.passive();
					if (sosftp.getReplyCode() > ERROR_CODE) {
						throw new Exception("..ftp server reply [passive]: "  + ftpClient.getReplyString());
					} else {
						this.getLogger().debug("..ftp server reply [passive]: "  + ftpClient.getReplyString());
					}
				}	*/


				if (transferMode.equalsIgnoreCase("ascii")) {
					if (sosftp.ascii()) {
						if(logtext != null)  logtext.append("..using ASCII mode for file transfer");
						if(logtext != null)  logtext.append("..ftp server reply [ascii]: "  + ftpClient.getReplyString());
					} else {
						throw new Exception(".. could not switch to ASCII mode for file transfer ..ftp server reply [ascii]: "  + ftpClient.getReplyString());
					}
				} else {
					if (sosftp.binary()) {
						if(logtext != null)  logtext.append("using binary mode for file transfers.");
						if(logtext != null)  logtext.append("..ftp server reply [binary]: "  + ftpClient.getReplyString());
					} else {
						throw new Exception(".. could not switch to binary mode for file transfer ..ftp server reply [ascii]: "  + ftpClient.getReplyString());
					}
				}
			}

			Options.setProperty("last_profile" , profile);
			Options.saveProperties();


		} catch (Exception ex) {
			isLoggedIn = false;
			hasError = true;

			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; error in ftp server init with [host=" + host + "], [port="+ port + "].", ex);
			} catch(Exception ee) {

			}

			if(logtext != null)  logtext.append("..error in ftp server init with [host=" + host + "], [port="+ port + "], cause: " + getErrorMessage(ex) + "\n");
			if(tryAgain)  {
				if(logtext != null)  logtext.append("..try connect with Authentication Method=password." + "\n");
				connect(profile);
				tryAgain = false;
			}
		}
	}

	public void setLogText(final org.eclipse.swt.widgets.Text text) {
		logtext = text;
	}

	public void saveAs(final String source, String target) {
		try {

			if(ftpClient instanceof SOSFTP)//change to Parent directory
				changeDirectory("./");

			target = target.replaceAll("\\\\", "/");

			long bytesSend = ftpClient.putFile(source, target);

			if(logtext != null)  logtext.append(source + " transfer to " + target + "[bytes=" + bytesSend + "]");
		} catch(Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; ..ftp server reply: "  + ftpClient.getReplyString(), e);
			} catch(Exception ee) {

			}
			if(logtext != null)  logtext.append("..ftp server reply: "  + ftpClient.getReplyString());
			hasError = true;
		}
	}

	public void saveHotFolderAs(String source, String target, final ArrayList listOfHotFolderElements, final HashMap changes) {
		try {
			Vector listOfExistFiles = new Vector(); //überprüft ob das HotFolderElement existiert. das kann passieren, wenn ein Element neu angelegt wird und ohne zwischespeichern der Name verändert wird.
			if(ftpClient != null) {
				listOfExistFiles = ftpClient.nList(target);
			}

			ArrayList listOfRemovedFiles = new ArrayList(); // hilfsvariable: verhindert das zweimal löschen der Hot Folder Element

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


					if(changes.containsKey(hotElementname + "_" + attrname) && changes.get(hotElementname + "_" + attrname).equals("delete")) {
						if(listOfExistFiles.contains(filename)){
							removeFile(target + filename);
							listOfRemovedFiles.add(target + filename);
						}

					}
				}
			}


			if(changes != null && changes.keySet() != null) {
				java.util.Iterator c = changes.keySet().iterator();
				while(c.hasNext()) {
					String remFile = c.next().toString();
					if(changes.get(remFile) != null && changes.get(remFile).equals("delete")) {
						String prefix = "";
						if(remFile.startsWith("job_chain"))
							prefix = "job_chain";
						else if(remFile.startsWith("job"))
							prefix = "job";
						else if(remFile.startsWith("order"))
							prefix = "order";
						else if(remFile.startsWith("add_order"))
							prefix = "add_order";
						else if(remFile.startsWith("process_class") )
							prefix = "process_class";
						else if(remFile.startsWith("lock") )
							prefix = "lock";
						else if(remFile.startsWith("schedule") )
							prefix = "schedule";

						remFile = remFile.substring(prefix.length() + 1) + "." + remFile.substring(0, prefix.length()) + ".xml";
						if(!listOfRemovedFiles.contains(target + remFile)) {
							if(listOfExistFiles.contains(remFile)){
								removeFile(target + remFile);
							}
						}
					}
				}
			}
			File fSource = new File(source);
			if(!fSource.exists())
				throw new Exception (source + " not exist." );

			if(!fSource.isDirectory())
				throw new Exception (source + " is not a directory." );

			String[] files =  fSource.list();
			for (String file : files) {
				String sourcefile = source + file;
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
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; ..ftp server reply: "  + ftpClient.getReplyString(), e);
			} catch(Exception ee) {

			}
			hasError = true;
			if(logtext != null)  logtext.append("..ftp server reply: "  + ftpClient.getReplyString());
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

			if(ftpClient instanceof SOSFTP)
				changeDirectory("./");//change to Parent Directory

			String[] files =  fSource.list();
			for (String file : files) {
				String sourcefile = source + file;
				//String targetFile = new File(sourcefile).getName();
				String targetFile = target + new File(sourcefile).getName();
				//System.out.println("source: " + sourcefile + ", target: " + targetFile);
				saveAs(sourcefile, targetFile);
				//list.add(target +targetFile);
				list.add(targetFile);
			}


		} catch(Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; ..ftp server reply: "  + ftpClient.getReplyString(), e);
			} catch(Exception ee) {

			}
			hasError = true;
			if(logtext != null)  logtext.append("..ftp server reply: "  + ftpClient.getReplyString());
		}
		return list;
	}
	public static byte[] getBytesFromFile(final File file) throws IOException {

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
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {

			}
		}
		return bytes;
	}

	public void deleteProfile(final String profilename) throws Exception {
		try {
			setCurrProfileName(profilename);
			//java.util.Properties profile = getCurrProfile();



			byte[] b = getBytesFromFile(configFile);
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


			boolean append = false;

			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(configFile, append).getChannel();

			wChannel.write(bbuf);

			wChannel.close();

		} catch (java.io.IOException e) {

			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not delete profile=" + profilename, e);
			} catch(Exception ee) {

			}
			hasError = true;
			throw new Exception (e.getMessage());

		} finally {

			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			txtPath.setText(currProfile.getProperty("root"));

		}


	}

	public void saveProfile(final boolean savePassword) {
		try {

			java.util.Properties profile = getCurrProfile();

			String profilename = currProfileName;

			byte[] b = getBytesFromFile(configFile);
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

			//s2 = s2 + "\n";
			s2 = s2 + "[profile " + profilename + "]\n\n";
			s2 = s2 + "host=" + sosString.parseToString(profile.get("host")) + "\n";
			s2 = s2 + "port=" + sosString.parseToString(profile.get("port")) + "\n";
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

					password =encrypt;

					getProfiles().put(profilename, profile);
				}
			} catch(Exception e) {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; ..could not encrypt.", e);
				throw e;
			}
			s2 = s2 + "root=" + sosString.parseToString(profile.get("root")) + "\n";
			s2 = s2 + "localdirectory=" + sosString.parseToString(profile.get("localdirectory")) + "\n";
			s2 = s2 + "transfermode=" + sosString.parseToString(profile.get("transfermode")) + "\n";
			s2 = s2 + "save_password=" + sosString.parseToString(profile.get("save_password")) + "\n";
			s2 = s2 + "protocol=" + sosString.parseToString(profile.get("protocol")) + "\n";
			s2 = s2 + "use_proxy=" + sosString.parseToString(profile.get("use_proxy")) + "\n";
			s2 = s2 + "proxy_server=" + sosString.parseToString(profile.get("proxy_server")) + "\n";
			s2 = s2 + "proxy_port=" + sosString.parseToString(profile.get("proxy_port")) + "\n";
			//auth_method=publickey oder password oder both
			s2 = s2 + "auth_method=" + sosString.parseToString(profile.get("auth_method")) + "\n";
			s2 = s2 + "auth_file=" + sosString.parseToString(profile.get("auth_file")) + "\n";
			s2 = s2 + "\n\n";

			s2 = s2 + s.substring(pos2);

			// System.out.println("+++++++++++++++++++++++++++++++++++");
			// System.out.println(s2);
			// System.out.println("+++++++++++++++++++++++++++++++++++");

			java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.wrap(s2.getBytes());



			boolean append = false;

			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(configFile, append).getChannel();

			wChannel.write(bbuf);

			wChannel.close();

			/*} catch (java.io.IOException e) {
			hasError = true;
			MainWindow.message("could not save configurations File: " + configFile + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);
			 */
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not save configurations File: " + configFile , e);
			} catch(Exception ee) {

			}
			hasError = true;
			MainWindow.message("could not save configurations File: " + configFile + ": cause:\n" + e.getMessage(), SWT.ICON_WARNING);
		} finally {

			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			txtPath.setText(currProfile.getProperty("root"));
		}


	}

	public void setConnectionsname(final Combo cboConnectname_) {
		cboConnectname = cboConnectname_;
	}

	public void setRemoteDirectory(final Text txtPath) {
		this.txtPath = txtPath;
	}


	public void removeFromProfilenames(final String profileName) {
		ArrayList l = new ArrayList() ;
		for(int i = 0; i < profileNames.length ; i++) {
			if(!profileNames[i].equalsIgnoreCase(profileName)) {
				l.add(prefix + profileNames[i]);
			}
		}

		profileNames = convert(l.toArray());
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}


	public boolean mkDirs(final String path) {
		try {

			if(ftpClient.mkdir(path)) {
				if(logtext != null)  logtext.append("..ftp server reply [mkdir] [path=" + path + "]: " + ftpClient.getReplyString() );
			} else {
				throw new Exception("..ftp server reply [mkdir failed] [path=" + path+ "]: " + ftpClient.getReplyString() );
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

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean hasError() {
		return hasError;
	}

	public String getErrorMessage(final Exception ex) {
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

	public void refresh() {

		if(cboConnectname != null && cboConnectname.getText().length() > 0 && currProfile != null ) {
			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			txtPath.setText(currProfile.getProperty("root"));
		}
	}

}

