package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jdom.JDOMException;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.doc.DocumentationDom;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FilenameFilter;

public class IOUtils {

    public static boolean openFile(DomParser dom) {
        return openFile(null, null, dom);
    }


    public static boolean openFile(Collection filenames, DomParser dom) {
        return openFile(null, filenames, dom);
    }


    private static String getDomInstance(DomParser dom) {
        if (dom instanceof SchedulerDom)
            return " Scheduler";
        else if (dom instanceof DocumentationDom)
            return " Documentation";
        else
            return "";
    }


    public static boolean openFile(String filename, Collection filenames, DomParser dom) {
        try {
            // open file dialog
            if (filename == null || filename.equals("")) {
                FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);
                fdialog.setFilterPath(Options.getLastDirectory());
                fdialog.setText("Open" + getDomInstance(dom) + " File");
                filename = fdialog.open();
            }

            // check for opened file
            if (filenames != null) {
                for (Iterator it = filenames.iterator(); it.hasNext();) {
                    if (((String) it.next()).equals(filename)) {
                        MainWindow
                                .message(Messages.getString("MainListener.fileOpened"), SWT.ICON_INFORMATION | SWT.OK);
                        return false;
                    }
                }
            }

            if (filename != null && !filename.equals("")) { //$NON-NLS-1$
                File file = new File(filename);

                // check the file
                if (!file.exists())
                    MainWindow.message(Messages.getString("MainListener.fileNotFound"), //$NON-NLS-1$
                            SWT.ICON_WARNING | SWT.OK);
                else if (!file.canRead())
                    MainWindow.message(Messages.getString("MainListener.fileReadProtected"), //$NON-NLS-1$
                            SWT.ICON_WARNING | SWT.OK);
                else { // open it...
                    int cont = SWT.NO;
                    try {
                        // read and validate
                        dom.read(filename);
                    } catch (JDOMException e) {
                        cont = MainWindow.message(Messages.getString("MainListener.validationError", new String[] {
                                file.getAbsolutePath(), e.getMessage() }), SWT.ICON_WARNING | SWT.YES | SWT.NO);
                        if (cont == SWT.NO)
                            return false;
                    } catch (IOException e) {
                        MainWindow.message(Messages.getString("MainListener.errorReadingFile", new String[] {
                                file.getAbsolutePath(), e.getMessage() }), SWT.ICON_ERROR | SWT.OK);
                        return false;
                    }

                    if (cont == SWT.YES) { // validation error, continue
                        // anyway...
                        if (!dom.read(filename, false)) { // unknown file
                            MainWindow
                                    .message(Messages.getString("MainListener.unknownXML"), SWT.ICON_WARNING | SWT.OK);
                            return false;
                        }
                    }
                }
            } else
                return false;

            MainWindow.getSShell().setText("Job Scheduler Editor [" + filename + "]");

            Options.setLastDirectory(new File(filename));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
        }
        return false;
    }


    public static boolean saveFile(DomParser dom, boolean saveas) {
        String filename = dom.getFilename();

        try {
            // if save file as...
            if (saveas || filename == null || filename.equals("")) { //$NON-NLS-1$
                FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.SAVE);
                fdialog.setFilterPath(Options.getLastDirectory());
                fdialog.setText("Save" + getDomInstance(dom) + " File");
                filename = fdialog.open();
                if (filename == null)
                    return false;
                saveas = true;
            }

            File file = new File(filename);
            Options.setLastDirectory(file);

            // overwrite the new file if it exists?
            if (saveas && file.exists()) {
                int ok = MainWindow.message(Messages.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
                        SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (ok == SWT.NO)
                    return false;
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            if (!file.canWrite()) {
                MainWindow.message(Messages.getString("MainListener.fileWriteProtected"), //$NON-NLS-1$
                        SWT.ICON_WARNING | SWT.OK);
                return false;
            } else {
                if (!saveas)
                    createBackup(filename);
                dom.write(filename);
                MainWindow.getSShell().setText("Job Scheduler Editor [" + filename + "]");

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
            return false;
        }
    }


    public static boolean continueAnyway(DomParser dom) {
        if (dom.isChanged()) {
            int ok = MainWindow.message(Messages.getString("MainListener.contentChanged"), //$NON-NLS-1$
                    SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);

            if (ok == SWT.CANCEL)
                return false;

            else if (ok == SWT.YES && !saveFile(dom, false))
                return false;
        }
        return true;
    }


    public static void createBackup(String filename) {
        String f;
        File outFile = null;
        if (filename == null) {
            f = "";
        } else {
            f = filename;
        }
        try {
            if (Options.getBackupEnabled() && !f.equals("")) {
                if (Options.getBackupDir().equals("")) {
                    outFile = new File(filename + ".bak");
                } else {
                    outFile = new File(Options.getBackupDir() + "/" + new File(filename).getName() + ".bak");
                }
                File inFile = new File(filename);
                if (inFile.exists()) {
                    if (outFile.exists()) { // Wenn destination schon existiert,
                        // dann vorher l�schen.
                        outFile.delete();
                    }

                    if (!inFile.canRead())
                        throw new Exception("Datei " + inFile + " kann nicht gelesen werden.");

                    copyFile(inFile, outFile, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
        }
    }


    public static boolean copyFile(File source, File dest, boolean append) throws Exception {
        int size = (int) source.length();
        int bytes_read = 0;
        byte[] data = null;
        FileInputStream in = null;
        FileOutputStream out = null;
        boolean retVal = true;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest, append);
            data = new byte[size];
            while (bytes_read < size)
                bytes_read += in.read(data, bytes_read, size - bytes_read);
            out.write(data);
            return retVal;

        } catch (Exception e) {
            throw new Exception("\n -> ..error in copyFile: " + e);
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
   

}
