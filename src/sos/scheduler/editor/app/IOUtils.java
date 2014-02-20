package sos.scheduler.editor.app;
import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSFile;

import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.jobdoc.DocumentationDom;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.SchedulerHotFolderFileList;
import com.sos.scheduler.model.SchedulerObjectFactory;

public class IOUtils {
	@SuppressWarnings("unused")
	private final String		conClsName		= "IOUtils";
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id$";
	private static final Logger	logger			= Logger.getLogger(IOUtils.class);

//	public static boolean openFile(DomParser dom) {
//		return openFile(null, null, dom);
//	}
//
//	public static boolean openFile(Collection filenames, DomParser dom) {
//		return openFile(null, filenames, dom);
//	}

	public static String getJobschedulerObjectPathName(final String mask) {
		return getJobSchedulerObjectPathFromFileSystem(mask, Options.getSchedulerHotFolder());
	}

	public static String getJobSchedulerObjectPathFromFileSystem(final String mask, final String pstrDirectoryName) {

		String filename = "";
		FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);

		fdialog.setFilterPath(pstrDirectoryName);
		String filterMask = mask.replaceAll("\\\\", "");
		filterMask = filterMask.replaceAll("\\^.", "");
		filterMask = filterMask.replaceAll("\\$", "");
		fdialog.setFilterExtensions(new String[] { filterMask });

		filename = fdialog.open();
		if (filename == null || filename.trim().length() == 0) {
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


	private static String getDomInstance(final DomParser dom) {
		if (dom instanceof SchedulerDom)
			return " Scheduler";
		else
			if (dom instanceof DocumentationDom)
				return " Documentation";
			else
				return "";
	}

	public static String openDirectoryFile(final String mask) {
		return openDirectoryFile(mask, Options.getLastDirectory());
	}

	public static String openDirectoryFile(final String mask, final String pstrDirectoryName) {

		String filename = "";
		FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);

		fdialog.setFilterPath(pstrDirectoryName);
		String filterMask = mask.replaceAll("\\\\", "");
		filterMask = filterMask.replaceAll("\\^.", "");
		filterMask = filterMask.replaceAll("\\$", "");
		fdialog.setFilterExtensions(new String[] { filterMask });

		filename = fdialog.open();
		if (filename == null || filename.trim().length() == 0) {
			return filename;
		}
		filename = filename.replaceAll("\\\\", "/");

		String env = Options.getSchedulerHotFolder().replaceAll("\\\\", "/");

		int pos = filename.toLowerCase().indexOf(env.toLowerCase().toLowerCase());
		if (pos >= 0) {
			File fleT = new File (filename);
			filename = fleT.getName();
//			int add = (env.endsWith("/") ? -1 : 0);
//			filename = filename.substring(pos == -1 ? 0 : pos + env.length() + add);
//			filename = filename.substring(0, filename.length() - filterMask.length() + 1);
		}
		return filename;

	}

	/**
	 *
	 * Es wird entweder eine Scheduler Konfigurationsdatei, eine Hot Folder Verzeichnis oder
	 * ein Hot Folder Datei geöffnet.
	 *
	 * Beim erfolgreichen Öffnet wird true, sansonsten flase geliefert.
	 *
	 * @param filename String
	 * @param filenames java.util.Collection
	 * @param dom DomParser
	 * @return boolean
	 *
	 *
	 */
	public static boolean openFile(String filename, SchedulerHotFolder pobjHotFolder) {
		try {
			boolean isDirectory = true;
			if (!isDirectory && (filename == null || filename.equals(""))) {
				FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);
				fdialog.setFilterPath(Options.getLastDirectory());
				filename = fdialog.open();
			}
			if (isDirectory) {
				pobjHotFolder = null;
				String strHotFolderPathName = filename;
				if (filename == null || filename.length() == 0) {
					DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
					fdialog.setFilterPath(Options.getLastDirectory());
					fdialog.setText("Open HotFolder directory ...");
					String fname = fdialog.open();
					if (fname == null) {
						return false;
					}
					strHotFolderPathName = fname;
				}

				ISOSVFSHandler					objVFS					= null;
				ISOSVfsFileTransfer				objFileSystemHandler	= null;

				try {
					objVFS = VFSFactory.getHandler("local");
					objFileSystemHandler = (ISOSVfsFileTransfer) objVFS;
				}
				catch (Exception e) {
					logger.error(e);
					return false;
				}

				SchedulerObjectFactory	objFactory				= null;
				objFactory = new SchedulerObjectFactory();

				ISOSVirtualFile objHotFolder = objFileSystemHandler.getFileHandle(strHotFolderPathName);
				SchedulerHotFolder objSchedulerHotFolder = objFactory.createSchedulerHotFolder(objHotFolder);
				logger.info(String.format("... load %1$s", strHotFolderPathName));
				SchedulerHotFolderFileList objSchedulerHotFolderFileList = objSchedulerHotFolder.load();
				objSchedulerHotFolderFileList.getFolderList();
				objSchedulerHotFolderFileList.getJobList();
				objSchedulerHotFolderFileList.getJobChainList();
				objSchedulerHotFolderFileList.getOrderList();
				objSchedulerHotFolderFileList.getProcessClassList();
				objSchedulerHotFolderFileList.getLockList();
				objSchedulerHotFolderFileList.getScheduleList();
				objSchedulerHotFolderFileList.getParamsList();

				pobjHotFolder = objSchedulerHotFolder;
			}
			MainWindow.getSShell().setText("JOE (JobScheduler Object Editor) [" + filename + "]");
			return true;
		}
		catch (Exception e) {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
		}
		finally {
		}
		return false;
	}


//	@Deprecated
	public static SchedulerHotFolder openHotFolder(final String filename) {
		SchedulerHotFolder objSchedulerHotFolder = null;
		try {
			boolean isDirectory = true;

			if (isDirectory) {
				String strHotFolderPathName = filename;
				if (filename == null || filename.length() == 0) {
					DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
					fdialog.setFilterPath(Options.getLastDirectory());
					fdialog.setText("Open HotFolder directory ...");
					String fname = fdialog.open();
					if (fname == null) {
						return null;
					}
					strHotFolderPathName = fname;
				}

				ISOSVFSHandler					objVFS					= null;
				ISOSVfsFileTransfer				objFileSystemHandler	= null;

				try {
					objVFS = VFSFactory.getHandler("local");
					objFileSystemHandler = (ISOSVfsFileTransfer) objVFS;
				}
				catch (Exception e) {
					logger.error(e);
					return null;
				}

				SchedulerObjectFactory	objFactory				= null;
				objFactory = new SchedulerObjectFactory();

				ISOSVirtualFile objHotFolder = objFileSystemHandler.getFileHandle(strHotFolderPathName);
				objSchedulerHotFolder = objFactory.createSchedulerHotFolder(objHotFolder);
				logger.info(String.format("... load %1$s", strHotFolderPathName));
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
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
			}
			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
		}
		finally {
		}
		return objSchedulerHotFolder;
	}

	public static boolean saveFile(final DomParser dom, boolean saveas) {
		String filename = dom.getFilename();
		String originFilename = null;
		boolean overrideOriFile = saveas;
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
			originFilename = filename;
			filename = filename + "~";
			File file = new File(filename);
			Options.setLastDirectory(file, dom);
			// overwrite the new file if it exists?
			if (saveas && new File(originFilename).exists()) {
				int ok = MainWindow.message(Messages.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				if (ok == SWT.NO)
					return false;
				else
					overrideOriFile = false;
			}
			if (!file.exists()) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {// Verzeichnis existier nicht.
					int c = MainWindow.message("This Directory does not exist. Should it be created now?", SWT.YES | SWT.NO | SWT.ICON_WARNING);
					if (c == SWT.YES) {
						if (!file.getParentFile().mkdirs()) {
							MainWindow.message("Could not create Directory: " + file.getParent(), SWT.ICON_WARNING | SWT.OK);
							return false;
						}
					}
					else
						return false;
				}
				if (!file.createNewFile()) {
					MainWindow.message("Could not save file", SWT.ICON_WARNING | SWT.OK);
					return false;
				}
			}
			if (!file.canWrite()) {
				MainWindow.message(Messages.getString("MainListener.fileWriteProtected"), //$NON-NLS-1$
						SWT.ICON_WARNING | SWT.OK);
				return false;
			}
			else {
				if (!saveas) {
					createBackup(filename);
				}
				if (dom instanceof SchedulerDom && ((SchedulerDom) dom).isLifeElement()) {
					//
					MergeAllXMLinDirectory save = new MergeAllXMLinDirectory(file.getParent());
					originFilename = save.saveAsLifeElement(dom.getRoot().getName(), dom.getRoot(), originFilename);
					try {
						dom.setFilename(new java.io.File(originFilename).getCanonicalPath());
					}
					catch (Exception e) {
					}
					dom.setChanged(false);
					new File(filename).delete();
				}
				else {
					dom.write(filename);
					dom.setFilename(originFilename);
					if (!overrideOriFile) {
						new File(originFilename).delete();
					}
					if (!(dom instanceof SchedulerDom && ((SchedulerDom) dom).isDirectory()) && !new File(filename).renameTo(new File(originFilename))) {
						MainWindow.message("..could not rename file: " + filename, SWT.ICON_ERROR | SWT.OK);
					}
				}
				dom.readFileLastModified();
				MainWindow.getSShell().setText("JOE (JobScheduler Object Editor) [" + originFilename + "]");
			}
			return true;
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
			return false;
		}
	}

	public static boolean continueAnyway(final DomParser dom) {
		if (dom.isChanged()) {
			int ok = MainWindow.message(Messages.getString("MainListener.contentChanged"), //$NON-NLS-1$
					SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
			if (ok == SWT.CANCEL)
				return false;
			else
				if (ok == SWT.YES && !saveFile(dom, false))
					return false;
		}
		return true;
	}

	public static void createBackup(final String filename) {
		String f;
		File outFile = null;
		if (filename == null) {
			f = "";
		}
		else {
			f = filename;
		}
		try {
			if (Options.getBackupEnabled() && !f.equals("")) {
				if (Options.getBackupDir().equals("")) {
					outFile = new File(filename + ".bak");
				}
				else {
					outFile = new File(Options.getBackupDir() + "/" + new File(filename).getName() + ".bak");
				}
				File inFile = new File(filename);
				if (inFile.exists()) {
					if (outFile.exists()) { // Wenn destination schon existiert,
						// dann vorher lï¿½schen.
						outFile.delete();
					}
					if (!inFile.canRead())
						throw new Exception("Datei " + inFile + " kann nicht gelesen werden.");
					SOSFile.copyFile(inFile, outFile, false);
				}
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
		}
	}

	public static boolean saveAction(final DomParser dom, final boolean saveas) {
		try {
			if (dom.getFilename() == null || saveas) {
				// sos.scheduler.editor.actions.forms.SaveEventsDialogForm d= new sos.scheduler.editor.actions.forms.SaveEventsDialogForm();
				new sos.scheduler.editor.actions.forms.SaveEventsDialogForm();
				if (dom.getFilename() == null)// Cancel
					return false;
			}
			saveFile(dom, false);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not save directory.", e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
		return true;
	}

	/**
	 * Speichert das Dokument in die einzelnen Dateien (als HOT FOLDER ELEMENT) wieder zurück
	 */
	public static boolean saveDirectory(final DomParser dom, final boolean saveas, final int type, final String nameOfElement, final IContainer container) {
		Document currDoc = dom.getDoc();
		File configFile = null;
		try {
			if (dom.getFilename() == null || saveas) {
				DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
				fdialog.setFilterPath(Options.getLastDirectory());
				fdialog.setText("Save object to hot folder ...");
				String path = fdialog.open();
				if (path == null) {
					return false;
				}
				File _file = null;
				// existiert der Hot Folder Element
				if (dom.getRoot().getName().equals("order") || dom.getRoot().getName().equals("add_order")) {
					_file = new File(path + "//" + Utils.getAttributeValue("job_chain", dom.getRoot()) + "," + Utils.getAttributeValue("id", dom.getRoot())
							+ ".order.xml");
				}
				else {
					_file = new File(path + "//" + Utils.getAttributeValue("name", dom.getRoot()) + "." + dom.getRoot().getName() + ".xml");
				}
				if (_file.exists()) {
					int ok = MainWindow.message(Messages.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
							SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (ok == SWT.NO)
						return false;
				}
				configFile = new File(path);
			}
			else {
				configFile = new File(dom.getFilename());
			}
			MergeAllXMLinDirectory save = null;
			if (dom instanceof SchedulerDom && ((SchedulerDom) dom).isLifeElement() && configFile.isFile())
				save = new MergeAllXMLinDirectory(configFile.getParent());
			else
				save = new MergeAllXMLinDirectory(configFile.getPath());
			if (type == SchedulerDom.DIRECTORY) {
				save.saveXMLDirectory(currDoc, ((SchedulerDom) dom).getChangedJob());
			}
			else {// sonst life element
				org.jdom.Element elem = null;
				if (type == SchedulerDom.LIFE_LOCK) {
					sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm) MainWindow.getContainer().getCurrentEditor();
					org.eclipse.swt.widgets.Tree tree = form.getTree();
					TreeData data = (TreeData) tree.getSelection()[0].getData();
					elem = data.getElement().getChild("locks").getChild("lock");
				}
				else
					if (type == SchedulerDom.LIFE_PROCESS_CLASS) {
						sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm) MainWindow.getContainer().getCurrentEditor();
						org.eclipse.swt.widgets.Tree tree = form.getTree();
						TreeData data = (TreeData) tree.getSelection()[0].getData();
						elem = data.getElement().getChild("process_classes").getChild("process_class");
					}
					else {
						elem = currDoc.getRootElement();
					}
				String name = save.saveLifeElement(nameOfElement, elem, ((SchedulerDom) dom).getChangedJob(),
						((SchedulerDom) dom).getListOfChangeElementNames());
				Options.setLastDirectory(new File(name), dom);
				try {
					dom.setFilename(new java.io.File(name).getCanonicalPath());
				}
				catch (Exception e) {
				}
				dom.setChanged(true);
			}
			dom.readFileLastModified();
			dom.setChanged(false);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not save directory.", e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
		return true;
	}

	public static void saveXML(final Document doc, final String filename) {
		try {
			// system.out.println("********************************************************************");
			JDOMSource in = new JDOMSource(doc);
			Format format = Format.getPrettyFormat();
			// format.setEncoding(encoding);
			XMLOutputter outp = new XMLOutputter(format);
			File f = new File(filename);
			outp.output(in.getDocument(), new FileWriter(f));
			// system.out.println("xml datei wurde gespeichert: " + f.getCanonicalPath());
			// system.out.println("********************************************************************");
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " .Could not save file " + filename, e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message("Could not save file " + e.getMessage(), SWT.ICON_ERROR);
		}
	}

	public static void saveXML(final String xml, final String filename) {
		try {
			org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
			org.jdom.Document doc = builder.build(new java.io.StringReader(xml));
			saveXML(doc, filename);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " .Could not save file " + filename, e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message("Could not save file " + e.getMessage(), SWT.ICON_ERROR);
		}
	}
}
