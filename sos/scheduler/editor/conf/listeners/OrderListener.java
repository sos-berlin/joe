package sos.scheduler.editor.conf.listeners;


import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class OrderListener {
    private ISchedulerUpdate _main;

    private SchedulerDom     _dom;

    private String[]         _chains = new String[0];

    private List             _params;

    private Element          _order;

    private Element          _config;


    public OrderListener(SchedulerDom dom, Element order, ISchedulerUpdate update) {
        _dom = dom;
        _config = _dom.getRoot().getChild("config");
        _order = order;
        _main = update;
        
    }


    private void initParams() {

        if (_order.getChild("params") != null) {
            _params = _order.getChild("params").getChildren();
        }

        if (_params == null) {
            _order.addContent(0, new Element("params"));
            _params = _order.getChild("params").getChildren();
        }
    }


    public void clearParams() {
        _params = null;
    }


    public void fillParams(Table tableParameters) {
        clearParams();
        tableParameters.removeAll();
        initParams();


        if (_params != null) {
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (e.getName().equals("param")) {
                        TableItem item = new TableItem(tableParameters, SWT.NONE);
                        item.setText(0, ((Element) o).getAttributeValue("name"));
                        item.setText(1, ((Element) o).getAttributeValue("value"));
                    }
                }
            }
        }
    }


    public void deleteParameter(Table table, int index) {
        if (_params != null) {
            _params.remove(index);
            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
            _dom.setChanged(true);
        }
        table.remove(index);
    }


    public void saveParameter(Table table, String name, String value) {
        boolean found = false;
        //String value2 = value.replaceAll("\"", "&quot;");

        if (_params != null) {

            int index = 0;
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (e.getName().equals("param")) {
                        if (name.equals(e.getAttributeValue("name"))) {
                            found = true;
                            //e.setAttribute("value", value2);
                            e.setAttribute("value", value);
                            _dom.setChanged(true);
                            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
                            table.getItem(index).setText(1, value);
                        }
                    }
                    index++;
                }
            }
        }

        if (!found) {
            Element e = new Element("param");

            e.setAttribute("name", name);
            //e.setAttribute("value", value2);
            e.setAttribute("value", value);
            _dom.setChanged(true);
            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);

            if (_params == null)
                initParams();
            if (_params != null)
                _params.add(e);

            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }


    public String getCommandAttribute(String attribute) {
        return Utils.getAttributeValue(attribute, _order);
    }


    public void setCommandAttribute(String name, String value) {
    	
    	_dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.DELETE);
        Utils.setAttribute(name, value, _order, _dom);        
        _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
    }


    public void setOrderId(String id, boolean updateTree) {
    	_dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.DELETE);
        Utils.setAttribute("id", id, _order, _dom);
        if (updateTree)
            _main.updateOrder(id);
        
        _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
    }


    public boolean getCommandReplace() {
        return Utils.getAttributeValue("replace", _order).equalsIgnoreCase("yes");
    }


    public String[] getJobChains() {
    	if(_dom.isLifeElement()) {
    		_chains = new String[0];
    		return _chains;		
    	}
    		
        Element element = _config.getChild("job_chains");
        if (element != null) {
            List chains = element.getChildren("job_chain");
            _chains = new String[chains.size()];
            int index = 0;
            Iterator it = chains.iterator();
            while (it.hasNext()) {
                String name = ((Element) it.next()).getAttributeValue("name");
                _chains[index++] = name != null ? name : "";
            }
        } else
            _chains = new String[0];

        return _chains;
    }
}
