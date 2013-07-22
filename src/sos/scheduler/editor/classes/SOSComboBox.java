/**
 *
 */
package sos.scheduler.editor.classes;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.SOSMsgJOE;
import sos.scheduler.editor.conf.forms.JobChainNodesForm;

/**
 * @author KB
 * for more information on CCombo see http://help.eclipse.org/helios/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fcustom%2FCCombo.html
 * JavaAPI: http://help.eclipse.org/helios/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fcustom%2FCCombo.html
 */
public class SOSComboBox extends CCombo {

	@SuppressWarnings("unused")
	private final String		conClassName			= "JobChainNodesForm";
	@SuppressWarnings("unused")
	private final String		conSVNVersion			= "$Id: SOSComboBox.java 19107 2013-02-12 11:38:14Z ur $";
	private static final Logger	logger					= Logger.getLogger(JobChainNodesForm.class);

	private SOSMsgJOE			objMsg					= null;

	FocusListener				ComboFocusListener		= new FocusListener() {
															@Override
															public void focusGained(final FocusEvent event) {
																logger.debug("combo: widgetDefaultSelected selected");
																CCombo c = (CCombo) event.widget;
																if (c.getText().length() <= 0 && c.getItemCount() > 0) {
																	c.setListVisible(true);
																}
															}

															@Override
															public void focusLost(final FocusEvent event) {
															}
														};
	SelectionListener			ComboSelectionListener	= new SelectionListener() {

															@Override
															public void widgetDefaultSelected(final SelectionEvent e) {
																logger.debug("combo: widgetDefaultSelected selected");
																CCombo c = (CCombo) e.widget;
																if (c.getText().length() <= 0 && c.getItemCount() > 0) {
																	c.setListVisible(true);
																}
															}

															@Override
															public void widgetSelected(final SelectionEvent e) {
																logger.debug("combo: widget selected");
																CCombo c = (CCombo) e.widget;
																if (c.getText().length() <= 0 && c.getItemCount() > 0) {
																	c.setListVisible(true);
																}
															}
														};

	public SOSComboBox(final Composite parent, final int style) {
		super(parent, style);
		init();
		// TODO Auto-generated constructor stub
	}

	public SOSComboBox(final Composite parent, final SOSMsgJOE pobjMsg) {
		super(parent, SWT.BORDER | SWT.FLAT);
		objMsg = pobjMsg;
		init();
	}

	private ArrayList<ISOSComboItem>	objComboItemList	= null;

	public int select(final String pstrValue) {
		String[] objL = super.getItems();
		int intR = -1;
		int i = 0;
		for (String string : objL) {
			if (string.equalsIgnoreCase(pstrValue)) {
				intR = i;
				break;
			}
			i++;
		}
		if (intR != -1) {
			super.select(intR);
		}
		return intR;
	}

	@Override
	public void setItems(final String[] pstrValList) {
		if (pstrValList.length > 1) {
			String[] objL = pstrValList;
			Arrays.sort(objL, Collator.getInstance());
			super.setItems(objL);
		}
	}

	public void setItemTexts(final ArrayList<ISOSComboItem> pobjComboItems) {
		super.removeAll();
		String[] objSortA = new String[pobjComboItems.size()];
		int i = 0;
		for (ISOSComboItem objComboItem : pobjComboItems) {
			objSortA[i++] = objComboItem.getText();
		}
		Arrays.sort(objSortA, Collator.getInstance());
		super.setItems(objSortA);

		objComboItemList = pobjComboItems;
	}

	public ISOSComboItem getItem(final String pstrText) {
		ISOSComboItem objItem = null;
		for (ISOSComboItem objComboItem : objComboItemList) {
			if (objComboItem.getText().equalsIgnoreCase(pstrText)) {
				objItem = objComboItem;
				break;
			}
		}
		return objItem;
	}

	private void init() {

		if (objMsg != null) {
			objMsg.Control(this);
		}
		this.addFocusListener(ComboFocusListener);
		super.setEditable(true);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
