package sos.scheduler.editor.app;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final String   BUNDLE_NAME = "sos.scheduler.editor.messages"; //$NON-NLS-1$

    private static ResourceBundle RESOURCE_BUNDLE;


    // private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
    // .getBundle(BUNDLE_NAME, Locale.GERMAN);

    private Messages() {
    }


    public static boolean setResource(Locale locale) {
        try {
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

 
    public static String getBundle() {
        return BUNDLE_NAME;
    }


    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }


    public static String getString(String key, String variable) {
        return getString(key, new String[] { variable });
    }


    public static String getString(String key, Object[] values) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), values);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }


    public static boolean hasMessage(String key) {
        try {
            String msg = RESOURCE_BUNDLE.getString(key);
            return msg != null && !msg.equals("");
        } catch (MissingResourceException e) {
            return false;
        }
    }


    public static String getTooltip(String key) {
        try {
            String msg = RESOURCE_BUNDLE.getString("tooltip." + key);
            return msg != null && !msg.equals("") ? msg : null;
        } catch (Exception e) {
            return null;
        }
    }
}
