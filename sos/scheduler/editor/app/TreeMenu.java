package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.jdom.JDOMException;
import java.util.ArrayList;
import java.util.List;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;

public class TreeMenu {

	private DomParser               _dom                  = null;

	private Tree                    _tree                 = null;

	private Menu                    _menu                 = null;

	//private Clipboard               _cb                   = null;

	private static Element          _copy                 = null;

	private static int              _type                 = -1; 

	private SchedulerForm           _gui                  = null;

	private static final String EDIT_XML                  = "Edit XML";

	private static final String SHOW_XML                  = "Show XML";

	private static final String SHOW_INFO                 = "Show Info";

	private static final String COPY                      = "Copy";

	private static final String COPY_TO_CLIPBOARD         = "Copy to Clipboard";

	private static final String PASTE                     = "Paste";

	private static final String DELETE                     = "Delete";


	public TreeMenu(Tree tree, DomParser dom, SchedulerForm gui) {
		_tree = tree;
		_dom = dom; 
		_gui = gui;
		createMenu();
	}


	public int message(String message, int style) {
		MessageBox mb = new MessageBox(_tree.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}


	private Element getElement() {
		if (_tree.getSelectionCount() > 0) {	
			Element retVal = (Element) _tree.getSelection()[0].getData("copy_element"); 
			if(retVal == null) {
				try {
					new sos.scheduler.editor.app.ErrorLog(_tree.getSelection()[0].getText() + " need copy_element." + sos.util.SOSClassUtil.getMethodName());
				} catch(Exception ee) {
					//tu nichts
				}
				return null;
			}
			return (Element)retVal.clone();
		}

		return null;

	}

	/*
	private Element getElement() {
		if (_tree.getSelectionCount() > 0) {			
			TreeData data = (TreeData) _tree.getSelection()[0].getData();
			if (data != null && data instanceof TreeData) {
				if (data.getChild() != null) {    				
					if (data.getChild().equalsIgnoreCase("orders")) {
						return data.getElement().getChild("commands");
					} else {
						if(data.getElement().getChild(data.getChild()) == null) {
							//data.getElement().addContent(new Element(data.getChild()));
							return data.getElement();
						}
						return data.getElement().getChild(data.getChild());    	
					}
				} else {					
					if(data.getElement().getName().equals("at") || data.getElement().getName().equals("date")) {
						return data.getElement().getParentElement();
					} else
						return data.getElement();
				}
			} else
				return null;
		} else
			return null;
	}
	 */

	private void createMenu() {
		_menu = new Menu(_tree.getShell(), SWT.POP_UP);

		MenuItem item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getXMLListener());
		item.setText(TreeMenu.EDIT_XML);

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getXMLListener());
		item.setText(TreeMenu.SHOW_XML);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getInfoListener());
		item.setText(TreeMenu.SHOW_INFO);

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getCopyListener());
		item.setText(TreeMenu.COPY);

		if((_dom instanceof sos.scheduler.editor.conf.SchedulerDom)) {
			if(((sos.scheduler.editor.conf.SchedulerDom)_dom).isLifeElement()) {				
				item = new MenuItem(_menu, SWT.PUSH);
				item.addListener(SWT.Selection, getDeleteListener());
				item.setText(TreeMenu.DELETE);

			}
		}


		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getClipboardListener());
		item.setText(TreeMenu.COPY_TO_CLIPBOARD);

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getPasteListener());
		item.setText(TreeMenu.PASTE);

		_menu.addListener(SWT.Show, new Listener() {
			public void handleEvent(Event e) {
				//MenuItem[] items = _menu.getItems();
				if(_copy == null)
					disableMenu();
				if (_tree.getSelectionCount() > 0) {
					Element element = getElement();
					if (element != null ) {
						//test element = _dom.getRoot().getChild("config");

						getItem(TreeMenu.EDIT_XML).setEnabled(true); 
						getItem(TreeMenu.SHOW_INFO).setEnabled(true); // show info
						getItem(TreeMenu.SHOW_XML).setEnabled(true); // show xml
						getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true); // copy to clipboard
						if(_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement())
							getItem(TreeMenu.DELETE).setEnabled(true);


						//String name = element.getName();
						//test
						getItem(TreeMenu.COPY).setEnabled(true); // copy
						/*if (name.equals("job") || name.equals("config") || name.equals("run_time"))
							getItem(TreeMenu.COPY).setEnabled(true); // copy
						else
							getItem(TreeMenu.COPY).setEnabled(false); // copy
						 */
						if (_copy != null) {
							//String cName = _copy.getName();

							MenuItem _paste = getItem(TreeMenu.PASTE);

							if(_tree.getSelectionCount() > 0) {								
								System.out.println(_tree.getSelection()[0].getData("key") + "   " + _tree.getSelection()[0].getText());
							}

							_paste.setEnabled(true); // paste
							/*if (name.equals("jobs") && cName.equals("job"))
								_paste.setEnabled(true); // paste
							else if (name.equals("job") && cName.equals("run_time"))
								_paste.setEnabled(true); // paste
							else if (name.equals("config") && cName.equals("config"))
								_paste.setEnabled(true); // paste
							else
								_paste.setEnabled(false); // paste
							 */
						}
					}
				}
			}
		});
	}


	public Menu getMenu() {
		return _menu;
	}


	private void disableMenu() {
		MenuItem[] items = _menu.getItems();
		for (int i = 0; i < items.length; i++)
			items[i].setEnabled(false);
	}


	private Listener getInfoListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null) {
					try {
						String filename = _dom.transform(element);

						Program prog = Program.findProgram("html");
						if (prog != null)
							prog.execute(filename);
						else {
							Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
						}
					} catch (Exception ex) {
						try {
							new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , ex);
						} catch(Exception ee) {
							//tu nichts
						}
						ex.printStackTrace();
						message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
					}
				}
			}
		};
	}


	private String getXML() {
		Element element = getElement();
		String xml = "";
		if (element != null) {
			try {

				xml = _dom.getXML(element);        

			} catch (JDOMException ex) {
				try {
					new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , ex);
				} catch(Exception ee) {
					//tu nichts
				}
				message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}

		}
		return xml;
	}

	private String getXML(Element element) {

		String xml = "";
		if (element != null) {
			try {

				xml = _dom.getXML(element);    

			} catch (JDOMException ex) {
				try {
					new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , ex);
				} catch(Exception ee) {
					//tu nichts
				}
				message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}

		}
		return xml;
	}



	private Listener getXMLListener() {

		return new Listener() {
			public void handleEvent(Event e) {
				MenuItem i = (MenuItem)e.widget;
				Element element = null;
				String xml = null;
				if(i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
					if(_dom instanceof sos.scheduler.editor.conf.SchedulerDom && ((sos.scheduler.editor.conf.SchedulerDom)_dom).isLifeElement()) {
						element = getElement();
					} else {
						element = _dom.getRoot().getChild("config");
					}
					if(element != null)
						xml = getXML(element);        				

				} else {
					element = getElement();
					if(element != null)        		
						xml = getXML(element);

				}


				if (element != null) {
					if (xml == null) // error
						return;

					String selectStr = Utils.getAttributeValue("name", getElement());
					selectStr = selectStr == null || selectStr.length() == 0 ? getElement().getName() : selectStr;
					String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML), selectStr);
					//newXML ist null, wenn �nderungen nicht �bernommen werden sollen
					if(newXML != null)
						applyXMLChange(newXML);

				}
			}

		};
	}

	private void applyXMLChange(String newXML){

		try {  
			if(_dom instanceof SchedulerDom) {
				if(!((sos.scheduler.editor.conf.SchedulerDom)_dom).isLifeElement())
					newXML = newXML.replaceAll("\\?>", "?><spooler>" )+ "</spooler>";
			}

			//System.out.println("debug: \n" + newXML);



			_dom.readString(newXML, true);
			_gui.update();

			refreshTree("main");


		} catch (Exception de) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , de);
			} catch(Exception ee) {
				//tu nichts
			}
			MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING );    		
		} 
	}

	private Listener getCopyListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null) {
					_copy = (Element) element.clone();
					_type = ((TreeData) _tree.getSelection()[0].getData()).getType();
				}
			}
		};
	}


	private Listener getDeleteListener () {
		return new Listener() {
			public void handleEvent(Event e) {

				int ok = MainWindow.message("Do you wont really remove life file: " + _dom.getFilename(), //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);

				if (ok == SWT.CANCEL || ok == SWT.NO)
					return;		           

				if(!new java.io.File(_dom.getFilename()).delete()) {
					MainWindow.message("could not remove life file", SWT.ICON_WARNING | SWT.OK);
				}
				sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
				con.getCurrentTab().dispose();

			}
		};
	}

	private Listener getClipboardListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();

				if (element != null) {
					String xml = getXML();
					if (xml == null) // error
						return;

					if (!element.getName().equals("config") && xml.indexOf("<?xml") > -1) {
						xml = xml.substring(xml.indexOf("?>")+2);
					}
					Utils.copyClipboard(xml, _tree.getDisplay());




				}
			}
		};
	}

	private Listener getPasteListener() {
		return new Listener() {

			public void handleEvent(Event e) {


				if(_tree.getSelectionCount() == 0)
					return;

				TreeData data = (TreeData) _tree.getSelection()[0].getData();

				boolean override = false;

				if(_tree.getSelection()[0].getData("override_attributes") != null)
					override = _tree.getSelection()[0].getData("override_attributes").equals("true");

				if(_tree.getSelection()[0].getData("key") instanceof String) {
					//ein Formular hat nur einen Kindknoten Element zum Kopieren, z.B. job Formular
					String key = _tree.getSelection()[0].getData("key").toString();				
					paste(key, data, override);
				} else if(_tree.getSelection()[0].getData("key") instanceof ArrayList) {
					//ein Formular hat mehreren Kindknoten Element zum Kopieren, z.B. job Run Option -> start_when_directory_changed; delay_after_error; delay_order_after_setback		
					ArrayList keys = (ArrayList)_tree.getSelection()[0].getData("key");
					for(int i = 0; i < keys.size(); i++) {
						paste(keys.get(i).toString(), data, override);
					}
				}	

				if(_dom instanceof SchedulerDom && (((SchedulerDom)_dom).isDirectory() || ((SchedulerDom)_dom).isLifeElement())) {
					Utils.setChangedForDirectory(data.getElement(), ((SchedulerDom)_dom));
				}

			}

		};
	}


	/*
	private Listener getPasteListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element target = getElement();								

				if ((target != null && _copy != null)) {
					String tName = target.getName();
					String cName = _copy.getName();

					if(_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement()) {

						//if(cName.equals("job")) {
						target = (Element)_copy.clone();
						TreeData data = (TreeData) _tree.getSelection()[0].getData();
						data.setElement(target);
						_gui.update();
						_gui.updateLifeElement();
						//_gui.updateJob(target.getName());
						return;
						//}
					}

					if (tName.equals("jobs") && cName.equals("job")) { // copy job

						String append = "copy(" + (target.getChildren("job").size() + 1);
						Element currCopy = (Element)_copy.clone();

						if(existJobname(target, Utils.getAttributeValue("name", _copy)))
							currCopy.setAttribute("name", append + ")of_" + Utils.getAttributeValue("name", _copy));

						target.addContent(currCopy);

						refreshTree("jobs");
						_gui.update();
						if(_dom instanceof SchedulerDom && !((SchedulerDom)_dom).isLifeElement())
							_gui.updateJobs();
						_dom.setChanged(true);

					} else if (tName.equals("job") && cName.equals("run_time")) { // copy
						// run_time
						target.removeChildren("run_time");
						target.addContent(_copy);
						_gui.updateJob();
						_dom.setChanged(true);
					} else if (tName.equals("config") && cName.equals("config")) { // copy
						// run_time
						//target.getParentElement().removeContent();
						Element spooler = target.getParentElement();
						spooler.removeChildren("config");
						spooler.addContent((Element)_copy.clone());

						refreshTree("main");
						_dom.setChanged(true);

						_gui.update();
					}  else if (tName.equals("commands") && cName.equals("order")) { // copy job

						String append = "copy(" + (target.getChildren("order").size() + 1);
						Element currCopy = (Element)_copy.clone();


						currCopy.setAttribute("id", append + ")of_" + Utils.getAttributeValue("id", _copy));

						target.addContent(currCopy);

						refreshTree("main");						
						_gui.updateCommands();
						_gui.updateOrders();
						_gui.update();
						_dom.setChanged(true);

					} else if (tName.equals("job_chains") && cName.equals("job_chain")) { // copy job

						String append = "copy(" + (target.getChildren("job_chain").size() + 1);
						Element currCopy = (Element)_copy.clone();

						if(existJobname(target, Utils.getAttributeValue("name", _copy)))
							currCopy.setAttribute("name", append + ")of_" + Utils.getAttributeValue("name", _copy));

						target.addContent(currCopy);

						_gui.updateJobChains();
						refreshTree("main");
						_gui.update();
						_dom.setChanged(true);

					}
				}
			}
		};
	}
	 */

	private boolean existJobname(Element jobs, String jobname) {
		boolean retVal = false;
		java.util.List list = jobs.getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element job = (Element)list.get(i);
			if(Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	private void refreshTree(String whichItem) {

		sos.scheduler.editor.app.IContainer con = MainWindow.getContainer(); 
		SchedulerForm sf = (SchedulerForm)(con.getCurrentEditor());
		sf.updateTree(whichItem);

	}

	private MenuItem getItem(String name) {
		MenuItem[] items = _menu.getItems();
		for (int i = 0; i < items.length; i++) {
			MenuItem item = items[i];
			if(item.getText().equalsIgnoreCase(name)) {
				return item;
			}
		}
		return null;
	}

	private boolean isParent(String key, String elemname) {

		//String[] split = key.split("\\.");
		String[] split = key.split("_@_");
		if(split.length > 1)
			key = split[0];

		String[] s = null;
		if(_dom.getDomOrders().get(key) != null)
			s = (String[])_dom.getDomOrders().get(key);

		else if (sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key) != null)
			//s = (String[])sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key);
			return true; //group sind nicht definiert
		else 
			s = getPossibleParent(key);
		for(int i = 0; s != null && i < s.length; i++) {
			if(s[i].equalsIgnoreCase(elemname)) {				
				return true;
			}						
		}
		return false;
	}

	//liefert m�gliche Vaterknoten der key, falls diese nicht in dom.orders steht
	private String[] getPossibleParent(String key) {
		if(key.equals("jobs") || key.equals("monitor"))
			return new String[] {"job"};
		if(key.equals("schedules"))
			return new String[] {"schedule"};
		if(key.equals("job_chains"))
			return new String[] {"job_chain"};

		//weekdays		monthdays		ultimos

		else
			return new String[] {};
	}


	private void paste(String key, TreeData data, boolean overrideAttributes) {

		try {
			//ungleiche Typen, �berpr�fen, ob das pastelement ein m�glicher Vaterknoten von _copy element ist, z.B. _copy Element ist job und paste Element ist jobs	
			if(_type != data.getType()) {
				System.out.println("*****************************************");					
				pasteChild(key, data);
				return;
			}

			//ab hier gleiche typen
			Element target = _copy;

			boolean isLifeElement = _dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement();

			if(key.equalsIgnoreCase(target.getName()) && !isLifeElement) {
				// Kopieren nur von Attributen und nicht der Kindelement, z.B. Config Formular
				Element currElem = data.getElement();
				removeAttributes(currElem);
				copyAttr(currElem, _copy.getAttributes());

			} else {

				//gleiche Typen und und gleiche Elementname -> alle vorhandenen Kindelement kopieren
				Element currElem = data.getElement();

				Element copyElement  = _copy;


				String[] split = null;

				//split = key.split("\\.");
				split = key.split("_@_");


				System.out.println("*****************************************");

				if(split.length > 1)
					key = split[split.length-1];
				java.util.List ce = null;		
				if(key.equals(copyElement.getName())) {
					//�berschreiben: z.B. copy ist job element und paste ist auch Job element			
					removeAttributes(currElem);
					currElem.removeContent();
					copyAttr(currElem, copyElement.getAttributes());				
					ce = copyElement.getChildren();
				} else {

					for(int i = 0; i < split.length-1; i++) {
						//key ist der Pfad ab data.getelement. 
						copyElement = copyElement.getChild(split[i]);
						System.out.println(Utils.getElementAsString(_dom.getRoot()));
						if(currElem.getChild(split[i]) == null) {						
							currElem = currElem.addContent(new Element(split[i]));
						}
						currElem = data.getElement().getChild(split[i]);					
						System.out.println(Utils.getElementAsString(_dom.getRoot()));
					}
					ce = copyElement.getChildren(key);
				}

				for(int i = 0; i < ce.size(); i++) {
					Element a = (Element)ce.get(i);
					Element cloneElement = (Element)a.clone();
					java.util.List currElemContent = null;
					if(_tree.getSelection()[0].getData("max_occur") != null && _tree.getSelection()[0].getData("max_occur").equals("1")) {
						//es darf nur einen currElem.get(key) Kindknoten geben. Also attribute l�schen aber die Kinder mitnehmen
						if(currElem.getChild(key) != null)
							currElemContent = currElem.getChild(key).cloneContent();					
						currElem.removeChild(key);
					}				

					//
					//Element copyClone = (Element)_copy.clone();


					if( !Utils.getAttributeValue("name", cloneElement).equals("") && existJobname(currElem, Utils.getAttributeValue("name", cloneElement))) {
						System.out.println(Utils.getAttributeValue("name", cloneElement) + " existiert berteits");
						String append = "copy(" + (cloneElement.getChildren("job").size() + currElem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", cloneElement);					
						cloneElement.setAttribute("name", append);
					}
					//

					currElem.addContent(cloneElement);
					if(currElemContent != null)
						cloneElement.addContent(currElemContent);

					if(overrideAttributes) {
						copyElement = cloneElement;
						currElem = currElem.getChild(key);
					}
					System.out.println(Utils.getElementAsString(_dom.getRoot()));
				}

				if(overrideAttributes) {
					removeAttributes(currElem);
					copyAttr(currElem, copyElement.getAttributes());
				}

			}

			updateTreeView();		
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " +  sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}	
		}
	}


	//Ein Kindelement hinzuf�gen, z.B. jobs einen job element oder job_chains einen job_chain element einf�gen
	private void pasteChild(String key, TreeData data) {
		if(!isParent(key, _copy.getName())) {
			return;
		} else {

			String[] split = key.split("_@_");
			Element elem = data.getElement();
			for(int i = 0; i < split.length-1; i++) {
				if(data.getElement().getChild(split[i]) == null)
					data.getElement().addContent(new Element(split[i]));
				elem = data.getElement().getChild(split[i]);												
				System.out.println(Utils.getElementAsString(_dom.getRoot()));
			}

			Element copyClone = (Element)_copy.clone();

			if(!Utils.getAttributeValue("name", _copy).equals("") &&  existJobname(elem, Utils.getAttributeValue("name", _copy))) {
				System.out.println(Utils.getAttributeValue("name", _copy) + " existiert berteits");
				String append = "copy(" + (copyClone.getChildren("job").size() + elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("name", copyClone);					
				copyClone.setAttribute("name", append);
			}
			elem.addContent(copyClone);

		}

		updateTreeView();

	}

	/*private TreeItem getCurrentItem(String text) {
		TreeItem retVal = null;
		for(int i = 0; i < _tree.getItemCount(); i++ ){

			TreeItem item = getCurrentItem(_tree.getItem(i), text);
			if(item != null && item.getText().equals(text))
				return item;
		}

		return retVal;
	}*/

	/*private TreeItem getCurrentItem(TreeItem parent, String text) {
		TreeItem retVal = null;
		if(parent.getText().equals(text))
			return parent;
		for(int i = 0; i < parent.getItemCount(); i++ ){							
			return getCurrentItem(parent.getItem(i), text);			
		}

		return retVal;
	}
*/
	private void removeAttributes(Element elem) {
		List l = elem.getAttributes();
		for(int i = 0; i < l.size(); i++)
			elem.removeAttribute((org.jdom.Attribute)l.get(i));
	}

	private void copyAttr(Element elem, java.util.List attr) {	
		for(int i = 0; i < attr.size(); i++) {
			org.jdom.Attribute a = (org.jdom.Attribute)attr.get(i);						
			elem.setAttribute(a.getName(), a.getValue());
		}
	}

	private void updateTreeView() {



		//unpdate der Formular
		//String currItemString =  _tree.getSelection()[0].getText();

		if(_type == Editor.SPECIFIC_WEEKDAYS)
			_gui.updateSpecificWeekdays();



		//if (_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement())
		//	return;

		//sucht das Treeitem mit der currItemString um die gleiche Parent zu selektieren.
		//TreeItem item = getCurrentItem(currItemString);
		//if(item != null)
		//	_tree.setSelection(new TreeItem [] {item});				
		_gui.update();


		if(_tree.getSelection()[0].getText().equals("Jobs"))
			_gui.updateJobs();


		//if(_copy.getName().equals("job"))
		if(_type == Editor.JOB &&  !_tree.getSelection()[0].getText().endsWith("Jobs") )
			_gui.updateJob();

		if(_type == Editor.SCHEDULES)
			_gui.updateSchedules();				

		if(_type == Editor.ORDERS)
			_gui.updateOrders();

		if(_type == Editor.JOB_CHAINS || _type == Editor.JOB_CHAIN)
			_gui.updateJobChains();

		_gui.expandItem(_tree.getSelection()[0].getText());
		_gui.updateTreeItem(_tree.getSelection()[0].getText());

		_gui.updateTree("");

		refreshTree("main");
		_dom.setChanged(true);
	}


}
