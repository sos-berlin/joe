package com.sos.joe.jobdoc.editor.listeners;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ParamsListener extends JobDocBaseListener<DocumentationDom> {
	private Element				_params;
	private Element				_param;
	private boolean				_newParam;

	public ParamsListener(DocumentationDom dom, Element parent) {
		_dom = dom;
		_parent = parent;
		_params = _parent.getChild("params", _dom.getNamespace());
	}

	private void setParams() {
		if (_params == null) {
			_params = new Element("params", _dom.getNamespace());
			_parent.addContent(_params);
		}
	}

	public void checkParams() {
		if (_params != null && getID().length() == 0 && getReference().length() == 0 && _params.getChildren().size() == 0) {
			_params.detach();
			_params = null;
		}
	}

	public String getID() {
		return getAttributeValue("id", _params);
	}

	public void setID(String id) {
		setParams();
		setAttribute("id", id, _params, _dom);
		checkParams();
	}

	public String[] getReferences(String ownID) {
		return DocumentationListener.getIDs(_dom, ownID);
	}

	public String getReference() {
		return getAttributeValue("reference", _params);
	}

	public void setReference(String reference) {
		setParams();
		setAttribute("reference", DocumentationListener.getID(reference), _params, _dom);
		checkParams();
	}

	public Element getParamsElement() {
		setParams();
		return _params;
	}

	public void fillParams(Table table) {
		table.removeAll();
		if (_params != null) {
			int index = 0;
			for (Iterator it = _params.getChildren("param", _dom.getNamespace()).iterator(); it.hasNext();) {
				Element param = (Element) it.next();
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, getAttributeValue("name", param));
				item.setText(1, getAttributeValue("default_value", param));
				item.setText(2, getBooleanValue("required", param) ? "yes" : "no");
				item.setText(3, getAttributeValue("reference", param));
				item.setText(4, getAttributeValue("id", param));
				if (param.equals(_param))
					table.select(index);
				index++;
			}
		}
	}

	public void setNewParam() {
		_param = new Element("param", _dom.getNamespace());
		_newParam = true;
	}

	public boolean selectParam(int index) {
		try {
			_param = (Element) _params.getChildren("param", _dom.getNamespace()).get(index);
			_newParam = false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getParamName() {
		return getAttributeValue("name", _param);
	}

	public String getParamDefaultValue() {
		return getAttributeValue("default_value", _param);
	}

	public String getParamID() {
		return getAttributeValue("id", _param);
	}

	public boolean getParamRequired() {
		return getBooleanValue("required", _param);
	}

	public String getParamReference() {
		return getAttributeValue("reference", _param);
	}

	public Element getParamElement() {
		return _param;
	}

	public void applyParam(String name, String defaultValue, String id, String reference, boolean required) {
		setParams();
		setAttribute("name", name, _param);
		setAttribute("default_value", defaultValue, _param);
		setAttribute("id", id, _param);
		setAttribute("reference", DocumentationListener.getID(reference), _param);
		setBoolean("required", required, _param);
		if (!_params.getContent().contains(_param))
			_params.addContent(_param);
		_newParam = false;
		checkParams();
		_dom.setChanged(true);
	}

	public void removeParam(int index) {
		if (selectParam(index)) {
			_param.detach();
			_param = null;
			_dom.setChanged(true);
			checkParams();
		}
	}

	public boolean isNewParam() {
		return _newParam;
	}
}
