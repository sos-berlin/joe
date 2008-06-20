package sos.scheduler.editor.conf.listeners;


import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.xpath.XPath;
//import org.jdom.xpath.XPath;
//import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;


public class JobChainNestedListener {

	private    SchedulerDom              _dom                 = null;

	private    Element                   _config              = null;

	private    Element                   _chain               = null;

	private    Element                   _node                = null;

	private    Element                   _source              = null;

	private    String[]                  _states              = null;


	public JobChainNestedListener(SchedulerDom dom, Element jobChain) {
		_dom = dom;
		_chain = jobChain;		
		if(_chain.getParentElement() != null)
			_config = _chain.getParentElement().getParentElement();
	}

	public String getChainName() {
		return Utils.getAttributeValue("name", _chain);
	}

	public void setChainName(String name) {
		Utils.setAttribute("name", name, _chain);
		_dom.setChanged(true);
		if(_dom.isDirectory()|| _dom.isLifeElement()) _dom.setChangedForDirectory("job_chain", name, SchedulerDom.MODIFY);
	}

	
	public String getTitle() {
		return Utils.getAttributeValue("title", _chain);
	}

	public void setTitle(String title) {
		Utils.setAttribute("title", title, _chain);
		_dom.setChanged(true);
		if(_dom.isDirectory()|| _dom.isLifeElement()) _dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public Element getChain() {
		return _chain;
	}


	public boolean getRecoverable() {
		return Utils.isAttributeValue("orders_recoverable", _chain);
	}

	public void setRecoverable(boolean ordersRecoverable) {
		Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
		_dom.setChanged(true);
		if(_dom.isDirectory()|| _dom.isLifeElement()) _dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}


	public boolean getVisible() {
		return Utils.isAttributeValue("visible", _chain);
	}

	public void setVisible(boolean visible) {
		Utils.setAttribute("visible", visible, _chain);
		_dom.setChanged(true);
		if(_dom.isDirectory()|| _dom.isLifeElement()) _dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}
	
	public boolean isDistributed() {		
		return Utils.getAttributeValue("distributed", _chain).equals("yes");
	}


	public void setDistributed(boolean distributed) {		
		Utils.setAttribute("distributed", distributed, false, _chain);
	}

	
	public void applyChain(String name, boolean ordersRecoverable, boolean visible, boolean distributed, String title) {
		String oldjobChainName = Utils.getAttributeValue("name", _chain);
		if (oldjobChainName != null && oldjobChainName.length() > 0) {			
			if(_dom.isDirectory()|| _dom.isLifeElement())
				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		}
		Utils.setAttribute("name", name, _chain);
		Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
		Utils.setAttribute("visible", visible, _chain);		
		Utils.setAttribute("distributed", distributed, false, _chain);
		Utils.setAttribute("title", title, _chain);

		_dom.setChanged(true);
		if(_dom.isDirectory()|| _dom.isLifeElement()) _dom.setChangedForDirectory("job_chain", name, SchedulerDom.MODIFY);

	}

	public void fillFileOrderSource(Table table) {
		table.removeAll();
		String directory = "";
		String regex = "";		
		String next_state="";

		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_source"){
					directory = Utils.getAttributeValue("directory", node);
					regex = Utils.getAttributeValue("regex", node);					
					next_state = Utils.getAttributeValue("next_state", node);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { directory, regex,next_state});

				}

			}
		}
	}

	public void fillFileOrderSink(Table table) {
		table.removeAll();
		String state = "";
		String moveFileTo = "";
		String remove = "";

		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_sink"){
					state = Utils.getAttributeValue("state", node);
					moveFileTo = Utils.getAttributeValue("move_to", node);
					remove = Utils.getAttributeValue("remove", node);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { state, moveFileTo,remove});
				}
			}
		}
	}

	public void fillChain(Table table) {
		table.removeAll();
		String state = "";		
		String nodetype = "";
		String action = "";
		String next = "";
		String error = "";		
		String onError = "";
		if (_chain != null) {

			setStates();

			Iterator it = _chain.getChildren().iterator();

			while (it.hasNext()) {
				state = "";
				Element node = (Element) it.next();
				if (node.getName().equals("job_chain_node.job_chain") || node.getName().equals("file_order_sink")){
					state = Utils.getAttributeValue("state", node);

					if (node.getName().equals("job_chain_node.job_chain")){
						if (Utils.getAttributeValue("job_chain", node)== "") {
							nodetype = "Endnode";
						}else {
							nodetype = "Job Chain";
						}
						action = Utils.getAttributeValue("job_chain", node);
						next = Utils.getAttributeValue("next_state", node);
						error = Utils.getAttributeValue("error_state", node);
						onError = Utils.getAttributeValue("on_error", node);
					}else {
						nodetype = "FileSink";
						action = Utils.getAttributeValue("move_to", node);
						next = "";
						error = "";
						onError = "";
						if (Utils.getAttributeValue("remove", node).equals("yes")) {
							action = "Remove file";
						}
					}


					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { state, nodetype, action, next, error, onError });

					if (!next.equals("") && !checkForState(next))
						item.setBackground(3, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

					if (!error.equals("") && !checkForState(error))
						item.setBackground(4, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				}
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


	public void selectNode(Table tableNodes) {
		if (tableNodes == null){
			_node = null;
		}else {			
			int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));
			_node = (Element) _chain.getChildren().get(index);
			if (_states == null)
				setStates();
		}
	}

	public void selectFileOrderSource(Table tableSources) {
		if (tableSources == null){
			_source = null;
		}else {
			int index = getIndexOfSource(tableSources.getItem(tableSources.getSelectionIndex()));
			_source = (Element) _chain.getChildren("file_order_source").get(index);
		}
	}    



	public boolean isFullNode() {
		if (_node != null)
			return _node.getAttributeValue("job_chain") != null;		
		else
			return true;
	}

	public boolean isFileSinkNode() {
		if (_node != null)
			return (_node.getAttributeValue("remove") != null || _node.getAttributeValue("move_to") != null) ;
		else
			return true;
	}


	public String getFileOrderSource(String a) {
		return Utils.getAttributeValue(a, _source);
	}



	public String getState() {
		return Utils.getAttributeValue("state", _node);
	}

	public String getDelay() {
		return Utils.getAttributeValue("delay", _node);
	}


	public void setState(String state) {
		Utils.setAttribute("state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public void setDelay(String delay) {
		Utils.setAttribute("delay", delay, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}


	public String getJobChain() {
		return Utils.getAttributeValue("job_chain", _node);
	}


	public void setJobChain(String jobChain) {
		Utils.setAttribute("job_chain", jobChain, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}


	public String getNextState() {
		return Utils.getAttributeValue("next_state", _node);
	}


	public void setNextState(String state) {
		Utils.setAttribute("next_state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}


	public String getErrorState() {
		return Utils.getAttributeValue("error_state", _node);
	}

	public void setErrorState(String state) {
		Utils.setAttribute("error_state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String getMoveTo() {
		return Utils.getAttributeValue("move_to", _node);
	}

	public boolean getRemoveFile() {
		return Utils.getAttributeValue("remove", _node).equals("yes");
	}

	
	public void applyNode(boolean isJobchainNode,
			String state, 
			String job, 
			//String delay, 
			String next, 
			String error 
			//,boolean removeFile,
			//String moveTo
			//,String onError
			) {
		Element node = null;

		if (_node != null) {//Wenn der Knotentyp geändert wird, alten löschen und einen neuen anlegen.
			if (isJobchainNode && _node.getName().equals("file_order_sink")){
				_node.detach();
				_node = null;
			}
			if (!isJobchainNode && _node.getName().equals("job_chain_node.job_chain")){
				_node.detach();
				_node = null;
			}
		}

		if (_node != null) {
			if (isJobchainNode) {
				Utils.setAttribute("state", state, _node, _dom);
				Utils.setAttribute("job_chain", job, _node, _dom);
				//Utils.setAttribute("delay", delay, _node, _dom);
				Utils.setAttribute("next_state", next, _node, _dom);
				Utils.setAttribute("error_state", error, _node, _dom);
				//Utils.setAttribute("on_error", onError, _node, _dom);
			}else {
				Utils.setAttribute("state", state, _node, _dom);
				//Utils.setAttribute("move_to", moveTo, _node, _dom);
				//Utils.setAttribute("remove", removeFile, _node, _dom);
			}
		} else {
			if (isJobchainNode) {
				node = new Element("job_chain_node.job_chain");
				Utils.setAttribute("state", state, node, _dom);
				Utils.setAttribute("job_chain", job, node, _dom);
				//Utils.setAttribute("delay", delay, node, _dom);
				Utils.setAttribute("next_state", next, node, _dom);
				Utils.setAttribute("error_state", error, node, _dom);
				//Utils.setAttribute("on_error", onError, node, _dom);
			}else {
				node = new Element("file_order_sink");
				Utils.setAttribute("state", state, node, _dom);
				//Utils.setAttribute("move_to", moveTo, node, _dom);
				//Utils.setAttribute("remove", removeFile, node, _dom);
			}

			_chain.addContent(node);
			_node = node;

		}


		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
	}

	public void changeUp(Table table, 
			boolean up, 
			boolean isJobchainNode ,
			String state, 
			String job, 
			String delay, 
			String next, 
			String error, 
			//boolean removeFile,
			//String moveTo, 
			int index ) {
		try {
			Element node = null;

			if (_node != null) {//Wenn der Knotentyp geändert wird, alten löschen und einen neuen anlegen.

				if (isJobchainNode && _node.getName().equals("file_order_sink")){

					_node.detach();
					_node = null;
				}
				if (!isJobchainNode && _node.getName().equals("job_chain_node.job_chain")){

					_node.detach();
					_node = null;
				}
			}

			if (_node != null) {
				if (isJobchainNode) {				
					Utils.setAttribute("state", state, _node, _dom);
					Utils.setAttribute("job_chain", job, _node, _dom);
					Utils.setAttribute("delay", delay, _node, _dom);
					Utils.setAttribute("next_state", next, _node, _dom);
					Utils.setAttribute("error_state", error, _node, _dom);
				}else {				
					Utils.setAttribute("state", state, _node, _dom);
					//Utils.setAttribute("move_to", moveTo, _node, _dom);
					//Utils.setAttribute("remove", removeFile, _node, _dom);
				}
			} else {
				if (isJobchainNode) {
					node = new Element("job_chain_node.job_chain");
					Utils.setAttribute("state", state, node, _dom);
					Utils.setAttribute("job_chain", job, node, _dom);
					Utils.setAttribute("delay", delay, node, _dom);
					Utils.setAttribute("next_state", next, node, _dom);
					Utils.setAttribute("error_state", error, node, _dom);
				}else {								
					node = new Element("file_order_sink");
					Utils.setAttribute("state", state, node, _dom);
					//Utils.setAttribute("move_to", moveTo, node, _dom);
					//Utils.setAttribute("remove", removeFile, node, _dom);
				}

			}
			List l = _chain.getContent();
			int cIndex =-1;
			boolean found = false;//Hilfsvariabkle für down
			for(int i =0; i < _chain.getContentSize(); i++) {
				if(l.get(i) instanceof Element) {				
					Element elem_ = (Element)l.get(i);
					String elemState = Utils.getAttributeValue("state", elem_);
					String nodeState = Utils.getAttributeValue("state", _node);
					if(up) {
						//up				
						if(elemState.equals(nodeState)) {
							break;
						} else {
							cIndex = i;					
							if(cIndex == -1)
								cIndex = 0;//up

						}
					} else {
						//down
						if(elem_.equals(_node)) {
							found = true;
						} else if(found) {
							cIndex = i;
							break;
						}
					}
				}
			}
			node = (Element)_node.clone();		

			if(_chain.getChildren().contains(_node)) {
				_chain.removeContent(_node);						
			}
			_chain.addContent(cIndex, node);
			_node = node;


			_dom.setChanged(true);
			_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);

			setStates();
			fillChain(table);
			if(up)
				table.setSelection(index-1);
			else
				table.setSelection(index+1);
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}

			sos.scheduler.editor.app.MainWindow.message(e.getMessage(), SWT.ICON_INFORMATION);
		}
	}



	public void applyFileOrderSource(String directory, String regex, String next_state, String max, String repeat, String delay_after_error) {
		Element source = null;
		if (_source != null) {
			Utils.setAttribute("directory", directory, _source, _dom);
			Utils.setAttribute("regex", regex, _source, _dom);
			Utils.setAttribute("next_state", next_state, _source, _dom);
			Utils.setAttribute("max", max, _source, _dom);
			Utils.setAttribute("repeat", repeat, _source, _dom);
			Utils.setAttribute("delay_after_error", delay_after_error, _source, _dom);
		} else {
			source = new Element("file_order_source");
			Utils.setAttribute("directory", directory, source, _dom);
			Utils.setAttribute("regex", regex, source, _dom);
			Utils.setAttribute("next_state", next_state, source, _dom);
			Utils.setAttribute("max", max, source, _dom);
			Utils.setAttribute("repeat", repeat, source, _dom);
			Utils.setAttribute("delay_after_error", delay_after_error, source, _dom);
			_chain.addContent(source);
			_source = source;
		}
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);

	}




	private int getIndexOfNode(TableItem item) {
		int index = 0;
		if (_chain != null) {

			Iterator it = _chain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if ( Utils.getAttributeValue("state", node).equals(item.getText(0))) {
					index = i;
				}
				i = i+1;
			}
		}		
		return index;
	}

	private int getIndexOfSource(TableItem item) {
		int index = 0;
		if (_chain != null) {

			Iterator it = _chain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_source") {

					if  (Utils.getAttributeValue("directory", node)==item.getText(0) &&
							Utils.getAttributeValue("regex",node)==item.getText(1)) {
						index = i;
					}
					i = i+1;
				}
			}
		}
		return index;
	}



	public void deleteNode(Table tableNodes) {
		List nodes = _chain.getChildren();
		int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));
		nodes.remove(index);
		_node = null;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
	}


	public void deleteFileOrderSource(Table tableSource) {
		List sources = _chain.getChildren("file_order_source");
		int index = getIndexOfSource(tableSource.getItem(tableSource.getSelectionIndex()));
		sources.remove(index);
		_source = null;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}



	public String[] getJobChains() {
		
		java.util.ArrayList list = new java.util.ArrayList();
		
		if(_config == null)
			return new String[0];
		try {
			XPath x3 = XPath.newInstance("//job_chain");				 
			List listOfElement = x3.selectNodes(_dom.getDoc());
			
			for(int i = 0; i < listOfElement.size(); i++ ) {
				Element e = (Element)listOfElement.get(i);
				if(e.getChild("job_chain_node.job_chain") == null && 
						!Utils.getAttributeValue("name", e).equalsIgnoreCase(getChainName())) {
					list.add(Utils.getAttributeValue("name", e));
				}					
			}
			String[] names = new String[list.size()];
			for(int i = 0; i < list.size(); i++)
				names[i] = list.get(i) != null ? list.get(i).toString() : "";
			return names;
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}

			return new String[0];
		}
	}


	private void setStates() {
		List nodes = _chain.getChildren("job_chain_node.job_chain");
		List sinks = _chain.getChildren("file_order_sink");
		Iterator it = nodes.iterator();
		_states = new String[sinks.size() + nodes.size()];
		int index = 0;
		while (it.hasNext()) {
			String state = ((Element) it.next()).getAttributeValue("state");
			_states[index++] = state != null ? state : "";
		}

		it = sinks.iterator();

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


	public SchedulerDom get_dom() {
		return _dom;
	}

	public String getOnError() {
		return Utils.getAttributeValue("on_error", _node);
	}		

}
