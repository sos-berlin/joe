package sos.scheduler.editor.conf.listeners;


import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.xpath.XPath;
//import org.jdom.xpath.XPath;
//import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSString;


public class JobChainNestedListener {

	private    SchedulerDom              _dom                 = null;

	private    Element                   _config              = null;

	private    Element                   _chain               = null;

	private    Element                   _node                = null;

	//private    Element                   _source              = null;

	private    String[]                  _states              = null;

	private    SOSString                 sosString            = new SOSString();	


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

	/*public void fillFileOrderSource(Table table) {
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
	 */
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

			//Iterator it = _chain.getChildren("job_chain_node.job_chain").iterator();
			Iterator it = _chain.getChildren().iterator();

			while (it.hasNext()) {

				state = "";		
				nodetype = "";
				action = "";
				next = "";
				error = "";		
				onError = "";
				Element node = (Element) it.next();
				if (node.getName().equals("job_chain_node.job_chain")){
					state = Utils.getAttributeValue("state", node);

					action = Utils.getAttributeValue("job_chain", node);
					next = Utils.getAttributeValue("next_state", node);
					error = Utils.getAttributeValue("error_state", node);
					onError = Utils.getAttributeValue("on_error", node);
					if (Utils.getAttributeValue("job_chain", node)== "") {
						nodetype = "Endnode";
					} else {
						nodetype = "Job Chain";		
					}
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { state, nodetype, action, next, error, onError });

					if (!next.equals("") && !checkForState(next))
						item.setBackground(3, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

					if (!error.equals("") && !checkForState(error))
						item.setBackground(4, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

				} else if (node.getName().equals("job_chain_node.end")) {
					state = Utils.getAttributeValue("state", node);										

					TableItem item = new TableItem(table, SWT.NONE);
					//item.setText(new String[] { state, nodetype, action, next, error, onError });
					item.setText(new String[] { state, "Endnode", "", "", "", "" });
				}

				/*
			Iterator itEndNode = _chain.getChildren("job_chain_node.end").iterator();

			while (itEndNode.hasNext()) {
				Element node = (Element) itEndNode.next();
				state = Utils.getAttributeValue("state", node);										

				TableItem item = new TableItem(table, SWT.NONE);
				//item.setText(new String[] { state, nodetype, action, next, error, onError });
				item.setText(new String[] { state, "Endnode", "", "", "", "" });


			}

				 */
			}
		}
	}


	public boolean checkForState(String state) {

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

	/*public void selectFileOrderSource(Table tableSources) {
		if (tableSources == null){
			_source = null;
		}else {
			int index = getIndexOfSource(tableSources.getItem(tableSources.getSelectionIndex()));
			_source = (Element) _chain.getChildren("file_order_source").get(index);
		}
	} */   



	public boolean isFullNode() {
		if (_node != null)
			return _node.getAttributeValue("job_chain") != null;		
		else
			return true;
	}

	/*public boolean isFileSinkNode() {
		if (_node != null)
			return (_node.getAttributeValue("remove") != null || _node.getAttributeValue("move_to") != null) ;
		else
			return true;
	}


	public String getFileOrderSource(String a) {
		return Utils.getAttributeValue(a, _source);
	}
	 */



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
			String next, 
			String error, 
			boolean isFullnode
			
	) {
		Element node = null;
		
		if(_node != null) {
			
			//System.out.println("node != null, old state=" + Utils.getAttributeValue("state", _node) + ", new state=" + state);
			String oldState = Utils.getAttributeValue("state", _node);

			if(oldState != null &&  state != null &&  !oldState.equals(state)) {
				//state hat sicg geändert. ggf die Details state auch ändern
				DetailsListener.changeDetailsState(oldState, state, Utils.getAttributeValue("name", _chain), _dom);
			}

			if(isJobchainNode && _node.getName().equals("job_chain_node.end"))		{			
				_node.detach();
				_node = null;
			}
		}



		if (_node != null) {
			if (isJobchainNode) {
				Utils.setAttribute("state", state, _node, _dom);
				Utils.setAttribute("job_chain", job, _node, _dom);				
				Utils.setAttribute("next_state", next, _node, _dom);
				Utils.setAttribute("error_state", error, _node, _dom);
			}else {
				Utils.setAttribute("state", state, _node, _dom);
			}
		} else {
			
			if (isJobchainNode) {
				if(!isFullnode)
					node = new Element("job_chain_node.end");
				else {
					node = new Element("job_chain_node.job_chain");
					Utils.setAttribute("job_chain", job, node, _dom);					
					Utils.setAttribute("next_state", next, node, _dom);
					Utils.setAttribute("error_state", error, node, _dom);
				}
				Utils.setAttribute("state", state, node, _dom);

			} 
			
			_chain.addContent(node);
			_node = node;

		}

		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
		
	}


	public void applyInsertNode(String state, 
			String job, 
			String next, 
			String error,
			boolean isFullnode
	) {

		Element node = null;



		if(!isFullnode)
			node = new Element("job_chain_node.end");
		else {
			node = new Element("job_chain_node.job_chain");
			Utils.setAttribute("job_chain", job, node, _dom);
			Utils.setAttribute("next_state", next, node, _dom);
			Utils.setAttribute("error_state", error, node, _dom);
		}
		Utils.setAttribute("state", state, node, _dom);

		boolean found = false;
		List list = _chain.getChildren();
		if(list.size() > 0 && _node != null) {
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).equals(_node)){						
					if(i > 0) {
						Element previosNode = (Element)list.get(i-1);
						Utils.setAttribute("next_state", state, previosNode, _dom);
						_chain.addContent(_chain.indexOf(previosNode) +1, node);
						found = true;
						break;
					}
				}
			}
		} 

		if(!found){
			_chain.addContent(0, node);
		} 

		_node = node;

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
			int index,
			boolean isFullNode,
			boolean reorder ) {
		try {
			Element node = null;


			if(reorder) {
				if(Utils.getAttributeValue("job_chain", _node).length() == 0 ||
						(_node != null && _node.getName().equals("job_chain_node.end"))
				) {
					sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
					return;
				}


				//der Austausch darf nicht mit einem EndNode stattfinden			
				if(up) {
					if(table.getSelectionIndex() > 0 
							&& table.getItem(table.getSelectionIndex()-1).getText(1).equals("Endnode")) {
						sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						return;
					}

				} else {
					if(table.getSelectionIndex() < table.getItemCount() -1  
							&& table.getItem(table.getSelectionIndex()+1).getText(1).equals("Endnode")) {
						sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						return;
					}
				}
			}

			List l = _chain.getContent();
			int cIndex =-1;
			boolean found = false;//Hilfsvariabkle für down
			for(int i =0; i < _chain.getContentSize(); i++) {
				if(l.get(i) instanceof Element) {				
					Element elem_ = (Element)l.get(i);
					if(up) {
						if(elem_.equals(_node)) {							
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

			if(reorder) {
				Filter elementFilter2 = new ElementFilter( "job_chain_node.job_chain", getChain().getNamespace() );
				// gets all element nodes under the rootElement
				List elements = getChain().getContent( elementFilter2 );
				// cycle through all immediate elements under the rootElement
				//for( java.util.Iterator it = elements.iterator(); it.hasNext(); ) {
				int size = elements.size();
				for(int i= 0; i < size; ++i) { 
					// note that this is a downcast because we
					// have used the element filter.  This would
					// not be the case for a getContents() on the element
					//Element currElement = (Element) it.next();
					Element currElement  = (Element) elements.get(i);
					Element prevElement  = null; 
					Element prev2Element = null;
					Element nextElement  = null;

					//String prev2State    = "";
					String prevState     = "";
					String curState      = "";
					String currNextState = "";
					//String nextState     = "";//only for down
					String nextNextState  = "";

					if(currElement.equals(_node)) {

						if(i >= 2) {
							prev2Element =  (Element)elements.get(i -2);
							//prev2State   =  Utils.getAttributeValue("state", prev2Element);
							//System.out.println("previous   Datensatz: \t\t" + prev2State);							
						}

						if(i >= 1) {
							prevElement =  (Element)elements.get(i -1);
							prevState   =  Utils.getAttributeValue("state", prevElement);
							//System.out.println("2 previous Datensatz: \t\t" + prevState);

						}

						if(size > i+1) {
							nextElement =  (Element)elements.get(i+1);
							//nextElement =  (Element)elements.get(i);
							//nextState   =  Utils.getAttributeValue("state", nextElement);
							nextNextState=  Utils.getAttributeValue("next_state", nextElement);
							//System.out.println("     next Datensatz: \t\t" + nextState);

						}

						curState = Utils.getAttributeValue("state", currElement);
						currNextState = Utils.getAttributeValue("next_state", currElement);

						//System.out.println(    "selektier  Datensatz: \t\t" + curState);
						if(up) {

							if(prev2Element != null && sosString.parseToString(curState).length() > 0 ) {
								Utils.setAttribute("next_state", curState, prev2Element);
							}
							if(prevElement != null && sosString.parseToString(currNextState).length() > 0) {
								Utils.setAttribute("next_state", currNextState , prevElement);
							}
							if(curState != null  && sosString.parseToString(prevState).length() > 0) {
								Utils.setAttribute("next_state", prevState , currElement);
								Utils.setAttribute("next_state", prevState , node);								
							}

						} else {
							//up							
							if(prevElement != null && sosString.parseToString(currNextState).length() > 0) {
								Utils.setAttribute("next_state", currNextState , prevElement);
							}

							if(nextElement != null) {
								Utils.setAttribute("next_state", curState, nextElement);	
							}

							if(curState != null  && sosString.parseToString(nextNextState).length() > 0) {
								Utils.setAttribute("next_state", nextNextState , currElement);
								Utils.setAttribute("next_state", nextNextState , node);								
								Utils.setAttribute("next_state", nextNextState , _node);
							}
						}

						break;
					}

					//System.out.println( currElement );
				}
			}


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



	public void deleteNode(Table tableNodes) {
		List nodes = _chain.getChildren();
		int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));
		
		DetailsListener.deleteDetailsState(tableNodes.getSelection()[0].getText(0), Utils.getAttributeValue("name", _chain), _dom);
		nodes.remove(index);
		_node = null;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
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
		//List sinks = _chain.getChildren("file_order_sink");
		List endNodes = _chain.getChildren("job_chain_node.end");
		Iterator it = nodes.iterator();
		//_states = new String[sinks.size() + nodes.size() + endNodes.size()];
		_states = new String[nodes.size() + endNodes.size()];
		int index = 0;
		while (it.hasNext()) {
			String state = ((Element) it.next()).getAttributeValue("state");
			_states[index++] = state != null ? state : "";
		}


		it = endNodes.iterator();

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
