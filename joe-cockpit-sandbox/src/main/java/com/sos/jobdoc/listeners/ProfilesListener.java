package com.sos.jobdoc.listeners;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.jobdoc.DocumentationDom;

import sos.scheduler.editor.app.Utils;

public class ProfilesListener {
    private DocumentationDom   _dom;

    private SettingsListener   _settings;

    private Element            _profile;

    private boolean            _newProfile;

    public final static String defaultName = "default";


    public ProfilesListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _settings = new SettingsListener(dom, parent);
    }


    public void checkSettings() {
        _settings.checkSettings();
    }


    public void fillProfiles(Table table) {
        table.removeAll();
        int index = 0;
        for (Iterator it = _settings.getSettingsElement().getChildren("profile", _dom.getNamespace()).iterator(); it
                .hasNext();) {
            Element profile = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            String name = Utils.getAttributeValue("name", profile);
            item.setText(name.length() > 0 ? name : defaultName);
            if (profile.equals(_profile))
                table.select(index);
            index++;
        }
    }


    public void setNewProfile() {
        _profile = new Element("profile", _dom.getNamespace());
        _newProfile = true;
    }


    public boolean selectProfile(int index) {
        try {
            _profile = (Element) _settings.getSettingsElement().getChildren("profile", _dom.getNamespace()).get(index);
            _newProfile = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getName() {
        String name = Utils.getAttributeValue("name", _profile);
        return name.length() > 0 ? name : defaultName;
    }


    public Element getProfileElement() {
        return _profile;
    }


    public void applyProfile(String name) {
        Utils.setAttribute("name", name, _profile);
        if (_newProfile)
            _settings.getSettingsElement().addContent(_profile);
        _newProfile = false;
        _dom.setChanged(true);
    }


    public boolean removeProfile(int index) {
        if (selectProfile(index)) {
            _profile.detach();
            _profile = null;
            _dom.setChanged(true);
            return true;
        } else
            return false;
    }


    public boolean isNewProfile() {
        return _newProfile;
    }
}
