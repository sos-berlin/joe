package sos.scheduler.editor.app;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class ResourceManager extends SWTResourceManager {
    private static final int MISSING_IMAGE_SIZE = 10;

    private static HashMap   m_ClassImageMap    = new HashMap();


    public static Image getImageFromResource(String path) {
        return getImageFromResource("default", path);
    }


    public static Image getImageFromResource(String section, String path) {
        String key = section + '|' + SWTResourceManager.class.getName() + '|' + path;
        Image image = (Image) m_ClassImageMap.get(key);
        if (image == null) {
            try {
                InputStream is = key.getClass().getResourceAsStream(path);
                image = getImage(is);
                m_ClassImageMap.put(key, image);
                is.close();
            } catch (Exception e) {
            	try {
        			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
        		} catch(Exception ee) {
        			//tu nichts
        		}
                image = getMissingImage();
                m_ClassImageMap.put(key, image);
            }
        }
        return image;
    }


    private static Image getMissingImage() {
        Image image = new Image(Display.getCurrent(), MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
        //
        GC gc = new GC(image);
        gc.setBackground(getColor(SWT.COLOR_RED));
        gc.fillRectangle(0, 0, MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
        gc.dispose();
        //
        return image;
    }


    /**
     * Dispose of cached objects and their underlying OS resources. This should
     * only be called when the cached objects are no longer needed (e.g. on
     * application shutdown)
     */
    public static void dispose() {
        disposeImages();
        SWTResourceManager.disposeColors();
        SWTResourceManager.disposeFonts();
        SWTResourceManager.disposeCursors();
    }


    /**
     * Dispose all of the cached images
     */
    public static void disposeImages() {
        SWTResourceManager.disposeImages();

        for (Iterator I = m_ClassImageMap.values().iterator(); I.hasNext();)
            ((Image) I.next()).dispose();
        m_ClassImageMap.clear();
    }


    /**
     * Dispose cached images in specified section
     * 
     * @param section the section do dispose
     */
    public static void disposeImages(String section) {
        SWTResourceManager.disposeImages(section);

        for (Iterator I = m_ClassImageMap.keySet().iterator(); I.hasNext();) {
            String key = (String) I.next();
            if (!key.startsWith(section + '|'))
                continue;
            Image image = (Image) m_ClassImageMap.get(key);
            image.dispose();
            I.remove();
        }
    }

}
