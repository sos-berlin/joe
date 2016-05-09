package com.sos.joe.jobdoc.editor.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.IUpdateTree;
import com.sos.joe.jobdoc.editor.forms.ApplicationsForm;
import com.sos.joe.jobdoc.editor.forms.AuthorsForm;
import com.sos.joe.jobdoc.editor.forms.ConnectionsForm;
import com.sos.joe.jobdoc.editor.forms.DatabaseResourcesForm;
import com.sos.joe.jobdoc.editor.forms.DatabasesForm;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.jobdoc.editor.forms.FilesForm;
import com.sos.joe.jobdoc.editor.forms.JobForm;
import com.sos.joe.jobdoc.editor.forms.JobScriptForm;
import com.sos.joe.jobdoc.editor.forms.NoteForm;
import com.sos.joe.jobdoc.editor.forms.ParamsForm;
import com.sos.joe.jobdoc.editor.forms.PayloadForm;
import com.sos.joe.jobdoc.editor.forms.ProcessForm;
import com.sos.joe.jobdoc.editor.forms.ProfilesForm;
import com.sos.joe.jobdoc.editor.forms.ReleaseForm;
import com.sos.joe.jobdoc.editor.forms.ReleasesForm;
import com.sos.joe.jobdoc.editor.forms.ResourcesForm;
import com.sos.joe.jobdoc.editor.forms.ScriptForm;
import com.sos.joe.jobdoc.editor.forms.SectionsForm;
import com.sos.joe.jobdoc.editor.forms.SettingForm;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DocumentationListener extends JobDocBaseListener<DocumentationDom> implements IUpdateTree {

    private final static Logger LOGGER = Logger.getLogger(DocumentationListener.class);
    private TreeItem _profiles;
    private TreeItem _connections;
    private static ArrayList _IDs;
    private final DocumentationForm _gui;
    public static String PREFIX_DATABSE = "Database: ";

    public DocumentationListener(DocumentationForm gui, DocumentationDom dom) {
        _gui = gui;
        _dom = dom;
    }

    public void fillTree(Tree tree) {
        tree.removeAll();
        Utils.setResetElement(_dom.getRoot());
        Element desc = _dom.getRoot();
        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText("Job");
        item.setData(new TreeData(JOEConstants.DOC_JOB, desc.getChild("job", _dom.getNamespace()), Options.getDocHelpURL("job")));
        TreeItem item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Process");
        item2.setData(new TreeData(JOEConstants.DOC_PROCESS, desc.getChild("job", _dom.getNamespace()), Options.getDocHelpURL("process")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Script");
        item2.setData(new TreeData(JOEConstants.DOC_SCRIPT, desc.getChild("job", _dom.getNamespace()), Options.getDocHelpURL("script")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Monitor");
        item2.setData(new TreeData(JOEConstants.DOC_MONITOR, desc.getChild("job", _dom.getNamespace()), Options.getDocHelpURL("monitor")));
        item.setExpanded(true);
        item = new TreeItem(tree, SWT.NONE);
        item.setText("Releases");
        item.setData(new TreeData(JOEConstants.DOC_RELEASES, desc.getChild("releases", _dom.getNamespace()), Options.getDocHelpURL("releases")));
        treeFillReleases(item, desc.getChild("releases", _dom.getNamespace()));
        item.setExpanded(true);
        item = new TreeItem(tree, SWT.NONE);
        item.setText("Resources");
        item.setData(new TreeData(JOEConstants.DOC_RESOURCES, desc, Options.getDocHelpURL("resources")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Files");
        item2.setData(new TreeData(JOEConstants.DOC_FILES, desc, Options.getDocHelpURL("files")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Databases");
        item2.setData(new TreeData(JOEConstants.DOC_DATABASES, desc, Options.getDocHelpURL("databases")));
        item.setExpanded(true);
        treeFillDatabaseResources(item2, desc.getChild("resources", _dom.getNamespace()));
        item = new TreeItem(tree, SWT.NONE);
        item.setText("Configuration");
        item.setData(new TreeData(JOEConstants.DOC_CONFIGURATION, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("configuration")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Parameters");
        item2.setData(new TreeData(JOEConstants.DOC_PARAMS, desc.getChild("configuration", _dom.getNamespace()), Options.getDocHelpURL("parameters")));
        item.setExpanded(true);
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Payload");
        item2.setData(new TreeData(JOEConstants.DOC_PAYLOAD, desc.getChild("configuration", _dom.getNamespace()), Options.getDocHelpURL("payload")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Settings");
        item2.setData(new TreeData(JOEConstants.DOC_SETTINGS, desc.getChild("configuration", _dom.getNamespace()), Options.getDocHelpURL("settings")));
        _profiles = new TreeItem(item2, SWT.NONE);
        item2.setExpanded(true);
        _profiles.setText("Profiles");
        _profiles.setData(new TreeData(JOEConstants.DOC_PROFILES, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("profiles")));
        fillProfiles();
        _connections = new TreeItem(item2, SWT.NONE);
        _connections.setText("Connections");
        _connections.setData(new TreeData(JOEConstants.DOC_CONNECTIONS, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("connections")));
        fillConnections();
        item = new TreeItem(tree, SWT.NONE);
        item.setText("Documentation");
        item.setData(new TreeData(JOEConstants.DOC_DOCUMENTATION, desc, Options.getDocHelpURL("documentation")));
    }

    private Element getSettings() {
        Element configuration = _dom.getRoot().getChild("configuration", _dom.getNamespace());
        return configuration.getChild("settings", _dom.getNamespace());
    }

    @Override
    public void fillProfiles() {
        Element settings = getSettings();
        _profiles.removeAll();
        if (settings != null) {
            for (Iterator it = settings.getChildren("profile", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element element = (Element) it.next();
                TreeItem item = new TreeItem(_profiles, SWT.NONE);
                String name = Utils.getAttributeValue("name", element);
                item.setText("Sections [Profile: " + (name != null ? name : ProfilesListener.defaultName) + "]");
                item.setData(new TreeData(JOEConstants.DOC_SECTIONS, element, Options.getDocHelpURL("sections")));
                _profiles.setExpanded(true);
                fillSections(item, element, false);
            }
        }
    }

    @Override
    public void fillConnections() {
        Element settings = getSettings();
        _connections.removeAll();
        if (settings != null) {
            for (Iterator it = settings.getChildren("connection", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element element = (Element) it.next();
                TreeItem item = new TreeItem(_connections, SWT.NONE);
                String name = Utils.getAttributeValue("name", element);
                item.setText("Applications [Connection: " + (name != null ? name : ConnectionsListener.defaultName) + "]");
                item.setData(new TreeData(JOEConstants.DOC_APPLICATIONS, element, Options.getDocHelpURL("applications")));
                _connections.setExpanded(true);
                fillApplications(item, element, false);
            }
        }
    }

    @Override
    public void fillApplications(TreeItem parent, Element element, boolean expand) {
        parent.removeAll();
        for (Iterator it = element.getChildren("application", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element section = (Element) it.next();
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText("Sections [Appl.: " + Utils.getAttributeValue("name", section) + "]");
            item.setData(new TreeData(JOEConstants.DOC_SECTIONS, section, Options.getDocHelpURL("sections")));
            parent.setExpanded(expand);
            fillSections(item, section, false);
        }
    }

    @Override
    public void fillSections(TreeItem parent, Element element, boolean expand) {
        parent.removeAll();
        for (Iterator it = element.getChildren("section", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element section = (Element) it.next();
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText("Settings [Section: " + Utils.getAttributeValue("name", section) + "]");
            item.setData(new TreeData(JOEConstants.DOC_SECTION_SETTINGS, section, Options.getDocHelpURL("setting")));
            parent.setExpanded(expand);
        }
    }

    public boolean treeSelection(Tree tree, Composite c) {
        try {
            if (tree.getSelectionCount() > 0) {
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i])) {
                        return false;
                    }
                    children[i].dispose();
                }
                TreeItem item = tree.getSelection()[0];
                TreeData data = (TreeData) item.getData();
                _dom.setInit(true);
                switch (data.getType()) {
                case JOEConstants.DOC_JOB:
                    new JobForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_PROCESS:
                    new ProcessForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_SCRIPT:
                    new JobScriptForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_MONITOR:
                    ScriptForm form = new ScriptForm(c, SWT.NONE);
                    form.setTitle("Monitor");
                    form.setParams(_dom, data.getElement(), JOEConstants.DOC_MONITOR);
                    form.init(true, true);
                    break;
                case JOEConstants.DOC_RELEASES:
                    new ReleasesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.DOC_RELEASE:
                    new ReleaseForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_RELEASE_AUTHOR:
                    new AuthorsForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_RESOURCES:
                    new ResourcesForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_DATABASES:
                    new DatabasesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.DOC_DATABASES_RESOURCE:
                    new DatabaseResourcesForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_FILES:
                    new FilesForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_DOCUMENTATION:
                    NoteForm doc = new NoteForm(c, SWT.NONE, JOEConstants.DOC_DOCUMENTATION);
                    doc.setTitle("Note");
                    doc.setParams(_dom, data.getElement(), "documentation", false);
                    break;
                case JOEConstants.DOC_CONFIGURATION:
                    NoteForm note = new NoteForm(c, SWT.NONE, JOEConstants.DOC_CONFIGURATION);
                    note.setTitle("Configuration Note");
                    note.setParams(_dom, data.getElement(), "note", true);
                    break;
                case JOEConstants.DOC_PARAMS:
                    new ParamsForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_PAYLOAD:
                    new PayloadForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_SETTINGS:
                    SettingsListener listener = new SettingsListener(_dom, data.getElement());
                    listener.setSettings();
                    NoteForm settings = new NoteForm(c, SWT.NONE, JOEConstants.DOC_SETTINGS);
                    settings.setSettingsListener(listener);
                    settings.setTitle("Settings Note");
                    settings.setParams(_dom, listener.getSettingsElement(), "note", true);
                    break;
                case JOEConstants.DOC_PROFILES:
                    new ProfilesForm(c, SWT.NONE, _dom, data.getElement(), this);
                    break;
                case JOEConstants.DOC_SECTIONS:
                    new SectionsForm(c, SWT.NONE, _dom, data.getElement(), this, item);
                    break;
                case JOEConstants.DOC_SECTION_SETTINGS:
                    new SettingForm(c, SWT.NONE, _dom, data.getElement());
                    break;
                case JOEConstants.DOC_CONNECTIONS:
                    new ConnectionsForm(c, SWT.NONE, _dom, data.getElement(), this);
                    break;
                case JOEConstants.DOC_APPLICATIONS:
                    new ApplicationsForm(c, SWT.NONE, _dom, data.getElement(), this, item);
                    break;
                default:
                    LOGGER.info("no form found for " + item.getText());
                }
                c.layout();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ErrorLog.message(e.getMessage(), SWT.ICON_ERROR);
        }
        _dom.setInit(false);
        return true;
    }

    public static String[] getIDs(DocumentationDom dom, String ownID) {
        ArrayList ids = new ArrayList();
        _IDs = getIDs(ids, dom.getRoot(), ownID);
        return (String[]) _IDs.toArray(new String[] {});
    }

    private static ArrayList getIDs(ArrayList list, Element element, String ownID) {
        for (Iterator it = element.getChildren().iterator(); it.hasNext();) {
            Element e = (Element) it.next();
            if (e.getNamespace().equals(element.getNamespace())) {
                String id = e.getAttributeValue("id");
                if (id != null && !id.equals(ownID)) {
                    list.add(e.getName() + ": " + id);
                }
                getIDs(list, e, ownID);
            }
        }
        return list;
    }

    public static String getID(String name) {
        int index = name.indexOf(':');
        if (index < 0) {
            return name;
        } else {
            return name.substring(index + 2);
        }
    }

    public static String getIDName(String id) {
        if ("".equals(id)) {
            return id;
        }
        for (Iterator it = _IDs.iterator(); it.hasNext();) {
            String name = (String) it.next();
            if (getID(name).equals(id)) {
                return name;
            }
        }
        return "unknown: " + id;
    }

    public static void setCheckbox(Combo c, String[] values, String value) {
        c.setItems(values);
        c.setText(getIDName(value));
    }

    public void treeFillReleases(TreeItem parent, Element elem) {
        parent.removeAll();
        java.util.List listOfRelease = elem.getChildren("release", elem.getNamespace());
        for (int i = 0; i < listOfRelease.size(); i++) {
            Element release = (Element) listOfRelease.get(i);
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText("Release: " + Utils.getAttributeValue("id", release));
            item.setData(new TreeData(JOEConstants.DOC_RELEASE, release, Options.getDocHelpURL("releases")));
            TreeItem itemAuthor = new TreeItem(item, SWT.NONE);
            itemAuthor.setText("Author");
            itemAuthor.setData(new TreeData(JOEConstants.DOC_RELEASE_AUTHOR, release, Options.getDocHelpURL("releases")));
            item.setExpanded(true);
            itemAuthor.setExpanded(true);
        }
    }

    public void treeFillDatabaseResources(TreeItem parent, Element resources) {
        if (resources == null) {
            return;
        }
        parent.removeAll();
        java.util.List list = resources.getChildren("database", resources.getNamespace());
        for (int i = 0; i < list.size(); i++) {
            Element database = (Element) list.get(i);
            TreeItem itemDatabase = new TreeItem(parent, SWT.NONE);
            itemDatabase.setText(PREFIX_DATABSE + Utils.getAttributeValue("name", database));
            itemDatabase.setData(new TreeData(JOEConstants.DOC_DATABASES_RESOURCE, database, Options.getDocHelpURL("databases")));
            parent.setExpanded(true);
            itemDatabase.setExpanded(true);
        }
    }

}