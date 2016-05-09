package com.sos.joe.jobdoc.editor;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sos.dialog.components.SOSPreferenceStore;
import com.sos.dialog.message.ErrorLog;
import com.sos.i18n.annotation.I18NResourceBundle;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class JobDocEditorMain {

    private static final Logger LOGGER = Logger.getLogger(JobDocEditorMain.class);
    private static Display display = null;
    public static MainWindow objMainWindow = null;

    public static void main(final String args[]) {
        try {
            Application window = new Application();
            ErrorLog.gstrApplication = "JobDocEditor";
            SOSPreferenceStore.gstrApplication = ErrorLog.gstrApplication;
            window.setBlockOnOpen(true);
            window.open();
            Display.getCurrent().dispose();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void openApplicationMainWnd(final Shell shell) {
        MainWindow.getSShell().open();
        MainWindow.getSShell().update();
        while (!MainWindow.getSShell().isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Exception e) {
                MainWindow.getSShell().redraw();
            }
        }
        if (!display.isDisposed()) {
            display.dispose();
        }
    }

}