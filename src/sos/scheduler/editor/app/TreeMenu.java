package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.sos.joe.objects.jobchain.forms.JobChainsForm;

import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.PreProstProcessingListener;
import sos.scheduler.editor.conf.listeners.SchedulerListener;
import sos.scheduler.editor.conf.listeners.SchedulesListener;
import sos.util.SOSClassUtil;

public class TreeMenu {

	private DomParser			_dom					= null;
	private Tree				_tree					= null;
	private Menu				_menu					= null;
	private static Element		_copy					= null;
	private static int			_type					= -1;
	private SchedulerForm		objSchedulerForm		= null;
	// TODO I18N
	private static final String	EDIT_XML				= "Edit XML";
	private static final String	SHOW_XML				= "Show XML";
	//private static final String SHOW_INFO = "Show Info";
	private static final String	COPY					= "Copy";
	private static final String	COPY_TO_CLIPBOARD		= "XML to Clipboard";
	private static final String	PASTE					= "Paste";
	private static final String	DELETE_HOT_HOLDER_FILE	= "Delete Hot Folder File";
	private static final String	NEW						= "New";
	private static final String	DELETE					= "Delete";

	public TreeMenu(final Tree tree, final DomParser dom, final SchedulerForm gui) {
		_tree = tree;
		_dom = dom;
		objSchedulerForm = gui;
		createMenu();
	}

	public int message(final String message, final int style) {
		MessageBox mb = new MessageBox(_tree.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}

	private Element getElement() {
		if (_tree.getSelectionCount() > 0) {
			Element retVal = (Element) _tree.getSelection()[0].getData("copy_element");
			if (retVal == null) {
				new ErrorLog(_tree.getSelection()[0].getText() + " need copy_element." + SOSClassUtil.getMethodName());
				return null;
			}
			if (((SchedulerDom) _dom).isDirectory()) {
				return (Element) Utils.getHotFolderParentElement(retVal).clone();
			}
			return (Element) retVal.clone();
		}
		return null;
	}

	/*
	 * private Element getElement() { if (_tree.getSelectionCount() > 0) {
	 * TreeData data = (TreeData) _tree.getSelection()[0].getData(); if (data !=
	 * null && data instanceof TreeData) { if (data.getChild() != null) { if
	 * (data.getChild().equalsIgnoreCase("orders")) { return
	 * data.getElement().getChild("commands"); } else {
	 * if(data.getElement().getChild(data.getChild()) == null) {
	 * //data.getElement().addContent(new Element(data.getChild())); return
	 * data.getElement(); } return data.getElement().getChild(data.getChild());
	 * } } else { if(data.getElement().getName().equals("at") ||
	 * data.getElement().getName().equals("date")) { return
	 * data.getElement().getParentElement(); } else return data.getElement(); }
	 * } else return null; } else return null; }
	 */

	private void createMenu() {
		_menu = new Menu(_tree.getShell(), SWT.POP_UP);

		MenuItem item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getCopyListener());
		item.setText(TreeMenu.COPY);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getPasteListener());
		item.setText(TreeMenu.PASTE);

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getNewItemSelection());
		item.setText(TreeMenu.NEW);
		item.setEnabled(false);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getDeleteSelection());
		item.setText(TreeMenu.DELETE);
		item.setEnabled(false);

		if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom) {
			if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
				item = new MenuItem(_menu, SWT.PUSH);
				item.addListener(SWT.Selection, getDeleteHoltFolderFileListener());
				item.setText(TreeMenu.DELETE_HOT_HOLDER_FILE);

			}
		}

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getClipboardListener());
		item.setText(TreeMenu.COPY_TO_CLIPBOARD);

		_menu.addListener(SWT.Show, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				// MenuItem[] items = _menu.getItems();
				if (_copy == null)
					disableMenu();
				if (_tree.getSelectionCount() > 0) {
					Element element = getElement();
					if (element != null) {
						// test element = _dom.getRoot().getChild("config");

						getItem(TreeMenu.EDIT_XML).setEnabled(true);
						// getItem(TreeMenu.SHOW_INFO).setEnabled(true); // show
						// info
						getItem(TreeMenu.SHOW_XML).setEnabled(true); // show xml
						getItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true); // copy
																				// to
																				// clipboard
						if (_dom instanceof SchedulerDom) {
							SchedulerDom curDom = (SchedulerDom) _dom;
							if (curDom.isLifeElement()) {
								getItem(TreeMenu.DELETE_HOT_HOLDER_FILE).setEnabled(true);
							}
							if (curDom.isDirectory()) {
								String elemName = getElement().getName();
								if (elemName.equals("config"))
									getItem(TreeMenu.EDIT_XML).setEnabled(false);
							}
						}

						// String name = element.getName();
						// test
						getItem(TreeMenu.COPY).setEnabled(true); // copy
						/*
						 * if (name.equals("job") || name.equals("config") ||
						 * name.equals("run_time"))
						 * getItem(TreeMenu.COPY).setEnabled(true); // copy else
						 * getItem(TreeMenu.COPY).setEnabled(false); // copy
						 */
						if (_copy != null) {
							// String cName = _copy.getName();

							MenuItem _paste = getItem(TreeMenu.PASTE);

							/*
							 * if(_tree.getSelectionCount() > 0) {
							 * System.out.println
							 * (_tree.getSelection()[0].getData("key") + "   " +
							 * _tree.getSelection()[0].getText()); }
							 */

							_paste.setEnabled(true); // paste
							/*
							 * if (name.equals("jobs") && cName.equals("job"))
							 * _paste.setEnabled(true); // paste else if
							 * (name.equals("job") && cName.equals("run_time"))
							 * _paste.setEnabled(true); // paste else if
							 * (name.equals("config") && cName.equals("config"))
							 * _paste.setEnabled(true); // paste else
							 * _paste.setEnabled(false); // paste
							 */
						}

						if (getParentItemName().length() > 0) {
							getItem(TreeMenu.NEW).setEnabled(true);
						}

						if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && !((SchedulerDom) _dom).isLifeElement()) {
							if (getItemElement() != null) {
								getItem(TreeMenu.DELETE).setEnabled(true);
							}
						}

					}
				}
			}
		});

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getXMLListener());
		item.setText(TreeMenu.EDIT_XML);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getXMLListener());
		item.setText(TreeMenu.SHOW_XML);

		/* item = new MenuItem(_menu, SWT.PUSH);
		 item.addListener(SWT.Selection, getInfoListener());
		 item.setText(TreeMenu.SHOW_INFO);
		 */

	}

	public Menu getMenu() {
		return _menu;
	}

	private void disableMenu() {
		MenuItem[] items = _menu.getItems();
		for (MenuItem item : items)
			item.setEnabled(false);
	}

	private Listener getInfoListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				Element element = getElement();
				if (element != null) {
					try {
						String filename = _dom.transform(element);

						Program prog = Program.findProgram("html");
						if (prog != null)
							prog.execute(filename);
						else {
							filename = new File(filename).toURL().toString();
							Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
						}
					}
					catch (Exception ex) {
						try {
							new ErrorLog("error in " + SOSClassUtil.getMethodName(), ex);
						}
						catch (Exception ee) {
							// tu nichts
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

			}
			catch (JDOMException ex) {
				try {
					new ErrorLog("error in " + SOSClassUtil.getMethodName(), ex);
				}
				catch (Exception ee) {
					// tu nichts
				}
				message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}

		}
		return xml;
	}

	private String getXML(final Element element) {

		String xml = "";
		if (element != null) {
			try {
				if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {

					xml = _dom.getXML(Utils.getHotFolderParentElement(element));
				}
				else {
					xml = _dom.getXML(element);
				}

			}
			catch (JDOMException ex) {
				try {
					new ErrorLog("error in " + SOSClassUtil.getMethodName(), ex);
				}
				catch (Exception ee) {
					// tu nichts
				}
				message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}

		}
		return xml;
	}

	private Listener getXMLListener() {

		return new Listener() {
			@Override
			public void handleEvent(final Event e) {

				MenuItem i = (MenuItem) e.widget;
				Element element = null;
				String xml = null;
				if (i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML)) {
					if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom
							&& (((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement() || ((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory())) {
						element = getElement();

					}
					else {
						element = _dom.getRoot().getChild("config");
					}
					if (element != null) {
						xml = getXML(element);
					}

				}
				else {
					element = getElement();
					if (element != null) {
						xml = getXML(element);

					}

				}

				if (element != null) {
					if (xml == null) // error
						return;

					String selectStr = Utils.getAttributeValue("name", getElement());
					selectStr = selectStr == null || selectStr.length() == 0 ? getElement().getName() : selectStr;

					String newXML = Utils.showClipboard(xml, _tree.getShell(), i.getText().equalsIgnoreCase(TreeMenu.EDIT_XML), selectStr);

					// newXML ist null, wenn Änderungen nicht übernommen werden
					// sollen
					if (newXML != null)
						applyXMLChange(newXML);

				}
			}

		};
	}

	private void applyXMLChange(String newXML) {
		String newName = "";
		String oldname = "";
		try {
			if (_dom instanceof SchedulerDom) {

				newName = getHotFolderName(newXML);

				if (((sos.scheduler.editor.conf.SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {
					String enco = " ";
					if (newXML.indexOf("?>") > -1) {
						enco = newXML.substring(0, newXML.indexOf("?>") + "?>".length());
						newXML = newXML.substring(newXML.indexOf("?>") + "?>".length());
					}
					String xml = "";
					if (((SchedulerDom) _dom).isDirectory())
						xml = Utils.getElementAsString(_dom.getRoot().getChild("config"));
					else
						xml = Utils.getElementAsString(_dom.getRoot());

					String oldxml = Utils.getElementAsString(getElement());
					oldname = getHotFolderName(oldxml);

					int iPosBegin = 0;
					if (oldxml.indexOf("\r\n") > -1)
						iPosBegin = xml.indexOf(oldxml.substring(0, oldxml.indexOf("\r\n")));
					else
						iPosBegin = xml.indexOf(oldxml);

					if (iPosBegin == -1)
						iPosBegin = 0;

					// int iPosEnd = xml.indexOf("</"+ getElement().getName() +
					// ">", iPosBegin) + (("</"+ getElement().getName() +
					// ">").length()) ;
					int iPosEnd = xml.indexOf("</" + getElement().getName() + ">", iPosBegin);
					if (iPosEnd == -1) {
						// hat keinen Kindknoten
						iPosEnd = xml.indexOf("/>", iPosBegin) + "/>".length();
					}
					else {
						iPosEnd = iPosEnd + ("</" + getElement().getName() + ">").length();
					}
					/*
					 * System.out.println("****************************************"
					 * ); System.out.println("Begin : "); System.out.println(
					 * xml.substring(0, iPosBegin));
					 * System.out.println("End   : "); System.out.println(
					 * xml.substring(iPosEnd));
					 * System.out.println("****************************************"
					 * );
					 */

					// int iPosEnd = iPosBegin + oldxml.length();
					newXML = xml.substring(0, iPosBegin) + newXML + xml.substring(iPosEnd);
					// snewXML = snewXML + newXML + xml.substring(iPosEnd);

					if (((SchedulerDom) _dom).isLifeElement())
						newXML = enco + newXML;
					else
						newXML = enco + "<spooler>" + newXML + "</spooler>";
					/*
					 * if(!oldname.equals(newName))
					 * ((sos.scheduler.editor.conf.SchedulerDom
					 * )_dom).setChangedForDirectory("job", oldname,
					 * SchedulerDom.DELETE);
					 *
					 * ((sos.scheduler.editor.conf.SchedulerDom)_dom).
					 * setChangedForDirectory("job", newName,
					 * SchedulerDom.MODIFY);
					 */
					// System.out.println(newXML);
				}
				else
					if (!((sos.scheduler.editor.conf.SchedulerDom) _dom).isLifeElement()) {
						newXML = newXML.replaceAll("\\?>", "?><spooler>") + "</spooler>";
					}

			}

			// System.out.println("debug: \n" + newXML);

			_dom.readString(newXML, true);

			objSchedulerForm.update();
			_dom.setChanged(true);
			Element elem = null;
			if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
				elem = getElement();
				if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName)
						&& (elem.getName().equals("order") || elem.getName().equals("add_order"))) {
					((SchedulerDom) _dom).setChangedForDirectory(elem.getName(),
							Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
					((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
				}
				else
					if (!newName.equals("") && !Utils.getAttributeValue("name", elem).equals(newName)) {
						((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
						((SchedulerDom) _dom).setChangedForDirectory(elem.getName(), newName, SchedulerDom.NEW);
					}
					else
						((SchedulerDom) _dom).setChangedForDirectory(elem, SchedulerDom.NEW);
			}

			if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement() && oldname != null && newName != null && !oldname.equals(newName)
					&& _dom.getFilename() != null) {

				String parent = "";
				if (_dom.getFilename() != null && new File(_dom.getFilename()).getParent() != null)
					parent = new File(_dom.getFilename()).getParent();

				File oldFilename = new File(parent, oldname + "." + getElement().getName() + ".xml");

				File newFilename = null;
				if (oldFilename != null && oldFilename.getParent() != null)
					newFilename = new File(oldFilename.getParent(), newName + "." + getElement().getName() + ".xml");
				else
					newFilename = new File(parent, newName + "." + getElement().getName() + ".xml");

				int c = MainWindow.message(MainWindow.getSShell(),
						"Do you want really rename Hot Folder File from " + oldFilename + " to " + newFilename + "?", SWT.ICON_WARNING | SWT.YES | SWT.NO);
				if (c == SWT.YES) {
					objSchedulerForm.updateJob(newName);
					if (_dom.getFilename() != null) {
						oldFilename.renameTo(newFilename);
						// _dom.setFilename(_dom.getFilename().replaceAll(new
						// File(_dom.getFilename()).getName(), (newName + "." +
						// getElement().getName() + ".xml")));
						_dom.setFilename(newFilename.getCanonicalPath());
					}
					// _gui.updateTree("main");
					// _dom.setChanged(false);
					if (MainWindow.getContainer().getCurrentEditor().applyChanges()) {
						MainWindow.getContainer().getCurrentEditor().save();
						MainWindow.setSaveStatus();
					}

				}
				else {
					return;
				}

			}

			objSchedulerForm.update();
			objSchedulerForm.updateTree("main");

			refreshTree("main");

		}
		catch (Exception de) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), de);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message(MainWindow.getSShell(), "..error while update XML: " + de.getMessage(), SWT.ICON_WARNING);
		}
	}

	private Listener getCopyListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				Element element = getElement();
				if (element != null) {
					_copy = (Element) element.clone();
					_type = ((TreeData) _tree.getSelection()[0].getData()).getType();
				}
			}
		};
	}

	// TreeData data = (TreeData)_tree.getSelection()[0].getData();
	// sos.scheduler.editor.conf.ISchedulerUpdate update = _gui;

	private Listener getNewItemSelection() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {

				String name = getParentItemName();

				if (name.equals(SchedulerListener.JOBS)) {
					JobsListener listeners = new JobsListener((SchedulerDom) _dom, objSchedulerForm);
					//					listeners.newJob(JobsForm.getTable(), false);
					listeners.newJob(false);
				}
				else
					if (name.equals(SchedulerListener.MONITOR)) {
						TreeData data = (TreeData) _tree.getSelection()[0].getData();
						PreProstProcessingListener listener = new PreProstProcessingListener((SchedulerDom) _dom, objSchedulerForm, data.getElement());
						//						Table table = ScriptsForm.getTable();
						//						listener.save(table, "monitor" + table.getItemCount(), String.valueOf(table.getItemCount()), null);
						// TODO Baustelle
						listener.save(null, "monitor" + "???", "99", null);
					}
					else
						if (name.equals(SchedulerListener.JOB_CHAINS)) {
							TreeData data = (TreeData) _tree.getSelection()[0].getData();
							com.sos.joe.objects.jobchain.JobChainListListener listeners = new com.sos.joe.objects.jobchain.JobChainListListener(
									(SchedulerDom) _dom, data.getElement(), objSchedulerForm);
							listeners.newChain("newChain");
							int i = 1;
							if (data.getElement().getChild("job_chains") != null)
								i = data.getElement().getChild("job_chains").getChildren("job_chain").size() + 1;
							listeners.applyChain("job_chain" + i, true, true);
							listeners.populateJobChainsTable(JobChainsForm.getTableChains());

						}
						else
							if (name.equals(SchedulerListener.SCHEDULES)) {
								SchedulesListener listener = new SchedulesListener((SchedulerDom) _dom, objSchedulerForm);
								listener.newScheduler(sos.scheduler.editor.conf.forms.SchedulesForm.getTable());
							}
							else
								if (name.equals(SchedulerListener.ORDERS)) {
									sos.scheduler.editor.conf.listeners.OrdersListener listener = new sos.scheduler.editor.conf.listeners.OrdersListener(
											(SchedulerDom) _dom, objSchedulerForm);
									listener.newCommands(sos.scheduler.editor.conf.forms.OrdersForm.getTable());
								}
								else
									if (name.equals(SchedulerListener.WEB_SERVICES)) {
										TreeData data = (TreeData) _tree.getSelection()[0].getData();
										sos.scheduler.editor.conf.listeners.WebservicesListener listener = new sos.scheduler.editor.conf.listeners.WebservicesListener(
												(SchedulerDom) _dom, data.getElement(), objSchedulerForm);
										listener.newService(sos.scheduler.editor.conf.forms.WebservicesForm.getTable());
									}
									else
										if (name.equals(SchedulerListener.LOCKS)) {
											try {
												TreeData data = (TreeData) _tree.getSelection()[0].getData();
												sos.scheduler.editor.conf.listeners.LocksListener listener = new sos.scheduler.editor.conf.listeners.LocksListener(
														(SchedulerDom) _dom, data.getElement());
												listener.newLock();
												int i = 1;
												if (data.getElement().getChild("locks") != null)
													i = data.getElement().getChild("locks").getChildren("lock").size() + 1;
												listener.applyLock("lock_" + i, 0, true);
												listener.fillTable(sos.scheduler.editor.conf.forms.LocksForm.getTable());
												listener.selectLock(i - 1);
											}
											catch (Exception es) {
												new ErrorLog("error in " + SOSClassUtil.getMethodName(), es);
											}
										}
										else {
											if (name.equals(SchedulerListener.PROCESS_CLASSES)) {
												TreeData data = (TreeData) _tree.getSelection()[0].getData();
												try {
													sos.scheduler.editor.conf.listeners.ProcessClassesListener listener = new sos.scheduler.editor.conf.listeners.ProcessClassesListener(
															(SchedulerDom) _dom, data.getElement());
													listener.newProcessClass();
													int i = 1;
													if (data.getElement().getChild("process_classes") != null)
														i = data.getElement().getChild("process_classes").getChildren("process_class").size() + 1;
													listener.applyProcessClass("processClass_" + i, "", "", 0);
													listener.fillTable(sos.scheduler.editor.conf.forms.ProcessClassesForm.getTable());
													listener.selectProcessClass(i - 1);

												}
												catch (Exception es) {
													new ErrorLog("error in " + SOSClassUtil.getMethodName(), es);
												}

											}
										}

			}
		};
	}

	private Listener getDeleteSelection() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				Element elem = getItemElement();
				String name = elem.getName();

				int c = MainWindow.message("Do you want remove the " + name + "?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				if (c != SWT.YES)
					return;

				if (name.equals("job")) {
					/*
					 * if (_tree.getSelection()[0] != null &&
					 * !_tree.getSelection
					 * ()[0].getText().equals(SchedulerListener.JOB)) { TreeData
					 * data = (TreeData)_tree.getSelection()[0].getData();
					 * ((SchedulerDom)_dom).setChangedForDirectory("job",
					 * Utils.getAttributeValue("name", elem)
					 * ,SchedulerDom.MODIFY); elem.detach(); //List el =
					 * elem.getChildren
					 * (_tree.getSelection()[0].getText().toLowerCase());
					 * //if(el != null) //el.clear();
					 *
					 * //getElement().detach(); } //TreeData data =
					 * (TreeData)_tree.getSelection()[0].getData(); else {
					 */
					_dom.setChanged(true);
					((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
					elem.detach();

					// }

					objSchedulerForm.updateJobs();

				}
				else
					if (name.equals("monitor")) {
						_dom.setChanged(true);
						((SchedulerDom) _dom).setChangedForDirectory("job", Utils.getAttributeValue("name", elem.getParentElement()), SchedulerDom.MODIFY);
						elem.detach();

						objSchedulerForm.updateJobs();

					}
					else
						if (name.equals("job_chain")) {

							// TreeData data =
							// (TreeData)_tree.getSelection()[0].getData();
							// data.getElement().detach();
							_dom.setChanged(true);
							((SchedulerDom) _dom).setChangedForDirectory("job_chain", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
							elem.detach();
							TreeItem parentItem = _tree.getSelection()[0].getParentItem();
							_tree.setSelection(new TreeItem[] { parentItem });
							if (parentItem.getItemCount() == 1)// job_chains Element hat
																// keine weiteren
																// Kindelemente
								((TreeData) parentItem.getData()).getElement().getChild("job_chains").detach();
							objSchedulerForm.updateJobChains();
							objSchedulerForm.updateCMainForm();

						}
						else
							if (name.equals("schedule")) {

								_dom.setChanged(true);
								((SchedulerDom) _dom).setChangedForDirectory("schedule", Utils.getAttributeValue("name", elem), SchedulerDom.DELETE);
								elem.detach();
								TreeItem parentItem = _tree.getSelection()[0].getParentItem();
								_tree.setSelection(new TreeItem[] { parentItem });
								if (parentItem.getItemCount() == 1)// job_chains Element hat
																	// keine weiteren
																	// Kindelemente
									((TreeData) parentItem.getData()).getElement().getChild("schedules").detach();
								objSchedulerForm.updateSchedules();
								objSchedulerForm.updateCMainForm();

							}
							else
								if (name.equals("order") || name.equals("add_order")) {

									_dom.setChanged(true);
									((SchedulerDom) _dom).setChangedForDirectory("order",
											Utils.getAttributeValue("job_chain", elem) + "," + Utils.getAttributeValue("id", elem), SchedulerDom.DELETE);
									elem.detach();

									TreeItem parentItem = _tree.getSelection()[0].getParentItem();
									_tree.setSelection(new TreeItem[] { parentItem });

									if (parentItem.getItemCount() == 1)// job_chains Element hat
																		// keine weiteren
																		// Kindelemente
										((TreeData) parentItem.getData()).getElement().getChild("commands").detach();

									objSchedulerForm.updateOrders();
									objSchedulerForm.updateCMainForm();

								}
								else
									if (name.equals("web_service")) {

										// TreeData data =
										// (TreeData)_tree.getSelection()[0].getData();
										_dom.setChanged(true);
										elem.detach();

										TreeItem parentItem = _tree.getSelection()[0].getParentItem();
										_tree.setSelection(new TreeItem[] { parentItem });

										// if(parentItem.getItemCount() == 1)//job_chains Element
										// hat keine weiteren Kindelemente
										// ((TreeData)parentItem.getData()).getElement().getChild("Web Services").detach();

										objSchedulerForm.updateWebServices();
										objSchedulerForm.updateCMainForm();

									}

			}
		};
	}

	private Listener getDeleteHoltFolderFileListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {

				String filename = _dom.getFilename();

				if (filename == null) {
					MainWindow.message("file name is null. Could not remove file.", SWT.ICON_WARNING | SWT.OK);
					return;
				}

				int ok = MainWindow.message("Do you want really remove live file: " + filename, //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);

				if (ok == SWT.CANCEL || ok == SWT.NO)
					return;

				if (!new java.io.File(filename).delete()) {
					MainWindow.message("could not remove live file", SWT.ICON_WARNING | SWT.OK);
				}
				if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement()) {
					sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
					con.getCurrentTab().dispose();
				}

			}
		};
	}

	private Listener getClipboardListener() {
		return new Listener() {
			@Override
			public void handleEvent(final Event e) {
				Element element = getElement();

				if (element != null) {
					String xml = getXML();
					if (xml == null) // error
						return;

					if (!element.getName().equals("config") && xml.indexOf("<?xml") > -1) {
						xml = xml.substring(xml.indexOf("?>") + 2);
					}
					Utils.copyClipboard(xml, _tree.getDisplay());

				}
			}
		};
	}

	private Listener getPasteListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event e) {

				if (_tree.getSelectionCount() == 0)
					return;

				TreeData data = (TreeData) _tree.getSelection()[0].getData();

				boolean override = false;

				if (_tree.getSelection()[0].getData("override_attributes") != null)
					override = _tree.getSelection()[0].getData("override_attributes").equals("true");

				if (_tree.getSelection()[0].getData("key") instanceof String) {
					// ein Formular hat nur einen Kindknoten Element zum
					// Kopieren, z.B. job Formular
					String key = _tree.getSelection()[0].getData("key").toString();
					paste(key, data, override);
				}
				else
					if (_tree.getSelection()[0].getData("key") instanceof ArrayList) {
						// ein Formular hat mehreren Kindknoten Element zum
						// Kopieren, z.B. job Run Option ->
						// start_when_directory_changed; delay_after_error;
						// delay_order_after_setback
						ArrayList keys = (ArrayList) _tree.getSelection()[0].getData("key");
						for (int i = 0; i < keys.size(); i++) {
							paste(keys.get(i).toString(), data, override);
						}
					}

				if (_dom instanceof SchedulerDom && (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement())) {
					Utils.setChangedForDirectory(data.getElement(), (SchedulerDom) _dom);
				}

			}

		};
	}

	/*
	 * private Listener getPasteListener() { return new Listener() { public void
	 * handleEvent(Event e) { Element target = getElement();
	 *
	 * if ((target != null && _copy != null)) { String tName = target.getName();
	 * String cName = _copy.getName();
	 *
	 * if(_dom instanceof SchedulerDom && ((SchedulerDom)_dom).isLifeElement())
	 * {
	 *
	 * //if(cName.equals("job")) { target = (Element)_copy.clone(); TreeData
	 * data = (TreeData) _tree.getSelection()[0].getData();
	 * data.setElement(target); _gui.update(); _gui.updateLifeElement();
	 * //_gui.updateJob(target.getName()); return; //} }
	 *
	 * if (tName.equals("jobs") && cName.equals("job")) { // copy job
	 *
	 * String append = "copy(" + (target.getChildren("job").size() + 1); Element
	 * currCopy = (Element)_copy.clone();
	 *
	 * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
	 * currCopy.setAttribute("name", append + ")of_" +
	 * Utils.getAttributeValue("name", _copy));
	 *
	 * target.addContent(currCopy);
	 *
	 * refreshTree("jobs"); _gui.update(); if(_dom instanceof SchedulerDom &&
	 * !((SchedulerDom)_dom).isLifeElement()) _gui.updateJobs();
	 * _dom.setChanged(true);
	 *
	 * } else if (tName.equals("job") && cName.equals("run_time")) { // copy //
	 * run_time target.removeChildren("run_time"); target.addContent(_copy);
	 * _gui.updateJob(); _dom.setChanged(true); } else if
	 * (tName.equals("config") && cName.equals("config")) { // copy // run_time
	 * //target.getParentElement().removeContent(); Element spooler =
	 * target.getParentElement(); spooler.removeChildren("config");
	 * spooler.addContent((Element)_copy.clone());
	 *
	 * refreshTree("main"); _dom.setChanged(true);
	 *
	 * _gui.update(); } else if (tName.equals("commands") &&
	 * cName.equals("order")) { // copy job
	 *
	 * String append = "copy(" + (target.getChildren("order").size() + 1);
	 * Element currCopy = (Element)_copy.clone();
	 *
	 *
	 * currCopy.setAttribute("id", append + ")of_" +
	 * Utils.getAttributeValue("id", _copy));
	 *
	 * target.addContent(currCopy);
	 *
	 * refreshTree("main"); _gui.updateCommands(); _gui.updateOrders();
	 * _gui.update(); _dom.setChanged(true);
	 *
	 * } else if (tName.equals("job_chains") && cName.equals("job_chain")) { //
	 * copy job
	 *
	 * String append = "copy(" + (target.getChildren("job_chain").size() + 1);
	 * Element currCopy = (Element)_copy.clone();
	 *
	 * if(existJobname(target, Utils.getAttributeValue("name", _copy)))
	 * currCopy.setAttribute("name", append + ")of_" +
	 * Utils.getAttributeValue("name", _copy));
	 *
	 * target.addContent(currCopy);
	 *
	 * _gui.updateJobChains(); refreshTree("main"); _gui.update();
	 * _dom.setChanged(true);
	 *
	 * } } } }; }
	 */

	private boolean existJobname(final Element jobs, final String jobname) {
		boolean retVal = false;
		java.util.List list = jobs.getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element job = (Element) list.get(i);
			if (Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	private void refreshTree(final String whichItem) {

		sos.scheduler.editor.app.IContainer con = MainWindow.getContainer();
		SchedulerForm sf = (SchedulerForm) con.getCurrentEditor();
		sf.updateTree(whichItem);

	}

	private MenuItem getItem(final String name) {
		MenuItem[] items = _menu.getItems();
		for (MenuItem item : items) {
			if (item.getText().equalsIgnoreCase(name)) {
				return item;
			}
		}
		return null;
	}

	private boolean isParent(String key, final String elemname) {

		// String[] split = key.split("\\.");
		String[] split = key.split("_@_");
		if (split.length > 1)
			key = split[0];

		String[] s = null;
		if (_dom.getDomOrders().get(key) != null)
			s = _dom.getDomOrders().get(key);

		else
			if (sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key) != null)
				// s =
				// (String[])sos.scheduler.editor.conf.listeners.DaysListener.getDays().get(key);
				return true; // group sind nicht definiert
			else
				s = getPossibleParent(key);
		for (int i = 0; s != null && i < s.length; i++) {
			if (s[i].equalsIgnoreCase(elemname)) {
				return true;
			}
		}
		return false;
	}

	// liefert mögliche Vaterknoten der key, falls diese nicht in dom.orders
	// steht
	private String[] getPossibleParent(final String key) {
		if (key.equals("jobs") || key.equals("monitor"))
			return new String[] { "job" };
		if (key.equals("schedules"))
			return new String[] { "schedule" };
		if (key.equals("job_chains"))
			return new String[] { "job_chain" };

		// weekdays monthdays ultimos

		else
			return new String[] {};
	}

	private void paste(String key, final TreeData data, final boolean overrideAttributes) {

		try {
			// ungleiche Typen, überprüfen, ob das pastelement ein möglicher
			// Vaterknoten von _copy element ist, z.B. _copy Element ist job und
			// paste Element ist jobs
			if (_type != data.getType()) {
				// System.out.println("*****************************************");
				pasteChild(key, data);
				return;
			}

			// ab hier gleiche typen
			Element target = _copy;

			boolean isLifeElement = _dom instanceof SchedulerDom && ((SchedulerDom) _dom).isLifeElement();

			if (key.equalsIgnoreCase(target.getName()) && !isLifeElement) {
				// Kopieren nur von Attributen und nicht der Kindelement, z.B.
				// Config Formular
				Element currElem = data.getElement();
				removeAttributes(currElem);
				copyAttr(currElem, _copy.getAttributes());

			}
			else {

				// gleiche Typen und und gleiche Elementname -> alle vorhandenen
				// Kindelement kopieren
				Element currElem = data.getElement();

				Element copyElement = _copy;

				String[] split = null;

				// split = key.split("\\.");
				split = key.split("_@_");

				// System.out.println("*****************************************");

				if (split.length > 1)
					key = split[split.length - 1];
				java.util.List ce = null;
				if (key.equals(copyElement.getName())) {
					// überschreiben: z.B. copy ist job element und paste ist
					// auch Job element
					removeAttributes(currElem);
					currElem.removeContent();
					copyAttr(currElem, copyElement.getAttributes());
					ce = copyElement.getChildren();
				}
				else {

					for (int i = 0; i < split.length - 1; i++) {
						// key ist der Pfad ab data.getelement.
						copyElement = copyElement.getChild(split[i]);
						// System.out.println(Utils.getElementAsString(_dom.getRoot()));
						if (currElem.getChild(split[i]) == null) {
							currElem = currElem.addContent(new Element(split[i]));
						}
						currElem = data.getElement().getChild(split[i]);
						// System.out.println(Utils.getElementAsString(_dom.getRoot()));
					}
					ce = copyElement.getChildren(key);
				}

				for (int i = 0; i < ce.size(); i++) {
					Element a = (Element) ce.get(i);
					Element cloneElement = (Element) a.clone();
					java.util.List currElemContent = null;
					if (_tree.getSelection()[0].getData("max_occur") != null && _tree.getSelection()[0].getData("max_occur").equals("1")) {
						// es darf nur einen currElem.get(key) Kindknoten geben.
						// Also attribute löschen aber die Kinder mitnehmen
						if (currElem.getChild(key) != null)
							currElemContent = currElem.getChild(key).cloneContent();
						currElem.removeChild(key);
					}

					//
					// Element copyClone = (Element)_copy.clone();

					if (!Utils.getAttributeValue("name", cloneElement).equals("") && existJobname(currElem, Utils.getAttributeValue("name", cloneElement))) {
						// System.out.println(Utils.getAttributeValue("name",
						// cloneElement) + " existiert berteits");
						String append = "copy(" + (cloneElement.getChildren("job").size() + currElem.getChildren().size() + 1) + ")of_"
								+ Utils.getAttributeValue("name", cloneElement);
						cloneElement.setAttribute("name", append);
					}
					//

					currElem.addContent(cloneElement);
					if (currElemContent != null)
						cloneElement.addContent(currElemContent);

					if (overrideAttributes) {
						copyElement = cloneElement;
						currElem = currElem.getChild(key);
					}

					// System.out.println(Utils.getElementAsString(_dom.getRoot()));
				}

				if (overrideAttributes) {
					removeAttributes(currElem);
					copyAttr(currElem, copyElement.getAttributes());
				}

			}

			updateTreeView(data);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
	}

	// Ein Kindelement hinzufügen, z.B. jobs einen job element oder job_chains
	// einen job_chain element einfügen
	private void pasteChild(final String key, final TreeData data) {
		if (key.equalsIgnoreCase("monitor") && _type != data.getType()) {
			// ausnahme
			data.getElement().addContent((Element) _copy.clone());
		}
		else
			if (!isParent(key, _copy.getName())) {
				return;
			}
			else {

				String[] split = key.split("_@_");
				Element elem = data.getElement();
				for (int i = 0; i < split.length - 1; i++) {
					if (data.getElement().getChild(split[i]) == null)
						data.getElement().addContent(new Element(split[i]));
					elem = data.getElement().getChild(split[i]);
					// System.out.println(Utils.getElementAsString(_dom.getRoot()));
				}

				Element copyClone = (Element) _copy.clone();

				if (!Utils.getAttributeValue("name", _copy).equals("") && existJobname(elem, Utils.getAttributeValue("name", _copy))) {
					// System.out.println(Utils.getAttributeValue("name", _copy) +
					// " existiert berteits");
					String append = "copy(" + (copyClone.getChildren("job").size() + elem.getChildren().size() + 1) + ")of_"
							+ Utils.getAttributeValue("name", copyClone);
					copyClone.setAttribute("name", append);
				}
				else
					if (!Utils.getAttributeValue("id", _copy).equals("")) {
						// System.out.println(Utils.getAttributeValue("name", _copy) +
						// " existiert berteits");
						String append = "copy(" + (elem.getChildren().size() + 1) + ")of_" + Utils.getAttributeValue("id", copyClone);
						copyClone.setAttribute("id", append);
					}

				elem.addContent(copyClone);

			}

		updateTreeView(data);

	}

	/*
	 * private TreeItem getCurrentItem(String text) { TreeItem retVal = null;
	 * for(int i = 0; i < _tree.getItemCount(); i++ ){
	 *
	 * TreeItem item = getCurrentItem(_tree.getItem(i), text); if(item != null
	 * && item.getText().equals(text)) return item; }
	 *
	 * return retVal; }
	 */

	/*
	 * private TreeItem getCurrentItem(TreeItem parent, String text) { TreeItem
	 * retVal = null; if(parent.getText().equals(text)) return parent; for(int i
	 * = 0; i < parent.getItemCount(); i++ ){ return
	 * getCurrentItem(parent.getItem(i), text); }
	 *
	 * return retVal; }
	 */
	private void removeAttributes(final Element elem) {
		List l = elem.getAttributes();
		for (int i = 0; i < l.size(); i++)
			elem.removeAttribute((org.jdom.Attribute) l.get(i));
	}

	private void copyAttr(final Element elem, final java.util.List attr) {
		for (int i = 0; i < attr.size(); i++) {
			org.jdom.Attribute a = (org.jdom.Attribute) attr.get(i);
			elem.setAttribute(a.getName(), a.getValue());
		}
	}

	private void updateTreeView(final TreeData data) {

		// unpdate der Formular
		// String currItemString = _tree.getSelection()[0].getText();

		if (_type == Editor.SPECIFIC_WEEKDAYS)
			objSchedulerForm.updateSpecificWeekdays();

		// if (_dom instanceof SchedulerDom &&
		// ((SchedulerDom)_dom).isLifeElement())
		// return;

		// sucht das Treeitem mit der currItemString um die gleiche Parent zu
		// selektieren.
		// TreeItem item = getCurrentItem(currItemString);
		// if(item != null)
		// _tree.setSelection(new TreeItem [] {item});
		objSchedulerForm.update();

		if (_tree.getSelection()[0].getText().equals("Jobs"))
			objSchedulerForm.updateJobs();

		// if(_copy.getName().equals("job"))
		if (_type == Editor.JOB && !_tree.getSelection()[0].getText().endsWith("Jobs"))
			objSchedulerForm.updateJob();

		if (_type == Editor.SCHEDULES)
			objSchedulerForm.updateSchedules();

		if (_type == Editor.ORDERS)
			objSchedulerForm.updateOrders();

		if (_type == Editor.JOB_CHAINS || _type == Editor.JOB_CHAIN)
			objSchedulerForm.updateJobChains();

		objSchedulerForm.expandItem(_tree.getSelection()[0].getText());
		objSchedulerForm.updateTreeItem(_tree.getSelection()[0].getText());

		objSchedulerForm.updateTree("");

		refreshTree("main");

		if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
			((SchedulerDom) _dom).setChangedForDirectory(data.getElement(), SchedulerDom.NEW);
		}

		_dom.setChanged(true);
	}

	private String getParentItemName() {
		if (_tree.getSelectionCount() > 0) {
			String name = _tree.getSelection()[0].getText();
			if (name.equals(SchedulerListener.JOBS) || name.equals(SchedulerListener.JOB_CHAINS) || name.equals(SchedulerListener.MONITOR)
					|| name.equals(SchedulerListener.ORDERS) || name.equals(SchedulerListener.WEB_SERVICES) || name.equals(SchedulerListener.SCHEDULES)) {
				return name;
			}

			if (_dom instanceof sos.scheduler.editor.conf.SchedulerDom && !((SchedulerDom) _dom).isLifeElement()
					&& (name.equals(SchedulerListener.LOCKS) || name.equals(SchedulerListener.PROCESS_CLASSES)))
				return name;
		}

		return "";
	}

	// liefert den Namen des selektierten Treeitems, wenn diese Job, Job chain
	// order, add_order, schedule ist
	private Element getItemElement() {
		if (_tree.getSelectionCount() > 0) {
			Element elem = ((TreeData) _tree.getSelection()[0].getData()).getElement();
			String name = elem.getName();
			if (name.equals("job") || name.equals("job_chain") || name.equals("order") || name.equals("add_order") || name.equals("web_service")
					|| name.equals("monitor") || name.equals("schedule")) {
				return elem;
			}
		}
		return null;
	}

	// liefert den namen des Hot Folders
	private String getHotFolderName(final String newXML) {
		String newName = "";

		if (_dom instanceof SchedulerDom) {

			if (((SchedulerDom) _dom).isDirectory() || ((SchedulerDom) _dom).isLifeElement()) {

				if (getElement().getName().equals("order") || getElement().getName().equals("add_order")) {

					int i1 = -1;
					int i2 = -1;

					i1 = newXML.indexOf("id=\"");
					i2 = newXML.indexOf("\"", i1 + "id=\"".length());
					if (i1 > +"id=\"".length() && i2 > i1)
						newName = newXML.substring(i1 + "id=\"".length(), i2) + ",";

					i1 = -1;
					i2 = -1;
					i1 = newXML.indexOf("job_chain=\"");
					i2 = newXML.indexOf("\"", i1 + "job_chain=\"".length());
					if (i1 > +"job_chain=\"".length() && i2 > i1)
						newName = newName + newXML.substring(i1 + +"job_chain=\"".length(), i2);

				}
				else {
					int i1 = newXML.indexOf("name=\"") + "name=\"".length();
					int i2 = newXML.indexOf("\"", i1);
					newName = newXML.substring(i1, i2);
				}
			}
		}
		return newName;
	}
}