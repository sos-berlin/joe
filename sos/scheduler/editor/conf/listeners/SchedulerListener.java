package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.BaseForm;
import sos.scheduler.editor.conf.forms.CommandsForm;
import sos.scheduler.editor.conf.forms.ConfigForm;
import sos.scheduler.editor.conf.forms.DateForm;
import sos.scheduler.editor.conf.forms.DaysForm;
import sos.scheduler.editor.conf.forms.ExecuteForm;
import sos.scheduler.editor.conf.forms.JobChainsForm;
import sos.scheduler.editor.conf.forms.JobCommandForm;
import sos.scheduler.editor.conf.forms.JobCommandsForm;
import sos.scheduler.editor.conf.forms.JobForm;
import sos.scheduler.editor.conf.forms.JobOptionsForm;
import sos.scheduler.editor.conf.forms.JobsForm;
import sos.scheduler.editor.conf.forms.OrderForm;
import sos.scheduler.editor.conf.forms.OrdersForm;
import sos.scheduler.editor.conf.forms.PeriodsForm;
import sos.scheduler.editor.conf.forms.ProcessClassesForm;
import sos.scheduler.editor.conf.forms.RunTimeForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.forms.ScriptForm;
import sos.scheduler.editor.conf.forms.SecurityForm;
import sos.scheduler.editor.conf.forms.WebservicesForm;

public class SchedulerListener {

    private SchedulerDom  _dom;

    private SchedulerForm _gui;


    public SchedulerListener(SchedulerForm gui, SchedulerDom dom) {
        _gui = gui;
        _dom = dom;
    }


    public void treeFillMain(Tree tree, Composite c) {
        tree.removeAll();

        Element config = _dom.getRoot().getChild("config");

        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.CONFIG, config, Options.getHelpURL("config")));
        item.setText("Config");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.BASE, config, Options.getHelpURL("base")));
        item.setText("Base Files");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.SECURITY, config, Options.getHelpURL("security"), "security"));
        item.setText("Security");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"),
                "process_classes"));
        item.setText("Process Classes");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
        item.setText("Start Script");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.WEBSERVICES, config, Options.getHelpURL("web_services"), "web_services"));
        item.setText("Web Services");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
        item.setText("Holidays");
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.JOBS, config, Options.getHelpURL("jobs"), "jobs"));
        item.setText("Jobs");
        treeFillJobs(item);
        item.setExpanded(true);
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.JOB_CHAINS, config, Options.getHelpURL("job_chains"), "job_chains"));
        item.setText("Job Chains");

        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.ORDERS, config, Options.getHelpURL("orders"), "orders"));
        item.setText("Orders");
        treeFillOrders(item, true);
        item = new TreeItem(tree, SWT.NONE);
        item.setData(new TreeData(Editor.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
        item.setText("Commands");

        tree.setSelection(new TreeItem[] { tree.getItem(0) });
        treeSelection(tree, c);
    }


    public void treeFillOrders(TreeItem parent, boolean expand) {
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
            Element commands = _dom.getRoot().getChild("config").getChild("commands");
            if (commands != null) {
                List l = commands.getChildren("add_order");
                if (l != null) {
                    Iterator it = l.iterator();
                    while (it.hasNext()) {
                        Element e = (Element) it.next();
                        if (e.getName().equals("add_order") && e.getAttributeValue("id") != null) {
                            TreeItem item = new TreeItem(orders, SWT.NONE);
                            item.setText("Order:" + e.getAttributeValue("id"));
                            item.setData(new TreeData(Editor.ORDER, e, Options.getHelpURL("orders")));
                            treeFillOrder(item, e, false);
                        }
                    }
                }
            }
        }
        orders.setExpanded(expand);
    }


    public void treeFillJobs(TreeItem parent) {
        Element jobs = _dom.getRoot().getChild("config").getChild("jobs");
        if (jobs != null) {
            Iterator it = jobs.getChildren().iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element element = (Element) o;
                    TreeItem i = new TreeItem(parent, SWT.NONE);
                    String name = Utils.getAttributeValue("name", element);
                    String job = "Job: " + name;
                    job += _dom.isJobDisabled(name) ? " (Disabled)" : "";

                    i.setText(job);
                    i.setData(new TreeData(Editor.JOB, element, Options.getHelpURL("job")));
                    treeFillJob(i, element, false);
                }
            }
        }
        parent.setExpanded(true);
    }


    public void treeFillJob(TreeItem parent, Element job, boolean expand) {
        parent.removeAll();
        TreeItem item = new TreeItem(parent, SWT.NONE);
        item.setText("Execute");
        item.setData(new TreeData(Editor.EXECUTE, job, Options.getHelpURL("job.execute")));

        item = new TreeItem(parent, SWT.NONE);
        item.setText("Monitor");
        item.setData(new TreeData(Editor.MONITOR, job, Options.getHelpURL("job.monitor"), "monitor"));

        item = new TreeItem(parent, SWT.NONE);
        item.setText("Run Options");
        item.setData(new TreeData(Editor.OPTIONS, job, Options.getHelpURL("job.options")));

        Element runtime = job.getChild("run_time");
        List commands = job.getChildren("commands");
        // create runtime tag
        if (runtime == null) {
            runtime = new Element("run_time");
            job.addContent(runtime);
        }
        if (runtime != null) {
            TreeItem run = new TreeItem(parent, SWT.NONE);
            run.setText("Run Time");
            run.setData(new TreeData(Editor.RUNTIME, job, Options.getHelpURL("job.run_time"), "run_time"));

            item = new TreeItem(run, SWT.NONE);
            item.setText("Everyday");
            item.setData(new TreeData(Editor.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));

            item = new TreeItem(run, SWT.NONE);
            item.setText("Weekdays");
            item
                    .setData(new TreeData(Editor.WEEKDAYS, runtime, Options.getHelpURL("job.run_time.weekdays"),
                            "weekdays"));
            treeFillDays(item, runtime, 0, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Monthdays");
            item.setData(new TreeData(Editor.MONTHDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"),
                    "monthdays"));
            treeFillDays(item, runtime, 1, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Ultimos");
            item.setData(new TreeData(Editor.ULTIMOS, runtime, Options.getHelpURL("job.run_time.ultimos"), "ultimos"));
            treeFillDays(item, runtime, 2, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Specific Days");
            item.setData(new TreeData(Editor.DAYS, runtime, Options.getHelpURL("job.run_time.specific_days")));
            if (runtime != null)
                treeFillDays(item, runtime, 3, false);
        }

        item = new TreeItem(parent, SWT.NONE);
        item.setText("Commands");

        if (commands != null)
            treeFillCommands(item, job, false);

        item.setData(new TreeData(Editor.JOB_COMMANDS, job, Options.getHelpURL("job.commands")));
        parent.setExpanded(expand);
    }


    public void treeFillOrder(TreeItem parent, Element order, boolean expand) {
        parent.removeAll();

        Element runtime = order.getChild("run_time");

        // create runtime tag
        if (runtime == null) {
            runtime = new Element("run_time");
            order.addContent(runtime);
        }
        if (runtime != null) {
            TreeItem run = new TreeItem(parent, SWT.NONE);
            run.setText("Run Time");
            run.setData(new TreeData(Editor.RUNTIME, order, Options.getHelpURL("job.run_time"), "run_time"));

            TreeItem item = new TreeItem(run, SWT.NONE);
            item.setText("Everyday");
            item.setData(new TreeData(Editor.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));

            item = new TreeItem(run, SWT.NONE);
            item.setText("Weekdays");
            item
                    .setData(new TreeData(Editor.WEEKDAYS, runtime, Options.getHelpURL("job.run_time.weekdays"),
                            "weekdays"));
            treeFillDays(item, runtime, 0, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Monthdays");
            item.setData(new TreeData(Editor.MONTHDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"),
                    "monthdays"));
            treeFillDays(item, runtime, 1, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Ultimos");
            item.setData(new TreeData(Editor.ULTIMOS, runtime, Options.getHelpURL("job.run_time.ultimos"), "ultimos"));
            treeFillDays(item, runtime, 2, false);

            item = new TreeItem(run, SWT.NONE);
            item.setText("Specific Days");
            item.setData(new TreeData(Editor.DAYS, runtime, Options.getHelpURL("job.run_time.specific_days")));
            if (runtime != null)
                treeFillDays(item, runtime, 3, false);
        }

        parent.setExpanded(expand);
    }


    public void treeFillCommands(TreeItem parent, Element job, boolean expand) {
        new JobCommandListener(_dom, null, null).fillCommands(job, parent, expand);
    }


    public void treeFillDays(TreeItem parent, Element element, int type, boolean expand) {
        if (element != null) {
            if (type == DaysListener.WEEKDAYS || type == DaysListener.MONTHDAYS || type == DaysListener.ULTIMOS)
                new DaysListener(_dom, element, type).fillTreeDays(parent, expand);
            else if (type == 3)
                new DateListener(_dom, element, 1).fillTreeDays(parent, expand);
            else
                System.out.println("Invalid type = " + type + " for filling the days in the tree!");
        }
    }


    public boolean treeSelection(Tree tree, Composite c) {
        try {
            if (tree.getSelectionCount() > 0) {

                // dispose the old form
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i]))
                        return false;
                    children[i].dispose();
                }

                TreeItem item = tree.getSelection()[0];
                TreeData data = (TreeData) item.getData();

                _dom.setInit(true);
                switch (data.getType()) {
                    case Editor.CONFIG:
                        new ConfigForm(c, SWT.NONE, _dom);
                        break;
                    case Editor.SECURITY:
                        new SecurityForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.BASE:
                        new BaseForm(c, SWT.NONE, _dom);
                        break;
                    case Editor.PROCESS_CLASSES:
                        new ProcessClassesForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.MONITOR:
                        new ScriptForm(c, SWT.NONE, "Monitor", _dom, data.getElement(), data.getType());
                        break;
                    case Editor.SCRIPT:
                        new ScriptForm(c, SWT.NONE, "Start Script", _dom, data.getElement(), data.getType());
                        break;
                    case Editor.JOB:
                        new JobForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.EXECUTE:
                        new ExecuteForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.ORDERS:
                        new OrdersForm(c, SWT.NONE, _dom, _gui, this);
                        break;
                    case Editor.ORDER:
                        new OrderForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.JOB_COMMAND:
                        new JobCommandForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.JOB_COMMANDS:
                        new JobCommandsForm(c, SWT.NONE, _dom, data.getElement(), _gui, this);
                        break;
                    case Editor.RUNTIME:
                        new RunTimeForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.WEEKDAYS:
                        new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.WEEKDAYS);
                        break;
                    case Editor.MONTHDAYS:
                        new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.MONTHDAYS);
                        break;
                    case Editor.ULTIMOS:
                        new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.ULTIMOS);
                        break;
                    case Editor.EVERYDAY:
                    case Editor.PERIODS:
                        new PeriodsForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.JOBS:
                        new JobsForm(c, SWT.NONE, _dom, _gui);
                        break;
                    case Editor.HOLIDAYS:
                        new DateForm(c, SWT.NONE, DateListener.HOLIDAY, _dom, data.getElement(), _gui);
                        break;
                    case Editor.DAYS:
                        new DateForm(c, SWT.NONE, DateListener.DATE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.WEBSERVICES:
                        new WebservicesForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.OPTIONS:
                        new JobOptionsForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.JOB_CHAINS:
                        new JobChainsForm(c, SWT.NONE, _dom, data.getElement());
                        break;
                    case Editor.COMMANDS:
                        new CommandsForm(c, SWT.NONE, _dom, _gui);
                        break;
                    default:
                        System.out.println("no form found for " + item.getText());
                }

                c.layout();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
        }
        _dom.setInit(false);
        return true;
    }

}
