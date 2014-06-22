package com.sos.joe.objects.jobchain;
import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_E_0002;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.SOSTable;
import sos.scheduler.editor.conf.listeners.DetailsListener;
import sos.util.SOSClassUtil;

import com.sos.JSHelper.io.Files.JSFile;
import com.sos.JSHelper.io.Files.JSXMLFile;
import com.sos.scheduler.model.objects.JSObjBase;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.JSObjJobChain;
import com.sos.scheduler.model.objects.JobChain.FileOrderSink;
import com.sos.scheduler.model.objects.JobChain.FileOrderSource;
import com.sos.scheduler.model.objects.JobChain.JobChainNode;
import com.sos.scheduler.model.objects.JobChainNodeEnd;

public class JobChainListener extends JOEJobChainDataProvider {
	@SuppressWarnings("unused")
	private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String	conSVNVersion		= "$Id$";
	private final Logger		logger				= Logger.getLogger(this.getClass());
	private Element				_objFileOrderSource	= null;
	//	private final SOSString	sosString			= new SOSString();
	private ArrayList<String>	listOfAllState		= null;
	public int					intStepIncr			= 100;

	public JobChainListener(final JSObjJobChain pobjJobChain) {
		super(pobjJobChain);
		objJSJobChain = pobjJobChain;
	}

//	@Deprecated public JobChainListener(final SchedulerDom dom, final Element jobChain) {
//		_dom = dom;
//		objJobChain = jobChain;
//		objElement = jobChain;
//		//		getJOMJobChain();
//		if (objJobChain.getParentElement() != null)
//			_config = objJobChain.getParentElement().getParentElement();
//	}
//
	/**
		 * This Method seems to be used to modify the name of the jobChain
		*
		* \brief setChainName
		*
		* \details
		*
		* \return void
		*
		 */
	@Override public void setChainName(final String name) {
		//		_dom.setChanged(true);
		String oldjobChainName = getChainName();
		objJSJobChain.setName(name);
		//Für job_chain node Parameter
		// TODO create separate Method or Class for JobChain Node Parameter handling
		if (objJobChain != null && _dom.getFilename() != null) {
			CTabItem currentTab = MainWindow.getContainer().getCurrentTab();
			String path = _dom.isDirectory() ? _dom.getFilename() : new java.io.File(_dom.getFilename()).getParent();
			try {
				if (currentTab.getData("details_parameter") != null) {
					HashMap<Element, String> h = new HashMap<Element, String>();
					h = (HashMap<Element, String>) currentTab.getData("details_parameter");
					if (!h.containsKey(objJobChain)) {
						h.put(objJobChain, new java.io.File(path, oldjobChainName + JSObjJobChain.conFileNameExtension4NodeParameterFile).getCanonicalPath());
					}
				}
				else {
					HashMap<Element, String> h = new HashMap<Element, String>();
					h.put(objJobChain, new java.io.File(path, oldjobChainName + JSObjJobChain.conFileNameExtension4NodeParameterFile).getCanonicalPath());
					currentTab.setData("details_parameter", h);
				}
				// für das Speicher per FTP
				String filename = _dom.isLifeElement() ? new File(_dom.getFilename()).getParent() : _dom.getFilename();
				currentTab.setData("ftp_details_parameter_file", filename + "/" + name + JSObjJobChain.conFileNameExtension4NodeParameterFile);
				if (oldjobChainName != null && oldjobChainName.length() > 0
						&& new File(filename + "/" + oldjobChainName + JSObjJobChain.conFileNameExtension4NodeParameterFile).exists()) {
					currentTab.setData("ftp_details_parameter_remove_file", oldjobChainName + JSObjJobChain.conFileNameExtension4NodeParameterFile);
				}
			}
			catch (Exception e) {
				new ErrorLog("error in setChainName, cause: " + e.toString(), e);
			}
		}
		//		if (oldjobChainName != null && oldjobChainName.length() > 0) {
		//			if (_dom.isChanged() && (_dom.isDirectory() && !Utils.existName(oldjobChainName, objJobChain, "job_chain") || _dom.isLifeElement())) {
		//				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		//			}
		//		}
		//		Utils.setAttribute("name", name, objJobChain);
		//		objJSJobChain.setName(name);
		//		if (_dom.isDirectory() || _dom.isLifeElement())
		//			_dom.setChangedForDirectory("job_chain", name, SchedulerDom.MODIFY);
	}

	public void applyChain(final String name, final boolean ordersRecoverable, final boolean visible, final boolean distributed, final String title) {
		//		String oldjobChainName = getChainName();
		//		if (oldjobChainName != null && oldjobChainName.length() > 0) {
		//			if (_dom.isDirectory() || _dom.isLifeElement()) {
		//				_dom.setChangedForDirectory("job_chain", oldjobChainName, SchedulerDom.DELETE);
		//			}
		//		}
		setName(name);
		setOrdersRecoverable(ordersRecoverable);
		setVisible(visible);
		setDistributed(distributed);
		setTitle(title);
	}

	public void populateTable4FileOrderSource() {
		if (objJobChainNodesTable == null | objJSJobChain == null) {
			return;
		}
		objJobChainNodesTable.removeAll();
		for (FileOrderSource objNode : objJSJobChain.getFileOrderSourceList()) {
			TableItem item = new TableItem(objJobChainNodesTable, SWT.NONE);
			item.setData("Element", objNode);
			String directory = objNode.getDirectory();
			String regex = objNode.getRegex();
			String next_state = objNode.getNextState();
			String strDelayAfterError = BigInt2String(objNode.getDelayAfterError());
			String strRepeat = objNode.getRepeat();
			String strMaxFiles = BigInt2String(objNode.getMax());
			item.setText(new String[] { directory, regex, next_state, strDelayAfterError, strRepeat, strMaxFiles });
		}
		//		if (objJobChain != null) {
		//			// TODO create class FileOrderSourceWrapper
		//
		//			for (Object object : objJobChain.getChildren()) {
		//				Element node = (Element) object;
		//				if (node.getName() == conTagFILE_ORDER_SOURCE) {
		//					String directory = Utils.getAttributeValue(conAttrDIRECTORY, node);
		//					String regex = Utils.getAttributeValue(conAttrREGEX, node);
		//					String next_state = Utils.getAttributeValue(conAttrNEXT_STATE, node);
		//
		//					String strDelayAfterError = Utils.getAttributeValue(conAttrDELAY_AFTER_ERROR, node);
		//					String strRepeat = Utils.getAttributeValue(conAttrREPEAT, node);
		//					String strMaxFiles = Utils.getAttributeValue(conAttrMAX, node);
		//
		//					TableItem item = new TableItem(objJobChainNodesTable, SWT.NONE);
		//					item.setData("Element", node);
		//					item.setText(new String[] { directory, regex, next_state, strDelayAfterError, strRepeat, strMaxFiles });
		//				}
		//			}
		//		}
	}

	public void fillFileOrderSink(final SOSTable table) {
		if (table == null | objJSJobChain == null) {
			return;
		}
		table.removeAll();
		for (FileOrderSink objNode : objJSJobChain.getFileOrderSinkList()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData("Element", objNode);
			String state = objNode.getState();
			String moveFileTo = objNode.getMoveTo();
			String remove = objNode.getRemove();
			item.setText(new String[] { state, moveFileTo, remove });
		}
		//			if (objJobChain != null) {
		//				Iterator it = objJobChain.getChildren().iterator();
		//				while (it.hasNext()) {
		//					Element node = (Element) it.next();
		//					if (node.getName() == "file_order_sink") {
		//						String state = Utils.getAttributeValue(conAttrSTATE, node);
		//						String moveFileTo = Utils.getAttributeValue("move_to", node);
		//						String remove = Utils.getAttributeValue("remove", node);
		//						TableItem item = new TableItem(table, SWT.NONE);
		//						item.setText(new String[] { state, moveFileTo, remove });
		//					}
		//				}
		//			}
	}

	public void populateNodesTable() {
		populateNodesTable(objJobChainNodesTable);
	}

	//
	public void populateNodesTable(final Table table) {
		if (table == null) {
			return;
		}
		table.removeAll();
		String state = "";
		String nodetype = "";
		String strJobName = "";
		String next = "";
		String error = "";
		String onError = "";
		String strDelayOnStart = "";
		setStates();
		for (JobChainNode objNode : objJSJobChain.getJobChainNodeList()) {
			strJobName = objNode.getJob();
			if (strJobName.length() <= 0) { // Ein EndNode
				nodetype = "Endnode";
			}
			else {
				nodetype = "Job";
			}
			state = objNode.getState();
			String strHasParameters = "";
			nodetype = "Job";
			next = objNode.getNextState();
			error = objNode.getErrorState();
			strDelayOnStart = BigInt2String(objNode.getDelay());
			int conItemIndexHasParams = 7;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData("element", objNode);
			item.setData("elementtype", nodetype);
			//		TODO		if (DetailsListener.existDetailsParameter(state, getChainName(), strJobName, _dom, update, false, null)) {
			//					item.setBackground(getColor4NodeParameter());
			//					strHasParameters = "yes";
			//				}
			//				else {
			//					item.setBackground(null);
			//				}
			item.setText(new String[] { state, nodetype, strJobName, next, error, onError, strDelayOnStart, strHasParameters });
			int conItemIndexNext = 3;
			int conItemIndexError = 4;
			if (!next.equals("") && !isStateDefined(next))
				item.setBackground(conItemIndexNext, getColor4InvalidValues());
			if (!error.equals("") && !isStateDefined(error)) {
				item.setBackground(conItemIndexError, getColor4InvalidValues());
			}
			if (strHasParameters.length() > 0) {
				item.setBackground(conItemIndexHasParams, getColor4hasParameter());
			}
		}
		state = "";
		strJobName = "";
		next = "";
		error = "";
		onError = "";
		strDelayOnStart = "";
		for (JobChainNodeEnd objNode : objJSJobChain.getJobChainNodeEndList()) {
			state = objNode.getState();
			nodetype = "Endnode";
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData("element", objNode);
			item.setData("elementtype", nodetype);
			item.setText(new String[] { state, nodetype, strJobName, next, error, onError, strDelayOnStart, "" });
		}
	}
	//
	public SOSTable	objJobChainNodesTable	= null;

	//	public void populateNodesTable() {
	//		assert objJobChainNodesTable != null;
	//		assert objJobChain != null;
	//		objJobChainNodesTable.removeAll();
	//		setStates();
	//		int intIndex = 0;
	//		for (Object eleNode : objJobChain.getChildren()) {
	//			JobChainNodeWrapper objJobChainNode = getJobChainNodeWrapper((Element) eleNode);
	//			objJobChainNode.setIndex(++intIndex);
	//			if (objJobChainNode.isNode4NodesTable() == true) {
	//				String strJobName = objJobChainNode.getJobName();
	//				if (objJobChainNode.isJobNode() == true && getJobFile(strJobName).exists() == false) {
	//					objJobChainNode.setJobIsMissing(true);
	//				}
	//				if (DetailsListener.existJobChainNodesParameter(objJobChainNode.getState(), getChainName(), strJobName, get_dom(), update, false, null)) {
	//					objJobChainNode.setHasNodeParameter(true);
	//				}
	//				String next = objJobChainNode.getNextState();
	//				if (!next.equals("") && !isStateDefined(next)) {
	//					objJobChainNode.setNextStateIsInvalid(true);
	//				}
	//				String error = objJobChainNode.getErrorState();
	//				if (!error.equals("") && !isStateDefined(error)) {
	//					objJobChainNode.setErrorStateIsInvalid(true);
	//				}
	//				TableItem objTI = new TableItem(objJobChainNodesTable, SWT.NONE);
	//				objTI.setData(objJobChainNode);
	//				objJobChainNode.populateTableItem(objTI);
	//			}
	//		}
	//	}
	//
	// TODO implement in JSObjJobChain
	public boolean isStateDefined(final String state) {
		for (String _state : strCurrentStates) {
			if (_state.equals(state)) {
				return true;
			}
		}
		return false;
	}

	public void selectNode() {
		try {
			if (objJobChainNodesTable == null) {
				logger.debug("objJobChainNodesTable is null");
				clearNode();
			}
			else {
				int intSelectionIndex = getSelectionIndex();
				if (intSelectionIndex != -1) {
					TableItem objTi = objJobChainNodesTable.getItem(intSelectionIndex);
					setNode(objTi.getData());
					if (strCurrentStates == null) {
						setStates();
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearFileOrderSource() {
		//	TODO	_objFileOrderSource = null;
	}

	public int getSelectionIndex() {
		int index = objJobChainNodesTable.getSelectionIndex();
		return index;
	}

	public void selectFileOrderSource() {
		if (objJobChainNodesTable == null) {
			clearFileOrderSource();
		}
		else {
			int index = getSelectionIndex();
			_objFileOrderSource = (Element) objJobChain.getChildren(conTagFILE_ORDER_SOURCE).get(index);
		}
	}

	public String getFileOrderSource(final String a) {
		return Utils.getAttributeValue(a, _objFileOrderSource);
	}

	public void applyNode(final boolean isJobchainNode, final String state, final String job, final String delay, final String next, final String error,
			final boolean removeFile, final String moveTo, final String onError) throws Exception {
		try {
			if (objJobChainNode != null) {//Wenn der Knotentyp geändert wird, alten löschen und einen neuen anlegen.
				if (getState().equals(state) == false) {
					DetailsListener.changeNodeParameters(getState(), state, getChainName(), _dom);
				}
				if (isJobchainNode && getNode().isFileSinkNode() || !isJobchainNode && getNode().isJobNode()) {
					//			  TODO		getNode().detach();
					clearNode();
				}
			}
			if (objJobChainNode == null) {
				if (isJobchainNode) {
					setNode(getNewJobChainNode());
				}
				else {
					setNode(getNewFileOrderSinkNode());
				}
			}
			setState(state);
			if (isJobchainNode) {
				getNode().setJobName(job).setDelay(delay).setNextState(next).setErrorState(error).setOnError(onError);
			}
			else {
				getNode().setMoveTo(moveTo).setRemoveFileB(removeFile);
			}
			setDirty();
			setStates();
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
		}
	}

	public void createNewJobChainNode(final String state, final String job, final String delay, final String strNextState, final String strErrorState,
			final String onError) {
		try {
			setNode(getNewJobChainNode());
			getNode().setState(state).setJobName(job).setDelay(delay).setNextState(strNextState).setErrorState(strErrorState).setOnError(onError);
			setDirty();
			setStates();
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(SOSClassUtil.getMethodName()), e);
		}
	}

	public void setFileOrderSink(final String state, final boolean removeFile, final String moveTo) {
		try {
			clearNode();
			for (FileOrderSink element : getFileOrderSinkList()) {
				if (element.getState().equalsIgnoreCase(state)) {
					setNode(getJobChainNodeWrapper(element));
					break;
				}
			}
			setNodeIfNull(getNewFileOrderSinkNode());
			getNode().setState(state).setMoveTo(moveTo).setRemoveFileB(removeFile);
		}
		catch (Exception e) {
			new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(conClassName), e);
		}
	}

	public void applyInsertNode(final boolean isJobchainNode, final String state, final String job, final String delay, final String next, final String error,
			final boolean removeFile, final String moveTo, final String onError) {
		//		JobChainNodeWrapper objCurrentNode = getNode();
		//		JobChainNodeWrapper objNode2Insert = null;
		//		if (isJobchainNode) {
		//			objNode2Insert = getAJobChainNode().setJobName(job).setDelay(delay).setNextState(next).setErrorState(error).setOnError(onError);
		//		}
		//		else {
		//			objNode2Insert = getAFileOrderSinkNode().setMoveTo(moveTo).setRemoveFileB(removeFile).setState(state);
		//		}
		//		objNode2Insert.setState(state);
		//		boolean found = false;
		//		List<Element> list = objJobChain.getChildren();
		//		if (list.size() > 0 && objCurrentNode != null) {
		//			// TODO Attribut "index" einführen. Damit entfällt die umsortiererei und die Reihenfolge ist nicht länger relevant
		//			for (int i = 0; i <= list.size(); i++) {
		//				if (list.get(i).equals(objCurrentNode.getDOMElement())) {
		//					JobChainNodeWrapper objPreviousNode = getJobChainNodeWrapper(list.get(i - 1));
		//					objPreviousNode.setNextState(state);
		//					objNode2Insert.setNextState(objCurrentNode.getState());
		//					objJobChain.addContent(i + 1, objNode2Insert.getDOMElement());
		//					found = true;
		//					break;
		//				}
		//			}
		//		}
		//		if (found == false) {
		//			objJobChain.addContent(0, objNode2Insert.getDOMElement());
		//		}
		//		setNode(objCurrentNode);
		//		setDirty();
		//		setStates();
	}

	//	public String changeNodeSequence(final boolean flgDirectionIsUpwards, final boolean isJobchainNode, final int intSelectedTabItem,
	//			final boolean flgDoReorderStates) {
	//		String state = getNode().getState();
	//		String job = getNode().getJobName();
	//		String delay = getNode().getDelay();
	//		String next = getNode().getNextState();
	//		String error = getNode().getErrorState();
	//		boolean removeFile = false; // TODO implement in NodeWrapper,
	//		String moveTo = ""; // TODO implement in NodeWrapper
	//		try {
	//			if (flgDoReorderStates) {
	//				// TODO I18N
	//				String msg = "The node " + state + " is an Endnode and cannot be changed with Node due to reorder is activated";
	//				if (getNode().isEndNode() == true) {
	//					MainWindow.message(msg, SWT.ICON_INFORMATION);
	//					return "";
	//				}
	//				int intSelectionIndex = getSelectionIndex();
	//				if (intSelectionIndex != -1) {
	//					if (flgDirectionIsUpwards) {
	//						JobChainNodeWrapper objN = (JobChainNodeWrapper) objJobChainNodesTable.getItem(intSelectionIndex - 1).getData();
	//						if (objN.isEndNode()) {
	//							MainWindow.message(msg, SWT.ICON_INFORMATION);
	//							return "";
	//						}
	//					}
	//					else {
	//						if (intSelectionIndex < objJobChainNodesTable.getItemCount() - 1) {
	//							JobChainNodeWrapper objN = (JobChainNodeWrapper) objJobChainNodesTable.getItem(intSelectionIndex + 1).getData();
	//							if (objN.isEndNode()) {
	//								MainWindow.message(msg, SWT.ICON_INFORMATION);
	//								return "";
	//							}
	//						}
	//					}
	//				}
	//			}
	//			List l = objJobChain.getContent();
	//			int cIndex = -1;
	//			boolean found = false;
	//			for (int i = 0; i < objJobChain.getContentSize(); i++) {
	//				Object objI = l.get(i);
	//				if (objI instanceof Element) {
	//					Element elem_ = (Element) objI;
	//					if (flgDirectionIsUpwards) {
	//						if (elem_.equals(getNode().getDOMElement())) {
	//							break;
	//						}
	//						else {
	//							cIndex = i;
	//							if (cIndex == -1) {
	//								cIndex = 0;
	//							}
	//						}
	//					}
	//					else {
	//						if (elem_.equals(getNode().getDOMElement())) {
	//							found = true;
	//						}
	//						else {
	//							if (found) {
	//								cIndex = i;
	//								break;
	//							}
	//						}
	//					}
	//				}
	//			}
	//			JobChainNodeWrapper objSelectedNode = getJobChainNodeWrapper((Element) getNode().getDOMElement().clone());
	//			boolean flgSwapped = false;
	//			if (flgDoReorderStates == false) {
	//				flgSwapped = true;
	//			}
	//			else {
	//				Filter elementFilter2 = new ElementFilter(conTagJOB_CHAIN_NODE, getChainElement().getNamespace());
	//				List elements = getChainElement().getContent(elementFilter2);
	//				int size = elements.size();
	//				for (int i = 0; i < size; ++i) {
	//					JobChainNodeWrapper objPredecessorNode = null;
	//					JobChainNodeWrapper prev2Element = null;
	//					JobChainNodeWrapper objSuccessorNode = null;
	//					if (elements.get(i).equals(getNode().getDOMElement())) {
	//						if (i >= 2) {
	//							prev2Element = getJobChainNodeWrapper((Element) elements.get(i - 2));
	//						}
	//						if (i >= 1) {
	//							objPredecessorNode = getJobChainNodeWrapper((Element) elements.get(i - 1));
	//						}
	//						if (size > i + 1) {
	//							objSuccessorNode = getJobChainNodeWrapper((Element) elements.get(i + 1));
	//						}
	//						// TODO move to JobChain class
	//						if (flgDirectionIsUpwards) { // what if the next-state are not in a row?
	//							if (objSelectedNode.CanSwap(objPredecessorNode) == true) {
	//								flgSwapped = true;
	//								String strN = objSelectedNode.getNextState();
	//								objSelectedNode.SwapNextState(objPredecessorNode);
	//								objPredecessorNode.setNextState(strN);
	//								if (prev2Element != null) {
	//									prev2Element.SwapNextState(objSelectedNode);
	//								}
	//							}
	//						}
	//						else { // what if the next-state are not in a row?
	//							if (objSuccessorNode != null && objSuccessorNode.CanSwap(objSelectedNode) == true) {
	//								flgSwapped = true;
	//								String strN = objSuccessorNode.getNextState();
	//								objSuccessorNode.SwapNextState(objSelectedNode);
	//								objSelectedNode.setNextState(strN);
	//								if (objPredecessorNode != null) {
	//									objPredecessorNode.SwapNextState(objSuccessorNode);
	//								}
	//							}
	//						}
	//						break;
	//					}
	//				}
	//			}
	//			if (flgSwapped == true) {
	//				if (objJobChain.getChildren().contains(getNode().getDOMElement())) {
	//					objJobChain.removeContent(getNode().getDOMElement());
	//				}
	//				objJobChain.addContent(cIndex, objSelectedNode.getDOMElement());
	//				setNode(objSelectedNode);
	//				setDirty();
	//				//			setStates();
	//				//			populateNodesTable(table);
	//				objJobChainNodesTable.getItem(intSelectedTabItem).dispose();
	//				int intNewIndex = intSelectedTabItem;
	//				if (flgDirectionIsUpwards) {
	//					intNewIndex--;
	//				}
	//				else {
	//					intNewIndex++;
	//				}
	//				objSelectedNode.populateTableItem(new TableItem(objJobChainNodesTable, SWT.None, intNewIndex));
	//				objJobChainNodesTable.setSelection(intNewIndex);
	//			}
	//		}
	//		catch (Exception e) {
	//			new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
	//		}
	//		return state;
	//	}
	//
	public void applyFileOrderSource(final String directory, final String regex, final String next_state, final String max, final String repeat,
			final String delay_after_error) {
		if (_objFileOrderSource == null) {
			Element eleFileSorderSource = new Element(conTagFILE_ORDER_SOURCE);
			objJobChain.addContent(eleFileSorderSource);
			_objFileOrderSource = eleFileSorderSource;
		}
		Utils.setAttribute(conAttrDIRECTORY, directory, _objFileOrderSource, _dom);
		Utils.setAttribute(conAttrREGEX, regex, _objFileOrderSource, _dom);
		Utils.setAttribute(conAttrNEXT_STATE, next_state, _objFileOrderSource, _dom);
		Utils.setAttribute(conAttrMAX, max, _objFileOrderSource, _dom);
		Utils.setAttribute(conAttrREPEAT, repeat, _objFileOrderSource, _dom);
		Utils.setAttribute(conAttrDELAY_AFTER_ERROR, delay_after_error, _objFileOrderSource, _dom);
		setDirty();
	}

	private int getIndexOfNode(final TableItem item) {
		int index = 0;
		if (objJobChain != null) {
			Iterator<Element> it = objJobChain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = it.next();
				if (Utils.getAttributeValue(conAttrSTATE, node).equals(item.getText(0))) {
					index = i;
				}
				i = i + 1;
			}
		}
		return index;
	}

	private int getIndexOfFileOrderSource(final TableItem item) {
		int index = 0;
		if (objJobChain != null) {
			Iterator<Element> it = objJobChain.getChildren().iterator();
			int i = 0;
			while (it.hasNext()) {
				Element node = it.next();
				if (node.getName() == conTagFILE_ORDER_SOURCE) {
					if (Utils.getAttributeValue(conAttrDIRECTORY, node) == item.getText(0) && Utils.getAttributeValue(conAttrREGEX, node) == item.getText(1)) {
						index = i;
					}
					i = i + 1;
				}
			}
		}
		return index;
	}

	public void deleteNode() {
		int intSelectionIndex = getSelectionIndex(); // starting with index 0
		if (intSelectionIndex >= 0) {
			int index = getIndexOfNode(objJobChainNodesTable.getItem(intSelectionIndex));
			if (index > 0) {
				try {
					JobChainNodeWrapper objCurrentNode = getJobChainNodeWrapper((JSObjBase) objJobChainNodesTable.getItem(intSelectionIndex).getData());
					JobChainNodeWrapper objPredecessor = getJobChainNodeWrapper((JSObjBase) objJobChainNodesTable.getItem(intSelectionIndex - 1).getData());
					JobChainNodeWrapper objSuccessor = getJobChainNodeWrapper((JSObjBase) objJobChainNodesTable.getItem(intSelectionIndex + 1).getData());
					if (checkNodesAreChained(objPredecessor, objJobChainNode, objSuccessor)) {
						objPredecessor.setNextState(objSuccessor.getState());
					}
					//		TODO			DetailsListener.deleteDetailsState(objJobChainNodesTable.getSelection()[0].getText(0), getChainName(), _dom);
					//		TODO			objJSJobChain.remove(objCurrentNode);
					clearNode();
					setDirty();
					setStates();
				}
				catch (Exception e) { // ignore
					new ErrorLog("Delete failed", e);
				}
			}
		}
	}

	public void completeNodes() {
		try {
			for (TableItem item : objJobChainNodesTable.getItems()) {
				JobChainNodeWrapper objN = (JobChainNodeWrapper) item.getData();
				if (objN.isJobNode() == true) {
					String strNextState = objN.getNextState();
					if (isStateDefined(strNextState) == false) {
						createNewJobChainNode(strNextState, "", "", "", "", "");
						objN.setNextStateIsInvalid(false);
					}
					String strErrorState = objN.getErrorState();
					if (isStateDefined(strErrorState) == false) {
						createNewJobChainNode(strErrorState, "", "", "", "", "");
						objN.setErrorStateIsInvalid(false);
					}
				}
			}
		}
		catch (Exception ex) {
			new ErrorLog(JOE_E_0002.params(SOSClassUtil.getMethodName()), ex);
		}
		finally {
			populateNodesTable(objJobChainNodesTable);
		}
	}

	private boolean checkNodesAreChained(final JobChainNodeWrapper objPredecessorNode, final JobChainNodeWrapper objCurrentNode,
			final JobChainNodeWrapper objSuccessorNode) {
		boolean flgR = false;
		if (objCurrentNode.getNextState().equalsIgnoreCase(objSuccessorNode.getState())
				&& objPredecessorNode.getNextState().equalsIgnoreCase(objCurrentNode.getState())) {
			flgR = true;
		}
		return flgR;
	}

	public void deleteFileOrderSource() {
		List sources = objJobChain.getChildren(conTagFILE_ORDER_SOURCE);
		int index = getIndexOfFileOrderSource(objJobChainNodesTable.getItem(getSelectionIndex()));
		sources.remove(index);
		clearFileOrderSource();
		setDirty();
	}

	/**
	 * wird benutzt um die combox mit den jobnamen zu füllen. hat hier eigentlich gar nichts zu suchen.
	*
	* \brief getJobs
	*
	* \details
	*
	* \return String[]
	*
	 */
	@Deprecated// TODO  implement in JSObjJobChain
	public String[] getJobs() {
		if (_config == null) {
			return new String[0];
		}
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
		String strState = "";
		String strErrorState = "";
		listOfAllState = new ArrayList<String>();
		for (Object objBaseNode : objJSJobChain.getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd()) {
			strState = "???";
			strErrorState = null;
			if (objBaseNode instanceof JobChainNode) {
				strState = ((JobChainNode) objBaseNode).getState();
				strErrorState = ((JobChainNode) objBaseNode).getErrorState();
			}
			else {
				if (objBaseNode instanceof FileOrderSink) {
					strState = ((FileOrderSink) objBaseNode).getState();
				}
				else {
					if (objBaseNode instanceof JobChainNodeEnd) {
						strState = ((JobChainNodeEnd) objBaseNode).getState();
					}
					else {
						strState = null;
						strErrorState = null;
					}
				}
			}
			if (strState != null && listOfAllState.contains(strState) == false) {
				listOfAllState.add(strState);
			}
			if (strErrorState != null && listOfAllState.contains(strErrorState) == false) {
				listOfAllState.add(strErrorState);
			}
		}
		strCurrentStates = listOfAllState.toArray(new String[listOfAllState.size()]);
		//		return _states;
		//		
		//		List nodes = objJobChain.getChildren(conTagJOB_CHAIN_NODE);
		//		List sinks = objJobChain.getChildren("file_order_sink");
		//		List endNodes = objJobChain.getChildren("job_chain_node.end");
		//		_states = new String[sinks.size() + nodes.size() + endNodes.size()];
		//		listOfAllState = new ArrayList<String>();
		//		int index = 0;
		//		Iterator it = nodes.iterator();
		//		while (it.hasNext()) {
		//			Element el = (Element) it.next();
		//			String state = el.getAttributeValue(conAttrSTATE);
		//			if (state == null) {
		//				state = "";
		//			}
		//			_states[index++] = state;
		//			if (!listOfAllState.contains(state))
		//				listOfAllState.add(state);
		//			String errorState = el.getAttributeValue(conAttrERROR_STATE);
		//			if (errorState == null) {
		//				errorState = "";
		//			}
		//			if (!listOfAllState.contains(errorState))
		//				listOfAllState.add(errorState);
		//		}
		//		it = sinks.iterator();
		//		while (it.hasNext()) {
		//			String state = ((Element) it.next()).getAttributeValue(conAttrSTATE);
		//			if (state == null) {
		//				state = "";
		//			}
		//			_states[index++] = state;
		//			if (!listOfAllState.contains(state))
		//				listOfAllState.add(state);
		//		}
		//		it = endNodes.iterator();
		//		while (it.hasNext()) {
		//			Element el = (Element) it.next();
		//			String state = el.getAttributeValue(conAttrSTATE);
		//			if (state == null) {
		//				state = "";
		//			}
		//			_states[index++] = state;
		//			if (!listOfAllState.contains(state))
		//				listOfAllState.add(state);
		//		}
	}

	public String[] emptyStringArray() {
		String[] strT = new String[0];
		strT[0] = "";
		return strT;
	}

	// TODO implement JSObjJobChain
	public String[] getStates() {
		if (strCurrentStates == null) {
			setStates();
		}
		else {
			if (objJobChainNode != null) {
				String state = getState();
				int intLength = strCurrentStates.length;
				ArrayList<String> arrL = new ArrayList<String>();
				for (int i = 0; i < intLength; i++) {
					if (!strCurrentStates[i].equals(state)) {
						arrL.add(strCurrentStates[i]);
					}
				}
				String[] states = arrL.toArray(new String[arrL.size()]);
				return states;
			}
		}
		return strCurrentStates;
	}

	// TODO implement JSObjJobChain
	public String[] getAllStates() {
		try {
			if (listOfAllState == null)
				return emptyStringArray();
			else {
				if (objJobChainNode == null) {
					return listOfAllState.toArray(new String[listOfAllState.size()]);
				}
				else {
					String errorState = getErrorState();
					String state = getState();
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
					ArrayList<String> arrL = new ArrayList<String>();
					for (String state2 : states) {
						if (state2 != null) {
							arrL.add(state2);
						}
					}
					String[] states2 = arrL.toArray(new String[arrL.size()]);
					return states2;
				}
			}
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
			return emptyStringArray();
		}
	}

	public String getDiagramFileName() {
		String strR = null;
		try {
			strR = objJSJobChain.createDOTFile();
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		return strR;
	}

	public void createMissingJobs() {
		try {
			int intIndex = 0;
			for (JobChainNode objNode : objJSJobChain.getJobChainNodeList()) {
				JobChainNodeWrapper objJobChainNode = getJobChainNodeWrapper(objNode);
				objJobChainNode.setIndex(++intIndex);
				String strJobName = objJobChainNode.getJobName();
				if (objJobChainNode.isNode4NodesTable() == true && isNotEmpty(strJobName)) {
					JSFile objF = getJobFile(strJobName);
					if (objF.exists() == false) {
						// TODO Template-Name as Parameter
						JSXMLFile objTemplateFile = new JSXMLFile(
								"R:/backup/sos/java/development/com.sos.scheduler.editor/src/sos/scheduler/editor/job-template.xml");
						System.out.println(objTemplateFile.getPath());
						if (objTemplateFile.exists() == true) {
							String strContent = objTemplateFile.getContent();
							strContent = strContent.replaceAll("\\$\\{jobname\\}", strJobName);
							strContent = strContent.replaceAll("\\$\\{title\\}", "title for " + strJobName);
							SAXBuilder objP = new SAXBuilder();
							Document document = objP.build(new InputSource(new StringReader(strContent)));
							objJobChain = (Element) document.getRootElement().clone();
							objF.WriteLine(strContent);
							objF.close();
						}
					}
				}
			}
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
	}

	private JSFile getJobFile(final String strJobName) {
		String strLiveFolderPathName = "";
		if (strJobName.startsWith("/")) {
			strLiveFolderPathName = Options.getSchedulerHotFolder();
			strLiveFolderPathName += strJobName + JSObjJob.fileNameExtension;
		}
		else {
			strLiveFolderPathName = get_dom().getFilename();
			strLiveFolderPathName += "/" + strJobName + JSObjJob.fileNameExtension;
		}
		strLiveFolderPathName = strLiveFolderPathName.replaceAll("\\\\", "/");
		JSFile objF = new JSFile(strLiveFolderPathName);
		boolean flgR = objF.exists();
		return objF;
	}
}
