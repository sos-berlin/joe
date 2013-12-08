package sos.scheduler.editor.app;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.classes.SOSComboBox;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class ContextMenu {

	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());

	private SchedulerDom		_dom	= null;
	private SOSComboBox			_combo	= null;
	private Menu				_menu	= null;
	// TODO I18N
	private static final String	GOTO	= "Goto";
	private static final String	DELETE	= "Delete";
	private static int			_type	= -1;

	public ContextMenu(final SOSComboBox combo, final SchedulerDom dom, final int type) {
		_combo = combo;
		_dom = dom;
		_type = type;

		createMenu();
	}

	public int message(final String message, final int style) {
		MessageBox mb = new MessageBox(_combo.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}

	private void createMenu() {
		_menu = new Menu(_combo.getShell(), SWT.POP_UP);

		MenuItem item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getListener());

		if (_type == Editor.SCRIPT)
			item.setText(ContextMenu.DELETE);
		else
			item.setText(ContextMenu.GOTO);

		_menu.addListener(SWT.Show, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				MenuItem item = null;
				if (_type == Editor.SCRIPT) {
					item = getItem(ContextMenu.DELETE);
				}
				else
					item = getItem(ContextMenu.GOTO);

				if (item != null)
					item.setEnabled(true);
			}
		});
	}

	public Menu getMenu() {
		return _menu;
	}

	private Listener getListener() {

		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				if (_type == Editor.SCRIPT)
					delete(_combo, _dom, _type);
				else
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
				new ErrorLog("error in " + SOSClassUtil.getMethodName() , de);
			} catch(Exception ee) {

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
	private MenuItem getItem(final String name) {
		MenuItem[] items = _menu.getItems();
		for (MenuItem item : items) {
			if (item.getText().equalsIgnoreCase(name)) {
				return item;
			}
		}
		return null;
	}

	private static String removeTitle(String s) {
		if (s.contains(" - ")) {
			int p = s.indexOf(" - ");
			s = s.substring(0, p);
		}
		if (s.startsWith("*")) {
			s = s.substring(1);
		}
		return s;
	}

	private static void selectTreeItem(final DomParser _dom, final String pstrTagName, final String pstrObjectName, final int pintType) {

		try {
//			XPath x3 = XPath.newInstance("//" + pstrTagName + "[@name='" + pstrObjectName + "']");
//			List listOfElement_3 = x3.selectNodes(_dom.getDoc());
//			if (!listOfElement_3.isEmpty()) {
				SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
				if (f == null) {
					return;
				}
				Tree tree = f.getTree();
				TreeItem objRootItem = tree.getItem(0);

				int intItemCount = objRootItem.getItemCount();
				for (int i = 0; i < intItemCount; i++) {
					TreeItem item = objRootItem.getItem(i);
					TreeData objTD = (TreeData) item.getData();
					String strText = item.getText();
					if (objTD.TypeEqualTo(pintType )) {
						TreeItem[] jobsItem = item.getItems();
						for (TreeItem jItem : jobsItem) {
							TreeData objTd = (TreeData) jItem.getData();
							if (objTd.NameEqualTo(pstrObjectName)) {
								tree.setSelection(new TreeItem[] { jItem });
								f.updateTreeItem(jItem.getText());
								f.updateTree("");
								jItem.setExpanded(true);
								break;
							}
						}
					}
				}
//			}
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}

	}

	public static void goTo(final String name, DomParser _dom, final int type) {
		try {

			if (name == null || name.length() == 0) {
				return;
			}

			if (_dom instanceof sos.scheduler.editor.actions.ActionsDom)
				_dom = _dom;
			else
				_dom = _dom;

			if (type == Editor.JOB) {
				selectTreeItem(_dom, "job", name, Editor.JOBS);

//				XPath x3 = XPath.newInstance("//job[@name='" + name + "']");
//				List listOfElement_3 = x3.selectNodes(_dom.getDoc());
//
//				if (!listOfElement_3.isEmpty()) {
//					SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
//					if (f == null) {
//						return;
//					}
//					Tree tree = f.getTree();
//					for (int i = 0; i < tree.getItemCount(); i++) {
//						TreeItem item = tree.getItem(i);
//						if (item.getText().equals(SchedulerListener.JOBS)) {
//							TreeItem[] jobsItem = item.getItems();
//							for (TreeItem jItem : jobsItem) {
//								String strName = jItem.getText();
//								strName = removeTitle(strName);
//
//								// TODO get the name of the job from the Element, not from the description
//
//								if (strName.equals(name)) {
//									tree.setSelection(new TreeItem[] { jItem });
//									f.updateTreeItem(jItem.getText());
//									f.updateTree("jobs");
//									break;
//								}
//							}
//						}
//					}
//				}
			}
			else
				if (type == Editor.MONITOR) {

					String[] split = name.split("_@_");
					String jobname = split[0];
					String monitorname = split[1];

					XPath x3 = XPath.newInstance("//job[@name='" + jobname + "']/monitor[@name='" + monitorname + "']");
					List listOfElement_3 = x3.selectNodes(_dom.getDoc());

					if (!listOfElement_3.isEmpty()) {

						SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
						if (f == null)
							return;
						Tree tree = f.getTree();
						if (tree.getSelection()[0].getText().equals(SchedulerListener.MONITOR)) {
							TreeItem[] monitorsItem = tree.getSelection()[0].getItems();
							for (TreeItem monitor : monitorsItem) {
								String strName = monitor.getText();
								strName = removeTitle(strName);

								if (strName.equals(monitorname)) {
									tree.setSelection(new TreeItem[] { monitor });
									f.updateTreeItem(monitorname);
									f.updateTree("monitor");
									break;
								}
							}
						}
						else {
							for (int i = 0; i < tree.getItemCount(); i++) {
								TreeItem item = tree.getItem(i);
								if (item.getText().equals(jobname)) {
									TreeItem[] jobsItem = item.getItems();
									for (TreeItem jItem : jobsItem) {
										if (jItem.getText().equals("Monitor")) {
											TreeItem[] monitorsItem = jItem.getItems();
											for (TreeItem monitor : monitorsItem) {
												if (monitor.getText().equals(monitorname)) {
													//if(jItem.getText().endsWith("Job: "+ name)){
													tree.setSelection(new TreeItem[] { monitor });
													f.updateTreeItem(monitorname);
													f.updateTree("monitor");
													break;
												}
											}
										}
									}
								}
							}
						}
					}

				}
				else
					if (type == Editor.JOB_CHAIN) {
						selectTreeItem(_dom, "job_chain", name, Editor.JOB_CHAINS);
//						XPath x3 = XPath.newInstance("//job_chain[@name='" + name + "']");
//						List listOfElement_3 = x3.selectNodes(_dom.getDoc());
//						if (!listOfElement_3.isEmpty()) {
//							SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
//							if (f == null) {
//								return;
//							}
//							Tree tree = f.getTree();
//							TreeItem objRootItem = tree.getItem(0);
//
//							int intItemCount = objRootItem.getItemCount();
//							for (int i = 0; i < intItemCount; i++) {
//								TreeItem item = objRootItem.getItem(i);
//								String strText = item.getText();
//								if (strText.equals(SchedulerListener.JOB_CHAINS)) {
//									TreeItem[] jobsItem = item.getItems();
//									for (TreeItem jItem : jobsItem) {
//										String strName = jItem.getText();
//										strName = removeTitle(strName);
//
//										if (strName.equals(name)) {
//											tree.setSelection(new TreeItem[] { jItem });
//											f.updateTreeItem(jItem.getText());
//											f.updateTree("");
//											jItem.setExpanded(true);
//											break;
//										}
//									}
//								}
//							}
//						}
//
					}
					else
						if (type == Editor.PROCESS_CLASSES) {
							selectTreeItem(_dom, "process_class", name, Editor.PROCESS_CLASSES);

//							XPath x3 = XPath.newInstance("//process_class[@name='" + name + "']");
//							List listOfElement_3 = x3.selectNodes(_dom.getDoc());
//							if (!listOfElement_3.isEmpty()) {
//								SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
//								if (f == null)
//									return;
//								Tree tree = f.getTree();
//								for (int i = 0; i < tree.getItemCount(); i++) {
//									TreeItem item = tree.getItem(i);
//									//if(item.getText().equals("Process Classes")){
//									if (item.getText().endsWith("Process Classes")) {
//										tree.setSelection(new TreeItem[] { item });
//										f.updateTreeItem(item.getText());
//										f.updateTree("");
//										break;
//									}
//								}
//							}
						}
						else
							if (type == Editor.SCHEDULE) {
								selectTreeItem(_dom, "schedule", name, Editor.SCHEDULES);

//								XPath x3 = XPath.newInstance("//schedule[@name='" + name + "']");
//								List listOfElement_3 = x3.selectNodes(_dom.getDoc());
//								if (!listOfElement_3.isEmpty()) {
//									SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
//									if (f == null)
//										return;
//									Tree tree = f.getTree();
//									for (int i = 0; i < tree.getItemCount(); i++) {
//										TreeItem item = tree.getItem(i);
//										if (item.getText().equals(SchedulerListener.SCHEDULES)) {
//
//											TreeItem[] items = item.getItems();
//											for (TreeItem jItem : items) {
//												String strName = jItem.getText();
//												strName = removeTitle(strName);
//
//												if (strName.equals(name)) {
//													tree.setSelection(new TreeItem[] { jItem });
//													f.updateTreeItem(jItem.getText());
//													f.updateTree("");
//													break;
//												}
//											}
//										}
//									}
//								}

							}
							else
								if (type == Editor.ORDER) {

									XPath x3 = XPath.newInstance("//order[@id='" + name + "']");
									List listOfElement_3 = x3.selectNodes(_dom.getDoc());

									if (listOfElement_3.isEmpty()) {
										x3 = XPath.newInstance("//add_order[@id='" + name + "']");
										listOfElement_3 = x3.selectNodes(_dom.getDoc());
									}

									if (!listOfElement_3.isEmpty()) {
										SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
										if (f == null)
											return;
										Tree tree = f.getTree();
										for (int i = 0; i < tree.getItemCount(); i++) {
											TreeItem item = tree.getItem(i);
											if (item.getText().equals(SchedulerListener.ORDERS)) {

												TreeItem[] items = item.getItems();
												for (TreeItem jItem : items) {
													String strName = jItem.getText();
													strName = removeTitle(strName);

													if (strName.equals(name)) {
														tree.setSelection(new TreeItem[] { jItem });
														f.updateTreeItem(jItem.getText());
														f.updateTree("");
														break;
													}
												}
											}
										}
									}

								}
								else
									if (type == Editor.WEBSERVICE) {

										XPath x3 = XPath.newInstance("//web_service[@name='" + name + "']");
										List listOfElement_3 = x3.selectNodes(_dom.getDoc());

										if (!listOfElement_3.isEmpty()) {
											SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
											if (f == null)
												return;
											Tree tree = f.getTree();
											for (int i = 0; i < tree.getItemCount(); i++) {
												TreeItem item = tree.getItem(i);
												if (item.getText().equals(SchedulerListener.HTTP_SERVER)) {
													for (int k = 0; k < item.getItemCount(); k++) {
														TreeItem httpItem = item.getItem(k);

														if (httpItem.getText().equals(SchedulerListener.WEB_SERVICES)) {

															TreeItem[] items = httpItem.getItems();
															for (TreeItem jItem : items) {
																if (jItem.getText().equals("Web Service: " + name)) {
																	tree.setSelection(new TreeItem[] { jItem });
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
									}
									else
										if (type == Editor.ACTIONS) {

											XPath x3 = XPath.newInstance("//action[@name='" + name + "']");
											List listOfElement_3 = x3.selectNodes(_dom.getDoc());
											if (!listOfElement_3.isEmpty()) {
												ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
												if (f == null)
													return;
												Tree tree = f.getTree();
												for (int i = 0; i < tree.getItemCount(); i++) {
													TreeItem item = tree.getItem(i);
													if (item.getText().equals("Actions")) {
														TreeItem[] jobsItem = item.getItems();
														for (TreeItem jItem : jobsItem) {
															//if(jItem.getText().equals("Job Chain: "+ name)){
															if (jItem.getText().endsWith(
																	sos.scheduler.editor.actions.listeners.ActionsListener.ACTION_PREFIX + name)) {
																tree.setSelection(new TreeItem[] { jItem });
																f.updateTreeItem(jItem.getText());
																f.updateTree("");
																break;
															}
														}
													}
												}
											}
										}
										else
											if (type == Editor.EVENTS) {
												//<event_group logic="or" group="1">
												XPath x3 = XPath.newInstance("//event_group[@group='" + name + "']");

												List listOfElement_3 = x3.selectNodes(_dom.getDoc());
												if (!listOfElement_3.isEmpty()) {
													ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
													if (f == null)
														return;
													Tree tree = f.getTree();
													if (tree.getSelectionCount() > 0) {
														TreeItem itemp = tree.getSelection()[0];
														for (int i = 0; i < itemp.getItemCount(); i++) {
															TreeItem item = itemp.getItem(i);
															if (item.getText().endsWith(
																	sos.scheduler.editor.actions.listeners.ActionsListener.GROUP_PREFIX + name)) {
																tree.setSelection(new TreeItem[] { item });
																f.updateTreeItem(item.getText());
																f.updateTree("");
																break;
															}
														}
													}
												}
											}
											else
												if (type == Editor.ACTION_COMMANDS) {
													XPath x3 = XPath.newInstance("//command[@name='" + name + "']");

													List listOfElement_3 = x3.selectNodes(_dom.getDoc());
													if (!listOfElement_3.isEmpty()) {
														ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
														if (f == null)
															return;
														Tree tree = f.getTree();
														if (tree.getSelectionCount() > 0) {
															TreeItem itemp = tree.getSelection()[0];
															for (int i = 0; i < itemp.getItemCount(); i++) {
																TreeItem item = itemp.getItem(i);
																if (item.getText().endsWith(
																		sos.scheduler.editor.actions.listeners.ActionsListener.COMMAND_PREFIX + name)) {
																	tree.setSelection(new TreeItem[] { item });
																	f.updateTreeItem(item.getText());
																	f.updateTree("");
																	break;
																}
															}
														}
													}
												}
												else
													if (type == Editor.JOB_COMMAND_EXIT_CODES
															&& sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor() instanceof ActionsForm) {

														XPath x3 = null;
														String job = "";
														if (name.startsWith("start_job")) {
															job = name.substring("start_job: ".length());
															x3 = XPath.newInstance("//command/start_job[@job='" + job + "']");

														}
														else {
															String child = name.substring(0, name.indexOf(": "));
															job = name.substring(child.length() + 2);
															x3 = XPath.newInstance("//command/" + child + "[@job_chain='" + job + "']");

														}

														List listOfElement_3 = x3.selectNodes(_dom.getDoc());
														if (!listOfElement_3.isEmpty()) {
															ActionsForm f = (ActionsForm) sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor();
															if (f == null)
																return;
															Tree tree = f.getTree();
															if (tree.getSelectionCount() > 0) {
																TreeItem itemp = tree.getSelection()[0];
																for (int i = 0; i < itemp.getItemCount(); i++) {
																	TreeItem item = itemp.getItem(i);
																	if (item.getText().equals(name)) {
																		tree.setSelection(new TreeItem[] { item });
																		f.updateTreeItem(item.getText());
																		f.updateTree("");
																		break;
																	}
																}
															}
														}
													}
													else
														if (type == Editor.JOB_COMMAND_EXIT_CODES
																&& sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {

															XPath x3 = null;
															String job = "";
															if (name.startsWith("start_job")) {
																job = name.substring("start_job: ".length());
																x3 = XPath.newInstance("//commands/start_job[@job='" + job + "']");

															}
															else {
																String child = name.substring(0, name.indexOf(": "));
																job = name.substring(child.length() + 2);
																x3 = XPath.newInstance("//commands/" + child + "[@job_chain='" + job + "']");

															}

															List listOfElement_3 = x3.selectNodes(_dom.getDoc());
															if (!listOfElement_3.isEmpty()) {
																SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer()
																		.getCurrentEditor();
																if (f == null)
																	return;
																Tree tree = f.getTree();
																if (tree.getSelectionCount() > 0) {
																	TreeItem itemp = tree.getSelection()[0];
																	for (int i = 0; i < itemp.getItemCount(); i++) {
																		TreeItem item = itemp.getItem(i);
																		if (item.getText().equals(name)) {
																			tree.setSelection(new TreeItem[] { item });
																			f.updateTreeItem(item.getText());
																			f.updateTree("");
																			break;
																		}
																	}
																}
															}

														}
														else
															if (type == Editor.JOB_COMMAND) {
																SchedulerForm f = (SchedulerForm) sos.scheduler.editor.app.MainWindow.getContainer()
																		.getCurrentEditor();
																if (f == null)
																	return;

																Tree tree = f.getTree();
																if (tree.getSelectionCount() > 0) {
																	TreeItem itemp = tree.getSelection()[0];
																	for (int i = 0; i < itemp.getItemCount(); i++) {
																		TreeItem item = itemp.getItem(i);
																		if (item.getText().equals(name)) {
																			tree.setSelection(new TreeItem[] { item });
																			f.updateTreeItem(item.getText());
																			f.updateTree("");
																			break;
																		}
																	}
																}

															}
		}
		catch (Exception e) {
			new ErrorLog(e.toString(), e);
		}
	}

	public static void delete(final SOSComboBox combo, final DomParser _dom, final int type) {
		try {
			//favoriten löschen
			if (combo.getData("favorites") == null)
				return;
			if (type == Editor.SCRIPT) {
				String prefix = "monitor_favorite_";
				String name = combo.getText();
				String lan = "";
				if (combo.getData("favorites") != null)
					lan = ((HashMap) combo.getData("favorites")).get(name) + "_";
				name = prefix + lan + name;
				Options.removeProperty(name);
				combo.remove(combo.getText());
			}
		}
		catch (Exception e) {
			new ErrorLog(e.toString(), e);
		}
	}

}
