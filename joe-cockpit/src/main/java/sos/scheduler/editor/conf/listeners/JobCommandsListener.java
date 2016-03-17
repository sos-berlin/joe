package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandsListener {

    private SchedulerDom _dom = null;
    private ISchedulerUpdate _main = null;
    private Element _job = null;
    private List _commands = null;

    public JobCommandsListener(SchedulerDom dom, Element job, ISchedulerUpdate update) {
        _dom = dom;
        _main = update;
        _job = job;
        if (_job != null) {
            _commands = _job.getChildren("commands");
        }
    }

    private void initCommands() {
        _commands = _job.getChildren("commands");
    }

    public void fillTable(Table table) {
        table.removeAll();
        if (_commands != null) {
            for (Iterator it = _commands.iterator(); it.hasNext();) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setData(e);
                    String exitcode = Utils.getAttributeValue("on_exit_code", e);
                    item.setText(0, exitcode);
                }
            }
        }
    }

    private boolean haveCode(int code, Table table) {
        int count = table.getItemCount();
        for (int i = 0; i < count; i++) {
            TableItem item = table.getItem(i);
            String actCode = item.getText();
            if (actCode.indexOf(" " + String.valueOf(code)) >= 0) {
                return true;
            }
            if (actCode.indexOf(String.valueOf(code) + " ") >= 0) {
                return true;
            }
            if (actCode.trim().equals(String.valueOf(code))) {
                return true;
            }
        }
        return false;
    }

    public void newCommands(Table table) {
        boolean error = false;
        boolean success = false;
        boolean found = false;
        String code = "";
        int count = table.getItemCount();
        for (int i = 0; i < count; i++) {
            TableItem item = table.getItem(i);
            if ("success".equals(item.getText())) {
                success = true;
            }
            if ("error".equals(item.getText())) {
                error = true;
            }
        }
        if (!success) {
            code = "success";
        }
        if (!error) {
            code = "error";
        }
        if ("".equals(code)) {
            int c = 1;
            while (!found) {
                if (!haveCode(c, table)) {
                    found = true;
                    code = String.valueOf(c);
                }
                c++;
            }
        }
        Element commands = new Element("commands");
        commands.setAttribute("on_exit_code", code);
        if (_commands == null) {
            initCommands();
        }
        _commands.add(commands);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
        fillTable(table);
        table.setSelection(table.getItemCount() - 1);
        _main.updateCommands();
    }

    public boolean deleteCommands(Table table) {
        int index = table.getSelectionIndex();
        if (index >= 0) {
            TableItem item = table.getItem(index);
            Element e = (Element) item.getData();
            e.detach();
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
            table.remove(index);
            _main.updateCommands();
            if (index >= table.getItemCount()) {
                index--;
            }
            if (index >= 0) {
                table.setSelection(index);
                return true;
            }
        }
        return false;
    }

}