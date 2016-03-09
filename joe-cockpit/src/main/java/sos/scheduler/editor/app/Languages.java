package sos.scheduler.editor.app;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class Languages {

    private static final String BUNDLE_NAME = "sos.scheduler.editor.languages";					//$NON-NLS-1$
    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.GERMAN);

    private Languages() {
    }

    public static HashMap getLanguages() {
        HashMap langs = new HashMap();
        for (Enumeration e = RESOURCE_BUNDLE.getKeys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            langs.put(key, RESOURCE_BUNDLE.getString(key));
        }
        return langs;
    }
}
