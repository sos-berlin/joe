package sos.scheduler.editor.doc.listeners;

import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;

public class JobListener {
    private DocumentationDom      _dom;

    private Element               _job;

    private static final String[] _orderValues = { "yes", "no", "both" };

    private static final String[] _tasksValues = { "", "1", "unbounded" };


    public JobListener(DocumentationDom dom, Element job) {
        _dom = dom;
        _job = job;
    }


    public String getName() {
        return Utils.getAttributeValue("name", _job);
    }


    public void setName(String name) {
        Utils.setAttribute("name", name, _job, _dom);
    }


    public String getTitle() {
        return Utils.getAttributeValue("title", _job);
    }


    public void setTitle(String title) {
        Utils.setAttribute("title", title, _job, _dom);
    }


    public String[] getOrderValues() {
        return _orderValues;
    }


    public String getOrder() {
        String order = Utils.getAttributeValue("order", _job);
        if (order.length() == 0)
            Utils.setAttribute("order", _orderValues[0], _job);
        return order.length() > 0 ? order : _orderValues[0];
    }


    public void setOrder(String order) {
        Utils.setAttribute("order", order, _job, _dom);
    }


    public String[] getTasksValues() {
        return _tasksValues;
    }


    public String getTasks() {
        return Utils.getAttributeValue("tasks", _job);
    }


    public void setTasks(String tasks) {
        Utils.setAttribute("tasks", tasks, _job, _dom);
    }
}
