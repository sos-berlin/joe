package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
 
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.Utils;

public class OrdersListener {

	private DomParser _dom;

	private IUpdate _main;
	
	private Element _commands;
	private Element _config;
	private List _orders;

	public OrdersListener(DomParser dom, IUpdate update) {
		_dom = dom;
		_main = update;
		_config = _dom.getRoot().getChild("config");
		_commands = _config.getChild("commands");
		if(_commands != null)
			_orders = _commands.getChildren("add_order");
	}

	private void initCommands() {
		if(_config.getChild("commands") == null) {
		  _commands = new Element("commands");
		  _config.addContent(_commands);
		} else {
			_commands = _config.getChild("commands");
		}
		_orders = _commands.getChildren("add_order");
	}
	
	public void fillTable(Table table) {
		table.removeAll();
		if (_orders != null) {
			for(Iterator it = _orders.iterator(); it.hasNext(); ) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String id = Utils.getAttributeValue("id", e);
					item.setText(0, id);
				}
			}
		}
	}

	private boolean haveId(int id,Table table) {
		int count = table.getItemCount();
		 for (int i=0;i<count;i++) {
			  TableItem item = table.getItem(i);
			  String actId = item.getText();
 		  if (actId.trim().equals(String.valueOf(id))) return true;
		  }
		return false;
	}
	
	public void newCommands(Table table) {
		
		  boolean found = false;
		  String id=""; 
  	  int c=1;
      while (!found) {
      	 if (!haveId(c,table)) {
          	 found = true;
          	 id=String.valueOf(c);
         	   }
         	c++;
       }
		
		Element add_order = new Element("add_order");
	  Element runtime = new Element("run_time");
		runtime.setAttribute("let_run", "no");
		add_order.setAttribute("id", id);
		

		if(_commands == null)
			initCommands();
		
	 	_orders.add(add_order.addContent(runtime));
		_dom.setChanged(true);
		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateOrders();
	}
	
	 


	public boolean deleteCommands(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			table.remove(index);
			_main.updateOrders();
 
			if (index >= table.getItemCount())
				index--;
			if (index >= 0) {
				table.setSelection(index);
				return true;
			}
		}
		return false;
	}
	
 
	 
}
