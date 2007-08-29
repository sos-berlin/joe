package sos.scheduler.editor.app;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSFile;



public class MergeAllXMLinDirectory {
	
	public final static String MASK_JOB              = "^.*\\.job\\.xml$";
	
	public final static String MASK_LOCK             = "^.*\\.lock\\.xml$";
	
	public final static String MASK_PROCESS_CLASS    = "^.*\\.process_class\\.xml$";
	
	public final static String MASK_JOB_CHAIN        = "^.*\\.job_chain\\.xml$";
		
	private             String     xmlFilename       = "";
	
	private             String     path              = "";
	
	private             Element    config            = null;	
	
	private             String     encoding          = "ISO-8859-1";
	
	private             HashMap    listOfChanges     = null;

	private             ArrayList  listOfReadOnly    = null;
	
	/**
	 * Alle XMLDateien, die in der inputPath liegen und den Maskierungen (siehe oben) matchen,
	 *  werden in einer XML-Datei konkatiniert und in die Datei  outputfilename geschrieben.
	 * @param inputPath
	 * @param outputfilename
	 */
	public MergeAllXMLinDirectory(String path_, String outputfilename) {
		path = path_;
		xmlFilename = outputfilename;
	}
	

	public MergeAllXMLinDirectory(String path_) {
		path = path_;		
	}
	
	public MergeAllXMLinDirectory() {
				
	}
	
	public void parseDocuments() {
		
		Element    root            = null;			
		Document   parentDoc       = null;		
		Element    jobs            = null;
		Element    locks           = null;
		Element    processClass    = null;
		Element    jobChains       = null;
		
		listOfReadOnly = new ArrayList();
		
		try {
			SAXBuilder builder = new SAXBuilder();
			
			String xml = createConfigurationFile();
			
			parentDoc = builder.build(new StringReader(xml));
			
			root = parentDoc.getRootElement();
			if(root != null) {
				config = root.getChild("config");
				/*if(config != null) {					
				 processClass = config.getChild("process_classes");
				 locks        = config.getChild("locks");
				 jobs         = config.getChild("jobs");
				 jobChains    = config.getChild("job_chains");										
				 }*/
			}	
			
			/*if(root == null || config == null) {
			 System.err.println("Root Element is null.");
			 return;				
			 }*/
			
			/*        Alle <name>.process_classes.xml parsieren     */
			addContains(processClass, "process_classes", MASK_PROCESS_CLASS);
			
			/*        Alle <name>.lock.xml parsieren     */
			addContains(locks, "locks", MASK_LOCK);
			
			/*        Alle <name>.job.xml parsieren     */
			addContains(jobs, "jobs", MASK_JOB);
			
			/*        Alle <name>.job_chain.xml parsieren     */
			addContains(jobChains, "job_chains", MASK_JOB_CHAIN);
			
			//Document als String ausgeben
			//printXML(parentDoc);
			
//			Document speichern
			saveXML(parentDoc, xmlFilename);
		} catch(Exception e) {
			System.err.println("..error : " + e.getMessage());
		}	
	}
	
	public void printXML(Document doc) {		
		
		try {
			//system.out.println("********************************************************************");
			JDOMSource in = new JDOMSource(doc);
			Format format = Format.getPrettyFormat();
			//format.setEncoding(encoding);
			XMLOutputter outp = new XMLOutputter(format);					
			String output = outp.outputString(doc);
			
			//system.out.println(output);
			//system.out.println("********************************************************************");
		} catch (Exception e) {
			System.out.println("..error in MergeAllXMLinDirectory.printXML. Could not save file " + e.getMessage());
		}
		
		
	}
	
	public void saveXML(Document doc,String filename) {		
		
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
			//System.out.println("..error in MergeAllXMLinDirectory.saveXML. Could not save file " + e.getMessage());
			MainWindow.message("Could not save file " + e.getMessage(), SWT.ICON_ERROR);
		}
		
		
	}
	
	protected File getNormalizedFile(String url) throws Exception {
		try {
			if (url.startsWith("file")) {
				return new java.io.File(java.net.URI.create(url));
			} else {
				return new java.io.File(url);
			}
		} catch (Exception e) {
			throw new Exception("-> ..error : " + e);
		}
	}
	
	private void addContains(Element parent, String name, String mask) {
		SAXBuilder builder = new SAXBuilder();
		Document currDocument = null;
		try {
			Vector filelist = SOSFile.getFilelist(getNormalizedFile(path).getAbsolutePath(), 
					mask,java.util.regex.Pattern.CASE_INSENSITIVE);
			Iterator orderIterator = filelist.iterator();
			while (orderIterator.hasNext()) {				
				String jobXMLFilename = orderIterator.next().toString();				
				currDocument = builder.build( new File( jobXMLFilename ) );
				Element xmlRoot = currDocument.getRootElement();
				if(xmlRoot != null) {
					if(parent == null) {
						//config hat keinen Kindknoten {name}, also erzeugen
						parent = new Element(name);
						config.addContent(parent);
					}
					parent.addContent((Element)xmlRoot.clone());
					if(!new File( jobXMLFilename ).canWrite()) {
						listOfReadOnly.add(getChildElementName(name) + "_" + Utils.getAttributeValue("name", xmlRoot));
					}
				}
				
			}
			
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
		}
	}
	
	private String getChildElementName(String pName) {
		if(pName.equals("jobs")) {
			return "job";
		} else if(pName.equals("process_classes")) {
			return "process_class";
		} else if(pName.equals("locks")) {
			return "lock";
		} else if(pName.equals("job_chains")) {
			return "job_chain";
		} else{
			return pName;
		}
		
	}
	
	private String  createConfigurationFile() {
		String xml = "<?xml version=\"1.0\" encoding=\""+ encoding + "\"?> ";
		
		try {
			
			xml = xml + "<spooler>  " +
			"      <config> " +
			"      </config> " +
			"    </spooler>";			
			
		} catch (Exception e) {
			System.out.println("..error in MergeAllXMLinDirectory.createConfigurationFile(). Could not create a new configuration file: " + e.getMessage());
		}
		return xml;
	}
	
	/**
	 * Speichert das Dokument in die einzelnen Dateien wieder zurück
	 */
	public void saveXMLDirectory(Document doc, HashMap listOfChanges_) {		
		Element    jobs            = null; 
		Element    locks           = null;
		Element    processClass    = null;
		Element    jobChains       = null;
		listOfChanges = listOfChanges_;
		try {
			//system.out.println("********************************************************************");
			Element root = doc.getRootElement();
			if(root != null) {
				config = root.getChild("config");
			}
			if(config != null) {					
				jobs         = config.getChild("jobs");
				processClass = config.getChild("process_classes");
				locks        = config.getChild("locks");				
				jobChains    = config.getChild("job_chains");										
			}
			
			save("job", jobs);
			
			save("process_class", processClass);
			
			save("lock", locks);
			
			save("job_chain", jobChains);
			
			deleteFiles();
						
			
			listOfChanges.clear();
			//system.out.println("********************************************************************");
		} catch (Exception e) {
			System.out.println("..error in MergeAllXMLinDirectory.save. Could not save file " + e.getMessage());
		}
		
		
	}
	
	private void save(String name, Element elem) {
		String filename = " ";
		List list = null; 
		String outputPath = "";
		
		if(elem != null) {
			list = elem.getChildren(name);
		}
		
		if (list == null || list.size() == 0) {
			return;
		}
		
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element)list.get(i);
			filename = (path.endsWith("/") || path.endsWith("\\")? path : path.concat("/")) + Utils.getAttributeValue("name", e) + "." + name + ".xml";
			if(listOfChanges.containsKey(name + "_" + Utils.getAttributeValue("name", e))) {				
				if(listOfChanges.get(name + "_" + Utils.getAttributeValue("name", e)).equals(SchedulerDom.DELETE)) {
					if (!new File(filename).delete()) {						
						int cont = MainWindow.message(MainWindow.getSShell(), filename + " could not delete.", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					}
				} else {								
					String xml = Utils.getElementAsString(e);
					saveXML(xml, filename);
					
				}
			}
			//Element ist neu angelegt, also muss dieser auch gespeichert werden.
			if(!new File(filename).exists()) {
				String xml = Utils.getElementAsString(e);
				saveXML(xml, filename);
			}
		}		
	}
	
	private void saveXML(String xml,String filename) {		
		FileWriter fw = null;
		try {
			//system.out.println("********************************************************************");
			SAXBuilder builder2 = new SAXBuilder();
			Document doc = builder2.build(new StringReader(xml));
			//test
			SchedulerDom dom = new SchedulerDom(SchedulerDom.DIRECTORY);
			dom.writeElement(filename, doc);
			
			//ende test
			
			
			/*JDOMSource in = new JDOMSource(doc);
			Format format = Format.getPrettyFormat();
			//format.setEncoding(encoding);
			XMLOutputter outp = new XMLOutputter(format);					
			//String output = outp.outputString(doc);
			
			File f = new File(filename);
			fw = new FileWriter(f);
			outp.output(in.getDocument(), fw);
			//system.out.println("xml datei wurde gespeichert: " + f.getCanonicalPath());
			//system.out.println("********************************************************************");
			 * 
			 */
		} catch (Exception e) {
			System.out.println("..error in MergeAllXMLinDirectory.saveXML. Could not save file " + e.getMessage());
			MainWindow.message(MainWindow.getSShell(), "could not save file " + filename + ". cause:"+ e.getMessage(), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		} finally {
			if(fw != null) {
				try {
				fw.close();
				} catch (Exception e) {
					System.out.println("..error in MergeAllXMLinDirectory.saveXML. Could not close FileWriter " + e.getMessage());
				}
			}
		}
		
		
	}
	
	private void deleteFiles() {
		String filename = "";
		String prefix   ="";
		Iterator keys1 = listOfChanges.keySet().iterator();         
		Iterator values1 = listOfChanges.values().iterator();
		while (keys1.hasNext()) {
			String key = keys1.next().toString();
			Object oVal = values1.next();
			String val = (oVal!= null ? oVal.toString(): "");
			if(val.equals(SchedulerDom.DELETE)) {
				if(key.startsWith("job_chain_")) {
					prefix = "job_chain_";
				} else if(key.startsWith("job_")) {
					prefix = "job_";
				} else if(key.startsWith("process_class_")) {
					prefix = "process_class_";
				} else if(key.startsWith("lock_")) {
					prefix = "lock_";
				} 
				
				filename = (path.endsWith("/") || path.endsWith("\\")? path : path.concat("\\")) + key.substring(prefix.length()) + "."+ prefix.substring(0, prefix.length()-1) + ".xml";
				File f = new File(filename);
				if(f.exists()) {
					if (!f.delete()) {	
						//job5.job.xml
						int cont = MainWindow.message(MainWindow.getSShell(), filename + " could not delete.", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
					} else {
						//system.out.println(filename + " wurde gelöscht.");
					}
				}
			}
			
		}
	}
	
	public String getJobname(String filename) {
		String jobname = "";
		try {
		SAXBuilder builder = new SAXBuilder();
		
		String xml = createConfigurationFile();
		
		Document doc = builder.build(new File(filename));
		
		Element root = doc.getRootElement();
		
		jobname = Utils.getAttributeValue("name", root);
		
		} catch (Exception e) {
			MainWindow.message(".. could not get jobname from " + filename + " cause: " + e.getMessage(), SWT.ICON_ERROR);
		}
		return jobname;
	}
	
	public static void main(String[] args) {
		MergeAllXMLinDirectory allJob = new MergeAllXMLinDirectory("C:/scheduler/config/temp", "C:/scheduler/config/temp/config.xml");
		allJob.parseDocuments();
	}


	public ArrayList getListOfReadOnly() {
		return listOfReadOnly;
	}
	
	
	
	
}
