package com.sos.joe.globals.misc;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.dialog.swtdesigner.SWTResourceManager;

public class ResourceManager extends SWTResourceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);
    private static final int MISSING_IMAGE_SIZE = 10;
    private static HashMap<String, Image> m_ClassImageMap = new HashMap<String, Image>();

    public static Image getImageFromResource(final String path) {
        return getImageFromResource("default", path);
    }

    public static Image getImageFromResource(final String section, final String path) {
        String key = section + '|' + SWTResourceManager.class.getName() + '|' + path;
        Image image = m_ClassImageMap.get(key);
        if (image == null) {
            try {
                InputStream is = key.getClass().getResourceAsStream(path);
                image = getImage(is);
                m_ClassImageMap.put(key, image);
                is.close();
            } catch (Exception e) {
                image = getMissingImage();
                m_ClassImageMap.put(key, image);
            }
        }
        return image;
    }

    public static InputStream getInputStream4Resource(final String pstrKey) {
        InputStream objSS = pstrKey.getClass().getResourceAsStream(pstrKey);
        if (objSS == null) {
            throw new JobSchedulerException("Resource not found: " + pstrKey);
        }
        return objSS;
    }

    private static Image getMissingImage() {
        Image image = new Image(Display.getCurrent(), MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
        GC gc = new GC(image);
        gc.setBackground(getColor(SWT.COLOR_RED));
        gc.fillRectangle(0, 0, MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
        gc.dispose();
        return image;
    }

    public static void dispose() {
        disposeImages();
        SWTResourceManager.disposeColors();
        SWTResourceManager.disposeFonts();
        SWTResourceManager.disposeCursors();
    }

    public static void disposeImages() {
        SWTResourceManager.disposeImages();
        for (Object element : m_ClassImageMap.values()) {
            ((Image) element).dispose();
        }
        m_ClassImageMap.clear();
    }

    public static void disposeImages(final String section) {
        SWTResourceManager.disposeImages(section);
        for (Iterator I = m_ClassImageMap.keySet().iterator(); I.hasNext();) {
            String key = (String) I.next();
            if (!key.startsWith(section + '|')) {
                continue;
            }
            Image image = m_ClassImageMap.get(key);
            image.dispose();
            I.remove();
        }
    }

}