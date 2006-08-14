package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class JobChainsListener {
    private SchedulerDom _dom;

    private Element      _config;

    private Element      _chains;

    private Element      _chain;

    private Element      _node;

    private String[]     _states;

    private String[]     _chainNames;


    public JobChainsListener(SchedulerDom dom, Element config) {
        _dom = dom;
        _config = config;
        _chains = _config.getChild("job_chains");
    }


    public void fillChains(Table table) {
        table.removeAll();
        if (_chains != null) {
            List list = _chains.getChildren("job_chain");
            _chainNames = new String[list.size()];
            int index = 0;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element chain = (Element) it.next();
                String name = Utils.getAttributeValue("name", chain);
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, name);
                item.setText(1, Utils.isAttributeValue("orders_recoverable", chain) ? "Yes" : "No");
                item.setText(2, Utils.isAttributeValue("visible", chain) ? "Yes" : "No");
                _chainNames[index++] = name;
            }
        }
    }


    public void selectChain(int index) {
        if (index >= 0)
            _chain = (Element) _chains.getChildren("job_chain").get(index);
        else
            _chain = null;
    }


    public String getChainName() {
        return Utils.getAttributeValue("name", _chain);
    }


    public boolean getRecoverable() {
        return Utils.isAttributeValue("orders_recoverable", _chain);
    }


    public boolean getVisible() {
        return Utils.isAttributeValue("visible", _chain);
    }


    public void newChain() {
        _chain = new Element("job_chain");
    }


    public void applyChain(String name, boolean ordersRecoverable, boolean visible) {
        Utils.setAttribute("name", name, _chain);
        Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
        Utils.setAttribute("visible", visible, _chain);

        if (_chains == null) {
            _chains = new Element("job_chains");
            _config.addContent(_chains);
        }

        if (!_chains.getChildren("job_chain").contains(_chain))
            _chains.addContent(_chain);

        _dom.setChanged(true);
    }


    public void deleteChain(int index) {
        List chains = _chains.getChildren("job_chain");
        ((Element) chains.get(index)).detach();
        if (chains.size() == 0) {
            _config.removeChild("job_chains");
            _chains = null;
        }
        _dom.setChanged(true);
        _chain = null;
    }


    public void fillChain(Table table) {
        table.removeAll();
        if (_chain != null) {

            setStates();

            Iterator it = _chain.getChildren("job_chain_node").iterator();
            while (it.hasNext()) {
                Element node = (Element) it.next();
                String state = Utils.getAttributeValue("state", node);
                String job = Utils.getAttributeValue("job", node);
                String next = Utils.getAttributeValue("next_state", node);
                String error = Utils.getAttributeValue("error_state", node);

                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(new String[] { state, job, next, error });

                if (!next.equals("") && (state.equals("next") || !checkForState(next)))
                    item.setBackground(2, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                if (!error.equals("") && (state.equals("error") || !checkForState(error)))
                    item.setBackground(3, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
            }
        }
    }


    private boolean checkForState(String state) {
        for (int i = 0; i < _states.length; i++) {
            if (_states[i].equals(state))
                return true;
        }
        return false;
    }


    public void selectNode(int index) {
        if (index >= 0) {
            _node = (Element) _chain.getChildren("job_chain_node").get(index);
        } else
            _node = null;
        if (_states == null)
            setStates();
    }


    public boolean isFullNode() {
        if (_node != null)
            return _node.getAttributeValue("job") != null;

        // return _node.getAttributeValue("next_state") != null
        // || _node.getAttributeValue("error_state") != null;
        else
            return true;
    }


    public String getState() {
        return Utils.getAttributeValue("state", _node);
    }


    public void setState(String state) {
        Utils.setAttribute("state", state, _node, _dom);
    }


    public String getJob() {
        return Utils.getAttributeValue("job", _node);
    }


    public void setJob(String job) {
        Utils.setAttribute("job", job, _node, _dom);
    }


    public String getNextState() {
        return Utils.getAttributeValue("next_state", _node);
    }


    public void setNextState(String state) {
        Utils.setAttribute("next_state", state, _node, _dom);
    }


    public String getErrorState() {
        return Utils.getAttributeValue("error_state", _node);
    }


    public void setErrorState(String state) {
        Utils.setAttribute("error_state", state, _node, _dom);
    }


    public void applyNode(String state, String job, String next, String error) {
        if (_node != null) {
            Utils.setAttribute("state", state, _node, _dom);
            Utils.setAttribute("job", job, _node, _dom);
            Utils.setAttribute("next_state", next, _node, _dom);
            Utils.setAttribute("error_state", error, _node, _dom);
        } else {
            Element node = new Element("job_chain_node");
            Utils.setAttribute("state", state, node, _dom);
            Utils.setAttribute("job", job, node, _dom);
            Utils.setAttribute("next_state", next, node, _dom);
            Utils.setAttribute("error_state", error, node, _dom);
            _chain.addContent(node);
            _node = node;
        }
        _dom.setChanged(true);
        setStates();
    }


    public void deleteNode(int index) {
        List nodes = _chain.getChildren("job_chain_node");
        nodes.remove(index);
        _node = null;
        _dom.setChanged(true);
        setStates();
    }


    public String[] getJobs() {
        Element job = _config.getChild("jobs");
        if (job != null) {
            List jobs = job.getChildren("job");
            String[] names = new String[jobs.size()];
            Iterator it = jobs.iterator();
            int index = 0;
            while (it.hasNext()) {
                String name = ((Element) it.next()).getAttributeValue("name");
                names[index++] = name != null ? name : "";
            }
            return names;
        } else
            return new String[0];
    }


    private void setStates() {
        List nodes = _chain.getChildren("job_chain_node");
        Iterator it = nodes.iterator();
        _states = new String[nodes.size()];
        int index = 0;
        while (it.hasNext()) {
            String state = ((Element) it.next()).getAttributeValue("state");
            _states[index++] = state != null ? state : "";
        }
    }


    public String[] getStates() {
        if (_states == null)
            return new String[0];
        else if (_node != null) {
            String state = getState();
            int index = 0;
            String[] states = new String[_states.length - 1];
            for (int i = 0; i < _states.length; i++) {
                if (!_states[i].equals(state))
                    states[index++] = _states[i];
            }
            return states;
        } else
            return _states;
    }


    public boolean isValidState(String state) {
        if (_states == null)
            return true;

        for (int i = 0; i < _states.length; i++) {
            if (_states[i].equalsIgnoreCase(state) && !_states[i].equals(getState()))
                return false;
        }
        return true;
    }


    public boolean isValidChain(String name) {
        if (_chainNames != null) {
            for (int i = 0; i < _chainNames.length; i++) {
                if (_chainNames[i].equals(name))
                    return false;
            }
        }
        return true;
    }
}
