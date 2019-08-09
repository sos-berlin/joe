package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import sos.connection.SOSConnection;
import sos.scheduler.editor.conf.listeners.JOEListener;
import sos.util.SOSString;

import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TextDialog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.IOUtils;

public class MainListener extends JOEListener {

    private static final String conPropertyEDITOR_LANGUAGE = "editor.language";
    private IContainer _container = null;
    private final SOSString sosString = new SOSString();
    private SOSConnection sosConnection = null;

    public MainListener(MainWindow gui, IContainer container) {
        _container = container;
    }

    public void showAbout() {
        TextDialog objAboutDialogBox = new TextDialog(MainWindow.getSShell());
        objAboutDialogBox.setText("About JOE - JobScheduler Object Editor");
        String message =
                String.format(
                        "JOE - JobScheduler Object Editor %s\n\nSoftware- und Organisations-Service GmbH\n\ninfo@sos-berlin.com\nwww.sos-berlin.com",
                        VersionInfo.VERSION_STRING);
        objAboutDialogBox.setContent(message, SWT.CENTER);
        objAboutDialogBox.getStyledText().setEditable(false);
        StyleRange bold = new StyleRange();
        bold.start = 0;
        bold.length = message.lastIndexOf("\n");
        bold.fontStyle = SWT.BOLD;
        objAboutDialogBox.setVisibleApplyButton(false);
        objAboutDialogBox.setShowWizzardInfo(false);
        objAboutDialogBox.open(false);
    }

    public String getSVNVersion() {
        String svnVersion = "";
        try {
            Manifest manifest = null;
            String classContainer = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            java.net.URL manifestUrl = new java.net.URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
            if (classContainer.contains(".jar")) {
                manifest = new Manifest(manifestUrl.openStream());
            } else {
                manifest = new Manifest(new java.net.URL(classContainer + "/META-INF/MANIFEST.MF").openStream());
            }
            if (manifest != null) {
                java.util.jar.Attributes atr = manifest.getMainAttributes();
                Iterator it = atr.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    if (key.contains("Implementation-Version")) {
                        String value = atr.getValue(key);
                        svnVersion = svnVersion + key + "=" + value;
                    }
                }
            }
        } catch (Exception e) {
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
            if (Options.getDefault(conPropertyEDITOR_LANGUAGE).equals(val)) {
                defaultItem = item;
            }
            item.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    MenuItem item = (MenuItem) e.widget;
                    if (item.getSelection()) {
                        String lang = (String) item.getData();
                        Options.setLanguage(lang);
                        com.sos.joe.globals.messages.Messages.clearMsgObj();
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    //
                }
            });
        }
        if (!found) {
            String def = Options.getDefault(conPropertyEDITOR_LANGUAGE);
            MainWindow.message("The language " + Options.getLanguage() + " was not found - setting to " + def, SWT.ICON_WARNING | SWT.OK);
            Options.setLanguage(def);
            if (defaultItem != null) {
                defaultItem.setSelection(true);
            }
        }
    }

    public void resetInfoDialog() {
        Options.setShowWizardInfo(true);
    }

    public void loadOptions() {
        String msg = Options.loadOptions(getClass());
        if (msg != null) {
            MainWindow.message("No options file " + Options.getDefaultOptionFilename() + " found - using defaults!\n" + msg, SWT.ICON_ERROR | SWT.OK);
        }
    }

    public void saveOptions() {
        String msg = Options.saveProperties();
        if (msg != null) {
            MainWindow.message("Options cannot be saved!\n" + msg, SWT.ICON_ERROR | SWT.OK);
        }
    }

    public void loadJobTitels() {
        String titleFile = Options.getProperty("title_file");
        String iniFile = Options.getProperty("ini_file");
        try {
            if (sosString.parseToString(titleFile).isEmpty() || sosString.parseToString(iniFile).isEmpty()) {
                return;
            }
            String data = new File(Options.getDefaultOptionFilename()).getParent();
            data = data.endsWith("/") || data.endsWith("\\") ? data : data + "/";
            iniFile = data + iniFile;
            List<Map<String, String>> jobTitleList = new ArrayList<Map<String, String>>();
            try {
                getConnection(iniFile);
                jobTitleList = sosConnection.getArray(titleFile);
            } catch (Exception e) {
                throw new Exception("Could not get the connection to database, cause: " + e.toString());
            }
            String[] titles = new String[jobTitleList.size()];
            for (int i = 0; i < jobTitleList.size(); i++) {
                Map<String, String> hash = jobTitleList.get(i);
                titles[i] = sosString.parseToString(hash, "description");
            }
            Options.setJobTitleList(titles);
        } catch (Exception e) {
            new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
            return;
        }
    }

    public void loadHolidaysTitel() {
        try {
            HashMap holidaysDescription = loadHolidaysDescription("holiday_description_file");
            HashMap holidayFile = loadHolidaysDescription("holiday_file");
            HashMap filenames = new HashMap();
            String data = Options.getSchedulerNormalizedHotFolder();
            Iterator desc = holidaysDescription.keySet().iterator();
            while (desc.hasNext()) {
                String holidayId = desc.next().toString();
                if (!holidayId.startsWith("holiday_id")) {
                    String xml = "<holidays>";
                    holidayId = holidaysDescription.get(holidayId).toString();
                    String filename = data + holidayId + ".holidays.xml";
                    Iterator files = holidayFile.keySet().iterator();
                    while (files.hasNext()) {
                        String date = files.next().toString();
                        if (holidayFile.get(date) != null && holidayFile.get(date).toString().equalsIgnoreCase(holidayId)) {
                            xml = xml + "<holiday date=\"" + date.substring(date.indexOf("_") + 1) + "\"/>";
                        }
                    }
                    xml = xml + "</holidays>";
                    filenames.put("file_" + holidayId, filename);
                    IOUtils.saveXML(xml, filename);
                }
            }
            holidaysDescription.putAll(filenames);
            Options.setHolidaysDescription(holidaysDescription);
        } catch (Exception e) {
            new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
        } finally {
            disconnect();
        }
    }

    public HashMap loadHolidaysDescription(String propertyName) {
        HashMap holidaysDescription = new HashMap();
        String holidayDescriptionFile = Options.getProperty(propertyName);
        String iniFile = Options.getProperty("ini_file");
        try {
            if (sosString.parseToString(holidayDescriptionFile).isEmpty() || sosString.parseToString(iniFile).isEmpty()) {
                return new HashMap();
            }
            String home = new File(Options.getDefaultOptionFilename()).getParent();
            home = home.endsWith("/") || home.endsWith("\\") ? home : home + "/";
            iniFile = home + iniFile;
            List<Map<String, String>> holidayList = new ArrayList<Map<String, String>>();
            try {
                getConnection(iniFile);
                holidayList = sosConnection.getArray(holidayDescriptionFile);
            } catch (Exception e) {
                throw new Exception("Could not get the connection to database, cause: " + e.toString());
            }
            String holidayId = "";
            String field2 = "";
            for (int i = 0; i < holidayList.size(); i++) {
                Map<String, String> hash = holidayList.get(i);
                if (!sosString.parseToString(hash, "holiday_id").isEmpty()) {
                    holidayId = sosString.parseToString(hash, "holiday_id");
                }
                if (!sosString.parseToString(hash, "description").isEmpty()) {
                    field2 = sosString.parseToString(hash, "description");
                    holidaysDescription.put("holiday_id_" + holidayId, field2);
                    holidaysDescription.put(field2, holidayId);
                    holidayId = "";
                    field2 = "";
                }
                if (!sosString.parseToString(hash, "holiday_date").isEmpty()) {
                    field2 = sosString.parseToString(hash, "holiday_date");
                    holidaysDescription.put(holidayId + "_" + field2, holidayId);
                    holidayId = "";
                    field2 = "";
                }
            }
        } catch (Exception e) {
            new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return holidaysDescription;
    }

    private void getConnection(String iniFile) throws Exception {
        try {
            if (sosConnection != null) {
                return;
            }
            sosConnection = SOSConnection.createInstance(iniFile);
            sosConnection.connect();
        } catch (Exception e) {
            new ErrorLog("error while read job descrition " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    public void disconnect() {
        try {
            if (sosConnection != null) {
                sosConnection.disconnect();
            }
        } catch (Exception e) {
            // do nothing
        }
    }

}