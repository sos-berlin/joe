package com.sos.event.service.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import com.sos.event.service.forms.ActionsForm;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class ParameterListener {

    private ActionsForm _main = null;
    private ActionsDom _dom = null;
    private Element _parent = null;
    private List _params = null;
    private List _environments = null;
    private List _includeParams = null;
    private static HashMap parameterDescription = new HashMap();
    private static HashMap parameterRequired = new HashMap();
    private int type = JOEConstants.CONFIG;

    public ParameterListener(ActionsDom dom, Element parent, ActionsForm update, int type_) {
        _dom = dom;
        _parent = parent;
        _main = update;
        type = type_;
        Element params = _parent.getChild("params");
        if (params != null) {
            _params = params.getChildren();
            _includeParams = params.getChildren("include");
        }
        Element environment = _parent.getChild("environment");
        if (environment != null)
            _environments = environment.getChildren("variable");
    }

    private void initParams() {
        Element params = _parent.getChild("params");
        if (params != null) {
            _params = params.getChildren();
            _includeParams = params.getChildren("include");
        } else {
            _parent.addContent(0, new Element("params"));
            _params = _parent.getChild("params").getChildren();
            _includeParams = _parent.getChild("params").getChildren("include");
        }
    }

    public void fillParams(Table table) {
        if (_params != null) {
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (e.getName().equals("copy_params") && type == JOEConstants.COMMANDS) {
                        TableItem item = new TableItem(table, SWT.NONE);
                        item.setText(0, "<from>");
                        item.setText(1, ((Element) o).getAttributeValue("from"));
                    } else if (e.getName().equals("param")) {
                        if (e.getAttributeValue("name") != null) {
                            TableItem item = new TableItem(table, SWT.NONE);
                            item.setText(0, ((Element) o).getAttributeValue("name"));
                            item.setText(1, (((Element) o).getAttributeValue("value") != null ? ((Element) o).getAttributeValue("value") : ""));
                            if (parameterDescription != null) {
                                item.setData("parameter_description_de", parameterDescription.get("parameter_description_de_"
                                        + ((Element) o).getAttributeValue("name")));
                                item.setData("parameter_description_en", parameterDescription.get("parameter_description_en_"
                                        + ((Element) o).getAttributeValue("name")));
                            }
                            if (parameterRequired != null && isParameterRequired(((Element) o).getAttributeValue("name"))) {
                                item.setBackground(Options.getRequiredColor());
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillParams(ArrayList listOfParams, Table table, boolean refreshTable) {
        if (refreshTable) {
            if (_params != null) {
                _params.clear();
            }
            table.removeAll();
        }
        for (int i = 0; i < listOfParams.size(); i++) {
            HashMap h = (HashMap) listOfParams.get(i);
            if (h.get("name") != null) {
                TableItem item = existsParams(h.get("name").toString(), table, (h.get("default_value") != null ? h.get("default_value").toString()
                        : ""));
                if (!refreshTable && item != null) {
                    if (h.get("required") != null && h.get("required").equals("true"))
                        item.setBackground(Options.getRequiredColor());
                } else {
                    String pname = h.get("name").toString();
                    String pvalue = (h.get("default_value") != null ? h.get("default_value").toString() : "");
                    String desc_de = (h.get("description_de") != null ? h.get("description_de").toString() : "");
                    String desc_en = (h.get("description_en") != null ? h.get("description_en").toString() : "");
                    saveParameter(table, pname, pvalue, desc_de, desc_en, (h.get("required") != null ? h.get("required").equals("true") : false));
                }
            }
        }
    }

    public void fillEnvironment(Table table) {
        if (_environments != null) {
            Iterator it = _environments.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, ((Element) o).getAttributeValue("name"));
                    item.setText(1, (((Element) o).getAttributeValue("value") != null ? ((Element) o).getAttributeValue("value") : ""));
                }
            }
        }
    }

    public void fillIncludeParams(Table table) {
        if (_includeParams != null) {
            Iterator it = _includeParams.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    Element elem = (Element) o;
                    if (elem.getAttribute("file") != null) {
                        item.setText(0, Utils.getAttributeValue("file", elem));
                        item.setText(2, "file");
                    } else {
                        item.setText(0, Utils.getAttributeValue("live_file", elem));
                        item.setText(2, "live_file");
                    }
                    item.setText(1, (((Element) o).getAttributeValue("node") != null ? ((Element) o).getAttributeValue("node") : ""));
                }
            }
        }
    }

    public TableItem existsParams(String name, Table table, String replaceValue) {
        try {
            for (int i = 0; i < table.getItemCount(); i++) {
                if (table.getItem(i).getText(0).equals(name)) {
                    return table.getItem(i);
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            new ErrorLog("error in ParameterListener.existsParams " + e.getMessage(), e);
        }
        return null;
    }

    public void deleteParameter(Table table, int index) {
        if (_params != null) {
            _params.remove(index);
            _dom.setChanged(true);

        }
        if (_params.size() == 0) {
            _parent.removeChild("params");
        }
        table.remove(index);
    }

    public void deleteIncludeParams(Table table, int index) {
        if (_includeParams != null) {
            _includeParams.remove(index);
            _dom.setChanged(true);

        }
        table.remove(index);
    }

    public void saveParameter(Table table, String name, String value, String parameterDescription_de, String parameterDescription_en, boolean required) {
        Element e = new Element("param");
        e.setAttribute("name", name);
        e.setAttribute("value", value);

        if (_params == null) {
            initParams();
        }
        _params.add(e);
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(new String[] { name, value });
        if (parameterDescription_de != null && parameterDescription_de.trim().length() > 0) {
            item.setData("parameter_description_de", parameterDescription_de);
            parameterDescription.put("parameter_description_de_" + name, parameterDescription_de);
        }
        if (parameterDescription_en != null && parameterDescription_en.trim().length() > 0) {
            item.setData("parameter_description_en", parameterDescription_en);
            parameterDescription.put("parameter_description_en_" + name, parameterDescription_de);
        }
        if (required) {
            item.setBackground(Options.getRequiredColor());
        }
        _dom.setChanged(true);

    }

    public void saveIncludeParams(Table table, String file, String node, boolean isLive) {
        boolean found = false;
        if (_includeParams != null) {
            int index = 0;
            Iterator it = _includeParams.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if ((file.equals(e.getAttributeValue("live_file")) || file.equals(e.getAttributeValue("file")))
                            && (node.equals(e.getAttributeValue("node")) || table.getSelectionCount() > 0)) {
                        found = true;
                        e.removeAttribute("live_file");
                        e.removeAttribute("file");
                        if (isLive)
                            e.setAttribute("live_file", file);
                        else
                            e.setAttribute("file", file);
                        Utils.setAttribute("node", node, e);
                        _dom.setChanged(true);

                        table.getItem(index).setText(1, node);
                        table.getItem(index).setText(2, (isLive ? "live_file" : "file"));
                        break;
                    }
                    index++;
                }
            }
        }
        if (!found) {
            Element e = new Element("include");
            if (isLive)
                e.setAttribute("live_file", file);
            else
                e.setAttribute("file", file);
            e.setAttribute("node", node);
            _dom.setChanged(true);

            if (_includeParams == null) {
                initParams();
            }
            _includeParams.add(e);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { file, node, (isLive ? "live_file" : "file") });
        }
    }

    public void saveParameter(Table table, String name, String value) {
        boolean found = false;
        if (_params != null) {
            if (name.equals("<from>") && type == JOEConstants.COMMANDS) {
                found = (table.getSelectionIndex() > -1);
            } else {
                int index = 0;
                Iterator it = _params.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof Element) {
                        Element e = (Element) o;
                        if (e.getName().equals("param")) {
                            if (name.equals(e.getAttributeValue("name"))) {
                                found = true;
                                e.setAttribute("value", value);
                                _dom.setChanged(true);

                                table.getItem(index).setText(1, value);
                            }
                            index++;
                        }
                    }
                }
            }
            if (name.equals("<from>") && found && type == JOEConstants.COMMANDS) {
                int index = table.getSelectionIndex();
                table.getItem(index).setText(0, name);
                table.getItem(index).setText(1, value);
                Element e = (Element) _params.get(index);
                e.setName("copy_params");
                e.setAttribute("from", value);
                e.removeAttribute("name");
                e.removeAttribute("value");
                _dom.setChanged(true);
            }
        }
        if (!found) {
            Element e = new Element("param");
            if (!name.equals("<from>")) {
                e.setAttribute("name", name);
                e.setAttribute("value", value);
            } else {
                e.setName("copy_params");
                e.setAttribute("from", value);
            }
            _dom.setChanged(true);
            if (_params == null) {
                initParams();
            }
            if (_params != null) {
                _params.add(e);
            }
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }

    public ActionsDom get_dom() {
        return _dom;
    }

    public ActionsForm get_main() {
        return _main;
    }

    public Element getParent() {
        return _parent;
    }

    public void getAllParameterDescription() {
        String xmlPaths = Options.getSchedulerData();
        String include = "";
        Element desc = _parent.getChild("description");
        if (desc != null) {
            Element inc = desc.getChild("include");
            if (inc != null) {
                include = inc.getAttributeValue("file");
            }
        }
        xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat(include) : xmlPaths.concat("/").concat(include));
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(xmlPaths));
            Element root = doc.getRootElement();
            Element config = root.getChild("configuration", root.getNamespace());
            Element params = config.getChild("params", config.getNamespace());
            if (params == null) {
                return;
            }
            List listMainElements = params.getChildren("param", params.getNamespace());
            for (int i = 0; i < listMainElements.size(); i++) {
                Element elMain = (Element) (listMainElements.get(i));
                if (elMain.getName().equalsIgnoreCase("param")) {
                    List noteList = elMain.getChildren("note", elMain.getNamespace());
                    for (int k = 0; k < noteList.size(); k++) {
                        Element note = (Element) noteList.get(k);
                        String language = Utils.getAttributeValue("language", note);
                        if (note != null) {
                            List notelist = note.getChildren();
                            for (int j = 0; j < notelist.size(); j++) {
                                Element elNote = (Element) (notelist.get(j));
                                parameterDescription.put("parameter_description_" + language + "_" + elMain.getAttributeValue("name"), elNote.getText());
                                if (elMain.getAttributeValue("required") != null)
                                    parameterRequired.put(elMain.getAttributeValue("name"), elMain.getAttributeValue("required"));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), ex);
        }
    }

    public String getParameterDescription(String name) {
        return (parameterDescription.get("parameter_description_" + Options.getLanguage() + "_" + name) != null ? parameterDescription.get("parameter_description_"
                + Options.getLanguage() + "_" + name).toString()
                : "");
    }

    public String getParameterDescription(String name, String language) {
        return (parameterDescription.get("parameter_description_" + language + "_" + name) != null ? parameterDescription.get("parameter_description_"
                + language + "_" + name).toString()
                : "");
    }

    private boolean isParameterRequired(String name) {
        String _isIt = (parameterRequired.get(name) != null ? parameterRequired.get(name).toString() : "");
        if (_isIt.equals("true")) {
            return (true);
        } else {
            return false;
        }
    }

    public void changeUp(Table table) {
        int index = table.getSelectionIndex();
        if (index < 0) {// nichts ist selektiert
            return;
        }
        if (index == 0) {// ist bereits ganz oben
            return;
        }
        if (_params == null) {
            initParams();
        }
        _dom.reorderDOM();
        Element params = _parent.getChild("params");
        if (params != null) {
            _params = params.getChildren();
            _includeParams = params.getChildren("include");
        }

        Element elem = (Element) (_params.get(index));
        Object obj = elem.clone();
        _params.remove(elem);
        _params.add(index - 1, obj);
        table.removeAll();
        fillParams(table);
        table.select(index - 1);
        _dom.setChanged(true);
    }

    public void changeDown(Table table) {
        int index = table.getSelectionIndex();
        if (index < 0) {// nichts ist selektiert
            return;
        }
        if (index == table.getItemCount() - 1) {// ist bereits ganz oben
            return;
        }
        if (_params == null) {
            initParams();
        }
        Element elem = (Element) (_params.get(index));
        Object obj = elem.clone();
        _params.remove(elem);
        _params.add(index + 1, obj);
        table.removeAll();
        fillParams(table);
        table.select(index + 1);
        _dom.setChanged(true);
    }
}
