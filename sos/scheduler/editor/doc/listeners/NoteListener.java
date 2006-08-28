package sos.scheduler.editor.doc.listeners;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;

public class NoteListener {
    private DocumentationDom _dom;

    private Element          _parent;

    private String           _name;

    private boolean          _optional;

    private String[]         _languages  = { "de", "en" };

    private String           _lang       = null;

    private boolean          _setChanged = true;


    public NoteListener(DocumentationDom dom, Element parent, String name, boolean optional, boolean changeStatus) {
        _dom = dom;
        _parent = parent;
        _name = name;
        _optional = optional;
        _setChanged = changeStatus;
        init();
    }


    private void init() {
        if (_lang == null) {
            if (_parent != null) {
                List list = _parent.getChildren(_name, _dom.getNamespace());
                if (list.size() > 0) {
                    Element item = (Element) list.get(0);
                    String lang = item.getAttributeValue("language");
                    _lang = lang == null ? _languages[0] : lang;
                } else
                    _lang = _languages[0];
            }
        }
    }


    public String[] getLanguages() {
        return _languages;
    }


    public void setLang(String lang) {
        _lang = lang;
    }


    public String getLang() {
        return _lang;
    }


    public void setNote(String note) {
        Element item = getElement();
        if (item == null && note.length() > 0) {
            // create new one
        	  
         	  if (_parent.getParent()==null) {
        	    Element r = _dom.getRoot();
        	    Element c = r.getChild("configuration",_dom.getNamespace());
        	    c.addContent(_parent);
        	  }
        	   
            item = new Element(_name, _dom.getNamespace());
            Utils.setAttribute("language", _lang, item);
            _parent.addContent(item);
        } else if (item != null && note.length() == 0) {
            // empty - remove it
            item.detach();
            if (_setChanged)
                _dom.setChanged(true);
            return;
        } else if (item == null)
            return;

         try {
             note = "<div xmlns=\"http://www.w3.org/1999/xhtml\">\n" + note + "\n</div>";
             item.setContent(_dom.noteAsDom(note));
          } catch (Exception e) {
            e.printStackTrace();
          }


        if (_setChanged)
            _dom.setChanged(true);
    }


    public String getNote() {
        Element item = getElement();
        if (item == null)
            return "";

        try {
            return _dom.noteAsStr(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private Element getElement() {
        if (_parent != null) {
            List list = _parent.getChildren(_name, _dom.getNamespace());
            for (Iterator it = list.iterator(); it.hasNext();) {
                Element item = (Element) it.next();
                if (Utils.getAttributeValue("language", item).equals(_lang))
                    return item;
            }
        }
        return null;
    }


    public void createDefault() {
        if (_parent != null && !_optional) {
            if (_parent.getChildren(_name, _dom.getNamespace()).size() == 0) {
                Element item = new Element(_name, _dom.getNamespace());
                item.setAttribute("language", _lang);
                _parent.addContent(item);
            }
        }
    }

}
