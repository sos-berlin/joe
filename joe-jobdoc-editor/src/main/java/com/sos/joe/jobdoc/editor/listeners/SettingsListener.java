package com.sos.joe.jobdoc.editor.listeners;
import org.jdom.Element;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class SettingsListener extends JobDocBaseListener<DocumentationDom> {
	Element				_settings;

	public SettingsListener(DocumentationDom dom, Element parent) {
		_dom = dom;
		_parent = parent;
		_settings = _parent.getChild("settings", _dom.getNamespace());
	}

	public void setSettings() {
		if (_settings == null) {
			_settings = new Element("settings", _dom.getNamespace());
			_parent.addContent(_settings);
		}
	}

	public void checkSettings() {
		if (_settings != null) {
			boolean remove = true;
			if (_settings.getChild("note", _dom.getNamespace()) != null)
				remove = false;
			if (_settings.getChild("profile", _dom.getNamespace()) != null)
				remove = false;
			if (_settings.getChild("connection", _dom.getNamespace()) != null)
				remove = false;
			if (remove) {
				_settings.detach();
				_settings = null;
			}
		}
	}

	public Element getSettingsElement() {
		setSettings();
		return _settings;
	}
}
