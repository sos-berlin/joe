package sos.scheduler.editor.conf.listeners;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.JSObjectElement;
import sos.scheduler.editor.conf.forms.BaseForm;
import sos.scheduler.editor.conf.forms.ClusterForm;
import sos.scheduler.editor.conf.forms.CommandsForm;
import sos.scheduler.editor.conf.forms.ConfigForm;
import sos.scheduler.editor.conf.forms.DateForm;
import sos.scheduler.editor.conf.forms.DaysForm;
import sos.scheduler.editor.conf.forms.HttpAuthenticationForm;
import sos.scheduler.editor.conf.forms.HttpDirectoriesForm;
import sos.scheduler.editor.conf.forms.JobCommandForm;
import sos.scheduler.editor.conf.forms.JobCommandsForm;
import sos.scheduler.editor.conf.forms.JobLockUseForm;
import sos.scheduler.editor.conf.forms.LocksForm;
import sos.scheduler.editor.conf.forms.MailForm;
import sos.scheduler.editor.conf.forms.OrderForm;
import sos.scheduler.editor.conf.forms.OrdersForm;
import sos.scheduler.editor.conf.forms.ParameterForm;
import sos.scheduler.editor.conf.forms.PeriodsForm;
import sos.scheduler.editor.conf.forms.ProcessClassesForm;
import sos.scheduler.editor.conf.forms.RunTimeForm;
import sos.scheduler.editor.conf.forms.ScheduleForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.forms.SchedulesForm;
import sos.scheduler.editor.conf.forms.ScriptFormPreProcessing;
import sos.scheduler.editor.conf.forms.ScriptFormSchedulerStartScript;
import sos.scheduler.editor.conf.forms.ScriptsForm;
import sos.scheduler.editor.conf.forms.SecurityForm;
import sos.scheduler.editor.conf.forms.SpecificWeekdaysForm;
import sos.scheduler.editor.conf.forms.WebserviceForm;
import sos.scheduler.editor.conf.forms.WebservicesForm;
import sos.util.SOSClassUtil;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.objects.job.forms.JobDocumentationForm;
import com.sos.joe.objects.job.forms.JobMainOptionForm;
import com.sos.joe.objects.job.forms.JobOptionsForm;
import com.sos.joe.objects.job.forms.JobParameterForm;
import com.sos.joe.objects.job.forms.JobsForm;
import com.sos.joe.objects.job.forms.ScriptJobMainForm;
import com.sos.joe.objects.jobchain.forms.JobChainNestedNodesForm;
import com.sos.joe.objects.jobchain.forms.JobChainNodesForm;
import com.sos.joe.objects.jobchain.forms.JobChainsForm;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.commands.AddJobs;
import com.sos.scheduler.model.commands.CheckFolders;
import com.sos.scheduler.model.objects.Commands;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.JSObjJobChain;
import com.sos.scheduler.model.objects.JSObjOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Job;

/*
 * jedes Treeitem muss folgende daten speichern, um die kopie und paste Funktion vollständig zu gewährleisten:
 *
 * item.setData("copy_element", element); -> Element zum kopieren
 * item.setData("key", "run_time"); -> name des Elements bzw. Pfad vom copy_element aus. Beispiel Element ist job, dann muss Parameter den key = params_@_param haben
 * item.setData("override_attributes", "true"); -> optional: default ist false. Wenn true angegeben wird, dann werden die Attribute überschrieben. z.B. runtime
 * item.setData("max_occur", "1"); -> Das Element darf max. einmal vorkommen: z.B. process_classes.
 *
 */
public class SchedulerListener { 
	private static final String	conItemDataKeyVISIBLE				= "visible";
	private static final String	conItemDataKeyORDERS_RECOVERABLE	= "orders_recoverable";
	private static final String	conItemDataKeyOVERRIDE_ATTRIBUTES	= "override_attributes";
	private static final String	conItemDataKeyMAX_OCCUR				= "max_occur";
	private static final String	conItemDataKeyKEY					= "key";
	private static final String	conItemDataKeyCOPY_ELEMENT			= "copy_element";
	@SuppressWarnings("unused")
	private final String		conSVNVersion						= "$Id$";
	@SuppressWarnings("unused")
	private final String		conClassName						= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private final Logger		logger								= Logger.getLogger(this.getClass());
	private SchedulerDom		objSchedulerDom						= null;
	private SchedulerForm		objSchedulerForm					= null;
	//    public static String        JOBS                              = "Jobs";
	public static String		JOBS								= SOSJOEMessageCodes.JOE_L_SchedulerListener_Jobs.label();
	//    public static String        JOB_CHAINS                        = "Job Chains";
	public static String		JOB_CHAINS							= SOSJOEMessageCodes.JOE_L_SchedulerListener_JobChains.label();
	//    public static String        HTTP_SERVER                       = "HttpServer";
	public static String		HTTP_SERVER							= SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPServer.label();
	//    public static String        WEB_SERVICES                      = "WebServices";
	public static String		WEB_SERVICES						= SOSJOEMessageCodes.JOE_L_SchedulerListener_Webservices.label();
	//    public static String        SCHEDULES                         = "Schedules";
	public static String		SCHEDULES							= SOSJOEMessageCodes.JOE_L_SchedulerListener_Schedules.label();
	//    public static String        ORDERS                            = "Job Chain Orders";
	public static String		ORDERS								= SOSJOEMessageCodes.JOE_L_SchedulerListener_JobChainOrders.label();
	//    public static String        LOCKS                             = "Locks";
	public static String		LOCKS								= SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label();
	//    public static String        PROCESS_CLASSES                   = "treeitem.ProcessClasses";
	public static String		PROCESS_CLASSES						= SOSJOEMessageCodes.JOE_L_SchedulerListener_ProcessClasses.label();
	//    public static String        MONITOR                           = "Pre-/Post-Processing";
	public static String		MONITOR								= SOSJOEMessageCodes.JOE_M_SchedulerListener_PrePostProcessing.label();
	/** Aufruf erfolgt durch open Directory oder open Configurations*/
	private int					type								= -1;
	public SchedulerHotFolder	objSchedulerHotFolder				= null;
	public class CompareNameAndTitle implements Comparator<Element> {
		@Override public int compare(final Element o1, final Element o2) {
			Element element1 = o1;
			Element element2 = o2;
			String e1 = new JSObjectElement(0, element1).getNameAndTitle();
			String e2 = new JSObjectElement(0, element2).getNameAndTitle();
			// JIRA  http://www.sos-berlin.com/jira/browse/JOE-25
			return e1.toLowerCase().compareTo(e2.toLowerCase());
		}
	}

	public SchedulerListener(final SchedulerForm gui, final SchedulerDom dom) {
		objSchedulerForm = gui;
		objSchedulerDom = dom;
	}

	public void treeFillMain1(final Tree tree, final Composite c, final int type_) {
		final String conMethodName = conClassName + "::treeFillMain";
		logger.debug(SOSJOEMessageCodes.JOE_M_0047.params(conMethodName));
		type = type_;
		if (objSchedulerDom.isLifeElement()) {
			// TODO needed?
			// treeFillMainForLifeElement(tree, c);
		}
		else {
			treeFillMain1(tree, c, objSchedulerHotFolder);
		}
	}

	//	public void treeFillMain(final Tree tree, final Composite c, final SchedulerHotFolder pobjHotFolder) {
	//
	//		final String conMethodName = conClassName + "::treeFillMain";
	//
	//		tree.removeAll();
	//		Element element = objSchedulerDom.getRoot();
	//		TreeItem item = new TreeItem(tree, SWT.NONE);
	//
	//		if (type == SchedulerDom.LIVE_JOB) {
	//			String name = "";
	//			if (objSchedulerDom.getFilename() != null && new File(objSchedulerDom.getFilename()).exists()) {
	//				name = new File(objSchedulerDom.getFilename()).getName();
	//				name = name.substring(0, name.indexOf(".job.xml"));
	//				checkLifeAttributes(element, name);
	//				Utils.setAttribute("name", name, element);
	//			}
	//			else {
	//				name = Utils.getAttributeValue("name", element);
	//			}
	//
	//			TreeData objTD = new TreeData(item, JOEConstants.JOB, element, Options.getHelpURL("job"));
	//			item.setData(conItemDataKeyKEY, "job");
	//
	//			setColorOfJobTreeItem(element, item);
	//
	//			treeFillJobChilds(item, element, false);
	//			item.setExpanded(true);
	//		}
	//		else
	//			if (type == SchedulerDom.LIVE_JOB_CHAIN) {
	//				String name = "";
	//				if (objSchedulerDom.getFilename() != null && new File(objSchedulerDom.getFilename()).exists()) {
	//					name = new java.io.File(objSchedulerDom.getFilename()).getName();
	//					name = name.substring(0, name.indexOf(".job_chain.xml"));
	//					checkLifeAttributes(element, name);
	//					Utils.setAttribute("name", name, element);
	//				}
	//				else {
	//					name = Utils.getAttributeValue("name", element);
	//				}
	//				item.setImage(getImage("jobchain.gif"));
	//				TreeData objTD = new TreeData(item, JOEConstants.JOB_CHAIN, element, Options.getHelpURL("job_chain"));
	//				item.setData(conItemDataKeyKEY, "job_chain");
	//
	//				Utils.setAttribute(conItemDataKeyORDERS_RECOVERABLE, true, element);
	//				Utils.setAttribute(conItemDataKeyVISIBLE, true, element);
	//				if (!Utils.isElementEnabled("job_chain", objSchedulerDom, element)) {
	//					setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//				}
	//				else {
	//					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//				}
	//				// Job Chain Nodes
	//				TreeItem in = new TreeItem(item, SWT.NONE);
	//				in.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StepsNodes.label());
	//				in.setImage(getImage("jobchain.gif"));
	//				in.setData(new TreeData(JOEConstants.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain")));
	//				in.setData(conItemDataKeyKEY, "job_chain_node");
	//				in.setData(conItemDataKeyCOPY_ELEMENT, element);
	//				// Job Chain Nested Nodes
	//				TreeItem iNestedNodes = new TreeItem(item, SWT.NONE);
	//				iNestedNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_NestedJobChains.label());
	//				iNestedNodes.setImage(getImage("jobchain.gif"));
	//				iNestedNodes.setData(new TreeData(JOEConstants.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("job_chain")));
	//				iNestedNodes.setData(conItemDataKeyKEY, "job_chain_node.job_chain");
	//				iNestedNodes.setData(conItemDataKeyCOPY_ELEMENT, element);
	//				iNestedNodes.setExpanded(true);
	//			}
	//			else
	//				if (type == SchedulerDom.LIFE_PROCESS_CLASS) {
	//					String name = "";
	//					if (objSchedulerDom.getFilename() != null && new File(objSchedulerDom.getFilename()).exists()) {
	//						name = new File(objSchedulerDom.getFilename()).getName();
	//						name = name.substring(0, name.indexOf(".process_class.xml"));
	//						checkLifeAttributes(element, name);
	//						Utils.setAttribute("name", name, element);
	//					}
	//					Element spooler = new Element("spooler");
	//					Element config = new Element("config");
	//					spooler.addContent(config);
	//					Element process_classes = new Element("process_classes");
	//					config.addContent(process_classes);
	//					process_classes.addContent((Element) element.clone());
	//					TreeData objTD = new TreeData(item, JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"));
	//					item.setData(conItemDataKeyKEY, "process_classes");
	//					item.setData(conItemDataKeyCOPY_ELEMENT, element);
	//					item.setData(conItemDataKeyMAX_OCCUR, "1");
	//					item.setText(PROCESS_CLASSES);
	//				}
	//				else
	//					if (type == SchedulerDom.LIFE_LOCK) {
	//						String name = "";
	//						if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
	//							name = new java.io.File(objSchedulerDom.getFilename()).getName();
	//							name = name.substring(0, name.indexOf(".lock.xml"));
	//							checkLifeAttributes(element, name);
	//							Utils.setAttribute("name", name, element);
	//						}
	//						Element spooler = new Element("spooler");
	//						Element config = new Element("config");
	//						spooler.addContent(config);
	//						Element locks = new Element("locks");
	//						config.addContent(locks);
	//						locks.addContent((Element) element.clone());
	//						item.setData(new TreeData(JOEConstants.LOCKS, config, Options.getHelpURL("locks"), "locks"));
	//						item.setData(conItemDataKeyKEY, "locks");
	//						item.setData(conItemDataKeyCOPY_ELEMENT, element);
	//						item.setText(LOCKS);
	//					}
	//					else
	//						if (type == SchedulerDom.LIFE_ORDER || type == SchedulerDom.LIFE_ADD_ORDER) {
	//							String name = "";
	//							if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
	//								name = new java.io.File(objSchedulerDom.getFilename()).getName();
	//								name = name.substring(0, name.indexOf(".order.xml"));
	//								checkLifeAttributes(element, name);
	//								Utils.setAttribute("job_chain", name.substring(0, name.indexOf(",")), element);
	//								Utils.setAttribute("id", name.substring(name.indexOf(",") + 1), element);
	//							}
	//							TreeData objTD = new TreeData(item, JOEConstants.ORDER, element, Options.getHelpURL("orders"));
	//							item.setData(conItemDataKeyKEY, "commands_@_order");
	//							if (!Utils.isElementEnabled("commands", objSchedulerDom, element)) {
	//								setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//							}
	//							else {
	//								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//							}
	//							treeFillOrder(item, element, false);
	//						}
	//						else {
	//							if (type == SchedulerDom.LIFE_SCHEDULE) {
	//								String name = "";
	//								if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
	//									name = new java.io.File(objSchedulerDom.getFilename()).getName();
	//									name = name.substring(0, name.indexOf(".schedule.xml"));
	//									checkLifeAttributes(element, name);
	//									Utils.setAttribute("name", name, element);
	//								}
	//								else {
	//									name = Utils.getAttributeValue("name", element);
	//								}
	//								treeFillRunTimes(item, element, false, Utils.getAttributeValue("name", element));
	//								List l = element.getChildren("month");
	//								for (int i = 0; i < l.size(); i++) {
	//									Element e = (Element) l.get(i);
	//									treeFillRunTimes(item.getItem(item.getItemCount() - 1).getItem(item.getItem(item.getItemCount() - 1).getItemCount() - 1),
	//											e, false, Utils.getAttributeValue("month", e));
	//								}
	//								item.setExpanded(true);
	//							}
	//						}
	//
	//		tree.setSelection(new TreeItem[] { tree.getItem(0) });
	//		treeSelection(tree, c);
	//	}
	public void treeFillMain2(final SchedulerHotFolder pobjHotFolder, final Object pobjTreeViewControl, final Composite c) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillMain";
		Display d = Display.getCurrent();
		Tree objTree = null;
		if (pobjTreeViewControl instanceof Tree) {
			objTree = (Tree) pobjTreeViewControl;
			objTree.removeAll();
		}
		TreeItem objRootNode = null;
		TreeItem item = null;
		if (type == SchedulerDom.DIRECTORY) {
			String name = pobjHotFolder.getHotFolderSrc().getName();
			if (pobjTreeViewControl instanceof Tree) {
				item = new TreeItem(objTree, SWT.NONE);
				item.setText(name);
				item.setImage(getImage("folder.gif"));
				item.setData(new TreeData(JOEConstants.ROOT_FOLDER, pobjHotFolder, Options.getHelpURL("rootfolder")));
				item.setData(conItemDataKeyKEY, "rootfolder");
				item.setData(conItemDataKeyCOPY_ELEMENT, pobjHotFolder);
			}
			else {
				item = (TreeItem) pobjTreeViewControl;
			}
			int intNoOfNodes = createTreeNodes4SubFolders(item, pobjHotFolder);
			if (intNoOfNodes > 0) {
				name = item.getText();
				item.setText(String.format("%1$s (%2$s)", name, intNoOfNodes + ""));
			}
		}
		objRootNode = item;
		Element config = objSchedulerDom.getRoot().getChild("config");
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.CONFIG, config, Options.getHelpURL("config")));
		item.setData(conItemDataKeyKEY, "config");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Config"));
		item.setImage(getImage("config.gif"));
		if (type == SchedulerDom.DIRECTORY) {
			item.dispose();
		}
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.BASE, config, Options.getHelpURL("base")));
		item.setData(conItemDataKeyKEY, "base");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.BaseFiles"));
		item.setImage(getImage("import_wiz.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.PARAMETER, config, Options.getHelpURL("parameter")));
		item.setData(conItemDataKeyKEY, "params_@_param");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.parameter"));
		item.setImage(getImage("parameter.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.SECURITY, config, Options.getHelpURL("security"), "security"));
		item.setData(conItemDataKeyKEY, "security");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.security"));
		item.setImage(getImage("10682.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
		item.setData(conItemDataKeyKEY, "cluster");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Cluster"));
		item.setImage(getImage("synced.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
		item.setData(conItemDataKeyKEY, "process_classes");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setImage(getImage("10360.gif"));
		item.setText(Messages.getLabel("treeitem.ProcessClasses"));
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.SCHEDULES, config, Options.getHelpURL("schedules"), "schedules"));
		item.setData(conItemDataKeyKEY, "schedules_@_schedule");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel(SCHEDULES));
		item.setImage(getImage("dates.gif"));
		treeFillSchedules(item);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.LOCKS, config, Options.getHelpURL("locks"), "locks"));
		item.setData(conItemDataKeyKEY, "locks");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("Locks"));
		item.setImage(getImage("lockedstate.gif"));
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
		item.setData(conItemDataKeyKEY, "script");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.StartScript"));
		item.setImage(getImage("help.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		if (type != SchedulerDom.DIRECTORY) {
			TreeItem http_server = new TreeItem(objRootNode, SWT.NONE);
			// http_server.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			http_server.setData(new TreeData(JOEConstants.HTTP_SERVER, config, Options.getHelpURL("http_server"), "http_server"));
			// http_server.setData("key", "http_server");
			ArrayList l = new ArrayList();
			l.add("http_server_@_web_service");
			l.add("http_server_@_http.authentication");
			l.add("http_server_@_http_directory");
			http_server.setData(conItemDataKeyKEY, l);
			http_server.setData(conItemDataKeyCOPY_ELEMENT, config);
			http_server.setText(Messages.getLabel(HTTP_SERVER));
			http_server.setImage(getImage("web.gif"));
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_web_service");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel(WEB_SERVICES));
			item.setImage(getImage("lockedstate.gif"));
			treeFillWebServices(item);
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HTTP_AUTHENTICATION, config, Options.getHelpURL("http_authentication"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_http.authentication");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.HttpAuthentication"));
			item.setImage(getImage("12126.gif"));
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_http_directory");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.HttpDirectories"));
			item.setImage(getImage("httpdirectory.gif"));
			item = new TreeItem(objRootNode, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
			item.setData(conItemDataKeyKEY, "holidays");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.Holidays"));
			item.setImage(getImage("holidays.gif"));
			// TODO implement
			//			treeFillHolidays(item, config);
		}
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.JOBS, null, Options.getHelpURL("jobs"), "jobs"));
		item.setData(conItemDataKeyKEY, "jobs_@_job");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel(JOBS));
		item.setImage(getImage("jobs.gif"));
		createTreeNodes4Jobs(item, pobjHotFolder);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.JOB_CHAINS, null, Options.getHelpURL("job_chains"), "job_chains"));
		item.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel(JOB_CHAINS));
		item.setImage(getImage("hierarchical.gif"));
		createTreeNodes4JobChains(item, pobjHotFolder);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.ORDERS, config, Options.getHelpURL("orders"), "orders"));
		item.setData(conItemDataKeyKEY, "commands_@_order");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel(ORDERS));
		item.setImage(getImage("orders.gif"));
		createTreeNodes4Orders(item, pobjHotFolder);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
		item.setData(conItemDataKeyKEY, "commands");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Commands"));
		item.setImage(getImage("commands.gif"));
		if (type == SchedulerDom.DIRECTORY) {
			item.dispose();
		}
		if (objTree != null) {
			objTree.setSelection(new TreeItem[] { objTree.getItem(0) });
			treeSelection(objTree, c);
		}
	}

	public void setColorOfJobTreeItem(final JSObjJob pobjJob, final TreeItem pobjTreeNode) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setColorOfJobTreeItem";
		try {
			if (pobjJob.getEnabled() == false) {
				setDisabled(pobjTreeNode);
			}
			else {
				setEnabled(pobjTreeNode);
				if (pobjJob.isOrderDrivenJob()) {
					pobjTreeNode.setImage(getImage("17382.png"));
				}
				else {
					pobjTreeNode.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
					pobjTreeNode.setImage(getImage("17379.png"));
				}
			}
		}
		catch (Exception e) {
			throw new JobSchedulerException(e);
		}
	}
	@SuppressWarnings("unused")
	private static final String	conImageEDITOR_SMALL_PNG	= "/sos/scheduler/editor/editor-small.png";
	private Tree				objTree						= null;

	//	public void treeFillMain(final Tree tree, final Composite c, final int type_) {
	//
	//		final String conMethodName = conClassName + "::treeFillMain";
	//
	//		logger.debug(String.format("Enter procedure %1$s ", conMethodName));
	//
	//		type = type_;
	//		if (objSchedulerDom.isLifeElement()) {
	//			// TODO needed?
	//			// treeFillMainForLifeElement(tree, c);
	//		}
	//		else {
	//			treeFillMain1(tree, c, objSchedulerHotFolder);
	//		}
	//	}
	//
	public void treeFillMain1(final Tree tree, final Composite c, final SchedulerHotFolder pobjHotFolder) {
		final String conMethodName = conClassName + "::treeFillMain";
		logger.debug(String.format("Enter procedure %1$s ", conMethodName));
		// type = type_;
		objSchedulerHotFolder = pobjHotFolder;
		type = SchedulerDom.DIRECTORY;
		// if (objSchedulerDom.isLifeElement()) {
		// createTreeNodes4HotFolderElements(tree, c, pobjHotFolder);
		// }
		// else {
		treeFillMain2(pobjHotFolder, tree, c);
		// }
	}

	public void treeFillMain1(final SchedulerHotFolder pobjHotFolder, final Object pobjTreeViewControl, final Composite c) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillMain";
		Display d = Display.getCurrent();
		objTree = null;
		if (pobjTreeViewControl instanceof Tree) {
			objTree = (Tree) pobjTreeViewControl;
			objTree.removeAll();
		}
		TreeItem objRootNode = null;
		TreeItem item = null;
		if (type == SchedulerDom.DIRECTORY) {
			String name = pobjHotFolder.getHotFolderSrc().getName();
			if (pobjTreeViewControl instanceof Tree) {
				item = new TreeItem(objTree, SWT.NONE);
				item.setText(name);
				item.setImage(getImage("folder.gif"));
				item.setData(new TreeData(JOEConstants.ROOT_FOLDER, pobjHotFolder, Options.getHelpURL("rootfolder")));
				item.setData(conItemDataKeyKEY, "rootfolder");
				item.setData(conItemDataKeyCOPY_ELEMENT, pobjHotFolder);
			}
			else { 
				item = (TreeItem) pobjTreeViewControl;
			}
			int intNoOfNodes = createTreeNodes4SubFolders(item, pobjHotFolder);
			if (intNoOfNodes > 0) {
				name = item.getText();
				item.setText(String.format("%1$s (%2$s)", name, intNoOfNodes + ""));
			}
		}
		objRootNode = item;
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.JOBS, null, Options.getHelpURL("jobs"), "jobs"));
		item.setData(conItemDataKeyKEY, "jobs_@_job");
		item.setText(Messages.getLabel(JOBS));
		item.setImage(getImage("jobs.gif"));
		createTreeNodes4Jobs(item, pobjHotFolder);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.JOB_CHAINS, null, Options.getHelpURL("job_chains"), "job_chains"));
		item.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
		item.setText(Messages.getLabel(JOB_CHAINS));
		item.setImage(getImage("hierarchical.gif"));
		createTreeNodes4JobChains(item, pobjHotFolder);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.ORDERS, null, Options.getHelpURL("orders"), "orders"));
		item.setData(conItemDataKeyKEY, "commands_@_order");
		item.setText(Messages.getLabel(ORDERS));
		item.setImage(getImage("orders.gif"));
		createTreeNodes4Orders(item, pobjHotFolder);
		Element config = objSchedulerDom.getRoot().getChild("config");
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.CONFIG, config, Options.getHelpURL("config")));
		item.setData(conItemDataKeyKEY, "config");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Config"));
		item.setImage(getImage("config.gif"));
		if (type == SchedulerDom.DIRECTORY) {
			item.dispose();
		}
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.BASE, config, Options.getHelpURL("base")));
		item.setData(conItemDataKeyKEY, "base");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.BaseFiles"));
		item.setImage(getImage("import_wiz.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.PARAMETER, config, Options.getHelpURL("parameter")));
		item.setData(conItemDataKeyKEY, "params_@_param");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.parameter"));
		item.setImage(getImage("parameter.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.SECURITY, config, Options.getHelpURL("security"), "security"));
		item.setData(conItemDataKeyKEY, "security");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.security"));
		item.setImage(getImage("10682.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
		item.setData(conItemDataKeyKEY, "cluster");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Cluster"));
		item.setImage(getImage("synced.gif"));
		if (type == SchedulerDom.DIRECTORY)
			item.dispose();
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
		item.setData(conItemDataKeyKEY, "process_classes");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setImage(getImage("10360.gif"));
		item.setText(Messages.getLabel("treeitem.ProcessClasses"));
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.SCHEDULES, config, Options.getHelpURL("schedules"), "schedules"));
		item.setData(conItemDataKeyKEY, "schedules_@_schedule");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel(SCHEDULES));
		item.setImage(getImage("dates.gif"));
		treeFillSchedules(item);
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.LOCKS, config, Options.getHelpURL("locks"), "locks"));
		item.setData(conItemDataKeyKEY, "locks");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("Locks"));
		item.setImage(getImage("lockedstate.gif"));
		if (type != SchedulerDom.DIRECTORY) {
			item = new TreeItem(objRootNode, SWT.NONE);
			item.setData(new TreeData(JOEConstants.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
			item.setData(conItemDataKeyKEY, "script");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.StartScript"));
			item.setImage(getImage("help.gif"));
			TreeItem http_server = new TreeItem(objRootNode, SWT.NONE);
			// http_server.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			http_server.setData(new TreeData(JOEConstants.HTTP_SERVER, config, Options.getHelpURL("http_server"), "http_server"));
			// http_server.setData("key", "http_server");
			ArrayList l = new ArrayList();
			l.add("http_server_@_web_service");
			l.add("http_server_@_http.authentication");
			l.add("http_server_@_http_directory");
			http_server.setData(conItemDataKeyKEY, l);
			http_server.setData(conItemDataKeyCOPY_ELEMENT, config);
			http_server.setText(Messages.getLabel(HTTP_SERVER));
			http_server.setImage(getImage("web.gif"));
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_web_service");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel(WEB_SERVICES));
			item.setImage(getImage("lockedstate.gif"));
			treeFillWebServices(item);
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HTTP_AUTHENTICATION, config, Options.getHelpURL("http_authentication"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_http.authentication");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.HttpAuthentication"));
			item.setImage(getImage("12126.gif"));
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
			item.setData(conItemDataKeyKEY, "http_server_@_http_directory");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.HttpDirectories"));
			item.setImage(getImage("httpdirectory.gif"));
			item = new TreeItem(objRootNode, SWT.NONE);
			item.setData(new TreeData(JOEConstants.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
			item.setData(conItemDataKeyKEY, "holidays");
			item.setData(conItemDataKeyCOPY_ELEMENT, config);
			item.setText(Messages.getLabel("treeitem.Holidays"));
			item.setImage(getImage("holidays.gif"));
			// TODO implement
			//			treeFillHolidays(item, config);
		}
		item = new TreeItem(objRootNode, SWT.NONE);
		item.setData(new TreeData(JOEConstants.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
		item.setData(conItemDataKeyKEY, "commands");
		item.setData(conItemDataKeyCOPY_ELEMENT, config);
		item.setText(Messages.getLabel("treeitem.Commands"));
		item.setImage(getImage("commands.gif"));
		if (type == SchedulerDom.DIRECTORY) {
			item.dispose();
		}
		if (objTree != null) {
			objTree.setSelection(new TreeItem[] { objTree.getItem(0) });
			treeSelection(objTree, c);
		}
	}

	private int createTreeNodes4SubFolders(final TreeItem pobjParentTreeNode, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
		pobjParentTreeNode.removeAll();
		int intNoOfNodes = pobjHotFolder.getHotFolderFileList().getFolderList().size();
		if (intNoOfNodes > 0) {
			for (SchedulerHotFolder objFolder : pobjHotFolder.getHotFolderFileList().getFolderList()) {
				String strNodeText = objFolder.getHotFolderSrc().getName();
				strNodeText = strNodeText.replace(pobjHotFolder.getHotFolderSrc().getName(), "");
				strNodeText = strNodeText.substring(1); // skip first "/"
				TreeItem item = new TreeItem(pobjParentTreeNode, SWT.NONE);
				item.setText(strNodeText);
				item.setImage(getImage("folder.gif"));
				setItemData(item, JOEConstants.SUB_FOLDER, objFolder, "folder");
			}
		}
		pobjParentTreeNode.setExpanded(true);
		return intNoOfNodes;
	}

	private void createTreeNodes4SubFolders1(final Tree pobjTree, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
		pobjTree.removeAll();
		if (pobjHotFolder.getHotFolderFileList().getFolderList().size() > 0) {
			for (SchedulerHotFolder objFolder : pobjHotFolder.getHotFolderFileList().getFolderList()) {
				String name = objFolder.getHotFolderSrc().getName();
				name = name.replace(pobjHotFolder.getHotFolderSrc().getName(), ".");
				TreeItem item = new TreeItem(pobjTree, SWT.NONE);
				item.setText(name);
				item.setImage(getImage("folder.gif"));
				setItemData(item, JOEConstants.SUB_FOLDER, objFolder, "folder");
			}
		}
	}

	public void createTreeNodes4Orders(final TreeItem pobjParentTreeNode, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::createTreeNodes4Orders";
		pobjParentTreeNode.removeAll();
		int intSize = pobjHotFolder.getHotFolderFileList().getOrderList().size();
		if (intSize > 0) {
			for (JSObjOrder objOrder : pobjHotFolder.getHotFolderFileList().getOrderList()) {
				TreeItem item = new TreeItem(pobjParentTreeNode, SWT.NONE);
				setItemData(item, JOEConstants.ORDER, objOrder, "order");
				item.setText(objOrder.getObjectNameAndTitle());
				item.setImage(getImage("order.gif"));
				createSubTreeNodes4Order(pobjHotFolder, item, objOrder);
			}
		}
	}

	private void createSubTreeNodes4Order(final SchedulerHotFolder pobjHotFolder, final TreeItem pobjParentItem, final JSObjOrder pobjJobOrder) {
		TreeItem item = new TreeItem(pobjParentItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.parameter"));
		setItemData(item, JOEConstants.ORDER_PARAMETER, pobjJobOrder, "treeitem.order.parameter");
		item.setImage(getImage("parameter.gif"));
	}

	public void createTreeNodes4HotFolderElements1(final Tree pobjTreeViewControl, final Composite c, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillMainForLifeElement";
		logger.debug(String.format("Enter procedure %1$s ", conMethodName));
		objSchedulerHotFolder = pobjHotFolder;
		pobjTreeViewControl.removeAll();
		TreeItem item = new TreeItem(pobjTreeViewControl, SWT.NONE);
		String name = "";
		for (Object objHotFolderObject : pobjHotFolder.getHotFolderFileList().getSortedFileList()) {
			// logger.info(String.format("File '%1$s' is an instance of '%2$s'", objHotFolderObject.getHotFolderSrc().getName(),
			if (objHotFolderObject instanceof SchedulerHotFolder) {
				SchedulerHotFolder objSubFolder = (SchedulerHotFolder) objHotFolderObject;
				logger.info(String.format("... load %1$s", objSubFolder.getHotFolderSrc().getName()));
				item = new TreeItem(pobjTreeViewControl, SWT.NONE);
				item.setData(new TreeData(-1, objSubFolder, "Hallo"));
				item.setText(objSubFolder.getHotFolderSrc().getName());
				item.setExpanded(false);
			}
			else
				if (objHotFolderObject instanceof JSObjJob) {
					JSObjJob objJob = (JSObjJob) objHotFolderObject;
					name = objJob.getHotFolderSrc().getName();
					name = name.substring(0, name.indexOf(JSObjJob.fileNameExtension));
					item.setText(objJob.getName() + " - " + objJob.getTitle());
					item.setData(new TreeData(JOEConstants.JOB, objJob, Options.getHelpURL("job")));
					item.setData(conItemDataKeyKEY, "job");
					item.setData(conItemDataKeyCOPY_ELEMENT, objJob);
					setColorOfJobTreeItem(objJob, item);
					createSubTreeNodes4Job(pobjHotFolder, item, objJob, false);
					item.setExpanded(true);
				}
				else
					if (objHotFolderObject instanceof JSObjJobChain) {
						JSObjJobChain objJobChain = (JSObjJobChain) objHotFolderObject;
						continue;
					}
		}
		pobjTreeViewControl.setSelection(new TreeItem[] { pobjTreeViewControl.getItem(0) });
		treeSelection(pobjTreeViewControl, c);
	}

	//	public void treeFillMain(final Tree tree, final Composite c) {
	//		Display d = Display.getCurrent();
	//		objTree = tree;
	//
	//		final String conMethodName = conClassName + "::treeFillMain";
	//
	//		logger.debug(SOSJOEMessageCodes.JOE_M_0047.params(conMethodName));
	//
	//		tree.removeAll();
	//		if (objSchedulerDom.isLifeElement()) {
	//			Utils.setResetElement(objSchedulerDom.getRoot());
	//		}
	//		else {
	//			Utils.setResetElement(objSchedulerDom.getRoot().getChild("config"));
	//		}
	//
	//		TreeItem objTreeObjects = new TreeItem(tree, SWT.NONE);
	//		objTreeObjects.setText("JobScheduler Objects");
	//		objTreeObjects.setData(conItemDataKeyKEY, "objects");
	//		objTreeObjects.setExpanded(true);
	//
	//		TreeItem item = null;
	//		Element config = objSchedulerDom.getRoot().getChild("config");
	//		if (type != SchedulerDom.DIRECTORY) {
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.CONFIG, config, Options.getHelpURL("config")));
	//			item.setData(conItemDataKeyKEY, "config");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.Config"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_BaseConfig.label());
	//			item.setImage(getImage("config.gif"));
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.BASEFILE, config, Options.getHelpURL("base")));
	//			item.setData(conItemDataKeyKEY, "base");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.BaseFiles"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_BaseFiles.label());
	//			item.setImage(getImage("import_wiz.gif"));
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.PARAMETER, config, Options.getHelpURL("parameter")));
	//			item.setData(conItemDataKeyKEY, "params_@_param");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.parameter"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
	//			item.setImage(getImage("parameter.gif"));
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.SECURITY, config, Options.getHelpURL("security"), "security"));
	//			item.setData(conItemDataKeyKEY, "security");
	//			item.setData(conItemDataKeyMAX_OCCUR, "1");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.security"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_AccessControl.label());
	//			item.setImage(getImage("10682.gif"));
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
	//			item.setData(conItemDataKeyKEY, "cluster");
	//			item.setData(conItemDataKeyMAX_OCCUR, "1");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.Cluster"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_ClusterConfig.label());
	//			item.setImage(getImage("synced.gif"));
	//		}
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		item.setData(new TreeData(JOEConstants.JOBS, config, Options.getHelpURL("jobs"), "jobs"));
	//		item.setData(conItemDataKeyKEY, "jobs_@_job");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//		item.setText(JOBS);
	//		item.setImage(getImage("jobs.gif"));
	//		treeFillJobs(item);
	//		item.setExpanded(true);
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		item.setData(new TreeData(JOEConstants.JOB_CHAINS, config, Options.getHelpURL("job_chains"), "job_chains"));
	//		item.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//		item.setText(JOB_CHAINS);
	//		item.setImage(getImage("hierarchical.gif"));
	//		treeFillJobChains(item);
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		TreeData objTD = new TreeData(item, JOEConstants.ORDERS, config, Options.getHelpURL("orders"));
	//		item.setData(conItemDataKeyKEY, "commands_@_order");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//		item.setText(ORDERS);
	//		item.setImage(getImage("orders.gif"));
	//
	//		treeFillOrders(item, true);
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		{
	//			objTD = new TreeData(item, JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"));
	//			item.setData(conItemDataKeyKEY, "process_classes");
	//			item.setData(conItemDataKeyMAX_OCCUR, "1");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			item.setImage(getImage("10360.gif"));
	//			item.setText(PROCESS_CLASSES);
	//
	//		}
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		objTD = new TreeData(item, JOEConstants.SCHEDULES, config, Options.getHelpURL("schedules"));
	//		item.setData(conItemDataKeyKEY, "schedules_@_schedule");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//		item.setText(SCHEDULES);
	//		item.setImage(getImage("dates.gif"));
	//		treeFillSchedules(item);
	//
	//		item = new TreeItem(objTreeObjects, SWT.NONE);
	//		objTD = new TreeData(item, JOEConstants.LOCKS, config, Options.getHelpURL("locks"));
	//		item.setData(conItemDataKeyKEY, "locks");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label());
	//		item.setImage(getImage("lockedstate.gif"));
	//
	//		if (type != SchedulerDom.DIRECTORY) {
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
	//			item.setData(conItemDataKeyKEY, "script");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.StartScript"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StartScript.label());
	//			item.setImage(getImage("help.gif"));
	//			if (type == SchedulerDom.DIRECTORY)
	//				item.dispose();
	//
	//			TreeItem http_server = new TreeItem(tree, SWT.NONE);
	//			// http_server.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
	//			http_server.setData(new TreeData(JOEConstants.HTTP_SERVER, config, Options.getHelpURL("http_server"), "http_server"));
	//			// http_server.setData("key", "http_server");
	//			ArrayList l = new ArrayList();
	//			l.add("http_server_@_web_service");
	//			l.add("http_server_@_http.authentication");
	//			l.add("http_server_@_http_directory");
	//			http_server.setData(conItemDataKeyKEY, l);
	//			http_server.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//            http_server.setText(Messages.getLabel(HTTP_SERVER));
	//			http_server.setText(HTTP_SERVER);
	//			http_server.setImage(getImage("web.gif"));
	//
	//			item = new TreeItem(http_server, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
	//			item.setData(conItemDataKeyKEY, "http_server_@_web_service");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			item.setText(Messages.getLabel(WEB_SERVICES));
	//			item.setImage(getImage("lockedstate.gif"));
	//
	//			treeFillWebServices(item);
	//
	//			item = new TreeItem(http_server, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.HTTP_AUTHENTICATION, config, Options.getHelpURL("http_authentication"), "http_server"));
	//			item.setData(conItemDataKeyKEY, "http_server_@_http.authentication");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//            item.setText(Messages.getLabel("treeitem.HttpAuthentication"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPAuthentication.label());
	//			item.setImage(getImage("12126.gif"));
	//
	//			item = new TreeItem(http_server, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
	//			item.setData(conItemDataKeyKEY, "http_server_@_http_directory");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//            item.setText(Messages.getLabel("treeitem.HttpDirectories"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPDirectories.label());
	//			item.setImage(getImage("httpdirectory.gif"));
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
	//			item.setData(conItemDataKeyKEY, "holidays");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//            item.setText(Messages.getLabel("treeitem.Holidays"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Holidays.label());
	//			item.setImage(getImage("holidays.gif"));
	//			treeFillHolidays(item, config);
	//
	//			item = new TreeItem(tree, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
	//			item.setData(conItemDataKeyKEY, "commands");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, config);
	//			//        item.setText(Messages.getLabel("treeitem.Commands"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Commands.label());
	//			item.setImage(getImage("commands.gif"));
	//			if (type == SchedulerDom.DIRECTORY) {
	//				item.dispose();
	//			}
	//		}
	//
	//		objTreeObjects.setExpanded(true);
	//
	//		tree.setSelection(new TreeItem[] { tree.getItem(0) });
	//		treeSelection(tree, c);
	//	}
	//	private List<Element> hasOrders(final SchedulerDom pobjSchedulerDom) {
	//		Element commands = objSchedulerDom.getRoot().getChild("config").getChild("commands");
	//		if (commands != null) {
	//			List<Element> lstOfOrders = commands.getChildren("add_order");
	//			if (lstOfOrders != null) {
	//				return lstOfOrders;
	//			}
	//			List<Element> lOrder = commands.getChildren("order");
	//			if (lOrder != null) {
	//				return lOrder;
	//			}
	//		}
	//		return null;
	//	}
	//	public void treeFillOrders(final TreeItem parent, final boolean expand) {
	//		TreeItem orders = parent;
	//		TreeData objTD = (TreeData) parent.getData();
	//		if (objTD.TypeEqualTo(JOEConstants.ORDERS) == false) {
	//			Tree t = parent.getParent();
	//			for (int i = 0; i < t.getItemCount(); i++) {
	//				TreeData objT = (TreeData) t.getData();
	//				if (objT != null) {
	//					if (objT.TypeEqualTo(JOEConstants.ORDERS) == true) {
	//						orders = t.getItem(i);
	//						break;
	//					}
	//				}
	//			}
	//		}
	//		if (orders != null) {
	//			orders.removeAll();
	//			Element commands = objSchedulerDom.getRoot().getChild("config").getChild("commands");
	//			if (commands != null) {
	//				List lstOfOrders = commands.getChildren("add_order");
	//				if (lstOfOrders != null) {
	//					Iterator it = getSortedIterator(lstOfOrders, new CompareNameAndTitle());
	//					while (it.hasNext()) {
	//						Element objOrderElement = (Element) it.next();
	//						if (objOrderElement.getName().equals("add_order") && objOrderElement.getAttributeValue("id") != null) {
	//							{
	//								TreeItem item = new TreeItem(orders, SWT.NONE);
	//								item.setImage(getImage("order.gif"));
	//								objTD = new TreeData(item, JOEConstants.ORDER, objOrderElement, Options.getHelpURL("orders"));
	//								item.setData(conItemDataKeyKEY, "commands_@_order");
	//								if (!Utils.isElementEnabled("commands", objSchedulerDom, objOrderElement)) {
	//									setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//								}
	//								else {
	//									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//								}
	//								treeFillOrder(item, objOrderElement, false);
	//							}
	//						}
	//					}
	//				}
	//				List lOrder = commands.getChildren("order");
	//				if (lOrder != null) {
	//					Iterator it = getSortedIterator(lOrder, new CompareNameAndTitle());
	//					while (it.hasNext()) {
	//						Element e = (Element) it.next();
	//						if (e.getName().equals("order") && e.getAttributeValue("id") != null) {
	//							TreeItem item = new TreeItem(orders, SWT.NONE);
	//							item.setImage(getImage("order.gif"));
	//							objTD = new TreeData(item, JOEConstants.ORDER, e, Options.getHelpURL("orders"));
	//							item.setData(conItemDataKeyKEY, "commands_@_order");
	//							item.setData(conItemDataKeyCOPY_ELEMENT, e);
	//							if (!Utils.isElementEnabled("commands", objSchedulerDom, e)) {
	//								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//							}
	//							else {
	//								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//							}
	//							treeFillOrder(item, e, false);
	//						}
	//					}
	//				}
	//			}
	//		}
	//		orders.setExpanded(expand);
	//	}
	//
	private Iterator<Element> getSortedIterator(final List<Object> l, final CompareNameAndTitle myElementComparator) {
		ArrayList<Element> al = new ArrayList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof Element) {
				Element element = (Element) o;
				al.add(element);
			}
		}
		Collections.sort(al, myElementComparator);
		return al.iterator();
	}

	//	public void treeFillJobs(final TreeItem parent) {
	//		@SuppressWarnings("unused")
	//		final String conMethodName = conClassName + "::treeFillJobs";
	//
	//		parent.removeAll();
	//		Element jobs = getJobs(objSchedulerDom);
	//		if (jobs == null) {
	//			TreeData data = (TreeData) parent.getData();
	//			jobs = data.getElement().getChild("jobs");
	//		}
	//		if (jobs != null) {
	//			Iterator<Element> it = getSortedIterator(jobs.getChildren(), new CompareNameAndTitle());
	//			while (it.hasNext()) {
	//				Element element = it.next();
	//				createJobItem(parent, element, "");
	//			}
	//		}
	//		parent.setExpanded(true);
	//	}
	//	private void createJobItem(final TreeItem parent, final Element element, final String strPrefix) {
	//		if (type == SchedulerDom.DIRECTORY) {
	//			checkLifeAttributes(element, Utils.getAttributeValue("name", element));
	//		}
	//		TreeItem objTreeItem = new TreeItem(parent, SWT.NONE);
	//		TreeData objTD = new TreeData(objTreeItem, JOEConstants.JOB, element, Options.getHelpURL("job"));
	//		objTreeItem.setText(strPrefix + objTD.getNameAndTitle());
	//		objTreeItem.setData(conItemDataKeyKEY, "jobs_@_job");
	//		setColorOfJobTreeItem(element, objTreeItem);
	//		treeFillJobChilds(objTreeItem, element, false);
	//	}
	//
	private Element getJobs(final SchedulerDom pobjSchedulerDom) {
		Element jobs = pobjSchedulerDom.getRoot().getChild("config").getChild("jobs");
		return jobs;
	}

	private void setDisabled(final TreeItem pobjC) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setDisabled";
		// TODO Color as an global Option
		pobjC.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	} // private void setDisabled

	private void setEnabled(final TreeItem pobjC) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setEnabled";
		// TODO Color as an global Option
		pobjC.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	} // private void setEnabled

	public void treeExpandJob(final TreeItem parent, final String job) {
		// if (parent.getText().equals("Jobs")) {
		for (int i = 0; i < parent.getItemCount(); i++)
			if (parent.getItem(i).getText().equals(job)) {
				parent.getItem(i).setExpanded(true);
			}
		// }
	}

	public void treeExpandItem(final TreeItem parent, final String elem) {
		for (int i = 0; i < parent.getItemCount(); i++)
			if (parent.getItem(i).getText().equals(elem)) {
				parent.getItem(i).setExpanded(true);
			}
	}

	//	public void treeFillJobChilds(final TreeItem parent, final Element job, final boolean expand) {
	//		boolean flgIsReadOnlyFile = !Utils.isElementEnabled("job", objSchedulerDom, job);
	//		parent.removeAll();
	//		ArrayList<String> l = new ArrayList<String>();
	//		Color isColor4ReadOnlyFiles = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	//
	//		// Options
	//		TreeItem item = new TreeItem(parent, SWT.NONE);
	//		//        item.setText(Messages.getLabel("treeitem.options"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Options.label());
	//		item.setImage(getImage("options.gif"));
	//
	//		item.setData(conItemDataKeyMAX_OCCUR, "1");
	//		item.setData(new TreeData(JOEConstants.JOB_OPTION, job, Options.getHelpURL("job")));
	//		item.setData(conItemDataKeyKEY, "job");
	//		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		if (flgIsReadOnlyFile) {
	//			item.setForeground(isColor4ReadOnlyFiles);
	//		}
	//
	//		// Parameter
	//		item = new TreeItem(parent, SWT.NONE);
	//		item.setData(new TreeData(JOEConstants.PARAMETER, job, Options.getHelpURL("parameter")));
	//		item.setData(conItemDataKeyKEY, "params_@_param");
	//		ArrayList ll = new ArrayList();
	//		ll.add("params_@_param");
	//		ll.add("params_@_include");
	//		// l.add("environment");
	//		item.setData(conItemDataKeyKEY, ll);
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		//        item.setText(Messages.getLabel("treeitem.parameter"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
	//		item.setImage(getImage("parameter.gif"));
	//
	//		// Mail
	//		item = new TreeItem(parent, SWT.NONE);
	//		item.setData(new TreeData(JOEConstants.SETTINGS, job, Options.getHelpURL("settings")));
	//		item.setData(conItemDataKeyKEY, "settings");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		//        item.setText(Messages.getLabel("treeitem.jobsettings"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_JobSettings.label());
	//		item.setData(conItemDataKeyMAX_OCCUR, "1");
	//		item.setImage(getImage("10036.gif"));
	//
	//		// Monitor
	//		item = new TreeItem(parent, SWT.NONE);
	//		item.setText(MONITOR);
	//		item.setImage(getImage("source_attach_attrib.gif"));
	//		item.setData(new TreeData(JOEConstants.MONITORS, job, Options.getHelpURL("job.monitor"), "monitor"));
	//		item.setData(conItemDataKeyKEY, "monitor");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		if (flgIsReadOnlyFile) {
	//			setDisabled(item);
	//		}
	//		treeFillMonitorScripts(item, job, flgIsReadOnlyFile);
	//
	//		// RunOptions
	//		item = new TreeItem(parent, SWT.NONE);
	//		//        item.setText(Messages.getLabel("treeitem.runoptions"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_RunOptions.label());
	//		item.setData(new TreeData(JOEConstants.OPTIONS, job, Options.getHelpURL("job.options")));
	//		l = new ArrayList<String>();
	//		l.add("start_when_directory_changed");
	//		l.add("delay_after_error");
	//		l.add("delay_order_after_setback");
	//		// item.setData("key", "job.options");
	//		item.setData(conItemDataKeyKEY, l);
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		item.setImage(getImage("properties.gif"));
	//		if (flgIsReadOnlyFile) {
	//			setDisabled(item);
	//		}
	//
	//		item = new TreeItem(parent, SWT.NONE);
	//		//        item.setText(Messages.getLabel("locks"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label());
	//		item.setData(new TreeData(JOEConstants.LOCKUSE, job, Options.getHelpURL("job.locks")));
	//		item.setData(conItemDataKeyKEY, "lock.use");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		item.setImage(getImage("lockedstate.gif"));
	//
	//		if (flgIsReadOnlyFile) {
	//			setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//		}
	//		treeFillRunTimes(parent, job, flgIsReadOnlyFile, "run_time");
	//
	//		List commands = job.getChildren("commands");
	//		item = new TreeItem(parent, SWT.NONE);
	//		//        item.setText(Messages.getLabel("treeitem.commands"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Commands.label());
	//		item.setImage(getImage("commands.gif"));
	//
	//		if (flgIsReadOnlyFile) {
	//			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//		}
	//		if (commands != null) {
	////			treeFillCommands(item, job, false);
	//		}
	//		item.setData(new TreeData(JOEConstants.JOB_COMMANDS, job, Options.getHelpURL("job.commands")));
	//		// item.setData("key", "job_@_commands");
	//		item.setData(conItemDataKeyKEY, "commands");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		parent.setExpanded(expand);
	//
	//		// Documentation
	//		item = new TreeItem(parent, SWT.NONE);
	//		//        item.setText(Messages.getLabel("treeitem.documentation"));
	//		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Documentation.label());
	//		item.setImage(getImage("11020.gif"));
	//
	//		item.setData(conItemDataKeyMAX_OCCUR, "1");
	//		item.setData(new TreeData(JOEConstants.JOB_DOCUMENTATION, job, Options.getHelpURL("job")));
	//		// ArrayList l = new ArrayList();
	//		// l.add("process");
	//		// l.add("script");
	//		item.setData(conItemDataKeyKEY, "job_@_description");
	//		item.setData(conItemDataKeyCOPY_ELEMENT, job);
	//		if (flgIsReadOnlyFile) {
	//			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//		}
	//	}
	//	private static final HashMap<String, Image>	hshImages	= new HashMap<String, Image>();
	private Image getImage(final String pstrImageFileName) {
		Image objI = ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/" + pstrImageFileName);
		return objI;
	}

	public void treeFillOrder(final TreeItem parent, final Element order, final boolean expand) {
		parent.removeAll();
		// Element runtime = order.getChild("run_time");
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setData(new TreeData(JOEConstants.PARAMETER, order, Options.getHelpURL("parameter")));
		item.setData(conItemDataKeyKEY, "params_@_param");
		item.setData(conItemDataKeyCOPY_ELEMENT, order);
		//        item.setText(Messages.getLabel("treeitem.parameter"));
		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
		item.setImage(getImage("parameter.gif"));
		treeFillRunTimes(parent, order, false, "run_time");
		List l = order.getChild("run_time").getChildren("month");
		for (int i = 0; i < l.size(); i++) {
			Element e = (Element) l.get(i);
			treeFillRunTimes(parent.getItem(parent.getItemCount() - 1).getItem(parent.getItem(parent.getItemCount() - 1).getItemCount() - 1), e,
					!Utils.isElementEnabled("job", objSchedulerDom, order), Utils.getAttributeValue("month", e));
		}
		parent.setExpanded(expand);
	}

	public void treeFillHolidays(TreeItem item, final Element elem) {
		item = new TreeItem(item, SWT.NONE);
		//        item.setText(Messages.getLabel("treeitem.weekdays"));
		item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Weekdays.label());
		item.setData(new TreeData(JOEConstants.WEEKDAYS, elem, Options.getHelpURL("job.run_time.weekdays"), "weekdays"));
		item.setData(conItemDataKeyKEY, "holidays_@_weekdays");
		item.setData(conItemDataKeyCOPY_ELEMENT, elem);
		if (elem.getChild("holidays") != null) {
			treeFillDays(item, elem, 0, false);
		}
		objSchedulerForm.updateFont(item);
	}

	//	public void treeFillExitCodesCommands(final TreeItem parent, final Element elem, final boolean expand) {
	//		parent.removeAll();
	//		treeFillExitCodesCommands(parent, elem.getChildren("order"));
	//		treeFillExitCodesCommands(parent, elem.getChildren("add_order"));
	//		treeFillExitCodesCommands(parent, elem.getChildren("start_job"));
	//	}
	//	private void treeFillExitCodesCommands(final TreeItem parent, final List cmdList) {
	//		for (int i = 0; i < cmdList.size(); i++) {
	//			Element cmdElem = (Element) cmdList.get(i);
	//			TreeItem item = new TreeItem(parent, SWT.NONE);
	//			String name = Utils.getAttributeValue("job_chain", cmdElem) != null && Utils.getAttributeValue("job_chain", cmdElem).length() > 0 ? Utils.getAttributeValue(
	//					"job_chain", cmdElem) : Utils.getAttributeValue("job", cmdElem);
	//			item.setText(cmdElem.getName() + ": " + name);
	//			item.setImage(getImage("commands.gif"));
	//			item.setData(new TreeData(JOEConstants.JOB_COMMAND, cmdElem, Options.getHelpURL("job.commands")));
	//			item.setExpanded(false);
	//			// PARAMETER
	//			item = new TreeItem(item, SWT.NONE);
	//			item.setData(new TreeData(JOEConstants.PARAMETER, cmdElem, Options.getHelpURL("parameter")));
	//			item.setData(conItemDataKeyKEY, "params_@_param");
	//			item.setData(conItemDataKeyCOPY_ELEMENT, cmdElem);
	//			//            item.setText(Messages.getLabel("treeitem.parameter"));
	//			item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
	//			item.setImage(getImage("parameter.gif"));
	//
	//		}
	//	}
	//	public void treeFillCommands(final TreeItem parent, final Element job, final boolean expand) {
	//		// new JobCommandListener(objSchedulerDom, null, null).fillCommands(job, parent, expand);
	//		// fillCommands(job, parent, expand);
	//		List commands = job.getChildren("commands");
	//		java.util.ArrayList listOfReadOnly = objSchedulerDom.getListOfReadOnlyFiles();
	//		if (commands != null) {
	//			Iterator it = commands.iterator();
	//			parent.removeAll();
	//			while (it.hasNext()) {
	//				Element e = (Element) it.next();
	//				if (e.getAttributeValue("on_exit_code") != null) {
	//					TreeItem item = new TreeItem(parent, SWT.NONE);
	//					//                    item.setText(e.getAttributeValue("on_exit_code"));
	//					item.setText(e.getAttributeValue("on_exit_code"));
	//					item.setData(new TreeData(JOEConstants.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
	//					item.setData(conItemDataKeyKEY, "commands_@_order");
	//					item.setData(conItemDataKeyCOPY_ELEMENT, e);
	//					if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
	//						setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//					}
	//					else {
	//						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//					}
	//					treeFillExitCodesCommands(item, e, false);
	//				}
	//			}
	//		}
	//		parent.setExpanded(expand);
	//	}
	//
	public void treeFillDays(final TreeItem parent, final Element element, final int type, final boolean expand) {
		treeFillDays(parent, element, type, expand, null);
	}

	public void treeFillDays(final TreeItem parent, Element element, final int type, final boolean expand, final String name) {
		parent.removeAll();
		if (element != null) {
			if (type == DaysListener.WEEKDAYS || type == DaysListener.MONTHDAYS || type == DaysListener.ULTIMOS || type == DaysListener.SPECIFIC_MONTHS) {
				if (parent.getParentItem().getText().equals("Holidays")) {
					if (element.getChild("holidays") == null)
						element.addContent(new Element("holidays"));
					element = element.getChild("holidays");
					return;
				}
				new DaysListener(objSchedulerDom, element, type, parent.getData(conItemDataKeyKEY) != null
						&& parent.getData(conItemDataKeyKEY).equals("holidays_@_weekdays")).fillTreeDays(parent, expand);
				if (type == DaysListener.SPECIFIC_MONTHS) {
					List l = element.getChildren("month");
					for (int i = 0; i < l.size(); i++) {
						Element e = (Element) l.get(i);
						treeFillRunTimes(parent, e, !Utils.isElementEnabled("job", objSchedulerDom, element), Utils.getAttributeValue("month", e));
					}
				}
			}
			else
				if (type == 4) {
				}
				else
					if (type == 6) {
						new DateListener(objSchedulerDom, element, DateListener.DATE).fillTreeDays(parent, expand);
					}
					else {
						//                System.out.println("Invalid type = " + type + " for filling the days in the tree!");
						System.out.println(SOSJOEMessageCodes.JOE_M_0048.params(type));
					}
		}
	}

	public void createTreeNodes4HotFolderElements(final Tree pobjTreeViewControl, final Composite c, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::createTreeNodes4HotFolderElements";
		logger.debug(String.format("Enter procedure %1$s ", conMethodName));
		objSchedulerHotFolder = pobjHotFolder;
		pobjTreeViewControl.removeAll();
		TreeItem item = new TreeItem(pobjTreeViewControl, SWT.NONE);
		String name = "";
		for (Object objHotFolderObject : pobjHotFolder.getHotFolderFileList().getSortedFileList()) {
			// logger.info(String.format("File '%1$s' is an instance of '%2$s'", objHotFolderObject.getHotFolderSrc().getName(),
			if (objHotFolderObject instanceof SchedulerHotFolder) {
				SchedulerHotFolder objSubFolder = (SchedulerHotFolder) objHotFolderObject;
				logger.info(String.format("... load %1$s", objSubFolder.getHotFolderSrc().getName()));
				item = new TreeItem(pobjTreeViewControl, SWT.NONE);
				item.setData(new TreeData(-1, objSubFolder, "Hallo"));
				item.setText(objSubFolder.getHotFolderSrc().getName());
				item.setExpanded(false);
			}
			else
				if (objHotFolderObject instanceof JSObjJob) {
					JSObjJob objJob = (JSObjJob) objHotFolderObject;
					name = objJob.getHotFolderSrc().getName();
					name = name.substring(0, name.indexOf(JSObjJob.fileNameExtension));
					item.setText(objJob.getName() + " - " + objJob.getTitle());
					item.setData(new TreeData(JOEConstants.JOB, objJob, Options.getHelpURL("job")));
					item.setData(conItemDataKeyKEY, "job");
					item.setData(conItemDataKeyCOPY_ELEMENT, objJob);
					setColorOfJobTreeItem(objJob, item);
					createSubTreeNodes4Job(pobjHotFolder, item, objJob, false);
					item.setExpanded(true);
				}
				else
					if (objHotFolderObject instanceof JSObjJobChain) {
						JSObjJobChain objJobChain = (JSObjJobChain) objHotFolderObject;
						continue;
					}
		}
		pobjTreeViewControl.setSelection(new TreeItem[] { pobjTreeViewControl.getItem(0) });
		treeSelection(pobjTreeViewControl, c);
	}

	public void createTreeNodes4Commands(final TreeItem parent, final JSObjJob pobjJob, final boolean expand) {
		List<Commands> commands = pobjJob.getCommands();
		parent.removeAll();
		for (Commands objCommand : commands) {
			if (objCommand.getOnExitCode() != null) {
				TreeItem item = new TreeItem(parent, SWT.NONE);
				// TODO I18N
				item.setText("on_exit_code");
				item.setData(new TreeData(JOEConstants.JOB_COMMAND_EXIT_CODES, objCommand, Options.getHelpURL("job.commands")));
				item.setData(conItemDataKeyKEY, "commands_@_order");
				item.setData(conItemDataKeyCOPY_ELEMENT, objCommand);
				// if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", pobjJob))) {
				// setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				// }
				// else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				// }
				// TODO activate
				//				treeFillExitCodesCommands(item, objCommand, false);
			}
		}
		parent.setExpanded(expand);
	}

	public void treeFillDays(final TreeItem parent, final JSObjRunTime element, final int type, final boolean expand) {
		if (element != null) {
			treeFillDays(parent, element, type, expand, null);
		}
	}

	public void treeFillDays(final TreeItem parent, final JSObjRunTime element, final int type, final boolean expand, final String name) {
		parent.removeAll();
		if (element != null) {
			if (type == DaysListener.WEEKDAYS || type == DaysListener.MONTHDAYS || type == DaysListener.ULTIMOS || type == DaysListener.SPECIFIC_MONTHS) {
				// TODO
				//				if (parent.getParentItem().getText().equals("Holidays")) {
				//					if (element.getChild("holidays") == null)
				//						element.addContent(new Element("holidays"));
				//					element = element.getChild("holidays");
				//					return;
				//				}
				//
				//				new DaysListener(objSchedulerDom, element, type, (parent.getData(conItemDataKeyKEY) != null && parent.getData(conItemDataKeyKEY).equals(
				//						"holidays_@_weekdays"))).fillTreeDays(parent, expand);
				//				if (type == DaysListener.SPECIFIC_MONTHS) {
				//					List l = element.getChildren("month");
				//					for (int i = 0; i < l.size(); i++) {
				//						Element e = (Element) l.get(i);
				//
				//						// TODO activate
				//						// treeFillRunTimes(parent, e, !Utils.isElementEnabled("job", objSchedulerDom, element),
				//						// Utils.getAttributeValue("month", e));
				//					}
				//				}
			}
			else
				if (type == 4) {
				}
				else
					if (type == 6) {
						// TODO
						//						new DateListener(objSchedulerDom, element, DateListener.DATE).fillTreeDays(parent, expand);
					}
					else {
						System.out.println("Invalid type = " + type + " for filling the days in the tree!");
					}
		}
	}

	public void treeFillSpecificWeekdays(final TreeItem parent, final JSObjRunTime element, final boolean expand) {
		if (element != null) {
			// TODO
			//			new SpecificWeekdaysListener(objSchedulerDom, element).fillTreeDays(parent, expand);
		}
	}

	public void createSubTreeNodes4Job(final SchedulerHotFolder pobjHotFolder, final TreeItem parent, final JSObjJob pobjJob, final boolean expand) {
		// TODO readonly job
		boolean flgIsReadOnlyFile = false; // !Utils.isElementEnabled("job", objSchedulerDom, job);
		parent.removeAll();
		ArrayList<String> l = new ArrayList<String>();
		Color isColor4ReadOnlyFiles = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		// Options
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.options"));
		item.setImage(getImage("options.gif"));
		setItemData(item, JOEConstants.JOB_OPTION, pobjJob, "job");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		// TODO gehört eigentlich an den JobKnoten
		if (flgIsReadOnlyFile) {
			item.setForeground(isColor4ReadOnlyFiles);
		}
		// Parameter
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.parameter"));
		item.setImage(getImage("parameter.gif"));
		item.setData(new TreeData(JOEConstants.JOB_PARAMETER, pobjJob, Options.getHelpURL("parameter")));
		item.setData(conItemDataKeyKEY, "params_@_param");
		ArrayList ll = new ArrayList();
		ll.add("params_@_param");
		ll.add("params_@_include");
		// l.add("environment");
		item.setData(conItemDataKeyKEY, ll);
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		// Mail
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.jobsettings"));
		item.setImage(getImage("10036.gif"));
		item.setData(new TreeData(JOEConstants.SETTINGS, pobjJob, Options.getHelpURL("settings")));
		item.setData(conItemDataKeyKEY, "settings");
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		// Monitor
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel(MONITOR));
		item.setImage(getImage("source_attach_attrib.gif"));
		item.setData(new TreeData(JOEConstants.MONITORS, pobjJob, Options.getHelpURL("job.monitor"), "monitor"));
		item.setData(conItemDataKeyKEY, "monitor");
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		if (flgIsReadOnlyFile) {
			setDisabled(item);
		}
		createPrePostProcessingNodes(item, pobjJob, flgIsReadOnlyFile);
		// RunOptions
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.runoptions"));
		item.setImage(getImage("properties.gif"));
		item.setData(new TreeData(JOEConstants.OPTIONS, pobjJob, Options.getHelpURL("job.options")));
		l = new ArrayList<String>();
		// TODO knoten?
		l.add("start_when_directory_changed");
		l.add("delay_after_error");
		l.add("delay_order_after_setback");
		item.setData(conItemDataKeyKEY, l);
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		if (flgIsReadOnlyFile) {
			setDisabled(item);
		}
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("locks"));
		item.setImage(getImage("lockedstate.gif"));
		item.setData(new TreeData(JOEConstants.LOCKUSE, pobjJob, Options.getHelpURL("job.locks")));
		item.setData(conItemDataKeyKEY, "lock.use");
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		if (flgIsReadOnlyFile) {
			setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		treeFillRunTimes(parent, pobjJob.getRunTimeObj(), flgIsReadOnlyFile, "run_time");
		List commands = pobjJob.getCommands();
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.commands"));
		item.setImage(getImage("commands.gif"));
		if (flgIsReadOnlyFile) {
			setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		if (commands != null) {
			createTreeNodes4Commands(item, pobjJob, false);
		}
		item.setData(new TreeData(JOEConstants.JOB_COMMANDS, pobjJob, Options.getHelpURL("job.commands")));
		item.setData(conItemDataKeyKEY, "commands");
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		parent.setExpanded(expand);
		// Documentation
		item = new TreeItem(parent, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.documentation"));
		item.setImage(getImage("11020.gif"));
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		item.setData(new TreeData(JOEConstants.JOB_DOCUMENTATION, pobjJob, Options.getHelpURL("job")));
		// ArrayList l = new ArrayList();
		// l.add("process");
		// l.add("script");
		item.setData(conItemDataKeyKEY, "job_@_description");
		item.setData(conItemDataKeyCOPY_ELEMENT, pobjJob);
		if (flgIsReadOnlyFile) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
	}

	public void treeFillSpecificWeekdays(final TreeItem parent, final Element element, final boolean expand) {
		if (element != null) {
			new SpecificWeekdaysListener(objSchedulerDom, element).fillTreeDays(parent, expand);
		}
	}

	private Element getJobChains(final SchedulerDom pobjSchedulerDom) {
		Element jobChains = objSchedulerDom.getRoot().getChild("config").getChild("job_chains");
		return jobChains;
	}

	public void createTreeNodes4JobChains(final TreeItem pobjParentTreeNode, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
		pobjParentTreeNode.removeAll();
		int intSize = pobjHotFolder.getHotFolderFileList().getJobChainList().size();
		if (intSize > 0) {
			for (JSObjJobChain objJobChain : pobjHotFolder.getHotFolderFileList().getJobChainList()) {
				TreeItem item = new TreeItem(pobjParentTreeNode, SWT.NONE);
				setItemData(item, JOEConstants.JOB_CHAIN, objJobChain, "treeitem.jobchain");
				item.setText(objJobChain.getObjectNameAndTitle());
				item.setImage(getImage("jobchain.gif"));
				if (objJobChain.isEnabled() == false) {
					setDisabled(item);
				}
				else {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				}
				createSubTreeNodes4JobChain(pobjHotFolder, item, objJobChain);
			}
		}
	}

	private void createSubTreeNodes4JobChain(final SchedulerHotFolder pobjHotFolder, final TreeItem pobjParentItem, final JSObjJobChain pobjJobChain) {
		// Job Chain Nodes
		TreeItem iNodes = new TreeItem(pobjParentItem, SWT.NONE);
		iNodes.setText(Messages.getLabel("treeitem.nodes"));
		iNodes.setImage(getImage("jobchain.gif"));
		setItemData(iNodes, JOEConstants.JOB_CHAIN_NODES, pobjJobChain, "job_chain_node");
		// Job Chain Nested Nodes
		TreeItem iNestedNodes = new TreeItem(pobjParentItem, SWT.NONE);
		iNestedNodes.setText(Messages.getLabel("treeitem.nestedjobchains"));
		iNestedNodes.setImage(getImage("jobchain.gif"));
		setItemData(iNodes, JOEConstants.JOB_CHAIN_NESTED_NODES, pobjJobChain, "nestedjobchains");
		if (pobjJobChain.isEnabled() == false) {
			setDisabled(iNodes);
			setDisabled(iNestedNodes);
		}
		else {
			iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
	}

	//
	//	public void treeFillJobChains(final TreeItem parent) {
	//		parent.removeAll();
	//		Element jobChains = getJobChains(objSchedulerDom);
	//		if (jobChains != null) {
	//			Iterator it = getSortedIterator(jobChains.getChildren(), new CompareNameAndTitle());
	//			while (it.hasNext()) {
	//				Object o = it.next();
	//				if (o instanceof Element) {
	//					Element element = (Element) o;
	//					TreeItem i = new TreeItem(parent, SWT.NONE);
	//					{
	//						i.setImage(getImage("jobchain.gif"));
	//						TreeData objTD = new TreeData(i, JOEConstants.JOB_CHAIN, element, Options.getHelpURL("job_chain"));
	//						i.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
	//					}
	//					// Job Chain Nodes
	//					TreeItem iNodes = new TreeItem(i, SWT.NONE);
	//					{
	//						iNodes.setImage(getImage("jobchain.gif"));
	//						TreeData objTD = new TreeData(iNodes, JOEConstants.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain"));
	//						iNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StepsNodes.label());
	//						iNodes.setData(conItemDataKeyKEY, "job_chain_node");
	//					}
	//					Iterator objNodesIt = element.getChildren().iterator();
	//
	//					while (objNodesIt.hasNext()) {
	//						Element node = (Element) objNodesIt.next();
	//						if (node.getName().equals("job_chain_node")) {
	//							String strJobName = Utils.getAttributeValue("job", node);
	//							String strState = Utils.getAttributeValue("state", node);
	//
	//							Tree tree = objTree;
	//							TreeItem objRootItem = tree.getItem(0);
	//							for (int k = 0; k < objRootItem.getItemCount(); k++) {
	//								TreeItem item = objRootItem.getItem(k);
	//								TreeData objTreeData = (TreeData) item.getData();
	//								if (objTreeData != null && objTreeData.TypeEqualTo(JOEConstants.JOBS)) { // Jobs node
	//									/*
	//									 * Loop over all Jobs in the Tree. Attention: not all Jobs are in the tree due to subfolders
	//									 */
	//									// TODO avoid loop over all Items in Treeview. Try direct search in list of all jobs
	//									for (TreeItem jItem : item.getItems()) {
	//										objTreeData = (TreeData) jItem.getData();
	//										Element objJob2Insert = objTreeData.getElement();
	//										String strName = objTreeData.getName();
	//
	//										if (strJobName.equals(strName)) {
	//											createJobItem(iNodes, objJob2Insert, strState + " - ");
	//											break;
	//										}
	//									}
	//								}
	//							}
	//						}
	//					}
	//
	//					iNodes.setExpanded(true);
	//
	//					// Job Chain Nested Nodes
	//					TreeItem iNestedNodes = new TreeItem(i, SWT.NONE);
	//					iNestedNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_NestedJobChains.label());
	//					iNestedNodes.setImage(getImage("jobchain.gif"));
	//					iNestedNodes.setData(new TreeData(JOEConstants.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("job_chain")));
	//					iNestedNodes.setData(conItemDataKeyKEY, "job_chain_node.job_chain");
	//					iNestedNodes.setData(conItemDataKeyCOPY_ELEMENT, element);
	//					iNestedNodes.setExpanded(true);
	//					if (!Utils.isElementEnabled("job_chain", objSchedulerDom, element)) {
	//						setDisabled(i); // i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//						setDisabled(iNodes); // iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//						setDisabled(iNestedNodes); // iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	//					}
	//					else {
	//						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//						iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//						iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	//					}
	//
	//					TreeItem objOrders4JobChain = new TreeItem(i, SWT.NONE);
	//					objOrders4JobChain.setText("Orders");
	//					objOrders4JobChain.setImage(getImage("orders.gif"));
	//					objOrders4JobChain.setData(new TreeData(JOEConstants.ORDERS, element, Options.getHelpURL("orders")));
	//					objOrders4JobChain.setData(conItemDataKeyKEY, "orders.job_chain");
	//					objOrders4JobChain.setData(conItemDataKeyCOPY_ELEMENT, element);
	//					objOrders4JobChain.setExpanded(true);
	//				}
	//			}
	//		}
	//		parent.setExpanded(true);
	//	}
	//
	//	private String getNameAndTitle(final Element element, final String pstrI18NKey) {
	//		String name = Utils.getAttributeValue("name", element);
	//		if (name == null || name.length() <= 0) {
	//			name = element.getAttributeValue("id");
	//		}
	//		if (name == null) {
	//			name = "???";
	//		}
	//		String strTitle = Utils.getAttributeValue("title", element);
	//		String jobChainName = name;
	//
	//		jobChainName += !objSchedulerDom.isEnabled(element) ? SOSJOEMessageCodes.JOE_M_JobCommand_Disabled.label() : "";
	//
	//		if (strTitle != null && strTitle.trim().length() > 0) {
	//			jobChainName += " - " + strTitle;
	//		}
	//		return jobChainName;
	//	}
	public boolean treeSelection(final Tree tree, final Composite pobjParentComposite) {
		try {
			if (tree.getSelectionCount() > 0) {
				// dispose the old form
				Control[] children = pobjParentComposite.getChildren();
				for (int i = 0; i < children.length; i++) {
					if (!Utils.applyFormChanges(children[i]))
						return false;
					children[i].dispose();
				}
				TreeItem objSelectedTreeItem = tree.getSelection()[0];
				TreeData objTreeItemUserdata = (TreeData) objSelectedTreeItem.getData();
				if (objTreeItemUserdata != null) {
					objSchedulerDom.setInit(true);
					if (pobjParentComposite.getChildren().length > 0) {
					}
					Element objElement = objTreeItemUserdata.getElement();
					int intType = objTreeItemUserdata.getType();
					pobjParentComposite.setLayout(new FillLayout());
					switch (intType) {
						case JOEConstants.ROOT_FOLDER:
							break;
						case JOEConstants.SUB_FOLDER:
							if (objSelectedTreeItem.getItemCount() > 0) {
							}
							else {
								SchedulerHotFolder objFolder = (SchedulerHotFolder) objTreeItemUserdata.getObject();
								objFolder.load();
								treeFillMain1(objFolder, objSelectedTreeItem, pobjParentComposite);
							}
							objSelectedTreeItem.setExpanded(true);
							break;
						case JOEConstants.CONFIG:  // JobScheduler Configuration
							new ConfigForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objSchedulerForm);
							break;
						case JOEConstants.PARAMETER:  // global JobScheduler Parameter
							type = objTreeItemUserdata.getType();
							new ParameterForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.JOB_PARAMETER:
							type = objTreeItemUserdata.getType();
							JSObjJob objJSJob = objTreeItemUserdata.getJob();
							String jobname = objJSJob.getJobName();
							new JobParameterForm(pobjParentComposite, objTreeItemUserdata.getJob(), objSchedulerForm, type);
							break;
						case JOEConstants.SECURITY:  // JobScheduler Configuration
							new SecurityForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.CLUSTER:  // JobScheduler Configuration
							new ClusterForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.BASE:  // JobScheduler Configuration
							new BaseForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.PROCESS_CLASSES:
							new ProcessClassesForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.LOCKS:
							new LocksForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.MONITORS:
							new ScriptsForm(pobjParentComposite,  objTreeItemUserdata);
							break;
						case JOEConstants.MONITOR:
							new ScriptFormPreProcessing(pobjParentComposite,   objTreeItemUserdata);
							break;
						case JOEConstants.SCRIPT:   // JobScheduler Configuration
							new ScriptFormSchedulerStartScript(pobjParentComposite,   objTreeItemUserdata);
							break;
						case JOEConstants.JOB:
							//							new ScriptJobMainForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							new ScriptJobMainForm(pobjParentComposite, objTreeItemUserdata);
							break;
						case JOEConstants.JOB_OPTION:
							//							new JobMainOptionForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							type = objTreeItemUserdata.getType();
							objJSJob = objTreeItemUserdata.getJob();
							jobname = objJSJob.getJobName();
							new JobMainOptionForm(pobjParentComposite, SWT.NONE, objTreeItemUserdata, objSchedulerForm);
							break;
						case JOEConstants.JOB_DOCUMENTATION:
							new JobDocumentationForm(pobjParentComposite, SWT.NONE, objTreeItemUserdata, objSchedulerForm);
							break;
						case JOEConstants.SETTINGS:
							new MailForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							break;
						case JOEConstants.ORDERS:
							new OrdersForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objSchedulerForm);
							break;
						case JOEConstants.ORDER:
							new OrderForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.JOB_COMMAND_EXIT_CODES:
							new sos.scheduler.editor.conf.forms.JobCommandExitCodesForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement,
									objSchedulerForm);
							break;
						case JOEConstants.JOB_COMMAND:
							new JobCommandForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.JOB_COMMANDS:
							new JobCommandsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, this);
							break;
						case JOEConstants.RUNTIME:
							new RunTimeForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.WEEKDAYS:
							new DaysForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.WEEKDAYS,
									objSelectedTreeItem.getData(conItemDataKeyKEY) != null
											&& objSelectedTreeItem.getData(conItemDataKeyKEY).equals("holidays_@_weekdays"));
							break;
						case JOEConstants.MONTHDAYS:
							new DaysForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.MONTHDAYS, false);
							break;
						case JOEConstants.SPECIFIC_MONTHS:
							new DaysForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.SPECIFIC_MONTHS, false);
							break;
						case JOEConstants.ULTIMOS:
							new DaysForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.ULTIMOS, false);
							break;
						case JOEConstants.EVERYDAY:
						case JOEConstants.PERIODS:
							new PeriodsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.JOBS:
							new JobsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objSchedulerForm);
							break;
						case JOEConstants.HOLIDAYS:
							new DateForm(pobjParentComposite, SWT.NONE, DateListener.HOLIDAY, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.DAYS:
							new DateForm(pobjParentComposite, SWT.NONE, DateListener.DATE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.SPECIFIC_WEEKDAYS:
							new SpecificWeekdaysForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.MONTHDAYS);
							break;
						case JOEConstants.WEBSERVICES:
							new WebservicesForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.WEBSERVICE:
							new WebserviceForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.HTTPDIRECTORIES:
							new HttpDirectoriesForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							break;
						case JOEConstants.HTTP_AUTHENTICATION:
							new HttpAuthenticationForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							break;
						case JOEConstants.OPTIONS:
							new JobOptionsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							break;
						case JOEConstants.LOCKUSE:
							new JobLockUseForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							break;
						case JOEConstants.JOB_CHAINS:
							new JobChainsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.JOB_CHAIN:
							//							JobChainForm jc_ = new JobChainForm(pobjParentComposite, SWT.NONE, objTreeItemUserdata);
							//							break;
						case JOEConstants.JOB_CHAIN_NODES:
							JobChainNodesForm jcn_ = new JobChainNodesForm(pobjParentComposite, SWT.NONE, objTreeItemUserdata);
							jcn_.setISchedulerUpdate(objSchedulerForm);
							break;
						case JOEConstants.JOB_CHAIN_NESTED_NODES:
							JobChainNestedNodesForm j = new JobChainNestedNodesForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement);
							j.setISchedulerUpdate(objSchedulerForm);
							break;
						case JOEConstants.COMMANDS:
							new CommandsForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objSchedulerForm);
							break;
						case JOEConstants.SCHEDULES:
							new SchedulesForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objSchedulerForm);
							break;
						case JOEConstants.SCHEDULE:
							new ScheduleForm(pobjParentComposite, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
							break;
						case JOEConstants.HTTP_SERVER:
							break;
						default:
							//                        System.out.println("no form found for " + objSelectedTreeItem.getText());
							System.out.println(SOSJOEMessageCodes.JOE_M_0049.params(objSelectedTreeItem.getText()));
					}
				}
				// c.setLayout(new FillLayout());
				pobjParentComposite.layout();
			}
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
		}
		objSchedulerDom.setInit(false);
		return true;
	}

	private void checkLifeAttributes(final Element element, final String name) {
		if (element.getName().equals("add_order") || element.getName().equals("order")) {
			if ((Utils.getAttributeValue("job_chain", element) + "," + Utils.getAttributeValue("id", element)).equalsIgnoreCase(name)) {
				// Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				if (objSchedulerDom.getListOfChangeElementNames() != null)
					l = objSchedulerDom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				objSchedulerDom.setListOfChangeElementNames(l);
			}
		}
		else {
			if (Utils.getAttributeValue("name", element).length() > 0 && !Utils.getAttributeValue("name", element).equalsIgnoreCase(name)) {
				// Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				if (objSchedulerDom.getListOfChangeElementNames() != null)
					l = objSchedulerDom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				objSchedulerDom.setListOfChangeElementNames(l);
				objSchedulerDom.setChanged(true);
			}
			if (element.getName().equals("job") && Utils.getAttributeValue("spooler_id", element).length() > 0) {
				element.removeAttribute("spooler_id");
				ArrayList l = new ArrayList();
				if (objSchedulerDom.getListOfChangeElementNames() != null)
					l = objSchedulerDom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				objSchedulerDom.setListOfChangeElementNames(l);
				objSchedulerDom.setChanged(true);
			}
		}
	}

	public void treeFillRunTimes(TreeItem item, Element job, final boolean disable, final String run_time) {
		Element runtime = null;
		Element _runtime = job.getChild("run_time");
		// create runtime tag
		if (_runtime == null && run_time.equals("run_time")) {
			_runtime = new Element(run_time);
			job.addContent(_runtime);
			runtime = _runtime;
		}
		else
			if (!run_time.equals("run_time") && job.getName().equals("month")) {
				// _runtime = Utils.getRunTimeParentElement(job);
				_runtime = job.getParentElement();
			}
			else
				if (job.getName().equals("schedule")) {
					runtime = job;
				}
				else {
					runtime = _runtime;
				}
		// Specific months: create month child tags (type=3)
		if (!run_time.equals("run_time") && !job.getName().equals("schedule")) {
			List _monthList = _runtime.getChildren("month");
			// List _monthList = job.getParentElement().getChildren("month");
			boolean monthFound = false;
			for (int i = 0; i < _monthList.size(); i++) {
				Element _month = (Element) _monthList.get(i);
				if (Utils.getAttributeValue("month", _month).equalsIgnoreCase(run_time)) {
					monthFound = true;
					job = _month;
					runtime = job;
				}
			}
			if (!monthFound) {
				Element newMonth = new Element("month");
				Utils.setAttribute("month", run_time, newMonth);
				// _runtime.addContent(newMonth);
				job.addContent(newMonth);
				job = newMonth;
				runtime = job;
			}
		}
		TreeItem runTreeItem = null;
		boolean hasSchedulesAttribut = Utils.getAttributeValue("schedule", _runtime).trim().length() > 0;
		if (hasSchedulesAttribut) {
			for (int i = 0; i < item.getItemCount(); i++) {
				//          	if (item.getItem(i).equals("Run Time")) {
				if (item.getItem(i).getText().equals(SOSJOEMessageCodes.JOE_M_SchedulerListener_RunTime.label())) {
					runTreeItem = item.getItem(i);
				}
			}
		}
		//        if (item.getText().equals("Run Time")) {
		if (item.getText().equals(SOSJOEMessageCodes.JOE_M_SchedulerListener_RunTime.label())) {
			runTreeItem = item;
		}
		// ende test
		// if(run == null) {
		if (runtime != null) {
			if (type == SchedulerDom.LIFE_SCHEDULE) {
				runTreeItem = item;
			}
			else
				if (runTreeItem == null) {
					runTreeItem = new TreeItem(item, SWT.NONE);
				}
			runTreeItem.setData(conItemDataKeyMAX_OCCUR, "1");
			runTreeItem.setImage(getImage("waiting.gif"));
			runTreeItem.setText(run_time);
			// run.setText(Messages.getLabel("treeitem." + run_time));
			// if (run_time.equals("run_time")) {
			// run.setText("Run Time");
			// }
			// else {
			// run.setText(run_time);
			// }
			if (run_time.equals("run_time")) {
				// runTreeItem.setText("Run Time");
				runTreeItem.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_RunTime.label());
				runTreeItem.setData(new TreeData(JOEConstants.RUNTIME, job, Options.getHelpURL("job.run_time"), "run_time"));
				runTreeItem.setData(conItemDataKeyKEY, "run_time");
				runTreeItem.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
				runTreeItem.setData(conItemDataKeyMAX_OCCUR, "1");
				runTreeItem.setData(conItemDataKeyCOPY_ELEMENT, job);
				if (disable) {
					runTreeItem.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				objSchedulerForm.updateFont(runTreeItem);
			}
			else
				if (job.getName().equals("schedule")) {
					runTreeItem.setText(run_time);
					runTreeItem.setData(new TreeData(JOEConstants.SCHEDULE, job, Options.getHelpURL("job.schedule"), "schedule"));
					runTreeItem.setData(conItemDataKeyKEY, "schedules_@_schedule");
					runTreeItem.setData(conItemDataKeyCOPY_ELEMENT, job);
				}
			item = new TreeItem(runTreeItem, SWT.NONE);
			//            item.setText(Messages.getLabel("treeitem.everyday"));
			item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_EveryDay.label());
			item.setData(new TreeData(JOEConstants.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));
			item.setData(conItemDataKeyKEY, "period");
			item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
			if (disable) {
				setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			objSchedulerForm.updateFont(item);
			item = new TreeItem(runTreeItem, SWT.NONE);
			//            item.setText(Messages.getLabel("treeitem.weekdays"));
			item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Weekdays.label());
			item.setData(new TreeData(JOEConstants.WEEKDAYS, runtime, Options.getHelpURL("job.run_time.weekdays"), "weekdays"));
			item.setData(conItemDataKeyKEY, "weekdays");
			item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
			item.setData(conItemDataKeyMAX_OCCUR, "1");
			item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
			if (disable) {
				setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 0, false);
			objSchedulerForm.updateFont(item);
			item = new TreeItem(runTreeItem, SWT.NONE);
			//            item.setText(Messages.getLabel("treeitem.monthdays"));
			item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Monthdays.label());
			item.setData(new TreeData(JOEConstants.MONTHDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"), "monthdays"));
			item.setData(conItemDataKeyKEY, "monthdays");
			item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
			item.setData(conItemDataKeyMAX_OCCUR, "1");
			item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
			if (disable) {
				setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 1, false);
			objSchedulerForm.updateFont(item);
			item = new TreeItem(runTreeItem, SWT.NONE);
			//            item.setText(Messages.getLabel("treeitem.ultimos"));
			item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Ultimos.label());
			item.setData(new TreeData(JOEConstants.ULTIMOS, runtime, Options.getHelpURL("job.run_time.ultimos"), "ultimos"));
			item.setData(conItemDataKeyKEY, "ultimos");
			item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
			item.setData(conItemDataKeyMAX_OCCUR, "1");
			item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
			if (disable) {
				setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 2, false);
			objSchedulerForm.updateFont(item);
			item = new TreeItem(runTreeItem, SWT.NONE);
			//            item.setText(Messages.getLabel("treeitem.specificweekdays"));
			item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_SpecificWeekdays.label());
			item.setData(new TreeData(JOEConstants.SPECIFIC_WEEKDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"), "monthdays"));
			item.setData(conItemDataKeyKEY, "monthdays");
			item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
			item.setData(conItemDataKeyMAX_OCCUR, "1");
			item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
			if (disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillSpecificWeekdays(item, runtime, false);
			objSchedulerForm.updateFont(item);
			// Specific Days
			if (run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(runTreeItem, SWT.NONE);
				//                item.setText(Messages.getLabel("treeitem.specificdays"));
				item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_SpecificDays.label());
				item.setData(new TreeData(JOEConstants.DAYS, runtime, Options.getHelpURL("job.run_time.specific_days")));
				item.setData(conItemDataKeyKEY, "specific_days");
				item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
				item.setData(conItemDataKeyMAX_OCCUR, "1");
				item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
				treeFillDays(item, runtime, 6, false);
				objSchedulerForm.updateFont(item);
			}
			if (disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			// Specific Monthdays
			if (run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(runTreeItem, SWT.NONE);
				//                item.setText(Messages.getLabel("treeitem.specificmonth"));
				item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_SpecificMonth.label());
				item.setData(new TreeData(JOEConstants.SPECIFIC_MONTHS, runtime, Options.getHelpURL("job.run_time.monthdays"), "month"));
				item.setData(conItemDataKeyKEY, "specific_monthdays");
				item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
				item.setData(conItemDataKeyMAX_OCCUR, "1");
				item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
				if (disable) {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				treeFillDays(item, runtime, DaysListener.SPECIFIC_MONTHS, false);
				objSchedulerForm.updateFont(item);
			}
			// holidays
			if (run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(runTreeItem, SWT.NONE);
				item.setData(new TreeData(JOEConstants.HOLIDAYS, runtime, Options.getHelpURL("holidays"), "holidays"));
				item.setData(conItemDataKeyKEY, "holidays");
				item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
				item.setData(conItemDataKeyMAX_OCCUR, "1");
				item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
				//                item.setText(Messages.getLabel("treeitem.holidays"));
				item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Holidays.label());
				item.setImage(getImage("holidays.gif"));
				treeFillHolidays(item, runtime);
			}
			/*if (runtime != null)
				treeFillDays(item, runtime, 6, false);
			*/
		}
	}

	/*public void fillCommands(Element job, TreeItem parent, boolean expand) {
	    List commands = job.getChildren("commands");
	    java.util.ArrayList listOfReadOnly = objSchedulerDom.getListOfReadOnlyFiles();
	    if (commands != null) {
	        Iterator it = commands.iterator();
	        parent.removeAll();

	        while (it.hasNext()) {
	            Element e = (Element) it.next();
	            if (e.getAttributeValue("on_exit_code") != null) {
	                TreeItem item = new TreeItem(parent, SWT.NONE);
	                item.setText(e.getAttributeValue("on_exit_code"));
	                item.setData(new TreeData(JOEConstants.JOB_COMMAND, e, Options.getHelpURL("job.commands")));

	                if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
	                	item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	                } else {
	                	item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	                }
	            }
	        }
	    }
	    parent.setExpanded(expand);

	}
	 */
	private int getType(final Element elem) {
		if (elem.getName().equals("job"))
			return JOEConstants.JOB;
		else
			if ((elem.getName().equals("order") || elem.getName().equals("add_order") || elem.getName().equals("start_job"))
					&& Utils.getJobElement(elem).getName().equals("job"))
				return JOEConstants.JOB_COMMANDS;
			else
				if ((elem.getName().equals("order") || elem.getName().equals("add_order"))
						&& (objSchedulerDom.isLifeElement() || elem.getParentElement() != null && elem.getParentElement().getName().equals("commands")))
					return JOEConstants.COMMANDS;
				/*else if( (elem.getName().equals("order") || elem.getName().equals("add_order") || elem.getName().equals("start_job")) )
					return JOEConstants.JOB_COMMANDS;
					*/
				else
					if (elem.getName().equals("web_service"))
						return JOEConstants.WEBSERVICE;
					else
						return JOEConstants.CONFIG;
	}

	public void treeFillWebServices(final TreeItem parent) {
		parent.removeAll();
		Element httpServer = null;
		Element config = objSchedulerDom.getRoot().getChild("config");
		if (config != null)
			httpServer = objSchedulerDom.getRoot().getChild("config").getChild("http_server");
		if (httpServer != null) {
			Iterator it = getSortedIterator(httpServer.getChildren("web_service"), new CompareNameAndTitle());
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setData(new TreeData(JOEConstants.WEBSERVICE, element, Options.getHelpURL("http_server"), "http_server"));
					item.setData(conItemDataKeyKEY, "http_server_@_web_service");
					item.setData(conItemDataKeyCOPY_ELEMENT, element);
					//                    item.setText(Messages.getLabel("treeitem.webservice") + ": " + element.getAttributeValue("name"));
					item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Webservice.params(": " + element.getAttributeValue("name")));
					TreeItem itemParam = new TreeItem(item, SWT.NONE);
					itemParam.setData(new TreeData(JOEConstants.PARAMETER, element, Options.getHelpURL("parameter")));
					itemParam.setData(conItemDataKeyKEY, "params_@_param");
					item.setData(conItemDataKeyCOPY_ELEMENT, element);
					//                    itemParam.setText(Messages.getLabel("treeitem.parameter"));
					itemParam.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Parameter.label());
				}
			}
		}
		parent.setExpanded(true);
	}

	public void treeFillSchedules(final TreeItem parent) {
		parent.removeAll();
		Element schedules = objSchedulerDom.getRoot().getChild("config").getChild("schedules");
		if (schedules != null) {
			Iterator it = schedules.getChildren().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					if (type == SchedulerDom.DIRECTORY) {
						checkLifeAttributes(element, Utils.getAttributeValue("name", element));
					}
					// TODO activate
					// treeFillRunTimes(parent, element, false, Utils.getAttributeValue("name", element));
				}
			}
		}
		parent.setExpanded(true);
	}

	public void createPrePostProcessingNodes(final TreeItem parent, final JSObjJob pobjJob, final boolean disable) {
		parent.removeAll();
		for (Job.Monitor monitor : pobjJob.getMonitor()) {
			TreeItem item = new TreeItem(parent, SWT.NONE);
			if (monitor.getName().length() >= 0) {
				item.setText(Messages.getLabel("treeitem.empty"));
			}
			else {
				item.setText(monitor.getName());
			}
			item.setImage(getImage("source_attach_attrib.gif"));
			item.setData(new TreeData(JOEConstants.MONITOR, monitor, Options.getHelpURL("job.monitor"), "monitor"));
			item.setData(conItemDataKeyKEY, "monitor");
			item.setData(conItemDataKeyCOPY_ELEMENT, monitor);
			if (disable) {
				setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
		}
		parent.setExpanded(true);
	}

	private TreeData setItemData(final TreeItem objTreeItem, final int type, final Object pobjHotFolderObject, final String helpKey) {
		TreeData objTreeData = new TreeData(type, pobjHotFolderObject, Options.getHelpURL(helpKey));
		// TODO TreeItem merken in TreeData
		// TODO setText für TreeItem über getObjectNameAndTitle (Interface für alle Objekte)
		objTreeItem.setData(objTreeData);
		objTreeItem.setData(conItemDataKeyKEY, helpKey);
		objTreeItem.setData(conItemDataKeyCOPY_ELEMENT, pobjHotFolderObject);
		objTreeData.setTreeItem(objTreeItem);
		return objTreeData;
	}

	public void treeFillRunTimes(TreeItem item, final JSObjRunTime pobjRunTime, final boolean disable, final String pstrTreeNodeLabelText) {
		TreeItem runTreeItem = new TreeItem(item, SWT.NONE);
		runTreeItem.setData(conItemDataKeyMAX_OCCUR, "1");
		runTreeItem.setImage(getImage("waiting.gif"));
		runTreeItem.setText(Messages.getLabel("treeitem." + pstrTreeNodeLabelText));
		TreeData objTreeData = setItemData(runTreeItem, JOEConstants.RUNTIME, pobjRunTime, "treeitem." + pstrTreeNodeLabelText);
		// SubNodes of run_time
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.everyday"));
		objTreeData = setItemData(item, JOEConstants.EVERYDAY, pobjRunTime, "treeitem.everyday");
		item.setData(conItemDataKeyKEY, "period");
		setDisabled(item, disable);
		objTreeData.UpdateFont(pobjRunTime.hasPeriod() || pobjRunTime.hasAt());
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.weekdays"));
		objTreeData = setItemData(item, JOEConstants.WEEKDAYS, pobjRunTime, "treeitem.job.run_time.weekdays");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		setDisabled(item, disable);
		treeFillDays(item, pobjRunTime, 0, false);
		objTreeData.UpdateFont(pobjRunTime.hasWeekdays());
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.monthdays"));
		objTreeData = setItemData(item, JOEConstants.MONTHDAYS, pobjRunTime, "treeitem.job.run_time.monthdays");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		setDisabled(item, disable);
		treeFillDays(item, pobjRunTime, 1, false);
		objTreeData.UpdateFont(pobjRunTime.hasMonthdays());
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.ultimos"));
		objTreeData = setItemData(item, JOEConstants.ULTIMOS, pobjRunTime, "treeitem.job.run_time.ultimos");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		setDisabled(item, disable); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		treeFillDays(item, pobjRunTime, 2, false);
		objTreeData.UpdateFont(pobjRunTime.hasUltimos());
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.specificweekdays"));
		objTreeData = setItemData(item, JOEConstants.SPECIFIC_WEEKDAYS, pobjRunTime, "treeitem.job.run_time.monthdays");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		setDisabled(item, disable);
		treeFillSpecificWeekdays(item, pobjRunTime, false);
		objTreeData.UpdateFont(pobjRunTime.getMonthdays().getDayOrWeekday().size() > 0);
		// Specific Days
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.specificdays"));
		objTreeData = setItemData(item, JOEConstants.DAYS, pobjRunTime, "treeitem.job.run_time.specific_days");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		treeFillDays(item, pobjRunTime, 6, false);
		objTreeData.UpdateFont(pobjRunTime.hasDate());
		setDisabled(item, disable);
		// Specific Monthdays
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.specificmonth"));
		objTreeData = setItemData(item, JOEConstants.MONTHDAYS, pobjRunTime, "treeitem.job.run_time.monthdays");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		setDisabled(item, disable);
		treeFillDays(item, pobjRunTime, DaysListener.SPECIFIC_MONTHS, false);
		objTreeData.UpdateFont(pobjRunTime.hasMonth());
		// holidays
		item = new TreeItem(runTreeItem, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.holidays"));
		item.setImage(getImage("holidays.gif"));
		objTreeData = setItemData(item, JOEConstants.HOLIDAYS, pobjRunTime, "treeitem.job.run_time.holidays");
		item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
		item.setData(conItemDataKeyMAX_OCCUR, "1");
		treeFillHolidays(item, pobjRunTime);
		objTreeData.UpdateFont(pobjRunTime.hasHolidays());
		setDisabled(item, disable);
	}

	//	public void treeFillWebServices(final TreeItem parent) {
	//		parent.removeAll();
	//		Element httpServer = null;
	//		Element config = objSchedulerDom.getRoot().getChild("config");
	//		if (config != null)
	//			httpServer = objSchedulerDom.getRoot().getChild("config").getChild("http_server");
	//		if (httpServer != null) {
	//			Iterator it = httpServer.getChildren("web_service").iterator();
	//			while (it.hasNext()) {
	//				Object o = it.next();
	//				if (o instanceof Element) {
	//					Element element = (Element) o;
	//					TreeItem item = new TreeItem(parent, SWT.NONE);
	//					item.setData(new TreeData(JOEConstants.WEBSERVICE, element, Options.getHelpURL("http_server"), "http_server"));
	//					item.setData(conItemDataKeyKEY, "http_server_@_web_service");
	//					item.setData(conItemDataKeyCOPY_ELEMENT, element);
	//					item.setText(Messages.getLabel("treeitem.webservice") + ": " + element.getAttributeValue("name"));
	//					TreeItem itemParam = new TreeItem(item, SWT.NONE);
	//					itemParam.setData(new TreeData(JOEConstants.PARAMETER, element, Options.getHelpURL("parameter")));
	//					itemParam.setData(conItemDataKeyKEY, "params_@_param");
	//					item.setData(conItemDataKeyCOPY_ELEMENT, element);
	//					itemParam.setText(Messages.getLabel("treeitem.parameter"));
	//				}
	//			}
	//		}
	//		parent.setExpanded(true);
	//	}
	private void setDisabled(final TreeItem pobjC, final boolean pflgFlag) {
		if (pflgFlag)
			setDisabled(pobjC);
	}

	public void treeFillHolidays(TreeItem item, final JSObjRunTime pobjRunTime) {
		item = new TreeItem(item, SWT.NONE);
		item.setText(Messages.getLabel("treeitem.weekdays"));
		TreeData objTreeData = setItemData(item, JOEConstants.WEEKDAYS, pobjRunTime, "job.run_time.weekdays");
		if (pobjRunTime != null && pobjRunTime.hasHolidays()) {
			treeFillDays(item, pobjRunTime, 0, false);
			objTreeData.UpdateFont(true);
		}
	}

	public void treeFillExitCodesCommands(final TreeItem parent, final List<Commands> cmdList, final boolean expand) {
		parent.removeAll();
		treeFillExitCodesCommands(parent, cmdList);
		treeFillExitCodesCommands(parent, cmdList);
		treeFillExitCodesCommands(parent, cmdList);
	}

	private void treeFillExitCodesCommands(final TreeItem parent, final List<Commands> cmdList) {
		for (Commands commands : cmdList) {
			for (Object command : commands.getAddJobsOrAddOrderOrCheckFolders()) {
				if (command instanceof AddJobs) {
				}
				//				if (command instanceof AddOrders) {
				//
				//				}
				if (command instanceof CheckFolders) {
				}
			}
		}
	}

	private void createTreeNodes4SubFolders(final Tree pobjTree, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
		pobjTree.removeAll();
		if (pobjHotFolder.getHotFolderFileList().getFolderList().size() > 0) {
			for (SchedulerHotFolder objFolder : pobjHotFolder.getHotFolderFileList().getFolderList()) {
				String name = objFolder.getHotFolderSrc().getName();
				name = name.replace(pobjHotFolder.getHotFolderSrc().getName(), ".");
				TreeItem item = new TreeItem(pobjTree, SWT.NONE);
				item.setText(name);
				item.setImage(getImage("folder.gif"));
				setItemData(item, JOEConstants.SUB_FOLDER, objFolder, "folder");
			}
		}
	}

	public void createTreeNodes4Jobs(final TreeItem pobjParentTreeNode, final SchedulerHotFolder pobjHotFolder) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
		pobjParentTreeNode.removeAll();
		int intSize = pobjHotFolder.getHotFolderFileList().getJobList().size();
		if (intSize > 0) {
			for (JSObjJob objJob : pobjHotFolder.getHotFolderFileList().getJobList()) {
				TreeItem item = new TreeItem(pobjParentTreeNode, SWT.NONE);
				setItemData(item, JOEConstants.JOB, objJob, "job");
				item.setText(objJob.getJobNameAndTitle());
				setColorOfJobTreeItem(objJob, item);
				createSubTreeNodes4Job(pobjHotFolder, item, objJob, false);
			}
		}
		pobjParentTreeNode.setExpanded(true);
	}
}
