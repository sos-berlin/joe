package com.sos.joe.objects.jobchain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.util.SOSClassUtil;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainNestedListener extends JOEJobChainDataProvider {

	@SuppressWarnings("unused")
	private final String		conClassName	= "JobChainNestedListener";
	@SuppressWarnings("unused")
	private static final String	conSVNVersion	= "$Id$";
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(JobChainNestedListener.class);

@Deprecated	public JobChainNestedListener(final SchedulerDom dom, final Element jobChain) {
		_dom = dom;
		objJobChain = jobChain;
//		getJOMJobChain();

		if (objJobChain.getParentElement() != null) {
			_config = objJobChain.getParentElement().getParentElement();
		}
	}

	public void applyChain(final String name, final boolean ordersRecoverable, final boolean visible, final boolean distributed, final String title) {
		String oldjobChainName = getChainName();
		if (oldjobChainName != null && oldjobChainName.length() > 0) {
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		}
		setName(name);
		setOrdersRecoverable(ordersRecoverable);
		setVisible(visible);
		setDistributed(distributed);
		setTitle(title);
	}

	public void fillChain(final SOSTable table) {
		table.removeAll();
		String state = "";
		String nodetype = "";
		String action = "";
		String next = "";
		String error = "";
		String onError = "";
		if (objJobChain != null) {

			setStates();

			//Iterator it = _chain.getChildren("job_chain_node.job_chain").iterator();
			Iterator it = objJobChain.getChildren().iterator();

			while (it.hasNext()) {
				state = "";
				nodetype = "";
				action = "";
				next = "";
				error = "";
				onError = "";
				TableItem item = null;
				Element node = (Element) it.next();
				if (node.getName().equals("job_chain_node.job_chain")) {
					state = Utils.getAttributeValue("state", node);
					action = Utils.getAttributeValue("job_chain", node);
					next = Utils.getAttributeValue("next_state", node);
					error = Utils.getAttributeValue("error_state", node);
					onError = Utils.getAttributeValue("on_error", node);
					if (Utils.getAttributeValue("job_chain", node) == "") {
						nodetype = "Endnode";
					}
					else {
						nodetype = "Job Chain";
					}
					item = new TableItem(table, SWT.NONE);

					item.setText(new String[] { state, nodetype, action, next, error, onError });

					if (!next.equals("") && !checkForState(next))
						item.setBackground(3, getColor4InvalidValues());

					if (!error.equals("") && !checkForState(error))
						item.setBackground(4, getColor4InvalidValues());

				}
				else {
					if (node.getName().equals("job_chain_node.end")) {
						state = Utils.getAttributeValue("state", node);

						item = new TableItem(table, SWT.NONE);
						//item.setText(new String[] { state, nodetype, action, next, error, onError });
						item.setText(new String[] { state, "Endnode", "", "", "", "" });
					}
				}
				if (item != null) {
					item.setData(node);
				}
			}
		}
	}

	public boolean checkForState(final String state) {

		for (String _state : strCurrentStates) {
			if (_state.equals(state))
				return true;
		}
		return false;
	}

	public void selectNode(final Table tableNodes) {
		if (tableNodes == null) {
			objJobChainNode = null;
		}
		else {
			int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));
			objJobChainNode = getJobChainNodeWrapper((Element) objJobChain.getChildren().get(index));
			if (strCurrentStates == null) {
				setStates();
			}
		}
	}

	@Override
	public boolean isFullNode() {
		if (objJobChainNode.getDOMElement() != null)
			return objJobChainNode.getDOMElement().getAttributeValue("job_chain") != null;
		else
			return true;
	}

	public String getNestedJobChain() {
		return Utils.getAttributeValue("job_chain", objJobChainNode.getDOMElement());
	}

	public void setNestedJobChain(final String jobChain) {
		Utils.setAttribute("job_chain", jobChain, objJobChainNode.getDOMElement(), _dom);
		setDirty();
		//		_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", _chain), SchedulerDom.MODIFY);
	}

	@Override
	public String getMoveTo() {
		return Utils.getAttributeValue("move_to", objJobChainNode.getDOMElement());
	}

	@Override
	public boolean getRemoveFile() {
		return Utils.getAttributeValue("remove", objJobChainNode.getDOMElement()).equals("yes");
	}

	public void applyNode(final boolean isJobchainNode, final String state, final String job, final String next, final String error, final boolean isFullnode) {

		JobChainNodeWrapper node = null;
		if (objJobChainNode.getDOMElement() != null) {
			//System.out.println("node != null, old state=" + Utils.getAttributeValue("state", _node.getDOMElement()) + ", new state=" + state);
			String oldState = Utils.getAttributeValue("state", objJobChainNode.getDOMElement());

			if (oldState != null && state != null && !oldState.equals(state)) {
				//state hat sicg geändert. ggf die Details state auch ändern
				DetailsListener.changeNodeParameters(oldState, state, Utils.getAttributeValue("name", objJobChain), _dom);
			}

			if (isJobchainNode && objJobChainNode.getDOMElement().getName().equals("job_chain_node.end")) {
				objJobChainNode.getDOMElement().detach();
				objJobChainNode = null;
			}
		}

		if (objJobChainNode.getDOMElement() != null) {
			if (isJobchainNode) {
				node.setState(state);
				Utils.setAttribute("job_chain", job, objJobChainNode.getDOMElement(), _dom);
				node.setNextState(next);
				node.setErrorState(error);
			}
			else {
				node.setState(state);
			}
		}
		else {

			if (isJobchainNode) {
				if (!isFullnode)
					node = getJobChainNodeWrapper (new Element("job_chain_node.end"));
				else {
					node = getJobChainNodeWrapper (new Element("job_chain_node.job_chain"));
					Utils.setAttribute("job_chain", job, node.getDOMElement(), _dom);
					node.setNextState(next);
					node.setErrorState(error);
				}
			}

			objJobChain.addContent(node.getDOMElement());
			objJobChainNode = node;

		}

		setDirty();
		setStates();
	}

	public void applyInsertNode(final String state, final String job, final String next, final String error, final boolean isFullnode) {

		JobChainNodeWrapper node = null;

		if (!isFullnode)
			node = new JobChainNodeWrapper(new Element("job_chain_node.end"));
		else {
			node = new JobChainNodeWrapper(new Element("job_chain_node.job_chain"));
			Utils.setAttribute("job_chain", job, node.getDOMElement(), _dom);
			Utils.setAttribute("next_state", next, node.getDOMElement(), _dom);
			Utils.setAttribute("error_state", error, node.getDOMElement(), _dom);
		}
		Utils.setAttribute("state", state, node.getDOMElement(), _dom);

		boolean found = false;
		List list = objJobChain.getChildren();
		if (list.size() > 0 && objJobChainNode.getDOMElement() != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(objJobChainNode.getDOMElement())) {
					if (i > 0) {
						Element previosNode = (Element) list.get(i - 1);
						Utils.setAttribute("next_state", state, previosNode, _dom);
						objJobChain.addContent(objJobChain.indexOf(previosNode) + 1, node.getDOMElement());
						found = true;
						break;
					}
				}
			}
		}

		if (!found) {
			objJobChain.addContent(0, node.getDOMElement());
		}

		objJobChainNode = node;
		setDirty();
		setStates();
	}

	// TODO use Method from JobChainListener
	public void MoveNodeUp(final SOSTable table, final boolean up, final boolean isJobchainNode, final String state, final String job, final String delay,
			final String next, final String error, final int index, final boolean isFullNode, final boolean reorder) {
		try {
			Element node = null;

			if (reorder) {
				if (Utils.getAttributeValue("job_chain", objJobChainNode.getDOMElement()).length() == 0 || objJobChainNode.getDOMElement() != null
						&& objJobChainNode.getDOMElement().getName().equals("job_chain_node.end")) {
					sos.scheduler.JOEConstants.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
					return;
				}

				//der Austausch darf nicht mit einem EndNode stattfinden
				if (up) {
					if (table.getSelectionIndex() > 0 && table.getItem(table.getSelectionIndex() - 1).getText(1).equals("Endnode")) {
						sos.scheduler.JOEConstants.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						return;
					}

				}
				else {
					if (table.getSelectionIndex() < table.getItemCount() - 1 && table.getItem(table.getSelectionIndex() + 1).getText(1).equals("Endnode")) {
						sos.scheduler.JOEConstants.app.MainWindow.message("Only Job Chain Node could be Reorder", SWT.ICON_INFORMATION);
						return;
					}
				}
			}

			List l = objJobChain.getContent();
			int cIndex = -1;
			boolean found = false;//Hilfsvariabkle für down
			for (int i = 0; i < objJobChain.getContentSize(); i++) {
				if (l.get(i) instanceof Element) {
					Element elem_ = (Element) l.get(i);
					if (up) {
						if (elem_.equals(objJobChainNode.getDOMElement())) {
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
						if (elem_.equals(objJobChainNode.getDOMElement())) {
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
			node = (Element) objJobChainNode.getDOMElement().clone();

			if (reorder) {
				Filter elementFilter2 = new ElementFilter("job_chain_node.job_chain", getChainElement().getNamespace());
				// gets all element nodes under the rootElement
				List elements = getChainElement().getContent(elementFilter2);
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

					if (currElement.equals(objJobChainNode.getDOMElement())) {

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

						if (up) {

							if (prev2Element != null && curState.length() > 0) {
								Utils.setAttribute("next_state", curState, prev2Element);
							}
							if (prevElement != null && currNextState.length() > 0) {
								Utils.setAttribute("next_state", currNextState, prevElement);
							}
							if (curState != null && prevState.length() > 0) {
								Utils.setAttribute("next_state", prevState, currElement);
								Utils.setAttribute("next_state", prevState, node);
							}

						}
						else {
							//up
							if (prevElement != null && currNextState.length() > 0) {
								Utils.setAttribute("next_state", currNextState, prevElement);
							}

							if (nextElement != null) {
								Utils.setAttribute("next_state", curState, nextElement);
							}

							if (curState != null && nextNextState.length() > 0) {
								Utils.setAttribute("next_state", nextNextState, currElement);
								Utils.setAttribute("next_state", nextNextState, node);
								Utils.setAttribute("next_state", nextNextState, objJobChainNode.getDOMElement());
							}
						}

						break;
					}

					//System.out.println( currElement );
				}
			}

			if (objJobChain.getChildren().contains(objJobChainNode.getDOMElement())) {
				objJobChain.removeContent(objJobChainNode.getDOMElement());
			}

			objJobChain.addContent(cIndex, node);
			objJobChainNode = new JobChainNodeWrapper(node);
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job_chain", Utils.getAttributeValue("name", objJobChain), SchedulerDom.MODIFY);

			setStates();
			fillChain(table);
			if (up)
				table.setSelection(index - 1);
			else
				table.setSelection(index + 1);

		}
		catch (Exception e) {
			new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
		}
	}

	private int getIndexOfNode(final TableItem item) {
		int index = 0;
		if (objJobChain != null) {

			Iterator it = objJobChain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = (Element) it.next();
				if (Utils.getAttributeValue("state", node).equals(item.getText(0))) {
					index = i;
					break;
				}
				i = i + 1;
			}
		}
		return index;
	}

	public void deleteNode(final Table tableNodes) {
		List nodes = objJobChain.getChildren();
		int index = getIndexOfNode(tableNodes.getItem(tableNodes.getSelectionIndex()));

		DetailsListener.deleteDetailsState(tableNodes.getSelection()[0].getText(0), Utils.getAttributeValue("name", objJobChain), _dom);
		nodes.remove(index);
		objJobChainNode = null;
		setDirty();
		setStates();
	}

	public String[] getJobChains() {

		ArrayList list = new java.util.ArrayList();

		if (_config == null) {
			return new String[0];
		}
		try {
			XPath x3 = XPath.newInstance("//job_chain");
			List listOfElement = x3.selectNodes(_dom.getDoc());

			for (int i = 0; i < listOfElement.size(); i++) {
				Element e = (Element) listOfElement.get(i);
				if (e.getChild("job_chain_node.job_chain") == null && !Utils.getAttributeValue("name", e).equalsIgnoreCase(getChainName())) {
					list.add(Utils.getAttributeValue("name", e));
				}
			}
			String[] names = new String[list.size()];
			for (int i = 0; i < list.size(); i++)
				names[i] = list.get(i) != null ? list.get(i).toString() : "";
			return names;
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
			}

			return new String[0];
		}
	}

	private void setStates() {
		List nodes = objJobChain.getChildren("job_chain_node.job_chain");
		List endNodes = objJobChain.getChildren("job_chain_node.end");
		Iterator it = nodes.iterator();
		strCurrentStates = new String[nodes.size() + endNodes.size()];
		int index = 0;
		while (it.hasNext()) {
			String state = ((Element) it.next()).getAttributeValue("state");
			strCurrentStates[index++] = state != null ? state : "";
		}

		it = endNodes.iterator();

		while (it.hasNext()) {
			String state = ((Element) it.next()).getAttributeValue("state");
			strCurrentStates[index++] = state != null ? state : "";
		}

	}

	public String[] getStates() {
		if (strCurrentStates == null)
			return new String[0];
		else
			if (objJobChainNode.getDOMElement() != null) {
				String state = getState();
				int index = 0;
				String[] states = new String[strCurrentStates.length - 1];
				for (int i = 0; i < strCurrentStates.length; i++) {
					if (!strCurrentStates[i].equals(state))
						states[index++] = strCurrentStates[i];
				}
				return states;
			}
			else
				return strCurrentStates;
	}

	@Override
	public boolean isUniqueState(final String state) {
		if (strCurrentStates == null)
			return true;

		for (int i = 0; i < strCurrentStates.length; i++) {
			if (strCurrentStates[i].equalsIgnoreCase(state) && !strCurrentStates[i].equals(getState()))
				return false;
		}
		return true;
	}

}
