package sos.scheduler.editor.conf.listeners;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.returncodes.JobchainListOfReturnCodeElements;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeAddOrderElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeElement;
import sos.scheduler.editor.classes.returncodes.JobchainReturnCodeNextStateElement;
import sos.util.SOSString;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainListener {
	private SchedulerDom		_dom			= null;
	private Element				_config			= null;
	private Element				_chain			= null;
	private Element				_node			= null;
	private Element				_source			= null;
	private String[]			_states			= null;
	private final SOSString		sosString		= new SOSString();
	private ISchedulerUpdate	update			= null;
	private ArrayList			listOfAllState	= null;
	private Namespace namespace;	
	//Contains the on_return_codes for the selected current node.
    private JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements = null;
 
	public JobChainListener(final SchedulerDom dom, final Element jobChain) {
	    
	    namespace = Namespace.getNamespace("https://jobscheduler-plugins.sos-berlin.com/NodeOrderPlugin");

		_dom = dom;
        _chain = jobChain;

         jobchainListOfReturnCodeElements = new JobchainListOfReturnCodeElements();
		if (_chain.getParentElement() != null)
			_config = _chain.getParentElement().getParentElement();
	}

	public String getChainName() {
		return Utils.getAttributeValue("name", _chain);
	}

	public void setChainName(final String name) {
		_dom.setChanged(true);
		String oldjobChainName = Utils.getAttributeValue("name", _chain);
		//Für job_chain node Parameter
		if (_chain != null && _dom.getFilename() != null) {
			org.eclipse.swt.custom.CTabItem currentTab = MainWindow.getContainer().getCurrentTab();
			String path = _dom.isDirectory() ? _dom.getFilename() : new java.io.File(_dom.getFilename()).getParent();
			try {
				if (currentTab.getData("details_parameter") != null) {
					HashMap h = new HashMap();
					h = (HashMap) currentTab.getData("details_parameter");
					if (!h.containsKey(_chain)) {
						h.put(_chain, new java.io.File(path, oldjobChainName + ".config.xml").getCanonicalPath());
					}
				}
				else {
					HashMap h = new HashMap();
					h.put(_chain, new java.io.File(path, oldjobChainName + ".config.xml").getCanonicalPath());
					currentTab.setData("details_parameter", h);
				}
				//für das Speicher per FTP
				String filename = _dom.isLifeElement() ? new File(_dom.getFilename()).getParent() : _dom.getFilename();
				currentTab.setData("ftp_details_parameter_file", filename + "/" + name + ".config.xml");
				if (oldjobChainName != null && oldjobChainName.length() > 0 && new File(filename + "/" + oldjobChainName + ".config.xml").exists()) {
					currentTab.setData("ftp_details_parameter_remove_file", oldjobChainName + ".config.xml");
				}
			}
			catch (Exception e) {
				System.out.println("error in setChainName, cause: " + e.toString());
			}
		}
		if (oldjobChainName != null && oldjobChainName.length() > 0) {
			if (_dom.isChanged() && (_dom.isDirectory() && !Utils.existName(oldjobChainName, _chain, "job_chain") || _dom.isLifeElement())) {
				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
				//_dom.setChangedForDirectory("job_chain", oldjobChainName + ".config.xml" , SchedulerDom.DELETE);
			}
		}
		Utils.setAttribute("name", name, _chain);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", name, SchedulerDom.MODIFY);
	}

	public String getTitle() {
		return Utils.getAttributeValue("title", _chain);
	}

	public void setTitle(final String title) {
		Utils.setAttribute("title", title, _chain);
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public int getMaxOrders() {
		int i;
		try {
			i = Integer.parseInt(Utils.getAttributeValue("max_orders", _chain));
		}
		catch (NumberFormatException e) {
			i = 0;
		}
		return i;
	}

	public void setMaxorders(final int maxOrder) {
		if (maxOrder == 0) {
			_chain.removeAttribute("max_orders");
		}
		else {
			Utils.setAttribute("max_orders", maxOrder, _chain);
		}
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public Element getChain() {
		return _chain;
	}

	public boolean getRecoverable() {
		return Utils.isAttributeValue("orders_recoverable", _chain);
	}

	public void setRecoverable(final boolean ordersRecoverable) {
		Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public boolean getVisible() {
		return Utils.isAttributeValue("visible", _chain);
	}

	public void setVisible(final boolean visible) {
		Utils.setAttribute("visible", visible, _chain);
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public boolean isDistributed() {
		return Utils.getAttributeValue("distributed", _chain).equals("yes");
	}

	public void setDistributed(final boolean distributed) {
		Utils.setAttribute("distributed", distributed, false, _chain);
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", getChainName(), SchedulerDom.MODIFY);
	}

	public void applyChain(final String name, final boolean ordersRecoverable, final boolean visible, final boolean distributed, final String title) {
		String oldjobChainName = Utils.getAttributeValue("name", _chain);
		if (oldjobChainName != null && oldjobChainName.length() > 0) {
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		}
		Utils.setAttribute("name", name, _chain);
		Utils.setAttribute("orders_recoverable", ordersRecoverable, _chain);
		Utils.setAttribute("visible", visible, _chain);
		Utils.setAttribute("distributed", distributed, false, _chain);
		Utils.setAttribute("title", title, _chain);
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job_chain", name, SchedulerDom.MODIFY);
	}

	public void fillFileOrderSource(final Table table) {
		table.removeAll();
		String directory = "";
		String regex = "";
		String next_state = "";
		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_source") {
					directory = Utils.getAttributeValue("directory", node);
					regex = Utils.getAttributeValue("regex", node);
					next_state = Utils.getAttributeValue("next_state", node);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { directory, regex, next_state });
				}
			}
		}
	}

	public void fillFileOrderSink(final Table table) {
		table.removeAll();
		String state = "";
		String moveFileTo = "";
		String remove = "";
		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_sink") {
					state = Utils.getAttributeValue("state", node);
					moveFileTo = Utils.getAttributeValue("move_to", node);
					remove = Utils.getAttributeValue("remove", node);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { state, moveFileTo, remove });
				}
			}
		}
	}

    public void setJobchainListOfReturnCodeElements(JobchainListOfReturnCodeElements jobchainListOfReturnCodeElements) {
        this.jobchainListOfReturnCodeElements = jobchainListOfReturnCodeElements;
    }
    
    public JobchainListOfReturnCodeElements getJobchainListOfReturnCodeElements() {
        return jobchainListOfReturnCodeElements;
    }
    
    public void fillChain(final Table table) {
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
				if (node.getName().equals("job_chain_node") || node.getName().equals("job_chain_node.end") || node.getName().equals("file_order_sink")) {
					state = Utils.getAttributeValue("state", node);
					if (node.getName().equals("job_chain_node")) {
						if (Utils.getAttributeValue("job", node) == "") {
							nodetype = "Endnode";
							node.removeAttribute("next_state");//kann eventuell bei reorder changeup entstanden sein
						}
						else {
							nodetype = "Job";
						}
						action = Utils.getAttributeValue("job", node);
						next = Utils.getAttributeValue("next_state", node);
						error = Utils.getAttributeValue("error_state", node);
						onError = Utils.getAttributeValue("on_error", node);
					}
					else
						if (node.getName().equals("job_chain_node.end")) {
							nodetype = "Endnode";
							action = Utils.getAttributeValue("job", node);
							next = Utils.getAttributeValue("next_state", node);
							error = Utils.getAttributeValue("error_state", node);
							onError = Utils.getAttributeValue("on_error", node);
						}
						else {
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
					
					if (sos.scheduler.editor.conf.listeners.DetailsListener.existDetailsParameter(state, Utils.getAttributeValue("name", _chain), action, _dom,	update, false, null)){
						item.setBackground(Options.getLightBlueColor());
					}else{
						item.setBackground(null);
					}
					item.setText(new String[] { state, nodetype, action, next, error, onError });
					if (!next.equals("") && !checkForState(next)){
						item.setBackground(3, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
					}
					if (!error.equals("") && !checkForState(error)){
						item.setBackground(4, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
					}
				}
			}
		}
	}

	public boolean checkForState(final String state) {
		for (String _state : _states) {
			if (_state.equals(state))
				return true;
		}
		return false;
	}

    public org.w3c.dom.Element convertToDOM(org.jdom.Element jdomElement) throws JDOMException {
       DOMOutputter outputter = new DOMOutputter();
       jdomElement.detach();
       org.jdom.Document jdomD = new org.jdom.Document(jdomElement);       
       org.w3c.dom.Document w3cD = outputter.output(jdomD);
       return w3cD.getDocumentElement();
    }

	private JobchainReturnCodeAddOrderElement addOrderParameters(Element addOrderParams, JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement){
// This is neccessary because
//            List<Element> listOfParams = addOrderParams.getChildren("param",addOrder.getNamespace());
// does not work within a namespace
	    
	    
        if (addOrderParams != null){
            org.w3c.dom.Element params;
            try {
                params = convertToDOM(addOrderParams);
             
                NodeList n = params.getElementsByTagNameNS(addOrderParams.getNamespace().getURI(), "param");
            
                for (int i = 0; i < n.getLength(); i++) {
                
                    Node currentNode = n.item(i);
                    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element e = (org.w3c.dom.Element)currentNode;
                        jobchainReturnCodeAddOrderElement.addParam(e.getAttribute("name"),  e.getAttribute("value"));
                    }
                }
             
            } catch (JDOMException e1) {
                e1.printStackTrace();
            }            
        }
	    return jobchainReturnCodeAddOrderElement;
	    
	}
	
	private void fillOnReturnCodes(Element node){
	    List<Element> listOfonReturnCodes = null;

	    if (jobchainListOfReturnCodeElements.size() == 0){
    	    Element onReturnCodes = _node.getChild("on_return_codes");
    	    if (onReturnCodes != null){
        	    listOfonReturnCodes = onReturnCodes.getChildren("on_return_code"); 
              
        	    Iterator<Element> it = listOfonReturnCodes.iterator();
                while (it.hasNext()) {
                    Element returnCode =  it.next();
                    Element toState = returnCode.getChild("to_state");
                    Element addOrder = returnCode.getChild("add_order",namespace);
                    String returnCodeValue = Utils.getAttributeValue("return_code", returnCode);
                    if (toState != null){
                        JobchainReturnCodeNextStateElement jobchainReturnCodeNextStateElement = new JobchainReturnCodeNextStateElement();
                        jobchainReturnCodeNextStateElement.setReturnCodes(returnCodeValue);
                        jobchainReturnCodeNextStateElement.setNextState(Utils.getAttributeValue("state", toState));
                        jobchainListOfReturnCodeElements.add(jobchainReturnCodeNextStateElement);
                    }
                    if (addOrder != null){
                        JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = new JobchainReturnCodeAddOrderElement();
                        jobchainReturnCodeAddOrderElement.setReturnCodes(returnCodeValue);
                        jobchainReturnCodeAddOrderElement.setJobChain(Utils.getAttributeValue("job_chain", addOrder)); 
                        jobchainReturnCodeAddOrderElement.setOrderId(Utils.getAttributeValue("order_id", addOrder)); 
                                               
                        Element addOrderParams = addOrder.getChild("params",addOrder.getNamespace());
                        jobchainReturnCodeAddOrderElement = addOrderParameters(addOrderParams, jobchainReturnCodeAddOrderElement);
  
                        jobchainListOfReturnCodeElements.add(jobchainReturnCodeAddOrderElement);
                    }
                }
               
             }
	    }
	}
	
	public void selectNode(final Table tableNodes) {
		if (tableNodes == null) {
			_node = null;
		}
		else {
			int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));

          
	          _node = (Element) _chain.getChildren().get(index);
	        
			fillOnReturnCodes(_node);
			if (_states == null)
				setStates();
		}
	}

	public void selectFileOrderSource(final Table tableSources) {
		if (tableSources == null) {
			_source = null;
		}
		else {
			int index = getIndexOfSource(tableSources.getItem(tableSources.getSelectionIndex()));
			_source = (Element) _chain.getChildren("file_order_source").get(index);
		}
	}

	public boolean isFullNode() {
		if (_node != null)
			return _node.getAttributeValue("job") != null;
		else
			return true;
	}

	public boolean isFileSinkNode() {
		if (_node != null)
			return _node.getAttributeValue("remove") != null || _node.getAttributeValue("move_to") != null;
		else
			return true;
	}

	public String getFileOrderSource(final String a) {
		return Utils.getAttributeValue(a, _source);
	}

	public String getState() {
		return Utils.getAttributeValue("state", _node);
	}

	public String getDelay() {
		return Utils.getAttributeValue("delay", _node);
	}

	public void setState(final String state) {
		Utils.setAttribute("state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public void setDelay(final String delay) {
		Utils.setAttribute("delay", delay, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String getJob() {
		return Utils.getAttributeValue("job", _node);
	}

	public void setJob(final String job) {
		Utils.setAttribute("job", job, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String getNextState() {
		return Utils.getAttributeValue("next_state", _node);
	}

	public void setNextState(final String state) {
		Utils.setAttribute("next_state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String getErrorState() {
		return Utils.getAttributeValue("error_state", _node);
	}

	public void setErrorState(final String state) {
		Utils.setAttribute("error_state", state, _node, _dom);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String getMoveTo() {
		return Utils.getAttributeValue("move_to", _node);
	}

	public boolean getRemoveFile() {
		return Utils.getAttributeValue("remove", _node).equals("yes");
	}

	private Element getOnReturnCodes(){
	    
        
	    Element onReturnCodes  = null;
	    
	    if (jobchainListOfReturnCodeElements.size() > 0){
	        onReturnCodes = new Element("on_return_codes");
   
	        jobchainListOfReturnCodeElements.reset();
        
    	     while (jobchainListOfReturnCodeElements.hasNext()) {
                JobchainReturnCodeElement jobchainReturnCodeElement = jobchainListOfReturnCodeElements.getNext();
                Element on_return_code  = new Element("on_return_code");
                onReturnCodes.addContent(on_return_code);
                if (jobchainReturnCodeElement.getJobchainListOfReturnCodeAddOrderElements() != null){
                    while(jobchainReturnCodeElement.getJobchainListOfReturnCodeAddOrderElements().hasNext()){
                        JobchainReturnCodeAddOrderElement jobchainReturnCodeAddOrderElement = jobchainReturnCodeElement.getJobchainListOfReturnCodeAddOrderElements().getNext();
                        Element add_order  = new Element("add_order",namespace);
                        Utils.setAttribute("job_chain", jobchainReturnCodeAddOrderElement.getJobChain(), add_order, _dom);
                        Utils.setAttribute("order_id", jobchainReturnCodeAddOrderElement.getOrderId(), add_order, _dom);
                        
                        if (jobchainReturnCodeAddOrderElement.getParams().size() > 0){
                            Element params  = new Element("params",namespace);
                            for (Entry<String, String> entry : jobchainReturnCodeAddOrderElement.getParams().entrySet()) {
                                Element param  = new Element("param",namespace);
                                Utils.setAttribute("name", entry.getKey(), param, _dom);
                                Utils.setAttribute("value", entry.getValue(), param, _dom);
                                params.addContent(param);
                            }
                            add_order.addContent(params);
                        }
                        on_return_code.addContent(add_order);
                    }
                }
                if (jobchainReturnCodeElement.getJobchainReturnCodeNextStateElement() != null){
                    Element to_state  = new Element("to_state");
                    Utils.setAttribute("state", jobchainReturnCodeElement.getJobchainReturnCodeNextStateElement().getNextState(), to_state, _dom);
                    on_return_code.addContent(to_state);
                }

                Utils.setAttribute("return_code", jobchainReturnCodeElement.getReturnCodes(), on_return_code, _dom);
            } 
	    }
      return onReturnCodes;         
	}
	
	public void applyNode(final boolean isJobchainNode, final String state, final String job, final String delay, final String next, final String error,
			final boolean removeFile, final String moveTo, final String onError) throws Exception {
		try {
			Element node = null;
            Element onReturnCodes = getOnReturnCodes();

            if (_node != null) {//Wenn der Knotentyp geändert wird, alten löschen und einen neuen anlegen.
				String oldState = Utils.getAttributeValue("state", _node);
				if (oldState != null && state != null && !oldState.equals(state)) {
					//state hat sicg geändert. ggf die Details state auch ändern
					DetailsListener.changeDetailsState(oldState, state, Utils.getAttributeValue("name", _chain), _dom);
				}
				if (isJobchainNode && _node.getName().equals("file_order_sink")) {
					_node.detach();
					_node = null;
				}
				if (!isJobchainNode && _node.getName().equals("job_chain_node")) {
					_node.detach();
					_node = null;
				}
			}
			if (_node != null) {
				if (isJobchainNode) {
					Utils.setAttribute("state", state, _node, _dom);
					Utils.setAttribute("job", job, _node, _dom);
					Utils.setAttribute("delay", delay, _node, _dom);
					Utils.setAttribute("next_state", next, _node, _dom);
					Utils.setAttribute("error_state", error, _node, _dom);
					Utils.setAttribute("on_error", onError, _node, _dom);
					if (onReturnCodes != null){
					    Element e = _node.getChild("on_return_codes");
	                    if (e != null){
	                        e.detach();
	                     }
      			       _node.addContent(onReturnCodes);
					}
					
				}
				else {
					Utils.setAttribute("state", state, _node, _dom);
					Utils.setAttribute("move_to", moveTo, _node, _dom);
					Utils.setAttribute("remove", removeFile, _node, _dom);
				}
			}
			else {
				if (isJobchainNode) {
					node = new Element("job_chain_node");
					Utils.setAttribute("state", state, node, _dom);
					Utils.setAttribute("job", job, node, _dom);
					Utils.setAttribute("delay", delay, node, _dom);
					Utils.setAttribute("next_state", next, node, _dom);
					Utils.setAttribute("error_state", error, node, _dom);
					Utils.setAttribute("on_error", onError, node, _dom);
				}
				else {
					node = new Element("file_order_sink");
					Utils.setAttribute("state", state, node, _dom);
					Utils.setAttribute("move_to", moveTo, node, _dom);
					Utils.setAttribute("remove", removeFile, node, _dom);
				}
				
                if (onReturnCodes != null){
                    Element e = _node.getChild("on_return_codes");
                    if (e != null){
                       e.detach();
                    }
                    node.addContent(onReturnCodes);
                }

                
				_chain.addContent(node);
				_node = node;
			}
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
			setStates();
		}
		catch (Exception e) {
			throw new Exception("error in JobChainListener.applyNode. Could not save Node, cause: " + e.toString());
		}
	}

	public void applyInsertNode(final boolean isJobchainNode, final String state, final String job, final String delay, final String next, final String error,
			final boolean removeFile, final String moveTo, final String onError) {
		Element node = null;
        Element onReturnCodes = getOnReturnCodes();
        
		if (isJobchainNode) {
			node = new Element("job_chain_node");
			Utils.setAttribute("state", state, node, _dom);
			Utils.setAttribute("job", job, node, _dom);
			Utils.setAttribute("delay", delay, node, _dom);
			Utils.setAttribute("next_state", next, node, _dom);
			Utils.setAttribute("error_state", error, node, _dom);
			Utils.setAttribute("on_error", onError, node, _dom);
		}
		else {
			node = new Element("file_order_sink");
			Utils.setAttribute("state", state, node, _dom);
			Utils.setAttribute("move_to", moveTo, node, _dom);
			Utils.setAttribute("remove", removeFile, node, _dom);
		}
		boolean found = false;
		List list = _chain.getChildren();
		if (list.size() > 0 && _node != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(_node)) {
					if (i > 0) {
						Element previosNode = (Element) list.get(i - 1);
						Utils.setAttribute("next_state", state, previosNode, _dom);
						_chain.addContent(_chain.indexOf(previosNode) + 1, node);
						found = true;
						break;
					}
				}
			}
		}
		
        if (onReturnCodes != null){
            node.addContent(onReturnCodes);
        }

		if (!found) {
			_chain.addContent(0, node);
		}
		_node = node;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
	}

	public void changeUp(final Table table, final boolean up, final boolean isJobchainNode, final String state, final String job, final String delay,
			final String next, final String error, final boolean removeFile, final String moveTo, final int index, final boolean reorder) {
		try {
			Element node = null;
			if (reorder) {
				String msg = "The node " + job + " is an Endnode and therefore cannot be changed with Node in case reorder is activated";
				if (Utils.getAttributeValue("job", _node).length() == 0 || _node != null && _node.getName().equals("job_chain_node.end")) {
					//sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
					MainWindow.message(msg, SWT.ICON_INFORMATION);
					return;
				}
				//der Austausch darf nicht mit einem EndNode stattfinden			
				if (up) {
					if (table.getSelectionIndex() > 0 && table.getItem(table.getSelectionIndex() - 1).getText(1).equals("Endnode")) {
						//sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						sos.scheduler.editor.app.MainWindow.message(msg, SWT.ICON_INFORMATION);
						return;
					}
				}
				else {
					if (table.getSelectionIndex() < table.getItemCount() - 1 && table.getItem(table.getSelectionIndex() + 1).getText(1).equals("Endnode")) {
						//sos.scheduler.editor.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						sos.scheduler.editor.app.MainWindow.message(msg, SWT.ICON_INFORMATION);
						return;
					}
				}
			}
			List l = _chain.getContent();
			int cIndex = -1;
			boolean found = false;//Hilfsvariabkle für down
			for (int i = 0; i < _chain.getContentSize(); i++) {
				if (l.get(i) instanceof Element) {
					Element elem_ = (Element) l.get(i);
					//String elemState = Utils.getAttributeValue("state", elem_);
					//String nodeState = Utils.getAttributeValue("state", _node);
					if (up) {
						//up				
						//if(elemState.equals(nodeState)) {
						if (elem_.equals(_node)) {
							//System.out.println("up " + elemState + "=" + nodeState + "    elem_.equals(_node) " +elem_.equals(_node) );							
							break;
						}
						else {
							cIndex = i;
							if (cIndex == -1)
								cIndex = 0;//up
						}
					}
					else {
						//down
						if (elem_.equals(_node)) {
							//System.out.println("down " + elemState + "=" + nodeState + "    elem_.equals(_node) " +elem_.equals(_node) );
							found = true;
						}
						else
							if (found) {
								cIndex = i;
								break;
							}
					}
				}
			}
			node = (Element) _node.clone();
			if (reorder) {
				Filter elementFilter2 = new ElementFilter("job_chain_node", getChain().getNamespace());
				// gets all element nodes under the rootElement
				List elements = getChain().getContent(elementFilter2);
				// cycle through all immediate elements under the rootElement
				//for( java.util.Iterator it = elements.iterator(); it.hasNext(); ) {
				int size = elements.size();
				for (int i = 0; i < size; ++i) {
					// note that this is a downcast because we
					// have used the element filter.  This would
					// not be the case for a getContents() on the element
					//Element currElement = (Element) it.next();
					Element currElement = (Element) elements.get(i);
					Element prevElement = null;
					Element prev2Element = null;
					Element nextElement = null;
					//String prev2State    = "";
					String prevState = "";
					String curState = "";
					String currNextState = "";
					//String nextState     = "";//only for down
					String nextNextState = "";
					if (currElement.equals(_node)) {
						if (i >= 2) {
							prev2Element = (Element) elements.get(i - 2);
							//prev2State   =  Utils.getAttributeValue("state", prev2Element);
							//System.out.println("previous   Datensatz: \t\t" + prev2State);							
						}
						if (i >= 1) {
							prevElement = (Element) elements.get(i - 1);
							prevState = Utils.getAttributeValue("state", prevElement);
							//System.out.println("2 previous Datensatz: \t\t" + prevState);
						}
						if (size > i + 1) {
							nextElement = (Element) elements.get(i + 1);
							//nextElement =  (Element)elements.get(i);
							//nextState   =  Utils.getAttributeValue("state", nextElement);
							nextNextState = Utils.getAttributeValue("next_state", nextElement);
							//System.out.println("     next Datensatz: \t\t" + nextState);
						}
						curState = Utils.getAttributeValue("state", currElement);
						currNextState = Utils.getAttributeValue("next_state", currElement);
						//System.out.println(    "selektier  Datensatz: \t\t" + curState);
						if (up) {
							if (prev2Element != null && sosString.parseToString(curState).length() > 0) {
								Utils.setAttribute("next_state", curState, prev2Element);
							}
							if (prevElement != null && sosString.parseToString(currNextState).length() > 0) {
								Utils.setAttribute("next_state", currNextState, prevElement);
							}
							if (curState != null && sosString.parseToString(prevState).length() > 0) {
								Utils.setAttribute("next_state", prevState, currElement);
								Utils.setAttribute("next_state", prevState, node);
							}
						}
						else {
							//up							
							if (prevElement != null && sosString.parseToString(currNextState).length() > 0) {
								Utils.setAttribute("next_state", currNextState, prevElement);
							}
							if (nextElement != null) {
								Utils.setAttribute("next_state", curState, nextElement);
							}
							if (curState != null && sosString.parseToString(nextNextState).length() > 0) {
								Utils.setAttribute("next_state", nextNextState, currElement);
								Utils.setAttribute("next_state", nextNextState, node);
								Utils.setAttribute("next_state", nextNextState, _node);
							}
						}
						break;
					}
					//System.out.println( currElement );
				}
			}
			if (_chain.getChildren().contains(_node)) {
				_chain.removeContent(_node);
			}
			_chain.addContent(cIndex, node);
			_node = node;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
			setStates();
			fillChain(table);
			if (up)
				table.setSelection(index - 1);
			else
				table.setSelection(index + 1);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				//tu nichts
			}
			sos.scheduler.editor.app.MainWindow.message(e.getMessage(), SWT.ICON_INFORMATION);
		}
	}

	public void applyFileOrderSource(final String directory, final String regex, final String next_state, final String max, final String repeat,
			final String delay_after_error) {
		Element source = null;
		if (_source != null) {
			Utils.setAttribute("directory", directory, _source, _dom);
			Utils.setAttribute("regex", regex, _source, _dom);
			Utils.setAttribute("next_state", next_state, _source, _dom);
			Utils.setAttribute("max", max, _source, _dom);
			Utils.setAttribute("repeat", repeat, _source, _dom);
			Utils.setAttribute("delay_after_error", delay_after_error, _source, _dom);
		}
		else {
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

	private int getIndexOfNode(final TableItem item) {
		int index = 0;
		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (Utils.getAttributeValue("state", node).equals(item.getText(0))) {
					index = i;
				}
				i = i + 1;
			}
		}
		return index;
	}

	private int getIndexOfSource(final TableItem item) {
		int index = 0;
		if (_chain != null) {
			Iterator it = _chain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (node.getName() == "file_order_source") {
					if (Utils.getAttributeValue("directory", node) == item.getText(0) && Utils.getAttributeValue("regex", node) == item.getText(1)) {
						index = i;
					}
					i = i + 1;
				}
			}
		}
		return index;
	}

	public void deleteNode(final Table tableNodes) {
		List nodes = _chain.getChildren();
		int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));
		DetailsListener.deleteDetailsState(tableNodes.getSelection()[0].getText(0), Utils.getAttributeValue("name", _chain), _dom);
		nodes.remove(index);
		_node = null;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
		setStates();
	}

	public void deleteFileOrderSource(final Table tableSource) {
		List sources = _chain.getChildren("file_order_source");
		int index = getIndexOfSource(tableSource.getItem(tableSource.getSelectionIndex()));
		sources.remove(index);
		_source = null;
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	public String[] getJobs() {
		if (_config == null)
			return new String[0];
		Element job = _config.getChild("jobs");
		if (job != null) {
			int size = 0;
			List jobs = job.getChildren("job");
			Iterator it = jobs.iterator();
			int index = 0;
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String order_job = e.getAttributeValue("order");
				if (order_job != null && order_job.equals("yes")) {
					size = size + 1;
				}
			}
			String[] names = new String[size];
			it = jobs.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getAttributeValue("name");
				String order_job = e.getAttributeValue("order");
				if (order_job != null && order_job.equals("yes")) {
					names[index++] = name != null ? name : "";
				}
			}
			return names;
		}
		else
			return new String[0];
	}

	private void setStates() {
		List nodes = _chain.getChildren("job_chain_node");
		List sinks = _chain.getChildren("file_order_sink");
		List endNodes = _chain.getChildren("job_chain_node.end");
		Iterator it = nodes.iterator();
		_states = new String[sinks.size() + nodes.size() + endNodes.size()];
		listOfAllState = new ArrayList();
		int index = 0;
		while (it.hasNext()) {
			Element el = (Element) it.next();
			String state = el.getAttributeValue("state");
			_states[index++] = state != null ? state : "";
			if (state != null && !listOfAllState.contains(state))
				listOfAllState.add(state);
			String errorState = el.getAttributeValue("error_state");
			if (errorState != null && !listOfAllState.contains(errorState))
				listOfAllState.add(errorState);
		}
		it = sinks.iterator();
		while (it.hasNext()) {
			String state = ((Element) it.next()).getAttributeValue("state");
			_states[index++] = state != null ? state : "";
			if (state != null && !listOfAllState.contains(state))
				listOfAllState.add(state);
		}
		it = endNodes.iterator();
		while (it.hasNext()) {
			Element el = (Element) it.next();
			String state = el.getAttributeValue("state");
			_states[index++] = state != null ? state : "";
			if (state != null && !listOfAllState.contains(state))
				listOfAllState.add(state);
		}
	}

	public String[] getStates() {
		if (_states == null)
			return new String[0];
		else
			if (_node != null) {
				String state = getState();
				int index = 0;
				String[] states = new String[_states.length - 1];
				for (int i = 0; i < _states.length; i++) {
					if (!_states[i].equals(state))
						states[index++] = _states[i];
				}
				return states;
			}
			else
				return _states;
	}

	public String[] getAllStates() {
		try {
			//String[] _statesWithError = new String[listOfAllState.size()]; 
			if (listOfAllState == null)
				return new String[0];
			else { //if (_node != null) {
				String errorState = getErrorState() != null ? getErrorState() : "";
				String state = getState() != null ? getState() : "";
				int i_ = 0;
				if (state.length() > 0)
					i_++;
				if (errorState.length() > 0)
					i_++;
				int index = 0;
				if (listOfAllState.size() - i_ < -1)
					i_ = 0;
				String[] states = new String[listOfAllState.size() - i_];
				for (int i = 0; i < listOfAllState.size(); i++) {
					if (!listOfAllState.get(i).equals(state) && !listOfAllState.get(i).equals(errorState))
						states[index++] = listOfAllState.get(i) != null ? listOfAllState.get(i).toString() : "";
				}
				return states;
			} //else
				//	return new String[0];
		}
		catch (Exception e) {
			sos.scheduler.editor.app.MainWindow.message("Could not Read Error State, cause " + e.toString(), SWT.ICON_WARNING);
			return new String[0];
		}
	}

	 
	public boolean isValidState(final String state) {
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

	public void setISchedulerUpdate(final ISchedulerUpdate update_) {
		update = update_;
	}
}
