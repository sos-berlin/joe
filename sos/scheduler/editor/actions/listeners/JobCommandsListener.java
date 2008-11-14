package sos.scheduler.editor.actions.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
//import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.forms.ActionsForm;

public class JobCommandsListener {


	private    ActionsDom            _dom        = null;

	private    ActionsForm           _main       = null;

	private    Element               _action        = null;

	private    List                  _commands   = null;


	public JobCommandsListener(ActionsDom dom, Element action, ActionsForm update) {

		_dom = dom;
		_main = update;
		_action = action;
		if (_action != null) {
			Element commands = _action.getChild("commands");
			if(commands != null)
				_commands = commands.getChildren("command");
		}

	}		
	
	private void initCommands() {
		if(_action.getChild("commands") == null)
			_action.addContent(new Element("commands"));
		_commands = _action.getChild("commands").getChildren("command");
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
					
					item.setText(0, Utils.getAttributeValue("name", e));
					item.setText(1, Utils.getAttributeValue("scheduler_host", e));
					item.setText(2, Utils.getAttributeValue("schdeuler_port", e));
				}
			}
		}

	}


	/*private boolean haveCode(int code, Table table) {
		int count = table.getItemCount();
		for (int i = 0; i < count; i++) {
			TableItem item = table.getItem(i);
			String actCode = item.getText();
			if (actCode.indexOf(" " + String.valueOf(code)) >= 0)
				return true;
			if (actCode.indexOf(String.valueOf(code) + " ") >= 0)
				return true;
			if (actCode.trim().equals(String.valueOf(code)))
				return true;
		}
		return false;
	}*/


	public void newCommands(Table table) {

		
		Element command = new Element("command");
		command.setAttribute("name", "command_" +(table.getItemCount()+1));
		if (_commands == null)
			initCommands();
		_commands.add(command);
		_dom.setChanged(true);
		
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
			//_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
			table.remove(index);
			_main.updateCommands();

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
