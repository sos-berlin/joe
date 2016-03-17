package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainsListener {

    private SchedulerDom _dom = null;
    private Element _config = null;
    private Element _chains = null;
    private Element _chain = null;
    private Element _node = null;
    private String[] _chainNames = null;
    private static Table tChains = null;
    private ISchedulerUpdate update = null;

    public JobChainsListener(SchedulerDom dom, Element config, ISchedulerUpdate update_) {
        _dom = dom;
        _config = config;
        update = update_;
        _chains = _config.getChild("job_chains");
    }

    public void fillChains() {
        if (tChains != null) {
            fillChains(tChains);
        }
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
                if (sos.scheduler.editor.conf.listeners.DetailsListener.existDetailsParameter(null, name, null, _dom, update, true, null)) {
                    item.setBackground(Options.getLightBlueColor());
                } else {
                    item.setBackground(null);
                }
                item.setText(0, name);
                item.setText(1, Utils.isAttributeValue("orders_recoverable", chain) ? "Yes" : "No");
                item.setText(2, Utils.isAttributeValue("visible", chain) ? "Yes" : "No");
                if (!Utils.isElementEnabled("job_chain", _dom, chain)) {
                    item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
                _chainNames[index++] = name;
            }
        }
    }

    public int indexOf(String jobChainName) {
        boolean found = false;
        List list;
        if (_chains == null) {
            return -1;
        } else {
            list = _chains.getChildren("job_chain");
        }
        Iterator it = list.iterator();
        int i = -1;
        int index = -1;
        while (it.hasNext() && !found) {
            Element chain = (Element) it.next();
            String name = Utils.getAttributeValue("name", chain);
            if (name.equals(jobChainName)) {
                found = true;
                i++;
                index = i;
            }
        }
        return index;
    }

    public void selectChain(int index) {
        if (_chains == null) {
            _chains = _config.getChild("job_chains");
        }
        if (index >= 0) {
            _chain = (Element) _chains.getChildren("job_chain").get(index);
        } else {
            _chain = null;
        }
    }

    public String getChainName() {
        return Utils.getAttributeValue("name", _chain);
    }

    public void newChain() {
        _chain = new Element("job_chain");
    }

    public void applyChain(String name, boolean ordersRecoverable, boolean visible) {
        String oldjobChainName = Utils.getAttributeValue("name", _chain);
        if (oldjobChainName != null && !oldjobChainName.isEmpty()) {
            _dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
        }
        Utils.setAttribute("name", name, _chain);
        Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
        Utils.setAttribute("visible", visible, _chain);
        if (_chains == null) {
            _chains = new Element("job_chains");
            _config.addContent(_chains);
        }
        if (!_chains.getChildren("job_chain").contains(_chain)) {
            _chains.addContent(_chain);
        }
        update.updateJobChains();
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job_chain", name, SchedulerDom.NEW);
    }

    public void deleteChain(int index) {
        List chains = _chains.getChildren("job_chain");
        String delname = Utils.getAttributeValue("name", (Element) chains.get(index));
        ((Element) chains.get(index)).detach();
        if (chains.isEmpty()) {
            _config.removeChild("job_chains");
            _chains = null;
        }
        _chainNames = new String[chains.size()];
        int i = 0;
        Iterator it = chains.iterator();
        while (it.hasNext()) {
            Element chain = (Element) it.next();
            String name = Utils.getAttributeValue("name", chain);
            _chainNames[i++] = name;
        }
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job_chain", delname, SchedulerDom.DELETE);
        _chain = null;
        update.updateJobChains();
    }

    public String getState() {
        return Utils.getAttributeValue("state", _node);
    }

    public SchedulerDom get_dom() {
        return _dom;
    }

    public Element getChain() {
        return _chain;
    }

}