package sos.scheduler.editor.conf.listeners;
import java.io.File;
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
import org.jdom.Attribute;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.ClusterForm;
import sos.scheduler.editor.conf.forms.CommandsForm;
import sos.scheduler.editor.conf.forms.ConfigForm;
import sos.scheduler.editor.conf.forms.DateForm;
import sos.scheduler.editor.conf.forms.DaysForm;
import sos.scheduler.editor.conf.forms.HttpAuthenticationForm;
import sos.scheduler.editor.conf.forms.HttpDirectoriesForm;
import sos.scheduler.editor.conf.forms.JobChainForm;
import sos.scheduler.editor.conf.forms.JobChainNestedNodesForm;
import sos.scheduler.editor.conf.forms.JobChainNodesForm;
import sos.scheduler.editor.conf.forms.JobChainsForm;
import sos.scheduler.editor.conf.forms.JobCommandForm;
import sos.scheduler.editor.conf.forms.JobCommandsForm;
import sos.scheduler.editor.conf.forms.JobDocumentationForm;
import sos.scheduler.editor.conf.forms.JobLockUseForm;
import sos.scheduler.editor.conf.forms.JobMainOptionForm;
import sos.scheduler.editor.conf.forms.JobOptionsForm;
import sos.scheduler.editor.conf.forms.JobsForm;
import sos.scheduler.editor.conf.forms.LocksForm;
import sos.scheduler.editor.conf.forms.MailForm;
import sos.scheduler.editor.conf.forms.OrderForm;
import sos.scheduler.editor.conf.forms.OrdersForm;
import sos.scheduler.editor.conf.forms.PeriodsForm;
import sos.scheduler.editor.conf.forms.ProcessClassesForm;
import sos.scheduler.editor.conf.forms.RunTimeForm;
import sos.scheduler.editor.conf.forms.SchedulerBaseFileForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.forms.ScriptFormPreProcessing;
import sos.scheduler.editor.conf.forms.ScriptFormSchedulerStartScript;
import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.forms.ScriptsForm;
import sos.scheduler.editor.conf.forms.SecurityForm;
import sos.scheduler.editor.conf.forms.SpecificWeekdaysForm;
import sos.scheduler.editor.conf.forms.WebserviceForm;
import sos.scheduler.editor.conf.forms.WebservicesForm;
import sos.util.SOSClassUtil;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

//import sos.scheduler.editor.conf.ISchedulerUpdate;
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
    private static final String                         conItemDataKeyVISIBLE               = "visible";
    private static final String                         conItemDataKeyORDERS_RECOVERABLE    = "orders_recoverable";
    private static final String                         conItemDataKeyOVERRIDE_ATTRIBUTES   = "override_attributes";
    private static final String                         conItemDataKeyMAX_OCCUR             = "max_occur";
    private static final String                         conItemDataKeyKEY                   = "key";
    private static final String                         conItemDataKeyCOPY_ELEMENT          = "copy_element";
    @SuppressWarnings("unused") private final String    conSVNVersion                       = "$Id$";
    @SuppressWarnings("unused") private final String    conClassName                        = this.getClass().getSimpleName();
    @SuppressWarnings("unused") private final Logger    logger                              = Logger.getLogger(this.getClass());
    private SchedulerDom                                objSchedulerDom                     = null;
    private SchedulerForm                               objSchedulerForm                    = null;
    //    public static String        JOBS                              = "Jobs";
    public static String                                JOBS                                = SOSJOEMessageCodes.JOE_L_SchedulerListener_Jobs.label();
    //    public static String        JOB_CHAINS                        = "Job Chains";
    public static String                                JOB_CHAINS                          = SOSJOEMessageCodes.JOE_L_SchedulerListener_JobChains.label();
    //    public static String        HTTP_SERVER                       = "HttpServer";
    public static String                                HTTP_SERVER                         = SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPServer.label();
    //    public static String        WEB_SERVICES                      = "WebServices";
    public static String                                WEB_SERVICES                        = SOSJOEMessageCodes.JOE_L_SchedulerListener_Webservices.label();
    //    public static String        SCHEDULES                         = "Schedules";
    public static String                                SCHEDULES                           = SOSJOEMessageCodes.JOE_L_SchedulerListener_Schedules.label();
    //    public static String        ORDERS                            = "Job Chain Orders";
    public static String                                ORDERS                              = SOSJOEMessageCodes.JOE_L_SchedulerListener_JobChainOrders.label();
    //    public static String        LOCKS                             = "Locks";
    public static String                                LOCKS                               = SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label();
    //    public static String        PROCESS_CLASSES                   = "treeitem.ProcessClasses";
    public static String                                PROCESS_CLASSES                     = SOSJOEMessageCodes.JOE_L_SchedulerListener_ProcessClasses.label();
    //    public static String        MONITOR                           = "Pre-/Post-Processing";
    public static String                                MONITOR                             = SOSJOEMessageCodes.JOE_M_SchedulerListener_PrePostProcessing.label();
    /** Aufruf erfolgt durch open Directory oder open Configurations*/
    private int                                         type                                = -1;
    public class MyElementComparator implements Comparator<Element> {
        @Override public int compare(final Element o1, final Element o2) {
            Element element1 = o1;
            Element element2 = o2;
            String e1 = getNameAndTitle(element1, "");
            String e2 = getNameAndTitle(element2, "");
            // JIRA  http://www.sos-berlin.com/jira/browse/JOE-25
            return e1.toLowerCase().compareTo(e2.toLowerCase());
        }
    }

    public SchedulerListener(final SchedulerForm gui, final SchedulerDom dom) {
        objSchedulerForm = gui;
        objSchedulerDom = dom;
    }

    public void treeFillMain(final Tree tree, final Composite c, final int type_) {
        final String conMethodName = conClassName + "::treeFillMain";
        logger.debug(SOSJOEMessageCodes.JOE_M_0047.params(conMethodName));
        type = type_;
        if (objSchedulerDom.isLifeElement()) {
            treeFillMainForLifeElement(tree, c);
        }
        else {
            treeFillMain(tree, c);
        }
    }

    public void treeFillMainForLifeElement(final Tree tree, final Composite c) {
        final String conMethodName = conClassName + "::treeFillMainForLifeElement";
        logger.debug(SOSJOEMessageCodes.JOE_M_0047.params(conMethodName));
        tree.removeAll();
        Element element = objSchedulerDom.getRoot();
        TreeItem item = new TreeItem(tree, SWT.NONE);
        if (type == SchedulerDom.LIVE_JOB) {
            String name = "";
            if (objSchedulerDom.getFilename() != null && new File(objSchedulerDom.getFilename()).exists()) {
                name = new File(objSchedulerDom.getFilename()).getName();
                name = name.substring(0, name.indexOf(".job.xml"));
                checkLifeAttributes(element, name);
                Utils.setAttribute("name", name, element);
            }
            else {
                name = Utils.getAttributeValue("name", element);
            }
            item.setText(getNameAndTitle(element, "job"));
            item.setData(new TreeData(JOEConstants.JOB, element, Options.getHelpURL("job")));
            item.setData(conItemDataKeyKEY, "job");
            item.setData(conItemDataKeyCOPY_ELEMENT, element);
            setColorOfJobTreeItem(element, item);
            treeFillJob(item, element, false);
            item.setExpanded(true);
        }
        else
            if (type == SchedulerDom.LIVE_JOB_CHAIN) {
                String name = "";
                if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
                    name = new java.io.File(objSchedulerDom.getFilename()).getName();
                    name = name.substring(0, name.indexOf(".job_chain.xml"));
                    checkLifeAttributes(element, name);
                    Utils.setAttribute("name", name, element);
                }
                else {
                    name = Utils.getAttributeValue("name", element);
                }
                item.setText(getNameAndTitle(element, "treeitem.jobchain"));
                item.setImage(getImage("jobchain.gif"));
                item.setData(new TreeData(JOEConstants.JOB_CHAIN, element, Options.getHelpURL("job_chain")));
                item.setData(conItemDataKeyKEY, "job_chain");
                item.setData(conItemDataKeyCOPY_ELEMENT, element);
                Utils.setAttribute(conItemDataKeyORDERS_RECOVERABLE, true, element);
                Utils.setAttribute(conItemDataKeyVISIBLE, true, element);
                if (!Utils.isElementEnabled("job_chain", objSchedulerDom, element)) {
                    setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
                else {
                    item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                }
                // Job Chain Nodes
                TreeItem in = new TreeItem(item, SWT.NONE);
                in.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StepsNodes.label());
                in.setImage(getImage("jobchain.gif"));
                in.setData(new TreeData(JOEConstants.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain")));
                in.setData(conItemDataKeyKEY, "job_chain_node");
                in.setData(conItemDataKeyCOPY_ELEMENT, element);
                // Job Chain Nested Nodes
                TreeItem iNestedNodes = new TreeItem(item, SWT.NONE);
                iNestedNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_NestedJobChains.label());
                iNestedNodes.setImage(getImage("jobchain.gif"));
                iNestedNodes.setData(new TreeData(JOEConstants.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("job_chain")));
                iNestedNodes.setData(conItemDataKeyKEY, "job_chain_node.job_chain");
                iNestedNodes.setData(conItemDataKeyCOPY_ELEMENT, element);
                iNestedNodes.setExpanded(true);
            }
            else
                if (type == SchedulerDom.LIFE_PROCESS_CLASS) {
                    String name = "";
                    if (objSchedulerDom.getFilename() != null && new File(objSchedulerDom.getFilename()).exists()) {
                        name = new File(objSchedulerDom.getFilename()).getName();
                        name = name.substring(0, name.indexOf(".process_class.xml"));
                        checkLifeAttributes(element, name);
                        Utils.setAttribute("name", name, element);
                    }
                    Element spooler = new Element("spooler");
                    Element config = new Element("config");
                    spooler.addContent(config);
                    Element process_classes = new Element("process_classes");
                    config.addContent(process_classes);
                    process_classes.addContent((Element) element.clone());
                    item.setData(new TreeData(JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
                    item.setData(conItemDataKeyKEY, "process_classes");
                    item.setData(conItemDataKeyCOPY_ELEMENT, element);
                    item.setData(conItemDataKeyMAX_OCCUR, "1");
                    //            item.setText(Messages.getLabel(PROCESS_CLASSES));
                    item.setText(PROCESS_CLASSES);
                }
                else
                    if (type == SchedulerDom.LIFE_LOCK) {
                        String name = "";
                        if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
                            name = new java.io.File(objSchedulerDom.getFilename()).getName();
                            name = name.substring(0, name.indexOf(".lock.xml"));
                            checkLifeAttributes(element, name);
                            Utils.setAttribute("name", name, element);
                        }
                        Element spooler = new Element("spooler");
                        Element config = new Element("config");
                        spooler.addContent(config);
                        Element locks = new Element("locks");
                        config.addContent(locks);
                        locks.addContent((Element) element.clone());
                        item.setData(new TreeData(JOEConstants.LOCKS, config, Options.getHelpURL("locks"), "locks"));
                        item.setData(conItemDataKeyKEY, "locks");
                        item.setData(conItemDataKeyCOPY_ELEMENT, element);
                        //            item.setText(Messages.getLabel(LOCKS));
                        item.setText(LOCKS);
                    }
                    else
                        if (type == SchedulerDom.LIFE_ORDER || type == SchedulerDom.LIFE_ADD_ORDER) {
                            String name = "";
                            if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
                                name = new java.io.File(objSchedulerDom.getFilename()).getName();
                                name = name.substring(0, name.indexOf(".order.xml"));
                                checkLifeAttributes(element, name);
                                Utils.setAttribute("job_chain", name.substring(0, name.indexOf(",")), element);
                                Utils.setAttribute("id", name.substring(name.indexOf(",") + 1), element);
                            }
                            String strT = getNameAndTitle(element, "order");
                            item.setText(strT);
                            item.setData(new TreeData(JOEConstants.ORDER, element, Options.getHelpURL("orders")));
                            item.setData(conItemDataKeyKEY, "commands_@_order");
                            item.setData(conItemDataKeyCOPY_ELEMENT, element);
                            if (!Utils.isElementEnabled("commands", objSchedulerDom, element)) {
                                setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                            }
                            else {
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                            }
                            treeFillOrder(item, element, false);
                        }
                        else {
                            if (type == SchedulerDom.LIFE_SCHEDULE) {
                                String name = "";
                                if (objSchedulerDom.getFilename() != null && new java.io.File(objSchedulerDom.getFilename()).exists()) {
                                    name = new java.io.File(objSchedulerDom.getFilename()).getName();
                                    name = name.substring(0, name.indexOf(".schedule.xml"));
                                    checkLifeAttributes(element, name);
                                    Utils.setAttribute("name", name, element);
                                }
                                else {
                                    name = Utils.getAttributeValue("name", element);
                                }
                                treeFillRunTimes(item, element, false, Utils.getAttributeValue("name", element));
                                List l = element.getChildren("month");
                                for (int i = 0; i < l.size(); i++) {
                                    Element e = (Element) l.get(i);
                                    treeFillRunTimes(item.getItem(item.getItemCount() - 1).getItem(item.getItem(item.getItemCount() - 1).getItemCount() - 1),
                                            e, false, Utils.getAttributeValue("month", e));
                                }
                                item.setExpanded(true);
                            }
                        }
        tree.setSelection(new TreeItem[] { tree.getItem(0) });
        treeSelection(tree, c);
    }

    public void setColorOfJobTreeItem(final Element element, final TreeItem item) {
        @SuppressWarnings("unused") final String conMethodName = conClassName + "::setColorOfJobTreeItem";
        //      logger.debug(String.format("Enter procedure %1$s ", conMethodName));
        if (!Utils.isElementEnabled("job", objSchedulerDom, element)) {
            setDisabled(item);
        }
        else {
            String order = element.getAttributeValue("order");
            logger.trace(element.getAttribute("name") + ", Order = " + order);
            if (order != null && order.equalsIgnoreCase("yes")) {
                setEnabled(item);
                item.setImage(getImage("17382.png"));
            }
            else {
                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
                item.setImage(getImage("standalone.png"));
            }
        }
    }
    @SuppressWarnings("unused") private static final String conImageEDITOR_SMALL_PNG    = "/sos/scheduler/editor/editor-small.png";
    private Tree                                            objTree                     = null;

    public void treeFillMain(final Tree tree, final Composite c) {
        Display d = Display.getCurrent();
        objTree = tree;
        final String conMethodName = conClassName + "::treeFillMain";
        logger.debug(SOSJOEMessageCodes.JOE_M_0047.params(conMethodName));
        tree.removeAll();
        if (objSchedulerDom.isLifeElement()) {
            Utils.setResetElement(objSchedulerDom.getRoot());
        }
        else {
            Utils.setResetElement(objSchedulerDom.getRoot().getChild("config"));
        }
        TreeItem objTreeObjects = new TreeItem(tree, SWT.NONE);
        objTreeObjects.setText("JobScheduler Objects");
        objTreeObjects.setData(conItemDataKeyKEY, "objects");
        objTreeObjects.setExpanded(true);
        TreeItem item = null; 
        Element config = objSchedulerDom.getRoot().getChild("config");
        if (type != SchedulerDom.DIRECTORY) {
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.CONFIG, config, Options.getHelpURL("config")));
            item.setData(conItemDataKeyKEY, "config");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.Config"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_BaseConfig.label());
            item.setImage(getImage("config.gif"));
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.BASEFILE, config, Options.getHelpURL("base")));
            item.setData(conItemDataKeyKEY, "base");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.BaseFiles"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_BaseFiles.label());
            item.setImage(getImage("import_wiz.gif"));
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.PARAMETER, config, Options.getHelpURL("parameter")));
            item.setData(conItemDataKeyKEY, "params_@_param");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.parameter"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
            item.setImage(getImage("parameter.gif"));
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.SECURITY, config, Options.getHelpURL("security"), "security"));
            item.setData(conItemDataKeyKEY, "security");
            item.setData(conItemDataKeyMAX_OCCUR, "1");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.security"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_AccessControl.label());
            item.setImage(getImage("10682.gif"));
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
            item.setData(conItemDataKeyKEY, "cluster");
            item.setData(conItemDataKeyMAX_OCCUR, "1");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.Cluster"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_ClusterConfig.label());
            item.setImage(getImage("synced.gif"));
        }
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.JOBS, config, Options.getHelpURL("jobs"), "jobs"));
        item.setData(conItemDataKeyKEY, "jobs_@_job");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        item.setText(JOBS);
        item.setImage(getImage("jobs.gif"));
        treeFillJobs(item);
        item.setExpanded(true);
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.JOB_CHAINS, config, Options.getHelpURL("job_chains"), "job_chains"));
        item.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        item.setText(JOB_CHAINS);
        item.setImage(getImage("hierarchical.gif"));
        treeFillJobChains(item);
        //if (hasOrders(objSchedulerDom) != null) {
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.ORDERS, config, Options.getHelpURL("orders"), "orders"));
        item.setData(conItemDataKeyKEY, "commands_@_order");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        //        item.setText(Messages.getLabel(ORDERS));
        item.setText(ORDERS);
        item.setImage(getImage("orders.gif"));
        treeFillOrders(item, true);
        //}
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
        item.setData(conItemDataKeyKEY, "process_classes");
        item.setData(conItemDataKeyMAX_OCCUR, "1");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        item.setImage(getImage("10360.gif"));
        //        item.setText(Messages.getLabel("treeitem.ProcessClasses"));
        item.setText(PROCESS_CLASSES);
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.SCHEDULES, config, Options.getHelpURL("schedules"), "schedules"));
        item.setData(conItemDataKeyKEY, "schedules_@_schedule");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        item.setText(SCHEDULES);
        item.setImage(getImage("dates.gif"));
        treeFillSchedules(item);
        item = new TreeItem(objTreeObjects, SWT.NONE);
        item.setData(new TreeData(JOEConstants.LOCKS, config, Options.getHelpURL("locks"), "locks"));
        item.setData(conItemDataKeyKEY, "locks");
        item.setData(conItemDataKeyCOPY_ELEMENT, config);
        //        item.setText(Messages.getLabel("Locks"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label());
        item.setImage(getImage("lockedstate.gif"));
        if (type != SchedulerDom.DIRECTORY) {
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
            item.setData(conItemDataKeyKEY, "script");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.StartScript"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StartScript.label());
            item.setImage(getImage("help.gif"));
            if (type == SchedulerDom.DIRECTORY)
                item.dispose();
            TreeItem http_server = new TreeItem(tree, SWT.NONE);
            // http_server.setData(new TreeData(JOEConstants.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
            http_server.setData(new TreeData(JOEConstants.HTTP_SERVER, config, Options.getHelpURL("http_server"), "http_server"));
            // http_server.setData("key", "http_server");
            ArrayList l = new ArrayList();
            l.add("http_server_@_web_service");
            l.add("http_server_@_http.authentication");
            l.add("http_server_@_http_directory");
            http_server.setData(conItemDataKeyKEY, l);
            http_server.setData(conItemDataKeyCOPY_ELEMENT, config);
            //            http_server.setText(Messages.getLabel(HTTP_SERVER));
            http_server.setText(HTTP_SERVER);
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
            //            item.setText(Messages.getLabel("treeitem.HttpAuthentication"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPAuthentication.label());
            item.setImage(getImage("12126.gif"));
            item = new TreeItem(http_server, SWT.NONE);
            item.setData(new TreeData(JOEConstants.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
            item.setData(conItemDataKeyKEY, "http_server_@_http_directory");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //            item.setText(Messages.getLabel("treeitem.HttpDirectories"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_HTTPDirectories.label());
            item.setImage(getImage("httpdirectory.gif"));
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
            item.setData(conItemDataKeyKEY, "holidays");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //            item.setText(Messages.getLabel("treeitem.Holidays"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Holidays.label());
            item.setImage(getImage("holidays.gif"));
            treeFillHolidays(item, config);
            item = new TreeItem(tree, SWT.NONE);
            item.setData(new TreeData(JOEConstants.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
            item.setData(conItemDataKeyKEY, "commands");
            item.setData(conItemDataKeyCOPY_ELEMENT, config);
            //        item.setText(Messages.getLabel("treeitem.Commands"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Commands.label());
            item.setImage(getImage("commands.gif"));
            if (type == SchedulerDom.DIRECTORY) {
                item.dispose();
            }
        }
        objTreeObjects.setExpanded(true);
        tree.setSelection(new TreeItem[] { tree.getItem(0) });
        treeSelection(tree, c);
    }

    private List<Element> hasOrders(final SchedulerDom pobjSchedulerDom) {
        Element commands = objSchedulerDom.getRoot().getChild("config").getChild("commands");
        if (commands != null) {
            List<Element> lstOfOrders = commands.getChildren("add_order");
            if (lstOfOrders != null) {
                return lstOfOrders;
            }
            List<Element> lOrder = commands.getChildren("order");
            if (lOrder != null) {
                return lOrder;
            }
        }
        return null;
    }

    public void treeFillOrders(final TreeItem parent, final boolean expand) {
        TreeItem orders = parent;
        if (!parent.getText().equals("Orders")) {
            Tree t = parent.getParent();
            for (int i = 0; i < t.getItemCount(); i++)
                if (t.getItem(i).getText().equals("Orders")) {
                    orders = t.getItem(i);
                }
        }
        if (orders != null) {
            orders.removeAll();
            Element commands = objSchedulerDom.getRoot().getChild("config").getChild("commands");
            if (commands != null) {
                List lstOfOrders = commands.getChildren("add_order");
                if (lstOfOrders != null) {
                    Iterator it = getSortedIterator(lstOfOrders, new MyElementComparator());
                    while (it.hasNext()) {
                        Element objOrderElement = (Element) it.next();
                        if (objOrderElement.getName().equals("add_order") && objOrderElement.getAttributeValue("id") != null) {
                            TreeItem item = new TreeItem(orders, SWT.NONE);
                            String strT = getNameAndTitle(objOrderElement, "treeitem.Order");
                            item.setText(strT);
                            item.setImage(getImage("order.gif"));
                            item.setData(new TreeData(JOEConstants.ORDER, objOrderElement, Options.getHelpURL("orders")));
                            item.setData(conItemDataKeyKEY, "commands_@_order");
                            item.setData(conItemDataKeyCOPY_ELEMENT, commands);
                            if (!Utils.isElementEnabled("commands", objSchedulerDom, objOrderElement)) {
                                setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                            }
                            else {
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                            }
                            treeFillOrder(item, objOrderElement, false);
                        }
                    }
                }
                List lOrder = commands.getChildren("order");
                if (lOrder != null) {
                    Iterator it = getSortedIterator(lOrder, new MyElementComparator());
                    while (it.hasNext()) {
                        Element e = (Element) it.next();
                        if (e.getName().equals("order") && e.getAttributeValue("id") != null) {
                            TreeItem item = new TreeItem(orders, SWT.NONE);
                            String strT = getNameAndTitle(e, "treeitem.Order");
                            item.setImage(getImage("order.gif"));
                            item.setText(strT);
                            item.setData(new TreeData(JOEConstants.ORDER, e, Options.getHelpURL("orders")));
                            item.setData(conItemDataKeyKEY, "commands_@_order");
                            item.setData(conItemDataKeyCOPY_ELEMENT, e);
                            if (!Utils.isElementEnabled("commands", objSchedulerDom, e)) {
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                            }
                            else {
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                            }
                            treeFillOrder(item, e, false);
                        }
                    }
                }
            }
        }
        orders.setExpanded(expand);
    }

    private Iterator<Element> getSortedIterator(final List<Object> l, final MyElementComparator myElementComparator) {
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

    public void treeFillJobs(final TreeItem parent) {
        @SuppressWarnings("unused") final String conMethodName = conClassName + "::treeFillJobs";
        parent.removeAll();
        Element jobs = getJobs(objSchedulerDom);
        if (jobs == null) {
            TreeData data = (TreeData) parent.getData();
            jobs = data.getElement().getChild("jobs");
        }
        if (jobs != null) {
            Iterator<Element> it = getSortedIterator(jobs.getChildren(), new MyElementComparator());
            while (it.hasNext()) {
                Element element = it.next();
                createJobItem(parent, element, "");
            }
        }
        parent.setExpanded(true);
    }

    private void createJobItem(final TreeItem parent, final Element element, final String strPrefix) {
        if (type == SchedulerDom.DIRECTORY) {
            checkLifeAttributes(element, Utils.getAttributeValue("name", element));
        }
        TreeItem objTreeItem = new TreeItem(parent, SWT.NONE);
        String job = getNameAndTitle(element, "Job");
        job = strPrefix + job;
        objTreeItem.setText(job);
        objTreeItem.setData(new TreeData(JOEConstants.JOB, element, Options.getHelpURL("job")));
        objTreeItem.setData(conItemDataKeyCOPY_ELEMENT, element);
        objTreeItem.setData(conItemDataKeyKEY, "jobs_@_job");
        setColorOfJobTreeItem(element, objTreeItem);
        treeFillJob(objTreeItem, element, false);
    }

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
            // if (parent.getItem(i).getText().equals("Job: "+job)) {
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

    public void treeFillJob(final TreeItem parent, final Element job, final boolean expand) {
        boolean flgIsReadOnlyFile = !Utils.isElementEnabled("job", objSchedulerDom, job);
        parent.removeAll();
        ArrayList<String> l = new ArrayList<String>();
        Color isColor4ReadOnlyFiles = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
        // Options
        TreeItem item = new TreeItem(parent, SWT.NONE);
        //        item.setText(Messages.getLabel("treeitem.options"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Options.label());
        item.setImage(getImage("options.gif"));
        item.setData(conItemDataKeyMAX_OCCUR, "1");
        item.setData(new TreeData(JOEConstants.JOB_OPTION, job, Options.getHelpURL("job")));
        item.setData(conItemDataKeyKEY, "job");
        item.setData(conItemDataKeyOVERRIDE_ATTRIBUTES, "true");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        if (flgIsReadOnlyFile) {
            item.setForeground(isColor4ReadOnlyFiles);
        }
        // Parameter
        item = new TreeItem(parent, SWT.NONE);
        item.setData(new TreeData(JOEConstants.PARAMETER, job, Options.getHelpURL("parameter")));
        item.setData(conItemDataKeyKEY, "params_@_param");
        ArrayList ll = new ArrayList();
        ll.add("params_@_param");
        ll.add("params_@_include");
        // l.add("environment");
        item.setData(conItemDataKeyKEY, ll);
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        //        item.setText(Messages.getLabel("treeitem.parameter"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
        item.setImage(getImage("parameter.gif"));
        // Mail
        item = new TreeItem(parent, SWT.NONE);
        item.setData(new TreeData(JOEConstants.SETTINGS, job, Options.getHelpURL("settings")));
        item.setData(conItemDataKeyKEY, "settings");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        //        item.setText(Messages.getLabel("treeitem.jobsettings"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_JobSettings.label());
        item.setData(conItemDataKeyMAX_OCCUR, "1");
        item.setImage(getImage("10036.gif"));
        // Monitor
        item = new TreeItem(parent, SWT.NONE);
        item.setText(MONITOR);
        item.setImage(getImage("source_attach_attrib.gif"));
        item.setData(new TreeData(JOEConstants.MONITORS, job, Options.getHelpURL("job.monitor"), "monitor"));
        item.setData(conItemDataKeyKEY, "monitor");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        if (flgIsReadOnlyFile) {
            setDisabled(item);
        }
        treeFillMonitorScripts(item, job, flgIsReadOnlyFile);
        // RunOptions
        item = new TreeItem(parent, SWT.NONE);
        //        item.setText(Messages.getLabel("treeitem.runoptions"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_RunOptions.label());
        item.setData(new TreeData(JOEConstants.OPTIONS, job, Options.getHelpURL("job.options")));
        l = new ArrayList<String>();
        l.add("start_when_directory_changed");
        l.add("delay_after_error");
        l.add("delay_order_after_setback");
        // item.setData("key", "job.options");
        item.setData(conItemDataKeyKEY, l);
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        item.setImage(getImage("properties.gif"));
        if (flgIsReadOnlyFile) {
            setDisabled(item);
        }
        item = new TreeItem(parent, SWT.NONE);
        //        item.setText(Messages.getLabel("locks"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Locks.label());
        item.setData(new TreeData(JOEConstants.LOCKUSE, job, Options.getHelpURL("job.locks")));
        item.setData(conItemDataKeyKEY, "lock.use");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        item.setImage(getImage("lockedstate.gif"));
        if (flgIsReadOnlyFile) {
            setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        }
        treeFillRunTimes(parent, job, flgIsReadOnlyFile, "run_time");
        List commands = job.getChildren("commands");
        item = new TreeItem(parent, SWT.NONE);
        //        item.setText(Messages.getLabel("treeitem.commands"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Commands.label());
        item.setImage(getImage("commands.gif"));
        if (flgIsReadOnlyFile) {
            item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        }
        if (commands != null) {
            treeFillCommands(item, job, false);
        }
        item.setData(new TreeData(JOEConstants.JOB_COMMANDS, job, Options.getHelpURL("job.commands")));
        // item.setData("key", "job_@_commands");
        item.setData(conItemDataKeyKEY, "commands");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        parent.setExpanded(expand);
        // Documentation
        item = new TreeItem(parent, SWT.NONE);
        //        item.setText(Messages.getLabel("treeitem.documentation"));
        item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Documentation.label());
        item.setImage(getImage("11020.gif"));
        item.setData(conItemDataKeyMAX_OCCUR, "1");
        item.setData(new TreeData(JOEConstants.JOB_DOCUMENTATION, job, Options.getHelpURL("job")));
        // ArrayList l = new ArrayList();
        // l.add("process");
        // l.add("script");
        item.setData(conItemDataKeyKEY, "job_@_description");
        item.setData(conItemDataKeyCOPY_ELEMENT, job);
        if (flgIsReadOnlyFile) {
            item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        }
    }

    //  private static final HashMap<String, Image> hshImages   = new HashMap<String, Image>();
    private Image getImage(final String pstrImageFileName) {
        Image objI = ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/" + pstrImageFileName);
        //      try {
        //          objI = hshImages.get(pstrImageFileName);
        //          if (objI == null) {
        //              objI = new Im age(Display.getCurrent(), getClass().getResourceAsStream("/sos/scheduler/editor/icons/" + pstrImageFileName));
        //              hshImages.put(pstrImageFileName, objI);
        //          }
        //      }
        //      catch (Exception e) {
        //          objI = hshImages.get("help.gif");
        //          if (objI == null) {
        //              objI = new Ima ge(Display.getCurrent(), getClass().getResourceAsStream("/sos/scheduler/editor/icons/" + "help.gif"));
        //              hshImages.put("help.gif", objI);
        //          }
        //      }
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

    public void treeFillExitCodesCommands(final TreeItem parent, final Element elem, final boolean expand) {
        parent.removeAll();
        treeFillExitCodesCommands(parent, elem.getChildren("order"));
        treeFillExitCodesCommands(parent, elem.getChildren("add_order"));
        treeFillExitCodesCommands(parent, elem.getChildren("start_job"));
    }

    private void treeFillExitCodesCommands(final TreeItem parent, final List cmdList) {
        for (int i = 0; i < cmdList.size(); i++) {
            Element cmdElem = (Element) cmdList.get(i);
            TreeItem item = new TreeItem(parent, SWT.NONE);
            String name = Utils.getAttributeValue("job_chain", cmdElem) != null && Utils.getAttributeValue("job_chain", cmdElem).length() > 0 ? Utils.getAttributeValue(
                    "job_chain", cmdElem) : Utils.getAttributeValue("job", cmdElem);
            item.setText(cmdElem.getName() + ": " + name);
            item.setImage(getImage("commands.gif"));
            item.setData(new TreeData(JOEConstants.JOB_COMMAND, cmdElem, Options.getHelpURL("job.commands")));
            item.setExpanded(false);
            // PARAMETER
            item = new TreeItem(item, SWT.NONE);
            item.setData(new TreeData(JOEConstants.PARAMETER, cmdElem, Options.getHelpURL("parameter")));
            item.setData(conItemDataKeyKEY, "params_@_param");
            item.setData(conItemDataKeyCOPY_ELEMENT, cmdElem);
            //            item.setText(Messages.getLabel("treeitem.parameter"));
            item.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_Parameter.label());
            item.setImage(getImage("parameter.gif"));
        }
    }

    public void treeFillCommands(final TreeItem parent, final Element job, final boolean expand) {
        // new JobCommandListener(objSchedulerDom, null, null).fillCommands(job, parent, expand);
        // fillCommands(job, parent, expand);
        List commands = job.getChildren("commands");
        java.util.ArrayList listOfReadOnly = objSchedulerDom.getListOfReadOnlyFiles();
        if (commands != null) {
            Iterator it = commands.iterator();
            parent.removeAll();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("on_exit_code") != null) {
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    //                    item.setText(e.getAttributeValue("on_exit_code"));
                    item.setText(e.getAttributeValue("on_exit_code"));
                    item.setData(new TreeData(JOEConstants.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
                    item.setData(conItemDataKeyKEY, "commands_@_order");
                    item.setData(conItemDataKeyCOPY_ELEMENT, e);
                    if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
                        setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                    }
                    else {
                        item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                    }
                    treeFillExitCodesCommands(item, e, false);
                }
            }
        }
        parent.setExpanded(expand);
    }

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

    public void treeFillSpecificWeekdays(final TreeItem parent, final Element element, final boolean expand) {
        if (element != null) {
            new SpecificWeekdaysListener(objSchedulerDom, element).fillTreeDays(parent, expand);
        }
    }

    private Element getJobChains(final SchedulerDom pobjSchedulerDom) {
        Element jobChains = objSchedulerDom.getRoot().getChild("config").getChild("job_chains");
        return jobChains;
    }

    public void treeFillJobChains(final TreeItem parent) {
        parent.removeAll();
        Element jobChains = getJobChains(objSchedulerDom);
        if (jobChains != null) {
            Iterator it = getSortedIterator(jobChains.getChildren(), new MyElementComparator());
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    boolean flgIsSimpleJobChain = false;
                    boolean flgHasNestedJobChains = false;
                    Element element = (Element) o;
                    TreeItem objJobChainRootItem = new TreeItem(parent, SWT.NONE);
                    objJobChainRootItem.setText(getNameAndTitle(element, "treeitem.jobchain"));
                    objJobChainRootItem.setImage(getImage("jobchain.gif"));
                    objJobChainRootItem.setData(new TreeData(JOEConstants.JOB_CHAIN, element, Options.getHelpURL("job_chain")));
                    objJobChainRootItem.setData(conItemDataKeyKEY, "job_chains_@_job_chain");
                    objJobChainRootItem.setData(conItemDataKeyCOPY_ELEMENT, element);
                    // Job Chain Nodes
                    TreeItem iNodes = new TreeItem(objJobChainRootItem, SWT.NONE);
                    iNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_StepsNodes.label());
                    iNodes.setImage(getImage("jobchain.gif"));
                    iNodes.setData(new TreeData(JOEConstants.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain")));
                    iNodes.setData(conItemDataKeyKEY, "job_chain_node");
                    iNodes.setData(conItemDataKeyCOPY_ELEMENT, element);
                    Iterator objNodesIt = element.getChildren().iterator();
                    while (objNodesIt.hasNext()) {
                        Element node = (Element) objNodesIt.next();
                        if (node.getName().equals("job_chain_node")) {
                            flgIsSimpleJobChain = true;
                            String strJobName = Utils.getAttributeValue("job", node);
                            String strState = Utils.getAttributeValue("state", node);
                            Tree tree = objTree;
                            TreeItem objRootItem = tree.getItem(0);
                            for (int k = 0; k < objRootItem.getItemCount(); k++) {
                                TreeItem item = objRootItem.getItem(k);
                                TreeData objTreeData = (TreeData) item.getData();
                                if (objTreeData != null && objTreeData.getType() == JOEConstants.JOBS) { // Jobs node
                                    /*
                                     * Loop over all Jobs in the Tree. Attention: not all Jobs are in the tree due to subfolders
                                     */
                                    // TODO avoid loop over all Items in Treeview. Try direct search in list of all jobs
                                    for (TreeItem jItem : item.getItems()) {
                                        objTreeData = (TreeData) jItem.getData();
                                        Element objJob2Insert = objTreeData.getElement();
                                        String strName = objJob2Insert.getAttributeValue("name");
                                        // TODO get the name of the job from the Element, not from the description
                                        if (strJobName.equals(strName)) {
                                            createJobItem(iNodes, objJob2Insert, strState + " - ");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    iNodes.setExpanded(true);
                    if (flgIsSimpleJobChain == true) {  // no need to show the "nested job Chain" treeitem
                    }
                    else {
                        // Job Chain Nested Nodes
                        TreeItem iNestedNodes = new TreeItem(objJobChainRootItem, SWT.NONE);
                        iNestedNodes.setText(SOSJOEMessageCodes.JOE_L_SchedulerListener_NestedJobChains.label());
                        iNestedNodes.setImage(getImage("category.gif"));
                        iNestedNodes.setData(new TreeData(JOEConstants.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("nested_job_chain")));
                        // iNestedNodes.setData("key", "job_chain_node.job_chain");
                        iNestedNodes.setData(conItemDataKeyKEY, "job_chain_node.job_chain");
                        iNestedNodes.setData(conItemDataKeyCOPY_ELEMENT, element);
                        
                         objNodesIt = element.getChildren().iterator();
                        while (objNodesIt.hasNext()) {
                            Element node = (Element) objNodesIt.next();
                            if (node.getName().equals("job_chain_node.job_chain")) {
                                flgHasNestedJobChains = true;
                                objJobChainRootItem.setImage(getImage("category.gif"));
                            }
                        }
                        
                        iNestedNodes.setExpanded(true);
                        if (!Utils.isElementEnabled("job_chain", objSchedulerDom, element)) {
                            setDisabled(objJobChainRootItem); // i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                            setDisabled(iNodes); // iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                            setDisabled(iNestedNodes); // iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                        }
                        else {
                            objJobChainRootItem.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                            iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                            iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                        }
                    }
                    if (flgHasNestedJobChains) {
                        iNodes.removeAll();
                        iNodes.dispose();
                    }
                    
                    TreeItem objOrders4JobChain = new TreeItem(objJobChainRootItem, SWT.NONE);
                    objOrders4JobChain.setText("Orders");
                    objOrders4JobChain.setImage(getImage("orders.gif"));
                    objOrders4JobChain.setData(new TreeData(JOEConstants.ORDERS, element, Options.getHelpURL("orders")));
                    // objOrders4JobChain.setData("key", "job_chain_node.job_chain");
                    objOrders4JobChain.setData(conItemDataKeyKEY, "orders.job_chain");
                    objOrders4JobChain.setData(conItemDataKeyCOPY_ELEMENT, element);
                    objOrders4JobChain.setExpanded(true);
                }
            }
        }
        parent.setExpanded(true);
    }

    private String getNameAndTitle(final Element element, final String pstrI18NKey) {
        String name = Utils.getAttributeValue("name", element);
        if (name == null || name.length() <= 0) {
            name = element.getAttributeValue("id");
        }
        if (name == null) {
            name = "???";
        }
        String strTitle = Utils.getAttributeValue("title", element);
        String jobChainName = name;
        jobChainName += !objSchedulerDom.isEnabled(element) ? SOSJOEMessageCodes.JOE_M_JobCommand_Disabled.label() : "";
        if (strTitle != null && strTitle.trim().length() > 0) {
            jobChainName += " - " + strTitle;
        }
        return jobChainName;
    }

    
    
    
    
    
    
       private Element addChildIfNotExist(Element parent,String child, String attr, String attrValue) {
            Element ee = parent.getChild(child);
            if (ee == null) {
                 ee = new Element(child);
                parent.addContent(ee);
            }
           if (attr.length() > 0) {
               ee.setAttribute(attr, attrValue);
           }
           return ee;
        }
        
        private void addAttr(Element source, Element target, String attr) {
            if (source.getAttributeValue(attr) != null) {
                target.setAttribute(attr, source.getAttributeValue(attr));
            }
        }

        
 
    
    
    /* Handle all periods directly under <run_time> as <weekdays><day day="1 2 3 4 5 6 7"/> </weekdays>
     * But only if no other tag is specified. This is because JobScheduler ignores periods if there are other tags. 
     * This behaviour should not be changed
     * JOE-102
     */
        
    private Element convertPeriodToEveryDay(Element objElement) {
        Element _runtime = objElement.getChild("run_time");
        if (_runtime != null) {
            List <Element>lRuntimeChilds = _runtime.getChildren();
            List <Element>lPeriods = _runtime.getChildren("period");

            if (lRuntimeChilds.size() > 0 && lRuntimeChilds.size() == lPeriods.size()) { // Only <period> tags found
                Element weekdays = addChildIfNotExist(_runtime, "weekdays", "", "");
                Element days = addChildIfNotExist(weekdays, "day", "day", "1 2 3 4 5 6 7");
                for (Element iPeriod : lPeriods) {  //clone the period into the days="1 2 3 4 5 6 7" 
                    Element period = new Element("period");
                    addAttr(iPeriod, period, "absolute_repeat");
                    addAttr(iPeriod, period, "single_start");
                    addAttr(iPeriod, period, "let_run");
                    addAttr(iPeriod, period, "begin");
                    addAttr(iPeriod, period, "end");
                    addAttr(iPeriod, period, "repeat");
    
                    days.addContent(period);

                } 
                
                //remove all period tags 
                int size = lPeriods.size();
                for (int i=0;i < size;i++) {
                    _runtime.removeChild("period");
                }

                objSchedulerForm.updateTree("main");
                objSchedulerDom.setChanged(true);
                objSchedulerDom.setChangedForDirectory(objElement, SchedulerDom.MODIFY);
            }

        }
        return objElement;
    }
    
    private boolean periodsAndGroup(Element objElement) {
        Element _runtime = objElement.getChild("run_time");
        if (_runtime != null) {
            List <Element>lRuntimeChilds = _runtime.getChildren();
            List <Element>lPeriods = _runtime.getChildren("period");
            return (lRuntimeChilds.size() != lPeriods.size() && lPeriods.size() > 0); 
        }
        return false;

    }

    public boolean treeSelection(final Tree tree, final Composite c) {
        try {
            if (tree.getSelectionCount() > 0) {
                // dispose the old form
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i]))
                        return false;
                    children[i].dispose();
                }
                TreeItem objSelectedTreeItem = tree.getSelection()[0];
                TreeData objTreeItemUserdata = (TreeData) objSelectedTreeItem.getData();
                if (objTreeItemUserdata != null) {
                    objSchedulerDom.setInit(true);
                    if (c.getChildren().length > 0) {
                    }
                    Element objElement = objTreeItemUserdata.getElement();
                    objElement = convertPeriodToEveryDay(objElement);
                    int intType = objTreeItemUserdata.getType();
                    c.setLayout(new FillLayout());
                    switch (intType) {
                        case JOEConstants.CONFIG:
                            new ConfigForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm);
                            break;
                        case JOEConstants.PARAMETER:
                            int type = getType(objElement);
                            Attribute a = Utils.getJobElement(objElement).getAttribute("name");
                            if (a == null) {
                                new sos.scheduler.editor.conf.forms.ParameterForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, type);
                            }
                            else {
                                String jobname = a.getValue();
                                new sos.scheduler.editor.conf.forms.ParameterForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, type, jobname);
                            }
                            break;
                        case JOEConstants.SECURITY:
                            new SecurityForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.CLUSTER:
                            new ClusterForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.BASEFILE:
                            new SchedulerBaseFileForm(c, SWT.NONE, objSchedulerDom);
                            break;
                        case JOEConstants.PROCESS_CLASSES:
                            new ProcessClassesForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.LOCKS:
                            new LocksForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.MONITORS:
                            new ScriptsForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm, objElement);
                            break;
                        case JOEConstants.MONITOR:
                            new ScriptFormPreProcessing(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.SCRIPT:
                            new ScriptFormSchedulerStartScript(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB:
                            new ScriptJobMainForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_OPTION:
                            new JobMainOptionForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_DOCUMENTATION:
                            new JobDocumentationForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.SETTINGS:
                            new MailForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.ORDERS:
                            new OrdersForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm);
                            break;
                        case JOEConstants.ORDER:
                            new OrderForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_COMMAND_EXIT_CODES:
                            new sos.scheduler.editor.conf.forms.JobCommandExitCodesForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_COMMAND:
                            new JobCommandForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_COMMANDS:
                            new JobCommandsForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, this);
                            break;
                        case JOEConstants.RUNTIME:
                            new RunTimeForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.WEEKDAYS:
                            new DaysForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.WEEKDAYS,
                                    objSelectedTreeItem.getData(conItemDataKeyKEY) != null
                                            && objSelectedTreeItem.getData(conItemDataKeyKEY).equals("holidays_@_weekdays"));
                            break;
                        case JOEConstants.MONTHDAYS:
                            new DaysForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.MONTHDAYS, false);
                            break;
                        case JOEConstants.SPECIFIC_MONTHS:
                            new DaysForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.SPECIFIC_MONTHS, false);
                            break;
                        case JOEConstants.ULTIMOS:
                            new DaysForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.ULTIMOS, false);
                            break;
                        case JOEConstants.EVERYDAY:
                        case JOEConstants.PERIODS:
                            new PeriodsForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOBS:
                            new JobsForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm);
                            break;
                        case JOEConstants.HOLIDAYS:
                            new DateForm(c, SWT.NONE, DateListener.HOLIDAY, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.DAYS:
                            new DateForm(c, SWT.NONE, DateListener.DATE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.SPECIFIC_WEEKDAYS:
                            new SpecificWeekdaysForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm, DaysListener.MONTHDAYS);
                            break;
                        case JOEConstants.WEBSERVICES:
                            new WebservicesForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.WEBSERVICE:
                            new WebserviceForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.HTTPDIRECTORIES:
                            new HttpDirectoriesForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.HTTP_AUTHENTICATION:
                            new HttpAuthenticationForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.OPTIONS:
                            new JobOptionsForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.LOCKUSE:
                            new JobLockUseForm(c, SWT.NONE, objSchedulerDom, objElement);
                            break;
                        case JOEConstants.JOB_CHAINS:
                            new JobChainsForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.JOB_CHAIN:
                            JobChainForm jc_ = new JobChainForm(c, SWT.NONE, objSchedulerDom, objElement);
                            jc_.setISchedulerUpdate(objSchedulerForm);
                            break;
                        case JOEConstants.JOB_CHAIN_NODES:
                            JobChainNodesForm jcn_ = new JobChainNodesForm(c, SWT.NONE, objSchedulerDom, objElement);
                            jcn_.setISchedulerUpdate(objSchedulerForm);
                            break;
                        case JOEConstants.JOB_CHAIN_NESTED_NODES:
                            JobChainNestedNodesForm j = new JobChainNestedNodesForm(c, SWT.NONE, objSchedulerDom, objElement);
                            j.setISchedulerUpdate(objSchedulerForm);
                            break;
                        case JOEConstants.COMMANDS:
                            new CommandsForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm);
                            break;
                        case JOEConstants.SCHEDULES:
                            new sos.scheduler.editor.conf.forms.SchedulesForm(c, SWT.NONE, objSchedulerDom, objSchedulerForm);
                            break;
                        case JOEConstants.SCHEDULE:
                            new sos.scheduler.editor.conf.forms.ScheduleForm(c, SWT.NONE, objSchedulerDom, objElement, objSchedulerForm);
                            break;
                        case JOEConstants.HTTP_SERVER:
                            break;
                        default:
                            //                        System.out.println("no form found for " + objSelectedTreeItem.getText());
                            System.out.println(SOSJOEMessageCodes.JOE_M_0049.params(objSelectedTreeItem.getText()));
                    }
                }
                // c.setLayout(new FillLayout());
                c.layout();
            }
        }
        catch (Exception e) {
            try {
                new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {
            }
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
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
                //              if (item.getItem(i).equals("Run Time")) {
                if (item.getItem(i).equals(SOSJOEMessageCodes.JOE_M_SchedulerListener_RunTime.label())) {
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
            if (periodsAndGroup(job)) {
                item = new TreeItem(runTreeItem, SWT.NONE);
                item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_EveryDay.label());
                item.setData(new TreeData(JOEConstants.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));
                item.setData(conItemDataKeyKEY, "period");
                
                item.setData(conItemDataKeyCOPY_ELEMENT, job.getChild("run_time"));
                if (disable) {
                    setDisabled(item); // item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
                objSchedulerForm.updateFont(item);
            }
     
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
            Iterator it = getSortedIterator(httpServer.getChildren("web_service"), new MyElementComparator());
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
            Iterator it = getSortedIterator(schedules.getChildren(), new MyElementComparator());
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element element = (Element) o;
                    if (type == SchedulerDom.DIRECTORY) {
                        checkLifeAttributes(element, Utils.getAttributeValue("name", element));
                    }
                    treeFillRunTimes(parent, element, false, Utils.getAttributeValue("name", element));
                }
            }
        }
        parent.setExpanded(true);
    }

    public void treeFillMonitorScripts(final TreeItem parent, final Element elem, final boolean disable) {
        parent.removeAll();
        List l = elem.getChildren("monitor");
        for (int i = 0; i < l.size(); i++) {
            Element monitor = (Element) l.get(i);
            TreeItem item = new TreeItem(parent, SWT.NONE);
            if (Utils.getAttributeValue("name", monitor).equals(""))
                //                item.setText(Messages.getLabel("treeitem.empty"));
                item.setText(SOSJOEMessageCodes.JOE_M_SchedulerListener_Empty.label());
            else
                item.setText(Utils.getAttributeValue("name", monitor));
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
}
