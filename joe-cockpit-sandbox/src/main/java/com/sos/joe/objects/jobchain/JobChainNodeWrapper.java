/**
 *
 */
package com.sos.joe.objects.jobchain;
import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.scheduler.model.objects.JSObjBase;
import com.sos.scheduler.model.objects.JobChain.FileOrderSink;
import com.sos.scheduler.model.objects.JobChain.JobChainNode;

/**
 * @author KB
 *
 */
public class JobChainNodeWrapper extends JSToolBox {
	@SuppressWarnings("unused") private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused") private final Logger logger = Logger.getLogger(this.getClass());
	

	// TODO NodeType "split" and "sync"
	private static final String	conTagFILE_ORDER_SINK		= "file_order_sink";
	public static final String	conTagJOB_CHAIN_NODE_END	= "job_chain_node.end";
	public static final String	conTagJOB_CHAIN_NODE		= "job_chain_node";
	public static final String	conTagFILE_ORDER_SOURCE		= "file_order_source";
//	public static final String	conAttrSTATE				= "state";
//	public static final String	conAttrJOB					= "job";
//	public static final String	conAttrMOVETO				= "move_to";
//	public static final String	conAttrREMOVE				= "remove";
//	public static final String	conAttrON_ERROR				= "on_error";
//	public static final String	conAttrERROR_STATE			= "error_state";
//	public static final String	conAttrDIRECTORY			= "directory";
//	public static final String	conAttrREGEX				= "regex";
//	public static final String	conAttrNEXT_STATE			= "next_state";
//	public static final String	conAttrREPEAT				= "repeat";
//	public static final String	conAttrDELAY				= "delay";
	private int					intIndex					= -1;
	private boolean				flgHasNodeParameter			= false;
	private boolean				flgJobIsMissing				= false;
	private String				strJobChainName				= "";
	private final String				strErrorState				= "";
	private String				strOnError					= "";
	private String				strDelay					= "";
	private String				strJobName					= "";
	private String				strMoveTo					= "";
	private String				strRemovefile				= "";
	//	private Element				objElement					= null;
	private final JobChainNode	objJCNode					= null;

	@Deprecated public SchedulerDom getDom() {
		//		return _dom;
		return null;
	}

	@Deprecated public void setDom(final SchedulerDom _dom) {
		//		this._dom = _dom;
	}

	public int getIndex() {
		return intIndex;
	}

	public JobChainNodeWrapper setIndex(final int pintIndex) {
		intIndex = pintIndex;
		return this;
	}

	/**
	 *
	 * @return the hasNodeParameter
	 */
	public boolean isHasNodeParameter() {
		return flgHasNodeParameter;
	}

	public JobChainNodeWrapper setChainName(final String pstrJobChainName) {
		strJobChainName = pstrJobChainName;
		return this;
	}

	/**
	 * @param hasNodeParameter the hasNodeParameter to set
	 */
	public void setHasNodeParameter(final boolean hasNodeParameter) {
		flgHasNodeParameter = hasNodeParameter;
	}

	/**
	 * @param strState the strState to set
	 */
	public JobChainNodeWrapper setState(final String pstrState) {
		getJCN().setState(pstrState);
		return this;
	}

//	@Deprecated public Element getDOMElement() {
//		//		return objElement;
//		return null;
//	}

//	@Deprecated private void setAttr(final String pstrAttributeName, final String pstrAttributValue) {
//		Utils.setAttribute(pstrAttributeName, pstrAttributValue, objElement, _dom);
//		//		_dom.setChanged(true);
//		//		_dom.setChangedForDirectory("job_chain", strJobChainName, SchedulerDom.MODIFY);
//	}
//
//	@Deprecated private void setAttr(final String pstrAttributeName, final boolean pflgValue) {
//		//		Utils.setAttribute(pstrAttributeName, boolean2String(pflgValue), objElement, _dom);
//		//		_dom.setChanged(true);
//		//		_dom.setChangedForDirectory("job_chain", strJobChainName, SchedulerDom.MODIFY);
//	}
	private JSObjBase	objNode	= null;

	public JobChainNodeWrapper setNode(final JobChainNode pobjNode) {
		objNode = pobjNode;
		return this;
	}

	/**
	 * @param strNextState the strNextState to set
	 */
	public JobChainNodeWrapper setNextState(final String strNextState) {
		getJCN().setNextState(strNextState);
		return this;
	}

	/**
	 * @param strErrorState the strErrorState to set
	 */
	public JobChainNodeWrapper setErrorState(final String strErrorState) {
		getJCN().setErrorState(strErrorState);
		return this;
	}

	/**
	 * @param strOnError the strOnError to set
	 */
	public JobChainNodeWrapper setOnError(final String strOnError) {
		getJCN().setOnError(strOnError);
		return this;
	}

	/**
	 * @param strDelay the strDelay to set
	 */
	public JobChainNodeWrapper setDelay(final String strDelay) {
		getJCN().setDelay(BigInteger.valueOf(Integer.valueOf(strDelay)));
		return this;
	}

	/**
	 * @param strJobName the strJobName to set
	 */
	public JobChainNodeWrapper setJobName(final String strJobName) {
		getJCN().setJob(strJobName);
		this.strJobName = strJobName;
		return this;
	}

	/**
	 * @param strMoveTo the strMoveTo to set
	 */
	public JobChainNodeWrapper setMoveTo(final String strMoveTo) {
		getFOS().setMoveTo(strMoveTo);
		return this;
	}

	/**
	 * @param strRemovefile the strRemovefile to set
	 */
	public JobChainNodeWrapper setRemoveFile(final String strRemovefile) {
		getFOS().setRemove(strRemovefile);
		return this;
	}

	/**
	 *
	 */
	public JobChainNodeWrapper() {
	}

	public JobChainNodeWrapper(final Element objE) {
	}

//	public Element getObjElement() {
//		return objElement;
//	}
//
//	public void setObjElement(final Element objElement) {
//		this.objElement = objElement;
//	}
//
	public String getState() {
		String strR = getJCN().getState();
		if (strR == null) {
			strR = "";
		}

		return strR;
	}

//	private String getAttr(final String pstrAttributeName) {
//		String strR = Utils.getAttributeValue(pstrAttributeName, objElement);
//		return strR;
//	}

	public String getNextState() {
		return getJCN().getNextState();
	}

	public String getErrorState() {
		return getJCN().getErrorState();
	}

	public String getOnError() {
		strOnError = getJCN().getOnError();
		return strOnError;
	}

	public String getDelay() {
		strDelay = getJCN().getDelay().toString();
		return strDelay;
	}

	public String getJobName() {
		strJobName = getJCN().getJob();
		return strJobName;
	}

	public String getMoveTo() {
		strMoveTo = getFOS().getMoveTo();
		return strMoveTo;
	}

	public String getRemovefile() {
		strRemovefile = getFOS().getRemove();
		return strRemovefile;
	}

	public FileOrderSink getFOS(final JSObjBase pobjNode) {
		return (FileOrderSink) pobjNode;
	}

	public JobChainNode getJCN(final JSObjBase pobjNode) {
		return (JobChainNode) pobjNode;
	}

	public FileOrderSink getFOS() {
		return getFOS(objNode);
	}

	public JobChainNode getJCN() {
		return getJCN(objNode);
	}

	public boolean getRemoveFileB() {
		boolean flgRemovefile = String2Bool(getFOS().getRemove());
		return flgRemovefile;
	}

	public JobChainNodeWrapper setRemoveFileB(final boolean pflgRemovefile) {
		getFOS(objNode).setRemove(boolean2String(pflgRemovefile));
		return this;
	}

	public JobChainNodeWrapper(final JobChainNode pobjNode) {
		objNode = pobjNode;
	}

	public JobChainNodeWrapper(final JSObjBase pobjNode) {
		objNode = pobjNode;
	}

	public String getNodeName() {
		String strR = getJCN().getState();
		return strR;
	}

	public boolean isFileSink() {
		boolean flgR = false;
		if (isNotEmpty(getMoveTo()) || isNotEmpty(getRemovefile())) {
			flgR = true;
		}
		return flgR;
	}

	public boolean isEndNode() {
		boolean flgR = false;
		if (getJobName().length() == 0 || getNodeName().equals(conTagJOB_CHAIN_NODE_END)) {
			flgR = true;
		}
		return flgR;
	}

	public boolean isNode4NodesTable() {
		boolean flgR = false;
		String strNodeName = this.getNodeName();
		if (strNodeName.equals(conTagJOB_CHAIN_NODE) || strNodeName.equals(conTagJOB_CHAIN_NODE_END) || strNodeName.equals(conTagFILE_ORDER_SINK)) {
			flgR = true;
		}
		return flgR;
	}

	// TODO I18N
	public String getNodeType() {
		String nodetype = "Job";
		if (isJobNode()) {
			if (isEmpty(getJobName())) {
				nodetype = "Endnode";
			}
			else {
				nodetype = "Job";
			}
		}
		else {
			if (isEndNode()) {
				nodetype = "Endnode";
			}
			else {
				if (isFileSink()) {
					nodetype = "FileSink";
				}
			}
		}
		return nodetype;
	}

	protected String getBoolYesNo(final boolean pflgValue) {
		String strR = "no";
		if (pflgValue == true) {
			strR = "yes";
		}
		return strR;
	}

	public boolean isJobNode() {
		boolean flgR = true;
		if (isEmpty(getJCN().getJob())) {
			flgR = false;
		}
		return flgR;
	}

	public boolean isFileSinkNode() {
		boolean flgR = true;
		if (isEmpty(getRemovefile()) && isEmpty(getMoveTo())) {
			flgR = false;
		}
		return flgR;
	}

	//	public void detach() {
	//		objElement.detach();
	//	}
	//
	public boolean CanSwap(final JobChainNodeWrapper objPredecessorNode) {
		boolean flgSwapDone = false;
		if (objPredecessorNode != null) {
			if (objPredecessorNode.getNextState().equalsIgnoreCase(getState()) == true) { // then we have a chain
				flgSwapDone = true;
			}
		}
		return flgSwapDone;
	}

	@SuppressWarnings("hiding") public boolean SwapNextState(final JobChainNodeWrapper objSwpNode) {
		boolean flgSwapDone = false;
		String strNextState = objSwpNode.getNextState();
		if (strNextState.equalsIgnoreCase(getState()) == true) { // then we have a valid link
			flgSwapDone = true;
			setNextState(objSwpNode.getState());
		}
		return flgSwapDone;
	}
	private boolean	flgNextStateIsInvalid	= false;
	private boolean	flgErrorStateIsInvalid	= false;

	/**
	 * @return the nextStateIsInvalid
	 */
	public boolean isNextStateIsInvalid() {
		return flgNextStateIsInvalid;
	}

	/**
	 * @param nextStateIsInvalid the nextStateIsInvalid to set
	 */
	public void setNextStateIsInvalid(final boolean nextStateIsInvalid) {
		flgNextStateIsInvalid = nextStateIsInvalid;
	}

	/**
	 * @return the errorStateIsInvalid
	 */
	public boolean isErrorStateIsInvalid() {
		return flgErrorStateIsInvalid;
	}

	/**
	 * @param errorStateIsInvalid the errorStateIsInvalid to set
	 */
	public void setErrorStateIsInvalid(final boolean errorStateIsInvalid) {
		flgErrorStateIsInvalid = errorStateIsInvalid;
	}

	public void populateFileOrderSinkTableItem(final TableItem objItem) {
		String state = this.getState();
		String moveFileTo = this.getMoveTo();
		String remove = this.getRemovefile();
		objItem.setText(new String[] { state, moveFileTo, remove });
	}

	public void populateTableItem(final TableItem objItem) {
		String nodetype = "";
		String action = "";
		String next = "";
		String error = "";
		String onError = "";
		String strDelayOnStart = "";
		String strRemoveFile = "";
		String strMoveTo1 = "";
		int conItemJobName = 2;
		int conItemIndexNext = 3;
		int conItemIndexError = 4;
		String strNodeName = this.getNodeName();
		String state = this.getState();
		nodetype = this.getNodeType();
		if (strNodeName.equals(conTagJOB_CHAIN_NODE)) {
			action = this.getJobName();
			next = this.getNextState();
			error = this.getErrorState();
			onError = this.getOnError();
			strDelayOnStart = this.getDelay();
		}
		else {
			if (this.getNodeName().equals(conTagJOB_CHAIN_NODE_END)) {
				action = this.getJobName();
				next = this.getNextState();
				error = this.getErrorState();
				onError = this.getOnError();
			}
			else {
				if (this.getNodeName().equals(conTagFILE_ORDER_SINK)) {
					strMoveTo1 = this.getMoveTo();
					strRemoveFile = this.getRemovefile();
				}
			}
		}
		int conItemIndexHasParams = 7;
		String strHasParameters = "";
		if (flgHasNodeParameter == true) {
			objItem.setBackground(getColor4NodeParameter());
			// TODO i18n
			strHasParameters = "yes";
			objItem.setBackground(conItemIndexHasParams, getColor4hasParameter());
		}
		objItem.setText(new String[] { state, nodetype, action, next, error, onError, strDelayOnStart, strHasParameters, strRemoveFile, strMoveTo1 });
		objItem.setData(this);
		if (flgNextStateIsInvalid == true) {
			objItem.setBackground(conItemIndexNext, getColor4InvalidValues());
		}
		if (flgErrorStateIsInvalid == true) {
			objItem.setBackground(conItemIndexError, getColor4InvalidValues());
		}
		if (flgJobIsMissing == true) {
			objItem.setBackground(conItemJobName, getColor4InvalidValues());
		}
	}

	public Color getColor4InvalidValues() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
		return objC;
	}

	public Color getColor4Backgrond() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		return objC;
	}

	public Color getColor4DisabledElements() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		return objC;
	}

	public Color getColor4NodeParameter() {
		Color objC = Options.getLightBlueColor();
		return objC;
	}

	public Color getColor4MissingJob() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		return objC;
	}

	public Color getColor4hasParameter() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		return objC;
	}

	/**
	 * @return the jobIsMissing
	 */
	public boolean isJobIsMissing() {
		return flgJobIsMissing;
	}

	/**
	 * @param jobIsMissing the jobIsMissing to set
	 */
	public void setJobIsMissing(final boolean jobIsMissing) {
		flgJobIsMissing = jobIsMissing;
	}
}
