package com.sos.joe.globals.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.options.Options;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en") public class Messages {
    //    private static final String   BUNDLE_NAME = "sos.scheduler.editor.messages"; //$NON-NLS-1$
    private static final String BUNDLE_NAME = "JOEMessages"; //$NON-NLS-1$
    public static String strLastMsgKey = "";
    private static ResourceBundle RESOURCE_BUNDLE;
    private static JSToolBox objToolBox;

    private Messages() {
        objToolBox = new JSToolBox("JOEMessages");
    }

    public static boolean setResource(Locale pobjLocale) {
        getMsgObj().setLocale(pobjLocale);
        // try {
        // RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        // } catch (MissingResourceException e) {
        // return false;
        // }
        return true;
    }

    public static void clearMsgObj() {
        objToolBox = null;
    }

    private static com.sos.localization.Messages getMsgObj() {
        if (objToolBox == null) {
            objToolBox = new JSToolBox("JOEMessages", Options.getLanguage());
        }
        return objToolBox.getMessageObject();
    }

    public static String getBundle() {
        return BUNDLE_NAME;
    }

    public static String getString(String key) {
        return getLabel(key);
    }

    public static String getString(String key, String variable) {
        strLastMsgKey = key;
        return getMsgObj().getMsg(key, variable);
    }

    public static String getMsg(String key, Object... pstrArgs) {
        strLastMsgKey = key;
        return getMsgObj().getMsg(key, pstrArgs);
    }

    public static String getString(String key, Object[] values) {
        strLastMsgKey = key;
        return getMsgObj().getMsg(key, values);

        // try {
        // return MessageFormat.format(RESOURCE_BUNDLE.getString(key), values);
        // } catch (MissingResourceException e) {
        // return '!' + key + '!';
        // }
    }

    public static boolean hasMessage(String key) {
        try {
            // String msg = RESOURCE_BUNDLE.getString(key);
            String msg = getMsgObj().getMsg(key);
            return msg != null && !msg.equals("");
        }
        catch (MissingResourceException e) {
            return false;
        }
    }

    public static String getTooltip(String key) {
        try {
            strLastMsgKey = key;
            String msg = getMsgObj().getTooltip("tooltip." + key.toLowerCase());
            if (msg == null) {
                msg = getMsgObj().getLabel(key.toLowerCase() + ".tooltip");
            }

            return msg != null && !msg.equals("") ? msg : key;
        }
        catch (Exception e) {
            return key;
        }
    }

    public static String getLabel(String pstrKey) {
        String key = pstrKey.trim().replaceAll(" ", "");
        try {
            strLastMsgKey = key;
            String msg = getMsgObj().getLabel(key.toLowerCase() + ".label");
            if (msg == null) {
                msg = getMsgObj().getLabel(key + ".label");
                if (msg == null) {
                    msg = getMsgObj().getLabel(key);
                }
            }
            String strR = "";
            if (msg != null && msg.equals("") == false) {
                strR = msg;
            }
            else {
                strR = key;
            }
            return strR;
        }
        catch (Exception e) {
            return pstrKey;
        }
    }

    public static String getF1(String pstrKey) {
        String key = pstrKey.trim().replaceAll(" ", "");
        try {
            strLastMsgKey = key;
            String msg = getMsgObj().getLabel(key.toLowerCase() + ".f1");
            if (msg == null) {
                msg = getMsgObj().getLabel(key.toLowerCase() + ".f1");
                if (msg == null) {
                    msg = getMsgObj().getLabel(key);
                }
            }
            return msg != null && !msg.equals("") ? msg : key;
        }
        catch (Exception e) {
            return pstrKey;
        }
    }

}
