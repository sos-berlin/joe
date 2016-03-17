package com.sos.joe.globals.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import sos.util.SOSClassUtil;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Options.SOSOptionLocale;
import com.sos.dialog.components.SOSPreferenceStore;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.resources.SOSProductionResource;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class Options extends JSToolBox {

    private static final String ENV_VAR_SOS_JOE_HOME = "SOS_JOE_HOME";
    private static final String ENV_VAR_SCHEDULER_HOME = "SCHEDULER_HOME";
    private static final String LANGUAGE_EN = "en";
    private static final String ENVIRONMENT_VARIABLE_SOS_LOCALE = "SOS_LOCALE";
    private static final Logger LOGGER = Logger.getLogger(Options.class);
    private static Properties JOESettingsDefaults = null;
    private static Properties _properties = null;
    private static boolean _changed = false;
    private static boolean _showWizardInfo = true;
    private static String[] jobTitleList = null;
    private static HashMap holidaysDescription = null;
    private static String strLastFolderName = "";
    public static final String conEnvVarSCHEDULER_DATA = "SCHEDULER_DATA";
    public static final String conPropertyEDITOR_OPTIONS_FILE = "editor.options.file";
    public static final String conPropertyEDITOR_LANGUAGE = "editor.language";
    public static final String conPropertyEDITOR_ShowSplashScreen = "editor.ShowSplashScreen";
    public static final String conPropertyEDITOR_OpenLastFolder = "editor.OpenLastFolder";
    public static final String conPropertyTEMPLATE_LANGUAGE = "template.language";
    public static final String conPropertyTEMPLATE_LANGUAGE_LIST = "template.language.list";
    public static final String conPropertyEDITOR_ShowSplashScreenPicture = "editor.ShowSplashScreenPicture";
    public static final String DEFAULT_OPTIONS = "/sos/scheduler/editor/options.properties";
    public static String conJOEGreeting = "";
    public static SOSPreferenceStore objPrefStore = new SOSPreferenceStore(getJOEHomeDir().replaceAll("\\\\", "_"));

    private Options() {
    }

    public static int getLastTabItemIndex() {
        String strR = getProperty("LastTabItemIndex");
        if (strR == null || "".equalsIgnoreCase(strR)) {
            return -1;
        }
        return new Integer(strR);
    }

    public static void setLastTabItemIndex(final int pintLastTabItemIndex) {
        setProperty("LastTabItemIndex", String.valueOf(pintLastTabItemIndex));
    }

    public static String getLastFolderName() {
        return getProperty("LastFolderName");
    }

    public static void setLastFolderName(final String pstrLastFolderName) {
        setProperty("LastFolderName", pstrLastFolderName);
    }

    public static String getLastIncludeFolderName() {
        return getProperty("LastIncludeFolderName");
    }

    public static String getLastPropertyValue(final String pstrPropertyName) {
        return getProperty("LastIncludeFolderName");
    }

    public static void setLastPropertyValue(final String pstrPropertyName, final String pstrPropertyValue) {
        setProperty(pstrPropertyName, pstrPropertyValue);
    }

    public static void setLastIncludeFolderName(final String pstrLastFolderName) {
        setProperty("LastIncludeFolderName", pstrLastFolderName);
    }

    public static String getDefaultOptionFilename() {
        getProperties();
        String strSD = getJOEHomeDir().replaceAll("\\\\", "/");
        String strKey = conPropertyEDITOR_OPTIONS_FILE;
        String strF = getDefault(strKey);
        String strRet = strF.replaceAll("\\{scheduler_data\\}", strSD);
        strRet = strRet.replaceAll("\\{joe_home_dir\\}", strSD);
        LOGGER.info("getDefaultOptionFilename = " + strRet);
        return strRet;
    }

    private static void getProperties() {
        if (_properties == null) {
            _properties = new Properties(JOESettingsDefaults);
        }
    }

    public static String loadOptions(final Class cl) {
        String fName = "";
        try {
            JOESettingsDefaults = new Properties();
            LOGGER.debug(String.format("load Options from file: %1$s", DEFAULT_OPTIONS));
            JOESettingsDefaults.load(cl.getResourceAsStream(DEFAULT_OPTIONS));
            _properties = new Properties(JOESettingsDefaults);
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName() + "; Error reading default options from " + DEFAULT_OPTIONS, e);
        }
        try {
            fName = getDefaultOptionFilename();
            File file = new File(fName);
            if (file.exists()) {
                FileInputStream fi = new FileInputStream(fName);
                _properties.load(fi);
                fi.close();
            }
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName() + ". Error reading custom options from " + fName, e);
        }
        return null;
    }

    public static String saveProperties() {
        if (_properties != null && _changed) {
            try {
                String strOptionsFileName = getDefaultOptionFilename();
                if (!strOptionsFileName.trim().isEmpty()) {
                    FileOutputStream fo = new FileOutputStream(strOptionsFileName);
                    _properties.store(fo, conJOEGreeting + " - Options --");
                    fo.close();
                }
            } catch (Exception e) {
                new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
            }
        }
        return null;
    }

    public static void setProperty(final String key, final String value) {
        if (_properties != null) {
            _properties.setProperty(key, value);
            _changed = true;
        }
        objPrefStore.saveProperty(key, value);
    }

    public static boolean showSplashScreen() {
        return getBoolOption(conPropertyEDITOR_ShowSplashScreen);
    }

    public static boolean openLastFolder() {
        return getBoolOption(conPropertyEDITOR_OpenLastFolder);
    }

    public static boolean checkBool(final String pstrText) {
        boolean flgR = false;
        if (pstrText != null && "true".equalsIgnoreCase(pstrText)) {
            flgR = true;
        }
        return flgR;
    }

    public static boolean getBoolOption(final String pstrPropertyName) {
        String strT = getProperty(pstrPropertyName);
        return checkBool(strT);
    }

    public static String showSplashScreenPicture() {
        return getProperty(conPropertyEDITOR_ShowSplashScreenPicture, "/SplashScreenJOE.bmp");
    }

    public static String getLanguage() {
        getProperties();
        String strT = Locale.getDefault().getLanguage();
        String strSOSLocale = System.getenv(ENVIRONMENT_VARIABLE_SOS_LOCALE);
        if (strSOSLocale != null) {
            strT = strSOSLocale;
        } else {
            strT = getProperty(conPropertyEDITOR_LANGUAGE);
            if (strT == null || strT.trim().isEmpty()) {
                strT = LANGUAGE_EN;
            }
        }
        SOSOptionLocale.i18nLocale = new Locale(strT);
        return strT;
    }

    public static String getTemplateLanguage() {
        getProperties();
        String strT = Locale.getDefault().getLanguage();
        String strSOSLocale = System.getenv(ENVIRONMENT_VARIABLE_SOS_LOCALE);
        if (strSOSLocale != null) {
            strT = strSOSLocale;
        } else {
            strSOSLocale = strT;
        }
        strT = getProperty(conPropertyTEMPLATE_LANGUAGE);
        if (strT == null || strT.trim().isEmpty()) {
            strT = strSOSLocale;
            setTemplateLanguage(strT);
        }
        return strT;
    }

    public static String getTemplateLanguageList() {
        getProperties();
        String strLanguages = "de;en;fr;it;es";
        String strT = getProperty(conPropertyTEMPLATE_LANGUAGE_LIST);
        if (strT == null || strT.trim().isEmpty()) {
            strT = strLanguages;
            setProperty(conPropertyTEMPLATE_LANGUAGE_LIST, strT);
        }
        return strT;
    }

    public static String getDefault(final String key) {
        if (JOESettingsDefaults == null) {
            JOESettingsDefaults = new Properties();
        }
        return JOESettingsDefaults.getProperty(key.toLowerCase());
    }

    public static void setLanguage(final String language) {
        SOSOptionLocale.i18nLocale = new Locale(language);
        setProperty(conPropertyEDITOR_LANGUAGE, language);
    }

    public static void setTemplateLanguage(final String language) {
        setProperty(conPropertyTEMPLATE_LANGUAGE, language);
    }

    private static String getHelp(final String key, final String prefix) {
        try {
            String url = getProperty(prefix + ".help.url." + key);
            return url != null && !"".equals(url) ? url : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHelpURL(final String key, final String prefix) {
        try {
            String helpKey = getHelp(key, prefix);
            String url = null;
            if (helpKey == null) {
                url = Options.getHelp("index", prefix).replaceAll("\\{lang\\}", getLanguage());
            } else {
                url = helpKey.replaceAll("\\{lang\\}}", getLanguage());
            }
            return (Options.getHelp("maindir", prefix) + url).replaceAll("\\{scheduler_home\\}", Options.getSchedulerHome().replaceAll("\\\\", "/")).replaceAll("\\{lang\\}", getLanguage());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDocHelpURL(final String key) {
        return getHelpURL(key, "doc");
    }

    public static String getHelpURL(final String key) {
        return getHelpURL(key, "editor");
    }

    public static String[] getBrowserExec(String url, final String lang) {
        String os = System.getProperty("os.name").toLowerCase();
        String value = "";
        if (os.indexOf("windows") > -1) {
            value = getProperty("editor.browser.windows");
        } else {
            value = getProperty("editor.browser.unix");
        }
        url = url.replaceAll("file:/", "file://");
        value = value.replaceAll("\\{file\\}", url);
        value = value.replaceAll("\\{lang\\}", lang);
        return value.split("\\|");
    }

    public static String getSchemaVersion() {
        return getProperty("editor.schemaversion");
    }

    public static void readSchemaVersion() {
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(System.class.getResource(Options.getSchema()).toString());
            XPath x = XPath.newInstance("//xsd:documentation");
            List<Element> listOfElement = x.selectNodes(doc);
            if (!listOfElement.isEmpty()) {
                Element e = listOfElement.get(0);
                String version = e.getText();
                _properties.put("editor.schemaversion", version);
            }
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName() + ". could not read schema version from ", e);
        }
    }

    public static String getVersion() {
        return getProperty("editor.version");
    }

    public static String getSchema() {
        return getProperty("editor.xml.xsd");
    }

    public static String getActionSchema() {
        return getProperty("actions.xml.xsd");
    }

    public static boolean isValidate() {
        return getProperty("editor.xml.validate", "true").equalsIgnoreCase("true");
    }

    public static String getXSLT() {
        return getProperty("editor.xml.xslt").replaceAll("\\{scheduler_data\\}", getSchedulerData().replaceAll("\\\\", "/"));
    }

    @Deprecated
    public static String getDocSchema() {
        return SOSProductionResource.JOB_DOC_XSD.getFullName();
    }

    public static String getActionsSchema() {
        return SOSProductionResource.EVENT_SERVICE_XSD.getFullName();
    }

    public static boolean isDocValidate() {
        return "true".equalsIgnoreCase(getProperty("documentation.xml.validate", "true"));
    }

    public static String getDocXSLT() {
        return getProperty("documentation.xml.xslt").replaceAll("\\{scheduler_data\\}", getSchedulerData().replaceAll("\\\\", "/"));
    }

    public static String getXHTMLSchema() {
        return getProperty("documentation.xhtml.xsd");
    }

    public static String getXSLTFilePrefix() {
        return getProperty("editor.xml.xslt.file.prefix", "scheduler_editor-");
    }

    public static String getXSLTFileSuffix() {
        return getProperty("editor.xml.xslt.file.suffix", ".html");
    }

    public static String getBackupDir() {
        String s = getProperty("editor.backup.path", "");
        if (!s.endsWith("")) {
            s = s + "/";
        }
        return s;
    }

    public static boolean getBackupEnabled() {
        return getProperty("editor.backup.enabled", "false").equalsIgnoreCase("true");
    }

    public static String getLastDirectory() {
        return getSchedulerHotFolder();
    }

    @Deprecated
    public static void setLastDirectory(final File f, final Object dom) {
        if (f != null && f.getParent() != null) {
            setProperty("editor.file.lastopendir", f.getPath());
        }
    }

    public static void saveWindow(final Shell shell, final String name) {
        setProperty(name + ".window.left", String.valueOf(shell.getLocation().x));
        setProperty(name + ".window.top", String.valueOf(shell.getLocation().y));
        setProperty(name + ".window.width", String.valueOf(shell.getSize().x));
        setProperty(name + ".window.height", String.valueOf(shell.getSize().y));
        setProperty(name + ".window.status", String.valueOf(shell.getMaximized()));
    }

    @Deprecated
    public static void loadWindow(final Shell shell, final String name) {
        getProperties();
        Point location = new Point(0, 0);
        Point size = new Point(0, 0);
        try {
            String left = getProperty(name + ".window.left");
            String top = getProperty(name + ".window.top");
            if (left != null && isNumeric(left) && top != null && isNumeric(top)) {
                location.x = new Integer(left).intValue();
                location.y = new Integer(top).intValue();
                shell.setLocation(location);
            }
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
        }
        try {
            Boolean b = new Boolean(getProperty(name + ".window.status"));
            shell.setMaximized(b.booleanValue());
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
        }
        try {
            String width = getProperty(name + ".window.width");
            String height = getProperty(name + ".window.height");
            if (width != null && isNumeric(width) && height != null && isNumeric(height)) {
                size.x = new Integer(getProperty(name + ".window.width")).intValue();
                size.y = new Integer(getProperty(name + ".window.height")).intValue();
                shell.setSize(size);
            }
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
        }
    }

    public static boolean isNumeric(final String str) {
        boolean retVal = true;
        char[] c = null;
        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return false;
        }
        c = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(c[i])) {
                return false;
            }
        }
        return retVal;
    }

    @Deprecated
    public static void saveSash(final String name, final int[] sash) {
        setProperty(name + ".sash.layout", sash[0] + "," + sash[1]);
    }

    @Deprecated
    public static void loadSash(final String name, final SashForm sash) {
        try {
            String value = getProperty(name + ".sash.layout");
            if (value != null && !value.isEmpty()) {
                String[] values = value.split(",");
                int[] weights = { new Integer(values[0].trim()).intValue(), new Integer(values[1].trim()).intValue() };
                sash.setWeights(weights);
            }
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
        }
    }

    private static String getSystemProperty(final String pstrPropertyName, final String pstrDefaultValue) {
        String strT = System.getProperty(pstrPropertyName);
        if (strT != null && !strT.isEmpty()) {
            if (strT.startsWith("%") && strT.endsWith("%")) {
                String strV = strT.substring(1, strT.length() - 1);
                String strW = System.getenv(strV);
                if (strW != null) {
                    strT = strW;
                } else {
                    strT = pstrDefaultValue;
                }
            }
        } else {
            strT = pstrDefaultValue;
        }
        return strT;
    }

    public static String getJOEHomeDir() {
        String strT = getSystemProperty(ENV_VAR_SOS_JOE_HOME, "");
        if (strT.isEmpty()) {
            strT = getSystemProperty(conEnvVarSCHEDULER_DATA, "");
            if (strT.isEmpty()) {
                strT = getSystemProperty(ENV_VAR_SCHEDULER_HOME, "");
            }
        }
        LOGGER.debug("getJOEHomeDir = " + strT);
        return strT;
    }

    public static String getSchedulerHome() {
        LOGGER.trace("getSchedulerHome = " + getSystemProperty(ENV_VAR_SCHEDULER_HOME, ""));
        return getSystemProperty(ENV_VAR_SCHEDULER_HOME, "");
    }

    public static String getSchedulerData() {
        LOGGER.debug("getSchedulerData = " + getSystemProperty(conEnvVarSCHEDULER_DATA, getSchedulerHome()));
        return getSystemProperty(conEnvVarSCHEDULER_DATA, getSchedulerHome());
    }

    public static String getSchedulerNormalizedHome() {
        String home = Options.getSchedulerHome();
        home = new File(home).getAbsolutePath() + "/";
        home = home.replaceAll("\\\\", "/");
        return home;
    }

    public static String getSchedulerNormalizedData() {
        String data = Options.getSchedulerData();
        data = new File(data).getAbsolutePath() + "/";
        data = data.replaceAll("\\\\", "/");
        return data;
    }

    public static String getSchedulerHotFolder() {
        String sdata = getSchedulerData();
        sdata = sdata.endsWith("/") || sdata.endsWith("\\") ? sdata : sdata + "/";
        String strData = sdata + "config/live/";
        return getSystemProperty("SCHEDULER_HOT_FOLDER", strData);
    }

    public static String getSchedulerNormalizedHotFolder() {
        String sdata = Options.getSchedulerHotFolder();
        sdata = new File(sdata).getAbsolutePath() + "/";
        sdata = sdata.replaceAll("\\\\", "/");
        return sdata;
    }

    public static Color getRequiredColor() {
        try {
            int r = new Integer(getProperty("required.color.r")).intValue();
            int g = new Integer(getProperty("required.color.g")).intValue();
            int b = new Integer(getProperty("required.color.b")).intValue();
            return ResourceManager.getColor(r, g, b);
        } catch (Exception e) {
            return ResourceManager.getColor(255, 255, 210);
        }
    }

    public static Color getLightBlueColor() {
        try {
            return ResourceManager.getColor(224, 255, 255);
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
            return ResourceManager.getColor(255, 255, 187);
        }
    }

    public static Color getLightYellow() {
        return ResourceManager.getColor(255, 255, 187);
    }

    public static Color getBlueColor() {
        try {
            int r = 0;
            int g = 0;
            int b = new Integer(getProperty("required.color.b")).intValue();
            return ResourceManager.getColor(r, g, b);
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
            return ResourceManager.getColor(255, 255, 210);
        }
    }

    public static Color getRedColor() {
        try {
            int r = new Integer(getProperty("required.color.r")).intValue();
            int g = 0;
            int b = 0;
            return ResourceManager.getColor(r, g, b);
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
            return ResourceManager.getColor(255, 255, 219);
        }
    }

    public static Color getWhiteColor() {
        try {
            int r = 255;
            int g = 255;
            int b = 255;
            return ResourceManager.getColor(r, g, b);
        } catch (Exception e) {
            new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
            return ResourceManager.getColor(255, 255, 219);
        }
    }

    public static boolean isShowWizardInfo() {
        String s = getProperty("editor.job.wizard.info.show");
        if (s != null && !s.trim().isEmpty()) {
            _showWizardInfo = "true".equals(s);
        }
        return _showWizardInfo;
    }

    public static void setShowWizardInfo(final boolean wizardInfo) {
        _showWizardInfo = wizardInfo;
        setProperty("editor.job.wizard.info.show", wizardInfo ? "true" : "false");
    }

    public static boolean getPropertyBoolean(final String name) {
        String s = getProperty(name);
        if (s == null) {
            return true;
        }
        return "true".equalsIgnoreCase(s);
    }

    public static void setPropertyBoolean(final String name, final boolean value) {
        setProperty(name, value ? "true" : "false");
    }

    public static String getDetailXSLT() {
        return getProperty("detail.editor.xslt");
    }

    public static String getProperty(final String key, final String pstrDefaultValue) {
        String strT = getProperty(key);
        if (strT == null || strT.isEmpty()) {
            strT = pstrDefaultValue;
        }
        if (objPrefStore.getProperty(key).isEmpty()) {
            objPrefStore.saveProperty(key, pstrDefaultValue);
        }
        return strT;
    }

    public static String getProperty(final String key) {
        String strT = objPrefStore.getProperty(key);
        if (strT.isEmpty()) {
            getProperties();
            strT = _properties.getProperty(key);
            if (strT == null) {
                strT = "";
            }
        }
        return strT;
    }

    public static String[] getJobTitleList() {
        if (jobTitleList != null) {
            return jobTitleList;
        } else {
            return new String[] {};
        }
    }

    public static void setJobTitleList(final String[] jobTitleList) {
        Options.jobTitleList = jobTitleList;
    }

    public static HashMap getHolidaysDescription() {
        if (holidaysDescription != null) {
            return holidaysDescription;
        } else {
            return new HashMap();
        }
    }

    public static void setHolidaysDescription(final HashMap holidaysDescription) {
        Options.holidaysDescription = holidaysDescription;
    }

    public static String[] getPropertiesWithPrefix(final String prefix) {
        String[] retVal = null;
        String s = "";
        Properties p = new Properties();
        p.putAll(JOESettingsDefaults);
        p.putAll(_properties);
        if (prefix.equalsIgnoreCase("monitor_favorite_")) {
            if (!p.containsKey("monitor_favorite_java_configuration_monitor")) {
                p.put("monitor_favorite_java_configuration_monitor", "sos.scheduler.managed.configuration.ConfigurationOrderMonitor");
                setProperty("monitor_favorite_java_configuration_monitor", "sos.scheduler.managed.configuration.ConfigurationOrderMonitor");
            }
            if (!p.containsKey("monitor_favorite_java_create_event_monitor")) {
                p.put("monitor_favorite_java_create_event_monitor", "sos.scheduler.job.JobSchedulerSubmitEventMonitor");
                setProperty("monitor_favorite_java_create_event_monitor", "sos.scheduler.job.JobSchedulerSubmitEventMonitor");
            }
        }
        java.util.Iterator keys = p.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            if (key != null && !key.toString().isEmpty() && key.toString().startsWith(prefix)) {
                s = key.toString().substring(prefix.length()) + ";" + s;
            }
        }
        retVal = s.split(";");
        return retVal;
    }

    public static void removeProperty(final String name) {
        if (name != null && !name.isEmpty()) {
            _properties.remove(name);
            saveProperties();
        }
    }

}