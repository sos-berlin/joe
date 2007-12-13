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
	
	public final static String MASK_ORDER            = "^.*\\.order\\.xml$";
		
	private             String     xmlFilename       = "";
	
	private             String     path              = "";
	
	private             Element    config            = null;	
	
	private             String     encoding          = "ISO-8859-1";
	
	private             HashMap    listOfChanges     = null;

	/* Liste der SChreibgeschützen Dateien*/
	private             ArrayList  listOfReadOnly    = null;
	
	/* Wenn dateiname ungleich der Element Attribute Name ist, dann wird der Dateiname als name-Attribut gesetzt und * für save */
	private             ArrayList  listOfChangeElementNames = null;
	
	
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
		Element    orders          = null;
		
		listOfReadOnly = new ArrayList();
		listOfChangeElementNames = new ArrayList();
		
		try {
			SAXBuilder builder = new SAXBuilder();
			
			String xml = createConfigurationFile();
			
			parentDoc = builder.build(new StringReader(xml));
			
			root = parentDoc.getRootElement();
			if(root != null) {
				config = root.getChild("config");
			}	
						
			/*        Alle <name>.process_classes.xml parsieren     */
			addContains(processClass, "process_classes", MASK_PROCESS_CLASS);
			
			/*        Alle <name>.lock.xml parsieren     */
			addContains(locks, "locks", MASK_LOCK);
			
			/*        Alle <name>.job.xml parsieren     */
			addContains(jobs, "jobs", MASK_JOB);
			
			/*        Alle <name>.job_chain.xml parsieren     */
			addContains(jobChains, "job_chains", MASK_JOB_CHAIN);
			
			/*        Alle <name>.order.xml parsieren     */
			addContainsForOrder(orders, "commands", MASK_ORDER);
			
			//Debug Document als String ausgeben
			//printXML(parentDoc);
			
//			Document speichern
			//System.out.println("test: xmlFilename: " + xmlFilename);
			//System.out.println("parentDoc: " + Utils.getElementAsString(parentDoc.getRootElement()));
			saveXML(parentDoc, xmlFilename);
			//System.out.println("OK: ");
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
			format.setEncoding(encoding);
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
			//System.out.println("test 0 " + Utils.getElementAsString(parent) );
			//System.out.println("test 1 " + name + " path " + path);
			Vector filelist = SOSFile.getFilelist(getNormalizedFile(path).getAbsolutePath(), 
					mask,java.util.regex.Pattern.CASE_INSENSITIVE);
			//System.out.println("test 2 " + getNormalizedFile(path).getAbsolutePath());
			Iterator orderIterator = filelist.iterator();
			//System.out.println("test 3 " + filelist);
			while (orderIterator.hasNext()) {				
				String jobXMLFilename = orderIterator.next().toString();
				//System.out.println("test 4 " + jobXMLFilename);
				File jobXMLFile = new File( jobXMLFilename );
				//System.out.println("test 5 " + jobXMLFile.getName());
				currDocument = builder.build( jobXMLFile );
				//System.out.println("test 6 ");
				Element xmlRoot = currDocument.getRootElement();
				//System.out.println("test 7 ");
				if(xmlRoot != null) {
					//System.out.println("test 8");
					if(parent == null) {
						//config hat keinen Kindknoten {name}, also erzeugen
						parent = new Element(name);
						config.addContent(parent);
					}
					//System.out.println("test 9 ");
					String jobXMLNameWithoutExtension = jobXMLFile.getName().substring(0, jobXMLFile.getName().indexOf("." + xmlRoot.getName() + ".xml"));
					//System.out.println("test 10 " + jobXMLNameWithoutExtension);
					if(Utils.getAttributeValue("name", xmlRoot).length() > 0 &&
							!jobXMLNameWithoutExtension.equalsIgnoreCase(Utils.getAttributeValue("name", xmlRoot))) {
						//life Dateiname und Element-Attribute-name müssen gleich sein. Wenn dieser ungleich sind, 
						// dann umbennen wie der Dateiname
						//System.out.println("test 11 ");
						listOfChangeElementNames.add(xmlRoot.getName() + "_" + jobXMLNameWithoutExtension);
						
						//System.out.println("test 12 ");
						xmlRoot.setAttribute("name", jobXMLNameWithoutExtension);
						//System.out.println("test 13 ");
					}
					//System.out.println("test 14 ");
					if(Utils.getAttributeValue("name", xmlRoot).length() == 0) {
						//System.out.println("test 15 ");
						//In der Formular sieht man den Namen. Beim speichern soll es nicht zurückgeschrieben werden
						//listOfEmptyElementNames.add(jobXMLNameWithoutExtension);
						xmlRoot.setAttribute("name", jobXMLNameWithoutExtension);
						//System.out.println("test 16 ");
					}
					
					parent.addContent((Element)xmlRoot.clone());
					//System.out.println("test 17 ");
					if(!new File( jobXMLFilename ).canWrite()) {
						//System.out.println("test 18 ");
						listOfReadOnly.add(getChildElementName(name) + "_" + Utils.getAttributeValue("name", xmlRoot));
						//System.out.println("test 19 ");
					}
				}
				//System.out.println("test 20 ");
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("error: " + e.getMessage() );
		}
	}
	
	
	private void addContainsForOrder(Element parent, String name, String mask) {
		SAXBuilder builder = new SAXBuilder();
		Document currDocument = null;
		try {
			Vector filelist = SOSFile.getFilelist(getNormalizedFile(path).getAbsolutePath(), 
					mask,java.util.regex.Pattern.CASE_INSENSITIVE);
			Iterator orderIterator = filelist.iterator();
			while (orderIterator.hasNext()) {				
				String xmlFilename = orderIterator.next().toString();
				File xmlFile = new File( xmlFilename );
				currDocument = builder.build( xmlFile );
				Element xmlRoot = currDocument.getRootElement();
				if(xmlRoot != null) {
					if(parent == null) {
						//config hat keinen Kindknoten {name}, also erzeugen
						parent = new Element(name);
						config.addContent(parent);
					}
					//jobchainname,orderid.order.xml 
					String xmlNameWithoutExtension = xmlFile.getName().substring(0, xmlFile.getName().indexOf("." + (xmlRoot.getName().equalsIgnoreCase("add_order")? "order" : xmlRoot.getName() + ".xml")));
					String[] splitNames = xmlNameWithoutExtension.split(",");
					String jobChainname = "";
					String orderId   = "";
					if(splitNames.length == 2) {
					      jobChainname = splitNames[0];
					      orderId = splitNames[1];
					}
					
					if(Utils.getAttributeValue("job_chain", xmlRoot).length() > 0 &&
							!jobChainname.equalsIgnoreCase(Utils.getAttributeValue("job_chain", xmlRoot))) {
						//life Dateiname und Element-Attribute-name müssen gleich sein. Wenn dieser ungleich sind, 
						// dann umbennen wie der Dateiname
						//listOfChangeElementNames.add(name + "_" + xmlNameWithoutExtension);
						listOfChangeElementNames.add(xmlRoot.getName() + "_" + xmlNameWithoutExtension);
						//listOfChanges.put(name + "_" + xmlNameWithoutExtension, SchedulerDom.MODIFY);
						xmlRoot.setAttribute("job_chain", jobChainname);
					}
					
					if(Utils.getAttributeValue("id", xmlRoot).length() > 0 &&
							!orderId.equalsIgnoreCase(Utils.getAttributeValue("id", xmlRoot))) {
						//life Dateiname und Element-Attribute-name müssen gleich sein. Wenn dieser ungleich sind, 
						// dann umbennen wie der Dateiname
						//listOfChangeElementNames.add(xmlNameWithoutExtension);
						//listOfChangeElementNames.add(name + "_" + xmlNameWithoutExtension);
						listOfChangeElementNames.add(xmlRoot.getName() + "_" + xmlNameWithoutExtension);
						xmlRoot.setAttribute("id", orderId);
					}
					
					if(Utils.getAttributeValue("job_chain", xmlRoot).length() == 0) {
						//In der Formular sieht man den Namen. Beim speichern soll es nicht zurückgeschrieben werden
						//listOfEmptyElementNames.add(jobChainname);
						xmlRoot.setAttribute("job_chain", jobChainname);
					}
					
					if(Utils.getAttributeValue("id", xmlRoot).length() == 0) {
						//In der Formular sieht man den Namen. Beim speichern soll es nicht zurückgeschrieben werden
						//listOfEmptyElementNames.add(orderId);
						xmlRoot.setAttribute("id", orderId);
					}
					
					parent.addContent((Element)xmlRoot.clone());
					if(!new File( xmlFilename ).canWrite()) {
						listOfReadOnly.add(getChildElementName(name) + "_" + Utils.getAttributeValue("job_chain", xmlRoot) + "," + Utils.getAttributeValue("id", xmlRoot));
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
		Element    orders          = null;
		listOfChanges=listOfChanges_;
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
				orders       = config.getChild("commands");
				
			} 
			
			save("job", jobs);
			
			save("process_class", processClass);
			
			save("lock", locks);
			
			save("job_chain", jobChains);
			
			save("order", orders);
			
			save("add_order", orders);
			
			deleteFiles();
						
			
			listOfChanges.clear();
			
			//system.out.println("********************************************************************");
		} catch (Exception e) {
			System.out.println("..error in MergeAllXMLinDirectory.save. Could not save file " + e.getMessage());
		}
		
		
	}
	
	private void save(String name, Element elem) {
		//String filename = " ";
		List list = null; 
		
		if(elem != null) {
			list = elem.getChildren(name);
		}
		
		if (list == null || list.size() == 0) {
			return;
		}
		
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element)list.get(i);
			

			saveLifeElement(name, e);
						
		}		
	}

	public String saveLifeElement(String name, Element e, HashMap listOfChanges_, ArrayList listOfChangeElementNames_) {
		listOfChangeElementNames = listOfChangeElementNames_;
		listOfChanges = listOfChanges_;
		return saveLifeElement(name, e);
	}
	
	public String saveAsLifeElement(String name, Element e, String filename) {		
		String attrName = "";
		if(name.equals("add_order")) name = "order";
				
		if(name.equals("order")) {
			if(!filename.endsWith(".order.xml")) {
				if(new File(filename).renameTo(new File(filename + ".order.xml"))) {
					new File(filename).deleteOnExit();
				}					
				filename = filename + ".order.xml";				 
			}
			
			String[] file = new File(filename).getName().split(",");
			if(file.length==2) {
				attrName = (file.length >= 2 ? file[0] : "") + "," + (file.length >= 2 ? file[1] : "");
				attrName = attrName.substring(0, attrName.indexOf(".order.xml"));								
			} else  {
				
				attrName = Utils.getAttributeValue("job_chain", e) + "," + file[0];
				filename = filename.replaceAll(new File(filename).getName(), attrName);
				attrName = attrName.substring(0, attrName.indexOf(".order.xml"));
				
			}
			
		} else {
			if(!filename.endsWith("." + e.getName() + ".xml")) {				
				if(!new File(filename).renameTo(new File(filename + "." + e.getName() + ".xml"))) {
					new File(filename).deleteOnExit();
				}
				filename = filename + "." + e.getName() + ".xml";
			}
			String fname = new File(filename).getName();
			attrName =  fname.substring(0, fname.indexOf("."+e.getName()));
			//attrName =  fname.substring(0, fname.indexOf("."+e.getName()));
		}
		
				Element _elem = e;				
					if(name.equals("order")) {
						_elem.removeAttribute("job_chain");
						_elem.removeAttribute("id");
						_elem.removeAttribute("replace");
					} else {
						_elem.removeAttribute("name");
					}
				String xml = Utils.getElementAsString(_elem);
				saveXML(xml, filename);
				

				if(name.equals("order")) {
						Utils.setAttribute("job_chain", attrName.substring(0, attrName.indexOf(",")) , e);
					    Utils.setAttribute("id",attrName.substring(attrName.indexOf(",")+1), e);
				} else {
					   Utils.setAttribute("name", attrName, e);
				}
				return filename;
				
	}
	
	public String saveLifeElement(String name, Element e) {
		String filename = " ";
		String attrName = "";
		if(name.equals("add_order")) name = "order";
		
		//if(name.equals("order") || name.equals("add_order")) {
		if(name.equals("order")) {
			attrName =  Utils.getAttributeValue("job_chain", e) + "," +  Utils.getAttributeValue("id", e);
		} else {
			attrName =  Utils.getAttributeValue("name", e);
		}
		filename = (path.endsWith("/") || path.endsWith("\\")? path : path.concat("/")) + attrName + "." + (name.equalsIgnoreCase("add_order")? "order": name) + ".xml";
		if(listOfChanges.containsKey(name + "_" + attrName)) {				
			if(listOfChanges.get(name + "_" + attrName).equals(SchedulerDom.DELETE)) {
				if (!new File(filename).delete()) {						
					int cont = MainWindow.message(MainWindow.getSShell(), filename + " could not delete.", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				}
			} else {	
				
				Element _elem = e;
					if(name.equals("order")) {
						_elem.removeAttribute("job_chain");
						_elem.removeAttribute("id");
						_elem.removeAttribute("replace");
					} else {
						_elem.removeAttribute("name");
					}
				
					if(name.equals("job")) {
						e.removeAttribute("spooler_id");
					}
					String xml = Utils.getElementAsString(_elem);
					saveXML(xml, filename);
					
				//attribute wieder zurückschreiben zum weiterverarbeiten
				//if(name.equals("order") || name.equals("add_order")) {					  
				if(name.equals("order")) {
						Utils.setAttribute("job_chain", attrName.substring(0, attrName.indexOf(",")) , e);
					    Utils.setAttribute("id",attrName.substring(attrName.indexOf(",")+1), e);
				} else {
					   Utils.setAttribute("name", attrName, e);
				}
				
				
			}
		}
		//Element ist neu angelegt, also muss dieser auch gespeichert werden.
		if(!new File(filename).exists()) {
			String xml = Utils.getElementAsString(e);
			saveXML(xml, filename);
		}
		
		deleteFiles();
		
		
		//listOfChanges.clear();
		
		return filename;
	}
	
	private void saveXML(String xml,String filename) {		
		
		String originalFilename = filename;
		filename = filename + "~";
		
		try {
			//system.out.println("********************************************************************");
			SAXBuilder builder2 = new SAXBuilder();
			Document doc = builder2.build(new StringReader(xml));
			//test
			SchedulerDom dom = new SchedulerDom(SchedulerDom.DIRECTORY);
			
			new File(originalFilename).delete();
			
			dom.writeElement(filename, doc);
			
			if(!new File(filename).renameTo(new File(originalFilename))) {
				MainWindow.message(MainWindow.getSShell(), "could not rename file in " + filename, SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			}
		
		} catch (Exception e) {
			//System.out.println("..error in MergeAllXMLinDirectory.saveXML. Could not save file " + e.getMessage());
			MainWindow.message(MainWindow.getSShell(), "could not save file " + filename + ". cause:"+ e.getMessage(), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
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
				} else if(key.startsWith("order_")) {
				    prefix = "order_";
				}
				filename = (path.endsWith("/") || path.endsWith("\\")? path : path.concat("/")) + key.substring(prefix.length()) + "."+ prefix.substring(0, prefix.length()-1) + ".xml";
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

	public ArrayList getListOfChangeElementNames() {
		return listOfChangeElementNames;
	}


	public void setListOfChangeElementNames(ArrayList listOfChangeElementNames) {
		this.listOfChangeElementNames = listOfChangeElementNames;
	}
	
	
	
	
}
