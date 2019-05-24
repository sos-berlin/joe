package sos.ftp.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;

import sos.settings.SOSProfileSettings;
import sos.util.SOSLogger;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class FTPDialogListener {

	private String[] profileNames = null;
	private HashMap<String, FTPProfile> profiles = null;
	private FTPProfile currProfile = null;
	private String currProfileName = null;
	private SOSProfileSettings settings = null;
	private String prefix = "profile ";
	private String configFile = "";
	private Combo cboConnectname = null;
	private String password = "";
	private boolean hasError = false;
	private Text txtPath = null;

	public FTPDialogListener(FTPProfile profile, String profilename) {
		try {
			FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
			currProfile = profile;
			currProfileName = profilename;
			profiles = new HashMap<String, FTPProfile>();
			profiles.put(profilename, profile);
		} catch (Exception e) {
			FTPProfile.log("error in FTPDialogListener,  cause: " + e.toString(), 1);
		}
	}

	public FTPDialogListener(File configfile) throws Exception {
		FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
		try {
			configFile = configfile.getCanonicalPath();
			if (!new File(configFile).exists()) {
				new File(configFile).createNewFile();
			}
			settings = new SOSProfileSettings(configFile);
			ArrayList<String> l = settings.getSections();
			profileNames = convert(settings.getSections());
			profiles = new HashMap<String, FTPProfile>();
			for (int i = 0; i < l.size(); i++) {
				String section = l.get(i);
				if (section.toLowerCase().startsWith(prefix)) {
					String sectionWithoutPrefix = section.substring(prefix.length());
					Properties prop = settings.getSection(section);
					prop.setProperty("profilename", sectionWithoutPrefix);
					profiles.put(sectionWithoutPrefix, new FTPProfile(prop));
					if (currProfileName == null) {
						init(sectionWithoutPrefix);
					}
				}
			}
		} catch (Exception e) {
			hasError = true;
			FTPProfile.log("error in " + sos.util.SOSClassUtil.getMethodName() + "could not read Profiles from "
					+ configFile + ", cause: " + e.toString(), SOSLogger.WARN);
			FTPProfileDialog.message("could not read Profiles from " + configFile, SWT.ICON_WARNING);
		}
	}

	private void init(String profile) {
		currProfileName = profile;
		currProfile = (FTPProfile) profiles.get(profile);
	}

	private String[] convert(ArrayList<String> obj) throws Exception {
		FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
		ArrayList<String> str = new ArrayList<String>();
		String[] retVal = null;
		try {
			for (int i = 0; i < obj.size(); i++) {
				if (obj.get(i).startsWith(prefix)) {
					str.add(obj.get(i).substring(prefix.length()));
				}
			}
			retVal = new String[str.size()];
			for (int i = 0; i < str.size(); i++) {
				retVal[i] = str.get(i).toString();
			}
		} catch (Exception e) {
			FTPProfile.log("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString(),
					SOSLogger.WARN);
			hasError = true;
		}
		return retVal;
	}

	public String[] getProfileNames() {
		try {
			profileNames = convert(settings.getSections());
		} catch (Exception e) {
		}
		return profileNames;
	}

	public HashMap<String, FTPProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(String key, FTPProfile profile) {
		try {
			settings.getSections().add(prefix + key);
			this.profiles.put(key, profile);
		} catch (Exception e) {
			FTPProfileDialog.message("error in FTPDialoListener: " + e.getMessage(), 1);
		}
	}

	public FTPProfile getCurrProfile() {
		return currProfile;
	}

	public void setCurrProfile(FTPProfile currProfile) {
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
			settings.getSections().remove(prefix + profilename);
			currProfile = new FTPProfile(new Properties());
			currProfileName = "";
		} catch (Exception e) {
			FTPProfileDialog.message("could not remove Profile: " + profilename + ": cause:\n" + e.getMessage(),
					SWT.ICON_WARNING);
		}

	}

	public static byte[] getBytesFromFile(File file) throws IOException, Exception {
		FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
		byte[] bytes = null;
		try {
			InputStream is = new FileInputStream(file);
			long length = file.length();
			bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				is.close();
				throw new IOException("Could not completely read file " + file.getName());
			}
			is.close();
		} catch (Exception e) {
			FTPProfile.log("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString(), 1);
		}
		return bytes;
	}

	private void deleteProfile(String profilename) throws Exception {
		FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
		try {
			setCurrProfileName(profilename);
			String filename = configFile;
			byte[] b = getBytesFromFile(new File(filename));
			String s = new String(b);
			int pos1 = s.indexOf("[profile " + profilename + "]");
			int pos2 = s.indexOf("[", pos1 + 1);
			if (pos1 == -1) {
				pos1 = s.length();
				pos2 = -1;
				return;
			}
			if (pos2 == -1) {
				pos2 = s.length();
			}
			String s2 = s.substring(0, pos1) + s.substring(pos2);
			java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.wrap(s2.getBytes());
			java.io.File file = new java.io.File(filename);
			boolean append = false;
			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(file, append).getChannel();
			wChannel.write(bbuf);
			wChannel.close();
		} catch (java.io.IOException e) {
			hasError = true;
			FTPProfile.log("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString(), 1);
		}
	}

	public void saveProfile() throws Exception {
		try {
			FTPProfile profile = getCurrProfile();
			String filename = configFile;
			String profilename = currProfileName;
			byte[] b = getBytesFromFile(new File(filename));
			String s = new String(b);
			int pos1 = s.indexOf("[profile " + profilename + "]");
			int pos2 = s.indexOf("[", pos1 + 1);
			if (pos1 == -1) {
				pos1 = s.length();
				pos2 = -1;
			}
			if (pos2 == -1) {
				pos2 = s.length();
			}
			String s2 = s.substring(0, pos1);
			s2 = s2 + "[profile " + profilename + "]\n\n";
			s2 = s2 + "host=" + profile.getHost() + "\n";
			s2 = s2 + "port=" + profile.getPort() + "\n";
			s2 = s2 + "user=" + profile.getUser() + "\n";
			try {
				if (!profile.getPassword().isEmpty()) {
					String encrypt = "";
					if (profile.getPassword().endsWith("=")) {
						encrypt = profile.getPassword();
					} else {
						String pass = profilename;
						encrypt = SOSProfileCrypt.encrypt(pass, profile.getPassword());
					}
					s2 = s2 + "password=" + encrypt + "\n";
					profile.setPassword(encrypt);
					this.password = encrypt;
					getProfiles().put(profilename, profile);
				}
			} catch (Exception e) {
				throw e;
			}
			s2 = s2 + "root=" + profile.getRoot() + "\n";
			s2 = s2 + "localdirectory=" + profile.getLocaldirectory() + "\n";
			s2 = s2 + "transfermode=" + profile.getTransfermode() + "\n";
			s2 = s2 + "save_password=" + profile.isSavePassword() + "\n";
			s2 = s2 + "protocol=" + profile.getProtocol() + "\n";
			s2 = s2 + "passivemode=" + profile.getPassiveMode() + "\n";
			s2 = s2 + "use_proxy=" + profile.getUseProxy() + "\n";
			s2 = s2 + "proxy_server=" + profile.getProxyServer() + "\n";
			s2 = s2 + "proxy_port=" + profile.getProxyPort() + "\n";
			s2 = s2 + "proxy_user=" + profile.getProxyUser() + "\n";
			s2 = s2 + "proxy_password=" + profile.getProxyPassword() + "\n";
			s2 = s2 + "proxy_protocol=" + profile.getProxyProtocol() + "\n";
			s2 = s2 + "auth_method=" + profile.getAuthMethod() + "\n";
			s2 = s2 + "auth_file=" + profile.getAuthFile() + "\n";
            s2 = s2 + "use_key_agent=" + profile.getUseKeyAgent() + "\n";

			s2 = s2 + "\n\n";
			s2 = s2 + s.substring(pos2);
			java.nio.ByteBuffer bbuf = java.nio.ByteBuffer.wrap(s2.getBytes());
			java.io.File file = new java.io.File(filename);
			boolean append = false;
			java.nio.channels.FileChannel wChannel = new java.io.FileOutputStream(file, append).getChannel();
			wChannel.write(bbuf);
			wChannel.close();
		} catch (Exception e) {
			hasError = true;
			FTPProfile.log("error in " + sos.util.SOSClassUtil.getMethodName() + "could not save configurations File: "
					+ configFile + ", cause: " + e.toString(), SOSLogger.WARN);
			FTPProfileDialog.message(
					"could not save configurations File: " + configFile + ": cause:\n" + e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

	public void setConnectionsname(Combo cboConnectname_) {
		cboConnectname = cboConnectname_;
	}

	public void setRemoteDirectory(Text txtPath) {
		this.txtPath = txtPath;
	}

	private void removeFromProfilenames(String profileName) throws Exception {
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < profileNames.length; i++) {
			if (!profileNames[i].equalsIgnoreCase(profileName)) {
				l.add(prefix + profileNames[i]);
			}
		}
		profileNames = convert(l);
	}

	public String getPassword() {
		return password;
	}

	public boolean hasError() {
		return hasError;
	}

	public static String getErrorMessage(Exception ex) {
		Throwable tr = ex.getCause();
		String s = "";
		if (ex.toString() != null) {
			s = ex.toString();
		}
		while (tr != null) {
			if (s.indexOf(tr.toString()) == -1) {
				s = (s.length() > 0 ? s + ", " : "") + tr.toString();
			}
			tr = tr.getCause();
		}
		return s;
	}

	public void refresh() {
		if (cboConnectname != null && cboConnectname.getText().length() > 0 && currProfile != null) {
			cboConnectname.setItems(getProfileNames());
			cboConnectname.setText(currProfileName);
			if (txtPath != null && currProfile != null && currProfile.getRoot() != null) {
				txtPath.setText(currProfile.getRoot());
			}
		}
	}

}