package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandListener {

    private ISchedulerUpdate _main;
    private SchedulerDom _dom;
    private List _params;
    private List _environments;
    private Element _command;
    private Element _job;

    public JobCommandListener(SchedulerDom dom, Element command, ISchedulerUpdate update) {
        _dom = dom;
        _command = command;
        _main = update;
        if (_command != null) {
            _job = _command.getParentElement().getParentElement();
        }
    }

    private void initParams(Table table) {
        int j = 0;
        j = getActCommand(table);
        if (j >= 0) {
            List l = _command.getChildren();
            if (l != null) {
                Element e = (Element) l.get(j);
                if (e.getChild("params") != null) {
                    _params = e.getChild("params").getChildren();
                }
                if (_params == null) {
                    e.addContent(0, new Element("params"));
                    _params = e.getChild("params").getChildren();
                }
            }
        }
    }

    private void initEnvironment(Table table) {
        int j = 0;
        j = getActCommand(table);
        if (j >= 0) {
            List l = _command.getChildren();
            if (l != null) {
                Element e = (Element) l.get(j);
                if (e.getChild("environment") != null) {
                    _environments = e.getChild("environment").getChildren();
                }
                if (_environments == null) {
                    e.addContent(0, new Element("environment"));
                    _environments = e.getChild("environment").getChildren();
                }
            }
        }
    }

    public void clearParams() {
        _params = null;
    }

    public void clearEnvironment() {
        _environments = null;
    }

    public boolean isDisabled() {
        return "no".equalsIgnoreCase(Utils.getAttributeValue("enabled", _job));
    }

    public String getName() {
        return Utils.getAttributeValue("name", _job);
    }

    public String getTitle() {
        return Utils.getAttributeValue("title", _job);
    }

    public void fillEnvironment(Table tableCommand, Table tableEnvironment) {
        _environments = null;
        initEnvironment(tableCommand);
        tableEnvironment.removeAll();
        if (_environments != null) {
            Iterator it = _environments.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if ("variable".equals(e.getName())) {
                        TableItem item = new TableItem(tableEnvironment, SWT.NONE);
                        item.setText(0, ((Element) o).getAttributeValue("name"));
                        item.setText(1, ((Element) o).getAttributeValue("value"));
                    }
                }
            }
        }
    }

    public void fillParams(Table tableCommand, Table tableParameters) {
        clearParams();
        initParams(tableCommand);
        tableParameters.removeAll();
        if (_params != null) {
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if ("param".equals(e.getName())) {
                        TableItem item = new TableItem(tableParameters, SWT.NONE);
                        item.setText(0, ((Element) o).getAttributeValue("name"));
                        item.setText(1, ((Element) o).getAttributeValue("value"));
                    } else if ("copy_params".equals(e.getName())) {
                        TableItem item = new TableItem(tableParameters, SWT.NONE);
                        item.setText(0, "<from>");
                        item.setText(1, ((Element) o).getAttributeValue("from"));
                    }
                }
            }
        }
    }

    public void deleteParameter(Table table, int index) {
        if (_params != null) {
            _params.remove(index);
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
        }
        table.remove(index);
    }

    public void deleteEnvironment(Table table, int index) {
        if (_environments != null) {
            _environments.remove(index);
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
        }
        table.remove(index);
    }

    public void addCommand(Element e) {
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
        _command.addContent(e);
    }

    private int getActCommand(Table table) {
        int index = table.getSelectionIndex();
        int j = index;
        int ignore = 0;
        List c = _command.getChildren();
        Iterator it2 = c.iterator();
        while (it2.hasNext() && j >= 0) {
            Element e2 = (Element) it2.next();
            if (!"start_job".equals(e2.getName()) && !"add_order".equals(e2.getName()) && !"order".equals(e2.getName())) {
                ignore++;
            } else {
                j--;
            }
        }
        return index + ignore;
    }

    public void deleteCommand(Table table) {
        int j = 0;
        int index = table.getSelectionIndex();
        j = getActCommand(table);
        table.remove(index);
        List c = _command.getChildren();
        if (_command != null) {
            c.remove(j);
        }
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void saveEnvironment(Table table, String name, String value) {
        boolean found = false;
        String value2 = value.replaceAll("\"", "&quot;");
        if (_environments != null) {
            int index = 0;
            Iterator it = _environments.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (name.equals(e.getAttributeValue("name"))) {
                        found = true;
                        e.setAttribute("value", value2);
                        _dom.setChanged(true);
                        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
                        table.getItem(index).setText(1, value);
                        break;
                    }
                    index++;
                }
            }
        }
        if (!found) {
            Element e = new Element("variable");
            e.setAttribute("name", name);
            e.setAttribute("value", value2);
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
            if (_environments == null) {
                initEnvironment(table);
            }
            _environments.add(e);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }

    public void saveParameter(Table table, String name, String value) {
        boolean found = false;
        if (_params != null) {
            if ("<from>".equals(name)) {
                found = (table.getSelectionIndex() > -1);
            } else {
                int index = 0;
                Iterator it = _params.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof Element) {
                        Element e = (Element) o;
                        if ("param".equals(e.getName()) && name.equals(e.getAttributeValue("name"))) {
                            found = true;
                            e.setAttribute("value", value);
                            _dom.setChanged(true);
                            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
                            table.getItem(index).setText(1, value);
                        }
                        index++;
                    }
                }
            }
            if ("<from>".equals(name) && found) {
                int index = table.getSelectionIndex();
                table.getItem(index).setText(0, name);
                table.getItem(index).setText(1, value);
                Element e = (Element) _params.get(index);
                e.setName("copy_params");
                e.setAttribute("from", value);
                e.removeAttribute("name");
                e.removeAttribute("value");
                _dom.setChanged(true);
                _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
            }
        }
        if (!found) {
            Element e = new Element("param");
            if (!"<from>".equals(name)) {
                e.setAttribute("name", name);
                e.setAttribute("value", value);
            } else {
                e.setName("copy_params");
                e.setAttribute("from", value);
            }
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
            if (_params == null) {
                initParams(table);
            }
            if (_params != null) {
                _params.add(e);
            }
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }

    public void setCommandName(Button bApply, String cmd, String value, Table table) {
        if (_command != null) {
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
            int i = getActCommand(table);
            List l = _command.getChildren();
            Element e = (Element) l.get(i);
            e.setName(cmd);
            if ("add_order".equals(cmd)) {
                Utils.setAttribute("id", value, e, _dom);
                Utils.setAttribute("job", "", e, _dom);
                e.removeChildren("environment");
            } else if ("order".equals(cmd)) {
                Utils.setAttribute("id", value, e, _dom);
                Utils.setAttribute("job", "", e, _dom);
                e.removeChildren("environment");
            } else {
                Utils.setAttribute("id", "", e, _dom);
                Utils.setAttribute("job", value, e, _dom);
            }
            if (bApply != null) {
                bApply.setEnabled(true);
            }
        }
    }

    public boolean getCommandReplace(Table table) {
        int i = getActCommand(table);
        List l = _command.getChildren();
        Element e = (Element) l.get(i);
        return "yes".equalsIgnoreCase(Utils.getAttributeValue("replace", e));
    }

    public String getExitCode() {
        return Utils.getAttributeValue("on_exit_code", _command);
    }

    public void setExitCode(String value, boolean updateTree) {
        Utils.setAttribute("on_exit_code", value, _command, _dom);
        if (updateTree) {
            _main.updateTreeItem(value);
        }
    }

    public Element getCommand() {
        return _command;
    }

    public void fillCommands(Table table) {
        boolean created;
        TableItem item = null;
        table.removeAll();
        List c = _command.getChildren();
        Iterator it2 = c.iterator();
        while (it2.hasNext()) {
            Element e2 = (Element) it2.next();
            created = false;
            if ("start_job".equals(e2.getName()) || "add_order".equals(e2.getName()) || "order".equals(e2.getName())) {
                if (!created) {
                    item = new TableItem(table, SWT.NONE);
                    item.setText(1, "");
                    created = true;
                }
                item.setText(0, e2.getName());
                item.setText(3, Utils.getAttributeValue("at", e2));
                if ("start_job".equals(e2.getName())) {
                    item.setText(1, Utils.getAttributeValue("job", e2));
                }
                if ("add_order".equals(e2.getName()) || "order".equals(e2.getName())) {
                    item.setText(1, Utils.getAttributeValue("id", e2));
                    item.setText(2, Utils.getAttributeValue("job_chain", e2));
                }
            }
        }
    }

    public String[] getJobChains() {
        String[] _chains = new String[0];
        if (_dom.isLifeElement()) {
            return new String[0];
        }
        Element element = null;
        if (_job != null && _job.getParentElement() != null && _job.getParentElement().getParentElement() != null) {
            element = _job.getParentElement().getParentElement().getChild("job_chains");
        }
        if (element != null) {
            List<Element> chains = element.getChildren("job_chain");
            _chains = new String[chains.size()];
            int index = 0;
            Iterator<Element> it = chains.iterator();
            while (it.hasNext()) {
                String name = (it.next()).getAttributeValue("name");
                _chains[index++] = name != null ? name : "";
            }
        } else {
            _chains = new String[0];
        }
        return _chains;
    }

    public List getEnvironments() {
        return _environments;
    }

    public void setJob(String job) {
        Utils.setAttribute("job", job, _command, _dom);
        _main.updateTreeItem(_command.getName() + ": " + job);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setAt(String at) {
        Utils.setAttribute("at", at, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setOrderId(String orderId) {
        Utils.setAttribute("id", orderId, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setPriority(String priority) {
        Utils.setAttribute("priority", priority, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setReplace(String replace) {
        Utils.setAttribute("replace", replace, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setState(String state) {
        Utils.setAttribute("state", state, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setJobChain(String jobChain) {
        Utils.setAttribute("job_chain", jobChain, _command, _dom);
        _main.updateTreeItem(_command.getName() + ": " + jobChain);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setEndState(String state) {
        Utils.setAttribute("end_state", state, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public void setTitle(String title) {
        Utils.setAttribute("title", title, _command, _dom);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
    }

    public String[] getStates() {
        String[] retVal = new String[] { "" };
        ArrayList stateList = new ArrayList();
        try {
            String jobChainname = Utils.getAttributeValue("job_chain", getCommand());
            XPath x3 = XPath.newInstance("//job_chain[@name='" + jobChainname + "']");
            Element jobChain = (Element) x3.selectSingleNode(_dom.getDoc());
            if (jobChain == null) {
                jobChainname = Utils.getAttributeValue("job_chain_node.job_chain", getCommand());
                x3 = XPath.newInstance("//job_chain_node.job_chain[@name='" + jobChainname + "']");
                jobChain = (Element) x3.selectSingleNode(_dom.getDoc());
            }
            if (jobChain == null) {
                return retVal;
            }
            List l = jobChain.getChildren();
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                String state = Utils.getAttributeValue("state", e);
                if (!state.isEmpty()) {
                    stateList.add(state);
                }
            }
            if (!stateList.isEmpty()) {
                retVal = new String[stateList.size()];
                for (int i = 0; i < stateList.size(); i++) {
                    retVal[i] = stateList.get(i).toString();
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
        return retVal;
    }

}