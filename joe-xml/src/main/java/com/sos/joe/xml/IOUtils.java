package com.sos.joe.xml;

import static sos.util.SOSClassUtil.getMethodName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.vfs.common.SOSVFSFactory;
import com.sos.vfs.common.interfaces.ISOSProvider;
import com.sos.vfs.common.interfaces.ISOSProviderFile;
import com.sos.vfs.common.options.SOSBaseOptions;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobdoc.DocumentationDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.SchedulerHotFolderFileList;
import com.sos.scheduler.model.SchedulerObjectFactory;

import sos.util.SOSFile;

public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static SchedulerHotFolder openHotFolder(final String filename) {
        SchedulerHotFolder objSchedulerHotFolder = null;
        try {
            boolean isDirectory = true;
            if (isDirectory) {
                String strHotFolderPathName = filename;
                if (filename == null || filename.isEmpty()) {
                    DirectoryDialog fdialog = new DirectoryDialog(ErrorLog.getSShell(), SWT.MULTI);
                    fdialog.setFilterPath(Options.getLastDirectory());
                    fdialog.setText("Open HotFolder directory ...");
                    String fname = fdialog.open();
                    if (fname == null) {
                        return null;
                    }
                    strHotFolderPathName = fname;
                }
                ISOSProvider objFileSystemHandler = null;
                try {
                    SOSBaseOptions vfsOptions = new SOSBaseOptions();
                    objFileSystemHandler = SOSVFSFactory.getProvider("local", vfsOptions.ssh_provider);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return null;
                }
                SchedulerObjectFactory objFactory = null;
                objFactory = new SchedulerObjectFactory();
                ISOSProviderFile objHotFolder = objFileSystemHandler.getFile(strHotFolderPathName);
                objSchedulerHotFolder = objFactory.createSchedulerHotFolder(objHotFolder);
                LOGGER.info(String.format("... load %1$s", strHotFolderPathName));
                SchedulerHotFolderFileList objSchedulerHotFolderFileList = objSchedulerHotFolder.load();
                objSchedulerHotFolderFileList.getFolderList();
                objSchedulerHotFolderFileList.getJobList();
                objSchedulerHotFolderFileList.getJobChainList();
                objSchedulerHotFolderFileList.getOrderList();
                objSchedulerHotFolderFileList.getProcessClassList();
                objSchedulerHotFolderFileList.getLockList();
                objSchedulerHotFolderFileList.getScheduleList();
                objSchedulerHotFolderFileList.getParamsList();
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return objSchedulerHotFolder;
    }

    public static boolean openFile(final DomParser dom) {
        return openFile(null, null, dom);
    }

    public static boolean openFile(final Collection filenames, final DomParser dom) {
        return openFile(null, filenames, dom);
    }

    private static String getDomInstance(final DomParser dom) {
        if (dom instanceof SchedulerDom) {
            return " Scheduler";
        } else if (dom instanceof DocumentationDom) {
            return " Documentation";
        } else {
            return "";
        }
    }

    public static String getJobschedulerObjectPathName(final String mask) {
        return getJobSchedulerObjectPathFromFileSystem(mask, Options.getSchedulerHotFolder());
    }

    public static String openDirectoryFile(final String mask, final String pstrDirectoryName) {
        String filename = "";
        FileDialog fdialog = new FileDialog(ErrorLog.getSShell(), SWT.OPEN);
        fdialog.setFilterPath(pstrDirectoryName);
        String filterMask = mask.replaceAll("\\\\", "");
        filterMask = filterMask.replaceAll("\\^.", "");
        filterMask = filterMask.replaceAll("\\$", "");
        fdialog.setFilterExtensions(new String[] { filterMask });
        filename = fdialog.open();
        if (filename == null || filename.trim().isEmpty()) {
            return filename;
        }
        filename = filename.replaceAll("\\\\", "/");
        String env = Options.getSchedulerHotFolder().replaceAll("\\\\", "/");
        int pos = filename.toLowerCase().indexOf(env.toLowerCase().toLowerCase());
        if (pos >= 0) {
            File fleT = new File(filename);
            filename = fleT.getName();
        }
        return filename;
    }

    public static String getJobSchedulerObjectPathFromFileSystem(final String mask, final String pstrDirectoryName) {
        String filename = "";
        FileDialog fdialog = new FileDialog(ErrorLog.getSShell(), SWT.OPEN);
        fdialog.setFilterPath(pstrDirectoryName);
        String filterMask = mask.replaceAll("\\\\", "");
        filterMask = filterMask.replaceAll("\\^.", "");
        filterMask = filterMask.replaceAll("\\$", "");
        fdialog.setFilterExtensions(new String[] { filterMask });
        filename = fdialog.open();
        if (filename == null || filename.trim().isEmpty()) {
            return filename;
        }
        String jobSchededulerObjectPath = filename.replaceAll("\\\\", "/");
        String hotFolderPath = Options.getSchedulerHotFolder().replaceAll("\\\\", "/");
        int pos = jobSchededulerObjectPath.toLowerCase().indexOf(hotFolderPath.toLowerCase().toLowerCase());
        if (pos >= 0) {
            int add = hotFolderPath.endsWith("/") ? -1 : 0;
            jobSchededulerObjectPath = jobSchededulerObjectPath.substring(pos == -1 ? 0 : pos + hotFolderPath.length() + add);
        }
        jobSchededulerObjectPath = jobSchededulerObjectPath.substring(0, jobSchededulerObjectPath.length() - filterMask.length() + 1);
        return jobSchededulerObjectPath;
    }

    public static boolean openFile(String filename, final Collection filenames, final DomParser dom) {
        try {
            boolean isDirectory = dom instanceof SchedulerDom && ((SchedulerDom) dom).isDirectory();
            String xml = null;
            if (!isDirectory && (filename == null || "".equals(filename))) {
                FileDialog fdialog = new FileDialog(ErrorLog.getSShell(), SWT.OPEN);
                fdialog.setFilterPath(Options.getLastDirectory());
                fdialog.setText("Open" + getDomInstance(dom) + " File");
                filename = fdialog.open();
            }
            if (isDirectory) {
                String path = filename;
                if (filename == null || filename.isEmpty()) {
                    DirectoryDialog fdialog = new DirectoryDialog(ErrorLog.getSShell(), SWT.MULTI);
                    fdialog.setFilterPath(Options.getLastDirectory());
                    fdialog.setText("Open HotFolder directory ...");
                    String fname = fdialog.open();
                    if (fname == null) {
                        return false;
                    }
                    path = fname;
                }
                MergeAllXMLinDirectory allJob = new MergeAllXMLinDirectory(path);
                xml = allJob.parseDocuments();
                ((SchedulerDom) dom).setListOfReadOnlyFiles(allJob.getListOfReadOnly());
                if (allJob.getListOfChangeElementNames() != null && !allJob.getListOfChangeElementNames().isEmpty()) {
                    ((SchedulerDom) dom).setListOfChangeElementNames(allJob.getListOfChangeElementNames());
                }
                filename = path;
                if (filename == null) {
                    return false;
                }
            }
            if (filenames != null) {
                for (Iterator it = filenames.iterator(); it.hasNext();) {
                    if (((String) it.next()).equals(filename)) {
                        ErrorLog.message(Messages.getString("MainListener.fileOpened"), SWT.ICON_INFORMATION | SWT.OK);
                        return false;
                    }
                }
            }
            if (filename != null && !"".equals(filename)) {
                File file = new File(filename);
                if (!file.exists()) {
                    file = new File(file.getAbsolutePath());
                }
                if (!file.exists()) {
                    ErrorLog.message(Messages.getString("MainListener.fileNotFound", new String[] { file.getAbsolutePath() }), SWT.ICON_WARNING
                            | SWT.OK);
                    return false;
                } else if (!file.canRead()) {
                    ErrorLog.message(Messages.getString("MainListener.fileReadProtected", new String[] { file.getAbsolutePath() }), SWT.ICON_WARNING
                            | SWT.OK);
                } else {
                    int cont = SWT.NO;
                    try {
                        if (isDirectory) {
                            dom.readString(xml, true);
                            dom.setFilename(filename);
                        } else {
                            dom.read(filename);
                        }
                    } catch (JDOMException e) {
                        cont = ErrorLog.message(Messages.getString("MainListener.validationError", new String[] { file.getAbsolutePath(), e
                                .getMessage() }), SWT.ICON_WARNING | SWT.YES | SWT.NO);
                        if (cont == SWT.NO) {
                            return false;
                        }
                    } catch (IOException e) {
                        new ErrorLog("error in " + getMethodName(), e);
                        ErrorLog.message(Messages.getString("MainListener.errorReadingFile", new String[] { file.getAbsolutePath(), e.getMessage() }),
                                SWT.ICON_ERROR | SWT.OK);
                        return false;
                    }
                    if (cont == SWT.YES) {
                        if (isDirectory) {
                            dom.readString(xml, false);
                            dom.setFilename(filename);
                        } else if (!dom.read(filename, false)) {
                            ErrorLog.message(Messages.getString("MainListener.unknownXML"), SWT.ICON_WARNING | SWT.OK);
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
            ErrorLog.getSShell().setText("Job Scheduler Editor [" + filename + "]");
            Options.setLastDirectory(new File(filename), dom);
            return true;
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName(), e);
            ErrorLog.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
        } finally {
            if (filename != null && new File(filename).getName().startsWith("#xml#.config.")) {
                new java.io.File(filename).delete();
            }
        }
        return false;
    }

    public static boolean saveFile(final DomParser dom, boolean saveas) {
        String filename = dom.getFilename();
        String originFilename = null;
        boolean overrideOriFile = saveas;
        try {
            if (saveas || filename == null || "".equals(filename)) {
                FileDialog fdialog = new FileDialog(ErrorLog.getSShell(), SWT.SAVE);
                fdialog.setFilterPath(Options.getLastDirectory());
                fdialog.setText("Save" + getDomInstance(dom) + " File");
                filename = fdialog.open();
                if (filename == null) {
                    return false;
                }
                saveas = true;
            }
            originFilename = filename;
            filename = filename + "~";
            File file = new File(filename);
            Options.setLastDirectory(file, dom);
            if (saveas && new File(originFilename).exists()) {
                int ok = ErrorLog.message(Messages.getString("MainListener.doFileOverwrite"), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (ok == SWT.NO) {
                    return false;
                } else {
                    overrideOriFile = false;
                }
            }
            if (!file.exists()) {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    int c = ErrorLog.message("This Directory does not exist. Should it be created now?", SWT.YES | SWT.NO | SWT.ICON_WARNING);
                    if (c == SWT.YES) {
                        if (!file.getParentFile().mkdirs()) {
                            ErrorLog.message("Could not create Directory: " + file.getParent(), SWT.ICON_WARNING | SWT.OK);
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                if (!file.createNewFile()) {
                    ErrorLog.message("Could not save file", SWT.ICON_WARNING | SWT.OK);
                    return false;
                }
            }
            if (!file.canWrite()) {
                ErrorLog.message(Messages.getString("MainListener.fileWriteProtected"), SWT.ICON_WARNING | SWT.OK);
                return false;
            } else {
                if (!saveas) {
                    createBackup(filename);
                }
                if (dom instanceof SchedulerDom && ((SchedulerDom) dom).isLifeElement()) {
                    MergeAllXMLinDirectory save = new MergeAllXMLinDirectory(file.getParent());
                    originFilename = save.saveAsLifeElement(dom.getRoot().getName(), dom.getRoot(), originFilename);
                    try {
                        dom.setFilename(new File(originFilename).getCanonicalPath());
                    } catch (Exception e) {
                    }
                    dom.setChanged(false);
                    new File(filename).delete();
                } else {
                    dom.write(filename);
                    dom.setFilename(originFilename);
                    if (!overrideOriFile) {
                        new File(originFilename).delete();
                    }
                    if (!(dom instanceof SchedulerDom && ((SchedulerDom) dom).isDirectory()) && !new File(filename).renameTo(new File(
                            originFilename))) {
                        ErrorLog.message("..could not rename file: " + filename, SWT.ICON_ERROR | SWT.OK);
                    }
                }
                dom.readFileLastModified();
                ErrorLog.getSShell().setText("Job Scheduler Editor [" + originFilename + "]");
            }
            return true;
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName(), e);
            ErrorLog.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
            return false;
        }
    }

    public static boolean continueAnyway(final DomParser dom) {
        if (dom.isChanged()) {
            int ok = ErrorLog.message(Messages.getString("MainListener.contentChanged"), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
            if (ok == SWT.CANCEL) {
                return false;
            } else if (ok == SWT.YES && !saveFile(dom, false)) {
                return false;
            }
        }
        return true;
    }

    public static void createBackup(final String filename) {
        String f;
        File outFile = null;
        if (filename == null) {
            f = "";
        } else {
            f = filename;
        }
        try {
            if (Options.getBackupEnabled() && !"".equals(f)) {
                if ("".equals(Options.getBackupDir())) {
                    outFile = new File(filename + ".bak");
                } else {
                    outFile = new File(Options.getBackupDir() + "/" + new File(filename).getName() + ".bak");
                }
                File inFile = new File(filename);
                if (inFile.exists()) {
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    if (!inFile.canRead()) {
                        throw new Exception("Datei " + inFile + " kann nicht gelesen werden.");
                    }
                    SOSFile.copyFile(inFile, outFile, false);
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName(), e);
        }
    }

    public static void saveXML(final Document doc, final String filename) {
        try {
            JDOMSource in = new JDOMSource(doc);
            Format format = Format.getPrettyFormat();
            XMLOutputter outp = new XMLOutputter(format);
            File f = new File(filename);
            outp.output(in.getDocument(), new FileWriter(f));
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName() + " . Could not save file " + filename, e);
        }
    }

    public static void saveXML(final String xml, final String filename) {
        try {
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = builder.build(new java.io.StringReader(xml));
            saveXML(doc, filename);
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName() + " . Could not save file " + filename, e);
        }
    }

}