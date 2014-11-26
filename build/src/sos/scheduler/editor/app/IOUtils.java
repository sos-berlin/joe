package sos.scheduler.editor.app;

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

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.util.SOSFile;


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


	/**
	 * öffnet
	 */
	public static String openDirectoryFile(String mask) {
		String filename = "";

		FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);    	     	



		fdialog.setFilterPath(Options.getLastDirectory());
		String filterMask = mask.replaceAll("\\\\", "");
		filterMask = filterMask.replaceAll("\\^.", "");
		filterMask = filterMask.replaceAll("\\$", "");

		fdialog.setFilterExtensions(new String[]{filterMask});

		filename = fdialog.open();  

		if(filename == null || filename.trim().length() == 0)
			return filename;


		filename = filename.replaceAll("\\\\", "/");
		String env = Options.getSchedulerHotFolder().replaceAll("\\\\", "/");
		int pos = filename.toLowerCase().indexOf(env.toLowerCase().toLowerCase());
		int add = (env.endsWith("/") ? -1 : 0 );
		filename = filename.substring(pos == -1 ? 0 : pos + env.length() + add) ;
		filename = filename.substring(0, filename.length() - filterMask.length() +1);
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
	public static boolean openFile(String filename, Collection filenames, DomParser dom) {    	
		try {
			boolean isDirectory = dom instanceof SchedulerDom &&  ((SchedulerDom)dom).isDirectory();
			String xml =null;
			// open file dialog            
			if (!isDirectory && (filename == null || filename.equals(""))) {
				FileDialog fdialog = new FileDialog(MainWindow.getSShell(), SWT.OPEN);            	            	
				fdialog.setFilterPath(Options.getLastDirectory());
				fdialog.setText("Open" + getDomInstance(dom) + " File");                
				filename = fdialog.open();                
			}

			// open Directory
			//if (filename != null && filename.equals("#xml#")) {
			if(isDirectory) {

				String path = filename;
				if(filename == null || filename.length() == 0) {
					DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
					fdialog.setFilterPath(Options.getLastDirectory());

					fdialog.setText("Open" + getDomInstance(dom) + " File");

					String fname = fdialog.open();
					if (fname == null)
						return false;
					//path = fdialog.getFilterPath();
					path = fname;

				}

				//File tempFile = File.createTempFile("#xml#.config.", ".xml~", new File(path) );

				//tempFile.deleteOnExit();

				//temporäre config.xml bilden
				MergeAllXMLinDirectory allJob = new MergeAllXMLinDirectory(path);
				xml = allJob.parseDocuments();
				//schreibgeschützte Dateien merken. Diese Elemente werden anders farbig dargestellt und können nicht verändert werden
				((SchedulerDom)dom).setListOfReadOnlyFiles(allJob.getListOfReadOnly());

				//life Dateiname und Element-Attribute-name sind nicht gleich. Attributname wird automatisch
				//verändert und das Dokument auf geändert markiert
				if(allJob.getListOfChangeElementNames()!= null && allJob.getListOfChangeElementNames().size() > 0)
					((SchedulerDom)dom).setListOfChangeElementNames(allJob.getListOfChangeElementNames());
				//tempFile.delete();                 
				//filename= tempFile.getCanonicalPath();
				filename = path;
				if (filename == null)
					return false;            	
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
				////System.out.println("~~~~~~~~~~~~~~~~~filename ioutils: " + filename);
				// check the file
				if (!file.exists())  {
					////System.out.println("~~~~~~~~~~~~~~~~~filename ioutils not exist: " + file.getCanonicalPath());
					MainWindow.message(Messages.getString("MainListener.fileNotFound"), //$NON-NLS-1$
							SWT.ICON_WARNING | SWT.OK);                    
					return false;
				} else if (!file.canRead() )
					MainWindow.message(Messages.getString("MainListener.fileReadProtected"), //$NON-NLS-1$
							SWT.ICON_WARNING | SWT.OK);
				else { // open it...
					int cont = SWT.NO;
					try {
						// read and validate   
						if(isDirectory) {
							dom.readString(xml, true);
							dom.setFilename(filename);
						}else
							dom.read(filename);
					} catch (JDOMException e) {

						cont = MainWindow.message(Messages.getString("MainListener.validationError", new String[] {
								file.getAbsolutePath(), e.getMessage() }), SWT.ICON_WARNING | SWT.YES | SWT.NO);
						if (cont == SWT.NO)
							return false;
					} catch (IOException e) {

						try {
							new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
						} catch(Exception ee) {
							//tu nichts
						}

						MainWindow.message(Messages.getString("MainListener.errorReadingFile", new String[] {
								file.getAbsolutePath(), e.getMessage() }), SWT.ICON_ERROR | SWT.OK);
						return false;
					}

					if (cont == SWT.YES) { // validation error, continue
						// anyway...
						if(isDirectory) {
							dom.readString(xml, false);
							dom.setFilename(filename);
						} else if (!dom.read(filename, false)) { // unknown file
							MainWindow
							.message(Messages.getString("MainListener.unknownXML"), SWT.ICON_WARNING | SWT.OK);
							return false;
						}
					}
				}
			} else
				return false;

			MainWindow.getSShell().setText("Job Scheduler Editor [" + filename + "]");

			Options.setLastDirectory(new File(filename), dom);
			return true;
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
		} finally {
			if(filename != null && new File(filename).getName().startsWith("#xml#.config.")) {
				new java.io.File(filename).delete();
			}
		}
		return false;
	}


	
	
	public static boolean saveFile(DomParser dom, boolean saveas) {
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
				if(file.getParentFile() != null && !file.getParentFile().exists()) {//Verzeichnis existier nicht.
					int c = MainWindow.message("This Directory does not exist. Should it be created now?", SWT.YES | SWT.NO | SWT.ICON_WARNING );
					if(c == SWT.YES) {
						if(!file.getParentFile().mkdirs()) {
							MainWindow.message("Could not create Directory: " + file.getParent(), SWT.ICON_WARNING | SWT.OK);
							return false;
						}
					} else
						return false;
				}
				if(!file.createNewFile()) {
					MainWindow.message("Could not save file", SWT.ICON_WARNING | SWT.OK);
						return false;
				}
			}


			if (!file.canWrite()) {
				MainWindow.message(Messages.getString("MainListener.fileWriteProtected"), //$NON-NLS-1$
						SWT.ICON_WARNING | SWT.OK);
				return false;
			} else {
				if (!saveas) {
					createBackup(filename);                    
				}
				if(dom instanceof SchedulerDom &&  ((SchedulerDom)dom).isLifeElement()) {
					//
					MergeAllXMLinDirectory save = new MergeAllXMLinDirectory(file.getParent());                	                	

					originFilename= save.saveAsLifeElement(dom.getRoot().getName(), dom.getRoot(), originFilename);

					try {
						dom.setFilename(new java.io.File(originFilename).getCanonicalPath()); 
					} catch(Exception e) {}

					dom.setChanged(false);

					new File(filename).delete();   

				} else {
					dom.write(filename);
					dom.setFilename(originFilename);

					if(!overrideOriFile) {
						new File(originFilename).delete();                			
					}
					

					if(!(dom instanceof SchedulerDom &&  ((SchedulerDom)dom).isDirectory()) 
							&& !new File(filename).renameTo(new File(originFilename))) {                   			
						MainWindow.message("..could not rename file: " + filename, SWT.ICON_ERROR | SWT.OK);
					}
					

				}
				dom.readFileLastModified();

				MainWindow.getSShell().setText("Job Scheduler Editor [" + originFilename  + "]");


			}

			return true;
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

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
						// dann vorher lï¿½schen.
						outFile.delete();
					}

					if (!inFile.canRead())
						throw new Exception("Datei " + inFile + " kann nicht gelesen werden.");

					SOSFile.copyFile(inFile, outFile, false);
				}
			}
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
		}
	}

	public static boolean saveAction(DomParser dom, boolean saveas) {
		
		
		try {
			if(dom.getFilename() == null || saveas) {

				//sos.scheduler.editor.actions.forms.SaveEventsDialogForm d= new sos.scheduler.editor.actions.forms.SaveEventsDialogForm();
				new sos.scheduler.editor.actions.forms.SaveEventsDialogForm();
				if(dom.getFilename() == null)//Cancel
					return false;
								
			} 
			saveFile(dom , false);
			

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not save directory." , e);
			} catch(Exception ee) {
				//tu nichts
			}
		}

		return true;

	}


	/**
	 * Speichert das Dokument in die einzelnen Dateien (als HOT FOLDER ELEMENT) wieder zurück
	 */
	public static boolean saveDirectory(DomParser dom, boolean saveas, int type, String nameOfElement, IContainer container) {
		Document currDoc = dom.getDoc();
		File configFile = null;
		try {
			if(dom.getFilename() == null || saveas) {

				DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
				fdialog.setFilterPath(Options.getLastDirectory());
				fdialog.setText("Save object to hot folder ...");

				String path = fdialog.open();    		    			

				if(path == null) {
					return false;
				}  
				File _file = null;
				//existiert der Hot Folder Element
				if(dom.getRoot().getName().equals("order") || dom.getRoot().getName().equals("add_order") ) {
					_file = new File(path + "//" + Utils.getAttributeValue("job_chain", dom.getRoot()) + ","+ Utils.getAttributeValue("id", dom.getRoot()) + ".order.xml");	
				} else  {
					_file = new File(path + "//" + Utils.getAttributeValue("name", dom.getRoot()) + "." + dom.getRoot().getName() + ".xml" );
				}    
				if(_file.exists()) {
					int ok = MainWindow.message(Messages.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
							SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					if (ok == SWT.NO)
						return false;
				}

				configFile = new File(path);

			} else {

				configFile = new File(dom.getFilename());

			}

			MergeAllXMLinDirectory save = null;
			if(dom instanceof SchedulerDom &&  ((SchedulerDom)dom).isLifeElement() && configFile.isFile())
				save = new MergeAllXMLinDirectory(configFile.getParent());
			else
				save = new MergeAllXMLinDirectory(configFile.getPath());

			if(type == SchedulerDom.DIRECTORY) {    			
				save.saveXMLDirectory(currDoc, ((SchedulerDom)dom).getChangedJob());

			} else {//sonst life element
				org.jdom.Element elem = null;
				if(type == SchedulerDom.LIFE_LOCK) {

					sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm)(MainWindow.getContainer().getCurrentEditor());
					org.eclipse.swt.widgets.Tree tree = form.getTree();
					TreeData data = (TreeData)tree.getSelection()[0].getData();
					elem = data.getElement().getChild("locks").getChild("lock");

				} else if (type == SchedulerDom.LIFE_PROCESS_CLASS) {

					sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm)(MainWindow.getContainer().getCurrentEditor());
					org.eclipse.swt.widgets.Tree tree = form.getTree();
					TreeData data = (TreeData)tree.getSelection()[0].getData();
					elem = data.getElement().getChild("process_classes").getChild("process_class");

				} else {
					elem = currDoc.getRootElement();
				}

				String name = save.saveLifeElement(nameOfElement, elem, ((SchedulerDom)dom).getChangedJob(),((SchedulerDom)dom).getListOfChangeElementNames());
				Options.setLastDirectory(new File(name), dom);
				try {dom.setFilename(new java.io.File(name).getCanonicalPath()); } catch(Exception e) {}
				dom.setChanged(true);

			}
			dom.readFileLastModified();
			dom.setChanged(false); 

		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not save directory." , e);
			} catch(Exception ee) {
				//tu nichts
			}
		}

		return true;

	}

	public static void saveXML(Document doc,String filename) {		

		try {
			//system.out.println("********************************************************************");
			JDOMSource in = new JDOMSource(doc);
			Format format = Format.getPrettyFormat();
			//format.setEncoding(encoding);			
			XMLOutputter outp = new XMLOutputter(format);					
			File f = new File(filename);
			outp.output(in.getDocument(), new FileWriter(f));			
			//system.out.println("xml datei wurde gespeichert: " + f.getCanonicalPath());
			//system.out.println("********************************************************************");
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " .Could not save file " + filename , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("Could not save file " + e.getMessage(), SWT.ICON_ERROR);
		}

	}

	public static void saveXML(String xml, String filename){
		try {
			
			org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();					
			org.jdom.Document doc = builder.build(new java.io.StringReader(xml));
			
			saveXML(doc, filename);
			
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " .Could not save file " + filename , e);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message("Could not save file " + e.getMessage(), SWT.ICON_ERROR);
		}

	}

}
