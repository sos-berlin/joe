package sos.scheduler.editor.app;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

import sos.scheduler.editor.actions.forms.ActionsForm;


public class ContextMenu {

	private SchedulerDom            _dom                  = null;

	private Combo                    _combo                 = null;

	private Menu                    _menu                 = null;	

	private static final String      GOTO                 = "Goto";

	private static int               _type                = -1;

	
	public ContextMenu(Combo combo, SchedulerDom dom, int type) {
		_combo = combo;
		_dom = dom; 		
		_type = type;
		
		createMenu();
	}


	public int message(String message, int style) {
		MessageBox mb = new MessageBox(_combo.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}


	private void createMenu() {
		_menu = new Menu(_combo.getShell(), SWT.POP_UP);

		MenuItem item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getListener());
		item.setText(ContextMenu.GOTO);

		_menu.addListener(SWT.Show, new Listener() {
			public void handleEvent(Event e) {
												
				getItem(ContextMenu.GOTO).setEnabled(true);
				
				
			}
		});
	}


	public Menu getMenu() {
		return _menu;
	}



	private Listener getListener() {

		return new Listener() {
			public void handleEvent(Event e) {
				
				goTo(_combo.getText(), _dom, _type);
				
			}

		};
	}

	/*private void applyXMLChange(String newXML){

		if(_dom instanceof SchedulerDom) {
			if(!((sos.scheduler.editor.conf.SchedulerDom)_dom).isLifeElement())
				newXML = newXML.replaceAll("\\?>", "?><spooler>" )+ "</spooler>";
		}

		//System.out.println("debug: \n" + newXML);

		try {    		

			_dom.readString(newXML, true);
			

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
				if (element != null)
					_copy = (Element) element.clone();
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
				
			}

				
		};
	}


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
						TreeData data = (TreeData) _combo.getData();
						data.setElement(target);
						
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
						//_gui.update();
						if(_dom instanceof SchedulerDom && !((SchedulerDom)_dom).isLifeElement())
							//_gui.updateJobs();
						_dom.setChanged(true);

					} else if (tName.equals("job") && cName.equals("run_time")) { // copy
						// run_time
						target.removeChildren("run_time");
						target.addContent(_copy);
						//_gui.updateJob();
						_dom.setChanged(true);
					} else if (tName.equals("config") && cName.equals("config")) { // copy
						// run_time
						//target.getParentElement().removeContent();
						Element spooler = target.getParentElement();
						spooler.removeChildren("config");
						spooler.addContent((Element)_copy.clone());

						refreshTree("main");
						_dom.setChanged(true);

						//_gui.update();
					}  else if (tName.equals("commands") && cName.equals("order")) { // copy job

						String append = "copy(" + (target.getChildren("order").size() + 1);
						Element currCopy = (Element)_copy.clone();


						currCopy.setAttribute("id", append + ")of_" + Utils.getAttributeValue("id", _copy));

						target.addContent(currCopy);

						refreshTree("main");						
						//_gui.updateCommands();
						//_gui.updateOrders();
						//_gui.update();
						_dom.setChanged(true);

					} else if (tName.equals("job_chains") && cName.equals("job_chain")) { // copy job

						String append = "copy(" + (target.getChildren("job_chain").size() + 1);
						Element currCopy = (Element)_copy.clone();

						if(existJobname(target, Utils.getAttributeValue("name", _copy)))
							currCopy.setAttribute("name", append + ")of_" + Utils.getAttributeValue("name", _copy));

						target.addContent(currCopy);

						//_gui.updateJobChains();
						refreshTree("main");
						//_gui.update();
						_dom.setChanged(true);

					}
				}
			}
		};
	}

	*/
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

	public static void goTo(String name, DomParser _dom, int type) {
		try {
			
			if(_dom instanceof sos.scheduler.editor.actions.ActionsDom)
				_dom = (sos.scheduler.editor.actions.ActionsDom)_dom;
			else 
				_dom = (SchedulerDom)_dom;
			
			if(name.startsWith("*")) {			
				name = name.substring(1);
			}

			if(type==Editor.JOB) {

				XPath x3 = XPath.newInstance("//job[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());								 
				
				if(!listOfElement_3.isEmpty()) {    
					
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals(SchedulerListener.JOBS)){

							TreeItem[] jobsItem = item.getItems();
							for(int j = 0; j < jobsItem.length; j++) {
								TreeItem jItem = jobsItem[j];
								//if(jItem.getText().equals("Job: "+ name)){
								if(jItem.getText().endsWith("Job: "+ name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("jobs");
									break;
								}
							}
						}
					}
				}

			} else if(type==Editor.JOB_CHAIN) {

				XPath x3 = XPath.newInstance("//job_chain[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty()) {    			
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals(SchedulerListener.JOB_CHAINS)){
							TreeItem[] jobsItem = item.getItems();
							for(int j = 0; j < jobsItem.length; j++) {
								TreeItem jItem = jobsItem[j];
								//if(jItem.getText().equals("Job Chain: "+ name)){
								if(jItem.getText().endsWith("Job Chain: "+ name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("");
									break;
								}
							}
						}
					}
				}

			} else if (type == Editor.PROCESS_CLASSES) {

				XPath x3 = XPath.newInstance("//process_class[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty()) {    			
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						//if(item.getText().equals("Process Classes")){
						if(item.getText().endsWith("Process Classes")){
							tree.setSelection(new TreeItem [] {item});
							f.updateTreeItem(item.getText());
							f.updateTree("");    						
							break;
						}
					}
				}
			} else if(type==Editor.SCHEDULE) {

				XPath x3 = XPath.newInstance("//schedule[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty()) {    			
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals(SchedulerListener.SCHEDULES)){

							TreeItem[] items = item.getItems();
							for(int j = 0; j < items.length; j++) {
								TreeItem jItem = items[j];
								//if(jItem.getText().equals(name)){
								if(jItem.getText().endsWith(name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("");
									break;
								}
							}
						}
					}
				}

			} else if(type == Editor.ORDER) {

				XPath x3 = XPath.newInstance("//order[@id='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());

				if(listOfElement_3.isEmpty()) {
					x3 = XPath.newInstance("//add_order[@id='"+ name + "']");				 
					listOfElement_3 = x3.selectNodes(_dom.getDoc());
				}

				if(!listOfElement_3.isEmpty()) {    			
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals(SchedulerListener.ORDERS)){

							TreeItem[] items = item.getItems();
							for(int j = 0; j < items.length; j++) {
								TreeItem jItem = items[j];
								//if(jItem.getText().equals("Order: " + name)){
								if(jItem.getText().endsWith("Order: " + name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("");
									break;
								}
							}
						}
					}
				}

			} else if(type == Editor.WEBSERVICE) {

				XPath x3 = XPath.newInstance("//web_service[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());				

				if(!listOfElement_3.isEmpty()) {    			
					SchedulerForm f = (SchedulerForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals(SchedulerListener.HTTP_SERVER)){
							for(int k = 0; k < item.getItemCount(); k++) {    
								TreeItem httpItem = item.getItem(k);

								if(httpItem.getText().equals(SchedulerListener.WEB_SERVICES)){

									TreeItem[] items = httpItem.getItems();
									for(int j = 0; j < items.length; j++) {
										TreeItem jItem = items[j];
										if(jItem.getText().equals("Web Service: " + name)){
											tree.setSelection(new TreeItem [] {jItem});
											f.updateTreeItem(jItem.getText());
											f.updateTree("");
											break;
										}
									}
								}
							}
						}
					}
				}
			} else if (type == Editor.ACTIONS) {


				XPath x3 = XPath.newInstance("//action[@name='"+ name + "']");				 
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty()) {    			
					ActionsForm f = (ActionsForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					for(int i = 0; i < tree.getItemCount(); i++) {    				
						TreeItem item = tree.getItem(i);
						if(item.getText().equals("Actions")){
							TreeItem[] jobsItem = item.getItems();
							for(int j = 0; j < jobsItem.length; j++) {
								TreeItem jItem = jobsItem[j];
								//if(jItem.getText().equals("Job Chain: "+ name)){
								if(jItem.getText().endsWith(sos.scheduler.editor.actions.listeners.ActionsListener.ACTION_PREFIX + name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("");
									break;
								}
							}
						}
					}
				}
			} else if(type == Editor.EVENTS) {
				 //<event_group logic="or" group="1">
				XPath x3 = XPath.newInstance("//event_group[@group='"+ name + "']");		
				
				
				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
				if(!listOfElement_3.isEmpty()) {    			
					ActionsForm f = (ActionsForm)(sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
					if(f == null)
						return;
					Tree tree = f.getTree();
					if(tree.getSelectionCount() > 0) {
					TreeItem itemp = tree.getSelection()[0];
					for(int i = 0; i < itemp.getItemCount(); i++) {    				
						TreeItem item = itemp.getItem(i);
						if(item.getText().endsWith(sos.scheduler.editor.actions.listeners.ActionsListener.GROUP_PREFIX + name)){
							tree.setSelection(new TreeItem [] {item});
							f.updateTreeItem(item.getText());
							f.updateTree("");
							break;
						}
					}
					}
						/*if(item.getText().equals("Events")){
							TreeItem[] jobsItem = item.getItems();
							for(int j = 0; j < jobsItem.length; j++) {
								TreeItem jItem = jobsItem[j];
						
								if(jItem.getText().endsWith(sos.scheduler.editor.actions.listeners.ActionsListener.GROUP_PREFIX + name)){
									tree.setSelection(new TreeItem [] {jItem});
									f.updateTreeItem(jItem.getText());
									f.updateTree("");
									break;
								}
							}
							}
						}
					}*/
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
