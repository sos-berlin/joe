package com.sos.joe.jobdoc.editor.listeners;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.program.Program;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class JobDocJobListener extends JobDocBaseListener<DocumentationDom> {

    private final Element _job;
    private static final String[] _orderValues = { "yes", "no", "both" };
    private static final String[] _tasksValues = { "", "1", "unbounded" };

    public JobDocJobListener(final DocumentationDom dom, final Element job) {
        _dom = dom;
        _job = job;
        initElement(job);
    }

    public Element getJob() {
        return _job;
    }

    public File writeToFile() throws IOException, JDOMException {
        File tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());
        tmp.deleteOnExit();
        _dom.writeFileWithDom(tmp);
        return tmp;
    }

    public void preview() {
        Element element = _dom.getRoot();
        if (element != null) {
            try {
                String filename = _dom.transform(element);
                if (filename.length() > 0) {
                    Program prog = Program.findProgram("html");
                    if (prog != null)
                        prog.execute(filename);
                    else {
                        Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
                    }
                }
            } catch (Exception ex) {
                new ErrorLog("error in preview.", ex);
            }
        }
    }

    public String getName() {
        return getAttributeValue("name", _job);
    }

    public void setName(final String name) {
        Utils.setAttribute("name", name, _job, _dom);
    }

    public String getTitle() {
        return getAttributeValue("title", _job);
    }

    public void setTitle(final String title) {
        Utils.setAttribute("title", title, _job, _dom);
    }

    public String[] getOrderValues() {
        return _orderValues;
    }

    public String getOrder() {
        String order = _job.getAttributeValue("order");
        return order;
    }

    public boolean isOrderJob() {
        String order = _job.getAttributeValue("order");
        return order == null ? false : order.equalsIgnoreCase("yes");
    }

    // public String getOrder() {
    // String order = getAttributeValue("order", _job);
    // if (order.length() == 0)
    // Utils.setAttribute("order", _orderValues[0], _job);
    // return order.length() > 0 ? order : _orderValues[0];
    // }
    //
    public void setOrder(final String order) {
        Utils.setAttribute("order", order, _job, _dom);
    }

    public String[] getTasksValues() {
        return _tasksValues;
    }

    public String getTasks() {
        return getAttributeValue("tasks", _job);
    }

    public void setTasks(final String tasks) {
        Utils.setAttribute("tasks", tasks, _job, _dom);
    }
}
